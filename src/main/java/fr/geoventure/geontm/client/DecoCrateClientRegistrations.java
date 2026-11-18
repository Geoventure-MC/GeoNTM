package fr.geoventure.geontm.client;

import fr.geoventure.geontm.blocks.generic.BlockBobble;
import fr.geoventure.geontm.blocks.generic.BlockSnowglobe;
import fr.geoventure.geontm.blocks.machine.RadioRec;
import fr.geoventure.geontm.client.render.GeoNtmBlockEntityRenderers;
import fr.geoventure.geontm.client.render.RenderBobble;
import fr.geoventure.geontm.client.render.RenderDecoBlock;
import fr.geoventure.geontm.client.render.RenderEmitter;
import fr.geoventure.geontm.client.render.RenderFileCabinet;
import fr.geoventure.geontm.client.render.RenderPlushie;
import fr.geoventure.geontm.client.render.RenderSnowglobe;
import fr.geoventure.geontm.client.render.RenderVendingMachine;
import fr.geoventure.geontm.inventory.ModDecoCrateMenus;
import fr.geoventure.geontm.inventory.gui.GUICrateTungsten;
import fr.geoventure.geontm.inventory.gui.GUIFileCabinet;
import fr.geoventure.geontm.inventory.gui.GUIRadioRec;
import fr.geoventure.geontm.inventory.gui.GUISafe;
import fr.geoventure.geontm.inventory.gui.GUIScreenBobble;
import fr.geoventure.geontm.inventory.gui.GUIScreenSnowglobe;
import fr.geoventure.geontm.tileentity.ModDecoCrateBlockEntities;
import fr.geoventure.geontm.tileentity.machine.TileEntityBroadcaster;
import fr.geoventure.geontm.tileentity.machine.TileEntityRadioRec;
import fr.geoventure.geontm.tileentity.machine.TileEntityRadiobox;
import net.minecraft.client.Minecraft;

/** Screens and block-entity renderers of the storage crates, decorative props and simple blocks. */
public final class DecoCrateClientRegistrations {

    private DecoCrateClientRegistrations() {}

    public static void registerScreens(GeoNtmClientRegistrations.ScreenSink sink) {
        sink.register(ModDecoCrateMenus.CRATE_TUNGSTEN.get(), GUICrateTungsten::new);
        sink.register(ModDecoCrateMenus.SAFE.get(), GUISafe::new);
        sink.register(ModDecoCrateMenus.FILE_CABINET.get(), GUIFileCabinet::new);
        // 1.7 opened these three client-side, with no container
        BlockBobble.OPEN_SCREEN = bobble -> Minecraft.getInstance().setScreen(new GUIScreenBobble(bobble));
        BlockSnowglobe.OPEN_SCREEN = globe -> Minecraft.getInstance().setScreen(new GUIScreenSnowglobe(globe));
        RadioRec.OPEN_SCREEN = radio -> Minecraft.getInstance().setScreen(new GUIRadioRec(radio));
    }

    public static void registerRenderers(GeoNtmBlockEntityRenderers.Sink sink) {
        GeoNtmBlockEntityRenderers.regGem(sink, ModDecoCrateBlockEntities.FILE_CABINET.get(), RenderFileCabinet::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModDecoCrateBlockEntities.BOBBLE.get(), RenderBobble::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModDecoCrateBlockEntities.PLUSHIE.get(), RenderPlushie::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModDecoCrateBlockEntities.SNOWGLOBE.get(), RenderSnowglobe::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModDecoCrateBlockEntities.VENDING_MACHINE.get(), RenderVendingMachine::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModDecoCrateBlockEntities.EMITTER.get(), RenderEmitter::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModDecoCrateBlockEntities.BROADCASTER.get(),
                () -> new RenderDecoBlock<TileEntityBroadcaster>(RenderDecoBlock.Kind.BROADCASTER));
        GeoNtmBlockEntityRenderers.regGem(sink, ModDecoCrateBlockEntities.RADIOREC.get(),
                () -> new RenderDecoBlock<TileEntityRadioRec>(RenderDecoBlock.Kind.RADIOREC));
        GeoNtmBlockEntityRenderers.regGem(sink, ModDecoCrateBlockEntities.RADIOBOX.get(),
                () -> new RenderDecoBlock<TileEntityRadiobox>(RenderDecoBlock.Kind.RADIOBOX));
    }
}
