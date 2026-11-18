package fr.geoventure.geontm.items.tool;

import fr.geoventure.geontm.blocks.machine.rbmk.RBMKBase;
import fr.geoventure.geontm.blocks.machine.rbmk.RBMKConsole;
import fr.geoventure.geontm.blocks.machine.rbmk.RBMKCraneConsole;
import fr.geoventure.geontm.blocks.machine.rbmk.RBMKDisplay;
import fr.geoventure.geontm.blocks.multiblock.MultiblockSurface;
import fr.geoventure.geontm.inventory.gui.GuiUtil;
import fr.geoventure.geontm.items.ModDataComponents;
import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityCraneConsole;
import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityRBMKConsole;
import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityRBMKDisplay;
import fr.geoventure.geocore.world.CompatItem;
import fr.geoventure.geocore.world.CompatTooltipContext;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

/** RBMK linker (mallet): links a reactor core to a console or crane console. */
public class ItemRBMKTool extends CompatItem {
    public ItemRBMKTool(Properties properties) { super(properties); }

    @Override
    protected void appendCompatHoverText(ItemStack stack, CompatTooltipContext context,
                                         List<Component> tooltip, TooltipFlag flag) {
        for (Component line : GuiUtil.keyArray("item.rbmk_tool.desc")) tooltip.add(line.copy().withStyle(ChatFormatting.YELLOW));
    }

    private static void message(Player player, String key) {
        if (player != null) player.sendSystemMessage(Component.translatable(key).withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Block block = level.getBlockState(pos).getBlock();
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();

        if (block instanceof RBMKBase) {
            if (!level.isClientSide()) {
                BlockPos core = MultiblockSurface.coreOfAny(level, pos);
                if (core != null) {
                    ModDataComponents.RBMK_TOOL_TARGET.set(stack, core.asLong());
                    message(player, "item.rbmk_tool.linked");
                }
            }
            return InteractionResult.SUCCESS;
        }

        // 1.7 ItemRBMKTool: the display panel takes the stored reactor position directly.
        if (block instanceof RBMKDisplay) {
            Long target = ModDataComponents.RBMK_TOOL_TARGET.get(stack).orElse(null);
            if (target == null) return InteractionResult.PASS;
            if (!level.isClientSide() && level.getBlockEntity(pos) instanceof BlockEntityRBMKDisplay display) {
                BlockPos targetPos = BlockPos.of(target);
                display.setTarget(targetPos.getX(), targetPos.getY(), targetPos.getZ());
                message(player, "item.rbmk_tool.set");
            }
            return InteractionResult.SUCCESS;
        }

        // 1.7 findCore: a console cell is the console's own block too, so the core comes from the index
        BlockPos consoleCore = MultiblockSurface.coreOfAny(level, pos);
        if (consoleCore == null) return InteractionResult.PASS;
        Block coreBlock = level.getBlockState(consoleCore).getBlock();
        if (!(coreBlock instanceof RBMKConsole) && !(coreBlock instanceof RBMKCraneConsole)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide()) {
            Long target = ModDataComponents.RBMK_TOOL_TARGET.get(stack).orElse(null);
            BlockEntity blockEntity = level.getBlockEntity(consoleCore);
            if (target != null) {
                BlockPos targetPos = BlockPos.of(target);
                if (blockEntity instanceof BlockEntityRBMKConsole console) {
                    console.setTarget(targetPos.getX(), targetPos.getY(), targetPos.getZ());
                    message(player, "item.rbmk_tool.set");
                } else if (blockEntity instanceof BlockEntityCraneConsole crane) {
                    crane.setTarget(targetPos.getX(), targetPos.getY(), targetPos.getZ());
                    message(player, "item.rbmk_tool.set");
                }
            }
        }
        return InteractionResult.SUCCESS;
    }
}
