package fr.geoventure.geontm.tileentity;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineBattery;
import fr.geoventure.geocore.registry.BlockEntities;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import fr.geoventure.geontm.blocks.ModEnergyNetBlocks;
import fr.geoventure.geontm.tileentity.machine.TileEntityBatteryTier;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineDetector;
import fr.geoventure.geontm.tileentity.machine.TileEntityTesla;
import fr.geoventure.geontm.tileentity.network.TileEntityCableClassic;
import fr.geoventure.geontm.tileentity.network.TileEntityCableGauge;
import fr.geoventure.geontm.tileentity.network.TileEntityCablePaintable;
import fr.geoventure.geontm.tileentity.network.TileEntityCableSwitch;
import fr.geoventure.geontm.tileentity.network.TileEntityConnector;
import fr.geoventure.geontm.tileentity.network.TileEntityConnectorSuper;
import fr.geoventure.geontm.tileentity.network.TileEntityDiode;
import fr.geoventure.geontm.tileentity.network.TileEntityNetworkSeparator;
import fr.geoventure.geontm.tileentity.network.TileEntityPylon;
import fr.geoventure.geontm.tileentity.network.TileEntityPylonLarge;
import fr.geoventure.geontm.tileentity.network.TileEntityPylonMedium;
import fr.geoventure.geontm.tileentity.network.TileEntitySubstation;

/** Feature-local block-entity types for the energy-network family. */
public final class ModEnergyNetBlockEntities {

    private ModEnergyNetBlockEntities() {}

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCK_ENTITIES);

    public static final RegistryObject<BlockEntityType<TileEntityPylon>> PYLON =
            REG.blockEntity("red_pylon", () -> BlockEntities.type(TileEntityPylon::new,
                    ModEnergyNetBlocks.red_pylon.get(), ModEnergyNetBlocks.red_pylon_steel.get()));
    public static final RegistryObject<BlockEntityType<TileEntityPylonMedium>> PYLON_MEDIUM =
            REG.blockEntity("red_pylon_medium", () -> BlockEntities.type(TileEntityPylonMedium::new,
                    ModEnergyNetBlocks.red_pylon_medium_wood.get(), ModEnergyNetBlocks.red_pylon_medium_wood_transformer.get(),
                    ModEnergyNetBlocks.red_pylon_medium_steel.get(), ModEnergyNetBlocks.red_pylon_medium_steel_transformer.get()));
    public static final RegistryObject<BlockEntityType<TileEntityPylonLarge>> PYLON_LARGE =
            REG.blockEntity("red_pylon_large", () -> BlockEntities.type(TileEntityPylonLarge::new,
                    ModEnergyNetBlocks.red_pylon_large.get()));
    public static final RegistryObject<BlockEntityType<TileEntitySubstation>> SUBSTATION =
            REG.blockEntity("substation", () -> BlockEntities.type(TileEntitySubstation::new,
                    ModEnergyNetBlocks.substation.get()));
    public static final RegistryObject<BlockEntityType<TileEntityConnector>> CONNECTOR =
            REG.blockEntity("red_connector", () -> BlockEntities.type(TileEntityConnector::new,
                    ModEnergyNetBlocks.red_connector.get()));
    public static final RegistryObject<BlockEntityType<TileEntityConnectorSuper>> CONNECTOR_SUPER =
            REG.blockEntity("red_connector_super", () -> BlockEntities.type(TileEntityConnectorSuper::new,
                    ModEnergyNetBlocks.red_connector_super.get()));

    public static final RegistryObject<BlockEntityType<TileEntityCableClassic>> RED_CABLE_CLASSIC =
            REG.blockEntity("red_cable_classic", () -> BlockEntities.type(TileEntityCableClassic::new,
                    ModEnergyNetBlocks.red_cable_classic.get()));
    public static final RegistryObject<BlockEntityType<TileEntityCablePaintable>> RED_CABLE_PAINTABLE =
            REG.blockEntity("red_cable_paintable", () -> BlockEntities.type(TileEntityCablePaintable::new,
                    ModEnergyNetBlocks.red_cable_paintable.get()));
    public static final RegistryObject<BlockEntityType<TileEntityCableGauge>> CABLE_GAUGE =
            REG.blockEntity("red_cable_gauge", () -> BlockEntities.serverTicking(TileEntityCableGauge::new,
                    TileEntityCableGauge::serverTick, ModEnergyNetBlocks.red_cable_gauge.get()));
    public static final RegistryObject<BlockEntityType<TileEntityCableSwitch>> CABLE_SWITCH =
            REG.blockEntity("cable_switch", () -> BlockEntities.type(TileEntityCableSwitch::new,
                    ModEnergyNetBlocks.cable_switch.get(), ModEnergyNetBlocks.cable_detector.get()));
    public static final RegistryObject<BlockEntityType<TileEntityDiode>> CABLE_DIODE =
            REG.blockEntity("cable_diode", () -> BlockEntities.type(TileEntityDiode::new, ModEnergyNetBlocks.cable_diode.get()));
    public static final RegistryObject<BlockEntityType<TileEntityNetworkSeparator>> NETWORK_SEPARATOR =
            REG.blockEntity("network_separator", () -> BlockEntities.serverTicking(TileEntityNetworkSeparator::new,
                    TileEntityNetworkSeparator::serverTick, ModEnergyNetBlocks.network_separator.get()));
    public static final RegistryObject<BlockEntityType<TileEntityMachineDetector>> MACHINE_DETECTOR =
            REG.blockEntity("machine_detector", () -> BlockEntities.serverTicking(TileEntityMachineDetector::new,
                    TileEntityMachineDetector::serverTick, ModEnergyNetBlocks.machine_detector.get()));

    public static final RegistryObject<BlockEntityType<TileEntityBatteryTier>> BATTERY_TIER =
            REG.blockEntity("machine_battery_tier", () -> BlockEntities.serverTicking(TileEntityBatteryTier::new,
                    TileEntityMachineBattery::serverTick,
                    ModEnergyNetBlocks.machine_battery_potato.get(), ModEnergyNetBlocks.machine_lithium_battery.get(),
                    ModEnergyNetBlocks.machine_schrabidium_battery.get(), ModEnergyNetBlocks.machine_dineutronium_battery.get()));
    public static final RegistryObject<BlockEntityType<TileEntityTesla>> TESLA =
            REG.blockEntity("tesla", () -> BlockEntities.serverTicking(TileEntityTesla::new,
                    TileEntityTesla::serverTick, ModEnergyNetBlocks.tesla.get()));

    public static void init() {}
}
