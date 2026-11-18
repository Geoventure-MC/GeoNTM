package fr.geoventure.geontm.hazard.type;

import java.util.List;

import fr.geoventure.geontm.config.RadiationConfig;
import fr.geoventure.geontm.hazard.modifier.IHazardModifier;
import fr.geoventure.geontm.util.ArmorRegistry;
import fr.geoventure.geontm.util.ArmorRegistry.HazardClass;
import fr.geoventure.geocore.world.Effects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class HazardTypeBlinding implements IHazardType {

    @Override
    public void onUpdate(final LivingEntity target, final double level, final ItemStack stack) {

        if (RadiationConfig.disableBlinding)
            return;

        if (!ArmorRegistry.hasProtection(target, EquipmentSlot.HEAD, HazardClass.LIGHT)) {
            // 1.7 re-applied blindness for ceil(level) ticks every tick, so it lasts ceil(level) ticks after the item
            // is gone. Applied every hazardRate ticks here, the duration is padded by one interval so it never lapses
            // while carried; a duration is not a dose, so it is not multiplied by the rate.
            Effects.add(target, MobEffects.BLINDNESS, (int) Math.ceil(level) + hazardRate, 0);
        }
    }

    @Override
    public void updateEntity(final ItemEntity item, final double level) {
    }

    @Override
    public void addHazardInformation(final Player player, final List<Component> list, final double level, final ItemStack stack, final List<IHazardModifier> modifiers) {
        list.add(Component.literal("[").append(Component.translatable("trait.blinding")).append(Component.literal("]")).withStyle(ChatFormatting.DARK_AQUA));
    }
}
