package fr.geoventure.geontm.packet;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.net.GeoCoreCodec;
import fr.geoventure.geocore.net.GeoCorePayload;
import net.minecraft.resources.ResourceLocation;

/**
 * S2C: 1.7 {@code GeoNtmAnimationPacket} for an {@code IAnimatedItem} tool (receiver and item index are always 0 there).
 * The item id travels with it so a late packet cannot start an animation on an item selected in the meantime.
 */
public record ToolAnimationPayload(int animation, String itemId) implements GeoCorePayload {

    public static final GeoCoreCodec<ToolAnimationPayload> CODEC = GeoCoreCodec.of(ToolAnimationPayload.class,
            Ids.of(Constants.MOD_ID, "tool_animation"),
            (payload, buffer) -> {
                buffer.writeVarInt(payload.animation);
                buffer.writeUtf(payload.itemId);
            },
            buffer -> new ToolAnimationPayload(buffer.readVarInt(), buffer.readUtf(256)));

    @Override
    public ResourceLocation id() {
        return CODEC.id();
    }
}
