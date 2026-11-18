package fr.geoventure.geontm.items;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geontm.items.tool.ItemCMStructure;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;

/** 1.7 {@code structure_custommachine}: stack size 1, consumable tab, {@code setFull3D}. */
public final class ModCustomMachineItems {

    private ModCustomMachineItems() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ITEMS);

    public static final RegistryObject<ItemCMStructure> structure_custommachine = REG.<ItemCMStructure>item(
                    "structure_custommachine", ItemCMStructure::new, p -> p.stacksTo(1))
            .transform(Presets.handheld()); // 1.7 setFull3D

    static {
        ModTabs.addTo(GeoNtmTab.CONSUMABLES, structure_custommachine);
    }

    public static void init() {}
}
