package fr.geoventure.geontm.client.render;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.tileentity.machine.TileEntityChungus;
import fr.geoventure.geontm.tileentity.MachineCore;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.GemBER;
import fr.geoventure.geocore.render.gem.Surface;
import net.minecraft.world.phys.Vec3;

/**
 * 1.7 {@code RenderChungus}: translate to the block centre, {@code glRotatef(90, Y)} plus the facing turn,
 * {@code glTranslated(0, 0, -3)}; the body, the lever tilted about X at z=4.5 by the steam grade, and the
 * blades spun about −Z at y=2.5.
 */
public final class RenderChungus extends GemBER<TileEntityChungus, ChungusRenderState> {

    /** An addon's wrecked turbine ({@link DamagedView}, ADDON-API-INVENTORY C-23); null in plain GEONTM. */
    public static volatile @org.jspecify.annotations.Nullable DamagedView DAMAGED;

    /** The view this renderer was declared with (read once, in {@link #setup}). */
    private @org.jspecify.annotations.Nullable DamagedView damagedView;

    private final WavefrontObj model = WavefrontObj.load(
            Ids.of(Constants.MOD_ID, "models/machines/chungus.obj"));
    private final Surface surface = Surface.cutoutCull(
            Constants.MOD_ID + ":textures/models/machines/chungus.png");

    @Override
    protected void setup(Gem.Model<ChungusRenderState> declaration) {
        Gem.Part<ChungusRenderState> machine = declaration.offScreen().viewDistance(256)
                .joint()
                .translate(0.5F, 0F, 0.5F)
                .rotate(Gem.Axes.Y, 90F)
                .angle(Gem.Axes.Y, state -> state.yaw)
                .translate(0F, 0F, -3F);

        Gem.Part<ChungusRenderState> intact = machine.joint().visibleIf(state -> !state.damaged);
        intact.child(model.part("Body"), surface);
        intact.child(model.part("Lever"), surface)
                .spin(Gem.Axes.X, 0F, 0F, 4.5F, state -> state.lever);
        intact.child(model.part("Blades"), surface)
                .spin(Gem.Axes.Z, 0F, 2.5F, 0F, state -> -state.rotor);
        damagedView = DAMAGED;
        if (damagedView != null) {
            machine.joint().visibleIf(state -> state.damaged).rotate(Gem.Axes.Y, damagedView.extraYaw())
                    .child(WavefrontObj.load(Ids.parse(damagedView.model())).all(!damagedView.cull()),
                            damagedView.cull() ? Surface.cutoutCull(damagedView.texture()) : Surface.cutoutNoCull(damagedView.texture()));
        }
    }

    @Override
    public ChungusRenderState createRenderState() {
        return new ChungusRenderState();
    }

    @Override
    public void extract(TileEntityChungus be, ChungusRenderState state, float partialTicks, Vec3 cameraPosition) {
        int meta = MachineCore.facingOrdinal(be.getBlockState(), 2);
        state.yaw = switch (meta) {
            case 2 -> 90F;
            case 4 -> 180F;
            case 3 -> 270F;
            default -> 0F;
        };
        // 1.7: FluidType.ordinal() is its id, and STEAM is id 2.
        state.lever = 15F - (Fluids.legacyId(be.tanks[0].getTankType()) - Fluids.legacyId(Fluids.STEAM)) * 10F;
        state.rotor = be.getRotor(partialTicks);
        state.damaged = damagedView != null && damagedView.damaged().test(be);
    }
}
