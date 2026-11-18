package fr.geoventure.geontm.client.render;

import fr.geoventure.geocore.render.CompatRenderState;

/** Render state for the drive enclosure: facing and one light state per slot. */
public final class TapeDriveRenderState extends CompatRenderState {
    public float yaw;
    public final byte[] tapes = new byte[12];
}
