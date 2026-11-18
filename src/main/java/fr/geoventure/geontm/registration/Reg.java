package fr.geoventure.geontm.registration;

import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.Registrar;

/**
 * HBM's registration chain: GeoCore's {@link Registrar} (registration plus its Dejson declarations, see there),
 * kept under its old name so every {@code Reg.of(...)}, {@code Reg.Handle} and {@code Reg.EntityHandle} in GEONTM reads
 * as before. GEONTM-only bundles live in {@link Presets}; an addon uses {@code Registrar} directly with its own
 * registers, and HBM's {@code Presets} and {@code GeoNtmTab} where they fit.
 */
public final class Reg extends Registrar {

    private Reg(DeferredRegister<?>... registers) {
        super(registers);
    }

    /** A registrar over the calling class's own registers (blocks, items, block entities, ...), in any order. */
    public static Reg of(DeferredRegister<?>... registers) {
        return new Reg(registers);
    }
}
