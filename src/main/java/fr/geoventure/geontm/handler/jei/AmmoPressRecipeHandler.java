package fr.geoventure.geontm.handler.jei;

import fr.geoventure.geontm.inventory.RecipesCommon.AStack;
import fr.geoventure.geontm.inventory.recipes.AmmoPressRecipes;
import fr.geoventure.geontm.inventory.recipes.AmmoPressRecipes.AmmoPressRecipe;
import fr.geoventure.geocore.viewer.ViewerType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/** JEI category matching the old AmmoPressHandler's compact non-null input layout. */
public class AmmoPressRecipeHandler extends JEIUniversalHandler<AmmoPressRecipe> {

    public AmmoPressRecipeHandler(ViewerType<AmmoPressRecipe> type,
                                  Component title, ItemStack machine) {
        super(type, title, machine);
    }

    @Override
    protected List<List<ItemStack>> inputs(AmmoPressRecipe recipe) {
        List<List<ItemStack>> inputs = new ArrayList<>();
        for (AStack input : recipe.input) {
            if (input != null) inputs.add(input.getDisplayStacks());
        }
        return inputs;
    }

    @Override
    protected List<List<ItemStack>> outputs(AmmoPressRecipe recipe) {
        return List.of(List.of(recipe.output.copy()));
    }
}
