package fr.geoventure.geontm.main;

import fr.geoventure.geontm.handler.BobmazonSignHandler;
import fr.geoventure.geontm.advancement.AdvancementCommandContent;
import fr.geoventure.geontm.blocks.ModAnvilBlocks;
import fr.geoventure.geontm.blocks.ModBeaconBlocks;
import fr.geoventure.geontm.blocks.ModBlocks;
import fr.geoventure.geontm.blocks.ModBombs2Content;
import fr.geoventure.geontm.blocks.ModConveyorContent;
import fr.geoventure.geontm.blocks.ModCustomMachineContent;
import fr.geoventure.geontm.blocks.ModCustomMissileContent;
import fr.geoventure.geontm.blocks.ModDecoBlocks;
import fr.geoventure.geontm.blocks.ModDecoContent;
import fr.geoventure.geontm.blocks.ModDecoCrateContent;
import fr.geoventure.geontm.blocks.ModDoorContent;
import fr.geoventure.geontm.blocks.ModDroneContent;
import fr.geoventure.geontm.blocks.ModDungeonContent;
import fr.geoventure.geontm.blocks.ModEnergyBedrockContent;
import fr.geoventure.geontm.blocks.ModEnergyNetContent;
import fr.geoventure.geontm.blocks.ModExplosivesContent;
import fr.geoventure.geontm.blocks.ModFallingBlocks;
import fr.geoventure.geontm.blocks.ModFalloutBlocks;
import fr.geoventure.geontm.blocks.ModFluidNetContent;
import fr.geoventure.geontm.blocks.ModGapsBContent;
import fr.geoventure.geontm.blocks.ModGlassBlocks;
import fr.geoventure.geontm.blocks.ModHazardBlocks;
import fr.geoventure.geontm.blocks.ModIcfContent;
import fr.geoventure.geontm.blocks.ModLadderBlocks;
import fr.geoventure.geontm.blocks.ModNuclearBlocks;
import fr.geoventure.geontm.blocks.ModNuclearMiscContent;
import fr.geoventure.geontm.blocks.ModOilBlocks;
import fr.geoventure.geontm.blocks.ModOreBlocks;
import fr.geoventure.geontm.blocks.ModOutgasBlocks;
import fr.geoventure.geontm.blocks.ModPneumaticContent;
import fr.geoventure.geontm.blocks.ModPowerGenContent;
import fr.geoventure.geontm.blocks.ModProduction2Content;
import fr.geoventure.geontm.blocks.ModProductionContent;
import fr.geoventure.geontm.blocks.ModPwrContent;
import fr.geoventure.geontm.blocks.ModRadarUtilityContent;
import fr.geoventure.geontm.blocks.ModRailBlocks;
import fr.geoventure.geontm.blocks.ModSatelliteContent;
import fr.geoventure.geontm.blocks.ModSecurityContent;
import fr.geoventure.geontm.blocks.ModSteelBlocks;
import fr.geoventure.geontm.blocks.ModTerrainContent;
import fr.geoventure.geontm.blocks.ModUtilityToolContent;
import fr.geoventure.geontm.blocks.bomb.ModBombBlocks;
import fr.geoventure.geontm.blocks.generic.ModStructureWandBlocks;
import fr.geoventure.geontm.blocks.machine.ModArmorTableBlocks;
import fr.geoventure.geontm.blocks.machine.ModMachineBlocks;
import fr.geoventure.geontm.blocks.machine.ModWeaponTableBlocks;
import fr.geoventure.geontm.blocks.machine.dfc.ModDFCBlocks;
import fr.geoventure.geontm.blocks.machine.pile.ModPileBlocks;
import fr.geoventure.geontm.blocks.machine.rbmk.ModRBMKBlocks;
import fr.geoventure.geontm.blocks.machine.storage.ModBatteryBlocks;
import fr.geoventure.geontm.blocks.machine.storage.ModStorageBlocks;
import fr.geoventure.geontm.blocks.turret.ModTurretBlocks;
import fr.geoventure.geontm.capability.ForeignFluids;
import fr.geoventure.geontm.capability.ForeignItems;
import fr.geoventure.geontm.compat.GeoNtmBlockInfo;
import fr.geoventure.geontm.compat.computer.GeoNtmComputers;
import fr.geoventure.geontm.handler.ae2.AE2CompatHandler;
import fr.geoventure.geontm.config.BombConfig;
import fr.geoventure.geontm.config.Configuration;
import fr.geoventure.geontm.config.GeneralConfig;
import fr.geoventure.geontm.config.MachineConfig;
import fr.geoventure.geontm.config.MobConfig;
import fr.geoventure.geontm.config.PotionConfig;
import fr.geoventure.geontm.config.RadiationConfig;
import fr.geoventure.geontm.config.StructureConfig;
import fr.geoventure.geontm.config.ToolConfig;
import fr.geoventure.geontm.config.WeaponConfig;
import fr.geoventure.geontm.config.WorldConfig;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geontm.entity.ModEntities;
import fr.geoventure.geontm.entity.ModMobSpawns;
import fr.geoventure.geontm.entity.ModNtmEntities;
import fr.geoventure.geontm.entity.ModRBMKEntities;
import fr.geoventure.geontm.entity.missile.ModMissileEntities;
import fr.geoventure.geontm.extprop.GeoNtmPlayerProps;
import fr.geoventure.geontm.handler.BossSpawnHandler;
import fr.geoventure.geontm.handler.ImpactSky;
import fr.geoventure.geontm.handler.JetpackHandler;
import fr.geoventure.geontm.handler.NTMToolHandler;
import fr.geoventure.geontm.handler.SpecialPlayers;
import fr.geoventure.geontm.handler.neutron.NeutronLifecycle;
import fr.geoventure.geontm.handler.pollution.PollutionLifecycle;
import fr.geoventure.geontm.handler.radiation.RadiationFog;
import fr.geoventure.geontm.handler.radiation.RadiationWalls;
import fr.geoventure.geontm.inventory.ModGunSupportMenus;
import fr.geoventure.geontm.inventory.ModMenus;
import fr.geoventure.geontm.inventory.ModRBMKMenus;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.inventory.material.Mats;
import fr.geoventure.geontm.inventory.material.ModMaterialTags;
import fr.geoventure.geontm.inventory.recipes.ModCraftingRecipes;
import fr.geoventure.geontm.inventory.recipes.ModRecipes;
import fr.geoventure.geontm.inventory.recipes.loader.RecipeConditions;
import fr.geoventure.geontm.itempool.ItemPool;
import fr.geoventure.geontm.items.ModAbilityToolItems;
import fr.geoventure.geontm.items.ModArmorItems;
import fr.geoventure.geontm.items.ModArmorModItems;
import fr.geoventure.geontm.items.ModArmorModules;
import fr.geoventure.geontm.items.ModArmorSetBContent;
import fr.geoventure.geontm.items.ModArmorSetContent;
import fr.geoventure.geontm.items.ModAutogenItems;
import fr.geoventure.geontm.items.ModBatterySCItems;
import fr.geoventure.geontm.items.ModCasingItems;
import fr.geoventure.geontm.items.ModCatalystItems;
import fr.geoventure.geontm.items.ModChemicalDyeItems;
import fr.geoventure.geontm.items.ModConsumableTailItems;
import fr.geoventure.geontm.items.ModEntityItems;
import fr.geoventure.geontm.items.ModFinalConsumableItems;
import fr.geoventure.geontm.items.ModFoodItems;
import fr.geoventure.geontm.items.ModFuelItems;
import fr.geoventure.geontm.items.ModGearItems;
import fr.geoventure.geontm.items.ModGunSupportItems;
import fr.geoventure.geontm.items.ModIntermediateItems;
import fr.geoventure.geontm.items.ModItemModels;
import fr.geoventure.geontm.items.ModJetpackDependencyItems;
import fr.geoventure.geontm.items.ModMeleeKitItems;
import fr.geoventure.geontm.items.ModMissileItems;
import fr.geoventure.geontm.items.ModOilItems;
import fr.geoventure.geontm.items.ModRBMKItems;
import fr.geoventure.geontm.items.ModRenderInputContent;
import fr.geoventure.geontm.items.ModRtgContent;
import fr.geoventure.geontm.items.ModSatelliteItems;
import fr.geoventure.geontm.items.ModSelfchargerItems;
import fr.geoventure.geontm.items.ModSingularityItems;
import fr.geoventure.geontm.items.ModSpawnEggs;
import fr.geoventure.geontm.items.ModStorageDrumItems;
import fr.geoventure.geontm.items.ModToolItems;
import fr.geoventure.geontm.items.ModUpgradeItems;
import fr.geoventure.geontm.items.ModWandItems;
import fr.geoventure.geontm.items.ModWatzItems;
import fr.geoventure.geontm.items.ModZirnoxItems;
import fr.geoventure.geontm.items.machine.ModMoldItems;
import fr.geoventure.geontm.items.machine.ModStampItems;
import fr.geoventure.geontm.items.special.ItemHot;
import fr.geoventure.geontm.items.weapon.sedna.factory.GunCatalogue;
import fr.geoventure.geontm.lib.ModDamageTypes;
import fr.geoventure.geontm.particle.GeoNtmParticles;
import fr.geoventure.geontm.potion.ModEffects;
import fr.geoventure.geontm.saveddata.TomSaveData;
import fr.geoventure.geontm.sound.ModSounds;
import fr.geoventure.geontm.items.ModBatteryItems;
import fr.geoventure.geontm.items.ModFluidItems;
import fr.geoventure.geontm.items.ModBombItems;
import fr.geoventure.geontm.items.ModDataComponents;
import fr.geoventure.geontm.items.ModItems;
import fr.geoventure.geontm.items.ModMachineItems;
import fr.geoventure.geontm.items.ModIngredientItems;
import fr.geoventure.geontm.items.ModMedicineItems;
import fr.geoventure.geontm.tileentity.ModBlockEntities;
import fr.geoventure.geontm.tileentity.ModRBMKBlockEntities;
import fr.geoventure.geontm.tileentity.machine.rbmk.RBMKDials;
import fr.geoventure.geontm.tileentity.machine.pile.TileEntityPileCore;
import fr.geoventure.geontm.tileentity.network.RTTYSystem;
import fr.geoventure.geontm.tileentity.network.RequestNetwork;
import fr.geoventure.geontm.util.ChunkUtil;
import fr.geoventure.geontm.world.biome.ModBiomes;
import fr.geoventure.geontm.world.chunk.ChunkLoadingManager;
import fr.geoventure.geontm.world.gen.LegacyDungeonDeclarations;
import fr.geoventure.geontm.world.gen.LegacyDungeonStructure;
import fr.geoventure.geontm.world.gen.ModBiomeModifiers;
import fr.geoventure.geontm.world.gen.ModFeatures;
import fr.geoventure.geontm.world.gen.NTMComponentStructures;
import fr.geoventure.geontm.world.gen.NTMWorldGenerator;
import fr.geoventure.geocore.platform.ConfigFiles;
import fr.geoventure.geocore.platform.Lifecycle;
import fr.geoventure.geocore.world.BiomeFeatures;
import java.nio.file.Path;
import java.util.Random;
import fr.geoventure.geontm.util.Owners;

