package fr.geoventure.geontm.recipes.data;

import fr.geoventure.geontm.api.data.MachinePartDataMaps;
import fr.geoventure.geocore.data.DataMapSource;

/**
 * 1.7 {@code ModuleBurnTime}: the item classes its name scan cannot see, the solid fuels ({@code solid}), the balefire
 * solid fuels ({@code balefire}) and rocket fuel ({@code rocket}), in its order.
 */
public final class FuelCategorySource extends DataMapSource<String> {

    public FuelCategorySource() {
        super(MachinePartDataMaps.FUEL_CATEGORY, false);
    }

    @Override
    protected void values() {
        put("geontm:solid_fuel", "solid");
        put("geontm:solid_fuel_presto", "solid");
        put("geontm:solid_fuel_presto_triplet", "solid");
        // 1.7 if(stack.getItem() == ModItems.solid_fuel_bf) return mod[modBalefire]; and the two presto forms
        put("geontm:solid_fuel_bf", "balefire");
        put("geontm:solid_fuel_presto_bf", "balefire");
        put("geontm:solid_fuel_presto_triplet_bf", "balefire");
        put("geontm:rocket_fuel", "rocket");
    }
}
