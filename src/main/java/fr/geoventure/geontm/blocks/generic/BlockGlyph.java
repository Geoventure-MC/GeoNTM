package fr.geoventure.geontm.blocks.generic;

import fr.geoventure.geocore.world.CompatBlock;

/**
 * 1.7 {@code BlockGlyph}, the enargite glyph brick: the glyph on the four sides, plain enargite on top and
 * bottom. 1.7 kept the sixteen glyphs as metadata; the port flattens them to {@code brick_jungle_glyph_<n>},
 * each dropping itself (1.7 {@code damageDropped} returned the meta). The glyph's name is on its item
 * ({@link fr.geoventure.geontm.items.block.ItemGlyphBlock}).
 */
public class BlockGlyph extends CompatBlock {

    public final int glyph;

    public BlockGlyph(Properties properties, int glyph) {
        super(properties);
        this.glyph = glyph;
    }
}
