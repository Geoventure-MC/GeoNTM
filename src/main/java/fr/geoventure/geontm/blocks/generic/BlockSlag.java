package fr.geoventure.geontm.blocks.generic;

import fr.geoventure.geocore.world.CompatBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

/**
 * Blast slag. 1.12.2 keeps the intact and cracked variants as two metadata values on one block; here
 * they fold onto {@link #CRACKED}.
 */
public class BlockSlag extends CompatBlock {

    public static final BooleanProperty CRACKED = BooleanProperty.create("cracked");

    public BlockSlag(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(CRACKED, false));
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CRACKED);
    }
}
