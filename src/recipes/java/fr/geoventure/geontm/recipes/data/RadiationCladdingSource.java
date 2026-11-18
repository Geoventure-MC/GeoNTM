package fr.geoventure.geontm.recipes.data;

import fr.geoventure.geontm.api.data.ArmorDataMaps;
import fr.geoventure.geocore.data.DataMapSource;

/** 1.7 {@code ModItems}: the {@code new ItemModCladding(rad)} lines. */
public final class RadiationCladdingSource extends DataMapSource<Double> {

    public RadiationCladdingSource() {
        super(ArmorDataMaps.RADIATION_CLADDING, false);
    }

    @Override
    protected void values() {
        put("geontm:cladding_paint", 0.025D);
        put("geontm:cladding_rubber", 0.005D);
        put("geontm:cladding_lead", 0.1D);
        put("geontm:cladding_desh", 0.2D);
        put("geontm:cladding_ghiorsium", 0.5D);
    }
}
