package fr.geoventure.geontm.explosion.vanillant.standard;

import fr.geoventure.geontm.explosion.vanillant.ExplosionVNT;
import fr.geoventure.geontm.explosion.vanillant.interfaces.IBlockMutator;
import fr.geoventure.geocore.world.Compat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/** Leaves fire in a third of the positions the blast cleared, if the block below can hold it. */
public class BlockMutatorFire implements IBlockMutator {

    @Override
    public void mutatePre(ExplosionVNT explosion, BlockState state, BlockPos pos) {}

    @Override
    public void mutatePost(ExplosionVNT explosion, BlockPos pos) {
        BlockState here = explosion.world.getBlockState(pos);
        BlockState below = explosion.world.getBlockState(pos.below());

        if (here.isAir() && Compat.isSolidRender(below, explosion.world, pos.below())
                && explosion.world.getRandom().nextInt(3) == 0) {
            explosion.world.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
        }
    }
}
