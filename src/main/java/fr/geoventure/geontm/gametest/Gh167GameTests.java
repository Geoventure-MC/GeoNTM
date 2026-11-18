package fr.geoventure.geontm.gametest;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.blocks.ModDecoCrateBlocks;
import fr.geoventure.geontm.blocks.generic.BlockTrinket;
import fr.geoventure.geontm.tileentity.deco.TileEntityTrinket;
import fr.geoventure.geocore.gametest.GameTestHelpers;
import fr.geoventure.geocore.gametest.GameTests;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

/** GitHub issue 167: 1.7 trinkets drop their figure to a survival player breaking them by hand. */
public final class Gh167GameTests {

    private Gh167GameTests() {}

    public static final GameTests.Suite TESTS = GameTests.suite(Constants.MOD_ID);
    public static final GameTests.Function BY_HAND =
            TESTS.function("gh167_trinkets_drop_when_broken_by_hand", 20, 4, () -> Gh167GameTests::byHand);

    public static void init() {}

    private static void byHand(GameTestHelper helper) {
        List<BlockTrinket> trinkets = List.of(ModDecoCrateBlocks.bobblehead.get(), ModDecoCrateBlocks.plushie.get(),
                ModDecoCrateBlocks.snowglobe.get());
        List<Item> figures = List.of(ModDecoCrateBlocks.bobblehead_items.get(2).get(),
                ModDecoCrateBlocks.plushie_items.get(2).get(), ModDecoCrateBlocks.snowglobe_items.get(2).get());
        ServerPlayer player = GameTestHelpers.mockServerPlayerInLevel(helper);
        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        for (int i = 0; i < trinkets.size(); i++) {
            BlockTrinket block = trinkets.get(i);
            BlockPos rel = new BlockPos(1 + i * 2, 2, 1);
            helper.setBlock(rel, block.defaultBlockState());
            GameTestHelpers.blockEntity(helper, rel, TileEntityTrinket.class).setTypeOrdinal(2);
            BlockPos at = helper.absolutePos(rel);
            player.gameMode.destroyBlock(at);
            Item wanted = figures.get(i);
            boolean dropped = !helper.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(at).inflate(1.5D),
                    e -> e.getItem().is(wanted)).isEmpty();
            helper.assertTrue(helper.getLevel().getBlockState(at).isAir(), block + " broken");
            helper.assertTrue(dropped, block + " drops " + wanted + " to a bare hand");
        }
        helper.succeed();
    }
}
