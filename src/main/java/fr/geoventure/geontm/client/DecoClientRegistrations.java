package fr.geoventure.geontm.client;

import fr.geoventure.geontm.client.render.GeoNtmBlockEntityRenderers;
import fr.geoventure.geontm.client.render.LanternRenderer;
import fr.geoventure.geontm.tileentity.ModDecoBlockEntities;
import fr.geoventure.geontm.client.render.DecoModelRenderer;
import fr.geoventure.geontm.client.render.DemonLampRenderer;
import fr.geoventure.geontm.client.render.FloodlightRenderer;

/** Client-side hooks of the decorative block family. */
public final class DecoClientRegistrations {
    private DecoClientRegistrations() {}

    public static void registerRenderers(GeoNtmBlockEntityRenderers.Sink sink) {
        GeoNtmBlockEntityRenderers.regGem(sink, ModDecoBlockEntities.DECO_MODEL.get(), DecoModelRenderer::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModDecoBlockEntities.FLOODLIGHT.get(), FloodlightRenderer::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModDecoBlockEntities.DEMON_LAMP.get(), DemonLampRenderer::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModDecoBlockEntities.LANTERN.get(),
                LanternRenderer::new);
    }
}
