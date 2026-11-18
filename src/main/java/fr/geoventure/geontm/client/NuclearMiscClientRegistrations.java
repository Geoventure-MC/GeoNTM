package fr.geoventure.geontm.client;

import fr.geoventure.geontm.client.render.GeoNtmBlockEntityRenderers;
import fr.geoventure.geontm.tileentity.ModNuclearMiscBlockEntities;
import fr.geoventure.geontm.inventory.ModNuclearMiscMenus;
import fr.geoventure.geontm.client.render.RenderBreeder;
import fr.geoventure.geontm.client.render.RenderRadiolysis;
import fr.geoventure.geontm.client.render.RenderSmallReactor;
import fr.geoventure.geontm.inventory.gui.GUIMachineReactorBreeding;
import fr.geoventure.geontm.inventory.gui.GUIRadiolysis;
import fr.geoventure.geontm.inventory.gui.GUIReactorControl;
import fr.geoventure.geontm.inventory.gui.GUIReactorResearch;

/** Screens and block-entity renderers for the miscellaneous nuclear machines. */
public final class NuclearMiscClientRegistrations {
    private NuclearMiscClientRegistrations() {}

    public static void registerScreens(GeoNtmClientRegistrations.ScreenSink sink) {
        sink.register(ModNuclearMiscMenus.RADIOLYSIS.get(), GUIRadiolysis::new);
        sink.register(ModNuclearMiscMenus.REACTOR_BREEDING.get(), GUIMachineReactorBreeding::new);
        sink.register(ModNuclearMiscMenus.REACTOR_RESEARCH.get(), GUIReactorResearch::new);
        sink.register(ModNuclearMiscMenus.REACTOR_CONTROL.get(), GUIReactorControl::new);
    }

    public static void registerRenderers(GeoNtmBlockEntityRenderers.Sink sink) {
        GeoNtmBlockEntityRenderers.regGem(sink, ModNuclearMiscBlockEntities.RADIOLYSIS.get(), RenderRadiolysis::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModNuclearMiscBlockEntities.REACTOR_BREEDING.get(), RenderBreeder::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModNuclearMiscBlockEntities.REACTOR_RESEARCH.get(), RenderSmallReactor::new);
    }
}
