package fr.geoventure.geontm.client.render;

import fr.geoventure.geocore.render.CompatRenderState;

public final class PumpRenderState extends CompatRenderState {
    public float yaw;
    public float rotation;
    /** Vertical travel of the piston and the arms riding on it, in blocks. */
    public float lift;
    /** Swing of the arms about the crank pivot, in degrees. */
    public float armAngle;
}
