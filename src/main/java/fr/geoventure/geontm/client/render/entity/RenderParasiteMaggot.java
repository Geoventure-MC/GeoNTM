package fr.geoventure.geontm.client.render.entity;

import fr.geoventure.geontm.entity.mob.EntityParasiteMaggot;
import fr.geoventure.geontm.main.ResourceManager;
import fr.geoventure.geocore.render.living.CompatLivingRenderState;
import fr.geoventure.geocore.render.living.CompatMobRenderer;
import fr.geoventure.geocore.render.living.LivingModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/** Vanilla silverfish geometry under the maggot's own sheet; it dies flat on its back like a silverfish. */
public final class RenderParasiteMaggot extends CompatMobRenderer<EntityParasiteMaggot, CompatLivingRenderState> {

    public RenderParasiteMaggot(EntityRendererProvider.Context context) {
        super(context, LivingModel.silverfish(context), 0.3F);
    }

    @Override
    public CompatLivingRenderState createRenderState() {
        return new CompatLivingRenderState();
    }

    @Override
    protected float flipDegrees() {
        return 180.0F;
    }

    @Override
    protected ResourceLocation texture(CompatLivingRenderState state) {
        return ResourceManager.parasite_maggot_tex;
    }
}
