package fr.geoventure.geontm.recipes.data;

import fr.geoventure.geontm.api.data.WorldDataMaps;
import fr.geoventure.geocore.data.DataMapSource;

/** 1.7 {@code ExplosionNT.errosion} (the {@code ERRODE} attribute's table). */
public final class ErosionSource extends DataMapSource<String> {

    public ErosionSource() {
        super(WorldDataMaps.EROSION, false);
    }

    @Override
    protected void values() {
        put("geontm:concrete", "minecraft:gravel");
        put("geontm:concrete_smooth", "minecraft:gravel");
        put("geontm:brick_concrete", "geontm:brick_concrete_broken");
        put("geontm:brick_concrete_broken", "minecraft:gravel");
    }
}
