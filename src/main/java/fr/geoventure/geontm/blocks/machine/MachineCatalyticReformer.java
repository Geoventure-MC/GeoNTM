package fr.geoventure.geontm.blocks.machine;

import fr.geoventure.geontm.capability.ICapabilityBlock;
import fr.geoventure.geontm.capability.MachineCaps;
import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geontm.api.energymk2.IEnergyConnectorBlock;
import fr.geoventure.geontm.api.fluidmk2.IFluidConnectorBlockMK2;
import fr.geoventure.geontm.blocks.multiblock.BlockMultiblockMachine;
import fr.geoventure.geontm.blocks.multiblock.MultiblockHandlerXR;
import fr.geoventure.geontm.blocks.multiblock.MultiblockSurface;
import fr.geoventure.geontm.tileentity.ModBlockEntities;
import fr.geoventure.geontm.tileentity.machine.oil.TileEntityMachineCatalyticReformer;
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

/** Catalytic Reformer block, a 1:1 port of the 1.12.2 {@code MachineCatalyticReformer} (dims
 * {@code {2,0,1,1,2,2}}, offset 1; two large body fills + 6 makeExtra corner/side ports). */
public class MachineCatalyticReformer extends BlockMultiblockMachine implements IEnergyConnectorBlock, IFluidConnectorBlockMK2, WorldlyContainerHolder, ICapabilityBlock {

    public MachineCatalyticReformer(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntity createCoreBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityMachineCatalyticReformer(pos, state);
    }

    @Override public int[] getDimensions() { return new int[]{2, 0, 1, 1, 2, 2}; }
    @Override public int getOffset() { return 1; }

    @Override
    @Nullable
    public WorldlyContainer getContainer(BlockState state, LevelAccessor level, BlockPos pos) {
        BlockPos core = MultiblockSurface.coreOfAny(level, pos);
        return core != null && level.getBlockEntity(core) instanceof TileEntityMachineCatalyticReformer te ? te : null;
    }

    private static final int[] WING1 = {3, -3, 1, 0, -1, 2};
    private static final int[] WING2 = {6, -3, 1, 1, 2, 0};

    @Override
    protected boolean checkRequirement(Level level, BlockPos core, Direction facing, BlockPos clicked) {
        return super.checkRequirement(level, core, facing, clicked)
                && MultiblockHandlerXR.checkSpace(level, core, WING1, clicked, facing)
                && MultiblockHandlerXR.checkSpace(level, core, WING2, clicked, facing);
    }

    /** Two wings off the body, four corner ports and two out on the rotation axis. */
    @Override
    protected void visitCells(BlockPos core, Direction facing, CellVisitor visitor) {
        super.visitCells(core, facing, visitor);
        MultiblockHandlerXR.visitBox(core, WING1, facing, visitor);
        MultiblockHandlerXR.visitBox(core, WING2, facing, visitor);

        Direction rot = facing.getClockWise();
        visitor.cell(core.offset(1, 0, 1), MASK_SOUTH);
        visitor.cell(core.offset(-1, 0, 1), MASK_SOUTH);
        visitor.cell(core.offset(1, 0, -1), MASK_NORTH);
        visitor.cell(core.offset(-1, 0, -1), MASK_NORTH);
        visitor.cell(core.offset(rot.getStepX() * 2, 0, rot.getStepZ() * 2), MASK_WEST);
        visitor.cell(core.offset(-rot.getStepX() * 2, 0, -rot.getStepZ() * 2), MASK_EAST);
    }

    @Override
    protected void visitPassiveCells(BlockPos core, Direction facing, PassiveCellVisitor visitor) {
        Direction rot = facing.getClockWise();
        int domains = PASSIVE_POWER_IN | PASSIVE_FLUID_IN;
        visitor.passiveCell(core.offset(1, 0, 1), MASK_ALL, domains);
        visitor.passiveCell(core.offset(-1, 0, 1), MASK_ALL, domains);
        visitor.passiveCell(core.offset(1, 0, -1), MASK_ALL, domains);
        visitor.passiveCell(core.offset(-1, 0, -1), MASK_ALL, domains);
        visitor.passiveCell(core.offset(rot.getStepX() * 2, 0, rot.getStepZ() * 2), MASK_ALL, domains);
        visitor.passiveCell(core.offset(-rot.getStepX() * 2, 0, -rot.getStepZ() * 2), MASK_ALL, domains);
    }

    /** 1.7 {@code MachineCatalyticReformer.getAllDimensions}, read only by the placement highlight. */
    @Override
    public int[][] getAllDimensions() {
        return new int[][] {
            new int[] {2, 0, 1, 1, 2, 2},
            new int[] {3, -3, 1, 0, -1, 2},
            new int[] {6, -3, 1, 1, 2, 0},
        };
    }

    @Override
    public int[][] getAllPorts(ForgeDirection dir) {
        return rotatePorts(new int[][] {
                {2, 0, 0, ForgeDirection.EAST.ordinal()},
                {-2, 0, 0, ForgeDirection.WEST.ordinal()},
                {1, 0, 1, ForgeDirection.SOUTH.ordinal()},
                {-1, 0, 1, ForgeDirection.SOUTH.ordinal()},
                {1, 0, -1, ForgeDirection.NORTH.ordinal()},
                {-1, 0, -1, ForgeDirection.NORTH.ordinal()},
        }, dir);
    }

    /** Live connection rules of the corresponding 1.7 tile, shared by native and foreign contacts. */
    private static final MachineCaps CAPS = MachineCaps.blockKeyed().powerIn().fluidIn().fluidOut().items()
            .faces(BlockEntity.class, (be, side) -> side != Direction.DOWN)
            .fluidFaces(BlockEntity.class, (be, side) -> side != Direction.DOWN);

    @Override
    public MachineCaps caps() { return CAPS; }
}
