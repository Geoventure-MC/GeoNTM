package fr.geoventure.geontm.packet;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.net.GeoCoreCodec;
import fr.geoventure.geocore.net.GeoCorePayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import fr.geoventure.geontm.inventory.container.ContainerPneumoStorageAccess;

/**
 * Client to server: a click on one of the access terminal's virtual slots, naming the type by the id the menu
 * handed out. 1.7 {@code ContainerPneumoStorageAccess.sendClickToServer}.
 */
public record PneumoAccessInputPayload(int containerId, int op, int entryId) implements GeoCorePayload {

    public static final GeoCoreCodec<PneumoAccessInputPayload> CODEC = GeoCoreCodec.of(PneumoAccessInputPayload.class,
            Ids.of(Constants.MOD_ID, "pneumo_access_input"),
            (payload, buffer) -> {
                buffer.writeVarInt(payload.containerId);
                buffer.writeByte(payload.op);
                buffer.writeVarInt(payload.entryId);
            },
            buffer -> new PneumoAccessInputPayload(buffer.readVarInt(), buffer.readByte(), buffer.readVarInt()));

    public static void handleServer(PneumoAccessInputPayload payload, ServerPlayer player) {
        // vanilla refuses a spectator's container clicks the same way
        if (player.isSpectator()) return;
        if (!(player.containerMenu instanceof ContainerPneumoStorageAccess menu)) return;
        if (menu.containerId != payload.containerId || !menu.stillValid(player)) return;
        ContainerPneumoStorageAccess.ClickOp[] ops = ContainerPneumoStorageAccess.ClickOp.values();
        if (payload.op < 0 || payload.op >= ops.length) return;
        menu.handleClick(ops[payload.op], payload.entryId);
    }

    @Override
    public ResourceLocation id() {
        return CODEC.id();
    }
}
