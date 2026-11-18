package fr.geoventure.geontm.client.render;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geontm.tileentity.machine.TileEntityFurnaceCombination;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.CameraInfo;
import fr.geoventure.geocore.render.CompatRenderTypes;
import fr.geoventure.geocore.render.Poses;
import fr.geoventure.geocore.render.SubmitSink;
import fr.geoventure.geocore.render.Vertexes;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.GemBER;
import fr.geoventure.geocore.render.gem.Surface;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

/** Static combination-oven OBJ plus the legacy fullbright, camera-facing RBMK flame quad. */
public final class CombinationFurnaceRenderer extends GemBER<TileEntityFurnaceCombination, CombinationFurnaceRenderState> {

    private static final ResourceLocation OBJ = Ids.of(Constants.MOD_ID, "models/machines/combination_oven.obj");
    private static final ResourceLocation MACHINE_TEXTURE = Ids.of(Constants.MOD_ID, "textures/models/machines/combination_oven.png");
    private static final ResourceLocation FLAME_TEXTURE = Ids.of(Constants.MOD_ID, "textures/particle/rbmk_fire.png");

    private final WavefrontObj model = WavefrontObj.load(OBJ);
    private final Surface surface = Surface.cutout(MACHINE_TEXTURE.toString());

    @Override
    protected void setup(Gem.Model<CombinationFurnaceRenderState> declaration) {
        declaration.offScreen().viewDistance(256)
                .part(model.all(), surface)
                .translate(0.5F, 0F, 0.5F);
    }

    @Override
    public CombinationFurnaceRenderState createRenderState() {
        return new CombinationFurnaceRenderState();
    }

    @Override
    public void extract(TileEntityFurnaceCombination furnace, CombinationFurnaceRenderState state,
                        float partialTicks, Vec3 cameraPosition) {
        state.burning = furnace.wasOn;
        state.gameTime = furnace.getLevel() == null ? 0L : furnace.getLevel().getGameTime();
    }

    @Override
    protected void submitUndeclared(CombinationFurnaceRenderState state, PoseStack poseStack, SubmitSink collector, CameraInfo camera) {
        if (!state.burning) return;
        collector.order(1).submitCustomGeometry(poseStack, CompatRenderTypes.energySwirl(FLAME_TEXTURE, 0F, 0F),
                (pose, buffer) -> {
                    Poses.translate(pose, 0.5F, 1.75F, 0.5F);
                    Poses.rotate(pose, Axis.YP.rotationDegrees(-camera.yRot));

                    int textureIndex = (int) ((state.gameTime / 2L) % 14L);
                    float frameWidth = 1F / 14F;
                    float uMin = (textureIndex % 5) * frameWidth;
                    float uMax = uMin + frameWidth;
                    flameVertex(buffer, pose, -1F, 0F, 0F, uMax, 1F);
                    flameVertex(buffer, pose, -1F, 3F, 0F, uMax, 0F);
                    flameVertex(buffer, pose, 1F, 3F, 0F, uMin, 0F);
                    flameVertex(buffer, pose, 1F, 0F, 0F, uMin, 1F);
                });
    }

    private static void flameVertex(VertexConsumer buffer, PoseStack.Pose pose,
                                    float x, float y, float z, float u, float v) {
        Vertexes.vertex(buffer, pose, x, y, z, 1F, 1F, 1F, 1F, u, v,
                OverlayTexture.NO_OVERLAY, 0x00F000F0, 0F, 1F, 0F);
    }
}
