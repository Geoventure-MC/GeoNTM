package fr.geoventure.geontm.blocks;

import fr.geoventure.geocore.dejson.Models;
import fr.geoventure.geocore.dejson.Variants;
import fr.geoventure.geocore.registry.BlockTag;
import fr.geoventure.geocore.registry.Loot;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.blocks.generic.BlockNTMLadder;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import fr.geoventure.geocore.world.Compat;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.SoundType;

/** The twelve GEONTM ladder blocks, backed by vanilla {@link net.minecraft.world.level.block.LadderBlock}. */
public final class ModLadderBlocks {

    private ModLadderBlocks() {}

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCKS, ITEMS);

    public static final RegistryObject<BlockNTMLadder> ladder_sturdy = ladder("ladder_sturdy", false)
            .model(d -> d.statesOnly().states(Variants.facing()))
            .icon(d -> d.models(Models.blockItem("geontm:block/ladder_sturdy")))
            .loot(Loot.self())
            .tag(BlockTag.AXE)
            .tag(BlockTag.CLIMBABLE);
    public static final RegistryObject<BlockNTMLadder> ladder_iron = ladder("ladder_iron", false)
            .model(d -> d.statesOnly().states(Variants.facing()))
            .icon(d -> d.models(Models.blockItem("geontm:block/ladder_iron")))
            .loot(Loot.self())
            .tag(BlockTag.AXE)
            .tag(BlockTag.CLIMBABLE);
    public static final RegistryObject<BlockNTMLadder> ladder_gold = ladder("ladder_gold", false)
            .model(d -> d.statesOnly().states(Variants.facing()))
            .icon(d -> d.models(Models.blockItem("geontm:block/ladder_gold")))
            .loot(Loot.self())
            .tag(BlockTag.AXE)
            .tag(BlockTag.CLIMBABLE);
    public static final RegistryObject<BlockNTMLadder> ladder_aluminium = ladder("ladder_aluminium", false)
            .model(d -> d.statesOnly().states(Variants.facing()))
            .icon(d -> d.models(Models.blockItem("geontm:block/ladder_aluminium")))
            .loot(Loot.self())
            .tag(BlockTag.AXE)
            .tag(BlockTag.CLIMBABLE);
    public static final RegistryObject<BlockNTMLadder> ladder_copper = ladder("ladder_copper", false)
            .model(d -> d.statesOnly().states(Variants.facing()))
            .icon(d -> d.models(Models.blockItem("geontm:block/ladder_copper")))
            .loot(Loot.self())
            .tag(BlockTag.AXE)
            .tag(BlockTag.CLIMBABLE);
    public static final RegistryObject<BlockNTMLadder> ladder_titanium = ladder("ladder_titanium", false)
            .model(d -> d.statesOnly().states(Variants.facing()))
            .icon(d -> d.models(Models.blockItem("geontm:block/ladder_titanium")))
            .loot(Loot.self())
            .tag(BlockTag.AXE)
            .tag(BlockTag.CLIMBABLE);
    public static final RegistryObject<BlockNTMLadder> ladder_lead = ladder("ladder_lead", false)
            .model(d -> d.statesOnly().states(Variants.facing()))
            .icon(d -> d.models(Models.blockItem("geontm:block/ladder_lead")))
            .loot(Loot.self())
            .tag(BlockTag.AXE)
            .tag(BlockTag.CLIMBABLE);
    public static final RegistryObject<BlockNTMLadder> ladder_cobalt = ladder("ladder_cobalt", false)
            .model(d -> d.statesOnly().states(Variants.facing()))
            .icon(d -> d.models(Models.blockItem("geontm:block/ladder_cobalt")))
            .loot(Loot.self())
            .tag(BlockTag.AXE)
            .tag(BlockTag.CLIMBABLE);
    public static final RegistryObject<BlockNTMLadder> ladder_steel = ladder("ladder_steel", false)
            .model(d -> d.statesOnly().states(Variants.facing()))
            .icon(d -> d.models(Models.blockItem("geontm:block/ladder_steel")))
            .loot(Loot.self())
            .tag(BlockTag.AXE)
            .tag(BlockTag.CLIMBABLE);
    public static final RegistryObject<BlockNTMLadder> ladder_tungsten = ladder("ladder_tungsten", false)
            .model(d -> d.statesOnly().states(Variants.facing()))
            .icon(d -> d.models(Models.blockItem("geontm:block/ladder_tungsten")))
            .loot(Loot.self())
            .tag(BlockTag.AXE)
            .tag(BlockTag.CLIMBABLE);
    public static final RegistryObject<BlockNTMLadder> ladder_red = ladder("ladder_red", false)
            .model(d -> d.statesOnly().states(Variants.facing()))
            .icon(d -> d.models(Models.blockItem("geontm:block/ladder_red_item")))
            .loot(Loot.self())
            .tag(BlockTag.AXE)
            .tag(BlockTag.CLIMBABLE);
    public static final RegistryObject<BlockNTMLadder> ladder_red_top = ladder("ladder_red_top", true)
            .model(d -> d.statesOnly().states(Variants.facing()))
            .icon(d -> d.models(Models.blockItem("geontm:block/ladder_red_top_item")))
            .loot(Loot.self())
            .tag(BlockTag.AXE)
            .tag(BlockTag.CLIMBABLE);

    private static Reg.Handle<BlockNTMLadder> ladder(String id, boolean top) {
        Reg.Handle<BlockNTMLadder> block = REG.block(id,
                p -> new BlockNTMLadder(p, top),
                p -> p.strength(0.25F, 1.2F).sound(SoundType.LADDER).noOcclusion());
        Reg.Handle<BlockItem> item = REG.blockItem(block, Compat::blockDescription);
        ModTabs.addTo(GeoNtmTab.BLOCKS, item);
        return block;
    }

    public static void init() {}
}
