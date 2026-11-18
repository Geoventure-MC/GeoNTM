package fr.geoventure.geontm.inventory.material;

import fr.geoventure.geocore.dejson.Tags;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Every {@code c:} convention tag geontm's materials belong to, declared through GeoCore's
 * {@code dejson} vocabulary instead of written as JSON.
 *
 * <p>These declarations stand in for {@value #FILES_REPLACED} tag files. 578 of those said what a
 * material tag usually says, that {@code c:ingots/iron} contains {@code geontm:ingot_iron}, which restates
 * {@link MaterialShapes} one file at a time. Here it is a single line per material listing the shapes it
 * comes in, and the item name is derived from the shape as the item's own registration derives it.
 *
 * <p>Those derivable lines, and the tags the registrations imply that are not derivable (an item whose
 * name does not follow its shape, {@code c:ingots/carbon} being {@code geontm:ingot_graphite}; a tag that also
 * admits a vanilla item; the {@code any_*} aggregates, which are tags of tags), are written by
 * {@code port-tools/gen_material_tags.py} into {@link ModMaterialTagsGenerated}. Re-run the generator after
 * adding content; do not edit that class. What is left here are the tags no registration implies: dyes,
 * tars, glass, the tiny dusts named differently from their items, and the one block tag.
 *
 * <p>{@link fr.geoventure.geontm.gametest.MaterialTagGameTests} is the other half: it asks the live item
 * registry which {@code <prefix>_<material>} items exist and fails if one of them is in no tag. This
 * file cannot see that failure itself, and a silently untagged item makes a recipe reject a valid
 * ingredient with nothing reported.
 */
public final class ModMaterialTags {

    private static final String NS = "c";

    /** How many tag files the declarations below stand in for; quoted in this class's own javadoc. */
    static final int FILES_REPLACED = 794;

    private static boolean declared;

    private ModMaterialTags() {}

    public static void declare() {
        if (declared) {
            return;
        }
        declared = true;

        // Original ModSword's steel material repaired with ingot_steel; this tag also gates every
        // legacy crate-opening path through BlockCrateSupply.CROWBAR.
        Tags.item("geontm", "crowbar", "geontm:crowbar");

        ModMaterialTagsGenerated.declare();
        named();
    }

    private static void tag(String path, String... values) {
        GeoventureMatieres.declarer(path, values);
    }

    private static void blockTag(String path, String... values) {
        Tags.block(NS, path, values);
    }

    /** The tags no registration implies; see the class comment. */
    private static void named() {
        blockTag("ores/uranium", "geontm:ore_uranium", "geontm:ore_uranium_scorched", "geontm:ore_gneiss_uranium",
                "geontm:ore_gneiss_uranium_scorched", "geontm:ore_nether_uranium", "geontm:ore_nether_uranium_scorched",
                "geontm:ore_uranium_deepslate");
        // a deepslate twin shares its stone ore's tag, so every machine table takes it
        for (String ore : new String[] {"uranium", "thorium", "titanium", "sulfur", "aluminium", "fluorite", "niter",
                "tungsten", "lead", "beryllium", "rare", "cinnabar", "cobalt", "lignite", "asbestos", "coltan"}) {
            Tags.member("item", NS, "ores/" + ore, "geontm:ore_" + ore + "_deepslate");
        }
        oreConventions();
        tag("cobblestones", "minecraft:cobblestone");
        tag("cokes", "geontm:coke_coal", "geontm:coke_lignite", "geontm:coke_petroleum");
        tag("concretes", "geontm:concrete", "geontm:concrete_smooth", "geontm:concrete_asbestos", "geontm:ducrete",
                "geontm:ducrete_smooth",
                // 1.7's wildcard concrete_colored / concrete_colored_ext: one item per colour
                "geontm:concrete_white", "geontm:concrete_orange", "geontm:concrete_magenta", "geontm:concrete_light_blue", "geontm:concrete_yellow", "geontm:concrete_lime", "geontm:concrete_pink", "geontm:concrete_gray", "geontm:concrete_silver", "geontm:concrete_cyan", "geontm:concrete_purple", "geontm:concrete_blue", "geontm:concrete_brown", "geontm:concrete_green", "geontm:concrete_red", "geontm:concrete_black",
                "geontm:concrete_ext_machine", "geontm:concrete_ext_machine_stripe", "geontm:concrete_ext_indigo", "geontm:concrete_ext_purple", "geontm:concrete_ext_pink", "geontm:concrete_ext_hazard", "geontm:concrete_ext_sand", "geontm:concrete_ext_bronze");
        tag("dusts/actinium_tiny", "geontm:powder_actinium_tiny");
        tag("dusts/ash", "geontm:powder_ash_wood", "geontm:powder_ash_coal", "geontm:powder_ash_misc", "geontm:powder_ash_fly",
                "geontm:powder_ash_soot");
        tag("dusts/boron_tiny", "geontm:powder_boron_tiny");
        tag("dusts/lanthanium_tiny", "geontm:powder_lanthanium_tiny");
        tag("dusts/meteorite_tiny", "geontm:powder_meteorite_tiny");
        tag("dusts/steel_tiny", "geontm:powder_steel_tiny");
        // port: the name other mods give sawdust (Mekanism's paper and charcoal); 1.7 had no ore name for it
        tag("dusts/wood", "geontm:powder_sawdust");
        // 1.7 P_WHITE.ingot() "ingotWhitePhosphorus": only ingot_phosphorus (OreDictManager P_WHITE.ingot(ingot_phosphorus))
        tag("ingots/white_phosphorus", "geontm:ingot_phosphorus");
        // 1.7 KEY_SAND, Forge's "sand" ore name: sand and red sand (Blocks.sand at any meta), and other mods' sands. The
        // convention tag on 26.1/1.21; declared here so it has these members on every node (vanilla's minecraft:sand also
        // holds suspicious sand since 1.20, which 1.7's "sand" did not)
        tag("sands", "minecraft:sand", "minecraft:red_sand");
        // 1.7 KEY_SLIME, Forge's "slimeball" ore name: the vanilla slime ball, and other mods' slime balls. The convention
        // tag on 26.1/1.21; declared here so it has this member on every node (1.20.1 Forge spells it forge:slimeballs)
        tag("slime_balls", "minecraft:slime_ball");
        tag("dyes/black", "geontm:chemical_dye_black", "geontm:crayon_black", "geontm:powder_ash_coal",
                "geontm:powder_ash_soot", "geontm:powder_coal", "geontm:oil_tar_crack", "geontm:oil_tar_crude",
                "minecraft:black_dye");
        tag("dyes/blue", "geontm:chemical_dye_blue", "geontm:crayon_blue", "geontm:powder_lapis", "minecraft:blue_dye");
        tag("dyes/brown", "geontm:chemical_dye_brown", "geontm:crayon_brown", "geontm:powder_ash_fly", "geontm:powder_lignite",
                "minecraft:brown_dye");
        tag("dyes/cyan", "geontm:chemical_dye_cyan", "geontm:crayon_cyan", "geontm:oil_tar_wax", "minecraft:cyan_dye");
        tag("dyes/gray", "geontm:chemical_dye_gray", "geontm:crayon_gray", "geontm:oil_tar_coal", "geontm:powder_ash_misc",
                "minecraft:gray_dye");
        tag("dyes/green", "geontm:chemical_dye_green", "geontm:crayon_green", "minecraft:green_dye");
        tag("dyes/light_blue", "geontm:chemical_dye_lightblue", "geontm:crayon_lightblue", "minecraft:light_blue_dye");
        tag("dyes/light_gray", "geontm:chemical_dye_silver", "geontm:crayon_silver", "geontm:powder_ash_wood",
                "geontm:powder_titanium", "minecraft:light_gray_dye");
        tag("dyes/lime", "geontm:chemical_dye_lime", "geontm:crayon_lime", "minecraft:lime_dye");
        tag("dyes/magenta", "geontm:chemical_dye_magenta", "geontm:crayon_magenta", "geontm:powder_ash_fullerene",
                "minecraft:magenta_dye");
        tag("dyes/orange", "geontm:chemical_dye_orange", "geontm:crayon_orange", "geontm:powder_cadmium",
                "minecraft:orange_dye");
        tag("dyes/pink", "geontm:chemical_dye_pink", "geontm:crayon_pink", "minecraft:pink_dye");
        tag("dyes/purple", "geontm:chemical_dye_purple", "geontm:crayon_purple", "minecraft:purple_dye");
        tag("dyes/red", "geontm:chemical_dye_red", "geontm:crayon_red", "geontm:cinnabar", "minecraft:red_dye");
        tag("dyes/white", "geontm:chemical_dye_white", "geontm:crayon_white", "geontm:fluorite", "geontm:oil_tar_paraffin",
                "minecraft:white_dye");
        tag("dyes/yellow", "geontm:chemical_dye_yellow", "geontm:crayon_yellow", "geontm:sulfur", "minecraft:yellow_dye");
        // 1.7 KEY_ANYGLASS "blockGlass": vanilla glass and every stained glass (stated here, like glass_panes, so 1.20.1
        // Forge, which has no conventional c:glass_blocks, matches the same items as the newer loaders)
        tag("glass_blocks", "geontm:glass_boron", "geontm:glass_lead",
                "minecraft:glass", "minecraft:white_stained_glass", "minecraft:orange_stained_glass",
                "minecraft:magenta_stained_glass", "minecraft:light_blue_stained_glass",
                "minecraft:yellow_stained_glass", "minecraft:lime_stained_glass", "minecraft:pink_stained_glass",
                "minecraft:gray_stained_glass", "minecraft:light_gray_stained_glass", "minecraft:cyan_stained_glass",
                "minecraft:purple_stained_glass", "minecraft:blue_stained_glass", "minecraft:brown_stained_glass",
                "minecraft:green_stained_glass", "minecraft:red_stained_glass", "minecraft:black_stained_glass");
        tag("glass_panes", "minecraft:glass_pane", "minecraft:white_stained_glass_pane",
                "minecraft:orange_stained_glass_pane", "minecraft:magenta_stained_glass_pane",
                "minecraft:light_blue_stained_glass_pane", "minecraft:yellow_stained_glass_pane",
                "minecraft:lime_stained_glass_pane", "minecraft:pink_stained_glass_pane",
                "minecraft:gray_stained_glass_pane", "minecraft:light_gray_stained_glass_pane",
                "minecraft:cyan_stained_glass_pane", "minecraft:purple_stained_glass_pane",
                "minecraft:blue_stained_glass_pane", "minecraft:brown_stained_glass_pane",
                "minecraft:green_stained_glass_pane", "minecraft:red_stained_glass_pane",
                "minecraft:black_stained_glass_pane");
        tag("glyphid_meats", "geontm:glyphid_meat", "geontm:glyphid_meat_grilled");
        tag("nuggets/gold", "minecraft:gold_nugget");
        tag("rods/wooden", "minecraft:stick");
        tag("storage_blocks/cdalloy", "geontm:block_cdalloy");
        tag("storage_blocks/lithium", "geontm:block_lithium");
        tag("storage_blocks/tcalloy", "geontm:block_tcalloy");
        // 1.7 ore names a machine table takes, stated with exactly 1.7's members so every node matches the same items
        // (1.20.1 Forge spells the loader's own tags forge:): KEY_BRICK "ingotBrick" (Forge's vanilla entry),
        // NETHERQUARTZ.ore() "oreNetherQuartz", QUARTZ.block() "blockQuartz" (Forge's vanilla entry, meta 0) and
        // FIBER.block() "blockFiberglass"
        tag("ingots/brick", "minecraft:brick");
        tag("ores/quartz", "minecraft:nether_quartz_ore");
        tag("ores/bauxite", "geontm:stone_resource_bauxite");
        // 1.7 ILMENITE.ingot(chunk_ore ilmenite) and the raw nuggets 1.7 registers for every NUGGET autogen material
        tag("ingots/ilmenite", "geontm:chunk_ore_ilmenite");
        tag("fragments/ilmenite", "geontm:bedrock_ore_fragment_ilmenite");
        for (String mat : new String[] {"titanium", "copper", "tungsten", "aluminium", "boron", "sodium", "strontium", "lithium"}) {
            Tags.member("item", NS, "nuggets/" + mat, "geontm:nugget_raw_" + mat);
        }
        tag("storage_blocks/quartz", "minecraft:quartz_block");
        tag("storage_blocks/fiberglass", "geontm:block_fiberglass");
        tag("tars", "geontm:oil_tar_crude", "geontm:oil_tar_coal", "geontm:oil_tar_crack", "geontm:oil_tar_wood");
    }

    /** port: the conventions' ground and drop-rate tags; ores in other rock or without a fixed drop get none. */
    private static void oreConventions() {
        String[] twins = {"aluminium", "asbestos", "beryllium", "cinnabar", "cobalt", "coltan", "fluorite", "lead",
                "lignite", "niter", "rare", "sulfur", "thorium", "titanium", "tungsten", "uranium"};
        List<String> stone = new ArrayList<>(List.of("geontm:ore_uranium_scorched", "geontm:ore_schrabidium", "geontm:ore_australium"));
        List<String> deepslate = new ArrayList<>();
        for (String ore : twins) {
            stone.add("geontm:ore_" + ore);
            deepslate.add("geontm:ore_" + ore + "_deepslate");
        }
        String[] netherrack = {"geontm:ore_nether_coal", "geontm:ore_nether_cobalt", "geontm:ore_nether_plutonium",
                "geontm:ore_nether_schrabidium", "geontm:ore_nether_smoldering", "geontm:ore_nether_sulfur",
                "geontm:ore_nether_tungsten", "geontm:ore_nether_uranium", "geontm:ore_nether_uranium_scorched"};
        both("ores_in_ground/stone", stone.toArray(String[]::new));
        both("ores_in_ground/deepslate", deepslate.toArray(String[]::new));
        both("ores_in_ground/netherrack", netherrack);

        List<String> dense = new ArrayList<>(List.of("geontm:ore_nether_sulfur", "geontm:ore_nether_cobalt"));
        List<String> singular = new ArrayList<>(List.of("geontm:ore_uranium_scorched", "geontm:ore_schrabidium",
                "geontm:ore_nether_plutonium", "geontm:ore_nether_schrabidium", "geontm:ore_nether_tungsten",
                "geontm:ore_nether_uranium", "geontm:ore_nether_uranium_scorched", "geontm:ore_sellafield_diamond",
                "geontm:ore_sellafield_emerald", "geontm:ore_gneiss_asbestos", "geontm:ore_gneiss_copper", "geontm:ore_gneiss_gold",
                "geontm:ore_gneiss_iron", "geontm:ore_gneiss_lithium", "geontm:ore_gneiss_rare", "geontm:ore_gneiss_schrabidium",
                "geontm:ore_gneiss_uranium", "geontm:ore_gneiss_uranium_scorched"));
        Set<String> several = Set.of("cobalt", "fluorite", "niter", "sulfur");
        for (String ore : twins) {
            List<String> rate = several.contains(ore) ? dense : singular;
            rate.add("geontm:ore_" + ore);
            rate.add("geontm:ore_" + ore + "_deepslate");
        }
        both("ore_rates/dense", dense.toArray(String[]::new));
        both("ore_rates/singular", singular.toArray(String[]::new));
    }

    private static void both(String path, String... values) {
        tag(path, values);
        blockTag(path, values);
    }
}
