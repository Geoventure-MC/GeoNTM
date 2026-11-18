package fr.geoventure.geontm.client.render;

import fr.geoventure.geontm.tileentity.DoorDecl;
import fr.geoventure.geocore.render.CompatRenderState;
import fr.geoventure.geocore.render.anim.BusAnimation;
import fr.geoventure.geocore.render.anim.BusAnimationSequence;
import org.jspecify.annotations.Nullable;

/** What {@link RenderDoorGeneric} copies out of a door tile each frame. */
public class DoorRenderState extends CompatRenderState {

    public @Nullable DoorDecl decl;
    public byte state;
    public int skinIndex;
    /** ForgeDirection ordinal of the core's facing (N=2, S=3, W=4, E=5). */
    public int facing;
    public long animStartTime;
    public long now;
    public @Nullable BusAnimation animation;
    /** The generic OBJ path's playhead in ticks, from the wall clock as the original computed it. */
    public float openTicks;

    private static final double[] REST = {
            0, 0, 0, // position
            0, 0, 0, // rotation
            1, 1, 1, // scale
            0, 0, 0, // offset
            0, 1, 2, // XYZ order
    };

    /** 1.7 {@code IRenderDoors.getRelevantTransformation}: one bus of the running animation, or rest. */
    public double[] bus(String name) {
        if (animation != null) {
            int millis = (int) (now - animStartTime);
            BusAnimationSequence seq = animation.getBus(name);
            if (seq != null) {
                double[] trans = seq.getTransformation(millis);
                if (trans != null) return trans;
            }
        }
        return REST;
    }
}
