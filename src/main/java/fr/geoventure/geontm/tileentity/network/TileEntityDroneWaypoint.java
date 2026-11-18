package fr.geoventure.geontm.tileentity.network;

import fr.geoventure.geontm.tileentity.IRelocatable;
import fr.geoventure.geocore.data.Tags;

import fr.geoventure.geontm.entity.item.EntityDeliveryDrone;
import fr.geoventure.geontm.entity.item.EntityDroneBase;
import fr.geoventure.geontm.tileentity.TileEntityLoadedBase;
import fr.geoventure.geocore.data.DataIn;
import fr.geoventure.geocore.data.DataOut;
import fr.geoventure.geocore.world.SubLevels;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import fr.geoventure.geontm.blocks.network.DroneWaypointBase;
import fr.geoventure.geontm.client.DroneLineClient;
import fr.geoventure.geontm.tileentity.ModDroneBlockEntities;

/**
 * 1.7 {@code TileEntityDroneWaypoint}: a transport-drone relay. Drones arriving at its stand-off point (its
 * facing times {@link #height}) are sent on to the next link.
 */
public class TileEntityDroneWaypoint extends TileEntityLoadedBase implements IDroneLinkable, IRelocatable {

    /** A sub-level move carried this waypoint; its next link follows when that block was carried too. */
    @Override
    public void relocate(SubLevels.Move move) {
        next = IRelocatable.followNullable(move, next);
        setChanged();
    }


    public int height = 5;
    public @Nullable BlockPos next;

    public TileEntityDroneWaypoint(BlockPos pos, BlockState state) {
        super(ModDroneBlockEntities.DRONE_WAYPOINT.get(), pos, state);
    }

    private Direction facing() {
        BlockState state = getBlockState();
        return state.hasProperty(DroneWaypointBase.FACING) ? state.getValue(DroneWaypointBase.FACING) : Direction.UP;
    }

    public void serverTick() {
        if (!(level instanceof ServerLevel server)) return;
        BlockPos point = getPoint();

        if (next != null) {
            for (EntityDeliveryDrone drone
                    : EntityDroneBase.restingAt(server, EntityDeliveryDrone.class, point, 0.05)) {
                drone.setTarget(next.getX() + 0.5, next.getY(), next.getZ() + 0.5);
            }
        }

        networkPackNT();
    }

    /** 1.7: a redstone dust marks the hover point and a blue line runs to the next link, every other tick. */
    public void clientTick() {
        if (next == null || level == null || level.getGameTime() % 2 != 0) return;

        BlockPos point = getPoint();
        double x = point.getX() + 0.5, y = point.getY() + 0.5, z = point.getZ() + 0.5;
        level.addParticle(DustParticleOptions.REDSTONE, x, y, z, 0, 0, 0);
        DroneLineClient.show(level, x, y, z, next.getX() - point.getX(), next.getY() - point.getY(),
                next.getZ() - point.getZ(), 0x0000ff);
    }

    @Override
    public BlockPos getPoint() {
        return worldPosition.relative(facing(), height);
    }

    @Override
    public void setNextTarget(BlockPos target) {
        this.next = target.immutable();
        setChanged();
    }

    public void addHeight(int delta) {
        height = Mth.clamp(height + delta, 1, 15);
        setChanged();
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeInt(height);
        buf.writeBoolean(next != null);
        if (next != null) buf.writeBlockPos(next);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        height = buf.readInt();
        next = buf.readBoolean() ? buf.readBlockPos() : null;
    }

    @Override
    protected void loadData(DataIn input) {
        super.loadData(input);
        height = input.getIntOr("height", 5);
        next = input.read("next", BlockPos.CODEC).orElse(null);
    }

    @Override
    protected void saveData(DataOut output) {
        super.saveData(output);
        output.putInt("height", height);
        if (next != null) output.store("next", BlockPos.CODEC, next);
    }
}
