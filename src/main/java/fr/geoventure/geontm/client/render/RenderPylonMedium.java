package fr.geoventure.geontm.client.render;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.blocks.network.PylonMedium;
import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geontm.tileentity.network.TileEntityPylonMedium;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.Surface;
import net.minecraft.world.phys.Vec3;

/** 1.7 {@code RenderPylonMedium}: {@code pylon_medium.obj}'s {@code Pylon} plus {@code Transformer} when fitted; old yaw map 2:180 4:270 3:0 5:90. */
public class RenderPylonMedium extends RenderPylonBase<TileEntityPylonMedium> {

    private final WavefrontObj model = WavefrontObj.load(Ids.of(Constants.MOD_ID, "models/network/pylon_medium.obj"));
    private final Surface wood = Surface.cutoutCull(Constants.MOD_ID + ":textures/models/network/pylon_medium.png");
    private final Surface steel = Surface.cutoutCull(Constants.MOD_ID + ":textures/models/network/pylon_medium_steel.png");

    @Override
    protected void setup(Gem.Model<PylonRenderState> declaration) {
        declaration.offScreen().viewDistance(256);
        Gem.Part<PylonRenderState> root = declaration.joint().translate(0.5F, 0F, 0.5F).angle(Gem.Axes.Y, s -> s.yaw);
        root.child(model.part("Pylon"), wood).skin(s -> s.steel ? steel : wood);
        root.child(model.part("Transformer"), wood).skin(s -> s.steel ? steel : wood).visibleIf(s -> s.transformer);
    }

    @Override
    public void extract(TileEntityPylonMedium be, PylonRenderState state, float partialTicks, Vec3 cameraPosition) {
        state.steel = be.getBlockState().getBlock() instanceof PylonMedium p && p.steel;
        state.transformer = be.hasTransformer();
        state.yaw = switch (be.facingMeta()) {
            case 2 -> 180F;
            case 4 -> 270F;
            case 5 -> 90F;
            default -> 0F;
        };
        extractWires(be, state);
    }
}
