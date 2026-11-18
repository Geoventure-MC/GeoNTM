package fr.geoventure.geontm.compat.computer.rbmk;

import fr.geoventure.geontm.compat.computer.Peripherals;
import fr.geoventure.geontm.compat.computer.Peripherals.Reach;
import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityCraneConsole;
import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityRBMKBoiler;
import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityRBMKConsole;
import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityRBMKControlAuto;
import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityRBMKControlManual;
import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityRBMKCooler;
import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityRBMKGauge;
import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityRBMKGraph;
import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityRBMKHeater;
import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityRBMKIndicator;
import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityRBMKKeyPad;
import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityRBMKLever;
import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityRBMKNumitron;
import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityRBMKOutgasser;
import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityRBMKRod;
import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityRBMKTerminal;

import java.util.List;

/**
 * RBMK columns, consoles and panels: 1.7's OpenComputers components on CC: Tweaked. The rod row covers the ReaSim
 * channel (a subclass, same 1.7 component). Only the boiler, cooler, heater and outgasser flag their column-top proxy
 * for fluid, so only they answer at the top; a control column's top proxy carries no flags and a fuel channel's
 * only its inventory.
 */
public final class RbmkRows {

    private RbmkRows() {}

    public static final List<Peripherals.Row<?>> ROWS = List.of(
            Peripherals.row(BlockEntityRBMKRod.class, RBMKRodPeripheral::new, Reach.BLOCK),
            Peripherals.row(BlockEntityRBMKControlManual.class, RBMKControlManualPeripheral::new, Reach.BLOCK),
            Peripherals.row(BlockEntityRBMKControlAuto.class, RBMKControlPeripheral::new, Reach.BLOCK),
            Peripherals.row(BlockEntityRBMKBoiler.class, RBMKBoilerPeripheral::new, Reach.RBMK_TOP),
            Peripherals.row(BlockEntityRBMKCooler.class, RBMKCoolerPeripheral::new, Reach.RBMK_TOP),
            Peripherals.row(BlockEntityRBMKHeater.class, RBMKHeaterPeripheral::new, Reach.RBMK_TOP),
            Peripherals.row(BlockEntityRBMKOutgasser.class, RBMKOutgasserPeripheral::new, Reach.RBMK_TOP),
            Peripherals.row(BlockEntityRBMKConsole.class, RBMKConsolePeripheral::new, Reach.BLOCK),
            Peripherals.row(BlockEntityCraneConsole.class, CraneConsolePeripheral::new, Reach.BLOCK),
            Peripherals.row(BlockEntityRBMKTerminal.class, RBMKTerminalPeripheral::new, Reach.BLOCK),
            Peripherals.row(BlockEntityRBMKNumitron.class, RBMKNumitronPeripheral::new, Reach.BLOCK),
            Peripherals.row(BlockEntityRBMKLever.class, RBMKLeverPeripheral::new, Reach.BLOCK),
            Peripherals.row(BlockEntityRBMKKeyPad.class, RBMKKeyPadPeripheral::new, Reach.BLOCK),
            Peripherals.row(BlockEntityRBMKIndicator.class, RBMKIndicatorPeripheral::new, Reach.BLOCK),
            Peripherals.row(BlockEntityRBMKGraph.class, RBMKGraphPeripheral::new, Reach.BLOCK),
            Peripherals.row(BlockEntityRBMKGauge.class, RBMKGaugePeripheral::new, Reach.BLOCK)
    );
}
