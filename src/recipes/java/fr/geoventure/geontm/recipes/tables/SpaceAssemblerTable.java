package fr.geoventure.geontm.recipes.tables;

import fr.geoventure.geontm.inventory.RecipesCommon.ComparableStack;
import fr.geoventure.geontm.inventory.recipes.SpaceAssemblerRecipes;
import fr.geoventure.geontm.inventory.recipes.loader.GenericRecipe;
import fr.geoventure.geontm.inventory.recipes.loader.GenericTableSource;
import fr.geoventure.geontm.items.ItemEnums.EnumCircuitType;
import fr.geoventure.geontm.items.ModIntermediateItems;
import fr.geoventure.geontm.items.ModSatelliteItems;
import fr.geoventure.geontm.items.machine.ItemOrbitalAssembly.EnumOrbitalAssembly;
import net.minecraft.world.item.ItemStack;

/** 1.7 {@code SpaceAssemblerRecipes.registerDefaults}. */
public final class SpaceAssemblerTable extends GenericTableSource<GenericRecipe> {

    public SpaceAssemblerTable() {
        super(SpaceAssemblerRecipes.INSTANCE);
    }

    @Override
    protected void registerDefaults() {

        int minute = 60 * 20;

        this.register(new GenericRecipe("space.crystalcircuit").setup(3 * minute, 0L)
                .inputItems(new ComparableStack(ModSatelliteItems.orbital_assembly.get(EnumOrbitalAssembly.CRYSTAL_CIRCUIT), 1))
                .outputItems(new ItemStack(ModIntermediateItems.circuit(EnumCircuitType.CRYSTAL))));
    }
}
