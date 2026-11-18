package fr.geoventure.geontm.handler.jei;

import fr.geoventure.geontm.blocks.machine.ModMachineBlocks;
import fr.geoventure.geontm.inventory.recipes.RTGRecipes;
import fr.geoventure.geocore.viewer.ViewerType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/** The old universal JEI strip showing each fresh pellet and the residual pellet it decays into. */
public final class RTGRecipeHandler extends JEIUniversalHandler<RTGRecipeHandler.Entry> {
    public static final ViewerType<Entry> TYPE =
            ViewerType.create("geontm", "rtg", Entry.class);

    public record Entry(ItemStack input, ItemStack output) {}

    public RTGRecipeHandler() {
        super(TYPE, Component.literal("RTG"), new ItemStack(ModMachineBlocks.machine_rtg_grey.get()));
    }

    public static List<Entry> buildRecipes() {
        List<Entry> result = new ArrayList<>();
        RTGRecipes.decayPairs().forEach((input, output) -> result.add(new Entry(input, output)));
        return result;
    }

    @Override
    protected List<List<ItemStack>> inputs(Entry recipe) {
        return List.of(List.of(recipe.input()));
    }

    @Override
    protected List<List<ItemStack>> outputs(Entry recipe) {
        return List.of(List.of(recipe.output()));
    }
}
