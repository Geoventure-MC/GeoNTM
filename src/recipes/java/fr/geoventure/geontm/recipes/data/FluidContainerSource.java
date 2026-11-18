package fr.geoventure.geontm.recipes.data;

import fr.geoventure.geontm.api.data.FluidDataMaps;
import fr.geoventure.geontm.api.data.FluidDataMaps.Container;
import fr.geoventure.geocore.data.DataMapSource;
import java.util.Optional;

/**
 * 1.7 {@code FluidContainerRegistry.register}: the fixed containers (full item, empty item, fluid, amount). The 1.7
 * fluid cells ({@code cell_deuterium}, ...) are one component cell in the port, and the per-fluid canisters, gas tanks,
 * tanks, barrels, disperser canisters and glands of its loop are component items too, so neither is listed here.
 */
public final class FluidContainerSource extends DataMapSource<Container> {

    public FluidContainerSource() {
        super(FluidDataMaps.FLUID_CONTAINER, false);
    }

    private void container(String full, String empty, String fluid, int amount) {
        put(full, new Container(fluid, amount, Optional.ofNullable(empty)));
    }

    @Override
    protected void values() {
        container("minecraft:water_bucket", "minecraft:bucket", "minecraft:water", 1000);
        // 1.7 new ItemStack(Items.potionitem) (meta 0, the water bottle), 250 mB of water: one item for every potion here,
        // so FluidContainerRegistry states it in code (potion contents minecraft:water)
        container("minecraft:lava_bucket", "minecraft:bucket", "minecraft:lava", 1000);
        container("geontm:bucket_mud", "minecraft:bucket", "geontm:watz", 1000);
        container("geontm:bucket_schrabidic_acid", "minecraft:bucket", "geontm:schrabidic", 1000);
        container("geontm:bucket_sulfuric_acid", "minecraft:bucket", "geontm:sulfuric_acid", 1000);

        // 1.7 new FluidContainer(new ItemStack(ModBlocks.red_barrel), new ItemStack(ModItems.tank_steel), Fluids.DIESEL, 10000)
        container("geontm:red_barrel", "geontm:tank_steel", "geontm:diesel", 10000);
        container("geontm:pink_barrel", "geontm:tank_steel", "geontm:kerosene", 10000);
        container("geontm:lox_barrel", "geontm:tank_steel", "geontm:oxygen", 10000);

        container("geontm:ore_oil", null, "geontm:oil", 250);
        // 1.7: GeneralConfig.enable528 ? 50 : 250
        put("geontm:ore_gneiss_gas", new Container("geontm:petroleum", 250, Optional.empty(), Optional.of(50)));

        container("geontm:bottle_mercury", "minecraft:glass_bottle", "geontm:mercury", 1000);
        // 1.7 ingot_mercury is the port's nugget_mercury
        container("geontm:nugget_mercury", null, "geontm:mercury", 125);

        container("geontm:rod_zirnox_tritium", "geontm:rod_zirnox_empty", "geontm:tritium", 2000);

        container("geontm:particle_hydrogen", "geontm:particle_empty", "geontm:hydrogen", 1000);
        container("geontm:particle_amat", "geontm:particle_empty", "geontm:amat", 1000);
        container("geontm:particle_aschrab", "geontm:particle_empty", "geontm:aschrab", 1000);

        container("geontm:iv_blood", "geontm:iv_empty", "geontm:blood", 100);
        container("geontm:iv_xp", "geontm:iv_xp_empty", "geontm:xpjuice", 100);
        // 1.7 new FluidContainer(new ItemStack(Items.experience_bottle), new ItemStack(Items.glass_bottle), Fluids.XPJUICE, 100)
        container("minecraft:experience_bottle", "minecraft:glass_bottle", "geontm:xpjuice", 100);

        // 1.7 new FluidContainer(new ItemStack(ModItems.can_mug), new ItemStack(ModItems.can_empty), Fluids.MUG, 100)
        container("geontm:can_mug", "geontm:can_empty", "geontm:mug", 100);
    }
}
