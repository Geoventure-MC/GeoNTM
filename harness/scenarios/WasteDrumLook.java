import fr.geoventure.harness.api.Client;
import fr.geoventure.harness.api.Expect;
import fr.geoventure.harness.api.Fixtures;
import fr.geoventure.harness.api.Scenario;

/** The spent fuel pool drum shows 1.7's {@code waste_drum} top and {@code waste_drum_side} sides, in the world and in hand. */
public final class WasteDrumLook implements Scenario {

    private static final int Y = 100;

    @Override
    public void run(Client c) throws Exception {
        c.useFlatWorld();
        c.waitInWorld(120_000L);
        Fixtures.platform(c, 0, Y, 0, 6);
        c.cmd("gamemode creative");
        c.cmd("fill -3 101 -3 3 104 3 air");
        Fixtures.setBlock(c, 0, Y + 1, 0, "geontm:machine_waste_drum");
        Fixtures.setBlock(c, 1, Y + 1, 0, "geontm:machine_lithium_battery");
        Fixtures.giveOnly(c, "geontm:machine_waste_drum", 1);
        c.teleport(0.5, Y + 2.2, -2.5);
        c.waitTicks(3);
        c.lookAt(0.7, Y + 1.5, 0.5);
        c.waitTicks(20);
        c.screenshot("01-drum-and-li-ion");
        Expect.equal("geontm:machine_waste_drum", c.block(0, Y + 1, 0).get("block").getAsString(), "drum placed");
    }
}
