package fr.geoventure.geontm.space.mob;

import fr.geoventure.geocore.Ids;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import fr.geoventure.geontm.interfaces.IRadiationImmune;
import fr.geoventure.geontm.space.mob.ai.MoonWalkControl;
import fr.geoventure.geontm.space.mob.SpaceMobs;
import fr.geoventure.geocore.data.CompatCow;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.Nullable;

/**
 * hbm-space {@code EntityMoonCow}: a vanilla cow in a boron-glass helmet that moonwalks ({@link MoonWalkControl}),
 * is muffled by the helmet and drops cheese. Immune to radiation ({@link IRadiationImmune}) and, through the entity
 * tag {@code ntmspace:suffocation_immune}, to the vacuum (1.7 {@code ISuffocationImmune}).
 */
public class EntityMoonCow extends CompatCow implements IRadiationImmune {

    public EntityMoonCow(EntityType<? extends EntityMoonCow> type, Level level) {
        super(type, level);
        this.moveControl = new MoonWalkControl(this);
    }

    @Override
    public @Nullable EntityMoonCow getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return fr.geoventure.geocore.registry.Entities.create(SpaceMobs.MOON_COW.get(), level, MobSpawnType.BREEDING);
    }

    /** 1.7: muffled by the helmet. */
    @Override
    protected float getSoundVolume() {
        return 0.1F;
    }

    /**
     * 1.7 {@code EntityAnimal.getCanSpawnHere} for the periodic spawner: grass below and a full light value above 8.
     * The Mun's turf is not grass, so in hbm-space Mun cows appeared only while chunks generated (1.7's
     * {@code performWorldGenSpawning}, which never asked this; the biome's spawning chance is 0.008) - kept as found.
     */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType reason) {
        if (level.getBiome(blockPosition()).is(
                ResourceKey.create(Registries.BIOME,
                        Ids.of("geontm", "mun")))) return false;
        if (reason == MobSpawnType.NATURAL) {
            BlockPos pos = blockPosition();
            return level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK) && level.getRawBrightness(pos, 0) > 8;
        }
        return true;
    }
}
