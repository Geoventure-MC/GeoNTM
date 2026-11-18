package fr.geoventure.geontm.compat.computer.machine;

import fr.geoventure.geontm.compat.computer.Peripherals;
import fr.geoventure.geontm.compat.computer.Peripherals.Reach;
import fr.geoventure.geontm.tileentity.machine.TileEntityChungus;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineCombustionEngine;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineIndustrialTurbine;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineLargeTurbine;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineTurbineGas;
import fr.geoventure.geontm.tileentity.machine.dfc.TileEntityCoreEmitter;
import fr.geoventure.geontm.tileentity.machine.dfc.TileEntityCoreInjector;
import fr.geoventure.geontm.tileentity.machine.dfc.TileEntityCoreReceiver;
import fr.geoventure.geontm.tileentity.machine.dfc.TileEntityCoreStabilizer;
import fr.geoventure.geontm.tileentity.machine.oil.TileEntityMachineCoker;

import java.util.List;

/** Turbines, DFC, combustion engine and coker: 1.7's OpenComputers components on CC: Tweaked. */
public final class PowerRows {

    private PowerRows() {}

    // CELLS: the 1.7 block's makeExtra cells are TileEntityProxyCombo flagged power and/or fluid
    public static final List<Peripherals.Row<?>> ROWS = List.of(
            Peripherals.row(TileEntityMachineLargeTurbine.class, LargeTurbinePeripheral::new, Reach.CELLS),
            Peripherals.row(TileEntityChungus.class, ChungusPeripheral::new, Reach.CELLS),
            Peripherals.row(TileEntityMachineIndustrialTurbine.class, IndustrialTurbinePeripheral::new, Reach.CELLS),
            Peripherals.row(TileEntityMachineTurbineGas.class, GasTurbinePeripheral::new, Reach.CELLS),
            Peripherals.row(TileEntityCoreEmitter.class, CoreEmitterPeripheral::new, Reach.BLOCK),
            Peripherals.row(TileEntityCoreReceiver.class, CoreReceiverPeripheral::new, Reach.BLOCK),
            Peripherals.row(TileEntityCoreInjector.class, CoreInjectorPeripheral::new, Reach.BLOCK),
            Peripherals.row(TileEntityCoreStabilizer.class, CoreStabilizerPeripheral::new, Reach.BLOCK),
            Peripherals.row(TileEntityMachineCombustionEngine.class, CombustionEnginePeripheral::new, Reach.CELLS),
            Peripherals.row(TileEntityMachineCoker.class, CokerPeripheral::new, Reach.CELLS)
    );
}
