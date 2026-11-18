package fr.geoventure.geontm.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.geoventure.geontm.client.render.RenderInfoSystem;
import fr.geoventure.geontm.handler.GeoNtmKeybinds.EnumKeybind;
import fr.geoventure.geontm.handler.ability.IBaseAbility;
import fr.geoventure.geontm.handler.ability.IToolAreaAbility;
import fr.geoventure.geontm.handler.ability.ToolPreset;
import fr.geoventure.geontm.inventory.gui.GUIScreenToolAbility;
import fr.geoventure.geontm.items.tool.ItemToolAbility;
import fr.geoventure.geontm.packet.KeybindPayload;
import fr.geoventure.geontm.packet.PlayerInformPayload;
import com.mojang.blaze3d.platform.InputConstants;
import fr.geoventure.geocore.client.ClientTicks;
import fr.geoventure.geocore.client.KeyMappings;
import fr.geoventure.geocore.gui.GuiDraw;
import fr.geoventure.geocore.harness.HarnessQueryRegistry;
import fr.geoventure.geocore.net.GeoCoreNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Client half of the ability tool family: the cycle key, the ability-alt key that opens the preset editor,
 * the info HUD (1.7 {@code RenderInfoSystem}) that shows preset changes, the area-ability indicator beside the
 * crosshair (1.7 {@code ItemToolAbility.renderHUD}), and the inform packet.
 */
public final class AbilityToolClientRegistrations {

    private AbilityToolClientRegistrations() {}

    /** 1.7 {@code GeoNtmKeybinds.abilityAlt}, left Alt by default. */
    public static KeyMappings.Mapping abilityAltKey;
    /** 1.7 {@code GeoNtmKeybinds.abilityCycle}, the use button by default. */
    public static KeyMappings.Mapping abilityCycleKey;
    private static boolean lastAlt;
    private static boolean lastCycle;
    /** Null until reported in the current world, so every join tells the server once. */
    private static Boolean lastRebound;

    /** 1.7 {@code ClientConfig.TOOL_HUD_INDICATOR_X/Y}, both 0 by default. */
    public static int toolHudIndicatorX = 0;
    public static int toolHudIndicatorY = 0;

    private static final Map<IBaseAbility, int[]> abilityGui = new HashMap<>();

    static {
        abilityGui.put(IToolAreaAbility.RECURSION, new int[]{0, 138});
        abilityGui.put(IToolAreaAbility.HAMMER, new int[]{16, 138});
        abilityGui.put(IToolAreaAbility.HAMMER_FLAT, new int[]{32, 138});
        abilityGui.put(IToolAreaAbility.EXPLOSION, new int[]{48, 138});
    }

    public static void registerKeys() {
        abilityCycleKey = KeyMappings.register("geontm", "key.geontm.ability", InputConstants.Type.MOUSE, 1, "geontm");
        abilityAltKey = KeyMappings.register("geontm", "key.geontm.abilityAlt", InputConstants.KEY_LALT, "geontm");
        ClientTicks.onEndTick(AbilityToolClientRegistrations::tick);
        HarnessQueryRegistry.register("geontm_info", (mc, params) -> {
            JsonObject json = new JsonObject();
            JsonArray lines = new JsonArray();
            RenderInfoSystem.currentTexts().forEach(lines::add);
            json.add("messages", lines);
            return json;
        });
    }

    public static void registerHud(GeoNtmClientRegistrations.HudSink sink) {
        sink.register("info_system", RenderInfoSystem::render);
        sink.register("tool_ability", AbilityToolClientRegistrations::renderAbilityHud);
    }

    public static void registerPacketHandlers() {
        GeoCoreNetwork.registerPlayToClient(PlayerInformPayload.CODEC,
                (payload, context) -> RenderInfoSystem.displayTooltip(payload.message(), payload.millis(), payload.infoId()));
    }

    private static void tick() {
        RenderInfoSystem.clientTick();

        Minecraft mc = Minecraft.getInstance();
        boolean alt = abilityAltKey != null && mc.player != null && abilityAltKey.isHeld();
        // 1.7 ItemToolAbility.handleKeybindClient: the key going down opens the editor for the held tool.
        if (alt && !lastAlt && mc.screen == null) {
            ItemStack held = mc.player.getMainHandItem();
            if (held.getItem() instanceof ItemToolAbility tool) {
                mc.setScreen(new GUIScreenToolAbility(tool.getAvailableAbilities()));
            }
        }
        lastAlt = alt;
        tickCycle(mc);
    }

    /** On Use the item's use click cycles; bound elsewhere, the key's edges go to the server instead. */
    private static void tickCycle(Minecraft mc) {
        if (mc.player == null || mc.level == null) {
            lastRebound = null;
            lastCycle = false;
            return;
        }
        boolean rebound = !abilityCycleKey.same(mc.options.keyUse);
        if (lastRebound == null || lastRebound != rebound) {
            ItemToolAbility.setCycleRebound(mc.player, rebound);
            send(EnumKeybind.ABILITY_CYCLE_REBOUND, rebound);
            lastRebound = rebound;
        }
        boolean cycle = rebound && mc.screen == null && abilityCycleKey.isHeld()
                && mc.player.getMainHandItem().getItem() instanceof ItemToolAbility;
        if (cycle != lastCycle) send(EnumKeybind.ABILITY_CYCLE, cycle);
        lastCycle = cycle;
    }

    private static void send(EnumKeybind keybind, boolean state) {
        GeoCoreNetwork.sendToServer(KeybindPayload.CODEC, new KeybindPayload(keybind.ordinal(), state));
    }

    /** 1.7 {@code ItemToolAbility.renderHUD}: the area ability's icon, inverted like the crosshair. */
    private static void renderAbilityHud(GuiGraphicsExtractor graphics) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui || mc.screen != null) return;

        ItemStack stack = mc.player.getMainHandItem();
        if (!(stack.getItem() instanceof ItemToolAbility tool)) return;

        ToolPreset preset = tool.getConfiguration(stack).getActivePreset();
        int[] uv = abilityGui.get(preset.areaAbility);
        if (uv == null) return;

        int size = 16;
        int x = mc.getWindow().getGuiScaledWidth() / 2 - size - 8 + toolHudIndicatorX;
        int y = mc.getWindow().getGuiScaledHeight() / 2 + 8 + toolHudIndicatorY;
        GuiDraw.crosshair(graphics, GUIScreenToolAbility.texture, x, y, uv[0], uv[1], size, size, 256, 256);
    }
}
