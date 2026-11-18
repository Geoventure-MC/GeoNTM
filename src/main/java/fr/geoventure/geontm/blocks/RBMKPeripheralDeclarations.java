package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.lib.ItemIds;
import fr.geoventure.geontm.lib.TagIds;

import static fr.geoventure.geocore.dejson.Crafting.rows;
import static fr.geoventure.geocore.dejson.Crafting.shaped;

/**
 * The 1.7 crafting recipes of the RBMK console peripheral family (CraftingManager 770-782, the gauge and terminal
 * included). The peripherals' own models, blockstates, item definitions and loot are stated on their registrations
 * in {@code ModRBMKBlocks} (Reg); recipes are not part of that migration. Touches no Minecraft class.
 */
public final class RBMKPeripheralDeclarations {

    private static final String NS = "geontm";
    private static boolean declared;

    private RBMKPeripheralDeclarations() {}

    public static void declare() {
        if (declared) return;
        declared = true;

        // --- 1.7 CraftingManager 770, 774-782 ---
        shaped(NS, "rbmk_loader", "geontm:rbmk_loader", rows("SCS", "CBC", "SCS"),
                "S", TagIds.plates_steel, "C", TagIds.ingots_copper, "B", ItemIds.tank_steel);
        shaped(NS, "rbmk_display_blank", "geontm:rbmk_display_blank", 8, rows("B", "D"),
                "B", TagIds.ingots_boron, "D", ItemIds.concrete_asbestos);
        shaped(NS, "rbmk_display", "geontm:rbmk_display", rows("C", "B"),
                "C", ItemIds.crt_display, "B", "geontm:rbmk_display_blank");
        shaped(NS, "rbmk_key_pad", "geontm:rbmk_key_pad", rows("R", "C", "B"),
                "R", ItemIds.radio_torch_sender, "B", "geontm:rbmk_display_blank", "C", ItemIds.circuit_vacuum_tube);
        shaped(NS, "rbmk_gauge", "geontm:rbmk_gauge", rows("R", "C", "B"),
                "R", ItemIds.radio_torch_receiver, "B", "geontm:rbmk_display_blank", "C", ItemIds.circuit_vacuum_tube);
        shaped(NS, "rbmk_numitron", "geontm:rbmk_numitron", rows(" R ", "CCC", " B "),
                "R", ItemIds.radio_torch_receiver, "B", "geontm:rbmk_display_blank", "C", ItemIds.circuit_numitron);
        shaped(NS, "rbmk_graph", "geontm:rbmk_graph", rows("R", "C", "B"),
                "R", ItemIds.radio_torch_receiver, "B", "geontm:rbmk_display_blank", "C", ItemIds.crt_display);
        shaped(NS, "rbmk_lever", "geontm:rbmk_lever", rows("R", "C", "B"),
                "R", ItemIds.radio_torch_sender, "B", "geontm:rbmk_display_blank", "C", TagIds.ingots_copper);
        shaped(NS, "rbmk_indicator", "geontm:rbmk_indicator", rows("R", "C", "B"),
                "R", ItemIds.radio_torch_receiver, "B", "geontm:rbmk_display_blank", "C", ItemIds.coil_tungsten);
        shaped(NS, "rbmk_terminal", "geontm:rbmk_terminal", rows("R ", "CD", "B "),
                "R", ItemIds.radio_torch_sender, "B", "geontm:rbmk_display_blank", "C", ItemIds.circuit_analog, "D", ItemIds.crt_display);
    }
}
