package fr.geoventure.geontm.client.render;

import fr.geoventure.geocore.render.CompatRenderState;


/** Render state for the firebox family: facing yaw, whether the fire is burning, and the interpolated
 * door swing angle (0 closed .. 135 open). */
public class FireboxRenderState extends CompatRenderState {
    public float yaw;
    public boolean burning;
    public float doorAngle;
}
