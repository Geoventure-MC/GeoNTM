package fr.geoventure.geontm.client.render;

import fr.geoventure.geocore.render.CompatRenderState;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;

/** Render state for {@link DecoObjRenderer}: which deco block (picks the model config) and its facing yaw. */
public class DecoRenderState extends CompatRenderState {
    public @Nullable Block block;
    public float yaw;
}
