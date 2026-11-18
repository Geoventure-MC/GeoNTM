package fr.geoventure.geontm.items.armor;

import fr.geoventure.geontm.capability.GeoNtmLivingProps;
import fr.geoventure.geontm.handler.ArmorModHandler;
import fr.geoventure.geocore.world.CompatTooltipContext;
import fr.geoventure.geocore.world.DamageEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/** Plutonic Quartz: an "extra" armor module that strips 10 RAD from the wearer whenever they are hurt. */
public class ItemModQuartz extends ItemArmorMod {

    public ItemModQuartz(Item.Properties properties) {
        super(ArmorModHandler.extra, true, true, true, true, properties);
    }

    @Override
    protected void appendCompatHoverText(ItemStack stack, CompatTooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Taking damage removes 10 RAD").withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(Component.literal(""));
        super.appendCompatHoverText(stack, context, tooltip, flag);
    }

    @Override
    public void addArmorModDescription(List<Component> tooltip, ItemStack stack, ItemStack armor) {
        tooltip.add(Component.literal("  ").append(stack.getHoverName()).append(" (-10 RAD when hit)").withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public void modDamage(DamageEvents.DamageEvent event, ItemStack armor) {
        if (!event.entity().level().isClientSide()) {
            double rad = GeoNtmLivingProps.getRadiation(event.entity());
            rad = Math.max(rad - 10, 0);
            GeoNtmLivingProps.setRadiation(event.entity(), rad);
        }
    }
}
