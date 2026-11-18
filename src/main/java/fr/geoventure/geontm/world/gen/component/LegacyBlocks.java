package fr.geoventure.geontm.world.gen.component;

/**
 * The 1.7 {@code ModBlocks} and {@code Blocks} fields the hardcoded structure builders reference, as {@link LegacyBlock}s
 * named by their 1.7 registry names (the field name where it differs is noted), transcribed from 1.7
 * {@code ModBlocks.java}'s {@code setBlockName} calls; field names are kept so the ported builders read like 1.7's.
 */
public final class LegacyBlocks {

    private LegacyBlocks() {}

    /** 1.7 {@code fr.geoventure.geontm.blocks.ModBlocks}. */
    public static final class Mod {
        private Mod() {}

        public static final LegacyBlock anvil_iron = LegacyBlock.geontm("anvil_iron");
        public static final LegacyBlock asphalt = LegacyBlock.geontm("asphalt");
        public static final LegacyBlock barbed_wire = LegacyBlock.geontm("barbed_wire");
        public static final LegacyBlock barrel_corroded = LegacyBlock.geontm("barrel_corroded");
        public static final LegacyBlock barrel_plastic = LegacyBlock.geontm("barrel_plastic");
        public static final LegacyBlock block_copper = LegacyBlock.geontm("block_copper");
        public static final LegacyBlock block_lead = LegacyBlock.geontm("block_lead");
        public static final LegacyBlock bobblehead = LegacyBlock.geontm("bobblehead");
        public static final LegacyBlock brick_concrete = LegacyBlock.geontm("brick_concrete");
        public static final LegacyBlock brick_concrete_broken = LegacyBlock.geontm("brick_concrete_broken");
        public static final LegacyBlock brick_concrete_broken_stairs = LegacyBlock.geontm("brick_concrete_broken_stairs");
        public static final LegacyBlock brick_concrete_cracked = LegacyBlock.geontm("brick_concrete_cracked");
        public static final LegacyBlock brick_concrete_cracked_stairs = LegacyBlock.geontm("brick_concrete_cracked_stairs");
        public static final LegacyBlock brick_concrete_mossy = LegacyBlock.geontm("brick_concrete_mossy");
        public static final LegacyBlock brick_concrete_mossy_stairs = LegacyBlock.geontm("brick_concrete_mossy_stairs");
        public static final LegacyBlock brick_concrete_stairs = LegacyBlock.geontm("brick_concrete_stairs");
        public static final LegacyBlock brick_light = LegacyBlock.geontm("brick_light");
        public static final LegacyBlock brick_slab = LegacyBlock.geontm("brick_slab");
        public static final LegacyBlock cable_detector = LegacyBlock.geontm("cable_detector");
        public static final LegacyBlock cable_diode = LegacyBlock.geontm("cable_diode");
        public static final LegacyBlock cable_switch = LegacyBlock.geontm("cable_switch");
        public static final LegacyBlock capacitor_copper = LegacyBlock.geontm("capacitor_copper");
        public static final LegacyBlock concrete = LegacyBlock.geontm("concrete");
        public static final LegacyBlock concrete_brick_slab = LegacyBlock.geontm("concrete_brick_slab");
        public static final LegacyBlock concrete_colored = LegacyBlock.geontm("concrete_colored");
        public static final LegacyBlock concrete_colored_ext = LegacyBlock.geontm("concrete_colored_ext");
        public static final LegacyBlock concrete_pillar = LegacyBlock.geontm("concrete_pillar");
        public static final LegacyBlock concrete_slab = LegacyBlock.geontm("concrete_slab");
        public static final LegacyBlock concrete_smooth = LegacyBlock.geontm("concrete_smooth");
        public static final LegacyBlock concrete_smooth_stairs = LegacyBlock.geontm("concrete_smooth_stairs");
        public static final LegacyBlock concrete_stairs = LegacyBlock.geontm("concrete_stairs");
        public static final LegacyBlock concrete_super = LegacyBlock.geontm("concrete_super");
        public static final LegacyBlock concrete_super_broken = LegacyBlock.geontm("concrete_super_broken");
        public static final LegacyBlock crashed_balefire = LegacyBlock.geontm("crashed_bomb");  // registered as tile.crashed_bomb
        public static final LegacyBlock crate = LegacyBlock.geontm("crate");
        public static final LegacyBlock crate_ammo = LegacyBlock.geontm("crate_ammo");
        public static final LegacyBlock crate_can = LegacyBlock.geontm("crate_can");
        public static final LegacyBlock crate_iron = LegacyBlock.geontm("crate_iron");
        public static final LegacyBlock crate_lead = LegacyBlock.geontm("crate_lead");
        public static final LegacyBlock crate_metal = LegacyBlock.geontm("crate_metal");
        public static final LegacyBlock crate_steel = LegacyBlock.geontm("crate_steel");
        public static final LegacyBlock crate_weapon = LegacyBlock.geontm("crate_weapon");
        public static final LegacyBlock deco_beryllium = LegacyBlock.geontm("deco_beryllium");
        public static final LegacyBlock deco_computer = LegacyBlock.geontm("deco_computer");
        public static final LegacyBlock deco_crt = LegacyBlock.geontm("deco_crt");
        public static final LegacyBlock deco_lead = LegacyBlock.geontm("deco_lead");
        public static final LegacyBlock deco_loot = LegacyBlock.geontm("deco_loot");
        public static final LegacyBlock deco_pipe_framed_green_rusted = LegacyBlock.geontm("deco_pipe_framed_green_rusted");
        public static final LegacyBlock deco_pipe_framed_rusted = LegacyBlock.geontm("deco_pipe_framed_rusted");
        public static final LegacyBlock deco_pipe_quad_red = LegacyBlock.geontm("deco_pipe_quad_red");
        public static final LegacyBlock deco_pipe_quad_rusted = LegacyBlock.geontm("deco_pipe_quad_rusted");
        public static final LegacyBlock deco_pipe_rim = LegacyBlock.geontm("deco_pipe_rim");
        public static final LegacyBlock deco_pipe_rim_green_rusted = LegacyBlock.geontm("deco_pipe_rim_green_rusted");
        public static final LegacyBlock deco_pipe_rim_rusted = LegacyBlock.geontm("deco_pipe_rim_rusted");
        public static final LegacyBlock deco_red_copper = LegacyBlock.geontm("deco_red_copper");
        public static final LegacyBlock deco_steel = LegacyBlock.geontm("deco_steel");
        public static final LegacyBlock deco_titanium = LegacyBlock.geontm("deco_titanium");
        public static final LegacyBlock deco_toaster = LegacyBlock.geontm("deco_toaster");
        public static final LegacyBlock deco_tungsten = LegacyBlock.geontm("deco_tungsten");
        public static final LegacyBlock door_bunker = LegacyBlock.geontm("door_bunker");
        public static final LegacyBlock door_metal = LegacyBlock.geontm("door_metal");
        public static final LegacyBlock door_office = LegacyBlock.geontm("door_office");
        public static final LegacyBlock fan = LegacyBlock.geontm("fan");
        public static final LegacyBlock fence_metal = LegacyBlock.geontm("fence_metal");
        public static final LegacyBlock filing_cabinet = LegacyBlock.geontm("filing_cabinet");
        public static final LegacyBlock fluid_duct_gauge = LegacyBlock.geontm("fluid_duct_gauge");
        public static final LegacyBlock hadron_coil_alloy = LegacyBlock.geontm("hadron_coil_alloy");
        public static final LegacyBlock hev_battery = LegacyBlock.geontm("hev_battery");
        public static final LegacyBlock ladder_steel = LegacyBlock.geontm("ladder_steel");
        public static final LegacyBlock ladder_sturdy = LegacyBlock.geontm("ladder_sturdy");
        public static final LegacyBlock launch_pad_rusted = LegacyBlock.geontm("launch_pad_rusted");
        public static final LegacyBlock lox_barrel = LegacyBlock.geontm("lox_barrel");
        public static final LegacyBlock machine_boiler_off = LegacyBlock.geontm("machine_boiler_off");
        public static final LegacyBlock machine_diesel = LegacyBlock.geontm("machine_diesel");
        public static final LegacyBlock machine_electric_furnace_off = LegacyBlock.geontm("machine_electric_furnace_off");
        public static final LegacyBlock machine_microwave = LegacyBlock.geontm("machine_microwave");
        public static final LegacyBlock machine_shredder = LegacyBlock.geontm("machine_shredder");
        public static final LegacyBlock machine_transformer = LegacyBlock.geontm("machine_transformer");
        public static final LegacyBlock machine_condenser = LegacyBlock.geontm("machine_condenser");
        public static final LegacyBlock meteor_brick = LegacyBlock.geontm("meteor_brick");
        public static final LegacyBlock meteor_brick_cracked = LegacyBlock.geontm("meteor_brick_cracked");
        public static final LegacyBlock meteor_brick_mossy = LegacyBlock.geontm("meteor_brick_mossy");
        public static final LegacyBlock meteor_polished = LegacyBlock.geontm("meteor_polished");
        public static final LegacyBlock meteor_spawner = LegacyBlock.geontm("meteor_spawner");
        public static final LegacyBlock mine_ap = LegacyBlock.geontm("mine_ap");
        public static final LegacyBlock pink_barrel = LegacyBlock.geontm("pink_barrel");
        public static final LegacyBlock plant_flower = LegacyBlock.geontm("plant_flower");
        public static final LegacyBlock pwr_channel = LegacyBlock.geontm("pwr_channel");
        public static final LegacyBlock pwr_control = LegacyBlock.geontm("pwr_control");
        public static final LegacyBlock pwr_fuel = LegacyBlock.geontm("pwr_fuel");
        public static final LegacyBlock radio_telex = LegacyBlock.geontm("radio_telex");
        public static final LegacyBlock radio_torch_receiver = LegacyBlock.geontm("radio_torch_receiver");
        public static final LegacyBlock radio_torch_sender = LegacyBlock.geontm("radio_torch_sender");
        public static final LegacyBlock radiorec = LegacyBlock.geontm("radiorec");
        public static final LegacyBlock red_barrel = LegacyBlock.geontm("red_barrel");
        public static final LegacyBlock red_cable_gauge = LegacyBlock.geontm("red_cable_gauge");
        public static final LegacyBlock reeds = LegacyBlock.geontm("plant_reeds");  // registered as tile.plant_reeds
        public static final LegacyBlock reinforced_glass = LegacyBlock.geontm("reinforced_glass");
        public static final LegacyBlock reinforced_lamp_off = LegacyBlock.geontm("reinforced_lamp_off");
        public static final LegacyBlock reinforced_sand = LegacyBlock.geontm("reinforced_sand");
        public static final LegacyBlock reinforced_stone = LegacyBlock.geontm("reinforced_stone");
        public static final LegacyBlock reinforced_stone_stairs = LegacyBlock.geontm("reinforced_stone_stairs");
        public static final LegacyBlock safe = LegacyBlock.geontm("safe");
        public static final LegacyBlock silo_hatch_large = LegacyBlock.geontm("silo_hatch_large");
        public static final LegacyBlock steel_beam = LegacyBlock.geontm("steel_beam");
        public static final LegacyBlock steel_corner = LegacyBlock.geontm("steel_corner");
        public static final LegacyBlock steel_grate = LegacyBlock.geontm("steel_grate");
        public static final LegacyBlock steel_poles = LegacyBlock.geontm("steel_poles");
        public static final LegacyBlock steel_roof = LegacyBlock.geontm("steel_roof");
        public static final LegacyBlock steel_scaffold = LegacyBlock.geontm("steel_scaffold");
        public static final LegacyBlock steel_wall = LegacyBlock.geontm("steel_wall");
        public static final LegacyBlock tape_recorder = LegacyBlock.geontm("tape_recorder");
        public static final LegacyBlock tile_lab = LegacyBlock.geontm("tile_lab");
        public static final LegacyBlock tile_lab_broken = LegacyBlock.geontm("tile_lab_broken");
        public static final LegacyBlock tile_lab_cracked = LegacyBlock.geontm("tile_lab_cracked");
        public static final LegacyBlock toxic_block = LegacyBlock.geontm("toxic_block");
        public static final LegacyBlock trapdoor_steel = LegacyBlock.geontm("trapdoor_steel");
        public static final LegacyBlock turret_howard_damaged = LegacyBlock.geontm("turret_howard_damaged");
        public static final LegacyBlock turret_sentry_damaged = LegacyBlock.geontm("turret_sentry_damaged");
        public static final LegacyBlock vinyl_tile = LegacyBlock.geontm("vinyl_tile");
        public static final LegacyBlock vitrified_barrel = LegacyBlock.geontm("vitrified_barrel");
        public static final LegacyBlock waste_planks = LegacyBlock.geontm("waste_planks");
    }

