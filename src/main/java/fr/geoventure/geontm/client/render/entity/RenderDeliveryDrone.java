package fr.geoventure.geontm.client.render.entity;

import fr.geoventure.geontm.entity.item.EntityDeliveryDrone;
import fr.geoventure.geontm.entity.item.EntityDroneBase;
import fr.geoventure.geontm.entity.item.EntityRequestDrone;
import fr.geoventure.geontm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.geoventure.geocore.render.CameraInfo;
import fr.geoventure.geocore.render.CompatEntityRenderState;
import fr.geoventure.geocore.render.CompatEntityRenderer;
import fr.geoventure.geocore.render.CompatRenderTypes;
import fr.geoventure.geocore.render.SubmitSink;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;

/** 1.7 RenderDeliveryDrone: the quadcopter body and, by appearance byte, the crate or barrel slung under it. */
public class RenderDeliveryDrone<T extends EntityDroneBase> extends CompatEntityRenderer<T, RenderDeliveryDrone.State> {

    private static final RenderType DELIVERY = CompatRenderTypes.entityCutoutNoCull(ResourceManager.delivery_drone_tex);
    private static final RenderType REQUEST = CompatRenderTypes.entityCutoutNoCull(ResourceManager.delivery_drone_request_tex);
    private static final RenderType EXPRESS = CompatRenderTypes.entityCutoutNoCull(ResourceManager.delivery_drone_express_tex);

    public RenderDeliveryDrone(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0F;
    }

    private static void part(PoseStack poseStack, SubmitSink sink, RenderType type, int light, String part) {
        sink.submitCustomGeometry(poseStack, type, (pose, buffer) ->
                NtmEntityModels.DELIVERY_DRONE.renderPart(part, pose, buffer, light, OverlayTexture.NO_OVERLAY));
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extract(T entity, State state, float partialTicks) {
        if (entity instanceof EntityRequestDrone) state.type = REQUEST;
        else if (entity instanceof EntityDeliveryDrone delivery && delivery.isExpress()) state.type = EXPRESS;
        else state.type = DELIVERY;
        state.appearance = entity.getAppearance();
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitSink sink, CameraInfo camera) {
        int light = state.lightCoords;
        part(poseStack, sink, state.type, light, "Drone");
        if (state.appearance == 1) part(poseStack, sink, state.type, light, "Crate");
        if (state.appearance == 2) part(poseStack, sink, state.type, light, "Barrel");
    }

    public static final class State extends CompatEntityRenderState {
        public RenderType type = DELIVERY;
        public int appearance;
    }
}
