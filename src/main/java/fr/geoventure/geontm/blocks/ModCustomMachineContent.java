package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.config.CustomMachineConfigJSON;
import fr.geoventure.geontm.inventory.ModCustomMachineMenus;
import fr.geoventure.geontm.inventory.recipes.CustomMachineCraftingRecipe;
import fr.geoventure.geontm.items.ModCustomMachineItems;
import fr.geoventure.geontm.tileentity.ModCustomMachineBlockEntities;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import fr.geoventure.geontm.registration.Reg;

/**
 * One bootstrap hook for the custom machine system: the controller and its item per configured machine, the
 * casing, sheet, engine, tank, circuit and port blocks, the flux and heat receivers, the positioning anchor, the
 * structure output wand, the controller's crafting recipe, and {@code geontmConfig/geontmCustomMachines.json}. 1.7 read
 * that file in {@code PostLoad}; it is read here so the client's creative tab and item names see the same list the
 * server does, and the blocks and ingredients it names resolve on first use.
 */
public final class ModCustomMachineContent {

    private ModCustomMachineContent() {}

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Constants.MOD_ID);
    private static final Reg REG = Reg.of(SERIALIZERS);
    public static final RegistryObject<RecipeSerializer<CustomMachineCraftingRecipe>> CUSTOM_MACHINE_RECIPE =
            REG.register(Registries.RECIPE_SERIALIZER, "custom_machine", () -> CustomMachineCraftingRecipe.SERIALIZER);

    public static void init() {
        ModCustomMachineBlocks.init();
        ModCustomMachineItems.init();
        ModCustomMachineBlockEntities.init();
        ModCustomMachineMenus.init();
        ModCustomMachineBlocks.declare();
        CustomMachineConfigJSON.initialize();
    }
}
