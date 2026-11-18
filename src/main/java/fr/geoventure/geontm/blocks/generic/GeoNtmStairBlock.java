package fr.geoventure.geontm.blocks.generic;

import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Public-constructor wrapper around vanilla {@link StairBlock} (whose constructor is {@code protected}),
 * so the cross-loader {@code DeferredRegister} can build GEONTM stairs from a base block's default state.
 * The generated {@code ModBlocks} passes the (already-registered) base block's {@code defaultBlockState()}.
 */
public class GeoNtmStairBlock extends StairBlock {

    public GeoNtmStairBlock(BlockState baseState, BlockBehaviour.Properties properties) {
        super(baseState, properties);
    }
}
