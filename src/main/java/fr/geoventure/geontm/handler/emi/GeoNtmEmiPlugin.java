package fr.geoventure.geontm.handler.emi;

//? if emi {
/*import fr.geoventure.geontm.handler.jei.GeoNtmRecipeViewer;
import fr.geoventure.geontm.handler.jei.GeoNtmViewerSubtypes;
import dev.emi.emi.api.EmiEntrypoint;
import fr.geoventure.geocore.emi.EmiViewerPlugin;

/^*
 * HBM's EMI plugin: the declarations the JEI and REI plugins use, fed to EMI by GeoCore. EMI finds it by
 * {@code @EmiEntrypoint} on Forge-likes and by the {@code emi} entrypoint in {@code fabric.mod.json}.
 ^/
@EmiEntrypoint
public final class GeoNtmEmiPlugin extends EmiViewerPlugin {

    public GeoNtmEmiPlugin() {
        super(GeoNtmRecipeViewer.INSTANCE, GeoNtmViewerSubtypes::register);
    }
}
*///?}
