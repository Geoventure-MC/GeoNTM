package fr.geoventure.geontm.inventory.gui;

import fr.geoventure.geontm.inventory.container.ContainerAshpit;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.gui.CompatContainerScreen;
import fr.geoventure.geocore.gui.GuiDraw;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/** The unchanged 176x168 ashpit screen. */
public final class GUIAshpit extends CompatContainerScreen<ContainerAshpit> {

    private static final ResourceLocation TEXTURE =
            Ids.of("geontm", "textures/gui/machine/gui_ashpit.png");

    public GUIAshpit(ContainerAshpit menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 168);
    }

    /** 1.7 {@code GUIAshpit.drawGuiContainerForegroundLayer}: centred title at y 6, "Inventory" at (8, ySize - 94). */
    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        GuiDraw.text(graphics, this.font, this.title, this.imageWidth / 2 - this.font.width(this.title) / 2, 6, 4210752, false);
        GuiDraw.text(graphics, this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    public void extractBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        GuiDraw.blit(graphics, TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F,
                this.imageWidth, this.imageHeight, 256, 256);
    }
}
