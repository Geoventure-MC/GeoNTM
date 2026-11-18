package fr.geoventure.geontm.client;

import fr.geoventure.geontm.client.render.GeoNtmBlockEntityRenderers;
import fr.geoventure.geontm.client.render.RenderPipeAnchor;
import fr.geoventure.geontm.tileentity.ModPneumaticBlockEntities;
import fr.geoventure.geontm.inventory.ModPneumaticMenus;
import fr.geoventure.geontm.packet.PneumoAccessSyncPayload;
import fr.geoventure.geocore.net.GeoCoreNetwork;
import fr.geoventure.geontm.inventory.gui.GUIPneumoStorageAccess;
import fr.geoventure.geontm.inventory.gui.GUIPneumoStorageClutter;
import fr.geoventure.geontm.inventory.gui.GUIPneumoStorageExporter;
import fr.geoventure.geontm.inventory.gui.GUIPneumoStorageImporter;
import fr.geoventure.geontm.inventory.gui.GUIPneumoStorageMono;
import fr.geoventure.geontm.inventory.gui.GUIPneumoTube;

/** Client-only registrations for the pneumatic tube storage system: screens, renderers and the terminal's packet. */
public final class PneumaticClientRegistrations {
    private PneumaticClientRegistrations() {}

    public static void registerScreens(GeoNtmClientRegistrations.ScreenSink sink) {
        sink.register(ModPneumaticMenus.PNEUMATIC_TUBE.get(), GUIPneumoTube::new);
        sink.register(ModPneumaticMenus.PNEUMATIC_STORAGE_ACCESS.get(), GUIPneumoStorageAccess::new);
        sink.register(ModPneumaticMenus.PNEUMATIC_STORAGE_CLUTTER.get(), GUIPneumoStorageClutter::new);
        sink.register(ModPneumaticMenus.PNEUMATIC_STORAGE_MONO.get(), GUIPneumoStorageMono::new);
        sink.register(ModPneumaticMenus.PNEUMATIC_STORAGE_IMPORTER.get(), GUIPneumoStorageImporter::new);
        sink.register(ModPneumaticMenus.PNEUMATIC_STORAGE_EXPORTER.get(), GUIPneumoStorageExporter::new);
    }

    public static void registerRenderers(GeoNtmBlockEntityRenderers.Sink sink) {
        GeoNtmBlockEntityRenderers.regGem(sink, ModPneumaticBlockEntities.PNEUMATIC_PIPE_ANCHOR.get(), () -> new RenderPipeAnchor<>("pipe_anchor_pneumatic"));
    }

    /** The access terminal's listing deltas; attached with the other client handlers. */
    public static void registerPacketHandlers() {
        GeoCoreNetwork.registerPlayToClient(PneumoAccessSyncPayload.CODEC, PneumoAccessSyncPayload::handleClient);
    }
}
