package fr.geoventure.geontm.compat.computer;

import fr.geoventure.geocore.computer.Computers;

/**
 * 1.7's OpenComputers components, served to CC: Tweaked and to OpenComputers: Rebooted through Core's
 * {@link Computers}, which owns each mod's registration. Peripheral classes name no computer mod's types.
 */
public final class GeoNtmComputers {

    private GeoNtmComputers() {}

    /** Common init, once, before the loaders' capability registration. */
    public static void init() {
        if (Computers.available()) Peripherals.init();
    }
}
