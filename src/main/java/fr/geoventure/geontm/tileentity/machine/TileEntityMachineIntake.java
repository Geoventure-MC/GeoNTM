package fr.geoventure.geontm.tileentity.machine;

import fr.geoventure.geontm.api.energymk2.IEnergyReceiverMK2;
import fr.geoventure.geontm.api.environment.Environment;
import fr.geoventure.geontm.api.fluidmk2.IFluidStandardSenderMK2;
import fr.geoventure.geontm.client.sound.IntakeSoundHandler;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.inventory.fluid.tank.FluidTankNTM;
import fr.geoventure.geontm.lib.DirPos;
import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geontm.tileentity.MachineCore;
import fr.geoventure.geontm.tileentity.TileEntityLoadedBase;
import fr.geoventure.geocore.data.DataIn;
import fr.geoventure.geocore.data.DataOut;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import fr.geoventure.geontm.tileentity.TilePort.PortDef;
import fr.geoventure.geontm.tileentity.TilePortShapes;

/** Server buffer and client motor state for the legacy air intake. */
public class TileEntityMachineIntake extends TileEntityLoadedBase
        implements IEnergyReceiverMK2, IFluidStandardSenderMK2 {

    public static final long MAX_POWER = 2_000L;
    public static final long POWER_PER_TICK = MAX_POWER / 20L;
    public static final int AIR_CAPACITY = 1_000;

    public final FluidTankNTM compair = new FluidTankNTM(Fluids.AIR, AIR_CAPACITY);
    public long power;
    public float fan;
    public float prevFan;

    public TileEntityMachineIntake(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public boolean isMotorRunning() {
        return power >= POWER_PER_TICK;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TileEntityMachineIntake intake) {
        intake.setupAllPorts(intake.getPorts());
        intake.updatePortPIFIFO();

        if (intake.isMotorRunning()) {
            if (intake.canCompress(level, pos)) intake.compair.setFill(intake.compair.getMaxFill());
            intake.power -= POWER_PER_TICK; // geontm-space: the motor draws power even when there is no air
        }

        intake.setChanged();
        intake.networkPackNT();
    }

    /**
     * geontm-space {@code canCompress}: 1 mB of the local air makes 100 mB of compressed air ("the same conversion ratio
     * as water to steam"), at least 1 mB, asked of {@link fr.geoventure.geontm.api.environment.Environment#compressAir} (which
     * captures it from the planet or the sealed room). A full tank asks nothing. Plain GEONTM always has air.
     */
    private boolean canCompress(Level level, BlockPos pos) {
        int consumption = compair.getMaxFill() - compair.getFill();
        if (consumption <= 0) return true;
        consumption = Math.max(1, consumption / 100);
        return Environment.compressAir(level, pos, consumption);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, TileEntityMachineIntake intake) {
        intake.prevFan = intake.fan;
        if (intake.isMotorRunning()) {
            intake.fan += 45F;
            if (intake.fan >= 360F) {
                intake.fan -= 360F;
                intake.prevFan -= 360F;
            }
        }
        IntakeSoundHandler.tickLoop(intake);
    }

    public ForgeDirection getFacing() {
        BlockState state = getBlockState();
        int meta = MachineCore.facingOrdinal(state, 2);
        return ForgeDirection.getOrientation(meta);
    }

    protected PortDef[] cachedPorts;
    public PortDef[] getPorts() { if (cachedPorts == null) cachedPorts = TilePortShapes.solderer(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), MachineCore.facing(getBlockState())); return cachedPorts; }

    public DirPos[] getConPos() { return PortDef.connections(getPorts()); }

    @Override public long getPower() { return power; }
    @Override public void setPower(long value) { power = value; }
    @Override public long getMaxPower() { return MAX_POWER; }
    @Override public FluidTankNTM[] getSendingTanks() { return new FluidTankNTM[]{compair}; }
    @Override public FluidTankNTM[] getAllTanks() { return new FluidTankNTM[]{compair}; }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(power);
        compair.serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        power = buf.readLong();
        compair.deserialize(buf);
    }

    @Override
    protected void saveData(DataOut output) {
        super.saveData(output);
        output.putLong("power", power);
        compair.writeToNBT(output, "compair");
    }

    @Override
    protected void loadData(DataIn input) {
        super.loadData(input);
        power = input.getLongOr("power", 0L);
        compair.readFromNBT(input, "compair");
    }
}
