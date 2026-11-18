package fr.geoventure.geontm.explosion.vanillant.standard;

import fr.geoventure.geontm.explosion.vanillant.ExplosionVNT;
import fr.geoventure.geontm.explosion.vanillant.interfaces.IBlockMutator;
import fr.geoventure.geocore.world.Compat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class BlockMutatorBulkie implements IBlockMutator {

    protected Supplier<BlockState> target;

    public BlockMutatorBulkie(BlockState target) {
        this(() -> target);
    }

    public BlockMutatorBulkie(Supplier<BlockState> target) {
        this.target = target;
    }

    @Override
    public void mutatePre(ExplosionVNT explosion, BlockState blockState, BlockPos pos) {

        if (!Compat.isSolidRender(blockState)) return;

        Vec3 vec = new Vec3(pos.getX() + 0.5 - explosion.posX, pos.getY() + 0.5 - explosion.posY,
                pos.getZ() + 0.5 - explosion.posZ);

        if (vec.length() >= explosion.size - 0.5) {
            explosion.world.setBlock(pos, target.get(), 3);
        }
    }

    @Override
    public void mutatePost(ExplosionVNT explosion, BlockPos pos) {
    }
}
