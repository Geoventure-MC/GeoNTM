package fr.geoventure.geontm.items.food;

import fr.geoventure.geontm.handler.ConsumableTreatment;
import fr.geoventure.geontm.handler.ConsumableTreatment.Context;
import fr.geoventure.geontm.handler.ConsumableTreatments;
import fr.geoventure.geontm.inventory.gui.GuiUtil;
import fr.geoventure.geocore.world.CompatItem;
import fr.geoventure.geocore.world.CompatTooltipContext;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

/** Edible GEONTM pills (1.7 {@code ItemPill}); what each does is its {@code geontm:consumable_treatment}. */
public final class ItemPill extends CompatItem implements ConsumableTreatments.Handled {

    public ItemPill(Item.Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult compatUse(Level level, Player player, InteractionHand hand) {
        if (ConsumableTreatments.blocked(this, player)) return InteractionResult.TRY_WITH_EMPTY_HAND;
        return compatVanillaUse(level, player, hand);
    }

    @Override
    protected int compatUseDuration(ItemStack stack, LivingEntity entity) {
        return 10;
    }

    @Override
    protected UseAnimation compatUseAnimation(ItemStack stack) {
        return UseAnimation.EAT;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        ConsumableTreatment treatment = ConsumableTreatments.of(this);
        if (treatment != null && level instanceof ServerLevel server && entity instanceof Player player) {
            treatment.run(Context.eaten(server, player));
        }
        return result;
    }

    @Override
    protected void appendCompatHoverText(ItemStack stack, CompatTooltipContext context,
                                         List<Component> tooltip, TooltipFlag flag) {
        String key = getDescriptionId() + ".desc";
        if (Language.getInstance().has(key)) tooltip.addAll(GuiUtil.keyArray(key));
    }
}
