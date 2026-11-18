package fr.geoventure.geontm.tileentity.machine;

import fr.geoventure.geontm.capability.GeoNtmLivingProps;
import fr.geoventure.geontm.particle.helper.GeoNtmEffectNT;
import fr.geoventure.geontm.potion.ModEffects;
import fr.geoventure.geontm.tileentity.ModBlockEntities;
import fr.geoventure.geocore.hook.Hook;
import fr.geoventure.geocore.world.Effects;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

/** Player decontaminator: clears absorbed radiation, potion radiation and timed contamination. */
public final class BlockEntityDecon extends BlockEntity {
    private static final double RAD_REMOVE = 0.5D;

    /** The living things the decontaminator just cleaned, when there were any. */
    @FunctionalInterface
    public interface Decontaminated {
        void decontaminated(ServerLevel level, BlockPos pos, List<LivingEntity> entities);
    }

    /** Called after the clean-up of each tick with someone inside (H-80); plain GEONTM registers none. */
    public static final Hook<Decontaminated> DECONTAMINATED = Hook.notifying();

    public BlockEntityDecon(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DECON.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BlockEntityDecon decon) {
        if (!(level instanceof ServerLevel server)) return;
        AABB area = new AABB(pos.getX() - 0.5D, pos.getY(), pos.getZ() - 0.5D,
                pos.getX() + 1.5D, pos.getY() + 2D, pos.getZ() + 1.5D);
        List<LivingEntity> entities = server.getEntitiesOfClass(LivingEntity.class, area);
        for (LivingEntity entity : entities) {
            GeoNtmLivingProps.incrementRadiation(entity, -RAD_REMOVE);
            Effects.removeRegistered(entity, ModEffects.RADIATION.get());
            GeoNtmLivingProps.getCont(entity).clear();
        }
        if (!entities.isEmpty()) {
            for (Decontaminated listener : DECONTAMINATED) listener.decontaminated(server, pos, entities);
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, BlockEntityDecon decon) {
        GeoNtmEffectNT.DeconAura.summonParticle(level,
                pos.getX() + 0.125D + level.getRandom().nextDouble() * 0.75D,
                pos.getY() + 1.1D,
                pos.getZ() + 0.125D + level.getRandom().nextDouble() * 0.75D,
                null);
    }
}
