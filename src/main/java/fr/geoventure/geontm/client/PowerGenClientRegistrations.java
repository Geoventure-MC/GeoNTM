package fr.geoventure.geontm.client;

import fr.geoventure.geontm.client.render.GeoNtmBlockEntityRenderers;
import fr.geoventure.geontm.tileentity.ModPowerGenBlockEntities;
import fr.geoventure.geontm.inventory.ModPowerGenMenus;
import fr.geoventure.geontm.packet.SirenPayload;
import fr.geoventure.geocore.net.GeoCoreNetwork;
import fr.geoventure.geontm.client.render.RenderChungus;
import fr.geoventure.geontm.client.render.RenderHephaestus;
import fr.geoventure.geontm.client.render.RenderIGenerator;
import fr.geoventure.geontm.client.render.RenderLPW2;
import fr.geoventure.geontm.client.render.SirenClient;
import fr.geoventure.geontm.inventory.gui.GUIMachineSiren;

/** Screens, block-entity renderers and the siren packet handler for the large power-generation family. */
public final class PowerGenClientRegistrations {
    private PowerGenClientRegistrations() {}

    public static void registerScreens(GeoNtmClientRegistrations.ScreenSink sink) {
        sink.register(ModPowerGenMenus.SIREN.get(), GUIMachineSiren::new);
    }

    public static void registerRenderers(GeoNtmBlockEntityRenderers.Sink sink) {
        GeoNtmBlockEntityRenderers.regGem(sink, ModPowerGenBlockEntities.CHUNGUS.get(), RenderChungus::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModPowerGenBlockEntities.LPW2.get(), RenderLPW2::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModPowerGenBlockEntities.HEPHAESTUS.get(), RenderHephaestus::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModPowerGenBlockEntities.INDUSTRIAL_GENERATOR.get(), RenderIGenerator::new);
    }

    /** 1.7 {@code TESirenPacket.Handler}. */
    public static void registerPacketHandlers() {
        GeoCoreNetwork.registerPlayToClient(SirenPayload.CODEC, SirenClient::handle);
    }
}
