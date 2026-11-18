package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.inventory.ModRadarUtilityMenus;
import fr.geoventure.geontm.items.ModRadarUtilityItems;
import fr.geoventure.geontm.tileentity.ModRadarUtilityBlockEntities;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineRadarNT;
import fr.geoventure.geocore.platform.Lifecycle;

/**
 * One bootstrap hook for the radar and utility machines: the small and large radar, the radar screen and
 * linker, the forcefield emitter and its upgrades, the teleporter, tele-anchor, telelinker and recall device,
 * the orbus, the silo hatch controller and hatch, and the Soyuz cargo capsule. Also starts the radar's global
 * entity sweep (1.7 {@code TileMappings} registered the classes and converters, {@code ModEventHandler} swept).
 */
public final class ModRadarUtilityContent {

    private ModRadarUtilityContent() {}

    public static void init() {
        ModRadarUtilityBlocks.init();
        ModRadarUtilityItems.init();
        ModRadarUtilityBlockEntities.init();
        ModRadarUtilityMenus.init();
        RadarUtilityDeclarations.declare();

        TileEntityMachineRadarNT.registerEntityClasses();
        TileEntityMachineRadarNT.registerConverters();
        Lifecycle.onServerTick(TileEntityMachineRadarNT::updateSystem);
        Lifecycle.onServerStopping(server -> TileEntityMachineRadarNT.matchingEntities.clear());
    }
}
