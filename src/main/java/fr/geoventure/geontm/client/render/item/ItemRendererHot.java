package fr.geoventure.geontm.client.render.item;

import fr.geoventure.geontm.items.special.ItemHot;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.geoventure.geocore.render.CompatRenderTypes;
import fr.geoventure.geocore.render.ItemSprites;
import fr.geoventure.geocore.render.SubmitSink;
import fr.geoventure.geocore.render.Vertexes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import static fr.geoventure.geontm.client.render.item.CustomItemRenderers.*;

/**
 * 1.7 {@code ItemRendererHot}, inventory only: the ordinary icon, then the item's {@code _hot} icon over it at
 * the stack's heat as alpha ({@code glColor4f(1, 1, 1, h)}, blended, lighting off). Everywhere else the item is
 * its plain sprite model; in the inventory the body draws that sprite and then the glowing sheet a hair in front of
 * its face.
 */
final class ItemRendererHot implements CustomItemRenderer {

    /** The front face of an {@code item/generated} sprite, and a hair in front of it. */
    private static final float FRONT = 8.5F / 16F + 0.001F;

    @Override
    public boolean handles(ItemStack stack, Type type) {
        return type == Type.INVENTORY;
    }

    @Override
    public void render(ItemStack stack, Type type, PoseStack pose, SubmitSink sink, int light, int overlay,
                       @Nullable LivingEntity owner) {
        // the ordinary icon first: the glow is blended over it (see Core ItemSprites)
        ItemSprites.submit(pose, sink, tex("textures/items/" + id(stack) + ".png"), light, overlay);
        double h = ItemHot.getHeat(stack);
        if (h <= 0) return;
        ResourceLocation hot = tex("textures/items/" + id(stack) + "_hot.png");
        int color = (int) Math.round(Math.min(1D, h) * 255D) << 24 | 0xFFFFFF;
        int o = OverlayTexture.NO_OVERLAY;
        sink.submitCustomGeometry(pose, CompatRenderTypes.entityTranslucent(hot), (p, buffer) -> {
            Vertexes.vertex(buffer, p, 0F, 0F, FRONT, color, 0F, 1F, o, light, 0F, 0F, 1F);
            Vertexes.vertex(buffer, p, 1F, 0F, FRONT, color, 1F, 1F, o, light, 0F, 0F, 1F);
            Vertexes.vertex(buffer, p, 1F, 1F, FRONT, color, 1F, 0F, o, light, 0F, 0F, 1F);
            Vertexes.vertex(buffer, p, 0F, 1F, FRONT, color, 0F, 0F, o, light, 0F, 0F, 1F);
        });
    }
}
