package fr.geoventure.geontm.blocks.machine;

import fr.geoventure.geontm.tileentity.ModRtgBlockEntities;
import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.registry.TickingBlock;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineRTG;
import fr.geoventure.geocore.world.CompatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

/** The single-block, 15-pellet grey radioisotope generator. */
public final class MachineRTG extends CompatBlock implements TickingBlock {
    public MachineRTG(Properties properties) {
        super(properties);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityMachineRTG(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL; // RTGRenderer is the only world-render path for this block.
    }

    @Override
    protected InteractionResult onUseWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                                  BlockHitResult hit) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (player.isShiftKeyDown()) return InteractionResult.PASS;
        if (player instanceof ServerPlayer serverPlayer && level.getBlockEntity(pos) instanceof MenuProvider provider) {
            Menus.openWithPos(serverPlayer, provider, pos);
        }
        return InteractionResult.SUCCESS;
    }

}
