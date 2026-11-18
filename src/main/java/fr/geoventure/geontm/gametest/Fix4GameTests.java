package fr.geoventure.geontm.gametest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.itempool.ItemPool;
import fr.geoventure.geontm.itempool.ItemPools;
import fr.geoventure.geontm.items.ModDataComponents;
import fr.geoventure.geontm.items.machine.ItemFluidTank;
import fr.geoventure.geontm.items.special.ItemCell;
import fr.geoventure.geontm.items.tool.ItemBlowtorch;
import com.mojang.serialization.JsonOps;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.gametest.GameTests;
import fr.geoventure.geocore.world.ChestLoot;
import fr.geoventure.geocore.world.CompatEnchantments;
import fr.geoventure.geocore.world.ItemStacks;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Block drops and chest loot against 1.7
 * (1.7.10). Block drops are sampled through {@code Block.getDrops} with a plain, a Fortune III and a Silk Touch
 * diamond pickaxe, and checked against the 1.7 range and the analytic mean of the 1.7 formula. The 1.7 ore
 * fortune multiplier is {@code max(rand(f + 2) - 1, 0) + 1}; at Fortune III it is 1, 1, 2, 3, 4 with equal
 * probability, mean 2.2.
 */
public final class Fix4GameTests {

    private Fix4GameTests() {}

    public static final GameTests.Suite TESTS = GameTests.suite(Constants.MOD_ID);
    public static final GameTests.Function ORE_DROPS =
            TESTS.function("fix4_ore_drops", 40, () -> Fix4GameTests::oreDrops);
    public static final GameTests.Function SILK_TOUCH =
            TESTS.function("fix4_silk_touch", 40, () -> Fix4GameTests::silkTouch);
    public static final GameTests.Function SPECIAL_DROPS =
            TESTS.function("fix4_special_drops", 40, () -> Fix4GameTests::specialDrops);
    public static final GameTests.Function ITEM_POOLS =
            TESTS.function("fix4_item_pools", 40, () -> Fix4GameTests::itemPools);
    public static final GameTests.Function CHEST_INJECTION =
            TESTS.function("fix4_chest_injection", 40, () -> Fix4GameTests::chestInjection);

    private static final int TRIALS = 600;
    private static final double FORTUNE_III = 2.2D;

    public static void init() {}

    private static Item item(String id) {
        return BuiltInRegistries.ITEM.getValue(Ids.parse(id));
    }

    private static Block block(String id) {
        return BuiltInRegistries.BLOCK.getOptional(Ids.parse(id)).orElseThrow(() -> new IllegalStateException("no block " + id));
    }

    private enum Tool { PLAIN, FORTUNE, SILK }

    private static ItemStack tool(ServerLevel level, Tool tool) {
        ItemStack pick = new ItemStack(Items.DIAMOND_PICKAXE);
        if (tool == Tool.FORTUNE) CompatEnchantments.enchantFortune(level, pick, 3);
        if (tool == Tool.SILK) CompatEnchantments.enchantSilkTouch(level, pick);
        return pick;
    }

    private static List<ItemStack> drops(GameTestHelper helper, BlockState state, Tool tool) {
        ServerLevel level = helper.getLevel();
        BlockPos pos = helper.absolutePos(new BlockPos(1, 1, 1));
        return Block.getDrops(state, level, pos, null, null, tool(level, tool));
    }

    /**
     * {@code TRIALS} breaks: every drop must be one of {@code allowed}, the per-break total of {@code counted}
     * within {@code min..max}, and its mean within 12% (plus 0.05) of {@code mean}.
     */
    private static void sample(GameTestHelper helper, String blockId, Tool tool, Set<String> allowed, String counted,
                               int min, int max, double mean) {
        BlockState state = block(blockId).defaultBlockState();
        Item countedItem = item(counted);
        long total = 0;
        int seenMin = Integer.MAX_VALUE;
        int seenMax = Integer.MIN_VALUE;
        for (int i = 0; i < TRIALS; i++) {
            int n = 0;
            for (ItemStack stack : drops(helper, state, tool)) {
                String id = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
                helper.assertTrue(allowed.contains(id), blockId + " (" + tool + ") dropped " + stack + ", 1.7 allows " + allowed);
                if (stack.is(countedItem)) n += stack.getCount();
            }
            total += n;
            seenMin = Math.min(seenMin, n);
            seenMax = Math.max(seenMax, n);
        }
        double avg = (double) total / TRIALS;
        helper.assertTrue(seenMin >= min && seenMax <= max,
                blockId + " (" + tool + "): " + counted + " per break " + seenMin + ".." + seenMax + ", 1.7 " + min + ".." + max);
        helper.assertTrue(Math.abs(avg - mean) <= mean * 0.12D + 0.05D,
                blockId + " (" + tool + "): mean " + avg + " " + counted + ", 1.7 formula " + mean);
    }

