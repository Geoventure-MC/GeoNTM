package fr.geoventure.geontm.pont;

import fr.geoventure.geontm.config.MobConfig;
import fr.geoventure.geontm.config.WorldConfig;

/**
 * Réglages imposés par Geoventure après la lecture de la configuration : aucun fichier de serveur ne peut les rouvrir.
 * Le monde reste celui des pays ; GeoNTM n'y ajoute ni invasion extraterrestre ni destruction aléatoire.
 */
public final class ReglagesGeoventure {

    private ReglagesGeoventure() {}

    public static void appliquer() {
        // Nids d'insectes Glyphides et leurs infestations.
        MobConfig.enableHives = false;
        MobConfig.enableInfestation = false;
        MobConfig.rampantMode = false;
        MobConfig.rampantNaturalScoutSpawn = false;
        // Boss Mask Man, élémentaux de fusion et raids.
        MobConfig.enableMaskman = false;
        MobConfig.enableElementals = false;
        MobConfig.enableRaids = false;
        // Météores, qui détruiraient le terrain des pays au hasard.
        WorldConfig.enableMeteorStrikes = false;
        WorldConfig.enableMeteorShowers = false;
    }
}
