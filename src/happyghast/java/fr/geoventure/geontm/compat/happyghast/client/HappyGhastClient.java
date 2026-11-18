package fr.geoventure.geontm.compat.happyghast.client;

import fr.geoventure.geontm.client.render.GeoNtmEntityRenderers;
import fr.geoventure.geontm.compat.happyghast.GeoNtmHappyGhast;

/**
 * The happy-ghast Fat Man's client half: its renderer joins HBM's own entity renderers ({@link GeoNtmEntityRenderers}),
 * so it is bound through the same sink and counted by the coverage audit. The loader roots call {@link #init()} on
 * the client before GEONTM registers renderers.
 */
public final class HappyGhastClient {

    private HappyGhastClient() {}

    private static boolean initialised;

    public static synchronized void init() {
        if (initialised) return;
        initialised = true;
        GeoNtmEntityRenderers.addExtension(sink -> sink.register(GeoNtmHappyGhast.RIGGED_FAT_MAN.get(), RenderRiggedFatMan::new));
    }
}
