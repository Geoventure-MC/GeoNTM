package fr.geoventure.geontm.packet;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.net.GeoCoreCodec;
import fr.geoventure.geocore.net.GeoCorePayload;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

/**
 * Cross-loader C2S payload carrying a {@link BlockPos} plus a control {@link CompoundTag}, the 26.1
 * equivalent of HBM's old {@code NBTControlPacket}. The server resolves the tile at {@code pos} and, if
 * it is an {@link fr.geoventure.geontm.interfaces.IControlReceiver}, feeds it the tag (see
 * {@link GeoNtmPackets#handleControl}).
 *
 * <p>The type + codec are pure-vanilla and live in common; only registration (per loader) and the
 * client send entry point is supplied by {@link fr.geoventure.geocore.net.GeoCoreNetwork}.
 */
public record ControlEventPayload(BlockPos pos, CompoundTag data) implements GeoCorePayload {

    public static final GeoCoreCodec<ControlEventPayload> CODEC = GeoCoreCodec.of(ControlEventPayload.class,
            Ids.of(Constants.MOD_ID, "control_event"),
            (payload, buffer) -> {
                buffer.writeBlockPos(payload.pos);
                buffer.writeNbt(payload.data);
            },
            buffer -> new ControlEventPayload(buffer.readBlockPos(), buffer.readNbt()));

    @Override
    public ResourceLocation id() {
        return CODEC.id();
    }
}
