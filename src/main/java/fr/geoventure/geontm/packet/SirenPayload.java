package fr.geoventure.geontm.packet;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.net.GeoCoreCodec;
import fr.geoventure.geocore.net.GeoCoreNetwork;
import fr.geoventure.geocore.net.GeoCorePayload;
import fr.geoventure.geocore.world.SubLevels;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import fr.geoventure.geontm.items.machine.ItemCassette;

/**
 * 1.7 {@code TESirenPacket}: the siren at {@code pos} says which track ({@link ItemCassette.TrackType} ordinal)
 * should be playing and whether it is active. Sent every server tick to every player within 1500 blocks, as
 * upstream did through {@code sendToAllAround}; the client handler is idempotent, so the repetition is what
 * lets a player who walks into range start hearing a siren that was already on.
 */
public record SirenPayload(BlockPos pos, int track, boolean active) implements GeoCorePayload {

    public static final GeoCoreCodec<SirenPayload> CODEC = GeoCoreCodec.of(SirenPayload.class,
            Ids.of(Constants.MOD_ID, "siren"),
            (payload, buffer) -> {
                buffer.writeBlockPos(payload.pos);
                buffer.writeVarInt(payload.track);
                buffer.writeBoolean(payload.active);
            },
            buffer -> new SirenPayload(buffer.readBlockPos(), buffer.readVarInt(), buffer.readBoolean()));

    public static final double RANGE = 1500D;

    public static void send(ServerLevel level, BlockPos pos, int track, boolean active) {
        SirenPayload payload = new SirenPayload(pos.immutable(), track, active);
        double radiusSquared = RANGE * RANGE;
        for (ServerPlayer player : level.players()) {
            if (SubLevels.distanceSq(level, Vec3.atLowerCornerOf(pos), player.position()) > radiusSquared) continue;
            GeoCoreNetwork.sendToPlayer(player, CODEC, payload);
        }
    }

    @Override
    public ResourceLocation id() {
        return CODEC.id();
    }
}
