package fr.geoventure.geontm.inventory.gui;

import fr.geoventure.geontm.inventory.container.ContainerMachineSatLinker;
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
 * 1.7 {@code GUIMachineSatLinker}: {@code gui_sat_linker.png} at 176x186, a centred dark-grey title, and two info
 * panels at (12,28) and (12,44) explaining the copy and randomise slots.
 */
public class GUIMachineSatLinker extends CompatContainerScreen<ContainerMachineSatLinker> {

    private static final ResourceLocation TEXTURE = Ids.of("geontm", "textures/gui/machine/gui_sat_linker.png");

    public GUIMachineSatLinker(ContainerMachineSatLinker menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 186);
    }

    @Override
    public void extractBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        GuiDraw.blit(graphics, TEXTURE, leftPos, topPos, 0F, 0F, imageWidth, imageHeight, 256, 256);

        GuiUtil.drawInfoPanel(graphics, leftPos + 12, topPos + 28, 2);
        GuiUtil.drawInfoPanel(graphics, leftPos + 12, topPos + 28 + 16, 3);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        GuiDraw.text(graphics, font, title, imageWidth / 2 - font.width(title) / 2, 6, 0xFF404040, false);
        GuiDraw.text(graphics, font, playerInventoryTitle, 8, imageHeight - 96 + 2, 0xFF404040, false);
    }

    @Override
    protected void extractOwnTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        // 1.7 drawCustomInfoStat: the text is anchored at (guiLeft + 20, the panel's bottom), not at the cursor
        if (isHovering(12, 28, 16, 16, mouseX, mouseY)) {
            GuiDraw.tooltip(graphics, font, lines(I18n.get("desc.gui.satlinker.chip")), Optional.empty(),
                    leftPos + 20, topPos + 28 + 16);
            return;
        }
        if (isHovering(12, 28 + 16, 16, 16, mouseX, mouseY)) {
            GuiDraw.tooltip(graphics, font, lines(I18n.get("desc.gui.satlinker.random")), Optional.empty(),
                    leftPos + 20, topPos + 28 + 32);
        }
    }

    /** 1.7 {@code I18nUtil.resolveKeyArray}: the string split on {@code $}. */
    private static List<Component> lines(String text) {
        List<Component> lines = new ArrayList<>();
        for (String line : text.split("\\$")) lines.add(Component.literal(line));
        return lines;
    }
}
