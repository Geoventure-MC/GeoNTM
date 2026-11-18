package fr.geoventure.geontm.tileentity;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.registry.BlockEntities;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import fr.geoventure.geontm.blocks.ModDroneBlocks;
import fr.geoventure.geontm.tileentity.network.TileEntityDroneCrate;
import fr.geoventure.geontm.tileentity.network.TileEntityDroneDock;
import fr.geoventure.geontm.tileentity.network.TileEntityDroneProvider;
import fr.geoventure.geontm.tileentity.network.TileEntityDroneRequester;
import fr.geoventure.geontm.tileentity.network.TileEntityDroneWaypoint;
import fr.geoventure.geontm.tileentity.network.TileEntityDroneWaypointRequest;

/** Feature-local block-entity types for the delivery drone blocks. */
public final class ModDroneBlockEntities {
    private ModDroneBlockEntities() {}

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCK_ENTITIES);

    public static final RegistryObject<BlockEntityType<TileEntityDroneCrate>> DRONE_CRATE =
            REG.blockEntity("drone_crate", () -> BlockEntities.serverTicking(
                    TileEntityDroneCrate::new,
                    (level, pos, state, be) -> be.serverTick(), ModDroneBlocks.DRONE_CRATE.get()));
    public static final RegistryObject<BlockEntityType<TileEntityDroneDock>> DRONE_DOCK =
            REG.blockEntity("drone_dock", () -> BlockEntities.serverTicking(
                    TileEntityDroneDock::new,
                    (level, pos, state, be) -> be.serverTick(), ModDroneBlocks.DRONE_DOCK.get()));
    public static final RegistryObject<BlockEntityType<TileEntityDroneProvider>> DRONE_PROVIDER =
            REG.blockEntity("drone_crate_provider", () -> BlockEntities.serverTicking(
                    TileEntityDroneProvider::new,
                    (level, pos, state, be) -> be.serverTick(), ModDroneBlocks.DRONE_CRATE_PROVIDER.get()));
    public static final RegistryObject<BlockEntityType<TileEntityDroneRequester>> DRONE_REQUESTER =
            REG.blockEntity("drone_crate_requester", () -> BlockEntities.serverTicking(
                    TileEntityDroneRequester::new,
                    (level, pos, state, be) -> be.serverTick(), ModDroneBlocks.DRONE_CRATE_REQUESTER.get()));
    public static final RegistryObject<BlockEntityType<TileEntityDroneWaypoint>> DRONE_WAYPOINT =
            REG.blockEntity("drone_waypoint", () -> BlockEntities.ticking(
                    TileEntityDroneWaypoint::new,
                    (level, pos, state, be) -> be.clientTick(),
                    (level, pos, state, be) -> be.serverTick(), ModDroneBlocks.DRONE_WAYPOINT.get()));
    public static final RegistryObject<BlockEntityType<TileEntityDroneWaypointRequest>> DRONE_WAYPOINT_REQUEST =
            REG.blockEntity("drone_waypoint_request", () -> BlockEntities.serverTicking(
                    TileEntityDroneWaypointRequest::new,
                    (level, pos, state, be) -> be.serverTick(), ModDroneBlocks.DRONE_WAYPOINT_REQUEST.get()));

    public static void init() {}
}
