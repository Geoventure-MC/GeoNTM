package fr.geoventure.geontm.particle;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import fr.geoventure.geontm.registration.Reg;

/** Common particle-type declarations. Client providers are installed separately. */
public final class GeoNtmParticles {
    private GeoNtmParticles() {}

    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, Constants.MOD_ID);
    private static final Reg REG = Reg.of(PARTICLES);
    // The original effect is uncapped: its 250-block broadcast outlives vanilla's normal
    // particle limiter and the cloud is the visible indication of an RBMK melt.
    public static final RegistryObject<SimpleParticleType> RBMK_MUSH =
            REG.register(Registries.PARTICLE_TYPE, "rbmk_mush", () -> new SimpleParticleType(true) {});

    public static final RegistryObject<SimpleParticleType> RBMK_JET_STEAM =
            REG.register(Registries.PARTICLE_TYPE, "rbmk_jet_steam", () -> new SimpleParticleType(true) {});

    /** Class-load hook for the parent bootstrap, alongside the other deferred registrars. */
    public static void init() {}
}
