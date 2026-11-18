package fr.geoventure.geontm.items.block;

import fr.geoventure.geontm.blocks.StandardInfo;
import fr.geoventure.geocore.world.CompatBlockItem;
import fr.geoventure.geocore.world.CompatTooltipContext;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

/** 1.7 {@code addStandardInfo} on the item, for a block whose class is not an {@code ITooltipProvider}. */
public class DescBlockItem extends CompatBlockItem {

    public DescBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected void appendCompatHoverText(ItemStack stack, CompatTooltipContext context, List<Component> tooltip,
                                         TooltipFlag flag) {
        StandardInfo.add(tooltip, getBlock().getDescriptionId() + ".desc");
    }
}
