package fr.geoventure.geontm.packet;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.net.GeoCoreCodec;
import fr.geoventure.geocore.net.GeoCorePayload;
import net.minecraft.resources.ResourceLocation;

/** C2S edge update for the copy tool's two modifier keys (1.7 {@code TOOL_ALT} / {@code TOOL_CTRL}). */
public record ToolKeyPayload(int key, boolean pressed) implements GeoCorePayload {
    public static final GeoCoreCodec<ToolKeyPayload> CODEC = GeoCoreCodec.of(ToolKeyPayload.class,
            Ids.of(Constants.MOD_ID, "tool_key"),
            (payload, buffer) -> {
                buffer.writeVarInt(payload.key);
                buffer.writeBoolean(payload.pressed);
            },
            buffer -> new ToolKeyPayload(buffer.readVarInt(), buffer.readBoolean()));

    @Override public ResourceLocation id() { return CODEC.id(); }
}
