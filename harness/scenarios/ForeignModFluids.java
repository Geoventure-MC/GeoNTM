import com.google.gson.JsonObject;
import fr.geoventure.harness.api.Client;
import fr.geoventure.harness.api.Expect;
import fr.geoventure.harness.api.Fixtures;
import fr.geoventure.harness.api.Scenario;

/**
 * GEONTM fluids with other mods' real pipes and tanks: Pipez fluid pipes (NeoForge and Forge dev runs) and, where the run
 * has Create ({@code -Psable.run=true} on 1.21.1 NeoForge), Create fluid tanks.
 * <ol>
 *   <li>Pipez between two GEONTM barrels: diesel leaves the output barrel and arrives in the input barrel, none lost;</li>
 *   <li>with Create: GEONTM barrel, pipe, Create tank, pipe, GEONTM barrel; the Create tank holds {@code geontm:diesel} on the
 *       way and the 5000 mB are conserved over the three containers;</li>
 *   <li>with Create: honey, a Create fluid, poured into a Create tank by hand, piped into an GEONTM barrel typed
 *       {@code create:honey};</li>
 *   <li>with Create: GEONTM ducts, placed by hand and typed with the identifier, carry diesel from a barrel into a Create
 *       tank and draw their arm to it.</li>
 * </ol>
 */
public final class ForeignModFluids implements Scenario {

    private static final int Y = 100, G = Y + 1;

    @Override
    public void run(Client c) throws Exception {
        c.useFlatWorld();
        c.waitInWorld(120_000L);
        Fixtures.platform(c, 0, Y, 0, 12);
        c.cmd("gamemode creative");
        c.cmd("gamerule sendCommandFeedback false");
        c.cmd("fill -10 " + G + " -10 10 " + (G + 4) + " 10 minecraft:air");
        c.waitTicks(5);

        // 1: Pipez alone, z = -4
        barrel(c, -2, -4, 5000, 2);
        pipe(c, -1, -4, "west");
        barrel(c, 0, -4, 0, 0);

        Fixtures.setBlock(c, -2, G, 0, "create:fluid_tank");
        boolean create = c.block(-2, G, 0).get("block").getAsString().equals("create:fluid_tank");
        System.out.println("[ForeignModFluids] Create in this run: " + create);
        if (create) {
            // 2: barrel, pipe, Create tank, pipe, barrel, z = 0
            barrel(c, -4, 0, 5000, 2);
            pipe(c, -3, 0, "west");
            barrel(c, 0, 0, 0, 0);
            // 3: honey poured into a Create tank, piped into an GEONTM barrel, z = 4
            Fixtures.setBlock(c, -2, G, 4, "create:fluid_tank");
            Fixtures.giveOnly(c, "create:honey_bucket", 1);
            c.teleport(-1.5, G, 6.5);
            c.waitTicks(5);
            c.lookAt(-1.5, G + 0.5, 4.5);
            c.use(2);
            c.waitTicks(5);
            c.cmd("setblock 0 " + G + " 4 geontm:barrel_steel{tank:0,tank_type:\"create:honey\",mode:0s}");
            pipe(c, -1, 4, "west");
            // 4: barrel, two GEONTM ducts, Create tank, z = 8
            barrel(c, -4, 8, 5000, 2);
            Fixtures.setBlock(c, -1, G, 8, "create:fluid_tank");
            for (int x = -3; x <= -2; x++) {
                Fixtures.giveOnly(c, "geontm:fluid_duct_neo", 1);
                aim(c, x, 8, Y + 0.98);
                c.use(1);
                c.waitTicks(4);
            }
            Fixtures.giveOnly(c, identifier(c, "geontm:diesel"), 1);
            for (int x = -3; x <= -2; x++) {
                aim(c, x, 8, G + 0.5);
                c.use(1);
                c.waitTicks(3);
            }
        }
        if (create) {
            // the Create tank filled before its outlet exists, read through Create's own handler
            c.waitTicks(40);
            JsonObject filling = find(c, -2, 0, "east");
            int held = amount(c, -2, 0, "geontm:diesel");
            System.out.println("[ForeignModFluids] Create tank while filling: " + filling);
            Expect.that(held > 0 && filling.toString().contains("\"geontm\":\"geontm:diesel\""),
                    "the Create tank holds GEONTM diesel: " + filling);
            pipe(c, -1, 0, "west");
        }
        c.teleport(-1.5, G + 3, -8.5);
        c.lookAt(-1.5, G, 1.5);
        c.waitTicks(100);
        c.screenshot("01-rows");

        int out1 = amount(c, -2, -4, "geontm:diesel"), in1 = amount(c, 0, -4, "geontm:diesel");
        System.out.println("[ForeignModFluids] Pipez: source " + out1 + " mB, sink " + in1 + " mB");
        Expect.that(in1 > 0 && out1 + in1 == 5000, "Pipez moved diesel between GEONTM barrels without loss: " + out1 + " + " + in1);

        if (create) {
            int src = amount(c, -4, 0, "geontm:diesel"), tank = amount(c, -2, 0, "geontm:diesel"), sink = amount(c, 0, 0, "geontm:diesel");
            System.out.println("[ForeignModFluids] Create chain: source " + src + ", Create tank " + tank + ", sink " + sink);
            Expect.that(src < 5000 && sink > 0 && src + tank + sink == 5000,
                    "diesel went GEONTM barrel, Create tank, GEONTM barrel with 5000 mB conserved: " + src + "/" + tank + "/" + sink);

            int honey = amount(c, 0, 4, "create:honey"), honeyLeft = amount(c, -2, 4, "create:honey");
            System.out.println("[ForeignModFluids] honey: Create tank " + honeyLeft + ", GEONTM barrel " + honey);
            Expect.that(honey > 0 && honey + honeyLeft == 1000, "Create honey reached the GEONTM barrel: " + honeyLeft + " + " + honey);
            int ductSource = amount(c, -4, 8, "geontm:diesel"), ductTank = amount(c, -1, 8, "geontm:diesel");
            System.out.println("[ForeignModFluids] ducts: source " + ductSource + ", Create tank " + ductTank
                    + ", last duct " + c.block(-2, G, 8).get("state"));
            Expect.that(ductTank > 0 && ductSource + ductTank == 5000,
                    "GEONTM ducts filled the Create tank with 5000 mB conserved: " + ductSource + " + " + ductTank);
            Expect.that(c.block(-2, G, 8).toString().contains("\"east\":\"true\""),
                    "the duct draws its arm to the Create tank: " + c.block(-2, G, 8));

            String saved = c.blockEntity(0, G, 4).toString().replaceAll("\\s+", "");
            Expect.that(saved.contains("create:honey"), "the GEONTM barrel saves the Create fluid by id: " + saved);
        }
    }

