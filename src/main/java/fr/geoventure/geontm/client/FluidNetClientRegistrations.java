package fr.geoventure.geontm.client;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.client.render.GeoNtmBlockEntityRenderers;
import fr.geoventure.geontm.client.render.MachineObjRenderer;
import fr.geoventure.geontm.client.render.MachineObjRenderer.ModelDef;
import fr.geoventure.geontm.tileentity.ModFluidNetBlockEntities;
import fr.geoventure.geontm.inventory.ModFluidNetMenus;
import fr.geoventure.geontm.blocks.network.FlowControlPump;
import fr.geoventure.geontm.tileentity.machine.storage.TileEntityBarrel;
import fr.geoventure.geontm.tileentity.network.TileEntityFluidPump;
import fr.geoventure.geontm.client.render.BarrelRenderer;
import fr.geoventure.geontm.client.render.FoundryTankRenderer;
import fr.geoventure.geontm.client.render.RenderFlowControlPump;
import fr.geoventure.geontm.inventory.gui.GUIBarrel;
import fr.geoventure.geontm.inventory.gui.GUIPump;
import fr.geoventure.geocore.Ids;
import net.minecraft.client.Minecraft;

/** Client hooks for the fluid-network family: the barrel screen, the pump screen and the block renderers. */
public final class FluidNetClientRegistrations {

    private FluidNetClientRegistrations() {}

    public static void registerScreens(GeoNtmClientRegistrations.ScreenSink sink) {
        sink.register(ModFluidNetMenus.BARREL.get(), GUIBarrel::new);
        FlowControlPump.OPEN_SCREEN = pump -> Minecraft.getInstance().setScreen(new GUIPump(pump));
    }

    public static void registerRenderers(GeoNtmBlockEntityRenderers.Sink sink) {
        GeoNtmBlockEntityRenderers.regGem(sink, ModFluidNetBlockEntities.BARREL.get(), BarrelRenderer<TileEntityBarrel>::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModFluidNetBlockEntities.FOUNDRY_TANK.get(), FoundryTankRenderer::new);
        // The deprecated fluid pump: fluid_diode.obj; 1.7 yaw map N=180, E=90, S=0, W=270 = the standard map + 180.
        GeoNtmBlockEntityRenderers.regGem(sink, ModFluidNetBlockEntities.PUMP.get(),
                () -> new MachineObjRenderer<TileEntityFluidPump>(180F, 1F,
                        new ModelDef(Ids.of(Constants.MOD_ID, "models/network/fluid_diode.obj"),
                                Ids.of(Constants.MOD_ID, "textures/models/network/fluid_diode.png"))));
        GeoNtmBlockEntityRenderers.regGem(sink, ModFluidNetBlockEntities.FLOW_CONTROL_PUMP.get(), RenderFlowControlPump::new);
    }
}
