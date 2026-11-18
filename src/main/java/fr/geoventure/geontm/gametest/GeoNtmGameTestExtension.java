package fr.geoventure.geontm.gametest;

import fr.geoventure.geocore.gametest.GameTests;
import java.util.List;

/** Additional development suites, listed after the built-in ones. */
public interface GeoNtmGameTestExtension {
    List<GameTests.Suite> suites();
}
