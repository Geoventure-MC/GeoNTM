package fr.geoventure.geontm.client;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.client.render.RenderInfoSystem;
import fr.geoventure.geontm.handler.ToolKeybinds.EnumToolKey;
import fr.geoventure.geontm.inventory.ModUtilityToolMenus;
import fr.geoventure.geontm.inventory.gui.GUIBookLore;
import fr.geoventure.geontm.inventory.gui.GUILeadBox;
import fr.geoventure.geontm.inventory.gui.GUILemegeton;
import fr.geoventure.geontm.inventory.gui.GUIScreenClayTablet;
import fr.geoventure.geontm.inventory.gui.GUIScreenFluid;
import fr.geoventure.geontm.inventory.gui.GUIScreenGuide;
import fr.geoventure.geontm.inventory.gui.GUIScreenHolotape;
import fr.geoventure.geontm.inventory.gui.GUIScreenPager;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geontm.items.tool.ItemColtanCompass;
import fr.geoventure.geontm.items.tool.UtilityToolScreens;
import fr.geoventure.geontm.main.MainRegistry;
import fr.geoventure.geontm.packet.ToolKeyPayload;
import com.mojang.blaze3d.platform.InputConstants;
import fr.geoventure.geocore.client.ClientTicks;
import fr.geoventure.geocore.client.ItemModelProperties;
import fr.geoventure.geocore.client.KeyMappings;
import fr.geoventure.geocore.net.GeoCoreNetwork;
import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Client half of the utility tools and books: the two menu screens, the item-screen opener, the
 * {@code geontm:coltass} and {@code geontm:polaroid} model properties, the Coltass's needle and distance readout
 * (1.7 {@code ItemColtanCompass.onUpdate} client branch and {@code TextureColtass}), and the copy tool's two
 * modifier keys ({@code copyToolAlt} left Alt, {@code copyToolCtrl} left Control).
 */
public final class UtilityToolClientRegistrations {

    /** 1.7 {@code ServerProxy.ID_COMPASS}. */
    private static final int ID_COMPASS = 2;
    private static final int FRAMES = 32;

    private static final KeyMappings.Mapping[] KEYS = new KeyMappings.Mapping[EnumToolKey.VALUES.length];
    private static final boolean[] LAST_STATE = new boolean[EnumToolKey.VALUES.length];

    // 1.7 ItemColtanCompass fields and TextureCompass state
    private static int lastX = 0;
    private static int lastZ = 0;
    private static long lease = 0;
    private static double currentAngle;
    private static double angleDelta;
    private static int frameCounter;

    private UtilityToolClientRegistrations() {}

    public static void registerScreens(GeoNtmClientRegistrations.ScreenSink sink) {
        UtilityToolScreens.setClientOpener((kind, stack) -> {
            Minecraft mc = Minecraft.getInstance();
            switch (kind) {
                case PAGER -> mc.setScreen(new GUIScreenPager(stack));
                case RTTY_DESIGNATOR -> mc.setScreen(new GUIScreenPager(stack, Component.translatable("container.rttyDesignator"),
                        Ids.of(Constants.MOD_ID, "textures/gui/machine/gui_rtty_designator.png")));
                case FLUID_MULTI -> mc.setScreen(new GUIScreenFluid());
                case GUIDE -> mc.setScreen(new GUIScreenGuide(stack));
                case BOOK_LORE -> mc.setScreen(new GUIBookLore(stack));
                case HOLOTAPE -> mc.setScreen(new GUIScreenHolotape());
                case CLAY_TABLET -> mc.setScreen(new GUIScreenClayTablet());
            }
        });
        sink.register(ModUtilityToolMenus.LEAD_BOX.get(), GUILeadBox::new);
        sink.register(ModUtilityToolMenus.LEMEGETON.get(), GUILemegeton::new);
    }

    public static void installItemModelProperties() {
        ItemModelProperties.register(Constants.MOD_ID, "coltass", stack -> frameCounter);
        // polaroid_<id>: case n is polaroid n + 1
        ItemModelProperties.register(Constants.MOD_ID, "polaroid", stack -> MainRegistry.polaroidID - 1);
    }

    public static void installKeybinds() {
        KEYS[EnumToolKey.TOOL_ALT.ordinal()] = KeyMappings.register("geontm", "key.geontm.copyToolAlt", InputConstants.KEY_LALT, "geontm");
        KEYS[EnumToolKey.TOOL_CTRL.ordinal()] = KeyMappings.register("geontm", "key.geontm.copyToolCtrl", InputConstants.KEY_LCONTROL, "geontm");
        ClientTicks.onEndTick(UtilityToolClientRegistrations::tick);
    }

    private static void tick() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || mc.level == null) {
            Arrays.fill(LAST_STATE, false);
            updateCompass(null, 0, 0, 0, true);
            return;
        }

        for (int i = 0; i < KEYS.length; i++) {
            boolean pressed = KEYS[i] != null && KEYS[i].isHeld();
            if (pressed == LAST_STATE[i]) continue;
            LAST_STATE[i] = pressed;
            GeoCoreNetwork.sendToServer(ToolKeyPayload.CODEC, new ToolKeyPayload(i, pressed));
        }

        // 1.7 onUpdate (client) for every Coltass in the inventory: remember the deposit, lease it for a second
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!(stack.getItem() instanceof ItemColtanCompass)) continue;
            int[] target = ItemColtanCompass.target(stack);
            if (target != null) {
                lastX = target[0];
                lastZ = target[1];
                lease = System.currentTimeMillis() + 1000;
                double dx = player.getX() - lastX, dz = player.getZ() - lastZ;
                RenderInfoSystem.displayTooltip(Component.literal(((int) Math.sqrt(dx * dx + dz * dz)) + "m"), 0, ID_COMPASS);
            }
        }
        if (lease < System.currentTimeMillis()) {
            lastX = 0;
            lastZ = 0;
        }

        // TextureColtass.updateAnimation
        if (!(lastX == 0 && lastZ == 0)) {
            updateCompass(mc.level, player.getX(), player.getZ(), player.getYRot(), false);
        } else {
            updateCompass(null, 0.0D, 0.0D, 0.0D, true);
        }
    }

    /** 1.7 {@code TextureColtass.updateCompass}, choosing a frame index instead of uploading it. */
    private static void updateCompass(Level world, double x, double z, double yaw, boolean ignoreDestination) {
        double angle;

        if (world != null && !ignoreDestination && world.dimension() == Level.OVERWORLD && lease > System.currentTimeMillis()) {
            double d4 = (double) lastX - x;
            double d5 = (double) lastZ - z;
            yaw %= 360.0D;
            angle = -((yaw - 90.0D) * Math.PI / 180.0D - Math.atan2(d5, d4));
        } else {
            angle = Math.random() * Math.PI * 2.0D;
        }

        double d6;
        for (d6 = angle - currentAngle; d6 < -Math.PI; d6 += (Math.PI * 2D)) { }
        while (d6 >= Math.PI) d6 -= (Math.PI * 2D);
        if (d6 < -1.0D) d6 = -1.0D;
        if (d6 > 1.0D) d6 = 1.0D;

        angleDelta += d6 * 0.1D;
        angleDelta *= 0.8D;
        currentAngle += angleDelta;

        int i;
        for (i = (int) ((currentAngle / (Math.PI * 2D) + 1.0D) * (double) FRAMES) % FRAMES; i < 0; i = (i + FRAMES) % FRAMES) { }
        frameCounter = i;
    }
}
