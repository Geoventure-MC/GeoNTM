package fr.geoventure.geontm.tileentity;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.registry.BlockEntities;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import fr.geoventure.geontm.blocks.ModPowerGenBlocks;
import fr.geoventure.geontm.tileentity.machine.TileEntityChungus;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineHephaestus;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineIGenerator;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineLPW2;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineSiren;

/** Feature-local block-entity types for the large power-generation machines. */
public final class ModPowerGenBlockEntities {
    private ModPowerGenBlockEntities() {}

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCK_ENTITIES);

    public static final RegistryObject<BlockEntityType<TileEntityChungus>> CHUNGUS =
            REG.blockEntity("machine_chungus", () -> BlockEntities.ticking(
                    TileEntityChungus::new, TileEntityChungus::clientTick, TileEntityChungus::serverTick,
                    ModPowerGenBlocks.MACHINE_CHUNGUS.get()));
    /** 1.7: a bare {@code TileEntity} whose only job is a render bounding box; it never ticks. */
    public static final RegistryObject<BlockEntityType<TileEntityMachineLPW2>> LPW2 =
            REG.blockEntity("machine_lpw2", () -> BlockEntities.type(
                    TileEntityMachineLPW2::new, ModPowerGenBlocks.MACHINE_LPW2.get()));
    public static final RegistryObject<BlockEntityType<TileEntityMachineHephaestus>> HEPHAESTUS =
            REG.blockEntity("machine_hephaestus", () -> BlockEntities.ticking(
                    TileEntityMachineHephaestus::new, TileEntityMachineHephaestus::clientTick,
                    TileEntityMachineHephaestus::serverTick, ModPowerGenBlocks.MACHINE_HEPHAESTUS.get()));
    /** 1.7: the whole of {@code updateEntity} is commented out ("o7"); the memorial never ticks. */
    public static final RegistryObject<BlockEntityType<TileEntityMachineIGenerator>> INDUSTRIAL_GENERATOR =
            REG.blockEntity("machine_industrial_generator", () -> BlockEntities.type(
                    TileEntityMachineIGenerator::new, ModPowerGenBlocks.MACHINE_INDUSTRIAL_GENERATOR.get()));
    public static final RegistryObject<BlockEntityType<TileEntityMachineSiren>> SIREN =
            REG.blockEntity("machine_siren", () -> BlockEntities.serverTicking(
                    TileEntityMachineSiren::new, TileEntityMachineSiren::serverTick,
                    ModPowerGenBlocks.MACHINE_SIREN.get()));

    public static void init() {}
}
