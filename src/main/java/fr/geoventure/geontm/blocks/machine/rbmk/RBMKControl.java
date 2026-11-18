package fr.geoventure.geontm.blocks.machine.rbmk;

import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityRBMKControl;
import fr.geoventure.geontm.capability.ICapabilityBlock;
import fr.geoventure.geontm.capability.MachineCaps;
import net.minecraft.core.Direction;import fr.geoventure.geontm.tileentity.ModRBMKBlockEntities; import fr.geoventure.geontm.tileentity.machine.rbmk.*; import net.minecraft.core.BlockPos; import net.minecraft.world.level.block.entity.BlockEntityType; import net.minecraft.world.level.block.state.BlockState;
public class RBMKControl extends RBMKBase implements ICapabilityBlock { public final boolean moderated; public RBMKControl(Properties properties, boolean moderated) { super(properties); this.moderated = moderated; } @Override public boolean hasOwnLid() { return true; } @Override public int coreMask() { return this == ModRBMKBlocks.rbmk_control_reasim.get() ? MASK_DOWN : MASK_NONE; } @Override protected BlockEntityType<? extends BlockEntityRBMKBase> beType() { return ModRBMKBlockEntities.RBMK_CONTROL.get(); } @Override protected BlockEntityRBMKBase createCore(BlockPos pos, BlockState state) { return new BlockEntityRBMKControlManual(pos, state); }
    /** Live connection rules of the corresponding 1.7 tile, shared by native and foreign contacts. */
    private static final MachineCaps CAPS = MachineCaps.blockKeyed().powerIn()
            .faces(BlockEntityRBMKControl.class, (be, side) -> be.isPowered() && side == Direction.DOWN);

    @Override
    public MachineCaps caps() { return this == ModRBMKBlocks.rbmk_control_reasim.get() ? CAPS : MachineCaps.NONE; }
}
