package fr.geoventure.geontm.items.armor;

import fr.geoventure.geocore.world.DamageEvents;
import net.minecraft.world.item.ItemStack;

/** 1.7 {@code IDamageHandler}: a worn armour piece that may change every {@code LivingHurtEvent} on its player. */
public interface IDamageHandler {

    void handleDamage(DamageEvents.DamageEvent event, ItemStack stack);
}
