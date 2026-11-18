package fr.geoventure.geontm.blocks.machine;

import fr.geoventure.geontm.api.fluidmk2.IFluidConnectorBlockMK2;
import fr.geoventure.geontm.blocks.multiblock.BlockMultiblockMachine;
import fr.geoventure.geontm.blocks.multiblock.MultiblockSurface;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineF6Tank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

/** Shared block behavior for the fixed-fluid UF6 and PuF6 tanks. */
public abstract class MachineF6Tank extends BlockMultiblockMachine implements IFluidConnectorBlockMK2, WorldlyContainerHolder {

    protected MachineF6Tank(Properties properties) {
        super(properties);
    }

    @Override
    @Nullable
    public WorldlyContainer getContainer(BlockState state, LevelAccessor level, BlockPos pos) {
        BlockPos core = MultiblockSurface.coreOfAny(level, pos);
        return core != null && level.getBlockEntity(core) instanceof TileEntityMachineF6Tank tank ? tank : null;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int onAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return MultiblockSurface.coreEntity(level, pos) instanceof TileEntityMachineF6Tank tank
                ? tank.tank.getRedstoneComparatorPower() : 0;
    }

    @Override
    public int[] getDimensions() {
        return new int[]{1, 0, 0, 0, 0, 0};
    }

    @Override
    public int getOffset() {
        return 0;
    }

    /** Six ports one block out, each pointing away: the whole surface is the core's own. */
    @Override public int coreMask() { return MASK_ALL; }

    /**
     * A fixed-fluid tank takes only what it already holds, which the geometry cannot say -- the answer
     * is the tile's tank type, not the block's.
     */
    @Override
    public boolean canConnect(FluidType type, BlockGetter world, int x, int y, int z, ForgeDirection dir) {
        return MultiblockSurface.coreEntity(world, new BlockPos(x, y, z)) instanceof TileEntityMachineF6Tank tank
                && (type == Fluids.NONE || type == tank.tank.getTankType())
                && super.canConnect(type, world, x, y, z, dir);
    }
}
