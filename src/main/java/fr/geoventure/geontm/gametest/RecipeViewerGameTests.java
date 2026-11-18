package fr.geoventure.geontm.gametest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.handler.NTMToolHandler;
import fr.geoventure.geontm.handler.jei.BookRecipeHandler;
import fr.geoventure.geontm.handler.jei.ConstructionRecipeHandler;
import fr.geoventure.geontm.handler.jei.CrucibleCastingHandler;
import fr.geoventure.geontm.handler.jei.CrucibleSmeltingHandler;
import fr.geoventure.geontm.handler.jei.ElectrolyserFluidRecipeHandler;
import fr.geoventure.geontm.handler.jei.ElectrolyserMetalRecipeHandler;
import fr.geoventure.geontm.handler.jei.FluidContainerRecipeHandler;
import fr.geoventure.geontm.handler.jei.GeoNtmJeiRecipeTypes;
import fr.geoventure.geontm.handler.jei.GeoNtmRecipeViewer;
import fr.geoventure.geontm.handler.jei.SILEXRecipeHandler;
import fr.geoventure.geontm.handler.jei.ToolingRecipeHandler;
import fr.geoventure.geontm.handler.jei.UniversalRecipeHandler;
import fr.geoventure.geontm.blocks.machine.ModMachineBlocks;
import fr.geoventure.geontm.config.GeneralConfig;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.inventory.recipes.AnnihilatorRecipes;
import fr.geoventure.geontm.inventory.recipes.OutgasserRecipes;
import fr.geoventure.geontm.inventory.recipes.StorageDrumRecipes;
import fr.geoventure.geontm.items.ModDataComponents;
import fr.geoventure.geontm.items.machine.ItemFluidIcon;
import fr.geoventure.geontm.items.special.BedrockOreType;
import net.minecraft.world.item.ItemStack;
import fr.geoventure.geocore.gametest.GameTests;
import fr.geoventure.geocore.viewer.ViewerCategory;
import fr.geoventure.geocore.viewer.ViewerType;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.Items;

/**
 * The recipe-viewer categories as both viewers receive them from {@link GeoNtmRecipeViewer}: every translated title has an
 * English line, and the tabs 1.7's NEI had are declared with rows from the real recipe sources.
 */
public final class RecipeViewerGameTests {

    private RecipeViewerGameTests() {}

    public static final GameTests.Suite TESTS = GameTests.suite(Constants.MOD_ID);
    public static final GameTests.Function TITLES =
            TESTS.function("viewer_category_titles_translated", 20, () -> RecipeViewerGameTests::titlesTranslated);
    public static final GameTests.Function NEI_TABS =
            TESTS.function("viewer_nei_tabs_have_recipes", 20, () -> RecipeViewerGameTests::neiTabsHaveRecipes);
    public static final GameTests.Function UNIVERSAL_TABS =
            TESTS.function("recipe_viewer_categories_1_7", 20, () -> RecipeViewerGameTests::universalTabs);

    public static void init() {}

    private static List<ViewerCategory<?>> categories() {
        List<ViewerCategory<?>> categories = new ArrayList<>();
        GeoNtmRecipeViewer.INSTANCE.categories(added -> categories.addAll(List.of(added)));
        return categories;
    }

