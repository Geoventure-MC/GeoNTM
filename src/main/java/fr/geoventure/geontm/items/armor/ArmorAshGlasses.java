package fr.geoventure.geontm.items.armor;

import fr.geoventure.geocore.world.ArmorSlot;
import fr.geoventure.geocore.world.CompatArmorItem;
import fr.geoventure.geocore.world.GeoCoreArmorMaterial;

/**
 * 1.7 {@code ArmorAshGlasses}: the ash goggles, worn as {@code ModelGlasses} ({@code goggles.obj} with
 * {@code textures/armor/goggles.png}). They protect against light and sand (ArmorUtil), let the wearer see gas
 * clouds, and keep most of the ash overlay out of the eyes.
 */
public class ArmorAshGlasses extends CompatArmorItem implements IWornModel {

    public ArmorAshGlasses(GeoCoreArmorMaterial material, ArmorSlot slot, Properties properties) {
        super(material, slot, properties);
    }

    @Override
    public String wornModel() {
        return "ashglasses";
    }
}
