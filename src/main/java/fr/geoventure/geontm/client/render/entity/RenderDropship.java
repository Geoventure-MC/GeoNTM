package fr.geoventure.geontm.client.render.entity;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geontm.entity.missile.EntitySatellitePod;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.CameraInfo;
import fr.geoventure.geocore.render.CompatEntityRenderState;
import fr.geoventure.geocore.render.CompatEntityRenderer;
import fr.geoventure.geocore.render.CompatRenderTypes;
import fr.geoventure.geocore.render.SubmitSink;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;

/** 1.7 {@code RenderDropship}: the cargo pod and its four landing struts, folded up 150 degrees in flight. */
public class RenderDropship extends CompatEntityRenderer<EntitySatellitePod, RenderDropship.State> {

    private static final WavefrontObj MODEL = WavefrontObj.load(Ids.of(Constants.MOD_ID, "models/dropship.obj"));
    private static final RenderType TYPE = CompatRenderTypes.entityCutoutCull(Ids.of(Constants.MOD_ID, "textures/entity/dropship.png"));

    public RenderDropship(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0F;
    }

    @Override
    public boolean culled(EntitySatellitePod entity) {
        return false;
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extract(EntitySatellitePod pod, State state, float partialTicks) {
        state.legs = pod.prevLegs + (pod.legs - pod.prevLegs) * partialTicks;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitSink sink, CameraInfo camera) {
        int light = state.lightCoords;
        sink.submitCustomGeometry(poseStack, TYPE, (pose, buffer) ->
                MODEL.renderPart("Pod", pose, buffer, light, OverlayTexture.NO_OVERLAY));

        for (int i = 0; i < 4; i++) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(45 + 90 * i));
            poseStack.translate(0.5D, 1.75D, 0D);
            poseStack.mulPose(Axis.ZP.rotationDegrees(150F * (1F - state.legs)));
            poseStack.translate(-0.5D, -1.75D, 0D);
            sink.submitCustomGeometry(poseStack, TYPE, (pose, buffer) ->
                    MODEL.renderPart("Leg", pose, buffer, light, OverlayTexture.NO_OVERLAY));
            poseStack.popPose();
        }
    }

    public static final class State extends CompatEntityRenderState {
        float legs;
    }
}
