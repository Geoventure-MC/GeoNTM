package fr.geoventure.geontm.blocks;

import fr.geoventure.geocore.registry.BlockTag;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.blocks.generic.BlockGasRadon;
import fr.geoventure.geontm.blocks.generic.BlockGasMeltdown;
import fr.geoventure.geontm.blocks.generic.BlockGasAsbestos;
import fr.geoventure.geontm.blocks.generic.BlockGasMonoxide;
import fr.geoventure.geontm.blocks.generic.BlockGasRadonTomb;
import fr.geoventure.geontm.blocks.generic.BlockOutgas;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geocore.registry.Loot;
import fr.geoventure.geocore.registry.Preset;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geocore.world.Compat;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.dejson.Variants;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import java.util.List;
import java.util.Map;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Non-generated nuclear resources and gas blocks. */
public final class ModNuclearBlocks {
    private ModNuclearBlocks() {}
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCKS, ITEMS);
    /** A gas: an authored single-state model (no item). */
    private static final Preset GAS = Presets.statesOnly(Variants.simple());

    public static final RegistryObject<BlockOutgas> ore_uranium = ore("ore_uranium", 5F, 20)
            .tag(BlockTag.PICKAXE)
            .tag(BlockTag.NEEDS_IRON_TOOL);
    public static final RegistryObject<BlockOutgas> ore_uranium_scorched = ore("ore_uranium_scorched", 5F, 15)
            .tag(BlockTag.PICKAXE)
            .tag(BlockTag.NEEDS_IRON_TOOL);
    public static final RegistryObject<BlockOutgas> ore_gneiss_uranium = ore("ore_gneiss_uranium", 1.5F, 20)
            .tag(BlockTag.PICKAXE)
            .tag(BlockTag.NEEDS_IRON_TOOL);
    public static final RegistryObject<BlockOutgas> ore_gneiss_uranium_scorched = ore("ore_gneiss_uranium_scorched", 1.5F, 20)
            .tag(BlockTag.PICKAXE)
            .tag(BlockTag.NEEDS_IRON_TOOL);
    public static final RegistryObject<BlockOutgas> ore_nether_uranium = ore("ore_nether_uranium", 0.4F, 20)
            .tag(BlockTag.PICKAXE)
            .tag(BlockTag.NEEDS_IRON_TOOL);
    public static final RegistryObject<BlockOutgas> ore_nether_uranium_scorched = ore("ore_nether_uranium_scorched", 0.4F, 20)
            .tag(BlockTag.PICKAXE)
            .tag(BlockTag.NEEDS_IRON_TOOL);
    public static final RegistryObject<BlockGasRadon> gas_radon = REG.block("gas_radon",
            (BlockBehaviour.Properties p) -> new BlockGasRadon(p.noOcclusion().noCollission().replaceable().randomTicks(), 0.05F, false))
            .transform(GAS).loot(Loot.nothing());
    public static final RegistryObject<BlockGasRadon> gas_radon_dense = REG.block("gas_radon_dense",
            (BlockBehaviour.Properties p) -> new BlockGasRadon(p.noOcclusion().noCollission().replaceable().randomTicks(), 0.5F, true))
            .transform(GAS).loot(Loot.nothing());
    public static final RegistryObject<BlockGasMeltdown> gas_meltdown = REG.block("gas_meltdown",
            (BlockBehaviour.Properties p) -> new BlockGasMeltdown(p.noOcclusion().noCollission().replaceable().randomTicks()))
            .transform(GAS);
    public static final RegistryObject<BlockGasRadonTomb> gas_radon_tomb = REG.block("gas_radon_tomb",
            (BlockBehaviour.Properties p) -> new BlockGasRadonTomb(p.noOcclusion().noCollission().replaceable().randomTicks()))
            .transform(GAS);
    public static final RegistryObject<BlockGasMonoxide> gas_monoxide = REG.block("gas_monoxide",
            (BlockBehaviour.Properties p) -> new BlockGasMonoxide(p.noOcclusion().noCollission().replaceable().randomTicks()))
            .transform(GAS);
    public static final RegistryObject<BlockGasAsbestos> gas_asbestos = REG.block("gas_asbestos",
            (BlockBehaviour.Properties p) -> new BlockGasAsbestos(p.noOcclusion().noCollission().replaceable().randomTicks()))
            .transform(GAS);

    /**
     * A cube of its own icon whose item model parents the block model; drops itself. Pickaxe, iron-tool and
     * material tags stay with the ore generator ({@code port-tools/gen_ore_blocks.py}).
     */
    private static Reg.Handle<BlockOutgas> ore(String id, float hardness, int rate) {
        return REG.block(id, p -> new BlockOutgas(p, true, rate, true),
                        p -> p.strength(hardness, 6F).sound(SoundType.STONE).requiresCorrectToolForDrops().randomTicks())
                .transform(Presets.cubeAll().viaItemModel())
                .loot(RawOres.drop("geontm:raw_uranium", Loot.self()));
    }

    static {
        for (RegistryObject<? extends Block> block : List.of(ore_uranium, ore_uranium_scorched,
                ore_gneiss_uranium, ore_gneiss_uranium_scorched, ore_nether_uranium, ore_nether_uranium_scorched)) {
            REG.blockItem(block).tab(GeoNtmTab.RESOURCES);
        }
        // 1.7 registers every gas with a plain item block in the machine tab, drawn with the gas texture.
        List<Map.Entry<String, RegistryObject<? extends Block>>> gases = List.of(Map.entry("gas_radon", gas_radon),
                Map.entry("gas_radon_dense", gas_radon_dense), Map.entry("gas_meltdown", gas_meltdown),
                Map.entry("gas_radon_tomb", gas_radon_tomb), Map.entry("gas_monoxide", gas_monoxide),
                Map.entry("gas_asbestos", gas_asbestos));
        for (Map.Entry<String, RegistryObject<? extends Block>> gas : gases) {
            REG.blockItem(gas.getValue(), Compat::blockDescription).transform(Presets.sprite("geontm:blocks/" + gas.getKey()))
                    .tab(GeoNtmTab.MACHINES);
        }
    }
    public static void init() {}
}
