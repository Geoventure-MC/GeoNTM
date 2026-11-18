package fr.geoventure.geontm.client.render;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.blocks.bomb.NukeBombBlock;
import fr.geoventure.geontm.tileentity.bomb.TileEntityNukeBalefire;
import fr.geoventure.geontm.client.model.WavefrontObj;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.CameraInfo;
import fr.geoventure.geocore.render.CompatRenderState;
import fr.geoventure.geocore.render.SubmitSink;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.GemBER;
import fr.geoventure.geocore.render.gem.Surface;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import fr.geoventure.geontm.blocks.bomb.NukeBalefire;

/**
 * 1.7 {@code RenderNukeFstbmb}: the two-part bomb OBJ (cull disabled) and, while loaded, the red countdown
 * printed on the casing. The classic balefire glint pass over the "Balefire" part has no GeoCore seam yet and
 * is deferred like the Fat Man's.
 */
public class RenderNukeFstbmb extends GemBER<TileEntityNukeBalefire, RenderNukeFstbmb.State> {

    private static final WavefrontObj MODEL = WavefrontObj.load(Ids.of(Constants.MOD_ID, "models/bombs/fstbmb.obj"));
    private static final ResourceLocation TEXTURE = Ids.of(Constants.MOD_ID, "textures/models/bombs/fstbmb.png");
    /**
     * Legacy facing 2..5 in the port's numbering. 1.7 RenderNukeFstbmb: meta 2 -> 90, 4 -> 180, 3 -> 270,
     * 5 -> 0. Unlike the BlockContainer bombs, NukeBalefire was a rotatable BlockMachineBase whose 1.7
     * placement map (S -> 2, W -> 5, N -> 3, E -> 4) already equals the port's numbering, so no remap.
     */
    private static final float[] YAWS = {90F, 270F, 180F, 0F};

    private final Font font;

    public RenderNukeFstbmb() {
        font = Minecraft.getInstance().font;
    }

    @Override
    protected void setup(Gem.Model<State> declaration) {
        Surface surface = Surface.cutoutCull(TEXTURE.toString());
        Gem.Part<State> bomb = declaration.offScreen().viewDistance(256).joint()
                .translate(0.5F, 0F, 0.5F)
                .angle(Gem.Axes.Y, state -> state.yaw);
        bomb.child(MODEL.parts(Set.of("Body"), true), surface);
        bomb.child(MODEL.parts(Set.of("Balefire"), true), surface);
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extract(TileEntityNukeBalefire bomb, State state, float partialTicks, Vec3 cameraPosition) {
        BlockState bs = bomb.getBlockState();
        int facing = bs.hasProperty(NukeBombBlock.FACING) ? bs.getValue(NukeBombBlock.FACING) : 2;
        state.yaw = facing >= 2 && facing <= 5 ? YAWS[facing - 2] : YAWS[0];
        state.loaded = bomb.loaded;
        state.timer = state.loaded ? Component.literal(bomb.getMinutes() + ":" + bomb.getSeconds()).getVisualOrderText() : null;
    }

    @Override
    protected void submitUndeclared(State state, PoseStack pose, SubmitSink sink, CameraInfo camera) {
        if (!state.loaded || state.timer == null) return;
        float f3 = 0.04F;
        pose.pushPose();
        pose.translate(0.5D, 0D, 0.5D);
        pose.mulPose(Axis.YP.rotationDegrees(state.yaw));
        pose.translate(0.815F, 0.9275F, 0.5F);
        pose.scale(f3, -f3, f3);
        pose.mulPose(Axis.YP.rotationDegrees(90F));
        pose.translate(0F, 1F, 0F);
        // 1.7 disabled the depth mask for the digits; see-through text is the polygon-offset-free equivalent.
        sink.submitText(pose, font, 0, 0, state.timer, false, Font.DisplayMode.SEE_THROUGH, 0xF000F0, 0xFFFF0000, 0);
        pose.popPose();
    }

    public static final class State extends CompatRenderState {
        float yaw;
        boolean loaded;
        FormattedCharSequence timer;
    }
}
