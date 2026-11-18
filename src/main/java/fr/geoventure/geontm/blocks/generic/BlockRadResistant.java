package fr.geoventure.geontm.blocks.generic;

import fr.geoventure.geontm.handler.radiation.RadiationSystemNT;
import fr.geoventure.geontm.interfaces.IRadResistantBlock;
import fr.geoventure.geocore.world.CompatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A block that seals radiation ({@link IRadResistantBlock}): the chunk engine's flood‑fill treats it as a
 * wall, so a room walled with these holds its radiation separately. Placing/removing one re‑partitions the
 * section's pockets, so both edges call {@link RadiationSystemNT#markSectionForRebuild}. Ported from
 * 1.12.2's {@code BlockRadResistant}.
 */
public class BlockRadResistant extends CompatBlock implements IRadResistantBlock {

    public BlockRadResistant(Properties props) {
        super(props);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide() && !state.is(oldState.getBlock())) {
            RadiationSystemNT.markSectionForRebuild((ServerLevel) level, pos);
        }
    }

    @Override
    protected void onBlockRemoved(BlockState state, Level level, BlockPos pos, boolean movedByPiston) {
        if (!level.isClientSide()) {
            RadiationSystemNT.markSectionForRebuild((ServerLevel) level, pos);
        }
    }
}
