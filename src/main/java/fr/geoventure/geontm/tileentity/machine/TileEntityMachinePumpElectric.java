package fr.geoventure.geontm.tileentity.machine;

import fr.geoventure.geontm.api.environment.Environment;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.lib.GeoNtmBlockTags;
import fr.geoventure.geontm.sound.ModSounds;
import fr.geoventure.geocore.data.Tags;

import fr.geoventure.geontm.api.energymk2.IEnergyReceiverMK2;
import fr.geoventure.geontm.api.fluidmk2.IFluidStandardSenderMK2;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.inventory.fluid.tank.FluidTankNTM;
import fr.geoventure.geontm.tileentity.ModBlockEntities;
import fr.geoventure.geontm.tileentity.TileEntityMachineBase;
import fr.geoventure.geocore.data.DataIn;
import fr.geoventure.geocore.data.DataOut;
import fr.geoventure.geocore.world.Compat;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import fr.geoventure.geontm.tileentity.TilePort.PortDef;
import fr.geoventure.geontm.tileentity.TilePortShapes;

/** Electric groundwater pump logic ported from the legacy pump base and electric implementation. */
public class TileEntityMachinePumpElectric extends TileEntityMachineBase
        implements IEnergyReceiverMK2, IFluidStandardSenderMK2, PumpAnimationState {

    public static final long MAX_POWER = 10_000;
    public static final int POWER_COST = 1_000;
    public static final int NON_WATER_DEBUFF = 100;

    public long power;
    public final FluidTankNTM water = new FluidTankNTM(Fluids.WATER, GroundwaterPumps.electricSpeed * 100);
    public boolean isOn;
    public float rotor;
    public float lastRotor;
    public boolean onGround;
    public int groundCheckDelay;

    public TileEntityMachinePumpElectric(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MACHINE_PUMP_ELECTRIC.get(), pos, state, 0);
    }

    @Override
    public String getDefaultName() {
        return "block.geontm.machine_pump_electric";
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TileEntityMachinePumpElectric pump) {
        pump.setupAllPorts(pump.getPorts());
        pump.updatePortPIFIFO();

        if (pump.groundCheckDelay > 0) pump.groundCheckDelay--;
        else pump.onGround = pump.checkGround(level, pos);

        pump.isOn = false;
        if (pump.canOperate() && pos.getY() <= GroundwaterPumps.groundHeight && pump.onGround) {
            pump.isOn = true;
            pump.operate();
        }

        pump.setChanged();
        pump.networkPackNT();
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, TileEntityMachinePumpElectric pump) {
        pump.lastRotor = pump.rotor;
        if (pump.isOn) pump.rotor += 10F;
        if (pump.rotor >= 360F) {
            pump.rotor -= 360F;
            pump.lastRotor -= 360F;
            play(level, pos, ModSounds.STEAM_ENGINE_OPERATE.get(), 0.5F, 0.75F);
            play(level, pos, SoundEvents.GENERIC_SPLASH, 1F, 0.5F);
        }
    }

    private static void play(Level level, BlockPos pos, SoundEvent sound, float volume, float pitch) {
        level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                sound, SoundSource.BLOCKS, volume, pitch, false);
    }

    private boolean canOperate() {
        return power >= POWER_COST && water.getFill() < water.getMaxFill();
    }

    private void operate() {
        power -= POWER_COST;
        int pumpSpeed = water.getTankType() == Fluids.WATER ? GroundwaterPumps.electricSpeed : GroundwaterPumps.electricSpeed / NON_WATER_DEBUFF;
        water.setFill(Math.min(water.getFill() + pumpSpeed, water.getMaxFill()));
    }

    private boolean checkGround(Level level, BlockPos pos) {
        if (!level.dimensionType().hasSkyLight()) return false;
        // geontm-space TileEntityMachinePumpBase.checkGround: no groundwater in orbit, and the pump lifts the body's
        // water table, whatever fluid that is (fr.geoventure.geontm.api.environment.Environment; plain GEONTM: water)
        FluidType table = Environment.groundwater(level, pos);
        if (table == Fluids.NONE) return false;
        water.setTankType(table);

        int validBlocks = 0;
        int invalidBlocks = 0;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y >= -GroundwaterPumps.groundDepth; y--) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos groundPos = pos.offset(x, y, z);
                    BlockState groundState = level.getBlockState(groundPos);
                    if (y == -1 && !Compat.isSolidRender(groundState, level, groundPos)) return false;
                    if (groundState.is(GeoNtmBlockTags.PUMP_GROUND)) validBlocks++;
                    else invalidBlocks++;
                }
            }
        }
        return validBlocks >= invalidBlocks;
    }

    protected PortDef[] cachedPorts;
    public PortDef[] getPorts() { if (cachedPorts == null) cachedPorts = TilePortShapes.flare(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()); return cachedPorts; }

    @Override public float getRotor() { return rotor; }
    @Override public float getLastRotor() { return lastRotor; }

    @Override public long getPower() { return power; }
    @Override public void setPower(long power) { this.power = power; }
    @Override public long getMaxPower() { return MAX_POWER; }
    @Override public FluidTankNTM[] getSendingTanks() { return new FluidTankNTM[]{water}; }
    @Override public FluidTankNTM[] getAllTanks() { return new FluidTankNTM[]{water}; }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeBoolean(isOn);
        buf.writeBoolean(onGround);
        water.serialize(buf);
        buf.writeLong(power);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        isOn = buf.readBoolean();
        onGround = buf.readBoolean();
        water.deserialize(buf);
        power = buf.readLong();
    }

    @Override
    protected void saveData(DataOut output) {
        super.saveData(output);
        output.putLong("power", power);
        output.putBoolean("isOn", isOn);
        output.putBoolean("onGround", onGround);
        output.putInt("groundCheckDelay", groundCheckDelay);
        water.writeToNBT(output, "water");
    }

    @Override
    protected void loadData(DataIn input) {
        super.loadData(input);
        power = input.getLongOr("power", 0L);
        isOn = input.getBooleanOr("isOn", false);
        onGround = input.getBooleanOr("onGround", false);
        groundCheckDelay = input.getIntOr("groundCheckDelay", 0);
        water.readFromNBT(input, "water");
    }
}
