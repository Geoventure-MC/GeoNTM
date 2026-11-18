package fr.geoventure.geontm.handler.jei;

//? if jei {

import fr.geoventure.geocore.jei.JeiViewer;
import mezz.jei.api.registration.IExtraIngredientRegistration;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import java.util.List;
import java.util.function.Supplier;

/**
 * The loader-agnostic body of the mod's JEI plugin. Each loader's thin {@code @JeiPlugin} entrypoint delegates its
 * phases here, and each phase hands HBM's recipe-viewer declarations ({@link GeoNtmRecipeViewer},
 * {@link GeoNtmViewerSubtypes}) to GeoCore's {@link JeiViewer}; the REI plugins ({@code fr.geoventure.geontm.handler.rei}) feed the
 * same declarations to REI. Only the JEI fluid-ingredient read-back below is JEI's alone.
 */
public final class GeoNtmJeiPluginCommon {

    private GeoNtmJeiPluginCommon() {}

    /** The names of the GEONTM fluid ingredients JEI lists; set once its runtime exists, read by the harness. */
    public static volatile Supplier<List<String>> fluidNames = null;

    public static void registerItemSubtypes(ISubtypeRegistration reg) {
        JeiViewer.subtypes(reg, GeoNtmViewerSubtypes::register);
    }

    public static void registerExtraIngredients(IExtraIngredientRegistration reg) {
        JeiViewer.ingredients(reg, GeoNtmRecipeViewer.INSTANCE);
    }

    public static void registerCategories(IRecipeCategoryRegistration reg) {
        JeiViewer.categories(reg, GeoNtmRecipeViewer.INSTANCE);
    }

    public static void registerRecipes(IRecipeRegistration reg) {
        JeiViewer.recipes(reg, GeoNtmRecipeViewer.INSTANCE);
    }

    public static void registerGuiHandlers(IGuiHandlerRegistration reg) {
        JeiViewer.clickAreas(reg, GeoNtmRecipeViewer.INSTANCE);
    }

    public static void registerRecipeTransferHandlers(IRecipeTransferRegistration reg) {
        JeiViewer.transfers(reg, GeoNtmRecipeViewer.INSTANCE);
    }

    public static void registerCatalysts(IRecipeCatalystRegistration reg) {
        JeiViewer.stations(reg, GeoNtmRecipeViewer.INSTANCE);
    }

    /**
     * Runs once JEI's runtime exists: HBM's runtime categories (the arc furnace's furnace pass, {@link GeoNtmRecipeViewer#runtime}),
     * then the JEI-only read-back of GEONTM fluids as JEI fluid ingredients. JEI lists every
     * registered source fluid itself; this only reads the list back, for the log and the harness.
     */
    public static void onRuntimeAvailable(mezz.jei.api.runtime.IJeiRuntime runtime) {
        JeiViewer.runtime(runtime, GeoNtmRecipeViewer.INSTANCE);
        java.util.List<String> fluids = geontmFluidIngredients(runtime);
        fluidNames = () -> geontmFluidIngredients(runtime);
        fr.geoventure.geontm.Constants.LOG.info("[JEI] {} GEONTM fluid(s) listed as fluid ingredients", fluids.size());
    }

    /** The display names of the JEI fluid ingredients that are GEONTM fluids. */
    private static java.util.List<String> geontmFluidIngredients(mezz.jei.api.runtime.IJeiRuntime runtime) {
        return geontmFluidIngredients(runtime, runtime.getJeiHelpers().getPlatformFluidHelper());
    }

    private static <T> java.util.List<String> geontmFluidIngredients(mezz.jei.api.runtime.IJeiRuntime runtime,
            mezz.jei.api.helpers.IPlatformFluidHelper<T> fluids) {
        mezz.jei.api.ingredients.IIngredientTypeWithSubtypes<net.minecraft.world.level.material.Fluid, T> type =
                fluids.getFluidIngredientType();
        mezz.jei.api.ingredients.IIngredientHelper<T> helper = runtime.getIngredientManager().getIngredientHelper(type);
        java.util.List<String> names = new java.util.ArrayList<>();
        for (T ingredient : runtime.getIngredientManager().getAllIngredients(type)) {
            if (fr.geoventure.geocore.fluid.TokenFluids.isToken(type.getBase(ingredient))) names.add(helper.getDisplayName(ingredient));
        }
        return names;
    }
}
//?}
