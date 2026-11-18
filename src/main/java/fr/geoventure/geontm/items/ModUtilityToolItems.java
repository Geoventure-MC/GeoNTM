package fr.geoventure.geontm.items;

import fr.geoventure.geocore.registry.Preset;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geontm.items.machine.ItemFluidIDMulti;
import fr.geoventure.geontm.items.special.ItemBookLore;
import fr.geoventure.geontm.items.special.ItemClayTablet;
import fr.geoventure.geontm.items.special.ItemGlitch;
import fr.geoventure.geontm.items.special.ItemHolotapeImage;
import fr.geoventure.geontm.items.special.ItemPolaroid;
import fr.geoventure.geontm.items.tool.ItemAnalysisTool;
import fr.geoventure.geontm.items.tool.ItemBookLemegeton;
import fr.geoventure.geontm.items.tool.ItemColtanCompass;
import fr.geoventure.geontm.items.tool.ItemDigammaDiagnostic;
import fr.geoventure.geontm.items.tool.ItemDyatlov;
import fr.geoventure.geontm.items.tool.ItemGuideBook;
import fr.geoventure.geontm.items.tool.ItemLeadBox;
import fr.geoventure.geontm.items.tool.ItemMeteorRemote;
import fr.geoventure.geontm.items.tool.ItemOreDensityScanner;
import fr.geoventure.geontm.items.tool.ItemPipette;
import fr.geoventure.geontm.items.tool.ItemPollutionDetector;
import fr.geoventure.geontm.items.tool.ItemPowerNetTool;
import fr.geoventure.geontm.items.tool.ItemRTTYDesignator;
import fr.geoventure.geontm.items.tool.ItemRTTYPager;
import fr.geoventure.geontm.items.tool.ItemSettingsTool;
import fr.geoventure.geontm.items.tool.ItemSurveyScanner;
import fr.geoventure.geocore.data.GeoCoreComponent;
import fr.geoventure.geocore.data.ItemContents;
import fr.geoventure.geocore.dejson.Models;
import fr.geoventure.geocore.dejson.Tints;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;

/**
 * The handheld utility and diagnostic tools and the books, from 1.7 {@code ModItems}: the analysis, power-net,
 * settings and meltdown tools, the Coltass, containment box, digamma diagnostic, multi fluid identifier,
 * meteorite remote, ore density / survey scanners, pollution detector, RoR pager, the Polaroid, the Glitch, the three
 * pipettes, the guide books, lore books, holotapes, clay tablets and the Lemegeton. Tabs follow 1.7
 * ({@code consumableTab}, {@code controlTab}, {@code templateTab}); items 1.7 hid stay hidden. Metadata items
 * (guide book, holotape, clay tablet) are one item per variant.
 */
public final class ModUtilityToolItems {

