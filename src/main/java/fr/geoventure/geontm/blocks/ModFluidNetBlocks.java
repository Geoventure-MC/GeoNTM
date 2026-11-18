package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.blocks.fluid.BlockAcid;
import fr.geoventure.geontm.blocks.fluid.ModWorldFluids;
import fr.geoventure.geontm.blocks.fluid.BlockCorium;
import fr.geoventure.geontm.blocks.machine.BlockFluidBarrel;
import fr.geoventure.geontm.blocks.fluid.BlockRadLava;
import fr.geoventure.geontm.blocks.fluid.BlockSulfuricAcid;
import fr.geoventure.geontm.blocks.fluid.BlockToxic;
import fr.geoventure.geontm.blocks.fluid.BlockVolcanicLava;
import fr.geoventure.geontm.blocks.network.FluidCounterValve;
import fr.geoventure.geontm.blocks.network.FluidDuctBox;
import fr.geoventure.geontm.blocks.network.FluidDuctBoxExhaust;
import fr.geoventure.geontm.blocks.network.FluidDuctGauge;
import fr.geoventure.geontm.blocks.network.FluidDuctNeo;
import fr.geoventure.geontm.blocks.network.FluidDuctPaintable;
import fr.geoventure.geontm.blocks.network.FluidDuctPaintableBlockExhaust;
import fr.geoventure.geontm.blocks.network.FluidPipeAnchor;
import fr.geoventure.geontm.blocks.network.ExhaustPipeAnchor;
import fr.geoventure.geontm.blocks.network.FlowControlPump;
import fr.geoventure.geontm.blocks.network.FluidPump;
import fr.geoventure.geontm.blocks.network.FluidSwitch;
import fr.geoventure.geontm.blocks.machine.FoundryTank;
import fr.geoventure.geontm.blocks.generic.RedBarrel;
import fr.geoventure.geontm.blocks.generic.YellowBarrel;
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
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import fr.geoventure.geocore.world.Compat;
import java.util.function.Consumer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.UnaryOperator;
import fr.geoventure.geontm.items.block.FluidNetBlockItem;

/**
 * The fluid-network blocks not covered by {@code ModMachineBlocks}: the 1.7 duct family, the storage
 * and effect barrels, the foundry storage basin and the fluid blocks. Ids are the 1.7
 * {@code setBlockName} strings; metadata sub-blocks are flattened ({@code fluid_duct_neo_silver},
 * {@code fluid_duct_box_copper_3}, {@code fluid_duct_exhaust_0}, ...).
 *
 * <p>Each block's blockstate, models, loot and mining tag are stated on its registration (Reg; emitted by
 * Dejson at build time). What Dejson cannot say stays authored under {@code src/main/resources}: the duct
 * and pipe-anchor element models, and the OBJ item definitions ({@code ObjItemDeclarations}).
 */
public final class ModFluidNetBlocks {

