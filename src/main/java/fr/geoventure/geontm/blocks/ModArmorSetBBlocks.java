package fr.geoventure.geontm.blocks;

import fr.geoventure.geocore.registry.Loot;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.dejson.Variants;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.blocks.generic.DecoBlockAlt;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.SoundType;

/**
 * The one block of armour set B: 1.7 {@code statue_elb_f}, the euphemium kit's statue ({@link DecoBlockAlt}).
 * 1.7 registered it under its unlocalized name {@code tile.#undef}, which no modern id can spell, so it keeps its
 * field name. Hardness and resistance infinite, light level 15, the default stone step sound, no creative tab.
 */
public final class ModArmorSetBBlocks {

    private ModArmorSetBBlocks() {}

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCKS, ITEMS);

    /** Renderer-drawn, facing states; 1.7 {@code func_147480_a(x, y, z, true)}: the red key pops it out as itself. */
    public static final RegistryObject<DecoBlockAlt> statue_elb_f = REG.block("statue_elb_f",
                    DecoBlockAlt::new, p -> p.strength(-1.0F, 3_600_000.0F).lightLevel(s -> 15).sound(SoundType.STONE)
                            .noOcclusion().isSuffocating((s, w, pos) -> false).isViewBlocking((s, w, pos) -> false))
            .transform(Presets.rendered(Variants.uniform("facing", "north", "east", "south", "west")))
            .loot(Loot.self());

    /** 1.7's item was the flat {@code blocks/code} icon. */
    public static final RegistryObject<BlockItem> statue_elb_f_item = REG.blockItem(statue_elb_f)
            .transform(Presets.sprite("geontm:blocks/code"));

    public static void init() {}
}
