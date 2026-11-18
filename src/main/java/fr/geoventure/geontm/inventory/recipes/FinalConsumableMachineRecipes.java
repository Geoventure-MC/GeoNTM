package fr.geoventure.geontm.inventory.recipes;

import fr.geoventure.geontm.inventory.RecipesCommon.AStack;
import fr.geoventure.geontm.inventory.RecipesCommon.ComparableStack;
import fr.geoventure.geontm.inventory.fluid.FluidStack;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.inventory.material.Mats;
import fr.geoventure.geontm.inventory.recipes.DiFurnaceRecipes.FuelEntry;
import fr.geoventure.geontm.inventory.recipes.anvil.AnvilSmithingRecipe;
import fr.geoventure.geontm.items.ItemEnums.EnumCokeType;
import fr.geoventure.geontm.items.ModConsumableTailItems;
import fr.geoventure.geontm.items.ModFinalConsumableItems;
import fr.geoventure.geontm.items.ModItems;
import fr.geoventure.geontm.items.ModOilItems;
import fr.geoventure.geontm.items.machine.ItemStamp.StampType;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * The machine recipes of the final consumables, each handed to its registry from one line in that
 * registry's own registration (the pattern of {@link AbilityToolMachineRecipes}).
 */
public final class FinalConsumableMachineRecipes {

    private FinalConsumableMachineRecipes() {}

    private static ComparableStack item(Item item) {
        return new ComparableStack(item);
    }

    /** 1.7 TileEntityDiFurnace.getItemPower: every briquette burns for 200. */
    public static void diFurnaceFuels(List<FuelEntry> entries) {
        entries.add(new FuelEntry(item(ModFinalConsumableItems.briquette_coal.get()), 200));
        entries.add(new FuelEntry(item(ModFinalConsumableItems.briquette_lignite.get()), 200));
        entries.add(new FuelEntry(item(ModFinalConsumableItems.briquette_wood.get()), 200));
    }
}
