package fr.geoventure.geontm.items.armor;

import fr.geoventure.geocore.world.ArmorSlot;
import fr.geoventure.geocore.world.CompatArmorItem;
import fr.geoventure.geocore.world.GeoCoreArmorMaterial;

/**
 * 1.7 {@code MaskOfInfamy}: an iron-material helmet with its own armor texture ({@code MaskOfInfamy.png},
 * here the {@code infamy} equipment asset) and nothing else.
 */
public class MaskOfInfamy extends CompatArmorItem {

    public MaskOfInfamy(GeoCoreArmorMaterial material, Properties properties) {
        super(material, ArmorSlot.HELMET, properties);
    }
}
