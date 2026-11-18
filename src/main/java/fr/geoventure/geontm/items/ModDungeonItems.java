package fr.geoventure.geontm.items;

import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geontm.items.tool.ItemRebarPlacer;
import fr.geoventure.geocore.data.GeoCoreComponent;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

/**
 * The dungeon family's one plain item, the rebar placer (1.7: max stack 1, consumable tab). 1.7 kept its loaded
 * concrete as a one-slot NBT inventory and its first corner as the {@code pos} int array; those are two data
 * components here.
 */
public final class ModDungeonItems {

    private ModDungeonItems() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ITEMS);

    /** 1.7 {@code ItemStackUtil.addStacksToNBT(stack, 1)}: the concrete the placed rebar will set into. */
    public static final GeoCoreComponent<ItemStack> REBAR_CONCRETE = GeoCoreComponent.itemStack(Constants.MOD_ID, "rebar_concrete");
    /** 1.7 {@code pos}: the first corner of the box, the cell in front of the first clicked face. */
    public static final GeoCoreComponent<BlockPos> REBAR_POS = GeoCoreComponent.of(Constants.MOD_ID, "rebar_pos", BlockPos.CODEC);

    public static final RegistryObject<ItemRebarPlacer> rebar_placer =
            REG.item("rebar_placer", ItemRebarPlacer::new, p -> p.stacksTo(1)).transform(Presets.sprite());

    static {
        ModTabs.addTo(GeoNtmTab.CONSUMABLES, rebar_placer);
    }

    public static void init() {}
}
