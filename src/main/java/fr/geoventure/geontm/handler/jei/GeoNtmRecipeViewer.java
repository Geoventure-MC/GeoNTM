package fr.geoventure.geontm.handler.jei;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.blocks.ModAnvilBlocks;
import fr.geoventure.geontm.blocks.ModProductionBlocks;
import fr.geoventure.geontm.blocks.ModProduction2Blocks;
import fr.geoventure.geontm.blocks.ModPwrBlocks;
import fr.geoventure.geontm.blocks.machine.rbmk.ModRBMKBlocks;
import fr.geoventure.geontm.blocks.machine.ModMachineBlocks;
import fr.geoventure.geontm.inventory.ModMenus;
import fr.geoventure.geontm.inventory.container.ContainerAnvil;
import fr.geoventure.geontm.inventory.container.ContainerDiFurnace;
import fr.geoventure.geontm.inventory.container.ContainerMachineArcFurnaceLarge;
import fr.geoventure.geontm.inventory.container.ContainerMachineArcWelder;
import fr.geoventure.geontm.inventory.container.ContainerMachineAssemblyFactory;
import fr.geoventure.geontm.inventory.container.ContainerMachineChemicalFactory;
import fr.geoventure.geontm.inventory.container.ContainerCentrifuge;
import fr.geoventure.geontm.inventory.container.ContainerMachineCyclotron;
import fr.geoventure.geontm.inventory.container.ContainerMachineElectrolyser;
import fr.geoventure.geontm.inventory.gui.GUIFusionBreeder;
import fr.geoventure.geontm.inventory.gui.GUIAnvil;
import fr.geoventure.geontm.inventory.gui.GUIDiFurnace;
import fr.geoventure.geontm.inventory.gui.GUIMachineArcFurnaceLarge;
import fr.geoventure.geontm.inventory.gui.GUIMachineArcWelder;
import fr.geoventure.geontm.inventory.container.ContainerMachineExposureChamber;
import fr.geoventure.geontm.inventory.container.ContainerMixer;
import fr.geoventure.geontm.inventory.container.ContainerMachineAmmoPress;
import fr.geoventure.geontm.inventory.container.ContainerMachinePrecAss;
import fr.geoventure.geontm.inventory.gui.GUICentrifuge;
import fr.geoventure.geontm.inventory.gui.GUIMachineCyclotron;
import fr.geoventure.geontm.inventory.gui.GUIMachineElectrolyser;
import fr.geoventure.geontm.inventory.gui.GUIMachineExposureChamber;
import fr.geoventure.geontm.inventory.gui.GUIMachineGasCent;
import fr.geoventure.geontm.inventory.gui.GUIMachineRotaryFurnace;
import fr.geoventure.geontm.inventory.gui.GUIMixer;
import fr.geoventure.geontm.inventory.gui.GUISILEX;
import fr.geoventure.geontm.inventory.gui.GUIBook;
import fr.geoventure.geontm.inventory.gui.GUIFurnaceCombination;
import fr.geoventure.geontm.inventory.gui.GUIPADetector;
import fr.geoventure.geontm.inventory.gui.GUIPASource;
import fr.geoventure.geontm.inventory.gui.ScreenRBMKOutgasser;
import fr.geoventure.geontm.inventory.recipes.ArcFurnaceRecipes;
import fr.geoventure.geontm.inventory.recipes.AssemblyMachineRecipes;
import fr.geoventure.geontm.inventory.recipes.BlastFurnaceRecipesNT;
import fr.geoventure.geontm.inventory.recipes.ChemicalPlantRecipes;
import fr.geoventure.geontm.inventory.recipes.CrucibleRecipes;
import fr.geoventure.geontm.inventory.recipes.CrystallizerRecipes;
import fr.geoventure.geontm.inventory.recipes.CentrifugeRecipes;
import fr.geoventure.geontm.inventory.recipes.CyclotronRecipes;
import fr.geoventure.geontm.inventory.recipes.FusionRecipes;
import fr.geoventure.geontm.inventory.recipes.GasCentrifugeRecipes;
import fr.geoventure.geontm.inventory.recipes.ElectrolyserFluidRecipes;
import fr.geoventure.geontm.inventory.recipes.ElectrolyserMetalRecipes;
import fr.geoventure.geontm.inventory.recipes.MixerRecipes;
import fr.geoventure.geontm.inventory.recipes.ExposureChamberRecipes;
import fr.geoventure.geontm.inventory.recipes.FluidBreederRecipes;
import fr.geoventure.geontm.inventory.recipes.PressRecipes;
import fr.geoventure.geontm.inventory.recipes.AmmoPressRecipes;
import fr.geoventure.geontm.inventory.recipes.PrecAssRecipes;
import fr.geoventure.geontm.items.ModSatelliteItems;
import fr.geoventure.geontm.items.machine.ItemSatellite.EnumSatType;
import fr.geoventure.geontm.inventory.recipes.SpaceAssemblerRecipes;
import fr.geoventure.geontm.inventory.recipes.PUREXRecipes;
import fr.geoventure.geontm.inventory.recipes.RockMillRecipes;
import fr.geoventure.geontm.inventory.recipes.SuperComputerRecipes;
import fr.geoventure.geontm.inventory.recipes.PlasmaForgeRecipes;
import fr.geoventure.geontm.inventory.recipes.SolderingRecipes;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.items.ModDataComponents;
import fr.geoventure.geontm.items.ModFluidItems;
import fr.geoventure.geontm.items.ModStorageDrumItems;
import fr.geoventure.geontm.items.machine.ItemFluidIcon;
import fr.geoventure.geontm.config.ClientConfig;
import fr.geoventure.geontm.items.weapon.sedna.ItemGunBaseNT;
import fr.geoventure.geontm.main.MainRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import fr.geoventure.geocore.data.ModelData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import fr.geoventure.geontm.blocks.ModSatelliteBlocks;
import fr.geoventure.geontm.inventory.recipes.anvil.AnvilRecipes.AnvilConstructionRecipe;
import fr.geoventure.geontm.handler.jei.JEIGenericRecipeHandler.Extras;
import fr.geoventure.geocore.viewer.TransferAction;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import fr.geoventure.geocore.viewer.ViewerCategory;
import fr.geoventure.geocore.viewer.ViewerPlugin;

/**
 * HBM's recipe-viewer integration, declared once for JEI, REI and EMI: the modern equivalent of the old {@code JEIConfig}.
 * The JEI plugins ({@link GeoNtmJeiPluginForgelike}, {@link GeoNtmJeiPluginFabric}) hand it to GeoCore's
 * {@code JeiViewer}, the REI plugins ({@code fr.geoventure.geontm.handler.rei}) to GeoCore's {@code ReiClientPlugin}, the EMI plugin
 * ({@code fr.geoventure.geontm.handler.emi}) to {@code EmiViewerPlugin}; the item
 * subtypes are {@link GeoNtmViewerSubtypes}. Adding a machine is a one-liner in each of the methods below, exactly as it
 * was in the old plugin.
 */
public final class GeoNtmRecipeViewer implements ViewerPlugin {

    public static final GeoNtmRecipeViewer INSTANCE = new GeoNtmRecipeViewer();

    private GeoNtmRecipeViewer() {}

