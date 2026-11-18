package fr.geoventure.geontm.client.render.entity;

import fr.geoventure.geontm.entity.projectile.EntityBullet;
import fr.geoventure.geontm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.geoventure.geocore.render.CameraInfo;
import fr.geoventure.geocore.render.CompatEntityRenderState;
import fr.geoventure.geocore.render.CompatEntityRenderer;
import fr.geoventure.geocore.render.CompatRenderTypes;
import fr.geoventure.geocore.render.SubmitSink;
import java.util.Random;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

/**
 * 1.7 RenderRocket: the legacy bullet body, rolled by a per-entity fixed angle so a burst does not look extruded.
 */
public class RenderBullet extends CompatEntityRenderer<EntityBullet, RenderBullet.State> {

    private static final RenderType PLAIN = CompatRenderTypes.entityCutout(ResourceManager.bullet_tex);
    private static final RenderType CHOPPER = CompatRenderTypes.entityCutout(ResourceManager.bullet_chopper_tex);
    private static final RenderType CRITICAL = CompatRenderTypes.entityCutout(ResourceManager.bullet_critical_tex);

    public RenderBullet(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0F;
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extract(EntityBullet entity, State state, float partialTicks) {
        state.yaw = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
        state.pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        state.roll = new Random(entity.getId()).nextInt(360);
        state.type = entity.getIsChopper() ? CHOPPER : entity.getIsCritical() ? CRITICAL : PLAIN;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitSink sink, CameraInfo camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yaw - 90F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.pitch + 180F));
        poseStack.scale(1.5F, 1.5F, 1.5F);
        poseStack.mulPose(Axis.XP.rotationDegrees(state.roll));
        RenderBoxModel.submit(poseStack, sink, state.type, RenderBoxModel.BULLET, state.lightCoords);
        poseStack.popPose();
    }

    public static final class State extends CompatEntityRenderState {
        float yaw;
        float pitch;
        float roll;
        RenderType type = PLAIN;
    }
}
