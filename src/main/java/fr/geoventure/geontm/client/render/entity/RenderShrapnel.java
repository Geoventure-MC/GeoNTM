package fr.geoventure.geontm.client.render.entity;

import fr.geoventure.geontm.entity.projectile.EntityShrapnel;
import fr.geoventure.geontm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.geoventure.geocore.render.CameraInfo;
import fr.geoventure.geocore.render.CompatEntityRenderState;
import fr.geoventure.geocore.render.CompatEntityRenderer;
import fr.geoventure.geocore.render.CompatRenderTypes;
import fr.geoventure.geocore.render.SubmitSink;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.entity.Entity;
import org.joml.Vector3f;

/**
 * 1.7 RenderShrapnel: the tumbling 4x4x4 cube, tripled for the volcano shards. Also serves the firework
 * ball, which 1.7 drew with the same model.
 */
public class RenderShrapnel<T extends Entity> extends CompatEntityRenderer<T, RenderShrapnel.State> {

    private static final RenderType TYPE = CompatRenderTypes.entityCutout(ResourceManager.shrapnel_tex);
    private static final Vector3f TUMBLE = new Vector3f(1F, 1F, 1F).normalize();

    public RenderShrapnel(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0F;
    }

    /** The volcano and rad-volcano trails (2 and 4 in {@code EntityShrapnel}'s synced byte) fly as boulders. */
    public static float scale(Entity entity) {
        if (!(entity instanceof EntityShrapnel shrapnel)) return 1F;
        byte trail = shrapnel.getTrail();
        return trail == 2 || trail == 4 ? 3F : 1F;
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extract(T entity, State state, float partialTicks) {
        state.spin = (entity.tickCount % 360) * 10 + partialTicks;
        state.scale = scale(entity);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitSink sink, CameraInfo camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(180F));
        poseStack.mulPose(Axis.of(TUMBLE).rotationDegrees(state.spin));
        poseStack.scale(state.scale, state.scale, state.scale);
        RenderBoxModel.submit(poseStack, sink, TYPE, RenderBoxModel.SHRAPNEL, state.lightCoords);
        poseStack.popPose();
    }

    public static final class State extends CompatEntityRenderState {
        float spin;
        float scale = 1F;
    }
}
