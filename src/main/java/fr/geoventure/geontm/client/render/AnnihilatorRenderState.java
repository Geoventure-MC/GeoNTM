package fr.geoventure.geontm.client.render;

import fr.geoventure.geocore.render.CompatRenderState;

/** Render snapshot for the annihilator: facing yaw, the roller spin and the belt scroll. */
public final class AnnihilatorRenderState extends CompatRenderState {
    public float yaw;
    public float roller;
    public float belt;
}
