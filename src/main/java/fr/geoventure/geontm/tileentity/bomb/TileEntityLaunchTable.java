package fr.geoventure.geontm.tileentity.bomb;

import fr.geoventure.geontm.lib.Library;
import fr.geoventure.geontm.inventory.container.ContainerLaunchTable;
import fr.geoventure.geontm.items.weapon.ItemCustomMissilePart.PartSize;
import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geontm.tileentity.ModCustomMissileBlockEntities;
import fr.geoventure.geontm.util.SavedIds;
import fr.geoventure.geocore.data.DataIn;
import fr.geoventure.geocore.data.DataOut;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import fr.geoventure.geontm.tileentity.TilePort.PortDef;

/**
 * 1.7 {@code TileEntityLaunchTable}: the 9x9 launch table. 100,000 l tanks, 100,000 solid, and a pad size the
 * GUI sets (size 10, 15 or 20 fuselage tops).
 *
 * <p>{@code height} is 1.7's client-side scaffold height, which its renderer overwrote from the loaded missile and
 * kept once the missile left.
 */
public class TileEntityLaunchTable extends TileEntityCustomMissileLauncher implements MenuProvider {

    public static final int maxSolid = 100000;
    public PartSize padSize = PartSize.SIZE_10;
    public int height = 10;

    public TileEntityLaunchTable(BlockPos pos, BlockState state) {
        super(ModCustomMissileBlockEntities.LAUNCH_TABLE.get(), pos, state, 100000);
    }

    @Override public String getDefaultName() { return "container.launchTable"; }
    @Override public int getMaxSolid() { return maxSolid; }
    @Override public PartSize acceptedSize() { return padSize; }
    @Override protected int footprintRadius() { return 4; }

    public PortDef[] getPorts() {
        if (cachedPorts == null) {
            int x = worldPosition.getX(), y = worldPosition.getY(), z = worldPosition.getZ();
            cachedPorts = new PortDef[] {
                    PortDef.make(x + 4, y, z + 4, Library.POS_X, Library.POS_Z),
                    PortDef.make(x + 4, y, z + 3, Library.POS_X),
                    PortDef.make(x + 4, y, z + 2, Library.POS_X),
                    PortDef.make(x + 4, y, z + 1, Library.POS_X),
                    PortDef.make(x + 4, y, z + 0, Library.POS_X),
                    PortDef.make(x + 4, y, z - 1, Library.POS_X),
                    PortDef.make(x + 4, y, z - 2, Library.POS_X),
                    PortDef.make(x + 4, y, z - 3, Library.POS_X),
                    PortDef.make(x + 4, y, z - 4, Library.POS_X, Library.NEG_Z),
                    PortDef.make(x + 3, y, z - 4, Library.NEG_Z),
                    PortDef.make(x + 2, y, z - 4, Library.NEG_Z),
                    PortDef.make(x + 1, y, z - 4, Library.NEG_Z),
                    PortDef.make(x + 0, y, z - 4, Library.NEG_Z),
                    PortDef.make(x - 1, y, z - 4, Library.NEG_Z),
                    PortDef.make(x - 2, y, z - 4, Library.NEG_Z),
                    PortDef.make(x - 3, y, z - 4, Library.NEG_Z),
                    PortDef.make(x - 4, y, z - 4, Library.NEG_X, Library.NEG_Z),
                    PortDef.make(x - 4, y, z - 3, Library.NEG_X),
                    PortDef.make(x - 4, y, z - 2, Library.NEG_X),
                    PortDef.make(x - 4, y, z - 1, Library.NEG_X),
                    PortDef.make(x - 4, y, z + 0, Library.NEG_X),
                    PortDef.make(x - 4, y, z + 1, Library.NEG_X),
                    PortDef.make(x - 4, y, z + 2, Library.NEG_X),
                    PortDef.make(x - 4, y, z + 3, Library.NEG_X),
                    PortDef.make(x - 4, y, z + 4, Library.NEG_X, Library.POS_Z),
                    PortDef.make(x - 3, y, z + 4, Library.POS_Z),
                    PortDef.make(x - 2, y, z + 4, Library.POS_Z),
                    PortDef.make(x - 1, y, z + 4, Library.POS_Z),
                    PortDef.make(x + 0, y, z + 4, Library.POS_Z),
                    PortDef.make(x + 1, y, z + 4, Library.POS_Z),
                    PortDef.make(x + 2, y, z + 4, Library.POS_Z),
                    PortDef.make(x + 3, y, z + 4, Library.POS_Z),
            };
        }
        return cachedPorts;
    }

    @Override
    public boolean canConnect(ForgeDirection dir) {
        return dir != ForgeDirection.UP && dir != ForgeDirection.DOWN && dir != ForgeDirection.UNKNOWN;
    }

    /** 1.7 AuxButtonPacket: the GUI's three pad-size buttons send the PartSize ordinal. */
    @Override
    public void handleButtonPacket(int value, int meta) {
        if (value >= 0 && value < PartSize.values().length) {
            this.padSize = PartSize.values()[value];
            setChanged();
        }
    }

    @Override
    public void serialize(FriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeByte((byte) padSize.ordinal());
    }

    @Override
    public void deserialize(FriendlyByteBuf buf) {
        super.deserialize(buf);
        this.padSize = PartSize.values()[buf.readByte()];
    }

    @Override
    protected void saveData(DataOut output) {
        super.saveData(output);
        SavedIds.putEnum(output, "padSize", padSize); // by name, never an ordinal
    }

    @Override
    protected void loadData(DataIn input) {
        super.loadData(input);
        padSize = SavedIds.getEnum(input, "padSize", PartSize.SIZE_10);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(getName());
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ContainerLaunchTable(containerId, playerInventory, this);
    }

    /** 1.7 {@code TileEntityLaunchTable}'s wider launch smoke. */
    @Override
    protected double smokeSpread() {
        return 0.65D;
    }
}
