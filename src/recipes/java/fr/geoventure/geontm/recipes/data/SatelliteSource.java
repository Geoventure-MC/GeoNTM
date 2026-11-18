package fr.geoventure.geontm.recipes.data;

import fr.geoventure.geontm.api.data.SatelliteDataMaps;
import fr.geoventure.geocore.data.DataMapSource;

/**
 * 1.7 {@code XSatelliteRegistry.registerSatellite}: the {@code satellite} chips (one item per {@code EnumSatType}, in the
 * enum's order) and {@code sat_gerald}. The legacy chips are left out: 1.7 refused them (their class was already mapped).
 */
public final class SatelliteSource extends DataMapSource<String> {

    public SatelliteSource() {
        super(SatelliteDataMaps.SATELLITE, false);
    }

    @Override
    protected void values() {
        put("geontm:satellite_spy", "mapper");
        put("geontm:satellite_scanner", "scanner");
        put("geontm:satellite_radar", "radar");
        put("geontm:satellite_miner_astro", "miner");
        put("geontm:satellite_miner_lunar", "lunar_miner");
        put("geontm:satellite_precision_laser", "precision_laser");
        put("geontm:satellite_death_ray", "laser");
        put("geontm:satellite_xenium_resonator", "resonator");
        put("geontm:satellite_relay", "relay");
        put("geontm:satellite_detector", "detector");
        put("geontm:satellite_ray_scan", "ray_scan");
        put("geontm:satellite_science", "science");
        put("geontm:sat_gerald", "horizons");
    }
}
