package fr.geoventure.geontm.items;

import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.items.machine.ItemForceFieldUpgrade;
import fr.geoventure.geontm.items.special.ItemTeleLink;
import fr.geoventure.geontm.items.tool.ItemAnchorRemote;
import fr.geoventure.geontm.items.tool.ItemRadarLinker;
import fr.geoventure.geocore.data.GeoCoreComponent;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

/**
 * The radar and utility machines' items, as 1.7 {@code ModItems} declared them: the radar linker, the telelinker
 * ({@code linker}), the recall device ({@code anchor_remote}) and the forcefield's two upgrades.
 */
public final class ModRadarUtilityItems {

    private ModRadarUtilityItems() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ITEMS);

    /** 1.7 {@code ItemCoordinateBase}'s {@code posX/posY/posZ}. */
    public static final GeoCoreComponent<BlockPos> COORDINATE_POSITION =
            GeoCoreComponent.of(Constants.MOD_ID, "coordinate_position", BlockPos.CODEC);
    /** 1.7 {@code ItemTeleLink}'s {@code x/y/z/dim}. */
    public static final GeoCoreComponent<CompoundTag> TELE_LINK = GeoCoreComponent.compound(Constants.MOD_ID, "tele_link");
    /** 1.7 {@code ItemAnchorRemote}'s {@code x/y/z}. */
    public static final GeoCoreComponent<BlockPos> ANCHOR_POSITION =
            GeoCoreComponent.of(Constants.MOD_ID, "anchor_position", BlockPos.CODEC);

    // 1.7: setMaxStackSize(1), consumableTab
    public static final RegistryObject<ItemRadarLinker> radar_linker = REG.item(
            "radar_linker", ItemRadarLinker::new, p -> p.stacksTo(1))
            .transform(Presets.sprite())
            .tab(GeoNtmTab.CONSUMABLES);
    public static final RegistryObject<ItemTeleLink> linker = REG.item(
            "linker", ItemTeleLink::new, p -> p.stacksTo(1))
            .transform(Presets.sprite())
            .tab(GeoNtmTab.CONSUMABLES);
    public static final RegistryObject<ItemAnchorRemote> anchor_remote = REG.item(
            "anchor_remote", ItemAnchorRemote::new, p -> p.stacksTo(1))
            .transform(Presets.handheld())
            .tab(GeoNtmTab.CONSUMABLES);

    // 1.7: setMaxStackSize(16), controlTab
    public static final RegistryObject<ItemForceFieldUpgrade> upgrade_radius = REG.item(
            "upgrade_radius", p -> new ItemForceFieldUpgrade(true, p), p -> p.stacksTo(16))
            .transform(Presets.sprite())
            .tab(GeoNtmTab.CONTROL);
    public static final RegistryObject<ItemForceFieldUpgrade> upgrade_health = REG.item(
            "upgrade_health", p -> new ItemForceFieldUpgrade(false, p), p -> p.stacksTo(16))
            .transform(Presets.sprite())
            .tab(GeoNtmTab.CONTROL);

    public static void init() {}
}
