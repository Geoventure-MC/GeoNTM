package fr.geoventure.geontm.recipes.tables;

import fr.geoventure.geontm.inventory.fluid.FluidStack;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.inventory.recipes.CrackingRecipes.Row;
import fr.geoventure.geontm.inventory.recipes.RadiolysisRecipes;
import fr.geoventure.geocore.world.RecipeSource;

/**
 * 1.7 {@code RadiolysisRecipes.registerRadiolysis}. Its second half, copying every cracking row into the table, is not
 * a file: {@code RadiolysisRecipes} still merges the cracking table at run time.
 */
public final class RadiolysisTable extends RecipeSource<Row> {

    public RadiolysisTable() {
        super(RadiolysisRecipes.TABLE.table.table);
    }

    @Override
    protected void registerDefaults() {
        put(Fluids.WATER, new FluidStack(80, Fluids.PEROXIDE), new FluidStack(20, Fluids.HYDROGEN));

        //automatically add cracking recipes to the radiolysis recipe list
        //we want the numbers and types to stay consistent anyway and this will save us a lot of headache later on
        // (done at run time from the cracking table, see RadiolysisRecipes)
    }

    /** 1.7 {@code radiolysis.put(input, new Pair(left, right))}; the id is the input fluid. */
    private void put(FluidType input, FluidStack left, FluidStack right) {
        register(input.getKey(), new Row(input, left, right));
    }
}
