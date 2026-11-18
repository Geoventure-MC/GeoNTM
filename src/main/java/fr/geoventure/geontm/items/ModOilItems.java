package fr.geoventure.geontm.items;

import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.items.ItemEnums.EnumCokeType;
import fr.geoventure.geontm.items.ItemEnums.EnumTarType;
import fr.geoventure.geontm.items.machine.ItemPistons;
import fr.geoventure.geontm.items.machine.ItemPistons.Variant;
import fr.geoventure.geontm.items.tool.ItemOilDetector;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

/**
 * The oil-chain items. The 1.12.2 metadata item {@code ModItems.oil_tar}
 * ({@code ItemEnumMulti<EnumTarType>}, old ModItems L1315) becomes one registered item per variant.
 * {@code oil_tar_<variant>} in the MATERIALS tab (same split convention as {@link ModIntermediateItems}).
 * Also hosts the {@code oil_detector} prospecting tool (old {@code consumableTab}).
 */
public final class ModOilItems {

    private ModOilItems() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ITEMS);

    private static final Map<EnumTarType, RegistryObject<Item>> TARS = new EnumMap<>(EnumTarType.class);
    private static final Map<EnumCokeType, RegistryObject<Item>> COKES = new EnumMap<>(EnumCokeType.class);
    private static final Map<Variant, RegistryObject<ItemPistons>> PISTONS = new EnumMap<>(Variant.class);

    static {
        for (EnumTarType v : EnumTarType.VALUES) {
            RegistryObject<Item> item = REG.item("oil_tar_" + v.name().toLowerCase(Locale.US))
                    .transform(Presets.sprite())
                    .tab(GeoNtmTab.MATERIALS);
            TARS.put(v, item);
        }
        for (EnumCokeType v : EnumCokeType.VALUES) {
            RegistryObject<Item> item = REG.item("coke_" + v.name().toLowerCase(Locale.US))
                    .transform(Presets.sprite())
                    .tab(GeoNtmTab.MATERIALS);
            COKES.put(v, item);
        }
        for (Variant v : Variant.VALUES) {
            RegistryObject<ItemPistons> item = REG.item(
                    "piston_set_" + v.name().toLowerCase(Locale.US),
                    p -> new ItemPistons(p, v), p -> p.stacksTo(1))
                    .transform(Presets.sprite())
                    .tab(GeoNtmTab.CONTROL);
            PISTONS.put(v, item);
        }
    }

    /** Oil Reservoir Detector, right-click prospecting for {@code ore_oil} deposits. */
    public static final RegistryObject<ItemOilDetector> oil_detector =
            reg("oil_detector", GeoNtmTab.CONSUMABLES);

    public static Item tar(EnumTarType v)                  { return TARS.get(v).get(); }
    public static ItemStack tar(EnumTarType v, int count)  { return new ItemStack(tar(v), count); }

    public static Item coke(EnumCokeType v)                { return COKES.get(v).get(); }
    public static ItemStack coke(EnumCokeType v, int count){ return new ItemStack(coke(v), count); }

    public static ItemPistons piston(Variant v)            { return PISTONS.get(v).get(); }
    public static ItemStack piston(Variant v, int count)   { return new ItemStack(piston(v), count); }

    private static RegistryObject<ItemOilDetector> reg(String name, GeoNtmTab tab) {
        return REG.item(name, ItemOilDetector::new, p -> p.stacksTo(1))
                .transform(Presets.sprite())
                .tab(tab);
    }

    public static void init() {}
}