    // Categories are created in categories() and reused in recipes() (each viewer calls both, in that order).
    private static JEIGenericRecipeHandler assembly;
    private static JEIGenericRecipeHandler precisionAssembler;
    private static JEIGenericRecipeHandler chemplant;
    private static JEIGenericRecipeHandler mixer;
    private static JEIGenericRecipeHandler exposure;
    private static JEIGenericRecipeHandler purex;
    private static JEIGenericRecipeHandler rockMill;
    private static JEIGenericRecipeHandler superComputer;
    private static JEIGenericRecipeHandler fusion;
    private static JEIGenericRecipeHandler fusionBreeder;
    private static JEIGenericRecipeHandler plasmaForge;
    private static JEIGenericRecipeHandler spaceAssembler;
    private static JEIGenericRecipeHandler blastFurnace;
    private static PressRecipeHandler press;
    private static AmmoPressRecipeHandler ammoPress;
    private static SolderingStationRecipeHandler soldering;
    private static CrystallizerRecipeHandler crystallizer;
    private static CentrifugeRecipeHandler centrifuge;
    private static CyclotronRecipeHandler cyclotron;
    private static ParticleAcceleratorRecipeHandler particleAccelerator;
    private static GasCentrifugeRecipeHandler gasCentrifuge;
    private static ElectrolyserFluidRecipeHandler electrolyserFluid;
    private static ElectrolyserMetalRecipeHandler electrolyserMetal;
    private static SILEXRecipeHandler silex;
    private static ShredderRecipeHandler shredder;
    private static SawmillRecipeHandler sawmill;
    private static CrucibleAlloyingHandler crucible;
    private static AnvilConstructionRecipeHandler anvilCon;
    private static AnvilSmithingRecipeHandler anvilSmith;
    private static RefineryRecipeHandler refinery;
    private static FractioningRecipeHandler fractioning;
    private static LiquefactionRecipeHandler liquefaction;
    private static SolidificationRecipeHandler solidification;
    private static BoilingRecipeHandler boiling;
    private static CoolingRecipeHandler cooling;
    private static VacuumRecipeHandler vacuum;
    private static ReformingRecipeHandler reforming;
    private static CrackingRecipeHandler cracking;
    private static HydrotreatingRecipeHandler hydrotreating;
    private static CokerRecipeHandler coker;
    private static PyrolysisRecipeHandler pyrolysis;
    private static CombinationRecipeHandler combination;
    private static CompressorRecipeHandler compressor;
    private static UniversalRecipeHandler storageDrum;
    private static FuelPoolRecipeHandler fuelPool;
    private static ArcWelderRecipeHandler arcWelder;
    private static RotaryFurnaceRecipeHandler rotaryFurnace;
    private static ArcFurnaceRecipeHandler arcFurnaceSolid;
    private static ArcFurnaceRecipeHandler arcFurnaceFluid;
    private static DiFurnaceRecipeHandler diFurnace;
    private static FluidContainerRecipeHandler fluidContainers;
    private static ConstructionRecipeHandler construction;
    private static ToolingRecipeHandler tooling;
    private static AshpitRecipeHandler ashpit;
    private static CrucibleSmeltingHandler crucibleSmelting;
    private static CrucibleCastingHandler crucibleCasting;
    private static BookRecipeHandler book;

    /** Add a droplet icon for every registered fluid to the viewers' ingredient lists / search: the fluid‑icon item
     * lives in no creative tab, so without this only the fluids that happen to appear in a ported recipe
     * would be searchable. (Modern equivalent of the old {@code FluidIconRecipeRegistryPlugin} seam.) */
    @Override
    public void ingredients(Ingredients reg) {
        List<ItemStack> icons = new ArrayList<>();
        for (FluidType fluid : Fluids.getInNiceOrder()) { // 1.7 ItemFluidIcon.getSubItems
            if (fluid == Fluids.NONE) continue;
            ItemStack icon = ItemFluidIcon.make(fluid);
            if (!icon.isEmpty()) icons.add(icon);
        }
        reg.addItemStacks(icons);
    }

    /** 1.7 {@code NEIConfig}'s {@code API.hideItem} list; ids 1.7 had that the port folded away are skipped. */
    private static final Set<String> HIDDEN = Set.of(
            "machine_electric_furnace_on", "machine_difurnace_on", "machine_rtg_furnace_on", "reinforced_lamp_on",
            "statue_elb_f", "euphemium_kit", "bobmazon_hidden", "book_lore",
            "dummy_block_blast", "dummy_port_compact_launcher", "dummy_port_launch_table",
            "dummy_plate_compact_launcher", "dummy_plate_launch_table",
            "pink_log", "pink_planks", "pink_slab", "pink_double_slab", "pink_stairs",
            "spotlight_incandescent_off", "spotlight_fluoro_off", "spotlight_halogen_off", "spotlight_beam",
            "conveyor", "conveyor_chute", "conveyor_lift", "conveyor_express", "conveyor_double", "conveyor_triple",
            "coal_eternal");
    private static final Set<String> HIDDEN_UNLESS_POLAROID_11 = Set.of("book_secret", "book_of_", "burnt_bark",
            "ams_core_thingy");

    @Override
    public void hidden(Ingredients reg) {
        boolean secrets = ClientConfig.NEI_HIDE_SECRETS;
        List<ItemStack> hidden = new ArrayList<>();
        for (Item item : BuiltInRegistries.ITEM) {
            var id = BuiltInRegistries.ITEM.getKey(item);
            if (!id.getNamespace().equals(Constants.MOD_ID)) continue;
            String path = id.getPath();
            // 1.7 hid brick_forgotten and brick_forgotten_lock at every meta; each meta is its own id here.
            // port: the universal bucket exists only for old stacks
            boolean hide = fr.geoventure.geontm.perimetre.Perimetre.retire(path)
                    || fr.geoventure.geontm.perimetre.Perimetre.simplifie(path)
                    || HIDDEN.contains(path) || path.equals("fluid_bucket") || path.startsWith("brick_forgotten")
                    || MainRegistry.polaroidID != 11 && HIDDEN_UNLESS_POLAROID_11.contains(path)
                    || secrets && (ItemGunBaseNT.secrets.contains(item)
                            || path.startsWith("ammo_secret_") || path.startsWith("item_secret_"));
            if (hide) hidden.add(new ItemStack(item));
        }
        reg.addItemStacks(hidden);
    }

