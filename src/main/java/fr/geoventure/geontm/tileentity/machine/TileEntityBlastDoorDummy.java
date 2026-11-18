package fr.geoventure.geontm.tileentity.machine;

import fr.geoventure.geontm.tileentity.IRelocatable;
import fr.geoventure.geocore.data.Tags;

import fr.geoventure.geocore.data.CompatBlockEntity;
import fr.geoventure.geocore.data.DataIn;
import fr.geoventure.geocore.data.DataOut;
import fr.geoventure.geocore.world.SubLevels;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import fr.geoventure.geontm.blocks.machine.BlastDoor;
import fr.geoventure.geontm.blocks.machine.DummyBlockBlast;
import fr.geoventure.geontm.tileentity.ModDoorBlockEntities;

/**
 * 1.7.10 {@code TileEntityDummy} as the blast door uses it: one block of the leaf, pointing at the
 * door that owns it. Removes itself once the door it points at is gone.
 */
public class TileEntityBlastDoorDummy extends CompatBlockEntity implements IRelocatable {

    /** A sub-level move carried this leaf; it keeps its door when the door was carried too. */
    @Override
    public void relocate(SubLevels.Move move) {
        target = IRelocatable.follow(move, target);
        setChanged();
    }


    public BlockPos target = BlockPos.ZERO;

    public TileEntityBlastDoorDummy(BlockPos pos, BlockState state) {
        super(ModDoorBlockEntities.BLAST_DOOR_DUMMY.get(), pos, state);
    }

    public void serverTick() {
        if (level == null) return;
        if (!(level.getBlockState(target).getBlock() instanceof BlastDoor)) {
            level.destroyBlock(worldPosition, false);
        }
    }

    /** 1.7 {@code DummyBlockBlast.breakBlock}: unless the door itself is retracting this leaf, breaking it breaks the door. */
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        if (level == null || level.isClientSide() || DummyBlockBlast.safeBreak) return;
        level.destroyBlock(target, true);
    }

    @Override
    protected void loadData(DataIn nbt) {
        super.loadData(nbt);
        target = new BlockPos(nbt.getIntOr("tx", 0), nbt.getIntOr("ty", 0), nbt.getIntOr("tz", 0));
    }

    @Override
    protected void saveData(DataOut nbt) {
        super.saveData(nbt);
        nbt.putInt("tx", target.getX());
        nbt.putInt("ty", target.getY());
        nbt.putInt("tz", target.getZ());
    }
}
