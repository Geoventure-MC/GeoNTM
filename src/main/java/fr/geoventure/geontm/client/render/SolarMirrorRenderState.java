package fr.geoventure.geontm.client.render;

import fr.geoventure.geocore.render.CompatRenderState;

/** Extracted mirror target, aiming, and active-beam state. */
public final class SolarMirrorRenderState extends CompatRenderState {
    public int targetX;
    public int targetY;
    public int targetZ;
    public boolean isOn;
    public boolean targetAbove;
    public float distance;
    public float yaw;
    public float pitch;
}
