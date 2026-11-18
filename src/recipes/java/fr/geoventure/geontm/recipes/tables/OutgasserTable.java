package fr.geoventure.geontm.recipes.tables;

import static fr.geoventure.geontm.inventory.OreDictManager.GOLD;
import static fr.geoventure.geontm.inventory.OreDictManager.LI;
import static fr.geoventure.geontm.inventory.OreDictManager.PVC;
import static fr.geoventure.geontm.inventory.OreDictManager.TH232;

import fr.geoventure.geontm.blocks.ModTerrainBlocks;
import fr.geoventure.geontm.inventory.RecipesCommon.AStack;
import fr.geoventure.geontm.inventory.RecipesCommon.ComparableStack;
import fr.geoventure.geontm.inventory.RecipesCommon.TagStack;
import fr.geoventure.geontm.inventory.fluid.FluidStack;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.inventory.material.MaterialShapes;
import fr.geoventure.geontm.inventory.material.Mats;
import fr.geoventure.geontm.inventory.material.NTMMaterial;
import fr.geoventure.geontm.inventory.recipes.OutgasserRecipes;
import fr.geoventure.geontm.inventory.recipes.OutgasserRecipes.OutgasserRecipe;
import fr.geoventure.geontm.items.ItemEnums.EnumTarType;
import fr.geoventure.geontm.items.ModFinalConsumableItems;
import fr.geoventure.geontm.items.ModFuelItems;
import fr.geoventure.geontm.items.ModItems;
import fr.geoventure.geontm.items.ModOilItems;
import fr.geoventure.geontm.inventory.recipes.loader.GenericTableSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * 1.7 {@code OutgasserRecipes.registerDefaults}. 1.7 keys rows by input; the port names each row
 * {@code outgasser.<what it makes>}, and every row states 1.7's fixed processing time as its duration.
 */
public final class OutgasserTable extends GenericTableSource<OutgasserRecipe> {

    private static final NTMMaterial COAL = Mats.MAT_COAL;

    public OutgasserTable() {
        super(OutgasserRecipes.INSTANCE);
    }

    @Override
    protected void registerDefaults() {

        // lithium to tritium
        put("tritium_block", LI.block(), null, new FluidStack(Fluids.TRITIUM, 10_000));
        put("tritium_ingot", LI.ingot(), null, new FluidStack(Fluids.TRITIUM, 1_000));
        put("tritium_dust", LI.dust(), null, new FluidStack(Fluids.TRITIUM, 1_000));
        put("tritium_dust_tiny", dustTiny(LI), null, new FluidStack(Fluids.TRITIUM, 100));

        // gold to gold-198
        put("au198_ingot", GOLD.ingot(), new ItemStack(ModItems.ingot_au198.get()), null);
        put("au198_nugget", GOLD.nugget(), new ItemStack(ModItems.nugget_au198.get()), null);
        put("au198_dust", GOLD.dust(), new ItemStack(ModItems.powder_au198.get()), null);

        // thorium to thorium fuel
        put("thorium_fuel_ingot", TH232.ingot(), new ItemStack(ModItems.ingot_thorium_fuel.get()), null);
        put("thorium_fuel_nugget", TH232.nugget(), new ItemStack(ModItems.nugget_thorium_fuel.get()), null);
        put("thorium_fuel_billet", TH232.billet(), new ItemStack(ModItems.billet_thorium_fuel.get()), null);

        // mushrooms to glowing mushrooms
        put("mush_brown", new ComparableStack(Items.BROWN_MUSHROOM), new ItemStack(ModTerrainBlocks.mush.get()), null);
        put("mush_red", new ComparableStack(Items.RED_MUSHROOM), new ItemStack(ModTerrainBlocks.mush.get()), null);
        put("glowing_stew", new ComparableStack(Items.MUSHROOM_STEW), new ItemStack(ModFinalConsumableItems.glowing_stew.get()), null);

        put("coal_gem", COAL.gem(), ModOilItems.tar(EnumTarType.COAL, 1), new FluidStack(Fluids.SYNGAS, 50));
        put("coal_dust", COAL.dust(), ModOilItems.tar(EnumTarType.COAL, 1), new FluidStack(Fluids.SYNGAS, 50));
        put("coal_block", COAL.block(), ModOilItems.tar(EnumTarType.COAL, 9), new FluidStack(Fluids.SYNGAS, 500));

        put("pvc", PVC.ingot(), new ItemStack(ModFuelItems.ingot_c4.get()), new FluidStack(Fluids.COLLOID, 250));

        put("coal_tar", new ComparableStack(ModOilItems.tar(EnumTarType.COAL)), null, new FluidStack(Fluids.COALOIL, 100));
        put("wax_tar", new ComparableStack(ModOilItems.tar(EnumTarType.WAX)), null, new FluidStack(Fluids.RADIOSOLVENT, 100));
    }

    /** 1.7 {@code recipes.put(input, new OutgasserRecipe(solid, fluid))}, under the port's row name. */
    private void put(String name, AStack input, ItemStack solid, FluidStack fluid) {
        OutgasserRecipe recipe = new OutgasserRecipe("outgasser." + name).inputItems(input);
        if (solid != null) recipe.outputItems(solid);
        if (fluid != null) recipe.outputFluids(fluid);
        recipe.setup(OutgasserRecipes.DURATION, 0);
        register(recipe);
    }

    /** 1.7 {@code new OreDictStack(mat.dustTiny())}. */
    private static AStack dustTiny(NTMMaterial mat) {
        return new TagStack(mat.tag(MaterialShapes.DUSTTINY));
    }
}
