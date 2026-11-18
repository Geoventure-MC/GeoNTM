package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.inventory.ModExplosivesMenus;
import fr.geoventure.geontm.items.ModExplosivesItems;
import fr.geoventure.geontm.tileentity.ModExplosivesBlockEntities;


/** One common bootstrap hook for the conventional explosives, mines, charges and detonators. */
public final class ModExplosivesContent {
    private ModExplosivesContent() {}

    public static void init() {
        ModExplosivesBlocks.init();
        ModExplosivesItems.init();
        ModExplosivesBlockEntities.init();
        ModExplosivesMenus.init();
    }
}
