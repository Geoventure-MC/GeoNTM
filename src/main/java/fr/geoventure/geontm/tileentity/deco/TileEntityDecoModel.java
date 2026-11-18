package fr.geoventure.geontm.tileentity.deco;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import fr.geoventure.geontm.client.render.DecoModelRenderer;
import fr.geoventure.geontm.tileentity.ModDecoBlockEntities;

/** A do-nothing block entity that lets {@link DecoModelRenderer} draw the CRTs and the pole satellite receiver. */
public class TileEntityDecoModel extends BlockEntity {

    public TileEntityDecoModel(BlockPos pos, BlockState state) {
        super(ModDecoBlockEntities.DECO_MODEL.get(), pos, state);
    }
}
