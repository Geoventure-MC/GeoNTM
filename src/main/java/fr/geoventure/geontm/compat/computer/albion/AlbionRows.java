package fr.geoventure.geontm.compat.computer.albion;

import fr.geoventure.geontm.compat.computer.Peripherals;
import fr.geoventure.geontm.compat.computer.Peripherals.Reach;
import fr.geoventure.geontm.tileentity.machine.albion.TileEntityPADetector;
import fr.geoventure.geontm.tileentity.machine.albion.TileEntityPADipole;
import fr.geoventure.geontm.tileentity.machine.albion.TileEntityPAQuadrupole;
import fr.geoventure.geontm.tileentity.machine.albion.TileEntityPARFC;
import fr.geoventure.geontm.tileentity.machine.albion.TileEntityPASource;

import java.util.List;

/** Particle accelerator parts: 1.7's OpenComputers components on CC: Tweaked. */
public final class AlbionRows {

    private AlbionRows() {}

    // 1.7 BlockPA*: every makeExtra cell is a TileEntityProxyCombo flagged power and fluid
    public static final List<Peripherals.Row<?>> ROWS = List.of(
            Peripherals.row(TileEntityPASource.class, PASourcePeripheral::new, Reach.CELLS),
            Peripherals.row(TileEntityPADipole.class, PADipolePeripheral::new, Reach.CELLS),
            Peripherals.row(TileEntityPAQuadrupole.class, PAQuadrupolePeripheral::new, Reach.CELLS),
            Peripherals.row(TileEntityPARFC.class, PARFCPeripheral::new, Reach.CELLS),
            Peripherals.row(TileEntityPADetector.class, PADetectorPeripheral::new, Reach.CELLS)
    );
}
