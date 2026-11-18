package fr.geoventure.geontm.pont;

import fr.geoventure.geocore.world.Protection;
import fr.geoventure.geontm.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Relie la protection de GeoCore à l'API publique de GeoMods. GeoNTM y passe déjà pour ses explosions, sa radiation et
 * ses transformations de blocs. Si l'API manque ou échoue sur un serveur, tout est refusé ; seul le solo reste libre.
 */
public final class PontGeoventure implements Protection.Provider {

    private static final String API = "fr.geoventure.geomods.api.GeoProtectionApi";
    private static boolean signale;

    private final Class<?> source, cause, danger;
    private final Method joueur, aucun, frappe, chunk, bloc, entite, acces;

    private PontGeoventure() throws ReflectiveOperationException {
        Class<?> api = Class.forName(API);
        source = Class.forName(API + "$ProtectionSource");
        cause = Class.forName(API + "$BlockCause");
        danger = Class.forName(API + "$EntityHazard");
        joueur = source.getMethod("player", UUID.class);
        aucun = source.getMethod("none");
        frappe = source.getMethod("warStrike", Class.forName("fr.geoventure.geomods.api.WarStrikeGrant"));
        chunk = api.getMethod("checkChunkBlock", Level.class, source, cause, ChunkPos.class);
        bloc = api.getMethod("canModifyBlock", Level.class, source, cause, BlockPos.class);
        entite = api.getMethod("canAffectEntity", Level.class, source, danger, BlockPos.class);
        acces = Class.forName(API + "$ChunkDecision").getMethod("access");
    }

    public static void installer() {
        try {
            Protection.register(new PontGeoventure());
            Constants.LOG.info("[GeoNTM] Protection reliée à GeoMods");
        } catch (ReflectiveOperationException | LinkageError e) {
            Constants.LOG.warn("[GeoNTM] API de protection GeoMods absente : actions refusées sur serveur ({})", e.toString());
            Protection.register(new SansApi());
        }
    }

    static boolean solo(Level level) {
        MinecraftServer server = level.getServer();
        return server != null && !server.isDedicatedServer();
    }

    private Object source(Protection.Source s, boolean destruction) throws ReflectiveOperationException {
        Object autorisation = destruction ? PontGuerre.frappeEnCours() : null;
        if (autorisation != null) return frappe.invoke(null, autorisation);
        return s.player() != null ? joueur.invoke(null, s.player()) : aucun.invoke(null);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object valeur(Class<?> type, Enum<?> nom) {
        return Enum.valueOf((Class) type, nom.name());
    }

    private static void erreur(Throwable t) {
        if (!signale) {
            signale = true;
            Constants.LOG.error("[GeoNTM] Protection indisponible, action refusée", t);
        }
    }

    @Override
    public Protection.Coverage coverage(Level level, int x, int z, Protection.Source s, Protection.Cause c) {
        try {
            Object decision = chunk.invoke(null, level, source(s, c == Protection.Cause.EXPLOSION), valeur(cause, c),
                    new ChunkPos(x, z));
            return Protection.Coverage.valueOf(acces.invoke(decision).toString());
        } catch (ReflectiveOperationException | RuntimeException e) {
            erreur(e);
            return Protection.Coverage.CLOSED;
        }
    }

    @Override
    public boolean mayAlter(Level level, BlockPos pos, Protection.Source s, Protection.Cause c) {
        try {
            return (boolean) bloc.invoke(null, level, source(s, c == Protection.Cause.EXPLOSION), valeur(cause, c), pos.immutable());
        } catch (ReflectiveOperationException | RuntimeException e) {
            erreur(e);
            return false;
        }
    }

    @Override
    public boolean mayHarm(Level level, Entity victime, Protection.Source s, Protection.Hazard h) {
        try {
            return (boolean) entite.invoke(null, level, source(s, h == Protection.Hazard.BLAST), valeur(danger, h),
                    victime.blockPosition());
        } catch (ReflectiveOperationException | RuntimeException e) {
            erreur(e);
            return false;
        }
    }

    /** GeoMods sans l'API : on protège le serveur plutôt que de laisser passer. */
    private static final class SansApi implements Protection.Provider {
        @Override
        public Protection.Coverage coverage(Level level, int x, int z, Protection.Source s, Protection.Cause c) {
            return solo(level) ? Protection.Coverage.OPEN : Protection.Coverage.CLOSED;
        }

        @Override
        public boolean mayAlter(Level level, BlockPos pos, Protection.Source s, Protection.Cause c) {
            return solo(level);
        }

        @Override
        public boolean mayHarm(Level level, Entity victime, Protection.Source s, Protection.Hazard h) {
            return solo(level);
        }
    }
}
