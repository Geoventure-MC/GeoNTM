package fr.geoventure.geontm.client.render;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.blocks.machine.IntakeFanDirections;
import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineIntake;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.GemBER;
import fr.geoventure.geocore.render.gem.Surface;
import net.minecraft.world.phys.Vec3;

/** Animated OBJ declaration for the two-by-two legacy air intake. */
public final class IntakeRenderer extends GemBER<TileEntityMachineIntake, IntakeRenderState> {

    private final WavefrontObj model = WavefrontObj.load(
            Ids.of(Constants.MOD_ID, "models/machines/intake.obj"));
    private final Surface surface = Surface.cutoutNoCull(
            Constants.MOD_ID + ":textures/models/machines/intake.png");

    @Override
    protected void setup(Gem.Model<IntakeRenderState> declaration) {
        Gem.Part<IntakeRenderState> machine = declaration.offScreen().viewDistance(256)
                .joint()
                .translate(0.5F, 0F, 0.5F)
                .angle(Gem.Axes.Y, state -> state.yaw)
                .translate(-0.5F, 0F, 0.5F);

        machine.child(model.part("Base"), surface);
        machine.child(model.part("Fan"), surface)
                .angle(Gem.Axes.Y, state -> -state.rotation);
    }

    @Override
    public IntakeRenderState createRenderState() {
        return new IntakeRenderState();
    }

    @Override
    public void extract(TileEntityMachineIntake be, IntakeRenderState state, float partialTicks,
                        Vec3 cameraPosition) {
        state.yaw = IntakeFanDirections.intakeYaw(be.getFacing());
        state.rotation = be.prevFan + (be.fan - be.prevFan) * partialTicks;
    }
}
