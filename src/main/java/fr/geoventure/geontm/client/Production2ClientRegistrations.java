package fr.geoventure.geontm.client;

import fr.geoventure.geontm.client.render.GeoNtmBlockEntityRenderers;
import fr.geoventure.geontm.tileentity.ModProduction2BlockEntities;
import fr.geoventure.geontm.inventory.ModProduction2Menus;
import fr.geoventure.geontm.client.render.RenderAnnihilator;
import fr.geoventure.geontm.client.render.RenderExcavator;
import fr.geoventure.geontm.client.render.RenderOreSlopper;
import fr.geoventure.geontm.inventory.gui.GUIMachineAnnihilator;
import fr.geoventure.geontm.inventory.gui.GUIMachineDiFurnaceRTG;
import fr.geoventure.geontm.inventory.gui.GUIMachineExcavator;
import fr.geoventure.geontm.inventory.gui.GUIOreSlopper;
import fr.geoventure.geontm.inventory.gui.GUIRtgFurnace;

/** Screens and block-entity renderers for the production machines (group B). */
public final class Production2ClientRegistrations {
    private Production2ClientRegistrations() {}

    public static void registerScreens(GeoNtmClientRegistrations.ScreenSink sink) {
        sink.register(ModProduction2Menus.EXCAVATOR.get(), GUIMachineExcavator::new);
        sink.register(ModProduction2Menus.ORE_SLOPPER.get(), GUIOreSlopper::new);
        sink.register(ModProduction2Menus.ANNIHILATOR.get(), GUIMachineAnnihilator::new);
        sink.register(ModProduction2Menus.RTG_FURNACE.get(), GUIRtgFurnace::new);
        sink.register(ModProduction2Menus.DIFURNACE_RTG.get(), GUIMachineDiFurnaceRTG::new);
    }

    public static void registerRenderers(GeoNtmBlockEntityRenderers.Sink sink) {
        GeoNtmBlockEntityRenderers.regGem(sink, ModProduction2BlockEntities.EXCAVATOR.get(), RenderExcavator::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModProduction2BlockEntities.ORE_SLOPPER.get(), RenderOreSlopper::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModProduction2BlockEntities.ANNIHILATOR.get(), RenderAnnihilator::new);
    }
}
