package fr.geoventure.geontm.tileentity.network;

import fr.geoventure.geontm.api.energymk2.CableData;
import fr.geoventure.geontm.api.energymk2.IEnergyConductorMK2;
import fr.geoventure.geontm.api.energymk2.PowerGraph;
import fr.geoventure.geontm.api.energymk2.PowerGraphProvider;
import fr.geoventure.geontm.api.tile.ILoadedTile;
import fr.geoventure.geontm.capability.MachineCaps;
import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geontm.uninos.graph.LevelNodeGraph;
import fr.geoventure.geocore.data.CompatBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import fr.geoventure.geontm.blocks.network.CableSwitch;
import fr.geoventure.geontm.tileentity.ModEnergyNetBlockEntities;

/**
 * The switchable conductor behind {@code cable_switch} and {@code cable_detector}: 1.7's
 * {@code TileEntityCableSwitch}, which only created its node while the block was ON. On the graph that is
 * the whole of it -- the cell exists exactly while the switch conducts, as the fluid valve does.
 */
public class TileEntityCableSwitch extends CompatBlockEntity implements IEnergyConductorMK2, ILoadedTile {

    public TileEntityCableSwitch(BlockPos pos, BlockState state) {
        super(ModEnergyNetBlockEntities.CABLE_SWITCH.get(), pos, state);
    }

    public boolean isOn() {
        return getBlockState().hasProperty(CableSwitch.ON) && getBlockState().getValue(CableSwitch.ON);
    }

    public void updateState() {
        syncCell();
        setChanged();
    }

    @Override
    public void clearRemoved() {
        super.clearRemoved();
        syncCell();
    }

    private void syncCell() {
        if (!(level instanceof ServerLevel server)) return;
        LevelNodeGraph<CableData> graph = PowerGraph.get(server);
        long key = getBlockPos().asLong();
        if (!isOn()) {
            if (graph.containsCell(key)) graph.removeNode(key);
        } else if (!graph.containsCell(key)) {
            graph.addNode(key, PowerGraphProvider.INSTANCE.createData(getBlockState()), MachineCaps.FACE_MASK_ALL);
        }
    }

    @Override
    public boolean canConnect(ForgeDirection dir) {
        return dir != ForgeDirection.UNKNOWN;
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
