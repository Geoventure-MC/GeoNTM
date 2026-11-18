package fr.geoventure.geontm.tileentity.machine.oil;

import fr.geoventure.geontm.api.energymk2.IEnergyReceiverMK2;
import fr.geoventure.geontm.api.fluidmk2.IFluidStandardTransceiverMK2;
import fr.geoventure.geontm.inventory.container.ContainerMachineCatalyticReformer;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.inventory.fluid.tank.FluidTankNTM;
import fr.geoventure.geontm.inventory.recipes.ReformingRecipes;
import fr.geoventure.geontm.items.ModItems;
import fr.geoventure.geontm.items.machine.IItemFluidId;
import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geontm.lib.Library;
import fr.geoventure.geontm.tileentity.MachineCore;
import fr.geoventure.geontm.tileentity.ModBlockEntities;
import fr.geoventure.geontm.tileentity.TileEntityMachineBase;
import fr.geoventure.geontm.tileentity.machine.MachineFluidContainerIO;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
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
import fr.geoventure.geontm.tileentity.TilePort.PortDef;

/**
 * Catalytic Reformer: 1:1 port of the 1.12.2 {@code TileEntityMachineCatalyticReformer}. 11 slots
 * (0 battery, 1 to 2 input container load, 3 to 4/5 to 6/7 to 8 three output-unload pairs, 9 fluid identifier,
 * 10 catalyst). Tanks: {@code [0] NAPHTHA 64k} input + REFORMATE/PETROLEUM/HYDROGEN 24k outputs. Each op
 * needs {@code power ≥ 20k}, {@code tank0 ≥ 100} and a {@code catalytic_converter} in slot 10; drains
 * 100 mB + 20k HE. maxPower 1M.
 *
 * <p>Not ported: IPersistentNBT/IFluidCopiable.
 */
