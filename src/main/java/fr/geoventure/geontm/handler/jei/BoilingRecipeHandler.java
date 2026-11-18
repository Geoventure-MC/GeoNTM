package fr.geoventure.geontm.handler.jei;

import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.inventory.recipes.HeatRecipes;
import fr.geoventure.geontm.items.machine.ItemFluidIcon;
import fr.geoventure.geocore.viewer.ViewerType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/** JEI category for the heat boiler: input droplet in, heated output droplet out. */
public class BoilingRecipeHandler extends JEIUniversalHandler<BoilingRecipeHandler.Entry> {

    public record Entry(FluidType input, HeatRecipes.HeatStep step) {}

    public BoilingRecipeHandler(ViewerType<Entry> type, Component title, ItemStack... machines) {
        super(type, title, machines);
    }

    public static List<Entry> buildRecipes() {
        List<Entry> list = new ArrayList<>();
        for (var e : HeatRecipes.getAll().entrySet()) list.add(new Entry(e.getKey(), e.getValue()));
        return list;
    }

    @Override
    protected List<List<ItemStack>> inputs(Entry recipe) {
        return List.of(List.of(ItemFluidIcon.make(recipe.input, recipe.step.amountReq())));
    }

    @Override
    protected List<List<ItemStack>> outputs(Entry recipe) {
        return List.of(List.of(ItemFluidIcon.make(recipe.step.output(), recipe.step.amountProduced())));
    }
}
