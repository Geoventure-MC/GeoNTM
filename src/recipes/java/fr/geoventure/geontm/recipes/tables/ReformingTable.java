package fr.geoventure.geontm.recipes.tables;

import fr.geoventure.geontm.inventory.fluid.FluidStack;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.inventory.recipes.ReformingRecipes;
import fr.geoventure.geontm.inventory.recipes.ReformingRecipes.Row;
import fr.geoventure.geocore.world.RecipeSource;
import java.util.List;

/** 1.7 {@code ReformingRecipes.registerDefaults}. */
public final class ReformingTable extends RecipeSource<Row> {

    public ReformingTable() {
        super(ReformingRecipes.TABLE.table.table);
    }

    @Override
    protected void registerDefaults() {
        put(Fluids.HEATINGOIL,
                new FluidStack(Fluids.NAPHTHA, 50),
                new FluidStack(Fluids.PETROLEUM, 15),
                new FluidStack(Fluids.HYDROGEN, 10)
                );
        put(Fluids.NAPHTHA,
                new FluidStack(Fluids.REFORMATE, 50),
                new FluidStack(Fluids.PETROLEUM, 15),
                new FluidStack(Fluids.HYDROGEN, 10)
                );
        put(Fluids.NAPHTHA_CRACK,
                new FluidStack(Fluids.REFORMATE, 50),
                new FluidStack(Fluids.AROMATICS, 10),
                new FluidStack(Fluids.HYDROGEN, 5)
                );
        put(Fluids.NAPHTHA_COKER,
                new FluidStack(Fluids.REFORMATE, 50),
                new FluidStack(Fluids.REFORMGAS, 10),
                new FluidStack(Fluids.HYDROGEN, 5)
                );
        put(Fluids.LIGHTOIL,
                new FluidStack(Fluids.AROMATICS, 50),
                new FluidStack(Fluids.REFORMGAS, 10),
                new FluidStack(Fluids.HYDROGEN, 15)
                );
        put(Fluids.LIGHTOIL_CRACK,
                new FluidStack(Fluids.AROMATICS, 50),
                new FluidStack(Fluids.REFORMGAS, 5),
                new FluidStack(Fluids.HYDROGEN, 20)
                );
        put(Fluids.PETROLEUM,
                new FluidStack(Fluids.UNSATURATEDS, 85),
                new FluidStack(Fluids.REFORMGAS, 10),
                new FluidStack(Fluids.HYDROGEN, 5)
                );
        put(Fluids.SOURGAS,
                new FluidStack(Fluids.SULFURIC_ACID, 75),
                new FluidStack(Fluids.PETROLEUM, 10),
                new FluidStack(Fluids.HYDROGEN, 15)
                );
        put(Fluids.CHOLESTEROL,
                new FluidStack(Fluids.ESTRADIOL, 50),
                new FluidStack(Fluids.REFORMGAS, 35),
                new FluidStack(Fluids.HYDROGEN, 15)
                );
    }

    /** 1.7 {@code recipes.put(input, new Triplet(out1, out2, out3))}; the id is the input fluid. */
    private void put(FluidType input, FluidStack out1, FluidStack out2, FluidStack out3) {
        register(input.getKey(), new Row(input, List.of(out1, out2, out3)));
    }
}
