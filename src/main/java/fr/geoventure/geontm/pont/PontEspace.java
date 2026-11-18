package fr.geoventure.geontm.pont;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.saveddata.satellites.Satellite;
import fr.geoventure.geontm.saveddata.satellites.XSatelliteRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.UUID;

/** Inscrit chaque satellite mis en orbite dans la course à l'espace de Geoventure, au nom de son lanceur. */
public final class PontEspace {

    private static final String API = "fr.geoventure.geomods.api.GeoSpaceApi";
    private static final String CLE_LANCEUR = "geoventure_lanceur";
    private static @Nullable Method inscrire;

    private PontEspace() {}

    public static void installer() {
        try {
            inscrire = Class.forName(API).getMethod("registerSatellite",
                    UUID.class, String.class, String.class, String.class, String.class);
            XSatelliteRegistry.ORBITED.register(PontEspace::enOrbite);
            Constants.LOG.info("[GeoNTM] Satellites reliés à la course à l'espace");
        } catch (ReflectiveOperationException | LinkageError e) {
            Constants.LOG.info("[GeoNTM] Course à l'espace non reliée : {}", e.toString());
        }
    }

    /** Appelé par les pas de tir juste avant de confier le satellite à la fusée. */
    public static void marquerLanceur(ItemStack satellite, @Nullable UUID lanceur) {
        if (lanceur != null && !satellite.isEmpty()) satellite.getOrCreateTag().putUUID(CLE_LANCEUR, lanceur);
    }

    private static void enOrbite(ServerLevel level, ItemStack stack, int frequence, Satellite satellite) {
        var tag = stack.getTag();
        UUID lanceur = tag != null && tag.hasUUID(CLE_LANCEUR) ? tag.getUUID(CLE_LANCEUR) : null;
        try {
            Object resultat = inscrire.invoke(null, lanceur, null, satellite.getType(),
                    level.dimension().location().toString(), "{\"frequence\":" + frequence + "}");
            if (!(boolean) resultat.getClass().getMethod("ok").invoke(resultat)) {
                Constants.LOG.warn("[GeoNTM] Satellite non inscrit : {}",
                        resultat.getClass().getMethod("message").invoke(resultat));
            }
        } catch (ReflectiveOperationException | RuntimeException e) {
            Constants.LOG.error("[GeoNTM] Inscription du satellite impossible", e);
        }
    }
}
