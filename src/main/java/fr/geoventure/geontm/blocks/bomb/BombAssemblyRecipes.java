package fr.geoventure.geontm.blocks.bomb;

import fr.geoventure.geontm.inventory.RecipesCommon.ComparableStack;
import fr.geoventure.geontm.inventory.RecipesCommon;
import fr.geoventure.geontm.inventory.recipes.loader.GenericRecipe;
import fr.geoventure.geontm.inventory.recipes.loader.GenericRecipes;
import fr.geoventure.geontm.inventory.fluid.FluidStack;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.items.ItemEnums.EnumCircuitType;
import fr.geoventure.geontm.items.ModBombItems;
import fr.geoventure.geontm.items.ModIntermediateItems;
import fr.geoventure.geontm.items.ModItems;
import fr.geoventure.geocore.Ids;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import static fr.geoventure.geontm.inventory.OreDictManager.AL;
import static fr.geoventure.geontm.inventory.OreDictManager.DURA;
import static fr.geoventure.geontm.inventory.OreDictManager.PB;
import static fr.geoventure.geontm.inventory.OreDictManager.TI;
import static fr.geoventure.geontm.inventory.OreDictManager.U238;
import static fr.geoventure.geontm.inventory.OreDictManager.WEAPONSTEEL;

/** Dependency-complete Ivy Mike assembly-machine recipes, kept with the bomb feature. */
public final class BombAssemblyRecipes {

    private BombAssemblyRecipes() {}

    public static void registerAll(GenericRecipes recipes) {
        recipes.registerGenerated(new GenericRecipe("ass.ivymike").setup(600, 100)
                .inputItems(new ComparableStack(ModItems.sphere_steel.get()), AL.shell().count(4),
                        new ComparableStack(ModIntermediateItems.circuit(EnumCircuitType.CONTROLLER_ADVANCED), 8),
                        new RecipesCommon.TagStack(TagKey.create(Registries.ITEM, Ids.of("c", "dyes/light_gray")), 16))
                .outputItems(new ItemStack(ModBombBlocks.nuke_mike.get()))
                .setPools528("528.controller"));
        recipes.registerGenerated(new GenericRecipe("ass.mikecore").setup(1_200, 100)
                .inputItems(U238.nugget().count(24), PB.plate().count(6))
                .outputItems(new ItemStack(ModBombItems.mike_core.get())));
        recipes.registerGenerated(new GenericRecipe("ass.mikedeut").setup(600, 100)
                .inputItems(WEAPONSTEEL.plate().count(16), TI.plate().count(16))
                .inputFluids(new FluidStack(Fluids.DEUTERIUM, 10_000))
                .outputItems(new ItemStack(ModBombItems.mike_deut.get())));
        recipes.registerGenerated(new GenericRecipe("ass.mikecooler").setup(300, 100)
                .inputItems(DURA.plate().count(8), new ComparableStack(ModItems.coil_copper.get(), 5),
                        new ComparableStack(ModItems.coil_tungsten.get(), 5),
                        new ComparableStack(ModItems.motor.get(), 2))
                .outputItems(new ItemStack(ModBombItems.mike_cooling_unit.get())));
    }
}
