import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.geoventure.harness.api.Client;
import fr.geoventure.harness.api.Expect;
import fr.geoventure.harness.api.Scenario;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Release audit, explosives: each nuclear bomb is filled through the container commands, lit with a redstone block on
 * fresh far ground and photographed; the crater floor is read back along +x and compared with the configured radius
 * (BombConfig = 1.7: Boy 120, Man 175, Mike filled 250, Tsar 500, Fleija 50, Prototype 150, Solinium 150, balefire 250).
 * The effect entities seen during the blast are listed. Fallout is stopped after the crater is read. Tsar runs last and
 * is skipped when the earlier bombs took more than eight minutes.
 */
public final class RcBombNukes implements Scenario {

    private static final int Y = -60;

    /** id, expected radius, fill as {slot, item} pairs */
    private static Object[][] bombs(boolean legacy) {
        String cell = legacy ? "geontm:cell{\"geontm:fluid\":\"geontm:sas3\",\"geontm:fluid_amount\":1000}"
                : "geontm:cell[geontm:fluid=\"geontm:sas3\",geontm:fluid_amount=1000]";
        return new Object[][]{
                {"geontm:nuke_boy", 120, new String[][]{{"0", "geontm:boy_shielding"}, {"1", "geontm:boy_target"}, {"2", "geontm:boy_bullet"},
                        {"3", "geontm:boy_propellant"}, {"4", "geontm:boy_igniter"}}},
                {"geontm:nuke_man", 175, new String[][]{{"0", "geontm:man_igniter"}, {"1", "geontm:gadget_explosive8"}, {"2", "geontm:gadget_explosive8"},
                        {"3", "geontm:gadget_explosive8"}, {"4", "geontm:gadget_explosive8"}, {"5", "geontm:man_core"}}},
                {"geontm:nuke_fleija", 50, new String[][]{{"0", "geontm:fleija_igniter"}, {"1", "geontm:fleija_igniter"}, {"2", "geontm:fleija_propellant"},
                        {"3", "geontm:fleija_propellant"}, {"4", "geontm:fleija_propellant"}, {"5", "geontm:fleija_core"}, {"6", "geontm:fleija_core"},
                        {"7", "geontm:fleija_core"}, {"8", "geontm:fleija_core"}, {"9", "geontm:fleija_core"}, {"10", "geontm:fleija_core"}}},
                {"geontm:nuke_solinium", 150, new String[][]{{"0", "geontm:solinium_igniter"}, {"1", "geontm:solinium_propellant"},
                        {"2", "geontm:solinium_propellant"}, {"3", "geontm:solinium_igniter"}, {"4", "geontm:solinium_core"},
                        {"5", "geontm:solinium_igniter"}, {"6", "geontm:solinium_propellant"}, {"7", "geontm:solinium_propellant"},
                        {"8", "geontm:solinium_igniter"}}},
                {"geontm:nuke_prototype", 150, new String[][]{{"0", cell}, {"1", cell}, {"12", cell}, {"13", cell},
                        {"2", "geontm:rod_quad_uranium"}, {"3", "geontm:rod_quad_uranium"}, {"10", "geontm:rod_quad_uranium"}, {"11", "geontm:rod_quad_uranium"},
                        {"4", "geontm:rod_quad_lead"}, {"5", "geontm:rod_quad_lead"}, {"8", "geontm:rod_quad_lead"}, {"9", "geontm:rod_quad_lead"},
                        {"6", "geontm:rod_quad_np237"}, {"7", "geontm:rod_quad_np237"}}},
                {"geontm:nuke_fstbmb", 250, new String[][]{{"0", "geontm:egg_balefire"}, {"1", "geontm:battery_spark"}}},
                {"geontm:nuke_mike", 250, new String[][]{{"0", "geontm:man_explosive8"}, {"1", "geontm:man_explosive8"}, {"2", "geontm:man_explosive8"},
                        {"3", "geontm:man_explosive8"}, {"4", "geontm:man_core"}, {"5", "geontm:mike_core"}, {"6", "geontm:mike_deut"},
                        {"7", "geontm:mike_cooling_unit"}}},
                {"geontm:nuke_tsar", 500, new String[][]{{"0", "geontm:man_explosive8"}, {"1", "geontm:man_explosive8"}, {"2", "geontm:man_explosive8"},
                        {"3", "geontm:man_explosive8"}, {"4", "geontm:man_core"}, {"5", "geontm:tsar_core"}}},
        };
    }

