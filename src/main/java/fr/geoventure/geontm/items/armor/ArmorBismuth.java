package fr.geoventure.geontm.items.armor;

import fr.geoventure.geocore.world.ArmorSlot;
import fr.geoventure.geocore.world.GeoCoreArmorMaterial;

/**
 * 1.7 {@code ArmorBismuth}: an ordinary {@link ArmorFSB} whose only addition was its OBJ body
 * ({@code ModelArmorBismuth}) and item renderer. Both are named through {@code setArmorModel("bismuth")}
 * and the {@code armor_bismuth_*} item-model animation; the three dashes come from the FSB dash count.
 */
public class ArmorBismuth extends ArmorFSB {

    public ArmorBismuth(GeoCoreArmorMaterial material, ArmorSlot slot, Properties properties) {
        super(material, slot, properties);
    }
}
