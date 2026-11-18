package fr.geoventure.geontm.world.gen;

import fr.geoventure.geontm.world.gen.component.LegacyBlock;

/**
 * The 1.7 {@code ModBlocks} and {@code Blocks} fields the legacy hardcoded dungeons (1.7 {@code fr.geoventure.geontm.world.dungeon},
 * {@code world.feature.Dud}, {@code world.generator}) reference, as {@link LegacyBlock}s named by their 1.7 registry
 * names (only {@code crashed_balefire} differs from its field: 1.7 registered it as {@code tile.crashed_bomb}). The
 * nested classes carry the 1.7 names so the ported builders read exactly like 1.7's; the pair (block, metadata) they
 * write becomes a flattened state in {@link LegacyDungeonWorld#resolve}.
 *
 * <p>Each constant is one shared instance, so the builders' identity comparisons ({@code world.getBlock(..) ==
 * Blocks.chest}, the jungle rooms' {@code bl == ModBlocks.brick_jungle}) work on the virtual world as they did on 1.7's.
 */
public final class LegacyDungeonBlocks {

    private LegacyDungeonBlocks() {}

    /** 1.7 {@code fr.geoventure.geontm.blocks.ModBlocks}. */
    public static final class ModBlocks {
        private ModBlocks() {}

        public static final LegacyBlock ancient_scrap = LegacyBlock.geontm("ancient_scrap");
        public static final LegacyBlock barbed_wire = LegacyBlock.geontm("barbed_wire");
        public static final LegacyBlock block_lead = LegacyBlock.geontm("block_lead");
        public static final LegacyBlock block_meteor = LegacyBlock.geontm("block_meteor");
        public static final LegacyBlock block_uranium = LegacyBlock.geontm("block_uranium");
        public static final LegacyBlock bobblehead = LegacyBlock.geontm("bobblehead");
        public static final LegacyBlock brick_concrete = LegacyBlock.geontm("brick_concrete");
        public static final LegacyBlock brick_concrete_broken = LegacyBlock.geontm("brick_concrete_broken");
        public static final LegacyBlock brick_concrete_cracked = LegacyBlock.geontm("brick_concrete_cracked");
        public static final LegacyBlock brick_concrete_marked = LegacyBlock.geontm("brick_concrete_marked");
        public static final LegacyBlock brick_concrete_mossy = LegacyBlock.geontm("brick_concrete_mossy");
        public static final LegacyBlock brick_jungle = LegacyBlock.geontm("brick_jungle");
        public static final LegacyBlock brick_jungle_circle = LegacyBlock.geontm("brick_jungle_circle");
        public static final LegacyBlock brick_jungle_cracked = LegacyBlock.geontm("brick_jungle_cracked");
        public static final LegacyBlock brick_jungle_fragile = LegacyBlock.geontm("brick_jungle_fragile");
        public static final LegacyBlock brick_jungle_glyph = LegacyBlock.geontm("brick_jungle_glyph");
        public static final LegacyBlock brick_jungle_lava = LegacyBlock.geontm("brick_jungle_lava");
        public static final LegacyBlock brick_jungle_trap = LegacyBlock.geontm("brick_jungle_trap");
        public static final LegacyBlock brick_light = LegacyBlock.geontm("brick_light");
        public static final LegacyBlock brick_obsidian = LegacyBlock.geontm("brick_obsidian");
        public static final LegacyBlock cable_switch = LegacyBlock.geontm("cable_switch");
        public static final LegacyBlock capacitor_copper = LegacyBlock.geontm("capacitor_copper");
        public static final LegacyBlock concrete = LegacyBlock.geontm("concrete");
        public static final LegacyBlock concrete_pillar = LegacyBlock.geontm("concrete_pillar");
        public static final LegacyBlock concrete_smooth = LegacyBlock.geontm("concrete_smooth");
        public static final LegacyBlock crashed_balefire = LegacyBlock.geontm("crashed_bomb");  // registered as tile.crashed_bomb
        public static final LegacyBlock crate = LegacyBlock.geontm("crate");
        public static final LegacyBlock crate_ammo = LegacyBlock.geontm("crate_ammo");
        public static final LegacyBlock crate_can = LegacyBlock.geontm("crate_can");
        public static final LegacyBlock crate_jungle = LegacyBlock.geontm("crate_jungle");
        public static final LegacyBlock crate_metal = LegacyBlock.geontm("crate_metal");
        public static final LegacyBlock crate_steel = LegacyBlock.geontm("crate_steel");
        public static final LegacyBlock deco_lead = LegacyBlock.geontm("deco_lead");
        public static final LegacyBlock deco_red_copper = LegacyBlock.geontm("deco_red_copper");
        public static final LegacyBlock deco_steel = LegacyBlock.geontm("deco_steel");
        public static final LegacyBlock deco_titanium = LegacyBlock.geontm("deco_titanium");
        public static final LegacyBlock deco_tungsten = LegacyBlock.geontm("deco_tungsten");
        public static final LegacyBlock fence_metal = LegacyBlock.geontm("fence_metal");
        public static final LegacyBlock fusion_hatch = LegacyBlock.geontm("fusion_hatch");
        public static final LegacyBlock fusion_heater = LegacyBlock.geontm("fusion_heater");
        public static final LegacyBlock gas_radon_tomb = LegacyBlock.geontm("gas_radon_tomb");
        public static final LegacyBlock geiger = LegacyBlock.geontm("geiger");
        public static final LegacyBlock hadron_coil_alloy = LegacyBlock.geontm("hadron_coil_alloy");
        public static final LegacyBlock machine_centrifuge = LegacyBlock.geontm("machine_centrifuge");
        public static final LegacyBlock machine_electric_furnace_off = LegacyBlock.geontm("machine_electric_furnace_off");
        public static final LegacyBlock machine_puf6_tank = LegacyBlock.geontm("machine_puf6_tank");
        public static final LegacyBlock machine_uf6_tank = LegacyBlock.geontm("machine_uf6_tank");
        public static final LegacyBlock nuke_man = LegacyBlock.geontm("nuke_man");
        public static final LegacyBlock ore_nether_plutonium = LegacyBlock.geontm("ore_nether_plutonium");
        public static final LegacyBlock ore_schrabidium = LegacyBlock.geontm("ore_schrabidium");
        public static final LegacyBlock pole_satellite_receiver = LegacyBlock.geontm("pole_satellite_receiver");
        public static final LegacyBlock pole_top = LegacyBlock.geontm("pole_top");
        public static final LegacyBlock pwr_control = LegacyBlock.geontm("pwr_control");
        public static final LegacyBlock pwr_fuel = LegacyBlock.geontm("pwr_fuel");
        public static final LegacyBlock red_cable = LegacyBlock.geontm("red_cable");
        public static final LegacyBlock red_wire_coated = LegacyBlock.geontm("red_wire_coated");
        public static final LegacyBlock reinforced_brick = LegacyBlock.geontm("reinforced_brick");
        public static final LegacyBlock reinforced_glass = LegacyBlock.geontm("reinforced_glass");
        public static final LegacyBlock reinforced_lamp_off = LegacyBlock.geontm("reinforced_lamp_off");
        public static final LegacyBlock reinforced_light = LegacyBlock.geontm("reinforced_light");
        public static final LegacyBlock reinforced_sand = LegacyBlock.geontm("reinforced_sand");
        public static final LegacyBlock reinforced_stone = LegacyBlock.geontm("reinforced_stone");
        public static final LegacyBlock sellafield = LegacyBlock.geontm("sellafield");
        public static final LegacyBlock sellafield_slaked = LegacyBlock.geontm("sellafield_slaked");
        public static final LegacyBlock steel_poles = LegacyBlock.geontm("steel_poles");
        public static final LegacyBlock tape_recorder = LegacyBlock.geontm("tape_recorder");
        public static final LegacyBlock toxic_block = LegacyBlock.geontm("toxic_block");
        public static final LegacyBlock waste_earth = LegacyBlock.geontm("waste_earth");
        public static final LegacyBlock waste_planks = LegacyBlock.geontm("waste_planks");
        public static final LegacyBlock yellow_barrel = LegacyBlock.geontm("yellow_barrel");
    }

