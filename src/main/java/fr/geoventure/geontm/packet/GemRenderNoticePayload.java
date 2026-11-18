package fr.geoventure.geontm.packet;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.net.GeoCoreCodec;
import fr.geoventure.geocore.net.GeoCorePayload;
import net.minecraft.resources.Identifier;

/**
 * S2C, on every join of a world: the client says in chat that GemRender is missing, if it is. Only the
 * client knows its own mods, so the server cannot decide this itself.
 */
public record GemRenderNoticePayload() implements GeoCorePayload {

    public static final GeoCoreCodec<GemRenderNoticePayload> CODEC = GeoCoreCodec.of(GemRenderNoticePayload.class,
            Ids.of(Constants.MOD_ID, "gemrender_notice"), (payload, buffer) -> {}, buffer -> new GemRenderNoticePayload());

    @Override
    public Identifier id() {
        return CODEC.id();
    }
}
