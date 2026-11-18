package fr.geoventure.geontm.recipes.data;

import fr.geoventure.geontm.api.data.FluidDataMaps;
import fr.geoventure.geocore.data.DataMapSource;

/** 1.7 {@code TileEntityMachineAutosaw.acceptedFuels}. */
public final class AutosawFuelSource extends DataMapSource<Boolean> {

    public AutosawFuelSource() {
        super(FluidDataMaps.AUTOSAW_FUEL, false);
    }

    @Override
    protected void values() {
        put("geontm:woodoil", true);
        put("geontm:ethanol", true);
        put("geontm:fishoil", true);
        put("geontm:heavyoil", true);
        // 1.7 acceptedFuels.add(Fluids.COALCREOSOTE)
        put("geontm:coalcreosote", true);
    }
}
