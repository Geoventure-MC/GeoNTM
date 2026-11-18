package fr.geoventure.geontm.blocks.bomb;

import fr.geoventure.geocore.world.Protection;
import fr.geoventure.geontm.util.Owners;
import fr.geoventure.geontm.explosion.vanillant.ExplosionVNT;
import fr.geoventure.geontm.explosion.vanillant.standard.BlockAllocatorStandard;
import fr.geoventure.geontm.explosion.vanillant.standard.BlockProcessorStandard;
import fr.geoventure.geontm.particle.helper.ExplosionSmallCreator;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

/**
 * 1.7 {@code BlockChargeMiner}: timed mining charge. Upstream: {@code ExplosionNT(4F)} with the
 * NOHURT and ALLDROP attributes. VNT equivalent: standard 16-ray allocation, every block dropped, and
 * no entity processor at all (which is what NOHURT amounts to).
 */
public class BlockChargeMiner extends BlockChargeBase {

    public BlockChargeMiner(Properties properties) {
        super(properties);
    }

    @Override
    public BombReturnCode explode(Level level, BlockPos pos, @Nullable Entity detonator) {
        if (level.isClientSide()) return BombReturnCode.UNDEFINED;
        Protection.Source source = Owners.source(detonator, level, pos);
        removeSafely(level, pos);
        double x = pos.getX(), y = pos.getY(), z = pos.getZ();

        ExplosionVNT exp = new ExplosionVNT(level, x + 0.5, y + 0.5, z + 0.5, 4F);
        exp.setSource(source);
        exp.setBlockAllocator(new BlockAllocatorStandard());
        exp.setBlockProcessor(new BlockProcessorStandard().setAllDrop());
        exp.explode();
        ExplosionSmallCreator.composeEffect(level, x + 0.5, y + 0.5, z + 0.5, 15, 3F, 1.25F);
        return BombReturnCode.DETONATED;
    }

    @Override
    public List<Component> tooltip() {
        List<Component> list = new ArrayList<>(super.tooltip());
        list.add(Component.literal("Will drop all blocks.").withStyle(ChatFormatting.BLUE));
        list.add(Component.literal("Does not do damage.").withStyle(ChatFormatting.BLUE));
        return list;
    }
}
