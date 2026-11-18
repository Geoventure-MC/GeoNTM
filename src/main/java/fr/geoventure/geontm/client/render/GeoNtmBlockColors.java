package fr.geoventure.geontm.client.render;

import java.awt.Color;
import java.util.Set;

import fr.geoventure.geontm.blocks.ModFalloutBlocks;
import fr.geoventure.geontm.blocks.generic.BlockSellafieldSlaked;
import fr.geoventure.geocore.client.BlockTint;
import fr.geoventure.geocore.client.Tints;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

/**
 * Cross-loader registration of HBM's block tint sources (same Sink pattern as
 * {@link GeoNtmEntityRenderers}). 26.1 has no per-quad {@code BlockColor} callback; it uses
 * {@link BlockTint} (a state-keyed provider registered on {@code BlockColors}). NeoForge hooks
 * it via {@code RegisterColorHandlersEvent.BlockTintSources}, Fabric by registering directly on
 * {@code Minecraft.getBlockColors()} once the client has started.
 *
 * <p>Only the sellafite family: a grayscale tint darkening with the {@code SHADE} state:
 * {@code brightness = 1 − shade/15}, the old {@code BlockSellafieldSlaked} {@code IBlockColor}.
 * Client-only.
 */
public final class GeoNtmBlockColors {

    private GeoNtmBlockColors() {}

    /** Shade-keyed grayscale: declares {@code SHADE} as its relevant property so the tint cache keys on it. */
    private static final BlockTint SELLAFIELD_TINT = new BlockTint() {
        @Override
        public int color(BlockState state) {
            int shade = state.getValue(BlockSellafieldSlaked.SHADE);
            return 0xFF000000 | Color.HSBtoRGB(0F, 0F, 1F - shade / 15F);
        }

        @Override
        public Set<Property<?>> relevantProperties() {
            return Set.of(BlockSellafieldSlaked.SHADE);
        }
    };

    public static void registerAll() {
        Tints.registerBlockTint(SELLAFIELD_TINT,
                ModFalloutBlocks.sellafield_slaked,
                ModFalloutBlocks.sellafield_bedrock,
                ModFalloutBlocks.ore_sellafield_diamond,
                ModFalloutBlocks.ore_sellafield_emerald,
                ModFalloutBlocks.ore_sellafield_radgem,
                ModFalloutBlocks.ore_sellafield_schrabidium,
                ModFalloutBlocks.ore_sellafield_uranium_scorched);
    }
}
