package fr.geoventure.geontm.gametest;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.api.event.GeoNtmEvents;
import fr.geoventure.geontm.api.script.GeoNtmScriptApi;
import fr.geoventure.geontm.blocks.machine.ModMachineBlocks;
import fr.geoventure.geontm.config.RadiationConfig;
import fr.geoventure.geontm.explosion.ExplosionLarge;
import fr.geoventure.geontm.explosion.vanillant.ExplosionVNT;
import fr.geoventure.geontm.handler.radiation.ChunkRadiationManager;
import fr.geoventure.geontm.interfaces.IBomb;
import fr.geoventure.geontm.inventory.RecipesCommon.AStack;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.inventory.recipes.AssemblyMachineRecipes;
import fr.geoventure.geontm.inventory.recipes.loader.GenericRecipe;
import fr.geoventure.geontm.items.ModItems;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineAssemblyMachine;
import fr.geoventure.geontm.util.ContaminationUtil;
import fr.geoventure.geocore.gametest.GameTestHelpers;
import fr.geoventure.geocore.gametest.GameTests;
import fr.geoventure.geocore.world.ItemStacks;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * The observe-only {@link GeoNtmEvents} hooks (the moments GEONTM offers Java addons and, through {@code GeoNtmScripts}, scripts)
 * fire at their GEONTM moment with the right payload, and the {@code GEONTM} script global reads what HBM's own APIs read.
 * Listeners are registered once and record only positions or entities a test put in the maps, so concurrent tests
 * never see each other's moments.
 */
public final class ScriptHookGameTests {

    private ScriptHookGameTests() {}

    public static final GameTests.Suite TESTS = GameTests.suite(Constants.MOD_ID);

    public static final GameTests.Function EXPLOSIONS =
            TESTS.function("script_hooks_explosions", 20, () -> ScriptHookGameTests::explosions);
    public static final GameTests.Function BOMB =
            TESTS.function("script_hooks_bomb_detonated", 20, () -> ScriptHookGameTests::bomb);
    public static final GameTests.Function MACHINE =
            TESTS.function("script_hooks_machine_recipe_completed", 20, 8, () -> ScriptHookGameTests::machine);
    public static final GameTests.Function IRRADIATED =
            TESTS.function("script_hooks_entity_irradiated", 20, () -> ScriptHookGameTests::irradiated);
    public static final GameTests.Function API =
            TESTS.function("script_hooks_binding_reads", 20, () -> ScriptHookGameTests::api);

    public static void init() {}

    private static final AtomicBoolean REGISTERED = new AtomicBoolean();
    private static final Map<BlockPos, GeoNtmEvents.Explosion> EXPLOSION_AT = new ConcurrentHashMap<>();
    private static final Map<BlockPos, GeoNtmEvents.BombDetonated> BOMB_AT = new ConcurrentHashMap<>();
    private static final Map<BlockPos, GeoNtmEvents.MachineRecipeCompleted> MACHINE_AT = new ConcurrentHashMap<>();
    private static final Map<UUID, GeoNtmEvents.EntityIrradiated> IRRADIATED_ENTITY = new ConcurrentHashMap<>();
    private static final Set<BlockPos> WATCHED = ConcurrentHashMap.newKeySet();
    private static final Set<UUID> WATCHED_ENTITIES = ConcurrentHashMap.newKeySet();

    private static void register() {
        if (!REGISTERED.compareAndSet(false, true)) return;
        GeoNtmEvents.EXPLOSION.register(e -> {
            BlockPos at = BlockPos.containing(e.x(), e.y(), e.z());
            if (WATCHED.contains(at)) EXPLOSION_AT.put(at, e);
        });
        GeoNtmEvents.BOMB_DETONATED.register(e -> {
            if (WATCHED.contains(e.pos())) BOMB_AT.put(e.pos(), e);
        });
        GeoNtmEvents.MACHINE_RECIPE_COMPLETED.register(e -> {
            if (WATCHED.contains(e.pos())) MACHINE_AT.put(e.pos(), e);
        });
        GeoNtmEvents.ENTITY_IRRADIATED.register(e -> {
            if (WATCHED_ENTITIES.contains(e.entity().getUUID())) IRRADIATED_ENTITY.put(e.entity().getUUID(), e);
        });
    }

