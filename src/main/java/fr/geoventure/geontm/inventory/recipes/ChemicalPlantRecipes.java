package fr.geoventure.geontm.inventory.recipes;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.inventory.recipes.loader.DataGenericRecipes;
import fr.geoventure.geontm.inventory.recipes.loader.GenericRecipe;

/**
 * Chemical plant recipes: the datapack table {@code geontm:chemical_plant}, one file per recipe in
 * {@code data/geontm/recipe/chemical_plant/} (ntm-next's schema, {@link fr.geoventure.geontm.inventory.recipes.loader.GenericRecipeCodec}).
 * The files were converted once from the former Java table (1.7's curated recipes, the fuel additives and the generated
 * 1.12.2 bulk) and are the source now; datapacks and addons add, replace or remove rows as with any recipe.
 */
public class ChemicalPlantRecipes extends DataGenericRecipes<GenericRecipe> {

    public static final ChemicalPlantRecipes INSTANCE = new ChemicalPlantRecipes();

    private ChemicalPlantRecipes() {
        super(Constants.MOD_ID, "chemical_plant");
    }

    /** Declares the table at mod construction (its recipe type and serializer register with HBM's content). */
    public static void declare() {}
}
