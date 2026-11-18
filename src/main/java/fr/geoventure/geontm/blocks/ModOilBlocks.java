package fr.geoventure.geontm.blocks;

import fr.geoventure.geocore.dejson.Shape;
import fr.geoventure.geocore.registry.BlockTag;
import fr.geoventure.geocore.registry.Loot;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.dejson.Models;
import fr.geoventure.geocore.dejson.Slots;
import fr.geoventure.geocore.dejson.Variants;
import fr.geoventure.geocore.world.Compat;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.blocks.generic.BlockLayering;
import fr.geoventure.geontm.blocks.generic.BlockOilDeposit;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

/**
 * The special oil-chain blocks, the ones whose 1.12.2 behavior a generated plain block
 * can't carry. {@code ore_oil} is hand-registered here (excluded from the gen_wave1 ore set via
 * {@code HAND_ORE_IDS}) as {@link BlockOilDeposit}; the generator still emits its blockstate/model/lang
 * and mineable tag. {@code oil_pipe} and {@code oil_spill} are described on their fields.
 */
public final class ModOilBlocks {

    private ModOilBlocks() {}

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCKS, ITEMS);

    /** Live oil deposit, old {@code BlockNTMOre(OreEnum.OIL)}, hardness 5, resistance 10. */
    public static final RegistryObject<BlockOilDeposit> ore_oil = REG.block("ore_oil",
            BlockOilDeposit::new, p -> p.strength(5.0F, 6.0F).requiresCorrectToolForDrops())
            .transform(Presets.cubeAll())
            // 1.7 BlockOre: one oil_tar (crude) on the multiplying fortune curve; canSilkHarvest is false for ore_oil
            .loot(Loot.dropsMultiplied("geontm:oil_tar_crude", 1, 1))
            .tag(BlockTag.PICKAXE);

    /**
     * Drill bore casing, old {@code BlockNoDrop}, Material.IRON, no creative tab. It drops nothing, but it is a
     * plain opaque cube without a tile entity, so 1.7's default {@code canSilkHarvest} lets silk touch keep it.
     */
    public static final RegistryObject<Block> oil_pipe = REG.block("oil_pipe",
            Block::new, p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL))
            .transform(Presets.cubeAll())
            .loot(Loot.silkTouchOnly())
            .tag(BlockTag.PICKAXE);

    /** Surface oil puddle (worldgen vents), old {@code BlockLayering}, hardness 0.1, snow sound. */
    public static final RegistryObject<BlockLayering> oil_spill = REG.block("oil_spill",
            BlockLayering::new, p -> p.strength(0.1F).sound(SoundType.SNOW).noOcclusion())
            .model(d -> d.models(Models.group()
                            .and("_full", Models.cubeAll("geontm:blocks/oil_spill"))
                            .and("_height10", layer(10))
                            .and("_height12", layer(12))
                            .and("_height14", layer(14))
                            .and("_height2", layer(2))
                            .and("_height4", layer(4))
                            .and("_height6", layer(6))
                            .and("_height8", layer(8)))
                    .states(Variants.select("layers", "1", "_height2", "2", "_height4", "3", "_height6", "4", "_height8",
                            "5", "_height10", "6", "_height12", "7", "_height14", "8", "_full")))
            .loot(Loot.nothing());

    static {
        blockItem(ore_oil, GeoNtmTab.RESOURCES);
        blockItem(oil_pipe, null);   // old setCreativeTab(null): item exists, no tab
        blockItem(oil_spill, GeoNtmTab.BLOCKS).transform(Presets.shares("geontm:block/oil_spill_height2"));
    }

    private static Reg.Handle<BlockItem> blockItem(RegistryObject<? extends Block> block, GeoNtmTab tab) {
        Reg.Handle<BlockItem> item = REG.blockItem(block, p -> Compat.blockDescription(p));
        return tab != null ? item.tab(tab) : item;
    }

    /** One snow-layer height of the puddle (vanilla's snow_height models, retextured). */
    private static Shape layer(int height) {
        return Models.parented("minecraft:block/snow_height" + height,
                Slots.of("particle", "geontm:blocks/oil_spill", "texture", "geontm:blocks/oil_spill"));
    }

    public static void init() {}
}
