package fr.geoventure.geontm.handler.jei;

import fr.geoventure.geontm.inventory.RecipesCommon.AStack;
import fr.geoventure.geontm.inventory.recipes.ArcWelderRecipes;
import fr.geoventure.geontm.items.machine.ItemFluidIcon;
import fr.geoventure.geocore.viewer.ViewerType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** JEI view of the legacy Arc Welder's unordered item inputs and optional fluid input. */
public class ArcWelderRecipeHandler extends JEIUniversalHandler<ArcWelderRecipes.ArcWelderRecipe> {

    public ArcWelderRecipeHandler(ViewerType<ArcWelderRecipes.ArcWelderRecipe> type,
                                  Component title, ItemStack... machines) {
        super(type, title, machines);
    }

    public static List<ArcWelderRecipes.ArcWelderRecipe> buildRecipes() {
        ArcWelderRecipes.register();
        return List.copyOf(ArcWelderRecipes.recipes);
    }

    @Override
    protected List<List<ItemStack>> inputs(ArcWelderRecipes.ArcWelderRecipe recipe) {
        List<List<ItemStack>> inputs = new ArrayList<>();
        for (AStack ingredient : recipe.ingredients) {
            List<ItemStack> display = ingredient.getDisplayStacks();
            if (!display.isEmpty()) inputs.add(display);
        }
        if (recipe.fluid != null) inputs.add(List.of(ItemFluidIcon.make(recipe.fluid)));
        return inputs;
    }

    @Override
    protected List<List<ItemStack>> outputs(ArcWelderRecipes.ArcWelderRecipe recipe) {
        return List.of(List.of(recipe.output));
    }

    @Override
    public void draw(ArcWelderRecipes.ArcWelderRecipe recipe, GuiGraphics graphics,
                     double mouseX, double mouseY) {
        super.draw(recipe, graphics, mouseX, mouseY);
        String duration = String.format(Locale.US, "%,d", recipe.duration) + " ticks";
        String consumption = String.format(Locale.US, "%,d", recipe.consumption) + " HE/t";
        text(graphics, duration, 160 - textWidth(duration), 43);
        text(graphics, consumption, 160 - textWidth(consumption), 55);
    }
}
