package fr.geoventure.geontm.client.render;

import fr.geoventure.geocore.render.CompatRenderState;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

/** Render state for {@link StructPreviewRenderer}: the cells of the pattern still missing. */
public class StructPreviewRenderState extends CompatRenderState {

    /** One ghost cube: offset from the struct core plus the textures it draws with. */
    public record Ghost(BlockPos offset, ResourceLocation top,
                        ResourceLocation side) {}

    public final List<Ghost> missing = new ArrayList<>();
}
