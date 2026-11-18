package fr.geoventure.geontm.tileentity;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.blocks.machine.ModMachineBlocks;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineRTG;
import fr.geoventure.geocore.registry.BlockEntities;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

/** Feature-local block-entity registration for the grey RTG. */
public final class ModRtgBlockEntities {
    private ModRtgBlockEntities() {}

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCK_ENTITIES);

    public static final RegistryObject<BlockEntityType<TileEntityMachineRTG>> MACHINE_RTG =
            REG.blockEntity("machine_rtg_grey", () -> BlockEntities.serverTicking(TileEntityMachineRTG::new, TileEntityMachineRTG::serverTick, ModMachineBlocks.machine_rtg_grey.get()));

    public static void init() {}
}
