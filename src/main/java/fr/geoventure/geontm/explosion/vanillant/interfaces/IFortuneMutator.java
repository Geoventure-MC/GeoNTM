package fr.geoventure.geontm.explosion.vanillant.interfaces;

import fr.geoventure.geontm.explosion.vanillant.ExplosionVNT;
import net.minecraft.world.level.block.Block;

/** Grants a fortune level to the blocks an explosion drops. */
public interface IFortuneMutator {

    int mutateFortune(ExplosionVNT explosion, Block block, int x, int y, int z);
}
