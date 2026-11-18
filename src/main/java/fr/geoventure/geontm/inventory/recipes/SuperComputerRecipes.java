package fr.geoventure.geontm.inventory.recipes;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.inventory.recipes.loader.DataGenericRecipes;
import fr.geoventure.geontm.inventory.recipes.loader.GenericRecipe;

/** 1.7 {@code SuperComputerRecipes}: the datapack table {@code geontm:supercomputer}. */
public final class SuperComputerRecipes extends DataGenericRecipes<GenericRecipe> {

    public static final SuperComputerRecipes INSTANCE = new SuperComputerRecipes();

    private SuperComputerRecipes() {
        super(Constants.MOD_ID, "supercomputer");
    }

    public static void declare() {}
}
