package fr.geoventure.geontm.handler.jei;

//? if jei && fabric {

/*import fr.geoventure.geontm.Constants;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IExtraIngredientRegistration;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;

/^*
 * Fabric {@code @JeiPlugin} entrypoint. Thin by design, all category/recipe/catalyst logic lives in
 * {@link GeoNtmJeiPluginCommon} (compiled against {@code jei-*-common-api}), so both loaders share it and only
 * the annotated entrypoint is per-module (JEI discovers {@code @JeiPlugin} by classpath annotation scan,
 * and the loader APIs differ). Mirrors the {@code GeoNtmClient}/{@code GeoNtmClientNeoForge} split.
 ^/
@JeiPlugin
public class GeoNtmJeiPluginFabric implements IModPlugin {

    private static final ResourceLocation UID = fr.geoventure.geocore.Ids.of(Constants.MOD_ID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
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
*///?}