    @Override
    public void categories(Categories viewer) {
        List<ViewerCategory<?>> all = new ArrayList<>();
        Categories reg = added -> all.addAll(List.of(added));
        assembly = new JEIGenericRecipeHandler(GeoNtmJeiRecipeTypes.ASSEMBLY_MACHINE,
                title("assembly_machine"), AssemblyMachineRecipes.INSTANCE, stack(ModMachineBlocks.machine_assembly_machine.get()))
                .extras(Extras.POWER);
        precisionAssembler = new JEIGenericRecipeHandler(GeoNtmJeiRecipeTypes.PRECISION_ASSEMBLER,
                title("precass"), PrecAssRecipes.INSTANCE, stack(ModMachineBlocks.machine_precass.get())).extras(Extras.POWER);
        chemplant = new JEIGenericRecipeHandler(GeoNtmJeiRecipeTypes.CHEMICAL_PLANT,
                title("chemical_plant"), ChemicalPlantRecipes.INSTANCE, stack(ModMachineBlocks.machine_chemical_plant.get()))
                .extras(Extras.POWER);
        mixer = new JEIGenericRecipeHandler(GeoNtmJeiRecipeTypes.MIXER,
                title("mixer"), MixerRecipes.INSTANCE, stack(ModMachineBlocks.machine_mixer.get()));
        exposure = new JEIGenericRecipeHandler(GeoNtmJeiRecipeTypes.EXPOSURE_CHAMBER,
                title("exposure_chamber"), ExposureChamberRecipes.INSTANCE,
                stack(ModMachineBlocks.machine_exposure_chamber.get()));
        purex = new JEIGenericRecipeHandler(GeoNtmJeiRecipeTypes.PUREX,
                title("purex"), PUREXRecipes.INSTANCE, stack(ModMachineBlocks.machine_purex.get())).extras(Extras.PUREX);
        rockMill = new JEIGenericRecipeHandler(GeoNtmJeiRecipeTypes.ROCK_MILL,
                Component.translatable("block.geontm.machine_rockmill"), RockMillRecipes.INSTANCE, stack(ModMachineBlocks.machine_rockmill.get()))
                .extras(Extras.POWER);
        superComputer = new JEIGenericRecipeHandler(GeoNtmJeiRecipeTypes.SUPERCOMPUTER,
                Component.translatable("block.geontm.machine_supercomputer"), SuperComputerRecipes.INSTANCE, stack(ModMachineBlocks.machine_supercomputer.get()))
                .extras(Extras.POWER);
        fusion = new JEIGenericRecipeHandler(GeoNtmJeiRecipeTypes.FUSION,
                title("fusion"), FusionRecipes.INSTANCE,
                stack(ModMachineBlocks.fusion_torus.get())).extras(Extras.IGNITION);
        fusionBreeder = new JEIGenericRecipeHandler(GeoNtmJeiRecipeTypes.FUSION_BREEDER,
                title("fusionbreeder"), FluidBreederRecipes.INSTANCE,
                stack(ModMachineBlocks.fusion_breeder.get()));
        ItemStack zeroG = ModSatelliteItems.satellite.stack(EnumSatType.SCIENCE_ASSEMBLER);
        spaceAssembler = new JEIGenericRecipeHandler(GeoNtmJeiRecipeTypes.SPACE_ASSEMBLER,
                zeroG.getHoverName(), SpaceAssemblerRecipes.INSTANCE, zeroG).extras(Extras.DURATION);
        plasmaForge = new JEIGenericRecipeHandler(GeoNtmJeiRecipeTypes.PLASMA_FORGE,
                title("plasma_forge"), PlasmaForgeRecipes.INSTANCE,
                stack(ModMachineBlocks.fusion_plasma_forge.get())).extras(Extras.IGNITION);
        blastFurnace = new JEIGenericRecipeHandler(GeoNtmJeiRecipeTypes.BLAST_FURNACE,
                Component.translatable("block.geontm.machine_blast_furnace"), BlastFurnaceRecipesNT.INSTANCE,
                stack(ModMachineBlocks.machine_blast_furnace.get())).extras(Extras.DURATION);
         press = new PressRecipeHandler(GeoNtmJeiRecipeTypes.PRESS, title("press"),
                 stack(ModMachineBlocks.machine_press.get()));
         ammoPress = new AmmoPressRecipeHandler(GeoNtmJeiRecipeTypes.AMMO_PRESS, title("ammo_press"),
                 stack(ModMachineBlocks.machine_ammo_press.get()));
        soldering = new SolderingStationRecipeHandler(GeoNtmJeiRecipeTypes.SOLDERING_STATION,
                title("soldering_station"), stack(ModMachineBlocks.machine_soldering_station.get()));
        crystallizer = new CrystallizerRecipeHandler(GeoNtmJeiRecipeTypes.CRYSTALLIZER,
                title("crystallizer"), stack(ModMachineBlocks.machine_crystallizer.get()));
        centrifuge = new CentrifugeRecipeHandler(GeoNtmJeiRecipeTypes.CENTRIFUGE,
                title("centrifuge"), stack(ModMachineBlocks.machine_centrifuge.get()));
        cyclotron = new CyclotronRecipeHandler(GeoNtmJeiRecipeTypes.CYCLOTRON,
                title("cyclotron"), stack(ModMachineBlocks.machine_cyclotron.get()));
        particleAccelerator = new ParticleAcceleratorRecipeHandler(GeoNtmJeiRecipeTypes.PARTICLE_ACCELERATOR,
                title("particle_accelerator"), stack(ModMachineBlocks.pa_detector.get()));
        gasCentrifuge = new GasCentrifugeRecipeHandler(GeoNtmJeiRecipeTypes.GAS_CENTRIFUGE,
                title("gas_centrifuge"), stack(ModMachineBlocks.machine_gascent.get()));
        electrolyserFluid = new ElectrolyserFluidRecipeHandler(GeoNtmJeiRecipeTypes.ELECTROLYSIS_FLUID,
                title("electrolysis_fluid"), stack(ModMachineBlocks.machine_electrolyser.get()));
        electrolyserMetal = new ElectrolyserMetalRecipeHandler(GeoNtmJeiRecipeTypes.ELECTROLYSIS_METAL,
                title("electrolysis_metal"), stack(ModMachineBlocks.machine_electrolyser.get()));
        silex = new SILEXRecipeHandler(GeoNtmJeiRecipeTypes.SILEX,
                title("silex"), stack(ModMachineBlocks.machine_silex.get()));
        fluidContainers = new FluidContainerRecipeHandler(GeoNtmJeiRecipeTypes.FLUID_CONTAINERS, title("fluid_containers"));
        construction = new ConstructionRecipeHandler(GeoNtmJeiRecipeTypes.CONSTRUCTION, title("construction"),
                ConstructionRecipeHandler.stations());
        tooling = new ToolingRecipeHandler(GeoNtmJeiRecipeTypes.TOOLING, title("tooling"), ToolingRecipeHandler.stations());
        ashpit = new AshpitRecipeHandler(GeoNtmJeiRecipeTypes.ASHPIT, Component.translatable("block.geontm.machine_ashpit"),
                stack(ModMachineBlocks.machine_ashpit.get()));
        crucibleSmelting = new CrucibleSmeltingHandler(GeoNtmJeiRecipeTypes.CRUCIBLE_SMELTING, title("crucible_smelting"),
                stack(ModMachineBlocks.machine_crucible.get()));
        crucibleCasting = new CrucibleCastingHandler(GeoNtmJeiRecipeTypes.CRUCIBLE_CASTING, title("crucible_casting"),
                CrucibleCastingHandler.stations()[0]);
        book = new BookRecipeHandler(GeoNtmJeiRecipeTypes.BOOK, title("book_of_boxcars"));
        shredder = new ShredderRecipeHandler(GeoNtmJeiRecipeTypes.SHREDDER,
                title("shredder"), stack(ModMachineBlocks.machine_shredder.get()));
        sawmill = new SawmillRecipeHandler(GeoNtmJeiRecipeTypes.SAWMILL,
                title("sawmill"), stack(ModMachineBlocks.machine_sawmill.get()));
        crucible = new CrucibleAlloyingHandler(GeoNtmJeiRecipeTypes.CRUCIBLE_ALLOY,
                title("crucible_alloy"), stack(ModMachineBlocks.machine_crucible.get()));
        anvilCon = new AnvilConstructionRecipeHandler(GeoNtmJeiRecipeTypes.ANVIL_CON,
                title("anvil_construction"), anvilIcon());
        anvilSmith = new AnvilSmithingRecipeHandler(GeoNtmJeiRecipeTypes.ANVIL_SMITH,
                title("anvil_smithing"), anvilIcon());

        refinery = new RefineryRecipeHandler(GeoNtmJeiRecipeTypes.REFINERY,
                title("refinery"), stack(ModMachineBlocks.machine_refinery.get()));
        fractioning = new FractioningRecipeHandler(GeoNtmJeiRecipeTypes.FRACTIONING,
                title("fractioning"), stack(ModMachineBlocks.machine_fraction_tower.get()));
        liquefaction = new LiquefactionRecipeHandler(GeoNtmJeiRecipeTypes.LIQUEFACTION,
                title("liquefaction"), stack(ModMachineBlocks.machine_liquefactor.get()));
        solidification = new SolidificationRecipeHandler(GeoNtmJeiRecipeTypes.SOLIDIFICATION,
                title("solidification"), stack(ModMachineBlocks.machine_solidifier.get()));
        boiling = new BoilingRecipeHandler(GeoNtmJeiRecipeTypes.BOILING,
                title("boiling"), stack(ModMachineBlocks.machine_boiler.get()));
        cooling = new CoolingRecipeHandler(GeoNtmJeiRecipeTypes.COOLING,
                title("cooling"), stack(ModMachineBlocks.machine_large_turbine.get()));

        vacuum = new VacuumRecipeHandler(GeoNtmJeiRecipeTypes.VACUUM,
                title("vacuum_distiller"), stack(ModMachineBlocks.machine_vacuum_distill.get()));
        reforming = new ReformingRecipeHandler(GeoNtmJeiRecipeTypes.REFORMING,
                title("reforming"), stack(ModMachineBlocks.machine_catalytic_reformer.get()));
        cracking = new CrackingRecipeHandler(GeoNtmJeiRecipeTypes.CRACKING,
                title("cracking"), stack(ModMachineBlocks.machine_catalytic_cracker.get()));
        hydrotreating = new HydrotreatingRecipeHandler(GeoNtmJeiRecipeTypes.HYDROTREATING,
                title("hydrotreating"), stack(ModMachineBlocks.machine_hydrotreater.get()));
        coker = new CokerRecipeHandler(GeoNtmJeiRecipeTypes.COKER,
                title("coker"), stack(ModMachineBlocks.machine_coker.get()));
        pyrolysis = new PyrolysisRecipeHandler(GeoNtmJeiRecipeTypes.PYROLYSIS,
                title("pyrolysis"), stack(ModMachineBlocks.machine_pyrooven.get()));
        combination = new CombinationRecipeHandler(GeoNtmJeiRecipeTypes.COMBINATION,
                Component.translatable("block.geontm.machine_furnace_combination"),
                stack(ModMachineBlocks.machine_furnace_combination.get()));
        compressor = new CompressorRecipeHandler(GeoNtmJeiRecipeTypes.COMPRESSOR,
                title("compressor"), stack(ModMachineBlocks.machine_compressor.get()));
        storageDrum = new UniversalRecipeHandler(GeoNtmJeiRecipeTypes.STORAGE_DRUM, Component.literal("Nuclear Waste Decay"),
                stack(ModMachineBlocks.machine_storage_drum.get()));
        fuelPool = new FuelPoolRecipeHandler(GeoNtmJeiRecipeTypes.FUEL_POOL,
                Component.translatable("block.geontm.machine_waste_drum"), stack(ModMachineBlocks.machine_waste_drum.get()));
        arcWelder = new ArcWelderRecipeHandler(GeoNtmJeiRecipeTypes.ARC_WELDER,
                Component.translatable("block.geontm.machine_arc_welder"),
                stack(ModMachineBlocks.machine_arc_welder.get()));
        arcFurnaceSolid = new ArcFurnaceRecipeHandler(GeoNtmJeiRecipeTypes.ARC_FURNACE_SOLID,
                Component.translatable("block.geontm.machine_arc_furnace"), false,
                stack(ModMachineBlocks.machine_arc_furnace.get()));
        arcFurnaceFluid = new ArcFurnaceRecipeHandler(GeoNtmJeiRecipeTypes.ARC_FURNACE_FLUID,
                Component.translatable("block.geontm.machine_arc_furnace"), true,
                stack(ModMachineBlocks.machine_arc_furnace.get()));
        diFurnace = new DiFurnaceRecipeHandler(GeoNtmJeiRecipeTypes.ALLOY,
                Component.translatable("block.geontm.machine_difurnace_off"));
        rotaryFurnace = new RotaryFurnaceRecipeHandler(GeoNtmJeiRecipeTypes.ROTARY_FURNACE,
                title("rotary_furnace"), stack(ModProductionBlocks.MACHINE_ROTARY_FURNACE.get()));

          reg.add(assembly, precisionAssembler, chemplant, mixer, exposure, purex, rockMill, superComputer, fusion, fusionBreeder, plasmaForge, spaceAssembler, blastFurnace, press, ammoPress, soldering, crystallizer, centrifuge, cyclotron, particleAccelerator, gasCentrifuge, silex, shredder, sawmill, crucible, anvilCon, anvilSmith,
                 electrolyserFluid, electrolyserMetal, refinery, fractioning, liquefaction, solidification, boiling, cooling,
                 vacuum, reforming, cracking, hydrotreating, coker, pyrolysis, combination, compressor, storageDrum, fuelPool,
                 arcWelder, arcFurnaceSolid, arcFurnaceFluid, diFurnace, rotaryFurnace,
                 fluidContainers, construction, tooling, ashpit, crucibleSmelting, crucibleCasting, book);
        // 1.7's display names: literal strings or the machine's name
        reg.add(new UniversalRecipeHandler(GeoNtmJeiRecipeTypes.ANNIHILATOR, Component.literal("Annihilator"),
                        stack(ModProduction2Blocks.MACHINE_ANNIHILATOR.get())),
                new UniversalRecipeHandler(GeoNtmJeiRecipeTypes.ORE_SLOPPER, ModProduction2Blocks.MACHINE_ORE_SLOPPER.get().getName(),
                        stack(ModProduction2Blocks.MACHINE_ORE_SLOPPER.get())),
                new UniversalRecipeHandler(GeoNtmJeiRecipeTypes.RBMK_DISASSEMBLY, Component.literal("RBMK Rod Disassembly"),
                        stack(Blocks.CRAFTING_TABLE)),
                new UniversalRecipeHandler(GeoNtmJeiRecipeTypes.GRENADE, Component.literal("Grenade Crafting"), stack(Blocks.CRAFTING_TABLE)),
                new UniversalRecipeHandler(GeoNtmJeiRecipeTypes.ZIRNOX, ModMachineBlocks.machine_zirnox.get().getName(),
                        stack(ModMachineBlocks.machine_zirnox.get())),
                new UniversalRecipeHandler(GeoNtmJeiRecipeTypes.PWR, ModPwrBlocks.PWR_CONTROLLER.get().getName(),
                        stack(ModPwrBlocks.PWR_CONTROLLER.get())),
                new UniversalRecipeHandler(GeoNtmJeiRecipeTypes.WATZ, ModMachineBlocks.watz.get().getName(), stack(ModMachineBlocks.watz.get())),
                new UniversalRecipeHandler(GeoNtmJeiRecipeTypes.OUTGASSER, ModRBMKBlocks.rbmk_outgasser.get().getName(),
                        stack(ModRBMKBlocks.rbmk_outgasser.get()), stack(ModMachineBlocks.fusion_breeder.get())),
                new UniversalRecipeHandler(GeoNtmJeiRecipeTypes.DEUTERIUM, ModMachineBlocks.machine_deuterium_extractor.get().getName(),
                        stack(ModMachineBlocks.machine_deuterium_extractor.get()), stack(ModMachineBlocks.machine_deuterium_tower.get())));
        RTGJeiIntegration.registerCategory(reg);
        reg.add(new SatelliteRecipeHandler());
        NuclearMiscJeiIntegration.registerCategories(reg);
        CustomMachineJeiIntegration.registerCategories(reg);
        all.sort(Comparator.comparingInt(GeoNtmRecipeViewer::neiPosition));
        viewer.add(all.toArray(ViewerCategory<?>[]::new));
    }

