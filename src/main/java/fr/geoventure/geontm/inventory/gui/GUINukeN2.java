package fr.geoventure.geontm.inventory.gui;

import fr.geoventure.geontm.inventory.container.ContainerNukeN2;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.gui.CompatContainerScreen;
import fr.geoventure.geocore.gui.GuiDraw;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/** N² schematic screen, ported from 1.7 {@code GUINukeN2}: twelve slots and the stacked charge-fill indicator. */
public class GUINukeN2 extends CompatContainerScreen<ContainerNukeN2> {

    private static final ResourceLocation TEXTURE = Ids.of("geontm", "textures/gui/weapon/n2schematic.png");

    public GUINukeN2(ContainerNukeN2 menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 222);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        GuiDraw.text(graphics, font, title, imageWidth / 2 - font.width(title) / 2, 6, 4210752, false);
        GuiDraw.text(graphics, font, playerInventoryTitle, 8, imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    public void extractBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int count = this.menu.countCharges();
        GuiDraw.blit(graphics, TEXTURE, leftPos, topPos, 0F, 0F, imageWidth, imageHeight, 256, 256);
        if (count > 0) {
            GuiDraw.blit(graphics, TEXTURE, leftPos + 35, topPos + 120 - 6 * count,
                    176F, 0F, 34, 6 * count, 256, 256);
        }
    }
}