    /** 1.7.10 {@code net.minecraft.init.Blocks}. */
    public static final class Vanilla {
        private Vanilla() {}

        public static final LegacyBlock air = LegacyBlock.vanilla("air");
        public static final LegacyBlock bed = LegacyBlock.vanilla("bed");
        public static final LegacyBlock bookshelf = LegacyBlock.vanilla("bookshelf");
        public static final LegacyBlock brick_block = LegacyBlock.vanilla("brick_block");
        public static final LegacyBlock carpet = LegacyBlock.vanilla("carpet");
        public static final LegacyBlock cauldron = LegacyBlock.vanilla("cauldron");
        public static final LegacyBlock chest = LegacyBlock.vanilla("chest");
        public static final LegacyBlock cocoa = LegacyBlock.vanilla("cocoa");
        public static final LegacyBlock crafting_table = LegacyBlock.vanilla("crafting_table");
        public static final LegacyBlock dark_oak_stairs = LegacyBlock.vanilla("dark_oak_stairs");
        public static final LegacyBlock dirt = LegacyBlock.vanilla("dirt");
        public static final LegacyBlock dispenser = LegacyBlock.vanilla("dispenser");
        public static final LegacyBlock double_stone_slab = LegacyBlock.vanilla("double_stone_slab");
        public static final LegacyBlock end_portal_frame = LegacyBlock.vanilla("end_portal_frame");
        public static final LegacyBlock fence = LegacyBlock.vanilla("fence");
        public static final LegacyBlock fence_gate = LegacyBlock.vanilla("fence_gate");
        public static final LegacyBlock flower_pot = LegacyBlock.vanilla("flower_pot");
        public static final LegacyBlock furnace = LegacyBlock.vanilla("furnace");
        public static final LegacyBlock glass_pane = LegacyBlock.vanilla("glass_pane");
        public static final LegacyBlock heavy_weighted_pressure_plate = LegacyBlock.vanilla("heavy_weighted_pressure_plate");
        public static final LegacyBlock hopper = LegacyBlock.vanilla("hopper");
        public static final LegacyBlock iron_bars = LegacyBlock.vanilla("iron_bars");
        public static final LegacyBlock iron_door = LegacyBlock.vanilla("iron_door");
        public static final LegacyBlock ladder = LegacyBlock.vanilla("ladder");
        public static final LegacyBlock lever = LegacyBlock.vanilla("lever");
        public static final LegacyBlock lit_pumpkin = LegacyBlock.vanilla("lit_pumpkin");
        public static final LegacyBlock log = LegacyBlock.vanilla("log");
        public static final LegacyBlock nether_brick_stairs = LegacyBlock.vanilla("nether_brick_stairs");
        public static final LegacyBlock noteblock = LegacyBlock.vanilla("noteblock");
        public static final LegacyBlock oak_stairs = LegacyBlock.vanilla("oak_stairs");
        public static final LegacyBlock piston = LegacyBlock.vanilla("piston");
        public static final LegacyBlock planks = LegacyBlock.vanilla("planks");
        public static final LegacyBlock powered_comparator = LegacyBlock.vanilla("powered_comparator");
        public static final LegacyBlock powered_repeater = LegacyBlock.vanilla("powered_repeater");
        public static final LegacyBlock pumpkin = LegacyBlock.vanilla("pumpkin");
        public static final LegacyBlock rail = LegacyBlock.vanilla("rail");
        public static final LegacyBlock redstone_lamp = LegacyBlock.vanilla("redstone_lamp");
        public static final LegacyBlock sand = LegacyBlock.vanilla("sand");
        public static final LegacyBlock sandstone = LegacyBlock.vanilla("sandstone");
        public static final LegacyBlock sandstone_stairs = LegacyBlock.vanilla("sandstone_stairs");
        public static final LegacyBlock spruce_stairs = LegacyBlock.vanilla("spruce_stairs");
        public static final LegacyBlock sticky_piston = LegacyBlock.vanilla("sticky_piston");
        public static final LegacyBlock stone_brick_stairs = LegacyBlock.vanilla("stone_brick_stairs");
        public static final LegacyBlock stone_button = LegacyBlock.vanilla("stone_button");
        public static final LegacyBlock stone_slab = LegacyBlock.vanilla("stone_slab");
        public static final LegacyBlock stone_stairs = LegacyBlock.vanilla("stone_stairs");
        public static final LegacyBlock stonebrick = LegacyBlock.vanilla("stonebrick");
        public static final LegacyBlock trapdoor = LegacyBlock.vanilla("trapdoor");
        public static final LegacyBlock tripwire_hook = LegacyBlock.vanilla("tripwire_hook");
        public static final LegacyBlock unpowered_comparator = LegacyBlock.vanilla("unpowered_comparator");
        public static final LegacyBlock unpowered_repeater = LegacyBlock.vanilla("unpowered_repeater");
        public static final LegacyBlock water = LegacyBlock.vanilla("water");
        public static final LegacyBlock web = LegacyBlock.vanilla("web");
        public static final LegacyBlock wooden_door = LegacyBlock.vanilla("wooden_door");
        public static final LegacyBlock wooden_pressure_plate = LegacyBlock.vanilla("wooden_pressure_plate");
        public static final LegacyBlock wooden_slab = LegacyBlock.vanilla("wooden_slab");
        public static final LegacyBlock wool = LegacyBlock.vanilla("wool");
    }
}
