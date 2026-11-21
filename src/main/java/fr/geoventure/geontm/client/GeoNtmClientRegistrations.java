package fr.geoventure.geontm.client;

import fr.geoventure.geontm.inventory.fluid.ClientFluidProperties;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.blocks.machine.rbmk.RBMKTerminal;
import fr.geoventure.geontm.client.anim.GunAnimations;
import fr.geoventure.geontm.client.anim.GunRecoil;
import fr.geoventure.geontm.client.anim.LagAnimationLoader;
import fr.geoventure.geontm.client.crucible.CrucibleClient;
import fr.geoventure.geontm.client.gun.GunSmokeClient;
import fr.geoventure.geontm.client.jetpack.JetpackClientBridge;
import fr.geoventure.geontm.client.jetpack.JetpackClientVisuals;
import fr.geoventure.geontm.client.jetpack.JetpackHudOverlay;
import fr.geoventure.geontm.client.jetpack.JetpackRenderLayer;
import fr.geoventure.geontm.client.gun.GluonGunClient;
import fr.geoventure.geontm.client.gun.JadeShotgunClient;
import fr.geoventure.geontm.client.render.EgonBackpackRenderLayer;
import fr.geoventure.geontm.client.jetpack.JetpackSmokeClient;
import fr.geoventure.geontm.client.particle.CasingParticles;
import fr.geoventure.geontm.client.particle.DebrisParticles;
import fr.geoventure.geontm.client.particle.DisintegrationParticles;
import fr.geoventure.geontm.client.render.ConveyorPreviewRenderer;
import fr.geoventure.geontm.client.render.GeoNtmBlockColors;
import fr.geoventure.geontm.client.render.GeoNtmBlockEntityRenderers;
import fr.geoventure.geontm.client.render.GeoNtmEntityRenderers;
import fr.geoventure.geontm.blocks.machine.rbmk.RBMKGauge;
import fr.geoventure.geontm.client.render.MultiblockOutline;
import fr.geoventure.geontm.client.render.MultiblockPlacementHighlight;
import fr.geoventure.geontm.client.render.RenderBobble;
import fr.geoventure.geontm.client.render.RenderDecoBlockAlt;
import fr.geoventure.geontm.client.render.RtgClientRegistrations;
import fr.geoventure.geontm.client.render.TorexBenchmarkClient;
import fr.geoventure.geontm.client.render.armor.ArmorModRenderLayer;
import fr.geoventure.geontm.client.render.armor.ArmorRenderLayer;
import fr.geoventure.geontm.client.render.armor.ArmorRenderers;
import fr.geoventure.geontm.client.render.armor.ModelMan;
import fr.geoventure.geontm.client.render.armor.WornModelRenderLayer;
import fr.geoventure.geontm.client.render.entity.EntityRenderSmokeClient;
import fr.geoventure.geontm.client.render.projectile.TracerRegistry;
import fr.geoventure.geontm.client.render.util.RenderAccessoryUtility;
import fr.geoventure.geontm.client.render.util.SpecialPlayerRenderLayer;
import fr.geoventure.geontm.client.render.weapon.DrillHighlightClient;
import fr.geoventure.geontm.client.render.weapon.GunPlayerPose;
import fr.geoventure.geontm.client.render.weapon.WeaponRenderers;
import fr.geoventure.geontm.client.render.world.NTMSkyboxes;
import fr.geoventure.geontm.client.sound.AssemblyFactorySoundHandler;
import fr.geoventure.geontm.client.sound.ClientAudioFactory;
import fr.geoventure.geontm.commands.GeoNtmClientCommands;
import fr.geoventure.geontm.handler.JetpackHandler;
import fr.geoventure.geontm.inventory.ModMenus;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.blocks.StandardInfo;
import fr.geoventure.geontm.inventory.gui.GUIAnvil;
import fr.geoventure.geontm.inventory.gui.GUIArmorTable;
import fr.geoventure.geontm.inventory.gui.GUIAshpit;
import fr.geoventure.geontm.inventory.gui.GUICartCrate;
import fr.geoventure.geontm.inventory.gui.GUICartDestroyer;
import fr.geoventure.geontm.inventory.gui.GUICore;
import fr.geoventure.geontm.inventory.gui.GUICoreEmitter;
import fr.geoventure.geontm.inventory.gui.GUICoreInjector;
import fr.geoventure.geontm.inventory.gui.GUICoreReceiver;
import fr.geoventure.geontm.inventory.gui.GUICoreStabilizer;
import fr.geoventure.geontm.inventory.gui.GUICrateTemplate;
import fr.geoventure.geontm.inventory.gui.GUICrucible;
import fr.geoventure.geontm.inventory.gui.GUIDiFurnace;
import fr.geoventure.geontm.inventory.gui.GUILaunchPad;
import fr.geoventure.geontm.inventory.gui.GUIMachineArcFurnaceLarge;
import fr.geoventure.geontm.inventory.gui.GUILaunchpadLambda;
import fr.geoventure.geontm.inventory.gui.GUILaunchpadSoyuz;
import fr.geoventure.geontm.inventory.gui.GUIMachineArcWelder;
import fr.geoventure.geontm.inventory.gui.GUIMachineAssemblyFactory;
import fr.geoventure.geontm.inventory.gui.GUIMachineChemicalFactory;
import fr.geoventure.geontm.inventory.gui.GUIMachineStrandCaster;
import fr.geoventure.geontm.inventory.gui.GUIMassStorage;
import fr.geoventure.geontm.inventory.gui.GUIPADetector;
import fr.geoventure.geontm.inventory.gui.GUIPADipole;
import fr.geoventure.geontm.inventory.gui.GUIPAQuadrupole;
import fr.geoventure.geontm.inventory.gui.GUIPARFC;
import fr.geoventure.geontm.inventory.gui.GUIPASource;
import fr.geoventure.geontm.inventory.gui.GUIScreenDesignator;
import fr.geoventure.geontm.inventory.gui.GUISoyuzLauncher;
import fr.geoventure.geontm.inventory.gui.GUITrainCargoTram;
import fr.geoventure.geontm.inventory.gui.GUITrainCargoTramTrailer;
import fr.geoventure.geontm.inventory.gui.GUITurretBase;
import fr.geoventure.geontm.inventory.gui.GUITurretMobFilter;
import fr.geoventure.geontm.inventory.gui.GUIWeaponTable;
import fr.geoventure.geontm.inventory.gui.GUIBatterySocket;
import fr.geoventure.geontm.inventory.gui.GUIBatteryREDD;
import fr.geoventure.geontm.inventory.gui.GUICrate;
import fr.geoventure.geontm.inventory.gui.GUICrystallizer;
import fr.geoventure.geontm.inventory.gui.GUICentrifuge;
import fr.geoventure.geontm.inventory.gui.GUIMachineCyclotron;
import fr.geoventure.geontm.inventory.gui.GUIMachineGasCent;
import fr.geoventure.geontm.inventory.gui.GUIMachineElectrolyser;
import fr.geoventure.geontm.inventory.gui.GUIMixer;
import fr.geoventure.geontm.inventory.gui.GUIMachineExposureChamber;
import fr.geoventure.geontm.inventory.gui.GUICombustionEngine;
import fr.geoventure.geontm.inventory.gui.GUICompressor;
import fr.geoventure.geontm.inventory.gui.GUIFEL;
import fr.geoventure.geontm.inventory.gui.GUIFirebox;
import fr.geoventure.geontm.inventory.gui.GUIHeaterHeatex;
import fr.geoventure.geontm.inventory.gui.GUIMachineWoodBurner;
import fr.geoventure.geontm.inventory.gui.GUILiquefactor;
import fr.geoventure.geontm.inventory.gui.GUIMachineAssemblyMachine;
import fr.geoventure.geontm.inventory.gui.GUIMachinePrecAss;
import fr.geoventure.geontm.inventory.gui.GUIMachineBattery;
import fr.geoventure.geontm.inventory.gui.GUIMachineCatalyticReformer;
import fr.geoventure.geontm.inventory.gui.GUIMachineChemicalPlant;
import fr.geoventure.geontm.inventory.gui.GUIMachineCoker;
import fr.geoventure.geontm.inventory.gui.GUIFurnaceCombination;
import fr.geoventure.geontm.inventory.gui.GUIMachineDiesel;
import fr.geoventure.geontm.inventory.gui.GUIMachineEPress;
import fr.geoventure.geontm.inventory.gui.GUIMachineElectricFurnace;
import fr.geoventure.geontm.inventory.gui.GUIMachineFluidTank;
import fr.geoventure.geontm.inventory.gui.GUIMachineGiantTank;
import fr.geoventure.geontm.inventory.gui.GUIF6Tank;
import fr.geoventure.geontm.inventory.gui.GUIMachineGasFlare;
import fr.geoventure.geontm.inventory.gui.GUIMachineHydrotreater;
import fr.geoventure.geontm.inventory.gui.GUIMachineMiningLaser;
import fr.geoventure.geontm.inventory.gui.GUIMachineOilWell;
import fr.geoventure.geontm.inventory.gui.GUIMachinePUREX;
import fr.geoventure.geontm.inventory.gui.GUIMachineRockMill;
import fr.geoventure.geontm.inventory.gui.GUIMachineSuperComputer;
import fr.geoventure.geontm.inventory.gui.GUITapeDrive;
import fr.geoventure.geontm.inventory.gui.GUIMachinePress;
import fr.geoventure.geontm.inventory.gui.GUIMachineAmmoPress;
import fr.geoventure.geontm.inventory.gui.GUIMachineRefinery;
import fr.geoventure.geontm.inventory.gui.GUIMachineShredder;
import fr.geoventure.geontm.inventory.gui.GUIMachineSolderingStation;
import fr.geoventure.geontm.inventory.gui.GUIMachineLargeTurbine;
import fr.geoventure.geontm.inventory.gui.GUIMachineTurbineGas;
import fr.geoventure.geontm.inventory.gui.GUIMachineTurbofan;
import fr.geoventure.geontm.inventory.gui.GUIMachineVacuumDistill;
import fr.geoventure.geontm.inventory.gui.GUINukeMan;
import fr.geoventure.geontm.inventory.gui.GUINukeN2;
import fr.geoventure.geontm.inventory.gui.GUINukeBoy;
import fr.geoventure.geontm.inventory.gui.GUINukeGadget;
import fr.geoventure.geontm.inventory.gui.GUINukeTsar;
import fr.geoventure.geontm.inventory.gui.GUIOilburner;
import fr.geoventure.geontm.inventory.gui.GUIReactorZirnox;
import fr.geoventure.geontm.inventory.gui.GUISILEX;
import fr.geoventure.geontm.inventory.gui.GUISolidifier;
import fr.geoventure.geontm.inventory.gui.GUIWasteDrum;
import fr.geoventure.geontm.inventory.gui.GUIStorageDrum;
import fr.geoventure.geontm.inventory.gui.GUIMachineRadGen;
import fr.geoventure.geontm.inventory.gui.GUIFusionKlystron;
import fr.geoventure.geontm.inventory.gui.GUIFusionTorus;
import fr.geoventure.geontm.inventory.gui.GUIFusionBreeder;
import fr.geoventure.geontm.inventory.gui.GUIMachinePlasmaForge;
import fr.geoventure.geontm.inventory.gui.GUIWatz;
import fr.geoventure.geontm.inventory.gui.GUIPyroOven;
import fr.geoventure.geontm.inventory.gui.GUIBlastFurnace;
import fr.geoventure.geontm.inventory.gui.GUIFurnaceBrick;
import fr.geoventure.geontm.inventory.gui.GUIFurnaceIron;
import fr.geoventure.geontm.inventory.gui.GUIFurnaceSteel;
import fr.geoventure.geontm.inventory.gui.GunSupportClientRegistrations;
import fr.geoventure.geontm.inventory.gui.RadioAutocalScreen;
import fr.geoventure.geontm.inventory.gui.RadioControllerScreen;
import fr.geoventure.geontm.inventory.gui.RadioCounterScreen;
import fr.geoventure.geontm.inventory.gui.RadioLogicScreen;
import fr.geoventure.geontm.inventory.gui.RadioReaderScreen;
import fr.geoventure.geontm.inventory.gui.RadioTelexScreen;
import fr.geoventure.geontm.inventory.gui.RadioTorchScreen;
import fr.geoventure.geontm.inventory.gui.ScreenRBMKGauge;
import fr.geoventure.geontm.inventory.gui.ScreenRBMKTerminal;
import fr.geoventure.geontm.items.ModelState;
import fr.geoventure.geontm.items.tool.ItemDesignatorManual;
import fr.geoventure.geontm.items.tool.ItemTurretMobFilter;
import fr.geoventure.geontm.items.weapon.sedna.factory.GunFactoryClient;
import fr.geoventure.geontm.particle.helper.CasingCreator;
import fr.geoventure.geontm.sound.AudioSystem;
import fr.geoventure.geontm.sound.ModSounds;
import fr.geoventure.geontm.tileentity.ModArmorSetBBlockEntities;
import fr.geoventure.geocore.client.BlockLayers;
import fr.geoventure.geocore.client.CameraHooks;
import fr.geoventure.geocore.client.Capes;
import fr.geoventure.geocore.client.ClientRegistrations;
import fr.geoventure.geocore.client.ClientResourceReloads;
import fr.geoventure.geocore.client.Crosshairs;
import fr.geoventure.geocore.client.DebugLines;
import fr.geoventure.geocore.client.Fov;
import fr.geoventure.geocore.client.Hud;
import fr.geoventure.geocore.client.ItemModelProperties;
import fr.geoventure.geocore.client.PlayerRenderHooks;
import fr.geoventure.geocore.client.Tints;
import fr.geoventure.geocore.fluid.TokenFluids;
import fr.geoventure.geocore.fluid.WorldFluids;
import fr.geoventure.geocore.hook.Hook;
import fr.geoventure.geocore.platform.Platform;
import fr.geoventure.geocore.render.CompatBERProvider;
import fr.geoventure.geocore.render.CompatRenderState;
import fr.geoventure.geocore.render.HeatDistortionRenderType;
import fr.geoventure.geocore.render.HumanoidLayers;
import fr.geoventure.geocore.render.Instancing;
import fr.geoventure.geocore.render.SceneWarp;
import fr.geoventure.geocore.render.ScreenEffects;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

