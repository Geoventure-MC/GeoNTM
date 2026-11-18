package fr.geoventure.geontm.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.geoventure.geontm.client.gun.GluonGunClient;
import fr.geoventure.geontm.client.gun.JadeShotgunClient;
import fr.geoventure.geontm.client.particle.GluonParticles;
import fr.geoventure.geontm.client.render.item.ItemRenderJShotgun;
import fr.geoventure.geontm.handler.GeoNtmKeybinds.EnumKeybind;
import fr.geoventure.geontm.items.weapon.sedna.ItemGunBaseNT;
import fr.geoventure.geontm.items.weapon.sedna.factory.XFactory10ga;
import fr.geoventure.geontm.items.weapon.sedna.impl.ItemGunEgon;
import fr.geoventure.geocore.harness.HarnessQueryRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

/**
 * The {@code geontm_reloaded_guns} harness query: client-side counters of the gluon gun and the jade shotgun.
 * {@code pin_flash} holds the shotgun's muzzle flash on. Inert unless the harness listener runs.
 */
public final class ReloadedGunHarnessQueries {

    private ReloadedGunHarnessQueries() {}

    public static void register() {
        HarnessQueryRegistry.register("geontm_reloaded_guns", ReloadedGunHarnessQueries::query);
        HarnessQueryRegistry.register("geontm_lamp_lights", (mc, params) -> LampLights.query(params));
    }

    private static JsonObject query(Minecraft mc, JsonObject params) {
        JsonObject json = new JsonObject();
        ItemStack held = mc.player == null ? ItemStack.EMPTY : mc.player.getMainHandItem();
        String mode = params.has("mode") ? params.get("mode").getAsString() : "gluon";
        if (mode.equals("gluon")) {
            json.addProperty("active_ticks", GluonGunClient.activeTicks);
            json.addProperty("beams_drawn", GluonGunClient.beamsDrawn);
            json.addProperty("muzzle", GluonGunClient.MUZZLE.size());
            JsonArray live = new JsonArray();
            for (int n : GluonParticles.live()) live.add(n);
            json.add("live", live);
            json.addProperty("flares_spawned", GluonParticles.flaresSpawned);
            json.addProperty("sparks_spawned", GluonParticles.sparksSpawned);
            json.addProperty("trail_nodes", GluonParticles.trailNodesAdded);
            json.addProperty("cubes_spawned", GluonParticles.cubesSpawned);
            json.addProperty("hits_shown", GluonParticles.hitsShown);
            json.addProperty("firing_flag", held.getItem() instanceof ItemGunEgon && ItemGunEgon.getIsFiring(held));
            json.addProperty("trigger_held", GeoNtmKeybindsClient.isHeld(EnumKeybind.GUN_PRIMARY));
            json.addProperty("local_firing", mc.player != null && GluonGunClient.localFiring(mc.player));
            if (params.has("server")) {
                JsonObject server = ServerQueries.answer(mc, "geontm_reloaded_guns", params.deepCopy(), (level, p) -> {
                    JsonObject out = new JsonObject();
                    var players = level.getServer().getPlayerList().getPlayers();
                    if (players.isEmpty()) return out;
                    ItemStack stack = players.get(0).getMainHandItem();
                    out.addProperty("item", stack.getItem().toString());
                    out.addProperty("primary", ItemGunBaseNT.getPrimary(stack, 0));
                    out.addProperty("firing", ItemGunEgon.getIsFiring(stack));
                    out.addProperty("state", ItemGunBaseNT.getState(stack, 0).name());
                    return out;
                });
                json.add("server", server);
            }
        } else {
            if (params.has("pin_flash")) ItemRenderJShotgun.pinnedFlash = params.get("pin_flash").getAsBoolean();
            json.addProperty("shots_seen", JadeShotgunClient.shotsSeen);
            json.addProperty("smokes", JadeShotgunClient.smokes());
            json.addProperty("smoke_draws", JadeShotgunClient.smokeDraws);
            json.addProperty("loaded", JadeShotgunClient.loaded(held));
            json.addProperty("flashlight", held.getItem() instanceof ItemGunBaseNT && XFactory10ga.getFlashlightActive(held));
        }
        return json;
    }
}
