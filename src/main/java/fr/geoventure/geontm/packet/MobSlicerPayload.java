package fr.geoventure.geontm.packet;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.items.weapon.MobSlicer;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.net.GeoCoreCodec;
import fr.geoventure.geocore.net.GeoCorePayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

/**
 * C2S: 1.12.2 {@code PacketMobSlicer}: the drawn cut, as the point one block along the starting look direction
 * ({@code startPos + eyes}), the plane's normal and the cutter's texture id. Three doubles, three doubles, one byte.
 */
public record MobSlicerPayload(double x, double y, double z, double nx, double ny, double nz, byte tex)
        implements GeoCorePayload {

    public static final GeoCoreCodec<MobSlicerPayload> CODEC = GeoCoreCodec.of(MobSlicerPayload.class,
            Ids.of(Constants.MOD_ID, "mob_slicer"),
            (payload, buffer) -> {
                buffer.writeDouble(payload.x);
                buffer.writeDouble(payload.y);
                buffer.writeDouble(payload.z);
                buffer.writeDouble(payload.nx);
                buffer.writeDouble(payload.ny);
                buffer.writeDouble(payload.nz);
                buffer.writeByte(payload.tex);
            },
            buffer -> new MobSlicerPayload(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(),
                    buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readByte()));

    /**
     * {@code PacketMobSlicer.Handler}: only for a cutter in the main hand, and only a sane cut (see
     * {@link MobSlicer#sliceFromClient}); the packet's {@code tex} is ignored in favour of the held cutter's.
     */
    public static void handleServer(MobSlicerPayload payload, ServerPlayer player) {
        MobSlicer.sliceFromClient(player, new Vec3(payload.x, payload.y, payload.z),
                new Vec3(payload.nx, payload.ny, payload.nz));
    }

    @Override
    public Identifier id() {
        return CODEC.id();
    }
}
