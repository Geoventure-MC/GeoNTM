package fr.geoventure.geontm.tileentity.machine;

import fr.geoventure.geontm.api.energymk2.IEnergyConnectorBlock;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.lib.ForgeDirection;
import net.minecraft.core.BlockPos;

/** Port matching shared by the factory block's energy and fluid connector views. */
public final class AssemblyFactoryPorts {

    private AssemblyFactoryPorts() {}

    /** Includes the ordinary machine anchors and the four coolant proxy anchors. */
    public static boolean isPort(TileEntityMachineAssemblyFactory factory, BlockPos cell, ForgeDirection direction) {
        return IEnergyConnectorBlock.isPort(factory.getConPos(), cell, direction)
                || IEnergyConnectorBlock.isPort(factory.getCoolPos(), cell, direction);
    }

    /** Coolant proxy cells accept only the factory's water and spent-steam channels. */
    public static boolean isFluidPort(TileEntityMachineAssemblyFactory factory, BlockPos cell,
                                      ForgeDirection direction, FluidType type) {
        return IEnergyConnectorBlock.isPort(factory.getConPos(), cell, direction)
                || (type != null && (type == Fluids.WATER || type == Fluids.SPENTSTEAM)
                && IEnergyConnectorBlock.isPort(factory.getCoolPos(), cell, direction));
    }
}