    private static void ore(GameTestHelper helper, String blockId, String drop, int min, int max, boolean fortune) {
        double mean = (min + max) / 2.0D;
        sample(helper, blockId, Tool.PLAIN, Set.of(drop), drop, min, max, mean);
        if (fortune) {
            sample(helper, blockId, Tool.FORTUNE, Set.of(drop), drop, min, max * 4, mean * FORTUNE_III);
        } else {
            sample(helper, blockId, Tool.FORTUNE, Set.of(drop), drop, min, max, mean);
        }
    }

    private static void exactly(GameTestHelper helper, String blockId, Tool tool, String drop, int count) {
        List<ItemStack> drops = drops(helper, block(blockId).defaultBlockState(), tool);
        int n = 0;
        for (ItemStack stack : drops) {
            helper.assertTrue(stack.is(item(drop)), blockId + " (" + tool + ") must drop only " + drop + ", got " + drops);
            n += stack.getCount();
        }
        helper.assertTrue(n == count, blockId + " (" + tool + ") must drop " + count + "x " + drop + ", got " + drops);
    }

    private static void nothing(GameTestHelper helper, String blockId, Tool tool) {
        List<ItemStack> drops = drops(helper, block(blockId).defaultBlockState(), tool);
        helper.assertTrue(drops.isEmpty(), blockId + " (" + tool + ") must drop nothing in 1.7, got " + drops);
    }

    /** 1.7 BlockOre, BlockDepthOre, BlockSellafieldOre, WasteLog and friends: identity, counts and fortune. */
    public static void oreDrops(GameTestHelper helper) {
        ore(helper, "geontm:ore_lignite", "geontm:lignite", 1, 1, true);
        ore(helper, "geontm:ore_rare", "geontm:chunk_ore_rare", 1, 1, true);
        ore(helper, "geontm:ore_gneiss_rare", "geontm:chunk_ore_rare", 1, 1, true);
        ore(helper, "geontm:ore_niter", "geontm:niter", 2, 4, true);
        ore(helper, "geontm:ore_sulfur", "geontm:sulfur", 2, 4, true);
        ore(helper, "geontm:ore_cinnabar", "geontm:cinnabar", 1, 1, true);
        // the cobalt ores drop raw cobalt while geontm.cfg's raw ore option is on
        RawOreGameTests.legacyDrops(() -> {
            ore(helper, "geontm:ore_cobalt", "geontm:fragment_cobalt", 4, 9, true);
            ore(helper, "geontm:ore_nether_cobalt", "geontm:fragment_cobalt", 5, 12, true);
        });
        ore(helper, "geontm:ore_coltan", "geontm:fragment_coltan", 1, 1, true);
        ore(helper, "geontm:waste_planks", "minecraft:charcoal", 1, 1, true);
        ore(helper, "geontm:frozen_dirt", "minecraft:snowball", 1, 1, true);
        ore(helper, "geontm:frozen_planks", "minecraft:snowball", 1, 1, true);
        ore(helper, "geontm:ore_sellafield_diamond", "minecraft:diamond", 1, 1, true);
        ore(helper, "geontm:ore_oil", "geontm:oil_tar_crude", 1, 1, true);
        ore(helper, "geontm:ore_nether_coal", "geontm:coal_infernal", 1, 1, true);
        ore(helper, "geontm:waste_trinitite", "geontm:trinitite", 1, 1, false);
        ore(helper, "geontm:waste_trinitite_red", "geontm:trinitite", 1, 1, false);
        ore(helper, "geontm:block_meteor_cobble", "geontm:fragment_meteorite", 1, 1, false);
        ore(helper, "geontm:waste_earth", "minecraft:dirt", 1, 1, false);
        ore(helper, "geontm:waste_mycelium", "minecraft:dirt", 1, 1, false);
        ore(helper, "geontm:ore_nether_smoldering", "geontm:powder_fire", 1, 1, false);
        ore(helper, "geontm:cluster_iron", "geontm:crystal_iron", 1, 1, false);
        // depth ores: every one multiplies by fortune in 1.7
        ore(helper, "geontm:ore_depth_cinnabar", "geontm:cinnabar", 2, 4, true);
        ore(helper, "geontm:ore_depth_zirconium", "geontm:nugget_zirconium", 2, 3, true);
        ore(helper, "geontm:ore_depth_nether_neodymium", "geontm:fragment_neodymium", 2, 3, true);
        ore(helper, "geontm:ore_depth_borax", "geontm:powder_borax", 1, 1, true);
        ore(helper, "geontm:cluster_depth_iron", "geontm:crystal_iron", 1, 1, true);
        // alexandrite: 1 gem, multiplied, clamped to 2 -> 1, 1, 2, 2, 2 at Fortune III
        sample(helper, "geontm:ore_alexandrite", Tool.PLAIN, Set.of("geontm:gem_alexandrite"), "geontm:gem_alexandrite", 1, 1, 1.0D);
        sample(helper, "geontm:ore_alexandrite", Tool.FORTUNE, Set.of("geontm:gem_alexandrite"), "geontm:gem_alexandrite", 1, 2, 1.6D);
        // nether phosphorus: one item per multiplied unit, each 1 in 10 phosphorus
        Set<String> fire = Set.of("geontm:powder_fire", "geontm:ingot_phosphorus");
        sample(helper, "geontm:ore_nether_fire", Tool.PLAIN, fire, "geontm:powder_fire", 0, 1, 0.9D);
        sample(helper, "geontm:ore_nether_fire", Tool.PLAIN, fire, "geontm:ingot_phosphorus", 0, 1, 0.1D);
        // waste log: 2-4 charcoal, no fortune; burnt bark is a 1-in-1000 replacement
        Set<String> log = Set.of("minecraft:charcoal", "geontm:burnt_bark");
        sample(helper, "geontm:waste_log", Tool.PLAIN, log, "minecraft:charcoal", 0, 4, 3.0D);
        sample(helper, "geontm:waste_log", Tool.FORTUNE, log, "minecraft:charcoal", 0, 4, 3.0D);
        // glyphid spawner: 1 + rand(3) + fortune eggs
        sample(helper, "geontm:glyphid_spawner", Tool.PLAIN, Set.of("geontm:egg_glyphid"), "geontm:egg_glyphid", 1, 3, 2.0D);
        sample(helper, "geontm:glyphid_spawner", Tool.FORTUNE, Set.of("geontm:egg_glyphid"), "geontm:egg_glyphid", 4, 6, 5.0D);
        helper.succeed();
    }

