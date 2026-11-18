package fr.geoventure.geontm.tileentity.machine;

import fr.geoventure.geontm.api.data.GeoNtmDataMaps;
import fr.geoventure.geontm.api.environment.Environment;
import fr.geoventure.geontm.blocks.machine.MachineFurnaceBrick;
import fr.geoventure.geontm.items.ItemEnums;
import fr.geoventure.geontm.items.ModIngredientItems;
import fr.geoventure.geontm.util.TickPhase;
import fr.geoventure.geocore.data.Tags;

import fr.geoventure.geontm.inventory.container.ContainerFurnaceBrick;
import fr.geoventure.geontm.tileentity.ModBlockEntities;
import fr.geoventure.geontm.tileentity.TileEntityMachineBase;
import fr.geoventure.geocore.data.DataIn;
import fr.geoventure.geocore.data.DataOut;
import fr.geoventure.geocore.world.Compat;
import fr.geoventure.geocore.world.ItemStacks;
import fr.geoventure.geocore.world.Recipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;


/** The ordinary brick furnace, including its three accumulated ash streams. */
public class TileEntityFurnaceBrick extends TileEntityMachineBase implements MenuProvider, WorldlyContainer {

    private static final int[] TOP = {0};
    private static final int[] BOTTOM = {2, 3};
    private static final int[] SIDES = {1};
    public int burnTime;
    public int maxBurnTime;
    public int progress;
    public int ashLevelWood;
    public int ashLevelCoal;
    public int ashLevelMisc;

