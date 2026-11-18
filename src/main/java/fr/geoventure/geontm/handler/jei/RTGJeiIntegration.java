package fr.geoventure.geontm.handler.jei;

import fr.geoventure.geontm.blocks.machine.ModMachineBlocks;
import fr.geoventure.geontm.inventory.ModRtgMenus;
import fr.geoventure.geontm.inventory.container.ContainerMachineRTG;
import fr.geoventure.geontm.inventory.gui.GUIMachineRTG;
import fr.geoventure.geontm.inventory.gui.RtgGuiLayout;
import fr.geoventure.geocore.viewer.ViewerPlugin;
import net.minecraft.world.item.ItemStack;

/** One-call hooks that keep every RTG-specific recipe-viewer registration out of the shared plugin tables. */
public final class RTGJeiIntegration {
    private RTGJeiIntegration() {}

    public static void registerCategory(ViewerPlugin.Categories registration) {
        registration.add(new RTGRecipeHandler());
    }

    public static void registerRecipes(ViewerPlugin.Recipes registration) {
        registration.add(RTGRecipeHandler.TYPE, RTGRecipeHandler.buildRecipes());
    }

    public static void registerCatalyst(ViewerPlugin.Stations registration) {
        registration.add(RTGRecipeHandler.TYPE, new ItemStack(ModMachineBlocks.machine_rtg_grey.get()));
    }

    public static void registerGuiHandler(ViewerPlugin.ClickAreas registration) {
        // The old registration covered the heat gauge. Keep that gauge's legacy tooltip intact and use
        // the empty separator beside it for JEI's "Show Recipes" affordance instead.
        registration.add(GUIMachineRTG.class,
                RtgGuiLayout.RECIPE_CLICK_X, RtgGuiLayout.RECIPE_CLICK_Y,
                RtgGuiLayout.RECIPE_CLICK_WIDTH, RtgGuiLayout.RECIPE_CLICK_HEIGHT,
                RTGRecipeHandler.TYPE);
    }

    public static void registerTransfer(ViewerPlugin.Transfers registration) {
        registration.add(ContainerMachineRTG.class, ModRtgMenus.MACHINE_RTG.get(),
                RTGRecipeHandler.TYPE, 0, 15, 15, 36);
    }
}
