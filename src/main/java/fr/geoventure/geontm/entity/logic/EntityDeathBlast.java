package fr.geoventure.geontm.entity.logic;

import fr.geoventure.geontm.particle.helper.EntityEffects;
import fr.geoventure.geocore.data.SynchedData;
import fr.geoventure.geocore.data.CompatEntity;
import fr.geoventure.geontm.entity.projectile.EntityBulletBaseMK4;
import fr.geoventure.geontm.items.weapon.sedna.factory.XFactoryNPC;
import fr.geoventure.geontm.pont.PontGuerre;
import fr.geoventure.geontm.sound.ModSounds;
import fr.geoventure.geontm.util.ChunkUtil;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ChunkPos;
import fr.geoventure.geocore.data.DataIn;
import fr.geoventure.geocore.data.DataOut;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * 1.7 EntityDeathBlast - server-side timer entity placed by SatelliteDeathRay's beam impact;
 * after 60 ticks it detonates a fallout-free MK5 blast of radius 40 with the muke flash and sound.
 */
public class EntityDeathBlast extends CompatEntity {

    public static final int maxAge = 60;
    private static final long NONE = Long.MIN_VALUE;
    private long anchorChunk = NONE;
    public @Nullable UUID frappeLancement;
    public @Nullable String frappeId;
    private boolean frappeClose;

    public EntityDeathBlast(EntityType<? extends EntityDeathBlast> type, Level level) {
        super(type, level);
        // 1.7: ignoreFrustumCheck is renderer-side in 26.1 (see RenderMeteor)
    }

    @Override
    protected void defineData(SynchedData builder) {
    }

    @Override
    protected void loadData(DataIn input) {
        String lancement = input.getStringOr("frappeLancement", "");
        frappeLancement = lancement.isEmpty() ? null : UUID.fromString(lancement);
        String frappe = input.getStringOr("frappeId", "");
        frappeId = frappe.isEmpty() ? null : frappe;
        PontGuerre.restaurerAutorisation(frappeId, input);
    }

    @Override
    protected void saveData(DataOut output) {
        if (frappeLancement != null) output.putString("frappeLancement", frappeLancement.toString());
        if (frappeId != null) {
            output.putString("frappeId", frappeId);
            PontGuerre.sauvegarderAutorisation(frappeId, output);
        }
    }

    // 26.1: hurt() final; 1.7 leaves Entity's default (immune)
    @Override
    protected boolean onHurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void tick() {
        ensureAnchor();
        super.tick();
        if (this.tickCount >= maxAge && !level().isClientSide()) {
            this.frappeClose = true;
            PontGuerre.impactOrbital(level(), frappeLancement, frappeId, blockPosition(), () -> {
                level().addFreshEntity(EntityNukeExplosionMK5.statFacNoRad(level(), 40, getX(), getY(), getZ()));

                // 1.7:42-54 ring of 100 MASKMAN_BOLT bolts; sedna's ctor normalizes the direction, per-shot speed
                // lives in the config (XFactoryNPC class doc)
                int count = 100;
                for (int i = 0; i < count; i++) {
                    Vec3 vec = new Vec3(0.2D, 0D, 0D).yRot((float) (2 * Math.PI * (i / (float) count)));
                    level().addFreshEntity(new EntityBulletBaseMK4(level(), null, XFactoryNPC.maskman_bolt,
                            XFactoryNPC.MASKMAN_BOLT_DAMAGE, 0F, getX(), getY() + 2, getZ(), vec.x, -0.01D, vec.z));
                }

                if (level() instanceof ServerLevel server) {
                    EntityEffects.muke(server, getX(), getY() + 0.5D, getZ(), false, false, 250);
                }
                // 1.7: playSoundEffect "geontm:weapon.mukeExplosion" vol 25 pitch 0.9
                level().playSound(null, getX(), getY(), getZ(), ModSounds.MUKE_EXPLOSION.get(),
                        SoundSource.BLOCKS, 25.0F, 0.9F);
            });
            this.discard();
        }
    }

    public void anchorAndLoad(ServerLevel level, int chunkX, int chunkZ) {
        long chunk = ChunkPos.asLong(chunkX, chunkZ);
        moveAnchor(level, chunk);
    }

    private void ensureAnchor() {
        if (level() instanceof ServerLevel server) {
            moveAnchor(server, ChunkPos.asLong(getBlockX() >> 4, getBlockZ() >> 4));
        }
    }

    private void moveAnchor(ServerLevel level, long chunk) {
        if (anchorChunk == chunk) return;
        ChunkUtil.addAnchor(level, chunk);
        if (anchorChunk != NONE) ChunkUtil.releaseAnchor(level, anchorChunk);
        anchorChunk = chunk;
    }

    @Override
    public void remove(RemovalReason reason) {
        if (!level().isClientSide() && reason.shouldDestroy() && !frappeClose && frappeLancement != null) {
            frappeClose = true;
            PontGuerre.annulerOrbital(level(), frappeLancement, frappeId, reason.name());
        }
        if (level() instanceof ServerLevel server && anchorChunk != NONE) {
            ChunkUtil.releaseAnchor(server, anchorChunk);
        }
        anchorChunk = NONE;
        super.remove(reason);
    }

    // 1.7: getBrightnessForRender/getBrightness fullbright - no per-entity hook in 26.1

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 25000D;
    }
}
