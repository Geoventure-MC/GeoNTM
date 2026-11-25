package fr.geoventure.geontm.inventory.recipes;

import fr.geoventure.geontm.lib.ItemIds;
import fr.geoventure.geontm.lib.TagIds;
import fr.geoventure.geontm.lib.VanillaIds;

import java.util.HashSet;
import java.util.Set;

import static fr.geoventure.geocore.dejson.Crafting.rows;
import static fr.geoventure.geocore.dejson.Crafting.shaped;
import static fr.geoventure.geocore.dejson.Crafting.shapeless;

/**
 * Recettes d'établi propres à Geoventure : du contenu gardé que ni NTM-Continuum ni HBM 1.7.10 ne savaient fabriquer
 * (voir {@code docs/perimetre/obtention.md}). Elles sont dans cette classe à part pour que les mises à jour de
 * NTM-Continuum ne les touchent pas. Les recettes de machines Geoventure sont dans les fichiers {@code geoventure_*.json}
 * de {@code src/generated/data} (outil {@code tools/recettes_geoventure.py}).
 */
public final class GeoventureCraftingRecipes {

    private static final String NS = "geontm";
    private static boolean declared;

    private GeoventureCraftingRecipes() {}

    /**
     * Colorant vanilla de chaque motif de peinture de fuselage : le même motif prend le même colorant sur toutes les
     * tailles. Table documentée dans {@code docs/perimetre/obtention.md}.
     */
    private static final String[][] TEINTES = {
            {"camo", VanillaIds.green_dye}, {"desert", VanillaIds.yellow_dye}, {"flames", VanillaIds.orange_dye},
            {"sky", VanillaIds.light_blue_dye}, {"sleek", VanillaIds.black_dye}, {"taint", VanillaIds.purple_dye},
            {"dash", VanillaIds.white_dye}, {"vap", VanillaIds.magenta_dye}, {"bullet", VanillaIds.gray_dye},
            {"silvermoonlight", VanillaIds.light_gray_dye}, {"soviet_glory", VanillaIds.red_dye},
            {"battery", VanillaIds.lime_dye}, {"cathedral", VanillaIds.cyan_dye}, {"duracell", VanillaIds.brown_dye},
            {"moonlit", VanillaIds.blue_dye}, {"magnusson", VanillaIds.blue_dye}, {"blackjack", VanillaIds.black_dye},
            {"decorated", VanillaIds.pink_dye}, {"lambda", VanillaIds.orange_dye}, {"minuteman", VanillaIds.blue_dye},
            {"pip", VanillaIds.lime_dye}, {"polite", VanillaIds.white_dye}, {"steampunk", VanillaIds.gray_dye},
            {"yuck", VanillaIds.brown_dye}, {"faust", VanillaIds.black_dye}, {"mimi", VanillaIds.pink_dye},
            {"panorama", VanillaIds.light_blue_dye}, {"roses", VanillaIds.magenta_dye}, {"snowy", VanillaIds.white_dye},
            {"soviet_stank", VanillaIds.brown_dye}};

    /** Fuselage de base suivi de ses variantes peintes sans recette d'origine (les variantes isolation, métal et desh ont la leur). */
    private static final String[][] PEINTURES = {
            {"mp_fuselage_10_kerosene", "camo", "desert", "flames", "sky", "sleek", "taint"},
            {"mp_fuselage_10_long_kerosene", "camo", "dash", "desert", "flames", "sky", "sleek", "taint", "vap"},
            {"mp_fuselage_10_solid", "battery", "cathedral", "duracell", "flames", "moonlit", "sleek", "soviet_glory"},
            {"mp_fuselage_10_long_solid", "bullet", "flames", "silvermoonlight", "sleek", "soviet_glory"},
            {"mp_fuselage_15_kerosene", "blackjack", "camo", "decorated", "desert", "lambda", "minuteman", "pip", "polite",
                    "sky", "steampunk", "taint", "yuck"},
            {"mp_fuselage_15_solid", "faust", "mimi", "panorama", "roses", "silvermoonlight", "snowy", "soviet_glory",
                    "soviet_stank"},
            {"mp_fuselage_15_hydrogen", "cathedral"},
            {"mp_fuselage_15_20_kerosene", "magnusson"}};

    public static void declare() {
        if (declared) return;
        declared = true;

        // Pièces de missile personnalisé sans recette d'origine (créatif seulement en 1.7), sur le modèle des empennages
        // mp_stability_15_flat / thin : empennage du Soyouz en plaques de titane, empennage de 2 m en plaques d'acier.
        shaped(NS, "mp_stability_15_soyuz", ItemIds.mp_stability_15_soyuz, rows("ASA", "PSP", "PSP"),
                "A", TagIds.plates_titanium, "P", TagIds.plates_steel, "S", TagIds.geontm_steel_scaffolds);
        shaped(NS, "mp_s_20", ItemIds.mp_s_20, rows("PSP", "PSP", "PSP"),
                "P", TagIds.plates_steel, "S", TagIds.geontm_steel_scaffolds);

        // Capteur de radiation (RadSensor) : aucune recette d'origine. Compteur Geiger, circuit et acier.
        shaped(NS, "radsensor", ItemIds.radsensor, rows(" G ", "SCS", "SSS"),
                "G", ItemIds.geiger_counter, "C", ItemIds.circuit_basic, "S", TagIds.plates_steel);

        peintures();
    }

    /**
     * Peinture des fuselages de missile personnalisé : le fuselage de base de même taille et de même carburant et un
     * colorant donnent la variante peinte ; la variante et un seau d'eau rendent le fuselage de base (le seau revient vide).
     * Les variantes d'un même fuselage ont chacune un colorant distinct, sinon deux recettes se disputeraient la grille.
     */
    private static void peintures() {
        for (String[] ligne : PEINTURES) {
            String base = NS + ":" + ligne[0];
            Set<String> utilises = new HashSet<>();
            for (int i = 1; i < ligne.length; i++) {
                String teinte = null;
                for (String[] t : TEINTES) if (t[0].equals(ligne[i])) teinte = t[1];
                if (teinte == null || !utilises.add(teinte)) {
                    throw new IllegalStateException("colorant absent ou en double pour " + ligne[0] + "_" + ligne[i]);
                }
                String variante = ligne[0] + "_" + ligne[i];
                shapeless(NS, variante + "_paint", NS + ":" + variante, base, teinte);
                shapeless(NS, variante + "_wash", base, NS + ":" + variante, VanillaIds.water_bucket);
            }
        }
    }
}
