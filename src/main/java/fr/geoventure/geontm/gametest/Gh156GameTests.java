package fr.geoventure.geontm.gametest;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.blocks.ModFluidNetBlocks;
import fr.geoventure.geontm.blocks.fluid.ModWorldFluids;
import fr.geoventure.geontm.blocks.machine.ModMachineBlocks;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geontm.inventory.fluid.ConventionFluidTags;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.inventory.recipes.ChemicalPlantRecipes;
import fr.geoventure.geontm.items.ModDataComponents;
import fr.geoventure.geontm.items.ModFluidItems;
import fr.geoventure.geontm.items.ModFluidNetItems;
import fr.geoventure.geontm.items.machine.ItemFluidBucket;
import fr.geoventure.geontm.items.machine.ItemFluidIDMulti;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineChemicalPlant;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineDiesel;
import fr.geoventure.geontm.tileentity.machine.storage.TileEntityBarrel;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.fluid.FluidCompat;
import fr.geoventure.geocore.fluid.FluidView;
import fr.geoventure.geocore.gametest.GameTests;
import fr.geoventure.geocore.platform.Platform;
import fr.geoventure.geocore.world.Ingredients;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;

/** GitHub issue 156 and the Immersive Engineering report: fluids listed once, and {@code c:} fluid tags both ways. */
public final class Gh156GameTests {

    private Gh156GameTests() {}

    public static final GameTests.Suite TESTS = GameTests.suite(Constants.MOD_ID);
    public static final GameTests.Function LISTS =
            TESTS.function("gh156_fluid_lists_name_each_fluid_once", 20, () -> Gh156GameTests::lists);
    public static final GameTests.Function BUCKETS =
            TESTS.function("gh156_buckets_fill_and_empty_as_17", 60, 8, () -> Gh156GameTests::buckets);
    public static final GameTests.Function TAGS =
            TESTS.function("gh156_geontm_fluids_join_convention_tags", 20, () -> Gh156GameTests::tags);
    public static final GameTests.Function MEKANISM =
            TESTS.function("gh156_mekanism_fluids_run_geontm_machines", 200, 16, () -> Gh156GameTests::mekanism);
    public static final GameTests.Function IMMERSIVE_ENGINEERING =
            TESTS.function("gh156_immersive_engineering_fluids_are_geontm_fluids", 200, 12, () -> Gh156GameTests::immersiveEngineering);
    public static final GameTests.Function IMMERSIVE_PETROLEUM =
            TESTS.function("gh156_immersive_petroleum_fluids_are_geontm_fluids", 200, 12, () -> Gh156GameTests::immersivePetroleum);

    public static void init() {}

    /** Recipe-viewer and creative lists hold HBM's fluids once; the picker adds only foreign fluids of their own kind. */
    private static void lists(GameTestHelper helper) {
        Fluids.resolveDeclaredResources();
        FluidType sulfuricPlaced = Fluids.fromVanillaExact(ModWorldFluids.SULFURIC_ACID.source());
        helper.assertTrue(sulfuricPlaced.isForeign() && Fluids.kindOf(sulfuricPlaced) == Fluids.SULFURIC_ACID,
                "placed sulfuric acid is " + Fluids.kindOf(sulfuricPlaced).getRegistryName() + " in tanks");
        helper.assertTrue(Fluids.kindOf(Fluids.fromVanillaExact(ModWorldFluids.MUD.source())) == Fluids.WATZ
                        && Fluids.kindOf(Fluids.fromVanillaExact(ModWorldFluids.SCHRABIDIC.source())) == Fluids.SCHRABIDIC,
                "placed mud and schrabidic acid are not WATZ and SCHRABIDIC in tanks");

        Set<FluidType> seen = Collections.newSetFromMap(new IdentityHashMap<>());
        for (FluidType fluid : Fluids.getInNiceOrder()) {
            helper.assertTrue(!fluid.isForeign(), fluid.getRegistryName() + " is listed as a fluid of its own");
            helper.assertTrue(seen.add(fluid), fluid.getRegistryName() + " is listed twice");
        }
        seen.clear();
        FluidType[] nice = Fluids.getInNiceOrder();
        FluidType[] selectable = Fluids.getSelectable();
        for (int i = 0; i < selectable.length; i++) {
            FluidType fluid = selectable[i];
            helper.assertTrue(i >= nice.length || fluid == nice[i], "the picker does not start with HBM's fluids");
            helper.assertTrue(seen.add(fluid) && Fluids.kindOf(fluid) == fluid, fluid.getRegistryName() + " is a duplicate in the picker");
            helper.assertTrue(!fluid.isForeign() || !fluid.getNamespace().equals(Constants.MOD_ID),
                    "the picker lists the placed fluid " + fluid.getRegistryName());
        }
        seen.clear();
        for (ItemStack identifier : ItemFluidIDMulti.creativeStacks()) {
            FluidType fluid = ItemFluidIDMulti.getType(identifier, true);
            helper.assertTrue(!fluid.isForeign() && seen.add(fluid), "identifier listed for " + fluid.getRegistryName());
        }

        List<String> wrong = new ArrayList<>();
        for (GeoNtmTab tab : GeoNtmTab.values()) {
            for (ItemStack stack : ModTabs.displayStacks(ModTabs.get(tab).getKey())) {
                FluidType fluid = ModDataComponents.fluid(stack);
                if (stack.is(ModFluidItems.fluid_bucket.get()) || fluid.isForeign()) {
                    wrong.add(BuiltInRegistries.ITEM.getKey(stack.getItem()) + "/" + fluid.getRegistryName());
                }
            }
        }
        helper.assertTrue(wrong.isEmpty(), "creative tabs list " + wrong.size() + " universal buckets or foreign fluids: "
                + wrong.subList(0, Math.min(5, wrong.size())));
        helper.succeed();
    }

