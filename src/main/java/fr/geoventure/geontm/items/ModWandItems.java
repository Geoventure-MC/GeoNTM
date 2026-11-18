package fr.geoventure.geontm.items;

import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.items.tool.ItemWand;
import fr.geoventure.geontm.items.tool.ItemWandD;
import fr.geoventure.geontm.items.tool.ItemWandS;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;

/**
 * 1.7's three creative wands, declared as {@code ModItems} declares them: {@code setMaxStackSize(1)},
 * {@code consumableTab}, {@code setFull3D} (handheld models). The construction wand's
 * field is {@code wand} but its registry name is {@code wand_k}.
 */
public final class ModWandItems {

    private ModWandItems() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ITEMS);

    public static final RegistryObject<ItemWand> wand =
            REG.item("wand_k", ItemWand::new, p -> p.stacksTo(1))
                    .transform(Presets.handheld("geontm:items/wand"))
                    .tab(GeoNtmTab.CONSUMABLES);
    public static final RegistryObject<ItemWandS> wand_s =
            REG.item("wand_s", ItemWandS::new, p -> p.stacksTo(1))
                    .transform(Presets.handheld())
                    .tab(GeoNtmTab.CONSUMABLES);
    public static final RegistryObject<ItemWandD> wand_d =
            REG.item("wand_d", ItemWandD::new, p -> p.stacksTo(1))
                    .transform(Presets.handheld())
                    .tab(GeoNtmTab.CONSUMABLES);

    public static void init() {}
}
