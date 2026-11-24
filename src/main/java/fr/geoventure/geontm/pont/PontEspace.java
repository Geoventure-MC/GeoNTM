package fr.geoventure.geontm.pont;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.api.environment.Environment;
import fr.geoventure.geontm.saveddata.satellites.Satellite;
import fr.geoventure.geontm.saveddata.satellites.XSatelliteRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/** Inscrit chaque satellite mis en orbite dans la course à l'espace de Geoventure, au nom de son lanceur. */
public final class PontEspace {

    private static final String API = "fr.geoventure.geomods.api.GeoSpaceApi";
    private static final String CLE_LANCEUR = "geoventure_lanceur";
    private static final String CLE_ORBITE = "geoventure_orbit";
    private static @Nullable Method inscrire;
    private static @Nullable Method retirer;
    private static boolean stationsReliees = false;

    private PontEspace() {}

    public static void installer() {
        relierStationsOrbitales();
        try {
            inscrire = Class.forName(API).getMethod("registerSatellite",
                    UUID.class, String.class, String.class, String.class, String.class);
            XSatelliteRegistry.ORBITED.register(PontEspace::enOrbite);
            try {
                retirer = Class.forName(API).getMethod("unregisterSatellite", int.class, String.class);
                XSatelliteRegistry.DESCENDED.register(PontEspace::redescendu);
            } catch (NoSuchMethodException ancien) {
                // geoMods antérieur au 23/11 : la descente n'est pas transmise
            }
            Constants.LOG.info("[GeoNTM] Satellites reliés à la course à l'espace (multi-astres)");
        } catch (ReflectiveOperationException | LinkageError e) {
            Constants.LOG.info("[GeoNTM] Course à l'espace non reliée : {}", e.toString());
        }
    }

    /**
     * Relie les stations orbitales Ad Astra ({@code *_orbit}) à leur astre sous-jacent afin qu'un
     * satellite lancé depuis une station orbitale rejoigne bien l'orbite de cet astre, sans modifier
     * le comportement par défaut des mondes de surface.
     */
    private static void relierStationsOrbitales() {
        if (stationsReliees) return;
        stationsReliees = true;
        Environment.SATELLITES.register(new Environment.Satellites() {
            @Override
            public @Nullable ServerLevel satellites(ServerLevel level, @Nullable BlockPos pos, ItemStack launched) {
                return mondeSurfacePourOrbite(level);
            }

            @Override
            public @Nullable List<ServerLevel> levels(ServerLevel level, @Nullable BlockPos pos) {
                ServerLevel surface = mondeSurfacePourOrbite(level);
                return surface != null && surface != level ? List.of(surface, level) : null;
            }
        });
    }

    private static @Nullable ServerLevel mondeSurfacePourOrbite(ServerLevel level) {
        String path = level.dimension().location().getPath().toLowerCase(Locale.ROOT);
        if (!path.endsWith("_orbit")) return null;
        MinecraftServer server = level.getServer();
        String body = path.substring(0, path.length() - "_orbit".length());
        if ("earth".equals(body) || "overworld".equals(body)) {
            return server.overworld();
        }
        ResourceLocation targetId = new ResourceLocation(level.dimension().location().getNamespace(), body);
        return server.getLevel(ResourceKey.create(Registries.DIMENSION, targetId));
    }

    /**
     * Déduit l'astre d'orbite ({@code earth}, {@code moon}, {@code mars}, {@code venus}, {@code mercury})
     * à partir de l'identifiant de dimension ({@code Level.dimension().location()}).
     */
    public static String astreDOrbite(@Nullable ResourceLocation dimension) {
        if (dimension == null) return "earth";
        String full = dimension.toString().toLowerCase(Locale.ROOT);
        String path = dimension.getPath().toLowerCase(Locale.ROOT);
        if (path.contains("mercury") || full.contains("mercury")) return "mercury";
        if (path.contains("venus") || full.contains("venus")) return "venus";
        if (path.contains("mars") || full.contains("mars")) return "mars";
        if (path.contains("moon") || full.contains("moon")) return "moon";
        return "earth";
    }

    /** Appelé par les pas de tir juste avant de confier le satellite à la fusée. */
    public static void marquerLanceur(ItemStack satellite, @Nullable UUID lanceur) {
        if (lanceur != null && !satellite.isEmpty()) satellite.getOrCreateTag().putUUID(CLE_LANCEUR, lanceur);
    }

    /** Satellite redescendu : retiré de la liste du plugin sur son astre d'orbite (ou toutes orbites si ancien). */
    private static void redescendu(int frequence, Satellite satellite) {
        if (retirer == null) return;
        String astre = satellite.extra != null && satellite.extra.contains(CLE_ORBITE)
                ? satellite.extra.getString(CLE_ORBITE) : null;
        try {
            retirer.invoke(null, frequence, (astre == null || astre.isBlank()) ? null : astre);
        } catch (ReflectiveOperationException | RuntimeException e) {
            Constants.LOG.warn("[GeoNTM] Retrait du satellite {} non transmis : {}", frequence, e.toString());
        }
    }

    private static void enOrbite(ServerLevel level, ItemStack stack, int frequence, Satellite satellite) {
        var tag = stack.getTag();
        UUID lanceur = tag != null && tag.hasUUID(CLE_LANCEUR) ? tag.getUUID(CLE_LANCEUR) : null;
        String astre = astreDOrbite(level.dimension().location());
        if (satellite.extra != null) {
            satellite.extra.putString(CLE_ORBITE, astre);
        }
        if (inscrire == null) return;
        String orbitDim = astre + "_orbit";
        String payload = "{\"frequence\":" + frequence
                + ",\"body\":\"" + astre + "\""
                + ",\"dimension\":\"" + level.dimension().location() + "\""
                + ",\"satId\":\"" + satellite.type().name() + "\"}";
        try {
            Object resultat = inscrire.invoke(null, lanceur, null, satellite.getType(), orbitDim, payload);
            if (!(boolean) resultat.getClass().getMethod("ok").invoke(resultat)) {
                Constants.LOG.warn("[GeoNTM] Satellite non inscrit : {}",
                        resultat.getClass().getMethod("message").invoke(resultat));
            }
        } catch (ReflectiveOperationException | RuntimeException e) {
            Constants.LOG.error("[GeoNTM] Inscription du satellite impossible", e);
        }
    }
}