/**
 * Loader-neutral inventory of HBM's client registrations. This class is loaded only from client
 * entrypoints; loaders supply the small API-specific sinks that perform each registration.
 */
public final class GeoNtmClientRegistrations {

    private GeoNtmClientRegistrations() {}

    public interface ScreenSink {
        <M extends AbstractContainerMenu, U extends AbstractContainerScreen<M>> void register(
                MenuType<M> menu, MenuScreens.ScreenConstructor<M, U> factory);
    }

    public interface HudSink {
        void register(String id, Consumer<GuiGraphics> renderer);
    }

    public static void registerScreens(ScreenSink sink) {
        ItemDesignatorManual.setClientScreenOpener(stack ->
                Minecraft.getInstance().setScreen(
                        new GUIScreenDesignator(stack)));
        ItemTurretMobFilter.setClientScreenOpener(turret ->
                Minecraft.getInstance().setScreen(
                        new GUITurretMobFilter(turret)));
        AssemblyFactorySoundHandler.setAssemblerCut(ModSounds.ASSEMBLER_CUT.get());
        sink.register(ModMenus.MACHINE_BATTERY.get(), GUIMachineBattery::new);
        sink.register(ModMenus.MACHINE_WASTE_DRUM.get(), GUIWasteDrum::new);
        sink.register(ModMenus.MACHINE_STORAGE_DRUM.get(), GUIStorageDrum::new);
        sink.register(ModMenus.MACHINE_RAD_GEN.get(), GUIMachineRadGen::new);
        sink.register(ModMenus.MACHINE_ASSEMBLY_MACHINE.get(), GUIMachineAssemblyMachine::new);
        sink.register(ModMenus.MACHINE_PREC_ASS.get(), GUIMachinePrecAss::new);
        sink.register(ModMenus.MACHINE_CHEMICAL_PLANT.get(), GUIMachineChemicalPlant::new);
        sink.register(ModMenus.MACHINE_MIXER.get(), GUIMixer::new);
        sink.register(ModMenus.MACHINE_EXPOSURE_CHAMBER.get(), GUIMachineExposureChamber::new);
        sink.register(ModMenus.PA_SOURCE.get(), GUIPASource::new);
        sink.register(ModMenus.PA_RFC.get(), GUIPARFC::new);
        sink.register(ModMenus.PA_QUADRUPOLE.get(), GUIPAQuadrupole::new);
        sink.register(ModMenus.PA_DIPOLE.get(), GUIPADipole::new);
        sink.register(ModMenus.PA_DETECTOR.get(), GUIPADetector::new);
        sink.register(ModMenus.SOYUZ_LAUNCHER.get(), GUISoyuzLauncher::new);
        sink.register(ModMenus.LAUNCHPAD_SOYUZ.get(), GUILaunchpadSoyuz::new);
        sink.register(ModMenus.LAUNCHPAD_LAMBDA.get(), GUILaunchpadLambda::new);
        sink.register(ModMenus.DFC_CORE.get(), GUICore::new);
        sink.register(ModMenus.DFC_EMITTER.get(), GUICoreEmitter::new);
        sink.register(ModMenus.DFC_RECEIVER.get(), GUICoreReceiver::new);
        sink.register(ModMenus.DFC_INJECTOR.get(), GUICoreInjector::new);
        sink.register(ModMenus.DFC_STABILIZER.get(), GUICoreStabilizer::new);
        sink.register(ModMenus.MACHINE_BLAST_FURNACE.get(), GUIBlastFurnace::new);
        sink.register(ModMenus.MACHINE_PUREX.get(), GUIMachinePUREX::new);
        sink.register(ModMenus.MACHINE_ROCKMILL.get(), GUIMachineRockMill::new);
        sink.register(ModMenus.MACHINE_SUPERCOMPUTER.get(), GUIMachineSuperComputer::new);
        sink.register(ModMenus.MACHINE_TAPE_DRIVE.get(), GUITapeDrive::new);
        sink.register(ModMenus.MACHINE_SOLDERING_STATION.get(), GUIMachineSolderingStation::new);
        sink.register(ModMenus.MACHINE_CRYSTALLIZER.get(), GUICrystallizer::new);
        sink.register(ModMenus.MACHINE_CENTRIFUGE.get(), GUICentrifuge::new);
        sink.register(ModMenus.MACHINE_CYCLOTRON.get(), GUIMachineCyclotron::new);
        sink.register(ModMenus.MACHINE_GAS_CENT.get(), GUIMachineGasCent::new);
        sink.register(ModMenus.MACHINE_ELECTROLYSER_FLUID.get(), GUIMachineElectrolyser::new);
        sink.register(ModMenus.MACHINE_ELECTROLYSER_METAL.get(), GUIMachineElectrolyser::new);
        sink.register(ModMenus.MACHINE_SILEX.get(), GUISILEX::new);
        sink.register(ModMenus.MACHINE_FEL.get(), GUIFEL::new);
        sink.register(ModMenus.MACHINE_MINING_LASER.get(), GUIMachineMiningLaser::new);
        sink.register(ModMenus.MACHINE_OIL_WELL.get(), GUIMachineOilWell::new);
        sink.register(ModMenus.HEATER_OILBURNER.get(), GUIOilburner::new);
        sink.register(ModMenus.MACHINE_REFINERY.get(), GUIMachineRefinery::new);
        sink.register(ModMenus.MACHINE_DIESEL.get(), GUIMachineDiesel::new);
        sink.register(ModMenus.LAUNCH_PAD.get(), GUILaunchPad::new);
        sink.register(ModMenus.TURRET_BASE.get(), GUITurretBase::new);
        sink.register(ModMenus.MACHINE_LARGE_TURBINE.get(), GUIMachineLargeTurbine::new);
        sink.register(ModMenus.MACHINE_TURBINE_GAS.get(), GUIMachineTurbineGas::new);
        sink.register(ModMenus.MACHINE_TURBOFAN.get(), GUIMachineTurbofan::new);
        sink.register(ModMenus.MACHINE_GAS_FLARE.get(), GUIMachineGasFlare::new);
        sink.register(ModMenus.MACHINE_LIQUEFACTOR.get(), GUILiquefactor::new);
        sink.register(ModMenus.MACHINE_SOLIDIFIER.get(), GUISolidifier::new);
         sink.register(ModMenus.NUKE_MAN.get(), GUINukeMan::new);
         sink.register(ModMenus.NUKE_N2.get(), GUINukeN2::new);
         sink.register(ModMenus.NUKE_BOY.get(), GUINukeBoy::new);
         sink.register(ModMenus.NUKE_GADGET.get(), GUINukeGadget::new);
         sink.register(ModMenus.NUKE_TSAR.get(), GUINukeTsar::new);
        sink.register(ModMenus.REACTOR_ZIRNOX.get(), GUIReactorZirnox::new);
        sink.register(ModMenus.WATZ.get(), GUIWatz::new);
        sink.register(ModMenus.FUSION_TORUS.get(), GUIFusionTorus::new);
        sink.register(ModMenus.FUSION_BREEDER.get(), GUIFusionBreeder::new);
        sink.register(ModMenus.FUSION_KLYSTRON.get(), GUIFusionKlystron::new);
        sink.register(ModMenus.FUSION_PLASMA_FORGE.get(), GUIMachinePlasmaForge::new);
         sink.register(ModMenus.FLUID_TANK.get(), GUIMachineFluidTank::new);
         sink.register(ModMenus.F6_TANK.get(), GUIF6Tank::new);
         sink.register(ModMenus.GIANT_TANK.get(), GUIMachineGiantTank::new);
         sink.register(ModMenus.BATTERY_SOCKET.get(), GUIBatterySocket::new);
         sink.register(ModMenus.BATTERY_REDD.get(), GUIBatteryREDD::new);
         sink.register(ModMenus.CRATE.get(), GUICrate::new);
         // the ntm-next entity port's vehicle menus
         sink.register(ModMenus.CART_CRATE.get(), GUICartCrate::new);
         sink.register(ModMenus.CART_DESTROYER.get(), GUICartDestroyer::new);
         sink.register(ModMenus.TRAIN_CARGO_TRAM.get(), GUITrainCargoTram::new);
         sink.register(ModMenus.TRAIN_CARGO_TRAM_TRAILER.get(), GUITrainCargoTramTrailer::new);
          sink.register(ModMenus.ARMOR_TABLE.get(), GUIArmorTable::new);
          sink.register(ModMenus.WEAPON_TABLE.get(), GUIWeaponTable::new);
         sink.register(ModMenus.MACHINE_PRESS.get(), GUIMachinePress::new);
          sink.register(ModMenus.MACHINE_AMMO_PRESS.get(), GUIMachineAmmoPress::new);
          GunSupportClientRegistrations.registerScreens(sink);
         sink.register(ModMenus.MACHINE_EPRESS.get(), GUIMachineEPress::new);
        sink.register(ModMenus.MACHINE_ELECTRIC_FURNACE.get(), GUIMachineElectricFurnace::new);
        sink.register(ModMenus.FURNACE_BRICK.get(), GUIFurnaceBrick::new);
        sink.register(ModMenus.FURNACE_IRON.get(), GUIFurnaceIron::new);
        sink.register(ModMenus.FURNACE_STEEL.get(), GUIFurnaceSteel::new);
        sink.register(ModMenus.MACHINE_SHREDDER.get(), GUIMachineShredder::new);
        sink.register(ModMenus.ANVIL.get(), GUIAnvil::new);
        sink.register(ModMenus.MACHINE_CRUCIBLE.get(), GUICrucible::new);
         sink.register(ModMenus.HEATER_FIREBOX.get(), GUIFirebox::new);
         sink.register(ModMenus.HEATER_HEATEX.get(), GUIHeaterHeatex::new);
         sink.register(ModMenus.MACHINE_WOOD_BURNER.get(), GUIMachineWoodBurner::new);
        sink.register(ModMenus.MACHINE_VACUUM_DISTILL.get(), GUIMachineVacuumDistill::new);
        sink.register(ModMenus.MACHINE_CATALYTIC_REFORMER.get(), GUIMachineCatalyticReformer::new);
        sink.register(ModMenus.MACHINE_HYDROTREATER.get(), GUIMachineHydrotreater::new);
        sink.register(ModMenus.MACHINE_COKER.get(), GUIMachineCoker::new);
        sink.register(ModMenus.FURNACE_COMBINATION.get(), GUIFurnaceCombination::new);
        sink.register(ModMenus.MACHINE_PYROOVEN.get(), GUIPyroOven::new);
        sink.register(ModMenus.MACHINE_COMBUSTION_ENGINE.get(), GUICombustionEngine::new);
        sink.register(ModMenus.MACHINE_COMPRESSOR.get(), GUICompressor::new);
        sink.register(ModMenus.MACHINE_ARC_WELDER.get(), GUIMachineArcWelder::new);
        sink.register(ModMenus.MACHINE_ASSEMBLY_FACTORY.get(), GUIMachineAssemblyFactory::new);
        sink.register(ModMenus.MACHINE_CHEMICAL_FACTORY.get(), GUIMachineChemicalFactory::new);
        sink.register(ModMenus.ASHPIT.get(), GUIAshpit::new);
        sink.register(ModMenus.MACHINE_STRAND_CASTER.get(), GUIMachineStrandCaster::new);
        sink.register(ModMenus.MACHINE_ARC_FURNACE.get(), GUIMachineArcFurnaceLarge::new);
        BombClientRegistrations.registerScreens(sink);
        Bombs2ClientRegistrations.registerScreens(sink);
        CustomMissileClientRegistrations.registerScreens(sink);
        RadarUtilityClientRegistrations.registerScreens(sink);
        SatelliteClientRegistrations.registerScreens(sink);
        DecoCrateClientRegistrations.registerScreens(sink);
        GapsBClientRegistrations.registerScreens(sink); // G-B: held crates, wrench readout
        IcfClientRegistrations.registerScreens(sink);
        DungeonClientRegistrations.registerScreens(sink);
        RtgClientRegistrations.registerScreen(sink);
        sink.register(ModMenus.RADIO_TORCH.get(), RadioTorchScreen::new);
        sink.register(ModMenus.RADIO_READER.get(), RadioReaderScreen::new);
        sink.register(ModMenus.RADIO_LOGIC.get(), RadioLogicScreen::new);
        sink.register(ModMenus.RADIO_COUNTER.get(), RadioCounterScreen::new);
        sink.register(ModMenus.RADIO_CONTROLLER.get(), RadioControllerScreen::new);
        sink.register(ModMenus.RADIO_TELEX.get(), RadioTelexScreen::new);
        sink.register(ModMenus.RADIO_AUTOCAL.get(), RadioAutocalScreen::new);
        FluidNetClientRegistrations.registerScreens(sink);
        ConveyorClientRegistrations.registerScreens(sink);
        EnergyNetClientRegistrations.registerScreens(sink);
        PneumaticClientRegistrations.registerScreens(sink);
        DroneClientRegistrations.registerScreens(sink);
        ProductionClientRegistrations.registerScreens(sink);
        Production2ClientRegistrations.registerScreens(sink);
        SecurityClientRegistrations.registerScreens(sink);
        NuclearMiscClientRegistrations.registerScreens(sink);
        PwrClientRegistrations.registerScreens(sink);
        PowerGenClientRegistrations.registerScreens(sink);
        ExplosivesClientRegistrations.registerScreens(sink);
        MeleeKitClientRegistrations.registerScreens(sink);
        UtilityToolClientRegistrations.registerScreens(sink);
        CustomMachineClientRegistrations.registerScreens(sink);
        sink.register(ModMenus.MACHINE_DIFURNACE.get(), GUIDiFurnace::new);
        sink.register(ModMenus.MASS_STORAGE.get(), GUIMassStorage::new);
        sink.register(ModMenus.CRATE_TEMPLATE.get(), GUICrateTemplate::new);
        RBMKClientScreens.register(sink);
        RBMKGauge.OPEN_SCREEN = gauge -> Minecraft.getInstance().setScreen(new ScreenRBMKGauge(gauge));
        RBMKTerminal.OPEN_SCREEN = terminal ->
                Minecraft.getInstance().setScreen(
                        new ScreenRBMKTerminal(terminal));
    }

