package fr.geoventure.geontm.particle.helper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

/**
 * 1.7 FlameCreator: fire-blob dispatch (ntm-next's common-side spelling). Routes onto this port's
 * {@link GeoNtmEffectNT#Flamethrower} effect; the meta constants match the client
 * {@code fr.geoventure.geontm.client.particle.FlameCreator} colour bands.
 */
public class FlameCreator {

    public static final int META_FIRE = 0;
    public static final int META_NORMAL = 0;
    public static final int META_BALEFIRE = 1;
    public static final int META_DIGAMMA = 2;
    public static final int META_OXY = 3;
    public static final int META_BLACK = 4;

    // 1.7 IParticleCreator.sendPacket radius.
    private static final double RANGE = 50D;

    public static void composeEffect(Level level, double x, double y, double z, int meta) {
        CompoundTag data = new CompoundTag();
        data.putInt("meta", meta);
        GeoNtmEffectNT.Flamethrower.send(level, x, y, z, data, RANGE);
    }

    public static void composeEffectClient(Level level, double x, double y, double z, int meta) {
        CompoundTag data = new CompoundTag();
        data.putInt("meta", meta);
        GeoNtmEffectNT.Flamethrower.summonParticle(level, x, y, z, data);
    }
}
