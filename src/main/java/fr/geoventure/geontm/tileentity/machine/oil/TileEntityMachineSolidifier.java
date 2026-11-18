package fr.geoventure.geontm.tileentity.machine.oil;

import fr.geoventure.geontm.api.energymk2.IEnergyReceiverMK2;
import fr.geoventure.geontm.api.fluidmk2.IFluidStandardReceiverMK2;
import fr.geoventure.geontm.blocks.machine.ModMachineBlocks;
import fr.geoventure.geontm.inventory.UpgradeManagerNT;
import fr.geoventure.geontm.inventory.container.ContainerSolidifier;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.inventory.fluid.tank.FluidTankNTM;
import fr.geoventure.geontm.inventory.recipes.SolidificationRecipes;
import fr.geoventure.geontm.items.machine.IItemFluidId;
import fr.geoventure.geontm.items.machine.ItemMachineUpgrade.UpgradeType;
import fr.geoventure.geontm.lib.DirPos;
import fr.geoventure.geontm.lib.Library;
import fr.geoventure.geontm.tileentity.IUpgradeInfoProvider;
import fr.geoventure.geontm.tileentity.ModBlockEntities;
import fr.geoventure.geontm.tileentity.TileEntityMachineBase;
import fr.geoventure.geocore.world.ItemStacks;
import java.util.HashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import fr.geoventure.geocore.data.DataIn;
import fr.geoventure.geocore.data.DataOut;

import java.util.List;
import fr.geoventure.geontm.tileentity.TilePort.PortDef;
import fr.geoventure.geontm.tileentity.TilePortShapes;

/**
 * Solidifier: 1:1 port of the 1.12.2 {@code TileEntityMachineSolidifier}: turns fluid into items.
 * 5 slots (0 output take-only, 1 battery, 2/3 upgrades, 4 fluid identifier that sets the tank type), a NONE 24k
 * receive tank. Same power/timing math as the liquefactor. Output stack-merge is gated in
 * {@link #canProcess()} before consuming fluid.
 */
