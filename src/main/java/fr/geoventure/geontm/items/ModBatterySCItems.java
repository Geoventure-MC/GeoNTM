package fr.geoventure.geontm.items;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.items.machine.ItemBatterySC;
import fr.geoventure.geontm.items.machine.ItemBatterySC.EnumBatterySC;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;

import java.util.EnumMap;
import java.util.Map;
import fr.geoventure.geontm.registration.Reg;
import net.minecraft.world.item.Item;
import fr.geoventure.geontm.registration.Presets;

/**
 * 1.7 {@code ModItems.battery_sc} (an {@code ItemBatterySC} enum-multi, control tab, stack size 1), flattened to
 * one {@code battery_sc_<variant>} item per {@link EnumBatterySC} in enum order.
 */
public final class ModBatterySCItems {
    private ModBatterySCItems() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ITEMS);

    public static final Map<EnumBatterySC, RegistryObject<ItemBatterySC>> BATTERIES = new EnumMap<>(EnumBatterySC.class);

    static {
        for (EnumBatterySC pack : EnumBatterySC.VALUES) {
            // Sprite battery_sc_<variant>; the recipes stay in GapsADeclarations.
            RegistryObject<ItemBatterySC> item = REG.<ItemBatterySC>item(pack.id(),
                            (Item.Properties p) -> new ItemBatterySC(p.stacksTo(1), pack))
                    .transform(Presets.sprite())
                    .tab(GeoNtmTab.CONTROL);
            BATTERIES.put(pack, item);
        }
    }

    public static ItemBatterySC get(EnumBatterySC pack) {
        return BATTERIES.get(pack).get();
    }

    public static void init() {}
}
