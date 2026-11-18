package fr.geoventure.geontm.pont;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.entity.missile.EntityMissileBaseNT;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Soumet les tirs de GeoNTM aux règles de guerre de Geoventure, de l'autorisation jusqu'à l'impact. */
public final class PontGuerre {

    private static final String API = "fr.geoventure.geomods.api.GeoWarApi";
    private static final Map<String, Object> AUTORISATIONS = new ConcurrentHashMap<>();
    private static final ThreadLocal<Object> FRAPPE = new ThreadLocal<>();

    private PontGuerre() {}

    /** L'autorisation de la frappe qui explose en ce moment, ou null. */
    static Object frappeEnCours() { return FRAPPE.get(); }

    /** Demande et paie le tir. Faux : le missile ne part pas. */
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
