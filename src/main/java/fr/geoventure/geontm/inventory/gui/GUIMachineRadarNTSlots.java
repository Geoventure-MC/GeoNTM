package fr.geoventure.geontm.inventory.gui;

import fr.geoventure.geontm.blocks.machine.MachineRadar;
import fr.geoventure.geontm.inventory.container.ContainerMachineRadarNT;
import fr.geoventure.geontm.main.MainRegistry;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineRadarNT;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.gui.CompatContainerScreen;
import fr.geoventure.geocore.gui.GuiDraw;
import fr.geoventure.geocore.gui.MouseInput;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;
import java.util.Optional;

/**
 * 1.7 {@code GUIMachineRadarNTSlots}: the radar's link slots, {@code gui_radar_link.png} at 176x184, with the
 * power bar at (8,64) and the "switch view" button at (5,5) that closes the menu on the server and reopens the
 * map on the client (1.7 sent the close packet itself so the cursor stayed where it was).
 */
public class GUIMachineRadarNTSlots extends CompatContainerScreen<ContainerMachineRadarNT> {

    private static final ResourceLocation TEXTURE = Ids.of("geontm", "textures/gui/machine/gui_radar_link.png");

    public GUIMachineRadarNTSlots(ContainerMachineRadarNT menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 184);
    }

    private boolean checkClick(double x, double y, int left, int top, int sizeX, int sizeY) {
        return leftPos + left <= x && leftPos + left + sizeX > x && topPos + top < y && topPos + top + sizeY >= y;
    }

    @Override
    protected boolean mouseClicked(MouseInput event, boolean doubleClick) {
        TileEntityMachineRadarNT radar = menu.getRadar();
        if (radar != null && checkClick(event.x(), event.y(), 5, 5, 8, 8)) {
            Minecraft mc = Minecraft.getInstance();
            mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            if (mc.player != null) mc.player.closeContainer();
            MachineRadar.OPEN_SCREEN.accept(radar);
            return true;
        }
        return false;
    }

    @Override
    protected void extractOwnTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        if (checkClick(mouseX, mouseY, 5, 5, 8, 8)) {
            GuiDraw.tooltip(graphics, font, List.of(Component.literal(I18n.get("radar.toggleGui"))), Optional.empty(), mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        String name = MainRegistry.polaroidID == 11 ? "Reda" : title.getString();
        GuiDraw.text(graphics, font, name, imageWidth / 2 - font.width(name) / 2, 6, 0xFF404040, false);
        GuiDraw.text(graphics, font, playerInventoryTitle, 8, imageHeight - 96 + 2, 0xFF404040, false);
    }

    @Override
    public void extractBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        GuiDraw.blit(graphics, TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);

        TileEntityMachineRadarNT radar = menu.getRadar();
        if (radar != null && radar.power > 0) {
            int i = (int) (radar.power * 160 / TileEntityMachineRadarNT.maxPower);
            GuiDraw.blit(graphics, TEXTURE, leftPos + 8, topPos + 64, 0, 185, i, 16, 256, 256);
        }
    }
}
