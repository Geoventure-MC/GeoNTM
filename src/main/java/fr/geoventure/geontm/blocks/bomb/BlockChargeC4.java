package fr.geoventure.geontm.blocks.bomb;

import fr.geoventure.geocore.world.Protection;
import fr.geoventure.geontm.util.Owners;
import fr.geoventure.geontm.explosion.vanillant.ExplosionVNT;
import fr.geoventure.geontm.explosion.vanillant.standard.BlockAllocatorStandard;
import fr.geoventure.geontm.explosion.vanillant.standard.BlockProcessorStandard;
import fr.geoventure.geontm.explosion.vanillant.standard.EntityProcessorStandard;
import fr.geoventure.geontm.explosion.vanillant.standard.PlayerProcessorStandard;
import fr.geoventure.geontm.particle.helper.ExplosionCreator;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

/** 1.7 {@code BlockChargeC4}: demolition charge, VNT 15 at 32 rays, no block drops. */
public class BlockChargeC4 extends BlockChargeBase {

    public BlockChargeC4(Properties properties) {
        super(properties);
    }

    @Override
    public BombReturnCode explode(Level level, BlockPos pos, @Nullable Entity detonator) {
        if (level.isClientSide()) return BombReturnCode.UNDEFINED;
        Protection.Source source = Owners.source(detonator, level, pos);
        removeSafely(level, pos);
        double x = pos.getX(), y = pos.getY(), z = pos.getZ();

        ExplosionVNT xnt = new ExplosionVNT(level, x + 0.5, y + 0.5, z + 0.5, 15F);
        xnt.setSource(source);
        xnt.setBlockAllocator(new BlockAllocatorStandard(32));
        xnt.setBlockProcessor(new BlockProcessorStandard().setNoDrop());
        xnt.setEntityProcessor(new EntityProcessorStandard());
        xnt.setPlayerProcessor(new PlayerProcessorStandard());
        xnt.explode();
        ExplosionCreator.composeEffectSmall(level, x + 0.5, y + 1, z + 0.5);
        return BombReturnCode.DETONATED;
    }

    @Override
    public List<Component> tooltip() {
        List<Component> list = new ArrayList<>(super.tooltip());
        list.add(Component.literal("Does not drop blocks.").withStyle(ChatFormatting.BLUE));
        return list;
    }
}
