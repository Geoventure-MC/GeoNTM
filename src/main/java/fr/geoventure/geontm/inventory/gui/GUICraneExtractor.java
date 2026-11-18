package fr.geoventure.geontm.inventory.gui;

import fr.geoventure.geontm.packet.ControlEventPayload;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.gui.CompatContainerScreen;
import fr.geoventure.geocore.gui.GuiDraw;
import fr.geoventure.geocore.gui.MouseInput;
import fr.geoventure.geocore.net.GeoCoreNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.Optional;
import fr.geoventure.geontm.inventory.container.ContainerCraneExtractor;
import fr.geoventure.geontm.tileentity.network.TileEntityCraneExtractor;
import fr.geoventure.geontm.module.ModulePatternMatcher;

/** 1.7 {@code GUICraneExtractor} ("Conveyor Ejector"): 212 wide. */
public class GUICraneExtractor extends CompatContainerScreen<ContainerCraneExtractor> {

    private static final ResourceLocation TEXTURE = Ids.of("geontm", "textures/gui/storage/gui_crane_ejector.png");

    public GUICraneExtractor(ContainerCraneExtractor menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 212, 185);
    }

    @Override
    public void extractBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        GuiDraw.blit(graphics, TEXTURE, leftPos, topPos, 0F, 0F, imageWidth, imageHeight, 256, 256);

        TileEntityCraneExtractor ejector = menu.getTile();
        if (ejector == null) return;

        if (ejector.maxEject) GuiDraw.blit(graphics, TEXTURE, leftPos + 187, topPos + 34, 212F, 0F, 18, 18, 256, 256);

        if (ejector.isWhitelist) {
            GuiDraw.blit(graphics, TEXTURE, leftPos + 139, topPos + 33, 212F, 18F, 3, 6, 256, 256);
        } else {
            GuiDraw.blit(graphics, TEXTURE, leftPos + 139, topPos + 47, 212F, 18F, 3, 6, 256, 256);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        GuiDraw.text(graphics, font, title, imageWidth / 2 - font.width(title) / 2, 6, 0xFF404040, false);
        GuiDraw.text(graphics, font, playerInventoryTitle, 26, imageHeight - 96 + 2, 0xFF404040, false);
    }

    @Override
    protected void extractOwnTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        TileEntityCraneExtractor ejector = menu.getTile();
        if (ejector == null) return;

        if (menu.getCarried().isEmpty()) {
            for (int i = 0; i < 9; i++) {
                Slot slot = menu.slots.get(i);
                if (overSlot(slot, mouseX, mouseY) && ejector.matcher.modes[i] != null) {
                    tooltip(graphics, List.of(Component.literal("Right click to change").withStyle(ChatFormatting.RED),
                            Component.literal(ModulePatternMatcher.getLabel(ejector.matcher.modes[i]))), mouseX, mouseY - 30);
                    return;
                }
            }
        }

        if (legacyHover(187, 34, 18, 18, mouseX, mouseY)) {
            tooltip(graphics, List.of(Component.literal("Only take maximum possible: ").append(onOff(ejector.maxEject))), mouseX, mouseY);
        }
    }

    @Override
    protected boolean mouseClicked(MouseInput event, boolean doubleClick) {
        TileEntityCraneExtractor ejector = menu.getTile();
        if (ejector == null) return false;
        boolean handled = false;

        if (legacyHover(187, 34, 18, 18, event.x(), event.y())) {
            CompoundTag data = new CompoundTag();
            data.putBoolean("maxEject", true);
            click(ejector.getBlockPos(), data);
            handled = true;
        }

        if (legacyHover(128, 30, 14, 26, event.x(), event.y())) {
            CompoundTag data = new CompoundTag();
            data.putBoolean("whitelist", true);
            click(ejector.getBlockPos(), data);
            handled = true;
        }
        return handled;
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

    private void tooltip(GuiGraphics graphics, List<Component> lines, int mouseX, int mouseY) {
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
