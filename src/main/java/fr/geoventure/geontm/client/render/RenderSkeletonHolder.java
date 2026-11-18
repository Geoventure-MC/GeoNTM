package fr.geoventure.geontm.client.render;

import fr.geoventure.geontm.blocks.generic.BlockSkeletonHolder;
import fr.geoventure.geontm.blocks.generic.BlockSkeletonHolder.TileEntitySkeletonHolder;
import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geontm.client.render.entity.FramedItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.CameraInfo;
import fr.geoventure.geocore.render.CompatItemRenderState;
import fr.geoventure.geocore.render.CompatRenderState;
import fr.geoventure.geocore.render.SubmitSink;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.GemBER;
import fr.geoventure.geocore.render.gem.Surface;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

/**
 * 1.7 {@code RenderSkeletonHolder}: the {@code Holder1} group of {@code skeleton_holder.obj} in the skeleton
 * particle texture, culled, turned by the block's facing (north 180, west 270, south 0, east 90 degrees), and the
 * held stack across it: turned a further quarter, a flat item at 1.5 scale, lifted 1/8, as a framed item.
 */
public class RenderSkeletonHolder extends GemBER<TileEntitySkeletonHolder, RenderSkeletonHolder.State> {

    public static final class State extends CompatRenderState {
        public final CompatItemRenderState item = new CompatItemRenderState();
        float yaw;
        FramedItem.Arm arm = FramedItem.Arm.SPRITE;
    }

    private final WavefrontObj model = WavefrontObj.load(Ids.of("geontm", "models/blocks/skeleton_holder.obj"));
    private final Surface skin = Surface.cutoutCull("geontm:textures/particle/skeleton.png");

    @Override
    protected void setup(Gem.Model<State> declaration) {
        declaration.part(model.part("Holder1"), skin)
                .translate(0.5F, 0F, 0.5F)
                .angle(Gem.Axes.Y, s -> s.yaw);
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    static float yaw(Direction facing) {
        return switch (facing) {
            case NORTH -> 180F;
            case WEST -> 270F;
            case EAST -> 90F;
            default -> 0F;
        };
    }

    @Override
    public void extract(TileEntitySkeletonHolder holder, State state, float partialTicks, Vec3 cameraPosition) {
        state.yaw = yaw(holder.getBlockState().getValue(BlockSkeletonHolder.FACING));
        if (holder.getItem().isEmpty()) state.item.clear();
        else state.arm = FramedItem.resolveArm(state.item, holder.getItem(), holder.getLevel());
    }

    @Override
    protected void submitUndeclared(State state, PoseStack pose, SubmitSink sink, CameraInfo camera) {
        if (state.item.isEmpty()) return;
        pose.pushPose();
        pose.translate(0.5, 0, 0.5);
        pose.mulPose(Axis.YP.rotationDegrees(state.yaw));
        pose.mulPose(Axis.YP.rotationDegrees(90F));
        if (!state.arm.block()) pose.scale(1.5F, 1.5F, 1.5F);
        pose.translate(0, 0.125, 0);
        if (state.arm.block()) {
            FramedItem.framedBlockPrefix(pose);
        } else {
            FramedItem.framedFlatArm(pose, state.arm);
        }
        FramedItem.draw(pose, state.item, sink, state.lightCoords);
        pose.popPose();
    }
}
