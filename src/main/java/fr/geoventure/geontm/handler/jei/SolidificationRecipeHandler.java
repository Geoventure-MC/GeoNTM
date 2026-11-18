package fr.geoventure.geontm.handler.jei;

import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.inventory.recipes.SolidificationRecipes;
import fr.geoventure.geontm.items.machine.ItemFluidIcon;
import fr.geoventure.geocore.viewer.ViewerType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/** JEI category for the solidifier: input droplet in, output item out. */
public class SolidificationRecipeHandler extends JEIUniversalHandler<SolidificationRecipeHandler.Entry> {

    public record Entry(FluidType input, SolidificationRecipes.SolidRecipe recipe) {}

    public SolidificationRecipeHandler(ViewerType<Entry> type, Component title, ItemStack... machines) {
        super(type, title, machines);
    }

    public static List<Entry> buildRecipes() {
        List<Entry> list = new ArrayList<>();
        for (var e : SolidificationRecipes.getAll().entrySet()) list.add(new Entry(e.getKey(), e.getValue()));
        return list;
    }

    @Override
    protected List<List<ItemStack>> inputs(Entry recipe) {
        return List.of(List.of(ItemFluidIcon.make(recipe.input, recipe.recipe.amount())));
    }

    @Override
    protected List<List<ItemStack>> outputs(Entry recipe) {
        return List.of(List.of(recipe.recipe.output().copy()));
    }
}