    /** 1.7 canSilkHarvest: plain opaque cubes without a tile entity give themselves; the exceptions do not. */
    public static void silkTouch(GameTestHelper helper) {
        String[] selfUnderSilk = {"geontm:ore_lignite", "geontm:ore_rare", "geontm:ore_nether_fire", "geontm:ore_cobalt",
                "geontm:waste_planks", "geontm:frozen_dirt", "geontm:waste_earth", "geontm:waste_mycelium", "geontm:waste_trinitite",
                "geontm:waste_log", "geontm:block_meteor_treasure", "geontm:ore_depth_borax", "geontm:ore_alexandrite",
                "geontm:ore_sellafield_diamond", "geontm:ore_sellafield_emerald", "geontm:ore_sellafield_radgem",
                "geontm:ore_nether_coal", "geontm:ore_nether_smoldering", "geontm:cluster_iron", "geontm:stone_resource_malachite",
                "geontm:reinforced_glass", "geontm:glass_uranium", "geontm:glass_trinitite", "geontm:glass_polonium",
                "geontm:glass_ash", "geontm:glass_polarized", "geontm:hadron_plating_glass", "geontm:hadron_analysis_glass",
                "geontm:reinforced_glass_pane", "geontm:reinforced_laminate_pane", "geontm:machine_boiler_off",
                "geontm:block_meteor_molten", "geontm:taint", "geontm:det_miner", "geontm:oil_pipe", "geontm:barricade",
                "geontm:brick_jungle_fragile", "geontm:burning_earth", "geontm:impact_dirt", "geontm:crate_jungle"};
        for (String id : selfUnderSilk) exactly(helper, id, Tool.SILK, id, 1);
        // the same glass without silk touch drops nothing (1.7 doesDrop = false; both panes always)
        for (String id : new String[]{"geontm:reinforced_glass", "geontm:glass_uranium", "geontm:glass_polarized",
                "geontm:reinforced_glass_pane", "geontm:reinforced_laminate_pane", "geontm:taint", "geontm:oil_pipe"}) {
            nothing(helper, id, Tool.PLAIN);
        }
        // doesDrop = true glass drops itself either way
        exactly(helper, "geontm:reinforced_laminate", Tool.PLAIN, "geontm:reinforced_laminate", 1);
        exactly(helper, "geontm:glass_quartz", Tool.PLAIN, "geontm:glass_quartz", 1);
        // no silk touch in 1.7: ore_oil (explicit), stalagmites (not a normal block), layers, waste leaves
        exactly(helper, "geontm:ore_oil", Tool.SILK, "geontm:oil_tar_crude", 1);
        exactly(helper, "geontm:stalagmite_sulfur", Tool.SILK, "geontm:sulfur", 1);
        for (String id : new String[]{"geontm:foam_layer", "geontm:sand_boron_layer", "geontm:leaves_layer", "geontm:oil_spill",
                "geontm:waste_leaves"}) {
            nothing(helper, id, Tool.SILK);
            nothing(helper, id, Tool.PLAIN);
        }
        helper.succeed();
    }

