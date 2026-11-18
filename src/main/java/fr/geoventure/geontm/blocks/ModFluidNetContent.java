package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.blocks.fluid.ModWorldFluids;
import fr.geoventure.geontm.inventory.ModFluidNetMenus;
import fr.geoventure.geontm.items.ModFluidNetItems;
import fr.geoventure.geontm.tileentity.ModFluidNetBlockEntities;


/** One bootstrap hook for the fluid-network family: blocks, items, block entities, menus. */
public final class ModFluidNetContent {

    private ModFluidNetContent() {}

    public static void init() {
        ModWorldFluids.init(); // the 1.7 world fluids, before the blocks that carry them
        ModFluidNetBlocks.init();
        ModFluidNetItems.init();
        ModFluidNetBlockEntities.init();
        ModFluidNetMenus.init();
    }
}
