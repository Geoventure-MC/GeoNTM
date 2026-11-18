package fr.geoventure.geontm.api.block;

import fr.geoventure.geontm.entity.item.EntityTntNtm;
import net.minecraft.world.level.Level;

// 1.7: api.geontm.block.IFuckingExplode: primes EntityTntNtm, not chain-exploding
public interface IFuckingExplode {

    void explodeEntity(Level level, double x, double y, double z, EntityTntNtm entity);
}
