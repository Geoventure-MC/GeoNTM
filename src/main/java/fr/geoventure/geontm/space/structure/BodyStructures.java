package fr.geoventure.geontm.space.structure;

import fr.geoventure.geontm.config.StructureConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import org.jspecify.annotations.Nullable;

/**
 * Where hbm-space's NBT structures generate on each celestial body: its per-dimension spawn lists
 * ({@code NBTStructure.registerStructure(dimension, spawn)} / {@code registerNullWeight(dimension, weight)}, in
 * registration order) and 1.7's region pick over them ({@code GenStructure.getSpawnAtCoords} + {@code findSpawn}).
 *
 * <p><b>The lists.</b> {@code PlanetGen} built {@code WorldGeneratorCelestial} first, whose constructor registered the
 * meteor dungeon (weight 1, {@code rootHeight >= 0}) on every space dimension, then each body's generator, which
 * registered its structures and null weight:
 * <pre>
 * Mun     meteor_dungeon 1, mun_base 6, null 18          Duna    meteor_dungeon 1, duna_comms 6, null 18
 * Minmus  meteor_dungeon 1, minmus_base 6, null 18       Ike     meteor_dungeon 1, ike_artifact 4, null 12
 * Moho    meteor_dungeon 1, moho_base 4, null 20         Dres    meteor_dungeon 1, dres_rbmk 8, null 16
 * Eve     meteor_dungeon 1, null 24                      Tekto   meteor_dungeon 1, null 24
 * Laythe  meteor_dungeon 1, laythe_nuke_sub 6, laythe_vertibird 6, laythe_crashed_vertibird 6
 * Thatmo  meteor_dungeon 1   (WorldGeneratorThatmo was never registered: no null weight, no Thatmo structures)
 * </pre>
 *
 * <p><b>The pick</b>, 1.7's, bit for bit: regions of {@code structureMaxChunks} chunks; a region's random is seeded
 * {@code rx * 341873128712 + rz * 132897987541 + seed + 996996996 - dimension} (the body's 1.7 dimension id, hbm-space's
 * {@code SpaceConfig} default), two {@code nextInt(max - min)} pick the region's chunk, and a third
 * {@code nextInt(totalWeight)} picks among the spawns valid for the biome at the chunk's centre {@code (x*16+8, z*16+8)}
 * (1.7 {@code WorldChunkManager.getBiomeGenAt}, the body's detail biome). The structure set placement
 * ({@code hbm:config_spread}, salt {@code 996996996 - dimension}) is the first part; {@link #pick} repeats it to reach the
 * third draw, and the start is then built from that same random (1.7's {@code MapGenStructure} did), so pieces and
 * rotations follow 1.7 as well. Each structure of a body's set computes the same pick; only the winner generates.
 *
 * <p><b>Biome rules.</b> 1.7's predicates read {@code BiomeGenBase} fields; each body's biomes carry their hbm-space
 * {@code BiomeGenBase.Height} here ({@link Biome}).
 */
public final class BodyStructures {

    private BodyStructures() {}

    /** A body's 1.7 biome as the predicates saw it: name (the body's biome key), {@code rootHeight}, {@code heightVariation}. */
    public record Biome(String name, float rootHeight, float heightVariation) {}

    /** One entry of a body's spawn list; {@code spawn} null for a null weight. */
    public record Member(@Nullable String spawn, IntSupplier weight, @Nullable Predicate<Biome> canSpawn) {
        boolean valid(Biome biome) {
            return canSpawn == null || canSpawn.test(biome);
        }
    }

    /** A region's winner and the region random, after the three draws (the start's random). */
    public record Pick(Member member, WorldgenRandom random) {}

    private static final Predicate<Biome> ROOT_ABOVE_ZERO = b -> b.rootHeight() >= 0;
    private static final Predicate<Biome> FLAT = b -> b.heightVariation() < 0.1F;

    /**
     * GeoNTM : les astres d'Ad Astra qui reçoivent les structures de NTM: Space. Chaque astre reprend le sel de région
     * (« dimension id » 1.7) et la liste de structures du corps de NTM: Space qui lui correspond (Mun -> Lune, Duna ->
     * Mars, Moho -> Mercure, Dres -> Vénus, Ike et Laythe -> Glacio ; Minmus, autre lune, rejoint la Lune). Ses biomes
     * Ad Astra portent la hauteur 1.7 du biome NTM: Space le plus proche, que lisent les règles de placement.
     */
    public enum Body {
        MOON(413_015, "ad_astra:moon", Map.of("lunar_wastelands", new Biome("mun", 0.125F, 0.05F))),
        MARS(413_016, "ad_astra:mars", Map.of("martian_wastelands", new Biome("plains", 0.125F, 0.05F),
                "martian_canyon_creek", new Biome("lowlands", -0.6F, 0.01F),
                "martian_polar_caps", new Biome("polar", 0.425F, 0.05F))),
        GLACIO(413_017, "ad_astra:glacio", Map.of("glacio_snowy_barrens", new Biome("islands", 0.256F, 0.05F),
                "glacio_ice_peaks", new Biome("polar", -0.1F, 0.05F))),
        VENUS(413_019, "ad_astra:venus", Map.of("venus_wastelands", new Biome("plains", 0.625F, 0.04F),
                "infernal_venus_barrens", new Biome("canyon", -1F, 0.34F))),
        MERCURY(413_020, "ad_astra:mercury", Map.of("mercury_deltas", new Biome("plateau", 0.255F, 0.432F)));

