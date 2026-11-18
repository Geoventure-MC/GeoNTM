package fr.geoventure.geontm.packet;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.net.GeoCoreCodec;
import fr.geoventure.geocore.net.GeoCorePayload;
import net.minecraft.resources.ResourceLocation;

/**
 * S2C: the player's level's Tom impact state (fire, dust, impact), 1.7's "TOM IMPACT DATA" block of
 * {@code PermaSyncHandler}. 1.7 wrote it to every player every tick; the port sends it when it changed for that
 * player (or the player changed level), which is the same information. The client stores it in
 * {@link fr.geoventure.geontm.handler.ImpactWorldHandler} against its current level.
 */
public record ImpactSyncPayload(float fire, float dust, boolean impact) implements GeoCorePayload {

    public static final GeoCoreCodec<ImpactSyncPayload> CODEC = GeoCoreCodec.of(ImpactSyncPayload.class,
            Ids.of(Constants.MOD_ID, "impact_sync"),
            (payload, buffer) -> {
                buffer.writeFloat(payload.fire);
                buffer.writeFloat(payload.dust);
                buffer.writeBoolean(payload.impact);
            },
            buffer -> new ImpactSyncPayload(buffer.readFloat(), buffer.readFloat(), buffer.readBoolean()));

    @Override
    public ResourceLocation id() {
        return CODEC.id();
    }
}
