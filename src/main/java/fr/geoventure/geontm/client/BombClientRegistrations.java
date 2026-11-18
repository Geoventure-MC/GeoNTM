package fr.geoventure.geontm.client;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.blocks.bomb.NukeBombBlock;
import fr.geoventure.geontm.client.render.BombObjRenderer;
import fr.geoventure.geontm.client.render.GeoNtmBlockEntityRenderers;
import fr.geoventure.geontm.client.render.GeoNtmEntityRenderers;
import fr.geoventure.geontm.client.render.PrimedExplosiveRenderer;
import fr.geoventure.geontm.entity.ModEntities;
import fr.geoventure.geontm.inventory.ModMenus;
import fr.geoventure.geontm.inventory.gui.GUINukeMike;
import fr.geoventure.geontm.tileentity.ModBlockEntities;
import fr.geoventure.geocore.Ids;

/** Feature-local client registrations; shared client wiring only needs to call these three methods. */
public final class BombClientRegistrations {

    private BombClientRegistrations() {}

    public static void registerScreens(GeoNtmClientRegistrations.ScreenSink sink) {
        sink.register(ModMenus.NUKE_MIKE.get(), GUINukeMike::new);
    }

    public static void registerBlockEntityRenderers(GeoNtmBlockEntityRenderers.Sink sink) {
        sink.register(ModBlockEntities.NUKE_MIKE.get(), context -> new BombObjRenderer<>(
                Ids.of(Constants.MOD_ID, "models/bombs/ivymike.obj"),
                Ids.of(Constants.MOD_ID, "textures/models/bombs/ivymike.png"),
                1F, NukeBombBlock.FACING, 0F, 0F, 0F,
                // 1.7 RenderNukeMike: 3 -> 0, 5 -> 90, 2 -> 180, 4 -> 270, remapped to the port's
                // opposite-facing numbering (1.7 meta 5/4/2/3 for port facing 2/3/4/5).
                90F, 270F, 180F, 0F));
    }

    public static void registerEntityRenderers(GeoNtmEntityRenderers.Sink sink) {
        GeoNtmEntityRenderers.regGem(sink, ModEntities.PRIMED_EXPLOSIVE.get(), PrimedExplosiveRenderer::new);
    }
}
