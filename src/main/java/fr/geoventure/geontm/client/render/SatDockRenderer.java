package fr.geoventure.geontm.client.render;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineSatDock;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.CompatRenderState;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.GemBER;
import fr.geoventure.geocore.render.gem.Surface;
import net.minecraft.world.phys.Vec3;

/**
 * 1.7 {@code RenderDecoBlock} for {@code sat_dock}: {@code T(0.5, 1.5, 0.5) Rz(180)}, then the pad's own
 * {@code Rz(180) T(0, -1.5, 0)}, which cancel to {@code T(0.5, 0, 0.5)}; {@code satDock} ({@code cargo_pad.obj}) lit, culled.
 */
public final class SatDockRenderer extends GemBER<TileEntityMachineSatDock, CompatRenderState> {

    private final WavefrontObj model = WavefrontObj.load(Ids.of(Constants.MOD_ID, "models/machines/cargo_pad.obj"));
    private final Surface skin = Surface.cutoutCull(Constants.MOD_ID + ":textures/models/machines/cargo_pad.png");

    @Override
    protected void setup(Gem.Model<CompatRenderState> declaration) {
        declaration.offScreen().viewDistance(256).joint()
                .translate(0.5F, 0F, 0.5F)
                .child(model.all(), skin);
    }

    @Override
    public CompatRenderState createRenderState() {
        return new CompatRenderState();
    }

    @Override
    public void extract(TileEntityMachineSatDock dock, CompatRenderState state, float partialTicks, Vec3 cameraPosition) {
    }
}
