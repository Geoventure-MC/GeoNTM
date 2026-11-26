package fr.geoventure.geontm.pont;

import fr.geoventure.geocore.dejson.BiomeModifiers;
import fr.geoventure.geocore.world.Spawns;
import fr.geoventure.geontm.entity.ModNtmEntities;
import fr.geoventure.geontm.entity.mob.glyphid.EntityGlyphidBrawler;

import java.util.List;

/**
 * Peuplement naturel de Mars, Vénus et Mercure par les aliens (glyphes) de NTM : NTM:Space n'y avait que des
 * machines de guerre, retirées le 23/11 (elles se battaient entre elles). Seuls les glyphes de base et le
 * bagarreur apparaissent : ni éclaireur (il bâtit des ruches et terraforme), ni creuseur, ni bombardier (explosions).
 * Les biomes sont les étiquettes {@code #geontm:space/<astre>} (biomes Ad Astra, facultatifs). Apparition de nuit
 * seulement (règle des monstres), comme pour tout monstre ; Mars en reçoit le plus, Mercure le moins.
 */
public final class PeuplementPlanetes {

    private PeuplementPlanetes() {}

    /** Une apparition naturelle : {@code astre} est le suffixe de l'étiquette {@code geontm:space/<astre>}. */
    public record Faune(String astre, String entite, int poids, int min, int max) {
        String nom() {
            return "peuplement_" + astre + "_" + entite.substring(entite.indexOf(':') + 1);
        }
    }

    public static final List<Faune> FAUNE = List.of(
            new Faune("mars", "geontm:entity_glyphid", 8, 1, 3),
            new Faune("mars", "geontm:entity_glyphid_brawler", 3, 1, 2),
            new Faune("venus", "geontm:entity_glyphid", 5, 1, 2),
            new Faune("venus", "geontm:entity_glyphid_brawler", 2, 1, 1),
            new Faune("mercury", "geontm:entity_glyphid", 3, 1, 2));

    private static boolean declared;

    /** Déclaration (Dejson, à la construction) des modificateurs de biome ; sans effet la seconde fois. */
    public static void declare() {
        if (declared) return;
        declared = true;
        for (Faune f : FAUNE) {
            BiomeModifiers.addSpawns("geontm", f.nom(), "#geontm:space/" + f.astre(), null, f.entite(), f.poids(), f.min(), f.max());
        }
    }

    /** Exécution : règles de placement des deux mobs (sol plein, règle des monstres). */
    public static void init() {
        declare();
        Spawns.placement(() -> ModNtmEntities.GLYPHID.get(), Spawns.Placement.ON_GROUND, Spawns.Rule.MONSTER);
        Spawns.placement(() -> EntityGlyphidBrawler.TYPE.get(), Spawns.Placement.ON_GROUND, Spawns.Rule.MONSTER);
    }
}
