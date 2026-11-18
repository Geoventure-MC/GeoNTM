package fr.geoventure.geontm.handler;

import fr.geoventure.geontm.extprop.GeoNtmPlayerProps;
import fr.geoventure.geontm.packet.PlayerInformPayload;
import fr.geoventure.geocore.net.GeoCoreNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 1.7 {@code GeoNtmPlayerProps.setKeyPressed(TOGGLE_MAGNET)}: on the key's press edge the server flips
 * {@code enableMagnet} and tells the player ("Magnet ON" green / "Magnet OFF" red, info id {@code ID_MAGNET} 6, one
 * second). The lodestone and magnet armour modules read the flag ({@code ItemModLodestone.modUpdate}).
 */
public final class MagnetToggleHandler {

    /** 1.7 {@code ServerProxy.ID_MAGNET}. */
    public static final int ID_MAGNET = 6;

    private static final Map<UUID, Boolean> PRESSED = new ConcurrentHashMap<>();

    private MagnetToggleHandler() {}

    public static void setKeyPressed(ServerPlayer player, boolean pressed) {
        boolean before = Boolean.TRUE.equals(PRESSED.put(player.getUUID(), pressed));
        if (before || !pressed) return;
        var props = GeoNtmPlayerProps.getData(player);
        props.enableMagnet = !props.enableMagnet;
        GeoCoreNetwork.sendToPlayer(player, PlayerInformPayload.CODEC, props.enableMagnet
                ? PlayerInformPayload.of(ID_MAGNET, 1000, ChatFormatting.GREEN, "Magnet ON")
                : PlayerInformPayload.of(ID_MAGNET, 1000, ChatFormatting.RED, "Magnet OFF"));
    }
}
