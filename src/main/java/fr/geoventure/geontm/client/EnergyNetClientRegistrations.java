package fr.geoventure.geontm.client;

import fr.geoventure.geontm.client.render.GeoNtmBlockEntityRenderers;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.blocks.network.BlockNetworkSeparator;
import fr.geoventure.geontm.client.render.MachineObjRenderer;
import fr.geoventure.geontm.client.render.MachineObjRenderer.ModelDef;
import fr.geoventure.geontm.tileentity.network.TileEntityNetworkSeparator;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geontm.tileentity.ModEnergyNetBlockEntities;
import net.minecraft.client.Minecraft;
import fr.geoventure.geontm.client.render.RenderCableDiode;
import fr.geoventure.geontm.client.render.RenderConnector;
import fr.geoventure.geontm.client.render.RenderPylon;
import fr.geoventure.geontm.client.render.RenderPylonLarge;
import fr.geoventure.geontm.client.render.RenderPylonMedium;
import fr.geoventure.geontm.client.render.RenderSubstation;
import fr.geoventure.geontm.client.render.RenderTesla;
import fr.geoventure.geontm.client.render.WiringHudOverlay;
import fr.geoventure.geontm.inventory.gui.GUIDiode;

/** Client hooks for the energy-network family: the separator screen, the pylon/wire renderers, the drum readout. */
public final class EnergyNetClientRegistrations {

    private EnergyNetClientRegistrations() {}

    public static void registerScreens(GeoNtmClientRegistrations.ScreenSink sink) {
        BlockNetworkSeparator.OPEN_SCREEN = separator -> Minecraft.getInstance().setScreen(new GUIDiode(separator));
    }

    public static void registerRenderers(GeoNtmBlockEntityRenderers.Sink sink) {
        GeoNtmBlockEntityRenderers.regGem(sink, ModEnergyNetBlockEntities.PYLON.get(), RenderPylon::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModEnergyNetBlockEntities.PYLON_MEDIUM.get(), RenderPylonMedium::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModEnergyNetBlockEntities.PYLON_LARGE.get(), RenderPylonLarge::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModEnergyNetBlockEntities.SUBSTATION.get(), RenderSubstation::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModEnergyNetBlockEntities.CONNECTOR.get(), () -> new RenderConnector<>(false));
        GeoNtmBlockEntityRenderers.regGem(sink, ModEnergyNetBlockEntities.CONNECTOR_SUPER.get(), () -> new RenderConnector<>(true));
        GeoNtmBlockEntityRenderers.regGem(sink, ModEnergyNetBlockEntities.CABLE_DIODE.get(), RenderCableDiode::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModEnergyNetBlockEntities.TESLA.get(), RenderTesla::new);
        // 1.7 turns N=90, W=180, S=270, E=0: the standard map + 90
        GeoNtmBlockEntityRenderers.regGem(sink, ModEnergyNetBlockEntities.NETWORK_SEPARATOR.get(),
                () -> new MachineObjRenderer<TileEntityNetworkSeparator>(90F, 1F,
                        new ModelDef(Ids.of(Constants.MOD_ID, "models/network/network_separator.obj"),
                                Ids.of(Constants.MOD_ID, "textures/models/network/network_separator.png"))));
    }

    public static void registerHud(GeoNtmClientRegistrations.HudSink sink) {
        sink.register("wiring_readout", WiringHudOverlay::render);
    }
}