    public static void registerHud(HudSink sink) {
        GunFactoryClient.init();
        sink.register("mode_badges", BadgeOverlay::render);
        ArmorSetBClient.registerHud(sink);
        sink.register("look_overlay", GeoNtmLookOverlay::render);
        sink.register("boss_status", BossStatus::render); // 1.7 BossStatus: render-pushed boss bar
        sink.register("rad_counter", RadCounterOverlay::render);
        EnergyNetClientRegistrations.registerHud(sink);
        sink.register("nuke_flash", NukeScreenEffects::render);
        sink.register("gun_hud", GunHudOverlay::render);
        sink.register("armor_hud", ArmorHudOverlay::render);
        ArmorSetClient.registerHud(sink); // final armor sets A: dash bar, HEV health/armour replacement
        sink.register("jetpack_hud", JetpackHudOverlay::render);
        AbilityToolClientRegistrations.registerHud(sink);
        RenderInputClientRegistrations.registerHud(sink); // armour-mod shield bar
        if (System.getenv("GEONTM_JETPACK_SCREENSHOTS") != null && Platform.isDevelopment())
            sink.register("jetpack_visual_check", JetpackSmokeClient::render);
        if (GunSmokeClient.enabled() && Platform.isDevelopment())
            sink.register("gun_smoke_capture", GunSmokeClient::render);
    }

