package fr.geoventure.geontm.client;

//? if neoforge {
/*import fr.geoventure.geontm.Constants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;

/^* Registers the GEONTM harness queries on the client. Found by annotation, so release jars hold no call to it. ^/
@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public final class HarnessQueriesSetup {

    private HarnessQueriesSetup() {}

    @SubscribeEvent
    public static void construct(FMLConstructModEvent event) {
        GeoNtmHarnessQueries.registerAll();
    }
}
*///?} elif forge {
import fr.geoventure.geontm.Constants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

/** Registers the GEONTM harness queries on the client. Found by annotation, so release jars hold no call to it. */
@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class HarnessQueriesSetup {

    private HarnessQueriesSetup() {}

    @SubscribeEvent
    public static void construct(FMLConstructModEvent event) {
        GeoNtmHarnessQueries.registerAll();
    }
}
//?}
