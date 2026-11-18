package fr.geoventure.geontm.blocks.network;

import fr.geoventure.geontm.blocks.multiblock.BlockMultiblockMachine;
import fr.geoventure.geontm.tileentity.network.TileEntityRadioAUTOCAL;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

/** The one-block-core AUTOCAL unit from the old Redstone-over-Radio network. */
public final class RadioAUTOCAL extends BlockMultiblockMachine {
    public RadioAUTOCAL(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntity createCoreBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityRadioAUTOCAL(pos, state);
    }

    @Override
    public int[] getDimensions() {
        return new int[]{1, 0, 0, 0, 0, 0};
    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    protected InteractionResult onUseWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                                  BlockHitResult hit) {
        return player.isShiftKeyDown() ? InteractionResult.PASS
                : super.onUseWithoutItem(state, level, pos, player, hit);
    }

}
