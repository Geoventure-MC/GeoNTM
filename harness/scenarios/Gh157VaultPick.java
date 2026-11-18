import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.geoventure.harness.api.Client;
import fr.geoventure.harness.api.Expect;
import fr.geoventure.harness.api.Fixtures;
import fr.geoventure.harness.api.Scenario;

/**
 * GitHub issue 157, point 3: a locked safe could not be picked with a bobby pin and a screwdriver. 1.7
 * {@code TileEntityLockableBase.tryPick}: a pin in hand while the inventory holds a screwdriver (plain or desh), or a
 * screwdriver in hand while it holds a pin, spends one pin and opens with chance {@code lockMod}. Each case is a real
 * right-click on a safe locked with pins 123; {@code lockMod} 1 always opens, 0 never does.
 */
public class Gh157VaultPick implements Scenario {

    private static final int Y = 100, G = 101;

    @Override
    public void run(Client c) throws Exception {
        c.useFlatWorld();
        c.markLog();
        Fixtures.platform(c, 0, Y, 0, 8);
        c.cmd("fill -8 " + G + " -8 8 " + (Y + 6) + " 8 minecraft:air");
        c.cmd("kill @e[type=item]");
        c.cmd("gamemode survival");

        attempt(c, "pin-in-hand-screwdriver-in-hotbar", "geontm:pin", "geontm:screwdriver", false, 1.0, true);
        attempt(c, "pin-in-hand-screwdriver-in-offhand", "geontm:pin", "geontm:screwdriver", true, 1.0, true);
        attempt(c, "screwdriver-in-hand-pin-in-hotbar", "geontm:screwdriver", "geontm:pin", false, 1.0, true);
        attempt(c, "screwdriver-in-hand-pin-in-offhand", "geontm:screwdriver", "geontm:pin", true, 1.0, true);
        attempt(c, "desh-screwdriver-in-hand", "geontm:screwdriver_desh", "geontm:pin", false, 1.0, true);
        attempt(c, "pin-with-desh-screwdriver", "geontm:pin", "geontm:screwdriver_desh", false, 1.0, true);
        attempt(c, "pin-alone", "geontm:pin", null, false, 1.0, false);
        attempt(c, "unpickable-lock", "geontm:pin", "geontm:screwdriver", false, 0.0, false);

        c.cmd("gamemode creative");
        Expect.logCleanSinceMark(c, "(?i)(exception|\\[ERROR\\]).*(geontm|geocore)");
        System.out.println("[Gh157] PASS");
    }

    private static void attempt(Client c, String label, String hand, String other, boolean offhand, double mod,
                                boolean opens) throws Exception {
        c.cmd("clear");
        c.cmd("setblock 0 " + G + " 0 minecraft:air");
        c.waitTicks(2);
        c.cmd("setblock 0 " + G + " 0 geontm:safe[facing=north]");
        c.cmd("data merge block 0 " + G + " 0 {lock:123,isLocked:1b,lockMod:" + mod + "d}");
        c.waitTicks(3);
        c.cmd("give @s " + hand + " " + count(hand));
        c.hotbar(1);
        if (other != null) {
            if (offhand) c.cmd("item replace entity @s weapon.offhand with " + other + " " + count(other));
            else c.cmd("item replace entity @s hotbar.4 with " + other + " " + count(other));
        }
        c.waitTicks(5);
        c.teleport(0.5, G, -2.5);
        c.waitTicks(4);
        c.lookAt(0.5, G + 0.5, 0.0);
        c.waitTicks(4);
        int pinsBefore = count(c, "geontm:pin");
        c.use(2);
        c.waitTicks(15);
        String screen = c.screenClass();
        boolean open = screen != null && !screen.isEmpty() && !screen.equals("null")
                && c.screen().toString().contains("Safe");
        c.screenshot(label);
        if (open) {
            c.key("escape");
            c.waitTicks(5);
        }
        int pinsAfter = count(c, "geontm:pin");
        System.out.println("[Gh157] " + label + ": opened " + open + ", pins " + pinsBefore + " -> " + pinsAfter);
        Expect.that(open == opens, label + ": the safe " + (opens ? "must" : "must not") + " open");
        boolean picks = other != null && (hand.equals("geontm:pin") || other.equals("geontm:pin"));
        if (picks) Expect.that(pinsAfter == pinsBefore - 1, label + ": a pick spends one pin");
        else Expect.that(pinsAfter == pinsBefore, label + ": no pick, no pin spent");
    }

    private static int count(String id) {
        return id.contains("screwdriver") ? 1 : 4;
    }

    private static int count(Client c, String id) {
        int total = 0;
        JsonObject inv = c.inventory();
        for (String key : new String[]{"slots", "items", "inventory"}) {
            if (!inv.has(key)) continue;
            for (JsonElement e : inv.getAsJsonArray(key)) {
                JsonObject s = e.getAsJsonObject();
                if (s.has("id") && s.get("id").getAsString().equals(id)) total += s.get("count").getAsInt();
            }
            return total;
        }
        Expect.that(false, "unknown inventory shape: " + inv);
        return 0;
    }
}
