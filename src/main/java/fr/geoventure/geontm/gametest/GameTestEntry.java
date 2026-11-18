package fr.geoventure.geontm.gametest;

import fr.geoventure.geocore.platform.ModEntry;

/** Fabric's {@code geocore:entry} for the GameTest suites; release jars drop the class and its line in fabric.mod.json. */
public final class GameTestEntry implements ModEntry {

    @Override
    public void declare() {
        GeoNtmGameTestSuites.all();
    }
}