    /**
     * 1.7's tab order: {@code NEIRegistry.listAllHandlers}, then the custom machines {@code NEIConfig} adds. Port-only
     * categories go after 1.7's last tab, before the custom machines.
     */
    public static final List<String> NEI_ORDER = List.of("tooling", "construction", "annihilator", "soldering_station",
            "blast_furnace", "anvil_construction", "anvil_smithing", "press", "shredder", "crucible_smelting",
            "crucible_alloy", "crucible_casting", "assembly_machine", "precass", "chemical_plant", "refinery", "centrifuge",
            "gas_centrifuge", "cyclotron", "vacuum_distiller", "cracking", "radiolysis", "reforming", "hydrotreating", "purex",
            "ore_slopper", "crystallizer", "book_of_boxcars", "fusion", "plasma_forge", "silex", "fuel_pool",
            "rbmk_disassembly", "storage_drum", "satellite", "grenade", "zirnox", "pwr", "watz", "rtg", "liquefaction",
            "solidification", "coker", "pyrolysis", "fractioning", "boiling", "combination", "sawmill", "mixer", "outgasser",
            "fusionbreeder", "electrolysis_fluid", "electrolysis_metal", "ashpit", "arc_welder", "exposure_chamber",
            "arc_furnace_solid", "arc_furnace_fluid", "rotary_furnace", "ammo_press", "compressor", "particle_accelerator",
            "deuterium", "rock_mill", "supercomputer", "space_assembler", "fluid_containers");

