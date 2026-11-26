package fr.geoventure.geontm.saveddata.satellites;

import fr.geoventure.geocore.Ids;
import fr.geoventure.geontm.entity.ModNtmEntities;
import fr.geoventure.geontm.entity.logic.EntityDeathBlast;
import fr.geoventure.geontm.items.machine.ItemSatellite.EnumSatType;
import fr.geoventure.geontm.pont.PontGuerre;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.Locale;

/** 1.7 {@code SatelliteDeathRay}, the orbital death ray: one {@link EntityDeathBlast} every five minutes. */
public class SatelliteDeathRay extends Satellite {

    public static final MapCodec<SatelliteDeathRay> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.LONG.optionalFieldOf("lastShot", 0L).forGetter(sat -> sat.lastShot)
    ).apply(i, SatelliteDeathRay::new));

    public static final String CMD_FIRE = "fire";
    public static final String CMD_CANFIRE = "canfire";

    public static final int CHARGE_TIME = 5 * 60 * 20;

    /** World time of the last shot, in ticks. */
    public long lastShot;

    public SatelliteDeathRay() {
        this(0L);
    }

    private SatelliteDeathRay(long lastShot) {
        this.lastShot = lastShot;
    }

    @Override
    public SatelliteType type() {
        return SatelliteType.DEATH_RAY;
    }

    @Override public String getType() { return "ORBITAL_FUN_PLATFORM_:)"; }

    @Override
    public Component[] getInfo(Level level) {
        boolean canFire = lastShot + CHARGE_TIME < level.getGameTime();
        int cooldown = (int) ((lastShot + CHARGE_TIME) - level.getGameTime());
        return new Component[] {
                nameOf(EnumSatType.DEATH_RAY),
                canFire ? Component.translatable("satellite.ready") : Component.translatable("satellite.cooldown", cooldown / 20 + "s")
        };
    }

    @Override
    public void onCommandImpl(Level level, String... cmd) {
        if (cmd.length <= 0) return;

        if (cmd[0].equals(CMD_FIRE)) {
            if (level instanceof ServerLevel server) deathBlast(server, targetX, targetZ);
            return;
        }

        if (cmd[0].equals(CMD_CANFIRE)) {
            this.tx = ("" + (lastShot + CHARGE_TIME < level.getGameTime())).toUpperCase(Locale.US);
        }
    }

    private static final ResourceLocation ID_ARME = Ids.of("geontm", "sat_laser");

    @Override
    public void onCoordAction(ServerLevel level, Player player, int x, int y, int z) {
        this.setTarget(x, z);
        this.deathBlast(level, targetX, targetZ, player);
    }

    public void deathBlast(ServerLevel level, int x, int z) {
        deathBlast(level, x, z, null);
    }

    public void deathBlast(ServerLevel level, int x, int z, @Nullable Player player) {
        if (lastShot + CHARGE_TIME < level.getGameTime()) {
            int y = Heights.heightValue(level, x, z);
            BlockPos cible = new BlockPos(x, y, z);
            BlockPos origine = player != null ? player.blockPosition() : cible;
            PontGuerre.FrappeOrbitale frappe = PontGuerre.autoriserFrappeOrbitale(level, origine, cible, ID_ARME, player);
            if (frappe == null) return;

            lastShot = level.getGameTime();
            this.markDirty();

            EntityDeathBlast blast = new EntityDeathBlast(ModNtmEntities.LASER_BLAST.get(), level);
            blast.frappeLancement = frappe.lancement();
            blast.frappeId = frappe.frappeId();
            // 1.7 dropped the entity into an unloaded column as readily as a loaded one; the claim keeps its
            // sixty-tick timer running there
            blast.anchorAndLoad(level, x >> 4, z >> 4);
            blast.setPos(x, y, z);
            level.addFreshEntity(blast);
        }
    }
}
