package fr.geoventure.geontm.tileentity.network;

import fr.geoventure.geontm.api.fluidmk2.DuctData;
import fr.geoventure.geontm.blocks.network.FluidDuctBox;
import fr.geoventure.geontm.tileentity.ModFluidNetBlockEntities;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geocore.data.DataIn;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The 1.7 {@code TileEntityPipeExhaust}: an exhaust duct that carries the three smoke fluids
 * ({@code SMOKE}, {@code SMOKE_LEADED}, {@code SMOKE_POISON}) at once.
 *
 * <p>1.7 minted one node per smoke type on the same block (three overlapping nets). The port's fluid
 * graph keys a cell by position, so the exhaust's one cell carries {@link DuctData#SMOKE_ANY} instead:
 * {@link fr.geoventure.geontm.api.fluidmk2.FluidGraphProvider#dataCompatible} joins it to any smoke duct and
 * {@link fr.geoventure.geontm.api.fluidmk2.FluidNetwork} rounds its net once per smoke, which is the same three
 * flows 1.7 ran. The tile's own {@link #getFluidType() type} is only the skin the renderer tints and the
 * look-overlay names: it defaults to {@code SMOKE}, a fluid identifier holding one of the other two
 * smokes reskins it (any other fluid is refused), and the graph cell never changes.
 */
public class TileEntityPipeExhaust extends TileEntityFluidDuctVariant {

    public TileEntityPipeExhaust(BlockPos pos, BlockState state) {
        super(state.getBlock() instanceof FluidDuctBox
                ? ModFluidNetBlockEntities.EXHAUST.get() : ModFluidNetBlockEntities.PAINTABLE_EXHAUST.get(), pos, state);
        this.type = Fluids.SMOKE;
    }

    public static boolean isSmoke(FluidType type) {
        return DuctData.isSmoke(type);
    }

    /** All three smokes, always: the skin in {@code type} does not narrow what the cell conducts. */
    @Override
    protected DuctData ductData() {
        return DuctData.SMOKE_ANY;
    }

    @Override
    public void setFluidType(FluidType type) {
        if (!isSmoke(type)) return;
        super.setFluidType(type);
    }

    @Override
    public boolean canConnect(FluidType type, ForgeDirection dir) {
        return dir != ForgeDirection.UNKNOWN && isSmoke(type);
    }

    @Override
    protected void loadData(DataIn input) {
        super.loadData(input);
        if (!isSmoke(this.type)) this.type = Fluids.SMOKE;
    }
}
