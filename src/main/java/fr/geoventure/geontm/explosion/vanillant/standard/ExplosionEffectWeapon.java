package fr.geoventure.geontm.explosion.vanillant.standard;

import fr.geoventure.geontm.explosion.vanillant.ExplosionVNT;
import fr.geoventure.geontm.explosion.vanillant.interfaces.IExplosionSFX;
import fr.geoventure.geontm.particle.helper.ParticleEffects;
import net.minecraft.world.level.Level;

/** The look a warhead wears: a knot of fireballs, sized by the caller. */
public class ExplosionEffectWeapon implements IExplosionSFX {

    private final int cloudCount;
    private final float cloudScale;
    private final float cloudSpeedMult;

    public ExplosionEffectWeapon(int cloudCount, float cloudScale, float cloudSpeedMult) {
        this.cloudCount = cloudCount;
        this.cloudScale = cloudScale;
        this.cloudSpeedMult = cloudSpeedMult;
    }

    @Override
    public void doEffect(ExplosionVNT explosion, Level world, double x, double y, double z, float size) {
        if (world.isClientSide()) return;
        ParticleEffects.explosionSmall(world, x, y, z, cloudCount, cloudScale, cloudSpeedMult);
    }
}
