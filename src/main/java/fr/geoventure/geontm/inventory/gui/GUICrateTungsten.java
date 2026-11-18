package fr.geoventure.geontm.inventory.gui;

import fr.geoventure.geontm.blocks.generic.BlockStorageCrate;
import fr.geoventure.geontm.inventory.container.ContainerCrateTungsten;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.gui.CompatContainerScreen;
import fr.geoventure.geocore.gui.GuiDraw;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * 1.7 {@code GUICrateTungsten}: 176x168, the hot sheet while a laser heats the crate, white labels (the sheet is
 * dark).
 */
public class GUICrateTungsten extends CompatContainerScreen<ContainerCrateTungsten> {

    private static final ResourceLocation TEXTURE = Ids.of("geontm", "textures/gui/storage/gui_crate_tungsten.png");
    private static final ResourceLocation TEXTURE_HOT = Ids.of("geontm", "textures/gui/storage/gui_crate_tungsten_hot.png");

    public GUICrateTungsten(ContainerCrateTungsten menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 168);
    }

    @Override
    public void extractBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        boolean hot = menu.tile() != null && menu.tile().getBlockState().hasProperty(BlockStorageCrate.HOT)
                && menu.tile().getBlockState().getValue(BlockStorageCrate.HOT);
        GuiDraw.blit(graphics, hot ? TEXTURE_HOT : TEXTURE, leftPos, topPos, 0.0F, 0.0F, imageWidth, imageHeight, 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        GuiDraw.text(graphics, font, title, imageWidth / 2 - font.width(title) / 2, 6, 0xFFFFFFFF, false);
        GuiDraw.text(graphics, font, playerInventoryTitle, 8, imageHeight - 96 + 2, 0xFFFFFFFF, false);
    }
}
