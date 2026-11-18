package fr.geoventure.geontm.items;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geontm.items.food.ItemAppleEuphemium;
import fr.geoventure.geontm.items.food.ItemAppleSchrabidium;
import fr.geoventure.geontm.items.food.ItemBDCL;
import fr.geoventure.geontm.items.food.ItemBowlFood;
import fr.geoventure.geontm.items.food.ItemCanteen;
import fr.geoventure.geontm.items.food.ItemConserve;
import fr.geoventure.geontm.items.food.ItemConserve.EnumFoodType;
import fr.geoventure.geontm.items.food.ItemCottonCandy;
import fr.geoventure.geontm.items.food.ItemFlask;
import fr.geoventure.geontm.items.food.ItemModFood;
import fr.geoventure.geontm.items.food.ItemMuchoMango;
import fr.geoventure.geontm.items.food.ItemPancake;
import fr.geoventure.geontm.items.food.ItemSchnitzelVegan;
import fr.geoventure.geontm.items.food.ItemTemFlakes;
import fr.geoventure.geontm.items.food.ItemWaffle;
import fr.geoventure.geontm.items.machine.ItemBattery;
import fr.geoventure.geontm.items.machine.ItemFELCrystal;
import fr.geoventure.geontm.items.machine.ItemMachineUpgrade;
import fr.geoventure.geontm.items.machine.ItemMuffler;
import fr.geoventure.geontm.items.machine.ItemStamp;
import fr.geoventure.geontm.items.machine.ItemStamp.StampType;
import fr.geoventure.geontm.items.special.ItemCigarette;
import fr.geoventure.geontm.items.special.ItemCustomLore;
import fr.geoventure.geontm.items.special.ItemEnergy;
import fr.geoventure.geontm.items.special.ItemPotatos;
import fr.geoventure.geontm.items.special.ItemRag;
import fr.geoventure.geontm.items.tool.ItemBalefireMatch;
import fr.geoventure.geontm.items.tool.ItemCraftingDegradation;
import fr.geoventure.geontm.items.tool.ItemFertilizer;
import fr.geoventure.geontm.items.tool.ItemFusionCore;
import fr.geoventure.geontm.items.tool.ItemMatch;
import fr.geoventure.geontm.items.tool.ItemPeas;
import fr.geoventure.geontm.items.tool.ItemRepairKit;
import fr.geoventure.geontm.sound.ModSounds;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import fr.geoventure.geocore.world.CompatConsumables;
import fr.geoventure.geocore.world.Records;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

/**
 * The final food, consumable, record, battery, loose-material and machine-upgrade items, straight from 1.7
 * {@code ModItems}. Metadata families are flattened the port's way, {@code <base>_<variant>} in lower case:
 * {@code canned_conserve_beef}, {@code briquette_coal}, {@code item_secret_selenium_steel},
 * {@code circuit_star_piece_cpu_cache}, {@code stamp_book_printing1}, {@code parts_legendary_tier2},
 * {@code flask_infusion_shield}; the page family drops the doubled underscore ({@code page_of_page1}). The
 * numbered apple and tem-flake tiers keep the bare id for meta 0 and name the others by their crafting
 * material or tier ({@code apple_lead_ingot}, {@code tem_flakes_expensive}).
 *
 * <p>Items 1.7 put in a vanilla tab ({@code tabTools}: matches; {@code tabMisc}: records) go to the
 * consumables tab; the port's tabs are HBM's own. Items 1.7 hid ({@code setCreativeTab(null)}) stay hidden.
 */
public final class ModFinalConsumableItems {

