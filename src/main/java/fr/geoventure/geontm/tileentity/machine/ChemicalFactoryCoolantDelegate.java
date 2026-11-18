package fr.geoventure.geontm.tileentity.machine;

import fr.geoventure.geontm.api.energymk2.IEnergyReceiverMK2;
import fr.geoventure.geontm.api.fluidmk2.IFluidStandardTransceiverMK2;
import fr.geoventure.geontm.inventory.fluid.tank.FluidTankNTM;

/** Network-facing coolant view used only by the factory's four coolant proxy positions. */
public final class ChemicalFactoryCoolantDelegate implements IEnergyReceiverMK2, IFluidStandardTransceiverMK2 {

    private final TileEntityMachineChemicalFactory owner;

    public ChemicalFactoryCoolantDelegate(TileEntityMachineChemicalFactory owner) {
        this.owner = owner;
    }

    @Override public long getPower() { return owner.getPower(); }
    @Override public void setPower(long power) { owner.setPower(power); }
    @Override public long getMaxPower() { return owner.getMaxPower(); }
    @Override public boolean isLoaded() { return owner.isLoaded(); }

    @Override public FluidTankNTM[] getReceivingTanks() { return new FluidTankNTM[]{owner.water}; }
    @Override public FluidTankNTM[] getSendingTanks() { return new FluidTankNTM[]{owner.lps}; }
    @Override public FluidTankNTM[] getAllTanks() { return new FluidTankNTM[]{owner.water, owner.lps}; }
}
