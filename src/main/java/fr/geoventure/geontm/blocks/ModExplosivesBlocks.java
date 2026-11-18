package fr.geoventure.geontm.blocks;

import fr.geoventure.geocore.dejson.Variants;
import fr.geoventure.geocore.dejson.Slots;
import fr.geoventure.geocore.dejson.Models;
import fr.geoventure.geocore.dejson.Dispatch;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geocore.registry.Preset;
import fr.geoventure.geocore.registry.Loot;
import fr.geoventure.geocore.registry.BlockTag;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import fr.geoventure.geocore.world.Compat;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import fr.geoventure.geontm.blocks.bomb.BlockChargeBase;
import fr.geoventure.geontm.blocks.bomb.BlockChargeC4;
import fr.geoventure.geontm.blocks.bomb.BlockChargeDynamite;
import fr.geoventure.geontm.blocks.bomb.BlockChargeMiner;
import fr.geoventure.geontm.blocks.bomb.BlockChargeSemtex;
import fr.geoventure.geontm.blocks.bomb.BlockCrashedBomb;
import fr.geoventure.geontm.blocks.bomb.BlockFissureBomb;
import fr.geoventure.geontm.blocks.bomb.BlockPlasticExplosive;
import fr.geoventure.geontm.blocks.bomb.BombMulti;
import fr.geoventure.geontm.blocks.bomb.DetCord;
import fr.geoventure.geontm.blocks.bomb.DetMiner;
import fr.geoventure.geontm.blocks.bomb.ExplosiveCharge;
import fr.geoventure.geontm.blocks.bomb.ItemNukeBlock;
import fr.geoventure.geontm.blocks.bomb.Landmine;
import fr.geoventure.geontm.explosion.BlastInfo;
import fr.geoventure.geontm.items.block.ExplosivesBlockItem;

/**
 * Blocks of the conventional-explosives family: landmines, timed charges, redstone-fired det blocks,
 * the solid plastic-explosive blocks, the fissure bomb, the crashed duds and the multi purpose bomb.
 * Properties follow the 1.7 {@code ModBlocks} lines: {@code Material.iron} blocks require a pickaxe for
 * their drop (and are in the mineable tag), {@code Material.tnt} ones do not.
 */
