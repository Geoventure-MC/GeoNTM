package fr.geoventure.geontm.gametest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geontm.pont.CategorieEcran;
import fr.geoventure.geontm.pont.PeuplementPlanetes;
import fr.geoventure.geontm.pont.PontEspace;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/** Orbites d'Ad Astra (Glacio comprise), faune naturelle de Mars, Vénus et Mercure, classement des écrans habillés. */
public final class PontsEspaceGameTests {

    private PontsEspaceGameTests() {}

    private static ResourceLocation id(String s) {
        return new ResourceLocation(s);
    }

    /** L'astre d'une dimension d'orbite et sa surface : les six astres d'Ad Astra, Glacio inclus. */
    public static void orbites(GameTestHelper helper) {
        for (String astre : new String[] {"moon", "mars", "venus", "mercury", "glacio"}) {
            helper.assertTrue(astre.equals(PontEspace.astreDOrbite(id("ad_astra:" + astre))), "surface " + astre);
            helper.assertTrue(astre.equals(PontEspace.astreDOrbite(id("ad_astra:" + astre + "_orbit"))), "orbite " + astre);
            helper.assertTrue(id("ad_astra:" + astre).equals(PontEspace.surfacePourOrbite(id("ad_astra:" + astre + "_orbit"))),
                    "surface de l'orbite " + astre);
            helper.assertTrue(PontEspace.surfacePourOrbite(id("ad_astra:" + astre)) == null, astre + " n'est pas une orbite");
        }
        helper.assertTrue("earth".equals(PontEspace.astreDOrbite(id("minecraft:overworld"))), "Terre");
        helper.assertTrue("earth".equals(PontEspace.astreDOrbite(id("ad_astra:earth_orbit"))), "orbite terrestre");
        helper.assertTrue("earth".equals(PontEspace.astreDOrbite(null)), "dimension inconnue : Terre");
        helper.assertTrue(id("minecraft:overworld").equals(PontEspace.surfacePourOrbite(id("ad_astra:earth_orbit"))), "Terre -> monde principal");
        helper.assertTrue(id("minecraft:overworld").equals(PontEspace.surfacePourOrbite(id("ad_astra:overworld_orbit"))), "overworld_orbit -> monde principal");
        helper.succeed();
    }

    /** Chaque apparition naturelle vise un mob enregistré, une étiquette de biome existante et un fichier de modificateur généré. */
    public static void peuplement(GameTestHelper helper) {
        var ressources = helper.getLevel().getServer().getResourceManager();
        for (PeuplementPlanetes.Faune f : PeuplementPlanetes.FAUNE) {
            helper.assertTrue(BuiltInRegistries.ENTITY_TYPE.containsKey(id(f.entite())), "mob non enregistré : " + f.entite());
            helper.assertTrue(f.poids() > 0 && f.min() >= 1 && f.max() >= f.min(), "groupe invalide : " + f);
            helper.assertTrue(ressources.getResource(Ids.of("geontm", "tags/worldgen/biome/space/" + f.astre() + ".json")).isPresent(),
                    "étiquette de biome absente : space/" + f.astre());
            helper.assertTrue(ressources.getResource(Ids.of("geontm", "forge/biome_modifier/" + "peuplement_" + f.astre() + "_"
                            + f.entite().substring(f.entite().indexOf(':') + 1) + ".json")).isPresent(),
                    "modificateur de biome non généré : " + f);
        }
        helper.succeed();
    }

    /** Écrans de GeoNTM : tous les {@code GUI...} sont habillés, la guerre a sa couleur, le reste garde l'image d'origine. */
    public static void ecrans(GameTestHelper helper) {
        String p = "fr.geoventure.geontm.inventory.gui.";
        helper.assertTrue(CategorieEcran.de(p + "GUIMachinePress") == CategorieEcran.INDUSTRIE, "presse");
        helper.assertTrue(CategorieEcran.de(p + "GUIPWR") == CategorieEcran.INDUSTRIE, "PWR");
        helper.assertTrue(CategorieEcran.de(p + "GUIAnvil") == CategorieEcran.INDUSTRIE, "enclume");
        helper.assertTrue(CategorieEcran.de(p + "GUILaunchPad") == CategorieEcran.GUERRE, "rampe de lancement");
        helper.assertTrue(CategorieEcran.de(p + "GUIMachineRadarNTSlots") == CategorieEcran.GUERRE, "radar");
        helper.assertTrue(CategorieEcran.de(p + "GUITurretChekhov") == CategorieEcran.GUERRE, "tourelle");
        helper.assertTrue(CategorieEcran.de(p + "ScreenRBMKRod") == CategorieEcran.AUCUNE, "RBMK garde son image");
        helper.assertTrue(CategorieEcran.de("autre.mod.GUIMachinePress") == CategorieEcran.AUCUNE, "écran d'un autre mod");
        helper.assertTrue(CategorieEcran.de(null) == CategorieEcran.AUCUNE, "nul");
        helper.succeed();
    }

    /**
     * Matières rares : le minerai de Glacio (starmetal) et celui de Mercure (schrabidium) ne sont dans aucune structure de
     * génération hors des fichiers {@code espace_*}, et ceux-ci ne visent que des biomes d'Ad Astra, jamais la Terre.
     */
    public static void mineraisRares(GameTestHelper helper) {
        var ressources = helper.getLevel().getServer().getResourceManager();
        List<String> fuites = new ArrayList<>();
        int[] espace = {0};
        ressources.listResources("worldgen/configured_feature", id -> id.getPath().endsWith(".json")).forEach((id, resource) -> {
            try (var in = resource.open()) {
                String texte = new String(in.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
                boolean rare = texte.contains("geontm:ore_glacio_starmetal") || texte.contains("geontm:ore_schrabidium");
                boolean nomme = id.getPath().contains("/espace_");
                if (rare && !nomme) fuites.add(id.toString());
                if (rare && nomme) espace[0]++;
            } catch (IOException e) {
                throw new IllegalStateException(id.toString(), e);
            }
        });
        helper.assertTrue(fuites.isEmpty(), "minerai rare hors fichiers espace_* : " + fuites);
        helper.assertTrue(espace[0] == 2, "starmetal et schrabidium : une génération chacun, vu " + espace[0]);
        int[] modificateurs = {0};
        ressources.listResources("forge/biome_modifier", id -> id.getPath().contains("/espace_") && id.getPath().endsWith(".json"))
                .forEach((id, resource) -> {
                    try (Reader reader = resource.openAsReader()) {
                        JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                        JsonElement biomes = json.get("biomes");
                        helper.assertTrue(biomes != null && biomes.isJsonPrimitive()
                                && biomes.getAsString().startsWith("ad_astra:"), id + " vise autre chose qu'un biome d'Ad Astra : " + biomes);
                        modificateurs[0]++;
                    } catch (IOException e) {
                        throw new IllegalStateException(id.toString(), e);
                    }
                });
        helper.assertTrue(modificateurs[0] == 3, "Glacio (2 biomes) et Mercure (1 biome) : 3 modificateurs, vu " + modificateurs[0]);
        helper.succeed();
    }
}
