package fr.geoventure.geontm.items;

import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geontm.items.food.ItemPill;
import fr.geoventure.geontm.items.special.ItemConsumable;
import fr.geoventure.geontm.items.special.ItemSimpleConsumable;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import fr.geoventure.geocore.world.CompatConsumables;
import net.minecraft.world.item.Item;

import java.util.List;

/** Hand-registered medicine and injection items formerly constructed in {@code ModItems}. */
public final class ModMedicineItems {

    private ModMedicineItems() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ITEMS);

    private static Reg.Handle<ItemPill> pill(String id, GeoNtmTab tab) {
        Reg.Handle<ItemPill> item = REG.item(id, ItemPill::new,
                p -> CompatConsumables.foodAlwaysEdible(p, 0, 0F, List.of()));
        if (tab != null) ModTabs.addTo(tab, item);
        return item;
    }

    private static Reg.Handle<ItemConsumable> consumable(String id, GeoNtmTab tab, boolean stackOne) {
        Reg.Handle<ItemConsumable> item = REG.item(id, p -> new ItemConsumable(p, id),
                p -> stackOne ? p.stacksTo(1) : p);
        if (tab != null) ModTabs.addTo(tab, item);
        return item;
    }

    private static Reg.Handle<ItemSimpleConsumable> simple(String id, GeoNtmTab tab) {
        Reg.Handle<ItemSimpleConsumable> item = REG.item(id,
                p -> new ItemSimpleConsumable(p, id));
        if (tab != null) ModTabs.addTo(tab, item);
        return item;
    }

    /** 1.7 ItemSimpleConsumable: draws 5 health into a blood bag (the IV bags live in ModArmorSetBItems). */
    public static final RegistryObject<ItemSimpleConsumable> iv_empty = simple("iv_empty", GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());

    public static final RegistryObject<ItemConsumable> syringe_awesome = consumable("syringe_awesome", GeoNtmTab.CONSUMABLES, false)
            .transform(Presets.handheld());
    public static final RegistryObject<ItemConsumable> syringe_antidote = consumable("syringe_antidote", GeoNtmTab.CONSUMABLES, false)
            .transform(Presets.handheld());
    public static final RegistryObject<ItemConsumable> syringe_poison = consumable("syringe_poison", GeoNtmTab.CONSUMABLES, false)
            .transform(Presets.handheld());
    public static final RegistryObject<ItemConsumable> syringe_metal_medx = consumable("syringe_metal_medx", GeoNtmTab.CONSUMABLES, false)
            .transform(Presets.handheld());
    public static final RegistryObject<ItemConsumable> syringe_metal_psycho = consumable("syringe_metal_psycho", GeoNtmTab.CONSUMABLES, false)
            .transform(Presets.handheld());
    public static final RegistryObject<ItemConsumable> syringe_metal_stimpak = consumable("syringe_metal_stimpak", GeoNtmTab.CONSUMABLES, false)
            .transform(Presets.handheld());
    public static final RegistryObject<ItemConsumable> syringe_metal_super = consumable("syringe_metal_super", GeoNtmTab.CONSUMABLES, false)
            .transform(Presets.handheld());
    public static final RegistryObject<ItemConsumable> syringe_taint = consumable("syringe_taint", GeoNtmTab.CONSUMABLES, false)
            .transform(Presets.handheld());
    public static final RegistryObject<ItemConsumable> syringe_mkunicorn = consumable("syringe_mkunicorn", null, false)
            .transform(Presets.handheld());
    public static final RegistryObject<ItemConsumable> med_bag = consumable("med_bag", GeoNtmTab.CONSUMABLES, false)
            .transform(Presets.sprite());

    public static final RegistryObject<ItemPill> radx = pill("radx", GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemPill> siox = pill("siox", GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemPill> pill_herbal = pill("pill_herbal", GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemPill> xanax = pill("xanax", GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemPill> fmn = pill("fmn", GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemPill> five_htp = pill("five_htp", GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemPill> pill_iodine = pill("pill_iodine", GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemPill> plan_c = pill("plan_c", GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemPill> pill_red = pill("pill_red", GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemPill> chocolate = pill("chocolate", GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());

    public static final RegistryObject<ItemSimpleConsumable> radaway = simple("radaway", GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemSimpleConsumable> radaway_strong = simple("radaway_strong", GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemSimpleConsumable> radaway_flush = simple("radaway_flush", GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());

    public static final RegistryObject<ItemConsumable> jetpack_tank = consumable("jetpack_tank", GeoNtmTab.CONSUMABLES, true)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemConsumable> cbt_device = consumable("cbt_device", null, true)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemConsumable> euphemium_stopper = consumable("euphemium_stopper", null, true)
            .transform(Presets.sprite());

    public static void init() {}
}
