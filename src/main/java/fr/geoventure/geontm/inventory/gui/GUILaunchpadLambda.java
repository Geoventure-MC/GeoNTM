package fr.geoventure.geontm.inventory.gui;

import fr.geoventure.geontm.inventory.container.ContainerLaunchpadLambda;
import fr.geoventure.geontm.packet.ControlEventPayload;
import fr.geoventure.geontm.tileentity.machine.TileEntityLaunchpadLambda;
import fr.geoventure.geontm.tileentity.machine.TileEntityLaunchpadSoyuz;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.gui.CompatContainerScreen;
import fr.geoventure.geocore.gui.GuiDraw;
import fr.geoventure.geocore.gui.MouseInput;
import fr.geoventure.geocore.net.GeoCoreNetwork;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

/**
 * 1.7 {@code GUILaunchpadLambda}: {@code gui_launchpad_lambda.png} at 176x226 with no title. Power and both tanks
 * with their ready lamps, the MANUAL / AUTO switch at (34,43)/(52,43), the launch button at (43,70) usable once the
 * rocket stands, the rocket and satellite lamps, and the clock or state below.
 */
public class GUILaunchpadLambda extends CompatContainerScreen<ContainerLaunchpadLambda> {

    private static final Identifier TEXTURE = Ids.of("geontm", "textures/gui/machine/gui_launchpad_lambda.png");

    public GUILaunchpadLambda(ContainerLaunchpadLambda menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 226);
    }

    private void blit(GuiGraphicsExtractor graphics, int x, int y, int u, int v, int w, int h) {
        GuiDraw.blit(graphics, TEXTURE, leftPos + x, topPos + y, (float) u, (float) v, w, h, 256, 256);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        GuiDraw.blit(graphics, TEXTURE, leftPos, topPos, 0F, 0F, imageWidth, imageHeight, 256, 256);

        TileEntityLaunchpadLambda pad = menu.getLauncher();
        if (pad == null) return;

        int power = (int) (pad.power * 52 / TileEntityLaunchpadLambda.maxPower);
        if (power > 0) blit(graphics, 89, 78 - power, 194, 52 - power, 16, power);

        blit(graphics, 34 + (pad.autolaunch ? 18 : 0), 43, 210 + (pad.autolaunch ? 18 : 0), 26, 18, 18);

        blit(graphics, 112, 13, pad.hasJetFuel() ? 210 : 216, 0, 6, 8);
        blit(graphics, 130, 13, pad.hasOxidizer() ? 210 : 216, 0, 6, 8);
        blit(graphics, 94, 13, pad.power >= TileEntityLaunchpadLambda.CONSUMPTION ? 210 : 216, 0, 6, 8);

        if (pad.countdown > 0) blit(graphics, 43, 70, 210, 44, 18, 18);

        blit(graphics, 34, 16, pad.hasRocketLoaded() ? 228 : 210, 8, 18, 18);
        boolean satReady = !pad.getItem(1).isEmpty() && !TileEntityLaunchpadSoyuz.needsOrbiter(pad.getItem(1));
        blit(graphics, 52, 16, satReady ? 228 : 210, 8, 18, 18);

        GuiUtil.tank(graphics, pad.tanks[0], leftPos + 107, topPos + 78, 16, 52);
        GuiUtil.tank(graphics, pad.tanks[1], leftPos + 125, topPos + 78, 16, 52);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        GuiDraw.text(graphics, font, playerInventoryTitle, 8, imageHeight - 96 + 2, 0xFF404040, false);

        TileEntityLaunchpadLambda pad = menu.getLauncher();
        if (pad == null) return;

        if (pad.erected && pad.countdown > 0) {
            String secs = "" + pad.countdown / 20;
            String cents = "" + (pad.countdown % 20) * 5;
            if (secs.length() == 1) secs = "0" + secs;
            if (cents.length() == 1) cents += "0";
            GuiDraw.text(graphics, font, Component.literal(secs + ":" + cents), 40, 103, 0xFFFF0000, false);
        } else if (!pad.hasRocketLoaded()) {
            GUILaunchpadSoyuz.constrainedLabel(graphics, font, I18n.get("desc.gui.soyuz.idle"), 53, 107, 0xFFFF0000);
        } else if (!pad.erected) {
            GUILaunchpadSoyuz.constrainedLabel(graphics, font, I18n.get("desc.gui.soyuz.loading"), 53, 107, 0xFFFF8000);
        } else {
            GUILaunchpadSoyuz.constrainedLabel(graphics, font, I18n.get("desc.gui.soyuz.ready"), 53, 107, 0xFF00FF00);
        }
    }

    @Override
    protected void extractOwnTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        TileEntityLaunchpadLambda pad = menu.getLauncher();
        if (pad == null) return;

        if (isHovering(107, 26, 16, 52, mouseX, mouseY)) {
            GuiUtil.tooltip(graphics, font, GuiUtil.tankTooltip(pad.tanks[0]), mouseX, mouseY);
        } else if (isHovering(125, 26, 16, 52, mouseX, mouseY)) {
            GuiUtil.tooltip(graphics, font, GuiUtil.tankTooltip(pad.tanks[1]), mouseX, mouseY);
        } else if (isHovering(89, 26, 16, 52, mouseX, mouseY)) {
            GuiUtil.tooltip(graphics, font, GuiUtil.energyTooltip(pad.power, TileEntityLaunchpadLambda.maxPower), mouseX, mouseY);
        } else if (isHovering(34, 43, 18, 18, mouseX, mouseY)) {
            GuiUtil.tooltip(graphics, font, GuiUtil.keyArray("desc.gui.lambda.manual"), mouseX, mouseY);
        } else if (isHovering(52, 43, 18, 18, mouseX, mouseY)) {
            GuiUtil.tooltip(graphics, font, GuiUtil.keyArray("desc.gui.lambda.auto"), mouseX, mouseY);
        }
    }

    @Override
    protected boolean mouseClicked(MouseInput event, boolean doubleClick) {
        TileEntityLaunchpadLambda pad = menu.getLauncher();
        if (pad == null) return super.mouseClicked(event, doubleClick);
        double x = event.x() - leftPos;
        double y = event.y() - topPos;
        CompoundTag data = null;

        if (GuiUtil.hover(x, y, 34, 43, 18, 18)) {
            data = new CompoundTag();
            data.putBoolean("auto", false);
        }
        if (GuiUtil.hover(x, y, 52, 43, 18, 18)) {
            data = new CompoundTag();
            data.putBoolean("auto", true);
        }
        if (GuiUtil.hover(x, y, 43, 70, 18, 18) && pad.erected) {
            data = new CompoundTag();
            data.putBoolean("launch", true);
        }

        if (data == null) return super.mouseClicked(event, doubleClick);
        GuiUtil.playClickSound();
        GeoCoreNetwork.sendToServer(ControlEventPayload.CODEC, new ControlEventPayload(pad.getBlockPos(), data));
        return true;
    }
}
