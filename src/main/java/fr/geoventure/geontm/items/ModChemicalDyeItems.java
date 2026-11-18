package fr.geoventure.geontm.items;

import fr.geoventure.geocore.dejson.Tints;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geontm.items.food.ItemCrayon;
import fr.geoventure.geontm.items.machine.ItemChemicalDye;
import fr.geoventure.geontm.items.machine.ItemChemicalDye.EnumChemDye;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import fr.geoventure.geocore.world.CompatConsumables;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Flattened hand-owned registrations for the old chemical dye and crayon metadata families. */
public final class ModChemicalDyeItems {

    private ModChemicalDyeItems() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ITEMS);
    private static final Map<EnumChemDye, RegistryObject<ItemChemicalDye>> CHEMICAL =
            new EnumMap<>(EnumChemDye.class);
    private static final Map<EnumChemDye, RegistryObject<ItemCrayon>> CRAYON =
            new EnumMap<>(EnumChemDye.class);

    static {
        for (EnumChemDye dye : EnumChemDye.VALUES) {
            String suffix = dye.name().toLowerCase(Locale.ROOT);
            // Both families paint the same overlay in the dye's colour; the base layer is pinned white.
            RegistryObject<ItemChemicalDye> chemical = REG.item(
                            "chemical_dye_" + suffix, p -> new ItemChemicalDye(p, dye))
                    .transform(dyed("geontm:items/chemical_dye", dye.color));
            RegistryObject<ItemCrayon> crayon = REG.item(
                            "crayon_" + suffix, p -> new ItemCrayon(p, dye),
                            p -> CompatConsumables.foodAlwaysEdible(p, 3, 0.6F, List.of()))
                    .transform(dyed("geontm:items/crayon", dye.color));
            CHEMICAL.put(dye, chemical);
            CRAYON.put(dye, crayon);
            ModTabs.addTo(GeoNtmTab.MATERIALS, chemical);
            ModTabs.addTo(GeoNtmTab.MATERIALS, crayon);
        }
    }

    private static Presets.ItemModel dyed(String base, int colour) {
        return Presets.sprite(base, base + "_overlay").tints(Tints.constant(-1), Tints.customModelData(0, colour));
    }

    public static ItemChemicalDye chemicalDye(EnumChemDye dye) {
        return CHEMICAL.get(dye).get();
    }

    public static ItemCrayon crayon(EnumChemDye dye) {
        return CRAYON.get(dye).get();
    }

    public static ItemStack chemicalDyeStack(EnumChemDye dye, int count) {
        return chemicalDye(dye).stack(count);
    }

    public static int registeredCount() {
        return CHEMICAL.size() + CRAYON.size();
    }

    public static void init() {}
}
