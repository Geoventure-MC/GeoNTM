package fr.geoventure.geontm.tileentity;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.registry.BlockEntities;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import fr.geoventure.geontm.blocks.ModPneumaticBlocks;
import fr.geoventure.geontm.tileentity.network.TileEntityPneumoTubePaintable;
import fr.geoventure.geontm.tileentity.network.pneumatic.TileEntityPneumoStorageAccess;
import fr.geoventure.geontm.tileentity.network.pneumatic.TileEntityPneumoStorageClutter;
import fr.geoventure.geontm.tileentity.network.pneumatic.TileEntityPneumoStorageExporter;
import fr.geoventure.geontm.tileentity.network.pneumatic.TileEntityPneumoStorageImporter;
import fr.geoventure.geontm.tileentity.network.pneumatic.TileEntityPneumoStorageMono;
import fr.geoventure.geontm.tileentity.network.pneumatic.TileEntityPneumaticPipeAnchor;
import fr.geoventure.geontm.tileentity.network.pneumatic.TileEntityPneumoTube;

/** Feature-local block-entity types for the pneumatic tube storage system; every one ticks on the server only. */
public final class ModPneumaticBlockEntities {
    private ModPneumaticBlockEntities() {}

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCK_ENTITIES);

    public static final RegistryObject<BlockEntityType<TileEntityPneumoTube>> PNEUMATIC_TUBE =
            REG.blockEntity("pneumatic_tube", () -> BlockEntities.serverTicking(
                    TileEntityPneumoTube::new,
                    (level, pos, state, be) -> be.serverTick(), ModPneumaticBlocks.PNEUMATIC_TUBE.get()));
    public static final RegistryObject<BlockEntityType<TileEntityPneumoTubePaintable>> PNEUMATIC_TUBE_PAINTABLE =
            REG.blockEntity("pneumatic_tube_paintable", () -> BlockEntities.serverTicking(
                    TileEntityPneumoTubePaintable::new,
                    (level, pos, state, be) -> be.serverTick(), ModPneumaticBlocks.PNEUMATIC_TUBE_PAINTABLE.get()));
    public static final RegistryObject<BlockEntityType<TileEntityPneumoStorageAccess>> PNEUMATIC_STORAGE_ACCESS =
            REG.blockEntity("pneumatic_storage_access", () -> BlockEntities.serverTicking(
                    TileEntityPneumoStorageAccess::new,
                    (level, pos, state, be) -> be.serverTick(), ModPneumaticBlocks.PNEUMATIC_STORAGE_ACCESS.get()));
    public static final RegistryObject<BlockEntityType<TileEntityPneumoStorageClutter>> PNEUMATIC_STORAGE_CLUTTER =
            REG.blockEntity("pneumatic_storage_clutter", () -> BlockEntities.serverTicking(
                    TileEntityPneumoStorageClutter::new,
                    (level, pos, state, be) -> be.serverTick(), ModPneumaticBlocks.PNEUMATIC_STORAGE_CLUTTER.get()));
    public static final RegistryObject<BlockEntityType<TileEntityPneumoStorageMono>> PNEUMATIC_STORAGE_MONO =
            REG.blockEntity("pneumatic_storage_mono", () -> BlockEntities.serverTicking(
                    TileEntityPneumoStorageMono::new,
                    (level, pos, state, be) -> be.serverTick(), ModPneumaticBlocks.PNEUMATIC_STORAGE_MONO.get()));
    public static final RegistryObject<BlockEntityType<TileEntityPneumoStorageImporter>> PNEUMATIC_STORAGE_IMPORTER =
            REG.blockEntity("pneumatic_storage_importer", () -> BlockEntities.serverTicking(
                    TileEntityPneumoStorageImporter::new,
                    (level, pos, state, be) -> be.serverTick(), ModPneumaticBlocks.PNEUMATIC_STORAGE_IMPORTER.get()));
    public static final RegistryObject<BlockEntityType<TileEntityPneumoStorageExporter>> PNEUMATIC_STORAGE_EXPORTER =
            REG.blockEntity("pneumatic_storage_exporter", () -> BlockEntities.serverTicking(
                    TileEntityPneumoStorageExporter::new,
                    (level, pos, state, be) -> be.serverTick(), ModPneumaticBlocks.PNEUMATIC_STORAGE_EXPORTER.get()));

    public static final RegistryObject<BlockEntityType<TileEntityPneumaticPipeAnchor>> PNEUMATIC_PIPE_ANCHOR =
            REG.blockEntity("pipe_anchor_pneumatic", () -> BlockEntities.type(
                    TileEntityPneumaticPipeAnchor::new, ModPneumaticBlocks.PIPE_ANCHOR_PNEUMATIC.get()));

    public static void init() {}
}
