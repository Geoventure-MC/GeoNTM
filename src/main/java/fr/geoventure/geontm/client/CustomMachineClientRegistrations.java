package fr.geoventure.geontm.client;

import fr.geoventure.geontm.client.render.GeoNtmBlockEntityRenderers;
import fr.geoventure.geontm.client.render.RenderCustomMachine;
import fr.geoventure.geontm.config.CustomMachineConfigJSON;
import fr.geoventure.geontm.inventory.ModCustomMachineMenus;
import fr.geoventure.geontm.inventory.gui.GUIMachineCustom;
import fr.geoventure.geontm.tileentity.ModCustomMachineBlockEntities;
import net.minecraft.client.Minecraft;

/** The custom machine controller's screen and structure-ghost renderer, and the client language for its names. */
public final class CustomMachineClientRegistrations {

    private CustomMachineClientRegistrations() {}

    public static void registerScreens(GeoNtmClientRegistrations.ScreenSink sink) {
        sink.register(ModCustomMachineMenus.CUSTOM_MACHINE.get(), GUIMachineCustom::new);
        // 1.7 MainRegistry.proxy.getLanguageCode(), read by the controller item's name and the GUI title
        CustomMachineConfigJSON.languageCode = () -> Minecraft.getInstance().getLanguageManager().getSelected();
    }

    public static void registerRenderers(GeoNtmBlockEntityRenderers.Sink sink) {
        sink.register(ModCustomMachineBlockEntities.CUSTOM_MACHINE.get(), ctx -> new RenderCustomMachine());
    }
}
