package fr.geoventure.geontm.client;

import fr.geoventure.geontm.inventory.ModSecurityMenus;
import fr.geoventure.geontm.inventory.gui.GUIMachineKeyForge;

/** Client hooks for the security family: the locksmith screen. */
public final class SecurityClientRegistrations {
    private SecurityClientRegistrations() {}

    public static void registerScreens(GeoNtmClientRegistrations.ScreenSink sink) {
        sink.register(ModSecurityMenus.MACHINE_KEYFORGE.get(), GUIMachineKeyForge::new);
    }
}