    /** 1.7 {@code FluidContainerRegistry}: buckets only for fluids that have one; an old universal bucket still empties. */
    private static void buckets(GameTestHelper helper) {
        TileEntityBarrel ethanol = barrel(helper, new BlockPos(1, 2, 1), Fluids.ETHANOL, 5_000);
        ethanol.inventory.setStackInSlot(4, new ItemStack(Items.BUCKET));
        TileEntityBarrel sulfuric = barrel(helper, new BlockPos(3, 2, 1), Fluids.SULFURIC_ACID, 0);
        sulfuric.inventory.setStackInSlot(2, new ItemStack(ModFluidNetItems.bucket_sulfuric_acid.get()));
        TileEntityBarrel mud = barrel(helper, new BlockPos(5, 2, 1), Fluids.NONE, 0);
        mud.inventory.setStackInSlot(2, new ItemStack(ModFluidNetItems.bucket_mud.get()));
        TileEntityBarrel refill = barrel(helper, new BlockPos(1, 2, 3), Fluids.SULFURIC_ACID, 1_000);
        refill.inventory.setStackInSlot(4, new ItemStack(Items.BUCKET));
        TileEntityBarrel old = barrel(helper, new BlockPos(3, 2, 3), Fluids.DIESEL, 0);
        old.inventory.setStackInSlot(2, ItemFluidBucket.make(Fluids.DIESEL));

        helper.runAfterDelay(20, () -> {
            helper.assertTrue(ethanol.inventory.getStackInSlot(4).is(Items.BUCKET) && ethanol.inventory.getStackInSlot(5).isEmpty()
                            && ethanol.tank.getFill() == 5_000,
                    "an ethanol barrel filled a bucket: " + ethanol.inventory.getStackInSlot(5) + ", " + ethanol.tank.getFill() + " mB left");
            helper.assertTrue(sulfuric.tank.getFill() == 1_000 && sulfuric.tank.getTankType() == Fluids.SULFURIC_ACID
                            && sulfuric.inventory.getStackInSlot(3).is(Items.BUCKET),
                    "the sulfuric acid bucket gave " + sulfuric.tank.getFill() + " mB");
            helper.assertTrue(mud.tank.getFill() == 1_000 && mud.tank.getTankType() == Fluids.WATZ,
                    "the mud bucket made a " + mud.tank.getTankType().getRegistryName() + " barrel with " + mud.tank.getFill() + " mB");
            helper.assertTrue(refill.tank.getFill() == 0 && refill.inventory.getStackInSlot(5).is(ModFluidNetItems.bucket_sulfuric_acid.get()),
                    "a sulfuric acid barrel filled " + refill.inventory.getStackInSlot(5));
            helper.assertTrue(old.tank.getFill() == ItemFluidBucket.VOLUME && old.inventory.getStackInSlot(3).is(Items.BUCKET),
                    "an old universal diesel bucket gave " + old.tank.getFill() + " mB");
            helper.succeed();
        });
    }

