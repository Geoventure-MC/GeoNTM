package fr.geoventure.geontm.items;

import fr.geoventure.geontm.items.special.CellVariants;
import fr.geoventure.geocore.dejson.Models;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geontm.items.special.ItemSingularity;
import fr.geoventure.geontm.items.special.ItemCell;
import fr.geoventure.geontm.items.special.ItemAntimatterPellet;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;

/** The five legacy drop capsules needed by the Spark Singularity progression. */
public final class ModSingularityItems {
    private ModSingularityItems() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ITEMS);

    /** The metadata cell required by the old antimatter-to-singularity production chain. */
    public static final RegistryObject<ItemCell> cell = REG.item("cell", ItemCell::new)
            .icon(d -> d.models(cellModels()).selects(Constants.MOD_ID + ":model_state"));

    /**
     * The fluid cell: {@code geontm:model_state} case <i>n</i> (computed from the fluid) is {@code CellVariants} <i>n</i>, the 1.7 cell item of that
     * declaration order (empty, uf6, puf6, antimatter, deuterium, tritium, sas3, anti_schrabidium). The
     * variant is the cell's own index, never the fluid id. See {@link fr.geoventure.geontm.items.special.CellVariants}.
     */
    private static Models.Group cellModels() {
        Models.Group models = Models.group();
        String[] names = CellVariants.NAMES;
        for (int i = 0; i < names.length; i++) {
            models = models.and(i == 0 ? "" : "_" + names[i], Models.generated(Constants.MOD_ID + ":items/cell_" + names[i]));
        }
        return models;
    }
    public static final RegistryObject<ItemAntimatterPellet> pellet_antimatter = REG.item(
                    "pellet_antimatter", p -> new ItemAntimatterPellet(ItemAntimatterPellet.properties(p)))
            .transform(Presets.sprite());

    public static final RegistryObject<ItemSingularity> singularity = register(
            "singularity", ItemSingularity.Kind.SINGULARITY);
    public static final RegistryObject<ItemSingularity> singularity_counter_resonant = register(
            "singularity_counter_resonant", ItemSingularity.Kind.COUNTER_RESONANT);
    public static final RegistryObject<ItemSingularity> singularity_super_heated = register(
            "singularity_super_heated", ItemSingularity.Kind.SUPER_HEATED);
    public static final RegistryObject<ItemSingularity> black_hole = register(
            "black_hole", ItemSingularity.Kind.BLACK_HOLE);
    public static final RegistryObject<ItemSingularity> singularity_spark = register(
            "singularity_spark", ItemSingularity.Kind.SPARK);

    static {
        ModTabs.addTo(GeoNtmTab.CONTROL, cell);
        ModTabs.addTo(GeoNtmTab.CONTROL, pellet_antimatter);
    }

    private static RegistryObject<ItemSingularity> register(String name, ItemSingularity.Kind kind) {
        RegistryObject<ItemSingularity> item = REG.item(name,
                        p -> new ItemSingularity(ItemSingularity.properties(p), kind))
                .transform(Presets.sprite());
        ModTabs.addTo(GeoNtmTab.CONTROL, item);
        return item;
    }

    public static void init() {}
}
