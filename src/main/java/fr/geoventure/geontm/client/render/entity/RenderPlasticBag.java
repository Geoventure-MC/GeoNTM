package fr.geoventure.geontm.client.render.entity;

import fr.geoventure.geontm.entity.mob.EntityPlasticBag;
import fr.geoventure.geontm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.geoventure.geocore.render.CameraInfo;
import fr.geoventure.geocore.render.CompatEntityRenderState;
import fr.geoventure.geocore.render.CompatEntityRenderer;
import fr.geoventure.geocore.render.CompatRenderTypes;
import fr.geoventure.geocore.render.SubmitSink;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;

/**
 * 1.7 RenderPlasticBag: the drifting bag mesh laid on its heading, culling off so the sheet reads from both sides.
 */
public class RenderPlasticBag extends CompatEntityRenderer<EntityPlasticBag, RenderPlasticBag.State> {

    private final RenderType type = CompatRenderTypes.entityTranslucent(ResourceManager.plasticbag_tex);

    public RenderPlasticBag(EntityRendererProvider.Context context) {
        super(context);
        this.shadowStrength = 0F;
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extract(EntityPlasticBag entity, State state, float partialTicks) {
        state.yaw = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
        state.tilt = Mth.lerp(partialTicks, entity.xBodyRotO, entity.xBodyRot);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitSink sink, CameraInfo camera) {
        int light = state.lightCoords;
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yaw + 90F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.tilt));
        sink.submitCustomGeometry(poseStack, type, (pose, buffer) ->
                NtmEntityModels.PLASTIC_BAG.renderAll(pose, buffer, light, OverlayTexture.NO_OVERLAY, true));
        poseStack.popPose();
    }

    public static final class State extends CompatEntityRenderState {
        float yaw;
        float tilt;
    }
}
