package fr.geoventure.geontm.gametest;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.handler.jei.ArcWelderRecipeHandler;
import fr.geoventure.geontm.handler.jei.AshpitRecipeHandler;
import fr.geoventure.geontm.handler.jei.BoilingRecipeHandler;
import fr.geoventure.geontm.handler.jei.CokerRecipeHandler;
import fr.geoventure.geontm.handler.jei.CombinationRecipeHandler;
import fr.geoventure.geontm.handler.jei.CompressorRecipeHandler;
import fr.geoventure.geontm.handler.jei.CoolingRecipeHandler;
import fr.geoventure.geontm.handler.jei.CrackingRecipeHandler;
import fr.geoventure.geontm.handler.jei.ElectrolyserFluidRecipeHandler;
import fr.geoventure.geontm.handler.jei.ElectrolyserMetalRecipeHandler;
import fr.geoventure.geontm.handler.jei.FluidContainerRecipeHandler;
import fr.geoventure.geontm.handler.jei.FractioningRecipeHandler;
import fr.geoventure.geontm.handler.jei.GeoNtmJeiRecipeTypes;
import fr.geoventure.geontm.handler.jei.GeoNtmRecipeViewer;
import fr.geoventure.geontm.handler.jei.HydrotreatingRecipeHandler;
import fr.geoventure.geontm.handler.jei.LiquefactionRecipeHandler;
import fr.geoventure.geontm.handler.jei.PyrolysisRecipeHandler;
import fr.geoventure.geontm.handler.jei.RadiolysisRecipeHandler;
import fr.geoventure.geontm.handler.jei.RefineryRecipeHandler;
import fr.geoventure.geontm.handler.jei.ReformingRecipeHandler;
import fr.geoventure.geontm.handler.jei.RotaryFurnaceRecipeHandler;
import fr.geoventure.geontm.handler.jei.SolidificationRecipeHandler;
import fr.geoventure.geontm.handler.jei.UniversalRecipeHandler;
import fr.geoventure.geontm.handler.jei.VacuumRecipeHandler;
import fr.geoventure.geontm.inventory.recipes.CrystallizerRecipes;
import fr.geoventure.geontm.inventory.recipes.GasCentrifugeRecipes;
import fr.geoventure.geontm.inventory.recipes.SolderingRecipes;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.items.ModDataComponents;
import fr.geoventure.geontm.items.ModItems;
import fr.geoventure.geontm.items.armor.ItemModLens;
import fr.geoventure.geontm.items.machine.ItemFluidIcon;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.gametest.GameTests;
import fr.geoventure.geocore.viewer.Drawable;
import fr.geoventure.geocore.viewer.Role;
import fr.geoventure.geocore.viewer.SlotLayout;
import fr.geoventure.geocore.viewer.ViewerCategory;
import fr.geoventure.geocore.viewer.ViewerType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * GitHub issue 185: every fluid a recipe viewer row shows carries 1.7's NEI amount, so its tooltip reads "N mB".
 * Issue 188: the neutrino lens sweeps from the world floor, where the bedrock deposits are, and looks for blocks that exist.
 */
public final class Gh185To188GameTests {

    private Gh185To188GameTests() {}

    public static final GameTests.Suite TESTS = GameTests.suite(Constants.MOD_ID);
    public static final GameTests.Function AMOUNTS =
            TESTS.function("gh185_viewer_fluids_show_amounts", 20, () -> Gh185To188GameTests::amounts);

    public static final GameTests.Function LENS =
            TESTS.function("gh188_lens_sweeps_from_the_floor", 20, () -> Gh185To188GameTests::lens);

    public static void init() {}

    // 1.7 builds these icons without an amount: annihilator milestones and SILEX fluid inputs.
    private static final Set<ViewerType<?>> NO_AMOUNT = Set.of(GeoNtmJeiRecipeTypes.ANNIHILATOR, GeoNtmJeiRecipeTypes.SILEX);

