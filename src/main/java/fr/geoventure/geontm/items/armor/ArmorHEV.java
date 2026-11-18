package fr.geoventure.geontm.items.armor;

import fr.geoventure.geontm.client.HEVOverlay;
import fr.geoventure.geontm.items.ModArmorSetItems;
import fr.geoventure.geocore.world.ArmorSlot;
import fr.geoventure.geocore.world.GeoCoreArmorMaterial;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

/**
 * 1.7 {@code ArmorHEV}: the Half-Life suit. Upstream cancelled the vanilla health and armour HUD elements
 * while the full set was worn (charged or not) with the HEV chestplate, and drew its own orange readout in
 * their place. The readout is client code ({@code fr.geoventure.geontm.client.HEVOverlay}); this class keeps the
 * condition, which the client uses both to suppress the vanilla rows and to draw.
 */
public class ArmorHEV extends ArmorFSBPowered {

    public ArmorHEV(GeoCoreArmorMaterial material, ArmorSlot slot, Properties properties,
                    long maxPower, long chargeRate, long consumption, long drain) {
        super(material, slot, properties, maxPower, chargeRate, consumption, drain);
    }

    /** Upstream's {@code hasFSBArmorIgnoreCharge(player) && armorInventory[2] == hev_plate}. */
    public static boolean replacesHud(Player player) {
        return player != null && ArmorFSB.hasFSBArmorIgnoreCharge(player)
                && player.getItemBySlot(EquipmentSlot.CHEST).is(ModArmorSetItems.hev_plate.get());
    }

    @Override
    public void handleOverlay(Object graphics, Player player) {
        if (replacesHud(player)) HEVOverlay.render(graphics, player);
    }
}
