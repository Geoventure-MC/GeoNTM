package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.sound.ModDoorSounds;
import fr.geoventure.geontm.tileentity.ModDoorBlockEntities;


/** One common bootstrap hook for the door family: blocks (with their declarations), block entities, sounds. */
public final class ModDoorContent {
    private ModDoorContent() {}

    public static void init() {
        ModDoorSounds.init();
        ModDoorBlocks.init();
        ModDoorBlockEntities.init();
    }
}
