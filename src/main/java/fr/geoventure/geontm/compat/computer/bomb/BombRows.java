package fr.geoventure.geontm.compat.computer.bomb;

import fr.geoventure.geontm.compat.computer.Peripherals;
import fr.geoventure.geontm.compat.computer.Peripherals.Reach;
import fr.geoventure.geontm.tileentity.bomb.TileEntityLaunchPad;
import fr.geoventure.geontm.tileentity.bomb.TileEntityLaunchPadLarge;
import fr.geoventure.geontm.tileentity.bomb.TileEntityLaunchTable;

import java.util.List;

/** Launch pads and the launch table: 1.7's OpenComputers components on CC: Tweaked. */
public final class BombRows {

    private BombRows() {}

    // 1.7 LaunchPad/LaunchPadLarge: the makeExtra cells are full proxies; the large pad's other cells are
    // inventory-only and do not connect. 1.7 LaunchTable's cells were plain dummies, so the table is its own block.
    // The rusted pad has no component, as in 1.7.
    public static final List<Peripherals.Row<?>> ROWS = List.of(
            Peripherals.row(TileEntityLaunchPad.class, LaunchPadPeripheral::new, Reach.CELLS),
            Peripherals.row(TileEntityLaunchPadLarge.class, LaunchPadPeripheral::new, Reach.CELLS),
            Peripherals.row(TileEntityLaunchTable.class, LaunchTablePeripheral::new, Reach.BLOCK)
    );
}
