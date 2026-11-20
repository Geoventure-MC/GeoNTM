package fr.geoventure.geontm.gametest;

import fr.geoventure.geontm.blocks.ModBlocks;
import fr.geoventure.geontm.blocks.ModCustomMissileBlocks;
import fr.geoventure.geontm.entity.missile.EntityMissileCustom;
import fr.geoventure.geontm.handler.MissileStruct;
import fr.geoventure.geontm.items.ModCustomMissileItems;
import fr.geoventure.geontm.items.ModDataComponents;
import fr.geoventure.geontm.items.ModFuelItems;
import fr.geoventure.geontm.items.ModMissileItems;
import fr.geoventure.geontm.items.weapon.ItemCustomMissile;
import fr.geoventure.geontm.items.weapon.ItemCustomMissilePart;
import fr.geoventure.geontm.tileentity.bomb.TileEntityCompactLauncher;
import fr.geoventure.geontm.tileentity.bomb.TileEntityCustomMissileLauncher;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineMissileAssembly;
import fr.geoventure.geontm.tileentity.machine.TileEntityMultiblock;
import fr.geoventure.geocore.gametest.GameTestHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.phys.AABB;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The custom missile system end to end, without a client: 1.7's assembly rules and output, the struct core
 * turning into a compact launcher, and that launcher firing the assembled missile on solid fuel.
 */
public final class CustomMissileGameTests {

    private CustomMissileGameTests() {}

    /** The launch pad structure blocks stay craftable in survival: the server's recipe manager must hold a recipe for each. */
    public static void launcherStructureRecipes(GameTestHelper helper) {
        Set<String> crafted = new HashSet<>();
        for (Recipe<?> recipe : helper.getLevel().getServer().getRecipeManager().getRecipes()) {
            crafted.add(BuiltInRegistries.ITEM.getKey(recipe.getResultItem(helper.getLevel().registryAccess()).getItem()).toString());
        }
        for (String id : List.of("struct_launcher", "struct_scaffold", "struct_launcher_core", "struct_launcher_core_large",
                "struct_soyuz_core")) {
            helper.assertTrue(crafted.contains("geontm:" + id), "no recipe produces geontm:" + id);
        }
        helper.succeed();
    }

