package fr.geoventure.geontm.compat.sable;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.config.GeneralConfig;
import fr.geoventure.geocore.world.SubLevels;

/**
 * GEONTM on Sable sub-levels (Create Aeronautics' engine): machines, cables, pipes and multiblocks carried onto and off
 * moving structures. The loader-neutral half; the loader roots ({@code src/sable-neoforge}, {@code src/sable-fabric})
 * call it from their own entry points, only when the {@code sable} mod is loaded. The shared tree names none of this:
 * its own part is neutral, asking GeoCore's {@link SubLevels} where a block really is ({@code toWorld}) and whether a
 * removal is a carry ({@code isMoving}).
 */
public final class GeoNtmSableIntegration {

    private GeoNtmSableIntegration() {}

    public static void setup() {
        SubLevels.addMoveListener(CarriedBlocks::afterMove);
        SubLevels.addStructureResolver(MultiblockParts::partsWith);
        SubLevels.addMassRule(BlockMasses::inIronBlocks);
        Constants.LOG.info("Sable present: GEONTM blocks follow sub-level assembly, splitting and disassembly"
                + (GeneralConfig.sableHardnessMass ? ", and weigh by hardness" : ""));
    }
}
