package fr.geoventure.geontm.tileentity.machine.rbmk;

import java.util.Map;
import fr.geoventure.geocore.data.Tags;

import fr.geoventure.geontm.api.fluidmk2.IFluidProviderMK2;
import fr.geoventure.geontm.entity.projectile.EntityRBMKDebris;
import fr.geoventure.geontm.handler.neutron.NeutronStream;
import fr.geoventure.geontm.handler.neutron.RBMKNeutronHandler.RBMKType;
import fr.geoventure.geontm.inventory.container.MenuRBMKOutgasser;
import fr.geoventure.geontm.inventory.fluid.FluidStack;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.inventory.fluid.tank.FluidTankNTM;
import fr.geoventure.geontm.inventory.recipes.OutgasserRecipes;
import fr.geoventure.geontm.inventory.recipes.OutgasserRecipes.OutgasserRecipe;
import fr.geoventure.geontm.tileentity.ModRBMKBlockEntities;
import fr.geoventure.geocore.data.DataIn;
import fr.geoventure.geocore.data.DataOut;
import fr.geoventure.geocore.world.ItemStacks;
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
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import fr.geoventure.geontm.module.portmanager.ModulePortManFluidAdaptive;

/** Flux-fed irradiation channel. Progress is flux, rather than elapsed ticks. */
public class BlockEntityRBMKOutgasser extends BlockEntityRBMKBase implements IRBMKFluxReceiver, IFluidProviderMK2, MenuProvider, WorldlyContainer {
    public static final int SLOT_INPUT = 0, SLOT_OUTPUT = 1;
    public final FluidTankNTM gas = new FluidTankNTM(Fluids.TRITIUM, 64_000);
    public double progress, lastUsedFlux;
    public int duration = OutgasserRecipes.DURATION;

    public BlockEntityRBMKOutgasser(BlockPos pos, BlockState state) { super(ModRBMKBlockEntities.RBMK_OUTGASSER.get(), pos, state, 2); ports = new ModulePortManFluidAdaptive(this).setOutputTanks(gas); }
    @Override public void tickServer() { ports.update(getLoaderPorts()); if (!canProcess()) { progress = 0D; lastUsedFlux = 0D; } super.tickServer(); }
    @Override public void receiveFlux(NeutronStream stream) {
        if (!canProcess()) return;
        lastUsedFlux = stream.fluxQuantity * Math.min(1D - stream.fluxRatio * .8D, 1D) * RBMKDials.getOutgasserMod(level);
        progress += lastUsedFlux;
        if (progress > duration) { process(); markChanged(); }
    }
    public boolean canProcess() {
        ItemStack input = inventory.getStackInSlot(SLOT_INPUT);
        if (!(OutgasserRecipes.INSTANCE.getRecipe(input) instanceof OutgasserRecipe recipe)) return false;
        FluidStack fluid = recipe.fluidOutput();
        if (fluid != null) {
            if (gas.getFill() > 0 && gas.getTankType() != fluid.type) return false;
            gas.setTankType(fluid.type);
            if (gas.getFill() + fluid.fill > gas.getMaxFill()) return false;
        }
        ItemStack output = recipe.solidOutput(), slot = inventory.getStackInSlot(SLOT_OUTPUT);
        return output.isEmpty() || slot.isEmpty() || ItemStacks.sameItemAndData(slot, output) && slot.getCount() + output.getCount() <= slot.getMaxStackSize();
    }
    private void process() {
        if (!(OutgasserRecipes.INSTANCE.getRecipe(inventory.getStackInSlot(SLOT_INPUT)) instanceof OutgasserRecipe recipe)) return;
        ItemStack input = inventory.getStackInSlot(SLOT_INPUT); input.shrink(1);
        if (input.isEmpty()) inventory.setStackInSlot(SLOT_INPUT, ItemStack.EMPTY);
        progress = 0D;
        FluidStack fluid = recipe.fluidOutput(); if (fluid != null) gas.setFill(gas.getFill() + fluid.fill);
        ItemStack output = recipe.solidOutput();
        if (!output.isEmpty()) {
            ItemStack slot = inventory.getStackInSlot(SLOT_OUTPUT);
            if (slot.isEmpty()) inventory.setStackInSlot(SLOT_OUTPUT, output.copy()); else slot.grow(output.getCount());
        }
    }
    @Override public void onMelt(int reduce) { for (int i = 0, count = 4 + level.getRandom().nextInt(2); i < count; i++) spawnDebris(EntityRBMKDebris.DebrisType.BLANK); super.onMelt(reduce); }
    @Override public RBMKType getRBMKType() { return RBMKType.OUTGASSER; }
    @Override public RBMKColumnType getConsoleType() { return RBMKColumnType.OUTGASSER; }
    @Override public RBMKColumn getConsoleData() { RBMKColumn.OutgasserColumn data = (RBMKColumn.OutgasserColumn) super.getConsoleData(); data.gas = gas.getFill(); data.maxGas = gas.getMaxFill(); data.progress = progress; data.maxProgress = duration; data.usedFlux = lastUsedFlux; return data; }
    @Override public FluidTankNTM[] getAllTanks() { return new FluidTankNTM[]{gas}; }
    @Override public boolean isItemValidForSlot(int slot, ItemStack stack) { return slot == SLOT_INPUT && OutgasserRecipes.INSTANCE.getRecipe(stack) != null; }
    @Override public int[] getSlotsForFace(Direction side) { return new int[]{SLOT_INPUT, SLOT_OUTPUT}; }
    @Override public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) { return isItemValidForSlot(slot, stack); }
    @Override public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) { return slot == SLOT_OUTPUT; }
    @Override public long getFluidAvailable(FluidType type, int pressure) { return gas.provides(type) && pressure == gas.getPressure() ? gas.getFill() : 0L; }
    @Override public void useUpFluid(FluidType type, int pressure, long amount) { if (gas.provides(type) && pressure == gas.getPressure()) { gas.setFill(gas.getFill() - (int) Math.min(amount, gas.getFill())); markChanged(); } }
    @Override protected void saveData(DataOut output) { super.saveData(output); output.putDouble("progress", progress); gas.writeToNBT(output, "gas"); }
    @Override protected void loadData(DataIn input) { super.loadData(input); progress = input.getDoubleOr("progress", progress); gas.readFromNBT(input, "gas"); }
    @Override public void serialize(FriendlyByteBuf buffer) { super.serialize(buffer); gas.serialize(buffer); buffer.writeDouble(progress); }
    @Override public void deserialize(FriendlyByteBuf buffer) { super.deserialize(buffer); gas.deserialize(buffer); progress = buffer.readDouble(); }
    @Override public Component getDisplayName() { return Component.translatable("container.rbmkOutgasser"); }
    @Override public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) { return new MenuRBMKOutgasser(id, inventory, this); }
    @Override protected void getDiagData(Map<String, String> data) { super.getDiagData(data); data.put("progress", String.valueOf(progress)); diagTank(data, "gas", gas); }
}
