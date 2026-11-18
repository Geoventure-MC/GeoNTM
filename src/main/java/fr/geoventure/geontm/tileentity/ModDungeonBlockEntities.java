package fr.geoventure.geontm.tileentity;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.blocks.ModDungeonBlocks;
import fr.geoventure.geontm.blocks.generic.BlockLoot;
import fr.geoventure.geontm.blocks.generic.BlockPedestal;
import fr.geoventure.geontm.blocks.generic.BlockRebar;
import fr.geoventure.geontm.blocks.generic.BlockSkeletonHolder;
import fr.geoventure.geontm.blocks.generic.DungeonSpawner;
import fr.geoventure.geontm.blocks.generic.LogicBlock;
import fr.geoventure.geontm.blocks.generic.TrappedBrick;
import fr.geoventure.geontm.tileentity.deco.TileEntityLanternBehemoth;
import fr.geoventure.geontm.tileentity.deco.TileEntityTrappedBrick;
import fr.geoventure.geocore.registry.BlockEntities;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

/** Block-entity types of the dungeon family, under 1.7 {@code TileMappings}' names. */
public final class ModDungeonBlockEntities {

    private ModDungeonBlockEntities() {}

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCK_ENTITIES);

    /** Only the detector traps carry one (1.7 {@code createNewTileEntity}). */
    public static final RegistryObject<BlockEntityType<TileEntityTrappedBrick>> TRAPPED_BRICK =
            REG.blockEntity("tileentity_trapped_brick", () -> BlockEntities.serverTicking(
                    TileEntityTrappedBrick::new, TileEntityTrappedBrick::serverTick,
                    ModDungeonBlocks.TRAPS.stream().map(RegistryObject::get)
                            .filter(b -> b instanceof TrappedBrick.Detector).toArray(Block[]::new)));
    public static final RegistryObject<BlockEntityType<BlockPedestal.TileEntityPedestal>> PEDESTAL =
            REG.blockEntity("tileentity_ntm_pedestal", () -> BlockEntities.serverTicking(
                    BlockPedestal.TileEntityPedestal::new, BlockPedestal.TileEntityPedestal::serverTick,
                    ModDungeonBlocks.pedestal.get()));
    public static final RegistryObject<BlockEntityType<BlockSkeletonHolder.TileEntitySkeletonHolder>> SKELETON_HOLDER =
            REG.blockEntity("tileentity_ntm_skeleton", () -> BlockEntities.type(
                    BlockSkeletonHolder.TileEntitySkeletonHolder::new, ModDungeonBlocks.skeleton_holder.get()));
    public static final RegistryObject<BlockEntityType<BlockLoot.TileEntityLoot>> LOOT =
            REG.blockEntity("tileentity_ntm_loot", () -> BlockEntities.type(
                    BlockLoot.TileEntityLoot::new, ModDungeonBlocks.deco_loot.get()));
    public static final RegistryObject<BlockEntityType<TileEntityLanternBehemoth>> LANTERN_BEHEMOTH =
            REG.blockEntity("tileentity_lantern_behemoth", () -> BlockEntities.serverTicking(
                    TileEntityLanternBehemoth::new, TileEntityLanternBehemoth::serverTick,
                    ModDungeonBlocks.lantern_behemoth.get()));
    public static final RegistryObject<BlockEntityType<DungeonSpawner.TileEntityDungeonSpawner>> DUNGEON_SPAWNER =
            REG.blockEntity("tileentity_ntm_dungeon_spawner", () -> BlockEntities.serverTicking(
                    DungeonSpawner.TileEntityDungeonSpawner::new, DungeonSpawner.TileEntityDungeonSpawner::serverTick,
                    ModDungeonBlocks.dungeon_spawner.get()));
    public static final RegistryObject<BlockEntityType<LogicBlock.TileEntityLogicBlock>> LOGIC_BLOCK =
            REG.blockEntity("tileentity_ntm_logic_block", () -> BlockEntities.serverTicking(
                    LogicBlock.TileEntityLogicBlock::new, LogicBlock.TileEntityLogicBlock::serverTick,
                    ModDungeonBlocks.logic_block.get(), ModDungeonBlocks.logic_block_invis.get()));
    public static final RegistryObject<BlockEntityType<BlockRebar.TileEntityRebar>> REBAR =
            REG.blockEntity("tileentity_rebar", () -> BlockEntities.serverTicking(
                    BlockRebar.TileEntityRebar::new, BlockRebar.TileEntityRebar::serverTick,
                    ModDungeonBlocks.rebar.get()));

    public static void init() {}
}
