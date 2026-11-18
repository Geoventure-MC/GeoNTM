package fr.geoventure.geontm.handler.radiation;

import fr.geoventure.geontm.interfaces.IRadResistantBlock;
import fr.geoventure.geontm.lib.GeoNtmBlockTags;
import fr.geoventure.geocore.platform.Lifecycle;
import fr.geoventure.geocore.world.BlockChanges;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The radiation engine's walls: {@link IRadResistantBlock}s that say so and the {@link GeoNtmBlockTags#RADIATION_RESISTANT}
 * tag. The classes mark their own sections; this marks them for tag members, and all of them when the tag changes.
 */
public final class RadiationWalls {

    private static Set<Block> tagged = Set.of();

    private RadiationWalls() {}

    public static void init() {
        BlockChanges.onChange((level, pos, oldState, newState) -> {
            if (oldState.is(GeoNtmBlockTags.RADIATION_RESISTANT) != newState.is(GeoNtmBlockTags.RADIATION_RESISTANT)) {
                RadiationSystemNT.markSectionForRebuild(level, pos);
            }
        });
        Lifecycle.onDatapackReloaded(server -> {
            Set<Block> now = new HashSet<>();
            for (Holder<Block> holder : BuiltInRegistries.BLOCK.getTagOrEmpty(GeoNtmBlockTags.RADIATION_RESISTANT)) now.add(holder.value());
            if (now.equals(tagged)) return;
            tagged = now;
            RadiationSystemNT.markAllForRebuild();
        });
    }

    /** False for a state that can never be a wall, wherever it stands. */
    static boolean mayBeWall(BlockState state) {
        return state.is(GeoNtmBlockTags.RADIATION_RESISTANT) || state.getBlock() instanceof IRadResistantBlock;
    }

    /** Whether the block at section-local {@code x, y, z} of section {@code cx, sy, cz} seals radiation. */
    static boolean isWall(ServerLevel level, BlockState state, int cx, int sy, int cz, int x, int y, int z) {
        if (state.is(GeoNtmBlockTags.RADIATION_RESISTANT)) return true;
        return state.getBlock() instanceof IRadResistantBlock r
                && r.isRadResistant(level, new BlockPos((cx << 4) + x, (sy << 4) + y, (cz << 4) + z));
    }
}
