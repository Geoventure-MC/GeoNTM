package fr.geoventure.geontm.compat.sable;

import fr.geoventure.geontm.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

/** NeoForge's entry to HBM's Sable integration ({@link GeoNtmSableIntegration}), found by NeoForge's annotation scan. */
@EventBusSubscriber(modid = Constants.MOD_ID)
public final class GeoNtmSable {

    private GeoNtmSable() {}

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        if (ModList.get().isLoaded("sable")) GeoNtmSableIntegration.setup();
    }
}
