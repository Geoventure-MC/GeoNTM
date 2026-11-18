package fr.geoventure.geontm.entity.mob;

import fr.geoventure.geocore.world.Protection;
import fr.geoventure.geocore.world.Explosions;
import fr.geoventure.geontm.advancement.AwardRegions;
import fr.geoventure.geontm.advancement.GeoNtmCriteria;
import fr.geoventure.geontm.blocks.bomb.ModBombBlocks;
import fr.geoventure.geontm.config.ServerConfig;
import fr.geoventure.geontm.lib.ModDamageSources;
import fr.geoventure.geocore.world.Compat;
import fr.geoventure.geontm.blocks.bomb.BlockTaint;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import fr.geoventure.geontm.interfaces.IRadiationImmune;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

public class EntityCreeperTainted extends EntityCreeperBase implements IRadiationImmune {

    public EntityCreeperTainted(EntityType<? extends EntityCreeperTainted> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Creeper.createAttributes()
                      .add(Attributes.MAX_HEALTH, 15.0D)
                      .add(Attributes.MOVEMENT_SPEED, 0.35D);
    }

    @Override
    public void tick() {
        super.tick();

        if (isAlive() && getHealth() < getMaxHealth() && tickCount % 10 == 0) heal(1.0F);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        // 1.7 ModEventHandler.onEntityDeath: bobHidden for every player within 50 of a tainted creeper a boxcar
        // crushed
        if (level() instanceof ServerLevel server && source.is(ModDamageSources.BOXCAR)) {
            String kind = EntityType.getKey(getType()).toString();
            AwardRegions.nearby(server, getBoundingBox().inflate(50D),
                    player -> GeoNtmCriteria.CRUSHED.fire(player, kind));
        }
    }


    @Override
    protected void detonate(ServerLevel level) {
        Explosions.explode(level, this, getX(), getY(), getZ(), 5.0F, Level.ExplosionInteraction.NONE);

        if (!Compat.canEntityGrief(level, this)) return;

        // ENTITY_TAINT_TRAILS keeps the scatter young so it keeps creeping; without it the taint starts old
        boolean trails = ServerConfig.ENTITY_TAINT_TRAILS.get(); // 1.7 read the /ntmserver key

        if (isPowered()) {
            scatter(level, 255, 15, 7, trails ? 0 : 5, 3);
        } else {
            scatter(level, 85, 7, 3, trails ? 4 : 10, trails ? 3 : 6);
        }
    }

    private void scatter(ServerLevel level, int count, int spread, int offset, int ageBase, int ageSpread) {
        Protection.Gate gate = Protection.gate(level, Protection.Source.NONE, Protection.Cause.FLUID);
        for (int i = 0; i < count; i++) {
            BlockPos pos = new BlockPos(
                    random.nextInt(spread) + (int) getX() - offset,
                    random.nextInt(spread) + (int) getY() - offset,
                    random.nextInt(spread) + (int) getZ() - offset);

            BlockState state = level.getBlockState(pos);
            if (!Compat.isSolidRender(state) || state.isAir() || !gate.mayAlter(pos)) continue;

            level.setBlock(pos, ModBombBlocks.taint.get().defaultBlockState()
                    .setValue(BlockTaint.TAINTAGE, random.nextInt(ageSpread) + ageBase), Block.UPDATE_CLIENTS);
        }
    }
}
