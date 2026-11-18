package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.blocks.generic.BlockBeaconable;
import fr.geoventure.geontm.blocks.generic.BlockLayering;
import fr.geoventure.geontm.blocks.generic.BlockStalagmite;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geocore.registry.BlockTag;
import fr.geoventure.geocore.registry.Loot;
import fr.geoventure.geocore.registry.Preset;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.dejson.Models;
import fr.geoventure.geocore.dejson.Shape;
import fr.geoventure.geocore.dejson.Slots;
import fr.geoventure.geocore.dejson.Tints;
import fr.geoventure.geocore.dejson.Variants;
import fr.geoventure.geontm.items.ModItems;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import fr.geoventure.geocore.world.Compat;
import java.util.Arrays;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import fr.geoventure.geontm.blocks.bomb.BlockVolcano;
import fr.geoventure.geontm.blocks.bomb.CrystalPulsar;
import fr.geoventure.geontm.blocks.bomb.CrystalVirus;
import fr.geoventure.geontm.blocks.gas.BlockGasCoal;
import fr.geoventure.geontm.blocks.generic.BlockBiomeStone;
import fr.geoventure.geontm.blocks.generic.BlockChlorineSeal;
import fr.geoventure.geontm.blocks.generic.BlockDeadPlant;
import fr.geoventure.geontm.blocks.generic.BlockFissure;
import fr.geoventure.geontm.blocks.generic.BlockGasChlorine;
import fr.geoventure.geontm.blocks.generic.BlockGeysir;
import fr.geoventure.geontm.blocks.generic.BlockHangingVine;
import fr.geoventure.geontm.blocks.generic.BlockHazardFalling;
import fr.geoventure.geontm.blocks.generic.BlockLithium;
import fr.geoventure.geontm.blocks.generic.BlockMeteorMolten;
import fr.geoventure.geontm.blocks.generic.BlockMush;
import fr.geoventure.geontm.blocks.generic.BlockMushHuge;
import fr.geoventure.geontm.blocks.generic.BlockNTMFlower;
import fr.geoventure.geontm.blocks.generic.BlockNuclearWaste;
import fr.geoventure.geontm.blocks.generic.BlockOreBasalt;
import fr.geoventure.geontm.blocks.generic.BlockPorous;
import fr.geoventure.geontm.blocks.generic.BlockReeds;
import fr.geoventure.geontm.blocks.generic.BlockSellafield;
import fr.geoventure.geontm.blocks.generic.BlockTallPlant;
import fr.geoventure.geontm.blocks.generic.BlockTerrainEarth;
import fr.geoventure.geontm.blocks.generic.BlockVent;
import fr.geoventure.geontm.blocks.generic.BlockRadResistant;

/**
 * The natural, terrain and hazard blocks of 1.7's {@code ModBlocks} not registered elsewhere:
 * the plant family, burning ground, stone and ores, geothermal and volcano cores, geysers, gases, hot
 * sellafite, cave spikes, dark crystals, material storage blocks and the gas vents.
 *
 * <p>Hardness, blast resistance, sound, light and creative tab follow each old registration line; the
 * old {@code blockTab} is {@link GeoNtmTab#BLOCKS}, {@code machineTab} {@link GeoNtmTab#MACHINES}, {@code nukeTab}
 * {@link GeoNtmTab#BOMBS}. Metadata sub-blocks are flattened to {@code <base>_<variant>}.
 *
 * <p>Models, blockstate, loot and tags are stated on each registration. The hand-built {@code elements}
 * models {@code plant_reeds}, {@code vine_phosphor_*} and {@code ore_volcano} are authored files under
 * {@code assets/geontm/models/block/}. The packing recipes and the grass tint are in {@link TerrainDeclarations}.
 */
public final class ModTerrainBlocks {

