import com.google.gson.JsonObject;
import fr.geoventure.harness.api.Client;
import fr.geoventure.harness.api.Expect;
import fr.geoventure.harness.api.Scenario;

/**
 * The 1.7 NEIUniversalHandler tabs (UniversalRecipeHandler): opens each in JEI through Core's {@code jei} query, checks
 * 1.7's title and that it holds recipes, and screenshots it for comparison with 1.7's NEI page. The annihilator tab is
 * empty outside 528 mode, as in 1.7. With {@code -Prei.run=true} it also opens two of them in REI.
 */
public final class UniversalRecipeTabs implements Scenario {

    private static final String[][] TABS = {
            {"geontm:storage_drum", "Nuclear Waste Decay"},
            {"geontm:ore_slopper", "Bedrock Ore Processor"},
            {"geontm:outgasser", "RBMK Irradiation Channel"},
            {"geontm:deuterium", "Deuterium Extractor"},
            {"geontm:grenade", "Grenade Crafting"},
            {"geontm:zirnox", "ZIRNOX Nuclear Reactor"},
            {"geontm:pwr", "PWR Controller"},
            {"geontm:watz", "Watz Powerplant"},
            {"geontm:rbmk_disassembly", "RBMK Rod Disassembly"},
    };

    @Override
    public void run(Client c) throws Exception {
        c.waitInWorld(180_000L);
        c.waitTicks(40);
        c.markLog();
        int shot = 10;
        StringBuilder titles = new StringBuilder();
        for (String[] tab : TABS) {
            JsonObject p = new JsonObject();
            p.addProperty("category", tab[0]);
            JsonObject opened = c.query("jei", p);
            Expect.that(opened.has("opened") && opened.get("opened").getAsBoolean(), "JEI opened " + tab[0] + ": " + opened);
            String title = opened.get("title").getAsString();
            titles.append(tab[0]).append('=').append(title).append(" (").append(opened.get("recipes")).append(") ");
            Expect.that(opened.get("recipes").getAsLong() > 0, tab[0] + " holds recipes: " + opened.get("recipes"));
            c.waitTicks(30);
            String screen = c.screenClass();
            Expect.that(screen != null && screen.contains("Recipes"), "JEI's recipe screen is up for " + tab[0] + ": " + screen);
            c.screenshot((shot++) + "-" + tab[0].substring(4));
            c.key("escape");
            c.waitTicks(10);
        }
        System.out.println("[UniversalRecipeTabs] " + c.node() + " " + titles);
        for (String[] tab : TABS) Expect.that(titles.indexOf(tab[0] + "=" + tab[1] + " ") >= 0, tab[0] + " is titled '" + tab[1] + "': " + titles);

        if (!c.log(40_000, "GeoNtmReiClientPlugin").isEmpty()) {
            for (String category : new String[] {"geontm:outgasser", "geontm:zirnox"}) {
                JsonObject p = new JsonObject();
                p.addProperty("op", "open");
                p.addProperty("category", category);
                JsonObject opened = c.query("rei", p);
                Expect.that(opened.has("opened") && opened.get("opened").getAsBoolean(), "REI opened " + category + ": " + opened);
                c.waitTicks(30);
                String screen = c.screenClass();
                Expect.that(screen != null && screen.contains("Display"), "REI's recipe screen is up for " + category + ": " + screen);
                c.screenshot((shot++) + "-rei-" + category.substring(4));
                c.key("escape");
                c.waitTicks(10);
            }
        }
        Expect.logCleanSinceMark(c, "(?i)(exception|\\[(JEI|REI)\\].*error).*");
    }
}