    private static TileEntityBarrel barrel(GameTestHelper helper, BlockPos at, FluidType type, int fill) {
        helper.setBlock(at, ModFluidNetBlocks.barrel_steel.get().defaultBlockState());
        TileEntityBarrel barrel = (TileEntityBarrel) helper.getLevel().getBlockEntity(helper.absolutePos(at));
        if (type != Fluids.NONE) barrel.tank.setTankType(type);
        barrel.tank.setFill(fill);
        return barrel;
    }

    /** Each GEONTM fluid with a common counterpart is in its tags (and their Forge copies there) and shares them. */
    private static void tags(GameTestHelper helper) {
        int checked = 0;
        for (Map.Entry<String, List<String>> entry : ConventionFluidTags.all().entrySet()) {
            FluidType fluid = Fluids.byRegistryName(entry.getKey());
            helper.assertTrue(fluid != Fluids.NONE && !fluid.isForeign(), entry.getKey() + " is not an GEONTM fluid");
            for (String path : entry.getValue()) {
                for (String namespace : new LinkedHashSet<>(List.of("c", Platform.conventionNamespace()))) {
                    TagKey<Fluid> tag = TagKey.create(Registries.FLUID, Ids.of(namespace, path));
                    helper.assertTrue(fluid.getFluid().builtInRegistryHolder().is(tag), entry.getKey() + " is not in #" + tag.location());
                    helper.assertTrue(fluid.getSharedTags().contains(tag), entry.getKey() + " does not share #" + tag.location());
                    checked++;
                }
            }
        }
        helper.assertTrue(checked > 0, "no convention tags");
        helper.succeed();
    }

