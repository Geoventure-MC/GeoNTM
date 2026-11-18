package fr.geoventure.geontm.client;

import fr.geoventure.geontm.client.render.GeoNtmBlockEntityRenderers;
import fr.geoventure.geontm.tileentity.ModExplosivesBlockEntities;
import fr.geoventure.geontm.inventory.ModExplosivesMenus;
import fr.geoventure.geontm.client.render.BombMultiRenderer;
import fr.geoventure.geontm.client.render.ChargeRenderer;
import fr.geoventure.geontm.client.render.CrashedBombRenderer;
import fr.geoventure.geontm.client.render.LandmineRenderer;
import fr.geoventure.geontm.inventory.gui.GUIBombMulti;

/** Client hooks for the conventional explosives: the multi purpose bomb's screen and the five renderers. */
public final class ExplosivesClientRegistrations {
    private ExplosivesClientRegistrations() {}

    public static void registerScreens(GeoNtmClientRegistrations.ScreenSink sink) {
        sink.register(ModExplosivesMenus.BOMB_MULTI.get(), GUIBombMulti::new);
    }

    public static void registerRenderers(GeoNtmBlockEntityRenderers.Sink sink) {
        GeoNtmBlockEntityRenderers.regGem(sink, ModExplosivesBlockEntities.LANDMINE.get(), LandmineRenderer::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModExplosivesBlockEntities.CHARGE.get(), ChargeRenderer::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModExplosivesBlockEntities.CRASHED_BOMB.get(), CrashedBombRenderer::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModExplosivesBlockEntities.BOMB_MULTI.get(), BombMultiRenderer::new);
    }
}
