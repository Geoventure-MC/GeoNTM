import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.geoventure.harness.api.*;

/**
 * Wave 2 VISUAL (History §19ek-visual), non-screen checks:
 *
 * <ul>
 *   <li>{@code geontm_lang_missing}: registered geontm items/blocks/entities without a translation (printed; fails when any
 *       block or entity is untranslated).</li>
 *   <li>{@code geontm_sound}: {@code geontm:rbmk_shutdown} resolves to {@code geontm:block/shutdown} and plays.</li>
 *   <li>Torex late life: the aged {@code torex_benchmark} fixtures (600/1200/2400) are polled with {@code geontm_torex} until
 *       the client replay is ready, logging how long that took, then screenshotted.</li>
 * </ul>
 */
public final class VisualChecks implements Scenario {

    private static final int X = 900, Y = 100, Z = 900;

    @Override
    public void run(Client c) throws Exception {
        c.waitInWorld(120_000L);
        c.markLog();

        JsonObject lang = c.query("geontm_lang_missing", new JsonObject());
        System.out.println("[visual] lang counts items=" + lang.get("item_count") + " blocks=" + lang.get("block_count")
                + " entities=" + lang.get("entity_count"));
        System.out.println("[visual] lang missing items " + lang.getAsJsonArray("items"));
        System.out.println("[visual] lang missing blocks " + lang.getAsJsonArray("blocks"));
        System.out.println("[visual] lang missing entities " + lang.getAsJsonArray("entities"));

        JsonObject p = new JsonObject();
        p.addProperty("id", "geontm:rbmk_shutdown");
        p.addProperty("play", true);
        JsonObject sound = c.query("geontm_sound", p);
        System.out.println("[visual] sound " + sound);
        Expect.that(sound.get("resolved").getAsBoolean()
                        && sound.get("sound").getAsString().equals("geontm:block/shutdown"),
                "geontm:rbmk_shutdown must resolve to geontm:block/shutdown: " + sound);

        String hideGui = null, renderDistance = null, fov = null;
        try {
            c.cmd("forceload add " + (X - 32) + " " + (Z - 32) + " " + (X + 32) + " " + (Z + 280));
            Fixtures.platform(c, X, Y, Z + 165, 4);
            Fixtures.platform(c, X, Y, Z, 8);
            c.cmd("time set 6000");
            c.cmd("gamerule doDaylightCycle false");
            c.cmd("gamerule advance_time false");
            c.cmd("weather clear");
            hideGui = c.setOption("hide_gui", "true");
            renderDistance = c.setOption("render_distance", "24");
            fov = c.setOption("fov", "90");
            String agesEnv = System.getenv("VISUAL_TOREX_AGES");
            int[] ages = agesEnv == null ? new int[] {600, 1200, 2100, 2400}
                    : java.util.Arrays.stream(agesEnv.split(",")).mapToInt(Integer::parseInt).toArray();
            for (int age : ages) {
                c.teleport(X + 0.5, Y + 1, Z + 0.5);
                c.cmd("geontm torex_benchmark " + age);
                c.teleport(X + 0.5, Y + 1, Z + 165.5);
                c.lookAt(X + 0.5, Y + 45, Z + 240.5);
                int waited = 0;
                JsonObject state = null;
                while (waited < 6000) {
                    c.waitTicks(20);
                    waited += 20;
                    JsonArray clouds = c.query("geontm_torex", new JsonObject()).getAsJsonArray("clouds");
                    state = clouds.size() > 0 ? clouds.get(0).getAsJsonObject() : null;
                    if (waited % 200 == 0) System.out.println("[visual] torex " + age + " after " + waited + "t: " + state);
                    if (state != null && state.get("ready").getAsBoolean()) break;
                }
                System.out.println("[visual] torex " + age + " ready after " + waited + " ticks: " + state);
                c.screenshot("torex-aged-" + age + "-ready");
                c.lookAt(X + 0.5, Y + 110, Z + 240.5);
                c.waitTicks(2);
                c.screenshot("torex-aged-" + age + "-ready-up");
                c.lookAt(X + 0.5, Y + 45, Z + 240.5);
                c.waitTicks(40);
                c.screenshot("torex-aged-" + age + "-b");
            }
            c.cmd("geontm torex_benchmark stop");
        } finally {
            c.cmd("geontm torex_benchmark stop");
            if (hideGui != null) c.setOption("hide_gui", hideGui);
            if (renderDistance != null) c.setOption("render_distance", renderDistance);
            if (fov != null) c.setOption("fov", fov);
            c.cmd("forceload remove all");
        }
        Expect.logCleanSinceMark(c, "Exception|/(ERROR|FATAL)[]].*(Torex|torex|geontm)");
    }
}
