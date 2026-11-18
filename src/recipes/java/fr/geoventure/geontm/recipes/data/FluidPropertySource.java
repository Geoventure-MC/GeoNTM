package fr.geoventure.geontm.recipes.data;

import com.google.gson.JsonElement;
import fr.geoventure.geontm.inventory.fluid.FluidProperties;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geocore.world.DataSource;
import java.util.Map;
import net.minecraft.core.HolderLookup;

/**
 * 1.7 {@code Fluids.init}'s fluid properties ({@link FluidPropertyTable}) as {@code data/<namespace>/fluid_property/},
 * one file per fluid of {@code namespace}: HBM's own fluids in {@code geontm}, and in {@code minecraft} the three that
 * are vanilla's empty, water and lava. Rows are {@link FluidProperties.Properties}, written and read by
 * {@link FluidProperties#write(FluidProperties.Properties)} and {@link FluidProperties#read}.
 */
public final class FluidPropertySource extends DataSource<FluidProperties.Properties> {

    private final String namespace;

    public FluidPropertySource(String namespace) {
        super(FluidProperties.FOLDER);
        this.namespace = namespace;
    }

    @Override
    protected void registerDefaults() {
        for (Map.Entry<FluidType, FluidProperties.Properties> row : FluidPropertyTable.rows().entrySet()) {
            String[] id = row.getKey().getRegistryName().split(":", 2);
            if (id[0].equals(namespace)) register(id[1], row.getValue());
        }
    }

    @Override
    public JsonElement encode(FluidProperties.Properties row, HolderLookup.Provider registries) {
        return FluidProperties.write(row);
    }

    @Override
    public FluidProperties.Properties decode(JsonElement json, HolderLookup.Provider registries) {
        return FluidProperties.read(json.getAsJsonObject());
    }

    @Override
    public String describe() {
        return namespace + ":" + FluidProperties.FOLDER;
    }
}