    static int neiPosition(ViewerCategory<?> category) {
        if (category instanceof CustomMachineRecipeHandler) return NEI_ORDER.size() + 1;
        int index = NEI_ORDER.indexOf(category.type().path());
        return index < 0 ? NEI_ORDER.size() : index;
    }

    @Override
    public void recipes(Recipes reg) {
         AssemblyMachineRecipes.INSTANCE.registerRecipes();
         PrecAssRecipes.INSTANCE.registerRecipes();
        ChemicalPlantRecipes.INSTANCE.registerRecipes();
        MixerRecipes.INSTANCE.registerRecipes();
        ExposureChamberRecipes.INSTANCE.registerRecipes();
        PUREXRecipes.INSTANCE.registerRecipes();
        RockMillRecipes.INSTANCE.registerRecipes();
        SuperComputerRecipes.INSTANCE.registerRecipes();
        FusionRecipes.INSTANCE.registerRecipes();
        FluidBreederRecipes.INSTANCE.registerRecipes();
        PlasmaForgeRecipes.INSTANCE.registerRecipes();
        SpaceAssemblerRecipes.INSTANCE.registerRecipes();
         PressRecipes.register();
         AmmoPressRecipes.register();
        SolderingRecipes.register();
        CrystallizerRecipes.register();
        CentrifugeRecipes.register();
        CyclotronRecipes.register();
        GasCentrifugeRecipes.register();
        ElectrolyserFluidRecipes.register();
        ElectrolyserMetalRecipes.register();

         reg.add(GeoNtmJeiRecipeTypes.ASSEMBLY_MACHINE, assembly.buildRecipes());
         reg.add(GeoNtmJeiRecipeTypes.PRECISION_ASSEMBLER, precisionAssembler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.CHEMICAL_PLANT, chemplant.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.MIXER, mixer.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.EXPOSURE_CHAMBER, exposure.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.PUREX, purex.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.ROCK_MILL, rockMill.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.SUPERCOMPUTER, superComputer.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.FUSION, fusion.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.FUSION_BREEDER, fusionBreeder.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.PLASMA_FORGE, plasmaForge.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.SPACE_ASSEMBLER, spaceAssembler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.BLAST_FURNACE, blastFurnace.buildRecipes());
         reg.add(GeoNtmJeiRecipeTypes.PRESS, PressRecipes.getRecipes());
         reg.add(GeoNtmJeiRecipeTypes.AMMO_PRESS, AmmoPressRecipes.getRecipes());
        reg.add(GeoNtmJeiRecipeTypes.SOLDERING_STATION, SolderingRecipes.recipes);
         reg.add(GeoNtmJeiRecipeTypes.CRYSTALLIZER, CrystallizerRecipes.recipes);
         reg.add(GeoNtmJeiRecipeTypes.CENTRIFUGE, CentrifugeRecipes.recipes);
         reg.add(GeoNtmJeiRecipeTypes.CYCLOTRON, CyclotronRecipes.recipes);
         reg.add(GeoNtmJeiRecipeTypes.PARTICLE_ACCELERATOR,
                 ParticleAcceleratorRecipeHandler.buildRecipes());
         reg.add(GeoNtmJeiRecipeTypes.GAS_CENTRIFUGE, GasCentrifugeRecipes.RECIPES);
        reg.add(GeoNtmJeiRecipeTypes.ELECTROLYSIS_FLUID, ElectrolyserFluidRecipeHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.ELECTROLYSIS_METAL, ElectrolyserMetalRecipeHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.SILEX, SILEXRecipeHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.FLUID_CONTAINERS, FluidContainerRecipeHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.CONSTRUCTION, ConstructionRecipeHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.TOOLING, ToolingRecipeHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.ASHPIT, AshpitRecipeHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.CRUCIBLE_SMELTING, CrucibleSmeltingHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.CRUCIBLE_CASTING, CrucibleCastingHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.BOOK, BookRecipeHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.SHREDDER, ShredderRecipeHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.SAWMILL, SawmillRecipeHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.CRUCIBLE_ALLOY, CrucibleAlloyingHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.ANVIL_CON, AnvilConstructionRecipeHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.ANVIL_SMITH, AnvilSmithingRecipeHandler.buildRecipes());

        reg.add(GeoNtmJeiRecipeTypes.REFINERY, RefineryRecipeHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.FRACTIONING, FractioningRecipeHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.LIQUEFACTION, LiquefactionRecipeHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.SOLIDIFICATION, SolidificationRecipeHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.BOILING, BoilingRecipeHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.COOLING, CoolingRecipeHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.VACUUM, VacuumRecipeHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.REFORMING, ReformingRecipeHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.CRACKING, CrackingRecipeHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.HYDROTREATING, HydrotreatingRecipeHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.COKER, CokerRecipeHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.PYROLYSIS, PyrolysisRecipeHandler.buildRecipes());
        reg.add(GeoNtmJeiRecipeTypes.COMBINATION, CombinationRecipeHandler.buildRecipes());
         reg.add(GeoNtmJeiRecipeTypes.COMPRESSOR, CompressorRecipeHandler.buildRecipes());
         reg.add(GeoNtmJeiRecipeTypes.STORAGE_DRUM, UniversalRecipeHandler.wasteDecay());
        reg.add(GeoNtmJeiRecipeTypes.ANNIHILATOR, UniversalRecipeHandler.annihilator());
        reg.add(GeoNtmJeiRecipeTypes.ORE_SLOPPER, UniversalRecipeHandler.oreSlopper());
        reg.add(GeoNtmJeiRecipeTypes.RBMK_DISASSEMBLY, UniversalRecipeHandler.rbmkDisassembly());
        reg.add(GeoNtmJeiRecipeTypes.GRENADE, UniversalRecipeHandler.grenade());
        reg.add(GeoNtmJeiRecipeTypes.ZIRNOX, UniversalRecipeHandler.zirnox());
        reg.add(GeoNtmJeiRecipeTypes.PWR, UniversalRecipeHandler.pwr());
        reg.add(GeoNtmJeiRecipeTypes.WATZ, UniversalRecipeHandler.watz());
        reg.add(GeoNtmJeiRecipeTypes.OUTGASSER, UniversalRecipeHandler.outgasser());
        reg.add(GeoNtmJeiRecipeTypes.DEUTERIUM, UniversalRecipeHandler.deuterium());
         reg.add(GeoNtmJeiRecipeTypes.FUEL_POOL, FuelPoolRecipeHandler.buildRecipes());
         reg.add(GeoNtmJeiRecipeTypes.ARC_WELDER, ArcWelderRecipeHandler.buildRecipes());
         reg.add(GeoNtmJeiRecipeTypes.ROTARY_FURNACE, RotaryFurnaceRecipeHandler.buildRecipes());
         reg.add(GeoNtmJeiRecipeTypes.ARC_FURNACE_SOLID,
                 ArcFurnaceRecipeHandler.buildRecipes(false));
         reg.add(GeoNtmJeiRecipeTypes.ARC_FURNACE_FLUID,
                 ArcFurnaceRecipeHandler.buildRecipes(true));
          reg.add(GeoNtmJeiRecipeTypes.ALLOY, DiFurnaceRecipeHandler.buildRecipes());
        RTGJeiIntegration.registerRecipes(reg);
        reg.add(SatelliteRecipeHandler.TYPE, SatelliteRecipeHandler.buildRecipes());
        NuclearMiscJeiIntegration.registerRecipes(reg);
        CustomMachineJeiIntegration.registerRecipes(reg);
      }

