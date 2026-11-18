import fr.geoventure.harness.api.Client;
import fr.geoventure.harness.api.Expect;
import fr.geoventure.harness.api.Fixtures;
import fr.geoventure.harness.api.Scenario;

/**
 * Visual acceptance for the 2026-09-28 JSON-to-Dejson migration (§19ed-dejson): every asset shown here used
 * to be an authored JSON file and is now emitted by a declaration.
 *
 * <p>Three inventory sheets cover the migrated item-definition shapes — {@code geontm:machine_obj} OBJ icons
 * (direct and inside {@code minecraft:special}), RBMK column icons, Sedna guns ({@code geontm:gun}),
 * {@code select} families, the tinted flag item and plain sprites whose item models moved with them. A row of
 * hand-placed blocks covers the migrated blockstates and hand-built element models (multipart ducts, cables,
 * fence, pipe anchor, glass pane, conveyor chute, foundry channel/tank, snow-style layers, lit furnaces,
 * weapon table, RBMK gauge). The client log is checked for model and texture load errors.
 */
public final class DejsonMigration implements Scenario {

    private static final int X = 384;
    private static final int Y = 100;
    private static final int Z = 384;

    private static final String[][] SHEETS = {
            { // OBJ machine and RBMK icons
                    "geontm:machine_assembly_machine", "geontm:machine_chemical_plant", "geontm:machine_press",
                    "geontm:machine_cyclotron", "geontm:machine_fluidtank", "geontm:machine_stirling", "geontm:machine_sawmill",
                    "geontm:machine_turbofan", "geontm:machine_mining_laser", "geontm:machine_large_turbine",
                    "geontm:machine_industrial_generator", "geontm:machine_satlink", "geontm:fusion_plasma_forge",
                    "geontm:turret_chekhov", "geontm:turret_howard", "geontm:turret_himars", "geontm:turret_arty",
                    "geontm:missile_nuclear", "geontm:missile_anti_ballistic", "geontm:nuke_boy", "geontm:nuke_man",
                    "geontm:blast_door", "geontm:vault_door", "geontm:silo_hatch", "geontm:cargo_elevator", "geontm:crane_splitter",
                    "geontm:crane_partitioner", "geontm:anvil_steel", "geontm:battery_lead", "geontm:barrel_antimatter",
                    "geontm:rbmk_absorber", "geontm:rbmk_control", "geontm:rbmk_fuel_rod", "geontm:rbmk_boiler",
                    "geontm:rbmk_heater", "geontm:rbmk_gauge"},
            { // guns, armour OBJ icons, glider, gear, scaffold, HIMARS ammo
                    "geontm:gun_aberrator", "geontm:gun_am180", "geontm:gun_b92", "geontm:gun_bolter", "geontm:gun_carbine",
                    "geontm:gun_congolake", "geontm:gun_fatman", "geontm:gun_flamer", "geontm:gun_g3", "geontm:gun_greasegun",
                    "geontm:gun_heavy_revolver", "geontm:gun_henry", "geontm:gun_lag", "geontm:gun_light_revolver",
                    "geontm:gun_maresleg", "geontm:gun_minigun", "geontm:gun_panzerschreck", "geontm:gun_spas12",
                    "geontm:gun_stg77", "geontm:gun_tau", "geontm:gun_uzi", "geontm:gun_coilgun", "geontm:gun_pepperbox",
                    "geontm:gun_amat", "geontm:bj_helmet", "geontm:bj_plate", "geontm:bj_plate_jetpack", "geontm:bj_legs",
                    "geontm:bj_boots", "geontm:dns_helmet", "geontm:t51_plate", "geontm:t51_boots", "geontm:jetpack_glider",
                    "geontm:gear_large", "geontm:steel_scaffold", "geontm:ammo_himars_standard"},
            { // select families, flag tint, sprites and block items of the migrated blockstates
                    "geontm:blueprints", "geontm:blueprint_folder", "geontm:ammo_container", "geontm:concrete_red",
                    "geontm:concrete_ext_hazard", "geontm:weapon_mod_generic_iron_damage", "geontm:weapon_mod_special_silencer",
                    "geontm:weapon_mod_caliber_p9", "geontm:scraps", "geontm:lightstone_unrefined", "geontm:ammo_bag", "geontm:casing_bag",
                    "geontm:ammo_standard_g12", "geontm:ammo_standard_p45_fmj", "geontm:ammo_standard_nuke_standard",
                    "geontm:key", "geontm:key_fake", "geontm:gun_pa_ranged", "geontm:fluid_duct_neo", "geontm:fluid_duct_gauge",
                    "geontm:fluid_valve", "geontm:foam_layer", "geontm:foundry_channel", "geontm:foundry_tank",
                    "geontm:foundry_mold", "geontm:pipe_anchor", "geontm:pneumatic_tube", "geontm:red_cable_classic",
                    "geontm:red_cable_paintable", "geontm:sand_boron_layer", "geontm:machine_weapon_table",
                    "geontm:machine_difurnace_off", "geontm:machine_electric_furnace_off", "geontm:machine_furnace_brick_off",
                    "geontm:fence_metal", "geontm:reinforced_glass_pane"},
    };

