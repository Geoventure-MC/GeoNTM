package fr.geoventure.geontm.packet;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.net.GeoCoreCodec;
import fr.geoventure.geocore.net.GeoCorePayload;
import net.minecraft.resources.ResourceLocation;

/**
 * S2C: the pollution of the region the player stands in (soot, poison, heavy metal, fallout), 1.7's "POLLUTION" block of
 * {@code PermaSyncHandler}. 1.7 wrote it to every player every tick; the port sends it when it changed for that
 * player, which is the same information. The client keeps it in {@link fr.geoventure.geontm.client.SmogFog} for the smog.
 */
public record PollutionSyncPayload(float soot, float poison, float heavyMetal, float fallout) implements GeoCorePayload {

    public static final GeoCoreCodec<PollutionSyncPayload> CODEC = GeoCoreCodec.of(PollutionSyncPayload.class,
            Ids.of(Constants.MOD_ID, "pollution_sync"),
            (payload, buffer) -> {
                buffer.writeFloat(payload.soot);
                buffer.writeFloat(payload.poison);
                buffer.writeFloat(payload.heavyMetal);
                buffer.writeFloat(payload.fallout);
            },
            buffer -> new PollutionSyncPayload(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat()));

    @Override
    public ResourceLocation id() {
        return CODEC.id();
    }
}