    @Override
    public void clickAreas(ClickAreas reg) {
        reg.add(GUIMixer.class, 62, 36, 52, 44,
                GeoNtmJeiRecipeTypes.MIXER);
        reg.add(GUIMachineExposureChamber.class, 36, 40, 40, 8,
                GeoNtmJeiRecipeTypes.EXPOSURE_CHAMBER);
        reg.add(GUICentrifuge.class, 35, 9, 106, 40,
                GeoNtmJeiRecipeTypes.CENTRIFUGE);
        // 1.7.10's NEI rect (43, 16, 34 x 34): the earlier 134 x 54 area covered all nine slots.
        reg.add(GUIMachineCyclotron.class, 43, 16, 34, 34,
                GeoNtmJeiRecipeTypes.CYCLOTRON);
        // The arrow between the two slot rows, as in 1.12.2 (75, 35, 7 x 8): a 36 x 45 area here covered all
        // four slots, and JEI took every click on them, so nothing could be put into the source by hand.
        reg.add(GUIPADetector.class, 75, 35, 7, 8,
                GeoNtmJeiRecipeTypes.PARTICLE_ACCELERATOR);
        reg.add(GUIPASource.class, 75, 35, 7, 8,
                GeoNtmJeiRecipeTypes.PARTICLE_ACCELERATOR);
        reg.add(GUIMachineGasCent.class, 70, 36, 36, 12,
                GeoNtmJeiRecipeTypes.GAS_CENTRIFUGE);
        reg.add(GUIMachineElectrolyser.class, 62, 26, 12, 40,
                GeoNtmJeiRecipeTypes.ELECTROLYSIS_FLUID);
        reg.add(GUIMachineElectrolyser.class, 7, 46, 22, 25,
                GeoNtmJeiRecipeTypes.ELECTROLYSIS_METAL);
        reg.add(GUIFusionBreeder.class, 67, 49, 42, 9,
                GeoNtmJeiRecipeTypes.FUSION_BREEDER);
        reg.add(GUIMachineArcWelder.class, 72, 38, 32, 13,
                GeoNtmJeiRecipeTypes.ARC_WELDER);
        reg.add(GUIMachineRotaryFurnace.class, 58, 19, 32, 10,
                GeoNtmJeiRecipeTypes.ROTARY_FURNACE);
        reg.add(GUIMachineArcFurnaceLarge.class, 17, 36, 7, 70,
                GeoNtmJeiRecipeTypes.ARC_FURNACE_SOLID);
        reg.add(GUIMachineArcFurnaceLarge.class, 152, 36, 16, 70,
                GeoNtmJeiRecipeTypes.ARC_FURNACE_FLUID);
        reg.add(GUIDiFurnace.class, 102, 36, 21, 14,
                GeoNtmJeiRecipeTypes.ALLOY);
        // 1.7 NEI rects, offset by NEI's (5, 11) recipe origin
        reg.add(GUISILEX.class, 44, 71, 60, 50, GeoNtmJeiRecipeTypes.SILEX);
        reg.add(GUIBook.class, 89, 34, 24, 18, GeoNtmJeiRecipeTypes.BOOK);
        reg.add(GUIFurnaceCombination.class, 54, 55, 18, 18, GeoNtmJeiRecipeTypes.COMBINATION);
        reg.add(ScreenRBMKOutgasser.class, 80, 37, 16, 32, GeoNtmJeiRecipeTypes.OUTGASSER);
        RTGJeiIntegration.registerGuiHandler(reg);
    }

