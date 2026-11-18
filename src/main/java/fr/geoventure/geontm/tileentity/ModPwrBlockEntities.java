package fr.geoventure.geontm.tileentity;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.registry.BlockEntities;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import fr.geoventure.geontm.blocks.ModPwrBlocks;
import fr.geoventure.geontm.tileentity.machine.TileEntityBlockPWR;
import fr.geoventure.geontm.tileentity.machine.TileEntityPWRController;

/** Feature-local block-entity types for the PWR: the controller and the assembled-shell cell. */
public final class ModPwrBlockEntities {
    private ModPwrBlockEntities() {}

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCK_ENTITIES);

    public static final RegistryObject<BlockEntityType<TileEntityPWRController>> PWR_CONTROLLER =
            REG.blockEntity("tileentity_pwr_controller", () -> BlockEntities.ticking(
                    TileEntityPWRController::new,
                    TileEntityPWRController::clientTick,
                    TileEntityPWRController::serverTick,
                    ModPwrBlocks.PWR_CONTROLLER.get()));
    public static final RegistryObject<BlockEntityType<TileEntityBlockPWR>> PWR_BLOCK =
            REG.blockEntity("tileentity_block_pwr", () -> BlockEntities.serverTicking(
                    TileEntityBlockPWR::new,
                    TileEntityBlockPWR::serverTick,
                    ModPwrBlocks.PWR_BLOCK.get()));

    public static void init() {}
}
