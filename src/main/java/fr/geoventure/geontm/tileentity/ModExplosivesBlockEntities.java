package fr.geoventure.geontm.tileentity;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.registry.BlockEntities;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import fr.geoventure.geontm.blocks.ModExplosivesBlocks;
import fr.geoventure.geontm.tileentity.bomb.TileEntityBombMulti;
import fr.geoventure.geontm.tileentity.bomb.TileEntityCharge;
import fr.geoventure.geontm.tileentity.bomb.TileEntityCrashedBomb;
import fr.geoventure.geontm.tileentity.bomb.TileEntityLandmine;

/** Feature-local block-entity types for the conventional explosives. */
public final class ModExplosivesBlockEntities {
    private ModExplosivesBlockEntities() {}

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCK_ENTITIES);

    public static final RegistryObject<BlockEntityType<TileEntityLandmine>> LANDMINE =
            REG.blockEntity("landmine", () -> BlockEntities.serverTicking(TileEntityLandmine::new,
                    TileEntityLandmine::serverTick,
                    ModExplosivesBlocks.mine_ap.get(), ModExplosivesBlocks.mine_he.get(), ModExplosivesBlocks.mine_shrap.get(),
                    ModExplosivesBlocks.mine_fat.get(), ModExplosivesBlocks.mine_naval.get()));

    public static final RegistryObject<BlockEntityType<TileEntityCharge>> CHARGE =
            REG.blockEntity("charge", () -> BlockEntities.serverTicking(TileEntityCharge::new,
                    TileEntityCharge::serverTick,
                    ModExplosivesBlocks.charge_dynamite.get(), ModExplosivesBlocks.charge_miner.get(),
                    ModExplosivesBlocks.charge_c4.get(), ModExplosivesBlocks.charge_semtex.get()));

    public static final RegistryObject<BlockEntityType<TileEntityCrashedBomb>> CRASHED_BOMB =
            REG.blockEntity("crashed_bomb", () -> BlockEntities.serverTicking(TileEntityCrashedBomb::new,
                    TileEntityCrashedBomb::serverTick,
                    ModExplosivesBlocks.crashed_bomb_balefire.get(), ModExplosivesBlocks.crashed_bomb_conventional.get(),
                    ModExplosivesBlocks.crashed_bomb_nuke.get(), ModExplosivesBlocks.crashed_bomb_salted.get()));

    public static final RegistryObject<BlockEntityType<TileEntityBombMulti>> BOMB_MULTI =
            REG.blockEntity("bomb_multi", () -> BlockEntities.type(TileEntityBombMulti::new,
                    ModExplosivesBlocks.bomb_multi.get()));

    public static void init() {}
}
