package fr.geoventure.geontm.items;

import fr.geoventure.geocore.registry.ItemTag;
import fr.geoventure.geocore.registry.Preset;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.api.block.IToolable.ToolType;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geontm.inventory.ModMeleeKitMenus;
import fr.geoventure.geontm.items.special.ItemBook;
import fr.geoventure.geontm.items.special.ItemKitCustom;
import fr.geoventure.geontm.items.special.ItemLootCrate;
import fr.geoventure.geontm.items.special.ItemStarterKit;
import fr.geoventure.geontm.items.special.ItemStarterKit.Kind;
import fr.geoventure.geontm.items.tool.BigSword;
import fr.geoventure.geontm.items.tool.ItemAcetyleneTorch;
import fr.geoventure.geontm.items.tool.ItemCatalog;
import fr.geoventure.geontm.items.tool.ItemDiscord;
import fr.geoventure.geontm.items.tool.ItemMS;
import fr.geoventure.geontm.items.tool.ItemToolBox;
import fr.geoventure.geontm.items.tool.ItemTooling;
import fr.geoventure.geontm.items.tool.ItemToolingWeapon;
import fr.geoventure.geontm.items.tool.ItemWrench;
import fr.geoventure.geontm.items.tool.ModSword;
import fr.geoventure.geontm.items.tool.WeaponSpecial;
import fr.geoventure.geontm.items.weapon.ItemGrenadeFishing;
import fr.geoventure.geontm.items.weapon.WeaponizedCell;
import fr.geoventure.geontm.packet.ItemBobmazonPayload;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.data.GeoCoreComponent;
import fr.geoventure.geocore.data.ItemContents;
import fr.geoventure.geocore.dejson.Models;
import fr.geoventure.geocore.dejson.Tints;
import fr.geoventure.geocore.net.GeoCoreNetwork;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import fr.geoventure.geocore.world.ToolTier;
import java.util.function.UnaryOperator;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * The special melee weapons, novelty tools, starter kits and loot items of 1.7 {@code ModItems}: the
 * {@code WeaponSpecial} family, the Great Sword, the Reer Graar and the manual override, the pipe and Archineer's
 * wrenches, the desh screwdriver and hand drill, the acetylene torch, the rigged star blaster cell, the Rod of
 * Discord, the Brittle Spade, fishing dynamite, every {@code ItemStarterKit}, the custom kit, the toolbox, the
 * missile loot crates, the two Bobmazon catalogues and the Book of Boxcars.
 *
 * <p>1.7 put the plain {@code ItemSword}s in vanilla's combat tab; like the ability swords they sit on the
 * consumables tab here.
 */
public final class ModMeleeKitItems {

    private ModMeleeKitItems() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.createSounds(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ITEMS, SOUNDS);

    // 1.7 NTMSounds BONK/STOP/BANG/SLICE/KAPENG; GAVEL is weapon.whack (ModGunSounds.WHACK)
    public static final RegistryObject<SoundEvent> BONK = sound("weapon.bonk");
    public static final RegistryObject<SoundEvent> STOP = sound("weapon.stop");
    public static final RegistryObject<SoundEvent> BANG = sound("weapon.bang");
    public static final RegistryObject<SoundEvent> SLICE = sound("weapon.slice");
    public static final RegistryObject<SoundEvent> KAPENG = sound("weapon.kapeng");

    private static RegistryObject<SoundEvent> sound(String id) {
        return REG.sound(id);
    }

    /** The give-list carried by {@code kit_custom} (1.7 {@code ItemStackUtil.addStacksToNBT}). */
    public static final GeoCoreComponent<ItemContents> KIT_CONTENTS =
            GeoCoreComponent.of(Constants.MOD_ID, "kit_contents", () -> ItemContents.CODEC);
    /** The toolbox's 24 slots. */
    public static final GeoCoreComponent<ItemContents> TOOLBOX =
            GeoCoreComponent.of(Constants.MOD_ID, "toolbox", () -> ItemContents.CODEC);

    private static TagKey<Item> repairs(String name) {
        return TagKey.create(Registries.ITEM, Ids.of(Constants.MOD_ID, "repairs/" + name));
    }

