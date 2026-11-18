package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.items.ModTerrainItems;
import fr.geoventure.geontm.tileentity.ModTerrainBlockEntities;


/**
 * Bootstrap for the natural / terrain / hazard block family ({@code fr.geoventure.geontm.terrain}): blocks and their
 * items, the plant items, the five block-entity types, then the Dejson declarations. Hazard values are
 * attached later by the geontm:hazards and geontm:radiation_emission data maps.
 */
public final class ModTerrainContent {

    private ModTerrainContent() {}

    public static void init() {
        ModTerrainBlocks.init();
        ModTerrainItems.init();
        ModTerrainBlockEntities.init();
        TerrainDeclarations.declare();
    }
}
