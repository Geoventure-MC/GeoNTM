package fr.geoventure.geontm.inventory.recipes;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.inventory.recipes.loader.DataGenericRecipes;
import fr.geoventure.geontm.inventory.recipes.loader.GenericRecipe;

/** 1.7 {@code RockMillRecipes}: the datapack table {@code geontm:rock_mill}. */
public final class RockMillRecipes extends DataGenericRecipes<GenericRecipe> {

    public static final RockMillRecipes INSTANCE = new RockMillRecipes();

    private RockMillRecipes() {
        super(Constants.MOD_ID, "rock_mill");
    }

    public static void declare() {}
}
