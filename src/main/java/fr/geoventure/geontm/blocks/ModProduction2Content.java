package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.inventory.ModProduction2Menus;
import fr.geoventure.geontm.sound.ModProduction2Sounds;
import fr.geoventure.geontm.tileentity.ModProduction2BlockEntities;


/**
 * One common bootstrap hook for the production-machine family (group B): the large mining drill
 * (excavator), the bedrock ore processor (ore slopper), the annihilator, the old dead boiler and the
 * two RTG-pellet furnaces.
 */
public final class ModProduction2Content {
    private ModProduction2Content() {}

    public static void init() {
        ModProduction2Blocks.init();
        ModProduction2BlockEntities.init();
        ModProduction2Menus.init();
        ModProduction2Sounds.init();
    }
}
