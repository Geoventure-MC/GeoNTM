package fr.geoventure.geontm.api.data;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.config.FalloutConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.data.DataFiles;
import fr.geoventure.geocore.data.DataMaps;
import fr.geoventure.geocore.data.DataMaps.DataMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * What explosions and radiation do to blocks, as data: the eroding blast's table ({@code geontm:erosion}), radiation's
 * surface decay ({@code geontm:radiation_decay}) and the fallout transform folder ({@code data/<ns>/fallout_transform/},
 * {@link FalloutConfig}).
 */
public final class WorldDataMaps {

    private WorldDataMaps() {}

    private static <V> DataMap<Block, V> block(String name, Codec<V> codec) {
        return DataMaps.map(WorldDataMaps.class, Constants.MOD_ID, name, "block", () -> BuiltInRegistries.BLOCK, codec);
    }

    /** Block ids by name, resolved at use time ({@code minecraft:air} for an unknown one). */
    public static Block blockOf(String id) {
        return BuiltInRegistries.BLOCK.getOptional(Ids.parse(id)).orElse(Blocks.AIR);
    }

    /** What an eroding blast (1.7 {@code ExplosionNT} {@code ERRODE}, the drill missile) leaves of a block: a block id. */
    public static final DataMap<Block, String> EROSION = block("erosion", Codec.STRING);

    /**
     * Radiation's surface decay of one block (1.7 {@code RadiationWorldHandler.decayBlock}): the block becomes {@code to}
     * with probability {@code chance} and air otherwise. A block named here follows its value instead of the built-in 1.7
     * rules (double plants, grass blocks, grass/fern, leaves), which stay code because they key on block classes; an
     * {@code "to": ""} value means the block does not decay.
     */
    public static final DataMap<Block, Decay> RADIATION_DECAY = block("radiation_decay", Decay.CODEC);

    public record Decay(String to, float chance) {
        public static final Codec<Decay> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.STRING.fieldOf("to").forGetter(Decay::to),
                Codec.FLOAT.optionalFieldOf("chance", 1F).forGetter(Decay::chance)
        ).apply(i, Decay::new));
    }

    /** Loads the class at mod construction (from {@link GeoNtmDataMaps#declare()}) and declares the fallout folder. */
    public static void declare() {
        DataFiles.folder(FalloutConfig.FOLDER, FalloutConfig::apply);
    }
}
