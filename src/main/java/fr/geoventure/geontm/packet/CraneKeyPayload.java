package fr.geoventure.geontm.packet;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.net.GeoCoreCodec;
import fr.geoventure.geocore.net.GeoCorePayload;
import net.minecraft.resources.ResourceLocation;

/** C2S edge update for one RBMK crane control key. */
public record CraneKeyPayload(int key, boolean pressed) implements GeoCorePayload {
    public static final GeoCoreCodec<CraneKeyPayload> CODEC = GeoCoreCodec.of(CraneKeyPayload.class,
            Ids.of(Constants.MOD_ID, "crane_key"),
            (payload, buffer) -> {
                buffer.writeVarInt(payload.key);
                buffer.writeBoolean(payload.pressed);
            },
            buffer -> new CraneKeyPayload(buffer.readVarInt(), buffer.readBoolean()));

    @Override public ResourceLocation id() { return CODEC.id(); }
}
