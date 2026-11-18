package fr.geoventure.geontm.tileentity.machine;

import fr.geoventure.geontm.api.fluidmk2.IFluidStandardSenderMK2;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.inventory.fluid.tank.FluidTankNTM;
import fr.geoventure.geontm.tileentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import fr.geoventure.geontm.tileentity.TileEntityLoadedBase;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import fr.geoventure.geontm.tileentity.TilePort.PortDef;
import fr.geoventure.geontm.tileentity.TilePortShapes;

/**
 * A creative (infinite) fluid source for testing the network, the fluid analogue of
 * {@link TileEntityCreativeGenerator}. Holds a single {@code WATER} sending tank kept permanently full;
 * offers it on all six sides.
 */
public class TileEntityCreativeFluidSource extends TileEntityLoadedBase implements IFluidStandardSenderMK2 {

    public static final int CAPACITY = 1_000_000;
    private final FluidTankNTM tank = new FluidTankNTM(Fluids.WATER, CAPACITY);

    public TileEntityCreativeFluidSource(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CREATIVE_FLUID_SOURCE.get(), pos, state);
        tank.setFill(CAPACITY);
    }

    protected PortDef[] cachedPorts;
    public PortDef[] getPorts() { if (cachedPorts == null) cachedPorts = TilePortShapes.around(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()); return cachedPorts; }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TileEntityCreativeFluidSource source) {
        source.tank.setFill(CAPACITY); // infinite: refill before providing
        source.setupFluidPorts(source.getPorts());
        source.updatePortFIFO();
    }

    @Override
    public void useUpFluid(FluidType type, int pressure, long amount) {
        // infinite source, never actually drains
    }

    @Override
    public @NonNull FluidTankNTM[] getSendingTanks() {
        return new FluidTankNTM[]{tank};
    }

    @Override
    public FluidTankNTM[] getAllTanks() {
        return new FluidTankNTM[]{tank};
    }

    @Override
    public long getProviderSpeed(FluidType type, int pressure) {
        return 100_000L;
    }

    @Override
    public boolean isLoaded() {
        return !isRemoved();
    }
}
