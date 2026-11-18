package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.config.GeneralConfig;
import fr.geoventure.geocore.registry.BlockTag;
import fr.geoventure.geocore.registry.Loot;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.dejson.Models;
import fr.geoventure.geocore.dejson.Slots;
import fr.geoventure.geocore.dejson.Variants;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.blocks.generic.Guide;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.hazard.HazardSystem;
import fr.geoventure.geontm.hazard.transformer.HazardTransformerRadiationContainer;
import fr.geoventure.geontm.handler.ToolKeybinds;
import fr.geoventure.geontm.inventory.ModUtilityToolMenus;
import fr.geoventure.geontm.items.ModUtilityToolItems;
import fr.geoventure.geontm.items.UtilityToolDeclarations;
import fr.geoventure.geontm.items.machine.IItemFluidId;
import fr.geoventure.geontm.items.tool.ItemSettingsTool;
import fr.geoventure.geontm.packet.ItemControlPayload;
import fr.geoventure.geontm.packet.ToolKeyPayload;
import fr.geoventure.geocore.net.GeoCoreNetwork;
import fr.geoventure.geocore.platform.Lifecycle;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import fr.geoventure.geocore.world.Compat;
import fr.geoventure.geocore.world.ItemFirstUse;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.SoundType;

/**
 * One bootstrap hook for the utility tools and books ({@link ModUtilityToolItems}), their menus, the legacy
 * manual block {@code book_guide_block}, the item-control and copy-tool key payloads, the containment box's
 * hazard transformer and the settings tool's first-use hook.
 */
public final class ModUtilityToolContent {

    private ModUtilityToolContent() {}

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    public static final DeferredRegister.Items BLOCK_ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCKS, BLOCK_ITEMS);
    private static final String B = "geontm:blocks/";

    /** 1.7 {@code ModBlocks.book_guide}: {@code Material.iron}, hardness 5, resistance 10, nuke tab. */
    public static final RegistryObject<Guide> book_guide_block = REG.block("book_guide_block", Guide::new,
            p -> p.strength(5.0F, 10.0F).sound(SoundType.METAL).requiresCorrectToolForDrops())
            // 1.7 Guide: bottom/top plus four sides. Modelled for a placer looking north (1.7 metadata 3:
            // north right, south left, east front, west back) and turned with the facing.
            .transform(Presets.shape(Models.parented("minecraft:block/cube",
                            Slots.of("down", B + "guide_bottom", "up", B + "guide_top", "north", B + "guide_side_right")
                                    .and("south", B + "guide_side_left").and("east", B + "guide_front")
                                    .and("west", B + "guide_back").and("particle", B + "guide_front")))
                    .states(Variants.facing())
                    .viaItemModel())
            .loot(Loot.self())
            .tag(BlockTag.PICKAXE);

    static {
        REG.blockItem(book_guide_block, Compat::blockDescription).tab(GeoNtmTab.BOMBS);
    }

    public static void init() {
        ModUtilityToolItems.init();
        ModUtilityToolMenus.init();
        UtilityToolDeclarations.declare();

        GeoCoreNetwork.registerPlayToServer(ItemControlPayload.CODEC, (payload, player) ->
                ItemControlPayload.handle(payload, player));
        GeoCoreNetwork.registerPlayToServer(ToolKeyPayload.CODEC, (payload, player) ->
                ToolKeybinds.setKeyPressed(player, payload.key(), payload.pressed()));
        Lifecycle.onServerStopping(server -> ToolKeybinds.clear());

        // 1.7 HazardRegistry: LBSM_safeCrates (in LBSM mode) keeps crates from becoming radioactive
        if (!(GeneralConfig.enableLBSM && GeneralConfig.enableLBSMSafeCrates))
            HazardSystem.trafos.add(new HazardTransformerRadiationContainer());
        ItemFirstUse.register(ItemSettingsTool::useFirst);
        // 1.7 ItemFluidIDMulti.doesSneakBypassUse, also for the single-fluid identifier: a sneak-click retypes the
        // duct line, barrel or tank it hits
        ItemFirstUse.sneakBypass(stack -> stack.getItem() instanceof IItemFluidId);
    }
}
