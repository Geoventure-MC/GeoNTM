package fr.geoventure.geontm.items.armor;

import fr.geoventure.geontm.api.data.ArmorDataMaps;
import fr.geoventure.geontm.api.item.IGasMask;
import fr.geoventure.geontm.handler.ArmorUtil;
import fr.geoventure.geontm.items.ModDataComponents;
import fr.geoventure.geontm.util.ArmorRegistry.HazardClass;
import fr.geoventure.geocore.world.ArmorSlot;
import fr.geoventure.geocore.world.CompatArmorItem;
import fr.geoventure.geocore.world.CompatTooltipContext;
import fr.geoventure.geontm.items.GeoNtmArmorMaterials;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import fr.geoventure.geontm.sound.ModSounds;
import net.minecraft.sounds.SoundSource;

/** Component-backed, filterable helmet. Right-click with a filter in the other hand to install it; sneak to remove it. */
public class ItemGasMask extends CompatArmorItem implements IGasMask {

    public ItemGasMask(Properties properties) {
        super(GeoNtmArmorMaterials.GAS_MASK, ArmorSlot.HELMET, properties);
    }

    @Override
    public List<HazardClass> getBlacklist(ItemStack stack) {
        return ArmorDataMaps.gasMaskBlacklist(this);
    }

    @Override
    public ItemStack getFilter(ItemStack stack) {
        return ModDataComponents.gasMaskFilter(stack);
    }

    @Override
    public boolean isFilterApplicable(ItemStack stack, ItemStack filter) {
        return !filter.isEmpty() && filter.isDamageableItem();
    }

    @Override
    public void installFilter(ItemStack stack, ItemStack filter) {
        if (isFilterApplicable(stack, filter)) ModDataComponents.gasMaskFilter(stack, filter);
    }

    public ItemStack removeFilter(ItemStack stack) {
        ItemStack filter = getFilter(stack);
        ModDataComponents.removeGasMaskFilter(stack);
        return filter;
    }

    @Override
    public void damageFilter(ItemStack stack, int damage) {
        if (damage <= 0) return;
        ItemStack filter = getFilter(stack);
        if (filter.isEmpty() || !filter.isDamageableItem()) return;

        int remaining = filter.getMaxDamage() - filter.getDamageValue();
        if (damage >= remaining) {
            removeFilter(stack);
        } else {
            filter.setDamageValue(filter.getDamageValue() + damage);
            installFilter(stack, filter);
        }
    }

    @Override
    protected void appendCompatHoverText(ItemStack stack, CompatTooltipContext context, List<Component> tooltip,
                                         TooltipFlag flag) {
        ArmorUtil.addGasMaskInformation(stack, tooltip);
    }

    @Override
    protected InteractionResult compatUse(Level level, Player player, InteractionHand hand) {
        return geontm$use(level, player, hand);
    }

    private InteractionResult geontm$use(Level level, Player player, InteractionHand hand) {
        ItemStack mask = player.getItemInHand(hand);
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        if (player.isShiftKeyDown()) {
            ItemStack filter = removeFilter(mask);
            if (!filter.isEmpty()) {
                if (!player.getInventory().add(filter)) player.drop(filter, false);
            }
            return InteractionResult.SUCCESS;
        }

        ItemStack offered = player.getItemInHand(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        if (!isFilterApplicable(mask, offered)) return InteractionResult.TRY_WITH_EMPTY_HAND;

        ItemStack previous = getFilter(mask);
        installFilter(mask, offered);
        if (!player.isCreative()) offered.shrink(1);
        if (!previous.isEmpty() && !player.getInventory().add(previous)) player.drop(previous, false);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.GASMASK_SCREW.get(),
                SoundSource.PLAYERS, 1.0F, 1.0F);
        return InteractionResult.SUCCESS;
    }
}
