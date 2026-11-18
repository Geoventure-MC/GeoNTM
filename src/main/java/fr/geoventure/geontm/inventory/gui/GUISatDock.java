package fr.geoventure.geontm.inventory.gui;

import fr.geoventure.geontm.inventory.container.ContainerSatDock;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.gui.CompatContainerScreen;
import fr.geoventure.geocore.gui.GuiDraw;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 1.7 {@code GUISatDock}: {@code gui_sat_dock.png} at 176x186, the title centred on x=115, and one info panel at
 * (-7,36) hanging off the left edge.
 */
public class GUISatDock extends CompatContainerScreen<ContainerSatDock> {

    private static final ResourceLocation TEXTURE = Ids.of("geontm", "textures/gui/storage/gui_sat_dock.png");

    public GUISatDock(ContainerSatDock menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 186);
    }

    @Override
    public void extractBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        GuiDraw.blit(graphics, TEXTURE, leftPos, topPos, 0F, 0F, imageWidth, imageHeight, 256, 256);
        GuiUtil.drawInfoPanel(graphics, leftPos - 7, topPos + 36, 2);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        GuiDraw.text(graphics, font, title, 115 - font.width(title) / 2, 6, 0xFF404040, false);
        GuiDraw.text(graphics, font, playerInventoryTitle, 8, imageHeight - 96 + 2, 0xFF404040, false);
    }

    @Override
    protected void extractOwnTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        if (isHovering(-7, 36, 16, 16, mouseX, mouseY)) {
            List<Component> lines = new ArrayList<>();
            for (String line : I18n.get("desc.gui.satdock.desc").split("\\$")) lines.add(Component.literal(line));
            GuiDraw.tooltip(graphics, font, lines, Optional.empty(), leftPos + 1, topPos + 36 + 16);
        }
    }
}
