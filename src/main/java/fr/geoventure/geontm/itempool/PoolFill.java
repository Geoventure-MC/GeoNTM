package fr.geoventure.geontm.itempool;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import fr.geoventure.geocore.world.ItemStacks;
import fr.geoventure.geocore.world.LootFunctions;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

/**
 * The loot function {@code geontm:fill_fluid}: an item pool row's {@code fluid}, filled the way {@link ItemPool} fills it
 * (a cell becomes that fluid's cell, a tank is filled to capacity). An item that cannot hold the fluid is left as it is.
 */
public record PoolFill(String fluid) implements LootFunctions.Transform {

    public static final String ID = Constants.MOD_ID + ":fill_fluid";
    public static final MapCodec<PoolFill> CODEC = Codec.STRING.fieldOf("fluid").xmap(PoolFill::new, PoolFill::fluid);

    public static void register() {
        LootFunctions.register(Constants.MOD_ID, "fill_fluid", CODEC);
    }

    /** Whether {@code item} holds {@code fluid}; {@link ItemPool} skips a row whose item does not. */
    public static boolean fills(Item item, String fluid) {
        return ItemPool.filled(new ItemStack(item), Fluids.fromName(fluid)) != null;
    }

    @Override
    public ItemStack apply(ItemStack stack, LootContext context) {
        ItemStack filled = ItemPool.filled(stack, Fluids.fromName(fluid));
        return filled == null ? stack : ItemStacks.copyWithCount(filled, stack.getCount());
    }
}
