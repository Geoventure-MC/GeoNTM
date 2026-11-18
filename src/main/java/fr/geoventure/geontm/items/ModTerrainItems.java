package fr.geoventure.geontm.items;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.world.item.Item;

/** 1.7 {@code plant_item} ({@code ItemEnumMulti} over {@code EnumPlantType}), flattened to three items in the parts tab. */
public final class ModTerrainItems {

    private ModTerrainItems() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ITEMS);

    public static final RegistryObject<Item> plant_item_tobacco = item("plant_item_tobacco");
    public static final RegistryObject<Item> plant_item_rope = item("plant_item_rope");
    public static final RegistryObject<Item> plant_item_mustardwillow = item("plant_item_mustardwillow");

    /** The flat 1.7 sprite under its dotted name ({@code plant_item.tobacco}). */
    private static RegistryObject<Item> item(String id) {
        return REG.item(id)
                .transform(Presets.sprite("geontm:items/plant_item." + id.substring("plant_item_".length())))
                .tab(GeoNtmTab.MATERIALS);
    }

    public static void init() {}
}
