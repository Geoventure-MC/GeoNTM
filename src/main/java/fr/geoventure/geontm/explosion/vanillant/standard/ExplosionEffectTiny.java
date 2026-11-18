package fr.geoventure.geontm.explosion.vanillant.standard;

import fr.geoventure.geontm.explosion.vanillant.ExplosionVNT;
import fr.geoventure.geontm.explosion.vanillant.interfaces.IExplosionSFX;
import fr.geoventure.geontm.sound.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

/** The smallest blast: one vanilla puff and a report that carries further than it looks like it should. */
public class ExplosionEffectTiny implements IExplosionSFX {

    @Override
    public void doEffect(ExplosionVNT explosion, Level world, double x, double y, double z, float size) {
        if (!(world instanceof ServerLevel level)) return;

        world.playSound(null, x, y, z, ModSounds.EXPLOSION_TINY.get(), SoundSource.BLOCKS, 15.0F, 1.0F);
        level.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
    }
}
