package fr.geoventure.geontm.items;

import fr.geoventure.geontm.lib.ItemIds;
import fr.geoventure.geontm.lib.TagIds;
import fr.geoventure.geontm.lib.VanillaIds;
import fr.geoventure.geocore.dejson.Tags;

import static fr.geoventure.geocore.dejson.Crafting.rows;
import static fr.geoventure.geocore.dejson.Crafting.shaped;
import static fr.geoventure.geocore.dejson.Crafting.shapeless;

/**
 * Dejson declarations for {@link ModArmorModItems} and {@code insert_era}: the new repair tags and the recipes
 * of 1.7 {@code ConsumableRecipes} and {@code ArmorRecipes}. Touches no Minecraft class. The sprite item models
 * are stated on the registrations (Reg).
 */
public final class ArmorModDeclarations {

    private static final String NS = "geontm";
    private static boolean declared;

    private ArmorModDeclarations() {}

    public static void declare() {
        if (declared) return;
        declared = true;

        // aMatLiquidator.customCraftingMaterial = plate_lead; the mask's material is vanilla iron.
        Tags.item(NS, "repairs/liquidator", ItemIds.plate_lead);
        Tags.item(NS, "repairs/infamy", VanillaIds.iron_ingot);
        // 1.7 red_flower with a wildcard meta: every small flower except the dandelion.
        Tags.item(NS, "red_flowers", VanillaIds.poppy, VanillaIds.blue_orchid, VanillaIds.allium,
                VanillaIds.azure_bluet, VanillaIds.red_tulip, VanillaIds.orange_tulip, VanillaIds.white_tulip,
                VanillaIds.pink_tulip, VanillaIds.oxeye_daisy);

        consumables();
        armor();
    }

    /** 1.7 {@code ConsumableRecipes}, in source order. */
    private static void consumables() {
        final String tape = ItemIds.ducttape;
        shaped(NS, "cladding_obsidian", "geontm:cladding_obsidian", rows("OOO", "PDP", "OOO"),
                "O", VanillaIds.obsidian, "P", TagIds.plates_steel, "D", tape);
        shaped(NS, "cladding_iron", "geontm:cladding_iron", rows("OOO", "PDP", "OOO"),
                "O", TagIds.plates_iron, "P", ItemIds.plate_polymer, "D", tape);
        shaped(NS, "insert_era", "geontm:insert_era", rows("DPD", "PSP", "DPD"),
                "D", tape, "P", TagIds.plates_iron, "S", ItemIds.ingot_semtex);
        shaped(NS, "australium_iii", "geontm:australium_iii", rows("WSW", "PAP", "SPS"),
                "S", TagIds.plates_welded_steel, "P", TagIds.ingots_any_plastic, "A", TagIds.ingots_australium,
                "W", TagIds.wires_dense_gold);
        shaped(NS, "servo_set", "geontm:servo_set", rows("MBM", "PBP", "MBM"),
                "M", ItemIds.motor, "B", TagIds.bolts_steel, "P", TagIds.plates_iron);
        shaped(NS, "servo_set_desh", "geontm:servo_set_desh", rows("MBM", "PSP", "MBM"),
                "M", ItemIds.motor_desh, "B", TagIds.bolts_dura_steel, "P", ItemIds.plate_desh, "S", "geontm:servo_set");
        shaped(NS, "attachment_mask", "geontm:attachment_mask", rows("DID", "IGI", " F "),
                "D", tape, "I", TagIds.ingots_any_rubber, "G", TagIds.glass_panes, "F", TagIds.plates_iron);
        shaped(NS, "attachment_mask_mono", "geontm:attachment_mask_mono", rows(" D ", "DID", " F "),
                "D", tape, "I", TagIds.ingots_any_rubber, "F", TagIds.plates_iron);
        shaped(NS, "pads_rubber", "geontm:pads_rubber", rows("P P", "IDI", "P P"),
                "P", TagIds.ingots_any_rubber, "I", TagIds.plates_iron, "D", tape);
        shaped(NS, "pads_slime", "geontm:pads_slime", rows("SPS", "DSD", "SPS"),
                "S", TagIds.slime_balls, "P", "geontm:pads_rubber", "D", tape);
        shaped(NS, "pads_static", "geontm:pads_static", rows("CDC", "ISI", "CDC"),
                "C", TagIds.ingots_copper, "D", tape, "I", TagIds.ingots_any_rubber, "S", "geontm:pads_slime");
        shaped(NS, "horseshoe_magnet", "geontm:horseshoe_magnet", rows("L L", "I I", "ILI"),
                "L", "geontm:lodestone", "I", TagIds.ingots_iron);
        shaped(NS, "industrial_magnet", "geontm:industrial_magnet", rows("SMS", " B ", "SMS"),
                "S", TagIds.ingots_steel, "M", "geontm:horseshoe_magnet", "B", TagIds.wires_dense_red_copper);
        shaped(NS, "heart_container", "geontm:heart_container", rows("HAH", "ACA", "HAH"),
                "H", "geontm:heart_piece", "A", TagIds.ingots_aluminium, "C", ItemIds.coin_creeper);
        shaped(NS, "heart_booster", "geontm:heart_booster", rows("GHG", "MCM", "GHG"),
                "G", TagIds.ingots_gold, "H", "geontm:heart_container", "M", "geontm:morning_glory", "C", ItemIds.coin_maskman);
        shaped(NS, "heart_fab", "geontm:heart_fab", rows("GHG", "MCM", "GHG"),
                "G", TagIds.billets_polonium, "H", "geontm:heart_booster", "M", TagIds.cokes, "C", ItemIds.coin_worm);
        shaped(NS, "ink", "geontm:ink", rows("FPF", "PIP", "FPF"),
                "F", "#geontm:red_flowers", "P", ItemIds.armor_polish, "I", TagIds.dyes_black);
        shaped(NS, "bathwater_mk2", "geontm:bathwater_mk2", rows("MWM", "WBW", "MWM"),
                "M", ItemIds.bottle_mercury, "W", ItemIds.nuclear_waste, "B", "geontm:bathwater");
        shaped(NS, "back_tesla", "geontm:back_tesla", rows("DGD", "GTG", "DGD"),
                "D", tape, "G", TagIds.wires_gold, "T", ItemIds.tesla);
        shaped(NS, "medal_liquidator", "geontm:medal_liquidator", rows("GBG", "BFB", "GBG"),
                "G", TagIds.nuggets_au198, "B", TagIds.ingots_boron, "F", ItemIds.debris_fuel);
        shapeless(NS, "injector_5htp", "geontm:injector_5htp",
                ItemIds.five_htp, ItemIds.circuit_basic, TagIds.plates_ostrum);
        shapeless(NS, "injector_knife", "geontm:injector_knife", "geontm:injector_5htp", VanillaIds.iron_sword);
        shaped(NS, "shackles", "geontm:shackles", rows("CIC", "C C", "I I"),
                "I", ItemIds.ingot_chainsteel, "C", "geontm:dungeon_chain");
        shaped(NS, "black_diamond", "geontm:black_diamond", rows("NIN", "IGI", "NIN"),
                "N", TagIds.nuggets_au198, "I", "geontm:ink", "G", TagIds.gems_volcanic);
        shaped(NS, "protection_charm", "geontm:protection_charm", rows(" M ", "MDM", " M "),
                "M", ItemIds.fragment_meteorite, "D", TagIds.gems_diamond);
        shaped(NS, "meteor_charm", "geontm:meteor_charm", rows(" M ", "MDM", " M "),
                "M", ItemIds.fragment_meteorite, "D", TagIds.gems_volcanic);
        shaped(NS, "neutrino_lens", "geontm:neutrino_lens", rows("PSP", "SCS", "PSP"),
                "P", TagIds.ingots_any_plastic, "S", TagIds.ingots_starmetal, "C", ItemIds.circuit_bismoid);
        shaped(NS, "gas_tester", "geontm:gas_tester", rows("G", "C", "I"),
                "G", TagIds.plates_gold, "C", ItemIds.circuit_vacuum_tube, "I", TagIds.plates_iron);
        shaped(NS, "ballistic_gauntlet", "geontm:ballistic_gauntlet", rows(" WS", "WRS", " RS"),
                "W", TagIds.wires_copper, "R", ItemIds.ring_starmetal, "S", TagIds.plates_steel);
        shaped(NS, "night_vision", "geontm:night_vision", rows("P P", "GCG"),
                "P", TagIds.ingots_any_plastic, "G", TagIds.glass_blocks, "C", ItemIds.circuit_basic);
    }

