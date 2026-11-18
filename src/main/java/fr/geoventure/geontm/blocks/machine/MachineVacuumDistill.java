package fr.geoventure.geontm.blocks.machine;

import fr.geoventure.geontm.capability.ICapabilityBlock;
import fr.geoventure.geontm.capability.MachineCaps;
import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geontm.api.energymk2.IEnergyConnectorBlock;
import fr.geoventure.geontm.api.fluidmk2.IFluidConnectorBlockMK2;
import fr.geoventure.geontm.blocks.multiblock.BlockMultiblockMachine;
import fr.geoventure.geontm.blocks.multiblock.MultiblockSurface;
import fr.geoventure.geontm.tileentity.ModBlockEntities;
import fr.geoventure.geontm.tileentity.machine.oil.TileEntityMachineVacuumDistill;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

/** Vacuum Distiller block: 3×3×9, the folded core (dims {@code {8,0,1,1,1,1}}, offset 1), with the
 * 4 base corners marked as extras (the fluid ports). Mirrors {@link MachineRefinery}. */
public class MachineVacuumDistill extends BlockMultiblockMachine implements IEnergyConnectorBlock, IFluidConnectorBlockMK2, WorldlyContainerHolder, ICapabilityBlock {

    public MachineVacuumDistill(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntity createCoreBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityMachineVacuumDistill(pos, state);
    }

    @Override public int[] getDimensions() { return new int[]{8, 0, 1, 1, 1, 1}; }
    @Override public int getOffset() { return 1; }

    @Override
    @Nullable
    public WorldlyContainer getContainer(BlockState state, LevelAccessor level, BlockPos pos) {
        BlockPos core = MultiblockSurface.coreOfAny(level, pos);
        return core != null && level.getBlockEntity(core) instanceof TileEntityMachineVacuumDistill te ? te : null;
    }

    /** The declared box fills a plus at ground level; 1.7 widens it to the full 3x3 with four corners. */
    @Override
    protected void visitCells(BlockPos core, Direction facing, CellVisitor visitor) {
        super.visitCells(core, facing, visitor);
        for (int dx = -1; dx <= 1; dx += 2) {
            for (int dz = -1; dz <= 1; dz += 2) {
                visitor.cell(core.offset(dx, 0, dz), outwardMask(dx, dz, 1));
            }
        }
    }

    @Override
    protected void visitPassiveCells(BlockPos core, Direction facing, PassiveCellVisitor visitor) {
        for (int dx = -1; dx <= 1; dx += 2) {
            for (int dz = -1; dz <= 1; dz += 2) {
                visitor.passiveCell(core.offset(dx, 0, dz), MASK_ALL, PASSIVE_POWER_IN | PASSIVE_FLUID_IN);
            }
        }
    }

    @Override
    public int[][] getAllPorts(ForgeDirection dir) {
        return rotatePorts(new int[][] {
                {1, 0, -1, ForgeDirection.NORTH.ordinal()},
                {-1, 0, -1, ForgeDirection.NORTH.ordinal()},
                {1, 0, -1, ForgeDirection.EAST.ordinal()},
                {-1, 0, -1, ForgeDirection.WEST.ordinal()},
                {1, 0, 1, ForgeDirection.SOUTH.ordinal()},
                {-1, 0, 1, ForgeDirection.SOUTH.ordinal()},
                {1, 0, 1, ForgeDirection.EAST.ordinal()},
                {-1, 0, 1, ForgeDirection.WEST.ordinal()},
        }, dir);
    }

    /** Live connection rules of the corresponding 1.7 tile, shared by native and foreign contacts. */
    private static final MachineCaps CAPS = MachineCaps.blockKeyed().powerIn().fluidIn().fluidOut().items()
            .faces(BlockEntity.class, (be, side) -> side != Direction.DOWN)
            .fluidFaces(BlockEntity.class, (be, side) -> side != Direction.DOWN);

    @Override
    public MachineCaps caps() { return CAPS; }
}
