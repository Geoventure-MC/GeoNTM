package fr.geoventure.geontm.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.geoventure.geontm.capability.GeoNtmLivingProps;
import fr.geoventure.geontm.client.particle.AmatFlashParticles;
import fr.geoventure.geontm.config.CustomMachineConfigJSON;
import fr.geoventure.geontm.handler.ToolKeybinds;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.inventory.recipes.CustomMachineRecipes;
import fr.geoventure.geontm.items.LegacyMeta;
import fr.geoventure.geontm.items.ModDataComponents;
import fr.geoventure.geontm.items.ModSingularityItems;
import fr.geoventure.geontm.items.ModUtilityToolItems;
import fr.geoventure.geontm.items.ModelState;
import fr.geoventure.geontm.items.special.ItemCell;
import fr.geoventure.geontm.items.weapon.ItemCrucible;
import fr.geoventure.geocore.client.KeyMappings;
import fr.geoventure.geocore.data.Tags;
import fr.geoventure.geocore.harness.HarnessQueryRegistry;
import fr.geoventure.geocore.platform.ConfigFiles;
import fr.geoventure.geocore.world.ItemStacks;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * The {@code geontm_gapsa} harness query, read by {@code FinalGapsA}: the server player's
 * held stack (id, variant, crucible charge, whether its modifiers are suppressed), attack damage and health, every
 * cell in the inventory with its model variant; the client player's hurt time/duration (the knife's jolt) and the
 * live antimatter flashes. {@code {"give":"oldcell:<FLUID>"}} hands the player a cell saved in the old format
 * (model variant = fluid id), which no /give can spell identically on every node. Inert unless the harness runs.
 */
public final class GapsAHarnessQueries {

    private GapsAHarnessQueries() {}

    public static void register() {
        HarnessQueryRegistry.register("geontm_gapsa", GapsAHarnessQueries::query);
    }

    private static JsonObject query(Minecraft mc, JsonObject params) {
        JsonObject json = new JsonObject();
        MinecraftServer server = mc.getSingleplayerServer();
        if (mc.player == null || server == null) return json;
        json.addProperty("hurt_time", mc.player.hurtTime);
        json.addProperty("hurt_duration", mc.player.hurtDuration);
        json.addProperty("amat_flashes", AmatFlashParticles.active());
        json.addProperty("client_alt_down", altDown(mc));
        CompletableFuture<JsonObject> result = new CompletableFuture<>();
        server.execute(() -> {
            try {
                ServerPlayer player = server.getPlayerList().getPlayer(mc.player.getUUID());
                result.complete(player == null ? new JsonObject() : onServer(player, params));
            } catch (Throwable t) {
                JsonObject error = new JsonObject();
                error.addProperty("error", t.toString());
                result.complete(error);
            }
        });
        try {
            JsonObject server1 = result.get(5, TimeUnit.SECONDS);
            for (var e : server1.entrySet()) json.add(e.getKey(), e.getValue());
        } catch (Exception e) {
            json.addProperty("error", e.toString());
        }
        return json;
    }

    private static boolean altDown(Minecraft mc) {
        for (var mapping : mc.options.keyMappings) {
            // the state the copy tool itself reads (UtilityToolClientRegistrations.tick): key.geontm.abilityAlt is on
            // left Alt too (as in 1.7), and Fabric below 26.1 dispatches a key to one mapping only, so isDown()
            // of the losing mapping stays false while the key is held; Core's isHeld() reads the winner as well
            if (mapping.getName().equals("key.geontm.copyToolAlt")) {
                return mapping instanceof KeyMappings.Mapping held ? held.isHeld() : mapping.isDown();
            }
        }
        return false;
    }

