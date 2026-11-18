package fr.geoventure.geontm.client.render;

import fr.geoventure.geocore.render.CompatEntityRenderState;

/** What {@link MissileRenderer} needs from a missile: where it is pointing. */
public class MissileRenderState extends CompatEntityRenderState {
    public float yaw;
    public float pitch;
}
