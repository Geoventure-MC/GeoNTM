package fr.geoventure.geontm.client.render;

import fr.geoventure.geontm.Constants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.GemBER;
import fr.geoventure.geocore.render.gem.Surface;
import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityRBMKControl;
import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityRBMKControlAuto;
import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityRBMKControlManual;
import fr.geoventure.geontm.tileentity.machine.rbmk.RBMKDials;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.CompatRenderState;
import fr.geoventure.geocore.render.Renderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/** Sliding Lid mesh for manual, automatic, and dyeable control columns. */
public final class RBMKControlRodRenderer<T extends BlockEntityRBMKControl> extends GemBER<T, RBMKControlRodRenderer.State> {

    private static final WavefrontObj RODS = WavefrontObj.load(Ids.of(Constants.MOD_ID, "models/rbmk/rbmk_rods.obj"));
    private static final Map<String, Surface> SKINS = new ConcurrentHashMap<>();

    private final String sheet;

    public RBMKControlRodRenderer(String sheet) {
        this.sheet = sheet;
    }

    /**
     * The lid is one mesh in as many skins as there are rod colours, and it rides on the rod, so its
     * height is the column's plus however far the rod is withdrawn.
     */
    @Override
    protected void setup(Gem.Model<State> declaration) {
        declaration.offScreen().viewDistance(256)
                .part(RODS.part("Lid"), skin(sheet))
                .skin(state -> skin(state.texture))
                .light(state -> state.lidLight)
                .translate(0.5F, 0F, 0.5F)
                .slide(Gem.Axes.Y, state -> (float) (state.height + state.level));
    }

    private static Surface skin(String name) {
        return SKINS.computeIfAbsent(name, id ->
                Surface.cutout(Ids.of(Constants.MOD_ID, "textures/blocks/rbmk/" + id + ".png").toString()));
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    /**
     * The old TESR sampled at {@code pos.up(offset + 1)}: one above the shaft mouth, where the sky
     * actually reaches. Sampling lower lands in the occluded lattice and renders the lid near-black.
     */
    @Override
    public void extract(T be, State state, float partial, Vec3 camera) {
        state.level = Mth.lerp(partial, be.lastLevel, be.level);
        state.height = RBMKDials.getColumnHeight(be.getLevel());
        state.texture = be instanceof BlockEntityRBMKControlAuto ? "rbmk_control_auto" : sheet;
        if (be instanceof BlockEntityRBMKControlManual manual && manual.color != null) {
            state.texture = "rbmk_control_" + manual.color.texture();
        }
        state.lidLight = be.getLevel() == null ? state.lightCoords
                : Renderers.lightAt(be.getLevel(), be.getBlockPos().above(state.height + 1));
    }

    public static final class State extends CompatRenderState {
        double level;
        int height;
        int lidLight;
        String texture = "rbmk_control";
    }
}
