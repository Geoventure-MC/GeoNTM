package fr.geoventure.geontm.tileentity.network;

import fr.geoventure.geontm.api.energymk2.IEnergyConductorMK2;
import fr.geoventure.geontm.api.energymk2.PowerGraph;
import fr.geoventure.geontm.api.tile.ILoadedTile;
import fr.geoventure.geontm.tileentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A power cable: a pure conductor, and nothing more. The conduction is a cell of the level's power
 * graph, minted and dropped by {@link fr.geoventure.geontm.blocks.network.BlockCable} with the block itself, so
 * this carries no node, no power and no tick. What is left is the marker a neighbour reads to decide
 * whether to draw an arm towards us, and something for the renderer to hang off.
 */
public class TileEntityCableBaseNT extends BlockEntity implements IEnergyConductorMK2, ILoadedTile {

    public TileEntityCableBaseNT(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RED_CABLE.get(), pos, state);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        PowerGraph.dropCells(this, getBlockPos());
    }

    @Override
    public boolean isLoaded() {
        return !isRemoved();
    }
}
