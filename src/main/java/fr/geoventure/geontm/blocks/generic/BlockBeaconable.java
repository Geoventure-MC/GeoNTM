package fr.geoventure.geontm.blocks.generic;

import fr.geoventure.geocore.world.CompatBlock;


/**
 * Marker block for the old {@code BlockBeaconable} family. In modern Minecraft beacon membership is
 * data-driven through {@code minecraft:beacon_base_blocks}; the dedicated type keeps the hand-registered
 * family explicit without duplicating vanilla beacon logic.
 */
public class BlockBeaconable extends CompatBlock {

    public BlockBeaconable(Properties properties) {
        super(properties);
    }
}
