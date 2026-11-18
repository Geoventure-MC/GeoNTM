package fr.geoventure.geontm.tileentity;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.blocks.ModCustomMachineBlocks;
import fr.geoventure.geontm.tileentity.machine.TileEntityCMPort;
import fr.geoventure.geontm.tileentity.machine.TileEntityCustomMachine;
import fr.geoventure.geocore.registry.BlockEntities;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import fr.geoventure.geontm.registration.Reg;

/** Block-entity types of the custom machine system: the controller (1.7 {@code tileentity_custom_machine}) and the port proxy. */
public final class ModCustomMachineBlockEntities {

    private ModCustomMachineBlockEntities() {}

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCK_ENTITIES);

    public static final RegistryObject<BlockEntityType<TileEntityCustomMachine>> CUSTOM_MACHINE =
            REG.blockEntity("custom_machine", () -> BlockEntities.serverTicking(TileEntityCustomMachine::new,
                    TileEntityCustomMachine::serverTick, ModCustomMachineBlocks.custom_machine.get()));
    public static final RegistryObject<BlockEntityType<TileEntityCMPort>> CM_PORT =
            REG.blockEntity("cm_port", () -> BlockEntities.type(TileEntityCMPort::new,
                    ModCustomMachineBlocks.cm_port.stream().map(RegistryObject::get).toArray(Block[]::new)));

    public static void init() {}
}
