package fr.geoventure.geontm.api.data;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.handler.ConsumableTreatment;
import com.mojang.serialization.Codec;
import fr.geoventure.geocore.data.DataMaps;
import fr.geoventure.geocore.data.DataMaps.DataMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

/**
 * HBM's per-entry values as data (GeoCore {@link DataMaps}): {@code data/geontm/value_maps/<registry>/<name>.json}.
 * HBM's own values are those files in HBM's jar; addons, datapacks and KubeJS add to or change them in the same files
 * (every pack's copy merges). Read a value with {@code MAP.get(entry)} at use time, never cache it across a reload.
 */
public final class GeoNtmDataMaps {

    private GeoNtmDataMaps() {}

    private static <V> DataMap<Item, V> item(String name, Codec<V> codec) {
        return DataMaps.map(GeoNtmDataMaps.class, Constants.MOD_ID, name, "item", () -> BuiltInRegistries.ITEM, codec);
    }

    /** Brick furnace: how many burn ticks one operation on this input takes away per tick (1.7 {@code TileEntityFurnaceBrick}). */
    public static final DataMap<Item, Integer> BRICK_FURNACE_SPEED = item("brick_furnace_speed", Codec.INT);

    /** What eating, using or injecting the item does (pills, syringes, drinks, foods; any item a pack names). */
    public static final DataMap<Item, ConsumableTreatment> CONSUMABLE_TREATMENT = item("consumable_treatment",
            ConsumableTreatment.CODEC);

    /** Loads the class at mod construction (GeoNtmEntry.declare). */
    public static void declare() {
        HazardDataMaps.declare();
        ArmorDataMaps.declare();
        ReactorDataMaps.declare();
        WorldDataMaps.declare();
        FluidDataMaps.declare();
        SatelliteDataMaps.declare();
        MachineDataMaps.declare();
        DiFurnaceDataMaps.declare();
        MachinePartDataMaps.declare();
        DepositDataMaps.declare();
        RadiationDataMaps.declare();
    }
}
