package fr.geoventure.geontm.recipes.data;

import fr.geoventure.geontm.api.data.MachinePartDataMaps;
import fr.geoventure.geontm.api.data.MachinePartDataMaps.Coil;
import fr.geoventure.geocore.data.DataMapSource;

/** 1.7 {@code ItemPACoil.EnumCoilType} (quadrupole min/max, dipole min/max, dipole minimum distance). */
public final class PACoilSource extends DataMapSource<Coil> {

    public PACoilSource() {
        super(MachinePartDataMaps.PA_COIL, false);
    }

    @Override
    protected void values() {
        put("geontm:pa_coil_gold", new Coil(0, 2_200, 0, 2_200, 15));
        put("geontm:pa_coil_niobium", new Coil(1_500, 8_400, 1_500, 8_400, 21));
        put("geontm:pa_coil_bscco", new Coil(7_500, 15_000, 7_500, 15_000, 27));
        put("geontm:pa_coil_chlorophyte", new Coil(14_500, 75_000, 14_500, 75_000, 51));
    }
}
