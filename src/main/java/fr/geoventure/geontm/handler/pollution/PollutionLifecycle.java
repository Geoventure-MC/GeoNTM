package fr.geoventure.geontm.handler.pollution;

import fr.geoventure.geocore.platform.Lifecycle;

/** Loader-neutral bootstrap for the pollution field's server-side driver. */
public final class PollutionLifecycle {
    private static boolean initialized;
    private PollutionLifecycle() {}
    public static void init() {
        if (initialized) return;
        initialized = true;
        Lifecycle.onServerTick(PollutionHandler::updateSystem);
    }
}
