package fr.geoventure.geontm.items.armor;

import fr.geoventure.geocore.world.ArmorSlot;
import fr.geoventure.geocore.world.CompatArmorItem;
import fr.geoventure.geocore.world.GeoCoreArmorMaterial;

/**
 * 1.7 {@code ArmorModel}: a plain helmet whose worn body is a model rather than an armour texture. In 1.7 the
 * class switched on {@code this == ModItems.goggles / hat / cape_*}; here each item names its model instead.
 * The goggles' blur overlay ({@code renderHelmetOverlay}) is drawn by the client HUD for the goggles alone -
 * 1.7's check also named the red and grey hazmat helmets, which are not {@code ArmorModel}s and so never reached it.
 */
public class ArmorModel extends CompatArmorItem implements IWornModel {

    private final String model;

    public ArmorModel(GeoCoreArmorMaterial material, ArmorSlot slot, Properties properties, String model) {
        super(material, slot, properties);
        this.model = model;
    }

    @Override
    public String wornModel() {
        return model;
    }
}
