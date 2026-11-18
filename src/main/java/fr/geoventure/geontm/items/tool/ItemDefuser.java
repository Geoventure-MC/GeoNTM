package fr.geoventure.geontm.items.tool;

import fr.geoventure.geontm.api.block.IToolable;
import fr.geoventure.geontm.entity.mob.glyphid.EntityGlyphidNuclear;
import fr.geoventure.geontm.explosion.vanillant.ExplosionVNT;
import fr.geoventure.geontm.explosion.vanillant.standard.EntityProcessorCrossSmooth;
import fr.geoventure.geontm.explosion.vanillant.standard.ExplosionEffectWeapon;
import fr.geoventure.geontm.explosion.vanillant.standard.PlayerProcessorStandard;
import fr.geoventure.geontm.handler.ability.IWeaponAbility;
import fr.geoventure.geontm.items.ModAmmoItems;
import fr.geoventure.geontm.items.armor.ItemModDefuser;
import fr.geoventure.geontm.items.weapon.sedna.factory.ConfettiUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/** The defuser: a screwdriver-class tool that also castrates creepers and finishes a dying nuclear glyphid. */
public final class ItemDefuser extends ItemTooling {

    public ItemDefuser(Properties properties) {
        super(properties, IToolable.ToolType.DEFUSER);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target,
                                                  InteractionHand hand) {
        if (target instanceof Creeper creeper) {
            return ItemModDefuser.castrateCreeper(creeper, player, true) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }

        if (target instanceof EntityGlyphidNuclear john) {
            if (john.level() instanceof ServerLevel level && john.deathTicks > 0) {
                john.discard();

                ExplosionVNT vnt = new ExplosionVNT(level, john.getX(), john.getY(), john.getZ(), 5F, john);
                vnt.setEntityProcessor(new EntityProcessorCrossSmooth(1, 20).setupPiercing(10F, 0.2F));
                vnt.setPlayerProcessor(new PlayerProcessorStandard());
                vnt.setSFX(new ExplosionEffectWeapon(10, 2.5F, 1F));
                vnt.explode();

                ConfettiUtil.gib(john);
                IWeaponAbility.drop(john, new ItemStack(ModAmmoItems.nuke_demo.get()), 1.5F);
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