    /**
     * Every GEONTM screen and renderer through GeoCore's loader-neutral {@code ClientRegistrations}: the same path an
     * addon's client start-up takes. Called once from each loader's client init.
     */
    public static void installScreensAndRenderers() {
        ClientRegistrations.screens(screens -> registerScreens(screens::register));
        ClientRegistrations.renderers(renderers -> registerRenderers(
                new GeoNtmBlockEntityRenderers.Sink() {
                    @Override
                    public <T extends BlockEntity,
                            S extends CompatRenderState> void register(
                            BlockEntityType<? extends T> type,
                            CompatBERProvider<T, S> provider) {
                        renderers.blockEntity(type, provider);
                    }
                }, new GeoNtmEntityRenderers.Sink() {
                    @Override
                    public <T extends Entity> void register(
                            EntityType<? extends T> type,
                            EntityRendererProvider<T> provider) {
                        renderers.entity(type, provider);
                    }
                }));
    }

    public static void registerRenderers(GeoNtmBlockEntityRenderers.Sink blockEntities,
            GeoNtmEntityRenderers.Sink downstream) {
        GeoNtmBlockEntityRenderers.registerAll(blockEntities);
        RailClientRegistrations.registerRenderers(blockEntities);
        Set<EntityType<?>> bound = new HashSet<>();
        GeoNtmEntityRenderers.Sink entities = GeoNtmEntityRenderers.recording(downstream, bound);
        GeoNtmEntityRenderers.registerAll(entities);
        FluidNetClientRegistrations.registerRenderers(blockEntities);
        ConveyorClientRegistrations.registerRenderers(blockEntities);
        EnergyNetClientRegistrations.registerRenderers(blockEntities);
        PneumaticClientRegistrations.registerRenderers(blockEntities);
        ProductionClientRegistrations.registerRenderers(blockEntities);
        Production2ClientRegistrations.registerRenderers(blockEntities);
        DoorClientRegistrations.registerRenderers(blockEntities);
        NuclearMiscClientRegistrations.registerRenderers(blockEntities);
        PowerGenClientRegistrations.registerRenderers(blockEntities);
        ExplosivesClientRegistrations.registerRenderers(blockEntities);
        DecoClientRegistrations.registerRenderers(blockEntities);
        BombClientRegistrations.registerBlockEntityRenderers(blockEntities);
        Bombs2ClientRegistrations.registerRenderers(blockEntities);
        CustomMissileClientRegistrations.registerRenderers(blockEntities);
        RadarUtilityClientRegistrations.registerRenderers(blockEntities);
        SatelliteClientRegistrations.registerRenderers(blockEntities);
        CustomMachineClientRegistrations.registerRenderers(blockEntities);
        DecoCrateClientRegistrations.registerRenderers(blockEntities);
        GapsBClientRegistrations.registerRenderers(blockEntities); // G-B: pipe anchor + pipelines
        GeoNtmBlockEntityRenderers.regGem(blockEntities, ModArmorSetBBlockEntities.STATUE_ELB_F.get(),
                RenderDecoBlockAlt::new); // final armor B: statue_elb_f
        IcfClientRegistrations.registerRenderers(blockEntities);
        EnergyBedrockClientRegistrations.registerRenderers(blockEntities);
        DungeonClientRegistrations.registerRenderers(blockEntities);
        StructureWandClientRegistrations.registerRenderers(blockEntities);
        BombClientRegistrations.registerEntityRenderers(entities);
        SatelliteClientRegistrations.registerEntityRenderers(entities);
        RtgClientRegistrations.registerRenderer(blockEntities);
        GeoNtmEntityRenderers.verifyCoverage(bound);

    }

