package fr.geoventure.geontm.client.render;

import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geontm.tileentity.machine.oil.TileEntityMachineRefinery;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.CompatRenderState;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.GemBER;
import fr.geoventure.geocore.render.gem.Surface;
import java.util.Set;
import net.minecraft.world.phys.Vec3;

/**
 * 1.7 {@code RenderRefinery}: the refinery OBJ turned 180 degrees whatever its facing, culling off - or, once it has
 * exploded ({@code hasExploded}), {@code refinery_exploded.obj} on the same texture.
 */
public class RefineryRenderer extends GemBER<TileEntityMachineRefinery, RefineryRenderer.State> {

    public static class State extends CompatRenderState {
        public boolean exploded;
    }

    private final WavefrontObj intact = WavefrontObj.load(Ids.of("geontm", "models/machines/refinery.obj"));
    private final WavefrontObj torn = WavefrontObj.load(Ids.of("geontm", "models/machines/refinery_exploded.obj"));
    private final Surface skin = Surface.cutoutNoCull(Ids.of("geontm", "textures/models/machines/refinery.png").toString());

    @Override
    protected void setup(Gem.Model<State> declaration) {
        declaration.offScreen().viewDistance(256);
        declaration.part(intact.allExcept(Set.of(), false), skin)
                .translate(0.5F, 0F, 0.5F).rotate(Gem.Axes.Y, 180F).visibleIf(state -> !state.exploded);
        declaration.part(torn.allExcept(Set.of(), false), skin)
                .translate(0.5F, 0F, 0.5F).rotate(Gem.Axes.Y, 180F).visibleIf(state -> state.exploded);
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extract(TileEntityMachineRefinery be, State state, float partialTicks, Vec3 cameraPosition) {
        state.exploded = be.hasExploded;
    }
}
