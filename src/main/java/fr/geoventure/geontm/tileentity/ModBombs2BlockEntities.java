package fr.geoventure.geontm.tileentity;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.registry.BlockEntities;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import fr.geoventure.geontm.blocks.ModBombs2Blocks;
import fr.geoventure.geontm.tileentity.bomb.TileEntityNukeBalefire;
import fr.geoventure.geontm.tileentity.bomb.TileEntityNukeCustom;
import fr.geoventure.geontm.tileentity.bomb.TileEntityNukeFleija;
import fr.geoventure.geontm.tileentity.bomb.TileEntityNukePrototype;
import fr.geoventure.geontm.tileentity.bomb.TileEntityNukeSolinium;

/** Block-entity types for the special bombs. The custom nuke and the balefire bomb tick on the server. */
public final class ModBombs2BlockEntities {

    private ModBombs2BlockEntities() {}

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCK_ENTITIES);

    public static final RegistryObject<BlockEntityType<TileEntityNukeCustom>> NUKE_CUSTOM =
            REG.blockEntity("nuke_custom", () -> BlockEntities.serverTicking(
                    TileEntityNukeCustom::new, TileEntityNukeCustom::serverTick, ModBombs2Blocks.nuke_custom.get()));
    public static final RegistryObject<BlockEntityType<TileEntityNukeFleija>> NUKE_FLEIJA =
            REG.blockEntity("nuke_fleija",
                    () -> BlockEntities.type(TileEntityNukeFleija::new, ModBombs2Blocks.nuke_fleija.get()));
    public static final RegistryObject<BlockEntityType<TileEntityNukeSolinium>> NUKE_SOLINIUM =
            REG.blockEntity("nuke_solinium",
                    () -> BlockEntities.type(TileEntityNukeSolinium::new, ModBombs2Blocks.nuke_solinium.get()));
    public static final RegistryObject<BlockEntityType<TileEntityNukePrototype>> NUKE_PROTOTYPE =
            REG.blockEntity("nuke_prototype",
                    () -> BlockEntities.type(TileEntityNukePrototype::new, ModBombs2Blocks.nuke_prototype.get()));
    public static final RegistryObject<BlockEntityType<TileEntityNukeBalefire>> NUKE_FSTBMB =
            REG.blockEntity("nuke_fstbmb", () -> BlockEntities.serverTicking(
                    TileEntityNukeBalefire::new, TileEntityNukeBalefire::serverTick, ModBombs2Blocks.nuke_fstbmb.get()));

    public static void init() {}
}
