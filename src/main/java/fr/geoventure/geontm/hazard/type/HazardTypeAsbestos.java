package fr.geoventure.geontm.hazard.type;

import java.util.List;

import fr.geoventure.geontm.capability.GeoNtmLivingProps;
import fr.geoventure.geontm.config.RadiationConfig;
import fr.geoventure.geontm.handler.ArmorUtil;
import fr.geoventure.geontm.hazard.modifier.IHazardModifier;
import fr.geoventure.geontm.util.ArmorRegistry;
import fr.geoventure.geontm.util.ArmorRegistry.HazardClass;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class HazardTypeAsbestos implements IHazardType {

    @Override
    public void onUpdate(final LivingEntity target, final double level, final ItemStack stack) {

        if (RadiationConfig.disableAsbestos)
            return;

        if (ArmorRegistry.hasProtection(target, EquipmentSlot.HEAD, HazardClass.PARTICLE_FINE))
            ArmorUtil.damageGasMaskFilter(target, (int) level * hazardRate);
        else
            GeoNtmLivingProps.incrementAsbestos(target, (int) Math.min(level, 10) * hazardRate);
    }

    @Override
    public void updateEntity(final ItemEntity item, final double level) {
    }

    @Override
    public void addHazardInformation(final Player player, final List<Component> list, final double level, final ItemStack stack, final List<IHazardModifier> modifiers) {
        list.add(Component.literal("[").append(Component.translatable("trait.asbestos")).append(Component.literal("]")).withStyle(ChatFormatting.WHITE));
    }
}
