package fr.geoventure.geontm.client.render;

import fr.geoventure.geocore.render.CompatRenderState;


/** Render state for the PUREX plant: facing, upper-frame visibility and the interpolated animation counter. */
public class PurexRenderState extends CompatRenderState {
    public float yaw;
    public boolean showFrame;
    public double anim;
}
