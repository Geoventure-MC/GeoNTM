package fr.geoventure.geontm.client;

import fr.geoventure.geontm.client.render.GeoNtmBlockEntityRenderers;
import fr.geoventure.geontm.client.render.GeoNtmEntityRenderers;
import fr.geoventure.geontm.client.render.SatDockRenderer;
import fr.geoventure.geontm.client.render.entity.RenderOrbitalLaser;
import fr.geoventure.geontm.entity.ModSatelliteEntities;
import fr.geoventure.geontm.inventory.ModSatelliteMenus;
import fr.geoventure.geontm.inventory.gui.GUIMachineSatLinker;
import fr.geoventure.geontm.inventory.gui.GUISatDock;
import fr.geoventure.geontm.inventory.gui.GUIScreenSatCoord;
import fr.geoventure.geontm.items.tool.ItemSatInterface;
import fr.geoventure.geontm.tileentity.ModSatelliteBlockEntities;
import net.minecraft.client.Minecraft;

/** Screens, block-entity renderers and entity renderers of the satellite family. */
public final class SatelliteClientRegistrations {

    private SatelliteClientRegistrations() {}

    public static void registerScreens(GeoNtmClientRegistrations.ScreenSink sink) {
        sink.register(ModSatelliteMenus.SATLINKER.get(), GUIMachineSatLinker::new);
        sink.register(ModSatelliteMenus.SAT_DOCK.get(), GUISatDock::new);
        // 1.7 ItemSatInterface.provideGUI: a client-only screen with no container
        ItemSatInterface.OPEN_SCREEN = player -> Minecraft.getInstance()
                .setScreen(new GUIScreenSatCoord(player));
    }

    public static void registerRenderers(GeoNtmBlockEntityRenderers.Sink sink) {
        GeoNtmBlockEntityRenderers.regGem(sink, ModSatelliteBlockEntities.SAT_DOCK.get(), SatDockRenderer::new);
    }

    public static void registerEntityRenderers(GeoNtmEntityRenderers.Sink sink) {
        sink.register(ModSatelliteEntities.ORBITAL_LASER.get(), RenderOrbitalLaser::new);
    }
}
