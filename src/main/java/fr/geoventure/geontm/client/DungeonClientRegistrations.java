package fr.geoventure.geontm.client;

import fr.geoventure.geontm.client.render.GeoNtmBlockEntityRenderers;
import fr.geoventure.geontm.client.render.RebarPlacerPreview;
import fr.geoventure.geontm.client.render.RenderLanternBehemoth;
import fr.geoventure.geontm.client.render.RenderLogicBlock;
import fr.geoventure.geontm.client.render.RenderLoot;
import fr.geoventure.geontm.client.render.RenderPedestalTile;
import fr.geoventure.geontm.client.render.RenderRebarFill;
import fr.geoventure.geontm.client.render.RenderSkeletonHolder;
import fr.geoventure.geontm.inventory.ModDungeonMenus;
import fr.geoventure.geontm.inventory.gui.GUIRebar;
import fr.geoventure.geontm.tileentity.ModDungeonBlockEntities;

/** Screen, block-entity renderers and the rebar placer's world preview of the dungeon family. */
public final class DungeonClientRegistrations {

    private DungeonClientRegistrations() {}

    public static void registerScreens(GeoNtmClientRegistrations.ScreenSink sink) {
        sink.register(ModDungeonMenus.REBAR.get(), GUIRebar::new);
    }

    public static void registerRenderers(GeoNtmBlockEntityRenderers.Sink sink) {
        sink.register(ModDungeonBlockEntities.PEDESTAL.get(), ctx -> new RenderPedestalTile());
        GeoNtmBlockEntityRenderers.regGem(sink, ModDungeonBlockEntities.SKELETON_HOLDER.get(), RenderSkeletonHolder::new);
        sink.register(ModDungeonBlockEntities.LOOT.get(), ctx -> new RenderLoot());
        GeoNtmBlockEntityRenderers.regGem(sink, ModDungeonBlockEntities.LANTERN_BEHEMOTH.get(), RenderLanternBehemoth::new);
        sink.register(ModDungeonBlockEntities.LOGIC_BLOCK.get(), ctx -> new RenderLogicBlock());
        sink.register(ModDungeonBlockEntities.REBAR.get(), ctx -> new RenderRebarFill());
        RebarPlacerPreview.init();
    }
}
