package fr.geoventure.geontm.recipes.data;

import static fr.geoventure.geontm.util.DamageResistanceHandler.CATEGORY_PHYSICAL;

import fr.geoventure.geontm.api.data.ArmorDataMaps;
import fr.geoventure.geontm.util.DamageResistanceHandler.ResistanceStats;
import fr.geoventure.geocore.data.DataMapSource;

/** 1.7 {@code DamageResistanceHandler.initDefaults}: the {@code itemStats} lines (single pieces, set or no set). */
public final class ItemDamageResistanceSource extends DataMapSource<ResistanceStats> {

    public ItemDamageResistanceSource() {
        super(ArmorDataMaps.DAMAGE_RESISTANCE, false);
    }

    @Override
    protected void values() {
        put("geontm:jackt", new ResistanceStats().addCategory(CATEGORY_PHYSICAL, 1F, 0.20F));
        put("geontm:jackt2", new ResistanceStats().addCategory(CATEGORY_PHYSICAL, 2F, 0.25F));
    }
}