    private static void aim(Client c, int x, int z, double y) {
        c.teleport(x + 0.5, G, z + 2.5);
        c.waitTicks(3);
        c.lookAt(x + 0.5, y, z + 0.5);
        c.waitTicks(1);
    }

    private static String identifier(Client c, String fluid) {
        return c.node().startsWith("1.20.") ? "geontm:fluid_identifier{\"geontm:fluid\":\"" + fluid + "\"}"
                : "geontm:fluid_identifier[geontm:fluid=\"" + fluid + "\"]";
    }

    /** An GEONTM steel barrel typed diesel; mode 0 takes, 2 gives. */
    private static void barrel(Client c, int x, int z, int mb, int mode) {
        c.cmd("setblock " + x + " " + G + " " + z + " geontm:barrel_steel{tank:" + mb + ",tank_type:\"geontm:diesel\",mode:" + mode + "s}");
        c.waitTicks(1);
    }

    /** A Pipez fluid pipe extracting from its {@code side}; Pipez keeps a block entity only with {@code has_data}. */
    private static void pipe(Client c, int x, int z, String side) {
        String[] order = {"down", "up", "north", "south", "west", "east"};
        StringBuilder flags = new StringBuilder();
        for (String s : order) flags.append(flags.length() == 0 ? "" : ",").append(s.equals(side) ? "1b" : "0b");
        c.cmd("setblock " + x + " " + G + " " + z + " pipez:fluid_pipe[has_data=true]{ExtractingSides:[" + flags + "]}");
        c.waitTicks(1);
    }

    /** mB of {@code fluid} the block's loader fluid handler reports, over every tank and face. */
    private static int amount(Client c, int x, int z, String fluid) {
        int best = 0;
        for (String side : new String[] {"up", "north", "south", "west", "east", "down"}) {
            JsonObject found = find(c, x, z, side);
            if (!found.has("tanks")) continue;
            int sum = 0;
            for (var e : found.getAsJsonArray("tanks")) {
                JsonObject tank = e.getAsJsonObject();
                if (tank.has("fluid") && tank.get("fluid").getAsString().equals(fluid)) sum += tank.get("amount").getAsInt();
            }
            best = Math.max(best, sum);
        }
        return best;
    }

    private static JsonObject find(Client c, int x, int z, String side) {
        JsonObject p = new JsonObject();
        p.addProperty("op", "find");
        p.addProperty("x", x);
        p.addProperty("y", G);
        p.addProperty("z", z);
        p.addProperty("side", side);
        return c.query("geontm_fluidcaps", p);
    }
}