    private ModUtilityToolItems() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ITEMS);

    /** Every id this class registers, in order: the harness and GameTests walk it. */
    public static final List<String> IDS = new ArrayList<>();

    // Stack data, 1.7's NBT keys under one component each
    /** Coltass deposit: {@code colX}, {@code colZ}. */
    public static final GeoCoreComponent<CompoundTag> COLTASS = GeoCoreComponent.compound(Constants.MOD_ID, "coltass");
    /** The containment box's 20 slots. */
    public static final GeoCoreComponent<ItemContents> CONTAINMENT =
            GeoCoreComponent.of(Constants.MOD_ID, "containment_box", () -> ItemContents.CODEC);
    /** Pipette: {@code type}, {@code fill}, {@code capacity}. */
    public static final GeoCoreComponent<CompoundTag> PIPETTE = GeoCoreComponent.compound(Constants.MOD_ID, "pipette");
    /** Multi fluid identifier: {@code fluid1}, {@code fluid2}. */
    public static final GeoCoreComponent<CompoundTag> FLUID_MULTI = GeoCoreComponent.compound(Constants.MOD_ID, "fluid_multi");
    /** Settings tool: the copied settings plus {@code tileName}, {@code copyIndex}, {@code inputDelay}, {@code displayInfo}. */
    public static final GeoCoreComponent<CompoundTag> SETTINGS = GeoCoreComponent.compound(Constants.MOD_ID, "settings_tool");
    /** RoR pager channel ({@code chan}). */
    public static final GeoCoreComponent<String> PAGER_CHANNEL = GeoCoreComponent.string(Constants.MOD_ID, "pager_channel");
    /** Lore book: {@code k}, {@code p}, {@code cov_col}, {@code tit_col}, {@code p<n>} arguments. */
    public static final GeoCoreComponent<CompoundTag> BOOK_LORE = GeoCoreComponent.compound(Constants.MOD_ID, "book_lore");
    /** Clay tablet {@code tabletSeed}. */
    public static final GeoCoreComponent<Long> TABLET_SEED = GeoCoreComponent.longValue(Constants.MOD_ID, "tablet_seed");

    /**
     * One registration with its item model, both as the old {@code UtilityToolDeclarations} stated them: 1.7 texture
     * names; tinted layers and variant sprites as 1.7's {@code getIcon}/{@code getColorFromItemStack} chose them.
     */
    private static <I extends Item> RegistryObject<I> reg(String id, GeoNtmTab tab, Function<Item.Properties, I> factory,
                                                          UnaryOperator<Item.Properties> props, Preset model) {
        Reg.Handle<I> ro = REG.item(id, factory, props).transform(model);
        if (tab != null) ro.tab(tab);
        IDS.add(id);
        return ro;
    }

    private static UnaryOperator<Item.Properties> single() {
        return p -> p.stacksTo(1);
    }

    /** 1.7 ItemPipette.getIcon: pass 1 is pipette_empty at fill 0, else the (laboratory) overlay, tinted. */
    private static Preset pipette(String overlay) {
        return handle -> handle.icon(d -> d
                .models(Models.group()
                        .and("", Models.generated("geontm:items/" + handle.path(), "geontm:items/pipette_empty"))
                        .and("_filled", Models.generated("geontm:items/" + handle.path(), "geontm:items/" + overlay)))
                .selects(Constants.MOD_ID + ":model_state")
                .tints(Tints.constant(-1), Tints.customModelData(0, 0xFFFFFF)));
    }

    /** 1.7 TextureColtass: 32 compass frames, the frame chosen by the client's needle. */
    private static Preset coltassFrames() {
        return handle -> handle.icon(d -> {
            Models.Group frames = Models.group();
            for (int i = 0; i < 32; i++) {
                // the first case is the item's own model
                frames = frames.and(i == 0 ? "" : String.format("_%02d", i),
                        Models.generated(String.format("geontm:items/coltass_%02d", i)));
            }
            return d.models(frames).selects(Constants.MOD_ID + ":coltass");
        });
    }

    /** 1.7 setTextureName(texture + "_" + polaroidID): case n is polaroid id n + 1. */
    private static Preset polaroids(String texture) {
        return handle -> handle.icon(d -> {
            Models.Group polaroids = Models.group();
            for (int i = 1; i <= 18; i++) {
                polaroids = polaroids.and(i == 1 ? "" : "_" + i, Models.generated("geontm:items/" + texture + "_" + i));
            }
            return d.models(polaroids).selects(Constants.MOD_ID + ":polaroid");
        });
    }

    public static final RegistryObject<ItemLeadBox> containment_box = reg("containment_box", GeoNtmTab.CONSUMABLES,
            ItemLeadBox::new, single(), Presets.sprite());
    public static final RegistryObject<ItemDyatlov> meltdown_tool = reg("meltdown_tool", GeoNtmTab.CONTROL,
            ItemDyatlov::new, single(), Presets.sprite());
    /** 1.7 {@code setMaxDamage(2)}: broken when damage passes 2, i.e. three uses; vanilla breaks at max, hence 3. */
    public static final RegistryObject<ItemMeteorRemote> meteor_remote = reg("meteor_remote", GeoNtmTab.CONSUMABLES,
            ItemMeteorRemote::new, p -> p.stacksTo(1).durability(3), Presets.handheld()); // setFull3D
    public static final RegistryObject<ItemDigammaDiagnostic> digamma_diagnostic = reg("digamma_diagnostic",
            GeoNtmTab.CONSUMABLES, ItemDigammaDiagnostic::new, single(), Presets.sprite());
    public static final RegistryObject<ItemPollutionDetector> pollution_detector = reg("pollution_detector",
            GeoNtmTab.CONSUMABLES, ItemPollutionDetector::new, single(), Presets.sprite());
    public static final RegistryObject<ItemOreDensityScanner> ore_density_scanner = reg("ore_density_scanner",
            GeoNtmTab.CONSUMABLES, ItemOreDensityScanner::new, single(), Presets.sprite());
    public static final RegistryObject<ItemSurveyScanner> survey_scanner = reg("survey_scanner", GeoNtmTab.CONSUMABLES,
            ItemSurveyScanner::new, single(), Presets.sprite());
    public static final RegistryObject<ItemColtanCompass> coltan_tool = reg("coltan_tool", GeoNtmTab.CONSUMABLES,
            ItemColtanCompass::new, single(), coltassFrames());
    public static final RegistryObject<ItemPowerNetTool> power_net_tool = reg("power_net_tool", GeoNtmTab.CONSUMABLES,
            ItemPowerNetTool::new, single(), Presets.sprite());
    public static final RegistryObject<ItemAnalysisTool> analysis_tool = reg("analysis_tool", GeoNtmTab.CONSUMABLES,
            ItemAnalysisTool::new, single(), Presets.sprite());
    public static final RegistryObject<ItemSettingsTool> settings_tool = reg("settings_tool", GeoNtmTab.CONSUMABLES,
            ItemSettingsTool::new, single(), Presets.sprite());
    public static final RegistryObject<ItemRTTYPager> rtty_pager = reg("rtty_pager", GeoNtmTab.CONSUMABLES,
            ItemRTTYPager::new, single(), Presets.sprite());
    /** Shares the pager's channel component; 1.7 used the same {@code chan} tag. */
    public static final RegistryObject<ItemRTTYDesignator> rtty_designator = reg("rtty_designator", GeoNtmTab.CONSUMABLES,
            ItemRTTYDesignator::new, single(), Presets.sprite());
    /** 1.7 templateTab, one identifier per fluid ({@link ItemFluidIDMulti#creativeStacks}); pass 1 tinted with the primary fluid. */
    public static final RegistryObject<ItemFluidIDMulti> fluid_identifier_multi = reg("fluid_identifier_multi", null,
            ItemFluidIDMulti::new, single(),
            Presets.sprite("geontm:items/fluid_identifier_multi", "geontm:items/fluid_identifier_overlay")
                    .tints(Tints.constant(-1), Tints.customModelData(0, 0xFFFFFF)));
    public static final RegistryObject<ItemPipette> pipette = reg("pipette", GeoNtmTab.CONTROL, ItemPipette::new, single(),
            pipette("pipette_overlay"));
    public static final RegistryObject<ItemPipette> pipette_boron = reg("pipette_boron", GeoNtmTab.CONTROL,
            ItemPipette::new, single(), pipette("pipette_overlay"));
    public static final RegistryObject<ItemPipette> pipette_laboratory = reg("pipette_laboratory", GeoNtmTab.CONTROL,
            ItemPipette::new, single(), pipette("pipette_laboratory_overlay"));

    /**
     * 1.7 book_guide metadata, one item per {@link ItemGuideBook.BookType} as ntm-next registers them
     * ({@code book_guide_<type>}, all drawn with the 1.7 sprite). 1.7 consumableTab with
     * the RBMK and starter books ({@link ItemGuideBook#creativeStacks}).
     */
    public static final List<RegistryObject<ItemGuideBook>> book_guide = guideBooks();

    /** By name, in {@link ItemGuideBook.BookType} order: the enum builds its pages and needs a running game. */
    private static List<RegistryObject<ItemGuideBook>> guideBooks() {
        List<RegistryObject<ItemGuideBook>> books = new ArrayList<>();
        String[] names = {"test", "rbmk", "hadron", "starter"};
        for (int i = 0; i < names.length; i++) {
            int ordinal = i;
            books.add(reg("book_guide_" + names[i], null,
                    p -> new ItemGuideBook(p, ItemGuideBook.BookType.values()[ordinal]), single(),
                    Presets.sprite("geontm:items/book_guide")));
        }
        return List.copyOf(books);
    }
    /** 1.7 ItemBookLore: pages, the tinted cover, the tinted title. */
    public static final RegistryObject<ItemBookLore> book_lore = reg("book_lore", null, ItemBookLore::new, single(),
            Presets.sprite("geontm:items/book_pages", "geontm:items/book_cover", "geontm:items/book_title")
                    .tints(Tints.constant(-1), Tints.customModelData(0, 0x303030), Tints.customModelData(1, 0xFFFFFF)));
    /** 1.7 holotape_image metadata, one item per image as ntm-next registers them ({@code holotape_image_<image>}). */
    public static final ItemFamily<ItemHolotapeImage.EnumHoloImage, ItemHolotapeImage> holotape_image = ItemFamily.of(
            ItemHolotapeImage.EnumHoloImage.class,
            type -> reg("holotape_image_" + type.name().toLowerCase(Locale.ROOT), null,
                    p -> new ItemHolotapeImage(p, type), single(), Presets.sprite("geontm:items/holotape")));
    /** 1.7 clay_tablet metadata 0 (the red-room tablet) and 1 (the dungeon one), one item each as in ntm-next. */
    public static final RegistryObject<ItemClayTablet> clay_tablet = reg("clay_tablet", null,
            p -> new ItemClayTablet(p, 0), single(), Presets.sprite());
    public static final RegistryObject<ItemClayTablet> clay_tablet_1 = reg("clay_tablet_1", null,
            p -> new ItemClayTablet(p, 1), single(), Presets.sprite("geontm:items/clay_tablet"));
    public static final RegistryObject<ItemPolaroid> polaroid = reg("polaroid", GeoNtmTab.CONSUMABLES, ItemPolaroid::new,
            single(), polaroids("polaroid"));
    /** 1.7 {@code setMaxDamage(1)}; one use breaks it. */
    public static final RegistryObject<ItemGlitch> glitch = reg("glitch", GeoNtmTab.CONSUMABLES, ItemGlitch::new,
            p -> p.stacksTo(1).durability(1), polaroids("glitch"));
    public static final RegistryObject<ItemBookLemegeton> book_lemegeton = reg("book_lemegeton", null,
            ItemBookLemegeton::new, single(), Presets.sprite());

    static {
        ModTabs.addStacksTo(GeoNtmTab.CONSUMABLES, ItemGuideBook::creativeStacks);
    }

    public static void init() {}
}