    /**
     * GemRender's count of what it holds, on the F3 screen: an instanced and a vanilla draw look the same, so a
     * working engine is told from one that received nothing by models owned against models declared.
     */
    public static void installDebugLines() {
        DebugLines.register(Constants.MOD_ID, "gemrender", out -> {
            Instancing.Report report = Instancing.report();
            out.accept("gemrender: " + report.backend() + ", " + report.owning() + "/" + report.visuals()
                    + " models (" + report.animated() + " animated, " + report.moving() + " moving), "
                    + report.instances() + " parts in " + report.buckets() + " meshes");
            out.accept("gemrender: " + report.writes() + " transforms written, " + report.skipped()
                    + " parts skipped");
            int fallen = report.visuals() - report.healthy();
            if (fallen > 0) out.accept("gemrender: " + fallen + " models fell back to vanilla");
        });
    }

    public static void installTints() {
        ClientFluidProperties.init();
        Tints.install(Constants.MOD_ID);
        BlockLayers.install(Constants.MOD_ID);
        EnergyBedrockClientRegistrations.installSprites(); // computed bedrock ore grade art
        TokenFluids.installClient(); // how other mods draw GEONTM fluids
        WorldFluids.installClient(); // the placeable 1.7 fluids' sprites and layers
    }

    /**
     * {@code geontm:model_state}: the sprite an item with typed state draws ({@link fr.geoventure.geontm.items.ModelState}), computed
     * from that state and never saved.
     */
    public static void installItemModelProperties() {
        UtilityToolClientRegistrations.installItemModelProperties();
        ScrapsRendering.install();
        ItemModelProperties.register(Constants.MOD_ID, "model_state",
                ModelState::of);
    }