    @Override
    public void transfers(Transfers reg) {
         reg.add(ContainerMachineAmmoPress.class,
                 ModMenus.MACHINE_AMMO_PRESS.get(), GeoNtmJeiRecipeTypes.AMMO_PRESS, 0, 9, 10, 36);
         reg.addSelective(ContainerMixer.class,
                ModMenus.MACHINE_MIXER.get(), GeoNtmJeiRecipeTypes.MIXER, new int[]{1}, 5, 36,
                recipe -> recipe.inputFluid == null || recipe.inputFluid.length == 0);
        reg.addSelective(ContainerMachineExposureChamber.class,
                ModMenus.MACHINE_EXPOSURE_CHAMBER.get(), GeoNtmJeiRecipeTypes.EXPOSURE_CHAMBER,
                new int[]{0, 2}, 7, 36, recipe -> true);
        reg.add(ContainerCentrifuge.class,
                ModMenus.MACHINE_CENTRIFUGE.get(), GeoNtmJeiRecipeTypes.CENTRIFUGE,
                0, 1, ContainerCentrifuge.MACHINE_SLOTS, 36);
         reg.addSelective(ContainerMachineCyclotron.class,
                 ModMenus.MACHINE_CYCLOTRON.get(), GeoNtmJeiRecipeTypes.CYCLOTRON,
                 new int[]{0, 3}, 12, 36, recipe -> true);
         reg.addSelective(ContainerMachinePrecAss.class,
                 ModMenus.MACHINE_PREC_ASS.get(), GeoNtmJeiRecipeTypes.PRECISION_ASSEMBLER,
                 new int[]{4, 5, 6, 7, 8, 9, 10, 11, 12}, 22, 36,
                 recipe -> recipe.inputFluid == null || recipe.inputFluid.length == 0);
         reg.add(ContainerMachineElectrolyser.class,
                 ModMenus.MACHINE_ELECTROLYSER_METAL.get(), GeoNtmJeiRecipeTypes.ELECTROLYSIS_METAL,
                 3, 1, 10, 36);
         reg.addSelective(ContainerMachineArcWelder.class,
                 ModMenus.MACHINE_ARC_WELDER.get(), GeoNtmJeiRecipeTypes.ARC_WELDER,
                 new int[]{0, 1, 2}, 8, 36, recipe -> recipe.fluid == null);
        reg.addSelective(ContainerMachineAssemblyFactory.class,
                 ModMenus.MACHINE_ASSEMBLY_FACTORY.get(),
                 GeoNtmJeiRecipeTypes.ASSEMBLY_MACHINE,
                 new int[]{5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16}, 60, 36,
                 recipe -> recipe.inputFluid == null || recipe.inputFluid.length == 0);
        reg.addSelective(ContainerMachineChemicalFactory.class,
                 ModMenus.MACHINE_CHEMICAL_FACTORY.get(),
                 GeoNtmJeiRecipeTypes.CHEMICAL_PLANT, new int[]{5, 6, 7}, 32, 36,
                 recipe -> recipe.inputFluid == null || recipe.inputFluid.length == 0);
         reg.add(ContainerMachineArcFurnaceLarge.class,
                 ModMenus.MACHINE_ARC_FURNACE.get(), GeoNtmJeiRecipeTypes.ARC_FURNACE_SOLID,
                 5, 1, 30, 36);
         reg.add(ContainerMachineArcFurnaceLarge.class,
                 ModMenus.MACHINE_ARC_FURNACE.get(), GeoNtmJeiRecipeTypes.ARC_FURNACE_FLUID,
                 5, 1, 30, 36);
          reg.addSelective(ContainerDiFurnace.class,
                  ModMenus.MACHINE_DIFURNACE.get(), GeoNtmJeiRecipeTypes.ALLOY,
                  new int[]{0, 1}, 4, 36, recipe -> true);
        reg.addAction(ContainerAnvil.class, ModMenus.ANVIL.get(), GeoNtmJeiRecipeTypes.ANVIL_CON,
                new TransferAction<ContainerAnvil, AnvilConstructionRecipe>() {
                    @Override
                    public Component unavailable(ContainerAnvil menu, AnvilConstructionRecipe recipe) {
                        return recipe.isTierValid(menu.tier) ? null : Component.translatable("geontm.anvil_construction.tier");
                    }

                    @Override
                    public void run(AbstractContainerScreen<?> screen, ContainerAnvil menu, AnvilConstructionRecipe recipe) {
                        if (screen instanceof GUIAnvil anvil) anvil.focusRecipe(recipe);
                    }
                });
        RTGJeiIntegration.registerTransfer(reg);
     }

