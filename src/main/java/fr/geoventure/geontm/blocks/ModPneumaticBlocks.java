package fr.geoventure.geontm.blocks;

import fr.geoventure.geocore.registry.BlockTag;
import fr.geoventure.geocore.registry.Loot;
import fr.geoventure.geocore.registry.Preset;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.dejson.Models;
import fr.geoventure.geocore.dejson.Slots;
import fr.geoventure.geocore.dejson.States;
import fr.geoventure.geocore.dejson.Variant;
import fr.geoventure.geocore.dejson.Variants;
import fr.geoventure.geocore.dejson.When;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.world.Compat;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import fr.geoventure.geontm.blocks.network.PneumoStorageAccess;
import fr.geoventure.geontm.blocks.network.PneumoStorageClutter;
import fr.geoventure.geontm.blocks.network.PneumoStorageExporter;
import fr.geoventure.geontm.blocks.network.PneumoStorageImporter;
import fr.geoventure.geontm.blocks.network.PneumoStorageMono;
import fr.geoventure.geontm.blocks.network.PneumoPipeAnchor;
import fr.geoventure.geontm.blocks.network.PneumoTube;
import fr.geoventure.geontm.blocks.network.PneumoTubePaintableBlock;
import fr.geoventure.geontm.items.block.DescBlockItem;

/**
 * Feature-local block and block-item registry for the pneumatic tube storage system. Strengths and tabs are
 * 1.7 {@code ModBlocks}: the tube 2.0/10, everything else 5.0/10, all on the machine tab. Models, blockstates, loot
 * and mining tags are stated on each registration (Reg; emitted by Dejson at build time).
 *
 * <p>The two tubes are renderer-drawn (their blockstates are one-part multipart placeholders and the plain
 * tube's item is an authored element model under {@code src/main/resources}); the storage blocks are plain
 * cubes over the 1.7 sprites, the access terminal an orientable cube with its front sprite on the facing side.
 */
