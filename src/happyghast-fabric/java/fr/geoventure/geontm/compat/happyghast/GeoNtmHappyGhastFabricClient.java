package fr.geoventure.geontm.compat.happyghast;

import fr.geoventure.geontm.compat.happyghast.client.HappyGhastClient;
import net.fabricmc.api.ClientModInitializer;

/**
 * The happy-ghast Fat Man on the Fabric client: a {@code client} entrypoint the build puts before HBM's
 * {@code FabricClientInit}, which registers every GEONTM renderer (this one included) and audits them in one go.
 */
public final class GeoNtmHappyGhastFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HappyGhastClient.init();
    }
}
