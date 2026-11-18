package fr.geoventure.geontm.client.render;

import fr.geoventure.geontm.entity.effect.EntityNukeTorex;
import fr.geoventure.geocore.render.CompatEntityRenderState;

/**
 * Render state for {@link TorexRenderer}. Unlike a normal entity render state (which snapshots all data
 * into POJO fields), this holds a live <b>reference</b> to the Torex entity plus the partial-tick: the
 * cloudlet simulation and the renderer both run on the client thread, so copying up to 20,000 cloudlets
 * per frame would be pure waste. The 1.12.2 renderer read the entity's list directly for the same reason.
 */
public class TorexRenderState extends CompatEntityRenderState {

    public EntityNukeTorex torex;
    public float partialTicks;
}
