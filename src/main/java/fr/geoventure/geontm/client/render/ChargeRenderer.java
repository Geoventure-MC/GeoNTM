package fr.geoventure.geontm.client.render;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geontm.blocks.bomb.BlockChargeBase;
import fr.geoventure.geontm.blocks.bomb.BlockChargeC4;
import fr.geoventure.geontm.blocks.bomb.BlockChargeDynamite;
import fr.geoventure.geontm.blocks.bomb.BlockChargeMiner;
import fr.geoventure.geontm.blocks.bomb.BlockChargeSemtex;
import fr.geoventure.geontm.tileentity.bomb.TileEntityCharge;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.CameraInfo;
import fr.geoventure.geocore.render.CompatRenderState;
import fr.geoventure.geocore.render.SubmitSink;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.GemBER;
import fr.geoventure.geocore.render.gem.Surface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * The timed charges: 1.7 drew the OBJ through {@code RenderBlockRotated} (the block icon as the OBJ's
 * texture) and the green {@code mm:ss} timer through {@code RenderExplosiveCharge}. Both use the same
 * face-dependent rotation table, transcribed here as a (yaw, roll) pair per facing. C-4 and Semtex
 * share {@code charge_c4.obj}; dynamite and the miner share {@code charge_dynamite.obj}.
 */
public final class ChargeRenderer extends GemBER<TileEntityCharge, ChargeRenderer.State> {

    private static final WavefrontObj MODEL_C4 = WavefrontObj.load(Ids.of(Constants.MOD_ID, "models/blocks/charge_c4.obj"));
    private static final WavefrontObj MODEL_DYN = WavefrontObj.load(Ids.of(Constants.MOD_ID, "models/blocks/charge_dynamite.obj"));

    private static final Surface TEX_C4 = Surface.cutout("geontm:textures/blocks/charge_c4.png");
    private static final Surface TEX_SEMTEX = Surface.cutout("geontm:textures/blocks/charge_semtex.png");
    private static final Surface TEX_DYN = Surface.cutout("geontm:textures/blocks/charge_dynamite.png");
    private static final Surface TEX_MINER = Surface.cutout("geontm:textures/blocks/charge_miner.png");

    private final Font font = Minecraft.getInstance().font;

    @Override
    protected void setup(Gem.Model<State> declaration) {
        Gem.Part<State> root = declaration.joint()
                .translate(0.5F, 0.5F, 0.5F)
                .angle(Gem.Axes.Y, state -> state.yaw)
                .angle(Gem.Axes.Z, state -> state.roll);

        root.child(MODEL_C4.all(true), TEX_C4).visibleIf(state -> state.kind == 0);
        root.child(MODEL_C4.all(true), TEX_SEMTEX).visibleIf(state -> state.kind == 1);
        root.child(MODEL_DYN.all(true), TEX_DYN).visibleIf(state -> state.kind == 2);
        root.child(MODEL_DYN.all(true), TEX_MINER).visibleIf(state -> state.kind == 3);
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extract(TileEntityCharge charge, State state, float partialTicks, Vec3 cameraPosition) {
        BlockState bs = charge.getBlockState();
        Block block = bs.getBlock();
        state.kind = block instanceof BlockChargeC4 ? 0 : block instanceof BlockChargeSemtex ? 1
                : block instanceof BlockChargeDynamite ? 2 : block instanceof BlockChargeMiner ? 3 : 0;
        Direction facing = bs.hasProperty(BlockChargeBase.FACING) ? bs.getValue(BlockChargeBase.FACING) : Direction.UP;
        // 1.7 metadata 0..5: 0 -> rotZ 180; 1 -> none; 2 -> rotY 90, rotZ -90; 3 -> rotY -90, rotZ -90;
        // 4 -> rotY 180, rotZ -90; 5 -> rotZ -90.
        switch (facing) {
            case DOWN -> { state.yaw = 0F; state.roll = 180F; }
            case UP -> { state.yaw = 0F; state.roll = 0F; }
            case NORTH -> { state.yaw = 90F; state.roll = -90F; }
            case SOUTH -> { state.yaw = -90F; state.roll = -90F; }
            case WEST -> { state.yaw = 180F; state.roll = -90F; }
            case EAST -> { state.yaw = 0F; state.roll = -90F; }
        }
        state.text = Component.literal(charge.getMinutes() + ":" + charge.getSeconds()).getVisualOrderText();
    }

    /** The timer digits are glyph quads the font atlas builds per frame. */
    @Override
    protected void submitUndeclared(State state, PoseStack pose, SubmitSink sink, CameraInfo camera) {
        pose.pushPose();
        pose.translate(0.5D, 0.5D, 0.5D);
        pose.mulPose(Axis.YP.rotationDegrees(state.yaw));
        pose.mulPose(Axis.ZP.rotationDegrees(state.roll));
        float f3 = 0.0125F;
        pose.translate(-0.05F, 0.315F - 0.5F, 0.15F);
        pose.scale(f3, -f3, f3);
        pose.mulPose(Axis.YP.rotationDegrees(90F));
        pose.mulPose(Axis.XP.rotationDegrees(90F));
        // 1.7 drew with depth writes off but depth-tested; NORMAL keeps the test, full-bright keeps the LED look.
        sink.submitText(pose, font, 0F, 0F, state.text, false, Font.DisplayMode.NORMAL, 0xF000F0, 0xFF00FF00, 0);
        pose.popPose();
    }

    public static final class State extends CompatRenderState {
        int kind;
        float yaw;
        float roll;
        FormattedCharSequence text = FormattedCharSequence.EMPTY;
    }
}