    public static void registerBlockColors() {
        GeoNtmBlockColors.registerAll();
        ConveyorClientRegistrations.registerBlockColors();
        TerrainClientRegistrations.registerBlockColors();
        ChunkModelRegistrations.registerBlockColors();
    }

    /**
     * Attaches the worn-armour layer to every humanoid renderer, and declares the suits' part trees.
     *
     * <p>GeoCore owns the attaching (a mod-bus event on NeoForge/Forge, a per-renderer callback on Fabric). Check
     * the layer count it reports ({@code HumanoidLayers.attachedCount}): a layer attached to nothing draws nothing
     * and throws nothing.
     */
    public static void installArmorLayers() {
        ArmorRenderers.init();
        fr.geoventure.geontm.space.client.SpaceMobRenderers.init(); // NTM: Space : rendus des créatures
        // 1.7 draws helmet overlays at HELMET and the scope at HOTBAR's pre event: both under the crosshair and hotbar.
        Hud.addCameraOverlay("geontm", "camera_overlays", graphics -> {
            ArmorSetBClient.renderHelmetOverlay(graphics);
            GunScopeOverlay.render(graphics);
        });
        installJetpackBridge();
        HeatDistortionRenderType.configure(
                "geontm:pipeline/heat_distortion", "geontm:heat_distortion");
        // port-only black hole lensing (BlackHoleRenderer.Lens)
        SceneWarp.configure("geontm:pipeline/black_hole_lens", "geontm:black_hole_lens");
        // 1.12 GeoNtmShaderManager2 bloom/distortion layers, composited after the hand (first-person crucible blade)
        ScreenEffects.init();
        // 1.12.2 crucible/sword cutters: first-person Collada scene, slicing input, cut mobs
        CrucibleClient.register();
        // 1.12.2 gluon gun (beam, flare, burn trail, disintegration, sounds) and jade shotgun (recoil, smoke)
        GluonGunClient.register();
        JadeShotgunClient.register();
        JetpackClientVisuals.init();
         JetpackSmokeClient.register();
         EntityRenderSmokeClient.register();
         GunSmokeClient.register();
         TorexBenchmarkClient.register();
        ClientResourceReloads.register(Constants.MOD_ID, "jetpack_visuals",
                () -> {
                    ArmorRenderers.reload();
                    JetpackClientVisuals.reload();
                });
        // HBM's player hooks run in the phase geontm:player, before the default phase addons register in (their
        // listeners see HBM's pose, as when HBM's was a single slot run first).
        PlayerRenderHooks.POSE.addPhaseOrdering("geontm:player", Hook.DEFAULT_PHASE);
        PlayerRenderHooks.POSE.register("geontm:player",
                JetpackClientVisuals::applyPlayerPose);
        PlayerRenderHooks.flightPose(JetpackClientVisuals::directionalRenderPose);
        PlayerRenderHooks.POSE.register("geontm:player", (player, pose, partialTick) -> ModelMan.posing(player));
        PlayerRenderHooks.MODEL.addPhaseOrdering("geontm:player", Hook.DEFAULT_PHASE);
        PlayerRenderHooks.MODEL.register("geontm:player", parts -> {
            // The source applies gun aimedBow during model setup; keep its arm pose after the glider's
            // flight freeze while retaining the glider's body, head and leg precedence.
            JetpackClientVisuals.applyPlayerModelPose(parts);
            GunPlayerPose.apply(parts);
        });
        CameraHooks.LOWER_THIRD_PERSON.addPhaseOrdering("geontm:player", Hook.DEFAULT_PHASE);
        CameraHooks.LOWER_THIRD_PERSON.register("geontm:player",
                JetpackClientVisuals::lowerThirdPersonCamera);
        HumanoidLayers.register(
                ArmorRenderLayer::new);
        HumanoidLayers.register(
                JetpackRenderLayer::new);
        HumanoidLayers.register(ArmorModRenderLayer::new);
        HumanoidLayers.register(WornModelRenderLayer::new); // final armor B: goggles, glasses, hat, no9
        HumanoidLayers.register(EgonBackpackRenderLayer::new);
        // 1.7 RenderAccessoryUtility: special players' wings, axe pack and tail, and the GEONTM capes
        HumanoidLayers.register(SpecialPlayerRenderLayer::new);
        Capes.register(RenderAccessoryUtility::getCloakFromPlayer);
        RenderBobble.preloadSkins(); // 1.12.2 ClientHttpHandler.preinit: skinned bobbleheads
    }

