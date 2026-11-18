package fr.geoventure.geontm.tileentity;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.blocks.ModCustomMissileBlocks;
import fr.geoventure.geontm.tileentity.bomb.TileEntityCompactLauncher;
import fr.geoventure.geontm.tileentity.bomb.TileEntityCustomMissileLauncher;
import fr.geoventure.geontm.tileentity.bomb.TileEntityLaunchPadLarge;
import fr.geoventure.geontm.tileentity.bomb.TileEntityLaunchPadRusted;
import fr.geoventure.geontm.tileentity.bomb.TileEntityLaunchTable;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineMissileAssembly;
import fr.geoventure.geontm.tileentity.machine.TileEntityMultiblock;
import fr.geoventure.geocore.registry.BlockEntities;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

/** Block-entity types of the custom missile system and the two extra launch pads. */
public final class ModCustomMissileBlockEntities {

    private ModCustomMissileBlockEntities() {}

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCK_ENTITIES);

    public static final RegistryObject<BlockEntityType<TileEntityMultiblock>> STRUCT_LAUNCHER_CORE =
            REG.blockEntity("struct_launcher_core", () -> BlockEntities.serverTicking(TileEntityMultiblock::new,
                    TileEntityMultiblock::serverTick, ModCustomMissileBlocks.struct_launcher_core.get()));
    public static final RegistryObject<BlockEntityType<TileEntityMultiblock>> STRUCT_LAUNCHER_CORE_LARGE =
            REG.blockEntity("struct_launcher_core_large", () -> BlockEntities.serverTicking(TileEntityMultiblock::new,
                    TileEntityMultiblock::serverTick, ModCustomMissileBlocks.struct_launcher_core_large.get()));

    public static final RegistryObject<BlockEntityType<TileEntityMachineMissileAssembly>> MACHINE_MISSILE_ASSEMBLY =
            REG.blockEntity("machine_missile_assembly", () -> BlockEntities.serverTicking(
                    TileEntityMachineMissileAssembly::new, TileEntityMachineMissileAssembly::serverTick,
                    ModCustomMissileBlocks.machine_missile_assembly.get()));
    public static final RegistryObject<BlockEntityType<TileEntityCompactLauncher>> COMPACT_LAUNCHER =
            REG.blockEntity("compact_launcher", () -> BlockEntities.ticking(
                    TileEntityCompactLauncher::new, TileEntityCustomMissileLauncher::clientTick,
                    TileEntityCustomMissileLauncher::serverTick,
                    ModCustomMissileBlocks.compact_launcher.get()));
    public static final RegistryObject<BlockEntityType<TileEntityLaunchTable>> LAUNCH_TABLE =
            REG.blockEntity("launch_table", () -> BlockEntities.ticking(
                    TileEntityLaunchTable::new, TileEntityCustomMissileLauncher::clientTick,
                    TileEntityCustomMissileLauncher::serverTick,
                    ModCustomMissileBlocks.launch_table.get()));
    public static final RegistryObject<BlockEntityType<TileEntityLaunchPadLarge>> LAUNCH_PAD_LARGE =
            REG.blockEntity("launch_pad_large", () -> BlockEntities.ticking(
                    TileEntityLaunchPadLarge::new, TileEntityLaunchPadLarge::clientTick,
                    TileEntityLaunchPadLarge::serverTick, ModCustomMissileBlocks.launch_pad_large.get()));
    public static final RegistryObject<BlockEntityType<TileEntityLaunchPadRusted>> LAUNCH_PAD_RUSTED =
            REG.blockEntity("launch_pad_rusted", () -> BlockEntities.serverTicking(
                    TileEntityLaunchPadRusted::new, TileEntityLaunchPadRusted::serverTick,
                    ModCustomMissileBlocks.launch_pad_rusted.get()));

    public static void init() {}
}
