package fr.geoventure.geontm.client.render;

import fr.geoventure.geocore.render.CompatRenderState;


public class CompressorRenderState extends CompatRenderState {
    public float yaw;
    /** Interpolated pump stroke (0-1); the Pump part translates by {@code lift·3 − 3}. */
    public float lift;
    /** Interpolated fan angle (degrees, about the X axis at y=1.5). */
    public float fan;
}