    /** Connects the client-owned visuals to the common JetpackHandler without making it import client APIs. */
    private static void installJetpackBridge() {
        JetpackHandler.setClientExhaustCallback((level, payload) ->
                JetpackClientVisuals.spawnExhaustCue(level, payload.playerId(), payload.mode()));
        JetpackClientBridge.install(player -> {
            JetpackHandler.JetpackInfo info = JetpackHandler.getState(player);
            if (info == null) return null;
            return new JetpackClientBridge.State() {
                @Override public boolean opening() { return info.opening; }
                @Override public boolean hover() { return info.hover; }
                @Override public float thrust() { return info.thrust; }
                @Override public float prevThrust() { return info.prevThrust; }
                @Override public int jetpackFlyTime() { return info.jetpackFlyTime; }
                @Override public int prevJetpackFlyTime() { return info.prevJetpackFlyTime; }
                @Override public int failureTicks() { return info.failureTicks; }
                @Override public boolean useCompactHUD() { return info.useCompactHUD; }
                @Override public long animTime() { return info.animTime; }
                @Override public boolean active() {
                    return JetpackHandler.isJetpackActive(player);
                }
            };
        }, stack -> {
            if (stack.isEmpty()) return null;
            FluidType type = JetpackHandler.getFuelType(stack);
            return new JetpackClientBridge.Fuel(type == null ? "" : type.getName(),
                    JetpackHandler.getFuel(stack),
                    JetpackHandler.getMaxFuel(stack));
        });
    }

