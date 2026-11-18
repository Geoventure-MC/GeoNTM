package fr.geoventure.geontm.gametest.deimport;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.gametest.GameTests;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;

/** The {@code geontm:deimport_*} GameTests, found by annotation; release jars drop this package. */
@EventBusSubscriber(modid = Constants.MOD_ID)
public final class DeImportGameTestsNeoForge {
    private DeImportGameTestsNeoForge() {}

    @SubscribeEvent
    public static void register(RegisterGameTestsEvent event) {
        GameTests.register(event, DeImportGameTests.TESTS);
    }
}
