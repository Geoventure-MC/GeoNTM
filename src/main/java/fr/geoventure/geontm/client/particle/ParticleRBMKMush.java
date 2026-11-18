package fr.geoventure.geontm.client.particle;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.CompatParticle;
import fr.geoventure.geocore.render.Particles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

/** The 30-frame, fullbright additive RBMK melt cloud. */
public final class ParticleRBMKMush extends CompatParticle {
    private static final int FRAMES = 30;

    private ParticleRBMKMush(ClientLevel level, double x, double y, double z, float scale) {
        super(level, x, y + scale, z, Particles.sprite(Ids.of(Constants.MOD_ID, "rbmk_mush")));
        lifetime = 50;
        setQuadSize(scale);
        gravity = 0F;
        friction = 1F;
        hasPhysics = false;
        rCol = gCol = bCol = 1F;
        drawAdditiveWithoutFog();
    }

    @Override public void tick() {
        xo = x;
        yo = y;
        zo = z;
        if (++age >= lifetime) remove();
    }
    private int frame() { return Math.min(FRAMES - 1, age * FRAMES / lifetime); }
    @Override protected float getV0() { return sprite.getV((float) frame() / FRAMES); }
    @Override protected float getV1() { return sprite.getV((float) (frame() + 1) / FRAMES); }

    public static void spawn(ClientLevel level, double x, double y, double z, float scale) {
        Particles.add(new ParticleRBMKMush(level, x, y, z, Math.max(1F, scale)));
    }
}
