package fr.geoventure.geontm.items.armor;

import fr.geoventure.geocore.world.ArmorSlot;
import fr.geoventure.geocore.world.GeoCoreArmorMaterial;

/**
 * 1.7 {@code ArmorDigamma}: Fau's digamma suit. Upstream's class only supplied {@code ModelArmorDigamma} and the item renderer,
 * both named here through {@code setArmorModel("fau")} and the {@code armor_fau_*} item models; power,
 * drain and the set bonus are {@link ArmorFSBPowered}'s.
 */
public class ArmorDigamma extends ArmorFSBPowered {

    public ArmorDigamma(GeoCoreArmorMaterial material, ArmorSlot slot, Properties properties,
                   long maxPower, long chargeRate, long consumption, long drain) {
        super(material, slot, properties, maxPower, chargeRate, consumption, drain);
    }
}