    /**
     * With Mekanism loaded: its liquid hydrogen, piped in and poured from its bucket, runs a diesel generator, its
     * sulfuric acid runs the chemical plant's nitric acid recipe into GEONTM nitric acid, and Mekanism's rotary recipe,
     * which takes {@code #c:hydrogen}, takes HBM's hydrogen. Skips without Mekanism.
     */
    private static void mekanism(GameTestHelper helper) {
        if (!Platform.isModLoaded("mekanism")) {
            Constants.LOG.info("[gh156] Mekanism absent, skipped");
            helper.succeed();
            return;
        }
        ServerLevel level = helper.getLevel();
        Fluid hydrogen = BuiltInRegistries.FLUID.getOptional(Ids.of("mekanism", "hydrogen")).orElseThrow();
        String[][] kinds = {{"hydrogen", "geontm:hydrogen"}, {"oxygen", "geontm:oxygen"}, {"steam", "geontm:steam"},
                {"sulfuric_acid", "geontm:sulfuric_acid"}, {"chlorine", "geontm:chlorine"}, {"heavy_water", "geontm:heavywater"},
                {"uranium_hexafluoride", "geontm:uf6"}, {"sodium", "geontm:sodium"}};
        for (String[] kind : kinds) {
            FluidType resource = Fluids.fromVanillaExact(BuiltInRegistries.FLUID.getOptional(Ids.of("mekanism", kind[0])).orElseThrow());
            helper.assertTrue(Fluids.kindOf(resource).getRegistryName().equals(kind[1]),
                    "mekanism:" + kind[0] + " is " + Fluids.kindOf(resource).getRegistryName() + " in GEONTM, expected " + kind[1]);
        }

        // brine has no GEONTM counterpart: picked once, as Mekanism's; hydrogen is HBM's and not picked twice
        long brine = Arrays.stream(Fluids.getSelectable()).filter(f -> f.getRegistryName().equals("mekanism:brine")).count();
        helper.assertTrue(brine == 1, "the picker lists mekanism:brine " + brine + " times");
        helper.assertTrue(Arrays.stream(Fluids.getSelectable()).noneMatch(f -> f.getRegistryName().equals("mekanism:hydrogen")),
                "the picker lists mekanism:hydrogen beside HBM's hydrogen");

        BlockPos piped = helper.absolutePos(new BlockPos(2, 2, 2));
        BlockPos poured = helper.absolutePos(new BlockPos(6, 2, 2));
        TileEntityMachineDiesel pipedGen = diesel(level, piped);
        TileEntityMachineDiesel pouredGen = diesel(level, poured);
        pouredGen.inventory.setStackInSlot(0, new ItemStack(item("mekanism:hydrogen_bucket")));

        BlockPos plantAt = FoldedTestSupport.assemble(helper, ModMachineBlocks.machine_chemical_plant.get(), new BlockPos(11, 2, 11));
        TileEntityMachineChemicalPlant plant = (TileEntityMachineChemicalPlant) level.getBlockEntity(plantAt);
        ChemicalPlantRecipes.INSTANCE.registerRecipes();
        plant.chemplantModule.recipe = "chem.nitricacid";
        plant.power = 1_000_000L;
        plant.inventory.setStackInSlot(4, new ItemStack(Ingredients.tagItems("c:dusts/niter").get(0)));
        plant.inventory.setStackInSlot(10, new ItemStack(item("mekanism:sulfuric_acid_bucket")));


        helper.runAfterDelay(20, () -> {
            FluidView view = FluidCompat.find(level, piped, Direction.UP);
            helper.assertTrue(view != null && view.insert(hydrogen, 1_000, false) == 1_000,
                    "the diesel generator refused Mekanism's liquid hydrogen through its fluid capability");
            helper.assertTrue(pipedGen.tank.getTankType() == Fluids.HYDROGEN && pipedGen.tank.getFill() == 1_000,
                    "the piped generator holds " + pipedGen.tank.getFill() + " mB of " + pipedGen.tank.getTankType().getRegistryName());
            // the generator has been burning since the bucket emptied, 1 mB a tick
            helper.assertTrue(pouredGen.tank.getTankType() == Fluids.HYDROGEN && pouredGen.tank.getFill() > 900
                            && pouredGen.inventory.getStackInSlot(0).isEmpty() && pouredGen.inventory.getStackInSlot(1).is(Items.BUCKET),
                    "Mekanism's hydrogen bucket gave the generator " + pouredGen.tank.getFill() + " mB");

            helper.assertTrue(rotaryTakes(level, Fluids.HYDROGEN.getFluid()) && rotaryTakes(level, hydrogen)
                            && !rotaryTakes(level, Fluids.DIESEL.getFluid()),
                    "Mekanism's rotary recipe does not take HBM's liquid hydrogen");

            helper.runAfterDelay(80, () -> {
                helper.assertTrue(pipedGen.power > 0 && pipedGen.tank.getFill() < 1_000,
                        "Mekanism hydrogen made " + pipedGen.power + " HE, " + pipedGen.tank.getFill() + " mB left");
                helper.assertTrue(plant.outputTanks[0].getTankType() == Fluids.NITRIC_ACID
                                && plant.outputTanks[0].getFluid() == Fluids.NITRIC_ACID && plant.outputTanks[0].getFill() >= 1_000,
                        "the chemical plant made " + plant.outputTanks[0].getFill() + " mB of "
                                + plant.outputTanks[0].getFluid().getRegistryName() + " from Mekanism sulfuric acid");
                helper.assertTrue(Fluids.kindOf(plant.inputTanks[0].getFluid()) == Fluids.SULFURIC_ACID,
                        "the plant's acid tank holds " + plant.inputTanks[0].getFluid().getRegistryName());
                helper.succeed();
            });
        });
    }

