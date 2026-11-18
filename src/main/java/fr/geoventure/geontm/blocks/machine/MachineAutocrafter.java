package fr.geoventure.geontm.blocks.machine;

import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.registry.TickingBlock;
import fr.geoventure.geocore.world.CompatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineAutocrafter;

/**
 * The automatic crafting table: a plain textured cube (top / side / bottom icons) with a menu. Breaking
 * it drops the recipe grid, output and battery (slots 10 and up); the nine pattern ghosts and the
 * template output are filters, not items, and vanish with the block as in 1.7.
 */
public class MachineAutocrafter extends CompatBlock implements TickingBlock {

    public MachineAutocrafter(Properties properties) {
        super(properties);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityMachineAutocrafter(pos, state);
    }

    @Override
    protected InteractionResult onUseWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (player.isShiftKeyDown()) return InteractionResult.PASS;
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer
                && level.getBlockEntity(pos) instanceof MenuProvider provider) {
            Menus.openWithPos(serverPlayer, provider, pos);
        }
        return InteractionResult.SUCCESS;
    }

}
