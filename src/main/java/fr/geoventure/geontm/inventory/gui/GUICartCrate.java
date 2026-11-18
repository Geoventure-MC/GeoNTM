package fr.geoventure.geontm.inventory.gui;

import fr.geoventure.geontm.inventory.container.MenuCrate;
import fr.geoventure.geontm.tileentity.machine.storage.CrateType;
import fr.geoventure.geocore.gui.CompatContainerScreen;
import fr.geoventure.geocore.gui.GuiDraw;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/** The crate minecart's screen: {@link GUICrate}'s drawing over the cart's {@link MenuCrate}. */
public class GUICartCrate extends CompatContainerScreen<MenuCrate> {

    private final CrateType type;

    public GUICartCrate(MenuCrate menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, menu.getCrateType().guiWidth, menu.getCrateType().guiHeight);
        this.type = menu.getCrateType();
    }

    @Override
    public void extractBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        GuiDraw.blit(graphics, type.texture, this.leftPos, this.topPos, 0.0F, 0.0F,
                this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        GuiDraw.text(graphics, this.font, this.title, (this.imageWidth - this.font.width(this.title)) / 2, this.titleLabelY,
                0xFF000000 | type.titleColor, false);
        GuiDraw.text(graphics, this.font, this.playerInventoryTitle, type.inventoryLabelX, this.imageHeight - 96 + 2,
                0xFF000000 | type.inventoryLabelColor, false);
    }
}
