package fr.geoventure.geontm.client.render;

import fr.geoventure.geocore.render.CompatRenderState;


public class HeatBoilerRenderState extends CompatRenderState {
    public float yaw;
    public boolean hasExploded;
    /** Pulsing squash factor when the output tank is over 90% (old near-burst wobble); 0 otherwise. */
    public float sine;
}