    /** Registers HBM's key mappings and starts the per-tick input poll that drives guns. */
    public static void installKeybinds() {
        GeoNtmKeybindsClient.register();
        ArmorSetClient.registerKeys(); // final armor sets A: bismuth dash key
        QMAWClient.register();
        CanneryClient.register(); // 1.7 WIAJ presentations: Left Shift + QMAW key on a hovered item
        ArmorModTooltips.register();
        BlockTraitTooltips.register();
        StandardInfo.setShiftHeldPoll(CanneryClient::shiftHeld);
        GunRecoil.register();
        LampLights.register();
        GrenadeUniversalClient.register(); // 1.7 ItemGrenadeUniversal.onUpdate's client half (arm swing)
        CraneKeybindHandler.register();
        UtilityToolClientRegistrations.installKeybinds();
        AbilityToolClientRegistrations.registerKeys();
        RenderInputClientRegistrations.installKeybinds(); // magnet toggle key
    }

    /**
     * Client halves of the ported ntm-next entities: the looped-sound factory behind
     * {@link fr.geoventure.geontm.sound.AudioSystem} (UFO, hunter chopper, worm), the entity particle handlers, and the
     * conveyor wand's world-frame ghost preview.
     */
    public static void installEntityEffects() {
        AudioSystem.installClientFactory(ClientAudioFactory.INSTANCE);
        ClientEffects.install();
        // 1.7 RenderOverhead.renderActionPreview: the conveyor wand's ghost run, drawn at world-frame end.
        ConveyorPreviewRenderer.init();
        // 1.7 BlockDummyable.drawPlacementHighlight: the held multiblock's footprint outline, green/red.
        MultiblockPlacementHighlight.init();
        MultiblockOutline.init(); // ntm-next: the hovered machine is outlined whole
        // 1.7 RenderNTMSkyboxImpact / RenderNTMSkyboxChainloader and the tick that installs them (1.31_enableSkyboxes).
        NTMSkyboxes.register();
        SmogFog.register(); // 1.7 ModEventHandlerRenderer smog (POL_03/06/07)
        GeoNtmClientCommands.register(); // 1.7 client commands: /ntmclient, /ntmwikirender, /dumpthreadsandcrashgame
        RenderInputClientRegistrations.install(); // overhead markers, HUD, world text, particles
    }

    /** Declares which animation each gun plays for each cue. Items resolve lazily, at render time. */
    public static void installGunAnimations() {
        GunEffectState.install();
        TracerRegistry.init();
        CasingParticles.init();
        DisintegrationParticles.init();
        DebrisParticles.init();
        GunEffectState.setCasingHandler(event -> CasingParticles.spawn(event));
        GunEffectState.setGunCasingHandler(payload -> CasingParticles.spawn(
                new CasingCreator.Event(payload.x(), payload.y(), payload.z(), payload.mX(),
                        payload.mY(), payload.mZ(), payload.yaw(), payload.pitch(), payload.mPitch(), payload.mYaw(),
                        payload.casing(), payload.smoking(), payload.smokeLife(), payload.smokeLift(), payload.nodeLife(),
                        true, payload.legacy())));
        GunAnimations.init();
        DrillHighlightClient.init();
        ClientResourceReloads.register(Constants.MOD_ID,
                "lag_animations", LagAnimationLoader::invalidate);
        // The weapon renderers a gun item definition can name. Both eras read the same definitions:
        // 26.1 through its item-model system, older versions through GunLegacyModels' own parse.
        WeaponRenderers.init();
        Fov.registerMultiplier(WeaponRenderers::viewFov);
        Crosshairs.register(GunCrosshairOverlay::render);
    }

}