public final class ModExplosivesBlocks {
    private ModExplosivesBlocks() {}

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCKS, ITEMS);

    /** Landmines and crashed duds: renderer-drawn, never drop (breaking a mine sets it off). */
    private static final Preset RENDERED_NO_DROP = Presets.rendered(Variants.simple())
            .and(handle -> handle.loot(Loot.nothing()));
    /** Timed charges: renderer-drawn, six-way facing, only the defuser yields the item back. */
    private static final Dispatch SIX_WAY = Variants.uniform("facing", "down", "up", "north", "south", "west", "east");
    private static final Preset CHARGE = Presets.rendered(SIX_WAY).and(handle -> handle.loot(Loot.nothing()));

    // --- landmines: Material.iron, hardness 1.0 (resistance 5), no drop ---
    public static final RegistryObject<Landmine> mine_ap = REG.block("mine_ap",
            p -> new Landmine(p, Landmine.Type.AP, 1.5D, 1D), ModExplosivesBlocks::mine).transform(RENDERED_NO_DROP).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Landmine> mine_he = REG.block("mine_he",
            p -> new Landmine(p, Landmine.Type.HE, 2D, 5D), ModExplosivesBlocks::mine).transform(RENDERED_NO_DROP).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Landmine> mine_shrap = REG.block("mine_shrap",
            p -> new Landmine(p, Landmine.Type.SHRAP, 1.5D, 1D), ModExplosivesBlocks::mine).transform(RENDERED_NO_DROP).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Landmine> mine_fat = REG.block("mine_fat",
            p -> new Landmine(p, Landmine.Type.FAT, 2.5D, 1D), ModExplosivesBlocks::mine).transform(RENDERED_NO_DROP).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Landmine> mine_naval = REG.block("mine_naval",
            p -> new Landmine(p, Landmine.Type.NAVAL, 2.5D, 1D), ModExplosivesBlocks::mine).transform(RENDERED_NO_DROP).tag(BlockTag.PICKAXE);

    // --- timed charges: Material.tnt, hardness 0, resistance 1, no drop ---
    public static final RegistryObject<BlockChargeDynamite> charge_dynamite = REG.block("charge_dynamite",
            BlockChargeDynamite::new, ModExplosivesBlocks::charge).transform(CHARGE);
    public static final RegistryObject<BlockChargeMiner> charge_miner = REG.block("charge_miner",
            BlockChargeMiner::new, ModExplosivesBlocks::charge).transform(CHARGE);
    public static final RegistryObject<BlockChargeC4> charge_c4 = REG.block("charge_c4",
            BlockChargeC4::new, ModExplosivesBlocks::charge).transform(CHARGE);
    public static final RegistryObject<BlockChargeSemtex> charge_semtex = REG.block("charge_semtex",
            BlockChargeSemtex::new, ModExplosivesBlocks::charge).transform(CHARGE);

    // --- redstone-fired det blocks: Material.iron, hardness 0.1, resistance 0 ---
    /** Renderer-drawn cable with six connection flags. */
    public static final RegistryObject<DetCord> det_cord = REG.block("det_cord", DetCord::new,
                    p -> det(p).noOcclusion())
            .transform(Presets.rendered(Variants.product(
                    Variants.uniform("down", "false", "true"), Variants.uniform("up", "false", "true"),
                    Variants.uniform("north", "false", "true"), Variants.uniform("south", "false", "true"),
                    Variants.uniform("west", "false", "true"), Variants.uniform("east", "false", "true"))))
            .loot(Loot.self())
            .tag(BlockTag.PICKAXE);
    // The plain det blocks: 1.7 icons (det_nuke and det_miner carry a top icon on up/down).
    public static final RegistryObject<ExplosiveCharge> det_charge = REG.block("det_charge",
                    p -> new ExplosiveCharge(p, ExplosiveCharge.Kind.CHARGE), ModExplosivesBlocks::det)
            .transform(Presets.cubeAll())
            .loot(Loot.self())
            .tag(BlockTag.PICKAXE);
    public static final RegistryObject<ExplosiveCharge> det_nuke = REG.block("det_nuke",
                    p -> new ExplosiveCharge(p, ExplosiveCharge.Kind.NUKE), ModExplosivesBlocks::det)
            .transform(Presets.column("geontm:blocks/det_nuke_top", "geontm:blocks/det_nuke"))
            .loot(Loot.self())
            .tag(BlockTag.PICKAXE);
    /** 1.7 {@code DetMiner.getItemDropped} returns null. */
    public static final RegistryObject<DetMiner> det_miner = REG.block("det_miner", DetMiner::new,
                    ModExplosivesBlocks::det)
            .transform(Presets.column("geontm:blocks/det_miner_top", "geontm:blocks/det_miner_side"))
            .loot(Loot.silkTouchOnly()) // 1.7: drops nothing, but a plain cube without a tile entity, so silk touch keeps it
            .tag(BlockTag.PICKAXE);

    // --- solid plastic explosive: Material.tnt, metal sound, hardness 2, resistance 2 ---
    public static final RegistryObject<BlockPlasticExplosive> block_semtex = REG.block("block_semtex",
                    BlockPlasticExplosive::new, p -> p.strength(2.0F, 1.2F).sound(SoundType.METAL))
            .transform(plastic("geontm:blocks/block_semtex"))
            .loot(Loot.self());
    public static final RegistryObject<BlockPlasticExplosive> block_c4 = REG.block("block_c4",
                    BlockPlasticExplosive::new, p -> p.strength(2.0F, 1.2F).sound(SoundType.METAL))
            .transform(plastic("geontm:blocks/block_c4"))
            .loot(Loot.self());

    // --- fissure bomb: BlockTNTBase, hardness 0, grass sound; TNT-family block with the armed flag ---
    public static final RegistryObject<BlockFissureBomb> fissure_bomb = REG.block("fissure_bomb",
                    BlockFissureBomb::new, p -> p.strength(0.0F).sound(SoundType.GRASS).ignitedByLava())
            .transform(Presets.cubeBottomTop("geontm:blocks/fissure_bomb_bottom", "geontm:blocks/fissure_bomb_side",
                    "geontm:blocks/fissure_bomb_top").states(Variants.uniform("armed", "false", "true")))
            .loot(Loot.self());

    // --- crashed duds: Material.iron, unbreakable, resistance 6000; the 1.7 metadata enum flattened.
    // Unbreakable: the defuser drops the payload instead. ---
    public static final RegistryObject<BlockCrashedBomb> crashed_bomb_balefire = REG.block("crashed_bomb_balefire",
            p -> new BlockCrashedBomb(p, BlockCrashedBomb.EnumDudType.BALEFIRE), ModExplosivesBlocks::dud)
            .transform(RENDERED_NO_DROP);
    public static final RegistryObject<BlockCrashedBomb> crashed_bomb_conventional = REG.block("crashed_bomb_conventional",
            p -> new BlockCrashedBomb(p, BlockCrashedBomb.EnumDudType.CONVENTIONAL), ModExplosivesBlocks::dud)
            .transform(RENDERED_NO_DROP);
    public static final RegistryObject<BlockCrashedBomb> crashed_bomb_nuke = REG.block("crashed_bomb_nuke",
            p -> new BlockCrashedBomb(p, BlockCrashedBomb.EnumDudType.NUKE), ModExplosivesBlocks::dud)
            .transform(RENDERED_NO_DROP);
    public static final RegistryObject<BlockCrashedBomb> crashed_bomb_salted = REG.block("crashed_bomb_salted",
            p -> new BlockCrashedBomb(p, BlockCrashedBomb.EnumDudType.SALTED), ModExplosivesBlocks::dud)
            .transform(RENDERED_NO_DROP);

    // --- multi purpose bomb: Material.iron, resistance 200 (hardness left at 0 upstream); renderer-drawn,
    // 1.7 facing metadata 2..5 ---
    public static final RegistryObject<BombMulti> bomb_multi = REG.block("bomb_multi", BombMulti::new,
                    p -> p.strength(0.0F, 120.0F).requiresCorrectToolForDrops().noOcclusion())
            .transform(Presets.rendered(Variants.uniformRange("facing", 2, 5)))
            .loot(Loot.self())
            .tag(BlockTag.PICKAXE);

    private static BlockBehaviour.Properties mine(BlockBehaviour.Properties p) {
        return p.strength(1.0F).noOcclusion().noLootTable();
    }

    private static BlockBehaviour.Properties charge(BlockBehaviour.Properties p) {
        return p.strength(0.0F, 0.6F).noOcclusion().noLootTable();
    }

    private static BlockBehaviour.Properties det(BlockBehaviour.Properties p) {
        return p.strength(0.1F, 0.0F).requiresCorrectToolForDrops();
    }

    private static BlockBehaviour.Properties dud(BlockBehaviour.Properties p) {
        return p.strength(-1.0F, 6000.0F).noOcclusion().noLootTable();
    }

    /**
     * Solid plastic explosive: the {@code _front} icon on the face opposite the piston-style orientation
     * (1.12.2 block_semtex.json: front on south, facing=north unrotated).
     */
    private static Presets.BlockModel plastic(String side) {
        return Presets.shape(Models.parented("minecraft:block/cube", Slots.of("particle", side)
                        .and("down", side).and("up", side).and("north", side).and("south", side + "_front")
                        .and("west", side).and("east", side)))
                .states(Variants.facingAll());
    }

    static {
        REG.blockItem(mine_ap).tab(GeoNtmTab.BOMBS);
        REG.blockItem(mine_he).tab(GeoNtmTab.BOMBS);
        REG.blockItem(mine_shrap).tab(GeoNtmTab.BOMBS);
        REG.blockItem(mine_fat, (b, p) -> new ItemNukeBlock(b, p, BlastInfo.FAT_MINE), Compat::blockDescription)
                .tab(GeoNtmTab.BOMBS);
        REG.blockItem(mine_naval).tab(GeoNtmTab.BOMBS);
        chargeItem(charge_dynamite);
        chargeItem(charge_miner);
        chargeItem(charge_c4);
        chargeItem(charge_semtex);
        REG.blockItem(det_cord).tab(GeoNtmTab.BOMBS);
        REG.blockItem(det_charge).tab(GeoNtmTab.BOMBS);
        REG.blockItem(det_nuke, (b, p) -> new ItemNukeBlock(b, p, BlastInfo.NUCLEAR_CHARGE), Compat::blockDescription)
                .tab(GeoNtmTab.BOMBS);
        REG.blockItem(det_miner).tab(GeoNtmTab.BOMBS);
        REG.blockItem(block_semtex).tab(GeoNtmTab.BLOCKS);
        REG.blockItem(block_c4).tab(GeoNtmTab.BLOCKS);
        REG.blockItem(fissure_bomb, (b, p) -> new ItemNukeBlock(b, p, BlastInfo.FISSURE), Compat::blockDescription)
                .tab(GeoNtmTab.BOMBS);
        REG.blockItem(crashed_bomb_balefire, (b, p) -> new ItemNukeBlock(b, p, BlastInfo.DUD_BALEFIRE), Compat::blockDescription)
                .tab(GeoNtmTab.BOMBS);
        REG.blockItem(crashed_bomb_conventional).tab(GeoNtmTab.BOMBS);
        REG.blockItem(crashed_bomb_nuke, (b, p) -> new ItemNukeBlock(b, p, BlastInfo.DUD_NUKE), Compat::blockDescription)
                .tab(GeoNtmTab.BOMBS);
        REG.blockItem(crashed_bomb_salted, (b, p) -> new ItemNukeBlock(b, p, BlastInfo.DUD_SALTED), Compat::blockDescription)
                .tab(GeoNtmTab.BOMBS);
        REG.blockItem(bomb_multi).tab(GeoNtmTab.BOMBS);
    }

    private static <B extends BlockChargeBase> void chargeItem(RegistryObject<B> block) {
        REG.blockItem(block, (b, p) -> new ExplosivesBlockItem(b, p, () -> block.get().tooltip()),
                Compat::blockDescription).tab(GeoNtmTab.BOMBS);
    }

    public static void init() {}
}