    // --- 1.7 tool materials: harvestLevel, maxUses, efficiency, damage, enchantability ---
    // Repair tags are named on first use (ToolTier.of): a TagKey names the item registry, which 1.20.1 cannot do in
    // the build-time Dejson JVM that initialises this class for the declarations chained below.
    /** Vanilla {@code EMERALD} (diamond). */
    public static final ToolTier EMERALD = ToolTier.of(3, 1561, 8.0F, 3.0F, 10, () -> repairs("diamond"));
    /** Vanilla {@code WOOD}. */
    public static final ToolTier WOOD = ToolTier.of(0, 59, 2.0F, 0.0F, 15, () -> ItemTags.PLANKS);
    /** SCHRABIDIUMHAMMER, repaired with a schrabidium block. */
    public static final ToolTier tMatHammmer = ToolTier.of(3, 0, 50.0F, 999999996F, 200, () -> repairs("block_schrabidium"));
    /** SHIMMERSLEDGE, no repair item. */
    public static final ToolTier enumToolMaterialSledge = new ToolTier(1, 0, 25.0F, 26F, 200, null);
    /** PIPELEAD, no repair item. */
    public static final ToolTier enumToolMaterialPipeLead = new ToolTier(1, 250, 1.5F, 3F, 25, null);

    /** Every id this family registers, in registration order, for the harness and the declarations. */
    public static final List<String> IDS = new ArrayList<>();

    private static <I extends Item> RegistryObject<I> reg(String id, GeoNtmTab tab, Function<Item.Properties, I> factory,
                                                         UnaryOperator<Item.Properties> properties,
                                                         Preset model) {
        RegistryObject<I> ro = REG.item(id, factory, properties).transform(model);
        IDS.add(id);
        if (tab != null) ModTabs.addTo(tab, ro);
        return ro;
    }

    private static RegistryObject<WeaponSpecial> special(String id, ToolTier tier, WeaponSpecial.Kind kind, GeoNtmTab tab,
                                                         Rarity rarity, Preset model) {
        return reg(id, tab, p -> new WeaponSpecial(p, tier, kind),
                p -> rarity == Rarity.COMMON ? p.stacksTo(1) : p.stacksTo(1).rarity(rarity), model);
    }

    // --- item models: 1.7 texture names; everything 1.7 drew setFull3D is handheld ---

    /** A sprite in the inventory, the 1.7 IItemRenderer everywhere else ({@link ItemRendererDeclarations#heldModel}), in
     * the full-3D hand pose: every item here is an ItemSword (1.7 isFull3D). */
    private static Preset held(String sprite, String renderer) {
        return handle -> ItemRendererDeclarations.heldModel(handle.path(), sprite, renderer, "weapon_full3d");
    }

    /** A member of {@code geontm:melee_kit_swords}, which {@code minecraft:swords} nests. */
    private static Preset sword(Preset model) {
        return model.and(handle -> handle.tag(ItemTag.of("geontm:melee_kit_swords")));
    }

    /** 1.7 ItemKitCustom: the kit, then kit_1 and kit_2 tinted by the stack's color1 and color2 (black if unset). */
    private static final Preset KIT_CUSTOM_MODEL = Presets.sprite("geontm:items/kit", "geontm:items/kit_1", "geontm:items/kit_2")
            .tints(Tints.constant(-1), Tints.customModelData(0, 0), Tints.customModelData(1, 0));

    /** 1.7 ItemToolBox: pass 1 is kit_toolbox, or its empty lid kit_toolbox_empty while open (geontm:model_state 1). */
    private static final Preset TOOLBOX_MODEL = handle -> handle.icon(d -> d.models(Models.group()
                    .and("", Models.generated("geontm:items/kit_toolbox"))
                    .and("_open", Models.generated("geontm:items/kit_toolbox", "geontm:items/kit_toolbox_empty")))
            .selects(Constants.MOD_ID + ":model_state"));

    private static final GeoNtmTab C = GeoNtmTab.CONSUMABLES;

