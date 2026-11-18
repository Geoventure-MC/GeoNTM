package fr.geoventure.geontm.blocks.machine;

import fr.geoventure.geontm.capability.ICapabilityBlock;
import fr.geoventure.geontm.capability.MachineCaps;
import net.minecraft.core.Direction;
import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geontm.tileentity.machine.TileEntityChimneyBase;
import fr.geoventure.geontm.tileentity.machine.TileEntityChimneyBrick;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

/** The 13-high brick smokestack. */
public final class MachineChimneyBrick extends MachineChimneyBase implements ICapabilityBlock {

    public MachineChimneyBrick(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntity createCoreBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityChimneyBrick(pos, state);
    }

    @Override public int[] getDimensions() { return new int[]{12, 0, 1, 1, 1, 1}; }
    @Override public int getOffset() { return 1; }

    @Override
    public int[][] getAllPorts(ForgeDirection dir) {
        return rotatePorts(new int[][] {
                {1, 0, 0, ForgeDirection.EAST.ordinal()},
                {-1, 0, 0, ForgeDirection.WEST.ordinal()},
                {0, 0, 1, ForgeDirection.SOUTH.ordinal()},
                {0, 0, -1, ForgeDirection.NORTH.ordinal()},
        }, dir);
    }

    /** Live connection rules of the corresponding 1.7 tile, shared by native and foreign contacts. */
    private static final MachineCaps CAPS = MachineCaps.blockKeyed().fluidIn()
            .fluidContacts(TileEntityChimneyBase.class);

    @Override
    public MachineCaps caps() { return CAPS; }
}
