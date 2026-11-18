package fr.geoventure.geontm.packet;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.items.weapon.ItemCrucible;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.net.GeoCoreCodec;
import fr.geoventure.geocore.net.GeoCorePayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * C2S: 1.12.2 {@code AuxButtonPacket(0, 0, 0, value, 1000)} from {@code ItemCrucible.updateClient}: whether the holder
 * is drawing a cut (or swinging one), which the server keeps as {@code doSpecialClick}.
 */
public record CrucibleClickPayload(boolean clicked) implements GeoCorePayload {

    public static final GeoCoreCodec<CrucibleClickPayload> CODEC = GeoCoreCodec.of(CrucibleClickPayload.class,
            Ids.of(Constants.MOD_ID, "crucible_click"),
            (payload, buffer) -> buffer.writeBoolean(payload.clicked),
            buffer -> new CrucibleClickPayload(buffer.readBoolean()));

    public static void handleServer(CrucibleClickPayload payload, ServerPlayer player) {
        ItemCrucible.setSpecialClick(player, payload.clicked);
    }

    @Override
    public ResourceLocation id() {
        return CODEC.id();
    }
}
