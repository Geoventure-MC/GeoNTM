package fr.geoventure.geontm.recipes.data;

import fr.geoventure.geontm.api.data.DepositDataMaps;
import fr.geoventure.geontm.api.data.DepositDataMaps.Deposit;
import fr.geoventure.geocore.data.DataMapSource;
import java.util.Optional;

/**
 * 1.7 {@code TileEntityOilDrillBase} and its drills: {@code ore_oil} gives oil and gas and drains into
 * {@code ore_oil_empty}; {@code ore_bedrock_oil} is fracking-only, a tenth of the yield, and never drains.
 */
public final class FluidDepositSource extends DataMapSource<Deposit> {

    public FluidDepositSource() {
        super(DepositDataMaps.FLUID_DEPOSIT, false);
    }

    @Override
    protected void values() {
        put("geontm:ore_oil", new Deposit("geontm:oil", "geontm:gas", Optional.of("geontm:ore_oil_empty"), 1D, Optional.empty(), 1D, false));
        put("geontm:ore_bedrock_oil", new Deposit("geontm:oil", "geontm:gas", Optional.empty(), 0.1D, Optional.empty(), 1D, true));
    }
}
