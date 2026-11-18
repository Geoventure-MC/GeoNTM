package fr.geoventure.geontm.client;

import fr.geoventure.geontm.client.render.GeoNtmBlockEntityRenderers;
import fr.geoventure.geontm.tileentity.ModProductionBlockEntities;
import fr.geoventure.geontm.inventory.ModProductionMenus;
import fr.geoventure.geontm.client.render.RenderConveyorPress;
import fr.geoventure.geontm.client.render.RenderMicrowave;
import fr.geoventure.geontm.client.render.RenderRotaryFurnace;
import fr.geoventure.geontm.client.render.RenderThresher;
import fr.geoventure.geontm.inventory.gui.GUIAutocrafter;
import fr.geoventure.geontm.inventory.gui.GUIMachineRotaryFurnace;
import fr.geoventure.geontm.inventory.gui.GUIMicrowave;
import fr.geoventure.geontm.inventory.gui.GUIFunnel;
import fr.geoventure.geontm.client.render.RenderFunnel;

/** Screens and block-entity renderers for the production machines (group A). */
public final class ProductionClientRegistrations {
    private ProductionClientRegistrations() {}

    public static void registerScreens(GeoNtmClientRegistrations.ScreenSink sink) {
        sink.register(ModProductionMenus.ROTARY_FURNACE.get(), GUIMachineRotaryFurnace::new);
        sink.register(ModProductionMenus.MICROWAVE.get(), GUIMicrowave::new);
        sink.register(ModProductionMenus.AUTOCRAFTER.get(), GUIAutocrafter::new);
        sink.register(ModProductionMenus.FUNNEL.get(), GUIFunnel::new);
    }

    public static void registerRenderers(GeoNtmBlockEntityRenderers.Sink sink) {
        GeoNtmBlockEntityRenderers.regGem(sink, ModProductionBlockEntities.ROTARY_FURNACE.get(), RenderRotaryFurnace::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModProductionBlockEntities.THRESHER.get(), RenderThresher::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModProductionBlockEntities.MICROWAVE.get(), RenderMicrowave::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModProductionBlockEntities.CONVEYOR_PRESS.get(), RenderConveyorPress::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModProductionBlockEntities.FUNNEL.get(), RenderFunnel::new);
    }
}
