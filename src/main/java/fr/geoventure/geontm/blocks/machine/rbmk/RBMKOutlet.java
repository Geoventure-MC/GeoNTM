package fr.geoventure.geontm.blocks.machine.rbmk;
import fr.geoventure.geontm.blocks.ITooltipProvider;
import fr.geoventure.geontm.api.fluidmk2.IFluidConnectorBlockMK2; import fr.geoventure.geontm.tileentity.ModRBMKBlockEntities; import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityRBMKOutlet; import net.minecraft.core.BlockPos; import net.minecraft.world.level.BlockGetter; import net.minecraft.world.level.Level; import net.minecraft.world.level.block.Block; import net.minecraft.world.level.block.EntityBlock; import net.minecraft.world.level.block.entity.BlockEntity; import net.minecraft.world.level.block.entity.BlockEntityTicker; import net.minecraft.world.level.block.entity.BlockEntityType; import net.minecraft.world.level.block.state.BlockState; import org.jspecify.annotations.Nullable;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geocore.registry.TickingBlock;
/** Single-block ReaSim steam endpoint: source behavior exposes all six pipe faces. */
public class RBMKOutlet extends Block implements IFluidConnectorBlockMK2, TickingBlock, ITooltipProvider { public RBMKOutlet(Properties properties) { super(properties); } @Override public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new BlockEntityRBMKOutlet(pos, state); } @Override public boolean canConnect(FluidType type, BlockGetter world, int x, int y, int z, ForgeDirection dir) { return dir != ForgeDirection.UNKNOWN && world.getBlockEntity(new BlockPos(x, y, z)) instanceof BlockEntityRBMKOutlet; } }
