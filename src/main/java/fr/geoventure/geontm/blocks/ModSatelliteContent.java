package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.commands.CommandSatellites;
import fr.geoventure.geontm.entity.ModSatelliteEntities;
import fr.geoventure.geontm.inventory.ModSatelliteMenus;
import fr.geoventure.geontm.saveddata.satellites.SatelliteSystems;
import fr.geoventure.geontm.tileentity.ModSatelliteBlockEntities;
import fr.geoventure.geocore.command.CompatCommands;
import fr.geoventure.geocore.platform.Lifecycle;

/**
 * One bootstrap hook for the satellite family's blocks, block entities, menus, entities and Dejson declarations
 * (the {@code satellite} item and the chips register through {@code ModSatelliteItems}). Also starts the
 * emission-log ageing that 1.7 ran from {@code ModEventHandler}'s world tick.
 */
public final class ModSatelliteContent {

    private ModSatelliteContent() {}

    public static void init() {
        ModSatelliteBlocks.init();
        ModSatelliteBlockEntities.init();
        ModSatelliteMenus.init();
        ModSatelliteEntities.init();
        SatelliteDeclarations.declare();

        Lifecycle.onServerTick(SatelliteSystems::tickServer);
        CompatCommands.server(CommandSatellites::register);
    }
}
