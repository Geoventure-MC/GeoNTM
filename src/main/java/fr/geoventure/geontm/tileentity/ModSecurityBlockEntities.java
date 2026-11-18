package fr.geoventure.geontm.tileentity;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.registry.BlockEntities;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import fr.geoventure.geontm.blocks.ModSecurityBlocks;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineKeyForge;

/** Feature-local block-entity types: the locksmith table. */
public final class ModSecurityBlockEntities {
    private ModSecurityBlockEntities() {}

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCK_ENTITIES);

    public static final RegistryObject<BlockEntityType<TileEntityMachineKeyForge>> MACHINE_KEYFORGE =
            REG.blockEntity("machine_keyforge", () -> BlockEntities.serverTicking(
                    TileEntityMachineKeyForge::new, TileEntityMachineKeyForge::serverTick,
                    ModSecurityBlocks.machine_keyforge.get()));

    public static void init() {}
}
