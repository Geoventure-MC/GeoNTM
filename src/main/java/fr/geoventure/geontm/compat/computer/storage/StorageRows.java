package fr.geoventure.geontm.compat.computer.storage;

import fr.geoventure.geontm.compat.computer.Peripherals;
import fr.geoventure.geontm.compat.computer.Peripherals.Reach;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineBattery;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineFluidTank;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineGiantTank;
import fr.geoventure.geontm.tileentity.machine.storage.TileEntityBarrel;
import fr.geoventure.geontm.tileentity.machine.storage.TileEntityBatteryREDD;
import fr.geoventure.geontm.tileentity.machine.storage.TileEntityBatterySocket;
import fr.geoventure.geontm.tileentity.machine.storage.TileEntityMachineOrbus;
import fr.geoventure.geontm.tileentity.machine.storage.TileEntityMassStorage;

import java.util.List;

/** Batteries, tanks and barrels: 1.7's OpenComputers components on CC: Tweaked. */
public final class StorageRows {

    private StorageRows() {}

    public static final List<Peripherals.Row<?>> ROWS = List.of(
            // 1.7 BlockFluidBarrel: a plain block; the Orbus, big-ass tank and BAT-9000 makeExtra fluid proxies
            Peripherals.row(TileEntityBarrel.class, BarrelPeripheral::new, Reach.BLOCK),
            Peripherals.row(TileEntityMachineOrbus.class, BarrelPeripheral::new, Reach.CELLS),
            Peripherals.row(TileEntityMachineGiantTank.class, BarrelPeripheral::new, Reach.CELLS),
            Peripherals.row(TileEntityMachineFluidTank.class, MachineFluidTankPeripheral::new, Reach.CELLS),
            // 1.7 MachineBattery: no makeExtra cells (the tiered batteries are machine batteries)
            Peripherals.row(TileEntityMachineBattery.class, MachineBatteryPeripheral<TileEntityMachineBattery>::new,
                    Reach.BLOCK),
            // 1.7 socket and REDD: makeExtra power proxies
            Peripherals.row(TileEntityBatterySocket.class, BatterySocketPeripheral::new, Reach.CELLS),
            Peripherals.row(TileEntityBatteryREDD.class, BatteryREDDPeripheral::new, Reach.CELLS),
            Peripherals.row(TileEntityMassStorage.class, MassStoragePeripheral::new, Reach.BLOCK)
    );
}
