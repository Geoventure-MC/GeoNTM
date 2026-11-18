package fr.geoventure.geontm.recipes.data;

import fr.geoventure.geontm.api.data.ArmorDataMaps;
import fr.geoventure.geontm.util.ArmorRegistry.HazardClass;
import fr.geoventure.geocore.data.DataMapSource;
import java.util.List;

/** 1.7 {@code ArmorGasMask.getBlacklist} and {@code ItemModGasmask.getBlacklist}; the hazmat hoods have none. */
public final class GasMaskBlacklistSource extends DataMapSource<List<HazardClass>> {

    private static final List<HazardClass> MONO = List.of(HazardClass.GAS_LUNG, HazardClass.GAS_BLISTERING, HazardClass.BACTERIA);
    private static final List<HazardClass> OTHERS = List.of(HazardClass.GAS_BLISTERING);

    public GasMaskBlacklistSource() {
        super(ArmorDataMaps.GAS_MASK_BLACKLIST, false);
    }

    @Override
    protected void values() {
        put("geontm:gas_mask", OTHERS);
        put("geontm:gas_mask_m65", OTHERS);
        put("geontm:gas_mask_mono", MONO);
        put("geontm:gas_mask_olde", OTHERS);
        put("geontm:attachment_mask", OTHERS);
        put("geontm:attachment_mask_mono", MONO);
    }
}
