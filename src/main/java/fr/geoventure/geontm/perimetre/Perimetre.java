package fr.geoventure.geontm.perimetre;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;

/** Le contenu retenu pour GeoNTM, généré par {@code tools/perimetre.py} depuis {@code docs/perimetre/contenu.csv}. */
public final class Perimetre {

    private static final Set<String> CREATIF = lire("creatif.txt");
    private static final Set<String> RETIRE = lire("retire.txt");
    private static final Set<String> SIMPLIFIE = lire("simplifie.txt");
    /** Objets tranchés plus tard par le propriétaire et ajoutés au créatif (drones, machines). */
    private static final Set<String> AJOUTE = lire("ajoute.txt");

    private Perimetre() {}

    /** Objet proposé aux joueurs dans les onglets créatifs. */
    public static boolean visibleEnCreatif(String id) { return (CREATIF.contains(id) || AJOUTE.contains(id)) && !SIMPLIFIE.contains(id); }

    /** Objet hors périmètre : masqué partout, mais conservé dans le code pour suivre NTM-Continuum. */
    public static boolean retire(String id) { return RETIRE.contains(id); }

    /** État intermédiaire d'un combustible : il existe en jeu, sans encombrer les listes ({@code tools/simplifier.py}). */
    public static boolean simplifie(String id) { return SIMPLIFIE.contains(id); }

    private static Set<String> lire(String fichier) {
        String chemin = "/geontm/perimetre/" + fichier;
        try (InputStream in = Perimetre.class.getResourceAsStream(chemin)) {
            if (in == null) throw new IllegalStateException("Ressource absente : " + chemin);
            try (BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                return r.lines().filter(l -> !l.isBlank()).collect(Collectors.toUnmodifiableSet());
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
