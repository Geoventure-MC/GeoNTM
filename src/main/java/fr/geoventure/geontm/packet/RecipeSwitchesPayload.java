package fr.geoventure.geontm.packet;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.net.GeoCoreCodec;
import fr.geoventure.geocore.net.GeoCorePayload;
import net.minecraft.resources.ResourceLocation;

/** S2C, with every datapack sync: the server's {@code GeneralConfig.recipeSwitches()}, so tables list its rows. */
public record RecipeSwitchesPayload(int bits) implements GeoCorePayload {

    public static final GeoCoreCodec<RecipeSwitchesPayload> CODEC = GeoCoreCodec.of(RecipeSwitchesPayload.class,
            Ids.of(Constants.MOD_ID, "recipe_switches"),
            (payload, buffer) -> buffer.writeVarInt(payload.bits),
            buffer -> new RecipeSwitchesPayload(buffer.readVarInt()));

    @Override
    public ResourceLocation id() {
        return CODEC.id();
    }
}
