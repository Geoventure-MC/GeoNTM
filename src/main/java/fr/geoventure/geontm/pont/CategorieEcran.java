package fr.geoventure.geontm.pont;

import java.util.Locale;
import java.util.Set;

/**
 * Classe un écran de conteneur de GeoNTM pour le bandeau Geoventure ({@link BandeauInterfaces}). Sans code client,
 * donc testable sur serveur. Tout écran de machine NTM porte un nom {@code GUI...} ; les autres (RBMK, radio,
 * pneumatique) gardent leur image d'origine.
 */
public enum CategorieEcran {
    AUCUNE, INDUSTRIE, GUERRE;

    private static final Set<String> GUERRE_EXPLICITE = Set.of("GUILaunchPad", "GUIMachineMissileAssembly",
            "GUIMachineRadarNT", "GUIMachineRadarNTSlots");
    private static final String[] MOTS_GUERRE = {"launch", "missile", "radar", "turret", "silo"};

    private static final String PAQUET = "fr.geoventure.geontm.";

    /** @param nomClasse nom complet de la classe de l'écran ; seuls les écrans de GeoNTM sont habillés. */
    public static CategorieEcran de(String nomClasse) {
        if (nomClasse == null || !nomClasse.startsWith(PAQUET)) return AUCUNE;
        String nomSimple = nomClasse.substring(nomClasse.lastIndexOf('.') + 1);
        if (!nomSimple.startsWith("GUI")) return AUCUNE;
        if (GUERRE_EXPLICITE.contains(nomSimple)) return GUERRE;
        String bas = nomSimple.toLowerCase(Locale.ROOT);
        for (String mot : MOTS_GUERRE) {
            if (bas.contains(mot)) return GUERRE;
        }
        return INDUSTRIE;
    }
}
