import com.google.gson.JsonObject;
import fr.geoventure.harness.api.*;

/**
 * Playtest 2026-10-12: GEONTM ladders collide like vanilla (and 1.12.2) ladders. A survival player walks south into an
 * {@code geontm:ladder_red} hanging on a stone wall: with collision the player stops at the ladder's face (z = 9.8125 - 0.3)
 * instead of the wall (z = 9.7), and climbs it. A vanilla ladder beside it is the control.
 */
public final class Pt1012LadderCollision implements Scenario {

    private static final int Y = 100;

    @Override
    public void run(Client c) throws Exception {
        c.waitInWorld(120_000L);
        c.markLog();
        Fixtures.platform(c, 0, Y, 0, 20);
        c.cmd("fill 0 " + (Y + 1) + " 0 15 " + (Y + 8) + " 15 minecraft:air");
        c.cmd("fill 3 " + (Y + 1) + " 10 8 " + (Y + 6) + " 10 minecraft:stone");
        double geontm = walk(c, 5, "geontm:ladder_red");
        double vanilla = walk(c, 7, "minecraft:ladder");
        c.cmd("gamemode creative");
        Expect.that(Math.abs(geontm - vanilla) < 0.02, "geontm ladder stops the player where a vanilla ladder does: geontm z="
                + geontm + ", vanilla z=" + vanilla);
        Expect.that(geontm < 9.6, "geontm ladder collides (player z " + geontm + " < 9.6; without collision it is 9.7)");
        Expect.logCleanSinceMark(c, "/(ERROR|FATAL)[]]");
    }

    /** Walks into a ladder at (x, Y+1..Y+4, 9) facing north; returns the player's z at the ladder and checks it climbs. */
    private static double walk(Client c, int x, String ladder) throws Exception {
        for (int k = 1; k <= 4; k++) c.cmd("setblock " + x + " " + (Y + k) + " 9 " + ladder + "[facing=north]");
        c.cmd("gamemode survival");
        c.teleport(x + 0.5, Y + 1, 7.5);
        c.look(0, 0);   // yaw 0 = south (+Z)
        c.waitTicks(10);
        c.keyDown("key.forward");
        c.waitTicks(12);
        JsonObject at = c.player();
        c.waitTicks(20);
        JsonObject up = c.player();
        c.keyUp("key.forward");
        c.screenshot("ladder-" + x);
        double z = at.get("z").getAsDouble();
        double y = up.get("y").getAsDouble();
        Expect.that(y > Y + 1.5, ladder + ": pressing into the ladder climbs it (y " + y + ")");
        return z;
    }
}
