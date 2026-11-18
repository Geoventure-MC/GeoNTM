package fr.geoventure.geontm.client.render;

import fr.geoventure.geocore.render.CompatItemRenderState;
import fr.geoventure.geocore.render.CompatRenderState;

/** Static Arc Welder render state plus the old floating recipe output. */
public class ArcWelderRenderState extends CompatRenderState {
    public float yaw;
    public final CompatItemRenderState outputItem = new CompatItemRenderState();
    public boolean showItem;
}