    @Override
    public void run(Client c) throws Exception {
        c.waitInWorld(120_000L);
        for (int i = 0; i < 3 && c.screenClass() != null; i++) { c.key("escape"); c.waitTicks(5); }
        c.useFlatWorld();
        c.markLog();
        c.cmd("gamemode creative");
        c.cmd("gamerule mobGriefing true");
        boolean legacy = c.node().startsWith("1.20.");
        long started = System.currentTimeMillis();
        List<String> notes = new ArrayList<>();
        Object[][] list = bombs(legacy);
        for (int i = 0; i < list.length; i++) {
            String id = (String) list[i][0];
            int radius = (Integer) list[i][1];
            if (id.equals("geontm:nuke_tsar") && System.currentTimeMillis() - started > 8 * 60_000L) {
                notes.add(id + ": skipped, the earlier bombs used more than eight minutes");
                continue;
            }
            try {
                notes.add(detonate(c, i, id, radius, (String[][]) list[i][2]));
            } catch (RuntimeException e) {
                notes.add(id + ": FAILED " + e.getMessage());
            }
        }
        for (String n : notes) System.out.println("[RcBombNukes] " + n);
        Expect.logCleanSinceMark(c, "(?i)(exception|crash report|failed to load|unregistered geontm:|is not registered).*");
    }

    private String detonate(Client c, int index, String id, int radius, String[][] fill) {
        int x = 3000 * (index + 1);
        int z = 3000;
        long t0 = System.currentTimeMillis();
        c.cmd("forceload add " + (x - 48) + " " + (z - 48) + " " + (x + 48) + " " + (z + 48));
        c.teleport(x + 0.5, Y + 6, z - 4.5);
        c.waitTicks(40);
        c.cmd("setblock " + x + " " + Y + " " + z + " " + id);
        c.waitTicks(4);
        for (String[] f : fill) {
            c.cmd("item replace block " + x + " " + Y + " " + z + " container." + f[0] + " with " + f[1] + " 1");
        }
        c.waitTicks(4);
        String before = c.block(x, Y, z).get("block").getAsString();
        c.setOption("hide_gui", "true");
        c.teleport(x + 75.5, Y + 3, z + 0.5);
        c.waitTicks(10);
        c.lookAt(x + 0.5, Y + 70.0, z + 0.5);
        c.cmd("setblock " + (x + 1) + " " + Y + " " + z + " minecraft:redstone_block");
        c.waitTicks(10);
        String after = c.block(x, Y, z).get("block").getAsString();
        Set<String> seen = new LinkedHashSet<>();
        boolean shot = false;
        int limitTicks = id.equals("geontm:nuke_tsar") ? 20 * 900 : 20 * 300;
        int ticks = 0;
        while (ticks < limitTicks) {
            c.waitTicks(20);
            ticks += 20;
            Set<String> now = effects(c);
            seen.addAll(now);
            if (!shot && ticks >= 100) { c.screenshot(String.format("%d-%s-cloud", index, id.substring(4))); shot = true; }
            if (now.stream().noneMatch(s -> s.contains("explosion") || s.contains("mk5") || s.contains("mk3")
                    || s.contains("balefire"))) break;
        }
        // read the crater floor before the chunks are released
        StringBuilder profile = new StringBuilder();
        int dug = 0;
        for (int d = 0; d <= radius * 1.2; d += 10) {
            JsonObject b = null;
            int floor = -64;
            boolean known = true;
            for (int yy = 40; yy >= -63; yy--) {
                b = c.block(x + d, yy, z);
                if (!b.has("block")) { known = false; break; }
                String bid = b.get("block").getAsString();
                if (!bid.equals("minecraft:air") && !bid.contains("water") && !bid.contains("fire")) { floor = yy; break; }
            }
            if (!known) { profile.append(d).append(":? "); continue; }
            profile.append(d).append(':').append(floor).append(' ');
            if (floor < -61) dug = d;
        }
        c.waitTicks(40);
        c.screenshot(String.format("%d-%s-after", index, id.substring(4)));
        c.setOption("hide_gui", "false");
        c.cmd("kill @e[type=!player]");
        return id + ": block before " + before + ", after the redstone " + after + "; effect entities " + seen + "; blast took "
                + ((System.currentTimeMillis() - t0) / 1000) + " s; configured radius " + radius + ", crater dug out to about "
                + dug + " blocks; floor by distance " + profile;
    }

    private static Set<String> effects(Client c) {
        Set<String> out = new LinkedHashSet<>();
        JsonObject params = new JsonObject();
        params.addProperty("radius", 700);
        params.addProperty("server", true);
        for (JsonElement e : c.query("entities", params).getAsJsonArray("entities")) {
            String t = e.getAsJsonObject().get("type").getAsString();
            if (t.startsWith("geontm:")) out.add(t.substring(4));
        }
        return out;
    }
}
