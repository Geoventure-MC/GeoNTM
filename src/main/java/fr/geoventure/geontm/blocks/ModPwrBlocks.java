package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geocore.registry.BlockTag;
import fr.geoventure.geocore.registry.Loot;
import fr.geoventure.geocore.registry.Preset;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.dejson.Models;
import fr.geoventure.geocore.dejson.Variants;
import fr.geoventure.geontm.items.block.DescBlockItem;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import fr.geoventure.geocore.world.Compat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import fr.geoventure.geontm.blocks.machine.BlockPWR;
import fr.geoventure.geontm.blocks.machine.MachinePWRController;

/**
 * Feature-local blocks of the PWR. 1.7 {@code ModBlocks}: every part is iron, 5.0/10, on the machine tab and
 * carries {@code ITooltipProvider}'s description lines; the assembled shell {@code pwr_block} is 15.0/10 with no
 * tab and no item.
 */
public final class ModPwrBlocks {
    private ModPwrBlocks() {}

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCKS, ITEMS);

    /** 1.7 {@code BlockPillarPWR}: the top texture on both ends, no axis. */
    private static Preset pillar(String name) {
        return Presets.column("geontm:blocks/" + name + "_top", "geontm:blocks/" + name + "_side").and(PART);
    }
    /** Every part drops itself and is pickaxe-mined; its item shows the block model. */
    private static final Preset PART = handle -> handle.loot(Loot.self()).tag(BlockTag.PICKAXE);

    public static final RegistryObject<Block> PWR_FUEL = part("pwr_fuel").transform(pillar("pwr_fuel"));
    public static final RegistryObject<Block> PWR_CONTROL = part("pwr_control").transform(pillar("pwr_control"));
    public static final RegistryObject<Block> PWR_CHANNEL = part("pwr_channel").transform(pillar("pwr_channel"));
    public static final RegistryObject<Block> PWR_HEATEX = part("pwr_heatex").transform(Presets.cubeAll().and(PART));
    public static final RegistryObject<Block> PWR_HEATSINK = part("pwr_heatsink").transform(Presets.cubeAll().and(PART));
    public static final RegistryObject<Block> PWR_NEUTRON_SOURCE = part("pwr_neutron_source").transform(Presets.cubeAll().and(PART));
    public static final RegistryObject<Block> PWR_REFLECTOR = part("pwr_reflector").transform(Presets.cubeAll().and(PART));
    public static final RegistryObject<Block> PWR_CASING = part("pwr_casing").transform(Presets.cubeAll().and(PART));
    public static final RegistryObject<Block> PWR_PORT = part("pwr_port").transform(Presets.cubeAll().and(PART));
    /** An orientable cube: its front on the facing side over the blank casing. */
    public static final RegistryObject<MachinePWRController> PWR_CONTROLLER = REG.block(
            "pwr_controller", MachinePWRController::new,
            p -> p.strength(5.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops())
            .transform(Presets.orientable("geontm:blocks/pwr_controller", "geontm:blocks/pwr_casing_blank",
                    "geontm:blocks/pwr_casing_blank").states(Variants.facing()))
            .transform(PART);
    /** The assembled shell picks {@code pwr_block} or {@code pwr_casing_port} by its {@code port} flag; no item, no drop. */
    public static final RegistryObject<BlockPWR> PWR_BLOCK = REG.block(
            "pwr_block", BlockPWR::new,
            p -> p.strength(15.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops())
            .transform(Presets.shape(Models.group()
                            .and("", Models.cubeAll("geontm:blocks/pwr_block"))
                            .and("_port", Models.cubeAll("geontm:blocks/pwr_casing_port")))
                    .states(Variants.flag("port", "_port")).noItem())
            .loot(Loot.nothing())
            .tag(BlockTag.PICKAXE);

    static {
        descItem(PWR_FUEL);
        descItem(PWR_CONTROL);
        descItem(PWR_CHANNEL);
        descItem(PWR_HEATEX);
        descItem(PWR_HEATSINK);
        descItem(PWR_NEUTRON_SOURCE);
        descItem(PWR_REFLECTOR);
        descItem(PWR_CASING);
        descItem(PWR_PORT);
        descItem(PWR_CONTROLLER);
    }

    private static Reg.Handle<Block> part(String name) {
        return REG.simpleBlock(name,
                (BlockBehaviour.Properties p) -> p.strength(5.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops());
    }

    /** 1.7 ITooltipProvider blocks: the item shows {@code addStandardInfo}. */
    private static <B extends Block> void descItem(RegistryObject<B> block) {
        REG.<B, DescBlockItem>blockItem(block, DescBlockItem::new, Compat::blockDescription)
                .tab(GeoNtmTab.MACHINES);
    }

    public static void init() {}
}