/**
 * Common bootstrap entry point. Loads the content classes so their {@code DeferredRegister}s are
 * populated. Must be invoked by every loader <em>before</em> registration is flushed (for NeoForge,
 * before {@code RegisterEvent} fires; for Fabric, before {@code flushAll}).
 */
public final class MainRegistry {

    private MainRegistry() {}

    /**
     * The "polaroid": a per-launch easter-egg roll, ported from 1.12.2's {@code MainRegistry}. Rolled
     * once at bootstrap as {@code rand.nextInt(18) + 1}, <b>re-rolling 4 and 9</b> (upstream skips
     * those two ids). Only one consumer is ported: on a world where this lands on 11, a
     * digamma-wavelength FEL beam leaves spreading {@code digamma_matter} instead of a plain
     * {@code fire_digamma}. Upstream also honoured a {@code generalOverride} config, which the port
     * has no config file for: set the field directly if you want to force an id.
     */
    public static int polaroidID = 1;

    private static void rollPolaroid() {
        Random rand = new Random();
        do {
            polaroidID = rand.nextInt(18) + 1;
        } while (polaroidID == 4 || polaroidID == 9);
    }

    /** {@code config/geontm.cfg}, 1.7's {@code event.getSuggestedConfigurationFile()}. */
    public static Path configFile() {
        return ConfigFiles.configDir().resolve("geontm.cfg");
    }

