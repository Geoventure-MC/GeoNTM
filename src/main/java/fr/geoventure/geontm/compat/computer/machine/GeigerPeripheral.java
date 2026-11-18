package fr.geoventure.geontm.compat.computer.machine;

import fr.geoventure.geontm.compat.computer.SnapshotPeripheral;
import fr.geoventure.geontm.tileentity.machine.TileEntityGeiger;
import fr.geoventure.geocore.computer.ComputerMethod;

/** 1.7 {@code TileEntityGeiger}'s component {@code ntm_geiger}. */
public final class GeigerPeripheral extends SnapshotPeripheral<TileEntityGeiger> {

    public GeigerPeripheral(TileEntityGeiger machine) {
        super(machine, "ntm_geiger", 0, 0);
    }

    @Override
    protected void capture(TileEntityGeiger machine) {
    }

    // 1.7: check() samples the chunk radiation at call time, a world query rather than a field, so it runs on the
    // server thread instead of reading a snapshot
    @ComputerMethod(mainThread = true)
    public final Object[] getRads() {
        return new Object[]{machine().check()};
    }
}
