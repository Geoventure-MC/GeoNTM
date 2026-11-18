package fr.geoventure.geontm.client.render.armor;

import fr.geoventure.geontm.client.jetpack.JetpackClientVisuals;
import fr.geoventure.geontm.items.armor.ArmorNo9;
import fr.geoventure.geontm.items.armor.IWornModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.CameraInfo;
import fr.geoventure.geocore.render.CompatHumanoidLayer;
import fr.geoventure.geocore.render.CompatRenderTypes;
import fr.geoventure.geocore.render.HumanoidPose;
import fr.geoventure.geocore.render.ModelParts;
import fr.geoventure.geocore.render.SubmitSink;
import fr.geoventure.geocore.render.Worn;
import fr.geoventure.geocore.world.ArmorSlot;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

/**
 * The worn bodies of the plain model helmets ({@link IWornModel}), which {@link ArmorRenderLayer} does not see
 * because they are not {@code ArmorFSB}s:
 * <ul>
 * <li>the goggles, a {@code ModelBiped} ({@link ModelGoggles}) that follows the live head and takes no crouch
 *     offset, textured {@code textures/models/Goggles.png};</li>
 * <li>the ash goggles, Fabulous Hat and mining helmet, {@code ModelArmorBase} OBJs drawn through
 *     {@link ArmorRenderers} with ModelArmorBase's +0.2Y crouch offset, as {@link ArmorRenderLayer} applies it.</li>
 * </ul>
 */
public class WornModelRenderLayer extends CompatHumanoidLayer {

    private static final Identifier GOGGLES = Ids.of("geontm", "textures/models/goggles.png");

    @SuppressWarnings("rawtypes")
    public WornModelRenderLayer(RenderLayerParent parent) {
        super(parent);
    }

    @Override
    public void submit(Worn worn, PoseStack pose, SubmitSink sink, int light, HumanoidPose parts,
                       CameraInfo camera) {
        if (parts.invisible) return;
        ItemStack head = worn.get(ArmorSlot.HELMET);
        if (!(head.getItem() instanceof IWornModel model)) return;

        if ("goggles".equals(model.wornModel())) {
            goggles(pose, sink, light, parts);
            return;
        }

        ArmorRenderer renderer = ArmorRenderers.get(model.wornModel());
        if (renderer == null) return;
        boolean crouching = parts.sneaking
                && !JetpackClientVisuals.freezesPlayerPose(parts.player());
        HumanoidPose armorPose = crouching == parts.sneaking ? parts
                : new HumanoidPose(parts.model(), false, parts.baby, parts.invisible, parts.player());
        No9ArmorRenderer.lampOn = ArmorNo9.isOn(head);
        pose.pushPose();
        if (crouching && !parts.baby) pose.translate(0F, 0.2F, 0F);
        renderer.submit(ArmorSlot.HELMET, pose, sink, light, armorPose);
        pose.popPose();
    }

    /** ModelGoggles.render: google copies the head's X/Y point, yaw and pitch; scale 1/16. */
    private static void goggles(PoseStack pose, SubmitSink sink, int light, HumanoidPose parts) {
        ModelPart head = parts.head();
        if (!head.visible) return;
        pose.pushPose();
        pose.translate(head.x / 16F, head.y / 16F, 0F);
        pose.mulPose(Axis.YP.rotation(head.yRot));
        pose.mulPose(Axis.XP.rotation(head.xRot));
        ModelParts.submit(sink, pose, CompatRenderTypes.entityCutoutNoCull(GOGGLES), ModelGoggles.GOOGLE, light,
                OverlayTexture.NO_OVERLAY);
        pose.popPose();
    }
}