    public TileEntityFurnaceBrick(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FURNACE_BRICK.get(), pos, state, 4);
    }

    @Override
    public String getDefaultName() {
        return "container.furnaceBrick";
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TileEntityFurnaceBrick furnace) {
        boolean wasBurning = furnace.burnTime > 0;
        // geontm-space breatheAir: 1 mB of oxygen every 5 ticks while burning; without air no fuel is lit and nothing
        // smelts, while the burn time still runs down (fr.geoventure.geontm.api.environment.Environment, always air on plain GEONTM)
        boolean canOperate = Environment.breatheAir(level, pos,
                wasBurning && TickPhase.every(level, pos, 5L) ? 1 : 0);
        boolean dirty = false;

        if (furnace.burnTime > 0) furnace.burnTime--;

        ItemStack input = furnace.inventory.getStackInSlot(0);
        ItemStack fuel = furnace.inventory.getStackInSlot(1);
        if (furnace.burnTime != 0 || (!fuel.isEmpty() && !input.isEmpty())) {
            if (canOperate && furnace.burnTime == 0 && furnace.canSmelt(level)) {
                int burn = Compat.burnDuration(level, fuel);
                if (burn > 0) {
                    furnace.maxBurnTime = furnace.burnTime = burn;
                    ItemEnums.EnumAshType type = TileEntityFireboxBase.getAshFromFuel(fuel);
                    if (type == ItemEnums.EnumAshType.WOOD) furnace.ashLevelWood += burn;
                    if (type == ItemEnums.EnumAshType.COAL) furnace.ashLevelCoal += burn;
                    if (type == ItemEnums.EnumAshType.MISC) furnace.ashLevelMisc += burn;

                    int threshold = 2_000;
                    if (furnace.processAsh(furnace.ashLevelWood, ItemEnums.EnumAshType.WOOD, threshold)) furnace.ashLevelWood -= threshold;
                    if (furnace.processAsh(furnace.ashLevelCoal, ItemEnums.EnumAshType.COAL, threshold)) furnace.ashLevelCoal -= threshold;
                    if (furnace.processAsh(furnace.ashLevelMisc, ItemEnums.EnumAshType.MISC, threshold)) furnace.ashLevelMisc -= threshold;

                    ItemStack remainder = Compat.craftingRemainder(fuel);
                    fuel.shrink(1);
                    if (fuel.isEmpty()) furnace.inventory.setStackInSlot(1, remainder);
                    dirty = true;
                }
            }

            if (canOperate && furnace.burnTime > 0 && furnace.canSmelt(level)) {
                furnace.progress += furnace.getBurnSpeed();
                if (furnace.progress >= 200) {
                    furnace.progress = 0;
                    furnace.smeltItem(level);
                    dirty = true;
                }
            } else {
                furnace.progress = 0;
            }
        }

        boolean burning = furnace.burnTime > 0;
        if (wasBurning != burning) {
            if (state.hasProperty(MachineFurnaceBrick.LIT)
                    && state.getValue(MachineFurnaceBrick.LIT) != burning) {
                level.setBlock(pos, state.setValue(MachineFurnaceBrick.LIT, burning), 3);
            }
            dirty = true;
        }
        if (dirty) furnace.setChanged();
        furnace.networkPackNT();
    }

    public int getBurnSpeed() {
        // per-input speed: data/geontm/value_maps/item/brick_furnace_speed.json (1.7 values: clay, fireclay, netherrack 4; cobble, sand 2)
        Integer speed = GeoNtmDataMaps.BRICK_FURNACE_SPEED.get(inventory.getStackInSlot(0).getItem());
        if (speed != null) return speed;
        return inventory.getStackInSlot(0).is(ItemTags.LOGS) ? 2 : 1;
    }

    public boolean canSmelt(Level level) {
        if (inventory.getStackInSlot(0).isEmpty()) return false;
        ItemStack result = Recipes.smeltingResult(level, inventory.getStackInSlot(0)).orElse(ItemStack.EMPTY);
        if (result.isEmpty()) return false;
        ItemStack output = inventory.getStackInSlot(2);
        if (output.isEmpty()) return true;
        if (!ItemStacks.sameItemAndData(output, result)) return false;
        return output.getCount() + result.getCount() <= 64
                && output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    public void smeltItem(Level level) {
        if (!canSmelt(level)) return;
        ItemStack result = Recipes.smeltingResult(level, inventory.getStackInSlot(0)).orElse(ItemStack.EMPTY);
        ItemStack output = inventory.getStackInSlot(2);
        if (output.isEmpty()) inventory.setStackInSlot(2, result.copy());
        else output.grow(result.getCount());
        inventory.getStackInSlot(0).shrink(1);
    }

    protected boolean processAsh(int level, ItemEnums.EnumAshType type, int threshold) {
        if (level < threshold) return false;
        ItemStack ash = new ItemStack(ModIngredientItems.ash(type));
        ItemStack output = inventory.getStackInSlot(3);
        if (output.isEmpty()) {
            inventory.setStackInSlot(3, ash);
            return true;
        }
        if (output.is(ash.getItem()) && output.getCount() < output.getMaxStackSize()) {
            output.grow(1);
            return true;
        }
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return slot == 0 || slot == 1 && level != null && Compat.burnDuration(level, stack) > 0;
    }

    @Override public int[] getSlotsForFace(Direction side) {
        return side == Direction.DOWN ? BOTTOM : side == Direction.UP ? TOP : SIDES;
    }
    @Override public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) { return isItemValidForSlot(slot, stack); }
    @Override public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) { return slot >= 2; }

    @Override public Component getDisplayName() { return Component.translatable("container.furnaceBrick"); }
    @Override public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ContainerFurnaceBrick(id, inventory, this);
    }

    @Override
    public void serialize(FriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeInt(burnTime);
        buf.writeInt(maxBurnTime);
        buf.writeInt(progress);
        buf.writeInt(ashLevelWood);
        buf.writeInt(ashLevelCoal);
        buf.writeInt(ashLevelMisc);
    }

    @Override
    public void deserialize(FriendlyByteBuf buf) {
        super.deserialize(buf);
        burnTime = buf.readInt();
        maxBurnTime = buf.readInt();
        progress = buf.readInt();
        ashLevelWood = buf.readInt();
        ashLevelCoal = buf.readInt();
        ashLevelMisc = buf.readInt();
    }

    @Override
    protected void saveData(DataOut output) {
        super.saveData(output);
        output.putInt("burnTime", burnTime);
        output.putInt("maxBurn", maxBurnTime);
        output.putInt("progress", progress);
        output.putInt("ashWood", ashLevelWood);
        output.putInt("ashCoal", ashLevelCoal);
        output.putInt("ashMisc", ashLevelMisc);
    }

    @Override
    protected void loadData(DataIn input) {
        super.loadData(input);
        burnTime = input.getIntOr("burnTime", 0);
        maxBurnTime = input.getIntOr("maxBurn", 0);
        progress = input.getIntOr("progress", 0);
        ashLevelWood = input.getIntOr("ashWood", 0);
        ashLevelCoal = input.getIntOr("ashCoal", 0);
        ashLevelMisc = input.getIntOr("ashMisc", 0);
    }
}
