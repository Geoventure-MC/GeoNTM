package fr.geoventure.geontm.client;

import fr.geoventure.geontm.client.render.GeoNtmBlockEntityRenderers;
import fr.geoventure.geontm.client.render.RenderICF;
import fr.geoventure.geontm.client.render.RenderICFController;
import fr.geoventure.geontm.client.render.StructPreviewRenderer;
import fr.geoventure.geontm.inventory.ModIcfMenus;
import fr.geoventure.geontm.inventory.gui.GUIICF;
import fr.geoventure.geontm.inventory.gui.GUIICFPress;
import fr.geoventure.geontm.tileentity.ModIcfBlockEntities;
import fr.geoventure.geontm.tileentity.machine.TileEntityICFStruct;

/** Screens and block-entity renderers of the ICF. */
public final class IcfClientRegistrations {

    private IcfClientRegistrations() {}

    public static void registerScreens(GeoNtmClientRegistrations.ScreenSink sink) {
        sink.register(ModIcfMenus.ICF.get(), GUIICF::new);
        sink.register(ModIcfMenus.ICF_PRESS.get(), GUIICFPress::new);
    }

    public static void registerRenderers(GeoNtmBlockEntityRenderers.Sink sink) {
        GeoNtmBlockEntityRenderers.regGem(sink, ModIcfBlockEntities.ICF.get(), RenderICF::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModIcfBlockEntities.ICF_CONTROLLER.get(), RenderICFController::new);
        // 1.7 RenderICFMultiblock: the ghost of the vessel the seed still needs
        sink.register(ModIcfBlockEntities.ICF_STRUCT.get(), ctx -> new StructPreviewRenderer<TileEntityICFStruct>());
    }
}
