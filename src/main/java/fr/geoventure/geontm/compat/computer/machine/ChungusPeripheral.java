package fr.geoventure.geontm.compat.computer.machine;

import fr.geoventure.geontm.compat.computer.SteamTypes;
import fr.geoventure.geontm.inventory.fluid.tank.FluidTankNTM;
import fr.geoventure.geontm.tileentity.machine.TileEntityChungus;
import fr.geoventure.geocore.computer.ComputerMethod;

/** 1.7 {@code TileEntityChungus}'s component {@code ntm_turbine}; power is the tick's {@code powerBuffer}. */
public final class ChungusPeripheral extends TurbinePeripheral<TileEntityChungus> {

    public ChungusPeripheral(TileEntityChungus machine) {
        super(machine, SLOTS);
    }

    @Override
    protected FluidTankNTM input(TileEntityChungus machine) {
        return machine.tanks[0];
    }

    @Override
    protected FluidTankNTM output(TileEntityChungus machine) {
        return machine.tanks[1];
    }

    @Override
    protected long power(TileEntityChungus machine) {
        return machine.powerBuffer;
    }

    @ComputerMethod(mainThread = true)
    public final Object[] setType(double type) {
        TileEntityChungus machine = machine();
        machine.tanks[0].setTankType(SteamTypes.fromInt((int) type));
        machine.setChanged();
        return new Object[]{};
    }

    @ComputerMethod
    public final Object[] getInfo() {
        return info();
    }
}
