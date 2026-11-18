import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.geoventure.harness.api.Client;
import fr.geoventure.harness.api.Expect;
import fr.geoventure.harness.api.Scenario;

import java.util.ArrayList;
import java.util.List;

/**
 * The order of HBM's recipe tabs. GEONTM declares them in 1.7's NEIRegistry order; JEI ignores declaration order (its
 * recipe-category-sort-order.ini, else crafting, minecraft, then by id), REI keeps it unless its own ordering config
 * says otherwise. Prints both viewers' order and screenshots one recipe screen with several tabs in each.
 */
public final class RecipeTabOrder implements Scenario {

    // 1.7 order: tooling, construction, soldering, blast furnace, press, shredder, assembly machine, centrifuge
    private static final String TABS = "geontm:tooling,geontm:construction,geontm:soldering_station,geontm:blast_furnace,geontm:press,"
            + "geontm:shredder,geontm:assembly_machine,geontm:centrifuge";

    @Override
    public void run(Client c) throws Exception {
        c.waitInWorld(180_000L);
        c.waitTicks(40);
        c.markLog();

        JsonObject all = c.query("jei", new JsonObject());
        System.out.println("[RecipeTabOrder] " + c.node() + " JEI geontm order: " + geontm(all.getAsJsonArray("order")));
        JsonObject p = new JsonObject();
        p.addProperty("category", TABS);
        Expect.that(c.query("jei", p).get("opened").getAsBoolean(), "JEI opened " + TABS);
        c.waitTicks(30);
        c.screenshot("10-jei-tab-bar");
        c.key("escape");
        c.waitTicks(10);

        if (!c.log(40_000, "GeoNtmReiClientPlugin").isEmpty()) {
            JsonObject status = new JsonObject();
            status.addProperty("op", "status");
            List<String> rei = geontm(c.query("rei", status).getAsJsonArray("order"));
            System.out.println("[RecipeTabOrder] " + c.node() + " REI geontm order: " + rei);
            Expect.that(rei.indexOf("geontm:tooling") == 0 && rei.indexOf("geontm:construction") == 1
                    && rei.indexOf("geontm:fluid_containers") > rei.indexOf("geontm:space_assembler"), "REI keeps 1.7's order: " + rei);
            JsonObject open = new JsonObject();
            open.addProperty("op", "open");
            open.addProperty("category", TABS);
            Expect.that(c.query("rei", open).get("opened").getAsBoolean(), "REI opened " + TABS);
            c.waitTicks(30);
            c.screenshot("11-rei-tab-bar");
            c.key("escape");
            c.waitTicks(10);
        }
        Expect.logCleanSinceMark(c, "(?i)(exception|\\[(JEI|REI)\\].*error).*");
    }

    private static List<String> geontm(JsonArray order) {
        List<String> ids = new ArrayList<>();
        for (var id : order) if (id.getAsString().startsWith("geontm:")) ids.add(id.getAsString());
        return ids;
    }
}
