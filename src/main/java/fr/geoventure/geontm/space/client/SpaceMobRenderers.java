package fr.geoventure.geontm.space.client;

import fr.geoventure.geontm.space.mob.SpaceMobs;
import fr.geoventure.geocore.client.ClientRegistrations;
import fr.geoventure.geocore.render.living.CompatCreeperRenderer;

/**
 * Renderer bindings for hbm-space's mobs, one line per type (hbm-space {@code ClientProxy.registerEntityRenderer}).
 * Every NTM: Space entity type must have one: a visible type without a renderer takes the render thread down.
 */
public final class SpaceMobRenderers {

    private SpaceMobRenderers() {}

    public static void init() {
        MobWeaponRenderers.init();
        ClientRegistrations.renderers(r -> {
            r.entity(SpaceMobs.SIEGE_UFO.get(), RenderSiegeUFO::new);
            r.entity(SpaceMobs.BF_ANGEL.get(), RenderBFA::new);
            r.entity(SpaceMobs.MOON_COW.get(), RenderMoonCow::new);
            r.entity(SpaceMobs.SCUTTERFISH.get(), RenderLaytheFauna.Scutterfish::new);
            r.entity(SpaceMobs.TANKBOT.get(), RenderTankbot::new);
            r.entity(SpaceMobs.WAR_BEHEMOTH.get(), RenderBehemoth::new);
            r.entity(SpaceMobs.SCUTTLECRAB.get(), RenderLaytheFauna.Scuttlecrab::new);
            r.entity(SpaceMobs.DEPTH_SQUID.get(), RenderLaytheFauna.DepthSquid::new);
            r.entity(SpaceMobs.SCRAPFISH.get(), RenderLaytheFauna.ScrapFish::new);
            r.entity(SpaceMobs.SIFTER_EEL.get(), RenderLaytheFauna.SifterEel::new);
        });
    }
}
