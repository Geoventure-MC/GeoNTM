package fr.geoventure.geontm.gametest;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.blocks.RawOres;
import fr.geoventure.geontm.config.GeneralConfig;
import fr.geoventure.geontm.hazard.HazardRegistry;
import fr.geoventure.geontm.hazard.HazardSystem;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.inventory.material.MaterialShapes;
import fr.geoventure.geontm.inventory.material.MaterialDistribution;
import fr.geoventure.geontm.inventory.material.Mats;
import fr.geoventure.geontm.inventory.recipes.BlastFurnaceRecipesNT;
import fr.geoventure.geontm.inventory.recipes.CentrifugeRecipes;
import fr.geoventure.geontm.inventory.recipes.CrystallizerRecipes;
import fr.geoventure.geontm.inventory.recipes.CrystallizerRecipes.CrystallizerRecipe;
import fr.geoventure.geontm.inventory.recipes.ShredderRecipes;
import fr.geoventure.geontm.inventory.recipes.loader.GenericRecipe;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.gametest.GameTests;
import fr.geoventure.geocore.world.CompatEnchantments;
import fr.geoventure.geocore.world.Recipes;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

/**
 * ntm-next's raw ores: what each ore drops with geontm.cfg's {@code 1.90_enableRawOres} on and off, and the raw items'
 * smelting and machine rows at two thirds of their ore's yield.
 */
public final class RawOreGameTests {

    private RawOreGameTests() {}

    public static final GameTests.Suite TESTS = GameTests.suite(Constants.MOD_ID);
    public static final GameTests.Function DROPS = TESTS.function("raw_ore_drops", 20, () -> RawOreGameTests::drops);
    public static final GameTests.Function RECIPES = TESTS.function("raw_ore_recipes", 20, () -> RawOreGameTests::recipes);

    /** Ore, raw drop, drop with raw ores off. */
    private static final String[][] ORES = {
            {"geontm:ore_titanium", "geontm:raw_titanium", "geontm:ore_titanium"},
            {"geontm:ore_titanium_deepslate", "geontm:raw_titanium", "geontm:ore_titanium_deepslate"},
            {"geontm:ore_uranium", "geontm:raw_uranium", "geontm:ore_uranium"},
            {"geontm:ore_nether_uranium_scorched", "geontm:raw_uranium", "geontm:ore_nether_uranium_scorched"},
            {"geontm:ore_schrabidium", "geontm:raw_schrabidium", "geontm:ore_schrabidium"},
            {"geontm:ore_gneiss_lithium", "geontm:raw_lithium", "geontm:ore_gneiss_lithium"},
            {"geontm:ore_gneiss_iron", "minecraft:raw_iron", "geontm:ore_gneiss_iron"},
            {"geontm:ore_gneiss_copper", "minecraft:raw_copper", "geontm:ore_gneiss_copper"},
            {"geontm:ore_cobalt", "geontm:raw_cobalt", "geontm:fragment_cobalt"},
    };

    public static void init() {}

    /** Runs {@code body} with raw ore drops off, then restores the option: 1.7's drops for tests that check them. */
    public static void legacyDrops(Runnable body) {
        boolean was = GeneralConfig.enableRawOres;
        GeneralConfig.enableRawOres = false;
        try {
            body.run();
        } finally {
            GeneralConfig.enableRawOres = was;
        }
    }

    private static Item item(String id) {
        return BuiltInRegistries.ITEM.getValue(Ids.parse(id));
    }

    private static List<ItemStack> drops(GameTestHelper helper, String block, ItemStack tool) {
        ServerLevel level = helper.getLevel();
        BlockPos pos = helper.absolutePos(new BlockPos(1, 1, 1));
        Block b = BuiltInRegistries.BLOCK.getOptional(Ids.parse(block)).orElseThrow();
        return Block.getDrops(b.defaultBlockState(), level, pos, null, null, tool);
    }

    private static boolean only(List<ItemStack> drops, String id, int count) {
        return drops.size() == 1 && drops.get(0).is(item(id)) && drops.get(0).getCount() == count;
    }

    public static void drops(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack pick = new ItemStack(Items.DIAMOND_PICKAXE);
        ItemStack silk = new ItemStack(Items.DIAMOND_PICKAXE);
        CompatEnchantments.enchantSilkTouch(level, silk);
        ItemStack fortune = new ItemStack(Items.DIAMOND_PICKAXE);
        CompatEnchantments.enchantFortune(level, fortune, 3);
        boolean was = GeneralConfig.enableRawOres;
        try {
            GeneralConfig.enableRawOres = true;
            for (String[] ore : ORES) {
                List<ItemStack> plain = drops(helper, ore[0], pick);
                helper.assertTrue(only(plain, ore[1], 1), ore[0] + " must drop one " + ore[1] + ", got " + plain);
                List<ItemStack> kept = drops(helper, ore[0], silk);
                helper.assertTrue(only(kept, ore[0], 1), ore[0] + " under silk touch must drop itself, got " + kept);
            }
            // vanilla ore_drops at Fortune III: 1, 1, 2, 3, 4 equally likely, mean 2.2
            int total = 0;
            for (int i = 0; i < 500; i++) for (ItemStack stack : drops(helper, "geontm:ore_titanium", fortune)) total += stack.getCount();
            helper.assertTrue(Math.abs(total / 500D - 2.2D) < 0.3D, "raw titanium at Fortune III averaged " + total / 500D);

            GeneralConfig.enableRawOres = false;
            for (String[] ore : ORES) {
                List<ItemStack> plain = drops(helper, ore[0], pick);
                helper.assertTrue(!plain.isEmpty() && plain.stream().allMatch(s -> s.is(item(ore[2]))),
                        ore[0] + " with raw ores off must drop " + ore[2] + ", got " + plain);
            }
        } finally {
            GeneralConfig.enableRawOres = was;
        }
        helper.succeed();
    }

