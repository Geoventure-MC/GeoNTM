package fr.geoventure.geontm.entity.mob;

import fr.geoventure.geontm.entity.ModNtmEntities;
import fr.geoventure.geocore.data.CompatChicken;
import fr.geoventure.geontm.sound.ModSounds;
import fr.geoventure.geocore.registry.Entities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class EntityDuck extends CompatChicken {

    public EntityDuck(EntityType<? extends EntityDuck> type, Level level) {
        super(type, level);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.ENTITY_DUCC.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.ENTITY_DUCC.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.ENTITY_DUCC.get();
    }

    @Override
    public @Nullable Chicken getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return Entities.create(ModNtmEntities.DUCK.get(), level, MobSpawnType.BREEDING);
    }

    /** 1.7 overrode neither drop method, so it kept {@code EntityChicken.dropFewItems}. */

    @Override
    public void die(DamageSource source) {
        // 1.7 broadcast every duck death to the whole server.
        if (level() instanceof ServerLevel server) {
            server.getServer().getPlayerList().broadcastSystemMessage(getCombatTracker().getDeathMessage(), false);
        }

        super.die(source);
    }
}
