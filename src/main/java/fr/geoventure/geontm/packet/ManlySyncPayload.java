package fr.geoventure.geontm.packet;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.net.GeoCoreCodec;
import fr.geoventure.geocore.net.GeoCorePayload;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

/** S2C: the Astolfized players in the receiver's level, 1.7's {@code PermaSyncHandler.boykissers}, sent on change. */
public record ManlySyncPayload(List<Integer> ids) implements GeoCorePayload {

    public static final GeoCoreCodec<ManlySyncPayload> CODEC = GeoCoreCodec.of(ManlySyncPayload.class,
            Ids.of(Constants.MOD_ID, "manly_sync"),
            (payload, buffer) -> {
                buffer.writeShort(payload.ids.size());
                for (int id : payload.ids) buffer.writeInt(id);
            },
            buffer -> {
                Integer[] ids = new Integer[buffer.readShort()];
                for (int i = 0; i < ids.length; i++) ids[i] = buffer.readInt();
                return new ManlySyncPayload(List.of(ids));
            });

    @Override
    public ResourceLocation id() {
        return CODEC.id();
    }
}
