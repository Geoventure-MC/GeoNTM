package fr.geoventure.geontm.explosion.vanillant.standard;

import fr.geoventure.geontm.explosion.vanillant.ExplosionVNT;
import fr.geoventure.geontm.explosion.vanillant.interfaces.IDropChanceMutator;
import net.minecraft.world.level.block.Block;

/** A flat drop chance for every block, ignoring the explosion's size. */
public class DropChanceMutatorStandard implements IDropChanceMutator {

    protected float chance;

    public DropChanceMutatorStandard(float chance) {
        this.chance = chance;
    }

    @Override
    public float mutateDropChance(ExplosionVNT explosion, Block block, int x, int y, int z, float dropChance) {
        return this.chance;
    }
}
