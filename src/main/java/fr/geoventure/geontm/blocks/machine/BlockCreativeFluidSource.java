package fr.geoventure.geontm.blocks.machine;

import fr.geoventure.geontm.tileentity.ModBlockEntities;
import fr.geoventure.geocore.registry.TickingBlock;
import fr.geoventure.geontm.tileentity.machine.TileEntityCreativeFluidSource;
import fr.geoventure.geocore.world.CompatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

/** Infinite fluid source for testing; backed by {@link TileEntityCreativeFluidSource}. */
public class BlockCreativeFluidSource extends CompatBlock implements TickingBlock {

    public BlockCreativeFluidSource(Properties properties) {
        super(properties);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityCreativeFluidSource(pos, state);
    }

}
