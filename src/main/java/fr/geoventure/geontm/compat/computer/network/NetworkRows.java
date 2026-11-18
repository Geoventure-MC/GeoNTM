package fr.geoventure.geontm.compat.computer.network;

import fr.geoventure.geontm.compat.computer.Peripherals;
import fr.geoventure.geontm.compat.computer.Peripherals.Reach;
import fr.geoventure.geontm.tileentity.network.TileEntityCableGauge;
import fr.geoventure.geontm.tileentity.network.TileEntityFluidCounterValve;
import fr.geoventure.geontm.tileentity.network.TileEntityPipeGauge;
import fr.geoventure.geontm.tileentity.network.TileEntityRadioAUTOCAL;
import fr.geoventure.geontm.tileentity.network.TileEntityRadioTelex;
import fr.geoventure.geontm.tileentity.network.TileEntityRadioTorchBase;
import fr.geoventure.geontm.tileentity.network.TileEntityRadioTorchController;
import fr.geoventure.geontm.tileentity.network.TileEntityRadioTorchReader;

import java.util.List;

/**
 * Gauges, pumps, valves and Redstone-over-Radio blocks: 1.7's OpenComputers components on CC: Tweaked.
 */
public final class NetworkRows {

    private NetworkRows() {}

    public static final List<Peripherals.Row<?>> ROWS = List.of(
            Peripherals.row(TileEntityCableGauge.class, CableGaugePeripheral::new, Reach.BLOCK),
            Peripherals.row(TileEntityPipeGauge.class, PipeGaugePeripheral::new, Reach.BLOCK),
            Peripherals.row(TileEntityFluidCounterValve.class, FluidCounterValvePeripheral::new, Reach.BLOCK),
            Peripherals.row(TileEntityRadioTelex.class, RadioTelexPeripheral::new, Reach.BLOCK),
            Peripherals.row(TileEntityRadioAUTOCAL.class, RadioAutocalPeripheral::new, Reach.BLOCK),
            Peripherals.row(TileEntityRadioTorchReader.class, RadioReaderPeripheral::new, Reach.BLOCK),
            Peripherals.row(TileEntityRadioTorchController.class, RadioControllerPeripheral::new, Reach.BLOCK),
            // the sender and the receiver
            Peripherals.row(TileEntityRadioTorchBase.class, RadioTorchPeripheral::new, Reach.BLOCK)
    );
}
