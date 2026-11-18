package fr.geoventure.geontm.client.render;

import fr.geoventure.geontm.client.render.MissileModels.MissileModel;
import fr.geoventure.geocore.render.CompatRenderState;
import org.jspecify.annotations.Nullable;

/** The pad's facing, and the body of whatever missile is loaded (null when the pad is empty). */
public class LaunchPadRenderState extends CompatRenderState {
    public float yaw;
    public @Nullable MissileModel missile;
}
