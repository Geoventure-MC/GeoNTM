package fr.geoventure.geontm.handler.jei;

import fr.geoventure.geontm.inventory.recipes.MagicRecipes;
import fr.geoventure.geontm.items.ModMeleeKitItems;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.viewer.Role;
import fr.geoventure.geocore.viewer.SlotLayout;
import fr.geoventure.geocore.viewer.ViewerType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * 1.7 {@code BookRecipeHandler} ("Black Book"): the Book of Boxcars' four corner inputs and its result, drawn on the
 * book's own GUI texture.
 */
public class BookRecipeHandler extends GeoNtmRecipeCategory<MagicRecipes.MagicRecipe> {

    public BookRecipeHandler(ViewerType<MagicRecipes.MagicRecipe> type, Component title) {
        super(type, title, book(), Ids.of("geontm", "textures/gui/processing/gui_book.png"), WIDTH, HEIGHT, 5, 11);
    }

    public static ItemStack book() {
        return new ItemStack(ModMeleeKitItems.book_of_.get());
    }

    public static List<MagicRecipes.MagicRecipe> buildRecipes() {
        return MagicRecipes.getRecipes();
    }

    @Override
    public void setRecipe(SlotLayout builder, MagicRecipes.MagicRecipe recipe) {
        for (int i = 0; i < Math.min(recipe.in.size(), 4); i++) {
            builder.addSlot(Role.INPUT, 25 + (i % 2) * 36, 6 + (i / 2) * 36).addItemStacks(stacks(recipe.in.get(i)));
        }
        builder.addSlot(Role.OUTPUT, 119, 24).addItemStack(recipe.getResult());
    }

    private static List<ItemStack> stacks(MagicRecipes.Input input) {
        if (input.isTag()) return CrucibleSmeltingHandler.tagged(TagKey.create(Registries.ITEM, Ids.parse(input.id())));
        return List.of(new ItemStack(BuiltInRegistries.ITEM.getValue(Ids.parse(input.id()))));
    }
}
