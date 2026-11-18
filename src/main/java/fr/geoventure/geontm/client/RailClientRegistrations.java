package fr.geoventure.geontm.client;

import fr.geoventure.geontm.blocks.ModRailBlocks;
import fr.geoventure.geontm.client.render.GeoNtmBlockEntityRenderers;
import fr.geoventure.geontm.client.render.RailRenderer;

/** Client-side binding for the folded rail family's source-faithful OBJ renderer. */
public final class RailClientRegistrations {

    private RailClientRegistrations() {}

    public static void registerRenderers(GeoNtmBlockEntityRenderers.Sink sink) {
        GeoNtmBlockEntityRenderers.regGem(sink, ModRailBlocks.RAIL_ENTITY.get(), RailRenderer::new);
    }
}
