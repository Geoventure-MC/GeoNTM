package fr.geoventure.geontm.client.model;

import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.client.UnbakedModels;

/** Registers HBM's grouped OBJ resources with the shared native model bakers. */
public final class ObjBlockModels {
    private ObjBlockModels() {}

    public static void install() {
        UnbakedModels.registerGroups(Ids.of("geontm", "obj"), WavefrontObj::load);
    }
}