    /** 1.7 {@code ArmorRecipes}. */
    private static void armor() {
        shaped(NS, "hazmat_paa_helmet", "geontm:hazmat_paa_helmet", rows("EEE", "IEI", " P "),
                "E", ItemIds.plate_paa, "I", TagIds.glass_panes, "P", TagIds.plates_iron);
        shaped(NS, "hazmat_paa_plate", "geontm:hazmat_paa_plate", rows("E E", "EEE", "EEE"), "E", ItemIds.plate_paa);
        shaped(NS, "hazmat_paa_legs", "geontm:hazmat_paa_legs", rows("EEE", "E E", "E E"), "E", ItemIds.plate_paa);
        shaped(NS, "hazmat_paa_boots", "geontm:hazmat_paa_boots", rows("E E", "E E"), "E", ItemIds.plate_paa);

        shaped(NS, "liquidator_helmet", "geontm:liquidator_helmet", rows("III", "CBC", "III"),
                "I", TagIds.ingots_any_rubber, "C", ItemIds.cladding_lead, "B", ItemIds.hazmat_helmet_grey);
        shaped(NS, "liquidator_plate", "geontm:liquidator_plate", rows("ICI", "TBT", "ICI"),
                "I", TagIds.ingots_any_rubber, "C", ItemIds.cladding_lead, "B", ItemIds.hazmat_plate_grey,
                "T", ItemIds.gas_empty);
        shaped(NS, "liquidator_legs", "geontm:liquidator_legs", rows("III", "CBC", "I I"),
                "I", TagIds.ingots_any_rubber, "C", ItemIds.cladding_lead, "B", ItemIds.hazmat_legs_grey);
        shaped(NS, "liquidator_boots", "geontm:liquidator_boots", rows("ICI", "IBI"),
                "I", TagIds.ingots_any_rubber, "C", ItemIds.cladding_lead, "B", ItemIds.hazmat_boots_grey);

        shaped(NS, "mask_of_infamy", "geontm:mask_of_infamy", rows("III", "III", " I "), "I", TagIds.plates_iron);
    }
}
