package fr.geoventure.geontm.blocks;

import fr.geoventure.geocore.registry.BlockTag;
import fr.geoventure.geocore.registry.Loot;
import fr.geoventure.geocore.registry.Preset;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.dejson.Models;
import fr.geoventure.geocore.dejson.Slots;
import fr.geoventure.geocore.dejson.Variants;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.world.item.Item;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geontm.items.special.BedrockOreType;
import fr.geoventure.geontm.items.special.ItemBedrockOreGraded;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import fr.geoventure.geocore.world.Compat;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.SoundType;

import java.util.Map;
import fr.geoventure.geontm.blocks.machine.MachineAnnihilator;
import fr.geoventure.geontm.blocks.machine.MachineBoiler;
import fr.geoventure.geontm.blocks.machine.MachineDiFurnaceRTG;
import fr.geoventure.geontm.blocks.machine.MachineExcavator;
import fr.geoventure.geontm.blocks.machine.MachineOreSlopper;
import fr.geoventure.geontm.blocks.machine.MachineRtgFurnace;

/**
 * Feature-local block and item registry for the production machines (group B), with each block's model,
 * blockstate, loot and mining tag stated on its registration (Reg; emitted by Dejson at build time). The three
 * OBJ-rendered multiblocks get the shared empty blockstate placeholder and a typed {@code geontm:machine_obj} item
 * definition ({@code ObjItemDeclarations}); the three cubes (old boiler, RTG furnace, nuclear blast furnace) are
 * fully declared from the 1.7 icon tables.
 */
