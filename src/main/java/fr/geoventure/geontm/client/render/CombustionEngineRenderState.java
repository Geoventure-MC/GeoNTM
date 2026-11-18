package fr.geoventure.geontm.client.render;

import fr.geoventure.geocore.render.CompatRenderState;

/** Render state for the combustion engine's facing, canister tint and interpolated hatch angle. */
public final class CombustionEngineRenderState extends CompatRenderState {
    public float yaw;
    public float doorAngle;
    public int canisterColor;
}
