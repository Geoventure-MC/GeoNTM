package fr.geoventure.geontm.tileentity.network;

import fr.geoventure.geontm.blocks.network.BlockFluidDuct;
import fr.geoventure.geontm.handler.ToolKeybinds;
import fr.geoventure.geontm.tileentity.IFluidCopiable;
import fr.geoventure.geocore.data.Tags;

import fr.geoventure.geontm.api.fluidmk2.DuctData;
import fr.geoventure.geontm.api.fluidmk2.FluidGraph;
import fr.geoventure.geontm.api.fluidmk2.IFluidPipeMK2;
import fr.geoventure.geontm.api.tile.ILoadedTile;
import fr.geoventure.geontm.capability.MachineCaps;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geontm.uninos.graph.LevelNodeGraph;
import fr.geoventure.geocore.data.CompatBlockEntity;
import fr.geoventure.geocore.data.DataIn;
import fr.geoventure.geocore.data.DataOut;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A solid fluid duct: a pure fluid conductor, the fluid analogue of {@link TileEntityCableBaseNT}.
 * Conduction is a cell of the level's fluid graph, minted by the block on placement; this carries no
 * fluid and does not tick.
 *
 * <p>Like the 1.12.2 {@code fluid_duct_mk2}, a duct defaults to {@code Fluids.NONE} and the player sets
 * its type by right-clicking with a {@code fluid_identifier}: it then forms a network with adjacent
 * ducts and machines of that one fluid only, which on the graph is
 * {@link fr.geoventure.geontm.api.fluidmk2.FluidGraphProvider#dataCompatible}. The carried type is synced to the
 * client so the renderer can tint the duct's overlay.
 *
 * <p>The block entity is authoritative for the type and the graph follows it: {@link #clearRemoved}
 * reconciles the cell on every chunk load, which is also what gives a duct placed before the fluid
 * graph existed a cell at all.
 */
public class TileEntityFluidDuctNT extends CompatBlockEntity implements IFluidPipeMK2, ILoadedTile,
        IFluidCopiable {

    protected FluidType type = Fluids.NONE;

    protected TileEntityFluidDuctNT(BlockEntityType<?> type,
            BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    public FluidType getFluidType() {
        return this.type;
    }

    public void setFluidType(FluidType type) {
        if (this.type == type) return;
        this.type = type;
        setChanged();
        if (level instanceof ServerLevel server) {
            FluidGraph.get(server).replaceNodeData(getBlockPos().asLong(), ductData());
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    /**
     * The datum this duct's graph cell carries: its one fluid. The exhaust duct answers
     * {@link DuctData#SMOKE_ANY} instead, whatever smoke it is skinned as.
     */
    protected DuctData ductData() {
        return DuctData.of(type);
    }

    @Override
    public void clearRemoved() {
        super.clearRemoved();
        if (!(level instanceof ServerLevel server)) return;
        LevelNodeGraph<DuctData> graph = FluidGraph.get(server);
        long key = getBlockPos().asLong();
        if (graph.containsCell(key)) graph.replaceNodeData(key, ductData());
        else graph.addNode(key, ductData(), MachineCaps.FACE_MASK_ALL);
    }

    @Override
    protected boolean syncFullTagOnChunkLoad() {
        return true;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public boolean canConnect(FluidType type, ForgeDirection dir) {
        return dir != ForgeDirection.UNKNOWN && type == this.type;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        FluidGraph.dropCells(this, getBlockPos());
    }

    @Override
    protected void saveData(DataOut output) {
        super.saveData(output);
        Fluids.write(output, "type", this.type);
    }

    @Override
    protected void loadData(DataIn input) {
        super.loadData(input);
        var old = this.type;
        setFluidType(Fluids.readExact(input, "type"));
        if (old != this.type) remesh();
    }

    @Override
    public boolean isLoaded() {
        return !isRemoved();
    }

    // 1.7 TileEntityPipeBaseNT's IFluidCopiable: the settings tool copies the duct's one type and pastes a type
    // into it; with the copy tool's Ctrl key held the paste retypes the whole run of the duct's old type (64).

    @Override
    public FluidType[] getFluidsToCopy() {
        return new FluidType[]{type};
    }

    @Override
    public fr.geoventure.geontm.inventory.fluid.tank.@org.jspecify.annotations.Nullable FluidTankNTM getTankToPaste() {
        return null;
    }

    @Override
    public void pasteSettings(CompoundTag nbt, int index, Level level,
                              Player player, BlockPos pos) {
        FluidType[] fluids = Fluids.readListExact(nbt, "fluids");
        if (fluids.length == 0) return;
        FluidType fluid = index < fluids.length ? fluids[index] : Fluids.NONE;
        boolean ctrl = ToolKeybinds.getKeyPressed(player,
                ToolKeybinds.EnumToolKey.TOOL_CTRL);
        // spreadType also refreshes the retyped ducts' connections, which a bare setFluidType would not.
        BlockFluidDuct.spreadType(level, pos, fluid, getFluidType(), ctrl ? 64 : 1);
    }
}
