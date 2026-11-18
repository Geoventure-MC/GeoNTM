package fr.geoventure.geontm.inventory.gui;

import fr.geoventure.geontm.packet.ControlEventPayload;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.gui.CompatContainerScreen;
import fr.geoventure.geocore.gui.GuiDraw;
import fr.geoventure.geocore.gui.MouseInput;
import fr.geoventure.geocore.net.GeoCoreNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.Optional;
import fr.geoventure.geontm.inventory.container.ContainerCraneUnboxer;

/** 1.7 {@code GUICraneUnboxer}. */
public class GUICraneUnboxer extends CompatContainerScreen<ContainerCraneUnboxer> {

    private static final Identifier TEXTURE = Ids.of("geontm", "textures/gui/storage/gui_crane_unboxer.png");

    public GUICraneUnboxer(ContainerCraneUnboxer menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 185);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        GuiDraw.blit(graphics, TEXTURE, leftPos, topPos, 0F, 0F, imageWidth, imageHeight, 256, 256);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        GuiDraw.text(graphics, font, title, imageWidth / 2 - font.width(title) / 2, 6, 0xFF404040, false);
        GuiDraw.text(graphics, font, playerInventoryTitle, 8, imageHeight - 96 + 2, 0xFF404040, false);
    }

    @Override
    protected void extractOwnTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
    }

    /** The old GUI used a closed lower bound and an open upper bound on x, the reverse on y. */
    private boolean legacyHover(int x, int y, int width, int height, double mouseX, double mouseY) {
        return mouseX >= leftPos + x && mouseX < leftPos + x + width
                && mouseY > topPos + y && mouseY <= topPos + y + height;
    }

    private void click(BlockPos pos, CompoundTag data) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        GeoCoreNetwork.sendToServer(ControlEventPayload.CODEC, new ControlEventPayload(pos, data));
    }

    private void tooltip(GuiGraphicsExtractor graphics, List<Component> lines, int mouseX, int mouseY) {
        GuiDraw.tooltip(graphics, font, lines, Optional.empty(), mouseX, mouseY);
    }

    private static Component onOff(boolean on) {
        return on ? Component.literal("ON").withStyle(ChatFormatting.GREEN) : Component.literal("OFF").withStyle(ChatFormatting.RED);
    }

    /** 1.7 {@code isMouseOverSlot}. */
    private boolean overSlot(Slot slot, double mouseX, double mouseY) {
        return isHovering(slot.x, slot.y, 16, 16, mouseX, mouseY);
    }
}
