package fr.geoventure.geontm.items;

import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import java.util.function.Supplier;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geontm.handler.ArmorUtil;
import fr.geoventure.geontm.handler.HazmatRegistry;
import fr.geoventure.geontm.items.armor.ItemGasMask;
import fr.geoventure.geontm.items.machine.ItemGear;
import fr.geoventure.geontm.util.ArmorRegistry;
import java.util.stream.IntStream;
import fr.geoventure.geontm.items.tool.ItemDosimeter;
import fr.geoventure.geontm.items.tool.ItemFilter;
import fr.geoventure.geontm.items.tool.ItemGeigerCounter;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import fr.geoventure.geocore.world.ArmorSlot;
import fr.geoventure.geocore.world.CompatArmorItem;
import fr.geoventure.geocore.world.GeoCoreArmorMaterial;

/**
 * Hand-registered gear: the radiation diagnostic tools (geiger counter / dosimeter) and the three hazmat
 * armor sets. Kept separate from the generated {@link ModItems} so codegen
 * never clobbers them. {@link #registerHazards()} wires the {@code HazmatRegistry} resistance coefficients
 * + {@code ArmorUtil} full-set membership (called post-registration by {@code Hazards.init}).
 */
public final class ModGearItems {

    private ModGearItems() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ITEMS);

    /** 1.7 {@code ItemFilter}: every filter wears out after 20,000 damage. */
    private static final int FILTER_DURABILITY = 20_000;

    // Diagnostic tools
    public static final RegistryObject<ItemGeigerCounter> geiger_counter =
            REG.item("geiger_counter", ItemGeigerCounter::new, p -> p.stacksTo(1)).transform(Presets.sprite());
    public static final RegistryObject<ItemDosimeter> dosimeter =
            REG.item("dosimeter", ItemDosimeter::new, p -> p.stacksTo(1)).transform(Presets.sprite());
    // 1.7 gear_large metadata 0/1, one item each
    public static final RegistryObject<ItemGear> gear_large =
            REG.item("gear_large", p -> new ItemGear(p, 0));
    public static final RegistryObject<ItemGear> gear_large_steel =
            REG.item("gear_large_steel", p -> new ItemGear(p, 1));

    /** The gear for 1.7 metadata {@code variant} (0 regular, 1 steel). */
    public static ItemStack gear(int variant) {
        return new ItemStack(variant == 1 ? gear_large_steel.get() : gear_large.get());
    }

    // Hazmat suits (yellow / red / grey)
    public static final RegistryObject<Item> hazmat_helmet = armor("hazmat_helmet", () -> GeoNtmArmorMaterials.HAZMAT, ArmorSlot.HELMET);
    public static final RegistryObject<Item> hazmat_plate = armor("hazmat_plate", () -> GeoNtmArmorMaterials.HAZMAT, ArmorSlot.CHESTPLATE);
    public static final RegistryObject<Item> hazmat_legs = armor("hazmat_legs", () -> GeoNtmArmorMaterials.HAZMAT, ArmorSlot.LEGGINGS);
    public static final RegistryObject<Item> hazmat_boots = armor("hazmat_boots", () -> GeoNtmArmorMaterials.HAZMAT, ArmorSlot.BOOTS);

    public static final RegistryObject<Item> hazmat_helmet_red = armor("hazmat_helmet_red", () -> GeoNtmArmorMaterials.HAZMAT_RED_HOOD, ArmorSlot.HELMET);
    public static final RegistryObject<Item> hazmat_plate_red = armor("hazmat_plate_red", () -> GeoNtmArmorMaterials.HAZMAT_RED, ArmorSlot.CHESTPLATE);
    public static final RegistryObject<Item> hazmat_legs_red = armor("hazmat_legs_red", () -> GeoNtmArmorMaterials.HAZMAT_RED, ArmorSlot.LEGGINGS);
    public static final RegistryObject<Item> hazmat_boots_red = armor("hazmat_boots_red", () -> GeoNtmArmorMaterials.HAZMAT_RED, ArmorSlot.BOOTS);

    public static final RegistryObject<Item> hazmat_helmet_grey = armor("hazmat_helmet_grey", () -> GeoNtmArmorMaterials.HAZMAT_GREY_HOOD, ArmorSlot.HELMET);
    public static final RegistryObject<Item> hazmat_plate_grey = armor("hazmat_plate_grey", () -> GeoNtmArmorMaterials.HAZMAT_GREY, ArmorSlot.CHESTPLATE);
    public static final RegistryObject<Item> hazmat_legs_grey = armor("hazmat_legs_grey", () -> GeoNtmArmorMaterials.HAZMAT_GREY, ArmorSlot.LEGGINGS);
    public static final RegistryObject<Item> hazmat_boots_grey = armor("hazmat_boots_grey", () -> GeoNtmArmorMaterials.HAZMAT_GREY, ArmorSlot.BOOTS);

    // Gas masks and replaceable filters
    public static final RegistryObject<ItemGasMask> gas_mask = gasMask("gas_mask", "geontm:items/gas_mask");
    public static final RegistryObject<ItemGasMask> gas_mask_m65 = gasMask("gas_mask_m65", "geontm:items/gas_mask_m65");
    public static final RegistryObject<ItemGasMask> gas_mask_mono = gasMask("gas_mask_mono", "geontm:items/gas_mask_mono");
    public static final RegistryObject<ItemGasMask> gas_mask_olde = gasMask("gas_mask_olde", "geontm:items/gas_mask_olde");
    public static final RegistryObject<ItemFilter> gas_mask_filter_rag = filter("gas_mask_filter_rag", "geontm:items/gas_mask_filter_rag");
    public static final RegistryObject<ItemFilter> gas_mask_filter_piss = filter("gas_mask_filter_piss", "geontm:items/gas_mask_filter_piss");
    public static final RegistryObject<ItemFilter> gas_mask_filter_mono = filter("gas_mask_filter_mono", "geontm:items/gas_mask_filter_mono");
    public static final RegistryObject<ItemFilter> gas_mask_filter = filter("gas_mask_filter", "geontm:items/gas_mask_filter");
    public static final RegistryObject<ItemFilter> gas_mask_filter_combo = filter("gas_mask_filter_combo", "geontm:items/gas_mask_filter_combo");

    /** The material is read when the item is built: the materials are registry-backed (see {@link ModArmorItems}). */
    private static RegistryObject<Item> armor(String name, Supplier<GeoCoreArmorMaterial> material, ArmorSlot slot) {
        RegistryObject<Item> ro = REG.<Item>item(name, p -> new CompatArmorItem(material.get(), slot, p), p -> p)
                .transform(Presets.sprite());
        ModTabs.addTo(GeoNtmTab.CONSUMABLES, ro);
        return ro;
    }

    private static RegistryObject<ItemGasMask> gasMask(String name, String sprite) {
        RegistryObject<ItemGasMask> ro = REG.item(name,
                ItemGasMask::new, p -> p.stacksTo(1)).transform(Presets.sprite(sprite));
        ModTabs.addTo(GeoNtmTab.CONSUMABLES, ro);
        return ro;
    }

    private static RegistryObject<ItemFilter> filter(String name, String sprite) {
        RegistryObject<ItemFilter> ro = REG.item(name, ItemFilter::new, p -> p.stacksTo(1).durability(FILTER_DURABILITY))
                .transform(Presets.sprite(sprite));
        ModTabs.addTo(GeoNtmTab.CONSUMABLES, ro);
        return ro;
    }

    static {
        ModTabs.addTo(GeoNtmTab.CONSUMABLES, geiger_counter);
        ModTabs.addTo(GeoNtmTab.CONSUMABLES, dosimeter);
        ModTabs.addStacksTo(GeoNtmTab.MATERIALS, () -> IntStream.range(0, 2)
                .mapToObj(ModGearItems::gear).toList());
    }

    // 1.7 HazmatRegistry.initDefault / ArmorUtil.register for these items (hazmat sets, gas masks and filters, vanilla
    // iron and gold armour) are the geontm:radiation_resistance, geontm:protection_classes and geontm:armor_sets data maps.

    public static void init() {}
}