    private static void amounts(GameTestHelper helper) {
        List<ViewerCategory<?>> categories = new ArrayList<>();
        GeoNtmRecipeViewer.INSTANCE.categories(added -> categories.addAll(List.of(added)));
        Map<ViewerType<?>, List<?>> recipes = recipes();

        Set<String> missing = new TreeSet<>();
        int checked = 0;
        for (ViewerCategory<?> category : categories) {
            if (NO_AMOUNT.contains(category.type())) continue;
            List<ItemStack> icons = new ArrayList<>();
            for (Object recipe : recipes.getOrDefault(category.type(), List.of())) record(category, recipe, icons);
            for (ItemStack icon : icons) {
                checked++;
                if (ModDataComponents.fluidAmount(icon) <= 0) missing.add(category.type().path());
            }
        }
        helper.assertTrue(missing.isEmpty(), "fluid icons without an amount in " + missing);
        helper.assertTrue(checked > 500, "only " + checked + " fluid icons checked");

        // 1.7 RefineryRecipes, HydrotreatingRecipes, ReformingRecipes, VacuumRefineryRecipes: rows per 1,000 mB of input
        for (ViewerType<?> type : List.of(GeoNtmJeiRecipeTypes.REFINERY, GeoNtmJeiRecipeTypes.HYDROTREATING,
                GeoNtmJeiRecipeTypes.REFORMING, GeoNtmJeiRecipeTypes.VACUUM)) {
            for (Object recipe : recipes.get(type)) {
                List<ItemStack> icons = new ArrayList<>();
                record(category(categories, type), recipe, icons);
                helper.assertTrue(ModDataComponents.fluidAmount(icons.get(0)) == 1000,
                        type.path() + " input is not 1,000 mB: " + describe(icons));
                if (recipe instanceof HydrotreatingRecipeHandler.Entry row) {
                    helper.assertTrue(ModDataComponents.fluidPressure(icons.get(1)) == row.recipe().hydrogen().pressure,
                            "hydrotreating hydrogen lost its row's pressure: " + describe(icons));
                }
            }
        }

        // 1.7 CrystallizerRecipes: iron ore and 500 mB of hydrogen peroxide make an iron crystal (raw iron is a port row)
        List<ItemStack> iron = new ArrayList<>();
        for (Object recipe : recipes.get(GeoNtmJeiRecipeTypes.CRYSTALLIZER)) {
            List<ItemStack> icons = new ArrayList<>();
            boolean[] crystal = {false};
            SlotLayout layout = layout(icons, crystal);
            category(categories, GeoNtmJeiRecipeTypes.CRYSTALLIZER).setRecipe(layout, cast(recipe));
            if (crystal[0]) iron.addAll(icons);
        }
        helper.assertTrue(iron.stream().anyMatch(icon -> ItemFluidIcon.getFluid(icon) == Fluids.PEROXIDE
                && ModDataComponents.fluidAmount(icon) == 500), "iron crystal acid: " + describe(iron));
        helper.succeed();
    }

    private static void lens(GameTestHelper helper) {
        for (String id : ItemModLens.targetIds()) {
            helper.assertTrue(BuiltInRegistries.BLOCK.getOptional(Ids.parse(id)).isPresent(), "lens looks for unknown " + id);
        }
        ServerLevel level = helper.getLevel();
        int y = 70, floor = level.getMinBuildHeight();
        int lowest = Integer.MAX_VALUE, highest = Integer.MIN_VALUE;
        for (long time = 0; time < 1000; time++) {
            int layer = ItemModLens.layer(level, y, time);
            lowest = Math.min(lowest, layer);
            highest = Math.max(highest, layer);
        }
        // 1.7: layers 0 to y + 9 above its floor, here min(y - floor + 10, height - 1) layers from the floor up
        int top = floor + Math.max(Math.min(y - floor + 10, level.getHeight() - 1), 64) - 1;
        helper.assertTrue(lowest == floor && highest == top, "lens sweeps " + lowest + ".." + highest + ", not " + floor + ".." + top);
        helper.succeed();
    }

    private static <T> void record(ViewerCategory<T> category, Object recipe, List<ItemStack> icons) {
        category.setRecipe(layout(icons, new boolean[1]), cast(recipe));
    }

    @SuppressWarnings("unchecked")
    private static <T> T cast(Object recipe) {
        return (T) recipe;
    }

    @SuppressWarnings("unchecked")
    private static <T> ViewerCategory<T> category(List<ViewerCategory<?>> categories, ViewerType<T> type) {
        for (ViewerCategory<?> category : categories) if (category.type() == type) return (ViewerCategory<T>) category;
        throw new IllegalStateException("no category " + type.path());
    }

    private static String describe(List<ItemStack> icons) {
        List<String> out = new ArrayList<>();
        for (ItemStack icon : icons) out.add(ItemFluidIcon.getFluid(icon).getName() + " " + ModDataComponents.fluidAmount(icon));
        return out.toString();
    }

