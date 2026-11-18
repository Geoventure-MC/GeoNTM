package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.inventory.ModCustomMissileMenus;
import fr.geoventure.geontm.items.ModCustomMissileItems;
import fr.geoventure.geontm.tileentity.ModCustomMissileBlockEntities;

/**
 * One bootstrap hook for the custom missile system: the 122 {@code mp_*} parts and {@code missile_custom}, the
 * assembly station, the compact launcher and launch table with their core components, and the large and rusted
 * launch pads.
 */
public final class ModCustomMissileContent {

    private ModCustomMissileContent() {}

    public static void init() {
        ModCustomMissileItems.init();
        ModCustomMissileBlocks.init();
        ModCustomMissileBlockEntities.init();
        ModCustomMissileMenus.init();
        CustomMissileDeclarations.declare();
    }
}
