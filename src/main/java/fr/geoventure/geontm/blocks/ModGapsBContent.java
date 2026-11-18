package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.inventory.ModHeldCrateMenus;

/** One bootstrap hook for the gap batch B systems that register anything (the held-crate menus). */
public final class ModGapsBContent {

    private ModGapsBContent() {}

    public static void init() {
        ModHeldCrateMenus.init();
    }
}
