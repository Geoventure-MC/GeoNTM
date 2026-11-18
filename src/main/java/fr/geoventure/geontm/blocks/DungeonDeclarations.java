package fr.geoventure.geontm.blocks;

import static fr.geoventure.geocore.dejson.Crafting.rows;
import static fr.geoventure.geocore.dejson.Crafting.shaped;

/**
 * The dungeon family's recipes; every block's models, blockstate, item model, loot and tags are stated on its
 * registration in {@link ModDungeonBlocks} (Reg), the rebar placer's item model on its own in {@code ModDungeonItems}.
 * Touches no Minecraft class: it runs in a plain JVM from {@code dejson.gradle.kts}.
 */
public final class DungeonDeclarations {

    private static final String NS = "geontm";
    private static boolean declared;

    private DungeonDeclarations() {}

    public static void declare() {
        if (declared) return;
        declared = true;
        recipes();
    }

    private static void recipes() {
        // 1.7 CraftingManager
        shaped(NS, "rebar", "geontm:rebar", 8, rows("BB", "BB"), "B", "#c:bolts/steel");
        shaped(NS, "rail_wood", "geontm:rail_wood", 16, rows("S S", "SRS", "S S"),
                "S", "minecraft:stick", "R", "geontm:plant_item_rope");
        shaped(NS, "rail_narrow", "geontm:rail_narrow", 64, rows("S S", "S S", "S S"), "S", "geontm:steel_beam");
        shaped(NS, "rail_highspeed", "geontm:rail_highspeed", 16, rows("S S", "SIS", "S S"),
                "S", "#c:ingots/steel", "I", "#c:plates/iron");
        shaped(NS, "rail_booster", "geontm:rail_booster", 6, rows("S S", "CIC", "SRS"),
                "S", "#c:ingots/steel", "I", "#c:plates/iron", "R", "#c:ingots/red_copper", "C", "geontm:coil_copper");
        // 1.7 ToolRecipes
        shaped(NS, "rebar_placer", "geontm:rebar_placer", rows("RDR", "DWD", "RDR"),
                "R", "geontm:rebar", "D", "geontm:ducttape", "W", "geontm:wrench");
    }
}
