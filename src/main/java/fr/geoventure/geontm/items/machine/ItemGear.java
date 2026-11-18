package fr.geoventure.geontm.items.machine;

import fr.geoventure.geontm.items.LegacyMeta;
import fr.geoventure.geontm.items.ModGearItems;
import fr.geoventure.geocore.world.CompatItem;
import net.minecraft.world.item.ItemStack;

/**
 * One of 1.7's two large gears ({@code gear_large} metadata 0, {@code gear_large_steel} metadata 1): two registered
 * items as in ntm-next; the item knows which one it is.
 */
public class ItemGear extends CompatItem implements LegacyMeta {

    private final int variant;

    public ItemGear(Properties properties, int variant) {
        super(properties);
        this.variant = variant;
    }

    /** 0 = regular, 1 = steel (1.7's metadata); a run-time value. */
    public int getVariant(ItemStack stack) {
        return variant;
    }

    @Override
    public int legacyMeta() {
        return variant;
    }

    /** A stack of the gear for 1.7 metadata {@code variant}. */
    public ItemStack of(int variant) {
        return ModGearItems.gear(variant);
    }
}
