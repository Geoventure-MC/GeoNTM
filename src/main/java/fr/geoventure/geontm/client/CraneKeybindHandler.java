package fr.geoventure.geontm.client;

import fr.geoventure.geontm.extprop.GeoNtmPlayerProps;
import fr.geoventure.geontm.handler.EnumCraneKey;
import fr.geoventure.geontm.packet.CraneKeyPayload;
import com.mojang.blaze3d.platform.InputConstants;
import fr.geoventure.geocore.client.ClientTicks;
import fr.geoventure.geocore.client.KeyMappings;
import fr.geoventure.geocore.net.GeoCoreNetwork;
import java.util.Arrays;
import net.minecraft.client.Minecraft;

/** Client-side RBMK crane key registration and edge polling. */
public final class CraneKeybindHandler {
    private static final KeyMappings.Mapping[] KEYS = new KeyMappings.Mapping[EnumCraneKey.VALUES.length];
    private static final boolean[] LAST_STATE = new boolean[EnumCraneKey.VALUES.length];

    private CraneKeybindHandler() {}

    public static void register() {
        KEYS[EnumCraneKey.UP.ordinal()] = KeyMappings.register("geontm", "key.geontm.craneUp", InputConstants.KEY_UP, "geontm");
        KEYS[EnumCraneKey.DOWN.ordinal()] = KeyMappings.register("geontm", "key.geontm.craneDown", InputConstants.KEY_DOWN, "geontm");
        KEYS[EnumCraneKey.LEFT.ordinal()] = KeyMappings.register("geontm", "key.geontm.craneLeft", InputConstants.KEY_LEFT, "geontm");
        KEYS[EnumCraneKey.RIGHT.ordinal()] = KeyMappings.register("geontm", "key.geontm.craneRight", InputConstants.KEY_RIGHT, "geontm");
        KEYS[EnumCraneKey.LOAD.ordinal()] = KeyMappings.register("geontm", "key.geontm.craneLoad", InputConstants.KEY_RETURN, "geontm");
        ClientTicks.onEndTick(CraneKeybindHandler::poll);
    }

    private static void poll() {
        var player = Minecraft.getInstance().player;
        if (player == null) {
            Arrays.fill(LAST_STATE, false);
            return;
        }
        var props = GeoNtmPlayerProps.getData(player);
        for (int i = 0; i < KEYS.length; i++) {
            boolean pressed = KEYS[i].isHeld();
            if (pressed == LAST_STATE[i]) continue;
            LAST_STATE[i] = pressed;
            props.setCraneKeyPressed(EnumCraneKey.VALUES[i], pressed);
            GeoCoreNetwork.sendToServer(CraneKeyPayload.CODEC, new CraneKeyPayload(i, pressed));
        }
    }
}
