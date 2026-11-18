package fr.geoventure.geontm.recipes.data;

import fr.geoventure.geontm.api.data.ArmorDataMaps;
import fr.geoventure.geocore.data.DataMapSource;

/** 1.7 {@code TileEntityLockableBase}: {@code checkArmorPiece(player, jackt, 2) || checkArmorPiece(player, jackt2, 2)}. */
public final class LockpickJacketSource extends DataMapSource<Boolean> {

    public LockpickJacketSource() {
        super(ArmorDataMaps.LOCKPICK_JACKET, false);
    }

    @Override
    protected void values() {
        put("geontm:jackt", true);
        put("geontm:jackt2", true);
    }
}