public final class ModProduction2Blocks {
    private ModProduction2Blocks() {}

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCKS, ITEMS);

    /** Renderer-drawn folded multiblock: the blockstate exists only because vanilla demands one. */
    private static final Preset FOLDED = Presets.rendered(Variants.product(
                    Variants.uniform("facing", "north", "east", "south", "west"),
                    Variants.uniform("cell", "false", "true")))
            .and(handle -> handle.loot(Loot.self()).tag(BlockTag.PICKAXE));

    /** 1.7: Material.iron, hardness 5, resistance 100, machineTab. */
    public static final RegistryObject<MachineExcavator> MACHINE_EXCAVATOR = REG.block(
            "machine_excavator", MachineExcavator::new,
            p -> p.strength(5.0F, 60.0F).sound(SoundType.METAL).noOcclusion().requiresCorrectToolForDrops())
            .transform(FOLDED);
    /** 1.7: hardness 5, resistance 10, machineTab. */
    public static final RegistryObject<MachineOreSlopper> MACHINE_ORE_SLOPPER = REG.block(
            "machine_ore_slopper", MachineOreSlopper::new,
            p -> p.strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion().requiresCorrectToolForDrops())
            .transform(FOLDED);
    /** 1.7: hardness 5, resistance 10, machineTab. */
    public static final RegistryObject<MachineAnnihilator> MACHINE_ANNIHILATOR = REG.block(
            "machine_annihilator", MachineAnnihilator::new,
            p -> p.strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion().requiresCorrectToolForDrops())
            .transform(FOLDED);
    /** 1.7: the dead "Old Boiler" decoration block, no tab, hardness 5, resistance 10. */
    public static final RegistryObject<MachineBoiler> MACHINE_BOILER_OFF = REG.block(
            "machine_boiler_off", MachineBoiler::new,
            p -> p.strength(5.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops())
            // 1.7 MachineBoiler icons: top/bottom = machine_boiler_base, front = machine_boiler_front, else machine_boiler_side.
            .transform(Presets.orientable("geontm:blocks/machine_boiler_front", "geontm:blocks/machine_boiler_side",
                    "geontm:blocks/machine_boiler_base").states(Variants.facing()))
            // 1.7 MachineBoiler.getDrops: 3-6 steel scraps, computed in MachineBoiler.getDrops (the table is unused).
            .loot(Loot.nothing())
            .tag(BlockTag.PICKAXE);
    /** 1.7: the deprecated RTG furnace; the {@code _on} block becomes {@code LIT} (light level 1.0F = 15 when lit). */
    public static final RegistryObject<MachineRtgFurnace> MACHINE_RTG_FURNACE = REG.block(
            "machine_rtg_furnace_off", MachineRtgFurnace::new,
            p -> p.strength(5.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(MachineRtgFurnace.LIT) ? 15 : 0))
            // 1.7 MachineRtgFurnace icons: top/bottom = machine_rtg_furnace_base_alt, front = machine_rtg_furnace_off_alt / _on_alt, side = machine_rtg_furnace_side_alt.
            .transform(Presets.shape(Models.group()
                            .and("", Models.orientable("geontm:blocks/machine_rtg_furnace_off_alt", "geontm:blocks/machine_rtg_furnace_side_alt", "geontm:blocks/machine_rtg_furnace_base_alt"))
                            .and("_on", Models.orientable("geontm:blocks/machine_rtg_furnace_on_alt", "geontm:blocks/machine_rtg_furnace_side_alt", "geontm:blocks/machine_rtg_furnace_base_alt")))
                    .states(Variants.product(Variants.facing(), Variants.flag("lit", "_on"))))
            // 1.7 MachineRtgFurnace.getItemDropped returns null: the deprecated RTG furnace drops nothing.
            .loot(Loot.nothing())
            .tag(BlockTag.PICKAXE);
    /** 1.7: the deprecated nuclear blast furnace, machineTab; {@code _on} is {@code LIT}. */
    public static final RegistryObject<MachineDiFurnaceRTG> MACHINE_DIFURNACE_RTG = REG.block(
            "machine_difurnace_rtg_off", MachineDiFurnaceRTG::new,
            p -> p.strength(5.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(MachineDiFurnaceRTG.LIT) ? 15 : 0))
            // 1.7 MachineDiFurnaceRTG icons: top = rtg_difurnace_top_off/_on, front = rtg_difurnace_front_off/_on, side and bottom = machine_rtg_furnace_side_alt.
            .transform(Presets.shape(Models.group()
                            .and("", Models.parented("minecraft:block/orientable_with_bottom", Slots.of(
                                    "front", "geontm:blocks/rtg_difurnace_front_off", "side", "geontm:blocks/machine_rtg_furnace_side_alt",
                                    "top", "geontm:blocks/rtg_difurnace_top_off").and("bottom", "geontm:blocks/machine_rtg_furnace_side_alt")))
                            .and("_on", Models.parented("minecraft:block/orientable_with_bottom", Slots.of(
                                    "front", "geontm:blocks/rtg_difurnace_front_on", "side", "geontm:blocks/machine_rtg_furnace_side_alt",
                                    "top", "geontm:blocks/rtg_difurnace_top_on").and("bottom", "geontm:blocks/machine_rtg_furnace_side_alt"))))
                    .states(Variants.product(Variants.facing(), Variants.flag("lit", "_on"))))
            .loot(Loot.self())
            .tag(BlockTag.PICKAXE);

    public static final RegistryObject<BlockItem> MACHINE_EXCAVATOR_ITEM = REG.blockItem(
            MACHINE_EXCAVATOR, Compat::blockDescription)
            .tab(GeoNtmTab.MACHINES);
    public static final RegistryObject<BlockItem> MACHINE_ORE_SLOPPER_ITEM = REG.blockItem(
            MACHINE_ORE_SLOPPER, Compat::blockDescription)
            .tab(GeoNtmTab.MACHINES);
    public static final RegistryObject<BlockItem> MACHINE_ANNIHILATOR_ITEM = REG.blockItem(
            MACHINE_ANNIHILATOR, Compat::blockDescription)
            .tab(GeoNtmTab.MACHINES);
    public static final RegistryObject<BlockItem> MACHINE_BOILER_OFF_ITEM = REG.blockItem(
            MACHINE_BOILER_OFF, BlockItem::new, Compat::blockDescription);
    public static final RegistryObject<BlockItem> MACHINE_RTG_FURNACE_ITEM = REG.blockItem(
            MACHINE_RTG_FURNACE, BlockItem::new, Compat::blockDescription);
    public static final RegistryObject<BlockItem> MACHINE_DIFURNACE_RTG_ITEM = REG.blockItem(
            MACHINE_DIFURNACE_RTG, BlockItem::new, Compat::blockDescription)
            .tab(GeoNtmTab.MACHINES);

    /**
     * The BASE grade of the 1.7 {@code bedrock_ore} metadata family, flattened to
     * {@code bedrock_ore_base_<type>}: what the ore slopper produces. The other 25 processing grades
     * belong to the (unported) bedrock refining chain and are not registered here.
     */
    public static final Map<BedrockOreType, RegistryObject<ItemBedrockOreGraded>> BEDROCK_ORE_BASE =
            new ConcurrentHashMap<>(); // an addon type's BASE item joins via ModBedrockOreItems.add

    static {
        for (BedrockOreType type : BedrockOreType.VALUES) {
            // the BASE-grade graded bedrock ores the slopper produces (1.7 bedrock_ore_base_<type>.png)
            BEDROCK_ORE_BASE.put(type, REG.item("bedrock_ore_base_" + type.suffix,
                    (Item.Properties p) -> new ItemBedrockOreGraded(p, ItemBedrockOreGraded.Grade.BASE, type))
                    .transform(Presets.sprite()));
        }

        // 1.7: machine_boiler_off has setCreativeTab(null) and machine_rtg_furnace_off never sets a tab.
        // The bedrock ores join their tab after the machines (as 1.7 listed them), so they are added here.
        for (BedrockOreType type : BedrockOreType.VALUES) ModTabs.addTo(GeoNtmTab.MATERIALS, BEDROCK_ORE_BASE.get(type)); // 1.7 partsTab
    }

    public static ItemBedrockOreGraded bedrockOreBase(BedrockOreType type) {
        return BEDROCK_ORE_BASE.get(type).get();
    }

    public static void init() {}
}
