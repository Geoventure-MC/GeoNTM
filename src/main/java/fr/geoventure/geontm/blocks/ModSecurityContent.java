package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.tileentity.machine.TileEntityLockableBase;
import fr.geoventure.geocore.data.GeoCoreComponent;
import fr.geoventure.geontm.inventory.ModSecurityMenus;
import fr.geoventure.geontm.items.ModSecurityItems;
import fr.geoventure.geontm.items.tool.ItemCounterfeitKeys;
import fr.geoventure.geontm.items.tool.ItemLock;
import fr.geoventure.geontm.sound.ModSecuritySounds;
import fr.geoventure.geontm.tileentity.ModSecurityBlockEntities;

/**
 * One common bootstrap hook for the security / perimeter family: barbed wire, spikes, the sand-bag
 * barricade, the chain-link fence, the wooden barrier, the locksmith table and the padlock / key items.
 */
public final class ModSecurityContent {
    private ModSecurityContent() {}

    /** The old {@code pins} NBT integer on keys and padlocks. */
    public static final GeoCoreComponent<Integer> KEY_PINS = GeoCoreComponent.integer(Constants.MOD_ID, "key_pins");

    public static void init() {
        // The lockable doors let these items' own use handlers run instead of toggling the door.
        TileEntityLockableBase.LOCK_TOOLS =
                stack -> stack.getItem() instanceof ItemLock || stack.getItem() instanceof ItemCounterfeitKeys;
        ModSecuritySounds.init();
        ModSecurityBlocks.init();
        ModSecurityItems.init();
        ModSecurityBlockEntities.init();
        ModSecurityMenus.init();
    }
}
