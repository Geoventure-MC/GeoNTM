package fr.geoventure.geontm.client.render;

import fr.geoventure.geocore.render.CompatRenderState;
import net.minecraft.core.Direction;

/** Extracted six-way pose for the redstone fan. */
public final class FanRenderState extends CompatRenderState {
    public Direction facing = Direction.NORTH;
    public float rotation;
}
