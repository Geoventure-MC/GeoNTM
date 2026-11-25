package fr.geoventure.geontm.space.client;

import fr.geoventure.geontm.space.mob.SpaceMobs;
import fr.geoventure.geontm.client.render.GeoNtmEntityRenderers;

/**
 * Renderer bindings for hbm-space's mobs, one line per type (hbm-space {@code ClientProxy.registerEntityRenderer}).
 * Every NTM: Space entity type must have one: a visible type without a renderer takes the render thread down.
 * The entity bindings go through {@link GeoNtmEntityRenderers.Sink} (see {@link #registerEntities}) so that
 * {@link GeoNtmEntityRenderers#verifyCoverage} counts them; bound through another route they were reported as
 * "10 geontm entity types have no renderer" although they drew fine.
 */
public final class SpaceMobRenderers {

    private SpaceMobRenderers() {}

    public static void init() {
        MobWeaponRenderers.init();
    }

    /** Binds the ten NTM: Space mobs; called from {@code GeoNtmClientRegistrations.registerRenderers} with the recording sink. */
    public static void registerEntities(GeoNtmEntityRenderers.Sink r) {
        r.register(SpaceMobs.SIEGE_UFO.get(), RenderSiegeUFO::new);
        r.register(SpaceMobs.BF_ANGEL.get(), RenderBFA::new);
        r.register(SpaceMobs.MOON_COW.get(), RenderMoonCow::new);
        r.register(SpaceMobs.SCUTTERFISH.get(), RenderLaytheFauna.Scutterfish::new);
        r.register(SpaceMobs.TANKBOT.get(), RenderTankbot::new);
        r.register(SpaceMobs.WAR_BEHEMOTH.get(), RenderBehemoth::new);
        r.register(SpaceMobs.SCUTTLECRAB.get(), RenderLaytheFauna.Scuttlecrab::new);
        r.register(SpaceMobs.DEPTH_SQUID.get(), RenderLaytheFauna.DepthSquid::new);
        r.register(SpaceMobs.SCRAPFISH.get(), RenderLaytheFauna.ScrapFish::new);
        r.register(SpaceMobs.SIFTER_EEL.get(), RenderLaytheFauna.SifterEel::new);
    }
}
