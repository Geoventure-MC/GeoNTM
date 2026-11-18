package fr.geoventure.geontm.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.geoventure.geontm.client.particle.DisintegrationParticles;
import fr.geoventure.geontm.client.particle.ParticleCoolingTower;
import fr.geoventure.geontm.client.particle.RBMKFlameParticles;
import fr.geoventure.geocore.harness.HarnessQueryRegistry;
import fr.geoventure.geocore.render.Particles;
import fr.geoventure.geocore.world.SubLevels;
import net.minecraft.world.phys.Vec3;

/**
 * The {@code geontm_effects} harness query: live effects that draw outside the particle engine, by count, and each live
 * cooling-tower puff's position and age; {@code geontm_sublevel}: a world point's position in each sub-level grid it lies in.
 */
public final class MachineEffectHarnessQueries {

    private MachineEffectHarnessQueries() {}

    public static void register() {
        HarnessQueryRegistry.register("geontm_effects", (mc, params) -> {
            JsonObject json = new JsonObject();
            json.addProperty("flames", RBMKFlameParticles.active());
            json.addProperty("giblets", DisintegrationParticles.gibletCount());
            JsonArray towers = new JsonArray();
            Particles.forEach(mc, particle -> {
                if (!(particle instanceof ParticleCoolingTower tower)) return;
                Vec3 at = Particles.position(tower);
                JsonObject puff = new JsonObject();
                puff.addProperty("x", at.x);
                puff.addProperty("y", at.y);
                puff.addProperty("z", at.z);
                puff.addProperty("age", tower.ageTicks());
                towers.add(puff);
            });
            json.add("towers", towers);
            return json;
        });
        HarnessQueryRegistry.register("geontm_sublevel", (mc, params) -> {
            Vec3 world = new Vec3(params.get("x").getAsDouble(), params.get("y").getAsDouble(), params.get("z").getAsDouble());
            JsonArray grids = new JsonArray();
            for (SubLevels.Local local : SubLevels.intoSubLevels(mc.level, world, 0.5)) {
                JsonObject grid = new JsonObject();
                grid.addProperty("x", local.point().x);
                grid.addProperty("y", local.point().y);
                grid.addProperty("z", local.point().z);
                grids.add(grid);
            }
            JsonObject json = new JsonObject();
            json.add("grids", grids);
            return json;
        });
    }
}
