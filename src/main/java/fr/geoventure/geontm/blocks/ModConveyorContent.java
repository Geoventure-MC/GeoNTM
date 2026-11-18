package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.inventory.ModConveyorMenus;
import fr.geoventure.geontm.items.ModConveyorItems;
import fr.geoventure.geontm.tileentity.ModConveyorBlockEntities;


/** One common bootstrap hook for the conveyor belt and crane family. */
public final class ModConveyorContent {

    private ModConveyorContent() {}

    public static void init() {
        ModConveyorBlocks.init();
        ModConveyorItems.init();
        ModConveyorBlockEntities.init();
        ModConveyorMenus.init();
    }
}
