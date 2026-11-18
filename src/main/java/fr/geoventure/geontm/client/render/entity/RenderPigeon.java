package fr.geoventure.geontm.client.render.entity;

import fr.geoventure.geontm.entity.mob.EntityPigeon;
import fr.geoventure.geontm.main.ResourceManager;
import fr.geoventure.geocore.render.living.CompatLivingRenderState;
import fr.geoventure.geocore.render.living.CompatMobRenderer;
import fr.geoventure.geocore.render.living.LivingModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

/**
 * 1.7 RenderPigeon.
 */
public class RenderPigeon extends CompatMobRenderer<EntityPigeon, RenderPigeon.State> {

    public RenderPigeon(EntityRendererProvider.Context context) {
        super(context, LivingModel.custom(new ModelPigeon(ModelPigeon.createBodyLayer().bakeRoot())), 0.3F);
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    protected void extract(EntityPigeon entity, State state, float partialTicks) {
        float fall = Mth.lerp(partialTicks, entity.prevFallTime, entity.fallTime);
        float dest = Mth.lerp(partialTicks, entity.prevDest, entity.dest);
        state.flap = (Mth.sin(fall) + 1.0F) * dest;
        state.fat = entity.isFat();
    }

    @Override
    protected Identifier texture(State state) {
        return ResourceManager.pigeon_tex;
    }

    public static final class State extends CompatLivingRenderState {
        /** 1.7 fed the wing flap through handleRotationFloat, which the model read as ageInTicks. */
        public float flap;
        public boolean fat;
    }
}
