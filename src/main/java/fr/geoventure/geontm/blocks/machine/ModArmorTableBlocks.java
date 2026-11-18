package fr.geoventure.geontm.blocks.machine;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import fr.geoventure.geocore.registry.BlockTag;
import fr.geoventure.geocore.registry.Loot;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.dejson.Models;
import fr.geoventure.geocore.dejson.Variants;

/** Hand-registered armor table block and item, kept separate from generated simple blocks. */
public final class ModArmorTableBlocks {

    private ModArmorTableBlocks() {}

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCKS, ITEMS);

    public static final RegistryObject<BlockArmorTable> ARMOR_TABLE = REG.<BlockArmorTable>block(
            "machine_armor_table", BlockArmorTable::new,
            // 1.7 BlockArmorTable(Material.iron) has no drop override: it drops itself, pickaxe-harvested.
            p -> p.strength(5.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops())
            .model(d -> d.models(Models.cubeBottomTop("geontm:blocks/armor_table_bottom", "geontm:blocks/armor_table_side", "geontm:blocks/armor_table_top")).states(Variants.simple()).blockItemDirect())
            .loot(Loot.self())
            .tag(BlockTag.PICKAXE);
    public static final RegistryObject<BlockItem> ARMOR_TABLE_ITEM = REG.blockItem(ARMOR_TABLE);

    static {
        ModTabs.addTo(GeoNtmTab.CONSUMABLES, ARMOR_TABLE_ITEM);
    }

    public static void init() {}
}
