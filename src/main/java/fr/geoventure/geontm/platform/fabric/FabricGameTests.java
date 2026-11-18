package fr.geoventure.geontm.platform.fabric;

/**
 * The {@code fabric-gametest} entrypoint. Fabric API registers this class by reflection and runs its
 * {@code @GameTestGenerator} when the server starts with {@code -Dfabric-api.gametest}
 * ({@code gradlew :<node>-fabric:runGametest}). 26.1 has no generator API; its Fabric API registry hook is forwarded
 * to GeoCore by the loader-specific mixin. The suites are declared by {@code GameTestEntry}.
 */
public final class FabricGameTests {

    public FabricGameTests() {}

    //? if <26.1 {
    /*@net.minecraft.gametest.framework.GameTestGenerator
    public static java.util.Collection<net.minecraft.gametest.framework.TestFunction> generate() {
        for (fr.geoventure.geocore.gametest.GameTests.Suite suite : fr.geoventure.geontm.gametest.GeoNtmGameTestSuites.all()) {
            fr.geoventure.geocore.gametest.GameTests.register(suite);
        }
        return fr.geoventure.geocore.gametest.GameTests.pending();
    }
    *///?}
}
