package fr.geoventure.geontm.gametest;

//? if neoforge {
/*import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.gametest.GameTests;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;

/^* Declares the GameTest suites while mods construct and hands them to NeoForge. Found by annotation, so release jars hold no call to it. ^/
@EventBusSubscriber(modid = Constants.MOD_ID)
public final class GameTestSetup {

    private GameTestSetup() {}

    @SubscribeEvent
    public static void construct(FMLConstructModEvent event) {
        GeoNtmGameTestSuites.all();
    }

    @SubscribeEvent
    public static void register(RegisterGameTestsEvent event) {
        for (GameTests.Suite suite : GeoNtmGameTestSuites.all()) GameTests.register(event, suite);
    }
}
*///?} elif forge {
import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.gametest.GameTests;
import net.minecraftforge.event.RegisterGameTestsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

/** Declares the GameTest suites while mods construct and hands them to Forge. Found by annotation, so release jars hold no call to it. */
@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class GameTestSetup {

    private GameTestSetup() {}

    @SubscribeEvent
    public static void construct(FMLConstructModEvent event) {
        GeoNtmGameTestSuites.all();
    }

    @SubscribeEvent
    public static void register(RegisterGameTestsEvent event) {
        for (GameTests.Suite suite : GeoNtmGameTestSuites.all()) GameTests.register(event, suite);
    }
}
//?}