    /** Collects the fluid icons a row puts in drawn slots; {@code crystal} marks a row making an iron crystal. */
    private static SlotLayout layout(List<ItemStack> icons, boolean[] crystal) {
        return new SlotLayout() {
            @Override
            public Slot addSlot(Role role, int x, int y) {
                return new Slot() {
                    @Override
                    public Slot addItemStacks(List<ItemStack> stacks) {
                        for (ItemStack stack : stacks) {
                            if (stack.getItem() instanceof ItemFluidIcon) icons.add(stack);
                            if (role == Role.OUTPUT && stack.is(ModItems.crystal_iron.get())) crystal[0] = true;
                        }
                        return this;
                    }

                    @Override
                    public Slot addFluidStack(Fluid fluid, long milliBuckets) {
                        return this;
                    }

                    @Override
                    public Slot setBackground(Drawable background, int dx, int dy) {
                        return this;
                    }

                    @Override
                    public Slot addTooltipCallback(TooltipCallback callback) {
                        return this;
                    }
                };
            }

            @Override
            public void addInvisible(Role role, List<ItemStack> stacks) {}
        };
    }

    /** The rows of every category with a fluid slot, as {@link GeoNtmRecipeViewer#recipes} adds them (that needs a client level). */
    private static Map<ViewerType<?>, List<?>> recipes() {
        CrystallizerRecipes.register();
        GasCentrifugeRecipes.register();
        SolderingRecipes.register();
        Map<ViewerType<?>, List<?>> map = new HashMap<>();
        map.put(GeoNtmJeiRecipeTypes.CRYSTALLIZER, CrystallizerRecipes.recipes);
        map.put(GeoNtmJeiRecipeTypes.GAS_CENTRIFUGE, GasCentrifugeRecipes.RECIPES);
        map.put(GeoNtmJeiRecipeTypes.SOLDERING_STATION, SolderingRecipes.recipes);
        map.put(GeoNtmJeiRecipeTypes.REFINERY, RefineryRecipeHandler.buildRecipes());
        map.put(GeoNtmJeiRecipeTypes.FRACTIONING, FractioningRecipeHandler.buildRecipes());
        map.put(GeoNtmJeiRecipeTypes.LIQUEFACTION, LiquefactionRecipeHandler.buildRecipes());
        map.put(GeoNtmJeiRecipeTypes.SOLIDIFICATION, SolidificationRecipeHandler.buildRecipes());
        map.put(GeoNtmJeiRecipeTypes.BOILING, BoilingRecipeHandler.buildRecipes());
        map.put(GeoNtmJeiRecipeTypes.COOLING, CoolingRecipeHandler.buildRecipes());
        map.put(GeoNtmJeiRecipeTypes.VACUUM, VacuumRecipeHandler.buildRecipes());
        map.put(GeoNtmJeiRecipeTypes.REFORMING, ReformingRecipeHandler.buildRecipes());
        map.put(GeoNtmJeiRecipeTypes.CRACKING, CrackingRecipeHandler.buildRecipes());
        map.put(GeoNtmJeiRecipeTypes.HYDROTREATING, HydrotreatingRecipeHandler.buildRecipes());
        map.put(GeoNtmJeiRecipeTypes.COKER, CokerRecipeHandler.buildRecipes());
        map.put(GeoNtmJeiRecipeTypes.PYROLYSIS, PyrolysisRecipeHandler.buildRecipes());
        map.put(GeoNtmJeiRecipeTypes.COMBINATION, CombinationRecipeHandler.buildRecipes());
        map.put(GeoNtmJeiRecipeTypes.COMPRESSOR, CompressorRecipeHandler.buildRecipes());
        map.put(GeoNtmJeiRecipeTypes.ELECTROLYSIS_FLUID, ElectrolyserFluidRecipeHandler.buildRecipes());
        map.put(GeoNtmJeiRecipeTypes.ELECTROLYSIS_METAL, ElectrolyserMetalRecipeHandler.buildRecipes());
        map.put(GeoNtmJeiRecipeTypes.FLUID_CONTAINERS, FluidContainerRecipeHandler.buildRecipes());
        map.put(GeoNtmJeiRecipeTypes.ASHPIT, AshpitRecipeHandler.buildRecipes());
        map.put(GeoNtmJeiRecipeTypes.ARC_WELDER, ArcWelderRecipeHandler.buildRecipes());
        map.put(GeoNtmJeiRecipeTypes.ROTARY_FURNACE, RotaryFurnaceRecipeHandler.buildRecipes());
        map.put(GeoNtmJeiRecipeTypes.ORE_SLOPPER, UniversalRecipeHandler.oreSlopper());
        map.put(GeoNtmJeiRecipeTypes.OUTGASSER, UniversalRecipeHandler.outgasser());
        map.put(GeoNtmJeiRecipeTypes.DEUTERIUM, UniversalRecipeHandler.deuterium());
        map.put(RadiolysisRecipeHandler.TYPE, RadiolysisRecipeHandler.buildRecipes());
        return map;
    }
}
