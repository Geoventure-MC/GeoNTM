package fr.geoventure.geontm.pont;

import fr.geoventure.geontm.Constants;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

/**
 * Relie les schémas courants à la recherche du pays. Les schémas « secret » n'y passent pas : ils ne s'obtiennent
 * qu'en butin de guerre. La répartition des recettes par famille est faite par {@code tools/schemas.py}.
 */
public final class PontProgression {

    private static final String API = "fr.geoventure.geomods.api.GeoProgressionApi";
    private static final Map<String, String> RECHERCHES = Map.of(
            "discover.radgen", "nuclear_tech",
            "discover.ogives", "mil_rocketry",
            "discover.ogives_lourdes", "mil_ballistics",
            "discover.missiles_modulaires", "mil_strategic",
            "discover.reacteurs", "nuclear_tech",
            "discover.fusion", "sci_materials",
            "discover.spatial", "sci_aerospace",
            "discover.espace_lointain", "sci_deepspace");

    private PontProgression() {}

    /** Vrai si le pays du joueur a la recherche de ce schéma. Un schéma sans recherche est libre. */
    public static boolean rechercheAcquise(Player joueur, String famille) {
        String recherche = RECHERCHES.get(famille);
        if (recherche == null) return true;
        try {
            return (boolean) Class.forName(API).getMethod("hasResearch", java.util.UUID.class, String.class)
                    .invoke(null, joueur.getUUID(), recherche);
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            return PontGeoventure.solo(joueur.level());
        } catch (ReflectiveOperationException | RuntimeException e) {
            Constants.LOG.error("[GeoNTM] Recherche illisible, schéma refusé", e);
            return false;
        }
    }
}
