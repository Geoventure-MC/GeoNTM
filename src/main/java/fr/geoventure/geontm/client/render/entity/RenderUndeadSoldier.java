package fr.geoventure.geontm.client.render.entity;

import fr.geoventure.geontm.entity.mob.EntityUndeadSoldier;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.living.CompatHumanoidMobRenderer;
import fr.geoventure.geocore.render.living.HumanoidBody;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

/**
 * 1.7 RenderUndeadSoldier: switches vanilla zombie/skeleton geometry and skin from the synced type byte.
 * Both bodies pose as zombies because 1.7's skeleton arm is {@code ModelSkeletonNT extends ModelZombie};
 * the armour set stays plain, which is 1.7's own {@code RenderBiped.func_82421_b} pass model. The soldier's
 * Taurun gear draws through that armour layer and HBM's own humanoid armour layers.
 */
public final class RenderUndeadSoldier extends CompatHumanoidMobRenderer<EntityUndeadSoldier> {

    private static final Identifier ZOMBIE_TEXTURE = Ids.minecraft("textures/entity/zombie/zombie.png");
    private static final Identifier SKELETON_TEXTURE = Ids.minecraft("textures/entity/skeleton/skeleton.png");

    public RenderUndeadSoldier(EntityRendererProvider.Context context) {
        super(context, 0.5F, new Identifier[]{ZOMBIE_TEXTURE, SKELETON_TEXTURE},
                HumanoidBody.zombie(context), HumanoidBody.skeleton(context));
        addZombieArmorLayer(context);
    }

    @Override
    protected int variant(EntityUndeadSoldier entity) {
        return entity.soldierType() == EntityUndeadSoldier.TYPE_SKELETON ? 1 : 0;
    }
}
