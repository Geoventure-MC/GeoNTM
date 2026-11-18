package fr.geoventure.geontm.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.geoventure.geontm.client.anim.GeoNtmAnimations;
import fr.geoventure.geontm.client.particle.ParticleSmokePlume;
import fr.geoventure.geontm.client.particle.SparkParticles;
import fr.geoventure.geontm.client.particle.TextParticles;
import fr.geoventure.geontm.client.render.RenderOverhead;
import fr.geoventure.geontm.client.render.armor.ArmorRenderers;
import fr.geoventure.geontm.client.render.entity.EntityItemRenderers;
import fr.geoventure.geontm.extprop.GeoNtmPlayerProps;
import fr.geoventure.geontm.handler.ArmorModHandler;
import fr.geoventure.geontm.items.ModArmorItems;
import fr.geoventure.geontm.items.ModArmorModItems;
import fr.geoventure.geontm.saveddata.SatelliteSavedData;
import fr.geoventure.geontm.api.block.IRadarCommandReceiver;
import fr.geoventure.geontm.tileentity.network.RTTYSystem;
import fr.geoventure.geontm.items.ModMissileItems;
import fr.geoventure.geontm.items.ModDataComponents;
import fr.geoventure.geontm.saveddata.satellites.SatelliteMiner;
import fr.geoventure.geontm.saveddata.satellites.SatelliteScanner;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.client.EquipProgress;
import fr.geoventure.geocore.client.VanillaHud;
import fr.geoventure.geocore.harness.HarnessQueryRegistry;
import fr.geoventure.geocore.render.ConnectedTextures;
import net.minecraft.core.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * The {@code geontm_gapsc} harness query of gap batch C, read by {@code FinalGapsC}. Client: the held
 * slot's animation buses ({@code SWING_ROT}, {@code SWING_TRANS}, {@code GUARD_ROT}, {@code RECOIL}), the vanilla swing
 * flag, the hand's equip progress, live sparks/texts/markers, launch-smoke count, connected-texture registrations,
 * framed-item routing, the client's synced shield/magnet, and the HUD suppression flags. Server (params):
 * {@code {"shield":[cur,max]}} sets the server props; {@code {"lens":freq}} puts a depth scanner on {@code freq} and
 * equips a T-51 helmet carrying a neutrino lens tuned to it; {@code {"framed":[ids]}} asks the routing for each id.
 * Inert unless the harness runs.
 */
public final class GapsCHarnessQueries {

    private GapsCHarnessQueries() {}

    public static void register() {
        HarnessQueryRegistry.register("geontm_gapsc", GapsCHarnessQueries::query);
    }

