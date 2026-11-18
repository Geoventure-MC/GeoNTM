package fr.geoventure.geontm.handler.rei;

//? if rei {

import fr.geoventure.geontm.handler.jei.GeoNtmRecipeViewer;
import fr.geoventure.geontm.handler.jei.GeoNtmViewerSubtypes;
import fr.geoventure.geocore.rei.ReiClientPlugin;

/**
 * HBM's REI client plugin: the same recipe-viewer declarations the JEI plugin uses ({@link GeoNtmRecipeViewer},
 * {@link GeoNtmViewerSubtypes}), fed to REI by GeoCore's {@link ReiClientPlugin}. Loader glue only: REI finds it by the
 * {@code @REIPluginClient} annotation on Forge-likes and by the {@code rei_client} entrypoint in {@code fabric.mod.json}.
 *
 * <p>Both viewers may be installed at once; each lists HBM's categories once, from its own plugin. On Forge 1.20.1 the
 * JEI plugin opts out of "REI Plugin Compatibilities" (JEI plugins run inside REI), which would otherwise list them a
 * second time in REI.
 */
//? if forgelike {
@me.shedaniel.rei.forge.REIPluginClient
//?}
public final class GeoNtmReiClientPlugin extends ReiClientPlugin {

    public GeoNtmReiClientPlugin() {
        super(GeoNtmRecipeViewer.INSTANCE, GeoNtmViewerSubtypes::register);
    }
}
//?}
