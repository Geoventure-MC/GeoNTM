package fr.geoventure.geontm.entity.projectile;

import fr.geoventure.geocore.data.DataIn;
import fr.geoventure.geocore.data.DataOut;
import fr.geoventure.geontm.entity.ModNtmEntities;
import fr.geoventure.geontm.pont.PontGuerre;
import fr.geoventure.geontm.sound.ModSounds;
import fr.geoventure.geontm.util.ChunkUtil;
import java.util.UUID;
import net.minecraft.sounds.SoundSource;
import fr.geoventure.geontm.entity.effect.EntityCloudTom;
import fr.geoventure.geontm.entity.logic.EntityTomBlast;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class EntityTom extends EntityThrowableNT {

    private static final long NONE = Long.MIN_VALUE;
    private long anchorChunk = NONE;
    public @Nullable UUID frappeLancement;
    public @Nullable String frappeId;
    private boolean frappeClose;

    public EntityTom(EntityType<? extends EntityTom> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        ensureAnchor();
        this.xo = getX();
        this.yo = getY();
        this.zo = getZ();

        Vec3 motion = getDeltaMovement();
        setPos(getX() + motion.x, getY() + motion.y, getZ() + motion.z);

        if (this.tickCount % 100 == 0) {
            level().playSound(null, getX(), getY(), getZ(), ModSounds.ALARM_CHIME.get(), SoundSource.RECORDS,
                    10000.0F, 1.0F);
        }

        setDeltaMovement(motion.x, -0.5D, motion.z);

        // 1.7: getBlock((int) posX, ...) != Blocks.air - int truncation, not floor
        if (!level().getBlockState(new BlockPos((int) getX(), (int) getY(), (int) getZ())).isAir() || getY() < level().getMinBuildHeight() + 10) {
            if (!level().isClientSide()) {
                this.frappeClose = true;
                PontGuerre.impactOrbital(level(), frappeLancement, frappeId, blockPosition(), () -> {
                    EntityTomBlast tom = new EntityTomBlast(ModNtmEntities.TOM_BUST.get(), level());
                    tom.setPos(getX(), getY(), getZ());
                    tom.destructionRange = 600;
                    level().addFreshEntity(tom);

                    level().addFreshEntity(EntityCloudTom.statFac(level(), 500, getX(), getY(), getZ()));
                });
            }
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
    protected void loadData(DataIn input) {
        super.loadData(input);
        String lancement = input.getStringOr("frappeLancement", "");
        frappeLancement = lancement.isEmpty() ? null : UUID.fromString(lancement);
        String frappe = input.getStringOr("frappeId", "");
        frappeId = frappe.isEmpty() ? null : frappe;
        PontGuerre.restaurerAutorisation(frappeId, input);
    }

    @Override
    protected void saveData(DataOut output) {
        super.saveData(output);
        if (frappeLancement != null) output.putString("frappeLancement", frappeLancement.toString());
        if (frappeId != null) {
            output.putString("frappeId", frappeId);
            PontGuerre.sauvegarderAutorisation(frappeId, output);
        }
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

    @Override
    protected void onImpact(HitResult mop) {
        // 1.7: empty override - detonation comes from the solid-block probe above
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 500000D;
    }

    // 26.1: 1.7's fullbright getBrightnessForRender/getBrightness have no common-code override left - RenderCloudTom
    // emits the packed light.
}
