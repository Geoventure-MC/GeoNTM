package fr.geoventure.geontm.items.tool;

import fr.geoventure.geocore.world.CompatHoeItem;
import fr.geoventure.geocore.world.ToolTier;
import fr.geoventure.geocore.world.Tools;

/**
 * 1.7 {@code ModHoe}: a plain hoe of an GEONTM tool material. 1.7 hoes carried no attack modifiers, so the
 * main-hand attack modifier is zero; the swing speed is vanilla's hoe speed for the tier.
 */
public class ModHoe extends CompatHoeItem {

    public ModHoe(Properties properties, ToolTier material) {
        super(properties, material, 0.0F, Tools.hoeSpeed(material.level()));
    }
}
