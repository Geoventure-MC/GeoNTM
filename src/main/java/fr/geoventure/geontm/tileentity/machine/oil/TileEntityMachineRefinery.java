package fr.geoventure.geontm.tileentity.machine.oil;

import fr.geoventure.geocore.world.Explosions;
import fr.geoventure.geontm.interfaces.IRepairable;
import fr.geoventure.geontm.inventory.RecipesCommon;
import fr.geoventure.geontm.items.ModItems;
import fr.geoventure.geontm.particle.helper.GeoNtmEffectNT;
import fr.geoventure.geontm.tileentity.machine.MachineFluidContainerIO;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.data.Tags;

import fr.geoventure.geontm.api.energymk2.IEnergyReceiverMK2;
import fr.geoventure.geontm.handler.pollution.PollutionHandler;
import fr.geoventure.geontm.handler.pollution.PollutionType;
import fr.geoventure.geontm.util.TickPhase;
import fr.geoventure.geontm.api.fluidmk2.IFluidStandardTransceiverMK2;
import fr.geoventure.geontm.inventory.container.ContainerMachineRefinery;
import fr.geoventure.geontm.inventory.fluid.FluidStack;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.inventory.fluid.tank.FluidTankNTM;
import fr.geoventure.geontm.inventory.recipes.RefineryRecipes;
import fr.geoventure.geontm.items.machine.IItemFluidId;
import fr.geoventure.geontm.lib.Library;
import fr.geoventure.geontm.tileentity.ModBlockEntities;
import fr.geoventure.geontm.tileentity.TileEntityMachineBase;
import fr.geoventure.geocore.world.Effects;
import fr.geoventure.geocore.world.ItemStacks;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import fr.geoventure.geocore.data.DataIn;
import fr.geoventure.geocore.data.DataOut;
import fr.geoventure.geontm.sound.AudioWrapper;
import fr.geoventure.geontm.sound.MachineAudio;
import fr.geoventure.geontm.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.AABB;
import fr.geoventure.geontm.tileentity.TilePort.PortDef;
import fr.geoventure.geontm.tileentity.TilePortShapes;

/**
 * Refinery: 1:1 port of the 1.12.2 {@code TileEntityMachineRefinery}: a 3×3×9 tower that fractionates
 * a HOT oil into four products. 13 slots (0 battery, 1 and 2 the input container pair, 3/4, 5/6, 7/8 and 9/10 four
 * output container pairs, 11 sulfur/byproduct out, 12 fluid identifier). Tanks: {@code [0] HOTOIL 64k}
 * input + 4×24k outputs conformed to the recipe. {@link #refine} needs {@code power ≥ 5} and
 * {@code tank0 ≥ 100} and all four outputs to fit; each op drains 100 mB + 5 HE and produces the
 * fractions, and every 10 ops (1.7.10) emits one byproduct item. maxPower 1000.
 *
 * <p>1.7's explosion damage is ported ({@link #hasExploded}: the first explosion that reaches it blows it open, it
 * burns off its tanks until extinguished and is repaired with a blowtorch, eight steel plates and four duct tape).
 * Refining adds {@code SOOT_PER_SECOND * 5} soot a second, as in 1.7.
 * Not ported: the meta&lt;12 self-heal branch and checkTilt.
 */