    public static void assembleAndLaunch(GameTestHelper helper) {
        // --- the upstream stats of two representative parts ---
        ItemCustomMissilePart fuselage = ModCustomMissileItems.mp_fuselage_15_kerosene_polite.get();
        helper.assertTrue(fuselage.fuelAmount() == 15000F && fuselage.health == 60F
                        && fuselage.rarity == ItemCustomMissilePart.Rarity.LEGENDARY
                        && fuselage.top == ItemCustomMissilePart.PartSize.SIZE_15 && fuselage.mesh != null,
                "mp_fuselage_15_kerosene_polite lost its 1.7 stats");
        helper.assertTrue(ModCustomMissileItems.mp_stability_20_flat.get().health == 0F
                        && ModCustomMissileItems.mp_chip_5.get().mesh == null,
                "mp_s_20 / mp_c_5 stats differ from 1.7");

        // --- assembly: a mismatched fin blocks the build, the right one lets it through ---
        BlockPos rack = new BlockPos(1, 1, 1);
        helper.setBlock(rack, ModCustomMissileBlocks.machine_missile_assembly.get().defaultBlockState());
        TileEntityMachineMissileAssembly assembly = GameTestHelpers.blockEntity(helper, rack, TileEntityMachineMissileAssembly.class);
        helper.assertTrue(assembly != null, "no assembly block entity");
        assembly.inventory.setStackInSlot(0, new ItemStack(ModCustomMissileItems.mp_chip_5.get()));
        assembly.inventory.setStackInSlot(1, new ItemStack(ModCustomMissileItems.mp_warhead_10_he.get()));
        assembly.inventory.setStackInSlot(2, new ItemStack(ModCustomMissileItems.mp_fuselage_10_solid.get()));
        assembly.inventory.setStackInSlot(3, new ItemStack(ModCustomMissileItems.mp_stability_15_flat.get()));
        assembly.inventory.setStackInSlot(4, new ItemStack(ModCustomMissileItems.mp_thruster_10_solid.get()));
        helper.assertTrue(assembly.stabilityState() == 0 && !assembly.canBuild(), "a size-15 fin fitted a size-10 fuselage");
        assembly.inventory.setStackInSlot(3, new ItemStack(ModCustomMissileItems.mp_stability_10_flat.get()));
        helper.assertTrue(assembly.canBuild(), "a valid solid-fuel missile cannot be built");
        assembly.construct();
        ItemStack missile = assembly.inventory.getStackInSlot(5);
        MissileStruct struct = ItemCustomMissile.getStruct(missile);
        helper.assertTrue(missile.is(ModCustomMissileItems.missile_custom.get()) && struct != null && struct.isComplete()
                        && struct.fins() == ModCustomMissileItems.mp_stability_10_flat.get()
                        && ItemCustomMissile.chip(missile) == ModCustomMissileItems.mp_chip_5.get(),
                "the assembled missile does not carry its parts: " + missile);
        for (int i = 0; i < 5; i++) {
            helper.assertTrue(assembly.inventory.getStackInSlot(i).isEmpty(), "part slot " + i + " was not consumed");
        }

        // --- the core component forms a compact launcher from eight launcher plates ---
        BlockPos core = new BlockPos(5, 1, 5);
        helper.setBlock(core, ModCustomMissileBlocks.struct_launcher_core.get().defaultBlockState());
        for (int x = -1; x <= 1; x++)
            for (int z = -1; z <= 1; z++)
                if (x != 0 || z != 0) helper.setBlock(core.offset(x, 0, z), ModBlocks.struct_launcher.get().defaultBlockState());
        TileEntityMultiblock seed = GameTestHelpers.blockEntity(helper, core, TileEntityMultiblock.class);
        helper.assertTrue(seed != null, "no struct core block entity");
        TileEntityMultiblock.serverTick(helper.getLevel(), helper.absolutePos(core), seed.getBlockState(), seed);
        TileEntityCompactLauncher launcher = GameTestHelpers.blockEntity(helper, core, TileEntityCompactLauncher.class);
        helper.assertTrue(launcher != null, "the struct core did not become a compact launcher");

        // --- load and fire: 10 rocket fuel = 2500 solid, the fuselage's whole tank; 75 kHE from the battery ---
        launcher.inventory.setStackInSlot(0, missile.copy());
        ItemStack designator = new ItemStack(ModMissileItems.designator.get());
        ModDataComponents.DESIGNATOR_X.set(designator, helper.absolutePos(core).getX() + 200);
        ModDataComponents.DESIGNATOR_Z.set(designator, helper.absolutePos(core).getZ());
        launcher.inventory.setStackInSlot(1, designator);
        launcher.solid = 2500;
        launcher.power = TileEntityCustomMissileLauncher.maxPower;
        helper.assertTrue(launcher.isMissileValid() && launcher.solidState() == 1 && launcher.canLaunch(),
                "the loaded compact launcher is not ready");
        launcher.launchFromDesignator();
        List<EntityMissileCustom> flying = helper.getLevel().getEntitiesOfClass(EntityMissileCustom.class,
                new AABB(helper.absolutePos(core)).inflate(16));
        helper.assertTrue(flying.size() == 1, "the compact launcher did not launch a custom missile");
        helper.assertTrue(flying.get(0).getStruct().fuselage() == ModCustomMissileItems.mp_fuselage_10_solid.get()
                        && flying.get(0).fuel == 2500F,
                "the flying missile lost its parts or fuel");
        helper.assertTrue(launcher.solid == 0 && launcher.power == TileEntityCustomMissileLauncher.maxPower / 4
                        && launcher.inventory.getStackInSlot(0).isEmpty(),
                "the launch did not charge fuel, power and the missile");
        flying.forEach(Entity::discard);
        helper.assertTrue(ModFuelItems.rocket_fuel.get() != null, "rocket fuel");
        helper.succeed();
    }
}
