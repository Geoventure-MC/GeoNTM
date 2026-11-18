package fr.geoventure.geontm.tileentity.machine.rbmk;

import fr.geoventure.geontm.api.fluidmk2.IFluidReceiverMK2;
import fr.geoventure.geontm.api.fluidmk2.IFluidStandardReceiverMK2;
import fr.geoventure.geontm.blocks.machine.rbmk.RBMKBase;
import fr.geoventure.geontm.blocks.multiblock.MultiblockSurface;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.inventory.fluid.tank.FluidTankNTM;
import fr.geoventure.geontm.tileentity.ModRBMKBlockEntities;
import fr.geoventure.geontm.tileentity.TileEntityMachineBase;
import fr.geoventure.geocore.data.DataIn;
import fr.geoventure.geocore.data.DataOut;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import fr.geoventure.geontm.tileentity.TilePort.PortDef;
import fr.geoventure.geontm.tileentity.TilePortShapes;

/** ReaSim inlet: transfers its complete water buffer to adjacent RBMK columns. */
public class BlockEntityRBMKInlet extends TileEntityMachineBase implements IFluidReceiverMK2, IFluidStandardReceiverMK2, IRBMKTicking {
    public final FluidTankNTM water = new FluidTankNTM(Fluids.WATER, 32_000);
    public BlockEntityRBMKInlet(BlockPos pos, BlockState state) { super(ModRBMKBlockEntities.RBMK_INLET.get(), pos, state, 0); }
    @Override public String getDefaultName() { return "container.rbmk_steam_inlet"; }
    protected PortDef[] cachedPorts;
    public PortDef[] getPorts() { if (cachedPorts == null) cachedPorts = TilePortShapes.around(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()); return cachedPorts; }

    @Override public void tickServer() {
        this.setupFluidPorts(getPorts());
        this.updatePortFIFO();
        if (!RBMKDials.getReasimBoilers(level)) return;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos adjacent = worldPosition.relative(direction);
            if (!(level.getBlockState(adjacent).getBlock() instanceof RBMKBase)) continue;
            BlockPos core = MultiblockSurface.coreOfAny(level, adjacent);
            if (core == null || !(level.getBlockEntity(core) instanceof BlockEntityRBMKBase column)) continue;
            int moved = Math.min(BlockEntityRBMKBase.maxWater - column.reasimWater, water.getFill());
            if (moved > 0) { column.reasimWater += moved; water.setFill(water.getFill() - moved); column.markChanged(); markChanged(); }
        }
    }
    @Override public long getDemand(FluidType type, int pressure) { return Fluids.kindOf(type) == Fluids.WATER && water.accepts(type) && pressure == water.getPressure() ? water.getMaxFill() - water.getFill() : 0; }
    @Override public FluidTankNTM[] getAllTanks() { return new FluidTankNTM[]{water}; }
    @Override public FluidTankNTM[] getReceivingTanks() { return new FluidTankNTM[]{water}; }
    @Override public long transferFluid(FluidType type, int pressure, long amount) { if (Fluids.kindOf(type) != Fluids.WATER || !water.accepts(type) || pressure != water.getPressure()) return amount; int accepted = water.fill(type, (int)Math.min(amount, Integer.MAX_VALUE), true); if (accepted > 0) setChanged(); return amount - accepted; }
    @Override protected void saveData(DataOut out) { super.saveData(out); water.writeToNBT(out, "tank"); }
    @Override protected void loadData(DataIn in) { super.loadData(in); water.readFromNBT(in, "tank"); }
    @Override public void serialize(RegistryFriendlyByteBuf buffer) { super.serialize(buffer); water.serialize(buffer); }
    @Override public void deserialize(RegistryFriendlyByteBuf buffer) { super.deserialize(buffer); water.deserialize(buffer); }
}