public class TileEntityMachineSolidifier extends TileEntityMachineBase
        implements MenuProvider, IEnergyReceiverMK2, IFluidStandardReceiverMK2, IUpgradeInfoProvider, WorldlyContainer {

    public static final long maxPower = 100000;
    public static final int usageBase = 250; // 1.7.10 value
    public static final int processTimeBase = 60;

    public final UpgradeManagerNT upgradeManager = new UpgradeManagerNT(this);
    public long power;
    public int usage;
    public int progress;
    public int processTime;
    public FluidTankNTM tank;

    public TileEntityMachineSolidifier(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MACHINE_SOLIDIFIER.get(), pos, state, 5);
        tank = new FluidTankNTM(Fluids.NONE, 24_000);
    }

    @Override
    public String getDefaultName() {
        return "container.machineSolidifier";
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TileEntityMachineSolidifier te) {
        te.setupAllPorts(te.getPorts());
        te.updatePortPIFIFO();

        te.power = Library.chargeTEFromItems(te.inventory, 1, te.power, maxPower);
        if (te.tank.setType(te.inventory, 4, 4)) te.setChanged();

        te.upgradeManager.checkSlots(te.inventory, 2, 3);
        int speed = Math.min(te.upgradeManager.getLevel(UpgradeType.SPEED), 3);
        int pow = Math.min(te.upgradeManager.getLevel(UpgradeType.POWER), 3);

        te.processTime = processTimeBase - (processTimeBase / 4) * speed;
        te.usage = (usageBase + (usageBase * speed)) / (pow + 1);

        if (te.canProcess()) te.process();
        else te.progress = 0;

        te.networkPackNT();
    }

    protected PortDef[] cachedPorts;
    public PortDef[] getPorts() { if (cachedPorts == null) cachedPorts = TilePortShapes.liquefactor(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()); return cachedPorts; }

    public DirPos[] getConPos() { return PortDef.connections(getPorts()); }

    public boolean canProcess() {
        if (this.power < usage) return false;
        SolidificationRecipes.SolidRecipe out = SolidificationRecipes.getOutput(tank.getTankType());
        if (out == null) return false;
        if (out.amount() > tank.getFill()) return false;

        ItemStack cur = inventory.getStackInSlot(0);
        if (!cur.isEmpty()) {
            if (!ItemStacks.sameItemAndData(cur, out.output())) return false;
            return cur.getCount() + out.output().getCount() <= cur.getMaxStackSize();
        }
        return true;
    }

    public void process() {
        this.power -= usage;
        progress++;

        if (progress >= processTime) {
            SolidificationRecipes.SolidRecipe out = SolidificationRecipes.getOutput(tank.getTankType());
            tank.setFill(tank.getFill() - out.amount());
            ItemStack cur = inventory.getStackInSlot(0);
            if (cur.isEmpty()) {
                inventory.setStackInSlot(0, out.output().copy());
            } else {
                cur.grow(out.output().getCount());
                inventory.setStackInSlot(0, cur);
            }
            progress = 0;
            setChanged();
        }
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (slot == 0) return false; // output, machine-write only
        if (slot == 1) return Library.isBattery(stack);
        if (slot >= 2 && slot <= 3) return Library.isMachineUpgrade(stack);
        if (slot == 4) return stack.getItem() instanceof IItemFluidId;
        return false;
    }

    @Override
    protected int[] getUpgradeSlotRange() {
        return new int[]{2, 3};
    }

    // WorldlyContainer: extract the solidified output

    @Override public int[] getSlotsForFace(Direction side) { return new int[]{0}; }
    @Override public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) { return false; }
    @Override public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) { return slot == 0; }

    @Override public long getPower() { return power; }
    @Override public void setPower(long power) { this.power = power; }
    @Override public long getMaxPower() { return maxPower; }

    @Override public FluidTankNTM[] getReceivingTanks() { return new FluidTankNTM[]{tank}; }
    @Override public FluidTankNTM[] getAllTanks() { return new FluidTankNTM[]{tank}; }

    @Override
    public boolean canProvideInfo(UpgradeType type, int level, boolean extendedInfo) {
        return type == UpgradeType.SPEED || type == UpgradeType.POWER;
    }

    @Override
    public void provideInfo(UpgradeType type, int level, List<Component> info, boolean extendedInfo) {
        info.add(IUpgradeInfoProvider.getStandardLabel(ModMachineBlocks.machine_solidifier.get()));
        if (type == UpgradeType.SPEED) {
            info.add(Component.translatable(KEY_DELAY, "-" + (level * 25) + "%").withStyle(ChatFormatting.GREEN));
            info.add(Component.translatable(KEY_CONSUMPTION, "+" + (level * 100) + "%").withStyle(ChatFormatting.RED));
        }
        if (type == UpgradeType.POWER) {
            info.add(Component.translatable(KEY_CONSUMPTION, "-" + (100 - 100 / (level + 1)) + "%").withStyle(ChatFormatting.GREEN));
        }
    }

    @Override
    public HashMap<UpgradeType, Integer> getValidUpgrades() {
        HashMap<UpgradeType, Integer> upgrades = new HashMap<>();
        upgrades.put(UpgradeType.SPEED, 3);
        upgrades.put(UpgradeType.POWER, 3);
        return upgrades;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.machineSolidifier");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ContainerSolidifier(containerId, playerInventory, this);
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(this.power);
        buf.writeInt(this.progress);
        buf.writeInt(this.usage);
        buf.writeInt(this.processTime);
        tank.serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.power = buf.readLong();
        this.progress = buf.readInt();
        this.usage = buf.readInt();
        this.processTime = buf.readInt();
        tank.deserialize(buf);
    }

    @Override
    protected void saveData(DataOut output) {
        super.saveData(output);
        output.putLong("power", power);
        output.putInt("progress", progress);
        tank.writeToNBT(output, "tank");
    }

    @Override
    protected void loadData(DataIn input) {
        super.loadData(input);
        power = input.getLongOr("power", 0L);
        progress = input.getIntOr("progress", 0);
        tank.readFromNBT(input, "tank");
    }
}
