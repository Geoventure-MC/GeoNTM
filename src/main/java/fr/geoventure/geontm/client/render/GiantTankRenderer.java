package fr.geoventure.geontm.client.render;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geontm.inventory.fluid.FluidHazard;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineGiantTank;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.CameraInfo;
import fr.geoventure.geocore.render.CompatRenderTypes;
import fr.geoventure.geocore.render.SubmitSink;
import fr.geoventure.geocore.render.Vertexes;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.GemBER;
import fr.geoventure.geocore.render.gem.Surface;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/** Shared static-model and hazard-diamond pass for the two old giant storage machines. */
public abstract class GiantTankRenderer<T extends TileEntityMachineGiantTank>
        extends GemBER<T, GiantTankRenderState> {

    protected final WavefrontObj model;
    protected final Surface body;

    protected GiantTankRenderer(String modelName, String textureName) {
        model = WavefrontObj.load(Ids.of(Constants.MOD_ID, "models/machines/" + modelName + ".obj"));
        body = Surface.cutout(Constants.MOD_ID + ":textures/models/machines/" + textureName + ".png");
    }

    @Override
    protected final void setup(Gem.Model<GiantTankRenderState> declaration) {
        placeMachine(declaration.offScreen().viewDistance(256).part(model.all(), body)
                .translate(0.5F, 0F, 0.5F));
    }

    /**
     * The machine's own placement, stated for the declared body. Its twin
     * {@link #applyMachineTransform} says the same thing to a pose stack, for the contents and the
     * hazard plate: those are built from the fill level rather than placed, so they keep drawing.
     */
    protected abstract void placeMachine(Gem.Part<GiantTankRenderState> part);

    @Override
    public GiantTankRenderState createRenderState() {
        return new GiantTankRenderState();
    }

    @Override
    public final void extract(T blockEntity, GiantTankRenderState state, float partialTicks, Vec3 cameraPosition) {
        state.fluidType = blockEntity.tankNew.getTankType();
        state.fill = blockEntity.tankNew.getFill();
        state.maxFill = blockEntity.tankNew.getMaxFill();
        state.gameTime = blockEntity.getLevel() == null ? 0L : blockEntity.getLevel().getGameTime();
        state.partialTick = partialTicks;
        extractMachine(blockEntity, state);
    }

    protected abstract void extractMachine(T blockEntity, GiantTankRenderState state);

    @Override
    protected final void submitUndeclared(GiantTankRenderState state, PoseStack poseStack, SubmitSink collector, CameraInfo camera) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0F, 0.5F);
        applyMachineTransform(state, poseStack);
        if (state.fluidType != null && state.fluidType != Fluids.NONE) {
            submitHazards(state, poseStack, collector);
            submitFluid(state, poseStack, collector);
        }
        poseStack.popPose();
    }

    protected abstract void applyMachineTransform(GiantTankRenderState state, PoseStack poseStack);

    protected abstract void submitHazards(GiantTankRenderState state, PoseStack poseStack, SubmitSink collector);

    protected abstract void submitFluid(GiantTankRenderState state, PoseStack poseStack, SubmitSink collector);

    protected static float fillHeight(GiantTankRenderState state, float fullHeight) {
        return state.maxFill <= 0 ? 0F : state.fill * fullHeight / state.maxFill;
    }

    protected final void submitHazardQuad(PoseStack poseStack, SubmitSink collector, FluidHazard hazard) {
        DiamondPronter.pront(poseStack, collector, hazard);
    }

    /** 1.7 {@code type.poison, type.flammability, type.reactivity, type.symbol}: the fluid's own diamond values. */
    protected static FluidHazard hazardFor(FluidType type) {
        return type == null ? FluidHazard.NONE : type.clientMetadata().getHazard();
    }
}
