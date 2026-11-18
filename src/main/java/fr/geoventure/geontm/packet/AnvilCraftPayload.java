package fr.geoventure.geontm.packet;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.net.GeoCoreCodec;
import fr.geoventure.geocore.net.GeoCorePayload;
import net.minecraft.resources.ResourceLocation;

/**
 * C2S payload for the anvil's construction crafting, the 26.1 port of {@code AnvilCraftPacket}. Carries
 * the recipe id of the construction row (1.7 sent its index in the recipe list; the rows are datapack recipes here, and
 * a reload on the server can reorder the list before the client has the new one, so an index could craft another
 * recipe) and the mode (0 = craft one, 1 = craft up to 64). Fully validated server-side in {@link GeoNtmPackets#handleAnvilCraft}.
 */
public record AnvilCraftPayload(String recipeId, int mode) implements GeoCorePayload {

    public static final GeoCoreCodec<AnvilCraftPayload> CODEC = GeoCoreCodec.of(AnvilCraftPayload.class,
            Ids.of(Constants.MOD_ID, "anvil_craft"),
            (payload, buffer) -> {
                buffer.writeUtf(payload.recipeId);
                buffer.writeVarInt(payload.mode);
            },
            buffer -> new AnvilCraftPayload(buffer.readUtf(256), buffer.readVarInt()));

    @Override
    public ResourceLocation id() {
        return CODEC.id();
    }
}
