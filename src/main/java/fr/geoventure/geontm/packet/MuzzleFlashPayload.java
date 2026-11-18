package fr.geoventure.geontm.packet;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.net.GeoCoreCodec;
import fr.geoventure.geocore.net.GeoCorePayload;
import net.minecraft.resources.ResourceLocation;

/** S2C: a tracked gun holder fired; the client records the observer-side flash timestamp. */
public record MuzzleFlashPayload(int entityId) implements GeoCorePayload {

    public static final GeoCoreCodec<MuzzleFlashPayload> CODEC = GeoCoreCodec.of(MuzzleFlashPayload.class,
            Ids.of(Constants.MOD_ID, "muzzle_flash"),
            (payload, buffer) -> buffer.writeVarInt(payload.entityId),
            buffer -> new MuzzleFlashPayload(buffer.readVarInt()));

    @Override
    public ResourceLocation id() {
        return CODEC.id();
    }
}