    // --- melee ---
    /** 1.7 {@code BigSword}: a combat-tab weapon; the consumables tab never listed it. */
    public static final RegistryObject<BigSword> big_sword = reg("big_sword", GeoNtmTab.WEAPONS,
            p -> new BigSword(p, EMERALD), p -> p.stacksTo(1), sword(held("big_sword", "big_sword")));
    public static final RegistryObject<WeaponSpecial> schrabidium_hammer = special("schrabidium_hammer", tMatHammmer,
            WeaponSpecial.Kind.SCHRABIDIUM_HAMMER, C, Rarity.RARE, sword(Presets.handheld()));
    public static final RegistryObject<WeaponSpecial> shimmer_sledge = special("shimmer_sledge", enumToolMaterialSledge,
            WeaponSpecial.Kind.SHIMMER_SLEDGE, C, Rarity.EPIC, sword(held("shimmer_sledge_original", "shim")));
    public static final RegistryObject<WeaponSpecial> shimmer_axe = special("shimmer_axe", enumToolMaterialSledge,
            WeaponSpecial.Kind.SHIMMER_AXE, C, Rarity.EPIC, sword(held("shimmer_axe", "shim")));
    public static final RegistryObject<WeaponSpecial> wrench_flipped = special("wrench_flipped", ModAbilityToolItems.tMatElec,
            WeaponSpecial.Kind.WRENCH_FLIPPED, C, Rarity.COMMON, sword(Presets.handheld()));
    public static final RegistryObject<WeaponSpecial> memespoon = special("memespoon", ModAbilityToolItems.tMatSteel,
            WeaponSpecial.Kind.MEMESPOON, C, Rarity.COMMON, sword(Presets.handheld()));
    public static final RegistryObject<WeaponSpecial> wood_gavel = special("wood_gavel", WOOD,
            WeaponSpecial.Kind.WOOD_GAVEL, C, Rarity.COMMON, sword(held("wood_gavel", "gavel")));
    public static final RegistryObject<WeaponSpecial> lead_gavel = special("lead_gavel", ModAbilityToolItems.tMatSteel,
            WeaponSpecial.Kind.LEAD_GAVEL, C, Rarity.COMMON, sword(held("lead_gavel", "gavel")));
    public static final RegistryObject<WeaponSpecial> diamond_gavel = special("diamond_gavel", EMERALD,
            WeaponSpecial.Kind.DIAMOND_GAVEL, C, Rarity.COMMON, sword(held("diamond_gavel", "gavel")));
    public static final RegistryObject<ModSword> weapon_pipe_lead = reg("weapon_pipe_lead", C,
            p -> new ModSword(p, enumToolMaterialPipeLead, "I'm going to attempt a manual override on this wall."),
            p -> p.stacksTo(1), sword(Presets.handheld()));
    public static final RegistryObject<ModSword> reer_graar = reg("reer_graar", C,
            p -> new ModSword(p, ModAbilityToolItems.tMatTitan, "Call now!", "555-10-3728-ZX7-INFINITE"),
            p -> p.stacksTo(1), sword(Presets.handheld("geontm:items/reer_graar_hd")));
    public static final RegistryObject<WeaponSpecial> stopsign = special("stopsign", ModAbilityToolItems.tMatAlloy,
            WeaponSpecial.Kind.STOPSIGN, C, Rarity.COMMON, sword(held("stopsign", "shim")));
    public static final RegistryObject<WeaponSpecial> sopsign = special("sopsign", ModAbilityToolItems.tMatAlloy,
            WeaponSpecial.Kind.SOPSIGN, C, Rarity.COMMON, sword(held("sopsign", "shim")));
    public static final RegistryObject<WeaponSpecial> chernobylsign = special("chernobylsign", ModAbilityToolItems.tMatAlloy,
            WeaponSpecial.Kind.CHERNOBYLSIGN, C, Rarity.COMMON, sword(held("chernobylsign", "shim")));
    public static final RegistryObject<WeaponSpecial> ullapool_caber = special("ullapool_caber", ModAbilityToolItems.tMatSteel,
            WeaponSpecial.Kind.ULLAPOOL_CABER, GeoNtmTab.WEAPONS, Rarity.UNCOMMON, sword(Presets.handheld()));

