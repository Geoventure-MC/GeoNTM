package fr.geoventure.geontm.blocks;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.geoventure.geontm.items.MachineObjModel;
import fr.geoventure.geontm.lib.ItemIds;
import fr.geoventure.geontm.lib.TagIds;
import fr.geoventure.geontm.lib.VanillaIds;
import fr.geoventure.geocore.dejson.Dejson;

import static fr.geoventure.geocore.dejson.Crafting.rows;
import static fr.geoventure.geocore.dejson.Crafting.shaped;
import static fr.geoventure.geocore.dejson.Crafting.shapeless;

/**
 * The satellite family's typed OBJ item data (the cargo landing pad and both launch pads, the 1.7
 * {@code ItemRenderLibrary} and {@code IItemRendererProvider} entries) and the 1.7 crafting-table recipes; the blocks' models, blockstates and loot and the chip sprites are
 * stated on their registrations ({@code ModSatelliteBlocks}, {@code ModSatelliteItems}). Recipes
 * ({@code CraftingManager} machine_satlinker / sat_coord, {@code ToolRecipes} sat_designator /
 * sat_relay). Touches no Minecraft class: it runs in a plain JVM from {@code dejson.gradle.kts}.
 */
public final class SatelliteDeclarations {

    private static final String NS = "geontm";
    private static boolean declared;

    private SatelliteDeclarations() {}

    public static void declare() {
        if (declared) return;
        declared = true;

        items();
        recipes();
    }

    private static void items() {
        // ItemRenderLibrary sat_dock: inventory S(3); common Ry(90 about -Y), renderAll
        {
            JsonObject model = new JsonObject();
            model.addProperty("type", "geontm:machine_obj");
            model.addProperty("obj", "geontm:models/machines/cargo_pad.obj");
            model.addProperty("texture", "geontm:textures/models/machines/cargo_pad.png");
            model.addProperty("scale", 1.0);
            model.addProperty("yaw", -90.0);
            model.addProperty("rawLegacyTransform", true);
            model.add("guiTranslate", numbers(0, 0, 0));
            model.addProperty("guiScale", 3.0);
            Dejson.item(NS, "sat_dock").shares("geontm:item/machine_obj_base").register();
            Dejson.itemDefinition("geontm:item/sat_dock").add("model", model);
            Dejson.itemDefinition("geontm:item/sat_dock").addProperty("oversized_in_gui", true);
        }
        // RenderLaunchpadSoyuz item: inventory T(6,-1.5,0) S(1.125) Ry(90); common S(0.25), the pad, then the struts at z 3.25
        {
            String texture = "geontm:textures/models/machines/launchpad_soyuz.png";
            Dejson.item(NS, "launchpad_soyuz")
                    .definition(MachineObjModel.of("geontm:models/machines/launchpad_soyuz.obj", texture)
                            .scale(0.25).ty(0.0).yaw(0.0).guiScale(1.125).guiYaw(90.0).rawLegacyTransform()
                            .guiTranslate(6.0, -1.5, 0.0).parts("Launchpad")
                            .passes(MachineObjModel.pass(texture).parts("Strut1", "Strut2", "Strut3", "Strut4", "Strut5")
                                    .translate(0.0, 0.0, 3.25)))
                    .oversizedInGui().register();
        }
        // RenderLaunchpadLambda item: inventory T(0,-3.5,0) S(1.75); common S(0.25), silo and doors, then T(0,2,0) the erector
        {
            String texture = "geontm:textures/models/machines/launchpad_lambda.png";
            Dejson.item(NS, "launchpad_lambda")
                    .definition(MachineObjModel.of("geontm:models/machines/launchpad_lambda.obj", texture)
                            .scale(0.25).ty(0.0).yaw(0.0).guiScale(1.75).rawLegacyTransform()
                            .guiTranslate(0.0, -3.5, 0.0).parts("Silo", "DoorLeft", "DoorRight")
                            .passes(MachineObjModel.pass(texture).parts("Erector", "Rotor", "PivotLower1", "ClampLower1",
                                    "PivotLower2", "ClampLower2", "PivotUpper1", "ClampUpper1", "PivotUpper2", "ClampUpper2")
                                    .translate(0.0, 2.0, 0.0)))
                    .oversizedInGui().register();
        }
    }

    private static void recipes() {
        // 1.7 CraftingManager:605
        shaped(NS, "machine_satlinker", "geontm:machine_satlinker", rows("PSP", "SCS", "PSP"),
                "P", TagIds.ingots_any_resistant_alloy, "S", TagIds.ingots_any_plastic, "C", "geontm:sat_chip");
        // 1.7 CraftingManager:608
        shaped(NS, "sat_coord", "geontm:sat_coord", rows("SII", "SCA", "SPP"),
                "I", TagIds.ingots_steel, "S", TagIds.ingots_starmetal, "P", ItemIds.plate_polymer,
                "C", "geontm:sat_chip", "A", ItemIds.circuit_advanced);
        // 1.7 ToolRecipes:122-123
        shaped(NS, "sat_designator", "geontm:sat_designator", rows("RRD", "PIC", "  P"),
                "P", TagIds.plates_gold, "R", VanillaIds.redstone, "C", ItemIds.circuit_advanced,
                "D", "geontm:sat_chip", "I", TagIds.ingots_gold);
        shapeless(NS, "sat_relay", "geontm:sat_relay", "geontm:sat_chip", ItemIds.ducttape, ItemIds.radar_linker);
    }

    private static JsonArray numbers(double... values) {
        JsonArray array = new JsonArray();
        for (double value : values) array.add(value);
        return array;
    }
}