        /** Le « dimension id » 1.7 du corps NTM: Space repris (le sel de région est {@code 996996996 - dimension}). */
        public final int dimension;
        /** La dimension Ad Astra. */
        public final String level;
        private final Map<String, Biome> biomes;
        private final List<Member> members = new ArrayList<>();

        Body(int dimension, String level, Map<String, Biome> biomes) {
            this.dimension = dimension;
            this.level = level;
            this.biomes = biomes;
        }

        /** Le sel du jeu de structures : {@code 996996996 - dimensionId} de 1.7. */
        public int salt() {
            return 996996996 - dimension;
        }

        /** Identifiant du champ {@code body} des structures ({@code moon}, {@code mars}…). */
        public String id() {
            return name().toLowerCase(Locale.ROOT);
        }

        public List<Member> members() {
            return members;
        }

        /** Le biome 1.7 du biome Ad Astra {@code path} (sans espace de noms), ou null. */
        public @Nullable Biome biomeOf(String path) {
            return biomes.get(path);
        }

        public java.util.Collection<Biome> biomes() {
            return biomes.values();
        }

        private Body add(@Nullable String spawn, int weight, @Nullable Predicate<Biome> canSpawn) {
            members.add(new Member(spawn, () -> weight, canSpawn));
            return this;
        }

        static {
            // WorldGeneratorCelestial : le donjon de météorite sur chaque astre (poids 1, rootHeight >= 0)
            for (Body body : values()) body.add(SpaceStructures.METEOR_DUNGEON, 1, ROOT_ABOVE_ZERO);
            MOON.add(SpaceStructures.MUN_BASE, 6, null).add(SpaceStructures.MINMUS_BASE, 6, null).add(null, 18, null);
            MARS.add(SpaceStructures.DUNA_COMMS, 6, FLAT).add(null, 18, null);
            GLACIO.add(SpaceStructures.IKE_ARTIFACT, 4, null)
                    .add(SpaceStructures.LAYTHE_VERTIBIRD, 6, ROOT_ABOVE_ZERO)
                    .add(SpaceStructures.LAYTHE_CRASHED_VERTIBIRD, 6, ROOT_ABOVE_ZERO)
                    .add(null, 18, null);
            VENUS.add(SpaceStructures.DRES_RBMK, 8, b -> b.name().equals("plains")).add(null, 16, null);
            MERCURY.add(SpaceStructures.MOHO_BASE, 4, null).add(null, 20, null);
        }

        public static @Nullable Body byId(String id) {
            for (Body body : values()) if (body.id().equals(id)) return body;
            return null;
        }
    }

    /** {@code StructureConfig.structureMaxChunks}, as {@code hbm:config_spread} clamps it. */
    public static int spacing() {
        return Math.max(2, StructureConfig.structureMaxChunks);
    }

    /** {@code StructureConfig.structureMinChunks}, as {@code hbm:config_spread} clamps it. */
    public static int separation() {
        return Math.max(0, Math.min(StructureConfig.structureMinChunks, spacing() - 1));
    }

    /**
     * 1.7's pick for chunk {@code (chunkX, chunkZ)} of {@code body}: null unless the chunk is its region's chunk and a
     * spawn (or a null weight) wins for {@code biome}. The returned random has made the three draws.
     */
    public static @Nullable Pick pick(Body body, long seed, int chunkX, int chunkZ, Biome biome) {
        int spacing = spacing();
        int rx = Math.floorDiv(chunkX, spacing);
        int rz = Math.floorDiv(chunkZ, spacing);
        WorldgenRandom rand = new WorldgenRandom(new LegacyRandomSource(0L));
        rand.setLargeFeatureWithSalt(seed, rx, rz, body.salt());
        int x = rx * spacing + rand.nextInt(spacing - separation());
        int z = rz * spacing + rand.nextInt(spacing - separation());
        if (x != chunkX || z != chunkZ) return null;

        int total = 0;
        for (Member member : body.members) if (member.valid(biome)) total += member.weight().getAsInt();
        if (total <= 0) return null;
        int weight = rand.nextInt(total);
        for (Member member : body.members) {
            if (!member.valid(biome)) continue;
            weight -= member.weight().getAsInt();
            if (weight < 0) return new Pick(member, rand);
        }
        return null;
    }

    /** Le biome 1.7 au centre du chunk {@code (chunkX, chunkZ)} : le biome Ad Astra lu par la source de biomes. */
    public static @Nullable Biome biomeAt(Body body, net.minecraft.world.level.levelgen.structure.Structure.GenerationContext context,
                                          int chunkX, int chunkZ) {
        int x = chunkX * 16 + 8, z = chunkZ * 16 + 8;
        var holder = context.biomeSource().getNoiseBiome(net.minecraft.core.QuartPos.fromBlock(x),
                net.minecraft.core.QuartPos.fromBlock(64), net.minecraft.core.QuartPos.fromBlock(z), context.randomState().sampler());
        return holder.unwrapKey().map(k -> body.biomeOf(k.location().getPath())).orElse(null);
    }
}
