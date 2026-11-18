package fr.geoventure.geontm.items;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geontm.inventory.recipes.AssemblyMachineRecipes;
import fr.geoventure.geontm.inventory.recipes.PrecAssRecipes;
import fr.geoventure.geontm.inventory.recipes.loader.GenericRecipes;
import fr.geoventure.geontm.inventory.material.Mats;
import fr.geoventure.geontm.inventory.material.NTMMaterial;
import fr.geoventure.geontm.inventory.material.NTMMaterial.SmeltingBehavior;
import fr.geoventure.geontm.items.machine.ItemBlades;
import fr.geoventure.geontm.items.machine.ItemBlueprints;
import fr.geoventure.geontm.items.machine.ItemBlueprintFolder;
import fr.geoventure.geontm.items.machine.ItemBroken;
import fr.geoventure.geontm.items.machine.ItemFELCrystal;
import fr.geoventure.geontm.items.machine.ItemFELCrystal.EnumWavelengths;
import fr.geoventure.geontm.items.machine.ItemFluidId;
import fr.geoventure.geontm.items.machine.ItemFluidIDMulti;
import fr.geoventure.geontm.items.machine.ItemScraps;
import fr.geoventure.geontm.inventory.material.MaterialShapes;
import fr.geoventure.geontm.items.machine.ItemTurretBiometry;
import fr.geoventure.geontm.items.machine.ItemTurretChip;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import java.util.Locale;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.dejson.ItemModels;
import fr.geoventure.geocore.dejson.Models;
import fr.geoventure.geocore.dejson.Tints;
import net.minecraft.world.item.Item;
import fr.geoventure.geontm.items.machine.ItemArcElectrode;
import fr.geoventure.geontm.items.machine.ItemArcElectrodeBurnt;
import java.util.EnumMap;
import java.util.Map;

/**
 * Hand-registered machine items (templates, etc.) that aren't part of the generated {@link ModItems}.
 * Kept separate so codegen never clobbers them.
 */
public final class ModMachineItems {

