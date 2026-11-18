package fr.geoventure.geontm.client;

import fr.geoventure.geontm.blocks.machine.rbmk.RBMKGraph;
import fr.geoventure.geontm.blocks.machine.rbmk.RBMKIndicator;
import fr.geoventure.geontm.blocks.machine.rbmk.RBMKKeyPad;
import fr.geoventure.geontm.blocks.machine.rbmk.RBMKLever;
import fr.geoventure.geontm.blocks.machine.rbmk.RBMKNumitron;
import fr.geoventure.geontm.inventory.ModRBMKMenus;
import net.minecraft.client.Minecraft;
import fr.geoventure.geontm.inventory.gui.*;

/** RBMK client registrations stay with the RBMK feature instead of growing the global registry. */
public final class RBMKClientScreens {
    private RBMKClientScreens() {}
    public static void register(GeoNtmClientRegistrations.ScreenSink sink) {
        sink.register(ModRBMKMenus.RBMK_ROD.get(), ScreenRBMKRod::new);
        sink.register(ModRBMKMenus.RBMK_CONTROL.get(), ScreenRBMKControl::new);
        sink.register(ModRBMKMenus.RBMK_CONTROL_AUTO.get(), ScreenRBMKControlAuto::new);
        sink.register(ModRBMKMenus.RBMK_BOILER.get(), ScreenRBMKBoiler::new);
        sink.register(ModRBMKMenus.RBMK_STORAGE.get(), ScreenRBMKStorage::new);
        sink.register(ModRBMKMenus.RBMK_HEATER.get(), ScreenRBMKHeater::new);
        sink.register(ModRBMKMenus.RBMK_OUTGASSER.get(), ScreenRBMKOutgasser::new);
        sink.register(ModRBMKMenus.RBMK_CONSOLE.get(), ScreenRBMKConsole::new);
        sink.register(ModRBMKMenus.RBMK_AUTOLOADER.get(), ScreenRBMKAutoloader::new);
        // The ROR panels are screwdriver-opened client screens, not menus.
        RBMKKeyPad.OPEN_SCREEN = panel -> Minecraft.getInstance().setScreen(new ScreenRBMKKeyPad(panel));
        RBMKLever.OPEN_SCREEN = panel -> Minecraft.getInstance().setScreen(new ScreenRBMKLever(panel));
        RBMKNumitron.OPEN_SCREEN = panel -> Minecraft.getInstance().setScreen(new ScreenRBMKNumitron(panel));
        RBMKGraph.OPEN_SCREEN = panel -> Minecraft.getInstance().setScreen(new ScreenRBMKGraph(panel));
        RBMKIndicator.OPEN_SCREEN = panel -> Minecraft.getInstance().setScreen(new ScreenRBMKIndicator(panel));
    }
}
