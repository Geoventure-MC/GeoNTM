package fr.geoventure.geontm.blocks.generic;

import fr.geoventure.geontm.tileentity.deco.TileEntityDeco;
import fr.geoventure.geocore.registry.TickingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

/**
 * A {@link BlockDeco} whose visual is a 3D OBJ model drawn by {@link fr.geoventure.geontm.client.render.DecoObjRenderer}
 * (boxcar / boat / the satellite dishes). Bakes an empty model and carries a
 * {@link TileEntityDeco} so the block-entity renderer runs (the empty-model + BER pattern the machines and
 * the Fat Man use). Collision/facing come from {@link BlockDeco} (these all use {@code FULL}).
 */
public class BlockDecoObj extends BlockDeco implements TickingBlock {

    public BlockDecoObj(Properties properties, Shape shape) {
        super(properties, shape);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityDeco(pos, state);
    }
}