    /** ExplosionVNT (and so ExplosionNT) reports "vnt", ExplosionLarge "large", each with its centre and size. */
    public static void explosions(GameTestHelper helper) {
        register();
        BlockPos vnt = helper.absolutePos(new BlockPos(1, 2, 1));
        WATCHED.add(vnt);
        // no allocator, processors or effects: the explosion changes nothing, only its moment is told
        new ExplosionVNT(helper.getLevel(), vnt.getX() + 0.5D, vnt.getY() + 0.5D, vnt.getZ() + 0.5D, 3F).explode();
        GeoNtmEvents.Explosion seen = EXPLOSION_AT.remove(vnt);
        WATCHED.remove(vnt);
        helper.assertTrue(seen != null && seen.kind().equals("vnt") && seen.size() == 3F && seen.level() == helper.getLevel(),
                "ExplosionVNT.explode did not reach GeoNtmEvents.EXPLOSION as a size-3 vnt blast: " + seen);

        BlockPos large = helper.absolutePos(new BlockPos(3, 2, 3));
        WATCHED.add(large);
        ExplosionLarge.explode(helper.getLevel(), null, large.getX() + 0.5D, large.getY() + 0.5D, large.getZ() + 0.5D, 0.5F,
                false, false, false);
        GeoNtmEvents.Explosion seenLarge = EXPLOSION_AT.remove(large);
        WATCHED.remove(large);
        helper.assertTrue(seenLarge != null && seenLarge.kind().equals("large") && seenLarge.size() == 0.5F,
                "ExplosionLarge.explode did not reach GeoNtmEvents.EXPLOSION as a large blast: " + seenLarge);
        helper.succeed();
    }

    /** The detonator dispatch reports the bomb block, the detonator and the bomb's own return code. */
    public static void bomb(GameTestHelper helper) {
        register();
        BlockPos pos = helper.absolutePos(new BlockPos(1, 2, 1));
        WATCHED.add(pos);
        IBomb bomb = (level, at, detonator) -> IBomb.BombReturnCode.ERROR_MISSING_COMPONENT;
        IBomb.BombReturnCode result = GeoNtmEvents.detonate(bomb, helper.getLevel(), pos, null);
        GeoNtmEvents.BombDetonated seen = BOMB_AT.remove(pos);
        WATCHED.remove(pos);
        helper.assertTrue(result == IBomb.BombReturnCode.ERROR_MISSING_COMPONENT, "detonate must return the bomb's code");
        helper.assertTrue(seen != null && seen.result().equals("ERROR_MISSING_COMPONENT") && !seen.success()
                        && seen.block().equals("minecraft:air") && seen.detonator() == null,
                "the detonation did not reach GeoNtmEvents.BOMB_DETONATED with its code: " + seen);
        helper.succeed();
    }

    /** An assembly machine finishing one operation reports its position, its type, its table and the recipe. */
    public static void machine(GameTestHelper helper) {
        register();
        AssemblyMachineRecipes.INSTANCE.registerRecipes();
        BlockPos rel = new BlockPos(2, 1, 2);
        FoldedTestSupport.assemble(helper, ModMachineBlocks.machine_assembly_machine.get(), rel);
        TileEntityMachineAssemblyMachine assembler = GameTestHelpers.blockEntity(helper, rel, TileEntityMachineAssemblyMachine.class);
        GenericRecipe recipe = loadRunnable(assembler);
        helper.assertTrue(recipe != null, "no runnable assembler recipe found");
        BlockPos abs = helper.absolutePos(rel);
        WATCHED.add(abs);
        assembler.power = assembler.maxPower;
        assembler.assemblerModule.progress = 0.9999D; // the next working tick completes the operation
        TileEntityMachineAssemblyMachine.serverTick(helper.getLevel(), abs, helper.getLevel().getBlockState(abs), assembler);
        GeoNtmEvents.MachineRecipeCompleted seen = MACHINE_AT.remove(abs);
        WATCHED.remove(abs);
        helper.assertTrue(assembler.didProcess, "the assembler worked on " + recipe.name);
        helper.assertTrue(seen != null && seen.recipe().equals(recipe.getInternalName())
                        && seen.table().equals(Constants.MOD_ID + ":assembly_machine") && seen.machine().startsWith(Constants.MOD_ID + ":"),
                "the completed operation did not reach GeoNtmEvents.MACHINE_RECIPE_COMPLETED: " + seen);
        helper.succeed();
    }

