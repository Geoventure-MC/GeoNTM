package fr.geoventure.geontm.blocks.bomb;

import fr.geoventure.geontm.config.BombConfig;
import fr.geoventure.geontm.tileentity.bomb.TileEntityNukeBomb;
import fr.geoventure.geontm.tileentity.bomb.TileEntityNukeMike;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/** Ivy Mike, retaining the legacy test-device versus fully assembled radius split. */
public class NukeMike extends NukeBombBlock {

    public NukeMike(Properties properties) {
        super(properties);
    }

    @Override
    public TileEntityNukeMike newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityNukeMike(pos, state);
    }

    @Override
    protected int radiusFor(TileEntityNukeBomb bomb) {
        // The five-part test device is ready at Fat Man yield; all three Mike components raise it to Mike yield.
        return ((TileEntityNukeMike) bomb).isFilled() ? BombConfig.mikeRadius : BombConfig.manRadius;
    }
}
