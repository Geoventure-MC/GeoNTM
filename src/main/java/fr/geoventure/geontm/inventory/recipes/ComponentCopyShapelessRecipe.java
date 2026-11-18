package fr.geoventure.geontm.inventory.recipes;

import fr.geoventure.geontm.items.ModDataComponents;
import fr.geoventure.geocore.world.CustomRecipes;
import fr.geoventure.geocore.world.ItemStackSpec;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

/** HBM's output policy for state-copy crafting: a newly loaded ZIRNOX rod starts fresh. */
public final class ComponentCopyShapelessRecipe
        extends fr.geoventure.geocore.world.ComponentCopyShapelessRecipe {

    public static final RecipeSerializer<ComponentCopyShapelessRecipe> SERIALIZER =
            CustomRecipes.componentCopyShapeless(ComponentCopyShapelessRecipe::new);

    private ComponentCopyShapelessRecipe(String namespace, String path, List<Ingredient> ingredients,
                                         Ingredient copyFrom, ItemStackSpec result) {
        super(namespace, path, ingredients, copyFrom, result);
    }

    /** Test and hand-built recipe entry point; datapack loading uses {@link #SERIALIZER}. */
    public static ComponentCopyShapelessRecipe of(List<Ingredient> ingredients, Ingredient copyFrom,
                                                   ItemStack result) {
        return new ComponentCopyShapelessRecipe("geontm", "component_copy_shapeless", ingredients, copyFrom,
                ItemStackSpec.of(result));
    }

    @Override
    protected ItemStack finishResult(ItemStack assembled) {
        // Empty rods normally carry neither value. Clear malformed legacy/component state rather than
        // turning it into an already-used or hot output rod.
        ModDataComponents.ZIRNOX_LIFE.remove(assembled);
        ModDataComponents.FUEL_WASTE_HOT.remove(assembled);
        return assembled;
    }

    @Override
    protected RecipeSerializer<?> serializer() {
        return SERIALIZER;
    }
}
