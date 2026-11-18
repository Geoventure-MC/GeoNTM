package fr.geoventure.geontm.handler.jei;

import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.inventory.recipes.CoolingRecipes;
import fr.geoventure.geontm.items.machine.ItemFluidIcon;
import fr.geoventure.geocore.viewer.ViewerType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/** JEI category for the steam turbine / condenser, hot droplet in, cooled droplet out. */
public class CoolingRecipeHandler extends JEIUniversalHandler<CoolingRecipeHandler.Entry> {

    public record Entry(FluidType input, CoolingRecipes.CoolingRecipe recipe) {}

    public CoolingRecipeHandler(ViewerType<Entry> type, Component title, ItemStack... machines) {
        super(type, title, machines);
    }

    public static List<Entry> buildRecipes() {
        List<Entry> list = new ArrayList<>();
        for (var e : CoolingRecipes.getAll().entrySet()) list.add(new Entry(e.getKey(), e.getValue()));
        return list;
    }

    @Override
    protected List<List<ItemStack>> inputs(Entry recipe) {
        return List.of(List.of(ItemFluidIcon.make(recipe.input, recipe.recipe.inputAmount())));
    }

    @Override
    protected List<List<ItemStack>> outputs(Entry recipe) {
        return List.of(List.of(ItemFluidIcon.make(recipe.recipe.output(), recipe.recipe.outputAmount())));
    }
}
