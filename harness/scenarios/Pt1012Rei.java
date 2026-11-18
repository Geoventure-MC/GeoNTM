import com.google.gson.JsonObject;
import fr.geoventure.harness.api.Client;
import fr.geoventure.harness.api.Expect;
import fr.geoventure.harness.api.Fixtures;
import fr.geoventure.harness.api.Scenario;

import java.util.List;

/**
 * REI support (playtest wave 2026-10-12): HBM's recipe-viewer declarations ({@code fr.geoventure.geontm.handler.jei.GeoNtmRecipeViewer})
 * fed to REI through GeoCore's {@code fr.geoventure.geocore.rei}, beside JEI, on a client started with {@code -Prei.run=true}
 * (REI, Cloth Config and Architectury in the run; JEI stays, so this is also the "both viewers installed" case).
 *
 * <ol>
 *   <li>REI registered HBM's client plugin ({@code GeoNtmReiClientPlugin}) and JEI still ran HBM's JEI plugin (its runtime
 *       line, "[JEI] arc furnace furnace pass");</li>
 *   <li>the Core query {@code rei} counts HBM's REI categories and their displays: every category once (REI would refuse
 *       a duplicate id), the machine tables populated;</li>
 *   <li>the fluid icons stay one REI entry per fluid (HBM's subtypes as REI item comparators);</li>
 *   <li>REI's recipe screen opened on GEONTM machine categories (centrifuge, press with its animated head, assembly
 *       machine, the solid arc furnace with its furnace-smelting pass, the anvil, the gas centrifuge), each
 *       screenshotted;</li>
 *   <li>REI's "+" transfer into an open centrifuge menu moves a euphemium cluster into input slot 0.</li>
 * </ol>
 * Needs a client built with REI: {@code ORG_GRADLE_PROJECT_rei.run=true harness up --rebuild}.
 */
public final class Pt1012Rei implements Scenario {

    private static JsonObject rei(Client c, String... kv) {
        JsonObject p = new JsonObject();
        for (int i = 0; i + 1 < kv.length; i += 2) p.addProperty(kv[i], kv[i + 1]);
        return c.query("rei", p);
    }

    @Override
    public void run(Client c) throws Exception {
        c.waitInWorld(180_000L);
        c.waitTicks(40);

        List<String> plugin = c.log(40_000, "GeoNtmReiClientPlugin");
        Expect.that(!plugin.isEmpty(), "REI lists HBM's client plugin (GeoNtmReiClientPlugin) in its plugin manager log");
        List<String> jei = c.log(40_000, "\\[JEI\\] arc furnace furnace pass");
        Expect.that(!jei.isEmpty(), "JEI still runs HBM's JEI plugin beside REI ([JEI] arc furnace furnace pass)");

        c.markLog();
        JsonObject status = rei(c, "op", "status");
        for (int i = 0; i < 40 && status.get("displays").getAsInt() == 0; i++) {
            c.waitTicks(20);
            status = rei(c, "op", "status");
        }
        int categories = status.get("categories").getAsInt();
        int displays = status.get("displays").getAsInt();
        String ids = status.get("ids").toString();
        System.out.println("[Pt1012Rei] " + c.node() + " REI: " + categories + " GEONTM categories, " + displays + " displays: " + ids);
        Expect.that(categories >= 48, "REI holds HBM's categories (48+ incl. RTG, satellite, radiolysis, breeder): " + categories);
        Expect.that(displays > 1000, "REI holds HBM's recipes as displays: " + displays);
        for (String id : new String[] {"geontm:centrifuge=", "geontm:press=", "geontm:assembly_machine=", "geontm:chemical_plant=",
                "geontm:anvil_construction=", "geontm:arc_furnace_solid="}) {
            Expect.that(ids.contains(id) && !ids.contains(id + "0\""), "REI category " + id + " has displays: " + ids);
        }

        // subtypes (GeoNtmViewerSubtypes as REI item comparators): one list entry per fluid, as in JEI
        JsonObject icons = rei(c, "op", "status", "entries", "geontm:fluid_icon");
        Expect.that(icons.get("entries").getAsInt() >= 150, "REI lists a fluid icon per GEONTM fluid (subtypes kept apart): " + icons.get("entries"));

        open(c, "geontm:centrifuge", "10-rei-centrifuge");
        open(c, "geontm:press", "11-rei-press");
        open(c, "geontm:assembly_machine", "12-rei-assembly-machine");
        open(c, "geontm:arc_furnace_solid", "13-rei-arc-furnace-solid");
        open(c, "geontm:anvil_construction", "14-rei-anvil-construction");
        open(c, "geontm:gas_centrifuge", "15-rei-gas-centrifuge");

        transfer(c);

        List<String> pass = c.log(40_000, "\\[REI\\] arc furnace furnace pass");
        Expect.that(!pass.isEmpty(), "REI built the arc furnace furnace pass when its page was asked for");
        Expect.logCleanSinceMark(c, "(?i)(exception|\\[REI\\].*error|failed to).*");
    }

    /**
     * REI's "+" transfer (the centrifuge's slot-0 handler, GeoNtmRecipeViewer.transfers): with the centrifuge's menu open
     * and a euphemium cluster in the inventory, REI's transfer handlers run on the centrifuge display for it, and the
     * cluster lands in the machine's input slot 0.
     */
    private static void transfer(Client c) {
        int x = 0, y = 100, z = 0;
        Fixtures.platform(c, x, y, z, 6);
        Fixtures.placeOnTop(c, "geontm:machine_centrifuge", x, y, z + 3);
        c.cmd("clear @s");
        c.cmd("give @s geontm:block_euphemium_cluster 4");
        c.waitTicks(4);
        c.teleport(x + 2.5, y + 1, z + 3.5);
        c.waitTicks(3);
        Fixtures.interact(c, x, y + 1, z + 3);
        c.waitScreen("Centrifuge", 5_000L);
        c.waitTicks(5);
        JsonObject result = rei(c, "op", "transfer", "category", "geontm:centrifuge", "item", "geontm:block_euphemium_cluster");
        c.waitTicks(20);
        c.screenshot("20-rei-transfer-centrifuge");
        JsonObject slot = c.slot(0);
        Expect.that(result.toString().contains("successful by fr.geoventure.geocore.rei"), "REI ran HBM's centrifuge transfer handler: " + result);
        Expect.that(slot.toString().contains("geontm:block_euphemium_cluster"), "the cluster went into the centrifuge's input slot 0: " + slot);
        c.key("escape");
        c.waitTicks(5);
        c.cmd("clear @s");
    }

    private static void open(Client c, String category, String shot) {
        JsonObject opened = rei(c, "op", "open", "category", category);
        Expect.that(opened.has("opened") && opened.get("opened").getAsBoolean(), "REI opened " + category + ": " + opened);
        c.waitTicks(30);
        String screen = c.screenClass();
        Expect.that(screen != null && screen.contains("Display"), "REI's recipe screen is up for " + category + ": " + screen);
        c.screenshot(shot);
        c.key("escape");
        c.waitTicks(10);
    }
}
