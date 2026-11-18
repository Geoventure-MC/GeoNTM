package fr.geoventure.geontm.recipes.data;

import fr.geoventure.geontm.api.data.MachineDataMaps;
import fr.geoventure.geontm.items.machine.ItemICFPellet.EnumICFFuel;
import fr.geoventure.geocore.data.DataMapSource;
import fr.geoventure.geocore.data.DataMaps.DataMap;

/**
 * 1.7 {@code ItemICFPellet.init}: {@code fluidMap} and {@code materialMap}, two files. 1.7's maps were {@code HashMap}s;
 * the files list their keys by name.
 */
public final class ICFFuelSource {

    private ICFFuelSource() {}

    /** 1.7 {@code fluidMap}, keyed by vanilla fluid id. */
    public static final class Fluid extends DataMapSource<EnumICFFuel> {

        public Fluid() {
            super(MachineDataMaps.ICF_FUEL_FLUID, false);
        }

        @Override
        protected void values() {
            put("geontm:chlorine", EnumICFFuel.CHLORINE);
            put("geontm:deuterium", EnumICFFuel.DEUTERIUM);
            put("geontm:helium3", EnumICFFuel.HELIUM3);
            put("geontm:helium4", EnumICFFuel.HELIUM4);
            put("geontm:hydrogen", EnumICFFuel.HYDROGEN);
            put("geontm:oxygen", EnumICFFuel.OXYGEN);
            put("geontm:tritium", EnumICFFuel.TRITIUM);
        }
    }

    /** 1.7 {@code materialMap}, keyed by GEONTM material name. */
    public static final class Material extends DataMapSource<EnumICFFuel> {

        public Material() {
            super(materialMap(), false);
        }

        @Override
        protected void values() {
            put("beryllium", EnumICFFuel.BERYLLIUM);
            put("boron", EnumICFFuel.BORON);
            put("calcium", EnumICFFuel.CALCIUM);
            put("graphite", EnumICFFuel.CARBON);
            put("lithium", EnumICFFuel.LITHIUM);
            put("sodium", EnumICFFuel.SODIUM);
        }
    }

    private static DataMap<?, EnumICFFuel> materialMap() {
        return MachineDataMaps.icfMaterialMap();
    }
}
