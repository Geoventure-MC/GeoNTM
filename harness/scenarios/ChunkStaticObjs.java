import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.geoventure.harness.api.Client;
import fr.geoventure.harness.api.Expect;
import fr.geoventure.harness.api.Fixtures;
import fr.geoventure.harness.api.Scenario;

import java.util.ArrayList;
import java.util.List;

/**
 * The static OBJ blocks drawn as chunk models ({@code StaticObjModels}), one row per family in every state, each row shot
 * from a fixed spot so runs before and after a change compare pixel by pixel. Check: none of them has a block entity.
 */
public class ChunkStaticObjs implements Scenario {

    private static final int Y = 100, D = Y + 1;
    private static final String[] H = {"north", "east", "south", "west"};
    private static final String[] ANVILS = {"iron", "lead", "steel", "desh", "ferrouranium", "saturnite", "bismuth_bronze",
            "arsenic_bronze", "schrabidate", "dnt", "osmiridium", "murky"};
    private static final String[] WIRES = {"barbed_wire", "barbed_wire_fire", "barbed_wire_poison", "barbed_wire_acid",
            "barbed_wire_wither", "barbed_wire_ultradeath"};
    private static final String[] BARRELS = {"red_barrel", "pink_barrel", "yellow_barrel", "vitrified_barrel", "lox_barrel",
            "taint_barrel"};

    private static void put(Client c, List<int[]> placed, int x, int z, String block) {
        Fixtures.setBlock(c, x, D, z, block);
        placed.add(new int[]{x, D, z});
    }

    @Override
    public void run(Client c) throws Exception {
        c.key("escape");
        c.waitTicks(2);
        c.resetWorld();
        c.markLog();
        List<int[]> placed = new ArrayList<>();
        List<String> rows = new ArrayList<>();
        Fixtures.platform(c, 12, Y, 20, 28);
        c.cmd("fill -4 " + D + " -4 40 " + (D + 8) + " 48 minecraft:air");
        c.cmd("time set 6000");
        c.cmd("gamerule doDaylightCycle false");

        String[] orients = {"horizontal_north_south", "horizontal_east_west", "vertical_north_south", "vertical_east_west"};
        String[] colours = {"steel", "red", "white", "yellow"};
        for (int o = 0; o < 4; o++) {
            for (int v = 0; v < 4; v++) {
                put(c, placed, (o * 4 + v) * 2, 0, "geontm:steel_scaffold[orient=" + orients[o] + ",variant=" + colours[v] + "]");
            }
        }
        rows.add("scaffold");
        String[] steel = {"steel_wall", "steel_corner", "steel_roof"};
        for (int s = 0; s < 3; s++) {
            for (int f = 0; f < 4; f++) put(c, placed, (s * 4 + f) * 2, 4, "geontm:" + steel[s] + "[facing=" + H[f] + "]");
        }
        rows.add("steel");
        String[] furniture = {"steel_poles", "tape_recorder", "deco_toaster_iron",
                "deco_toaster_steel", "deco_toaster_wood", "deco_computer_ibm_300pl"};
        for (int k = 0; k < furniture.length; k++) {
            for (int f = 0; f < 4; f++) put(c, placed, k * 5 + f, 8, "geontm:" + furniture[k] + "[facing=" + H[f] + "]");
        }
        put(c, placed, 36, 8, "geontm:pole_top");
        rows.add("furniture");
        String[] lights = {"spotlight_incandescent", "spotlight_incandescent_off", "spotlight_fluoro", "spotlight_fluoro_off",
                "spotlight_halogen", "spotlight_halogen_off"};
        for (int k = 0; k < lights.length; k++) put(c, placed, k * 2, 12, "geontm:" + lights[k] + "[facing=up]");
        for (int k = 0; k < 3; k++) put(c, placed, 14 + k, 12, "geontm:spotlight_fluoro[facing=up]");
        c.cmd("fill 20 " + D + " 11 30 " + (D + 2) + " 11 minecraft:stone");
        for (int k = 0; k < lights.length; k++) put(c, placed, 20 + k * 2, 12, "geontm:" + lights[k] + "[facing=south]");
        rows.add("lights");
        for (int k = 0; k < WIRES.length; k++) {
            put(c, placed, k * 3, 16, "geontm:" + WIRES[k] + "[facing=north]");
            put(c, placed, k * 3 + 1, 16, "geontm:" + WIRES[k] + "[facing=east]");
        }
        put(c, placed, 19, 16, "geontm:spikes");
        rows.add("security");
        for (int k = 0; k < BARRELS.length; k++) put(c, placed, k * 2, 20, "geontm:" + BARRELS[k]);
        put(c, placed, 13, 20, "geontm:crate_can");
        put(c, placed, 15, 20, "geontm:hev_battery");
        rows.add("barrels");
        for (int k = 0; k < ANVILS.length; k++) put(c, placed, k * 2, 24, "geontm:anvil_" + ANVILS[k] + "[facing=north]");
        for (int f = 0; f < 4; f++) put(c, placed, 26 + f * 2, 24, "geontm:anvil_iron[facing=" + H[f] + "]");
        rows.add("anvils");
        int[][] cord = {{0, 28}, {1, 28}, {2, 28}, {1, 29}, {6, 28}, {10, 28}};
        for (int[] cell : cord) {
            Fixtures.placeOnTop(c, "geontm:det_cord", cell[0], Y, cell[1]);
            placed.add(new int[]{cell[0], D, cell[1]});
        }
        Fixtures.placeOnTop(c, "geontm:det_cord", 6, D, 28);
        placed.add(new int[]{6, D + 1, 28});
        rows.add("detcord");
        c.cmd("clear");
        c.waitTicks(40);

        for (int r = 0; r < rows.size(); r++) {
            int z = r * 4;
            c.teleport(12.5, D + 2.5, z - 6.5);
            c.lookAt(12.5, D, z + 0.5);
            c.waitTicks(15);
            c.screenshot(String.format("%02d-%s", 10 + r, rows.get(r)));
        }
        c.teleport(36.5, D + 4, 36.5);
        c.lookAt(14.5, D, 14);
        c.waitTicks(15);
        c.screenshot("30-overview");

        List<String> withEntity = new ArrayList<>();
        for (int[] p : placed) {
            JsonObject block = c.block(p[0], p[1], p[2]);
            JsonElement be = block.get("block_entity");
            if (be != null && !be.isJsonNull()) withEntity.add(block.get("block") + "@" + p[0] + "," + p[1] + "," + p[2]);
        }
        Expect.that(withEntity.isEmpty(), "blocks still carrying a block entity: " + withEntity);
        Expect.logCleanSinceMark(c, "/(ERROR|FATAL)[]]");
    }
}
