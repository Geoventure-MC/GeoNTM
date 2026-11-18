package fr.geoventure.geontm.entity.projectile.rocketbehavior;

import fr.geoventure.geontm.entity.projectile.EntityArtilleryRocket;
import net.minecraft.world.entity.Entity;

/** Recalculates where a rocket should aim; {@link IRocketSteeringBehavior} then does the chasing. */
public interface IRocketTargetingBehavior {

    void recalculateTargetPosition(EntityArtilleryRocket rocket, Entity target);
}
