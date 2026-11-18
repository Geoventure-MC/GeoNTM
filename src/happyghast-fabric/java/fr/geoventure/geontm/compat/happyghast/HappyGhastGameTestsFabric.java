package fr.geoventure.geontm.compat.happyghast;

import fr.geoventure.geocore.platform.ModEntry;

/** Declares the happy-ghast GameTests on Fabric, before the registries fill; release jars drop the class and its entry. */
public final class HappyGhastGameTestsFabric implements ModEntry {

    @Override
    public void declare() {
        HappyGhastGameTests.init();
    }
}
