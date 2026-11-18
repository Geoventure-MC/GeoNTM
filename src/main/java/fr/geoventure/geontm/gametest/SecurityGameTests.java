package fr.geoventure.geontm.gametest;

import fr.geoventure.geontm.items.ModItems;
import fr.geoventure.geontm.items.tool.ItemKeyPin;
import fr.geoventure.geontm.inventory.container.ContainerMachineKeyForge;
import fr.geoventure.geontm.blocks.ModSecurityBlocks;
import fr.geoventure.geontm.items.ModSecurityItems;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineKeyForge;
import fr.geoventure.geocore.gametest.GameTestHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

/** Behavioural contracts for the locksmith table and the pin-carrying items. */
public final class SecurityGameTests {

    private SecurityGameTests() {}

    /** Slot 0 pastes onto slot 1 every tick; slot 2 is randomised to 100..999; the counterfeit key refuses both. */
    public static void keyForge(GameTestHelper helper) {
        BlockPos pos = new BlockPos(1, 1, 1);
        helper.setBlock(pos, ModSecurityBlocks.machine_keyforge.get().defaultBlockState());
        TileEntityMachineKeyForge forge = GameTestHelpers.blockEntity(helper, pos, TileEntityMachineKeyForge.class);

        ItemStack source = new ItemStack(ModItems.key.get());
        ItemKeyPin.setPins(source, 123);
        ItemStack copy = new ItemStack(ModSecurityItems.padlock.get());
        ItemStack fake = new ItemStack(ModItems.key_fake.get());
        ItemKeyPin.setPins(fake, 7);
        forge.inventory.setStackInSlot(TileEntityMachineKeyForge.SLOT_SOURCE, source);
        forge.inventory.setStackInSlot(TileEntityMachineKeyForge.SLOT_COPY, copy);
        forge.inventory.setStackInSlot(TileEntityMachineKeyForge.SLOT_RANDOM, fake);

        helper.runAfterDelay(5, () -> {
            helper.assertTrue(ItemKeyPin.pins(forge.inventory.getStackInSlot(TileEntityMachineKeyForge.SLOT_COPY)) == 123,
                    "key forge did not paste the source pins onto the padlock");
            helper.assertTrue(ItemKeyPin.pins(forge.inventory.getStackInSlot(TileEntityMachineKeyForge.SLOT_RANDOM)) == 7,
                    "key forge re-pinned a counterfeit key");

            ItemStack blank = new ItemStack(ModItems.key.get());
            forge.inventory.setStackInSlot(TileEntityMachineKeyForge.SLOT_RANDOM, blank);
            helper.runAfterDelay(5, () -> {
                int pins = ItemKeyPin.pins(forge.inventory.getStackInSlot(TileEntityMachineKeyForge.SLOT_RANDOM));
                helper.assertTrue(pins >= 100 && pins <= 999, "key forge random pins out of the old 100..999 range: " + pins);
                helper.succeed();
            });
        });
    }

    /** The old container layout: three machine slots at (44,36) (80,36) (116,36), player rows from y 104, hotbar at 162. */
    public static void keyForgeMenu(GameTestHelper helper) {
        BlockPos pos = new BlockPos(1, 1, 1);
        helper.setBlock(pos, ModSecurityBlocks.machine_keyforge.get().defaultBlockState());
        TileEntityMachineKeyForge forge = GameTestHelpers.blockEntity(helper, pos, TileEntityMachineKeyForge.class);
        var player = GameTestHelpers.mockPlayer(helper, GameType.SURVIVAL);
        ContainerMachineKeyForge menu = new ContainerMachineKeyForge(1, player.getInventory(), forge);
        int[][] expected = {{44, 36}, {80, 36}, {116, 36}};
        for (int i = 0; i < expected.length; i++) {
            var slot = menu.getSlot(i);
            helper.assertTrue(slot.x == expected[i][0] && slot.y == expected[i][1],
                    "key forge slot " + i + " sits at " + slot.x + "," + slot.y);
        }
        helper.assertTrue(menu.getSlot(3).y == 104 && menu.getSlot(30).y == 162 && menu.slots.size() == 39,
                "key forge player inventory rows drifted from the 186-tall layout");
        helper.succeed();
    }
}
