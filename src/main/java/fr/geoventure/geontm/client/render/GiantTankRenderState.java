package fr.geoventure.geontm.client.render;

import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geocore.render.CompatRenderState;

/** Extracted state shared by the two legacy giant-tank renderers. */
public final class GiantTankRenderState extends CompatRenderState {
    public FluidType fluidType = Fluids.NONE;
    public int fill;
    public int maxFill;
    public long gameTime;
    public float partialTick;
    public float yaw;
    public boolean tilted;
}
