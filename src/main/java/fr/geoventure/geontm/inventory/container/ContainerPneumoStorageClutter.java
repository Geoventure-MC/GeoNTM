package fr.geoventure.geontm.inventory.container;

import fr.geoventure.geocore.world.Compat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.jspecify.annotations.Nullable;
import fr.geoventure.geontm.inventory.ModPneumaticMenus;
import fr.geoventure.geontm.tileentity.network.pneumatic.TileEntityPneumoStorageClutter;

/** 1.7 {@code ContainerPneumoStorageClutter}: a plain 6x9 bank. */
public class ContainerPneumoStorageClutter extends PneumaticMenuBase<TileEntityPneumoStorageClutter> {

    public ContainerPneumoStorageClutter(int containerId, Inventory playerInventory, BlockPos pos) {
        this(containerId, playerInventory, resolve(playerInventory, pos, TileEntityPneumoStorageClutter.class));
    }

    public ContainerPneumoStorageClutter(int containerId, Inventory playerInventory,
                                         @Nullable TileEntityPneumoStorageClutter storage) {
        super(ModPneumaticMenus.PNEUMATIC_STORAGE_CLUTTER.get(), containerId, storage,
                TileEntityPneumoStorageClutter.SLOT_COUNT);

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(machine, col + row * 9, 8 + col * 18, 17 + row * 18));
            }
        }

        Compat.addPlayerInventory(this::addSlot, playerInventory, 8, 153);
    }
}
