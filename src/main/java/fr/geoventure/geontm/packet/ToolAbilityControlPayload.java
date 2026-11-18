package fr.geoventure.geontm.packet;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.items.tool.ItemToolAbility;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.net.GeoCoreCodec;
import fr.geoventure.geocore.net.GeoCorePayload;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/**
 * 1.7 {@code NBTItemControlPacket} as the tool-ability screen used it: the edited preset configuration
 * of the held tool, handed to its {@code receiveControl}, which restricts it to what the tool offers.
 */
public record ToolAbilityControlPayload(CompoundTag data) implements GeoCorePayload {

    public static final GeoCoreCodec<ToolAbilityControlPayload> CODEC = GeoCoreCodec.of(ToolAbilityControlPayload.class,
            Ids.of(Constants.MOD_ID, "tool_ability_control"),
            (payload, buffer) -> buffer.writeNbt(payload.data),
            buffer -> new ToolAbilityControlPayload(buffer.readNbt()));

    public static void handleServer(ToolAbilityControlPayload payload, ServerPlayer player) {
        if (payload.data == null) return;
        ItemStack held = player.getMainHandItem();
        if (held.getItem() instanceof ItemToolAbility tool) tool.receiveControl(held, payload.data);
    }

    @Override
    public Identifier id() {
        return CODEC.id();
    }
}
