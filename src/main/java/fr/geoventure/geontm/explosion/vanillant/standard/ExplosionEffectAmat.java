package fr.geoventure.geontm.explosion.vanillant.standard;

import fr.geoventure.geontm.explosion.vanillant.ExplosionVNT;
import fr.geoventure.geontm.explosion.vanillant.interfaces.IExplosionSFX;
import fr.geoventure.geontm.particle.helper.GeoNtmEffectNT;
import fr.geoventure.geontm.sound.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

/**
 * 1.7 {@code ExplosionEffectAmat}: below size 15 the vanilla explosion sound (volume 4, pitch 0.7 around 1.4),
 * from 15 up the mini-nuke boom; then the {@code amat} flash ({@code ParticleAmatFlash}) for everyone within
 * 200 blocks, scaled by the explosion size.
 */
public class ExplosionEffectAmat implements IExplosionSFX {

    @Override
    public void doEffect(ExplosionVNT explosion, Level world, double x, double y, double z, float size) {
        if (world.isClientSide()) return;
        RandomSource rand = world.getRandom();
        if (size < 15) {
            world.playSound(null, x, y, z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F,
                    (1.4F + (rand.nextFloat() - rand.nextFloat()) * 0.2F) * 0.7F);
        } else {
            world.playSound(null, x, y, z, ModSounds.MUKE_EXPLOSION.get(), SoundSource.BLOCKS, 15.0F, 1.0F);
        }
        CompoundTag data = new CompoundTag();
        data.putFloat("scale", size);
        GeoNtmEffectNT.AmatFlash.send(world, x, y, z, data, 200);
    }
}
