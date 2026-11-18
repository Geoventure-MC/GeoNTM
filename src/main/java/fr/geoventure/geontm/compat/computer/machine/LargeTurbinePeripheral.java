package fr.geoventure.geontm.compat.computer.machine;

import fr.geoventure.geontm.compat.computer.SteamTypes;
import fr.geoventure.geontm.inventory.fluid.tank.FluidTankNTM;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineLargeTurbine;
import fr.geoventure.geocore.computer.ComputerMethod;

/**
 * 1.7 {@code TileEntityMachineLargeTurbine}'s component {@code ntm_turbine}. 1.7's {@code ManagedPeripheral.invoke}
 * forgot {@code getPower}, but OpenComputers prefers the {@code @Callback}, so it was callable and stays.
 */
public final class LargeTurbinePeripheral extends TurbinePeripheral<TileEntityMachineLargeTurbine> {

    public LargeTurbinePeripheral(TileEntityMachineLargeTurbine machine) {
        super(machine, SLOTS);
    }

    @Override
    protected FluidTankNTM input(TileEntityMachineLargeTurbine machine) {
        return machine.tanks[0];
    }

    @Override
    protected FluidTankNTM output(TileEntityMachineLargeTurbine machine) {
        return machine.tanks[1];
    }

    @Override
    protected long power(TileEntityMachineLargeTurbine machine) {
        return machine.power;
    }

    @ComputerMethod(mainThread = true)
    public final Object[] setType(double type) {
        TileEntityMachineLargeTurbine machine = machine();
        machine.tanks[0].setTankType(SteamTypes.fromInt((int) type));
        machine.setChanged();
        return new Object[]{};
    }

    @ComputerMethod
    public final Object[] getInfo() {
        return info();
    }
}
