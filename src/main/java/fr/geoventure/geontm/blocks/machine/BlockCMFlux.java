package fr.geoventure.geontm.blocks.machine;

import fr.geoventure.geocore.world.CompatBlock;

/**
 * 1.7 {@code BlockCMFlux}, a {@code BlockPillar} ({@code cm_flux_top} ends, {@code cm_flux_side} sides). As a
 * custom machine component it makes the controller read the neutron flux of a research reactor beside it.
 */
public class BlockCMFlux extends CompatBlock {

    public BlockCMFlux(Properties properties) {
        super(properties);
    }
}
