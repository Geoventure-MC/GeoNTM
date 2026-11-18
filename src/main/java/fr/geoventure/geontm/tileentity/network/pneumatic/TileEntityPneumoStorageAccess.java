package fr.geoventure.geontm.tileentity.network.pneumatic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import fr.geoventure.geontm.inventory.StackCache;
import fr.geoventure.geontm.inventory.container.ContainerPneumoStorageAccess;
import fr.geoventure.geontm.tileentity.ModPneumaticBlockEntities;

/**
 * 1.7 {@code TileEntityPneumoStorageAccess}: the network's terminal, whose grid is its {@link StackCache} rather
 * than an inventory.
 */
public class TileEntityPneumoStorageAccess extends TileEntityPneumaticMachineBase {

    public TileEntityPneumoStorageAccess(BlockPos pos, BlockState state) {
        super(ModPneumaticBlockEntities.PNEUMATIC_STORAGE_ACCESS.get(), pos, state, 0);
    }

    @Override
    public String getDefaultName() {
        return "container.pneumoStorageAccess";
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ContainerPneumoStorageAccess(containerId, playerInventory, this);
    }
}
