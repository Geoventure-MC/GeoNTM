package fr.geoventure.geontm.blocks.generic;

import fr.geoventure.geontm.config.GeneralConfig;
import fr.geoventure.geontm.handler.ArmorUtil;
import fr.geoventure.geontm.lib.ModDamageSources;
import fr.geoventure.geontm.util.ArmorRegistry;
import fr.geoventure.geocore.world.Compat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/** Carbon monoxide, ported from the old {@code BlockGasMonoxide}. */
public class BlockGasMonoxide extends BlockGasBase {
    public BlockGasMonoxide(Properties properties) {
        super(properties);
        cloudColor(0.1F, 0.1F, 0.1F);
    }

    @Override
    protected void onEntityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide() || !GeneralConfig.enableCarbonMonoxide || !(entity instanceof LivingEntity living)) return;
        if (ArmorRegistry.hasAllProtection(living, EquipmentSlot.HEAD, ArmorRegistry.HazardClass.GAS_MONOXIDE)) {
            ArmorUtil.damageGasMaskFilter(living, 1);
        } else if (level instanceof ServerLevel server) {
            Compat.hurt(living, server, ModDamageSources.monoxide(server), 1.0F);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!GeneralConfig.enableCarbonMonoxide || random.nextInt(100) == 0) {
            level.removeBlock(pos, false);
            return;
        }
        drift(level, state, pos, random, Direction.DOWN, randomHorizontal(random));
    }
}