    /** A radiation dose that gets through reports the dose after resistance; a blocked one reports nothing. */
    public static void irradiated(GameTestHelper helper) {
        register();
        LivingEntity pig = helper.spawn(EntityType.PIG, new BlockPos(1, 2, 1));
        WATCHED_ENTITIES.add(pig.getUUID());
        boolean applied = ContaminationUtil.contaminate(pig, ContaminationUtil.HazardType.RADIATION,
                ContaminationUtil.ContaminationType.RAD_BYPASS, 10D);
        GeoNtmEvents.EntityIrradiated seen = IRRADIATED_ENTITY.remove(pig.getUUID());
        // a zombie is radiation-immune (1.7 immuneEntities): no dose, no moment
        LivingEntity zombie = helper.spawn(EntityType.ZOMBIE, new BlockPos(3, 2, 3));
        WATCHED_ENTITIES.add(zombie.getUUID());
        ContaminationUtil.contaminate(zombie, ContaminationUtil.HazardType.RADIATION, ContaminationUtil.ContaminationType.NONE, 10D);
        GeoNtmEvents.EntityIrradiated immune = IRRADIATED_ENTITY.remove(zombie.getUUID());
        WATCHED_ENTITIES.remove(pig.getUUID());
        WATCHED_ENTITIES.remove(zombie.getUUID());
        pig.discard();
        zombie.discard();
        helper.assertTrue(applied, "a RAD_BYPASS dose reaches a pig");
        if (RadiationConfig.enableContamination) {
            helper.assertTrue(seen != null && seen.hazard().equals("RADIATION") && seen.contamination().equals("RAD_BYPASS")
                            && seen.amount() == 10D && seen.dose() == 10D,
                    "the dose did not reach GeoNtmEvents.ENTITY_IRRADIATED: " + seen);
        }
        helper.assertTrue(immune == null, "an immune entity's refused dose must not be reported");
        helper.succeed();
    }

    /** The GEONTM script global answers with HBM's own values and plain types. */
    public static void api(GameTestHelper helper) {
        GeoNtmScriptApi api = GeoNtmScriptApi.INSTANCE;
        helper.assertTrue(api.fluids().contains("geontm:oil") && !api.fluids().contains("geontm:none"), "fluids() lists geontm:oil");
        Map<String, Object> oil = api.fluid("oil");
        helper.assertTrue(oil != null && "geontm:oil".equals(oil.get("id"))
                        && oil.get("temperature").equals(Fluids.OIL.getTemperature()),
                "fluid('oil') must read geontm:oil: " + oil);
        helper.assertTrue(api.fluid("geontm:no_such_fluid") == null && !api.hasFluid("bad id!"), "unknown fluids read as null");
        helper.assertTrue(api.fluidTraits("geontm:oil").contains("liquid"), "oil is a liquid: " + api.fluidTraits("geontm:oil"));
        helper.assertTrue(api.hasFluid("minecraft:water") && api.hasFluid("water"), "water is an GEONTM fluid");
        Map<String, Object> iron = api.material("iron");
        helper.assertTrue(iron != null && "SMELTABLE".equals(iron.get("smelting")), "material('iron') is smeltable: " + iron);
        helper.assertTrue(api.material("no_such_material") == null, "unknown materials read as null");
        ItemStack uranium = new ItemStack(ModItems.ingot_uranium.get());
        List<Map<String, Object>> hazards = api.itemHazards(uranium);
        helper.assertTrue(!hazards.isEmpty() && hazards.stream().anyMatch(h -> "geontm:radiation".equals(h.get("type"))),
                "a uranium ingot is radioactive: " + hazards);
        helper.assertTrue(api.itemHazardLevel(uranium, "radiation") == api.itemRadiation(uranium)
                        && api.itemRadiation(uranium) > 0D, "itemHazardLevel(radiation) equals itemRadiation");
        BlockPos pos = helper.absolutePos(new BlockPos(1, 1, 1));
        helper.assertTrue(api.getChunkRadiation(helper.getLevel(), pos)
                        == ChunkRadiationManager.proxy.getRadiation(helper.getLevel(), pos),
                "getChunkRadiation reads RadiationSystemNT");
        helper.succeed();
    }

    /** The first unpooled assembler recipe whose inputs all resolve, its inputs put in the machine. */
    private static GenericRecipe loadRunnable(TileEntityMachineAssemblyMachine assembler) {
        for (GenericRecipe recipe : AssemblyMachineRecipes.INSTANCE.recipeOrderedList) {
            if (recipe.isPooled() || recipe.power > 100_000 || recipe.inputItem == null) continue;
            if (recipe.inputFluid != null && recipe.inputFluid.length > 0) continue;
            if (recipe.inputItem.length > assembler.assemblerModule.inputSlots.length) continue;
            boolean ok = true;
            ItemStack[] fills = new ItemStack[recipe.inputItem.length];
            for (int i = 0; i < recipe.inputItem.length; i++) {
                AStack in = recipe.inputItem[i];
                List<ItemStack> shown = in.getDisplayStacks();
                if (shown.isEmpty() || shown.get(0).isEmpty() || in.stacksize > shown.get(0).getMaxStackSize()) {
                    ok = false;
                    break;
                }
                fills[i] = ItemStacks.copyWithCount(shown.get(0), in.stacksize);
            }
            if (!ok) continue;
            assembler.assemblerModule.recipe = recipe.name;
            for (int i = 0; i < fills.length; i++) {
                assembler.inventory.setStackInSlot(assembler.assemblerModule.inputSlots[i], fills[i]);
            }
            return recipe;
        }
        return null;
    }
}