    @Override
    public void stations(Stations reg) {
        RTGJeiIntegration.registerCatalyst(reg);
        reg.add(SatelliteRecipeHandler.TYPE, new ItemStack(ModSatelliteBlocks.sat_dock.get()));
        reg.add(GeoNtmJeiRecipeTypes.SPACE_ASSEMBLER, ModSatelliteItems.satellite.stack(EnumSatType.SCIENCE_ASSEMBLER));
        NuclearMiscJeiIntegration.registerCatalysts(reg);
        CustomMachineJeiIntegration.registerCatalysts(reg);
          reg.add(GeoNtmJeiRecipeTypes.ASSEMBLY_MACHINE, stack(ModMachineBlocks.machine_assembly_machine.get()));
          reg.add(GeoNtmJeiRecipeTypes.ASSEMBLY_MACHINE,
                  stack(ModMachineBlocks.machine_assembly_factory.get()));
          reg.add(GeoNtmJeiRecipeTypes.PRECISION_ASSEMBLER, stack(ModMachineBlocks.machine_precass.get()));
         reg.add(GeoNtmJeiRecipeTypes.CHEMICAL_PLANT, stack(ModMachineBlocks.machine_chemical_plant.get()));
         reg.add(GeoNtmJeiRecipeTypes.CHEMICAL_PLANT,
                 stack(ModMachineBlocks.machine_chemical_factory.get()));
        reg.add(GeoNtmJeiRecipeTypes.MIXER, stack(ModMachineBlocks.machine_mixer.get()));
        reg.add(GeoNtmJeiRecipeTypes.PUREX, stack(ModMachineBlocks.machine_purex.get()));
        reg.add(GeoNtmJeiRecipeTypes.ROCK_MILL, stack(ModMachineBlocks.machine_rockmill.get()));
        reg.add(GeoNtmJeiRecipeTypes.SUPERCOMPUTER, stack(ModMachineBlocks.machine_supercomputer.get()));
        reg.add(GeoNtmJeiRecipeTypes.FUSION, stack(ModMachineBlocks.fusion_torus.get()));
        reg.add(GeoNtmJeiRecipeTypes.FUSION_BREEDER,
                stack(ModMachineBlocks.fusion_breeder.get()));
        reg.add(GeoNtmJeiRecipeTypes.PLASMA_FORGE, stack(ModMachineBlocks.fusion_plasma_forge.get()));
        reg.add(GeoNtmJeiRecipeTypes.BLAST_FURNACE, stack(ModMachineBlocks.machine_blast_furnace.get()));
         reg.add(GeoNtmJeiRecipeTypes.PRESS, stack(ModMachineBlocks.machine_press.get()), stack(ModMachineBlocks.machine_epress.get()));
         reg.add(GeoNtmJeiRecipeTypes.AMMO_PRESS,
                 stack(ModMachineBlocks.machine_ammo_press.get()));
        reg.add(GeoNtmJeiRecipeTypes.SOLDERING_STATION, stack(ModMachineBlocks.machine_soldering_station.get()));
         reg.add(GeoNtmJeiRecipeTypes.CRYSTALLIZER, stack(ModMachineBlocks.machine_crystallizer.get()));
         reg.add(GeoNtmJeiRecipeTypes.CENTRIFUGE, stack(ModMachineBlocks.machine_centrifuge.get()));
         reg.add(GeoNtmJeiRecipeTypes.CYCLOTRON, stack(ModMachineBlocks.machine_cyclotron.get()));
         reg.add(GeoNtmJeiRecipeTypes.PARTICLE_ACCELERATOR, stack(ModMachineBlocks.pa_detector.get()));
         reg.add(GeoNtmJeiRecipeTypes.GAS_CENTRIFUGE, stack(ModMachineBlocks.machine_gascent.get()));
        reg.add(GeoNtmJeiRecipeTypes.ELECTROLYSIS_FLUID, stack(ModMachineBlocks.machine_electrolyser.get()));
        reg.add(GeoNtmJeiRecipeTypes.ELECTROLYSIS_METAL, stack(ModMachineBlocks.machine_electrolyser.get()));
        reg.add(GeoNtmJeiRecipeTypes.EXPOSURE_CHAMBER,
                stack(ModMachineBlocks.machine_exposure_chamber.get()));
        reg.add(GeoNtmJeiRecipeTypes.SILEX, stack(ModMachineBlocks.machine_silex.get()));
        reg.add(GeoNtmJeiRecipeTypes.FLUID_CONTAINERS, FluidContainerRecipeHandler.machines().toArray(ItemStack[]::new));
        reg.add(GeoNtmJeiRecipeTypes.CONSTRUCTION, ConstructionRecipeHandler.stations());
        reg.add(GeoNtmJeiRecipeTypes.TOOLING, ToolingRecipeHandler.stations());
        reg.add(GeoNtmJeiRecipeTypes.ASHPIT, stack(ModMachineBlocks.machine_ashpit.get()));
        reg.add(GeoNtmJeiRecipeTypes.CRUCIBLE_SMELTING, stack(ModMachineBlocks.machine_crucible.get()));
        reg.add(GeoNtmJeiRecipeTypes.CRUCIBLE_CASTING, CrucibleCastingHandler.stations());
        reg.add(GeoNtmJeiRecipeTypes.BOOK, BookRecipeHandler.book());
        reg.add(GeoNtmJeiRecipeTypes.SHREDDER, stack(ModMachineBlocks.machine_shredder.get()));
        reg.add(GeoNtmJeiRecipeTypes.SAWMILL, stack(ModMachineBlocks.machine_sawmill.get()));
         reg.add(GeoNtmJeiRecipeTypes.CRUCIBLE_ALLOY, stack(ModMachineBlocks.machine_crucible.get()));
         reg.add(GeoNtmJeiRecipeTypes.ARC_WELDER,
                 stack(ModMachineBlocks.machine_arc_welder.get()));
         reg.add(GeoNtmJeiRecipeTypes.ROTARY_FURNACE,
                 stack(ModProductionBlocks.MACHINE_ROTARY_FURNACE.get()));
         reg.add(GeoNtmJeiRecipeTypes.ARC_FURNACE_SOLID,
                 stack(ModMachineBlocks.machine_arc_furnace.get()));
         reg.add(GeoNtmJeiRecipeTypes.ARC_FURNACE_FLUID,
                 stack(ModMachineBlocks.machine_arc_furnace.get()));
         reg.add(GeoNtmJeiRecipeTypes.ALLOY,
                 stack(ModMachineBlocks.machine_difurnace_off.get()));
        reg.add(GeoNtmJeiRecipeTypes.ANVIL_CON, anvilIcon());
        reg.add(GeoNtmJeiRecipeTypes.ANVIL_SMITH, anvilIcon());

        reg.add(GeoNtmJeiRecipeTypes.REFINERY, stack(ModMachineBlocks.machine_refinery.get()));
        reg.add(GeoNtmJeiRecipeTypes.FRACTIONING, stack(ModMachineBlocks.machine_fraction_tower.get()));
        reg.add(GeoNtmJeiRecipeTypes.LIQUEFACTION, stack(ModMachineBlocks.machine_liquefactor.get()));
        reg.add(GeoNtmJeiRecipeTypes.SOLIDIFICATION, stack(ModMachineBlocks.machine_solidifier.get()));
        // Boiling: the boiler + all three heaters (firebox / electric / oilburner) can drive it.
        reg.add(GeoNtmJeiRecipeTypes.VACUUM, stack(ModMachineBlocks.machine_vacuum_distill.get()));
        reg.add(GeoNtmJeiRecipeTypes.REFORMING, stack(ModMachineBlocks.machine_catalytic_reformer.get()));
        reg.add(GeoNtmJeiRecipeTypes.CRACKING, stack(ModMachineBlocks.machine_catalytic_cracker.get()));
        reg.add(GeoNtmJeiRecipeTypes.HYDROTREATING, stack(ModMachineBlocks.machine_hydrotreater.get()));
        reg.add(GeoNtmJeiRecipeTypes.COKER, stack(ModMachineBlocks.machine_coker.get()));
        reg.add(GeoNtmJeiRecipeTypes.PYROLYSIS, stack(ModMachineBlocks.machine_pyrooven.get()));
        reg.add(GeoNtmJeiRecipeTypes.COMBINATION, stack(ModMachineBlocks.machine_furnace_combination.get()));
        reg.add(GeoNtmJeiRecipeTypes.COMPRESSOR, stack(ModMachineBlocks.machine_compressor.get()));
        reg.add(GeoNtmJeiRecipeTypes.BOILING, stack(ModMachineBlocks.machine_boiler.get()),
                stack(ModMachineBlocks.heater_firebox.get()), stack(ModMachineBlocks.heater_electric.get()),
                stack(ModMachineBlocks.heater_oilburner.get()));
         reg.add(GeoNtmJeiRecipeTypes.COOLING, stack(ModMachineBlocks.machine_large_turbine.get()),
                  stack(ModMachineBlocks.machine_condenser.get()), stack(ModMachineBlocks.machine_condenser_powered.get()));
         reg.add(GeoNtmJeiRecipeTypes.STORAGE_DRUM,
                 stack(ModMachineBlocks.machine_storage_drum.get()));
         reg.add(GeoNtmJeiRecipeTypes.FUEL_POOL,
                 stack(ModMachineBlocks.machine_waste_drum.get()));
        reg.add(GeoNtmJeiRecipeTypes.ANNIHILATOR, stack(ModProduction2Blocks.MACHINE_ANNIHILATOR.get()));
        reg.add(GeoNtmJeiRecipeTypes.ORE_SLOPPER, stack(ModProduction2Blocks.MACHINE_ORE_SLOPPER.get()));
        reg.add(GeoNtmJeiRecipeTypes.RBMK_DISASSEMBLY, stack(Blocks.CRAFTING_TABLE));
        reg.add(GeoNtmJeiRecipeTypes.GRENADE, stack(Blocks.CRAFTING_TABLE));
        reg.add(GeoNtmJeiRecipeTypes.ZIRNOX, stack(ModMachineBlocks.machine_zirnox.get()));
        reg.add(GeoNtmJeiRecipeTypes.PWR, stack(ModPwrBlocks.PWR_CONTROLLER.get()));
        reg.add(GeoNtmJeiRecipeTypes.WATZ, stack(ModMachineBlocks.watz.get()));
        reg.add(GeoNtmJeiRecipeTypes.OUTGASSER, stack(ModRBMKBlocks.rbmk_outgasser.get()), stack(ModMachineBlocks.fusion_breeder.get()));
        reg.add(GeoNtmJeiRecipeTypes.DEUTERIUM, stack(ModMachineBlocks.machine_deuterium_extractor.get()),
                stack(ModMachineBlocks.machine_deuterium_tower.get()));

        // The electric furnace runs vanilla smelting, so list it under the viewer's built-in furnace category
        // (mirrors the old JEIConfig line; no bespoke category needed).
        reg.addSmelting(stack(ModMachineBlocks.machine_electric_furnace_off.get()));
    }

    /**
     * Runs once the viewer has its recipes: 1.7 {@code ArcFurnaceRecipes.registerFurnaceSmeltables}, the arc furnace's
     * furnace-smelting pass, listed in the solid arc furnace page. The furnace recipes are only readable from the viewer's
     * own lookup on a 26.1 client, so the pass is added here rather than at registration: JEI runs it
     * at {@code onRuntimeAvailable}, REI the first time it is asked for the page.
     */
    @Override
    public void runtime(Runtime runtime) {
        List<ArcFurnaceRecipes.ArcFurnaceRecipe> pass = ArcFurnaceRecipes
                .furnacePass(runtime.smeltingRecipes());
        runtime.addRecipes(GeoNtmJeiRecipeTypes.ARC_FURNACE_SOLID, pass);
        Constants.LOG.info("[{}] arc furnace furnace pass: {} recipe(s)", runtime.viewer(), pass.size());
    }

    /** The category title, from the {@code geontm.<path>.name} lang key (the old title-key convention). */
    static Component title(String path) {
        return Component.translatable("geontm." + path + ".name");
    }

    static ItemStack stack(Block block) {
        return new ItemStack(block);
    }

    /** Icon/catalyst for the anvil categories: the tier-1 iron anvil (the anvil has no processing TE). */
    static ItemStack anvilIcon() {
        return new ItemStack(ModAnvilBlocks.anvil_iron.get());
    }
}
