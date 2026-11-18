package fr.geoventure.geontm.tileentity.network;

import fr.geoventure.geontm.api.fluidmk2.DuctData;
import fr.geoventure.geontm.api.fluidmk2.FluidGraph;
import fr.geoventure.geontm.api.redstoneoverradio.IRORValueProvider;
import fr.geoventure.geontm.tileentity.ModFluidNetBlockEntities;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.uninos.graph.NodeNetwork;
import fr.geoventure.geocore.data.DataIn;
import fr.geoventure.geocore.data.DataOut;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import fr.geoventure.geontm.blocks.network.FluidDuctGauge;

/**
 * The 1.7 {@code FluidDuctGauge.TileEntityPipeGauge}: a duct that reads its net's per-tick transfer
 * ({@code fluidTracker}, here {@link NodeNetwork#tracker}) and keeps a per-second total. The two values
 * are synced to the client for the look overlay; 1.7 sent them by {@code networkPackNT(25)}, here they
 * ride the block-entity update packet on the same 25-tick cadence.
 */
public class TileEntityPipeGauge extends TileEntityFluidDuctVariant implements IRORValueProvider {

    public long deltaTick = 0;
    private long deltaSecond = 0;
    public long deltaLastSecond = 0;

    public TileEntityPipeGauge(BlockPos pos, BlockState state) {
        super(ModFluidNetBlockEntities.GAUGE.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TileEntityPipeGauge gauge) {
        if (!(level instanceof ServerLevel server)) return;
        if (gauge.getFluidType() != Fluids.NONE) {
            NodeNetwork<DuctData> net = FluidGraph.get(server).networkAt(pos.asLong());
            if (net != null) {
                gauge.deltaTick = net.tracker;
                if (level.getGameTime() % 20 == 0) {
                    gauge.deltaLastSecond = gauge.deltaSecond;
                    gauge.deltaSecond = 0;
                }
                gauge.deltaSecond += gauge.deltaTick;
            }
        }
        // 1.7 networkPackNT(25): one sync every 25 ticks, not one per tick of fluctuating flow.
        if (level.getGameTime() % 25 == 0) level.sendBlockUpdated(pos, state, state, 3);
    }

    @Override
    protected void saveData(DataOut output) {
        super.saveData(output);
        output.putLong("deltaTick", Math.max(deltaTick, 0));
        output.putLong("deltaLastSecond", Math.max(deltaLastSecond, 0));
    }

    @Override
    protected void loadData(DataIn input) {
        super.loadData(input);
        this.deltaTick = Math.max(input.getLongOr("deltaTick", 0L), 0);
        this.deltaLastSecond = Math.max(input.getLongOr("deltaLastSecond", 0L), 0);
    }

    @Override
    public String[] getFunctionInfo() {
        return new String[]{PREFIX_VALUE + "deltatick", PREFIX_VALUE + "deltasecond"};
    }

    @Override
    public String provideRORValue(String name) {
        if ((PREFIX_VALUE + "deltatick").equals(name)) return "" + deltaTick;
        if ((PREFIX_VALUE + "deltasecond").equals(name)) return "" + deltaLastSecond;
        return null;
    }
}
