package fr.geoventure.geontm.inventory.recipes;

import fr.geoventure.geontm.api.data.ReactorDataMaps;
import fr.geoventure.geontm.items.ModZirnoxItems;
import fr.geoventure.geontm.items.machine.ItemZirnoxRod;
import fr.geoventure.geontm.items.machine.ItemZirnoxWaste;
import fr.geoventure.geontm.items.machine.ZirnoxFuel;
import fr.geoventure.geocore.world.CompatCustomRecipe;
import fr.geoventure.geocore.world.CustomRecipes;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * 1.7 {@code RodRecipes}:39-47: a depleted ZIRNOX rod alone in the grid gives two of its hot spent-fuel waste
 * ({@code waste_*} metadata 1) and leaves the empty rod (the depleted rod's crafting remainder). Nine shapeless
 * recipes in 1.7; decided in Java here because "hot" is a stack state ({@code ItemZirnoxWaste.makeHot}: a flag
 * and the icon tint), which a declared result cannot state.
 */
public final class ZirnoxWasteRecipe extends CompatCustomRecipe {
    public static final ZirnoxWasteRecipe INSTANCE = new ZirnoxWasteRecipe();
    public static final RecipeSerializer<ZirnoxWasteRecipe> SERIALIZER = CustomRecipes.serializer(() -> INSTANCE);

    private ZirnoxWasteRecipe() {
        super("geontm", "zirnox_waste");
    }

    /** The waste a used-up rod reprocesses into: the {@code waste} of the {@code geontm:zirnox_fuel} entry that depletes into it. */
    private static Item waste(List<ItemStack> input) {
        ItemStack rod = ItemStack.EMPTY;
        for (ItemStack stack : input) {
            if (stack.isEmpty()) continue;
            if (!rod.isEmpty()) return null;
            rod = stack;
        }
        if (rod.isEmpty()) return null;
        for (Item item : BuiltInRegistries.ITEM) {
            if (!(item instanceof ItemZirnoxRod active)) continue;
            ReactorDataMaps.ZirnoxFuel fuel = active.stats();
            if (fuel.waste().isPresent() && rod.is(ReactorDataMaps.itemOf(fuel.depletesInto()))) {
                return ReactorDataMaps.itemOf(fuel.waste().get());
            }
        }
        return null;
    }

    private static ZirnoxFuel fuel(List<ItemStack> input) {
        ItemStack rod = ItemStack.EMPTY;
        for (ItemStack stack : input) {
            if (stack.isEmpty()) continue;
            if (!rod.isEmpty()) return null;
            rod = stack;
        }
        if (rod.isEmpty()) return null;
        for (ZirnoxFuel fuel : ZirnoxFuel.values()) {
            if (ModZirnoxItems.hasDepleted(fuel) && rod.is(ModZirnoxItems.depleted(fuel))) return fuel;
        }
        return null;
    }

    @Override
    protected boolean matchesItems(List<ItemStack> input, Level level) {
        return waste(input) != null;
    }

    @Override
    protected ItemStack assembleItems(List<ItemStack> input) {
        Item waste = waste(input);
        return waste == null ? ItemStack.EMPTY : ItemZirnoxWaste.makeHot(waste, 2);
    }

    @Override
    protected RecipeSerializer<ZirnoxWasteRecipe> serializer() {
        return SERIALIZER;
    }
}
