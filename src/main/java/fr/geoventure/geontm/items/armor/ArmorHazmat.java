package fr.geoventure.geontm.items.armor;

import fr.geoventure.geocore.world.ArmorSlot;
import fr.geoventure.geocore.world.GeoCoreArmorMaterial;

/**
 * 1.7 {@code ArmorHazmat}: a full-set-bonus armor piece whose helmet draws the {@code overlay_hazmat}
 * blur. The overlay is set on the helmet with {@code setOverlay}; the remaining behaviour is
 * {@link ArmorFSB}'s. Used here by the PaA battle hazmat suit.
 */
public class ArmorHazmat extends ArmorFSB {

    public ArmorHazmat(GeoCoreArmorMaterial material, ArmorSlot slot, Properties properties) {
        super(material, slot, properties);
    }
}
