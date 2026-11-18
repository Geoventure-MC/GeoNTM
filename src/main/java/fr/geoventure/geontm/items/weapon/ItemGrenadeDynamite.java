package fr.geoventure.geontm.items.weapon;

import fr.geoventure.geontm.explosion.vanillant.ExplosionVNT;
import fr.geoventure.geontm.explosion.vanillant.standard.EntityProcessorCrossSmooth;
import fr.geoventure.geontm.explosion.vanillant.standard.ExplosionEffectWeapon;
import fr.geoventure.geontm.explosion.vanillant.standard.PlayerProcessorStandard;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

/**
 * 1.7 ItemGrenadeDynamite. Entities and players only - 1.7 sets no block allocator/processor, so the blast leaves terrain intact.
 */
public class ItemGrenadeDynamite extends ItemGenericGrenade {

    public ItemGrenadeDynamite(Properties properties, int fuse) {
        super(properties, fuse);
    }

    @Override
    public void explode(Entity grenade, LivingEntity thrower, Level level, double x, double y, double z) {
        ExplosionVNT vnt = new ExplosionVNT(level, x, y, z, 5, thrower);
        vnt.setEntityProcessor(new EntityProcessorCrossSmooth(1, 15));
        vnt.setPlayerProcessor(new PlayerProcessorStandard());
        vnt.setSFX(new ExplosionEffectWeapon(10, 2.5F, 1F));
        vnt.explode();
    }
}
