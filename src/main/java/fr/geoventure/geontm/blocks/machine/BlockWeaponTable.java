package fr.geoventure.geontm.blocks.machine;

import fr.geoventure.geontm.inventory.container.ContainerWeaponTable;
import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.world.CompatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/** The 1.7 weapon modification table; it has no block entity and opens a stack-owned menu. */
public final class BlockWeaponTable extends CompatBlock {
    public BlockWeaponTable(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult onUseWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                                  BlockHitResult hit) {
        if (player.isShiftKeyDown()) return InteractionResult.PASS;
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            Menus.openWithPos(serverPlayer, new SimpleMenuProvider(
                    (id, inventory, ignored) -> new ContainerWeaponTable(id, inventory, pos),
                    Component.translatable("container.weaponsTable")), pos);
        }
        return InteractionResult.SUCCESS;
    }
}
