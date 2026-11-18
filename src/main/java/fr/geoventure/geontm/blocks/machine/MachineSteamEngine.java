package fr.geoventure.geontm.blocks.machine;

import fr.geoventure.geontm.capability.ICapabilityBlock;
import fr.geoventure.geontm.capability.MachineCaps;
import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geontm.blocks.ITooltipProvider;
import fr.geoventure.geontm.api.energymk2.IEnergyConnectorBlock;
import fr.geoventure.geontm.api.fluidmk2.IFluidConnectorBlockMK2;
import fr.geoventure.geontm.blocks.multiblock.BlockMultiblockMachine;
import fr.geoventure.geontm.blocks.multiblock.MultiblockSurface;
import fr.geoventure.geontm.interfaces.ILookOverlay;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.inventory.fluid.tank.FluidTankNTM;
import fr.geoventure.geontm.tileentity.ModBlockEntities;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineSteamEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/** The legacy one-core steam engine with three ports in the upper row. */
public class MachineSteamEngine extends BlockMultiblockMachine
        implements IEnergyConnectorBlock, IFluidConnectorBlockMK2, ILookOverlay, ITooltipProvider, ICapabilityBlock {

    public MachineSteamEngine(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntity createCoreBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityMachineSteamEngine(pos, state);
    }

    @Override
    public int[] getDimensions() {
        return new int[]{1, 0, 5, 1, 1, 1};
    }

    @Override
    public int getOffset() {
        return 1;
    }

    @Override
    public ILookOverlay.Data getLookOverlay(Level level, BlockPos pos, Player player) {
        BlockPos core = MultiblockSurface.coreOfAny(level, pos);
        if (core == null || !(level.getBlockEntity(core) instanceof TileEntityMachineSteamEngine engine)) return null;

        List<String> text = new ArrayList<>();
        text.add("§a-> §r" + tankLine(engine.tanks[0]));
        text.add("§c<- §r" + tankLine(engine.tanks[1]));
        return new ILookOverlay.Data(this.getName().getString(), 0xffff00, 0x404000, text);
    }

    private static String tankLine(FluidTankNTM tank) {
        return tank.getTankType().getDisplayNameString() + ": " + tank.getFill() + "/" + tank.getMaxFill() + "mB";
    }

    /** Three ports in a row along one flank, one above the core. */
    @Override
    protected void visitCells(BlockPos core, Direction facing, CellVisitor visitor) {
        super.visitCells(core, facing, visitor);
        Direction rot = facing.getClockWise();
        for (int i = -1; i <= 1; i++) {
            visitor.cell(core.offset(rot.getStepX() + facing.getStepX() * i, 1,
                    rot.getStepZ() + facing.getStepZ() * i), MASK_WEST);
        }
    }

    @Override
    protected void visitPassiveCells(BlockPos core, Direction facing, PassiveCellVisitor visitor) {
        Direction rot = facing.getClockWise();
        for (int i = -1; i <= 1; i++) {
            visitor.passiveCell(core.offset(rot.getStepX() + facing.getStepX() * i, 1,
                            rot.getStepZ() + facing.getStepZ() * i),
                    MASK_ALL, PASSIVE_POWER_IN | PASSIVE_FLUID_IN);
        }
    }

    @Override
    public boolean acceptsFluid(FluidType type) {
        return type == Fluids.STEAM || type == Fluids.SPENTSTEAM;
    }

    @Override
    public int[][] getAllPorts(ForgeDirection dir) {
        return rotatePorts(new int[][] {
                {1, 1, 0, ForgeDirection.EAST.ordinal()},
                {1, 1, 1, ForgeDirection.EAST.ordinal()},
                {1, 1, -1, ForgeDirection.EAST.ordinal()},
        }, dir);
    }

    /** Live connection rules of the corresponding 1.7 tile, shared by native and foreign contacts. */
    private static final MachineCaps CAPS = MachineCaps.blockKeyed().powerOut().fluidIn().fluidOut()
            .faces(BlockEntity.class, (be, side) -> side.getAxis().isHorizontal());

    @Override
    public MachineCaps caps() { return CAPS; }
}