    // --- tools ---
    public static final RegistryObject<ItemWrench> wrench = reg("wrench", GeoNtmTab.MATERIALS,
            p -> new ItemWrench(p, ModAbilityToolItems.tMatSteel), p -> p.stacksTo(1), sword(Presets.handheld()));
    public static final RegistryObject<ItemToolingWeapon> wrench_archineer = reg("wrench_archineer", GeoNtmTab.CONTROL,
            p -> new ItemToolingWeapon(p, ToolType.WRENCH, 12F), p -> p.stacksTo(1).durability(1000), Presets.handheld("geontm:items/wrench_archineer_hd"));
    public static final RegistryObject<ItemTooling> screwdriver_desh = reg("screwdriver_desh", GeoNtmTab.CONTROL,
            p -> new ItemTooling(p, ToolType.SCREWDRIVER), p -> p.stacksTo(1), Presets.handheld());
    public static final RegistryObject<ItemTooling> hand_drill_desh = reg("hand_drill_desh", GeoNtmTab.CONTROL,
            p -> new ItemTooling(p, ToolType.HAND_DRILL), p -> p.stacksTo(1), Presets.handheld());
    public static final RegistryObject<ItemAcetyleneTorch> acetylene_torch = reg("acetylene_torch", GeoNtmTab.CONTROL,
            ItemAcetyleneTorch::new, p -> p.stacksTo(1), Presets.handheld());

    // --- novelty ---
    public static final RegistryObject<WeaponizedCell> weaponized_starblaster_cell = reg("weaponized_starblaster_cell",
            GeoNtmTab.WEAPONS, WeaponizedCell::new, p -> p.stacksTo(1), Presets.sprite("geontm:items/gun_b92_ammo_weaponized"));
    public static final RegistryObject<ItemDiscord> rod_of_discord = reg("rod_of_discord", C,
            ItemDiscord::new, p -> p.stacksTo(1), Presets.handheld());
    public static final RegistryObject<ItemMS> mysteryshovel = reg("mysteryshovel", null,
            ItemMS::new, p -> p.stacksTo(1), Presets.handheld("geontm:items/cursed_shovel"));
    public static final RegistryObject<ItemGrenadeFishing> stick_dynamite_fishing = reg("stick_dynamite_fishing",
            GeoNtmTab.WEAPONS, p -> new ItemGrenadeFishing(p, 3), p -> p.stacksTo(16), Presets.sprite()); // 1.7 ItemGrenade: stacks to 16
    /** 1.7 {@code ItemBook}: no creative tab. */
    public static final RegistryObject<ItemBook> book_of_ = reg("book_of_", null, ItemBook::new, p -> p.stacksTo(1), Presets.sprite());

    // --- kits ---
    public static final RegistryObject<ItemStarterKit> stealth_boy = kit("stealth_boy", Kind.STEALTH_BOY, C, false, Presets.sprite());
    public static final RegistryObject<ItemStarterKit> nuke_starter_kit = kit("nuke_starter_kit", Kind.NUKE_STARTER, C, true, Presets.sprite());
    public static final RegistryObject<ItemStarterKit> nuke_advanced_kit = kit("nuke_advanced_kit", Kind.NUKE_ADVANCED, C, true, Presets.sprite());
    public static final RegistryObject<ItemStarterKit> nuke_commercially_kit = kit("nuke_commercially_kit", Kind.NUKE_COMMERCIALLY, C, true, Presets.sprite());
    public static final RegistryObject<ItemStarterKit> nuke_electric_kit = kit("nuke_electric_kit", Kind.NUKE_ELECTRIC, C, true, Presets.sprite());
    public static final RegistryObject<ItemStarterKit> gadget_kit = kit("gadget_kit", Kind.GADGET, GeoNtmTab.BOMBS, true, Presets.sprite());
    public static final RegistryObject<ItemStarterKit> boy_kit = kit("boy_kit", Kind.BOY, GeoNtmTab.BOMBS, true, Presets.sprite());
    public static final RegistryObject<ItemStarterKit> man_kit = kit("man_kit", Kind.MAN, GeoNtmTab.BOMBS, true, Presets.sprite());
    public static final RegistryObject<ItemStarterKit> mike_kit = kit("mike_kit", Kind.MIKE, GeoNtmTab.BOMBS, true, Presets.sprite());
    public static final RegistryObject<ItemStarterKit> tsar_kit = kit("tsar_kit", Kind.TSAR, GeoNtmTab.BOMBS, true, Presets.sprite());
    public static final RegistryObject<ItemStarterKit> multi_kit = kit("multi_kit", Kind.MULTI, GeoNtmTab.BOMBS, true, Presets.sprite());
    public static final RegistryObject<ItemStarterKit> custom_kit = kit("custom_kit", Kind.CUSTOM, GeoNtmTab.BOMBS, true, Presets.sprite());
    public static final RegistryObject<ItemStarterKit> fleija_kit = kit("fleija_kit", Kind.FLEIJA, GeoNtmTab.BOMBS, true, Presets.sprite());
    public static final RegistryObject<ItemStarterKit> prototype_kit = kit("prototype_kit", Kind.PROTOTYPE, GeoNtmTab.BOMBS, true, Presets.sprite());
    public static final RegistryObject<ItemStarterKit> missile_kit = kit("missile_kit", Kind.MISSILE, GeoNtmTab.MISSILES, true, Presets.sprite());
    /** 1.7: no creative tab, the {@code code} icon. */
    public static final RegistryObject<ItemStarterKit> euphemium_kit = kit("euphemium_kit", Kind.EUPHEMIUM, null, true, Presets.sprite("geontm:items/code"));
    public static final RegistryObject<ItemStarterKit> solinium_kit = kit("solinium_kit", Kind.SOLINIUM, GeoNtmTab.BOMBS, true, Presets.sprite());
    public static final RegistryObject<ItemStarterKit> hazmat_kit = kit("hazmat_kit", Kind.HAZMAT, C, true, Presets.sprite());
    public static final RegistryObject<ItemStarterKit> hazmat_red_kit = kit("hazmat_red_kit", Kind.HAZMAT_RED, C, true, Presets.sprite());
    public static final RegistryObject<ItemStarterKit> hazmat_grey_kit = kit("hazmat_grey_kit", Kind.HAZMAT_GREY, C, true, Presets.sprite());
    /** 1.7 {@code ItemKitCustom}: no creative tab (its getSubItems is empty). */
    public static final RegistryObject<ItemKitCustom> kit_custom = reg("kit_custom", null, ItemKitCustom::new, p -> p.stacksTo(1), KIT_CUSTOM_MODEL);
    public static final RegistryObject<ItemToolBox> toolbox = reg("toolbox", C, ItemToolBox::new, p -> p.stacksTo(1), TOOLBOX_MODEL);

