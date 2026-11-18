package fr.geoventure.geontm.blocks.bomb;

import fr.geoventure.geontm.config.BombConfig;
import fr.geoventure.geontm.tileentity.bomb.TileEntityNukeBomb;
import fr.geoventure.geontm.tileentity.bomb.TileEntityNukeTsar;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/** Tsar Bomba: the five-part Ivy-Mike core is a Fat Man yield; its sixth part raises it to Tsar yield. */
public class NukeTsar extends NukeBombBlock {

    public NukeTsar(Properties properties) {
        super(properties);
    }

    @Override
    public TileEntityNukeTsar newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityNukeTsar(pos, state);
    }

    @Override
    protected int radiusFor(TileEntityNukeBomb bomb) {
        TileEntityNukeTsar tsar = (TileEntityNukeTsar) bomb;
        return tsar.isFilled() ? BombConfig.tsarRadius : BombConfig.manRadius;
    }
}
