package fr.geoventure.geontm.client.render;

import fr.geoventure.geocore.render.CompatRenderState;

/** Render state for {@link FusionSpinRenderer}: the machine's facing plus its rotor angle. */
public class FusionSpinRenderState extends CompatRenderState {
    public float yaw;
    /** Interpolated rotation of the spinning part, in degrees about the machine's own axis. */
    public float spin;
}
