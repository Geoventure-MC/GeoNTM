package fr.geoventure.geontm.items.armor;

import fr.geoventure.geontm.capability.GeoNtmLivingProps;
import fr.geoventure.geontm.handler.ArmorModHandler;
import fr.geoventure.geocore.world.CompatTooltipContext;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

/** 1.7 {@code ItemModMedal} (Liquidator Medal): removes 0.5 RAD per tick, i.e. the advertised 10 RAD/s. */
public class ItemModMedal extends ItemArmorMod {

    public ItemModMedal(Item.Properties properties) {
        super(ArmorModHandler.plate_only, true, true, false, false, properties);
    }

    @Override
    protected void appendCompatHoverText(ItemStack stack, CompatTooltipContext context,
                                         List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(coloured(ChatFormatting.GOLD, "-10 RAD/s"));
        super.appendCompatHoverText(stack, context, tooltip, flag);
    }

    @Override
    public void addArmorModDescription(List<Component> tooltip, ItemStack stack, ItemStack armor) {
        tooltip.add(modLine(ChatFormatting.GOLD, stack, " (-10 RAD/s)"));
    }

    @Override
    public void modUpdate(LivingEntity entity, ItemStack armor) {
        if (entity.level().isClientSide()) return;
        double rad = GeoNtmLivingProps.getRadiation(entity) - 0.5F;
        GeoNtmLivingProps.setRadiation(entity, Math.max(rad, 0));
    }
}
