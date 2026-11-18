package fr.geoventure.geontm.items;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geontm.items.machine.ItemBatteryPack;
import fr.geoventure.geontm.items.machine.ItemBatteryPack.EnumBatteryPack;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;

import java.util.EnumMap;
import java.util.Map;
import fr.geoventure.geontm.registration.Reg;

/**
 * The GEONTM battery and capacitor items: the storage medium the {@code machine_battery_socket} holds.
 * One registered item per {@link EnumBatteryPack} variant (the old single metadata item is split into 12
 * in 26.1). Kept out of the generated {@link ModItems} so codegen never clobbers them.
 */
public final class ModBatteryItems {

    private ModBatteryItems() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ITEMS);

    /** Lookup from variant to its registered item, used by the socket renderer and item-icon JSON wiring. */
    public static final Map<EnumBatteryPack, RegistryObject<ItemBatteryPack>> PACKS = new EnumMap<>(EnumBatteryPack.class);

    static {
        for (EnumBatteryPack pack : EnumBatteryPack.VALUES) {
            // The OBJ item definitions are typed data (ObjItemDeclarations), not stated here.
            RegistryObject<ItemBatteryPack> item = REG.<ItemBatteryPack>item(
                    pack.name, p -> new ItemBatteryPack(p, pack), p -> p.stacksTo(1)).tab(GeoNtmTab.CONTROL);
            PACKS.put(pack, item);
            ModTabs.displayStacks(GeoNtmTab.CONTROL, item, () -> item.get().creativeStacks());
        }
    }

    public static void init() {}
}
