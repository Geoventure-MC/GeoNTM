package fr.geoventure.geontm.items.food;

import fr.geoventure.geontm.capability.GeoNtmLivingProps;
import fr.geoventure.geontm.entity.EntityHbmProps;
import fr.geoventure.geontm.handler.ArmorModEvents;
import fr.geoventure.geocore.world.CompatItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * 1.7 {@code ItemFlask}, an {@code ItemEnumMulti} over {@code EnumInfusion} whose only constant is SHIELD; the
 * port registers it as {@code flask_infusion_shield}. Drinking it raises the permanent shield by 5 (up to the
 * 100 cap) and tops the current shield up by the same amount.
 */
public class ItemFlask extends CompatItem {

    /** 1.7 {@code GeoNtmPlayerProps.shieldCap}. */
    public static final float SHIELD_CAP = 100F;

    public ItemFlask(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult compatUse(Level level, Player player, InteractionHand hand) {
        if (GeoNtmLivingProps.getData(player).maxShield >= SHIELD_CAP) return InteractionResult.TRY_WITH_EMPTY_HAND;
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!(entity instanceof Player player)) return stack;
        if (!player.getAbilities().instabuild) stack.shrink(1);
        if (level.isClientSide()) return stack;

        float infusion = 5F;
        EntityHbmProps props = GeoNtmLivingProps.getData(player);
        props.maxShield = Math.min(SHIELD_CAP, props.maxShield + infusion);
        props.shield = Math.min(props.shield + infusion, ArmorModEvents.effectiveMaxShield(player));
        return stack;
    }

    @Override
    protected int compatUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    protected UseAnimation compatUseAnimation(ItemStack stack) {
        return UseAnimation.DRINK;
    }
}
