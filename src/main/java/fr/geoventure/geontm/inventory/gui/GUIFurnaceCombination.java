package fr.geoventure.geontm.inventory.gui;

import fr.geoventure.geontm.inventory.container.ContainerFurnaceCombination;
import fr.geoventure.geontm.tileentity.machine.TileEntityFurnaceCombination;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.gui.CompatContainerScreen;
import fr.geoventure.geocore.gui.GuiDraw;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/** Screen for the combination furnace, preserving the 176x186 legacy layout. */
public class GUIFurnaceCombination extends CompatContainerScreen<ContainerFurnaceCombination> {

    private static final ResourceLocation TEXTURE = Ids.of("geontm", "textures/gui/processing/gui_furnace_combination.png");

    public GUIFurnaceCombination(ContainerFurnaceCombination menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 186);
    }

    /** 1.7 {@code GUIFurnaceCombo.drawGuiContainerForegroundLayer}: centred title at y 6, "Inventory" at (8, ySize - 94). */
    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        GuiDraw.text(graphics, font, title, imageWidth / 2 - font.width(title) / 2, 6, 4210752, false);
        GuiDraw.text(graphics, font, playerInventoryTitle, 8, imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    public void extractBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        GuiDraw.blit(graphics, TEXTURE, leftPos, topPos, 0F, 0F, imageWidth, imageHeight, 256, 256);
        TileEntityFurnaceCombination furnace = menu.getFurnace();
        if (furnace == null) return;

        int progress = furnace.getProgressScaled(38);
        if (progress > 0) GuiDraw.blit(graphics, TEXTURE, leftPos + 45, topPos + 37, 176F, 0F, progress, 5, 256, 256);
        int heat = furnace.getHeatScaled(37);
        if (heat > 0) GuiDraw.blit(graphics, TEXTURE, leftPos + 45, topPos + 46, 176F, 5F, heat, 5, 256, 256);

        // 1.7: furnace.tank.renderTank(guiLeft + 118, guiTop + 70, z, 16, 52), tinted and bottom-anchored.
        Production2Gui.drawTank(graphics, furnace.tank, leftPos + 118, topPos + 70, 16, 52);
    }

    /** 1.7 {@code GUIFurnaceCombo.drawScreen}: tank readout, then progress and heat stats ("TU" unspaced, as 1.7). */
    @Override
    protected void extractOwnTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        TileEntityFurnaceCombination furnace = menu.getFurnace();
        if (furnace == null) return;
        if (in17(118, 18, 16, 52, mouseX, mouseY)) {
            GuiDraw.tooltip(graphics, font, GuiUtil.tankTooltip(furnace.tank), Optional.empty(), mouseX, mouseY);
        } else if (in17(44, 36, 39, 7, mouseX, mouseY)) {
            GuiDraw.tooltip(graphics, font, List.of(Component.literal(String.format(Locale.US, "%,d", furnace.progress)
                    + " / " + String.format(Locale.US, "%,d", TileEntityFurnaceCombination.processTime) + "TU")),
                    Optional.empty(), mouseX, mouseY);
        } else if (in17(44, 45, 39, 7, mouseX, mouseY)) {
            GuiDraw.tooltip(graphics, font, List.of(Component.literal(String.format(Locale.US, "%,d", furnace.heat)
                    + " / " + String.format(Locale.US, "%,d", TileEntityFurnaceCombination.maxHeat) + "TU")),
                    Optional.empty(), mouseX, mouseY);
        }
    }

    /** The 1.7 GUI hit test ({@code drawCustomInfoStat}/{@code renderTankInfo}): x <= mouse < x + w, y < mouse <= y + h. */
    private boolean in17(int x, int y, int w, int h, int mouseX, int mouseY) {
        int mx = mouseX - leftPos, my = mouseY - topPos;
        return x <= mx && x + w > mx && y < my && y + h >= my;
    }
}
