package fr.geoventure.geontm.client.render;

import fr.geoventure.geocore.render.CompatRenderState;

/** Render snapshot for the rotary furnace: facing yaw, the piston stroke, and the pour stream. */
public final class RotaryFurnaceRenderState extends CompatRenderState {
    public float yaw;
    public float piston;
    public int dirOrdinal;
    public int pourColor = -1;
    public float pourLen;
}
