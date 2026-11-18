package fr.geoventure.geontm.packet;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.net.GeoCoreCodec;
import fr.geoventure.geocore.net.GeoCorePayload;
import net.minecraft.resources.Identifier;

/**
 * S2C payload syncing the local player's {@link fr.geoventure.geontm.entity.EntityHbmProps}: the 26.1 replacement
 * for the old {@code GeoNtmPlayerSyncPacket}. The blob is the props' versioned ByteBuf serialization
 * ({@code EntityHbmProps.serialize}); the client applies it to the local player's duck props. Sent
 * only to the owning player (every 5 ticks from {@code EntityEffectHandler}), so no entity id is
 * carried.
 */
public record EntityPropsSyncPayload(byte[] data) implements GeoCorePayload {

    public static final GeoCoreCodec<EntityPropsSyncPayload> CODEC = GeoCoreCodec.of(EntityPropsSyncPayload.class,
            Ids.of(Constants.MOD_ID, "props_sync"),
            (payload, buffer) -> buffer.writeByteArray(payload.data),
            buffer -> new EntityPropsSyncPayload(buffer.readByteArray()));

    @Override
    public Identifier id() {
        return CODEC.id();
    }
}
