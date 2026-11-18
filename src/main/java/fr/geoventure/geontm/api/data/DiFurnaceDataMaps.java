package fr.geoventure.geontm.api.data;

import fr.geoventure.geontm.Constants;
import com.mojang.serialization.Codec;
import fr.geoventure.geocore.data.DataMaps;
import fr.geoventure.geocore.data.DataMaps.DataMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

/** The DiFurnace's per-item values as data ({@link GeoNtmDataMaps}): {@code data/geontm/value_maps/item/difurnace_fuel.json}. */
public final class DiFurnaceDataMaps {

    private DiFurnaceDataMaps() {}

    /**
     * DiFurnace fuel: the burn value one item adds (1.7 {@code TileEntityDiFurnace.getItemPower}, item-exact, plus the
     * coke tag and the briquettes). An item's own entry beats a tag.
     */
    public static final DataMap<Item, Integer> DIFURNACE_FUEL = DataMaps.map(DiFurnaceDataMaps.class, Constants.MOD_ID,
            "difurnace_fuel", "item", () -> BuiltInRegistries.ITEM, Codec.INT);

    /** Loads the class at mod construction ({@link GeoNtmDataMaps#declare}). */
    public static void declare() {}
}
