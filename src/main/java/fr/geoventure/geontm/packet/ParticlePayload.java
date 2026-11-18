package fr.geoventure.geontm.packet;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.net.GeoCoreCodec;
import fr.geoventure.geocore.net.GeoCorePayload;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

/**
 * S2C: play one of HBM's own particle effects at a world position: the 26.1 equivalent of the old
 * {@code AuxParticlePacketNT}.
 *
 * <p>HBM's particles are <i>not</i> registered as vanilla {@code ParticleType}s. One
 * effect can mean "spawn 40 particles of four different classes, coloured from this recipe and aimed
 * along this vector", and the parameters ride along as an arbitrary {@link CompoundTag}. Vanilla
 * {@code ParticleOptions} cannot express that without one registered type (and one codec) per effect,
 * so the effect is named here and the client rebuilds the particles from the tag.
 *
 * <p>Two deviations from 1.12.2:
 *
 * <ul>
 *   <li>The effect is keyed by <b>name</b>, not by enum ordinal. Upstream wrote the ordinal, which
 *       silently remaps every effect when someone inserts an entry mid-enum. Index-keyed bullet
 *       configs in the gun code had the same trap and get the same fix.
 *   <li>Delivery is by <b>chunk tracking</b> rather than upstream's {@code TargetPoint} radius. The
 *       port has no radius-based sender; {@code sendToTracking} is what the BlockEntity sync already
 *       uses, and a player who has the chunk loaded is the population that could see the effect
 *       anyway. Effects with a range far larger than a chunk view would need a real radius sender.
 * </ul>
 */
public record ParticlePayload(String effect, double x, double y, double z, CompoundTag data)
        implements GeoCorePayload {

    public static final GeoCoreCodec<ParticlePayload> CODEC = GeoCoreCodec.of(ParticlePayload.class,
            Ids.of(Constants.MOD_ID, "particle"),
            (payload, buffer) -> {
                buffer.writeUtf(payload.effect);
                buffer.writeDouble(payload.x);
                buffer.writeDouble(payload.y);
                buffer.writeDouble(payload.z);
                buffer.writeNbt(payload.data);
            },
            buffer -> new ParticlePayload(buffer.readUtf(), buffer.readDouble(), buffer.readDouble(),
                    buffer.readDouble(), buffer.readNbt()));

    @Override
    public ResourceLocation id() {
        return CODEC.id();
    }
}
