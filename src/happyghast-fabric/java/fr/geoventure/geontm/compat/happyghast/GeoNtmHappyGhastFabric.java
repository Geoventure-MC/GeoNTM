package fr.geoventure.geontm.compat.happyghast;

import fr.geoventure.geocore.platform.ModEntry;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.InteractionResult;

/**
 * Fabric's entry to the happy-ghast Fat Man ({@link GeoNtmHappyGhast}): a {@code geocore:entry} the build puts
 * <b>before</b> HBM's {@code FabricInit} in {@code fabric.mod.json}, only on nodes that set {@code feature.happy_ghast}.
 * GeoCore fills the registries once, after every entry's declarations, so the entity type and the GameTest bodies
 * declared here exist by then; the 26.1 GameTest hook then finds this suite with every other.
 */
public final class GeoNtmHappyGhastFabric implements ModEntry {

    @Override
    public void declare() {
        GeoNtmHappyGhast.init();
        UseBlockCallback.EVENT.register((player, level, hand, hit) -> {
            InteractionResult result = GeoNtmHappyGhast.tryRig(player, level, hand, hit.getBlockPos());
            return result != null ? result : InteractionResult.PASS;
        });
    }
}
