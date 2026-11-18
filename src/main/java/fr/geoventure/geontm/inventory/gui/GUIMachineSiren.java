package fr.geoventure.geontm.inventory.gui;

import fr.geoventure.geontm.inventory.container.ContainerMachineSiren;
import fr.geoventure.geontm.items.machine.ItemCassette.TrackType;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineSiren;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.gui.CompatContainerScreen;
import fr.geoventure.geocore.gui.GuiDraw;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/** 1.7 {@code GUIMachineSiren}: {@code gui_siren.png}, 176x166; the cassette's title, type and range in its colour. */
public final class GUIMachineSiren extends CompatContainerScreen<ContainerMachineSiren> {

    private static final ResourceLocation TEXTURE = Ids.of("geontm", "textures/gui/gui_siren.png");

    public GUIMachineSiren(ContainerMachineSiren menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 166);
    }

    @Override
    public void extractBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        GuiDraw.blit(graphics, TEXTURE, leftPos, topPos, 0F, 0F, imageWidth, imageHeight, 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        GuiDraw.text(graphics, font, title, imageWidth / 2 - font.width(title) / 2, 6, 4210752, false);
        GuiDraw.text(graphics, font, playerInventoryTitle, 8, imageHeight - 96 + 2, 4210752, false);

        // Draw record meta here //
        TileEntityMachineSiren siren = menu.getSiren();
        if (siren == null) return;
        TrackType track = siren.getCurrentType();
        if (track != TrackType.NULL) {
            int color = track.getColor();
            GuiDraw.text(graphics, font, track.getTrackTitle(), 46, 28, color, false);
            GuiDraw.text(graphics, font, "Type: " + track.getType().name(), 46, 40, color, false);
            GuiDraw.text(graphics, font, "Volume: " + track.getVolume(), 46, 52, color, false);
        }
    }
}
