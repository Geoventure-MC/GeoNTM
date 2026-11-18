package fr.geoventure.geontm.inventory;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.inventory.container.ContainerAnvil;
import fr.geoventure.geontm.inventory.container.ContainerBatterySocket;
import fr.geoventure.geontm.inventory.container.ContainerCombustionEngine;
import fr.geoventure.geontm.inventory.container.ContainerCompressor;
import fr.geoventure.geontm.inventory.container.ContainerCore;
import fr.geoventure.geontm.inventory.container.ContainerCoreEmitter;
import fr.geoventure.geontm.inventory.container.ContainerCoreInjector;
import fr.geoventure.geontm.inventory.container.ContainerCoreReceiver;
import fr.geoventure.geontm.inventory.container.ContainerCoreStabilizer;
import fr.geoventure.geontm.inventory.container.ContainerCrateTemplate;
import fr.geoventure.geontm.inventory.container.ContainerCrucible;
import fr.geoventure.geontm.inventory.container.ContainerFEL;
import fr.geoventure.geontm.inventory.container.ContainerFirebox;
import fr.geoventure.geontm.inventory.container.ContainerFurnaceCombination;
import fr.geoventure.geontm.inventory.container.ContainerFusionBreeder;
import fr.geoventure.geontm.inventory.container.ContainerFusionKlystron;
import fr.geoventure.geontm.inventory.container.ContainerFusionTorus;
import fr.geoventure.geontm.inventory.container.ContainerHeaterHeatex;
import fr.geoventure.geontm.inventory.container.ContainerLaunchPad;
import fr.geoventure.geontm.inventory.container.ContainerLiquefactor;
import fr.geoventure.geontm.inventory.container.ContainerLaunchpadLambda;
import fr.geoventure.geontm.inventory.container.ContainerLaunchpadSoyuz;
import fr.geoventure.geontm.inventory.container.ContainerMachineArcWelder;
import fr.geoventure.geontm.inventory.container.ContainerMachineCatalyticReformer;
import fr.geoventure.geontm.inventory.container.ContainerMachineCoker;
import fr.geoventure.geontm.inventory.container.ContainerMachineDiesel;
import fr.geoventure.geontm.inventory.container.ContainerBatteryREDD;
import fr.geoventure.geontm.inventory.container.ContainerCrate;
import fr.geoventure.geontm.inventory.container.ContainerMachineAssemblyMachine;
import fr.geoventure.geontm.inventory.container.ContainerMachineGasFlare;
import fr.geoventure.geontm.inventory.container.ContainerMachineHydrotreater;
import fr.geoventure.geontm.inventory.container.ContainerMachineLargeTurbine;
import fr.geoventure.geontm.inventory.container.ContainerMachineMiningLaser;
import fr.geoventure.geontm.inventory.container.ContainerMachineOilWell;
import fr.geoventure.geontm.inventory.container.ContainerMachinePlasmaForge;
import fr.geoventure.geontm.inventory.container.ContainerMachinePrecAss;
import fr.geoventure.geontm.inventory.container.ContainerMachineBattery;
import fr.geoventure.geontm.inventory.container.ContainerCrystallizer;
import fr.geoventure.geontm.inventory.container.ContainerCentrifuge;
import fr.geoventure.geontm.inventory.container.ContainerMachineCyclotron;
import fr.geoventure.geontm.inventory.container.ContainerMachineGasCent;
import fr.geoventure.geontm.inventory.container.ContainerMachineElectrolyser;
import fr.geoventure.geontm.inventory.container.ContainerMachineChemicalPlant;
import fr.geoventure.geontm.inventory.container.ContainerMachineRefinery;
import fr.geoventure.geontm.inventory.container.ContainerMachineTurbineGas;
import fr.geoventure.geontm.inventory.container.ContainerMachineTurbofan;
import fr.geoventure.geontm.inventory.container.ContainerMachineVacuumDistill;
import fr.geoventure.geontm.inventory.container.ContainerMachineWoodBurner;
import fr.geoventure.geontm.inventory.container.ContainerMassStorage;
import fr.geoventure.geontm.inventory.container.ContainerMixer;
import fr.geoventure.geontm.inventory.container.ContainerMachineExposureChamber;
import fr.geoventure.geontm.inventory.container.ContainerBlastFurnace;
import fr.geoventure.geontm.inventory.container.ContainerMachinePUREX;
import fr.geoventure.geontm.inventory.container.ContainerMachineRockMill;
import fr.geoventure.geontm.inventory.container.ContainerMachineSuperComputer;
import fr.geoventure.geontm.inventory.container.ContainerTapeDrive;
import fr.geoventure.geontm.inventory.container.ContainerMachineElectricFurnace;
import fr.geoventure.geontm.inventory.container.ContainerFurnaceBrick;
import fr.geoventure.geontm.inventory.container.ContainerFurnaceIron;
import fr.geoventure.geontm.inventory.container.ContainerFurnaceSteel;
import fr.geoventure.geontm.inventory.container.ContainerMachineEPress;
import fr.geoventure.geontm.inventory.container.ContainerMachinePress;
import fr.geoventure.geontm.inventory.container.ContainerMachineAmmoPress;
import fr.geoventure.geontm.inventory.container.ContainerMachineShredder;
import fr.geoventure.geontm.inventory.container.ContainerMachineSolderingStation;
import fr.geoventure.geontm.inventory.container.ContainerMachineFluidTank;
import fr.geoventure.geontm.inventory.container.ContainerMachineF6Tank;
import fr.geoventure.geontm.inventory.container.ContainerMachineGiantTank;
import fr.geoventure.geontm.inventory.container.ContainerNukeMan;
import fr.geoventure.geontm.inventory.container.ContainerNukeN2;
import fr.geoventure.geontm.inventory.container.ContainerNukeBoy;
import fr.geoventure.geontm.inventory.container.ContainerNukeGadget;
import fr.geoventure.geontm.inventory.container.ContainerNukeTsar;
import fr.geoventure.geontm.inventory.container.ContainerNukeMike;
import fr.geoventure.geontm.inventory.container.ContainerOilburner;
import fr.geoventure.geontm.inventory.container.ContainerPADetector;
import fr.geoventure.geontm.inventory.container.ContainerPADipole;
import fr.geoventure.geontm.inventory.container.ContainerPAQuadrupole;
import fr.geoventure.geontm.inventory.container.ContainerPARFC;
import fr.geoventure.geontm.inventory.container.ContainerPASource;
import fr.geoventure.geontm.inventory.container.ContainerPyroOven;
import fr.geoventure.geontm.inventory.container.ContainerSILEX;
import fr.geoventure.geontm.inventory.container.ContainerSolidifier;
import fr.geoventure.geontm.inventory.container.ContainerSoyuzLauncher;
import fr.geoventure.geontm.inventory.container.ContainerTurretBase;
import fr.geoventure.geontm.inventory.container.ContainerWasteDrum;
import fr.geoventure.geontm.inventory.container.ContainerArmorTable;
import fr.geoventure.geontm.inventory.container.ContainerWatz;
import fr.geoventure.geontm.inventory.container.ContainerWeaponTable;
import fr.geoventure.geontm.inventory.container.ContainerStorageDrum;
import fr.geoventure.geontm.inventory.container.ContainerMachineRadGen;
import fr.geoventure.geontm.inventory.container.ContainerReactorZirnox;
import fr.geoventure.geontm.inventory.container.MenuCartDestroyer;
import fr.geoventure.geontm.inventory.container.MenuCrate;
import fr.geoventure.geontm.inventory.container.MenuTrainCargoTram;
import fr.geoventure.geontm.inventory.container.MenuTrainCargoTramTrailer;
import fr.geoventure.geontm.tileentity.machine.storage.MassStorageType;
import fr.geoventure.geontm.tileentity.machine.storage.TemplateCrateLayout;
import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.inventory.container.ContainerMachineAssemblyFactory;
import fr.geoventure.geontm.inventory.container.ContainerMachineChemicalFactory;
import fr.geoventure.geontm.inventory.container.ContainerMachineArcFurnaceLarge;
import fr.geoventure.geontm.inventory.container.ContainerMachineStrandCaster;
import fr.geoventure.geontm.inventory.container.ContainerAshpit;
import fr.geoventure.geontm.inventory.container.ContainerDiFurnace;
import fr.geoventure.geontm.inventory.container.RadioTorchMenu;
import fr.geoventure.geontm.inventory.container.RadioReaderMenu;
import fr.geoventure.geontm.inventory.container.RadioLogicMenu;
import fr.geoventure.geontm.inventory.container.RadioCounterMenu;
import fr.geoventure.geontm.inventory.container.RadioControllerMenu;
import fr.geoventure.geontm.inventory.container.RadioTelexMenu;
import fr.geoventure.geontm.inventory.container.RadioAutocalMenu;

