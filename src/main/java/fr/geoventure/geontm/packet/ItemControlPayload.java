package fr.geoventure.geontm.packet;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.items.IItemControlReceiver;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.net.GeoCoreCodec;
import fr.geoventure.geocore.net.GeoCorePayload;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/**
 * 1.7 {@code NBTItemControlPacket}: a control tag from an item's own screen, applied server-side to the
 * item in the sender's main hand ({@code player.getHeldItem()}) when that item is an
 * {@link IItemControlReceiver}.
 */
public record ItemControlPayload(CompoundTag data) implements GeoCorePayload {

    public static final GeoCoreCodec<ItemControlPayload> CODEC = GeoCoreCodec.of(ItemControlPayload.class,
            Ids.of(Constants.MOD_ID, "item_control"),
            (payload, buffer) -> buffer.writeNbt(payload.data),
            buffer -> new ItemControlPayload(buffer.readNbt()));

    public static void handle(ItemControlPayload payload, ServerPlayer player) {
        if (player == null || payload.data == null || payload.data.sizeInBytes() > 4 * 1024) return;
        ItemStack held = player.getMainHandItem();
        if (held.getItem() instanceof IItemControlReceiver receiver) {
            receiver.receiveControl(player, held, payload.data);
            player.inventoryMenu.broadcastChanges();
        }
    }

    @Override
    public Identifier id() {
        return CODEC.id();
    }
}
