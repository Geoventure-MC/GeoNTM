package fr.geoventure.geontm.blocks.machine;

import fr.geoventure.geontm.capability.ICapabilityBlock;
import fr.geoventure.geontm.capability.MachineCaps;
import fr.geoventure.geontm.api.energymk2.IEnergyConnectorBlock;
import fr.geoventure.geontm.api.fluidmk2.IFluidConnectorBlockMK2;
import fr.geoventure.geontm.blocks.multiblock.BlockMultiblockMachine;
import fr.geoventure.geontm.blocks.multiblock.MultiblockSurface;
import fr.geoventure.geontm.interfaces.ILookOverlay;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.lib.DirPos;
import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geontm.tileentity.ModBlockEntities;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineTurbineGas;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/** The five-long legacy gas turbine multiblock. */
public class MachineTurbineGas extends BlockMultiblockMachine
        implements IEnergyConnectorBlock, IFluidConnectorBlockMK2, ILookOverlay, ICapabilityBlock {

    public MachineTurbineGas(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntity createCoreBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityMachineTurbineGas(pos, state);
    }

    @Override public int[] getDimensions() { return new int[]{2, 0, 1, 1, 4, 5}; }
    @Override public int getOffset() { return 1; }

    @Override
    public boolean canConnect(BlockGetter world, BlockPos pos, ForgeDirection dir) {
        return MultiblockSurface.coreEntity(world, pos) instanceof TileEntityMachineTurbineGas turbine
                && IEnergyConnectorBlock.isPort(new DirPos[]{turbine.getPowerPos()}, pos, dir);
    }

    @Override
    public boolean canConnect(FluidType type, BlockGetter world, int x, int y, int z, ForgeDirection dir) {
        BlockPos pos = new BlockPos(x, y, z);
        return MultiblockSurface.coreEntity(world, pos) instanceof TileEntityMachineTurbineGas turbine
                && turbine.isFluidPort(pos, dir);
    }

    @Override
    public ILookOverlay.Data getLookOverlay(Level level, BlockPos pos, Player player) {
        if (!(MultiblockSurface.coreEntity(level, pos) instanceof TileEntityMachineTurbineGas turbine)) return null;

        ForgeDirection facing = turbine.getFacing();
        List<String> text = new ArrayList<>();

        BlockPos core = turbine.getBlockPos();
        if (hitCheck(facing, core, -1, -1, 0, pos) || hitCheck(facing, core, 1, -1, 0, pos)
                || hitCheck(facing, core, -1, 4, 0, pos) || hitCheck(facing, core, 1, 4, 0, pos)
                || hitCheck(facing, core, 0, 5, 1, pos)) {
            text.add("§a-> §r" + turbine.tanks[0].getTankType().getDisplayNameString());
            text.add("§a-> §r" + turbine.tanks[1].getTankType().getDisplayNameString());
            text.add("§a-> §r" + turbine.tanks[2].getTankType().getDisplayNameString());
            text.add("§c<- §r" + turbine.tanks[3].getTankType().getDisplayNameString());
        }

        if (hitCheck(facing, core, 0, -4, 1, pos)) {
            text.add("§c<- §rPower");
        }

        if (!text.isEmpty()) {
            return new ILookOverlay.Data(getName().getString(), 0xffff00, 0x404000, text);
        }
        return null;
    }

    private static boolean hitCheck(ForgeDirection facing, BlockPos core, int facingOffset, int rotationOffset,
                                    int yOffset, BlockPos hit) {
        ForgeDirection turn = facing.getRotation(ForgeDirection.DOWN);
        int x = core.getX() + facing.offsetX * facingOffset + turn.offsetX * rotationOffset;
        int y = core.getY() + yOffset;
        int z = core.getZ() + facing.offsetZ * facingOffset + turn.offsetZ * rotationOffset;
        return x == hit.getX() && y == hit.getY() && z == hit.getZ();
    }

    /** Four anchors along the flanks and two more a step up, all riding the facing. */
    @Override
    protected void visitCells(BlockPos core, Direction facing, CellVisitor visitor) {
        super.visitCells(core, facing, visitor);
        Direction rot = facing.getClockWise();
        int dx = facing.getStepX(), dz = facing.getStepZ();
        int rx = rot.getStepX(), rz = rot.getStepZ();

        visitor.cell(core.offset(-dx + rx, 0, -dz + rz), MASK_NORTH);
        visitor.cell(core.offset(dx + rx, 0, dz + rz), MASK_SOUTH);
        visitor.cell(core.offset(-dx + rx * -4, 0, -dz + rz * -4), MASK_NORTH);
        visitor.cell(core.offset(dx + rx * -4, 0, dz + rz * -4), MASK_SOUTH);
        visitor.cell(core.offset(rx * 4, 1, rz * 4), MASK_WEST);
        visitor.cell(core.offset(rx * -5, 1, rz * -5), MASK_EAST);
    }

    @Override
    protected void visitPassiveCells(BlockPos core, Direction facing, PassiveCellVisitor visitor) {
        Direction rot = facing.getClockWise();
        int dx = facing.getStepX(), dz = facing.getStepZ();
        int rx = rot.getStepX(), rz = rot.getStepZ();
        int domains = PASSIVE_POWER_IN | PASSIVE_FLUID_IN;

        visitor.passiveCell(core.offset(-dx + rx, 0, -dz + rz), MASK_ALL, domains);
        visitor.passiveCell(core.offset(dx + rx, 0, dz + rz), MASK_ALL, domains);
        visitor.passiveCell(core.offset(-dx + rx * -4, 0, -dz + rz * -4), MASK_ALL, domains);
        visitor.passiveCell(core.offset(dx + rx * -4, 0, dz + rz * -4), MASK_ALL, domains);
        visitor.passiveCell(core.offset(rx * 4, 1, rz * 4), MASK_ALL, domains);
        visitor.passiveCell(core.offset(rx * -5, 1, rz * -5), MASK_ALL, domains);
    }

    @Override
    public int[][] getAllPorts(ForgeDirection dir) {
        return rotatePorts(new int[][] {
                {1, 0, -1, ForgeDirection.NORTH.ordinal()},
                {1, 0, 1, ForgeDirection.SOUTH.ordinal()},
                {-4, 0, -1, ForgeDirection.NORTH.ordinal()},
                {-4, 0, 1, ForgeDirection.SOUTH.ordinal()},
                {4, 1, 0, ForgeDirection.EAST.ordinal()},
                {-5, 1, 0, ForgeDirection.WEST.ordinal()},
        }, dir);
    }

    /** Live connection rules of the corresponding 1.7 tile, shared by native and foreign contacts. */
    private static final MachineCaps CAPS = MachineCaps.blockKeyed().powerOut().fluidIn().fluidOut().items()
            .faces(BlockEntity.class, (be, side) -> side != Direction.DOWN)
            .fluidFaces(BlockEntity.class, (be, side) -> side != Direction.DOWN);

    @Override
    public MachineCaps caps() { return CAPS; }
}
