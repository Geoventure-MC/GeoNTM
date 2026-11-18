package fr.geoventure.geontm.api.data;

import fr.geoventure.geontm.Constants;
import com.mojang.serialization.Codec;
import fr.geoventure.geocore.data.DataMaps;
import fr.geoventure.geocore.data.DataMaps.DataMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

/**
 * Satellite data maps ({@link GeoNtmDataMaps} explains the format and the rules).
 */
public final class SatelliteDataMaps {

    private SatelliteDataMaps() {}

    /**
     * Which satellite an item puts into orbit: the {@code SatelliteType} name ({@code "mapper"}, an addon's
     * {@code "ntmspace:dyson_relay"}). 1.7 {@code XSatelliteRegistry.registerSatellite}: the eleven {@code satellite}
     * chips and {@code sat_gerald}. The legacy chips ({@code sat_mapper}, ...) are absent on purpose: 1.7's
     * {@code registerSatellite} refused them (their class was already mapped), so they launch nothing.
     */
    public static final DataMap<Item, String> SATELLITE =
            DataMaps.map(SatelliteDataMaps.class, Constants.MOD_ID, "satellite", "item", () -> BuiltInRegistries.ITEM, Codec.STRING);

    /** Loads the class at mod construction (from {@link GeoNtmDataMaps#declare()}). */
    public static void declare() {}
}
