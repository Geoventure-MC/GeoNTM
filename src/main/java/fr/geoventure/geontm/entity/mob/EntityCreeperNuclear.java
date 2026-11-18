package fr.geoventure.geontm.entity.mob;

import fr.geoventure.geocore.world.Protection;
import fr.geoventure.geontm.items.ModAmmoItems;
import fr.geoventure.geontm.particle.helper.EntityEffects;
import fr.geoventure.geocore.world.Compat;
import fr.geoventure.geontm.advancement.DetonationTrigger;
import fr.geoventure.geontm.advancement.GeoNtmCriteria;
import fr.geoventure.geontm.entity.logic.EntityNukeExplosionMK5;
import fr.geoventure.geontm.explosion.ExplosionNukeGeneric;
import fr.geoventure.geontm.explosion.ExplosionNukeSmall;
import fr.geoventure.geontm.items.ModItems;
import fr.geoventure.geontm.items.weapon.sedna.factory.GunFactory;
import fr.geoventure.geontm.lib.ModDamageSources;
import fr.geoventure.geontm.sound.ModSounds;
import fr.geoventure.geontm.util.ContaminationUtil;
import fr.geoventure.geontm.util.ContaminationUtil.ContaminationType;
import fr.geoventure.geontm.util.ContaminationUtil.HazardType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

public class EntityCreeperNuclear extends EntityCreeperBase {

    public EntityCreeperNuclear(EntityType<? extends EntityCreeperNuclear> type, Level level) {
        super(type, level);
        setMaxSwell(75);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Creeper.createAttributes()
                      .add(Attributes.MAX_HEALTH, 50.0D)
                      .add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {

        // 1.7: the nuclear blast would damage the already dead creeper, reviving it into its death animation
        if (isRemoved() || dead) return false; // dead: set by EntityCreeperBase.tick while the blast runs (1.7 setDead() came first)

        if (source.is(ModDamageSources.RADIATION) || source.is(ModDamageSources.MUD_POISONING)) {
            if (isAlive()) heal(amount);
            return false;
        }

        return super.hurtServer(level, source, amount);
    }


    @Override
    public void die(DamageSource source) {
        super.die(source);

        if (level() instanceof ServerLevel server) {
            for (Player player : server.getEntitiesOfClass(Player.class, getBoundingBox().inflate(50D))) {
                if (player instanceof ServerPlayer serverPlayer) {
                    GeoNtmCriteria.detonation(serverPlayer, DetonationTrigger.Kind.NUCLEAR_CREEPER);
                }
            }
        }

        // 1.7 EntitySkeleton was also the wither skeleton; AbstractSkeleton additionally admits Stray and Bogged
        boolean shotBySkeleton = source.getEntity() instanceof AbstractSkeleton;
        boolean strayArrow = source.getDirectEntity() instanceof Arrow arrow && arrow.getOwner() == null;

        if ((shotBySkeleton || strayArrow) && level() instanceof ServerLevel server) {
            spawnAtLocation(server, new ItemStack(ModAmmoItems.nuke_standard.get())); // 1.7: ModItems.ammo_nuke
        }
    }

    @Override
    public void tick() {

        if (level() instanceof ServerLevel server) {
            for (Entity e : server.getEntities(this, getBoundingBox().inflate(5, 5, 5))) {
                if (e instanceof LivingEntity living) {
                    ContaminationUtil.contaminate(living, HazardType.RADIATION, ContaminationType.CREATIVE, 0.25F);
                }
            }
        }

        super.tick();

        if (isAlive() && getHealth() < getMaxHealth() && tickCount % 10 == 0) heal(1.0F);
    }

    @Override
    protected void detonate(ServerLevel level) {

        boolean griefing = Compat.canEntityGrief(level, this);

        if (isPowered()) {
            // 1.7 sent no balefire key, which the client read as false
            EntityEffects.muke(level, getX(), getY() + 0.5D, getZ(), false, false, 250);
            level.playSound(null, getX(), getY() + 0.5D, getZ(), ModSounds.MUKE_EXPLOSION.get(),
                    SoundSource.BLOCKS, 15.0F, 1.0F);

            if (griefing) {
                level.addFreshEntity(EntityNukeExplosionMK5.statFac(level, 50, getX(), getY(), getZ()));
            } else {
                ExplosionNukeGeneric.dealDamage(level, getX(), getY() + 0.5D, getZ(), 100);
            }
        } else {
            ExplosionNukeSmall.explode(level, Protection.Source.NONE, getX(), getY() + 0.5D, getZ(),
                    griefing ? ExplosionNukeSmall.PARAMS_MEDIUM : ExplosionNukeSmall.PARAMS_SAFE);
        }
    }
}