    private static RegistryObject<ItemStarterKit> kit(String id, Kind kind, GeoNtmTab tab, boolean single, Preset model) {
        return reg(id, tab, p -> new ItemStarterKit(p, kind), p -> single ? p.stacksTo(1) : p, model);
    }

    // --- loot ---
    public static final RegistryObject<ItemLootCrate> loot_10 = reg("loot_10", GeoNtmTab.MISSILES,
            p -> new ItemLootCrate(p, ItemLootCrate.Pool.SIZE_10), p -> p.stacksTo(1), Presets.sprite());
    public static final RegistryObject<ItemLootCrate> loot_15 = reg("loot_15", GeoNtmTab.MISSILES,
            p -> new ItemLootCrate(p, ItemLootCrate.Pool.SIZE_15), p -> p.stacksTo(1), Presets.sprite());
    public static final RegistryObject<ItemLootCrate> loot_misc = reg("loot_misc", GeoNtmTab.MISSILES,
            p -> new ItemLootCrate(p, ItemLootCrate.Pool.MISC), p -> p.stacksTo(1), Presets.sprite());
    public static final RegistryObject<ItemCatalog> bobmazon = reg("bobmazon", C,
            p -> new ItemCatalog(p, false), p -> p.stacksTo(1), Presets.sprite());
    /** 1.7: no creative tab. */
    public static final RegistryObject<ItemCatalog> bobmazon_hidden = reg("bobmazon_hidden", null,
            p -> new ItemCatalog(p, true), p -> p.stacksTo(1), Presets.sprite("geontm:items/bobmazon_special"));

    static {
        ToolType.WRENCH.register(wrench_archineer);
        ToolType.SCREWDRIVER.register(screwdriver_desh);
        ToolType.HAND_DRILL.register(hand_drill_desh);
        ToolType.TORCH.register(acetylene_torch);
    }

    public static void init() {
        // Force the component classes before the loader flushes the data-component registers.
        ItemAcetyleneTorch.UNSATURATEDS.getClass();
        ModMeleeKitMenus.init();
        GeoCoreNetwork.registerPlayToServer(ItemBobmazonPayload.CODEC, ItemBobmazonPayload::handleServer);
        MeleeKitDeclarations.declare(); // tags and recipes; the item models are on the registrations above
    }
}
