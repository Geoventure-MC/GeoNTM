package fr.geoventure.geontm.tileentity.machine;

import fr.geoventure.geontm.api.energymk2.IEnergyReceiverMK2;
import fr.geoventure.geontm.tileentity.TileEntityLoadedBase;
import fr.geoventure.geocore.data.DataIn;
import fr.geoventure.geocore.data.DataOut;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import fr.geoventure.geontm.blocks.machine.PowerDetector;
import fr.geoventure.geontm.tileentity.ModEnergyNetBlockEntities;
import fr.geoventure.geontm.tileentity.TilePort.PortDef;
import fr.geoventure.geontm.tileentity.TilePortShapes;

/** 1.7's {@code TileEntityMachineDetector}: a 5 HE receiver that lights while it is being fed. */
public class TileEntityMachineDetector extends TileEntityLoadedBase implements IEnergyReceiverMK2 {

    long power;

    public TileEntityMachineDetector(BlockPos pos, BlockState state) {
        super(ModEnergyNetBlockEntities.MACHINE_DETECTOR.get(), pos, state);
    }

    protected PortDef[] cachedPorts;
    public PortDef[] getPorts() { if (cachedPorts == null) cachedPorts = TilePortShapes.around(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()); return cachedPorts; }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TileEntityMachineDetector detector) {
        detector.setupPowerPorts(detector.getPorts());
        detector.updateAllPorts();
        detector.receivePower();
        boolean on = false;
        if (detector.power > 0) {
            on = true;
            detector.power--;
        }
        if (state.getValue(PowerDetector.ON) != on) {
            level.setBlock(pos, state.setValue(PowerDetector.ON, on), 3);
            detector.setChanged();
        }
    }

    @Override public void setPower(long i) { power = i; }
    @Override public long getPower() { return power; }
    @Override public long getMaxPower() { return 5; }
    @Override public ConnectionPriority getPriority() { return ConnectionPriority.HIGH; }

    @Override
    protected void saveData(DataOut output) {
        super.saveData(output);
        output.putLong("power", power);
    }

    @Override
    protected void loadData(DataIn input) {
        super.loadData(input);
        power = input.getLongOr("power", 0L);
    }
}
