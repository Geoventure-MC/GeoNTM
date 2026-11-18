package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.tileentity.network.RequestNetwork;
import fr.geoventure.geocore.data.GeoCoreComponent;
import fr.geoventure.geocore.net.GeoCoreNetwork;
import fr.geoventure.geocore.platform.Lifecycle;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.BlockPos;
import fr.geoventure.geontm.inventory.ModDroneMenus;
import fr.geoventure.geontm.items.tool.ItemDroneLinker;
import fr.geoventure.geontm.packet.DroneLinePayload;
import fr.geoventure.geontm.tileentity.ModDroneBlockEntities;

/**
 * One common bootstrap hook for the delivery drone blocks' feature-local registries, the linker item, and the
 * request network's server-tick sweep (1.7 {@code ModEventHandler} calling {@code RequestNetwork.updateEntries}).
 */
public final class ModDroneContent {
    private ModDroneContent() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ITEMS);

    /** 1.7 {@code ModItems.drone_linker}: stack size 1, consumables tab. */
    public static final RegistryObject<ItemDroneLinker> DRONE_LINKER =
            REG.item("drone_linker", ItemDroneLinker::new, p -> p.stacksTo(1))
                    .transform(Presets.sprite())
                    .tab(GeoNtmTab.CONSUMABLES);

    /** The linker's remembered stop; 1.7 kept it as x/y/z on the stack's NBT. */
    public static final GeoCoreComponent<BlockPos> LINKER_POSITION =
            GeoCoreComponent.of(Constants.MOD_ID, "drone_linker_position", BlockPos.CODEC);

    private static boolean ticking;

    public static void init() {
        ModDroneBlocks.init();
        ModDroneBlockEntities.init();
        ModDroneMenus.init();
        GeoCoreNetwork.declarePlayToClient(DroneLinePayload.CODEC);
        if (!ticking) {
            ticking = true;
            Lifecycle.onServerTick(RequestNetwork::updateEntries);
        }
    }
}
