package fr.geoventure.geontm.pont;

import fr.geoventure.geocore.Ids;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.handler.HazmatRegistry;
import fr.geoventure.geontm.handler.radiation.ChunkRadiationManager;
import fr.geoventure.geontm.util.ContaminationUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Une seule radioactivité pour le joueur : la dose de GeoNTM. Les retombées de GeoMods s'y ajoutent, et les zones
 * irradiées par GeoNTM apparaissent sur les outils de GeoMods. Sans {@code GeoRadiationApi}, ce pont reste inactif et
 * chaque mod garde son fonctionnement. Rien n'est réinjecté dans les chunks de GeoNTM, sinon la diffusion s'emballerait.
 */
public final class PontRadiation {

    private static final String API = "fr.geoventure.geomods.api.GeoRadiationApi";
    /**
     * 100 unités GeoMods valent 1 RAD/s de GeoNTM : le seuil 400 donne 4 RAD/s et le pic 9 000 donne 90 RAD/s.
     * GeoNTM tue à 1 000 RAD, donc une échelle plus forte rendrait toute retombée mortelle en un instant.
     */
    private static final int ECHELLE = 100;
    /** En dessous, GeoMods n'appliquait aucun effet au joueur. */
    private static final int SEUIL_GEOMODS = 400;
    private static final int MAXIMUM_GEOMODS = 10_000;
    private static final int PERIODE = 100;
    private static final Map<String, Double> COMBINAISON =
            Map.of("hazmat_helmet", 0.6D, "hazmat_chestplate", 0.8D, "hazmat_leggings", 0.4D, "hazmat_boots", 0.2D);

    private static Method niveau, relever;
    private static boolean actif, armuresPretes, premierPassage;

    private PontRadiation() {}

    public static void installer() {
        try {
            Class<?> api = Class.forName(API);
            niveau = api.getMethod("chunkLevel", ServerLevel.class, ChunkPos.class);
            relever = api.getMethod("raiseChunkTo", ServerLevel.class, ChunkPos.class, int.class);
            api.getMethod("setExternalPlayerDose", boolean.class).invoke(null, true);
            actif = true;
            // Pas EntityTicks : sous Mohist, ce crochet n'a pas été appelé pour les joueurs lors des essais.
            MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, TickEvent.PlayerTickEvent.class,
                    PontRadiation::tick);
            ContaminationUtil.GEIGER_LINES.register(PontRadiation::ligneGeiger);
            Constants.LOG.info("[GeoNTM] Radioactivité reliée à GeoMods");
        } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
            actif = false;
            Constants.LOG.info("[GeoNTM] Radioactivité GeoMods non reliée : {}", e.toString());
        }
    }

    private static void tick(TickEvent.PlayerTickEvent event) {
        if (!actif || event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer joueur)) return;
        ServerLevel level = joueur.serverLevel();
        if ((level.getGameTime() + joueur.getId()) % PERIODE != 0) return;
        try {
            preparerArmures();
            if (!premierPassage) {
                premierPassage = true;
                Constants.LOG.info("[GeoNTM] Pont de radioactivité actif sur les joueurs");
            }
            ChunkPos chunk = joueur.chunkPosition();
            double geontm = ChunkRadiationManager.proxy.getRadiation(level, joueur.blockPosition());
            if (geontm >= 1D) {
                relever.invoke(null, level, chunk, (int) Math.min(MAXIMUM_GEOMODS, geontm * ECHELLE));
            }
            // Seul l'excédent compte : GeoNTM a déjà appliqué sa propre valeur de chunk à chaque tick.
            int valeur = (int) niveau.invoke(null, level, chunk);
            double retombees = valeur / (double) ECHELLE;
            if (valeur >= SEUIL_GEOMODS && retombees > geontm) {
                ContaminationUtil.contaminate(joueur, ContaminationUtil.HazardType.RADIATION,
                        ContaminationUtil.ContaminationType.CREATIVE, (retombees - geontm) * PERIODE / 20D);
            }
        } catch (ReflectiveOperationException | RuntimeException e) {
            actif = false;
            Constants.LOG.error("[GeoNTM] Pont de radioactivité arrêté", e);
        }
    }

    /** Les objets de GeoMods n'existent pas encore quand GeoNTM s'initialise. */
    private static void preparerArmures() {
        if (armuresPretes) return;
        armuresPretes = true;
        COMBINAISON.forEach((id, resistance) -> BuiltInRegistries.ITEM.getOptional(Ids.of("geomods", id))
                .ifPresent(objet -> HazmatRegistry.registerHazmat(objet, resistance)));
    }

    private static void ligneGeiger(net.minecraft.world.entity.player.Player joueur,
                                    java.util.function.Consumer<Component> sortie) {
        if (!actif || !(joueur instanceof ServerPlayer serveur)) return;
        try {
            int valeur = (int) niveau.invoke(null, serveur.serverLevel(), serveur.chunkPosition());
            if (valeur > 0) sortie.accept(Component.literal("Retombées : " + valeur / ECHELLE + " RAD/s"));
        } catch (ReflectiveOperationException | RuntimeException e) {
            Constants.LOG.warn("[GeoNTM] Retombées illisibles : {}", e.toString());
        }
    }
}
