package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.inventory.ModProductionMenus;
import fr.geoventure.geontm.sound.ModProductionSounds;
import fr.geoventure.geontm.tileentity.ModProductionBlockEntities;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineFunnel;


/**
 * One common bootstrap hook for the production-machine family (group A): rotary furnace, thresher,
 * microwave, automatic crafting table, conveyor press and the burner press preheater.
 */
public final class ModProductionContent {
    private ModProductionContent() {}

    public static void init() {
        ModProductionBlocks.init();
        ModProductionBlockEntities.init();
        ModProductionMenus.init();
        ModProductionSounds.init();
        TileEntityMachineFunnel.init();
    }
}
