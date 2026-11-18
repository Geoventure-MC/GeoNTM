package fr.geoventure.geontm.compat.sable;

import fr.geoventure.geocore.platform.ModEntry;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Fabric's entry to HBM's Sable integration ({@link GeoNtmSableIntegration}): a {@code geocore:entry} the build appends
 * after HBM's {@code FabricInit} in {@code fabric.mod.json} only on nodes that declare Sable.
 */
public final class GeoNtmSableFabric implements ModEntry {

    @Override
    public void declare() {
        if (FabricLoader.getInstance().isModLoaded("sable")) GeoNtmSableIntegration.setup();
    }
}