    /**
     * With Immersive Engineering loaded: its ethanol, biodiesel, creosote and plant oil are HBM's ethanol, biofuel, coal
     * creosote and sunflower oil; its ethanol piped in and its biodiesel poured from a bucket run diesel generators; and
     * HBM's biofuel and creosote are in the tags IE's generator and drill burn. Skips without IE.
     */
    private static void immersiveEngineering(GameTestHelper helper) {
        if (!Platform.isModLoaded("immersiveengineering")) {
            Constants.LOG.info("[gh156] Immersive Engineering absent, skipped");
            helper.succeed();
            return;
        }
        ServerLevel level = helper.getLevel();
        String[][] kinds = {{"ethanol", "geontm:ethanol"}, {"biodiesel", "geontm:biofuel"}, {"creosote", "geontm:coalcreosote"},
                {"plantoil", "geontm:sunfloweroil"}};
        for (String[] kind : kinds) {
            FluidType resource = Fluids.fromVanillaExact(BuiltInRegistries.FLUID.getOptional(Ids.of("immersiveengineering", kind[0])).orElseThrow());
            helper.assertTrue(Fluids.kindOf(resource).getRegistryName().equals(kind[1]),
                    "immersiveengineering:" + kind[0] + " is " + Fluids.kindOf(resource).getRegistryName() + " in GEONTM, expected " + kind[1]);
        }
        helper.assertTrue(Arrays.stream(Fluids.getSelectable()).noneMatch(f -> f.getNamespace().equals("immersiveengineering")
                        && Fluids.kindOf(f) != f), "the picker lists an IE fluid beside its GEONTM counterpart");
        helper.assertTrue(isIn(Fluids.BIOFUEL, "c", "biodiesel") && isIn(Fluids.BIOFUEL, "immersiveengineering", "drill_fuel")
                        && isIn(Fluids.COALCREOSOTE, "c", "creosote"),
                "GEONTM biofuel and creosote are not in the tags IE's generator and drill burn");
        helper.assertTrue(ieBurns(level, Fluids.BIOFUEL.getFluid()) && ieBurns(level, Fluids.COALCREOSOTE.getFluid())
                        && !ieBurns(level, Fluids.ETHANOL.getFluid()),
                "IE's generator fuel lookup does not take GEONTM biofuel and creosote (or takes ethanol)");

        Fluid ethanol = BuiltInRegistries.FLUID.getOptional(Ids.of("immersiveengineering", "ethanol")).orElseThrow();
        BlockPos piped = helper.absolutePos(new BlockPos(2, 2, 2));
        BlockPos poured = helper.absolutePos(new BlockPos(6, 2, 2));
        TileEntityMachineDiesel pipedGen = diesel(level, piped, Fluids.ETHANOL);
        TileEntityMachineDiesel pouredGen = diesel(level, poured, Fluids.BIOFUEL);
        pouredGen.inventory.setStackInSlot(0, new ItemStack(item("immersiveengineering:biodiesel_bucket")));
        helper.runAfterDelay(20, () -> {
            FluidView view = FluidCompat.find(level, piped, Direction.UP);
            helper.assertTrue(view != null && view.insert(ethanol, 1_000, false) == 1_000,
                    "the diesel generator refused IE's ethanol through its fluid capability");
            helper.assertTrue(pouredGen.tank.getTankType() == Fluids.BIOFUEL && pouredGen.tank.getFill() > 900
                            && pouredGen.inventory.getStackInSlot(1).is(Items.BUCKET),
                    "IE's biodiesel bucket gave the generator " + pouredGen.tank.getFill() + " mB");
            helper.runAfterDelay(80, () -> {
                helper.assertTrue(pipedGen.power > 0 && pipedGen.tank.getFill() < 1_000 && pouredGen.power > 0,
                        "IE ethanol made " + pipedGen.power + " HE, IE biodiesel " + pouredGen.power + " HE");
                helper.succeed();
            });
        });
    }

