package fr.geoventure.geontm.inventory.material;

import fr.geoventure.geocore.dejson.Tags;

import java.util.Map;

/** Ressources GeoMods acceptées par les recettes GeoNTM. Généré par {@code tools/fusion_ressources.py}. */
final class GeoventureMatieres {

    private static final Map<String, String> OBJETS = Map.ofEntries(
            Map.entry("dusts/aluminium", "geomods:aluminum_dust"),
            Map.entry("dusts/quartz", "geomods:quartz_dust"),
            Map.entry("dusts/titanium", "geomods:titanium_dust"),
            Map.entry("dusts/zinc", "geomods:zinc_dust"),
            Map.entry("ingots/aluminium", "geomods:aluminum_ingot"),
            Map.entry("ingots/bronze", "geomods:bronze_ingot"),
            Map.entry("ingots/chromium", "geomods:chromium_ingot"),
            Map.entry("ingots/cobalt", "geomods:cobalt_ingot"),
            Map.entry("ingots/electrum", "geomods:electrum_ingot"),
            Map.entry("ingots/invar", "geomods:invar_ingot"),
            Map.entry("ingots/iridium", "geomods:iridium_ingot"),
            Map.entry("ingots/lead", "geomods:lead_ingot"),
            Map.entry("ingots/lithium", "geomods:lithium_ingot"),
            Map.entry("ingots/magnesium", "geomods:magnesium_ingot"),
            Map.entry("ingots/nickel", "geomods:nickel_ingot"),
            Map.entry("ingots/osmium", "geomods:osmium_ingot"),
            Map.entry("ingots/platinum", "geomods:platinum_ingot"),
            Map.entry("ingots/silver", "geomods:silver_ingot"),
            Map.entry("ingots/stainless", "geomods:stainless_ingot"),
            Map.entry("ingots/steel", "geomods:steel_ingot"),
            Map.entry("ingots/tin", "geomods:tin_ingot"),
            Map.entry("ingots/titanium", "geomods:titanium_ingot"),
            Map.entry("ingots/tungsten", "geomods:tungsten_ingot"),
            Map.entry("ingots/uranium", "geomods:uranium_ingot"),
            Map.entry("ingots/zinc", "geomods:zinc_ingot"),
            Map.entry("ores/aluminium", "geomods:aluminum_ore"),
            Map.entry("ores/chromium", "geomods:chromium_ore"),
            Map.entry("ores/cobalt", "geomods:cobalt_ore"),
            Map.entry("ores/garnet", "geomods:garnet_ore"),
            Map.entry("ores/iridium", "geomods:iridium_ore"),
            Map.entry("ores/jade", "geomods:jade_ore"),
            Map.entry("ores/lead", "geomods:lead_ore"),
            Map.entry("ores/lithium", "geomods:lithium_ore"),
            Map.entry("ores/magnesium", "geomods:magnesium_ore"),
            Map.entry("ores/nickel", "geomods:nickel_ore"),
            Map.entry("ores/onyx", "geomods:onyx_ore"),
            Map.entry("ores/opal", "geomods:opal_ore"),
            Map.entry("ores/osmium", "geomods:osmium_ore"),
            Map.entry("ores/platinum", "geomods:platinum_ore"),
            Map.entry("ores/ruby", "geomods:ruby_ore"),
            Map.entry("ores/sapphire", "geomods:sapphire_ore"),
            Map.entry("ores/silver", "geomods:silver_ore"),
            Map.entry("ores/tin", "geomods:tin_ore"),
            Map.entry("ores/titanium", "geomods:titanium_ore"),
            Map.entry("ores/topaz", "geomods:topaz_ore"),
            Map.entry("ores/tungsten", "geomods:tungsten_ore"),
            Map.entry("ores/uranium", "geomods:uranium_ore"),
            Map.entry("ores/zinc", "geomods:zinc_ore"),
            Map.entry("plates/iron", "geomods:iron_plate"),
            Map.entry("plates/steel", "geomods:steel_plate"),
            Map.entry("plates/titanium", "geomods:titanium_plate"),
            Map.entry("plates/zinc", "geomods:zinc_plate"),
            Map.entry("rods/steel", "geomods:steel_rod"),
            Map.entry("storage_blocks/aluminium", "geomods:aluminum_block"),
            Map.entry("storage_blocks/asphalt", "geomods:asphalt_block"),
            Map.entry("storage_blocks/bunker", "geomods:bunker_block"),
            Map.entry("storage_blocks/chromium", "geomods:chromium_block"),
            Map.entry("storage_blocks/cobalt", "geomods:cobalt_block"),
            Map.entry("storage_blocks/forge", "geomods:forge_block"),
            Map.entry("storage_blocks/garnet", "geomods:garnet_block"),
            Map.entry("storage_blocks/iridium", "geomods:iridium_block"),
            Map.entry("storage_blocks/jade", "geomods:jade_block"),
            Map.entry("storage_blocks/lead", "geomods:lead_block"),
            Map.entry("storage_blocks/lithium", "geomods:lithium_block"),
            Map.entry("storage_blocks/magnesium", "geomods:magnesium_block"),
            Map.entry("storage_blocks/nickel", "geomods:nickel_block"),
            Map.entry("storage_blocks/onyx", "geomods:onyx_block"),
            Map.entry("storage_blocks/opal", "geomods:opal_block"),
            Map.entry("storage_blocks/osmium", "geomods:osmium_block"),
            Map.entry("storage_blocks/plastic", "geomods:plastic_block"),
            Map.entry("storage_blocks/platinum", "geomods:platinum_block"),
            Map.entry("storage_blocks/ruby", "geomods:ruby_block"),
            Map.entry("storage_blocks/sapphire", "geomods:sapphire_block"),
            Map.entry("storage_blocks/silver", "geomods:silver_block"),
            Map.entry("storage_blocks/steel", "geomods:steel_block"),
            Map.entry("storage_blocks/tin", "geomods:tin_block"),
            Map.entry("storage_blocks/titanium", "geomods:titanium_block"),
            Map.entry("storage_blocks/topaz", "geomods:topaz_block"),
            Map.entry("storage_blocks/trophy", "geomods:trophy_block"),
            Map.entry("storage_blocks/tungsten", "geomods:tungsten_block"),
            Map.entry("storage_blocks/uranium", "geomods:uranium_block"),
            Map.entry("storage_blocks/workbench", "geomods:workbench_block"),
            Map.entry("storage_blocks/zinc", "geomods:zinc_block"),
            Map.entry("wires/aluminium", "geomods:aluminum_wire"),
            Map.entry("wires/barbed", "geomods:barbed_wire"),
            Map.entry("wires/copper", "geomods:copper_wire"),
            Map.entry("wires/gold", "geomods:gold_wire"),
            Map.entry("wires/hv", "geomods:hv_wire"));

    private GeoventureMatieres() {}

    /** Déclare un tag {@code c:} de matière, avec l'objet Geoventure équivalent s'il existe. */
    static void declarer(String chemin, String... valeurs) {
        Tags.Tag tag = Tags.of("item", "c", chemin).values(valeurs);
        String objet = OBJETS.get(chemin);
        if (objet != null) tag.optional(objet);
        tag.register();
    }
}
