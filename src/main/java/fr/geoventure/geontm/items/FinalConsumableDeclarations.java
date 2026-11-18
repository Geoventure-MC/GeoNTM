package fr.geoventure.geontm.items;

import fr.geoventure.geocore.dejson.Dejson;
import fr.geoventure.geocore.dejson.JukeboxSongs;
import fr.geoventure.geocore.dejson.Models;

import static fr.geoventure.geocore.dejson.Crafting.compress;
import static fr.geoventure.geocore.dejson.Crafting.decompress;
import static fr.geoventure.geocore.dejson.Crafting.rows;
import static fr.geoventure.geocore.dejson.Crafting.shaped;
import static fr.geoventure.geocore.dejson.Crafting.shapeless;
import static fr.geoventure.geocore.dejson.Crafting.smelt;

/**
 * Dejson declarations for {@link ModFinalConsumableItems} (whose item models are stated on its registrations,
 * Reg): the four jukebox songs, and every crafting and smelting recipe of 1.7 {@code CraftingManager}, {@code ConsumableRecipes},
 * {@code ToolRecipes}, {@code PowderRecipes}, {@code ArmorRecipes}, {@code WeaponRecipes},
 * {@code MineralRecipes} and {@code SmeltingRecipes} that makes or uses these items and has all its parts in
 * the port. Touches no Minecraft class.
 *
 * <p>{@code chocolate_milk}, {@code bdcl}, {@code gun_kit_1}, {@code gun_kit_2} (filled fluid containers,
 * {@code Fluids.X.getDict(1000)}) and {@code mucho_mango} (the water bottle, {@code Items.potionitem} meta 0)
 * are declared in {@link fr.geoventure.geontm.inventory.recipes.ModContainerRecipes}, whose component shapeless recipe
 * can say those ingredients. Not declared here: the plushies and {@code radiobox} have no ported output.
 */
public final class FinalConsumableDeclarations {

    private static final String NS = "geontm";
    private static boolean declared;

    private FinalConsumableDeclarations() {}

    public static void declare() {
        if (declared) return;
        declared = true;
        songs();
        recipes();
    }

    /** 1.7 ItemModRecord.getRecordResource; the descriptions are 1.7's item.record.X.desc lines. */
    private static void songs() {
        JukeboxSongs.declare(NS, "lc", "geontm:music.record_lambda_core", "item.geontm.record_lc.desc", 104.54F, 15);
        JukeboxSongs.declare(NS, "ss", "geontm:music.record_sector_sweep", "item.geontm.record_ss.desc", 166.50F, 15);
        JukeboxSongs.declare(NS, "vc", "geontm:music.record_vortal_combat", "item.geontm.record_vc.desc", 194.72F, 15);
        JukeboxSongs.declare(NS, "glass", "geontm:music.transmission", "item.geontm.record_glass.desc", 62.23F, 15);
        JukeboxSongs.discs("geontm:record_lc", "geontm:record_ss", "geontm:record_vc", "geontm:record_glass");
    }