    private ModFluidNetBlocks() {}

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCKS, ITEMS);

    /**
     * The box-duct materials, in {@code FluidDuctBox.MATERIALS} order. A copy, because reading that array
     * initialises the block class (and through it the block-state properties), which the build-time Dejson
     * run cannot do.
     */
    private static final String[] BOX_MATERIALS = {"silver", "copper", "white"};
    private static final String PLACEHOLDER = "geontm:block/machine_dummy_empty";

    /** Drops itself, pickaxe-mined. */
    private static final Preset SELF_PICKAXE = handle -> handle.loot(Loot.self()).tag(BlockTag.PICKAXE);
    /** Renderer-drawn duct: a one-part multipart blockstate on the shared empty placeholder. */
    private static final Preset DRAWN_DUCT = multipart(file -> file.part(new Variant(PLACEHOLDER))).and(SELF_PICKAXE);
    /** Effect barrels: renderer-drawn, keyed by "armed". */
    /** Renderer-drawn folded multiblocks: the shared placeholder; the item is an OBJ definition (ObjItemDeclarations). */
    private static final Preset FOLDED = Presets.rendered(PLACEHOLDER, Variants.product(
                    Variants.uniform("facing", "north", "east", "south", "west"),
                    Variants.uniform("cell", "false", "true")))
            .and(SELF_PICKAXE);
    /** The pipe anchors: renderer-drawn, one steel-box model per mounted face for particles and breaking. */
    private static final Preset ANCHOR = multipart(file -> {
        for (String facing : new String[]{"down", "up", "north", "south", "west", "east"}) {
            file.part(When.of("facing", facing), new Variant("geontm:block/pipe_anchor_" + facing));
        }
    }).and(SELF_PICKAXE);
    private static final Preset EFFECT_BARREL_DECL = Presets.rendered(Variants.uniform("armed", "false", "true"))
            .and(SELF_PICKAXE);
    /**
     * The foundry basin's walls: per side, the side or upper wall, with or without the outlet, inner or
     * bottom edge, turned a quarter per side; plus the floor.
     */
    private static final Preset FOUNDRY_TANK_STATES = multipart(file -> {
        String[] sides = {"north", "east", "south", "west"};
        for (int i = 0; i < sides.length; i++) {
            for (String down : new String[]{"false", "true"}) {
                for (String out : new String[]{"false", "true"}) {
                    for (String up : new String[]{"false", "true"}) {
                        String model = "geontm:block/foundry_tank_wall_" + (down.equals("true") ? "upper" : "side")
                                + (out.equals("true") ? "_outlet" : "") + (up.equals("true") ? "_bottom" : "_inner");
                        file.part(When.of(sides[i], "false", "down", down, "out_" + sides[i], out, "up", up),
                                i == 0 ? new Variant(model) : new Variant(model, 0, 90 * i, false));
                    }
                }
            }
        }
        file.part(When.of("down", "false"), new Variant("geontm:block/foundry_tank_floor"));
    });

    /** 1.7 {@code ModSoundTypes.pipe}: ducts, box ducts, exhaust ducts and the anchors. */
    private static final UnaryOperator<BlockBehaviour.Properties> PIPE =
            p -> p.strength(5.0F, 6.0F).sound(ModSoundTypes.PIPE).noOcclusion().requiresCorrectToolForDrops();
    private static final UnaryOperator<BlockBehaviour.Properties> STEEL =
            p -> p.strength(5.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops();
    private static final UnaryOperator<BlockBehaviour.Properties> EFFECT_BARREL =
            p -> p.strength(0.5F, 1.5F).sound(SoundType.METAL).noOcclusion().requiresCorrectToolForDrops(); // 1.7 Material.iron

    // --- ducts ---

    public static final RegistryObject<FluidDuctNeo> fluid_duct_neo = REG.block("fluid_duct_neo", p -> new FluidDuctNeo(p, 0), PIPE)
            .transform(DRAWN_DUCT);
    public static final RegistryObject<FluidDuctNeo> fluid_duct_neo_silver = REG.block("fluid_duct_neo_silver", p -> new FluidDuctNeo(p, 1), PIPE)
            .transform(DRAWN_DUCT);
    public static final RegistryObject<FluidDuctNeo> fluid_duct_neo_colored = REG.block("fluid_duct_neo_colored", p -> new FluidDuctNeo(p, 2), PIPE)
            .transform(DRAWN_DUCT);

    /** {@code fluid_duct_box_<material>_<size>}, indexed [material][size] like the 1.7 metadata. */
    public static final RegistryObject<FluidDuctBox>[][] fluid_duct_box = boxDucts();
    /** {@code fluid_duct_exhaust_<size>}. */
    public static final RegistryObject<FluidDuctBoxExhaust>[] fluid_duct_exhaust = exhaustDucts();

    public static final RegistryObject<FluidDuctPaintableBlockExhaust> fluid_duct_paintable_block_exhaust =
            REG.block("fluid_duct_paintable_block_exhaust", FluidDuctPaintableBlockExhaust::new, STEEL)
            .transform(multipart(file -> file.part(new Variant("geontm:block/fluid_duct_paintable_block_exhaust"))).and(SELF_PICKAXE));
    public static final RegistryObject<FluidPipeAnchor> pipe_anchor = REG.block("pipe_anchor", FluidPipeAnchor::new, PIPE)
            .transform(ANCHOR);
    public static final RegistryObject<ExhaustPipeAnchor> pipe_anchor_exhaust = REG.block("pipe_anchor_exhaust", ExhaustPipeAnchor::new, PIPE)
            .transform(ANCHOR);
    public static final RegistryObject<FluidDuctPaintable> fluid_duct_paintable = REG.block("fluid_duct_paintable", FluidDuctPaintable::new, STEEL)
            .transform(multipart(file -> file.part(new Variant("geontm:block/fluid_duct_paintable"))).and(SELF_PICKAXE));
    public static final RegistryObject<FluidDuctGauge> fluid_duct_gauge = REG.block("fluid_duct_gauge", FluidDuctGauge::new, STEEL)
            .transform(Presets.statesOnly(Variants.facingAll())).transform(SELF_PICKAXE);
    public static final RegistryObject<FluidSwitch> fluid_switch = REG.block("fluid_switch", FluidSwitch::new, STEEL)
            .transform(valve("geontm:blocks/fluid_switch_off", "geontm:blocks/fluid_switch_on"));
    public static final RegistryObject<FluidCounterValve> fluid_counter_valve = REG.block("fluid_counter_valve", FluidCounterValve::new, STEEL)
            .transform(valve("geontm:blocks/fluid_counter_valve_off", "geontm:blocks/fluid_counter_valve_on"));
    public static final RegistryObject<FluidPump> fluid_pump = REG.block("fluid_pump", FluidPump::new,
            p -> p.strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion().requiresCorrectToolForDrops())
            .transform(Presets.rendered(Variants.uniform("facing", "north", "east", "south", "west")).and(SELF_PICKAXE));
    public static final RegistryObject<FlowControlPump> flow_control_pump = REG.block("flow_control_pump", FlowControlPump::new,
            p -> p.strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion().requiresCorrectToolForDrops()
                    .isSuffocating((state, world, pos) -> false).isViewBlocking((state, world, pos) -> false))
            .transform(FOLDED);

    // --- storage barrels ---

    public static final RegistryObject<BlockFluidBarrel> barrel_plastic = REG.block("barrel_plastic",
            p -> new BlockFluidBarrel(p, 12000, "barrel_plastic", false), p -> p.requiresCorrectToolForDrops().strength(2.0F, 3.0F).sound(SoundType.STONE).noOcclusion())
            .transform(Presets.rendered(Variants.simple()))
            .loot(Loot.container("minecraft:custom_data", "minecraft:custom_name")).tag(BlockTag.PICKAXE);
    public static final RegistryObject<BlockFluidBarrel> barrel_corroded = REG.block("barrel_corroded",
            p -> new BlockFluidBarrel(p, 16000, "barrel_corroded", true), p -> p.requiresCorrectToolForDrops().strength(2.0F, 3.0F).sound(SoundType.METAL).noOcclusion())
            .transform(Presets.rendered(Variants.simple()))
            .loot(Loot.container("minecraft:custom_data", "minecraft:custom_name")).tag(BlockTag.PICKAXE);
    public static final RegistryObject<BlockFluidBarrel> barrel_steel = REG.block("barrel_steel",
            p -> new BlockFluidBarrel(p, 16000, "barrel_steel", false), p -> p.requiresCorrectToolForDrops().strength(2.0F, 3.0F).sound(SoundType.METAL).noOcclusion())
            .transform(Presets.rendered(Variants.simple()))
            .loot(Loot.container("minecraft:custom_data", "minecraft:custom_name")).tag(BlockTag.PICKAXE);
    public static final RegistryObject<BlockFluidBarrel> barrel_tcalloy = REG.block("barrel_tcalloy",
            p -> new BlockFluidBarrel(p, 24000, "barrel_tcalloy", false), p -> p.requiresCorrectToolForDrops().strength(2.0F, 3.0F).sound(SoundType.METAL).noOcclusion())
            .transform(Presets.rendered(Variants.simple()))
            .loot(Loot.container("minecraft:custom_data", "minecraft:custom_name")).tag(BlockTag.PICKAXE);
    public static final RegistryObject<BlockFluidBarrel> barrel_antimatter = REG.block("barrel_antimatter",
            p -> new BlockFluidBarrel(p, 16000, "barrel_antimatter", false), p -> p.requiresCorrectToolForDrops().strength(2.0F, 3.0F).sound(SoundType.METAL).noOcclusion())
            .transform(Presets.rendered(Variants.simple()))
            .loot(Loot.container("minecraft:custom_data", "minecraft:custom_name")).tag(BlockTag.PICKAXE);

    // --- effect barrels (nukeTab); kind ids 20+ for EntityPrimedExplosive ---

    public static final RegistryObject<RedBarrel> red_barrel = REG.block("red_barrel", p -> new RedBarrel(p, "barrel_red", 20, RedBarrel.Kind.RED), EFFECT_BARREL)
            .transform(EFFECT_BARREL_DECL);
    public static final RegistryObject<RedBarrel> pink_barrel = REG.block("pink_barrel", p -> new RedBarrel(p, "barrel_pink", 21, RedBarrel.Kind.PINK), EFFECT_BARREL)
            .transform(EFFECT_BARREL_DECL);
    public static final RegistryObject<YellowBarrel> yellow_barrel = REG.block("yellow_barrel", p -> new YellowBarrel(p, "barrel_yellow", 22, false), EFFECT_BARREL)
            .transform(EFFECT_BARREL_DECL);
    public static final RegistryObject<YellowBarrel> vitrified_barrel = REG.block("vitrified_barrel", p -> new YellowBarrel(p, "barrel_vitrified", 23, true), EFFECT_BARREL)
            .transform(EFFECT_BARREL_DECL);
    public static final RegistryObject<RedBarrel> lox_barrel = REG.block("lox_barrel", p -> new RedBarrel(p, "barrel_lox", 24, RedBarrel.Kind.LOX), EFFECT_BARREL)
            .transform(EFFECT_BARREL_DECL);
    public static final RegistryObject<RedBarrel> taint_barrel = REG.block("taint_barrel", p -> new RedBarrel(p, "barrel_taint", 25, RedBarrel.Kind.TAINT), EFFECT_BARREL)
            .transform(EFFECT_BARREL_DECL);

    // --- foundry storage basin ---

    public static final RegistryObject<FoundryTank> foundry_tank = REG.block("foundry_tank", FoundryTank::new,
            p -> p.strength(5.0F, 6.0F).sound(SoundType.STONE).noOcclusion().requiresCorrectToolForDrops())
            .transform(FOUNDRY_TANK_STATES.and(SELF_PICKAXE));

    // --- fluid blocks (1.7 blocks/fluid): GeoCore world fluids, declared in ModWorldFluids ---

    public static final RegistryObject<BlockAcid> acid_block = REG.block("acid_block", BlockAcid::new,
                    p -> ModWorldFluids.properties(p, ModWorldFluids.ACID, true))
            .transform(ModWorldFluids.blockstate("acid"));
    public static final RegistryObject<BlockToxic> toxic_block = REG.block("toxic_block",
                    p -> new BlockToxic(p, ModWorldFluids.TOXIC, true), p -> ModWorldFluids.properties(p, ModWorldFluids.TOXIC, true))
            .transform(ModWorldFluids.blockstate("toxic"));
    public static final RegistryObject<BlockToxic> schrabidic_block = REG.block("schrabidic_block",
                    p -> new BlockToxic(p, ModWorldFluids.SCHRABIDIC, true), p -> ModWorldFluids.properties(p, ModWorldFluids.SCHRABIDIC, true))
            .transform(ModWorldFluids.blockstate("schrabidic_acid"));
    public static final RegistryObject<BlockSulfuricAcid> sulfuric_acid_block = REG.block("sulfuric_acid_block", BlockSulfuricAcid::new,
                    p -> ModWorldFluids.properties(p, ModWorldFluids.SULFURIC_ACID, true))
            .transform(ModWorldFluids.blockstate("sulfuric_acid"));
    // 1.7 CoriumFinite: isReplaceable() false
    public static final RegistryObject<BlockCorium> corium_block = REG.block("corium_block", BlockCorium::new,
                    p -> ModWorldFluids.properties(p, ModWorldFluids.CORIUM, false))
            .transform(ModWorldFluids.blockstate("corium"));
    public static final RegistryObject<BlockVolcanicLava> volcanic_lava_block = REG.block("volcanic_lava_block", BlockVolcanicLava::new,
                    p -> ModWorldFluids.properties(p, ModWorldFluids.VOLCANIC_LAVA, true))
            .transform(ModWorldFluids.blockstate("volcanic_lava"));
    public static final RegistryObject<BlockRadLava> rad_lava_block = REG.block("rad_lava_block", BlockRadLava::new,
                    p -> ModWorldFluids.properties(p, ModWorldFluids.RAD_LAVA, true))
            .transform(ModWorldFluids.blockstate("rad_lava"));

    // --- block items ---

    private static Reg.Handle<FluidNetBlockItem> item(RegistryObject<? extends Block> block,
                                                      GeoNtmTab tab) {
        Reg.Handle<FluidNetBlockItem> item = REG.blockItem(block, FluidNetBlockItem::new, Compat::blockDescription);
        return tab != null ? item.tab(tab) : item;
    }

    /** The redstone valves: the fluid_valve pair of cubes, keyed by "open". */
    private static Preset valve(String off, String on) {
        return Presets.shape(Models.group().and("_off", Models.cubeAll(off)).and("_on", Models.cubeAll(on)))
                .states(Variants.select("open", "false", "_off", "true", "_on"))
                .noItem()
                .and(SELF_PICKAXE);
    }


    /** A multipart blockstate built part by part (the duct shapes the Variants vocabulary cannot say). */
    private static Preset multipart(Consumer<States.MultipartFile> parts) {
        return handle -> {
            States.MultipartFile file = States.multipart(handle.namespace(), handle.path());
            parts.accept(file);
            file.register();
        };
    }

    @SuppressWarnings("unchecked")
    private static RegistryObject<FluidDuctBox>[][] boxDucts() {
        RegistryObject<FluidDuctBox>[][] ducts = new RegistryObject[BOX_MATERIALS.length][5];
        for (int m = 0; m < BOX_MATERIALS.length; m++) {
            for (int s = 0; s < 5; s++) {
                int material = m, size = s;
                ducts[m][s] = REG.block(boxId(m, s), p -> new FluidDuctBox(p, material, size), PIPE)
                        .transform(DRAWN_DUCT);
            }
        }
        return ducts;
    }

    @SuppressWarnings("unchecked")
    private static RegistryObject<FluidDuctBoxExhaust>[] exhaustDucts() {
        RegistryObject<FluidDuctBoxExhaust>[] ducts = new RegistryObject[5];
        for (int s = 0; s < 5; s++) {
            int size = s;
            ducts[s] = REG.block(exhaustId(s), p -> new FluidDuctBoxExhaust(p, size), PIPE)
                    .transform(DRAWN_DUCT);
        }
        return ducts;
    }

    public static String boxId(int material, int size) {
        return "fluid_duct_box_" + BOX_MATERIALS[material] + "_" + size;
    }

    public static String exhaustId(int size) {
        return "fluid_duct_exhaust_" + size;
    }

    static {
        // The items register here, in the old order (the basin sits among the storage barrels).
        item(fluid_duct_neo, GeoNtmTab.MACHINES);
        item(fluid_duct_neo_silver, GeoNtmTab.MACHINES);
        item(fluid_duct_neo_colored, GeoNtmTab.MACHINES);
        for (int m = 0; m < BOX_MATERIALS.length; m++) {
            String straight = "geontm:blocks/boxduct_" + BOX_MATERIALS[m] + "_straight";
            String end = "geontm:blocks/boxduct_" + BOX_MATERIALS[m] + "_end";
            for (int s = 0; s < 5; s++) {
                // Box-duct items: 1.7 RenderBoxDuct.renderInventoryBlock drew the size's straight run along Z
                // (straight on four sides turned along the duct, the end icon on both ends), authored per size.
                String parent = "geontm:block/boxduct_item_" + s;
                item(fluid_duct_box[m][s], GeoNtmTab.MACHINES)
                        .icon(d -> d.models(Models.parented(parent, Slots.of("straight", straight, "end", end))));
            }
        }
        for (int s = 0; s < 5; s++) {
            String parent = "geontm:block/boxduct_item_" + s;
            item(fluid_duct_exhaust[s], GeoNtmTab.MACHINES)
                    .icon(d -> d.models(Models.parented(parent, Slots.of("straight", "geontm:blocks/boxduct_exhaust_straight",
                            "end", "geontm:blocks/boxduct_exhaust_end"))));
        }
        item(fluid_duct_paintable_block_exhaust, GeoNtmTab.MACHINES)
                .transform(Presets.shares("geontm:block/fluid_duct_paintable_block_exhaust"));
        item(pipe_anchor, GeoNtmTab.MACHINES);
        item(pipe_anchor_exhaust, GeoNtmTab.MACHINES);
        item(fluid_duct_paintable, GeoNtmTab.MACHINES).icon(d -> d.models(
                Models.parented("minecraft:block/cube_all", Slots.of("all", "geontm:blocks/fluid_duct_paintable"))));
        item(fluid_duct_gauge, GeoNtmTab.MACHINES).transform(Presets.shares("geontm:block/fluid_duct_gauge"));
        item(fluid_switch, GeoNtmTab.MACHINES).transform(Presets.shares("geontm:block/fluid_switch_off"));
        item(fluid_counter_valve, GeoNtmTab.MACHINES).transform(Presets.shares("geontm:block/fluid_counter_valve_off"));
        item(fluid_pump, null); // deprecated in 1.7: no tab, no recipe
        item(flow_control_pump, GeoNtmTab.MACHINES);
        item(barrel_plastic, GeoNtmTab.MACHINES);
        item(barrel_corroded, null); // 1.7: setCreativeTab(null)
        item(barrel_steel, GeoNtmTab.MACHINES);
        item(barrel_tcalloy, GeoNtmTab.MACHINES);
        item(barrel_antimatter, GeoNtmTab.MACHINES);
        // hand-built element geometry: the model is authored (assets/geontm/models/item/foundry_tank.json)
        item(foundry_tank, GeoNtmTab.MACHINES).transform(Presets.shares("geontm:item/foundry_tank"));
        item(red_barrel, GeoNtmTab.BOMBS);
        item(pink_barrel, GeoNtmTab.BOMBS);
        item(yellow_barrel, GeoNtmTab.BOMBS);
        item(vitrified_barrel, GeoNtmTab.BOMBS);
        item(lox_barrel, GeoNtmTab.BOMBS);
        item(taint_barrel, GeoNtmTab.BOMBS);
        // The fluid blocks have no item (1.7 setCreativeTab(null)): their buckets place them.
    }

    public static void init() {}
}
