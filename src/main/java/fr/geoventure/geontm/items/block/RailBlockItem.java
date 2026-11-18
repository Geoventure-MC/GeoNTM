package fr.geoventure.geontm.items.block;

import fr.geoventure.geontm.blocks.machine.RailGeneric;
import fr.geoventure.geocore.world.CompatBlockItem;
import fr.geoventure.geocore.world.CompatTooltipContext;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

/** The rail items: 1.7 {@code RailGeneric} was an {@code ITooltipProvider}, its speed and shape notes on the item. */
public class RailBlockItem extends CompatBlockItem {

    public RailBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected void appendCompatHoverText(ItemStack stack, CompatTooltipContext context, List<Component> tooltip,
                                         TooltipFlag flag) {
        if (getBlock() instanceof RailGeneric rail) rail.addInformation(tooltip);
    }
}
