import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.geoventure.harness.api.Client;
import fr.geoventure.harness.api.Expect;
import fr.geoventure.harness.api.Fixtures;
import fr.geoventure.harness.api.Scenario;

import java.util.ArrayList;
import java.util.List;

/**
 * Armor table with the insert, cladding, battery and revive modules in their own slots (real shift-clicks), then the
 * helmet-only slot taking one charm and refusing a second helmet module. Wearing the plate: the steel insert slows the
 * wearer by 5%. Hover screenshots of the insert, cladding and charm tooltips.
 */
public final class RcGear2Mods implements Scenario {

    private static final int Y = 100;
    private static final int TZ = 2;
    private static final int HELMET_SLOT = 0, CLADDING = 5, KEVLAR = 6, EXTRA = 7, BATTERY = 8, ARMOR = 9, CHEST = 11;

    @Override
    public void run(Client c) throws Exception {
        c.waitInWorld(120_000L);
        c.markLog();
        c.key("escape");
        c.waitTicks(5);
        Fixtures.platform(c, 0, Y, 0, 10);
        c.cmd("gamemode survival");
        c.cmd("effect clear @s");
        c.cmd("clear");
        for (String s : new String[] {"head", "chest", "legs", "feet"}) c.cmd("item replace entity @s armor." + s + " with minecraft:air");
        Fixtures.placeOnTop(c, "geontm:machine_armor_table", 0, Y, TZ);
        c.waitTicks(3);
        for (String id : new String[] {"geontm:steel_plate", "geontm:insert_steel", "geontm:cladding_lead", "geontm:armor_battery", "geontm:scrumpy"}) {
            c.cmd("give @s " + id + " 1");
        }
        c.waitTicks(5);
        open(c);
        shift(c, "geontm:steel_plate");
        shift(c, "geontm:insert_steel");
        shift(c, "geontm:cladding_lead");
        shift(c, "geontm:armor_battery");
        shift(c, "geontm:scrumpy");
        slotHolds(c, KEVLAR, "geontm:insert_steel");
        slotHolds(c, CLADDING, "geontm:cladding_lead");
        slotHolds(c, BATTERY, "geontm:armor_battery");
        // 1.7 ItemModRevive fits leggings only, so a chestplate refuses it
        Expect.that(c.slot(EXTRA).get("empty").getAsBoolean(), "a revive module is refused by a chestplate");
        hover(c, KEVLAR, "insert-tooltip");
        hover(c, CLADDING, "cladding-tooltip");
        c.clickSlot(ARMOR);
        c.waitTicks(2);
        c.clickSlot(CHEST);
        c.waitTicks(3);
        c.key("escape");
        c.waitTicks(30);
        JsonObject armor = c.query("geontm_armor", new JsonObject());
        List<String> mods = mods(armor, "chest");
        System.out.println("[RcGear2Mods] " + c.node() + " chest mods " + mods + " speed " + armor.get("movement_speed"));
        Expect.that(mods.size() == 3, "insert, cladding and battery stay on the plate, got " + mods);
        double speed = armor.get("movement_speed").getAsDouble();
        Expect.that(Math.abs(speed - 0.1 * 0.95) < 0.0005, "steel insert slows by 5%: 0.095, got " + speed);

        // helmet-only slot: one charm fits, the gas mask attachment is then refused by the occupied slot
        c.cmd("clear");
        for (String id : new String[] {"geontm:steel_helmet", "geontm:protection_charm", "geontm:attachment_mask"}) c.cmd("give @s " + id + " 1");
        c.waitTicks(4);
        open(c);
        shift(c, "geontm:steel_helmet");
        shift(c, "geontm:protection_charm");
        slotHolds(c, HELMET_SLOT, "geontm:protection_charm");
        shift(c, "geontm:attachment_mask");
        slotHolds(c, HELMET_SLOT, "geontm:protection_charm");
        Expect.that(find(c, "geontm:attachment_mask") >= 0, "the second helmet module stays in the inventory");
        hover(c, HELMET_SLOT, "charm-tooltip");
        c.key("escape");
        c.waitTicks(20);
        c.cmd("clear");
        c.cmd("gamemode creative");
        Expect.logCleanSinceMark(c, "/(ERROR|FATAL)[]]|Exception");
    }

    private void open(Client c) {
        c.teleport(0.5, Y + 1, TZ - 1.5);
        c.waitTicks(2);
        Fixtures.interact(c, 0, Y + 1, TZ);
        c.waitScreen("Armor Modification Table", 5000);
        Expect.screenStaysOpen(c, "Armor Modification Table", 10);
    }

    private void shift(Client c, String id) {
        int from = find(c, id);
        Expect.that(from >= 0, id + " is in the menu inventory");
        c.slotClick(from, 0, "QUICK_MOVE");
        c.waitTicks(3);
    }

    private void slotHolds(Client c, int slot, String id) {
        JsonObject s = c.slot(slot);
        Expect.that(s != null && !s.get("empty").getAsBoolean() && s.get("id").getAsString().equals(id),
                "slot " + slot + " holds " + id + ", got " + s);
    }

    private void hover(Client c, int slot, String shot) {
        JsonObject s = c.slot(slot);
        c.mouseMove(s.get("center_x").getAsDouble(), s.get("center_y").getAsDouble());
        c.waitTicks(8);
        c.screenshot(shot);
    }

    private int find(Client c, String id) {
        for (JsonElement e : c.screen().getAsJsonArray("slots")) {
            JsonObject s = e.getAsJsonObject();
            if (s.get("player_inventory").getAsBoolean() && s.get("index").getAsInt() > 13
                    && !s.get("empty").getAsBoolean() && s.get("id").getAsString().equals(id)) return s.get("index").getAsInt();
        }
        return -1;
    }

    private static List<String> mods(JsonObject armor, String slot) {
        List<String> out = new ArrayList<>();
        JsonArray mods = armor.getAsJsonObject(slot).getAsJsonArray("mods");
        for (JsonElement e : mods) out.add(e.getAsString());
        return out;
    }
}
