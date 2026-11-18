package fr.geoventure.geontm.packet;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.net.GeoCoreCodec;
import fr.geoventure.geocore.net.GeoCorePayload;
import net.minecraft.resources.Identifier;

/** S2C exhaust cue for the old remote jetpack plume effects. */
public record JetpackExhaustPayload(int playerId, int mode) implements GeoCorePayload {

    public static final GeoCoreCodec<JetpackExhaustPayload> CODEC = GeoCoreCodec.of(JetpackExhaustPayload.class,
            Ids.of(Constants.MOD_ID, "jetpack_exhaust"),
            (payload, buffer) -> {
                buffer.writeInt(payload.playerId);
                buffer.writeByte(payload.mode);
            },
            buffer -> new JetpackExhaustPayload(buffer.readInt(), buffer.readByte()));

    @Override
    public Identifier id() {
        return CODEC.id();
    }
}
