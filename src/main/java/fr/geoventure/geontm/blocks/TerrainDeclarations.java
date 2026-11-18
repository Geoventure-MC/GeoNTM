package fr.geoventure.geontm.blocks;

import fr.geoventure.geocore.dejson.Crafting;

/**
 * The terrain family's packing recipes and grass tint; every block's models, blockstate, item model, loot and
 * tags are stated on its registration in {@link ModTerrainBlocks} / {@code ModTerrainItems} (Reg). Touches no
 * Minecraft class (it runs in a plain JVM from {@code dejson.gradle.kts}).
 *
 * <p>Authored recipes: the painted-waste, rope and strong-radaway recipes under {@code data/geontm/recipe/}.
 */
public final class TerrainDeclarations {

    private static final String NS = "geontm";
    /**
     * 1.7 {@code ColorizerGrass.getGrassColor(0.5, 1.0)} for the tobacco/hemp tint, as a constant: 26.1
     * tints are state-keyed and cannot read the biome (see {@code fr.geoventure.geocore.client.BlockTint}).
     */
    public static final int GRASS_TINT = 0x63A948;

    private static boolean declared;

    private TerrainDeclarations() {}

    public static void declare() {
        if (declared) return;
        declared = true;
        recipes();
    }

    /** {@code MineralRecipes.add1To9Pair} and the block/nine-item pairs of this family. */
    private static void recipes() {
        pair("block_tcalloy", "geontm:ingot_tcalloy");
        pair("block_cdalloy", "geontm:ingot_cdalloy");
        pair("block_coke_coal", "geontm:coke_coal");
        pair("block_coke_lignite", "geontm:coke_lignite");
        pair("block_coke_petroleum", "geontm:coke_petroleum");
        pair("block_fallout", "geontm:falloutitem");
        pair("block_waste", "geontm:nuclear_waste");
        pair("block_australium", "geontm:ingot_australium");
        pair("block_yellowcake", "geontm:powder_yellowcake");
        pair("block_lithium", "geontm:lithium");
    }

    private static void pair(String block, String item) {
        Crafting.compress(NS, "terrain_" + block, item, "geontm:" + block);
        Crafting.decompress(NS, "terrain_" + block + "_unpack", "geontm:" + block, item);
    }
}
