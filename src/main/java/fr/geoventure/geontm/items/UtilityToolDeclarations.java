package fr.geoventure.geontm.items;

import static fr.geoventure.geocore.dejson.Crafting.rows;
import static fr.geoventure.geocore.dejson.Crafting.shaped;
import static fr.geoventure.geocore.dejson.Crafting.shapeless;
import static fr.geoventure.geocore.dejson.Crafting.stack;

/**
 * Dejson declarations for {@link ModUtilityToolItems}: the 1.7 {@code ToolRecipes} / {@code CraftingManager} recipes
 * of every item that has one. The item models are stated on the registrations in {@code ModUtilityToolItems}, the
 * guide block's model and loot on its registration in {@code ModUtilityToolContent}. Touches no Minecraft class.
 *
 * <p>The holotape restoration and the restored-holotape-to-damaged recipes take one holotape variant only;
 * they are component shapeless recipes in {@link fr.geoventure.geontm.inventory.recipes.ModContainerRecipes}.
 */
public final class UtilityToolDeclarations {

    private static final String NS = "geontm";
    private static boolean declared;

    private UtilityToolDeclarations() {}

    public static void declare() {
        if (declared) return;
        declared = true;
        recipes();
    }

    private static void recipes() {
        // 1.7 ToolRecipes
        shaped(NS, "survey_scanner", "geontm:survey_scanner", rows("SWS", " G ", "PCP"),
                "W", "#c:wires/gold", "P", "#c:ingots/any_plastic", "C", "geontm:circuit_advanced",
                "S", "#c:plates/steel", "G", "#c:ingots/gold");
        shapeless(NS, "digamma_diagnostic", "geontm:digamma_diagnostic",
                "geontm:geiger_counter", "#c:billets/polonium", "#c:ingots/asbestos");
        shaped(NS, "pollution_detector", "geontm:pollution_detector", rows("SFS", "SCS", " S "),
                "S", "#c:plates/steel", "F", "geontm:filter_coal", "C", "geontm:circuit_vacuum_tube");
        shaped(NS, "ore_density_scanner", "geontm:ore_density_scanner", rows("VVV", "CSC", "GGG"),
                "V", "geontm:circuit_vacuum_tube", "C", "geontm:circuit_capacitor", "S", "geontm:circuit_controller_chassis",
                "G", "#c:plates/gold");
        shaped(NS, "coltan_tool", "geontm:coltan_tool", rows("ACA", "CXC", "ACA"),
                "A", "#c:ingots/copper", "C", "#c:gems/cinnabar", "X", "minecraft:compass");
        shaped(NS, "settings_tool", "geontm:settings_tool", rows(" P ", "PCP", "III"),
                "P", "#c:plates/iron", "C", "geontm:circuit_analog", "I", "geontm:plate_polymer");
        shaped(NS, "pipette", "geontm:pipette", rows("  L", " G ", "G  "),
                "L", "#c:ingots/any_rubber", "G", "#c:glass_blocks/colorless");
        shaped(NS, "pipette_boron", "geontm:pipette_boron", rows("  P", " B ", "B  "),
                "P", "#c:ingots/rubber", "B", "geontm:glass_boron");
        shaped(NS, "pipette_laboratory", "geontm:pipette_laboratory", rows("  C", " R ", "P  "),
                "C", "geontm:circuit_chip", "R", "#c:ingots/rubber", "P", "geontm:pipette_boron");
        shaped(NS, "power_net_tool", "geontm:power_net_tool", rows("WRW", " I ", " B "),
                "W", "#c:wires/red_copper", "R", "#c:dusts/redstone", "I", "#c:ingots/iron", "B", "geontm:battery_lead");
        shaped(NS, "analysis_tool", "geontm:analysis_tool", rows("  G", " S ", "S  "),
                "G", "#c:glass_panes", "S", "#c:ingots/steel");

        // 1.7 CraftingManager
        shaped(NS, "polaroid", "geontm:polaroid", rows(" C ", "RPY", " B "),
                "B", "#c:dusts/lapis", "C", "#c:dusts/coal", "R", "#c:dusts/red_copper", "Y", "#c:dusts/gold",
                "P", "minecraft:paper");
        shaped(NS, "containment_box", "geontm:containment_box", rows("LUL", "UCU", "LUL"),
                "L", "#c:plates/lead", "U", "#c:ingots/ferrouranium", "C", "geontm:crate_steel");
        shaped(NS, "rtty_pager", "geontm:rtty_pager", rows("R", "C", "S"),
                "R", "geontm:radio_torch_receiver", "C", "geontm:circuit_basic", "S", "#c:plates/steel");
        shaped(NS, "rtty_designator", "geontm:rtty_designator", rows("R", "C", "T"),
                "R", "geontm:radio_torch_reader", "C", "geontm:circuit_analog", "T", "geontm:rangefinder");
        shaped(NS, "fluid_identifier_multi", "geontm:fluid_identifier_multi", rows("D", "C", "P"),
                "D", "#c:dyes", "C", "geontm:circuit_vacuum_tube", "P", "#c:plates/iron");
        shapeless(NS, "book_guide_rbmk", "geontm:book_guide_rbmk",
                "minecraft:book", "minecraft:potato");
        shapeless(NS, "book_guide_starter", "geontm:book_guide_starter",
                "minecraft:book", "minecraft:iron_ingot");
        shaped(NS, "book_guide_block", "geontm:book_guide_block", rows("IBI", "LBL", "IBI"),
                "B", "minecraft:book", "I", "#c:dyes/black", "L", "#c:dyes/blue");
    }
}
