package fr.geoventure.geontm.tileentity.network;

import fr.geoventure.geontm.tileentity.ModFluidNetBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import fr.geoventure.geontm.blocks.network.FluidSwitch;

/** The redstone-driven valve's tile: the 1.7 {@code TileEntityFluidValve} under a {@code FluidSwitch}. */
public class TileEntityFluidSwitch extends TileEntityFluidValve {

    public TileEntityFluidSwitch(BlockPos pos, BlockState state) {
        super(ModFluidNetBlockEntities.SWITCH.get(), pos, state);
    }

    protected TileEntityFluidSwitch(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
