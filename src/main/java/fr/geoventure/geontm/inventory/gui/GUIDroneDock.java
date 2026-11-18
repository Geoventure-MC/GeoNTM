package fr.geoventure.geontm.inventory.gui;

import fr.geoventure.geontm.inventory.container.ContainerDroneDock;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.gui.GuiDraw;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/** 1.7 {@code GUIDroneDock}: a plain 3x3 drone bay. */
public class GUIDroneDock extends PneumaticScreenBase<ContainerDroneDock> {

    private static final ResourceLocation TEXTURE = Ids.of("geontm", "textures/gui/storage/gui_drone_dock.png");

    public GUIDroneDock(ContainerDroneDock menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 185);
    }

    @Override
    public void extractBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        GuiDraw.blit(graphics, TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        GuiDraw.text(graphics, font, title, imageWidth / 2 - font.width(title) / 2, 6, 0xFF404040, false);
        GuiDraw.text(graphics, font, playerInventoryTitle, 8, imageHeight - 96 + 2, 0xFF404040, false);
    }
}
