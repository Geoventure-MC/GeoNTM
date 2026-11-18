package fr.geoventure.geontm.tileentity.machine;

import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geontm.tileentity.MachineCore;
import fr.geoventure.geontm.tileentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import fr.geoventure.geontm.tileentity.TilePort.PortDef;
import fr.geoventure.geontm.tileentity.TilePortShapes;

public class TileEntityMachineCompressorCompact extends TileEntityMachineCompressorBase {

    public TileEntityMachineCompressorCompact(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MACHINE_COMPRESSOR_COMPACT.get(), pos, state);
    }

    protected PortDef[] cachedPorts;
    @Override
    public PortDef[] getPorts() {
        if (cachedPorts == null) cachedPorts = TilePortShapes.condenser(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), ForgeDirection.getOrientation(MachineCore.facingOrdinal(getBlockState(), 2)));
        return cachedPorts;
    }
}
