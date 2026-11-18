package fr.geoventure.geontm.explosion.vanillant.interfaces;

import fr.geoventure.geontm.explosion.vanillant.ExplosionVNT;

/** Scales the entity-damage radius independently of the block radius. */
public interface IEntityRangeMutator {

    float mutateRange(ExplosionVNT explosion, float range);
}
