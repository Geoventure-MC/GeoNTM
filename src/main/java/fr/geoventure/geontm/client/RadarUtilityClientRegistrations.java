package fr.geoventure.geontm.client;

import fr.geoventure.geontm.blocks.machine.MachineRadar;
import fr.geoventure.geontm.client.render.GeoNtmBlockEntityRenderers;
import fr.geoventure.geontm.client.render.RenderCapsule;
import fr.geoventure.geontm.client.render.RenderMachineForceField;
import fr.geoventure.geontm.client.render.RenderOrbus;
import fr.geoventure.geontm.client.render.RenderRadar;
import fr.geoventure.geontm.client.render.RenderRadarLarge;
import fr.geoventure.geontm.client.render.RenderRadarScreen;
import fr.geoventure.geontm.inventory.ModRadarUtilityMenus;
import fr.geoventure.geontm.inventory.gui.GUIForceField;
import fr.geoventure.geontm.inventory.gui.GUIMachineRadarNT;
import fr.geoventure.geontm.inventory.gui.GUIMachineRadarNTSlots;
import fr.geoventure.geontm.inventory.gui.GUISoyuzCapsule;
import fr.geoventure.geontm.tileentity.ModRadarUtilityBlockEntities;
import net.minecraft.client.Minecraft;

/** Screens and block-entity renderers of the radar and utility machines. */
public final class RadarUtilityClientRegistrations {

    private RadarUtilityClientRegistrations() {}

    public static void registerScreens(GeoNtmClientRegistrations.ScreenSink sink) {
        sink.register(ModRadarUtilityMenus.RADAR.get(), GUIMachineRadarNTSlots::new);
        sink.register(ModRadarUtilityMenus.FORCEFIELD.get(), GUIForceField::new);
        sink.register(ModRadarUtilityMenus.SOYUZ_CAPSULE.get(), GUISoyuzCapsule::new);
        // 1.7 GUI id 0: the radar map is a client-only screen with no menu
        MachineRadar.OPEN_SCREEN = radar -> Minecraft.getInstance().setScreen(new GUIMachineRadarNT(radar));
    }

    public static void registerRenderers(GeoNtmBlockEntityRenderers.Sink sink) {
        GeoNtmBlockEntityRenderers.regGem(sink, ModRadarUtilityBlockEntities.RADAR.get(), RenderRadar::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModRadarUtilityBlockEntities.RADAR_LARGE.get(), RenderRadarLarge::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModRadarUtilityBlockEntities.RADAR_SCREEN.get(), RenderRadarScreen::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModRadarUtilityBlockEntities.FORCEFIELD.get(), RenderMachineForceField::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModRadarUtilityBlockEntities.ORBUS.get(), RenderOrbus::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModRadarUtilityBlockEntities.SOYUZ_CAPSULE.get(), RenderCapsule::new);
    }
}
