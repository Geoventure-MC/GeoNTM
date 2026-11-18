package fr.geoventure.geontm.tileentity;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.blocks.ModArmorSetBBlocks;
import fr.geoventure.geontm.tileentity.deco.TileEntityDecoBlockAltF;
import fr.geoventure.geocore.registry.BlockEntities;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

/** Block-entity type of the armour set B block, the {@code statue_elb_f} statue. */
public final class ModArmorSetBBlockEntities {

    private ModArmorSetBBlockEntities() {}

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCK_ENTITIES);

    /** 1.7 updateEntity ran on both sides; the aura only matters on the server. */
    public static final RegistryObject<BlockEntityType<TileEntityDecoBlockAltF>> STATUE_ELB_F =
            REG.blockEntity("statue_elb_f", () -> BlockEntities.serverTicking(TileEntityDecoBlockAltF::new,
                    TileEntityDecoBlockAltF::tick, ModArmorSetBBlocks.statue_elb_f.get()));

    public static void init() {}
}