/** Registry for GEONTM menu types. Menu types are created via the platform service so each loader can
 * carry the {@link net.minecraft.core.BlockPos} open-data in its own way. */
public final class ModMenus {

    private ModMenus() {}

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Constants.MOD_ID);
    private static final Reg REG = Reg.of(MENUS);

    public static final RegistryObject<MenuType<ContainerPASource>> PA_SOURCE =
            REG.menu("pa_source", () -> Menus.typeWithPos(ContainerPASource::new));
    public static final RegistryObject<MenuType<ContainerPARFC>> PA_RFC =
            REG.menu("pa_rfc", () -> Menus.typeWithPos(ContainerPARFC::new));
    public static final RegistryObject<MenuType<ContainerPAQuadrupole>> PA_QUADRUPOLE =
            REG.menu("pa_quadrupole", () -> Menus.typeWithPos(ContainerPAQuadrupole::new));
    public static final RegistryObject<MenuType<ContainerPADipole>> PA_DIPOLE =
            REG.menu("pa_dipole", () -> Menus.typeWithPos(ContainerPADipole::new));
    public static final RegistryObject<MenuType<ContainerPADetector>> PA_DETECTOR =
            REG.menu("pa_detector", () -> Menus.typeWithPos(ContainerPADetector::new));

    public static final RegistryObject<MenuType<ContainerCore>> DFC_CORE =
            REG.menu("dfc_core", () -> Menus.typeWithPos(ContainerCore::new));
    public static final RegistryObject<MenuType<ContainerCoreEmitter>> DFC_EMITTER =
            REG.menu("dfc_emitter", () -> Menus.typeWithPos(ContainerCoreEmitter::new));
    public static final RegistryObject<MenuType<ContainerCoreReceiver>> DFC_RECEIVER =
            REG.menu("dfc_receiver", () -> Menus.typeWithPos(ContainerCoreReceiver::new));
    public static final RegistryObject<MenuType<ContainerCoreInjector>> DFC_INJECTOR =
            REG.menu("dfc_injector", () -> Menus.typeWithPos(ContainerCoreInjector::new));
    public static final RegistryObject<MenuType<ContainerCoreStabilizer>> DFC_STABILIZER =
            REG.menu("dfc_stabilizer", () -> Menus.typeWithPos(ContainerCoreStabilizer::new));

    public static final RegistryObject<MenuType<ContainerSoyuzLauncher>> SOYUZ_LAUNCHER =
            REG.menu("soyuz_launcher", () -> Menus.typeWithPos(ContainerSoyuzLauncher::new));
    public static final RegistryObject<MenuType<ContainerLaunchpadSoyuz>> LAUNCHPAD_SOYUZ =
            REG.menu("launchpad_soyuz", () -> Menus.typeWithPos(ContainerLaunchpadSoyuz::new));
    public static final RegistryObject<MenuType<ContainerLaunchpadLambda>> LAUNCHPAD_LAMBDA =
            REG.menu("launchpad_lambda", () -> Menus.typeWithPos(ContainerLaunchpadLambda::new));


    public static final RegistryObject<MenuType<ContainerMachineBattery>> MACHINE_BATTERY =
            REG.menu("machine_battery", () -> Menus.typeWithPos(ContainerMachineBattery::new));

    public static final RegistryObject<MenuType<ContainerWasteDrum>> MACHINE_WASTE_DRUM =
            REG.menu("machine_waste_drum", () -> Menus.typeWithPos(ContainerWasteDrum::new));
    public static final RegistryObject<MenuType<ContainerStorageDrum>> MACHINE_STORAGE_DRUM =
            REG.menu("machine_storage_drum", () -> Menus.typeWithPos(ContainerStorageDrum::new));

    public static final RegistryObject<MenuType<ContainerMachineRadGen>> MACHINE_RAD_GEN =
            REG.menu("machine_radgen", () -> Menus.typeWithPos(ContainerMachineRadGen::new));

    public static final RegistryObject<MenuType<ContainerMachineAssemblyMachine>> MACHINE_ASSEMBLY_MACHINE =
            REG.menu("machine_assembly_machine", () -> Menus.typeWithPos(ContainerMachineAssemblyMachine::new));
    public static final RegistryObject<MenuType<ContainerMachineAssemblyFactory>> MACHINE_ASSEMBLY_FACTORY =
            REG.menu("machine_assembly_factory", () -> Menus.typeWithPos(ContainerMachineAssemblyFactory::new));
    public static final RegistryObject<MenuType<ContainerMachinePrecAss>> MACHINE_PREC_ASS =
            REG.menu("machine_precass", () -> Menus.typeWithPos(ContainerMachinePrecAss::new));

    public static final RegistryObject<MenuType<ContainerMachineChemicalPlant>> MACHINE_CHEMICAL_PLANT =
            REG.menu("machine_chemical_plant", () -> Menus.typeWithPos(ContainerMachineChemicalPlant::new));
    public static final RegistryObject<MenuType<ContainerMachineChemicalFactory>> MACHINE_CHEMICAL_FACTORY =
            REG.menu("machine_chemical_factory", () -> Menus.typeWithPos(ContainerMachineChemicalFactory::new));

    public static final RegistryObject<MenuType<ContainerMixer>> MACHINE_MIXER =
            REG.menu("machine_mixer", () -> Menus.typeWithPos(ContainerMixer::new));

    public static final RegistryObject<MenuType<ContainerMachineExposureChamber>> MACHINE_EXPOSURE_CHAMBER =
            REG.menu("machine_exposure_chamber",
                    () -> Menus.typeWithPos(ContainerMachineExposureChamber::new));

    public static final RegistryObject<MenuType<ContainerBlastFurnace>> MACHINE_BLAST_FURNACE =
            REG.menu("machine_blast_furnace", () -> Menus.typeWithPos(ContainerBlastFurnace::new));

    public static final RegistryObject<MenuType<ContainerMachinePUREX>> MACHINE_PUREX =
            REG.menu("machine_purex", () -> Menus.typeWithPos(ContainerMachinePUREX::new));

    public static final RegistryObject<MenuType<ContainerMachineRockMill>> MACHINE_ROCKMILL =
            REG.menu("machine_rockmill", () -> Menus.typeWithPos(ContainerMachineRockMill::new));

    public static final RegistryObject<MenuType<ContainerMachineSuperComputer>> MACHINE_SUPERCOMPUTER =
            REG.menu("machine_supercomputer", () -> Menus.typeWithPos(ContainerMachineSuperComputer::new));

    public static final RegistryObject<MenuType<ContainerTapeDrive>> MACHINE_TAPE_DRIVE =
            REG.menu("machine_tape_drive", () -> Menus.typeWithPos(ContainerTapeDrive::new));

    public static final RegistryObject<MenuType<ContainerMachineSolderingStation>> MACHINE_SOLDERING_STATION =
            REG.menu("machine_soldering_station", () -> Menus.typeWithPos(ContainerMachineSolderingStation::new));
    public static final RegistryObject<MenuType<ContainerMachineArcFurnaceLarge>> MACHINE_ARC_FURNACE =
            REG.menu("machine_arc_furnace", () -> Menus.typeWithPos(ContainerMachineArcFurnaceLarge::new));
    public static final RegistryObject<MenuType<ContainerMachineArcWelder>> MACHINE_ARC_WELDER =
            REG.menu("machine_arc_welder", () -> Menus.typeWithPos(ContainerMachineArcWelder::new));

    public static final RegistryObject<MenuType<ContainerCrystallizer>> MACHINE_CRYSTALLIZER =
            REG.menu("machine_crystallizer", () -> Menus.typeWithPos(ContainerCrystallizer::new));

    public static final RegistryObject<MenuType<ContainerCentrifuge>> MACHINE_CENTRIFUGE =
            REG.menu("machine_centrifuge", () -> Menus.typeWithPos(ContainerCentrifuge::new));

    public static final RegistryObject<MenuType<ContainerMachineCyclotron>> MACHINE_CYCLOTRON =
            REG.menu("machine_cyclotron", () -> Menus.typeWithPos(ContainerMachineCyclotron::new));

    public static final RegistryObject<MenuType<ContainerMachineGasCent>> MACHINE_GAS_CENT =
            REG.menu("machine_gascent", () -> Menus.typeWithPos(ContainerMachineGasCent::new));

    public static final RegistryObject<MenuType<ContainerMachineElectrolyser>> MACHINE_ELECTROLYSER_FLUID =
            REG.menu("machine_electrolyser_fluid", () -> Menus.typeWithPos(
                    (id, inventory, pos) -> new ContainerMachineElectrolyser(id, inventory, pos, false)));
    public static final RegistryObject<MenuType<ContainerMachineElectrolyser>> MACHINE_ELECTROLYSER_METAL =
            REG.menu("machine_electrolyser_metal", () -> Menus.typeWithPos(
                    (id, inventory, pos) -> new ContainerMachineElectrolyser(id, inventory, pos, true)));

    public static final RegistryObject<MenuType<ContainerSILEX>> MACHINE_SILEX =
            REG.menu("machine_silex", () -> Menus.typeWithPos(ContainerSILEX::new));

    public static final RegistryObject<MenuType<ContainerFEL>> MACHINE_FEL =
            REG.menu("machine_fel", () -> Menus.typeWithPos(ContainerFEL::new));


    public static final RegistryObject<MenuType<ContainerMachineLargeTurbine>> MACHINE_LARGE_TURBINE =
            REG.menu("machine_large_turbine", () -> Menus.typeWithPos(ContainerMachineLargeTurbine::new));

    public static final RegistryObject<MenuType<ContainerMachineTurbineGas>> MACHINE_TURBINE_GAS =
            REG.menu("machine_turbine_gas", () -> Menus.typeWithPos(ContainerMachineTurbineGas::new));

    public static final RegistryObject<MenuType<ContainerMachineTurbofan>> MACHINE_TURBOFAN =
            REG.menu("machine_turbofan", () -> Menus.typeWithPos(ContainerMachineTurbofan::new));

    public static final RegistryObject<MenuType<ContainerMachineDiesel>> MACHINE_DIESEL =
            REG.menu("machine_diesel", () -> Menus.typeWithPos(ContainerMachineDiesel::new));

    public static final RegistryObject<MenuType<ContainerMachineGasFlare>> MACHINE_GAS_FLARE =
            REG.menu("machine_flare", () -> Menus.typeWithPos(ContainerMachineGasFlare::new));

    public static final RegistryObject<MenuType<ContainerLiquefactor>> MACHINE_LIQUEFACTOR =
            REG.menu("machine_liquefactor", () -> Menus.typeWithPos(ContainerLiquefactor::new));

    public static final RegistryObject<MenuType<ContainerSolidifier>> MACHINE_SOLIDIFIER =
            REG.menu("machine_solidifier", () -> Menus.typeWithPos(ContainerSolidifier::new));

    public static final RegistryObject<MenuType<ContainerMachineRefinery>> MACHINE_REFINERY =
            REG.menu("machine_refinery", () -> Menus.typeWithPos(ContainerMachineRefinery::new));

    public static final RegistryObject<MenuType<ContainerOilburner>> HEATER_OILBURNER =
            REG.menu("heater_oilburner", () -> Menus.typeWithPos(ContainerOilburner::new));

    public static final RegistryObject<MenuType<ContainerMachineWoodBurner>> MACHINE_WOOD_BURNER =
            REG.menu("machine_wood_burner", () -> Menus.typeWithPos(ContainerMachineWoodBurner::new));

    /** Shared by the oil derrick and the pumpjack (both bind {@code TileEntityOilDrillBase}). */
    public static final RegistryObject<MenuType<ContainerMachineOilWell>> MACHINE_OIL_WELL =
            REG.menu("machine_oil_well", () -> Menus.typeWithPos(ContainerMachineOilWell::new));

    public static final RegistryObject<MenuType<ContainerMachineMiningLaser>> MACHINE_MINING_LASER =
            REG.menu("machine_mining_laser", () -> Menus.typeWithPos(ContainerMachineMiningLaser::new));

    // Advanced oil

    public static final RegistryObject<MenuType<ContainerMachineVacuumDistill>> MACHINE_VACUUM_DISTILL =
            REG.menu("machine_vacuum_distill", () -> Menus.typeWithPos(ContainerMachineVacuumDistill::new));

    public static final RegistryObject<MenuType<ContainerMachineCatalyticReformer>> MACHINE_CATALYTIC_REFORMER =
            REG.menu("machine_catalytic_reformer", () -> Menus.typeWithPos(ContainerMachineCatalyticReformer::new));

    public static final RegistryObject<MenuType<ContainerMachineHydrotreater>> MACHINE_HYDROTREATER =
            REG.menu("machine_hydrotreater", () -> Menus.typeWithPos(ContainerMachineHydrotreater::new));

    public static final RegistryObject<MenuType<ContainerMachineCoker>> MACHINE_COKER =
            REG.menu("machine_coker", () -> Menus.typeWithPos(ContainerMachineCoker::new));

    public static final RegistryObject<MenuType<ContainerFurnaceCombination>> FURNACE_COMBINATION =
            REG.menu("machine_furnace_combination", () -> Menus.typeWithPos(ContainerFurnaceCombination::new));

    public static final RegistryObject<MenuType<ContainerPyroOven>> MACHINE_PYROOVEN =
            REG.menu("machine_pyrooven", () -> Menus.typeWithPos(ContainerPyroOven::new));

    public static final RegistryObject<MenuType<ContainerCombustionEngine>> MACHINE_COMBUSTION_ENGINE =
            REG.menu("machine_combustion_engine", () -> Menus.typeWithPos(ContainerCombustionEngine::new));

    public static final RegistryObject<MenuType<ContainerCompressor>> MACHINE_COMPRESSOR =
            REG.menu("machine_compressor", () -> Menus.typeWithPos(ContainerCompressor::new));

    public static final RegistryObject<MenuType<ContainerCrucible>> MACHINE_CRUCIBLE =
            REG.menu("machine_crucible", () -> Menus.typeWithPos(ContainerCrucible::new));

    public static final RegistryObject<MenuType<ContainerFirebox>> HEATER_FIREBOX =
            REG.menu("heater_firebox", () -> Menus.typeWithPos(ContainerFirebox::new));
    public static final RegistryObject<MenuType<ContainerAshpit>> ASHPIT =
            REG.menu("ashpit", () -> Menus.typeWithPos(ContainerAshpit::new));
    public static final RegistryObject<MenuType<ContainerMachineStrandCaster>> MACHINE_STRAND_CASTER =
            REG.menu("machine_strand_caster", () -> Menus.typeWithPos(ContainerMachineStrandCaster::new));
    public static final RegistryObject<MenuType<ContainerDiFurnace>> MACHINE_DIFURNACE =
            REG.menu("machine_difurnace", () -> Menus.typeWithPos(ContainerDiFurnace::new));

    public static final RegistryObject<MenuType<ContainerHeaterHeatex>> HEATER_HEATEX =
            REG.menu("heater_heatex", () -> Menus.typeWithPos(ContainerHeaterHeatex::new));

    public static final RegistryObject<MenuType<ContainerNukeMan>> NUKE_MAN =
            REG.menu("nuke_man", () -> Menus.typeWithPos(ContainerNukeMan::new));

    public static final RegistryObject<MenuType<ContainerNukeN2>> NUKE_N2 =
            REG.menu("nuke_n2", () -> Menus.typeWithPos(ContainerNukeN2::new));
    public static final RegistryObject<MenuType<ContainerNukeBoy>> NUKE_BOY =
            REG.menu("nuke_boy", () -> Menus.typeWithPos(ContainerNukeBoy::new));
    public static final RegistryObject<MenuType<ContainerNukeGadget>> NUKE_GADGET =
            REG.menu("nuke_gadget", () -> Menus.typeWithPos(ContainerNukeGadget::new));
    public static final RegistryObject<MenuType<ContainerNukeTsar>> NUKE_TSAR =
            REG.menu("nuke_tsar", () -> Menus.typeWithPos(ContainerNukeTsar::new));
    public static final RegistryObject<MenuType<ContainerNukeMike>> NUKE_MIKE =
            REG.menu("nuke_mike", () -> Menus.typeWithPos(ContainerNukeMike::new));

    public static final RegistryObject<MenuType<ContainerReactorZirnox>> REACTOR_ZIRNOX =
            REG.menu("reactor_zirnox", () -> Menus.typeWithPos(ContainerReactorZirnox::new));

    public static final RegistryObject<MenuType<ContainerWatz>> WATZ =
            REG.menu("watz", () -> Menus.typeWithPos(ContainerWatz::new));

    public static final RegistryObject<MenuType<ContainerFusionTorus>> FUSION_TORUS =
            REG.menu("fusion_torus", () -> Menus.typeWithPos(ContainerFusionTorus::new));


    public static final RegistryObject<MenuType<ContainerFusionBreeder>> FUSION_BREEDER =
            REG.menu("fusion_breeder", () -> Menus.typeWithPos(ContainerFusionBreeder::new));

    public static final RegistryObject<MenuType<ContainerFusionKlystron>> FUSION_KLYSTRON =
            REG.menu("fusion_klystron", () -> Menus.typeWithPos(ContainerFusionKlystron::new));
    public static final RegistryObject<MenuType<ContainerMachinePlasmaForge>> FUSION_PLASMA_FORGE =
            REG.menu("fusion_plasma_forge", () -> Menus.typeWithPos(ContainerMachinePlasmaForge::new));

    public static final RegistryObject<MenuType<ContainerMachinePress>> MACHINE_PRESS =
            REG.menu("machine_press", () -> Menus.typeWithPos(ContainerMachinePress::new));

    public static final RegistryObject<MenuType<ContainerMachineAmmoPress>> MACHINE_AMMO_PRESS =
            REG.menu("machine_ammo_press", () -> Menus.typeWithPos(ContainerMachineAmmoPress::new));

    public static final RegistryObject<MenuType<ContainerMachineEPress>> MACHINE_EPRESS =
            REG.menu("machine_epress", () -> Menus.typeWithPos(ContainerMachineEPress::new));

    public static final RegistryObject<MenuType<ContainerMachineElectricFurnace>> MACHINE_ELECTRIC_FURNACE =
            REG.menu("machine_electric_furnace", () -> Menus.typeWithPos(ContainerMachineElectricFurnace::new));

    public static final RegistryObject<MenuType<ContainerFurnaceBrick>> FURNACE_BRICK =
            REG.menu("machine_furnace_brick_off", () -> Menus.typeWithPos(ContainerFurnaceBrick::new));
    public static final RegistryObject<MenuType<ContainerFurnaceIron>> FURNACE_IRON =
            REG.menu("furnace_iron", () -> Menus.typeWithPos(ContainerFurnaceIron::new));
    public static final RegistryObject<MenuType<ContainerFurnaceSteel>> FURNACE_STEEL =
            REG.menu("furnace_steel", () -> Menus.typeWithPos(ContainerFurnaceSteel::new));

    public static final RegistryObject<MenuType<ContainerMachineShredder>> MACHINE_SHREDDER =
            REG.menu("machine_shredder", () -> Menus.typeWithPos(ContainerMachineShredder::new));


    public static final RegistryObject<MenuType<ContainerMachineFluidTank>> FLUID_TANK =
            REG.menu("machine_fluidtank", () -> Menus.typeWithPos(ContainerMachineFluidTank::new));

    public static final RegistryObject<MenuType<ContainerMachineF6Tank>> F6_TANK =
            REG.menu("f6_tank", () -> Menus.typeWithPos(ContainerMachineF6Tank::new));

    public static final RegistryObject<MenuType<ContainerMachineGiantTank>> GIANT_TANK =
            REG.menu("giant_tank", () -> Menus.typeWithPos(ContainerMachineGiantTank::new));

    public static final RegistryObject<MenuType<ContainerBatterySocket>> BATTERY_SOCKET =
            REG.menu("machine_battery_socket", () -> Menus.typeWithPos(ContainerBatterySocket::new));
    public static final RegistryObject<MenuType<ContainerBatteryREDD>> BATTERY_REDD =
            REG.menu("machine_battery_redd", () -> Menus.typeWithPos(ContainerBatteryREDD::new));

    /** Shared by all 12 anvils; the container re-derives the tier from the block state at the pos. */
    public static final RegistryObject<MenuType<ContainerAnvil>> ANVIL =
            REG.menu("anvil", () -> Menus.typeWithPos(ContainerAnvil::new));

    /** Shared by all three storage crates; the container reads the BlockEntity's {@code CrateType} for layout. */
    public static final RegistryObject<MenuType<ContainerCrate>> CRATE =
            REG.menu("crate", () -> Menus.typeWithPos(ContainerCrate::new));

    // The ntm-next entity port: vehicle menus opened through vanilla's data-less MenuProvider path
    /** The crate minecart (1.7 ContainerCrate on the cart's own inventory). */
    public static final RegistryObject<MenuType<MenuCrate>> CART_CRATE =
            REG.menu("cart_crate", () -> new MenuType<>(MenuCrate::new,
                    FeatureFlags.DEFAULT_FLAGS));
    /** The destroyer minecart's two ghost filter grids (1.7 ContainerCartDestroyer). */
    public static final RegistryObject<MenuType<MenuCartDestroyer>> CART_DESTROYER =
            REG.menu("cart_destroyer", () -> new MenuType<>(MenuCartDestroyer::new,
                    FeatureFlags.DEFAULT_FLAGS));
    /** The cargo tram engine: 28 cargo slots, a charge slot and the power gauge. */
    public static final RegistryObject<MenuType<MenuTrainCargoTram>> TRAIN_CARGO_TRAM =
            REG.menu("train_cargo_tram", () -> new MenuType<>(MenuTrainCargoTram::new,
                    FeatureFlags.DEFAULT_FLAGS));
    /** The cargo tram trailer: cargo slots only. */
    public static final RegistryObject<MenuType<MenuTrainCargoTramTrailer>> TRAIN_CARGO_TRAM_TRAILER =
            REG.menu("train_cargo_tram_trailer", () -> new MenuType<>(
                    MenuTrainCargoTramTrailer::new,
                    FeatureFlags.DEFAULT_FLAGS));

    public static final RegistryObject<MenuType<ContainerArmorTable>> ARMOR_TABLE =
            REG.menu("armor_table", () -> Menus.typeWithPos(ContainerArmorTable::new));
    public static final RegistryObject<MenuType<ContainerWeaponTable>> WEAPON_TABLE =
            REG.menu("weapon_table", () -> Menus.typeWithPos(ContainerWeaponTable::new));

    public static final RegistryObject<MenuType<ContainerMassStorage>> MASS_STORAGE =
            REG.menu("mass_storage", () -> Menus.typeWithPos((id, inventory, pos) ->
                    new ContainerMassStorage(id, inventory, pos, ModMenus::massStorageMenu)));
    public static final RegistryObject<MenuType<ContainerCrateTemplate>> CRATE_TEMPLATE =
            REG.menu("crate_template", () -> Menus.typeWithPos((id, inventory, pos) ->
                    new ContainerCrateTemplate(id, inventory, pos, ModMenus::templateCrateMenu)));

    static {
        for (MassStorageType type : MassStorageType.values()) {
            type.setMenuTypeSupplier(MASS_STORAGE::get);
        }
        TemplateCrateLayout.setMenuTypeSupplier(CRATE_TEMPLATE::get);
    }

    /** Shared by every launch pad type (only the small silo pad exists so far). */
    public static final RegistryObject<MenuType<ContainerLaunchPad>> LAUNCH_PAD =
            REG.menu("launch_pad", () -> Menus.typeWithPos(ContainerLaunchPad::new));

    /** Shared by every turret: the layout is identical, only the background texture differs. */
    public static final RegistryObject<MenuType<ContainerTurretBase>> TURRET_BASE =
            REG.menu("turret_base", () -> Menus.typeWithPos(
                    ContainerTurretBase::new));

    private static MenuType<ContainerMassStorage> massStorageMenu() {
        return MASS_STORAGE.get();
    }

    private static MenuType<ContainerCrateTemplate> templateCrateMenu() {
        return CRATE_TEMPLATE.get();
    }

    // Redstone over Radio
    public static final RegistryObject<MenuType<RadioTorchMenu>> RADIO_TORCH =
            REG.menu("radio_torch", () -> Menus.typeWithPos(RadioTorchMenu::new));
    public static final RegistryObject<MenuType<RadioReaderMenu>> RADIO_READER =
            REG.menu("radio_reader", () -> Menus.typeWithPos(RadioReaderMenu::new));
    public static final RegistryObject<MenuType<RadioLogicMenu>> RADIO_LOGIC =
            REG.menu("radio_logic", () -> Menus.typeWithPos(RadioLogicMenu::new));
    public static final RegistryObject<MenuType<RadioCounterMenu>> RADIO_COUNTER =
            REG.menu("radio_counter", () -> Menus.typeWithPos(RadioCounterMenu::new));
    public static final RegistryObject<MenuType<RadioControllerMenu>> RADIO_CONTROLLER =
            REG.menu("radio_controller", () -> Menus.typeWithPos(RadioControllerMenu::new));
    public static final RegistryObject<MenuType<RadioTelexMenu>> RADIO_TELEX =
            REG.menu("radio_telex", () -> Menus.typeWithPos(RadioTelexMenu::new));
    public static final RegistryObject<MenuType<RadioAutocalMenu>> RADIO_AUTOCAL =
            REG.menu("radio_autocal", () -> Menus.typeWithPos(RadioAutocalMenu::new));

    public static void init() {}
}
