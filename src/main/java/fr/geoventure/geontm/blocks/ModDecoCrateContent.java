package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.inventory.ModDecoCrateMenus;
import fr.geoventure.geontm.tileentity.ModDecoCrateBlockEntities;

/**
 * One bootstrap hook for the storage crates, decorative props and simple blocks ({@link ModDecoCrateBlocks}).
 */
public final class ModDecoCrateContent {

    private ModDecoCrateContent() {}

    public static void init() {
        ModDecoCrateBlocks.init();
        ModDecoCrateBlockEntities.init();
        ModDecoCrateMenus.init();
        DecoCrateDeclarations.declare();
    }
}
