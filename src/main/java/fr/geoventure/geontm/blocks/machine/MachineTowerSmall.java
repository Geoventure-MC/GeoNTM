package fr.geoventure.geontm.blocks.machine;

import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geontm.api.fluidmk2.IFluidConnectorBlockMK2;
import fr.geoventure.geontm.blocks.multiblock.BlockMultiblockMachine;
import fr.geoventure.geontm.blocks.multiblock.MultiblockSurface;
import fr.geoventure.geontm.interfaces.ILookOverlay;
import fr.geoventure.geontm.tileentity.machine.TileEntityTowerSmall;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import java.util.ArrayList;
import java.util.List;

/** 5x5x19 auxiliary cooling tower, folded from the legacy dummy block. */
public class MachineTowerSmall extends BlockMultiblockMachine implements IFluidConnectorBlockMK2, ILookOverlay {
    public MachineTowerSmall(Properties properties) { super(properties); }
    @Override protected BlockEntity createCoreBlockEntity(BlockPos pos, BlockState state) { return new TileEntityTowerSmall(pos, state); }
    @Override public int[] getDimensions() { return new int[]{18, 0, 2, 2, 2, 2}; }
    @Override public int getOffset() { return 2; }

    /** The four legacy {@code makeExtra} ports sit one block beyond the 5x5 footprint. */
    @Override
    protected void visitCells(BlockPos core, Direction facing, CellVisitor visitor) {
        super.visitCells(core, facing, visitor);
        visitor.cell(core.offset(2, 0, 0), MASK_EAST);
        visitor.cell(core.offset(-2, 0, 0), MASK_WEST);
        visitor.cell(core.offset(0, 0, 2), MASK_SOUTH);
        visitor.cell(core.offset(0, 0, -2), MASK_NORTH);
    }

    @Override public ILookOverlay.Data getLookOverlay(Level level, BlockPos pos, Player player) {
        if (!(MultiblockSurface.coreEntity(level, pos) instanceof TileEntityTowerSmall tower)) return null;
        List<String> text = new ArrayList<>();
        MachineCondenser.vacuumWarning(level, tower, text);
        text.add("§a-> §r" + tower.input.getTankType().getDisplayNameString() + ": " + tower.input.getFill() + "/" + tower.input.getMaxFill() + "mB");
        text.add("§c<- §r" + tower.output.getTankType().getDisplayNameString() + ": " + tower.output.getFill() + "/" + tower.output.getMaxFill() + "mB");
        return new ILookOverlay.Data(getName().getString(), 0xffff00, 0x404000, text);
    }

    @Override
    public int[][] getAllPorts(ForgeDirection dir) {
        return rotatePorts(new int[][] {
                {2, 0, 0, ForgeDirection.EAST.ordinal()},
                {-2, 0, 0, ForgeDirection.WEST.ordinal()},
                {0, 0, -2, ForgeDirection.NORTH.ordinal()},
                {0, 0, 2, ForgeDirection.SOUTH.ordinal()},
        }, dir);
    }
}
