package fr.geoventure.geontm.items;

import fr.geoventure.geontm.lib.ItemIds;
import fr.geoventure.geontm.lib.TagIds;
import fr.geoventure.geontm.lib.VanillaIds;
import fr.geoventure.geocore.dejson.Tags;

import static fr.geoventure.geocore.dejson.Crafting.rows;
import static fr.geoventure.geocore.dejson.Crafting.shaped;
import static fr.geoventure.geocore.dejson.Crafting.shapeless;
import static fr.geoventure.geocore.dejson.Crafting.stack;

/**
 * Dejson declarations for {@link ModMeleeKitItems}: the family's repair tags and the crafting recipes of 1.7
 * {@code CraftingManager}, {@code ToolRecipes}, {@code WeaponRecipes} and {@code ConsumableRecipes}, including the
 * missile assembly station's, which waited on the wrench. The Book of Boxcars recipes are
 * {@code MagicRecipes}, not data. The item models and the {@code geontm:melee_kit_swords} membership are stated on the
 * registrations in {@link ModMeleeKitItems}. Touches no Minecraft class.
 */
public final class MeleeKitDeclarations {

    private static final String NS = "geontm";
    private static boolean declared;

    private MeleeKitDeclarations() {}

    public static void declare() {
        if (declared) return;
        declared = true;

        // --- tags ---
        Tags.item(NS, "repairs/diamond", VanillaIds.diamond);
        Tags.item(NS, "repairs/block_schrabidium", ItemIds.block_schrabidium);
        // geontm:melee_kit_swords (nested into minecraft:swords) is stated per sword on the registrations

        recipes();
    }

    private static void recipes() {
        String stick = VanillaIds.stick;
        String plastic = TagIds.ingots_any_plastic;

        // 1.7 CraftingManager
        shaped(NS, "big_sword", "geontm:big_sword", rows("QIQ", "QIQ", "GSG"),
                "G", VanillaIds.gold_ingot, "S", stick, "I", VanillaIds.iron_ingot, "Q", VanillaIds.quartz);
        shaped(NS, "wrench", "geontm:wrench", rows(" S ", " IS", "I  "),
                "S", TagIds.ingots_steel, "I", TagIds.ingots_iron);
        shaped(NS, "wrench_flipped", "geontm:wrench_flipped", rows("S", "D", "W"),
                "S", VanillaIds.iron_sword, "D", ItemIds.ducttape, "W", "geontm:wrench");
        shaped(NS, "machine_missile_assembly", "geontm:machine_missile_assembly", rows("PWP", "SSS", "CCC"),
                "P", ItemIds.pedestal_steel, "W", "geontm:wrench", "S", TagIds.plates_steel, "C", "#geontm:steel_scaffolds");

        // 1.7 ToolRecipes
        shaped(NS, "wood_gavel", "geontm:wood_gavel", rows("SWS", " R ", " R "),
                "S", TagIds.minecraft_wooden_slabs, "W", TagIds.minecraft_logs, "R", stick);
        shaped(NS, "lead_gavel", "geontm:lead_gavel", rows("PIP", "IGI", "PIP"),
                "P", ItemIds.pellet_buckshot, "I", TagIds.ingots_lead, "G", "geontm:wood_gavel");
        shaped(NS, "weapon_pipe_lead", "geontm:weapon_pipe_lead", rows("II", " I", " I"), "I", TagIds.pipes_lead);
        shaped(NS, "ullapool_caber", "geontm:ullapool_caber", rows("ITI", " S ", " S "),
                "I", TagIds.plates_iron, "T", VanillaIds.tnt, "S", stick);
        shaped(NS, "toolbox", "geontm:toolbox", rows("CCC", "CIC"), "C", TagIds.plates_copper, "I", TagIds.ingots_iron);
        shaped(NS, "screwdriver_desh", "geontm:screwdriver_desh", rows("  I", " I ", "S  "),
                "S", plastic, "I", TagIds.ingots_desh);
        shaped(NS, "hand_drill_desh", "geontm:hand_drill_desh", rows(" D", "S ", " S"),
                "D", TagIds.ingots_desh, "S", plastic);
        // ItemBlowtorch.getEmptyTool: the torch is crafted with both tanks written empty
        shaped(NS, "acetylene_torch", stack("geontm:acetylene_torch").with("geontm:torch_unsaturateds", 0)
                        .with("geontm:torch_oxygen", 0), rows("SS ", " PS", " T "),
                "S", TagIds.plates_steel, "P", plastic, "T", ItemIds.tank_steel);
        shapeless(NS, "bobmazon", "geontm:bobmazon", VanillaIds.book, VanillaIds.gold_nugget, VanillaIds.string,
                TagIds.dyes_blue);

        // 1.7 WeaponRecipes: the chemthrower waited on the wrench too
        shaped(NS, "gun_chemthrower", "geontm:gun_chemthrower", rows("MHW", "PSS"),
                "M", TagIds.mechanisms_weaponsteel, "H", TagIds.pipes_rubber, "W", "geontm:wrench",
                "P", TagIds.barrels_heavy_weaponsteel, "S", TagIds.shells_weaponsteel);
        // 1.7 WeaponRecipes: three sticks of dynamite, paper and any tar
        shapeless(NS, "stick_dynamite_fishing", "geontm:stick_dynamite_fishing", ItemIds.stick_dynamite,
                ItemIds.stick_dynamite, ItemIds.stick_dynamite, VanillaIds.paper, TagIds.tars);

        // 1.7 ConsumableRecipes
        shaped(NS, "stealth_boy", "geontm:stealth_boy", rows(" B", "LI", "LC"),
                "B", VanillaIds.stone_button, "L", VanillaIds.leather, "I", TagIds.ingots_steel,
                "C", ItemIds.circuit_basic);
    }
}
