package fr.geoventure.geontm.packet;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.net.GeoCoreCodec;
import fr.geoventure.geocore.net.GeoCorePayload;
import net.minecraft.resources.ResourceLocation;

/**
 * C2S: one of HBM's keybinds changed state.
 *
 * <p>Ported from 1.12.2 {@code KeybindPacket}. This is the <em>only</em> thing the client tells the
 * server about firing: it reports that a key is now down or up, and the server decides what that
 * means. A client cannot ask to fire a shot, so it cannot fire faster than the gun allows, fire while
 * jammed, or fire a round it does not have.
 */
public record KeybindPayload(int keybind, boolean state) implements GeoCorePayload {

    public static final GeoCoreCodec<KeybindPayload> CODEC = GeoCoreCodec.of(KeybindPayload.class,
            Ids.of(Constants.MOD_ID, "keybind"),
            (payload, buffer) -> {
                buffer.writeVarInt(payload.keybind);
                buffer.writeBoolean(payload.state);
            },
            buffer -> new KeybindPayload(buffer.readVarInt(), buffer.readBoolean()));

    @Override
    public ResourceLocation id() {
        return CODEC.id();
    }
}
