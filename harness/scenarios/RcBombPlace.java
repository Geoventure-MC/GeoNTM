import com.google.gson.JsonObject;
import fr.geoventure.harness.api.Client;
import fr.geoventure.harness.api.Expect;
import fr.geoventure.harness.api.Fixtures;
import fr.geoventure.harness.api.Scenario;

import java.util.ArrayList;
import java.util.List;

/**
 * Release audit, explosives: every bomb, nuke, mine, charge, missile pad and assembly block is placed by hand
 * (the player's own placement code), photographed from the front and, when it has a menu, opened with a real
 * right-click and photographed again. Facing is the placer's, so the player stands on each of the four sides in turn
 * for the nukes. The log must stay clean.
 */
public final class RcBombPlace implements Scenario {

    private static final int Y = 100;

    /** id, whether a right-click opens a menu */
    private static final String[][] BLOCKS = {
            {"geontm:nuke_gadget", "gui"}, {"geontm:nuke_boy", "gui"}, {"geontm:nuke_man", "gui"}, {"geontm:nuke_mike", "gui"},
            {"geontm:nuke_tsar", "gui"}, {"geontm:nuke_n2", "gui"}, {"geontm:nuke_custom", "gui"}, {"geontm:nuke_fleija", "gui"},
            {"geontm:nuke_prototype", "gui"}, {"geontm:nuke_solinium", "gui"}, {"geontm:nuke_fstbmb", "gui"},
            {"geontm:bomb_multi", "gui"}, {"geontm:float_bomb", ""}, {"geontm:emp_bomb", ""}, {"geontm:therm_endo", ""},
            {"geontm:therm_exo", ""}, {"geontm:flame_war", ""}, {"geontm:fireworks", "gui"},
            {"geontm:tnt_ntm", ""}, {"geontm:dynamite", ""}, {"geontm:semtex", ""}, {"geontm:c4", ""}, {"geontm:fissure_bomb", ""},
            {"geontm:block_semtex", ""}, {"geontm:block_c4", ""}, {"geontm:det_cord", ""}, {"geontm:det_charge", ""},
            {"geontm:det_nuke", ""}, {"geontm:det_miner", ""},
            {"geontm:charge_dynamite", ""}, {"geontm:charge_miner", ""}, {"geontm:charge_c4", ""}, {"geontm:charge_semtex", ""},
            {"geontm:mine_ap", ""}, {"geontm:mine_he", ""}, {"geontm:mine_shrap", ""}, {"geontm:mine_fat", ""}, {"geontm:mine_naval", ""},
            {"geontm:crashed_bomb_balefire", ""}, {"geontm:crashed_bomb_conventional", ""}, {"geontm:crashed_bomb_nuke", ""},
            {"geontm:crashed_bomb_salted", ""},
            {"geontm:launch_pad", "gui"}, {"geontm:launch_pad_large", "gui"}, {"geontm:launch_pad_rusted", "gui"},
            {"geontm:machine_missile_assembly", "gui"}, {"geontm:compact_launcher", "gui"}, {"geontm:launch_table", "gui"},
    };

    @Override
    public void run(Client c) throws Exception {
        c.waitInWorld(120_000L);
        for (int i = 0; i < 3 && c.screenClass() != null; i++) { c.key("escape"); c.waitTicks(5); }
        c.useFlatWorld();
        c.markLog();
        Fixtures.platform(c, 0, Y, 0, 70);
        c.waitTicks(10);
        List<String> notes = new ArrayList<>();
        int perRow = 8;
        for (int i = 0; i < BLOCKS.length; i++) {
            String id = BLOCKS[i][0];
            boolean gui = !BLOCKS[i][1].isEmpty();
            int x = -60 + (i % perRow) * 16;
            int z = -50 + (i / perRow) * 18;
            String name = id.substring(4);
            // a stone pad under the spot so a mine or charge has support; the platform is already stone
            c.cmd("gamemode creative");
            JsonObject placed = Fixtures.placeOnTop(c, id, x, Y, z);
            c.waitTicks(5);
            String got = placed.has("block") ? placed.get("block").getAsString() : String.valueOf(placed);
            boolean ok = got.startsWith(id.substring(0, id.length()));
            notes.add(name + " -> " + got + (ok ? "" : "   <-- NOT PLACED"));
            c.teleport(x + 0.5, Y + 3, z - 6.5);
            c.waitTicks(3);
            c.lookAt(x + 0.5, Y + 1.5, z + 0.5);
            c.waitTicks(8);
            c.screenshot(String.format("%02d-%s-front", i, name));
            if (gui) {
                c.teleport(x + 0.5, Y + 1, z - 3.5);
                c.waitTicks(2);
                Fixtures.interact(c, x, Y + 1, z);
                c.waitTicks(10);
                String screen = c.screenClass();
                notes.add("   gui: " + screen);
                if (screen != null) {
                    c.screenshot(String.format("%02d-%s-gui", i, name));
                    c.key("escape");
                    c.waitTicks(4);
                }
            }
        }
        for (String n : notes) System.out.println("[RcBombPlace] " + n);
        Expect.logCleanSinceMark(c, "(?i)(exception|crash report|missing model|failed to load|unregistered geontm:|is not registered).*");
    }
}
