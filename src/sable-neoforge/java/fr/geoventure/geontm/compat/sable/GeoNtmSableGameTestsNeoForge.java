package fr.geoventure.geontm.compat.sable;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.gametest.GameTests;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;

/** The {@code geontm:sable_*} GameTests on NeoForge, found by annotation; release jars drop the class. */
@EventBusSubscriber(modid = Constants.MOD_ID)
public final class GeoNtmSableGameTestsNeoForge {

    private GeoNtmSableGameTestsNeoForge() {}

    @SubscribeEvent
    public static void register(RegisterGameTestsEvent event) {
        if (ModList.get().isLoaded("sable")) GameTests.register(event, SableGameTests.TESTS);
    }
}
