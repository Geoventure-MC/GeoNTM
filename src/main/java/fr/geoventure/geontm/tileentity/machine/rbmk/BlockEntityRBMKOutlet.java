package fr.geoventure.geontm.tileentity.machine.rbmk;

import fr.geoventure.geontm.api.fluidmk2.IFluidProviderMK2;
import fr.geoventure.geontm.api.fluidmk2.IFluidStandardSenderMK2;
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

/** ReaSim outlet: exposes the steam extracted from adjacent RBMK columns. */
public class BlockEntityRBMKOutlet extends TileEntityMachineBase implements IFluidProviderMK2, IFluidStandardSenderMK2, IRBMKTicking {
    public final FluidTankNTM steam = new FluidTankNTM(Fluids.SUPERHOTSTEAM, 32_000);
    public BlockEntityRBMKOutlet(BlockPos pos, BlockState state) { super(ModRBMKBlockEntities.RBMK_OUTLET.get(), pos, state, 0); }
    @Override public String getDefaultName() { return "container.rbmk_steam_outlet"; }
    protected PortDef[] cachedPorts;
    public PortDef[] getPorts() { if (cachedPorts == null) cachedPorts = TilePortShapes.around(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()); return cachedPorts; }

    @Override public void tickServer() {
        this.setupAllPorts(getPorts());
        this.updatePortPIFIFO();
        if (RBMKDials.getReasimBoilers(level)) for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos adjacent = worldPosition.relative(direction);
            if (!(level.getBlockState(adjacent).getBlock() instanceof RBMKBase)) continue;
            BlockPos core = MultiblockSurface.coreOfAny(level, adjacent);
            if (core == null || !(level.getBlockEntity(core) instanceof BlockEntityRBMKBase column)) continue;
            int moved = Math.min(steam.getMaxFill() - steam.getFill(), column.reasimSteam);
            if (moved > 0) { column.reasimSteam -= moved; steam.setFill(steam.getFill() + moved); column.markChanged(); markChanged(); }
        }
    }
    @Override public long getFluidAvailable(FluidType type, int pressure) { return steam.provides(type) && pressure == steam.getPressure() ? steam.getFill() : 0; }
    @Override public FluidTankNTM[] getAllTanks() { return new FluidTankNTM[]{steam}; }
    @Override public FluidTankNTM[] getSendingTanks() { return new FluidTankNTM[]{steam}; }
    @Override public void useUpFluid(FluidType type, int pressure, long amount) { if (steam.provides(type) && pressure == steam.getPressure()) { steam.setFill(steam.getFill() - (int)Math.min(amount, steam.getFill())); setChanged(); } }
    @Override protected void saveData(DataOut out) { super.saveData(out); steam.writeToNBT(out, "tank"); }
    @Override protected void loadData(DataIn in) { super.loadData(in); steam.readFromNBT(in, "tank"); }
    @Override public void serialize(RegistryFriendlyByteBuf buffer) { super.serialize(buffer); steam.serialize(buffer); }
    @Override public void deserialize(RegistryFriendlyByteBuf buffer) { super.deserialize(buffer); steam.deserialize(buffer); }
}
