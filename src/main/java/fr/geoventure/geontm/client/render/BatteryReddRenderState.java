package fr.geoventure.geontm.client.render;

import fr.geoventure.geocore.render.CompatRenderState;

/** Extracted state for the FEnSU's flywheel, plasma glow, and arcs. */
public final class BatteryReddRenderState extends CompatRenderState {
    public float yaw;
    public float rotation;
    public float speed;
    public long gameTime;
    public long gameMillis;
    public boolean sparkleNearby;
}