    /** 1.7 breaks a stalagmite with drops when its floor goes. */
    public static void stalagmitePopDrops(GameTestHelper helper) {
        BlockPos floor = new BlockPos(1, 1, 1);
        BlockPos spike = floor.above();
        helper.setBlock(floor, Blocks.STONE);
        helper.setBlock(spike, block("geontm:stalagmite_sulfur"));
        helper.setBlock(floor, Blocks.AIR);
        helper.runAfterDelay(2, () -> {
            helper.assertTrue(helper.getLevel().getBlockState(helper.absolutePos(spike)).isAir(), "the stalagmite pops with its floor");
            List<ItemEntity> items = helper.getLevel().getEntitiesOfClass(
                    ItemEntity.class, new AABB(helper.absolutePos(spike)).inflate(3));
            helper.assertTrue(items.stream().anyMatch(i -> i.getItem().is(item("geontm:sulfur"))), "it drops its sulfur: " + items);
            helper.succeed();
        });
    }

    /** Drops with their own 1.7 logic: the meteorite treasure pool, the boiler's scraps, the self-dropping blocks. */
    public static void specialDrops(GameTestHelper helper) {
        BlockState treasure = block("geontm:block_meteor_treasure").defaultBlockState();
        for (int i = 0; i < 200; i++) {
            List<ItemStack> drops = drops(helper, treasure, Tool.FORTUNE);
            helper.assertTrue(drops.size() >= 1 && drops.size() <= 3 && drops.stream().noneMatch(ItemStack::isEmpty)
                            && drops.stream().noneMatch(s -> s.is(treasure.getBlock().asItem())),
                    "block_meteor_treasure must draw 1 + rand(3) stacks from POOL_METEORITE_TREASURE, got " + drops);
        }
        sample(helper, "geontm:machine_boiler_off", Tool.PLAIN, Set.of("geontm:scraps"), "geontm:scraps", 3, 6, 4.5D);
        for (String id : new String[]{"geontm:fusion_torus", "geontm:stone_depth", "geontm:stone_depth_nether", "geontm:depth_brick",
                "geontm:depth_dnt"}) {
            exactly(helper, id, Tool.PLAIN, id, 1);
        }
        for (String id : new String[]{"geontm:turret_howard_damaged", "geontm:machine_uf6_tank", "geontm:machine_puf6_tank",
                "geontm:block_meteor_molten", "geontm:glyphid_base"}) {
            nothing(helper, id, Tool.PLAIN);
        }
        helper.succeed();
    }

