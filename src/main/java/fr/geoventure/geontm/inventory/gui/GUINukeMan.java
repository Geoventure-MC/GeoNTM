package fr.geoventure.geontm.inventory.gui;

import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.gui.CompatContainerScreen;
import fr.geoventure.geocore.gui.GuiDraw;
import fr.geoventure.geontm.inventory.container.ContainerNukeMan;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * Client screen for the Fat Man ({@code nuke_man}), ported from 1.7 {@code GUINukeMan}. The lens "installed"
 * overlays and the ready indicator are drawn from the right-hand side of the schematic texture, gated by the
 * container's arming state (read off the synced BE); the info panel on the left lists the required parts.
 */
public class GUINukeMan extends CompatContainerScreen<ContainerNukeMan> {

    private static final ResourceLocation TEXTURE = Ids.of("geontm", "textures/gui/weapon/fatmanschematic.png");

    public GUINukeMan(ContainerNukeMan menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 166);
    }

    @Override
    public void extractBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int x = this.leftPos;
        int y = this.topPos;
        GuiDraw.blit(graphics, TEXTURE, x, y, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

        if (this.menu.exp1()) GuiDraw.blit(graphics, TEXTURE, x + 82, y + 19, 176.0F, 0.0F, 24, 24, 256, 256);
        if (this.menu.exp2()) GuiDraw.blit(graphics, TEXTURE, x + 106, y + 19, 200.0F, 0.0F, 24, 24, 256, 256);
        if (this.menu.exp3()) GuiDraw.blit(graphics, TEXTURE, x + 82, y + 43, 176.0F, 24.0F, 24, 24, 256, 256);
        if (this.menu.exp4()) GuiDraw.blit(graphics, TEXTURE, x + 106, y + 43, 200.0F, 24.0F, 24, 24, 256, 256);
        if (this.menu.isReady()) GuiDraw.blit(graphics, TEXTURE, x + 134, y + 35, 176.0F, 48.0F, 16, 16, 256, 256);
        GuiUtil.drawInfoPanel(graphics, x - 16, y + 16, 2);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        GuiDraw.text(graphics, this.font, this.title, this.imageWidth / 2 - this.font.width(this.title) / 2, 6, 4210752, false);
        GuiDraw.text(graphics, this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void extractOwnTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        // 1.7 drawCustomInfoStat: the text is anchored at (guiLeft - 8, guiTop + 32), not at the mouse.
        if (this.isHovering(-16, 16, 16, 16, mouseX, mouseY)) {
            GuiDraw.tooltip(graphics, this.font, GUINukeBoy.lines("desc.gui.nukeMan.desc"), Optional.empty(),
                    this.leftPos - 8, this.topPos + 16 + 16);
        }
    }
}
