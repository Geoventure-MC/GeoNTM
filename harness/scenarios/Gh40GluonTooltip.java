import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.geoventure.harness.api.Client;
import fr.geoventure.harness.api.Expect;
import fr.geoventure.harness.api.Fixtures;
import fr.geoventure.harness.api.Scenario;

/**
 * GitHub issue 40: the gluon gun's tooltip read "Base Damage: 0" because its receiver never fires. Hovers the gun in
 * the survival inventory with uranium in the belt and shoots the tooltip, which should name the beam's damage.
 */
public final class Gh40GluonTooltip implements Scenario {

    @Override
    public void run(Client c) throws Exception {
        c.useFlatWorld();
        c.waitInWorld(120_000L);
        c.cmd("gamemode survival");
        Fixtures.giveOnly(c, "geontm:gun_egon", 1);
        Fixtures.give(c, "geontm:ammo_standard_tau_uranium", 34);
        c.key("e");
        c.waitTicks(5);
        JsonObject screen = Expect.screenOpen(c, "Inventory");
        JsonObject gun = null;
        for (JsonElement e : screen.getAsJsonArray("slots")) {
            JsonObject s = e.getAsJsonObject();
            if (!s.get("empty").getAsBoolean() && s.get("id").getAsString().equals("geontm:gun_egon")) gun = s;
        }
        Expect.that(gun != null, "the gluon gun is in the inventory screen");
        c.mouseMove(gun.get("center_x").getAsInt(), gun.get("center_y").getAsInt());
        c.waitTicks(5);
        c.screenshot("10-gluon-tooltip");
        c.key("escape");
        c.waitTicks(3);
        c.cmd("clear");
        c.cmd("gamemode creative");
    }
}
