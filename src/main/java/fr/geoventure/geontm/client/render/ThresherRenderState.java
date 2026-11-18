package fr.geoventure.geontm.client.render;

import fr.geoventure.geocore.render.CompatRenderState;

/** Render snapshot for the thresher's folded arm, wheel spin and engine jitter. */
public final class ThresherRenderState extends CompatRenderState {
    public float yaw;
    public float angle;
    public float spin;
    public double engine;
}
