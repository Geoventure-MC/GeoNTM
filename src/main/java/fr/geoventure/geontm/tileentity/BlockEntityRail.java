package fr.geoventure.geontm.tileentity;

import fr.geoventure.geontm.blocks.ModRailBlocks;
import fr.geoventure.geocore.data.DataIn;
import fr.geoventure.geocore.data.DataOut;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;

/** The switch-state block entity every folded rail carries (ntm-next's BlockEntityRail on this port's sync base). */
public class BlockEntityRail extends TileEntityLoadedBase {

    /** 1.7 TileEntityRailSwitch; only the switch reads it, every rail carries the BE so one renderer can serve the family */
    public boolean isSwitched = false;

    public BlockEntityRail(BlockPos pos, BlockState state) {
        super(ModRailBlocks.RAIL_ENTITY.get(), pos, state);
    }

    public void toggleSwitch() {
        isSwitched = !isSwitched;
        setChanged();
        if (level != null && !level.isClientSide()) networkPackNT();
    }

    @Override
    protected void saveData(DataOut out) {
        super.saveData(out);
        out.putBoolean("isSwitched", isSwitched);
    }

    @Override
    protected void loadData(DataIn in) {
        super.loadData(in);
        isSwitched = in.getBooleanOr("isSwitched", false);
    }

    @Override
    public void serialize(FriendlyByteBuf buf) {
        buf.writeBoolean(isSwitched);
    }

    @Override
    public void deserialize(FriendlyByteBuf buf) {
        isSwitched = buf.readBoolean();
    }
}
