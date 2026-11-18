package fr.geoventure.geontm.items;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geontm.items.machine.EnumWatzType;
import fr.geoventure.geontm.items.machine.ItemWatzPellet;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

/**
 * The Watz power plant's fuel: one {@link ItemWatzPellet} per {@link EnumWatzType}, in a fresh and a
 * depleted flavour (24 items, replacing 1.12.2's two twelve-metadata items). Registry ids are
 * {@code watz_pellet_<type>} / {@code watz_pellet_depleted_<type>}, matching the pre-baked textures
 * written by {@code port-tools/gen_watz_pellet_textures.py}.
 */
public final class ModWatzItems {

    private ModWatzItems() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ITEMS);

    private static final Map<EnumWatzType, RegistryObject<ItemWatzPellet>> FRESH = new EnumMap<>(EnumWatzType.class);
    private static final Map<EnumWatzType, RegistryObject<ItemWatzPellet>> DEPLETED = new EnumMap<>(EnumWatzType.class);

    static {
        for (EnumWatzType type : EnumWatzType.VALUES) {
            RegistryObject<ItemWatzPellet> fresh = REG.item("watz_pellet_" + type.itemSuffix(),
                    p -> new ItemWatzPellet(p, type, false), p -> p.stacksTo(16))
                    .transform(Presets.sprite());
            RegistryObject<ItemWatzPellet> spent = REG.item("watz_pellet_depleted_" + type.itemSuffix(),
                    p -> new ItemWatzPellet(p, type, true), p -> p.stacksTo(16))
                    .transform(Presets.sprite());
            FRESH.put(type, fresh);
            DEPLETED.put(type, spent);
            ModTabs.addTo(GeoNtmTab.CONTROL, fresh);
            ModTabs.addTo(GeoNtmTab.CONTROL, spent);
        }
    }

    public static ItemWatzPellet fresh(EnumWatzType type) { return FRESH.get(type).get(); }

    public static ItemWatzPellet depleted(EnumWatzType type) { return DEPLETED.get(type).get(); }

    /** The depleted counterpart of a fresh pellet item, or null if the stack isn't a fresh pellet. */
    public static @Nullable ItemWatzPellet depletedFor(Item item) {
        return item instanceof ItemWatzPellet pellet && !pellet.depleted ? depleted(pellet.type) : null;
    }

    public static void init() {}
}
