package fr.geoventure.geontm.client.render;

import fr.geoventure.geocore.render.CompatRenderState;

/** Where the dish is pointing this frame, interpolated, and which way the machine was placed. */
public class SatLinkRenderState extends CompatRenderState {
    public float rot;
    public float lift;
    public float offsetX;
    public float offsetZ;
}