    /**
     * {@code {"custom_machines":"stage","machines":[…],"recipes":{…}}} loads the real {@code geontmCustomMachines.json}
     * plus the scenario's test machines, and the scenario's custom machine recipe file, from a staging folder beside
     * the config ({@code config/geontmHarnessStaging}); {@code "restore"} reloads the real files. The real config files
     * are never written, so a scenario that dies half-way leaves nothing behind but the staging folder. Machines
     * are read once at startup in 1.7; the running game shares one static list between the integrated server and
     * the client, so the reload is seen by both (and by JEI on the next world join).
     */
    private static int customMachines(JsonObject params) {
        String mode = params.get("custom_machines").getAsString();
        Path real = CustomMachineConfigJSON.configFile();
        if (mode.equals("restore")) {
            CustomMachineConfigJSON.reloadFrom(real);
            CustomMachineRecipes.reload();
            return CustomMachineConfigJSON.niceList.size();
        }
        try {
            Path dir = ConfigFiles.configDir().resolve("geontmHarnessStaging");
            Path recipes = dir.resolve("geontmRecipes");
            Files.createDirectories(recipes);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject machines;
            try (Reader reader = Files.newBufferedReader(real, StandardCharsets.UTF_8)) {
                machines = gson.fromJson(reader, JsonObject.class);
            }
            for (var machine : params.getAsJsonArray("machines")) machines.getAsJsonArray("machines").add(machine);
            Path staged = dir.resolve("geontmCustomMachines.json");
            Files.writeString(staged, gson.toJson(machines), StandardCharsets.UTF_8);
            Files.writeString(recipes.resolve(CustomMachineRecipes.getFileName()),
                    gson.toJson(params.getAsJsonObject("recipes")), StandardCharsets.UTF_8);
            CustomMachineConfigJSON.reloadFrom(staged);
            CustomMachineRecipes.loadFrom(recipes);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return CustomMachineConfigJSON.niceList.size();
    }

    private static JsonObject onServer(ServerPlayer player, JsonObject params) {
        JsonObject json = new JsonObject();
        if (params.has("clearrads")) {
            // between scenario sections: the radioactive test items (cells, crucible) are lethal over a long run
            GeoNtmLivingProps.setRadiation(player, 0);
            GeoNtmLivingProps.setNeutron(player, 0);
            json.addProperty("cleared", true);
            return json;
        }
        if (params.has("custom_machines")) {
            json.addProperty("custom_machines", customMachines(params));
            return json;
        }
        if (params.has("give")) {
            String[] give = params.get("give").getAsString().split(":", 2);
            if (give[0].equals("oldcell")) {
                var fluid = Fluids.fromName(give[1]);
                ItemStack cell = new ItemStack(ModSingularityItems.cell.get());
                ModDataComponents.fluid(cell, fluid);
                ModDataComponents.fluidAmount(cell, ItemCell.CAPACITY);
                // no stored sprite number: an "old" cell is any cell of the fluid
                json.addProperty("given", player.getInventory().add(cell));
                player.inventoryMenu.broadcastChanges();
            }
            return json;
        }
        ItemStack held = player.getMainHandItem();
        json.addProperty("held", held.isEmpty() ? "" : BuiltInRegistries.ITEM.getKey(held.getItem()).toString());
        // the 1.7 metadata the held item stands for, or its drawn state (both computed, never stored)
        json.addProperty("held_variant", held.getItem() instanceof LegacyMeta meta ? meta.legacyMeta()
                : ModelState.of(held));
        json.addProperty("crucible_charges", held.getItem() instanceof ItemCrucible ? ItemCrucible.getCharges(held) : -1);
        json.addProperty("modifiers_suppressed", ItemStacks.attributeModifiersSuppressed(held));
        json.addProperty("attack_damage", player.getAttributeValue(Attributes.ATTACK_DAMAGE));
        json.addProperty("tool_alt", ToolKeybinds.getKeyPressed(player,
                ToolKeybinds.EnumToolKey.TOOL_ALT));
        json.addProperty("tool_ctrl", ToolKeybinds.getKeyPressed(player,
                ToolKeybinds.EnumToolKey.TOOL_CTRL));
        // the settings tool's paste selection, read in the same server step as tool_alt: FinalGapsA polls both
        // while Alt is held, so it can release the key before 1.7's 5-tick repeat cycles the selection again
        CompoundTag settings = ModUtilityToolItems.SETTINGS.getOr(held, null);
        json.addProperty("settings_copy_index", settings == null ? -1
                : Tags.getIntOr(settings, "copyIndex", 0));
        json.addProperty("health", player.getHealth());
        json.addProperty("max_health", player.getMaxHealth());
        JsonArray cells = new JsonArray();
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!(stack.getItem() instanceof ItemCell)) continue;
            JsonObject c = new JsonObject();
            c.addProperty("slot", i);
            c.addProperty("count", stack.getCount());
            c.addProperty("fluid", ModDataComponents.fluid(stack).getRegistryName());
            c.addProperty("variant", ModelState.of(stack));
            c.addProperty("name", stack.getHoverName().getString());
            cells.add(c);
        }
        json.add("cells", cells);
        return json;
    }
}
