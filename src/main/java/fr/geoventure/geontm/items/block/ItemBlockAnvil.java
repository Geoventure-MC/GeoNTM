package fr.geoventure.geontm.items.block;

import fr.geoventure.geontm.blocks.machine.NTMAnvil;
import fr.geoventure.geocore.world.CompatBlockItem;
import fr.geoventure.geocore.world.CompatTooltipContext;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;


/** Block item for the {@link NTMAnvil}s. Adds the gold "Tier: N" tooltip that the 1.12.2 block showed. */
public class ItemBlockAnvil extends CompatBlockItem {

    public ItemBlockAnvil(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected void appendCompatHoverText(ItemStack stack, CompatTooltipContext context,
                                         List<Component> lines, TooltipFlag flag) {
        if (getBlock() instanceof NTMAnvil anvil) {
            lines.add(Component.literal("Tier: " + anvil.tier).withStyle(ChatFormatting.GOLD));
        }
    }
}