    private static void recipes() {
        // 1.7 ConsumableRecipes
        shaped(NS, "bomb_waffle", "geontm:bomb_waffle", rows("WEW", "MPM", "WEW"),
                "W", "minecraft:wheat", "E", "minecraft:egg", "M", "minecraft:milk_bucket", "P", "geontm:man_core");
        shaped(NS, "schnitzel_vegan", "geontm:schnitzel_vegan", 3, rows("RWR", "WPW", "RWR"),
                "W", "geontm:nuclear_waste", "R", "minecraft:sugar_cane", "P", "minecraft:pumpkin_seeds");
        shaped(NS, "cotton_candy", "geontm:cotton_candy", 2, rows(" S ", "SPS", " H "),
                "P", "#c:nuggets/pu239", "S", "minecraft:sugar", "H", "minecraft:stick");
        apples("apple_schrabidium", "schrabidium");
        apples("apple_lead", "lead");
        shaped(NS, "apple_euphemium", "geontm:apple_euphemium", rows("EEE", "EAE", "EEE"),
                "E", "#c:nuggets/euphemium", "A", "minecraft:apple");
        shapeless(NS, "tem_flakes", "geontm:tem_flakes", "#c:nuggets/gold", "minecraft:paper");
        shapeless(NS, "tem_flakes_normal", "geontm:tem_flakes_normal",
                "#c:nuggets/gold", "#c:nuggets/gold", "#c:nuggets/gold", "minecraft:paper");
        shapeless(NS, "tem_flakes_expensive", "geontm:tem_flakes_expensive",
                "#c:ingots/gold", "#c:ingots/gold", "#c:nuggets/gold", "#c:nuggets/gold", "minecraft:paper");
        shapeless(NS, "glowing_stew", "geontm:glowing_stew", "minecraft:bowl", "geontm:mush", "geontm:mush");
        shapeless(NS, "balefire_scrambled", "geontm:balefire_scrambled", "minecraft:bowl", "geontm:egg_balefire");
        shapeless(NS, "balefire_and_ham", "geontm:balefire_and_ham", "geontm:balefire_scrambled", "minecraft:cooked_beef");
        shapeless(NS, "pancake", "geontm:pancake", "#c:dusts/redstone", "#c:dusts/diamond", "minecraft:wheat",
                "#c:bolts/steel", "#c:wires/copper", "#c:plates/steel");
        shapeless(NS, "pancake_1", "geontm:pancake", "#c:dusts/redstone", "#c:dusts/emerald", "minecraft:wheat",
                "#c:bolts/steel", "#c:wires/copper", "#c:plates/steel");
        // marshmallow meta 1 is the roasted marshmallow, dye meta 3 cocoa beans
        shapeless(NS, "ingot_smore", "geontm:ingot_smore", "minecraft:wheat", "geontm:marshmallow_roasted",
                "minecraft:cocoa_beans");
        // 1.7 really does register canned recursion as crafting into itself
        shapeless(NS, "canned_conserve_recursion", "geontm:canned_conserve_recursion", "geontm:canned_conserve_recursion");
        shaped(NS, "peas", "geontm:peas", rows(" S ", "SNS", " S "), "S", "minecraft:wheat_seeds", "N", "#c:nuggets/gold");
        shaped(NS, "canteen_vodka", "geontm:canteen_vodka", rows("O", "P"), "O", "minecraft:potato", "P", "#c:plates/steel");
        shapeless(NS, "five_htp", "geontm:five_htp", "#c:dusts/coal", "#c:dusts/euphemium", "geontm:canteen_vodka");
        shapeless(NS, "cigarette", "geontm:cigarette", 16, "#c:ingots/asbestos", "#c:tars", "#c:nuggets/polonium",
                "geontm:plant_item_tobacco");
        shapeless(NS, "crackpipe", "geontm:crackpipe", "geontm:catalytic_converter");

        // 1.7 PowderRecipes / SmeltingRecipes / MineralRecipes
        shapeless(NS, "powder_fertilizer", "geontm:powder_fertilizer", 4,
                "#c:dusts/calcium", "#c:dusts/red_phosphorus", "#c:dusts/niter", "#c:dusts/sulfur");
        shapeless(NS, "powder_fertilizer_1", "geontm:powder_fertilizer", 4,
                "#c:dusts/ash", "#c:dusts/red_phosphorus", "#c:dusts/niter", "#c:dusts/sulfur");
        smelt(NS, "coke_coal_from_briquette", "geontm:briquette_coal", "geontm:coke_coal", 1.0);
        smelt(NS, "coke_lignite_from_briquette", "geontm:briquette_lignite", "geontm:coke_lignite", 1.0);
        smelt(NS, "charcoal_from_briquette", "geontm:briquette_wood", "minecraft:charcoal", 1.0);
        smelt(NS, "rag_from_rag_damp", "geontm:rag_damp", "geontm:rag", 0.1);
        smelt(NS, "rag_from_rag_piss", "geontm:rag_piss", "geontm:rag", 0.1);
        compress(NS, "block_smore", "geontm:ingot_smore", "geontm:block_smore");
        decompress(NS, "ingot_smore_from_block", "geontm:block_smore", "geontm:ingot_smore");

        // 1.7 ToolRecipes
        shaped(NS, "matchstick", "geontm:matchstick", 16, rows("I", "S"), "I", "#c:dusts/sulfur", "S", "minecraft:stick");
        shaped(NS, "matchstick_1", "geontm:matchstick", 24, rows("I", "S"), "I", "#c:dusts/red_phosphorus",
                "S", "minecraft:stick");
        shaped(NS, "chemistry_set", "geontm:chemistry_set", rows("GIG", "GCG"),
                "G", "#c:glass_blocks", "I", "#c:ingots/iron", "C", "#c:ingots/copper");
        shaped(NS, "chemistry_set_boron", "geontm:chemistry_set_boron", rows("GIG", "GCG"),
                "G", "geontm:glass_boron", "I", "#c:ingots/steel", "C", "#c:ingots/cobalt");

        // 1.7 ArmorRecipes (the rag robes)
        shaped(NS, "robes_helmet", "geontm:robes_helmet", rows("EEE", "E E"), "E", "geontm:rag");
        shaped(NS, "robes_plate", "geontm:robes_plate", rows("E E", "EEE", "EEE"), "E", "geontm:rag");
        shaped(NS, "robes_legs", "geontm:robes_legs", rows("EEE", "E E", "E E"), "E", "geontm:rag");
        shaped(NS, "robes_boots", "geontm:robes_boots", rows("R R", "P P"), "R", "geontm:rag", "P", "#c:ingots/any_rubber");

        // 1.7 WeaponRecipes
        shapeless(NS, "gun_double_barrel_sacred_dragon", "geontm:gun_double_barrel_sacred_dragon",
                "geontm:gun_double_barrel", "geontm:item_secret_selenium_steel");

        // 1.7 CraftingManager
        shaped(NS, "upgrade_muffler", "geontm:upgrade_muffler", 16, rows("III", "IWI", "III"),
                "I", "#c:ingots/any_rubber", "W", "#minecraft:wool");
        shaped(NS, "laser_crystal_dnt", "geontm:laser_crystal_dnt", rows("QDQ", "SBS", "QDQ"),
                "Q", "geontm:glass_quartz", "D", "#c:ingots/dineutronium", "B", "geontm:egg_balefire",
                "S", "geontm:powder_spark_mix");
        shaped(NS, "laser_crystal_digamma", "geontm:laser_crystal_digamma", rows("QUQ", "UEU", "QUQ"),
                "Q", "geontm:glass_quartz", "U", "geontm:undefined", "E", "geontm:ingot_electronium");
        // a crafted potato battery is ItemBattery.getFullBattery: an absent charge reads as full
        shapeless(NS, "battery_potato", "geontm:battery_potato", "minecraft:potato", "#c:wires/aluminium",
                "#c:wires/copper");
        shapeless(NS, "battery_potatos", "geontm:battery_potatos", "geontm:battery_potato", "geontm:turret_chip",
                "#c:dusts/redstone");
        shaped(NS, "record_lc", "geontm:record_lc", rows(" S ", "SDS", " S "),
                "S", "#c:ingots/any_plastic", "D", "#c:dusts/lapis");
        shaped(NS, "record_ss", "geontm:record_ss", rows(" S ", "SDS", " S "),
                "S", "#c:ingots/any_plastic", "D", "#c:dusts/red_copper");
        shaped(NS, "record_vc", "geontm:record_vc", rows(" S ", "SDS", " S "),
                "S", "#c:ingots/any_plastic", "D", "#c:dusts/combine_steel");
        shaped(NS, "rag", "geontm:rag", 4, rows("SW", "WS"), "S", "minecraft:string", "W", "#minecraft:wool");
        shapeless(NS, "parts_legendary_tier1", "geontm:parts_legendary_tier1",
                "geontm:ingot_chainsteel", "#c:ingots/asbestos", "geontm:gem_alexandrite");
        shapeless(NS, "parts_legendary_tier1_from_tier2", "geontm:parts_legendary_tier1", 3, "geontm:parts_legendary_tier2");
        shapeless(NS, "parts_legendary_tier2", "geontm:parts_legendary_tier2",
                "geontm:ingot_chainsteel", "geontm:ingot_bismuth", "geontm:gem_alexandrite", "geontm:gem_alexandrite");
        shapeless(NS, "parts_legendary_tier2_from_tier3", "geontm:parts_legendary_tier2", 3, "geontm:parts_legendary_tier3");
        shapeless(NS, "parts_legendary_tier3", "geontm:parts_legendary_tier3", "geontm:ingot_chainsteel",
                "geontm:ingot_smore", "geontm:gem_alexandrite", "geontm:gem_alexandrite", "geontm:gem_alexandrite");
        shapeless(NS, "upgrade_5g", "geontm:upgrade_5g", "geontm:upgrade_template", "geontm:gem_alexandrite");
        shapeless(NS, "book_of_", "geontm:book_of_", "geontm:page_of_page1", "geontm:page_of_page2", "geontm:page_of_page3",
                "geontm:page_of_page4", "geontm:page_of_page5", "geontm:page_of_page6", "geontm:page_of_page7",
                "geontm:page_of_page8", "geontm:egg_balefire");

        // 1.7 CraftingManager:1004-1043, the StarControl circuit from its pieces
        String p = "geontm:circuit_star_piece_";
        String c = "geontm:circuit_star_component_";
        shapeless(NS, "circuit_star_component_chipset", c + "chipset", p + "bridge_bios", p + "bridge_bus",
                p + "bridge_chipset", p + "bridge_cmos", p + "bridge_io", p + "bridge_north", p + "bridge_south");
        shapeless(NS, "circuit_star_component_cpu", c + "cpu", p + "cpu_cache", p + "cpu_clock", p + "cpu_ext",
                p + "cpu_logic", p + "cpu_register", p + "cpu_socket");
        shapeless(NS, "circuit_star_component_ram", c + "ram", p + "mem_socket", p + "mem_16k_a", p + "mem_16k_b",
                p + "mem_16k_c", p + "mem_16k_d");
        shapeless(NS, "circuit_star_component_card", c + "card", p + "card_board", p + "card_processor");
        shapeless(NS, "circuit_star", "geontm:circuit_star", c + "chipset", c + "cpu", c + "ram", c + "card",
                p + "board_transistor", p + "board_converter", p + "board_blank");
    }

    /** 1.7 ConsumableRecipes:42-47: an apple ringed by nuggets, ingots or blocks gives meta 0, 1 or 2. */
    private static void apples(String id, String material) {
        shaped(NS, id, "geontm:" + id, rows("SSS", "SAS", "SSS"), "S", "#c:nuggets/" + material, "A", "minecraft:apple");
        shaped(NS, id + "_ingot", "geontm:" + id + "_ingot", rows("SSS", "SAS", "SSS"),
                "S", "#c:ingots/" + material, "A", "minecraft:apple");
        shaped(NS, id + "_block", "geontm:" + id + "_block", rows("SSS", "SAS", "SSS"),
                "S", "#c:storage_blocks/" + material, "A", "minecraft:apple");
    }
}