public final class ModPneumaticBlocks {
    private ModPneumaticBlocks() {}

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCKS, ITEMS);

    /** Renderer-drawn tube: a one-part multipart blockstate on the shared empty placeholder; drops itself. */
    private static final Preset DRAWN_TUBE = handle -> {
        States.multipart(handle.namespace(), handle.path()).part(new Variant("geontm:block/machine_dummy_empty")).register();
        handle.loot(Loot.self()).tag(BlockTag.PICKAXE);
    };
    /** A plain storage cube over its 1.7 sprite. */
    private static final Preset STORAGE_CUBE = Presets.cubeAll().and(handle -> handle.tag(BlockTag.PICKAXE));

    public static final RegistryObject<PneumoTube> PNEUMATIC_TUBE = REG.block(
            "pneumatic_tube", PneumoTube::new,
            p -> p.strength(2.0F, 6.0F).sound(ModSoundTypes.PIPE).noOcclusion().requiresCorrectToolForDrops())
            .transform(DRAWN_TUBE);
    public static final RegistryObject<PneumoTubePaintableBlock> PNEUMATIC_TUBE_PAINTABLE = REG.block(
            "pneumatic_tube_paintable", PneumoTubePaintableBlock::new,
            p -> p.strength(5.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops())
            .transform(Presets.shape(Models.renderType("minecraft:cutout",
                    Models.cubeAll("geontm:blocks/pneumatic_tube_paintable"))).noItem())
            .loot(Loot.self()).tag(BlockTag.PICKAXE);
    public static final RegistryObject<PneumoStorageAccess> PNEUMATIC_STORAGE_ACCESS = REG.block(
            "pneumatic_storage_access", PneumoStorageAccess::new,
            p -> p.strength(5.0F, 6.0F).sound(ModSoundTypes.PIPE).requiresCorrectToolForDrops())
            .transform(Presets.orientable("geontm:blocks/pneumatic_storage_access_front",
                    "geontm:blocks/pneumatic_storage_access", "geontm:blocks/pneumatic_storage_access").states(Variants.facingAll()))
            .loot(Loot.self())
            .tag(BlockTag.PICKAXE);
    public static final RegistryObject<PneumoStorageClutter> PNEUMATIC_STORAGE_CLUTTER = REG.block(
            "pneumatic_storage_clutter", PneumoStorageClutter::new,
            p -> p.strength(5.0F, 6.0F).sound(ModSoundTypes.PIPE).requiresCorrectToolForDrops())
            .transform(STORAGE_CUBE)
            .loot(Loot.container("minecraft:custom_name", "minecraft:container"));
    public static final RegistryObject<PneumoStorageMono> PNEUMATIC_STORAGE_MONO = REG.block(
            "pneumatic_storage_mono", PneumoStorageMono::new,
            p -> p.strength(5.0F, 6.0F).sound(ModSoundTypes.PIPE).requiresCorrectToolForDrops())
            .transform(STORAGE_CUBE)
            .loot(Loot.container("minecraft:container", "minecraft:custom_data", "minecraft:custom_name"));
    public static final RegistryObject<PneumoStorageImporter> PNEUMATIC_STORAGE_IMPORTER = REG.block(
            "pneumatic_storage_importer", PneumoStorageImporter::new,
            p -> p.strength(5.0F, 6.0F).sound(ModSoundTypes.PIPE).requiresCorrectToolForDrops())
            .transform(STORAGE_CUBE)
            .loot(Loot.self());
    public static final RegistryObject<PneumoStorageExporter> PNEUMATIC_STORAGE_EXPORTER = REG.block(
            "pneumatic_storage_exporter", PneumoStorageExporter::new,
            p -> p.strength(5.0F, 6.0F).sound(ModSoundTypes.PIPE).requiresCorrectToolForDrops())
            .transform(STORAGE_CUBE)
            .loot(Loot.self());

    public static final RegistryObject<PneumoPipeAnchor> PIPE_ANCHOR_PNEUMATIC = REG.block(
            "pipe_anchor_pneumatic", PneumoPipeAnchor::new,
            p -> p.strength(5.0F, 6.0F).sound(ModSoundTypes.PIPE).noOcclusion().requiresCorrectToolForDrops())
            .andThen(handle -> {
                States.MultipartFile file = States.multipart(handle.namespace(), handle.path());
                for (String facing : new String[]{"down", "up", "north", "south", "west", "east"}) {
                    file.part(When.of("facing", facing), new Variant("geontm:block/pipe_anchor_" + facing));
                }
                file.register();
            })
            .loot(Loot.self())
            .tag(BlockTag.PICKAXE);

    static {
        // hand-built element geometry: the model is authored (assets/geontm/models/item/pneumatic_tube.json)
        descItem("pneumatic_tube", PNEUMATIC_TUBE).transform(Presets.shares("geontm:item/pneumatic_tube"));
        descItem("pneumatic_tube_paintable", PNEUMATIC_TUBE_PAINTABLE).icon(d -> d.models(
                Models.parented("minecraft:block/cube_all", Slots.of("all", "geontm:blocks/pneumatic_tube_paintable"))));
        tabItem("pneumatic_storage_access", PNEUMATIC_STORAGE_ACCESS);
        tabItem("pneumatic_storage_clutter", PNEUMATIC_STORAGE_CLUTTER);
        tabItem("pneumatic_storage_mono", PNEUMATIC_STORAGE_MONO);
        tabItem("pneumatic_storage_importer", PNEUMATIC_STORAGE_IMPORTER);
        tabItem("pneumatic_storage_exporter", PNEUMATIC_STORAGE_EXPORTER);
        tabItem("pipe_anchor_pneumatic", PIPE_ANCHOR_PNEUMATIC);
    }

    /** 1.7 ITooltipProvider blocks: the item shows {@code addStandardInfo}. */
    private static Reg.Handle<DescBlockItem> descItem(String name, RegistryObject<? extends Block> block) {
        return REG.item(name, p -> new DescBlockItem(block.get(), p), Compat::blockDescription)
                .tab(GeoNtmTab.MACHINES);
    }

    private static void tabItem(String name, RegistryObject<? extends Block> block) {
        REG.blockItem(block).tab(GeoNtmTab.MACHINES);
    }

    public static void init() {}
}
