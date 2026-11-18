package fr.geoventure.geontm.packet;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.net.GeoCoreCodec;
import fr.geoventure.geocore.net.GeoCoreNetwork;
import fr.geoventure.geocore.net.GeoCorePayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * S2C: 1.12.2's {@code AuxParticlePacketNT(GeoNtmEffectNT.Anim, {hand, mode, name})} as the sword cutters sent it to
 * their holder: {@link #EQUIP} ({@code "equip"}, the Collada deploy) and {@link #CSWING} ({@code "cSwing"}, the
 * crucible's random swing). Always the main hand; the client keys the animation to the item it then holds.
 */
public record CrucibleAnimPayload(byte mode) implements GeoCorePayload {

    public static final byte EQUIP = 0;
    public static final byte CSWING = 1;

    public static final GeoCoreCodec<CrucibleAnimPayload> CODEC = GeoCoreCodec.of(CrucibleAnimPayload.class,
            Ids.of(Constants.MOD_ID, "crucible_anim"),
            (payload, buffer) -> buffer.writeByte(payload.mode),
            buffer -> new CrucibleAnimPayload(buffer.readByte()));

    /** To the holder only. */
    public static void send(ServerPlayer player, byte mode) {
        GeoCoreNetwork.sendToPlayer(player, CODEC, new CrucibleAnimPayload(mode));
    }

    @Override
    public ResourceLocation id() {
        return CODEC.id();
    }
}