    public static void recipes(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack smelted = Recipes.smeltingResult(level, new ItemStack(item("geontm:raw_titanium"))).orElse(ItemStack.EMPTY);
        helper.assertTrue(smelted.is(item("geontm:ingot_titanium")) && smelted.getCount() == 1, "raw titanium smelts to " + smelted);
        ItemStack cryolite = Recipes.smeltingResult(level, new ItemStack(item("geontm:raw_aluminium"))).orElse(ItemStack.EMPTY);
        helper.assertTrue(cryolite.is(item("geontm:chunk_ore_cryolite")), "raw aluminium smelts to " + cryolite);

        // centrifuge: the ore row's outputs, each at two thirds
        ItemStack rawTitanium = new ItemStack(item("geontm:raw_titanium"));
        ItemStack[] full = CentrifugeRecipes.getOutput(rawTitanium);
        ItemStack[] ore = CentrifugeRecipes.getOutput(new ItemStack(item("geontm:ore_titanium")));
        helper.assertTrue(full != null && ore != null && full.length == ore.length, "raw titanium has no centrifuge row");
        for (int i = 0; i < full.length; i++) {
            helper.assertTrue(ItemStack.matches(full[i], ore[i]), "raw titanium centrifuge output " + i + " " + full[i] + " vs ore " + ore[i]);
        }
        RandomSource random = RandomSource.create(4815162342L);
        int made = 0;
        for (int i = 0; i < 3000; i++) if (!CentrifugeRecipes.getOutput(rawTitanium, random)[0].isEmpty()) made++;
        helper.assertTrue(Math.abs(made / 3000D - RawOres.SHARE) < 0.03D, "raw titanium first output made " + made + " of 3000");
        ItemStack[] plainOre = CentrifugeRecipes.getOutput(new ItemStack(item("geontm:ore_titanium")), random);
        for (ItemStack stack : plainOre) helper.assertTrue(!stack.isEmpty(), "an ore row lost an output to a roll");

        // crystallizer: 400 ticks, 333 mB, a crystal at two thirds
        CrystallizerRecipe crystal = CrystallizerRecipes.getOutput(new ItemStack(item("geontm:raw_uranium")), Fluids.SULFURIC_ACID);
        helper.assertTrue(crystal != null && crystal.output.is(item("geontm:crystal_uranium")) && crystal.duration == 400
                && crystal.acidAmount == 333 && crystal.chance == RawOres.SHARE, "raw uranium crystallizer row");

        // crucible: two ingots of iron at two thirds is 96, at least one ingot (72)
        List<Mats.MaterialStack> melt = MaterialDistribution.tags().get("c:raw_materials/iron");
        helper.assertTrue(melt != null && melt.get(0).material == Mats.MAT_IRON
                && melt.get(0).amount == Math.round(MaterialShapes.INGOT.quantity * 2 * RawOres.SHARE), "raw iron melt " + melt);
        List<Mats.MaterialStack> cobalt = MaterialDistribution.tags().get("c:raw_materials/cobalt");
        helper.assertTrue(cobalt != null && cobalt.get(0).amount == MaterialShapes.INGOT.quantity, "raw cobalt melt " + cobalt);

        // blast furnace: steel from raw iron, both outputs at two thirds
        GenericRecipe steel = BlastFurnaceRecipesNT.INSTANCE.getRecipe(new ItemStack(Items.RAW_IRON), new ItemStack(Items.SAND));
        helper.assertTrue(steel != null && steel.getChance(0) == RawOres.SHARE && steel.getChance(1) == RawOres.SHARE,
                "raw iron blast furnace row " + (steel == null ? null : steel.name));

        // shredder: two dust, raw aluminium two cryolite like its ore
        ItemStack dust = ShredderRecipes.getShredderResult(rawTitanium);
        helper.assertTrue(dust.is(item("geontm:powder_titanium")) && dust.getCount() == 2, "raw titanium shreds to " + dust);
        ItemStack shredded = ShredderRecipes.getShredderResult(new ItemStack(item("geontm:raw_aluminium")));
        helper.assertTrue(shredded.is(item("geontm:chunk_ore_cryolite")) && shredded.getCount() == 2, "raw aluminium shreds to " + shredded);

        // hazards at ore strength
        double rad = HazardSystem.getHazardLevelFromStack(new ItemStack(item("geontm:raw_uranium")), HazardRegistry.RADIATION);
        helper.assertTrue(Math.abs(rad - HazardRegistry.u * HazardRegistry.ore) < 1e-3, "raw uranium radiation " + rad);
        Map<String, List<Mats.MaterialStack>> tags = MaterialDistribution.tags();
        helper.assertTrue(tags.containsKey("c:raw_materials/aluminium"), "raw aluminium melts");
        helper.succeed();
    }
}
