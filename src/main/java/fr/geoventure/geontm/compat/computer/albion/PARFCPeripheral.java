package fr.geoventure.geontm.compat.computer.albion;

import fr.geoventure.geontm.tileentity.machine.albion.TileEntityPARFC;
import fr.geoventure.geocore.computer.ComputerMethod;

/** 1.7 {@code TileEntityPARFC}'s component {@code ntm_pa_rfc}. */
public final class PARFCPeripheral extends CooledPeripheral<TileEntityPARFC> {

    public PARFCPeripheral(TileEntityPARFC machine) {
        super(machine, "ntm_pa_rfc", SLOTS, 0);
    }

    @ComputerMethod
    public final Object[] getInfo() {
        return read(s -> new Object[]{s.longAt(POWER), s.longAt(MAX_POWER), s.intAt(COOLANT), s.intAt(COOLANT_MAX),
                s.intAt(HOT), s.intAt(HOT_MAX)});
    }
}
