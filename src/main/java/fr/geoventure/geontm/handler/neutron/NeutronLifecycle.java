package fr.geoventure.geontm.handler.neutron;

import fr.geoventure.geocore.platform.Lifecycle;

/** Loader-neutral bootstrap for the server-side neutron queue driver. */
public final class NeutronLifecycle {
    private static boolean initialized;
    private NeutronLifecycle() {}
    public static void init() {
        if (initialized) return;
        initialized = true;
        Lifecycle.onServerTick(NeutronHandler::onServerTick);
    }
}