    /**
     * With Immersive Petroleum loaded: its oil products are HBM's, its crude oil fills an GEONTM oil barrel through the fluid
     * capability, its diesel runs a diesel generator, and GEONTM diesel and kerosene are in the tags IE's drill and IP's
     * flare stack burn. Skips without IP.
     */
    private static void immersivePetroleum(GameTestHelper helper) {
        if (!Platform.isModLoaded("immersivepetroleum")) {
            Constants.LOG.info("[gh156] Immersive Petroleum absent, skipped");
            helper.succeed();
            return;
        }
        ServerLevel level = helper.getLevel();
        String[][] kinds = {{"crudeoil", "geontm:oil"}, {"diesel", "geontm:diesel"}, {"diesel_sulfur", "geontm:diesel"},
                {"gasoline", "geontm:gasoline"}, {"kerosene", "geontm:kerosene"}, {"lubricant", "geontm:lubricant"},
                {"naphtha", "geontm:naphtha"}, {"petroleum_gas", "geontm:petroleum"}, {"benzol", "geontm:xylene"}};
        for (String[] kind : kinds) {
            FluidType resource = Fluids.fromVanillaExact(BuiltInRegistries.FLUID.getOptional(Ids.of("immersivepetroleum", kind[0])).orElseThrow());
            helper.assertTrue(Fluids.kindOf(resource).getRegistryName().equals(kind[1]),
                    "immersivepetroleum:" + kind[0] + " is " + Fluids.kindOf(resource).getRegistryName() + " in GEONTM, expected " + kind[1]);
        }
        helper.assertTrue(isIn(Fluids.DIESEL, "immersiveengineering", "drill_fuel") && isIn(Fluids.KEROSENE, "immersiveengineering", "drill_fuel")
                        && isIn(Fluids.PETROLEUM, "immersivepetroleum", "burnable_in_flarestack"),
                "GEONTM diesel, kerosene and petroleum gas are not in IE's drill fuel and IP's flare stack tags");

        Fluid crude = BuiltInRegistries.FLUID.getOptional(Ids.of("immersivepetroleum", "crudeoil")).orElseThrow();
        Fluid diesel = BuiltInRegistries.FLUID.getOptional(Ids.of("immersivepetroleum", "diesel")).orElseThrow();
        BlockPos barrelAt = helper.absolutePos(new BlockPos(2, 2, 6));
        TileEntityBarrel oilBarrel = barrel(helper, new BlockPos(2, 2, 6), Fluids.OIL, 0);
        BlockPos genAt = helper.absolutePos(new BlockPos(2, 2, 2));
        TileEntityMachineDiesel gen = diesel(level, genAt, Fluids.DIESEL);
        helper.runAfterDelay(20, () -> {
            FluidView barrelView = FluidCompat.find(level, barrelAt, Direction.UP);
            helper.assertTrue(barrelView != null && barrelView.insert(crude, 1_000, false) == 1_000
                            && oilBarrel.tank.getTankType() == Fluids.OIL && oilBarrel.tank.getFill() == 1_000,
                    "the oil barrel holds " + oilBarrel.tank.getFill() + " mB after IP crude oil");
            FluidView genView = FluidCompat.find(level, genAt, Direction.UP);
            helper.assertTrue(genView != null && genView.insert(diesel, 1_000, false) == 1_000,
                    "the diesel generator refused IP's diesel");
            helper.runAfterDelay(80, () -> {
                helper.assertTrue(gen.power > 0 && gen.tank.getFill() < 1_000, "IP diesel made " + gen.power + " HE");
                helper.succeed();
            });
        });
    }

    /** IE's {@code GeneratorFuel.getRecipeFor}, what its diesel generator asks, reached reflectively like Mekanism's API. */
    private static boolean ieBurns(ServerLevel level, Fluid fluid) {
        try {
            Class<?> fuel = Class.forName("blusunrize.immersiveengineering.api.energy.GeneratorFuel");
            return fuel.getMethod("getRecipeFor", Level.class, Fluid.class, fuel).invoke(null, level, fluid, null) != null;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("IE's generator fuel API changed", e);
        }
    }

    private static boolean isIn(FluidType fluid, String namespace, String path) {
        return fluid.getFluid().defaultFluidState().is(TagKey.create(Registries.FLUID, Ids.of(namespace, path)));
    }

    /** Mekanism's {@code mekanism:rotary/hydrogen} recipe ({@code #c:hydrogen}), reached reflectively like Pipez. */
    private static boolean rotaryTakes(ServerLevel level, Fluid fluid) {
        try {
            Object id = Ids.parse("mekanism:rotary/hydrogen");
            Object manager = level.getServer().getRecipeManager();
            Object holder = null;
            for (Method method : manager.getClass().getMethods()) {
                if (method.getName().equals("byKey") && method.getParameterCount() == 1 && method.getParameterTypes()[0].isInstance(id)) {
                    holder = ((Optional<?>) method.invoke(manager, id)).orElseThrow();
                }
            }
            Object recipe = holder.getClass().getMethod("value").invoke(holder);
            Class<?> stackClass = Class.forName("net.neoforged.neoforge.fluids.FluidStack");
            Object stack = stackClass.getConstructor(Fluid.class, int.class).newInstance(fluid, 1_000);
            return (Boolean) recipe.getClass().getMethod("test", stackClass).invoke(recipe, stack);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Mekanism's rotary recipe API changed", e);
        }
    }

    private static TileEntityMachineDiesel diesel(ServerLevel level, BlockPos pos) {
        return diesel(level, pos, Fluids.HYDROGEN);
    }

    private static TileEntityMachineDiesel diesel(ServerLevel level, BlockPos pos, FluidType type) {
        level.setBlock(pos, ModMachineBlocks.machine_diesel.get().defaultBlockState(), 3);
        TileEntityMachineDiesel gen = (TileEntityMachineDiesel) level.getBlockEntity(pos);
        gen.tank.setTankType(type);
        gen.isOn = true;
        return gen;
    }

    private static Item item(String id) {
        return BuiltInRegistries.ITEM.get(Ids.parse(id));
    }
}
