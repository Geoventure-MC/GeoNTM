package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.inventory.ModBombs2Menus;
import fr.geoventure.geontm.items.ModBombs2Items;
import fr.geoventure.geontm.tileentity.ModBombs2BlockEntities;


/**
 * One bootstrap hook for the special-bomb family: the custom nuke, F.L.E.I.J.A., Blue Rinse, Prototype,
 * Balefire Bomb, the thermobaric pair, EMP device, levitation bomb and Flame War in a Box.
 */
public final class ModBombs2Content {

    private ModBombs2Content() {}

    public static void init() {
        ModBombs2Blocks.init();
        ModBombs2Items.init();
        ModBombs2BlockEntities.init();
        ModBombs2Menus.init();
    }
}
