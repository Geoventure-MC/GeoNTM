package fr.geoventure.geontm.blocks.machine;

import fr.geoventure.geontm.blocks.ITooltipProvider;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineFunnel;
import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.registry.TickingBlock;
import fr.geoventure.geocore.world.CompatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

/** The old combinator funnel: a three-part OBJ machine with a position-bound menu. */
public final class MachineFunnel extends CompatBlock implements TickingBlock, ITooltipProvider {

    public MachineFunnel(Properties properties) { super(properties); }

    @Override @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityMachineFunnel(pos, state);
    }

    @Override public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }

    @Override
    protected InteractionResult onUseWithoutItem(BlockState state, Level level, BlockPos pos,
                                                  Player player, BlockHitResult hit) {
        if (player.isShiftKeyDown()) return InteractionResult.PASS;
        if (!level.isClientSide() && player instanceof ServerPlayer server
                && level.getBlockEntity(pos) instanceof MenuProvider provider) {
            Menus.openWithPos(server, provider, pos);
        }
        return InteractionResult.SUCCESS;
    }
}
