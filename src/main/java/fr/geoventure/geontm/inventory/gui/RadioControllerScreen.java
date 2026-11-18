package fr.geoventure.geontm.inventory.gui;

import fr.geoventure.geontm.api.redstoneoverradio.IRORInfo;
import fr.geoventure.geontm.packet.ControlEventPayload;
import fr.geoventure.geontm.inventory.container.RadioControllerMenu;
import fr.geoventure.geontm.tileentity.network.TileEntityRadioTorchController;
import fr.geoventure.geontm.tileentity.network.RTTYSystem;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.gui.CompatContainerScreen;
import fr.geoventure.geocore.gui.GuiDraw;
import fr.geoventure.geocore.gui.MouseInput;
import fr.geoventure.geocore.net.GeoCoreNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Faithful 256x42 ROR controller GUI. */
public final class RadioControllerScreen extends CompatContainerScreen<RadioControllerMenu> {
    private static final Identifier TEXTURE = Ids.of("geontm", "textures/gui/machine/gui_rtty_controller.png");
    private EditBox frequency;
    private boolean polling;

    public RadioControllerScreen(RadioControllerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 256, 42);
    }

    @Override
    protected void init() {
        super.init();
        TileEntityRadioTorchController controller = menu.controller();
        polling = controller != null && controller.polling;
        frequency = new EditBox(font, leftPos + 29, topPos + 21, 82, 14, Component.empty());
        frequency.setBordered(false);
        frequency.setTextColor(0xFF00FF00);
        frequency.setTextColorUneditable(0xFF00FF00);
        frequency.setMaxLength(RTTYSystem.MAX_CHAN_LENGTH);
        frequency.setValue(controller == null ? "" : controller.channel);
        addRenderableWidget(frequency);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        GuiDraw.blit(graphics, TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
        if (polling) GuiDraw.blit(graphics, TEXTURE, leftPos + 173, topPos + 17,
                0, 42, 18, 18, 256, 256);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        GuiDraw.text(graphics, font, Component.translatable("container.rttyController"),
                (imageWidth - font.width(Component.translatable("container.rttyController"))) / 2,
                6, 0xFF404040, false);
    }

    @Override
    protected void extractOwnTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (hover(173, 17, 18, 18, mouseX, mouseY)) {
            tooltip(graphics, polling ? "Polling" : "State Change", mouseX, mouseY);
            return;
        }
        if (hover(209, 17, 18, 18, mouseX, mouseY)) {
            tooltip(graphics, "Save Settings", mouseX, mouseY);
            return;
        }
        if (hover(137, 17, 18, 18, mouseX, mouseY)) {
            IRORInfo info = menu.controller() == null ? null : menu.controller().attachedInfo();
            if (info == null) return;
            List<Component> lines = new ArrayList<>();
            lines.add(Component.literal("Usable functions:"));
            for (String function : info.getFunctionInfo()) {
                if (function.startsWith(IRORInfo.PREFIX_FUNCTION)) {
                    lines.add(Component.literal(function.substring(IRORInfo.PREFIX_FUNCTION.length()))
                            .withStyle(ChatFormatting.AQUA));
                }
            }
            GuiDraw.tooltip(graphics, font, lines, Optional.empty(), mouseX, mouseY);
        }
    }

    private void tooltip(GuiGraphicsExtractor graphics, String value, int mouseX, int mouseY) {
        GuiDraw.tooltip(graphics, font, List.of(Component.literal(value)), Optional.empty(), mouseX, mouseY);
    }

    private boolean hover(int x, int y, int width, int height, double mouseX, double mouseY) {
        return mouseX >= leftPos + x && mouseX < leftPos + x + width
                && mouseY >= topPos + y && mouseY < topPos + y + height;
    }

    @Override
    protected boolean mouseClicked(MouseInput event, boolean doubleClick) {
        if (hover(173, 17, 18, 18, event.x(), event.y())) {
            RadioScreenUtil.click();
            polling = !polling;
            CompoundTag data = new CompoundTag();
            data.putBoolean("polling", polling);
            send(data);
            return true;
        }
        if (hover(209, 17, 18, 18, event.x(), event.y())) {
            RadioScreenUtil.click();
            CompoundTag data = new CompoundTag();
            data.putString("channel", frequency.getValue());
            send(data);
            return true;
        }
        return false;
    }

    private void send(CompoundTag data) {
        TileEntityRadioTorchController controller = menu.controller();
        if (controller != null) {
            GeoCoreNetwork.sendToServer(ControlEventPayload.CODEC,
                    new ControlEventPayload(controller.getBlockPos(), data));
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