    /**
     * 1.7 {@code MainRegistry.loadConfig}: every {@code loadFromConfig} in 1.7's order, then {@code config.save()}, which
     * writes the file with any missing key at its default. Runs first in {@link #bootstrap}, before anything is
     * registered, as 1.7's ran in pre-init.
     */
    public static void loadConfig(Configuration config) {
        config.load();

        GeneralConfig.loadFromConfig(config);
        WorldConfig.loadFromConfig(config);
        MachineConfig.loadFromConfig(config);
        BombConfig.loadFromConfig(config);
        RadiationConfig.loadFromConfig(config);
        PotionConfig.loadFromConfig(config);
        ToolConfig.loadFromConfig(config);
        WeaponConfig.loadFromConfig(config);
        MobConfig.loadFromConfig(config);
        StructureConfig.loadFromConfig(config);

        config.save();
    }

    public static void bootstrap() {
        loadConfig(new Configuration(configFile()));
        // GeoCore recipe flags for the config branches of 1.7's crafting recipes ("geocore:requires")
        RecipeConditions.init();
        // The fluid registry is a plain static map (not an MC registry); populate it first so any
        // fluid tile/recipe that references Fluids.* during registration finds the types ready.
        rollPolaroid();
        Fluids.init();
        Fluids.declareVanilla(); // every FluidType as a vanilla Fluid for other mods
        Mats.init(); // material registry (tag-based ore-dict replacement)
        // Order matters: blocks before items (block items reference blocks), tabs last.
        ModBlocks.init();
        ModItemModels.declare();
        ModMaterialTags.declare();
        ModDamageTypes.declare();
        ModBiomeModifiers.declare();
        LegacyDungeonDeclarations.declare(); // WG-D: legacy dungeon structures + scattered-block features (before BiomeFeatures.install)
        ModMobSpawns.init(); // 1.7 EntityMappings natural spawns + placements (before BiomeFeatures.install)
        BiomeFeatures.install(); // Fabric: the same biome modifiers through BiomeModifications (no-op elsewhere)
        ModCraftingRecipes.declare();
        ModArmorTableBlocks.init();
        ModWeaponTableBlocks.init();
        ModBeaconBlocks.init(); // plain beacon-base storage blocks
        ModMachineBlocks.init();   // machine blocks, Redstone-over-Radio included (must precede block entities, which reference them)
        ModConveyorContent.init(); // conveyor belts, cranes, piston inserter, wand
        ModPneumaticContent.init(); // pneumatic tube storage system
        ModDroneContent.init(); // delivery drone blocks, linker, request network sweep
        ModProductionContent.init(); // production machines A: rotary furnace, thresher, microwave, autocrafter, conveyor press
        ModProduction2Content.init(); // production machines B: excavator, ore slopper, annihilator, old boiler, RTG furnaces
        ModEnergyNetContent.init(); // pylons/wires, cable variants, legacy storage tiers, HEV battery, tesla
        ModDoorContent.init(); // generic OBJ doors, the old blast door, mod doors and the steel trapdoor
        ModSecurityContent.init(); // barbed wire, spikes, fences, barricade, locksmith table, padlocks
        ModPwrContent.init(); // pressurized water reactor: parts, controller, fuel rods, printer
        ModPowerGenContent.init(); // Leviathan turbine, LPW-2, geothermal exchanger, IGen memorial, siren + tracks
        ModDecoContent.init(); // structural/decorative blocks: pipes, pillars, lamps, spotlights, bricks, asphalt, crates, poles
        ModTerrainContent.init(); // natural/terrain/hazard blocks: plants, frozen ground, biome stone, fissure, volcano, geysers, vents, storage blocks
        ModPileBlocks.init();
        ModDFCBlocks.init();
        ModSatelliteItems.init();
        ModSatelliteContent.init(); // satellites: ID manager, cargo pad, orbital laser, /ntmsatellites
        ModBatteryBlocks.init();   // energy-storage machine blocks (battery socket)
        ModTurretBlocks.init(); // turret pad cell and the turrets on it
        ModStorageBlocks.init();   // storage crates (iron / steel / desh)
        ModBombBlocks.init();      // bomb blocks (also precede block entities)
        ModBombs2Content.init(); // special bombs: custom nuke, F.L.E.I.J.A., Blue Rinse, Prototype, balefire bomb, thermo/EMP/float/flame war
        ModRailBlocks.init(); // the folded rails the ntm-next trains run on
        ModCustomMissileContent.init(); // custom missile parts, assembly station, compact launcher, launch table, large/rusted pads
        ModRadarUtilityContent.init(); // radars, radar screen/linker, forcefield, teleporter/anchor, orbus, silo hatch, Soyuz capsule
        ModDecoCrateContent.init(); // storage crates, bobbleheads/plushies/snowglobes, radios, emitters, coils, cap blocks, absorbers, glyphid hive blocks
        ModGapsBContent.init(); // G-B: crates opened from the hand (CRATE_OPEN_HELD menus)
        ModRenderInputContent.init(); // G-C: tool animation cues, equip edge, rendering/input gap content
        ModIcfContent.init(); // ICF reactor, seed, vessel components, laser controller/array, pellet maker, fuel pellet
        ModEnergyBedrockContent.init(); // boxcable, coated wire, charging station, HE/RF converters, bedrock ore grades
        ModDungeonContent.init(); // dungeon/structure bricks, keyholes, pedestal, skeleton holder, loot pile, old lantern, action blocks, cart rails, rebar + placer
        ModStructureWandBlocks.init(); // structure wand blocks: wand_air/loot/jigsaw/logic/tandem/structure (NBTStructure editing)
        ModHazardBlocks.init(); // radioactive / rad-resistant blocks (radiation emitters)
        ModFalloutBlocks.init(); // fallout layer + waste/sellafite terrain (nuke aftermath)
        ModSteelBlocks.init();   // stateful steel grate + OBJ scaffold
        ModDecoBlocks.init();    // decorative DecoBlock set (steel structures, sat dishes, boxcar/boat)
        ModAnvilBlocks.init();   // the 12 tiered anvils (must precede block entities)
        ModOilBlocks.init();     // special oil blocks (ore_oil deposit / oil_pipe / oil_spill)
        ModNuclearBlocks.init(); // uranium outgassing ores and radon gases
        ModOutgasBlocks.init(); // asbestos, corium cobble, tomb scrap, and lab outgas blocks
        ModOreBlocks.init();     // clusters, depth rock/ores, resource stone, cave spikes, gas pockets
        ModGlassBlocks.init();   // the BlockNTMGlass family (glass_quartz so far)
        ModLadderBlocks.init();  // vanilla LadderBlock family, including ladder_red_top
        ModRBMKBlocks.init(); // reactor columns/peripherals before their block items and BE types
        ModDataComponents.init();  // custom item data components (referenced by hand items below)
        ItemHot.init(); // declare the hot-item component before Fabric's registry flush
        ModRecipes.init(); // custom crafting serializers
        ModItems.init();
        // After ModItems: its container items name ModItems empties as craft remainders at registration time.
        ModFluidNetContent.init(); // fluid-network ducts, barrels, foundry tank, fluid blocks, containers
        ModEntityItems.init(); // spawn items, carts, drones, trains, grenades, dispersers (entity port)
        ModGunSupportItems.init(); // ammo container and stack-backed ammo/casing bags
        ModJetpackDependencyItems.init(); // hot steel and the functional xenon thruster part
        ModAbilityToolItems.init(); // ability tools/weapons, tool sets, meteorite sword chain
        ModMeleeKitItems.init(); // special melee weapons, novelty tools, starter kits, toolbox, loot, Bobmazon
        ModSingularityItems.init(); // dropped singularity capsules and Spark progression
        ModSelfchargerItems.init(); // deprecated powered-armour auxiliary batteries
        ModBatterySCItems.init(); // 1.7 ItemBatterySC radiovoltaic batteries (battery_sc_<variant>)
        ModRtgContent.init(); // RTG pellet state, block entity, and menu registrations
        ModChemicalDyeItems.init(); // flattened chemical-dye and crayon variants
        ModSpawnEggs.init(); // spawn eggs for the 29 1.7 addMob mobs
        ModStorageDrumItems.init();
        ModFoodItems.init();
        ModConsumableTailItems.init();
        ModFinalConsumableItems.init(); // food, consumables, records, batteries, loose materials, 5G/muffler upgrades
        ModCatalystItems.init();
        ModMedicineItems.init();
        ModRBMKItems.init(); // RBMK rods/pellets and reactor tools
        ModZirnoxItems.init(); // component-backed reactor fuel and spent fuel
        ModMachineItems.init();    // hand-registered machine items (templates)
        ModCasingItems.init(); // metal variants of the old metadata casing item
        ModStampItems.init(); // press stamps (dies)
        ModMoldItems.init();  // casting molds (foundry mold/basin) + mold_base
        ModAutogenItems.init();        // auto-generated metal-shape items (cast plate, shell, …)
        ModIntermediateItems.init();   // recipe intermediates (circuit / item_expensive / chunk_ore)
        ModIngredientItems.init(); // escaped metadata families: generic parts, ash, and plate fuel
        ModBatteryItems.init();    // modern battery / capacitor items (the socket's storage medium)
        ModUpgradeItems.init(); // machine upgrade modules (speed/effect/power/overdrive)
        ModFuelItems.init();  // the ItemFuel family (furnace-burnable; burn times wired per loader)
        ModOilItems.init();   // oil-chain items (oil_tar variants + oil_detector)
        ModToolItems.init();  // in-world construction tools (blowtorch, boltgun)
        ModWatzItems.init();  // Watz fuel pellets (one item per type, fresh + depleted)
        ModFallingBlocks.init(); // the BlockFallingBase family (gravity blocks)
        ModFluidItems.init();      // fluid-container items
        ModBombItems.init();       // hand-registered bomb components
        ModGearItems.init(); // radiation gear (geiger/dosimeter + hazmat armor)
        ModArmorItems.init(); // modelled power armour (T-51b)
        ModArmorSetBContent.init(); // final armor B: remaining ModItemsArmor ids, IV bags, statue_elb_f
        ModArmorSetContent.init(); // final armor sets A: alloy, cmb, bismuth, euphemium, zirconium, dnt, AJR/AJRO, HEV, Fau, trenchmaster
        ModArmorModules.init(); // complete armor modules (storage, cladding, inserts)
        ModArmorModItems.init(); // remaining ItemMod* modules, PaA hazmat, liquidator, mask of infamy
        ModUtilityToolContent.init(); // utility/diagnostic tools, pipettes, guide/lore books, holotapes, tablets, Lemegeton, legacy manual block
        ModCustomMachineContent.init(); // custom machine system: controllers from geontmCustomMachines.json, cm_* components, anchor, structure wand
        ModNuclearMiscContent.init(); // radiolysis RTG, legacy breeder/research reactors + controller, legacy graphite pile, breeding/pile rods, spent plate fuel
        ModExplosivesContent.init(); // landmines, timed charges, det blocks, plastic explosive blocks, duds, multi purpose bomb, detonators
        // Ammo, factories, and gun suppliers must all be declared before any loader flushes ITEMs.
        GunCatalogue.init();
        TileEntityPileCore.initDebris();
        ModMissileItems.init(); // launchable missiles + the designators that aim them
        ModWandItems.init(); // creative wand_k / wand_s / wand_d
        AdvancementCommandContent.init(); // 1.7 achievements as advancements (triggers, icons) + ntm* commands
        ModFeatures.init(); // worldgen feature types (1.7 ores, strata, MapGen terrain, surface features)
        BossSpawnHandler.init(); // 1.7 meteor strikes and showers, rolled every level tick
        SpecialPlayers.init(); // 1.7 onPlayerRespawn: Dr_Nostalgia's hat and Beta
        BobmazonSignHandler.init();
        GeoNtmPlayerProps.init(); // 1.7 onPlayerClone: reputation, shield, fbi/rad marks survive respawn
        NTMWorldGenerator.init(); // NBT structures: geontm:nbt structure type, piece type, 1.7 spawn registrations
        NTMComponentStructures.init(); // features/bunker/meteor_dungeon spawns + hardcoded piece types (WG-C)
        LegacyDungeonStructure.init(); // WG-D: 1.7 GeoNtmWorldGen legacy dungeons (geontm:legacy_dungeon) + scattered-block feature types
        ModBiomes.init(); // crater biome keys, backed by worldgen biome JSON
        ModBlockEntities.init();
        ModRBMKBlockEntities.init();
        ModMenus.init();
        ModGunSupportMenus.init();
        ModRBMKMenus.init();
        ModEntities.init();        // entity types (the explosion + fallout entities)
        ModRBMKEntities.init();
        ModMissileEntities.init(); // the missiles
        ModNtmEntities.init(); // the ntm-next entity port (mobs, vehicles, effects, remaining projectiles)
        GeoNtmParticles.init();
        ModEffects.init(); // status effects (radiation)
        ModSounds.init();          // sound events (assembly machine motor/strike/start/stop)
        ModTabs.init();
        NeutronLifecycle.init();
        RBMKDials.init();            // the RBMK dials as /gamerule world rules
        PollutionLifecycle.init();   // soot/poison field: diffusion, decay and blight
        ModEventHandlerMobs.init();     // 1.7 ModEventHandler mob death drops, decorateMob, addAITasks
        ModEventHandlerImpact.init();   // Tom impact aftermath: dust/fire decay, burning, spawns
        ModEventHandlerHooks.init();    // food eaten, block break, sleep, potential spawns
        Owners.init();                  // placer UUID on placed block entities
        ImpactSky.register();         // Tom impact sky darkening (1.7 WorldProviderNTM)
        RadiationFog.init(); // 1.7 radFog, read from the chunk radiation
        RadiationWalls.init(); // the geontm:radiation_resistant tag as radiation walls
        ChunkLoadingManager.init();   // forced-chunk bookkeeping + restart reconciliation
        Lifecycle.onLevelUnload((server, level) -> JetpackHandler.clear(level));
        Lifecycle.onServerTick(server ->
                server.getAllLevels().forEach(JetpackHandler::cleanup));
        // ntm-next entity port: item pools resolve their registry ids once the server holds registries; the
        // TOM impact gate is republished for off-thread worldgen readers; per-server caches clear on stop.
        Lifecycle.onServerStarted(server -> {
            ItemPool.load(server.registryAccess());
            TomSaveData.publish(server);
        });
        Lifecycle.onServerStopping(server -> {
            TomSaveData.onServerStopping();
            RequestNetwork.onServerStopping();
            RTTYSystem.onServerStopping();
            ChunkUtil.onServerStopped();
        });
        // Needs both the tool items and the convertible blocks, so it runs after every registry.
        NTMToolHandler.register();
        // Machines and container items on the loaders' fluid capabilities.
        ForeignFluids.register();
        // Machine and crate contents as a read-only view for other mods' viewers
        ForeignItems.register();
        // 1.7's OpenComputers components as CC: Tweaked peripherals, when CC is loaded
        GeoNtmComputers.init();
        // AE2 pattern providers push jobs into the recipe machines, when AE2 is loaded
        AE2CompatHandler.init();
        // Buffers, tanks and multiblock cores in a block-info overlay (Jade), when one is loaded
        GeoNtmBlockInfo.init();
    }
}
