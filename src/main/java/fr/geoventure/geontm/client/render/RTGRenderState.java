package fr.geoventure.geontm.client.render;

import fr.geoventure.geocore.render.CompatRenderState;

/** Render state for all three RTG bodies and their four horizontal cable connectors. */
public final class RTGRenderState extends CompatRenderState {
    public boolean polonium;
    public boolean grey;
    public boolean positiveX;
    public boolean negativeX;
    public boolean negativeZ;
    public boolean positiveZ;
}
