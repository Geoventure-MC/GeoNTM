package fr.geoventure.geontm.recipes;

import fr.geoventure.geontm.recipes.data.FluidPropertySource;
import fr.geoventure.geocore.world.DataSource;
import fr.geoventure.geocore.world.DataSources;
import java.util.List;

/**
 * HBM's files in the {@code minecraft} namespace: the {@code fluid_property} files of vanilla's empty, water and lava,
 * which are 1.7's NONE, WATER and LAVA (1.7 {@code Fluids.init}).
 */
public final class MinecraftDataSources implements DataSources {

    @Override
    public String namespace() {
        return "minecraft";
    }

    @Override
    public List<DataSource<?>> sources() {
        return List.of(new FluidPropertySource("minecraft"));
    }
}
