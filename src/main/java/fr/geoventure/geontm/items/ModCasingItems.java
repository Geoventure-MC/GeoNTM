package fr.geoventure.geontm.items;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geontm.items.ItemEnums.EnumCasingType;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

/** Every identity from 1.7's metadata-backed {@code casing} item. */
public final class ModCasingItems {

    private ModCasingItems() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ITEMS);

    private static final Map<EnumCasingType, RegistryObject<Item>> CASINGS = new EnumMap<>(EnumCasingType.class);
    private static final EnumCasingType[] PORTED = {
            EnumCasingType.SMALL,
            EnumCasingType.LARGE,
            EnumCasingType.SMALL_STEEL,
            EnumCasingType.LARGE_STEEL,
            EnumCasingType.SHOTSHELL,
            EnumCasingType.BUCKSHOT,
            EnumCasingType.BUCKSHOT_ADVANCED
    };

    static {
        for (EnumCasingType type : PORTED) {
            // The original generated catalogue already owns this flattened identity.
            if (type == EnumCasingType.BUCKSHOT) {
                CASINGS.put(type, ModItems.casing_buckshot);
                continue;
            }
            String id = "casing_" + type.name().toLowerCase(Locale.ROOT);
            // the four brass/steel casings keep the 1.7 metadata sprites (casing.<type>); the shotgun ones are their own
            boolean ownSprite = type == EnumCasingType.SHOTSHELL || type == EnumCasingType.BUCKSHOT_ADVANCED;
            RegistryObject<Item> casing = REG.item(id).transform(ownSprite ? Presets.sprite()
                    : Presets.sprite("geontm:items/casing." + type.name().toLowerCase(Locale.ROOT)));
            CASINGS.put(type, casing);
            ModTabs.addTo(GeoNtmTab.MATERIALS, casing);
        }
    }

    public static Item casing(EnumCasingType type) {
        return CASINGS.get(type).get();
    }

    public static ItemStack casing(EnumCasingType type, int count) {
        return new ItemStack(casing(type), count);
    }

    public static void init() {}
}
