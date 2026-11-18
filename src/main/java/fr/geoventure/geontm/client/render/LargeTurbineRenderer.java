package fr.geoventure.geontm.client.render;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geontm.tileentity.MachineCore;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineLargeTurbine;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.GemBER;
import fr.geoventure.geocore.render.gem.Surface;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

/** Large steam turbine: a static housing and the blade bank turning inside it, both double-sided. */
public final class LargeTurbineRenderer
        extends GemBER<TileEntityMachineLargeTurbine, LargeTurbineRenderState> {

    private final WavefrontObj model = WavefrontObj.load(
            Ids.of(Constants.MOD_ID, "models/machines/turbine.obj"));
    private final Surface body = Surface.cutout(
            Constants.MOD_ID + ":textures/models/machines/turbine.png");
    private final Surface blades = Surface.cutout(
            Constants.MOD_ID + ":textures/models/machines/turbofan_blades.png");

    @Override
    protected void setup(Gem.Model<LargeTurbineRenderState> declaration) {
        Gem.Part<LargeTurbineRenderState> machine = declaration.offScreen().viewDistance(256)
                .joint()
                .translate(0.5F, 0F, 0.5F)
                .angle(Gem.Axes.Y, state -> state.yaw)
                .translate(0F, 0F, -1F);

        machine.child(model.parts(Set.of("Body"), true), body);
        machine.child(model.parts(Set.of("Blades"), true), blades)
                .spin(Gem.Axes.Z, 0F, 1F, 0F, state -> state.rotor);
    }

    @Override
    public LargeTurbineRenderState createRenderState() {
        return new LargeTurbineRenderState();
    }

    @Override
    public void extract(TileEntityMachineLargeTurbine be, LargeTurbineRenderState state,
                        float partialTicks, Vec3 cameraPosition) {
        BlockState blockState = be.getBlockState();
        int meta = MachineCore.facingOrdinal(blockState, 2);
        state.yaw = 180F + switch (ForgeDirection.getOrientation(meta)) {
            case WEST -> 90F;
            case SOUTH -> 180F;
            case EAST -> 270F;
            default -> 0F;
        };
        state.rotor = be.getRotor(partialTicks);
    }
}
