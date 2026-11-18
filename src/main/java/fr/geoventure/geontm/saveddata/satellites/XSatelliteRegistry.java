package fr.geoventure.geontm.saveddata.satellites;

import fr.geoventure.geontm.api.data.SatelliteDataMaps;
import fr.geoventure.geontm.api.environment.Environment;
import fr.geoventure.geontm.items.machine.ItemSatellite;
import fr.geoventure.geontm.items.machine.ItemSatellite.EnumSatType;
import fr.geoventure.geontm.saveddata.SatelliteSavedData;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.hook.Hook;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

/**
 * 1.7 {@code XSatelliteRegistry}: which item launches which satellite, and the orbit entry point.
 *
 * <p>1.7 registers twelve of the {@code satellite} variants, then {@code sat_gerald} for Horizons, then "all the
 * legacy crap" ({@code sat_mapper}, {@code sat_scanner}, {@code sat_radar}, {@code sat_laser},
 * {@code sat_resonator}, {@code sat_miner}, {@code sat_lunar_miner}). {@code registerSatellite} refuses any class
 * that is already a value in the map, and every legacy chip names a class a variant already claimed, so none of
 * those seven calls registers anything: in 1.7 a legacy chip carries a frequency but launches nothing. That is
 * ported as it behaves, not as it reads. {@code sat_foeq}, {@code sat_relay} and {@code sat_chip} were never
 * registered at all.
 */
public final class XSatelliteRegistry {

    private XSatelliteRegistry() {}

    /**
     * The satellite class an item launches, or null where it launches nothing: the {@code geontm:satellite} item data
     * map ({@link SatelliteDataMaps#SATELLITE}, {@code data/geontm/value_maps/item/satellite.json}), which carries 1.7's
     * {@code registerSatellite} calls. An addon chip adds a line to that file in its own jar.
     */
    public static @Nullable SatelliteType typeFor(ItemStack stack) {
        if (stack.isEmpty()) return null;
        String name = SatelliteDataMaps.SATELLITE.get(stack.getItem());
        return name == null ? null : SatelliteType.byName(name);
    }

    /** 1.7 {@code registerSatellite(..., new ComparableStack(ModItems.satellite, 1, EnumSatType.X))}. */
    public static @Nullable SatelliteType forChip(EnumSatType chip) {
        return typeFor(ItemSatellite.make(chip));
    }

    /** The item that launches a satellite class (1.7 NEI's reverse lookup); empty for none. */
    public static ItemStack stackFor(SatelliteType type) {
        for (var entry : SatelliteDataMaps.SATELLITE.byId().entrySet()) {
            if (!entry.getValue().equals(type.name())) continue;
            var item = BuiltInRegistries.ITEM.get(Ids.parse(entry.getKey()));
            if (item != null && item != Items.AIR) return new ItemStack(item);
        }
        return ItemStack.EMPTY;
    }

    /** A listener told that a new satellite reached orbit from {@code stack}, before it is stored. */
    @FunctionalInterface
    public interface Orbited {
        void orbited(ServerLevel level, ItemStack stack, int freq, Satellite sat);
    }

    /**
     * Notified when {@link #orbit} creates a satellite, before it is stored and told it reached orbit: geontm-space copied
     * the chip's orbit settings onto the new satellite here; an addon copies its own item data into
     * {@link Satellite#extra}. {@code level} is the level whose satellites it joins.
     */
    public static final Hook<Orbited> ORBITED = Hook.notifying();

    /** 1.7 {@code createFromItem}. */
    public static @Nullable Satellite createFromItem(ItemStack stack) {
        SatelliteType type = typeFor(stack);
        return type == null ? null : type.create();
    }

    /**
     * 1.7 {@code orbit}: a frequency already in orbit is not replaced; the payload is handed to the satellite
     * that is already there ({@code onPartDelivered}). Otherwise the item's satellite, if it has one, is created,
     * stored and told it reached orbit.
     */
    public static void orbit(ServerLevel level, ItemStack stack, int freq, double x, double y, double z) {
        // geontm-space: launched from an orbital station, the satellite joins the body's below
        BlockPos at = BlockPos.containing(x, y, z);
        SatelliteSavedData data = SatelliteSavedData.get(level, at, freq);
        Satellite existing = data.sats.get(freq);

        if (existing != null) {
            existing.onPartDelivered(level, stack);
            data.setDirty();
        } else {
            Satellite sat = createFromItem(stack);

            if (sat != null) {
                ServerLevel target = Environment.satelliteLevel(level, at, stack);
                data = SatelliteSavedData.get(target);
                for (Orbited listener : ORBITED) listener.orbited(target, stack, freq, sat);
                data.put(freq, sat);
                sat.onOrbit(target, x, y, z);
            }
        }
    }
}
