package fr.geoventure.geontm.items;

import fr.geoventure.geontm.items.special.ItemHot;
import fr.geoventure.geontm.lib.ItemIds;
import fr.geoventure.geontm.lib.TagIds;
import fr.geoventure.geontm.lib.VanillaIds;
import fr.geoventure.geocore.dejson.Tags;

import static fr.geoventure.geocore.dejson.Crafting.componentSmelt;
import static fr.geoventure.geocore.dejson.Crafting.item;
import static fr.geoventure.geocore.dejson.Crafting.requires;
import static fr.geoventure.geocore.dejson.Crafting.rows;
import static fr.geoventure.geocore.dejson.Crafting.shaped;
import static fr.geoventure.geocore.dejson.Crafting.smelt;
import static fr.geoventure.geocore.dejson.Crafting.stack;

/**
 * Dejson declarations for the ability tool family ({@link ModAbilityToolItems}): the repair tags of the 1.7 tool
 * materials and the crafting and furnace recipes of 1.7 {@code ToolRecipes}, {@code WeaponRecipes},
 * {@code CraftingManager} and {@code SmeltingRecipes}. The item models and the vanilla tool-class item tags
 * (enchanting and anvil applicability) are stated on the registrations. Touches no Minecraft class.
 */
public final class AbilityToolDeclarations {

    private static final String NS = "geontm";
    private static boolean declared;

    private AbilityToolDeclarations() {}

    public static void declare() {
        if (declared) return;
        declared = true;

        // The tool-class tags' members are stated on the registrations in ModAbilityToolItems; minecraft:swords also
        // nests the WeaponSpecial family's tag, whose members ModMeleeKitItems states.
        Tags.item("minecraft", "swords", "#geontm:melee_kit_swords");

        // Repair items of the 1.7 materials that the existing repair tags do not already name.
        Tags.item(NS, "repairs/cmb", ItemIds.ingot_combine_steel);
        Tags.item(NS, "repairs/desh", ItemIds.ingot_desh);
        Tags.item(NS, "repairs/bismuth", ItemIds.ingot_bismuth);
        Tags.item(NS, "repairs/chlorophyte", ItemIds.powder_chlorophyte);
        Tags.item(NS, "repairs/copper", VanillaIds.copper_ingot);
        Tags.item(NS, "repairs/cobalt_ingot", ItemIds.ingot_cobalt);
        Tags.item(NS, "repairs/starmetal_ingot", ItemIds.ingot_starmetal);

        recipes();
    }

