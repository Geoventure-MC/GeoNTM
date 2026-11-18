package fr.geoventure.geontm.client;

import fr.geoventure.geontm.inventory.ModMeleeKitMenus;
import fr.geoventure.geontm.inventory.gui.GUIBook;
import fr.geoventure.geontm.inventory.gui.GUIScreenBobmazon;
import fr.geoventure.geontm.inventory.gui.GUIToolBox;
import fr.geoventure.geontm.items.tool.ItemCatalog;

/** Client registrations of the special melee / kit family: the toolbox and book screens and the Bobmazon opener. */
public final class MeleeKitClientRegistrations {

    private MeleeKitClientRegistrations() {}

    public static void registerScreens(GeoNtmClientRegistrations.ScreenSink sink) {
        ItemCatalog.setClientScreenOpener(GUIScreenBobmazon::open);
        sink.register(ModMeleeKitMenus.TOOLBOX.get(), GUIToolBox::new);
        sink.register(ModMeleeKitMenus.BOOK.get(), GUIBook::new);
    }
}
