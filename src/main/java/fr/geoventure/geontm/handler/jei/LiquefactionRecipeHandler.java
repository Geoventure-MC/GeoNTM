package fr.geoventure.geontm.handler.jei;

import fr.geoventure.geontm.inventory.fluid.FluidStack;
import fr.geoventure.geontm.inventory.recipes.LiquefactionRecipes;
import fr.geoventure.geontm.items.machine.ItemFluidIcon;
import fr.geoventure.geocore.viewer.ViewerType;
import fr.geoventure.geocore.world.Compat;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/** JEI category for the liquefactor: input item(s) in, output droplet out. */
public class LiquefactionRecipeHandler extends JEIUniversalHandler<LiquefactionRecipeHandler.Entry> {

    public record Entry(List<ItemStack> input, FluidStack output) {}

    public LiquefactionRecipeHandler(ViewerType<Entry> type, Component title, ItemStack... machines) {
        super(type, title, machines);
    }

    public static List<Entry> buildRecipes() {
        List<Entry> list = new ArrayList<>();
        for (var e : LiquefactionRecipes.getItemRecipes().entrySet()) {
            list.add(new Entry(List.of(new ItemStack(e.getKey())), e.getValue()));
        }
        for (var e : LiquefactionRecipes.getTagRecipes().entrySet()) {
            List<ItemStack> stacks = itemsOfTag(e.getKey());
            if (!stacks.isEmpty()) list.add(new Entry(stacks, e.getValue()));
        }
        return list;
    }

    private static List<ItemStack> itemsOfTag(TagKey<Item> tag) {
        List<ItemStack> stacks = new ArrayList<>();
        stacks.addAll(Compat.itemsInTag(tag));
        return stacks;
    }

    @Override
    protected List<List<ItemStack>> inputs(Entry recipe) {
        return List.of(recipe.input);
    }

    @Override
    protected List<List<ItemStack>> outputs(Entry recipe) {
        return List.of(List.of(ItemFluidIcon.make(recipe.output)));
    }
}
