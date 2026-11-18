package fr.geoventure.geontm.recipes.data;

import fr.geoventure.geontm.api.data.FluidDataMaps;
import fr.geoventure.geocore.data.DataMapSource;

/** 1.7 {@code TileEntityMachineTurbineGas.fuelMaxCons}: mB per tick at full throttle; a fuel not listed burns 5. */
public final class GasTurbineFuelSource extends DataMapSource<Double> {

    public GasTurbineFuelSource() {
        super(FluidDataMaps.GAS_TURBINE_FUEL, false);
    }

    @Override
    protected void values() {
        put("geontm:gas", 50D);           // natgas doesn't burn well so it burns faster to compensate
        put("geontm:syngas", 10D);        // syngas just fucks
        put("geontm:oxyhydrogen", 100D);  // oxyhydrogen is terrible so it needs to burn a ton for the bare minimum
        put("geontm:reformgas", 5D);      // fuck it we ball
    }
}