    private ModMachineItems() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ITEMS);

    /** Turret targeting chip: the biometry card a turret accepts in its chip slot. */
    public static final RegistryObject<ItemTurretChip> turret_chip = REG.<ItemTurretChip>item(
            "turret_chip", ItemTurretChip::new, p -> p.stacksTo(1))
            .transform(Presets.sprite())
            .tab(GeoNtmTab.WEAPONS);

    /** Handheld biometric scanner: writes the holder's name onto a turret whitelist. */
    public static final RegistryObject<ItemTurretBiometry> turret_biometry = REG.<ItemTurretBiometry>item(
            "turret_biometry", ItemTurretBiometry::new, p -> p.stacksTo(1))
            .icon(d -> d.models(Models.generated("geontm:items/rei_scanner")));

    /** Assembly machine "plates" template: unlocks the metal-plate recipes. */
    public static final RegistryObject<ItemBlueprints> blueprint_plate = REG.<ItemBlueprints>item(
            "blueprint_plate", p -> new ItemBlueprints(p, AssemblyMachineRecipes.POOL_PLATES), p -> p.stacksTo(1))
            .icon(d -> d.models(Models.generated("geontm:items/blueprints")));

    /** Dynamic blueprint selected from a blueprint folder's pool. */
    public static final RegistryObject<ItemBlueprints> blueprints = REG.<ItemBlueprints>item(
            "blueprints", (Item.Properties p) -> new ItemBlueprints(p, null))
            .icon(d -> d.models(Models.generated("geontm:items/blueprints"))
                    // geontm:model_state: the pool's prefix (ItemBlueprints.modelState), never stored
                    .definition(ItemModels.select("geontm:model_state").when("1", ItemModels.model("geontm:item/blueprints_discover")).when("2", ItemModels.model("geontm:item/blueprints_secret")).when("3", ItemModels.model("geontm:item/blueprints_528")).fallback(ItemModels.model("geontm:item/blueprints"))));
    /**
     * The booklet which turns into a randomly selected blueprint on right-click: 1.7 metadata 0 ({@code blueprint_folder})
     * and 1 ({@code blueprint_folder_discover}), one item each as in ntm-next.
     */
    public static final RegistryObject<ItemBlueprintFolder> blueprint_folder = REG.<ItemBlueprintFolder>item(
            "blueprint_folder", p -> new ItemBlueprintFolder(p, 0), p -> p.stacksTo(1)).transform(Presets.sprite());
    public static final RegistryObject<ItemBlueprintFolder> blueprint_folder_discover = REG.<ItemBlueprintFolder>item(
            "blueprint_folder_discover", p -> new ItemBlueprintFolder(p, 1), p -> p.stacksTo(1)).transform(Presets.sprite());
    /** 1.7 metadata 2 (SECRET pools; dungeon loot only, 1.7 kept it out of the creative tab). */
    public static final RegistryObject<ItemBlueprintFolder> blueprint_folder_secret = REG.<ItemBlueprintFolder>item(
            "blueprint_folder_secret", p -> new ItemBlueprintFolder(p, 2), p -> p.stacksTo(1)).transform(Presets.sprite());
    /** Faulty output from precision-assembler recipes; the original item id is carried as data. */
    public static final RegistryObject<ItemBroken> broken_item = REG.<ItemBroken>item(
            "broken_item", ItemBroken::new)
            .transform(Presets.sprite());

    /** Fluid identifier: a dedicated one per fluid; set a tank/duct's type with it. */
    public static final RegistryObject<ItemFluidId> fluid_identifier = REG.<ItemFluidId>item(
            "fluid_identifier", ItemFluidId::new, p -> p.stacksTo(1))
            .transform(Presets.sprite("geontm:items/fluid_identifier_multi", "geontm:items/fluid_identifier_overlay").tints(Tints.constant(-1), Tints.customModelData(0, 0xFFFFFF)));

    // Shredder blades (wear parts; two per shredder). Durabilities are the 1.12.2 values; desh has
    // none, which means unbreakable (the original's maxDamage == 0).

    public static final RegistryObject<ItemBlades> blades_steel = REG.<ItemBlades>item(
            "blades_steel", ItemBlades::new, p -> p.stacksTo(1).durability(400))
            .transform(Presets.sprite());

    public static final RegistryObject<ItemBlades> blades_titanium = REG.<ItemBlades>item(
            "blades_titanium", ItemBlades::new, p -> p.stacksTo(1).durability(500))
            .transform(Presets.sprite());

    public static final RegistryObject<ItemBlades> blades_desh = REG.<ItemBlades>item(
            "blades_desh", ItemBlades::new, p -> p.stacksTo(1))
            .transform(Presets.sprite());

    /** Foundry scraps: solidified molten material (material + quanta amount in data components). */
    public static final RegistryObject<ItemScraps> scraps = REG.<ItemScraps>item(
            "scraps", ItemScraps::new, p -> p.stacksTo(64))
            .icon(declaration -> declaration.definition(ScrapsModels.definition(null)));

    // FEL laser crystals: the wavelength "lens" a Free Electron Laser is loaded with. Each one
    // unlocks the SILEX recipes at or below its wavelength and multiplies the speed of the weaker
    // ones (see TileEntitySILEX.process).

    public static final RegistryObject<ItemFELCrystal> laser_crystal_co2 = REG.<ItemFELCrystal>item(
            "laser_crystal_co2", (Item.Properties p) -> new ItemFELCrystal(p, EnumWavelengths.IR, false))
            .transform(Presets.sprite());
    public static final RegistryObject<ItemFELCrystal> laser_crystal_bismuth = REG.<ItemFELCrystal>item(
            "laser_crystal_bismuth", (Item.Properties p) -> new ItemFELCrystal(p, EnumWavelengths.VISIBLE, false))
            .transform(Presets.sprite());
    public static final RegistryObject<ItemFELCrystal> laser_crystal_cmb = REG.<ItemFELCrystal>item(
            "laser_crystal_cmb", (Item.Properties p) -> new ItemFELCrystal(p, EnumWavelengths.UV, false))
            .transform(Presets.sprite());
    public static final RegistryObject<ItemFELCrystal> laser_crystal_bale = REG.<ItemFELCrystal>item(
            "laser_crystal_bale", (Item.Properties p) -> new ItemFELCrystal(p, EnumWavelengths.GAMMA, false))
            .transform(Presets.sprite());
    /** The digamma crystal shows scrambled text instead of a description (upstream's easter egg). */
    public static final RegistryObject<ItemFELCrystal> laser_crystal_digamma = REG.<ItemFELCrystal>item(
            "laser_crystal_digamma", (Item.Properties p) -> new ItemFELCrystal(p, EnumWavelengths.DRX, true))
            .transform(Presets.sprite());

    // Large arc furnace electrodes (1.7 ItemArcElectrode / ItemArcElectrodeBurnt), fresh and burnt per type

    public static final RegistryObject<ItemArcElectrode> arc_electrode =
            arcElectrode("arc_electrode", ItemArcElectrode.ElectrodeType.GRAPHITE);
    /** 1.7's graphite electrode under its family id (structure crates and recipes name it explicitly). */
    public static final RegistryObject<ItemArcElectrode> arc_electrode_graphite =
            arcElectrode("arc_electrode_graphite", ItemArcElectrode.ElectrodeType.GRAPHITE);
    public static final RegistryObject<ItemArcElectrode> arc_electrode_lanthanium =
            arcElectrode("arc_electrode_lanthanium", ItemArcElectrode.ElectrodeType.LANTHANIUM);
    public static final RegistryObject<ItemArcElectrode> arc_electrode_desh =
            arcElectrode("arc_electrode_desh", ItemArcElectrode.ElectrodeType.DESH);
    public static final RegistryObject<ItemArcElectrode> arc_electrode_saturnite =
            arcElectrode("arc_electrode_saturnite", ItemArcElectrode.ElectrodeType.SATURNITE);

    public static final RegistryObject<ItemArcElectrodeBurnt> arc_electrode_burnt =
            arcElectrodeBurnt("arc_electrode_burnt", ItemArcElectrode.ElectrodeType.GRAPHITE);
    public static final RegistryObject<ItemArcElectrodeBurnt> arc_electrode_burnt_lanthanium =
            arcElectrodeBurnt("arc_electrode_burnt_lanthanium", ItemArcElectrode.ElectrodeType.LANTHANIUM);
    public static final RegistryObject<ItemArcElectrodeBurnt> arc_electrode_burnt_desh =
            arcElectrodeBurnt("arc_electrode_burnt_desh", ItemArcElectrode.ElectrodeType.DESH);
    public static final RegistryObject<ItemArcElectrodeBurnt> arc_electrode_burnt_saturnite =
            arcElectrodeBurnt("arc_electrode_burnt_saturnite", ItemArcElectrode.ElectrodeType.SATURNITE);

    private static final Map<ItemArcElectrode.ElectrodeType, RegistryObject<ItemArcElectrode>> ARC_ELECTRODES =
            new EnumMap<>(ItemArcElectrode.ElectrodeType.class);
    private static final Map<ItemArcElectrode.ElectrodeType, RegistryObject<ItemArcElectrodeBurnt>> ARC_ELECTRODES_BURNT =
            new EnumMap<>(ItemArcElectrode.ElectrodeType.class);

    static {
        ARC_ELECTRODES.put(ItemArcElectrode.ElectrodeType.GRAPHITE, arc_electrode);
        ARC_ELECTRODES.put(ItemArcElectrode.ElectrodeType.LANTHANIUM, arc_electrode_lanthanium);
        ARC_ELECTRODES.put(ItemArcElectrode.ElectrodeType.DESH, arc_electrode_desh);
        ARC_ELECTRODES.put(ItemArcElectrode.ElectrodeType.SATURNITE, arc_electrode_saturnite);
        ARC_ELECTRODES_BURNT.put(ItemArcElectrode.ElectrodeType.GRAPHITE, arc_electrode_burnt);
        ARC_ELECTRODES_BURNT.put(ItemArcElectrode.ElectrodeType.LANTHANIUM, arc_electrode_burnt_lanthanium);
        ARC_ELECTRODES_BURNT.put(ItemArcElectrode.ElectrodeType.DESH, arc_electrode_burnt_desh);
        ARC_ELECTRODES_BURNT.put(ItemArcElectrode.ElectrodeType.SATURNITE, arc_electrode_burnt_saturnite);
    }

    /** The fresh electrode item of a type. */
    public static ItemArcElectrode arcElectrode(ItemArcElectrode.ElectrodeType type) {
        return ARC_ELECTRODES.get(type).get();
    }

    /** The burnt electrode item of a type. */
    public static ItemArcElectrodeBurnt arcElectrodeBurnt(ItemArcElectrode.ElectrodeType type) {
        return ARC_ELECTRODES_BURNT.get(type).get();
    }

    private static RegistryObject<ItemArcElectrode> arcElectrode(String name, ItemArcElectrode.ElectrodeType type) {
        return REG.<ItemArcElectrode>item(name, p -> new ItemArcElectrode(p, type),
                        p -> p.stacksTo(1).durability(type.durability))
                .transform(Presets.handheld("geontm:items/arc_electrode." + type.name().toLowerCase(Locale.ROOT)));
    }

    private static RegistryObject<ItemArcElectrodeBurnt> arcElectrodeBurnt(String name, ItemArcElectrode.ElectrodeType type) {
        return REG.<ItemArcElectrodeBurnt>item(name, p -> new ItemArcElectrodeBurnt(p, type), p -> p.stacksTo(64))
                .transform(Presets.handheld("geontm:items/arc_electrode_burnt." + type.name().toLowerCase(Locale.ROOT)));
    }

    static {
        ModTabs.addTo(GeoNtmTab.TEMPLATES, blueprint_plate);
        ModTabs.addTo(GeoNtmTab.TEMPLATES, blueprints);
        ModTabs.addTo(GeoNtmTab.TEMPLATES, blueprint_folder);
        ModTabs.addTo(GeoNtmTab.TEMPLATES, blueprint_folder_discover); // 1.7 getSubItems: metadata 0 and 1
        ModTabs.addTo(GeoNtmTab.CONTROL, laser_crystal_co2);
        ModTabs.addTo(GeoNtmTab.CONTROL, laser_crystal_bismuth);
        ModTabs.addTo(GeoNtmTab.CONTROL, laser_crystal_cmb);
        ModTabs.addTo(GeoNtmTab.CONTROL, laser_crystal_bale);
        ModTabs.addTo(GeoNtmTab.CONTROL, laser_crystal_digamma);
        ModTabs.addTo(GeoNtmTab.CONTROL, blades_steel);
        ModTabs.addTo(GeoNtmTab.CONTROL, blades_titanium);
        ModTabs.addTo(GeoNtmTab.CONTROL, blades_desh);
        ModTabs.addTo(GeoNtmTab.CONTROL, arc_electrode);
        ModTabs.addTo(GeoNtmTab.CONTROL, arc_electrode_graphite);
        ModTabs.addTo(GeoNtmTab.CONTROL, arc_electrode_lanthanium);
        ModTabs.addTo(GeoNtmTab.CONTROL, arc_electrode_desh);
        ModTabs.addTo(GeoNtmTab.CONTROL, arc_electrode_saturnite);
        ModTabs.addTo(GeoNtmTab.CONTROL, arc_electrode_burnt);
        ModTabs.addTo(GeoNtmTab.CONTROL, arc_electrode_burnt_lanthanium);
        ModTabs.addTo(GeoNtmTab.CONTROL, arc_electrode_burnt_desh);
        ModTabs.addTo(GeoNtmTab.CONTROL, arc_electrode_burnt_saturnite);
        // Resolved at tab-build time, after Fluids.init(). The identifiers are 1.7's craftable multi identifier;
        // the single-fluid fluid_identifier stays registered for existing stacks only.
        ModTabs.addStacksTo(GeoNtmTab.TEMPLATES, () -> {
            List<ItemStack> list = new ArrayList<>();
            AssemblyMachineRecipes.INSTANCE.registerRecipes();
            PrecAssRecipes.INSTANCE.registerRecipes();
            for (String pool : GenericRecipes.blueprintPools.keySet()) {
                if (!pool.startsWith(GenericRecipes.POOL_PREFIX_SECRET)) list.add(ItemBlueprints.make(pool));
            }
            list.addAll(ItemFluidIDMulti.creativeStacks());
            return list;
        });
        // One ingot's worth of scraps per meltable material (the old ItemScraps creative subtypes).
        ModTabs.addStacksTo(GeoNtmTab.MATERIALS, () -> {
            List<ItemStack> list = new ArrayList<>();
            for (NTMMaterial mat : Mats.orderedList) {
                if (mat.smeltable == SmeltingBehavior.SMELTABLE || mat.smeltable == SmeltingBehavior.ADDITIVE) {
                    list.add(ItemScraps.create(new Mats.MaterialStack(mat, MaterialShapes.INGOT.quantity)));
                }
            }
            return list;
        });
    }

    public static void init() {}
}
