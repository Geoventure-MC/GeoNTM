package fr.geoventure.geontm.handler.jei;

//? if jei && forgelike {

import fr.geoventure.geontm.Constants;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IExtraIngredientRegistration;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.Identifier;

/**
 * The Forge-like {@code @JeiPlugin} entrypoint, shared by NeoForge and Forge. Thin by design, all
 * category/recipe/catalyst logic lives in {@link GeoNtmJeiPluginCommon} (compiled against
 * {@code jei-*-common-api}), so every loader shares it and only the annotated entrypoint is per-module.
 *
 * <p>Fabric needs its own copy ({@link GeoNtmJeiPluginFabric}) because JEI discovers plugins differently
 * there, a {@code jei_mod_plugin} entrypoint in {@code fabric.mod.json} rather than an annotation
 * scan, but NeoForge and Forge discover this class the same way and need nothing loader-specific in
 * it, so widening the guard beats keeping a third identical copy.
 *
 * <p>On Forge 1.20.1 the class also opts out of "REI Plugin Compatibilities", which runs JEI plugins inside REI when
 * JEI is absent: GEONTM has a native REI plugin ({@code fr.geoventure.geontm.handler.rei}), and both would list every category twice.
 * That mod exists for no later target.
 */
//? if forge {
/*@me.shedaniel.rei.plugincompatibilities.api.REIPluginCompatIgnore
*///?}
@JeiPlugin
public class GeoNtmJeiPluginForgelike implements IModPlugin {

    private static final Identifier UID = fr.geoventure.geocore.Ids.of(Constants.MOD_ID, "jei_plugin");

    @Override
    public Identifier getPluginUid() {
        return UID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        GeoNtmJeiPluginCommon.registerItemSubtypes(registration);
    }

    @Override
    public void registerExtraIngredients(IExtraIngredientRegistration registration) {
        GeoNtmJeiPluginCommon.registerExtraIngredients(registration);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        GeoNtmJeiPluginCommon.registerCategories(registration);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        GeoNtmJeiPluginCommon.registerRecipes(registration);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        GeoNtmJeiPluginCommon.registerGuiHandlers(registration);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        GeoNtmJeiPluginCommon.registerRecipeTransferHandlers(registration);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        GeoNtmJeiPluginCommon.registerCatalysts(registration);
    }

    @Override
    public void onRuntimeAvailable(mezz.jei.api.runtime.IJeiRuntime runtime) {
        GeoNtmJeiPluginCommon.onRuntimeAvailable(runtime);
    }
}
//?}