    private static void titlesTranslated(GameTestHelper helper) {
        JsonObject lang;
        try (InputStream in = RecipeViewerGameTests.class.getResourceAsStream("/assets/geontm/lang/en_us.json")) {
            helper.assertTrue(in != null, "en_us.json is on the classpath");
            lang = JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        List<ViewerCategory<?>> categories = categories();
        List<String> missing = new ArrayList<>();
        for (ViewerCategory<?> category : categories) {
            if (category.title().getContents() instanceof TranslatableContents translatable
                    && !lang.has(translatable.getKey())) {
                missing.add(category.type().id() + " -> " + translatable.getKey());
            }
        }
        helper.assertTrue(categories.size() >= 50, "the viewer declares its categories: " + categories.size());
        helper.assertTrue(missing.isEmpty(), "every category title key is in en_us.json; missing " + missing);
        helper.succeed();
    }

    private static void neiTabsHaveRecipes(GameTestHelper helper) {
        List<ViewerCategory<?>> categories = categories();
        Map<ViewerType<?>, Integer> counts = new LinkedHashMap<>();
        counts.put(GeoNtmJeiRecipeTypes.SILEX, SILEXRecipeHandler.buildRecipes().size());
        counts.put(GeoNtmJeiRecipeTypes.ELECTROLYSIS_FLUID, ElectrolyserFluidRecipeHandler.buildRecipes().size());
        counts.put(GeoNtmJeiRecipeTypes.ELECTROLYSIS_METAL, ElectrolyserMetalRecipeHandler.buildRecipes().size());
        counts.put(GeoNtmJeiRecipeTypes.FLUID_CONTAINERS, FluidContainerRecipeHandler.buildRecipes().size());
        counts.put(GeoNtmJeiRecipeTypes.CONSTRUCTION, ConstructionRecipeHandler.buildRecipes().size());
        counts.put(GeoNtmJeiRecipeTypes.TOOLING, ToolingRecipeHandler.buildRecipes().size());
        counts.put(GeoNtmJeiRecipeTypes.CRUCIBLE_SMELTING, CrucibleSmeltingHandler.buildRecipes().size());
        counts.put(GeoNtmJeiRecipeTypes.CRUCIBLE_CASTING, CrucibleCastingHandler.buildRecipes().size());
        counts.put(GeoNtmJeiRecipeTypes.BOOK, BookRecipeHandler.buildRecipes().size());
        for (Map.Entry<ViewerType<?>, Integer> tab : counts.entrySet()) {
            helper.assertTrue(categories.stream().anyMatch(c -> c.type().equals(tab.getKey())), tab.getKey() + " is declared");
            helper.assertTrue(tab.getValue() > 0, tab.getKey() + " has recipes: " + tab.getValue());
        }
        Constants.LOG.info("[viewer] 1.7 NEI tab rows: {}", counts);

        // 1.7 ConstructionHandler lists six builds, each with its assembled block
        List<ConstructionRecipeHandler.Row> builds = ConstructionRecipeHandler.buildRecipes();
        helper.assertTrue(builds.size() == 6, "six construction rows: " + builds.size());
        for (ConstructionRecipeHandler.Row build : builds) {
            helper.assertTrue(!build.output().isEmpty(), "every construction row shows its block: " + build.machine());
        }
        helper.assertTrue(counts.get(GeoNtmJeiRecipeTypes.TOOLING) == NTMToolHandler.all().size(),
                "one tooling row per tool conversion");
        helper.assertTrue(FluidContainerRecipeHandler.buildRecipes().stream().anyMatch(e -> e.full().is(Items.WATER_BUCKET)
                && e.empty().is(Items.BUCKET) && e.amount() == 1000), "the water bucket is a fluid container row");
        helper.succeed();
    }

    /** The 1.7 NEIUniversalHandler tabs: each declared, with the row count of the table it reads or 1.7's own count. */
    private static void universalTabs(GameTestHelper helper) {
        List<ViewerCategory<?>> categories = categories();
        // 1.7 registers the annihilator's rows only in 528 mode
        helper.assertTrue(UniversalRecipeHandler.annihilator().isEmpty(), "no annihilator rows outside 528 mode");
        boolean was528 = GeneralConfig.enable528;
        int annihilator;
        try {
            GeneralConfig.enable528 = true;
            ItemRecipeTableGameTests.rebuild(AnnihilatorRecipes.TABLE);
            annihilator = UniversalRecipeHandler.annihilator().size();
        } finally {
            GeneralConfig.enable528 = was528;
            ItemRecipeTableGameTests.rebuild(AnnihilatorRecipes.TABLE);
        }
        OutgasserRecipes.INSTANCE.registerRecipes();
        Map<ViewerType<?>, int[]> tabs = new LinkedHashMap<>();
        tabs.put(GeoNtmJeiRecipeTypes.ANNIHILATOR, new int[]{annihilator, 16});
        tabs.put(GeoNtmJeiRecipeTypes.ORE_SLOPPER, new int[]{UniversalRecipeHandler.oreSlopper().size(), 1});
        tabs.put(GeoNtmJeiRecipeTypes.OUTGASSER,
                new int[]{UniversalRecipeHandler.outgasser().size(), OutgasserRecipes.INSTANCE.recipeOrderedList.size()});
        tabs.put(GeoNtmJeiRecipeTypes.DEUTERIUM, new int[]{UniversalRecipeHandler.deuterium().size(), 1});
        // 1.7: 19 shell/filling pairs x 5 fuzes x (plain + 4 extras)
        tabs.put(GeoNtmJeiRecipeTypes.GRENADE, new int[]{UniversalRecipeHandler.grenade().size(), 475});
        // 1.7 fuelMap, EnumPWRFuel and EnumWatzType sizes
        tabs.put(GeoNtmJeiRecipeTypes.ZIRNOX, new int[]{UniversalRecipeHandler.zirnox().size(), 11});
        tabs.put(GeoNtmJeiRecipeTypes.PWR, new int[]{UniversalRecipeHandler.pwr().size(), 15});
        tabs.put(GeoNtmJeiRecipeTypes.WATZ, new int[]{UniversalRecipeHandler.watz().size(), 12});
        // 1.7: 32 rods with pellets x 5 enrichments, doubled for the 27 whose pellets track xenon
        tabs.put(GeoNtmJeiRecipeTypes.RBMK_DISASSEMBLY, new int[]{UniversalRecipeHandler.rbmkDisassembly().size(), 295});
        tabs.put(GeoNtmJeiRecipeTypes.STORAGE_DRUM, new int[]{UniversalRecipeHandler.wasteDecay().size(), StorageDrumRecipes.getAll().size()});
        for (Map.Entry<ViewerType<?>, int[]> tab : tabs.entrySet()) {
            int[] rows = tab.getValue();
            helper.assertTrue(categories.stream().anyMatch(c -> c.type().equals(tab.getKey())), tab.getKey() + " is declared");
            helper.assertTrue(rows[0] > 0 && rows[0] == rows[1], tab.getKey() + " rows " + rows[0] + ", expected " + rows[1]);
        }

        // 1.7 OreSlopperHandler: 1,000 mB water and bedrock ore to every base ore plus 1,000 mB slop
        UniversalRecipeHandler.Row slop = UniversalRecipeHandler.oreSlopper().get(0);
        helper.assertTrue(slop.outputs().size() == BedrockOreType.values().size() + 1, "one slopper output per ore type plus slop");
        ItemStack slopIcon = slop.outputs().get(slop.outputs().size() - 1).get(0);
        helper.assertTrue(ItemFluidIcon.getFluid(slopIcon) == Fluids.SLOP && ModDataComponents.fluidAmount(slopIcon) == 1000,
                "the slopper makes 1,000 mB slop");
        // 1.7 DeuteriumHandler: 1,000 mB water to 20 mB heavy water
        ItemStack heavy = UniversalRecipeHandler.deuterium().get(0).outputs().get(0).get(0);
        helper.assertTrue(ItemFluidIcon.getFluid(heavy) == Fluids.HEAVYWATER && ModDataComponents.fluidAmount(heavy) == 20,
                "1,000 mB water gives 20 mB heavy water");
        // 1.7 OutgasserHandler machineOverrides: fusion-only rows show only the fusion breeder
        for (UniversalRecipeHandler.Row row : UniversalRecipeHandler.outgasser()) {
            boolean fusionOnly = !row.machines().isEmpty();
            helper.assertTrue(!fusionOnly || row.machines().get(0).is(ModMachineBlocks.fusion_breeder.get().asItem()),
                    "a fusion-only outgasser row names the fusion breeder");
        }
        // 1.7's tab order: every NEIRegistry tab declared, in NEI_ORDER, port-only ones after them
        List<String> declared = categories.stream().map(c -> c.type().path()).toList();
        helper.assertTrue(declared.containsAll(GeoNtmRecipeViewer.NEI_ORDER), "every 1.7 tab is declared: " + declared);
        List<String> nei = declared.stream().filter(GeoNtmRecipeViewer.NEI_ORDER::contains).toList();
        helper.assertTrue(nei.equals(GeoNtmRecipeViewer.NEI_ORDER) && declared.subList(0, nei.size()).equals(nei),
                "categories are declared in 1.7's NEIRegistry order: " + declared);
        Map<ViewerType<?>, Integer> counts = new LinkedHashMap<>();
        tabs.forEach((type, rows) -> counts.put(type, rows[0]));
        Constants.LOG.info("[viewer] 1.7 universal tab rows: {}", counts);
        Constants.LOG.info("[viewer] category order: {}", declared);
        helper.succeed();
    }
}