    private static void recipes() {
        String stick = VanillaIds.stick;

        // 1.7 ToolRecipes: regular tools
        toolSet("steel", TagIds.ingots_steel, stick);
        toolSet("titanium", TagIds.ingots_titanium, stick);
        toolSet("cobalt", TagIds.ingots_cobalt, stick);
        toolSet("cmb", TagIds.ingots_combine_steel, stick);
        toolSet("desh", TagIds.ingots_desh, stick);

        String plastic = TagIds.ingots_any_plastic;
        String duraBolt = TagIds.bolts_dura_steel;
        shaped(NS, "elec_sword", "geontm:elec_sword", rows("RPR", "RPR", " B "),
                "P", plastic, "R", duraBolt, "B", ItemIds.battery_lead);
        shaped(NS, "elec_pickaxe", "geontm:elec_pickaxe", rows("RDM", " PB", " P "),
                "P", plastic, "D", TagIds.ingots_dura_steel, "R", duraBolt, "M", ItemIds.motor, "B", ItemIds.battery_lead);
        shaped(NS, "elec_axe", "geontm:elec_axe", rows(" DP", "RRM", " PB"),
                "P", plastic, "D", TagIds.ingots_dura_steel, "R", duraBolt, "M", ItemIds.motor, "B", ItemIds.battery_lead);
        shaped(NS, "elec_shovel", "geontm:elec_shovel", rows("  P", "RRM", "  B"),
                "P", plastic, "R", duraBolt, "M", ItemIds.motor, "B", ItemIds.battery_lead);
        shaped(NS, "smashing_hammer", "geontm:smashing_hammer", rows("STS", "SPS", " P "),
                "S", TagIds.storage_blocks_steel, "T", TagIds.storage_blocks_tungsten, "P", plastic);
        shaped(NS, "meteorite_sword", "geontm:meteorite_sword", rows("  B", "GB ", "SG "),
                "B", "geontm:blade_meteorite", "G", TagIds.plates_gold, "S", stick);
        shaped(NS, "dwarven_pickaxe", "geontm:dwarven_pickaxe", rows("CIC", " S ", " S "),
                "C", TagIds.ingots_copper, "I", TagIds.ingots_iron, "S", stick);

        // Super pickaxes and axes
        for (String kind : new String[]{"pickaxe", "axe"}) {
            shaped(NS, "bismuth_" + kind, "geontm:bismuth_" + kind, rows(" BM", "BPB", "TB "),
                    "B", TagIds.ingots_bismuth, "M", "geontm:ingot_meteorite", "P", "geontm:starmetal_" + kind, "T", TagIds.bolts_tungsten);
            shaped(NS, "volcanic_" + kind, "geontm:volcanic_" + kind, rows(" BM", "BPB", "TB "),
                    "B", ItemIds.gem_volcanic, "M", "geontm:ingot_meteorite", "P", "geontm:starmetal_" + kind, "T", TagIds.bolts_tungsten);
            shaped(NS, "chlorophyte_" + kind, "geontm:chlorophyte_" + kind, rows(" SD", "APS", "FA "),
                    "S", ItemIds.blades_steel, "D", ItemIds.powder_chlorophyte, "A", TagIds.ingots_fiberglass,
                    "P", "geontm:bismuth_" + kind, "F", duraBolt);
            shaped(NS, "chlorophyte_" + kind + "_1", "geontm:chlorophyte_" + kind, rows(" SD", "APS", "FA "),
                    "S", ItemIds.blades_steel, "D", ItemIds.powder_chlorophyte, "A", TagIds.ingots_fiberglass,
                    "P", "geontm:volcanic_" + kind, "F", duraBolt);
            shaped(NS, "mese_" + kind, "geontm:mese_" + kind, rows(" SD", "APS", "FA "),
                    "S", ItemIds.blades_desh, "D", ItemIds.powder_dineutronium, "A", ItemIds.plate_paa,
                    "P", "geontm:chlorophyte_" + kind, "F", ItemIds.shimmer_handle);
        }

        // Chainsaw: crafted empty (1.7 ItemToolAbilityFueled.getEmptyTool). 1.7's ModBlocks.chain is the
        // port's chain tag.
        shaped(NS, "chainsaw", stack("geontm:chainsaw").with("geontm:tool_fuel", 0), rows("CCH", "BBP", "CCE"),
                "H", TagIds.shells_steel, "B", ItemIds.blades_steel, "P", ItemIds.piston_selenium,
                "C", TagIds.chains, "E", ItemIds.canister_empty);

        // Configged: the default (non-LBSM) upgrade recipes
        String star = TagIds.ingots_starmetal;
        String ring = ItemIds.ring_starmetal;
        requires(shaped(NS, "starmetal_sword", "geontm:starmetal_sword", rows(" I ", " B ", "ISI"),
                "I", star, "S", ring, "B", "geontm:cobalt_decorated_sword"), "!lbsm_simple_tool");
        requires(shaped(NS, "starmetal_pickaxe", "geontm:starmetal_pickaxe", rows("ISI", " B ", " I "),
                "I", star, "S", ring, "B", "geontm:cobalt_decorated_pickaxe"), "!lbsm_simple_tool");
        requires(shaped(NS, "starmetal_axe", "geontm:starmetal_axe", rows("IS", "IB", " I"),
                "I", star, "S", ring, "B", "geontm:cobalt_decorated_axe"), "!lbsm_simple_tool");
        requires(shaped(NS, "starmetal_shovel", "geontm:starmetal_shovel", rows("I", "B", "I"),
                "I", star, "B", "geontm:cobalt_decorated_shovel"), "!lbsm_simple_tool");
        requires(shaped(NS, "starmetal_hoe", "geontm:starmetal_hoe", rows("IS", " B", " I"),
                "I", star, "S", ring, "B", "geontm:cobalt_decorated_hoe"), "!lbsm_simple_tool");
        String schrabBlock = TagIds.storage_blocks_schrabidium;
        requires(shaped(NS, "schrabidium_sword", "geontm:schrabidium_sword", rows("I", "W", "S"),
                "I", schrabBlock, "W", "geontm:desh_sword", "S", plastic), "!lbsm_simple_tool");
        requires(shaped(NS, "schrabidium_pickaxe", "geontm:schrabidium_pickaxe", rows("BSB", " W ", " P "),
                "B", ItemIds.blades_desh, "S", schrabBlock, "W", "geontm:desh_pickaxe", "P", plastic), "!lbsm_simple_tool");
        requires(shaped(NS, "schrabidium_axe", "geontm:schrabidium_axe", rows("BS", "BW", " P"),
                "B", ItemIds.blades_desh, "S", schrabBlock, "W", "geontm:desh_axe", "P", plastic), "!lbsm_simple_tool");
        requires(shaped(NS, "schrabidium_shovel", "geontm:schrabidium_shovel", rows("B", "W", "P"),
                "B", schrabBlock, "W", "geontm:desh_shovel", "P", plastic), "!lbsm_simple_tool");
        requires(shaped(NS, "schrabidium_hoe", "geontm:schrabidium_hoe", rows("IW", " S", " S"),
                "I", TagIds.ingots_schrabidium, "W", "geontm:desh_hoe", "S", plastic), "!lbsm_simple_tool");

        // 1.7 CraftingManager:97
        shaped(NS, "redstone_sword", "geontm:redstone_sword", rows("R", "R", "S"),
                "R", TagIds.storage_blocks_redstone, "S", stick);

        // 1.12.2 WeaponRecipes:334: new ItemStack(crucible, 1, 3): item damage 3 and no charges (an empty crucible).
        shaped(NS, "crucible", stack("geontm:crucible").with("minecraft:damage", 3), rows("MEM", "YDY", "YCY"),
                "M", "geontm:ingot_meteorite_forged", "E", TagIds.ingots_euphemium, "Y", ItemIds.billet_yharonite,
                "D", ItemIds.demon_core_closed, "C", ItemIds.ingot_chainsteel);

        // 1.7 SmeltingRecipes:168-171: the furnace reheats the meteorite materials (ItemHot.heatUp), and
        // sears the sword. heatUp on the seared sword (not an ItemHot) did nothing, so that one is plain.
        // 1.7 ItemHot.heat is static, so the last constructor (ingot_chainsteel, 100) wins and heatUp writes 100 for
        // every hot item although the meteorite items were constructed with 200 (see LEGACY_HEAT).
        for (String id : new String[]{"ingot_meteorite", "ingot_meteorite_forged", "blade_meteorite"}) {
            componentSmelt(NS, id + "_heat", item("geontm:" + id),
                    stack("geontm:" + id).with("geontm:heat", ItemHot.LEGACY_HEAT), 0.0);
        }
        smelt(NS, "meteorite_sword_seared", "geontm:meteorite_sword", "geontm:meteorite_sword_seared", 0.0);
    }

    /** 1.7 {@code ToolRecipes.addSword/addPickaxe/addAxe/addShovel/addHoe}: X the material, # a stick. */
    private static void toolSet(String set, String ingot, String stick) {
        shaped(NS, set + "_sword", "geontm:" + set + "_sword", rows("X", "X", "#"), "X", ingot, "#", stick);
        shaped(NS, set + "_pickaxe", "geontm:" + set + "_pickaxe", rows("XXX", " # ", " # "), "X", ingot, "#", stick);
        shaped(NS, set + "_axe", "geontm:" + set + "_axe", rows("XX", "X#", " #"), "X", ingot, "#", stick);
        shaped(NS, set + "_shovel", "geontm:" + set + "_shovel", rows("X", "#", "#"), "X", ingot, "#", stick);
        shaped(NS, set + "_hoe", "geontm:" + set + "_hoe", rows("XX", " #", " #"), "X", ingot, "#", stick);
    }
}
