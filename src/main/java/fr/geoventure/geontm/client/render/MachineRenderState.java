package fr.geoventure.geontm.client.render;

import fr.geoventure.geocore.render.CompatRenderState;


/** Render state for {@link MachineObjRenderer}: the core's facing (yaw) and whether its frame shows. */
public class MachineRenderState extends CompatRenderState {
    public float yaw;
    /** Whether to draw the conditional "Frame" part: true when a block sits above the machine. */
    public boolean showFrame = true;
    /** Burning state used by the destroyed ZIRNOX renderer's legacy flame overlay. */
    public boolean burningWreck;
    public long wreckGameTime;
}
