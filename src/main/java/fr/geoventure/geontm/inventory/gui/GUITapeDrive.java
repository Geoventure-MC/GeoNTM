package fr.geoventure.geontm.inventory.gui;

import fr.geoventure.geontm.inventory.container.ContainerTapeDrive;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.gui.CompatContainerScreen;
import fr.geoventure.geocore.gui.GuiDraw;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/** 1.7 {@code GUITapeDrive}. */
public class GUITapeDrive extends CompatContainerScreen<ContainerTapeDrive> {

    private static final ResourceLocation TEXTURE = Ids.of("geontm", "textures/gui/machine/gui_tape_drive.png");

    public GUITapeDrive(ContainerTapeDrive menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 186);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        GuiDraw.text(graphics, this.font, this.title, this.imageWidth / 2 - this.font.width(this.title) / 2, 6, 0xffffff, false);
        GuiDraw.text(graphics, this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    public void extractBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        GuiDraw.blit(graphics, TEXTURE, leftPos, topPos, 0.0F, 0.0F, imageWidth, imageHeight, 256, 256);
    }
}
