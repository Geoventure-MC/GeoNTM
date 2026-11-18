package fr.geoventure.geontm.client.render;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geontm.blocks.ModEnergyNetBlocks;
import fr.geoventure.geontm.tileentity.network.TileEntityPylon;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.Surface;
import net.minecraft.world.phys.Vec3;

/** 1.7 {@code RenderPylon}: the {@code Pylon} or {@code Pylon_steel} part of {@code pylon.obj}, drawn both-sided. */
public class RenderPylon extends RenderPylonBase<TileEntityPylon> {

    private final WavefrontObj model = WavefrontObj.load(Ids.of(Constants.MOD_ID, "models/network/pylon.obj"));
    private final Surface wood = Surface.cutoutNoCull(Constants.MOD_ID + ":textures/models/network/pylon.png");
    private final Surface steel = Surface.cutoutNoCull(Constants.MOD_ID + ":textures/models/network/pylon_steel.png");

    @Override
    protected void setup(Gem.Model<PylonRenderState> declaration) {
        declaration.offScreen().viewDistance(256);
        declaration.part(model.part("Pylon"), wood).visibleIf(s -> !s.steel).translate(0.5F, 0F, 0.5F);
        declaration.part(model.part("Pylon_steel"), steel).visibleIf(s -> s.steel).translate(0.5F, 0F, 0.5F);
    }

    @Override
    public void extract(TileEntityPylon be, PylonRenderState state, float partialTicks, Vec3 cameraPosition) {
        state.steel = be.getBlockState().is(ModEnergyNetBlocks.red_pylon_steel.get());
        extractWires(be, state);
    }
}
