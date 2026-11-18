package fr.geoventure.geontm.client;

import fr.geoventure.geontm.blocks.ModEnergyNetContent;
import fr.geoventure.geontm.client.render.GeoNtmBlockEntityRenderers;
import fr.geoventure.geontm.client.render.RenderInfoSystem;
import fr.geoventure.geontm.client.render.RenderPipeAnchor;
import fr.geoventure.geontm.inventory.ModHeldCrateMenus;
import fr.geoventure.geontm.inventory.gui.GUICartCrate;
import fr.geoventure.geontm.inventory.gui.GUICrateTungsten;
import fr.geoventure.geontm.inventory.gui.GUISafe;
import fr.geoventure.geontm.items.tool.ItemWrench;
import fr.geoventure.geontm.tileentity.ModFluidNetBlockEntities;
import fr.geoventure.geocore.client.ClientTicks;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Client side of the gap batch B systems: the screens of crates opened from the hand, the pipe anchor renderer
 * (anchor + pipelines) and the pipe wrench's distance readout.
 */
public final class GapsBClientRegistrations {

    private GapsBClientRegistrations() {}

    /** 1.7 {@code ServerProxy.ID_WRENCH}. */
    private static final int ID_WRENCH = 13;
    private static boolean ticking;

    public static void registerScreens(GeoNtmClientRegistrations.ScreenSink sink) {
        sink.register(ModHeldCrateMenus.CRATE_HELD.get(), GUICartCrate::new);
        sink.register(ModHeldCrateMenus.CRATE_TUNGSTEN_HELD.get(), GUICrateTungsten::new);
        sink.register(ModHeldCrateMenus.SAFE_HELD.get(), GUISafe::new);
        if (!ticking) {
            ticking = true;
            ClientTicks.onEndTick(GapsBClientRegistrations::tick);
        }
    }

    public static void registerRenderers(GeoNtmBlockEntityRenderers.Sink sink) {
        GeoNtmBlockEntityRenderers.regGem(sink, ModFluidNetBlockEntities.ANCHOR.get(), () -> new RenderPipeAnchor<>("pipe_anchor"));
        GeoNtmBlockEntityRenderers.regGem(sink, ModFluidNetBlockEntities.ANCHOR_EXHAUST.get(), () -> new RenderPipeAnchor<>("pipe_anchor_exhaust"));
    }

    /** 1.7 {@code ItemWrench.onUpdate} (client): every wrench with a stored start shows "name: Nm". */
    private static void tick() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!(stack.getItem() instanceof ItemWrench)) continue;
            BlockPos start = ModEnergyNetContent.WIRE_START.get(stack).orElse(null);
            if (start == null) continue;
            double dx = player.getX() - start.getX(), dy = player.getY() - start.getY(), dz = player.getZ() - start.getZ();
            RenderInfoSystem.displayTooltip(Component.literal(stack.getHoverName().getString() + ": "
                    + ((int) Math.sqrt(dx * dx + dy * dy + dz * dz)) + "m"), 0, ID_WRENCH);
        }
    }
}
