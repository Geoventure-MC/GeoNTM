package fr.geoventure.geontm.tileentity;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.blocks.ModSatelliteBlocks;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineSatDock;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineSatLinker;
import fr.geoventure.geocore.registry.BlockEntities;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

/** Block-entity types of the satellite family; 1.7 {@code TileMappings} "tileentity_satlinker"/"tileentity_miner_dock". */
public final class ModSatelliteBlockEntities {

    private ModSatelliteBlockEntities() {}

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCK_ENTITIES);

    public static final RegistryObject<BlockEntityType<TileEntityMachineSatLinker>> MACHINE_SATLINKER =
            REG.blockEntity("machine_satlinker", () -> BlockEntities.serverTicking(
                    TileEntityMachineSatLinker::new, TileEntityMachineSatLinker::serverTick,
                    ModSatelliteBlocks.machine_satlinker.get()));
    public static final RegistryObject<BlockEntityType<TileEntityMachineSatDock>> SAT_DOCK =
            REG.blockEntity("sat_dock", () -> BlockEntities.serverTicking(
                    TileEntityMachineSatDock::new, TileEntityMachineSatDock::serverTick,
                    ModSatelliteBlocks.sat_dock.get()));

    public static void init() {}
}
