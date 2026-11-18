package fr.geoventure.geontm.blocks.machine;

import fr.geoventure.geocore.registry.BlockTag;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geocore.dejson.Variants;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import fr.geoventure.geocore.world.Compat;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.SoundType;
import fr.geoventure.geocore.registry.Loot;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;

/** Registration, blockstate, item definition and loot of the legacy weapon modification table. */
public final class ModWeaponTableBlocks {
    private ModWeaponTableBlocks() {}

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCKS, ITEMS);

    public static final RegistryObject<BlockWeaponTable> MACHINE_WEAPON_TABLE = REG.<BlockWeaponTable>block(
                    "machine_weapon_table", BlockWeaponTable::new,
                    // 1.7 Material.iron, setHardness(5.0F).setResistance(10.0F): drops only to a pickaxe.
                    p -> p.requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL))
            // The block model is authored (models/block/machine_weapon_table.json, a gun_table cube_bottom_top).
            .transform(Presets.statesOnly(Variants.simple()))
            .loot(Loot.self())
            .tag(BlockTag.PICKAXE);
    public static final RegistryObject<BlockItem> MACHINE_WEAPON_TABLE_ITEM = REG.blockItem(
                    MACHINE_WEAPON_TABLE, Compat::blockDescription)
            .transform(Presets.shares("geontm:block/machine_weapon_table"));

    static {
        ModTabs.addTo(GeoNtmTab.CONSUMABLES, MACHINE_WEAPON_TABLE_ITEM);
    }

    public static void init() {}
}
