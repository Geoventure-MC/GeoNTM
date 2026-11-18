package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.blocks.generic.BlockOutgas;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.blocks.generic.BlockSlag;
import fr.geoventure.geontm.blocks.generic.BlockMeteoriteTreasure;
import fr.geoventure.geontm.blocks.generic.BlockNetherFireOre;
import fr.geoventure.geontm.blocks.machine.BlockCybercrab;
import fr.geoventure.geontm.blocks.generic.GeoNtmStairBlock;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geocore.registry.BlockTag;
import fr.geoventure.geocore.registry.Loot;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.dejson.Models;
import fr.geoventure.geocore.dejson.Slots;
import fr.geoventure.geocore.dejson.Variants;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import fr.geoventure.geocore.world.CompatDropExperienceBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * Simple full-cube blocks, ores, slabs and stairs. Each registration states its model, blockstate, loot and
 * tags (Reg; emitted by Dejson at build time); the block items are registered in ModItems. Edited by hand: the
 * retired port-tools/gen_wave1.py that first wrote it followed 1.12.2, and this file follows 1.7.
 */
public final class ModBlocks {

    private ModBlocks() {}

    // Some Wave-2c stairs stand on rad-resistant bases registered in ModHazardBlocks. The stair
    // factory resolves base.get().defaultBlockState() at BLOCK-registry flush time, so those bases
    // must flush FIRST. Registers flush in creation order, so force ModHazardBlocks's DeferredRegister
    // to be created (join the flush list) before this class's BLOCKS below.
    static { ModHazardBlocks.init(); ModOutgasBlocks.init(); }

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCKS);

    /** A registered block plus the creative tab its item belongs in (null = no tab). */
    public record BlockEntry(RegistryObject<Block> block, GeoNtmTab tab) {}
    public static final List<BlockEntry> ALL = new ArrayList<>();

    private static Reg.Handle<Block> block(String name, GeoNtmTab tab, UnaryOperator<BlockBehaviour.Properties> props) {
        Reg.Handle<Block> ro = REG.simpleBlock(name, props);
        ALL.add(new BlockEntry(ro, tab));
        return ro;
    }

    private static Reg.Handle<Block> block(String name, GeoNtmTab tab, java.util.function.Function<BlockBehaviour.Properties, ? extends Block> factory, UnaryOperator<BlockBehaviour.Properties> props) {
        Reg.Handle<Block> ro = REG.<Block>block(name, factory, props);
        ALL.add(new BlockEntry(ro, tab));
        return ro;
    }

    /** Ore that grants mining XP and (typically) drops a material item via its loot table. */
    /**
     * An ore block with an experience drop. Every 1.7 caller passes 0: 1.7 {@code BlockOre} never
     * overrides {@code getExpDrop}, so its ores give no experience (the per-ore xp values were 1.12.2's
     * {@code BlockNTMOre}; fix wave 4, 2026-10-03). Only {@code BlockSellafieldOre}'s gems pay 3-7 in 1.7.
     */
    private static Reg.Handle<Block> oreXp(String name, GeoNtmTab tab, int xp, UnaryOperator<BlockBehaviour.Properties> props) {
        Reg.Handle<Block> ro = REG.<Block>block(name, p -> new CompatDropExperienceBlock(ConstantInt.of(xp), p), props);
        ALL.add(new BlockEntry(ro, tab));
        return ro;
    }

    /** A vanilla slab over one texture: TYPE=bottom/top/double (the double is the full cube); drops 1 or 2. */
    private static Reg.Handle<Block> slab(String name, GeoNtmTab tab, String texture, UnaryOperator<BlockBehaviour.Properties> props) {
        Reg.Handle<Block> ro = REG.<Block>block(name, SlabBlock::new, props)
                .transform(Presets.shape(Models.slabAll(texture)).states(Variants.slab()))
                .loot(Loot.slab());
        ALL.add(new BlockEntry(ro, tab));
        return ro;
    }

    /** Stairs over an already-registered base block (its defaultBlockState() is resolved at flush), one texture. */
    private static Reg.Handle<Block> stairs(String name, GeoNtmTab tab, Supplier<? extends Block> base, String texture, UnaryOperator<BlockBehaviour.Properties> props) {
        Reg.Handle<Block> ro = REG.<Block>block(name, p -> new GeoNtmStairBlock(base.get().defaultBlockState(), p), props)
                .transform(Presets.shape(Models.stairsAll(texture)).states(Variants.stairs()))
                .loot(Loot.self());
        ALL.add(new BlockEntry(ro, tab));
        return ro;
    }

    // --- simple full-cube blocks (Wave 1) ---
    public static final RegistryObject<Block> fatduck = block("fatduck", GeoNtmTab.BLOCKS, p -> p)
            .transform(Presets.cubeAll()).loot(Loot.self());
    public static final RegistryObject<Block> structure_anchor = block("structure_anchor", null, p -> p.requiresCorrectToolForDrops().strength(2.5F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> crystal_hardened = block("crystal_hardened", null, p -> p.requiresCorrectToolForDrops().strength(15.0F, Float.POSITIVE_INFINITY))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> reinforced_stone = block("reinforced_stone", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(15.0F, 60.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_concrete_cracked = block("brick_concrete_cracked", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(15.0F, 36.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_concrete_broken = block("brick_concrete_broken", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(15.0F, 27.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_light = block("brick_light", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(15.0F, 12.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> reinforced_sand = block("reinforced_sand", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(15.0F, 24.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_obsidian = block("brick_obsidian", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(15.0F, 72.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> cmb_brick = block("cmb_brick", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(25.0F, 3000.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete = block("concrete", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(15.0F, 84.0F))
            .transform(Presets.cubeAll("geontm:blocks/concrete_tile")).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete_smooth = block("concrete_smooth", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(15.0F, 84.0F))
            .transform(Presets.cubeAll("geontm:blocks/concrete")).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete_asbestos = block("concrete_asbestos", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(15.0F, 90.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_bedrock_oil = block("ore_bedrock_oil", GeoNtmTab.RESOURCES, p -> p.strength(-1.0F, 600000.0F))
            .transform(Presets.cubeAll()).loot(Loot.nothing());
    public static final RegistryObject<Block> ore_oil_empty = block("ore_oil_empty", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> stone_gneiss = block("stone_gneiss", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(1.5F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> gneiss_brick = block("gneiss_brick", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(1.5F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> gneiss_tile = block("gneiss_tile", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(1.5F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> gneiss_chiseled = block("gneiss_chiseled", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(1.5F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> meteor_polished = block("meteor_polished", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(15.0F, 216.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> meteor_brick = block("meteor_brick", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(15.0F, 216.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> meteor_brick_mossy = block("meteor_brick_mossy", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(15.0F, 216.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> meteor_brick_cracked = block("meteor_brick_cracked", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(15.0F, 216.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> meteor_brick_chiseled = block("meteor_brick_chiseled", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(15.0F, 216.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> meteor_battery = block("meteor_battery", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(15.0F, 216.0F))
            .transform(Presets.column("geontm:blocks/meteor_power", "geontm:blocks/meteor_spawner_side")).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> meteor_spawner = block("meteor_spawner", GeoNtmTab.BLOCKS, BlockCybercrab::new, p -> p.requiresCorrectToolForDrops().strength(15.0F, 216.0F))
            .transform(Presets.cubeBottomTop("geontm:blocks/meteor_polished", "geontm:blocks/meteor_spawner_side", "geontm:blocks/meteor_spawner_top")).loot(Loot.nothing()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_jungle = block("brick_jungle", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(15.0F, 216.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_jungle_cracked = block("brick_jungle_cracked", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(15.0F, 216.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_dungeon = block("brick_dungeon", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(15.0F, 216.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_dungeon_flat = block("brick_dungeon_flat", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(15.0F, 216.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_dungeon_tile = block("brick_dungeon_tile", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(15.0F, 216.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_dungeon_circle = block("brick_dungeon_circle", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(15.0F, 216.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> block_niter = block("block_niter", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> block_sulfur = block("block_sulfur", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> block_titanium = block("block_titanium", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 30.0F).sound(SoundType.METAL))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> block_red_copper = block("block_red_copper", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 15.0F).sound(SoundType.METAL))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> block_tungsten = block("block_tungsten", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 12.0F).sound(SoundType.METAL))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> block_aluminium = block("block_aluminium", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 12.0F).sound(SoundType.METAL))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> block_fluorite = block("block_fluorite", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> block_steel = block("block_steel", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 30.0F).sound(SoundType.METAL))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> block_beryllium = block("block_beryllium", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 12.0F).sound(SoundType.METAL))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> block_euphemium = block("block_euphemium", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 36000.0F).sound(SoundType.METAL))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> block_dineutronium = block("block_dineutronium", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 36000.0F).sound(SoundType.METAL))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> block_combine_steel = block("block_combine_steel", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 360.0F).sound(SoundType.METAL))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> block_desh = block("block_desh", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 180.0F).sound(SoundType.METAL))
            .transform(Presets.cubeAll()).loot(Loot.self())
            .tag(BlockTag.PICKAXE).tag(BlockTag.of("c:storage_blocks/desh"));
    public static final RegistryObject<Block> block_dura_steel = block("block_dura_steel", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 120.0F).sound(SoundType.METAL))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> block_saturnite = block("block_saturnite", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(6.0F, 240.0F).sound(SoundType.METAL))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> tektite = block("tektite", GeoNtmTab.RESOURCES, p -> p.strength(1.5F, 0.5F).sound(SoundType.STONE))
            .transform(Presets.cubeAll()).loot(Loot.self());
    public static final RegistryObject<Block> ore_tektite_osmiridium = block("ore_tektite_osmiridium", GeoNtmTab.RESOURCES, p -> p.strength(2.5F, 0.5F).sound(SoundType.STONE))
            .transform(Presets.cubeAll()).loot(Loot.self());
    public static final RegistryObject<Block> deco_titanium = block("deco_titanium", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> deco_red_copper = block("deco_red_copper", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> deco_tungsten = block("deco_tungsten", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> deco_aluminium = block("deco_aluminium", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> deco_steel = block("deco_steel", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> deco_rusty_steel = block("deco_rusty_steel", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> deco_lead = block("deco_lead", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> deco_beryllium = block("deco_beryllium", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> stone_cracked = block("stone_cracked", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(5.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> block_smore = block("block_smore", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(15.0F, 360.0F))
            .transform(Presets.column("geontm:blocks/block_smore_top", "geontm:blocks/block_smore_side")).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> block_starmetal = block("block_starmetal", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 240.0F).sound(SoundType.METAL))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> block_cobalt = block("block_cobalt", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 30.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> block_foam = block("block_foam", GeoNtmTab.BLOCKS, p -> p.strength(0.5F, 0.0F).sound(SoundType.SNOW))
            .transform(Presets.cubeAll()).loot(Loot.self());
    public static final RegistryObject<Block> basalt = block("basalt", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.column("geontm:blocks/basalt_top", "geontm:blocks/basalt")).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> basalt_smooth = block("basalt_smooth", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> basalt_brick = block("basalt_brick", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> basalt_polished = block("basalt_polished", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> basalt_tiles = block("basalt_tiles", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> seal_frame = block("seal_frame", GeoNtmTab.MACHINES, p -> p.requiresCorrectToolForDrops().strength(10.0F, 60.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> machine_transformer = block("machine_transformer", GeoNtmTab.MACHINES, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeBottomTop("geontm:blocks/machine_transformer_bottom_iron", "geontm:blocks/machine_transformer_iron", "geontm:blocks/machine_transformer_top_iron")).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> machine_transformer_20 = block("machine_transformer_20", GeoNtmTab.MACHINES, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeBottomTop("geontm:blocks/machine_transformer_top_iron", "geontm:blocks/machine_transformer_iron", "geontm:blocks/machine_transformer_bottom_iron")).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> machine_transformer_dnt = block("machine_transformer_dnt", GeoNtmTab.MACHINES, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeBottomTop("geontm:blocks/machine_transformer_bottom", "geontm:blocks/machine_transformer", "geontm:blocks/machine_transformer_top")).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> machine_transformer_dnt_20 = block("machine_transformer_dnt_20", GeoNtmTab.MACHINES, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeBottomTop("geontm:blocks/machine_transformer_top", "geontm:blocks/machine_transformer", "geontm:blocks/machine_transformer_bottom")).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> muffler = block("muffler", GeoNtmTab.BLOCKS, p -> p.strength(0.8F).sound(SoundType.WOOL))
            .transform(Presets.cubeAll()).loot(Loot.self());
    public static final RegistryObject<Block> struct_launcher = block("struct_launcher", GeoNtmTab.MISSILES, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> struct_scaffold = block("struct_scaffold", GeoNtmTab.MISSILES, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> fusion_heater = block("fusion_heater", null, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.column("geontm:blocks/fusion_heater_top", "geontm:blocks/fusion_heater_side")).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> fusion_hatch = block("fusion_hatch", null, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.shape(Models.parented("minecraft:block/cube", Slots.of("particle", "geontm:blocks/fusion_hatch").and("north", "geontm:blocks/fusion_hatch").and("south", "geontm:blocks/fusion_heater_side").and("east", "geontm:blocks/fusion_heater_side").and("west", "geontm:blocks/fusion_heater_side").and("up", "geontm:blocks/block_tungsten").and("down", "geontm:blocks/block_tungsten")))).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> fusion_core_block = block("fusion_core_block", GeoNtmTab.MACHINES, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.column("geontm:blocks/fusion_core_side_alt", "geontm:blocks/fusion_core_side_alt")).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> watz_element = block("watz_element", GeoNtmTab.MACHINES, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.column("geontm:blocks/watz_element_top", "geontm:blocks/watz_element_side")).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> watz_cooler = block("watz_cooler", GeoNtmTab.MACHINES, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.column("geontm:blocks/watz_cooler_top", "geontm:blocks/watz_cooler_side")).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> pink_planks = block("pink_planks", null, p -> p.sound(SoundType.WOOD).instrument(NoteBlockInstrument.BASS).ignitedByLava())
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.AXE);
    public static final RegistryObject<Block> brick_fire = block("brick_fire", GeoNtmTab.BLOCKS, p -> p.requiresCorrectToolForDrops().strength(10.0F, 21.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> tile_lab = block("tile_lab", GeoNtmTab.BLOCKS, p -> new BlockOutgas(p, false, 5, true), p -> p.requiresCorrectToolForDrops().strength(1.0F, 12.0F).sound(SoundType.GLASS))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> tile_lab_cracked = block("tile_lab_cracked", GeoNtmTab.BLOCKS, p -> new BlockOutgas(p, false, 5, true), p -> p.requiresCorrectToolForDrops().strength(1.0F, 12.0F).sound(SoundType.GLASS))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> block_slag = block("block_slag", GeoNtmTab.BLOCKS, BlockSlag::new, p -> p.requiresCorrectToolForDrops().strength(5.0F, 2.0F).sound(SoundType.STONE))
            .model(d -> d.models(Models.group().and("", Models.cubeAll("geontm:blocks/block_slag")).and("_cracked", Models.cubeAll("geontm:blocks/block_slag_broken"))).states(Variants.select("cracked", "false", "", "true", "_cracked")).blockItemDirect()).loot(Loot.self()).tag(BlockTag.PICKAXE);

    // --- ores (Wave 2b); XP ores drop materials, plain ores drop themselves ---
    public static final RegistryObject<Block> ore_schrabidium = block("ore_schrabidium", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(15.0F, 360.0F))
            .transform(Presets.cubeAll()).loot(RawOres.drop("geontm:raw_schrabidium", Loot.self())).tag(BlockTag.PICKAXE);
    /** Minerais rares de Geoventure : on ne les trouve que sur Mars et sur Glacio. */
    public static final RegistryObject<Block> ore_mars_saturnite = block("ore_mars_saturnite", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(8.0F, 30.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_glacio_starmetal = block("ore_glacio_starmetal", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(8.0F, 30.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_thorium = block("ore_thorium", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(RawOres.drop("geontm:raw_thorium", Loot.self())).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_titanium = block("ore_titanium", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(RawOres.drop("geontm:raw_titanium", Loot.self())).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_sulfur = oreXp("ore_sulfur", GeoNtmTab.RESOURCES, 0, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.oreMultiplied("geontm:sulfur", 2, 4)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_niter = oreXp("ore_niter", GeoNtmTab.RESOURCES, 0, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.oreMultiplied("geontm:niter", 2, 4)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_tungsten = block("ore_tungsten", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(RawOres.drop("geontm:raw_tungsten", Loot.self())).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_aluminium = block("ore_aluminium", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(RawOres.drop("geontm:raw_aluminium", Loot.self())).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_fluorite = oreXp("ore_fluorite", GeoNtmTab.RESOURCES, 0, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.oreMultiplied("geontm:fluorite", 2, 4)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_lead = block("ore_lead", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(RawOres.drop("geontm:raw_lead", Loot.self())).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_beryllium = block("ore_beryllium", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(5.0F, 9.0F))
            .transform(Presets.cubeAll()).loot(RawOres.drop("geontm:raw_beryllium", Loot.self())).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_lignite = block("ore_lignite", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(5.0F, 9.0F))
            .transform(Presets.cubeAll()).loot(Loot.oreMultiplied("geontm:lignite", 1, 1)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_asbestos = block("ore_asbestos", GeoNtmTab.RESOURCES, p -> new BlockOutgas(p, true, 5, true), p -> p.requiresCorrectToolForDrops().strength(5.0F, 9.0F).randomTicks())
            .transform(Presets.cubeAll()).loot(Loot.oreMultiplied("geontm:ingot_asbestos", 1, 1)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_rare = block("ore_rare", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.oreMultiplied("geontm:chunk_ore_rare", 1, 1)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_cobalt = oreXp("ore_cobalt", GeoNtmTab.RESOURCES, 0, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(RawOres.drop("geontm:raw_cobalt", Loot.oreMultiplied("geontm:fragment_cobalt", 4, 9))).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_cinnabar = oreXp("ore_cinnabar", GeoNtmTab.RESOURCES, 0, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.oreMultiplied("geontm:cinnabar", 1, 1)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_coltan = oreXp("ore_coltan", GeoNtmTab.RESOURCES, 0, p -> p.requiresCorrectToolForDrops().strength(15.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.oreMultiplied("geontm:fragment_coltan", 1, 1)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_australium = block("ore_australium", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    // ore_oil: hand-registered functional block (see ModOilBlocks), assets still generated
    public static final RegistryObject<Block> ore_gneiss_iron = block("ore_gneiss_iron", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(1.5F, 6.0F))
            .transform(Presets.cubeAll()).loot(RawOres.drop("minecraft:raw_iron", Loot.self())).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_gneiss_gold = block("ore_gneiss_gold", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(1.5F, 6.0F))
            .transform(Presets.cubeAll()).loot(RawOres.drop("minecraft:raw_gold", Loot.self())).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_gneiss_copper = block("ore_gneiss_copper", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(1.5F, 6.0F))
            .transform(Presets.cubeAll()).loot(RawOres.drop("minecraft:raw_copper", Loot.self())).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_gneiss_asbestos = block("ore_gneiss_asbestos", GeoNtmTab.RESOURCES, p -> new BlockOutgas(p, true, 5, true), p -> p.requiresCorrectToolForDrops().strength(1.5F, 6.0F).randomTicks())
            .transform(Presets.cubeAll()).loot(Loot.oreMultiplied("geontm:ingot_asbestos", 1, 1)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_gneiss_lithium = block("ore_gneiss_lithium", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(1.5F, 6.0F))
            .transform(Presets.cubeAll()).loot(RawOres.drop("geontm:raw_lithium", Loot.self())).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_gneiss_schrabidium = block("ore_gneiss_schrabidium", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(1.5F, 6.0F))
            .transform(Presets.cubeAll()).loot(RawOres.drop("geontm:raw_schrabidium", Loot.self())).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_gneiss_rare = block("ore_gneiss_rare", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(1.5F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.oreMultiplied("geontm:chunk_ore_rare", 1, 1)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_gneiss_gas = block("ore_gneiss_gas", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(1.5F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_tikite = block("ore_tikite", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F))
            .transform(Presets.cubeAll("geontm:blocks/ore_tikite_alt")).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_nether_cobalt = block("ore_nether_cobalt", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(0.4F, 6.0F))
            .transform(Presets.cubeAll()).loot(RawOres.drop("geontm:raw_cobalt", Loot.oreMultiplied("geontm:fragment_cobalt", 5, 12))).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_nether_tungsten = block("ore_nether_tungsten", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(0.4F, 6.0F))
            .transform(Presets.cubeAll()).loot(RawOres.drop("geontm:raw_tungsten", Loot.self())).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_nether_sulfur = oreXp("ore_nether_sulfur", GeoNtmTab.RESOURCES, 0, p -> p.requiresCorrectToolForDrops().strength(0.4F, 6.0F))
            .transform(Presets.cubeAll()).loot(Loot.oreMultiplied("geontm:sulfur", 2, 4)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_nether_fire = block("ore_nether_fire", GeoNtmTab.RESOURCES, BlockNetherFireOre::new, p -> p.requiresCorrectToolForDrops().strength(0.4F, 6.0F).noLootTable())
            .transform(Presets.cubeAll()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_nether_plutonium = block("ore_nether_plutonium", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(0.4F, 6.0F))
            .transform(Presets.cubeAll()).loot(RawOres.drop("geontm:raw_plutonium", Loot.self())).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ore_nether_schrabidium = block("ore_nether_schrabidium", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(15.0F, 360.0F))
            .transform(Presets.cubeAll()).loot(RawOres.drop("geontm:raw_schrabidium", Loot.self())).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> block_meteor = block("block_meteor", GeoNtmTab.RESOURCES, p -> p.requiresCorrectToolForDrops().strength(15.0F, 216.0F))
            .transform(Presets.cubeAll()).loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> block_meteor_cobble = oreXp("block_meteor_cobble", GeoNtmTab.RESOURCES, 0, p -> p.requiresCorrectToolForDrops().strength(15.0F, 216.0F))
            .transform(Presets.cubeAll()).loot(Loot.oreNoFortune("geontm:fragment_meteorite", 1, 1)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> block_meteor_broken = oreXp("block_meteor_broken", GeoNtmTab.RESOURCES, 0, p -> p.requiresCorrectToolForDrops().strength(15.0F, 216.0F))
            .transform(Presets.cubeAll()).loot(Loot.oreNoFortune("geontm:fragment_meteorite", 1, 3)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> block_meteor_treasure = block("block_meteor_treasure", GeoNtmTab.RESOURCES, BlockMeteoriteTreasure::new, p -> p.requiresCorrectToolForDrops().strength(15.0F, 216.0F).noLootTable())
            .transform(Presets.cubeAll()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> waste_planks = block("waste_planks", GeoNtmTab.RESOURCES, p -> p.strength(0.5F, 1.5F).sound(SoundType.WOOD).instrument(NoteBlockInstrument.BASS).ignitedByLava())
            .transform(Presets.cubeAll()).loot(Loot.oreMultiplied("minecraft:charcoal", 1, 1)).tag(BlockTag.AXE);

    // --- slabs (Wave 2c); vanilla SlabBlock (TYPE=bottom/top/double replaces the old *_double_slab) ---
    public static final RegistryObject<Block> reinforced_stone_slab = slab("reinforced_stone_slab", GeoNtmTab.BLOCKS, "geontm:blocks/reinforced_stone", p -> p.requiresCorrectToolForDrops().strength(15.0F, 60.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_concrete_slab = slab("brick_concrete_slab", GeoNtmTab.BLOCKS, "geontm:blocks/brick_concrete", p -> p.requiresCorrectToolForDrops().strength(15.0F, 96.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_concrete_mossy_slab = slab("brick_concrete_mossy_slab", GeoNtmTab.BLOCKS, "geontm:blocks/brick_concrete_mossy", p -> p.requiresCorrectToolForDrops().strength(15.0F, 96.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_concrete_cracked_slab = slab("brick_concrete_cracked_slab", GeoNtmTab.BLOCKS, "geontm:blocks/brick_concrete_cracked", p -> p.requiresCorrectToolForDrops().strength(15.0F, 36.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_concrete_broken_slab = slab("brick_concrete_broken_slab", GeoNtmTab.BLOCKS, "geontm:blocks/brick_concrete_broken", p -> p.requiresCorrectToolForDrops().strength(15.0F, 27.0F)).tag(BlockTag.PICKAXE);
    /** 1.7 concrete_brick_slab meta 4 (BlockMultiSlab takes the brick's own hardness/resistance); History §19ej-1b. */
    public static final RegistryObject<Block> brick_ducrete_slab = slab("brick_ducrete_slab", GeoNtmTab.BLOCKS, "geontm:blocks/brick_ducrete", p -> p.requiresCorrectToolForDrops().strength(15.0F, 450.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> reinforced_brick_slab = slab("reinforced_brick_slab", GeoNtmTab.BLOCKS, "geontm:blocks/reinforced_brick", p -> p.requiresCorrectToolForDrops().strength(15.0F, 180.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_light_slab = slab("brick_light_slab", GeoNtmTab.BLOCKS, "geontm:blocks/brick_light", p -> p.requiresCorrectToolForDrops().strength(15.0F, 12.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_compound_slab = slab("brick_compound_slab", GeoNtmTab.BLOCKS, "geontm:blocks/brick_compound", p -> p.requiresCorrectToolForDrops().strength(15.0F, 240.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_asbestos_slab = slab("brick_asbestos_slab", GeoNtmTab.BLOCKS, "geontm:blocks/brick_asbestos", p -> p.requiresCorrectToolForDrops().strength(15.0F, 600.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> lightstone_tile_slab = slab("lightstone_tile_slab", GeoNtmTab.BLOCKS, "geontm:blocks/lightstone.tile", p -> p.requiresCorrectToolForDrops().strength(15.0F, 9.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> lightstone_bricks_slab = slab("lightstone_bricks_slab", GeoNtmTab.BLOCKS, "geontm:blocks/lightstone.bricks", p -> p.requiresCorrectToolForDrops().strength(15.0F, 9.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_fire_slab = slab("brick_fire_slab", GeoNtmTab.BLOCKS, "geontm:blocks/brick_fire", p -> p.requiresCorrectToolForDrops().strength(15.0F, 21.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> reinforced_sand_slab = slab("reinforced_sand_slab", GeoNtmTab.BLOCKS, "geontm:blocks/reinforced_sand", p -> p.requiresCorrectToolForDrops().strength(15.0F, 12.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_obsidian_slab = slab("brick_obsidian_slab", GeoNtmTab.BLOCKS, "geontm:blocks/brick_obsidian", p -> p.requiresCorrectToolForDrops().strength(15.0F, 72.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> cmb_brick_reinforced_slab = slab("cmb_brick_reinforced_slab", GeoNtmTab.BLOCKS, "geontm:blocks/cmb_brick_reinforced", p -> p.requiresCorrectToolForDrops().strength(25.0F, 15000.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete_slab = slab("concrete_slab", GeoNtmTab.BLOCKS, "geontm:blocks/concrete_tile", p -> p.requiresCorrectToolForDrops().strength(15.0F, 84.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete_smooth_slab = slab("concrete_smooth_slab", GeoNtmTab.BLOCKS, "geontm:blocks/concrete", p -> p.requiresCorrectToolForDrops().strength(15.0F, 84.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete_white_slab = slab("concrete_white_slab", GeoNtmTab.BLOCKS, "geontm:blocks/concrete_white", p -> p.requiresCorrectToolForDrops().strength(15.0F, 42.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete_orange_slab = slab("concrete_orange_slab", GeoNtmTab.BLOCKS, "geontm:blocks/concrete_orange", p -> p.requiresCorrectToolForDrops().strength(15.0F, 42.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete_magenta_slab = slab("concrete_magenta_slab", GeoNtmTab.BLOCKS, "geontm:blocks/concrete_magenta", p -> p.requiresCorrectToolForDrops().strength(15.0F, 42.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete_light_blue_slab = slab("concrete_light_blue_slab", GeoNtmTab.BLOCKS, "geontm:blocks/concrete_light_blue", p -> p.requiresCorrectToolForDrops().strength(15.0F, 42.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete_yellow_slab = slab("concrete_yellow_slab", GeoNtmTab.BLOCKS, "geontm:blocks/concrete_yellow", p -> p.requiresCorrectToolForDrops().strength(15.0F, 42.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete_lime_slab = slab("concrete_lime_slab", GeoNtmTab.BLOCKS, "geontm:blocks/concrete_lime", p -> p.requiresCorrectToolForDrops().strength(15.0F, 42.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete_pink_slab = slab("concrete_pink_slab", GeoNtmTab.BLOCKS, "geontm:blocks/concrete_pink", p -> p.requiresCorrectToolForDrops().strength(15.0F, 42.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete_gray_slab = slab("concrete_gray_slab", GeoNtmTab.BLOCKS, "geontm:blocks/concrete_gray", p -> p.requiresCorrectToolForDrops().strength(15.0F, 42.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete_silver_slab = slab("concrete_silver_slab", GeoNtmTab.BLOCKS, "geontm:blocks/concrete_silver", p -> p.requiresCorrectToolForDrops().strength(15.0F, 42.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete_cyan_slab = slab("concrete_cyan_slab", GeoNtmTab.BLOCKS, "geontm:blocks/concrete_cyan", p -> p.requiresCorrectToolForDrops().strength(15.0F, 42.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete_purple_slab = slab("concrete_purple_slab", GeoNtmTab.BLOCKS, "geontm:blocks/concrete_purple", p -> p.requiresCorrectToolForDrops().strength(15.0F, 42.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete_blue_slab = slab("concrete_blue_slab", GeoNtmTab.BLOCKS, "geontm:blocks/concrete_blue", p -> p.requiresCorrectToolForDrops().strength(15.0F, 42.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete_brown_slab = slab("concrete_brown_slab", GeoNtmTab.BLOCKS, "geontm:blocks/concrete_brown", p -> p.requiresCorrectToolForDrops().strength(15.0F, 42.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete_green_slab = slab("concrete_green_slab", GeoNtmTab.BLOCKS, "geontm:blocks/concrete_green", p -> p.requiresCorrectToolForDrops().strength(15.0F, 42.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete_red_slab = slab("concrete_red_slab", GeoNtmTab.BLOCKS, "geontm:blocks/concrete_red", p -> p.requiresCorrectToolForDrops().strength(15.0F, 42.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete_black_slab = slab("concrete_black_slab", GeoNtmTab.BLOCKS, "geontm:blocks/concrete_black", p -> p.requiresCorrectToolForDrops().strength(15.0F, 42.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete_asbestos_slab = slab("concrete_asbestos_slab", GeoNtmTab.BLOCKS, "geontm:blocks/concrete_asbestos", p -> p.requiresCorrectToolForDrops().strength(15.0F, 90.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ducrete_smooth_slab = slab("ducrete_smooth_slab", GeoNtmTab.BLOCKS, "geontm:blocks/ducrete_smooth", p -> p.requiresCorrectToolForDrops().strength(20.0F, 300.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ducrete_slab = slab("ducrete_slab", GeoNtmTab.BLOCKS, "geontm:blocks/ducrete", p -> p.requiresCorrectToolForDrops().strength(20.0F, 300.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ducrete_brick_slab = slab("ducrete_brick_slab", GeoNtmTab.BLOCKS, "geontm:blocks/ducrete_brick", p -> p.requiresCorrectToolForDrops().strength(15.0F, 225.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ducrete_reinforced_slab = slab("ducrete_reinforced_slab", GeoNtmTab.BLOCKS, "geontm:blocks/ducrete_reinforced", p -> p.requiresCorrectToolForDrops().strength(20.0F, 300.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> tile_lab_slab = slab("tile_lab_slab", GeoNtmTab.BLOCKS, "geontm:blocks/tile_lab", p -> p.requiresCorrectToolForDrops().strength(1.0F, 6.0F).sound(SoundType.GLASS)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> tile_lab_cracked_slab = slab("tile_lab_cracked_slab", GeoNtmTab.BLOCKS, "geontm:blocks/tile_lab_cracked", p -> p.requiresCorrectToolForDrops().strength(1.0F, 6.0F).sound(SoundType.GLASS)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> tile_lab_broken_slab = slab("tile_lab_broken_slab", GeoNtmTab.BLOCKS, "geontm:blocks/tile_lab_broken", p -> p.requiresCorrectToolForDrops().strength(1.0F, 6.0F).sound(SoundType.GLASS)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> pink_slab = slab("pink_slab", null, "geontm:blocks/pink_planks", p -> p.sound(SoundType.WOOD).instrument(NoteBlockInstrument.BASS).ignitedByLava()).tag(BlockTag.AXE);

    // --- stairs (Wave 2c); base block must be registered above so defaultBlockState() resolves ---
    public static final RegistryObject<Block> reinforced_stone_stairs = stairs("reinforced_stone_stairs", GeoNtmTab.BLOCKS, reinforced_stone, "geontm:blocks/reinforced_stone", p -> p.requiresCorrectToolForDrops().strength(15.0F, 76.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_concrete_stairs = stairs("brick_concrete_stairs", GeoNtmTab.BLOCKS, ModHazardBlocks.brick_concrete, "geontm:blocks/brick_concrete", p -> p.requiresCorrectToolForDrops().strength(15.0F, 95.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_concrete_mossy_stairs = stairs("brick_concrete_mossy_stairs", GeoNtmTab.BLOCKS, ModHazardBlocks.brick_concrete_mossy, "geontm:blocks/brick_concrete_mossy", p -> p.requiresCorrectToolForDrops().strength(15.0F, 94.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_concrete_cracked_stairs = stairs("brick_concrete_cracked_stairs", GeoNtmTab.BLOCKS, brick_concrete_cracked, "geontm:blocks/brick_concrete_cracked", p -> p.requiresCorrectToolForDrops().strength(15.0F, 40.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_concrete_broken_stairs = stairs("brick_concrete_broken_stairs", GeoNtmTab.BLOCKS, brick_concrete_broken, "geontm:blocks/brick_concrete_broken", p -> p.requiresCorrectToolForDrops().strength(15.0F, 30.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> reinforced_brick_stairs = stairs("reinforced_brick_stairs", GeoNtmTab.BLOCKS, ModHazardBlocks.reinforced_brick, "geontm:blocks/reinforced_brick", p -> p.requiresCorrectToolForDrops().strength(15.0F, 240.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_compound_stairs = stairs("brick_compound_stairs", GeoNtmTab.BLOCKS, ModHazardBlocks.brick_compound, "geontm:blocks/brick_compound", p -> p.requiresCorrectToolForDrops().strength(15.0F, 320.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_asbestos_stairs = stairs("brick_asbestos_stairs", GeoNtmTab.BLOCKS, ModOutgasBlocks.brick_asbestos, "geontm:blocks/brick_asbestos", p -> p.requiresCorrectToolForDrops().strength(15.0F, 32.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_light_stairs = stairs("brick_light_stairs", GeoNtmTab.BLOCKS, brick_light, "geontm:blocks/brick_light", p -> p.requiresCorrectToolForDrops().strength(15.0F, 12.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> reinforced_sand_stairs = stairs("reinforced_sand_stairs", GeoNtmTab.BLOCKS, reinforced_sand, "geontm:blocks/reinforced_sand", p -> p.requiresCorrectToolForDrops().strength(15.0F, 19.2F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_obsidian_stairs = stairs("brick_obsidian_stairs", GeoNtmTab.BLOCKS, brick_obsidian, "geontm:blocks/brick_obsidian", p -> p.requiresCorrectToolForDrops().strength(15.0F, 96.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> cmb_brick_reinforced_stairs = stairs("cmb_brick_reinforced_stairs", GeoNtmTab.BLOCKS, ModHazardBlocks.cmb_brick_reinforced, "geontm:blocks/cmb_brick_reinforced", p -> p.requiresCorrectToolForDrops().strength(25.0F, 27000.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete_stairs = stairs("concrete_stairs", GeoNtmTab.BLOCKS, concrete, "geontm:blocks/concrete_tile", p -> p.requiresCorrectToolForDrops().strength(15.0F, 84.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete_smooth_stairs = stairs("concrete_smooth_stairs", GeoNtmTab.BLOCKS, concrete_smooth, "geontm:blocks/concrete", p -> p.requiresCorrectToolForDrops().strength(15.0F, 94.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> concrete_asbestos_stairs = stairs("concrete_asbestos_stairs", GeoNtmTab.BLOCKS, concrete_asbestos, "geontm:blocks/concrete_asbestos", p -> p.requiresCorrectToolForDrops().strength(15.0F, 94.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ducrete_smooth_stairs = stairs("ducrete_smooth_stairs", GeoNtmTab.BLOCKS, ModHazardBlocks.ducrete_smooth, "geontm:blocks/ducrete_smooth", p -> p.requiresCorrectToolForDrops().strength(20.0F, 360.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> ducrete_stairs = stairs("ducrete_stairs", GeoNtmTab.BLOCKS, ModHazardBlocks.ducrete, "geontm:blocks/ducrete", p -> p.requiresCorrectToolForDrops().strength(20.0F, 360.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> tile_lab_stairs = stairs("tile_lab_stairs", GeoNtmTab.BLOCKS, tile_lab, "geontm:blocks/tile_lab", p -> p.requiresCorrectToolForDrops().strength(1.0F, 9.0F).sound(SoundType.GLASS)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> tile_lab_cracked_stairs = stairs("tile_lab_cracked_stairs", GeoNtmTab.BLOCKS, tile_lab_cracked, "geontm:blocks/tile_lab_cracked", p -> p.requiresCorrectToolForDrops().strength(1.0F, 9.0F).sound(SoundType.GLASS)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> tile_lab_broken_stairs = stairs("tile_lab_broken_stairs", GeoNtmTab.BLOCKS, ModOutgasBlocks.tile_lab_broken, "geontm:blocks/tile_lab_broken", p -> p.requiresCorrectToolForDrops().strength(1.0F, 9.0F).sound(SoundType.GLASS)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> brick_fire_stairs = stairs("brick_fire_stairs", GeoNtmTab.BLOCKS, brick_fire, "geontm:blocks/brick_fire", p -> p.requiresCorrectToolForDrops().strength(15.0F, 21.0F)).tag(BlockTag.PICKAXE);
    public static final RegistryObject<Block> pink_stairs = stairs("pink_stairs", null, pink_planks, "geontm:blocks/pink_planks", p -> p.sound(SoundType.WOOD).instrument(NoteBlockInstrument.BASS).ignitedByLava()).tag(BlockTag.AXE);

    public static void init() {}
}
