package fr.geoventure.geontm.items.machine;

import fr.geoventure.geocore.world.CompatItem;

/** One stable modern item identity for each old depleted-pellet metadata value. */
public final class ItemRTGPelletDepleted extends CompatItem {
    public final DepletedRTGMaterial material;

    public ItemRTGPelletDepleted(Properties properties, DepletedRTGMaterial material) {
        super(properties);
        this.material = material;
    }
}
