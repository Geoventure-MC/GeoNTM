package fr.geoventure.geontm.compat.computer.machine;

import fr.geoventure.geontm.compat.computer.Peripherals;
import fr.geoventure.geontm.compat.computer.Peripherals.Reach;
import fr.geoventure.geontm.tileentity.machine.TileEntityGeiger;
import fr.geoventure.geontm.tileentity.machine.TileEntityICF;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineRadarLarge;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineRadarNT;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineReactorBreeding;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineSatLink;
import fr.geoventure.geontm.tileentity.machine.TileEntityMicrowave;
import fr.geoventure.geontm.tileentity.machine.TileEntityPWRController;
import fr.geoventure.geontm.tileentity.machine.TileEntityReactorControl;
import fr.geoventure.geontm.tileentity.machine.TileEntityReactorResearch;
import fr.geoventure.geontm.tileentity.machine.TileEntityReactorZirnox;
import fr.geoventure.geontm.tileentity.machine.TileEntityWatz;
import fr.geoventure.geontm.tileentity.machine.pile.TileEntityPileControl;
import fr.geoventure.geontm.tileentity.machine.pile.TileEntityPileLoader;

import java.util.List;

/** Reactors, radar, ground station, Geiger counter and microwave: 1.7's OpenComputers components on CC: Tweaked. */
public final class ReactorRows {

    private ReactorRows() {}

    public static final List<Peripherals.Row<?>> ROWS = List.of(
            Peripherals.row(TileEntityGeiger.class, GeigerPeripheral::new, Reach.BLOCK),
            Peripherals.row(TileEntityMicrowave.class, MicrowavePeripheral::new, Reach.BLOCK),
            // 1.7 makeExtra cells: TileEntityProxyCombo(true, true, true)
            Peripherals.row(TileEntityReactorZirnox.class, ZirnoxPeripheral::new, Reach.CELLS),
            // 1.7 ReactorResearch stamps no makeExtra cell
            Peripherals.row(TileEntityReactorResearch.class, ResearchReactorPeripheral::new, Reach.BLOCK),
            // 1.7 MachineReactorBreeding's cells are TileEntityProxyInventory, never a component
            Peripherals.row(TileEntityMachineReactorBreeding.class, BreedingReactorPeripheral::new, Reach.BLOCK),
            Peripherals.row(TileEntityReactorControl.class, ReactorControlPeripheral::new, Reach.BLOCK),
            Peripherals.row(TileEntityPWRController.class, PWRControllerPeripheral::new, Reach.BLOCK),
            Peripherals.row(TileEntityPileLoader.class, PileLoaderPeripheral::new, Reach.BLOCK),
            Peripherals.row(TileEntityPileControl.class, PileControlPeripheral::new, Reach.BLOCK),
            // 1.7 makeExtra cells: TileEntityProxyCombo().inventory().fluid()
            Peripherals.row(TileEntityWatz.class, WatzPeripheral::new, Reach.CELLS),
            Peripherals.row(TileEntityICF.class, ICFPeripheral::new, Reach.CELLS),
            Peripherals.row(TileEntityMachineRadarNT.class, RadarPeripheral::new, Reach.BLOCK),
            // 1.7 makeExtra cells: TileEntityProxyCombo().power()
            Peripherals.row(TileEntityMachineRadarLarge.class, RadarPeripheral::new, Reach.CELLS),
            // 1.7 MachineSatLink's cells are a flagless TileEntityProxyCombo, never a component
            Peripherals.row(TileEntityMachineSatLink.class, SatLinkPeripheral::new, Reach.BLOCK)
    );
}
