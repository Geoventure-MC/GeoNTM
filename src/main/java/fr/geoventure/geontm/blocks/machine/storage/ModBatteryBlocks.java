package fr.geoventure.geontm.blocks.machine.storage;

import fr.geoventure.geocore.registry.BlockTag;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.items.machine.ItemBatteryREDD;
import fr.geoventure.geocore.world.Compat;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.world.level.block.SoundType;
import fr.geoventure.geocore.registry.Loot;
import fr.geoventure.geocore.registry.Preset;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.dejson.Variants;

/**
 * Hand-registered energy-storage machine blocks (the battery socket and the FEnSU). Separate from the generated
 * {@link fr.geoventure.geontm.blocks.ModBlocks} and the general {@link fr.geoventure.geontm.blocks.machine.ModMachineBlocks} so codegen
 * never clobbers them.
 */
public final class ModBatteryBlocks {

    private ModBatteryBlocks() {}

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCKS, ITEMS);
    /** Folded multiblocks drawn by their renderer: every core/cell state is the shared empty placeholder. */
    private static final Preset FOLDED = Presets.rendered(Variants.product(
            Variants.uniform("facing", "north", "east", "south", "west"),
            Variants.uniform("cell", "false", "true")));

    public static final RegistryObject<MachineBatterySocket> machine_battery_socket = REG.<MachineBatterySocket>block(
            "machine_battery_socket", MachineBatterySocket::new,
            p -> p.strength(5.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion())
            .transform(FOLDED)
            .loot(Loot.self())
            .tag(BlockTag.PICKAXE);

    public static final RegistryObject<MachineBatteryREDD> machine_battery_redd = REG.<MachineBatteryREDD>block(
            "machine_battery_redd", MachineBatteryREDD::new,
            p -> p.strength(5.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion()
                    .isSuffocating((state, world, pos) -> false)
                    .isViewBlocking((state, world, pos) -> false))
            .transform(FOLDED)
            .loot(Loot.self())
            .tag(BlockTag.PICKAXE);

    static {
        REG.blockItem(machine_battery_socket).tab(GeoNtmTab.MACHINES);
        REG.<MachineBatteryREDD, ItemBatteryREDD>blockItem(machine_battery_redd, ItemBatteryREDD::new,
                        Compat::blockDescription)
                .tab(GeoNtmTab.MACHINES);
    }

    public static void init() {}
}
