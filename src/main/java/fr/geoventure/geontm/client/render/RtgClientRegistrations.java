package fr.geoventure.geontm.client.render;

import fr.geoventure.geontm.client.GeoNtmClientRegistrations;
import fr.geoventure.geontm.inventory.ModRtgMenus;
import fr.geoventure.geontm.inventory.gui.GUIMachineRTG;
import fr.geoventure.geontm.tileentity.ModRtgBlockEntities;

/** Client registration bridge kept with the feature instead of the shared registration tables. */
public final class RtgClientRegistrations {
    private RtgClientRegistrations() {}

    public static void registerScreen(GeoNtmClientRegistrations.ScreenSink sink) {
        sink.register(ModRtgMenus.MACHINE_RTG.get(), GUIMachineRTG::new);
    }

    public static void registerRenderer(GeoNtmBlockEntityRenderers.Sink sink) {
        sink.register(ModRtgBlockEntities.MACHINE_RTG.get(), context -> new RTGRenderer<>());
    }
}