    /** Hand-placed in pairs so connection-driven multipart states actually vary. */
    private static final String[] PLACED = {
            "geontm:fluid_duct_neo", "geontm:red_cable", "geontm:fence_metal", "geontm:reinforced_glass_pane",
            "geontm:pneumatic_tube", "geontm:conveyor_chute", "geontm:foundry_channel", "geontm:foundry_tank",
            "geontm:pipe_anchor", "geontm:foam_layer", "geontm:sand_boron_layer", "geontm:machine_difurnace_off",
            "geontm:machine_electric_furnace_off", "geontm:machine_weapon_table", "geontm:rbmk_gauge", "geontm:wood_barrier",
    };

    @Override
    public void run(Client c) throws Exception {
        c.waitInWorld(120_000L);
        c.markLog();
        Fixtures.platform(c, X, Y, Z, 24);
        c.cmd("effect give @s minecraft:resistance 900 4 true");

        for (int i = 0; i < SHEETS.length; i++) inventorySheet(c, i, SHEETS[i]);

        c.cmd("gamemode creative");
        c.waitTicks(2);
        for (int i = 0; i < PLACED.length; i++) {
            int x = X - 15 + (i % 8) * 4;
            int z = Z - 4 + (i / 8) * 5;
            String id = PLACED[i];
            Fixtures.placeOnTop(c, id, x, Y, z);
            Fixtures.placeOnTop(c, id, x + 1, Y, z);
            c.waitTicks(2);
            Expect.block(c, x, Y + 1, z, id);
        }
        shot(c, "20-placed-blocks", X + 0.5, Y + 9, Z - 14.5, X + 0.5, Y + 1.0, Z + 1.0);
        shot(c, "21-placed-blocks-close-left", X - 8.5, Y + 4, Z - 8.5, X - 8.5, Y + 1.0, Z);
        shot(c, "22-placed-blocks-close-right", X + 8.5, Y + 4, Z - 8.5, X + 8.5, Y + 1.0, Z);

        for (String error : c.logSinceMark(
                "(Unable to load model|Missing model|missing texture|Failed to load texture|Unable to resolve texture"
                        + "|Couldn't parse|Exception loading blockstate|Unable to load definition|ERROR)")) {
            boolean offlineRealms = error.contains("[RealmsAvailability]");
            Expect.that(offlineRealms, "unexpected client error: " + error);
        }
    }

    private static void inventorySheet(Client c, int index, String[] items) {
        c.cmd("gamemode creative");
        c.teleport(X + 0.5, Y + 1, Z + 0.5);
        c.waitTicks(8);
        c.cmd("gamemode survival");
        c.waitTicks(2);
        c.cmd("clear");
        for (String id : items) c.cmd("give @s " + id + " 1");
        c.waitTicks(4);
        for (String id : items) Expect.inventoryHolds(c, id, 1);
        c.key("e");
        c.waitScreen("Inventory", 5_000L);
        Expect.screenOpen(c, "Inventory");
        c.waitTicks(10);
        c.screenshot("1" + index + "-inventory-sheet-" + index);
        c.key("escape");
        c.waitTicks(3);
    }

    private static void shot(Client c, String name, double x, double y, double z, double tx, double ty, double tz) {
        c.hotbar(9);
        c.cmd("gamemode spectator");
        c.waitTicks(2);
        c.teleport(x, y, z);
        c.waitTicks(15);
        c.lookAt(tx, ty, tz);
        c.waitTicks(12);
        c.screenshot(name);
        c.cmd("gamemode creative");
        c.waitTicks(2);
    }
}