    /** 1.7.10 {@code net.minecraft.init.Blocks} (field name = registry name in 1.7.10). */
    public static final class Blocks {
        private Blocks() {}

        public static final LegacyBlock air = LegacyBlock.vanilla("air");
        public static final LegacyBlock bedrock = LegacyBlock.vanilla("bedrock");
        public static final LegacyBlock bookshelf = LegacyBlock.vanilla("bookshelf");
        public static final LegacyBlock chest = LegacyBlock.vanilla("chest");
        public static final LegacyBlock dirt = LegacyBlock.vanilla("dirt");
        public static final LegacyBlock double_wooden_slab = LegacyBlock.vanilla("double_wooden_slab");
        public static final LegacyBlock fence = LegacyBlock.vanilla("fence");
        public static final LegacyBlock flower_pot = LegacyBlock.vanilla("flower_pot");
        public static final LegacyBlock grass = LegacyBlock.vanilla("grass");
        public static final LegacyBlock gravel = LegacyBlock.vanilla("gravel");
        public static final LegacyBlock hardened_clay = LegacyBlock.vanilla("hardened_clay");
        public static final LegacyBlock iron_door = LegacyBlock.vanilla("iron_door");
        public static final LegacyBlock ladder = LegacyBlock.vanilla("ladder");
        public static final LegacyBlock lava = LegacyBlock.vanilla("lava");
        public static final LegacyBlock lever = LegacyBlock.vanilla("lever");
        public static final LegacyBlock mob_spawner = LegacyBlock.vanilla("mob_spawner");
        public static final LegacyBlock oak_stairs = LegacyBlock.vanilla("oak_stairs");
        public static final LegacyBlock obsidian = LegacyBlock.vanilla("obsidian");
        public static final LegacyBlock planks = LegacyBlock.vanilla("planks");
        public static final LegacyBlock redstone_block = LegacyBlock.vanilla("redstone_block");
        public static final LegacyBlock redstone_wire = LegacyBlock.vanilla("redstone_wire");
        public static final LegacyBlock sand = LegacyBlock.vanilla("sand");
        public static final LegacyBlock sandstone = LegacyBlock.vanilla("sandstone");
        public static final LegacyBlock snow_layer = LegacyBlock.vanilla("snow_layer");
        public static final LegacyBlock stained_hardened_clay = LegacyBlock.vanilla("stained_hardened_clay");
        public static final LegacyBlock stone = LegacyBlock.vanilla("stone");
        public static final LegacyBlock stone_brick_stairs = LegacyBlock.vanilla("stone_brick_stairs");
        public static final LegacyBlock stone_stairs = LegacyBlock.vanilla("stone_stairs");
        public static final LegacyBlock stonebrick = LegacyBlock.vanilla("stonebrick");
        public static final LegacyBlock torch = LegacyBlock.vanilla("torch");
        public static final LegacyBlock unpowered_repeater = LegacyBlock.vanilla("unpowered_repeater");
        public static final LegacyBlock vine = LegacyBlock.vanilla("vine");
        public static final LegacyBlock wall_sign = LegacyBlock.vanilla("wall_sign");
        public static final LegacyBlock water = LegacyBlock.vanilla("water");
        public static final LegacyBlock web = LegacyBlock.vanilla("web");
        public static final LegacyBlock wooden_door = LegacyBlock.vanilla("wooden_door");
        public static final LegacyBlock wooden_slab = LegacyBlock.vanilla("wooden_slab");
    }
}
