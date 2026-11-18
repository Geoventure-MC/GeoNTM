package fr.geoventure.geontm.client.render;

import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geontm.items.weapon.sedna.impl.ItemGunEgon;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.CameraInfo;
import fr.geoventure.geocore.render.CompatHumanoidLayer;
import fr.geoventure.geocore.render.CompatRenderTypes;
import fr.geoventure.geocore.render.HumanoidPose;
import fr.geoventure.geocore.render.SubmitSink;
import fr.geoventure.geocore.render.Worn;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

/**
 * 1.12.2 {@code EgonBackpackRenderer}: a child of the player model's body that drew the gluon gun's backpack while
 * the player held the gun in the main hand.
 */
public final class EgonBackpackRenderLayer extends CompatHumanoidLayer {

    private static final Identifier TEXTURE = Ids.of("geontm", "textures/models/weapons/egon.png");
    private static WavefrontObj model;

    @SuppressWarnings("rawtypes")
    public EgonBackpackRenderLayer(RenderLayerParent parent) {
        super(parent);
    }

    @Override
    protected boolean drawsUnequipped() {
        return true;
    }

    @Override
    public void submit(Worn worn, PoseStack pose, SubmitSink sink, int light, HumanoidPose parts, CameraInfo camera) {
        Player player = parts.player();
        if (parts.invisible || player == null || !(player.getMainHandItem().getItem() instanceof ItemGunEgon)) return;
        if (model == null) model = WavefrontObj.load(Ids.of("geontm", "models/weapons/egon.obj"));
        WavefrontObj obj = model;
        pose.pushPose();
        parts.body().translateAndRotate(pose);
        pose.translate(0F, 0.75F, 0F);
        pose.scale(0.0625F, 0.0625F, 0.0625F);
        pose.mulPose(Axis.ZP.rotationDegrees(180F));
        pose.mulPose(Axis.YP.rotationDegrees(90F));
        // 1.12 drew the living model with culling off
        sink.submitCustomGeometry(pose, CompatRenderTypes.entityCutoutNoCull(TEXTURE),
                (p, buffer) -> obj.renderAll(p, buffer, light, OverlayTexture.NO_OVERLAY));
        pose.popPose();
    }
}
