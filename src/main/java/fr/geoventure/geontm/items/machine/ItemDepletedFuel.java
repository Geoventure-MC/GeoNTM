package fr.geoventure.geontm.items.machine;

import fr.geoventure.geontm.items.ModDataComponents;
import fr.geoventure.geontm.items.special.ItemNuclearWaste;
import fr.geoventure.geocore.data.ModelData;
import fr.geoventure.geocore.world.CompatTooltipContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * The old two-subtype {@code ItemDepletedFuel} (0 = cooled, 1 = hot): heat is the same mutable stack state
 * the ZIRNOX waste uses, so the spent-fuel drum understands both.
 */
public class ItemDepletedFuel extends ItemNuclearWaste {

    public ItemDepletedFuel(Properties properties) {
        super(properties);
    }

    public static boolean isHot(ItemStack stack) {
        return ModDataComponents.wasteHot(stack);
    }

    public static ItemStack makeHot(Item item, int count) {
        ItemStack stack = new ItemStack(item, count);
        setHot(stack, true);
        return stack;
    }

    public static void setHot(ItemStack stack, boolean hot) {
        if (hot) {
            ModDataComponents.wasteHot(stack, true);
            ModelData.setColor(stack, ItemZirnoxWaste.HOT_TINT);
        } else {
            ModDataComponents.removeWasteHot(stack);
            ModDataComponents.removeCustomModelData(stack);
        }
    }

    @Override
    protected void appendCompatHoverText(ItemStack stack, CompatTooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (isHot(stack)) tooltip.add(Component.translatable("desc.item.wasteCooling").withStyle(ChatFormatting.GOLD));
    }
}
