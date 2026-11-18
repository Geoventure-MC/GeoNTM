package fr.geoventure.geontm.inventory.gui;

import fr.geoventure.geontm.client.GeoNtmClientRegistrations;
import fr.geoventure.geontm.inventory.ModGunSupportMenus;

/** Client registration helper for the stack-backed gun support menus. */
public final class GunSupportClientRegistrations {

    private GunSupportClientRegistrations() {}

    public static void registerScreens(GeoNtmClientRegistrations.ScreenSink sink) {
        sink.register(ModGunSupportMenus.AMMO_BAG.get(), ScreenItemBox::new);
        sink.register(ModGunSupportMenus.CASING_BAG.get(), ScreenItemBox::new);
        sink.register(ModGunSupportMenus.PLASTIC_BAG.get(), ScreenItemBox::new);
    }
}
