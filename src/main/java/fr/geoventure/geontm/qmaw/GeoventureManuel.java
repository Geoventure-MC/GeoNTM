package fr.geoventure.geontm.qmaw;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.geoventure.geontm.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Traductions françaises des pages d'origine du manuel F1, dans un fichier à part
 * ({@code assets/geontm/geoventure/manuel_fr.json} : nom de page -> {title, content}) pour que les mises à jour de
 * NTM-Continuum ne les touchent pas. Une page d'origine déjà traduite garde son texte.
 */
final class GeoventureManuel {

    private static final String FR = "fr_fr";
    private static final Map<String, JsonObject> PAGES = lire();

    private GeoventureManuel() {}

    /** Ajoute le titre et le texte français de la page {@code name} s'ils manquent. */
    static void completer(String name, Map<String, String> titles, Map<String, String> contents) {
        JsonObject page = PAGES.get(name);
        if (page == null) return;
        if (page.has("title")) titles.putIfAbsent(FR, page.get("title").getAsString());
        if (page.has("content") && !contents.isEmpty()) contents.putIfAbsent(FR, page.get("content").getAsString());
    }

    private static Map<String, JsonObject> lire() {
        Map<String, JsonObject> pages = new HashMap<>();
        try (InputStream in = GeoventureManuel.class.getResourceAsStream("/assets/geontm/geoventure/manuel_fr.json")) {
            if (in == null) return pages;
            JsonObject racine = JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
            for (Map.Entry<String, JsonElement> e : racine.entrySet()) pages.put(e.getKey(), e.getValue().getAsJsonObject());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (RuntimeException e) {
            Constants.LOG.error("[QMAW] Traductions françaises illisibles", e);
        }
        return pages;
    }
}
