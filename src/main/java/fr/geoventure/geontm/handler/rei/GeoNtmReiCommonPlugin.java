package fr.geoventure.geontm.handler.rei;

//? if rei {

import fr.geoventure.geontm.handler.jei.GeoNtmViewerSubtypes;
import fr.geoventure.geocore.rei.ReiCommonPlugin;

/**
 * HBM's REI common plugin: registers {@link GeoNtmViewerSubtypes} as REI item comparators, so fluid icons, tanks,
 * blueprints and the other component-backed families stay distinct entries (JEI's subtypes). Server-safe. Loader glue
 * only: {@code @REIPluginCommon} on Forge-likes, the {@code rei_common} entrypoint on Fabric.
 */
//? if forgelike {
@me.shedaniel.rei.forge.REIPluginCommon
//?}
public final class GeoNtmReiCommonPlugin extends ReiCommonPlugin {

    public GeoNtmReiCommonPlugin() {
        super(GeoNtmViewerSubtypes::register);
    }
}
//?}
