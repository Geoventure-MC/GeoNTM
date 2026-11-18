package fr.geoventure.geontm.client.render;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineHephaestus;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.CameraInfo;
import fr.geoventure.geocore.render.CompatRenderTypes;
import fr.geoventure.geocore.render.SubmitSink;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.GemBER;
import fr.geoventure.geocore.render.gem.Geometry;
import fr.geoventure.geocore.render.gem.Surface;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;

/**
 * 1.7 {@code RenderHephaestus}: the housing and three rotors 120° apart (culling is re-enabled only after
 * them) turning with the heat, and the core drawn with a texture-matrix-scrolled skin: fullbright lava while heat flows, half-grey
 * cobblestone when it does not. The core keeps its scrolling through {@link UvTransformDraw} since a scaled
 * texture matrix has no Gem spelling.
 */
public final class RenderHephaestus extends GemBER<TileEntityMachineHephaestus, HephaestusRenderState> {

    private final WavefrontObj model = WavefrontObj.load(
            Ids.of(Constants.MOD_ID, "models/machines/hephaestus.obj"));
    private final Surface housing = Surface.cutoutNoCull(
            Constants.MOD_ID + ":textures/models/machines/hephaestus.png");
    private final Geometry core = model.part("Core");
    private final RenderType lava = CompatRenderTypes.entityCutoutCull(
            Ids.of(Constants.MOD_ID, "textures/models/machines/lava.png"));
    private final RenderType cobble = CompatRenderTypes.entityCutoutCull(
            Ids.of(Constants.MOD_ID, "textures/models/machines/cobblestone.png"));

    @Override
    protected void setup(Gem.Model<HephaestusRenderState> declaration) {
        Gem.Part<HephaestusRenderState> machine = declaration.offScreen().viewDistance(256)
                .joint()
                .translate(0.5F, 0F, 0.5F);

        machine.child(model.part("Main"), housing);
        Gem.Part<HephaestusRenderState> spinning = machine.joint().angle(Gem.Axes.Y, state -> state.movement);
        for (int i = 0; i < 3; i++) {
            spinning.child(model.part("Rotor"), housing).rotate(Gem.Axes.Y, 120F * i);
        }
    }

    @Override
    public HephaestusRenderState createRenderState() {
        return new HephaestusRenderState();
    }

    @Override
    public void extract(TileEntityMachineHephaestus be, HephaestusRenderState state, float partialTicks,
                        Vec3 cameraPosition) {
        state.movement = be.getRot(partialTicks);
        state.on = be.bufferedHeat > 0;
    }

    /** The core: 1.7 scaled the texture matrix by 0.5 and scrolled v by {@code movement / 10}. */
    @Override
    protected void submitUndeclared(HephaestusRenderState state, PoseStack poseStack, SubmitSink sink,
                                    CameraInfo camera) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0F, 0.5F);
        float scroll = state.movement / 10F;
        if (state.on) {
            sink.submitCustomGeometry(poseStack, lava, (pose, buffer) -> UvTransformDraw.draw(core, pose, buffer,
                    FoundryRenderHelper.FULLBRIGHT, OverlayTexture.NO_OVERLAY, -1, 0.5F, 0F, scroll));
        } else {
            int light = state.lightCoords;
            sink.submitCustomGeometry(poseStack, cobble, (pose, buffer) -> UvTransformDraw.draw(core, pose, buffer,
                    light, OverlayTexture.NO_OVERLAY, 0xFF808080, 0.5F, 0F, scroll));
        }
        poseStack.popPose();
    }
}
