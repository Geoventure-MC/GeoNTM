package fr.geoventure.geontm.tileentity;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.registry.BlockEntities;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import fr.geoventure.geontm.blocks.ModProductionBlocks;
import fr.geoventure.geontm.tileentity.machine.TileEntityConveyorPress;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineAutocrafter;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineRotaryFurnace;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineThresher;
import fr.geoventure.geontm.tileentity.machine.TileEntityMicrowave;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineFunnel;

/** Feature-local block-entity types for the production machines. */
public final class ModProductionBlockEntities {
    private ModProductionBlockEntities() {}

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCK_ENTITIES);

    public static final RegistryObject<BlockEntityType<TileEntityMachineRotaryFurnace>> ROTARY_FURNACE =
            REG.blockEntity("machine_rotary_furnace", () -> BlockEntities.ticking(
                    TileEntityMachineRotaryFurnace::new, TileEntityMachineRotaryFurnace::clientTick,
                    TileEntityMachineRotaryFurnace::serverTick, ModProductionBlocks.MACHINE_ROTARY_FURNACE.get()));
    public static final RegistryObject<BlockEntityType<TileEntityMachineThresher>> THRESHER =
            REG.blockEntity("machine_thresher", () -> BlockEntities.ticking(
                    TileEntityMachineThresher::new, TileEntityMachineThresher::clientTick,
                    TileEntityMachineThresher::serverTick, ModProductionBlocks.MACHINE_THRESHER.get()));
    public static final RegistryObject<BlockEntityType<TileEntityMicrowave>> MICROWAVE =
            REG.blockEntity("machine_microwave", () -> BlockEntities.serverTicking(
                    TileEntityMicrowave::new, (level, pos, state, be) -> TileEntityMicrowave.serverTick(level, pos, state, be),
                    ModProductionBlocks.MACHINE_MICROWAVE.get()));
    public static final RegistryObject<BlockEntityType<TileEntityMachineAutocrafter>> AUTOCRAFTER =
            REG.blockEntity("machine_autocrafter", () -> BlockEntities.serverTicking(
                    TileEntityMachineAutocrafter::new,
                    (level, pos, state, be) -> TileEntityMachineAutocrafter.serverTick(level, pos, state, be),
                    ModProductionBlocks.MACHINE_AUTOCRAFTER.get()));
    public static final RegistryObject<BlockEntityType<TileEntityConveyorPress>> CONVEYOR_PRESS =
            REG.blockEntity("machine_conveyor_press", () -> BlockEntities.ticking(
                    TileEntityConveyorPress::new, TileEntityConveyorPress::clientTick,
                    TileEntityConveyorPress::serverTick, ModProductionBlocks.MACHINE_CONVEYOR_PRESS.get()));
    public static final RegistryObject<BlockEntityType<TileEntityMachineFunnel>> FUNNEL =
            REG.blockEntity("machine_funnel", () -> BlockEntities.serverTicking(
                    TileEntityMachineFunnel::new, (level, pos, state, be) -> TileEntityMachineFunnel.serverTick(level, pos, state, be),
                    ModProductionBlocks.MACHINE_FUNNEL.get()));

    public static void init() {}
}