    private ModFinalConsumableItems() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ITEMS);

    /** Every id this class registers, in order: the harness and GameTests walk it. */
    public static final List<String> IDS = new ArrayList<>();

    private static <I extends Item> Reg.Handle<I> reg(String id, GeoNtmTab tab, Function<Item.Properties, I> factory,
                                                      UnaryOperator<Item.Properties> props) {
        Reg.Handle<I> ro = REG.item(id, factory, props);
        if (tab != null) ModTabs.addTo(tab, ro);
        IDS.add(id);
        return ro;
    }

    private static Reg.Handle<Item> plain(String id, GeoNtmTab tab, UnaryOperator<Item.Properties> props) {
        return reg(id, tab, Item::new, props);
    }

    // Food

    /** 1.7 {@code ItemFood(heal, saturation, wolf)}; {@code ItemFood(heal, wolf)} means 0.6 saturation. */
    private static UnaryOperator<Item.Properties> food(int nutrition, float saturation, boolean alwaysEdible) {
        return p -> alwaysEdible ? CompatConsumables.foodAlwaysEdible(p, nutrition, saturation, List.of())
                : CompatConsumables.food(p, nutrition, saturation, List.of());
    }

    private static UnaryOperator<Item.Properties> and(UnaryOperator<Item.Properties> a, UnaryOperator<Item.Properties> b) {
        return p -> b.apply(a.apply(p));
    }

    /** In a holder: {@code Rarity} is game state, and only the properties lambdas read it. */
    private static final class Rarities {
        static final Rarity[] APPLE_RARITY = {Rarity.UNCOMMON, Rarity.RARE, Rarity.EPIC};
    }

    /** Every tier of an apple shows the plain apple's sprite. */
    private static RegistryObject<ItemAppleSchrabidium> apple(String id, boolean lead, int tier, int nutrition,
                                                               float saturation) {
        return reg(id, GeoNtmTab.CONSUMABLES, p -> new ItemAppleSchrabidium(p, tier),
                and(food(nutrition, saturation, true), p -> p.rarity(Rarities.APPLE_RARITY[tier])))
                .transform(Presets.sprite(lead ? "geontm:items/apple_lead" : "geontm:items/apple_schrabidium"));
    }

    public static final RegistryObject<ItemAppleSchrabidium> apple_lead = apple("apple_lead", true, 0, 5, 0F);
    public static final RegistryObject<ItemAppleSchrabidium> apple_lead_ingot = apple("apple_lead_ingot", true, 1, 5, 0F);
    public static final RegistryObject<ItemAppleSchrabidium> apple_lead_block = apple("apple_lead_block", true, 2, 5, 0F);
    public static final RegistryObject<ItemAppleSchrabidium> apple_schrabidium = apple("apple_schrabidium", false, 0, 20, 100F);
    public static final RegistryObject<ItemAppleSchrabidium> apple_schrabidium_ingot = apple("apple_schrabidium_ingot", false, 1, 20, 100F);
    public static final RegistryObject<ItemAppleSchrabidium> apple_schrabidium_block = apple("apple_schrabidium_block", false, 2, 20, 100F);
    public static final RegistryObject<ItemAppleEuphemium> apple_euphemium = reg("apple_euphemium", null,
            ItemAppleEuphemium::new, and(food(20, 100F, true), p -> p.stacksTo(1).rarity(Rarity.EPIC)))
            .transform(Presets.sprite());

    private static RegistryObject<ItemBowlFood> soup(String id) {
        return reg(id, GeoNtmTab.CONSUMABLES, ItemBowlFood::new,
                and(food(6, 0.6F, false), p -> CompatConsumables.usingConvertsTo(p.stacksTo(1), Items.BOWL)))
                .transform(Presets.sprite());
    }

    public static final RegistryObject<ItemBowlFood> glowing_stew = soup("glowing_stew");
    public static final RegistryObject<ItemBowlFood> balefire_scrambled = soup("balefire_scrambled");
    public static final RegistryObject<ItemBowlFood> balefire_and_ham = soup("balefire_and_ham");

    public static final RegistryObject<ItemWaffle> bomb_waffle = reg("bomb_waffle", GeoNtmTab.CONSUMABLES,
            ItemWaffle::new, food(20, 0.6F, false))
            .transform(Presets.sprite());
    public static final RegistryObject<ItemSchnitzelVegan> schnitzel_vegan = reg("schnitzel_vegan", GeoNtmTab.CONSUMABLES,
            ItemSchnitzelVegan::new, food(0, 0.6F, false))
            .transform(Presets.sprite());
    public static final RegistryObject<ItemCottonCandy> cotton_candy = reg("cotton_candy", GeoNtmTab.CONSUMABLES,
            ItemCottonCandy::new, food(5, 0.6F, true))
            .transform(Presets.handheld());
    public static final RegistryObject<ItemModFood> ingot_smore = reg("ingot_smore", GeoNtmTab.MATERIALS,
            ItemModFood::new, food(10, 20F, false))
            .transform(Presets.sprite());
    public static final RegistryObject<ItemMuchoMango> mucho_mango = reg("mucho_mango", GeoNtmTab.CONSUMABLES,
            ItemMuchoMango::new, food(10, 0.6F, true))
            .transform(Presets.sprite());
    public static final RegistryObject<ItemPancake> pancake = reg("pancake", GeoNtmTab.CONSUMABLES,
            ItemPancake::new, food(20, 20F, true))
            .transform(Presets.sprite());

    public static final RegistryObject<ItemTemFlakes> tem_flakes = reg("tem_flakes", GeoNtmTab.CONSUMABLES,
            p -> new ItemTemFlakes(p, 0), food(0, 0F, true))
            .transform(Presets.sprite("geontm:items/tem_flakes"));
    public static final RegistryObject<ItemTemFlakes> tem_flakes_normal = reg("tem_flakes_normal", GeoNtmTab.CONSUMABLES,
            p -> new ItemTemFlakes(p, 1), food(0, 0F, true))
            .transform(Presets.sprite("geontm:items/tem_flakes"));
    public static final RegistryObject<ItemTemFlakes> tem_flakes_expensive = reg("tem_flakes_expensive", GeoNtmTab.CONSUMABLES,
            p -> new ItemTemFlakes(p, 2), food(0, 0F, true))
            .transform(Presets.sprite("geontm:items/tem_flakes"));

    private static final Map<EnumFoodType, RegistryObject<ItemConserve>> CANNED = new EnumMap<>(EnumFoodType.class);

    static {
        for (EnumFoodType type : EnumFoodType.values()) {
            String id = "canned_conserve_" + type.name().toLowerCase(Locale.US);
            // ItemConserve names its sprites canned_<constant>
            CANNED.put(type, reg(id, GeoNtmTab.CONSUMABLES, p -> new ItemConserve(p, type),
                    food(type.foodLevel, type.saturation, false))
                    .transform(Presets.sprite("geontm:items/canned_" + type.name().toLowerCase(Locale.US))));
        }
    }

    public static Item canned(EnumFoodType type) {
        return CANNED.get(type).get();
    }

    public static final RegistryObject<ItemCanteen> canteen_vodka = reg("canteen_vodka", GeoNtmTab.CONSUMABLES,
            ItemCanteen::new, p -> p.durability(3 * 60))
            .transform(Presets.sprite());
    public static final RegistryObject<ItemEnergy> chocolate_milk = reg("chocolate_milk", GeoNtmTab.CONSUMABLES,
            p -> new ItemEnergy(p, "chocolate_milk", ItemEnergy.Container.NONE), p -> p)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemCigarette> cigarette = reg("cigarette", GeoNtmTab.CONSUMABLES,
            ItemCigarette::new, p -> p.stacksTo(16))
            .transform(Presets.handheld());
    public static final RegistryObject<ItemCigarette> crackpipe = reg("crackpipe", GeoNtmTab.CONSUMABLES,
            ItemCigarette::new, p -> p.stacksTo(1))
            .transform(Presets.handheld());
    public static final RegistryObject<ItemFlask> flask_infusion_shield = reg("flask_infusion_shield",
            GeoNtmTab.CONSUMABLES, ItemFlask::new, p -> p)
            .transform(Presets.sprite("geontm:items/flask_shield"));
    public static final RegistryObject<ItemBDCL> bdcl = reg("bdcl", GeoNtmTab.CONSUMABLES, ItemBDCL::new, p -> p)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemPeas> peas = reg("peas", GeoNtmTab.CONSUMABLES, ItemPeas::new, p -> p)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemMatch> matchstick = reg("matchstick", GeoNtmTab.CONSUMABLES,
            ItemMatch::new, p -> p)
            .transform(Presets.handheld());
    public static final RegistryObject<ItemBalefireMatch> balefire_and_steel = reg("balefire_and_steel",
            GeoNtmTab.CONSUMABLES, ItemBalefireMatch::new, p -> p.durability(256))
            .transform(Presets.handheld());
    public static final RegistryObject<ItemRepairKit> gun_kit_1 = reg("gun_kit_1", GeoNtmTab.CONSUMABLES,
            p -> new ItemRepairKit(p, () -> ModSounds.SPRAY.get()), p -> p.durability(10 - 1))
            .transform(Presets.sprite());
    public static final RegistryObject<ItemRepairKit> gun_kit_2 = reg("gun_kit_2", GeoNtmTab.CONSUMABLES,
            p -> new ItemRepairKit(p, () -> ModSounds.REPAIR.get()), p -> p.durability(100 - 1))
            .transform(Presets.sprite());

    // Records

    /** Song path -> (sound, length in seconds). Lengths are the Ogg streams' own. */
    public static final Map<String, RegistryObject<Item>> RECORDS = new LinkedHashMap<>();

    private static RegistryObject<Item> record(String id, String song, Supplier<SoundEvent> sound, int seconds,
                                               GeoNtmTab tab) {
        // 1.7 jukeboxes output (item id + 1 - record_13's id) on a comparator, which for a mod item is far
        // above 15 and so always saturates.
        RegistryObject<Item> ro = reg(id, tab,
                p -> Records.disc(p, Constants.MOD_ID, song, sound, 15, seconds), p -> p.rarity(Rarity.RARE))
                .transform(Presets.sprite());
        RECORDS.put(song, ro);
        return ro;
    }

    public static final RegistryObject<Item> record_lc = record("record_lc", "lc", () -> ModSounds.RECORD_LAMBDA_CORE.get(), 105, GeoNtmTab.CONSUMABLES);
    public static final RegistryObject<Item> record_ss = record("record_ss", "ss", () -> ModSounds.RECORD_SECTOR_SWEEP.get(), 167, GeoNtmTab.CONSUMABLES);
    public static final RegistryObject<Item> record_vc = record("record_vc", "vc", () -> ModSounds.RECORD_VORTAL_COMBAT.get(), 195, GeoNtmTab.CONSUMABLES);
    public static final RegistryObject<Item> record_glass = record("record_glass", "glass", () -> ModSounds.MUSIC_TRANSMISSION.get(), 63, null);

    // Batteries

    public static final RegistryObject<ItemBattery> battery_potato = reg("battery_potato", GeoNtmTab.CONTROL,
            p -> new ItemBattery(p, 1000, 0, 100), p -> p.stacksTo(1))
            .transform(Presets.sprite());
    public static final RegistryObject<ItemPotatos> battery_potatos = reg("battery_potatos", GeoNtmTab.CONTROL,
            p -> new ItemPotatos(p, 500000, 0, 100), p -> p.stacksTo(1))
            .transform(Presets.sprite());
    public static final RegistryObject<ItemBattery> cube_power = reg("cube_power", GeoNtmTab.CONTROL,
            p -> new ItemBattery(p, 1000000000000000000L, 1000000000000000L, 1000000000000000L), p -> p.stacksTo(1))
            .transform(Presets.sprite());
    public static final RegistryObject<ItemFusionCore> fusion_core = reg("fusion_core", GeoNtmTab.CONTROL,
            p -> new ItemFusionCore(p, 2500000), p -> p.stacksTo(1))
            .transform(Presets.sprite());
    /** 1.7 {@code ModItems.hev_battery}: {@code new ItemFusionCore(150000)}, stack size 4, control tab. */
    public static final RegistryObject<ItemFusionCore> hev_battery = reg("hev_battery", GeoNtmTab.CONTROL,
            p -> new ItemFusionCore(p, 150000), p -> p.stacksTo(4))
            .transform(Presets.sprite());

    // Loose materials

    public static final RegistryObject<ItemRag> rag = reg("rag", GeoNtmTab.MATERIALS, ItemRag::new, p -> p)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemFertilizer> powder_fertilizer = reg("powder_fertilizer", GeoNtmTab.MATERIALS,
            ItemFertilizer::new, p -> p)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemCustomLore> ingot_electronium = reg("ingot_electronium", GeoNtmTab.MATERIALS,
            ItemCustomLore::new, p -> p)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemFELCrystal> laser_crystal_dnt = reg("laser_crystal_dnt", GeoNtmTab.CONTROL,
            p -> new ItemFELCrystal(p, ItemFELCrystal.EnumWavelengths.GAMMA, false), p -> p)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemCraftingDegradation> chemistry_set = reg("chemistry_set", GeoNtmTab.CONTROL,
            ItemCraftingDegradation::new, p -> p.durability(100))
            .transform(Presets.sprite());
    public static final RegistryObject<ItemCraftingDegradation> chemistry_set_boron = reg("chemistry_set_boron",
            GeoNtmTab.CONTROL, ItemCraftingDegradation::new, p -> p.stacksTo(1))
            .transform(Presets.sprite());

    public static final RegistryObject<Item> parts_legendary_tier1 = plain("parts_legendary_tier1", GeoNtmTab.MATERIALS, p -> p)
            .transform(Presets.sprite("geontm:items/parts_legendary.tier1"));
    public static final RegistryObject<Item> parts_legendary_tier2 = plain("parts_legendary_tier2", GeoNtmTab.MATERIALS, p -> p)
            .transform(Presets.sprite("geontm:items/parts_legendary.tier2"));
    public static final RegistryObject<Item> parts_legendary_tier3 = plain("parts_legendary_tier3", GeoNtmTab.MATERIALS, p -> p)
            .transform(Presets.sprite("geontm:items/parts_legendary.tier3"));

    public static final RegistryObject<Item> briquette_coal = plain("briquette_coal", GeoNtmTab.MATERIALS, p -> p)
            .transform(Presets.sprite("geontm:items/briquette.coal"));
    public static final RegistryObject<Item> briquette_lignite = plain("briquette_lignite", GeoNtmTab.MATERIALS, p -> p)
            .transform(Presets.sprite("geontm:items/briquette.lignite"));
    public static final RegistryObject<Item> briquette_wood = plain("briquette_wood", GeoNtmTab.MATERIALS, p -> p)
            .transform(Presets.sprite("geontm:items/briquette.wood"));

    static {
        // 1.7 FuelHandler: single = 200 ticks; coal x10, lignite x8, sawdust x2 (FuelDataMapDeclarations.BURN_TIMES)
    }

    /** 1.7 {@code EnumSecretType}; hidden from the tabs as in 1.7 ({@code NEIConfig} hid them too). */
    public static final String[] SECRETS = {"canister", "controller", "selenium_steel", "aberrator", "folly"};
    /** 1.7 {@code ItemPlasticScrap.ScrapType}, in enum order. */
    public static final String[] SCRAPS = {"board_blank", "board_transistor", "board_converter", "bridge_north",
            "bridge_south", "bridge_io", "bridge_bus", "bridge_chipset", "bridge_cmos", "bridge_bios", "cpu_register",
            "cpu_clock", "cpu_logic", "cpu_cache", "cpu_ext", "cpu_socket", "mem_socket", "mem_16k_a", "mem_16k_b",
            "mem_16k_c", "mem_16k_d", "card_board", "card_processor"};
    /** 1.7 {@code ItemCircuitStarComponent.CircuitComponentType}. */
    public static final String[] STAR_COMPONENTS = {"chipset", "cpu", "ram", "card"};

    public static final Map<String, RegistryObject<? extends Item>> FLAT = new LinkedHashMap<>();

    static {
        // the 1.7 metadata sprites keep their dotted names: <base>.<subtype>
        for (String s : SECRETS) FLAT.put("item_secret_" + s, plain("item_secret_" + s, null, p -> p)
                .transform(Presets.sprite("geontm:items/item_secret." + s)));
        for (String s : SCRAPS) FLAT.put("circuit_star_piece_" + s, plain("circuit_star_piece_" + s, null, p -> p)
                .transform(Presets.sprite("geontm:items/circuit_star_piece." + s)));
        // 1.7 ItemPlasticScrap: ScrapType metadata, one sprite, one name ("Plastic Scraps"), no creative tab
        for (String s : SCRAPS) FLAT.put("scrap_plastic_" + s, plain("scrap_plastic_" + s, null, p -> p)
                .transform(Presets.sprite("geontm:items/scrap_plastic")));
        for (String s : STAR_COMPONENTS) {
            FLAT.put("circuit_star_component_" + s, plain("circuit_star_component_" + s, null, p -> p)
                    .transform(Presets.sprite("geontm:items/circuit_star_component." + s)));
        }
        // page_of_ is single-texture (multiTexture false); ItemStampBook draws every plate with stamp_book
        for (int i = 1; i <= 8; i++) FLAT.put("page_of_page" + i, plain("page_of_page" + i, null, p -> p.stacksTo(1))
                .transform(Presets.sprite("geontm:items/page_of_")));
        for (int i = 1; i <= 8; i++) {
            StampType type = StampType.valueOf("PRINTING" + i);
            String id = "stamp_book_printing" + i;
            // 1.7 ItemStampBook is an ItemStamp(0, null): unbreakable
            FLAT.put(id, reg(id, null, p -> new ItemStamp(p, type), p -> p.stacksTo(1))
                    .transform(Presets.sprite("geontm:items/stamp_book")));
        }
    }

    public static Item flat(String id) {
        return FLAT.get(id).get();
    }

    // Upgrades

    public static final RegistryObject<ItemMachineUpgrade> upgrade_5g = reg("upgrade_5g", GeoNtmTab.CONTROL,
            p -> new ItemMachineUpgrade(ItemMachineUpgrade.UpgradeType.SPECIAL, 0, p), p -> p.stacksTo(1))
            .transform(Presets.sprite());
    public static final RegistryObject<ItemMuffler> upgrade_muffler = reg("upgrade_muffler", GeoNtmTab.MATERIALS,
            ItemMuffler::new, p -> p)
            .transform(Presets.sprite());

    public static void init() {}
}
