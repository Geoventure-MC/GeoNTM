package fr.geoventure.geontm.pont;

import fr.geoventure.geocore.data.DataIn;
import fr.geoventure.geocore.data.DataOut;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.entity.missile.EntityMissileBaseNT;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Soumet les tirs de GeoNTM (missiles et satellites offensifs) aux règles de guerre de Geoventure. */
public final class PontGuerre {

    private static final String API = "fr.geoventure.geomods.api.GeoWarApi";
    private static final String GRANT_CLASS = "fr.geoventure.geomods.api.WarStrikeGrant";
    private static final Map<String, Object> AUTORISATIONS = new ConcurrentHashMap<>();
    private static final ThreadLocal<Object> FRAPPE = new ThreadLocal<>();

    /** Identifiant d'une frappe orbitale autorisée (satellite). */
    public record FrappeOrbitale(UUID lancement, String frappeId) {}

    private PontGuerre() {}

    /** L'autorisation de la frappe qui explose en ce moment, ou null. */
    static Object frappeEnCours() { return FRAPPE.get(); }

    /** Demande et paie le tir d'un missile. Faux : le missile ne part pas. */
    public static boolean autoriser(EntityMissileBaseNT missile, Level level, BlockPos origine, ResourceLocation objet,
                                    UUID tireur) {
        try {
            Class<?> api = Class.forName(API);
            int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, missile.targetX, missile.targetZ);
            BlockPos cible = new BlockPos(missile.targetX, y, missile.targetZ);
            UUID lancement = UUID.randomUUID();
            Method m = api.getMethod("authorizeMissileLaunch", UUID.class, UUID.class, String.class, String.class,
                    ResourceLocation.class, Level.class, BlockPos.class, Level.class, BlockPos.class);
            Object d = m.invoke(null, lancement, tireur, null, null, objet, level, origine, level, cible);
            if (!(boolean) lire(d, "allowed")) {
                Constants.LOG.info("[GeoNTM] Tir refusé : {}", lire(d, "message"));
                return false;
            }
            Object autorisation = lire(d, "grant");
            if (autorisation == null) return false;
            String frappe = (String) lire(autorisation, "strikeId");
            AUTORISATIONS.put(frappe, autorisation);
            missile.frappeLancement = lancement;
            missile.frappeId = frappe;
            return true;
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            return PontGeoventure.solo(level);
        } catch (ReflectiveOperationException | RuntimeException e) {
            Constants.LOG.error("[GeoNTM] Autorisation de tir impossible", e);
            return false;
        }
    }

    /**
     * Soumet une frappe orbitale (DeathRay, PrecisionLaser, Horizons) aux règles de guerre de Geoventure.
     * Retourne {@code null} si le tir est refusé (le satellite ne doit pas consommer son tir/recharge).
     */
    public static @Nullable FrappeOrbitale autoriserFrappeOrbitale(ServerLevel level, BlockPos origine, BlockPos cible,
                                                                   ResourceLocation objet, @Nullable Player tireur) {
        try {
            Class<?> api = Class.forName(API);
            UUID lancement = UUID.randomUUID();
            UUID uuidTireur = tireur != null ? tireur.getUUID() : null;
            Method m = api.getMethod("authorizeMissileLaunch", UUID.class, UUID.class, String.class, String.class,
                    ResourceLocation.class, Level.class, BlockPos.class, Level.class, BlockPos.class);
            Object d = m.invoke(null, lancement, uuidTireur, null, "ORBITAL", objet, level, origine, level, cible);
            if (!(boolean) lire(d, "allowed")) {
                String msg = String.valueOf(lire(d, "message"));
                Constants.LOG.info("[GeoNTM] Frappe orbitale refusée ({}) : {}", objet, msg);
                if (tireur != null) {
                    tireur.sendSystemMessage(Component.literal("[GeoNTM] " + msg).withStyle(ChatFormatting.RED));
                }
                return null;
            }
            Object autorisation = lire(d, "grant");
            if (autorisation == null) return null;
            String frappe = (String) lire(autorisation, "strikeId");
            AUTORISATIONS.put(frappe, autorisation);
            return new FrappeOrbitale(lancement, frappe);
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            if (PontGeoventure.solo(level)) {
                return new FrappeOrbitale(UUID.randomUUID(), "solo-orbital-" + UUID.randomUUID());
            }
            return null;
        } catch (ReflectiveOperationException | RuntimeException e) {
            Constants.LOG.error("[GeoNTM] Autorisation de frappe orbitale impossible", e);
            return null;
        }
    }

    /** Exécute une frappe orbitale avec son autorisation puis signale l'impact au plugin. */
    public static void impactOrbital(Level level, @Nullable UUID lancement, @Nullable String frappeId,
                                     BlockPos pos, Runnable explosion) {
        Object autorisation = frappeId == null ? null : AUTORISATIONS.get(frappeId);
        FRAPPE.set(autorisation);
        try {
            explosion.run();
        } finally {
            FRAPPE.remove();
        }
        if (lancement != null && frappeId != null && !frappeId.startsWith("solo-orbital-")) {
            appeler("reportMissileImpact", new Class<?>[]{Level.class, UUID.class, String.class, BlockPos.class, boolean.class},
                    level, lancement, frappeId, pos, true);
        }
        if (frappeId != null) AUTORISATIONS.remove(frappeId);
    }

    /** Annule une frappe orbitale avant détonation (remboursement + révocation du strikeId). */
    public static void annulerOrbital(Level level, @Nullable UUID lancement, @Nullable String frappeId, String raison) {
        if (lancement == null) return;
        if (frappeId == null || !frappeId.startsWith("solo-orbital-")) {
            appeler("cancelMissileLaunch", new Class<?>[]{Level.class, UUID.class, String.class, String.class},
                    level, lancement, frappeId, raison);
        }
        if (frappeId != null) AUTORISATIONS.remove(frappeId);
    }

    /** Exécute l'explosion avec l'autorisation du missile, puis signale l'impact. */
    public static void impact(EntityMissileBaseNT missile, BlockPos pos, Runnable explosion) {
        Object autorisation = missile.frappeId == null ? null : AUTORISATIONS.get(missile.frappeId);
        FRAPPE.set(autorisation);
        try {
            explosion.run();
        } finally {
            FRAPPE.remove();
        }
        if (missile.frappeLancement == null) return;
        appeler("reportMissileImpact", new Class<?>[]{Level.class, UUID.class, String.class, BlockPos.class, boolean.class},
                missile.level(), missile.frappeLancement, missile.frappeId, pos, true);
        oublier(missile);
    }

    /** Le missile a été détruit en vol. */
    public static void intercepte(EntityMissileBaseNT missile) {
        if (missile.frappeLancement == null) return;
        appeler("reportMissileIntercepted", new Class<?>[]{Level.class, UUID.class, String.class, BlockPos.class, boolean.class},
                missile.level(), missile.frappeLancement, missile.frappeId, missile.blockPosition(), false);
        oublier(missile);
    }

    /** Le missile a été supprimé anormalement avant impact/interception : annulation et remboursement. */
    public static void annuler(EntityMissileBaseNT missile, String raison) {
        if (missile.frappeLancement == null) return;
        appeler("cancelMissileLaunch", new Class<?>[]{Level.class, UUID.class, String.class, String.class},
                missile.level(), missile.frappeLancement, missile.frappeId, raison);
        oublier(missile);
    }

    /** Persiste les champs d'un WarStrikeGrant dans le NBT de l'entité pour survivre à un redémarrage en vol. */
    public static void sauvegarderAutorisation(@Nullable String frappeId, DataOut out) {
        if (frappeId == null) return;
        Object grant = AUTORISATIONS.get(frappeId);
        if (grant == null) return;
        try {
            out.putString("frappePays", String.valueOf(lire(grant, "countryId")));
            out.putString("frappeDim", String.valueOf(lire(grant, "targetDim")));
            out.putInt("frappeTX", (int) lire(grant, "targetX"));
            out.putInt("frappeTY", (int) lire(grant, "targetY"));
            out.putInt("frappeTZ", (int) lire(grant, "targetZ"));
            out.putInt("frappeRayon", (int) lire(grant, "maxRadiusBlocks"));
            out.putLong("frappeExpire", (long) lire(grant, "expiresAtMs"));
        } catch (ReflectiveOperationException | RuntimeException e) {
            Constants.LOG.warn("[GeoNTM] Sauvegarde d'autorisation ignorée : {}", e.toString());
        }
    }

    /** Reconstruit le WarStrikeGrant en mémoire au chargement d'une entité sauvegardée en plein vol. */
    public static void restaurerAutorisation(@Nullable String frappeId, DataIn in) {
        if (frappeId == null || frappeId.isEmpty() || AUTORISATIONS.containsKey(frappeId)) return;
        String pays = in.getStringOr("frappePays", "");
        String dim = in.getStringOr("frappeDim", "");
        if (pays.isEmpty() || dim.isEmpty()) return;
        try {
            Class<?> cls = Class.forName(GRANT_CLASS);
            Constructor<?> ctor = cls.getConstructor(String.class, String.class, String.class,
                    int.class, int.class, int.class, int.class, long.class);
            Object grant = ctor.newInstance(
                    frappeId, pays, dim,
                    in.getIntOr("frappeTX", 0),
                    in.getIntOr("frappeTY", 0),
                    in.getIntOr("frappeTZ", 0),
                    in.getIntOr("frappeRayon", 0),
                    in.getLongOr("frappeExpire", 0L));
            AUTORISATIONS.put(frappeId, grant);
        } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
            Constants.LOG.warn("[GeoNTM] Restauration d'autorisation impossible : {}", e.toString());
        }
    }

    private static void oublier(EntityMissileBaseNT missile) {
        if (missile.frappeId != null) AUTORISATIONS.remove(missile.frappeId);
        missile.frappeLancement = null;
        missile.frappeId = null;
    }

    private static Object lire(Object objet, String accesseur) throws ReflectiveOperationException {
        return objet.getClass().getMethod(accesseur).invoke(objet);
    }

    private static void appeler(String methode, Class<?>[] types, Object... valeurs) {
        try {
            Class.forName(API).getMethod(methode, types).invoke(null, valeurs);
        } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
            Constants.LOG.warn("[GeoNTM] Suivi de frappe impossible : {}", e.toString());
        }
    }
}
