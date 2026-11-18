package fr.geoventure.geontm.explosion.vanillant.interfaces;

import fr.geoventure.geontm.explosion.vanillant.ExplosionVNT;
import net.minecraft.world.entity.Entity;

/** An extra effect applied to every entity the explosion damaged, given its scaled distance. */
public interface ICustomDamageHandler {

    void handleAttack(ExplosionVNT explosion, Entity entity, double distanceScaled);
}