public class TileEntityMachineRefinery extends TileEntityMachineBase
        implements MenuProvider, IEnergyReceiverMK2, IFluidStandardTransceiverMK2, WorldlyContainer,
        IRepairable {

    /** 1.7 {@code hasExploded}: blown open by an explosion; stops refining and burns off its tanks until repaired. */
    public boolean hasExploded = false;
    /** 1.7 {@code onFire}: always set by the explosion, cleared by foam or CO2. */
    public boolean onFire = false;
    /** 1.7 {@code lastExplosion}: one explosion damages the refinery once. */
    public @org.jspecify.annotations.Nullable Object lastExplosion;

    public static final int maxSulfur = 10; // 1.7.10 (1.12.2 used 100)
    public static final long maxPower = 1000;

    public long power = 0;
    public int sulfur = 0;
    public boolean isOn;
    private AudioWrapper audio;
    private int audioTime;
    public FluidTankNTM[] tanks;

    private static final int[] IO_SLOTS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};

    public TileEntityMachineRefinery(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MACHINE_REFINERY.get(), pos, state, 13);
        tanks = new FluidTankNTM[5];
        tanks[0] = new FluidTankNTM(Fluids.HOTOIL, 64_000);
        tanks[1] = new FluidTankNTM(Fluids.HEAVYOIL, 24_000);
        tanks[2] = new FluidTankNTM(Fluids.NAPHTHA, 24_000);
        tanks[3] = new FluidTankNTM(Fluids.LIGHTOIL, 24_000);
        tanks[4] = new FluidTankNTM(Fluids.PETROLEUM, 24_000);
    }

    @Override
    public String getDefaultName() {
        return "container.machineRefinery";
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, TileEntityMachineRefinery te) {
        if (te.isOn) te.audioTime = 20;
        if (te.audioTime > 0) te.audioTime--;
        te.audio = MachineAudio.tick(te.audio, te.audioTime > 0, ModSounds.BOILER.get(),
                SoundSource.BLOCKS, pos, te.getVolume(0.25F), 15F, 1.0F, 20);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TileEntityMachineRefinery te) {
        te.setupAllPorts(te.getPorts());
        te.updatePortPIFIFO();

        te.isOn = false;

        if (te.hasExploded) {
            if (te.onFire) te.burn(level, pos);
            te.networkPackNT();
            return;
        }

        te.power = Library.chargeTEFromItems(te.inventory, 0, te.power, maxPower);
        if (te.tanks[0].setType(te.inventory, 12, 12)) te.setChanged();
        MachineFluidContainerIO.load(te.tanks[0], te.inventory, 1, 2);

        te.refine();

        MachineFluidContainerIO.unload(te.tanks[1], te.inventory, 3, 4);
        MachineFluidContainerIO.unload(te.tanks[2], te.inventory, 5, 6);
        MachineFluidContainerIO.unload(te.tanks[3], te.inventory, 7, 8);
        MachineFluidContainerIO.unload(te.tanks[4], te.inventory, 9, 10);

        te.networkPackNT();
    }

    /**
     * 1.7's burning refinery: 10 mB a tick off every tank that still holds something; while there is fuel,
     * everything in the 4x8x4 box is set alight, gas flames rise from the tower, and 1.7's
     * {@code SOOT_PER_SECOND * 70} soot goes up once a second.
     */
    private void burn(Level level, BlockPos pos) {
        boolean hasFuel = false;
        for (int i = 0; i < 5; i++) {
            if (tanks[i].getFill() > 0) {
                tanks[i].setFill(Math.max(tanks[i].getFill() - 10, 0));
                hasFuel = true;
            }
        }
        if (!hasFuel || !(level instanceof ServerLevel server)) return;
        int x = pos.getX(), y = pos.getY(), z = pos.getZ();
        for (Entity e : server.getEntitiesOfClass(Entity.class,
                new AABB(x - 1.5, y, z - 1.5, x + 2.5, y + 8, z + 2.5))) {
            Effects.ignite(e, 5);
        }
        RandomSource rand = server.getRandom();
        CompoundTag flame = new CompoundTag();
        flame.putDouble("moX", rand.nextGaussian() * 0.05);
        flame.putDouble("moY", 0.1);
        flame.putDouble("moZ", rand.nextGaussian() * 0.05);
        GeoNtmEffectNT.GasFlame.send(server, x + rand.nextDouble(), y + 1.5 + rand.nextDouble() * 3,
                z + rand.nextDouble(), flame, 150);

        if (TickPhase.every(level, pos, 20L)) {
            PollutionHandler.incrementPollution(level, pos, PollutionType.SOOT, PollutionHandler.SOOT_PER_SECOND * 70);
        }
    }

    /** 1.7 {@code explode}: once, and always on fire. */
    public void explode() {
        if (this.hasExploded) return;
        this.hasExploded = true;
        this.onFire = true;
        this.setChanged();
    }

    /** 1.7 {@code tryExtinguish}: foam and CO2 put it out; water on a refinery that still holds fluid blows it up. */
    @Override
    public void tryExtinguish(Level world, BlockPos pos, EnumExtinguishType type) {
        if (!this.hasExploded || !this.onFire) return;
        if (type == EnumExtinguishType.FOAM || type == EnumExtinguishType.CO2) {
            this.onFire = false;
            this.setChanged();
            return;
        }
        if (type == EnumExtinguishType.WATER) {
            for (FluidTankNTM tank : tanks) {
                if (tank.getFill() > 0) {
                    Explosions.explode(world, null, worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5,
                            5F, true, Level.ExplosionInteraction.BLOCK);
                    return;
                }
            }
        }
    }

    public boolean isDamaged() {
        return this.hasExploded;
    }

    private final List<RecipesCommon.AStack> repair = new ArrayList<>();

    /** 1.7 {@code getRepairMaterials}: eight steel plates and four duct tape. */
    public List<RecipesCommon.AStack> getRepairMaterials() {
        if (!repair.isEmpty()) return repair;
        repair.add(new RecipesCommon.TagStack(TagKey.create(
                Registries.ITEM, Ids.of("c", "plates/steel")), 8));
        repair.add(new RecipesCommon.ComparableStack(ModItems.ducttape.get(), 4));
        return repair;
    }

    public void repair() {
        this.hasExploded = false;
        this.setChanged();
    }

    @Override
    public void repair(Player player) {
        repair();
    }

    /** 1.7 {@code writeNBT}: a refinery with fluid in it, or a torn one, travels with its item. */
    @Override
    protected CompoundTag collectCarriedData() {
        boolean empty = !hasExploded;
        for (FluidTankNTM tank : tanks) if (tank.getFill() > 0) empty = false;
        if (empty) return null;
        CompoundTag data = new CompoundTag();
        for (int i = 0; i < tanks.length; i++) tanks[i].writeCarried(data, "tank" + i);
        data.putBoolean("exploded", hasExploded);
        data.putBoolean("onFire", onFire);
        return data;
    }

    @Override
    protected void applyCarriedData(CompoundTag data) {
        for (int i = 0; i < tanks.length; i++) tanks[i].readCarried(data, "tank" + i);
        hasExploded = Tags.getBooleanOr(data, "exploded", false);
        onFire = Tags.getBooleanOr(data, "onFire", false);
    }

    @Override
    protected void removeSavedComponents(DataOut output) {
        super.removeSavedComponents(output);
        for (String key : new String[] {"inventory", "input", "input_max", "input_type", "input_p", "heavy", "heavy_max",
                "heavy_type", "heavy_p", "naphtha", "naphtha_max", "naphtha_type", "naphtha_p", "light", "light_max",
                "light_type", "light_p", "petroleum", "petroleum_max", "petroleum_type", "petroleum_p", "exploded", "onFire"}) {
            output.discard(key);
        }
    }

    protected PortDef[] cachedPorts;
    public PortDef[] getPorts() { if (cachedPorts == null) cachedPorts = TilePortShapes.refinery(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()); return cachedPorts; }

    private void refine() {
        RefineryRecipes.RefineryRecipe recipe = RefineryRecipes.getRefinery(tanks[0].getTankType());
        if (recipe == null) {
            for (int i = 1; i < 5; i++) tanks[i].setTankType(Fluids.NONE);
            return;
        }

        FluidStack[] stacks = recipe.fractions();
        for (int i = 0; i < stacks.length; i++) tanks[i + 1].setTankType(stacks[i].type);

        if (power < 5 || tanks[0].getFill() < 100) return;

        for (int i = 0; i < stacks.length; i++) {
            if (tanks[i + 1].getFill() + stacks[i].fill > tanks[i + 1].getMaxFill()) return;
        }

        this.isOn = true;
        tanks[0].setFill(tanks[0].getFill() - 100);

        for (int i = 0; i < stacks.length; i++) tanks[i + 1].setFill(tanks[i + 1].getFill() + stacks[i].fill);

        this.sulfur++;

        if (this.sulfur >= maxSulfur) {
            this.sulfur -= maxSulfur;
            ItemStack out = recipe.byproduct();
            if (out != null && !out.isEmpty()) {
                ItemStack cur = inventory.getStackInSlot(11);
                if (cur.isEmpty()) {
                    inventory.setStackInSlot(11, out.copy());
                } else if (ItemStacks.sameItemAndData(cur, out) && cur.getCount() + out.getCount() <= cur.getMaxStackSize()) {
                    cur.grow(out.getCount());
                    inventory.setStackInSlot(11, cur);
                }
            }
            setChanged();
        }

        if (TickPhase.every(level, worldPosition, 20L)) PollutionHandler.incrementPollution(level, worldPosition, PollutionType.SOOT, PollutionHandler.SOOT_PER_SECOND * 5);
        this.power -= 5;
    }

    public long getPowerScaled(long i) {
        return (power * i) / maxPower;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (slot == 0) return Library.isBattery(stack);
        if (slot == 12) return stack.getItem() instanceof IItemFluidId;
        // The container-out slots (2/4/6/8/10) stay TRUE. The machine writes them via
        // inventory.insertItem, which validates against this method; the menu's take-only slots block
        // the player. Slot 11 (byproduct) is written via setStackInSlot (no validation) so it can stay
        // false.
        if (slot == 11) return false;
        return true;
    }

    // --- WorldlyContainer: extract only from the output/byproduct slots ---

    @Override public int[] getSlotsForFace(Direction side) { return IO_SLOTS; }
    @Override public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) { return isItemValidForSlot(slot, stack); }
    @Override public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return slot == 2 || slot == 4 || slot == 6 || slot == 8 || slot == 10 || slot == 11;
    }

    @Override public long getPower() { return power; }
    @Override public void setPower(long i) { power = i; }
    @Override public long getMaxPower() { return maxPower; }

    @Override public FluidTankNTM[] getSendingTanks() { return new FluidTankNTM[]{tanks[1], tanks[2], tanks[3], tanks[4]}; }
    @Override public FluidTankNTM[] getReceivingTanks() { return new FluidTankNTM[]{tanks[0]}; }
    @Override public FluidTankNTM[] getAllTanks() { return tanks; }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.machineRefinery");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ContainerMachineRefinery(containerId, playerInventory, this);
    }

    @Override
    public void serialize(FriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(this.power);
        for (FluidTankNTM tank : tanks) tank.serialize(buf);
        buf.writeBoolean(isOn);
        buf.writeBoolean(hasExploded);
    }

    @Override
    public void deserialize(FriendlyByteBuf buf) {
        super.deserialize(buf);
        this.power = buf.readLong();
        for (FluidTankNTM tank : tanks) tank.deserialize(buf);
        this.isOn = buf.readBoolean();
        this.hasExploded = buf.readBoolean();
    }

    @Override
    protected void saveData(DataOut output) {
        super.saveData(output);
        output.putLong("power", power);
        output.putInt("sulfur", sulfur);
        tanks[0].writeToNBT(output, "input");
        tanks[1].writeToNBT(output, "heavy");
        tanks[2].writeToNBT(output, "naphtha");
        tanks[3].writeToNBT(output, "light");
        tanks[4].writeToNBT(output, "petroleum");
        output.putBoolean("exploded", hasExploded);
        output.putBoolean("onFire", onFire);
    }

    @Override
    protected void loadData(DataIn input) {
        super.loadData(input);
        power = input.getLongOr("power", 0L);
        sulfur = input.getIntOr("sulfur", 0);
        tanks[0].readFromNBT(input, "input");
        tanks[1].readFromNBT(input, "heavy");
        tanks[2].readFromNBT(input, "naphtha");
        tanks[3].readFromNBT(input, "light");
        tanks[4].readFromNBT(input, "petroleum");
        hasExploded = input.getBooleanOr("exploded", false);
        onFire = input.getBooleanOr("onFire", false);
    }
}