    /**
     * Every row of the baked pools names a registered item, its components decode and its fluid fills, so no entry
     * is silently dropped (a dropped row shrinks the pool and its total weight); spot checks of the flattened 1.7
     * identities, including the component-carried ones on every node.
     */
    public static void itemPools(GameTestHelper helper) {
        // the pools as loaded from data/geontm/item_pool (the 1.7 tables ntm-next baked, split into one file per pool)
        Map<String, JsonArray> root = ItemPool.rawPools();
        helper.assertTrue(root.size() >= 50, "expected HBM's 50 item pools, loaded " + root.size());
        var registries = helper.getLevel().registryAccess();
        int rows = 0;
        for (Map.Entry<String, JsonArray> pool : root.entrySet()) {
            JsonArray body = pool.getValue();
            List<ItemPool.Entry> read = ItemPool.read(registries, pool.getKey());
            helper.assertTrue(read.size() == body.size(),
                    pool.getKey() + ": " + body.size() + " rows but only " + read.size() + " resolve");
            for (int i = 0; i < body.size(); i++) {
                JsonObject row = body.get(i).getAsJsonObject();
                if (row.has("components")) {
                    ItemStack bare = new ItemStack(item(row.get("item").getAsString()));
                    helper.assertTrue(ItemStacks.withComponents(bare, row.getAsJsonObject("components"),
                                    RegistryOps.create(JsonOps.INSTANCE, registries)).isPresent(),
                            pool.getKey() + "[" + i + "] components do not decode: " + row);
                }
                rows++;
            }
        }
        helper.assertTrue(root.containsKey(ItemPools.POOL_PILE_SUPPLIES) && root.get(ItemPools.POOL_PILE_SUPPLIES).size() == 7,
                "POOL_PILE_SUPPLIES must be baked with its seven 1.7 rows");

        Map<String, List<ItemPool.Entry>> pools = new HashMap<>();
        Function<String, List<ItemPool.Entry>> get = name -> pools.computeIfAbsent(name, n -> ItemPool.read(registries, n));
        helper.assertTrue(get.apply(ItemPools.POOL_AMMO).get(0).template().is(item("geontm:ammo_standard_m357_sp")),
                "POOL_AMMO[0] is 1.7 ammo_standard M357_SP");
        ItemStack deuterium = get.apply("POOL_SPACESHIP").get(3).template();
        helper.assertTrue(ItemStacks.sameItemAndData(deuterium, ItemCell.getFullCell(Fluids.DEUTERIUM)),
                "POOL_SPACESHIP[3] is a full deuterium cell, got " + deuterium);
        ItemStack canister = get.apply(ItemPools.POOL_SUPPLIES).get(3).template();
        helper.assertTrue(canister.is(item("geontm:canister_full"))
                        && ItemFluidTank.getType(canister) == Fluids.DIESEL,
                "POOL_SUPPLIES[3] is a diesel canister, got " + canister);
        ItemStack torch = get.apply("POOL_VAULT_LAB").get(0).template();
        helper.assertTrue(torch.getItem() instanceof ItemBlowtorch blowtorch && blowtorch.getFill(torch) == 0,
                "POOL_VAULT_LAB[0] is 1.7 ItemBlowtorch.getEmptyTool, got " + torch);
        ItemStack book = get.apply("POOL_OFFICE_TRASH").get(11).template();
        helper.assertTrue(book.is(item("geontm:book_guide_starter")),
                "POOL_OFFICE_TRASH[11] is book_guide meta 3, book_guide_starter here, got " + book);
        ItemStack ammoBox = get.apply(ItemPools.POOL_PILE_SUPPLIES).get(6).template();
        helper.assertTrue(ammoBox.is(item("geontm:ammo_container")), "POOL_PILE_SUPPLIES[6] is the ammo container");
        helper.assertTrue(rows > 400, "expected the full baked pool set, read " + rows + " rows");
        helper.succeed();
    }

    /** The thirteen 1.7 ChestGenHooks additions appear in the matching vanilla chest tables on this loader. */
    public static void chestInjection(GameTestHelper helper) {
        String[][] expected = {
                {"minecraft:chests/village/village_weaponsmith", "geontm:armor_polish", "geontm:bathwater"},
                {"minecraft:chests/abandoned_mineshaft", "geontm:bathwater", "geontm:serum", "geontm:no9", "geontm:key_red_cracked"},
                {"minecraft:chests/simple_dungeon", "geontm:heart_piece", "geontm:key_red_cracked", "geontm:scrumpy"},
                {"minecraft:chests/desert_pyramid", "geontm:heart_piece", "geontm:scrumpy"},
                {"minecraft:chests/jungle_temple", "geontm:heart_piece"},
                {"minecraft:chests/spawn_bonus_chest", "geontm:no9"},
        };
        Vec3 origin = Vec3.atCenterOf(helper.absolutePos(new BlockPos(1, 1, 1)));
        for (String[] table : expected) {
            Map<Item, Integer> seen = new HashMap<>();
            for (int i = 0; i < 600; i++) {
                for (ItemStack stack : ChestLoot.roll(helper.getLevel(), table[0], origin)) {
                    seen.merge(stack.getItem(), stack.getCount(), Integer::sum);
                }
            }
            for (int i = 1; i < table.length; i++) {
                helper.assertTrue(seen.getOrDefault(item(table[i]), 0) > 0,
                        table[0] + " never rolled " + table[i] + " in 600 chests; HBM's 1.7 chest injection is missing");
            }
        }
        helper.succeed();
    }
}
