import fr.geoventure.harness.api.Client;
import fr.geoventure.harness.api.Expect;
import fr.geoventure.harness.api.Fixtures;
import fr.geoventure.harness.api.Scenario;

/**
 * GitHub issue 72: the Target Pistol (gun_star_f) shows 1.7's durability bar and ammo counter. Shots of the held pistol
 * and of the Uzi, which always had them, for comparison of the bottom-right HUD.
 */
public final class Gh72TargetPistolHud implements Scenario {

    @Override
    public void run(Client c) throws Exception {
        c.waitInWorld(180_000L);
        c.useFlatWorld();
        c.cmd("gamemode survival");
        c.markLog();
        for (String gun : new String[] {"gun_star_f", "gun_uzi"}) {
            Fixtures.giveOnly(c, "geontm:" + gun, 1);
            c.look(0, 10);
            c.waitTicks(40);
            Expect.that(c.inventory().toString().contains(gun), gun + " is held: " + c.inventory());
            c.screenshot("hud-" + gun);
        }
        c.cmd("gamemode creative");
        Expect.logCleanSinceMark(c, "(?i)(exception|/(ERROR|FATAL)[]]).*");
    }
}
