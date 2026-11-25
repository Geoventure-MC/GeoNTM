package fr.geoventure.geontm.inventory.recipes;

import fr.geoventure.geontm.lib.ItemIds;
import fr.geoventure.geontm.lib.TagIds;

import static fr.geoventure.geocore.dejson.Crafting.rows;
import static fr.geoventure.geocore.dejson.Crafting.shaped;

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
    }
}
