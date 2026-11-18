import com.google.gson.JsonObject;
import fr.geoventure.harness.api.Client;
import fr.geoventure.harness.api.Expect;
import fr.geoventure.harness.api.Fixtures;
import fr.geoventure.harness.api.Scenario;

/**
 * In-game acceptance for the norwood-deco lantern: the folded five-tall post, its core block entity,
 * the {@code LanternRenderer} view, and the once-a-second glyphid blinding sweep.
 */
public final class LanternPost implements Scenario {

    private static final int X = 0;
    private static final int Y = 100;
    private static final int Z = 2;

    @Override
    public void run(Client c) throws Exception {
        c.markLog();
        Fixtures.platform(c, X, Y, 0, 10);
        c.cmd("difficulty normal");
        c.waitTicks(2);

        // The item places the core one above the clicked block; the four dummy cells fold upward.
        JsonObject placed = Fixtures.placeOnTop(c, "geontm:lantern", X, Y, Z);
        Expect.equal("geontm:lantern", placed.get("block").getAsString(), "lantern core placed by hand");
        for (int dy = 1; dy <= 4; dy++) {
            Expect.block(c, X, Y + 1 + dy, Z, "geontm:lantern");
        }
        c.screenshot("10-lantern-placed");

        // Only the core carries the block entity; the dummy cells must not.
        JsonObject core = c.blockEntity(X, Y + 1, Z);
        Expect.that(String.valueOf(core.get("block_entity")).contains("TileEntityLantern"),
                "core holds TileEntityLantern: " + core.get("block_entity"));

        // The post from a distance: five cells of OBJ, drawn by LanternRenderer.
        c.teleport(X + 7.5, Y + 1, Z + 0.5);
        c.waitTicks(2);
        c.lookAt(X + 0.5, Y + 4.0, Z + 0.5);
        c.waitTicks(5);
        c.screenshot("20-lantern-render");

        // The sweep: a glyphid inside 7.5 blocks of the lamp must get blindness within one second.
        c.cmd("summon geontm:entity_glyphid " + (X + 1) + " " + (Y + 1) + " " + (Z + 1));
        c.waitTicks(40);
        JsonObject glyphids = c.query("entities", json("radius", 20, "type", "glyphid"));
        Expect.that(glyphids.getAsJsonArray("entities").size() > 0,
                "a glyphid stands beside the lantern: " + glyphids);
        c.cmd("data get entity @e[type=geontm:entity_glyphid,limit=1]");
        c.waitTicks(5);
        // Vanilla 1.20.1 persists mob effects by numeric registry id; Forge may also name them.
        Expect.logMatches(c, c.node().startsWith("1.20.")
                ? "(?i)blindness|ActiveEffects:.*\\bId:\\s*15\\b"
                : "(?i)blindness", 80);
        c.screenshot("30-lantern-glyphid-blinded");

        Expect.logCleanSinceMark(c, "/(ERROR|FATAL)[]]|Failed to load texture");
    }

    private static JsonObject json(String k1, Object v1, String k2, Object v2) {
        JsonObject o = new JsonObject();
        o.addProperty(k1, (Number) v1);
        o.addProperty(k2, String.valueOf(v2));
        return o;
    }
}
