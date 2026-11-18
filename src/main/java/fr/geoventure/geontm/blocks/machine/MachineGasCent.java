package fr.geoventure.geontm.blocks.machine;

import fr.geoventure.geontm.capability.ICapabilityBlock;
import fr.geoventure.geontm.capability.MachineCaps;
import net.minecraft.core.Direction;
import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geontm.api.energymk2.IEnergyConnectorBlock;
import fr.geoventure.geontm.api.fluidmk2.IFluidConnectorBlockMK2;
import fr.geoventure.geontm.blocks.multiblock.BlockMultiblockMachine;
import fr.geoventure.geontm.blocks.multiblock.MultiblockSurface;
import fr.geoventure.geontm.tileentity.ModBlockEntities;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineGasCent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;

/** Four-block gas centrifuge tower with energy and fluid ports around its core. */
public class MachineGasCent extends BlockMultiblockMachine implements IEnergyConnectorBlock, IFluidConnectorBlockMK2,
        WorldlyContainerHolder, ICapabilityBlock {

    public MachineGasCent(Properties properties) {
        super(properties);
        this.bounding.add(new AABB(-0.5D, 0D, -0.5D, 0.5D, 1D, 0.5D));
        this.bounding.add(new AABB(-0.4375D, 1D, -0.4375D, 0.4375D, 4D, 0.4375D));
    }

    @Override
    protected BlockEntity createCoreBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityMachineGasCent(pos, state);
    }

    @Override
    @Nullable
    public WorldlyContainer getContainer(BlockState state, LevelAccessor level, BlockPos pos) {
        BlockPos core = MultiblockSurface.coreOfAny(level, pos);
        return core != null && level.getBlockEntity(core) instanceof TileEntityMachineGasCent te ? te : null;
    }

    @Override public int[] getDimensions() { return new int[]{3, 0, 0, 0, 0, 0}; }
    @Override public int getOffset() { return 0; }

    /** 1.7's six-direction loop minus UP, buried under the machine's own cell. */
    @Override
    public int coreMask() {
        return MASK_HORIZONTAL | MASK_DOWN;
    }

    @Override
    public int[][] getAllPorts(ForgeDirection dir) {
        return rotatePorts(new int[][] {
                {0, 0, 0, ForgeDirection.EAST.ordinal()},
                {0, 0, 0, ForgeDirection.WEST.ordinal()},
                {0, 0, 0, ForgeDirection.SOUTH.ordinal()},
                {0, 0, 0, ForgeDirection.NORTH.ordinal()},
        }, dir);
    }

    /** Live connection rules of the corresponding 1.7 tile, shared by native and foreign contacts. */
    private static final MachineCaps CAPS = MachineCaps.blockKeyed().powerIn().fluidIn().items()
            .fluidContacts(TileEntityMachineGasCent.class);

    @Override
    public MachineCaps caps() { return CAPS; }
}