    private static JsonObject query(Minecraft mc, JsonObject params) {
        JsonObject json = new JsonObject();
        MinecraftServer server = mc.getSingleplayerServer();
        if (mc.player == null || server == null) return json;

        json.add("swing_rot", array(GeoNtmAnimations.getRelevantTransformation("SWING_ROT")));
        json.add("swing_trans", array(GeoNtmAnimations.getRelevantTransformation("SWING_TRANS")));
        json.add("guard_rot", array(GeoNtmAnimations.getRelevantTransformation("GUARD_ROT")));
        json.add("recoil", array(GeoNtmAnimations.getRelevantTransformation("RECOIL")));
        json.addProperty("anim_running", GeoNtmAnimations.getRelevantAnim(0) != null);
        json.addProperty("swinging", mc.player.swinging);
        json.addProperty("equip", EquipProgress.mainHand());
        json.addProperty("equip_prev", EquipProgress.previousMainHand());
        json.addProperty("sparks", SparkParticles.active());
        json.addProperty("sparks_spawned", SparkParticles.spawned);
        JsonArray texts = new JsonArray();
        for (String text : TextParticles.active()) texts.add(text);
        json.add("texts", texts);
        json.addProperty("markers", RenderOverhead.count());
        JsonArray labels = new JsonArray();
        for (String label : RenderOverhead.labels()) labels.add(label);
        json.add("marker_labels", labels);
        json.addProperty("launch_smoke", ParticleSmokePlume.spawned);
        json.addProperty("launch_smoke_ticks", ParticleSmokePlume.ticked);
        json.addProperty("launch_smoke_drift", ParticleSmokePlume.maxDrift);
        json.addProperty("ct_registered", ConnectedTextures.registered());
        json.addProperty("ct_wrapped", ConnectedTextures.wrapped());
        json.addProperty("framed_routes", EntityItemRenderers.count());
        if (params.has("framed")) {
            JsonObject framed = new JsonObject();
            for (var id : params.getAsJsonArray("framed")) {
                var item = BuiltInRegistries.ITEM.getOptional(Ids.parse(id.getAsString()));
                framed.addProperty(id.getAsString(), item.isPresent() && EntityItemRenderers.handles(new ItemStack(item.get())));
            }
            json.add("framed", framed);
        }
        var props = GeoNtmPlayerProps.getData(mc.player);
        json.addProperty("client_shield", props.shield);
        json.addProperty("client_max_shield", props.maxShield);
        json.addProperty("client_magnet", props.enableMagnet);
        json.addProperty("hide_health", VanillaHud.hideHealth());
        json.addProperty("hide_armor", VanillaHud.hideArmor());
        json.addProperty("hud_suppressible", VanillaHud.supported());
        json.addProperty("rpa_renderer", ArmorRenderers.get("rpa") != null);
        json.addProperty("ncrpa_renderer", ArmorRenderers.get("ncrpa") != null);

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
            JsonObject fromServer = result.get(5, TimeUnit.SECONDS);
            for (var e : fromServer.entrySet()) json.add(e.getKey(), e.getValue());
        } catch (Exception e) {
            json.addProperty("error", e.toString());
        }
        return json;
    }

    private static JsonObject onServer(ServerPlayer player, JsonObject params) {
        JsonObject json = new JsonObject();
        var props = GeoNtmPlayerProps.getData(player);
        if (params.has("shield")) {
            props.shield = params.getAsJsonArray("shield").get(0).getAsFloat();
            props.maxShield = params.getAsJsonArray("shield").get(1).getAsFloat();
        }
        if (params.has("lens")) {
            int freq = params.get("lens").getAsInt();
            SatelliteSavedData.get((ServerLevel) player.level()).put(freq, new SatelliteScanner());
            ItemStack lens = new ItemStack(ModArmorModItems.neutrino_lens.get());
            ModArmorModItems.neutrino_lens.get().setFreq(lens, freq);
            ItemStack helmet = new ItemStack(ModArmorItems.t51_helmet.get());
            ArmorModHandler.applyMod(helmet, lens);
            player.setItemSlot(EquipmentSlot.HEAD, helmet);
            json.addProperty("lens_applied", ArmorModHandler.pryMods(helmet)[ArmorModHandler.extra] != null);
        }
        if (params.has("command")) {
            var c = params.getAsJsonArray("command");
            var be = player.level().getBlockEntity(new BlockPos(c.get(0).getAsInt(), c.get(1).getAsInt(), c.get(2).getAsInt()));
            json.addProperty("command_accepted", be instanceof IRadarCommandReceiver rec
                    && rec.sendCommandPosition(c.get(3).getAsInt(), c.get(4).getAsInt(), c.get(5).getAsInt()));
        }
        if (params.has("designate")) {
            var xz = params.getAsJsonArray("designate");
            ItemStack designator = new ItemStack(ModMissileItems.designator_manual.get());
            ModDataComponents.DESIGNATOR_X.set(designator, xz.get(0).getAsInt());
            ModDataComponents.DESIGNATOR_Z.set(designator, xz.get(1).getAsInt());
            json.addProperty("designated", player.getInventory().add(designator));
        }
        if (params.has("rtty")) {
            var msg = params.getAsJsonArray("rtty");
            String text = msg.get(1).getAsString();
            RTTYSystem.broadcast(player.level(), msg.get(0).getAsString(), msg.size() > 2 ? (Object) text.charAt(0) : text);
            json.addProperty("rtty_sent", true);
        }
        if (params.has("miner")) {
            var sat = SatelliteSavedData.get((ServerLevel) player.level()).getSatFromFreq(params.get("miner").getAsInt());
            if (sat instanceof SatelliteMiner miner) miner.progress = 1D - SatelliteMiner.SPEED / 2;
            json.addProperty("miner_primed", sat instanceof SatelliteMiner);
        }
        json.addProperty("server_shield", props.shield);
        json.addProperty("server_max_shield", props.maxShield);
        json.addProperty("server_magnet", props.enableMagnet);
        json.addProperty("held", player.getMainHandItem().isEmpty() ? ""
                : BuiltInRegistries.ITEM.getKey(player.getMainHandItem().getItem()).toString());
        return json;
    }

    private static JsonArray array(double[] values) {
        JsonArray out = new JsonArray();
        for (int i = 0; i < 6; i++) out.add(values[i]);
        return out;
    }
}
