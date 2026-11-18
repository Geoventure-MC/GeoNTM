package fr.geoventure.geontm.blocks;

import fr.geoventure.geocore.registry.BlockTag;
import fr.geoventure.geocore.registry.Loot;
import fr.geoventure.geocore.registry.Preset;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.dejson.Models;
import fr.geoventure.geocore.dejson.Slots;
import fr.geoventure.geocore.dejson.Variants;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.items.block.DescBlockItem;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.world.Compat;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import fr.geoventure.geontm.blocks.network.DroneCrate;
import fr.geoventure.geontm.blocks.network.DroneDock;
import fr.geoventure.geontm.blocks.network.DroneWaypoint;
import fr.geoventure.geontm.blocks.network.DroneWaypointRequest;

/**
 * Feature-local block and block-item registry for the delivery drone blocks; 1.7: every one 0.1/10, machine tab.
 * Models, blockstates and loot are stated on each registration (Reg; emitted by Dejson at build time): the crates
 * are bottom/top/side cubes over the 1.7 sprites; the two waypoints ride the radio torch's element model over
 * their own sprite, mounted on any side, and their items are that flat sprite.
 */
public final class ModDroneBlocks {
    private ModDroneBlocks() {}

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCKS, ITEMS);

    /** A bottom/top/side cube over {@code geontm:blocks/<id>_bottom|_side|_top}; drops itself, pickaxe-mined. */
    private static final Preset CRATE = handle -> handle
            .transform(Presets.cubeBottomTop("geontm:blocks/" + handle.path() + "_bottom", "geontm:blocks/" + handle.path() + "_side",
                    "geontm:blocks/" + handle.path() + "_top"))
            .loot(Loot.self())
            .tag(BlockTag.PICKAXE);
    /** The radio torch's element model over the waypoint's own sprite, mounted on any side; drops itself. */
    private static final Preset WAYPOINT = handle -> handle
            .transform(Presets.shape(Models.renderType("minecraft:cutout", Models.parented("geontm:block/radio_torch_template",
                    Slots.of("torch", "geontm:blocks/" + handle.path())))).states(Variants.facingUp()).noItem())
            .loot(Loot.self());

    public static final RegistryObject<DroneWaypoint> DRONE_WAYPOINT = REG.block(
            "drone_waypoint", DroneWaypoint::new,
            p -> p.strength(0.1F, 6.0F).sound(SoundType.WOOD).noCollision().noOcclusion())
            .transform(WAYPOINT);
    public static final RegistryObject<DroneCrate> DRONE_CRATE = REG.block(
            "drone_crate", DroneCrate::new,
            p -> p.strength(0.1F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops())
            .transform(CRATE); // 1.7 Material.iron
    public static final RegistryObject<DroneWaypointRequest> DRONE_WAYPOINT_REQUEST = REG.block(
            "drone_waypoint_request", DroneWaypointRequest::new,
            p -> p.strength(0.1F, 6.0F).sound(SoundType.WOOD).noCollision().noOcclusion())
            .transform(WAYPOINT);
    public static final RegistryObject<DroneDock> DRONE_DOCK = REG.block(
            "drone_dock", p -> new DroneDock(p, DroneDock.Type.DOCK),
            p -> p.strength(0.1F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops())
            .transform(CRATE); // 1.7 Material.iron
    public static final RegistryObject<DroneDock> DRONE_CRATE_PROVIDER = REG.block(
            "drone_crate_provider", p -> new DroneDock(p, DroneDock.Type.PROVIDER),
            p -> p.strength(0.1F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops())
            .transform(CRATE); // 1.7 Material.iron
    public static final RegistryObject<DroneDock> DRONE_CRATE_REQUESTER = REG.block(
            "drone_crate_requester", p -> new DroneDock(p, DroneDock.Type.REQUESTER),
            p -> p.strength(0.1F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops())
            .transform(CRATE); // 1.7 Material.iron

    static {
        descItem("drone_waypoint", DRONE_WAYPOINT).transform(Presets.sprite("geontm:blocks/drone_waypoint"));
        descItem("drone_crate", DRONE_CRATE);
        tabItem("drone_waypoint_request", DRONE_WAYPOINT_REQUEST).transform(Presets.sprite("geontm:blocks/drone_waypoint_request"));
        descItem("drone_dock", DRONE_DOCK);
        descItem("drone_crate_provider", DRONE_CRATE_PROVIDER);
        descItem("drone_crate_requester", DRONE_CRATE_REQUESTER);
    }

    /** 1.7 ITooltipProvider blocks: the item shows {@code addStandardInfo}. */
    private static Reg.Handle<DescBlockItem> descItem(String name, RegistryObject<? extends Block> block) {
        return REG.item(name, p -> new DescBlockItem(block.get(), p), Compat::blockDescription)
                .tab(GeoNtmTab.MACHINES);
    }

    private static Reg.Handle<BlockItem> tabItem(String name, RegistryObject<? extends Block> block) {
        return REG.blockItem(block).tab(GeoNtmTab.MACHINES);
    }

    public static void init() {}
}
