package fr.geoventure.geontm.blocks.machine;

import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geontm.api.energymk2.IEnergyConnectorBlock;
import fr.geoventure.geontm.api.fluidmk2.IFluidConnectorBlockMK2;
import fr.geoventure.geontm.blocks.multiblock.BlockMultiblockMachine;
import fr.geoventure.geontm.blocks.multiblock.MultiblockSurface;
import fr.geoventure.geontm.interfaces.ILookOverlay;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.inventory.fluid.tank.FluidTankNTM;
import fr.geoventure.geontm.tileentity.ModBlockEntities;
import fr.geoventure.geontm.tileentity.machine.TileEntityCondenserPowered;
import fr.geoventure.geontm.lib.Library;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** The six-port, powered 3x3x3 steam condenser multiblock. */
public class MachineCondenserPowered extends BlockMultiblockMachine
        implements IEnergyConnectorBlock, IFluidConnectorBlockMK2, ILookOverlay {

    public MachineCondenserPowered(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntity createCoreBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityCondenserPowered(pos, state);
    }

    @Override
    public int[] getDimensions() {
        return new int[]{2, 0, 1, 1, 3, 3};
    }

    @Override
    public int getOffset() {
        return 1;
    }

    @Override
    public ILookOverlay.Data getLookOverlay(Level level, BlockPos pos, Player player) {
        if (!(MultiblockSurface.coreEntity(level, pos) instanceof TileEntityCondenserPowered condenser)) return null;

        List<String> text = new ArrayList<>();
        text.add(Library.getShortNumber(condenser.power) + "HE / "
                + Library.getShortNumber(condenser.getMaxPower()) + "HE");
        text.add("§a-> §r" + tankLine(condenser.input));
        text.add("§c<- §r" + tankLine(condenser.output));
        return new ILookOverlay.Data(getName().getString(), 0xffff00, 0x404000, text);
    }

    private static String tankLine(FluidTankNTM tank) {
        return tank.getTankType().getDisplayNameString() + ": "
                + String.format(Locale.US, "%,d", tank.getFill()) + "/"
                + String.format(Locale.US, "%,d", tank.getMaxFill()) + "mB";
    }

    /** Two ports out on the rotation axis and four at the corners, all one above the core. */
    @Override
    protected void visitCells(BlockPos core, Direction facing, CellVisitor visitor) {
        super.visitCells(core, facing, visitor);
        Direction rot = facing.getClockWise();
        int dx = facing.getStepX(), dz = facing.getStepZ();
        int rx = rot.getStepX(), rz = rot.getStepZ();

        visitor.cell(core.offset(rx * 3, 1, rz * 3), MASK_WEST);
        visitor.cell(core.offset(-rx * 3, 1, -rz * 3), MASK_EAST);
        visitor.cell(core.offset(dx + rx, 1, dz + rz), MASK_SOUTH);
        visitor.cell(core.offset(dx - rx, 1, dz - rz), MASK_SOUTH);
        visitor.cell(core.offset(-dx + rx, 1, -dz + rz), MASK_NORTH);
        visitor.cell(core.offset(-dx - rx, 1, -dz - rz), MASK_NORTH);
    }

    @Override
    protected void visitPassiveCells(BlockPos core, Direction facing, PassiveCellVisitor visitor) {
        Direction rot = facing.getClockWise();
        int dx = facing.getStepX(), dz = facing.getStepZ();
        int rx = rot.getStepX(), rz = rot.getStepZ();
        int domains = PASSIVE_POWER_IN | PASSIVE_FLUID_IN;

        visitor.passiveCell(core.offset(rx * 3, 1, rz * 3), MASK_ALL, domains);
        visitor.passiveCell(core.offset(-rx * 3, 1, -rz * 3), MASK_ALL, domains);
        visitor.passiveCell(core.offset(dx + rx, 1, dz + rz), MASK_ALL, domains);
        visitor.passiveCell(core.offset(dx - rx, 1, dz - rz), MASK_ALL, domains);
        visitor.passiveCell(core.offset(-dx + rx, 1, -dz + rz), MASK_ALL, domains);
        visitor.passiveCell(core.offset(-dx - rx, 1, -dz - rz), MASK_ALL, domains);
    }

    /** 1.7 takes spent steam in and water out here, and refuses everything else at the same call site. */
    @Override
    public boolean acceptsFluid(FluidType type) {
        return type == Fluids.SPENTSTEAM || type == Fluids.WATER;
    }

    @Override
    public int[][] getAllPorts(ForgeDirection dir) {
        return rotatePorts(new int[][] {
                {3, 1, 0, ForgeDirection.EAST.ordinal()},
                {-3, 1, 0, ForgeDirection.WEST.ordinal()},
                {1, 1, 1, ForgeDirection.SOUTH.ordinal()},
                {-1, 1, 1, ForgeDirection.SOUTH.ordinal()},
                {1, 1, -1, ForgeDirection.NORTH.ordinal()},
                {-1, 1, -1, ForgeDirection.NORTH.ordinal()},
        }, dir);
    }
}
