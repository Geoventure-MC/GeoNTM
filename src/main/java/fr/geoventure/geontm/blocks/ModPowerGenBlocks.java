package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geocore.registry.BlockTag;
import fr.geoventure.geocore.registry.Loot;
import fr.geoventure.geocore.registry.Preset;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.dejson.Variants;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import fr.geoventure.geocore.world.Compat;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.SoundType;
import fr.geoventure.geontm.blocks.machine.MachineChungus;
import fr.geoventure.geontm.blocks.machine.MachineHephaestus;
import fr.geoventure.geontm.blocks.machine.MachineIGenerator;
import fr.geoventure.geontm.blocks.machine.MachineLPW2;
import fr.geoventure.geontm.blocks.machine.MachineSiren;

/**
 * Feature-local block and block-item registry for the large power-generation machines, with each block's
 * model, blockstate, loot and mining tag stated on its registration (Reg; emitted by Dejson at build time).
 *
 * <p>The four OBJ-rendered machines get the shared empty blockstate placeholder and an authored/typed
 * {@code geontm:machine_obj} item definition ({@code ObjItemDeclarations}); the siren is a plain cube.
 */
public final class ModPowerGenBlocks {
    private ModPowerGenBlocks() {}

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCKS, ITEMS);

    /**
     * Renderer-drawn folded machines: the blockstate exists only because vanilla demands one, every state
     * pointing at the shared empty placeholder; they drop themselves and are pickaxe-mined.
     */
    private static final Preset FOLDED_MACHINE = Presets.rendered(Variants.product(
                    Variants.uniform("facing", "north", "east", "south", "west"),
                    Variants.uniform("cell", "false", "true")))
            .and(handle -> handle.loot(Loot.self()).tag(BlockTag.PICKAXE));

    /** 1.7: {@code Material.iron}, hardness 5, resistance 10, machineTab, {@code block_steel} icon. */
    public static final RegistryObject<MachineChungus> MACHINE_CHUNGUS = REG.block(
            "machine_chungus", MachineChungus::new,
            p -> p.strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion().requiresCorrectToolForDrops())
            .transform(FOLDED_MACHINE);
    /** 1.7: hardness 5, resistance 100. */
    public static final RegistryObject<MachineLPW2> MACHINE_LPW2 = REG.block(
            "machine_lpw2", MachineLPW2::new,
            p -> p.strength(5.0F, 60.0F).sound(SoundType.METAL).noOcclusion().requiresCorrectToolForDrops())
            .transform(FOLDED_MACHINE);
    /** 1.7: hardness 10, resistance 10, {@code block_steel_machine} icon. */
    public static final RegistryObject<MachineHephaestus> MACHINE_HEPHAESTUS = REG.block(
            "machine_hephaestus", MachineHephaestus::new,
            p -> p.strength(10.0F, 6.0F).sound(SoundType.METAL).noOcclusion().requiresCorrectToolForDrops())
            .transform(FOLDED_MACHINE);
    /** 1.7: hardness 5, resistance 10, vanilla {@code gold_block} icon (never seen: the block is renderer-drawn). */
    public static final RegistryObject<MachineIGenerator> MACHINE_INDUSTRIAL_GENERATOR = REG.block(
            "machine_industrial_generator", MachineIGenerator::new,
            p -> p.strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion().requiresCorrectToolForDrops())
            .transform(FOLDED_MACHINE);
    /** 1.7: {@code Material.iron}, hardness 5, resistance 10; icons block_steel on top and bottom, machine_siren on the sides. */
    public static final RegistryObject<MachineSiren> MACHINE_SIREN = REG.block(
            "machine_siren", MachineSiren::new,
            p -> p.strength(5.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops())
            .transform(Presets.cubeBottomTop("geontm:blocks/block_steel", "geontm:blocks/machine_siren",
                    "geontm:blocks/block_steel"))
            .loot(Loot.self())
            .tag(BlockTag.PICKAXE);

    public static final RegistryObject<BlockItem> MACHINE_CHUNGUS_ITEM = REG.blockItem(
            MACHINE_CHUNGUS, Compat::blockDescription)
            .tab(GeoNtmTab.MACHINES);
    /** 1.7 ModBlocks: the LPW-2 has no item renderer; its item is the flat block_steel sprite. */
    public static final RegistryObject<BlockItem> MACHINE_LPW2_ITEM = REG.blockItem(
            MACHINE_LPW2, BlockItem::new, Compat::blockDescription)
            .transform(Presets.sprite("geontm:blocks/block_steel"))
            .tab(GeoNtmTab.MACHINES);
    public static final RegistryObject<BlockItem> MACHINE_HEPHAESTUS_ITEM = REG.blockItem(
            MACHINE_HEPHAESTUS, Compat::blockDescription)
            .tab(GeoNtmTab.MACHINES);
    public static final RegistryObject<BlockItem> MACHINE_INDUSTRIAL_GENERATOR_ITEM = REG.blockItem(
            MACHINE_INDUSTRIAL_GENERATOR, Compat::blockDescription)
            .tab(GeoNtmTab.MACHINES);
    public static final RegistryObject<BlockItem> MACHINE_SIREN_ITEM = REG.blockItem(
            MACHINE_SIREN, BlockItem::new, Compat::blockDescription)
            .tab(GeoNtmTab.MACHINES);

    public static void init() {}
}