    private ModTerrainBlocks() {}

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCKS, ITEMS);

    private static final String CUTOUT = "minecraft:cutout";
    /** One texture all round, single state, dropping itself, pickaxe. */
    private static Preset cube(String texture) {
        return Presets.cubeAll(texture).and(handle -> handle.loot(Loot.self()).tag(BlockTag.PICKAXE));
    }
    /** A cut-out cross plant whose item is its flat sprite. */
    private static Preset plant(String texture) {
        return handle -> handle.model(d -> d.models(Models.renderType(CUTOUT, Models.cross(texture)))
                .states(Variants.simple()).item(Models.generated(texture)));
    }
    /** The tobacco/hemp tint as an item tint source (see {@link TerrainDeclarations#GRASS_TINT}). */
    private static Presets.ItemModel grassTinted(String texture) {
        return Presets.sprite(texture).tints(Tints.constant(0xFF000000 | TerrainDeclarations.GRASS_TINT));
    }

    private static Shape cross(boolean tinted, String texture) {
        return tinted ? Models.parented("minecraft:block/tinted_cross", Slots.of("cross", texture)) : Models.cross(texture);
    }

    /** Every block item registered here, by block id (for hazard lookup and the declarations). */
    public static final Map<String, RegistryObject<? extends Item>> BLOCK_ITEMS = new LinkedHashMap<>();

    private static final Map<BlockDeadPlant.Type, RegistryObject<BlockDeadPlant>> DEAD_PLANTS = new EnumMap<>(BlockDeadPlant.Type.class);
    private static final Map<BlockNTMFlower.Type, RegistryObject<BlockNTMFlower>> FLOWERS = new EnumMap<>(BlockNTMFlower.Type.class);
    private static final Map<BlockTallPlant.Type, RegistryObject<BlockTallPlant>> TALL_PLANTS = new EnumMap<>(BlockTallPlant.Type.class);
    private static final Map<BlockBiomeStone.Type, RegistryObject<BlockBiomeStone>> BIOME_STONE = new EnumMap<>(BlockBiomeStone.Type.class);
    private static final Map<BlockOreBasalt.Type, RegistryObject<BlockOreBasalt>> BASALT_ORES = new EnumMap<>(BlockOreBasalt.Type.class);
    private static final Map<String, RegistryObject<Block>> METEOR_ORES = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<BlockBeaconable>> COKE_BLOCKS = new LinkedHashMap<>();
    @SuppressWarnings("unchecked")
    private static final RegistryObject<BlockSellafield>[] SELLAFIELD = new RegistryObject[BlockSellafield.LEVELS];
    private static final Map<String, RegistryObject<BlockStalagmite>> SPIKES = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<BlockVolcano>> VOLCANO_CORES = new LinkedHashMap<>();

    // Plants (blockTab, grass sound)

    static {
        for (BlockDeadPlant.Type t : BlockDeadPlant.Type.values()) {
            // 1.7 getItemDropped -> null; the Java getDrops says so, the table exists for completeness
            DEAD_PLANTS.put(t, block(t.id(), p -> new BlockDeadPlant(p, t), ModTerrainBlocks::plantProps, GeoNtmTab.BLOCKS)
                    .transform(plant(t.texture()))
                    .loot(Loot.nothing()));
        }
        for (BlockNTMFlower.Type t : BlockNTMFlower.Type.values()) {
            Reg.Handle<BlockNTMFlower> flower = block(t.id(), p -> new BlockNTMFlower(p, t), p -> plantProps(p).randomTicks(), GeoNtmTab.BLOCKS);
            if (t.tinted()) {
                flower.model(d -> d.models(Models.renderType(CUTOUT, cross(true, t.texture()))).states(Variants.simple()))
                        .transform(grassTinted(t.texture()));
            } else {
                flower.transform(plant(t.texture()));
            }
            FLOWERS.put(t, flower.loot(Loot.self()));
        }
        for (BlockTallPlant.Type t : BlockTallPlant.Type.values()) {
            Reg.Handle<BlockTallPlant> tall = block(t.id(), p -> new BlockTallPlant(p, t), p -> plantProps(p).randomTicks(), GeoNtmTab.BLOCKS);
            Models.Group group = Models.group()
                    .and("_lower", Models.renderType(CUTOUT, cross(t.tinted(), t.texture("lower"))))
                    .and("_upper", Models.renderType(CUTOUT, cross(t.tinted(), t.texture("upper"))));
            if (t.tinted()) {
                tall.model(d -> d.models(group).states(Variants.select("half", "lower", "_lower", "upper", "_upper")))
                        .transform(grassTinted(t.texture("lower")));
            } else {
                tall.model(d -> d.models(group).states(Variants.select("half", "lower", "_lower", "upper", "_upper"))
                        .item(Models.generated(t.texture("lower"))));
            }
            TALL_PLANTS.put(t, tall.loot(Loot.nothing())); // drops are computed in Java (flower item + willow leaves)
        }
    }

    /** The block model is the top segment only; RenderReeds (block entity) draws the column below. */
    public static final RegistryObject<BlockReeds> plant_reeds = block("plant_reeds", BlockReeds::new,
            ModTerrainBlocks::plantProps, GeoNtmTab.BLOCKS)
            .transform(Presets.statesOnly(Variants.simple()))
            .transform(Presets.sprite("geontm:blocks/reeds_top"))
            .loot(Loot.only("minecraft:stick"));
    /** Authored vine_phosphor_ground/_middle/_hang (1.12.2's three models). */
    public static final RegistryObject<BlockHangingVine> vine_phosphor = block("vine_phosphor", BlockHangingVine::new,
            p -> p.strength(0.5F).sound(SoundType.GRASS).noOcclusion().noCollission(), GeoNtmTab.BLOCKS)
            .transform(Presets.statesOnly(Variants.select("part", "ground", "_ground", "middle", "_middle", "hang", "_hang")))
            .transform(Presets.sprite("geontm:blocks/vine_phosphor_item"))
            .loot(Loot.self());
    /** {@code setLightLevel(0.5F)} = 7. */
    public static final RegistryObject<BlockMush> mush = block("mush", BlockMush::new,
            p -> plantProps(p).lightLevel(s -> 7).randomTicks(), GeoNtmTab.BLOCKS)
            .transform(plant("geontm:blocks/mush"))
            .loot(Loot.self());
    /** No creative tab upstream (the huge mushroom is grown, not placed). */
    public static final RegistryObject<BlockMushHuge> mush_block = block("mush_block", BlockMushHuge::new,
            p -> p.strength(0.2F).sound(SoundType.GRASS).lightLevel(s -> 15), null)
            .transform(Presets.cubeAll("geontm:blocks/mush_block_skin")).tag(BlockTag.AXE);
    public static final RegistryObject<BlockMushHuge> mush_block_stem = block("mush_block_stem", BlockMushHuge::new,
            p -> p.strength(0.2F).sound(SoundType.GRASS).lightLevel(s -> 15), null)
            .transform(Presets.column("geontm:blocks/mush_block_inside", "geontm:blocks/mush_block_stem")).tag(BlockTag.AXE);
    /**
     * {@code BlockLayering(Material.leaves)}, texture {@code waste_leaves}, hardness 0.1: the snow-layer shape over
     * waste_leaves (as oil_spill / foam_layer).
     */
    public static final RegistryObject<BlockLayering> leaves_layer = block("leaves_layer", BlockLayering::new,
            p -> p.strength(0.1F).sound(SoundType.GRASS).noOcclusion().randomTicks(), GeoNtmTab.BLOCKS)
            .model(d -> d.models(leafLayers())
                    .states(Variants.select("layers", "1", "_height2", "2", "_height4", "3", "_height6", "4", "_height8",
                            "5", "_height10", "6", "_height12", "7", "_height14", "8", "_full")))
            .icon(d -> d.models(Models.blockItem("geontm:block/leaves_layer_height2")))
            .loot(Loot.nothing()); // 1.7 BlockLayering drops nothing (BlockLayering.getDrops)

    private static Models.Group leafLayers() {
        String leaves = "geontm:blocks/waste_leaves";
        Models.Group layers = Models.group().and("_full", Models.renderType(CUTOUT, Models.cubeAll(leaves)));
        for (int h = 2; h <= 14; h += 2) {
            layers = layers.and("_height" + h, Models.renderType(CUTOUT, Models.parented("minecraft:block/snow_height" + h,
                    Slots.of("particle", leaves, "texture", leaves))));
        }
        return layers;
    }

    // Burning ground (frozen_* live in fr.geoventure.geontm.bombs2)

    public static final RegistryObject<BlockTerrainEarth> burning_earth = block("burning_earth",
            BlockTerrainEarth::new,
            p -> p.strength(0.6F).sound(SoundType.GRASS).randomTicks(), GeoNtmTab.BLOCKS)
            .transform(Presets.cubeBottomTop("geontm:blocks/waste_earth_bottom", "geontm:blocks/burning_grass_side", "geontm:blocks/burning_grass_top"))
            .loot(Loot.nothing()) // drops dirt, computed in Java
            .tag(BlockTag.SHOVEL);

    // Stone and ores

    static {
        for (BlockBiomeStone.Type t : BlockBiomeStone.Type.values()) {
            String top = "geontm:blocks/stone_biome_top" + t.suffix();
            BIOME_STONE.put(t, block(t.id(), p -> new BlockBiomeStone(p, t), ModTerrainBlocks::stoneOre, GeoNtmTab.BLOCKS)
                    .model(d -> d.models(Models.group()
                                    .and("", Models.cubeBottomTop(top, "geontm:blocks/stone_biome_layer" + t.suffix(), top))
                                    .and("_covered", Models.cubeBottomTop(top, "geontm:blocks/stone_biome" + t.suffix(), top)))
                            .states(Variants.select("covered", "false", "", "true", "_covered"))
                            .blockItemDirect())
                    .loot(Loot.self())
                    .tag(BlockTag.PICKAXE));
        }
        for (BlockOreBasalt.Type t : BlockOreBasalt.Type.values()) {
            BASALT_ORES.put(t, block(t.id(), p -> new BlockOreBasalt(p, t), ModTerrainBlocks::stoneOre, GeoNtmTab.BLOCKS)
                    .transform(Presets.cubeBottomTop(t.texture() + "_top", t.texture(), t.texture() + "_top"))
                    .loot(Loot.nothing()) // one drop item, silk touch the block, in Java
                    .tag(BlockTag.PICKAXE));
        }
        for (String v : new String[]{"iron", "copper", "aluminium", "rareearth", "cobalt"}) {
            METEOR_ORES.put(v, block("ore_meteor_" + v, Block::new, ModTerrainBlocks::stoneOre, GeoNtmTab.BLOCKS)
                    .transform(cube("geontm:blocks/ore_meteor." + v)));
        }
    }

    /** 1.7 {@code BlockPorous}: {@code BlockStone} with hardness 1.5 / resistance 30. */
    public static final RegistryObject<BlockPorous> stone_porous = block("stone_porous", BlockPorous::new,
            p -> p.strength(1.5F, 18.0F).sound(SoundType.STONE).requiresCorrectToolForDrops(), GeoNtmTab.BLOCKS)
            .transform(Presets.cubeAll("geontm:blocks/stone_porous"))
            .loot(Loot.nothing()) // cobblestone / silk-touch stone in Java
            .tag(BlockTag.PICKAXE);
    /** {@code setLightLevel(0.75F)} = 11; hardness 15, resistance 360, random-ticked. */
    public static final RegistryObject<BlockMeteorMolten> block_meteor_molten = block("block_meteor_molten", BlockMeteorMolten::new,
            p -> p.strength(15.0F, 216.0F).sound(SoundType.STONE).lightLevel(s -> 11).requiresCorrectToolForDrops().randomTicks(), GeoNtmTab.BLOCKS)
            .transform(Presets.cubeAll("geontm:blocks/meteor_cobble_molten"))
            .loot(Loot.nothing())
            .tag(BlockTag.PICKAXE);
    /** Unbreakable, resistance 1,000,000, full light, random-ticked. */
    public static final RegistryObject<BlockFissure> ore_volcano = block("ore_volcano", BlockFissure::new,
            p -> p.strength(-1.0F, 1_000_000F).sound(SoundType.STONE).lightLevel(s -> 15).randomTicks(), GeoNtmTab.BLOCKS)
            // fissure: authored bedrock + molten overlay model, both crater states
            .transform(Presets.statesOnly(Variants.uniform("crater", "false", "true")))
            .icon(d -> d.models(Models.blockItem("geontm:block/ore_volcano")))
            .loot(Loot.nothing());

    static {
        for (boolean rad : new boolean[]{false, true}) {
            for (BlockVolcano.Mode mode : BlockVolcano.Mode.values()) {
                String id = mode.id(rad);
                Reg.Handle<BlockVolcano> core = REG.block(id, p -> new BlockVolcano(p, mode, rad),
                                p -> p.strength(-1.0F, 6_000F)) // 1.7: no setStepSound -> default stone
                        .transform(Presets.cubeAll(rad ? "geontm:blocks/volcano_rad_core" : "geontm:blocks/volcano_core"))
                        .loot(Loot.self());
                RegistryObject<BlockVolcano.VolcanoItem> item = REG.item(id,
                        p -> new BlockVolcano.VolcanoItem(core.get(), p, mode), Compat::blockDescription)
                        .tab(GeoNtmTab.BOMBS);
                BLOCK_ITEMS.put(id, item);
                VOLCANO_CORES.put(id, core);
            }
        }
    }

    /** Geysers had no creative tab upstream and drop nothing. */
    public static final RegistryObject<BlockGeysir> geysir_chlorine = block("geysir_chlorine",
            p -> new BlockGeysir(p, BlockGeysir.Kind.CHLORINE),
            p -> p.strength(5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops(), null)
            .transform(Presets.cubeBottomTop("minecraft:block/stone", "minecraft:block/stone", "geontm:blocks/geysir_stone")
                    .states(Variants.uniform("active", "false", "true")))
            .loot(Loot.nothing())
            .tag(BlockTag.PICKAXE);
    public static final RegistryObject<BlockGeysir> geysir_nether = block("geysir_nether",
            p -> new BlockGeysir(p, BlockGeysir.Kind.NETHER),
            p -> p.strength(2.0F).sound(SoundType.STONE).lightLevel(s -> 15).requiresCorrectToolForDrops(), null)
            .transform(Presets.cubeBottomTop("minecraft:block/netherrack", "minecraft:block/netherrack", "geontm:blocks/geysir_nether")
                    .states(Variants.uniform("active", "false", "true")))
            .loot(Loot.nothing())
            .tag(BlockTag.PICKAXE);

    // Gases (worldgen and vent cells)

    /** Invisible coal dust (smoke particles only). */
    public static final RegistryObject<BlockGasCoal> gas_coal = REG.block("gas_coal", BlockGasCoal::new, ModTerrainBlocks::gasProps)
            .transform(Presets.statesOnly("geontm:block/gas_radon", Variants.simple()));
    /** Translucent chlorine. */
    public static final RegistryObject<BlockGasChlorine> chlorine_gas = REG.block("chlorine_gas", BlockGasChlorine::new, ModTerrainBlocks::gasProps)
            .transform(Presets.shape(Models.renderType("minecraft:translucent", Models.cubeAll("geontm:blocks/chlorine_gas"))).noItem());

    static {
        // 1.7 registers both gases with a plain item block in the machine tab, drawn with the gas texture.
        REG.blockItem(gas_coal, Compat::blockDescription).transform(Presets.sprite("geontm:blocks/gas_coal")).tab(GeoNtmTab.MACHINES);
        REG.blockItem(chlorine_gas, Compat::blockDescription).transform(Presets.sprite("geontm:blocks/chlorine_gas"))
                .tab(GeoNtmTab.MACHINES);
    }

    // Hot sellafite (sellafield metas 0..5)

    static {
        for (int i = 0; i < BlockSellafield.LEVELS; i++) {
            int level = i;
            SELLAFIELD[i] = block("sellafield_" + i, p -> new BlockSellafield(p, level),
                    p -> p.strength(5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops().randomTicks(), GeoNtmTab.BLOCKS)
                    .transform(cube("geontm:blocks/sellafield_" + i));
        }
    }

    // Cave spikes (EnumStalagmiteType ICE.. GLYPHID3)

    static {
        spike("ice", () -> new ItemStack(ModItems.powder_ice.get()));
        spike("snow", () -> new ItemStack(Items.SNOWBALL));
        spike("glyphid1", () -> ItemStack.EMPTY);
        spike("glyphid2", () -> ItemStack.EMPTY);
        spike("glyphid3", () -> ItemStack.EMPTY);
    }

    // Dark crystals (no tab)

    public static final RegistryObject<CrystalVirus> crystal_virus = block("crystal_virus", CrystalVirus::new,
            p -> p.strength(15.0F, Float.POSITIVE_INFINITY).requiresCorrectToolForDrops().randomTicks(), null) // 1.7: default stone sound
            .transform(cube("geontm:blocks/crystal_virus"));
    public static final RegistryObject<CrystalPulsar> crystal_pulsar = block("crystal_pulsar", CrystalPulsar::new,
            p -> p.strength(15.0F, Float.POSITIVE_INFINITY).requiresCorrectToolForDrops(), null) // 1.7: default stone sound
            .transform(cube("geontm:blocks/crystal_pulsar"));

    // Material and storage blocks

    public static final RegistryObject<BlockBeaconable> block_tcalloy = block("block_tcalloy", BlockBeaconable::new,
            p -> p.strength(5.0F, 42.0F).sound(SoundType.METAL).requiresCorrectToolForDrops(), GeoNtmTab.BLOCKS)
            .transform(cube("geontm:blocks/block_tcalloy"));
    public static final RegistryObject<BlockBeaconable> block_cdalloy = block("block_cdalloy", BlockBeaconable::new,
            p -> p.strength(5.0F, 42.0F).sound(SoundType.METAL).requiresCorrectToolForDrops(), GeoNtmTab.BLOCKS)
            .transform(cube("geontm:blocks/block_cdalloy"));
    public static final RegistryObject<BlockRadResistant> block_australium = block("block_australium", BlockRadResistant::new,
            p -> p.strength(5.0F, 6.0F).requiresCorrectToolForDrops(), GeoNtmTab.BLOCKS) // 1.7: no setStepSound -> default stone
            .transform(cube("geontm:blocks/block_australium"));

    static {
        for (String v : new String[]{"coal", "lignite", "petroleum"}) {
            // 1.7 BlockCoke: flammability 5, spread 10. No cross-loader flammability seam exists yet.
            COKE_BLOCKS.put(v, block("block_coke_" + v, BlockBeaconable::new,
                    p -> p.strength(5.0F, 10.0F).sound(SoundType.METAL).requiresCorrectToolForDrops(), GeoNtmTab.BLOCKS)
                    .transform(cube("geontm:blocks/block_coke." + v)));
        }
    }

    public static final RegistryObject<BlockLithium> block_lithium = block("block_lithium", BlockLithium::new,
            p -> p.strength(5.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops(), GeoNtmTab.BLOCKS)
            .transform(cube("geontm:blocks/block_lithium"));

    public static final RegistryObject<BlockNuclearWaste> block_waste = waste("block_waste");
    public static final RegistryObject<BlockNuclearWaste> block_waste_painted = waste("block_waste_painted");
    public static final RegistryObject<BlockNuclearWaste> block_waste_vitrified = waste("block_waste_vitrified");

    public static final RegistryObject<BlockHazardFalling> block_yellowcake = block("block_yellowcake", BlockHazardFalling::new,
            p -> p.strength(5.0F, 6.0F).sound(SoundType.SAND), GeoNtmTab.BLOCKS)
            .transform(Presets.cubeAll("geontm:blocks/block_yellowcake"))
            .loot(Loot.self())
            .tag(BlockTag.SHOVEL);
    public static final RegistryObject<BlockHazardFalling> block_fallout = block("block_fallout", BlockHazardFalling::new,
            p -> p.strength(0.2F).sound(SoundType.GRAVEL), GeoNtmTab.BLOCKS)
            .transform(Presets.cubeAll("geontm:blocks/ash"))
            .loot(Loot.self())
            .tag(BlockTag.SHOVEL);

    // Vents (machineTab, Material.iron)

    public static final RegistryObject<BlockVent> vent_chlorine = vent("vent_chlorine", BlockVent.Kind.CHLORINE);
    public static final RegistryObject<BlockVent> vent_cloud = vent("vent_cloud", BlockVent.Kind.CLOUD);
    public static final RegistryObject<BlockVent> vent_pink_cloud = vent("vent_pink_cloud", BlockVent.Kind.PINK_CLOUD);
    public static final RegistryObject<BlockChlorineSeal> vent_chlorine_seal = block("vent_chlorine_seal", BlockChlorineSeal::new,
            p -> p.strength(5.0F, 6.0F).requiresCorrectToolForDrops(), GeoNtmTab.MACHINES) // 1.7: default stone sound
            .transform(Presets.cubeBottomTop("geontm:blocks/vent_chlorine_seal_side", "geontm:blocks/vent_chlorine_seal_side", "geontm:blocks/vent_chlorine_seal_top"))
            .loot(Loot.self())
            .tag(BlockTag.PICKAXE);

    // Lookups

    public static BlockDeadPlant plant_dead(BlockDeadPlant.Type type) { return DEAD_PLANTS.get(type).get(); }
    public static BlockNTMFlower plant_flower(BlockNTMFlower.Type type) { return FLOWERS.get(type).get(); }
    public static BlockTallPlant plant_tall(BlockTallPlant.Type type) { return TALL_PLANTS.get(type).get(); }
    public static BlockBiomeStone stone_biome(BlockBiomeStone.Type type) { return BIOME_STONE.get(type).get(); }
    public static BlockOreBasalt ore_basalt(BlockOreBasalt.Type type) { return BASALT_ORES.get(type).get(); }
    public static Block ore_meteor(String variant) { return METEOR_ORES.get(variant).get(); }
    public static BlockBeaconable block_coke(String variant) { return COKE_BLOCKS.get(variant).get(); }
    public static BlockSellafield sellafield(int level) { return SELLAFIELD[level].get(); }
    public static BlockStalagmite spike(String id) { return SPIKES.get(id).get(); }
    public static BlockVolcano volcanoCore(String id) { return VOLCANO_CORES.get(id).get(); }
    public static Iterable<RegistryObject<BlockVolcano>> volcanoCores() { return VOLCANO_CORES.values(); }
    public static Iterable<RegistryObject<BlockSellafield>> sellafieldLevels() { return Arrays.asList(SELLAFIELD); }

    // Helpers

    private static <B extends Block> Reg.Handle<B> block(String id, Function<BlockBehaviour.Properties, ? extends B> factory,
                                                         UnaryOperator<BlockBehaviour.Properties> props, GeoNtmTab tab) {
        Reg.Handle<B> block = REG.block(id, factory, props);
        Reg.Handle<BlockItem> item = REG.blockItem(block, Compat::blockDescription);
        BLOCK_ITEMS.put(id, item);
        if (tab != null) item.tab(tab);
        return block;
    }

    /** 1.7 plants: {@code Material.plants}, hardness 0, grass sound, no collision. */
    private static BlockBehaviour.Properties plantProps(BlockBehaviour.Properties p) {
        return p.strength(0.0F).sound(SoundType.GRASS).noOcclusion().noCollission();
    }

    private static BlockBehaviour.Properties stoneOre(BlockBehaviour.Properties p) {
        return p.strength(5.0F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops();
    }

    /** Old {@code BlockGasBase}: hardness 0, no collision, no occlusion, replaceable, random-ticked (as ModOreBlocks). */
    private static BlockBehaviour.Properties gasProps(BlockBehaviour.Properties p) {
        return p.strength(0.0F, 0.0F).noOcclusion().noCollission().replaceable().randomTicks().noLootTable();
    }

    private static void spike(String variant, Supplier<ItemStack> drop) {
        for (String base : new String[]{"stalagmite", "stalactite"}) {
            String id = base + "_" + variant.toLowerCase(Locale.ROOT);
            Direction anchor = base.equals("stalagmite") ? Direction.DOWN : Direction.UP;
            SPIKES.put(id, block(id, p -> new BlockStalagmite(p, anchor, drop),
                    p -> p.requiresCorrectToolForDrops().strength(0.5F, 2.0F).sound(SoundType.STONE).noOcclusion().noCollission(), GeoNtmTab.BLOCKS)
                    .transform(plant("geontm:blocks/" + base + "." + variant.toLowerCase(Locale.ROOT)))
                    .loot(Loot.nothing()).tag(BlockTag.PICKAXE)); // BlockStalagmite computes its own drops
        }
    }

    private static RegistryObject<BlockNuclearWaste> waste(String id) {
        return block(id, BlockNuclearWaste::new,
                p -> p.strength(5.0F, 6.0F).requiresCorrectToolForDrops().randomTicks(), GeoNtmTab.BLOCKS) // 1.7: default stone sound
                .transform(cube("geontm:blocks/" + id));
    }

    private static RegistryObject<BlockVent> vent(String id, BlockVent.Kind kind) {
        return block(id, p -> new BlockVent(p, kind),
                p -> p.strength(5.0F, 6.0F).requiresCorrectToolForDrops(), GeoNtmTab.MACHINES) // 1.7: default stone sound
                .transform(Presets.column("geontm:blocks/vent_blank", "geontm:blocks/" + id))
                .loot(Loot.nothing())
                .tag(BlockTag.PICKAXE);
    }

    public static void init() {}
}
