package fr.geoventure.geontm.inventory.gui;

import fr.geoventure.geontm.inventory.container.ContainerDroneCrate;
import fr.geoventure.geontm.tileentity.network.TileEntityDroneCrate;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.gui.GuiDraw;
import fr.geoventure.geocore.gui.MouseInput;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/** 1.7 {@code GUIDroneCrate}: the cargo grid, the tank and the type/mode toggles at the right. */
public class GUIDroneCrate extends PneumaticScreenBase<ContainerDroneCrate> {

    private static final ResourceLocation TEXTURE = Ids.of("geontm", "textures/gui/storage/gui_crate_drone.png");

    public GUIDroneCrate(ContainerDroneCrate menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 185);
    }

    @Override
    public void extractBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        GuiDraw.blit(graphics, TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);

        TileEntityDroneCrate crate = menu.tile();
        if (crate == null) return;

        GuiDraw.blit(graphics, TEXTURE, leftPos + 151, topPos + 16, 194, crate.itemType ? 0 : 18, 18, 18, 256, 256);
        GuiDraw.blit(graphics, TEXTURE, leftPos + 151, topPos + 52, 176, crate.sendingMode ? 18 : 0, 18, 18, 256, 256);

        drawTank(graphics, crate.tank, 125, 51, 16, 34);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        GuiDraw.text(graphics, font, title, imageWidth / 2 - font.width(title) / 2, 6, 0xFF404040, false);
        GuiDraw.text(graphics, font, playerInventoryTitle, 8, imageHeight - 96 + 2, 0xFF404040, false);
    }

    @Override
    protected void extractOwnTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        TileEntityDroneCrate crate = menu.tile();
        if (crate == null) return;
        drawCustomInfoStat(graphics, mouseX, mouseY, 125, 17, 16, 34, GuiUtil.tankTooltip(crate.tank));
    }

    @Override
    protected boolean mouseClicked(MouseInput event, boolean doubleClick) {
        TileEntityDroneCrate crate = menu.tile();
        if (crate == null) return false;
        double x = event.x(), y = event.y();
        boolean handled = false;
        handled |= clickSendFlag(crate.getBlockPos(), x, y, 151, 16, 18, 18, "type");
        handled |= clickSendFlag(crate.getBlockPos(), x, y, 151, 52, 18, 18, "mode");
        return handled;
    }
}