public class TileEntityMachineCatalyticReformer extends TileEntityMachineBase
        implements MenuProvider, IEnergyReceiverMK2, IFluidStandardTransceiverMK2, WorldlyContainer {

    public static final long maxPower = 1_000_000;

    public long power = 0;
    public FluidTankNTM[] tanks;

    public TileEntityMachineCatalyticReformer(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MACHINE_CATALYTIC_REFORMER.get(), pos, state, 11);
        tanks = new FluidTankNTM[4];
        tanks[0] = new FluidTankNTM(Fluids.NAPHTHA, 64_000);
        tanks[1] = new FluidTankNTM(Fluids.REFORMATE, 24_000);
        tanks[2] = new FluidTankNTM(Fluids.PETROLEUM, 24_000);
        tanks[3] = new FluidTankNTM(Fluids.HYDROGEN, 24_000);
    }

    @Override public String getDefaultName() { return "container.catalyticReformer"; }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TileEntityMachineCatalyticReformer te) {
        te.setupAllPorts(te.getPorts());
        te.updatePortPIFIFO();

        te.power = Library.chargeTEFromItems(te.inventory, 0, te.power, maxPower);
        if (te.tanks[0].setType(te.inventory, 9, 9)) te.setChanged();
        MachineFluidContainerIO.load(te.tanks[0], te.inventory, 1, 2);

        te.reform();

        MachineFluidContainerIO.unload(te.tanks[1], te.inventory, 3, 4);
        MachineFluidContainerIO.unload(te.tanks[2], te.inventory, 5, 6);
        MachineFluidContainerIO.unload(te.tanks[3], te.inventory, 7, 8);

        te.networkPackNT();
    }

    protected PortDef[] cachedPorts;

    public PortDef[] getPorts() {
        if (cachedPorts == null) {
            int meta = MachineCore.facingOrdinal(getBlockState(), 2);
            ForgeDirection dir = ForgeDirection.getOrientation(meta);
            ForgeDirection rot = dir.getRotation(ForgeDirection.UP);
            int x = worldPosition.getX(), y = worldPosition.getY(), z = worldPosition.getZ();
            cachedPorts = new PortDef[] {
                    PortDef.make(x + dir.offsetX * 1 + rot.offsetX, y, z + dir.offsetZ * 1 + rot.offsetZ, dir),
                    PortDef.make(x + dir.offsetX * 1 - rot.offsetX, y, z + dir.offsetZ * 1 - rot.offsetZ, dir),
                    PortDef.make(x - dir.offsetX * 1 + rot.offsetX, y, z - dir.offsetZ * 1 + rot.offsetZ, dir.getOpposite()),
                    PortDef.make(x - dir.offsetX * 1 - rot.offsetX, y, z - dir.offsetZ * 1 - rot.offsetZ, dir.getOpposite()),
                    PortDef.make(x + rot.offsetX * 2, y, z + rot.offsetZ * 2, rot),
                    PortDef.make(x - rot.offsetX * 2, y, z - rot.offsetZ * 2, rot.getOpposite()),
            };
        }
        return cachedPorts;
    }

    private void reform() {
        ReformingRecipes.ReformingRecipe out = ReformingRecipes.getOutput(tanks[0].getTankType());
        if (out == null) {
            tanks[1].setTankType(Fluids.NONE);
            tanks[2].setTankType(Fluids.NONE);
            tanks[3].setTankType(Fluids.NONE);
            return;
        }

        tanks[1].setTankType(out.out1().type);
        tanks[2].setTankType(out.out2().type);
        tanks[3].setTankType(out.out3().type);

        if (power < 20_000 || tanks[0].getFill() < ReformingRecipes.FILL_PER_OP) return;
        if (inventory.getStackInSlot(10).isEmpty() || inventory.getStackInSlot(10).getItem() != ModItems.catalytic_converter.get()) return;
        if (tanks[1].getFill() + out.out1().fill > tanks[1].getMaxFill()) return;
        if (tanks[2].getFill() + out.out2().fill > tanks[2].getMaxFill()) return;
        if (tanks[3].getFill() + out.out3().fill > tanks[3].getMaxFill()) return;

        tanks[0].setFill(tanks[0].getFill() - ReformingRecipes.FILL_PER_OP);
        tanks[1].setFill(tanks[1].getFill() + out.out1().fill);
        tanks[2].setFill(tanks[2].getFill() + out.out2().fill);
        tanks[3].setFill(tanks[3].getFill() + out.out3().fill);
        power -= 20_000;
    }

    public long getPowerScaled(long i) { return (power * i) / maxPower; }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (slot == 0) return Library.isBattery(stack);
        if (slot == 9) return stack.getItem() instanceof IItemFluidId;
        if (slot == 10) return stack.getItem() == ModItems.catalytic_converter.get();
        return true;
    }

    @Override public int[] getSlotsForFace(Direction side) { return new int[0]; }
    @Override public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) { return false; }
    @Override public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) { return false; }

    @Override public long getPower() { return power; }
    @Override public void setPower(long i) { power = i; }
    @Override public long getMaxPower() { return maxPower; }

    @Override public FluidTankNTM[] getSendingTanks() { return new FluidTankNTM[]{tanks[1], tanks[2], tanks[3]}; }
    @Override public FluidTankNTM[] getReceivingTanks() { return new FluidTankNTM[]{tanks[0]}; }
    @Override public FluidTankNTM[] getAllTanks() { return tanks; }

    @Override
    public Component getDisplayName() { return Component.translatable("container.catalyticReformer"); }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ContainerMachineCatalyticReformer(containerId, playerInventory, this);
    }

    @Override
    public void serialize(FriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(this.power);
        for (FluidTankNTM tank : tanks) tank.serialize(buf);
    }

    @Override
    public void deserialize(FriendlyByteBuf buf) {
        super.deserialize(buf);
        this.power = buf.readLong();
        for (FluidTankNTM tank : tanks) tank.deserialize(buf);
    }

    @Override
    protected void saveData(DataOut output) {
        super.saveData(output);
        output.putLong("power", power);
        tanks[0].writeToNBT(output, "input");
        tanks[1].writeToNBT(output, "o1");
        tanks[2].writeToNBT(output, "o2");
        tanks[3].writeToNBT(output, "o3");
    }

    @Override
    protected void loadData(DataIn input) {
        super.loadData(input);
        power = input.getLongOr("power", 0L);
        tanks[0].readFromNBT(input, "input");
        tanks[1].readFromNBT(input, "o1");
        tanks[2].readFromNBT(input, "o2");
        tanks[3].readFromNBT(input, "o3");
    }
}
