package fr.geoventure.geontm.inventory.gui;

import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.gui.CompatContainerScreen;
import fr.geoventure.geocore.gui.GuiDraw;
import fr.geoventure.geontm.inventory.container.ContainerStorageDrum;
import fr.geoventure.geontm.inventory.fluid.tank.FluidTankNTM;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;


/** Storage-drum screen, 1.7 {@code GUIStorageDrum} ({@code processing/gui_drum.png}, 176x234). */
public class GUIStorageDrum extends CompatContainerScreen<ContainerStorageDrum> {

    private static final ResourceLocation TEXTURE = Ids.of("geontm", "textures/gui/processing/gui_drum.png");

    public GUIStorageDrum(ContainerStorageDrum menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 234);
    }

    @Override
    public void extractBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        GuiDraw.blit(graphics, TEXTURE, leftPos, topPos, 0F, 0F, imageWidth, imageHeight, 256, 256);

        if (menu.getDrum() == null) return;
        FluidTankNTM liquid = menu.getDrum().liquid;
        FluidTankNTM gas = menu.getDrum().gas;
        int liquidHeight = liquid.getMaxFill() <= 0 ? 0 : liquid.getFill() * 106 / liquid.getMaxFill();
        if (liquidHeight > 0) {
            GuiDraw.blit(graphics, TEXTURE, leftPos + 17, topPos + 130 - liquidHeight,
                    176F, 106F - liquidHeight, 7, liquidHeight, 256, 256);
        }
        int gasHeight = gas.getMaxFill() <= 0 ? 0 : gas.getFill() * 106 / gas.getMaxFill();
        if (gasHeight > 0) {
            GuiDraw.blit(graphics, TEXTURE, leftPos + 152, topPos + 130 - gasHeight,
                    183F, 106F - gasHeight, 7, gasHeight, 256, 256);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        GuiDraw.text(graphics, font, title, imageWidth / 2 - font.width(title) / 2, 6, 4210752, false);
        GuiDraw.text(graphics, font, Component.translatable("container.inventory"), 8, imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void extractOwnTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        if (menu.getDrum() == null) return;
        if (GuiUtil.hover(mouseX, mouseY, leftPos + 16, topPos + 23, 9, 108)) {
            GuiUtil.tooltip(graphics, font, GuiUtil.tankTooltip(menu.getDrum().liquid), mouseX, mouseY);
        } else if (GuiUtil.hover(mouseX, mouseY, leftPos + 151, topPos + 23, 9, 108)) {
            GuiUtil.tooltip(graphics, font, GuiUtil.tankTooltip(menu.getDrum().gas), mouseX, mouseY);
        }
    }
}
