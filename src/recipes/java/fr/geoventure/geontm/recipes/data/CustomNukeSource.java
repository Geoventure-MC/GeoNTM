package fr.geoventure.geontm.recipes.data;

import fr.geoventure.geontm.api.data.MachineDataMaps;
import fr.geoventure.geontm.api.data.MachineDataMaps.NukeEntry;
import fr.geoventure.geontm.tileentity.bomb.TileEntityNukeCustom.EnumBombType;
import fr.geoventure.geontm.tileentity.bomb.TileEntityNukeCustom.EnumEntryType;
import fr.geoventure.geocore.data.DataMapSource;
import java.util.Map;
import java.util.TreeMap;

/**
 * 1.7 {@code TileEntityNukeCustom.registerBombItems}, line for line. The fluid cells ({@code cell_deuterium},
 * {@code cell_tritium}, {@code cell_antimatter}, {@code cell_sas3}, {@code cell_anti_schrabidium}) are one cell item in
 * the port, so they go to the fluid-keyed file. 1.7's {@code entries} was a {@code HashMap}; both files list their
 * entries by id.
 */
public final class CustomNukeSource {

    private CustomNukeSource() {}

    private static final Map<String, NukeEntry> ITEMS = new TreeMap<>();
    private static final Map<String, NukeEntry> CELLS = new TreeMap<>();

    private static void entry(String id, EnumBombType type, float value) {
        ITEMS.put(id, new NukeEntry(type, value, EnumEntryType.ADD));
    }

    private static void entry(String id, EnumBombType type, float value, EnumEntryType op) {
        ITEMS.put(id, new NukeEntry(type, value, op));
    }

    /** A 1.7 fluid cell item, keyed by the fluid it holds. */
    private static void cell(String fluid, EnumBombType type, float value) {
        CELLS.put(fluid, new NukeEntry(type, value, EnumEntryType.ADD));
    }

    private static synchronized void registerBombItems() {
        if (!ITEMS.isEmpty()) return;

        entry("minecraft:gunpowder", EnumBombType.TNT, 0.8F);
        entry("minecraft:tnt", EnumBombType.TNT, 4F);
        entry("geontm:det_cord", EnumBombType.TNT, 1.5F);
        entry("geontm:ingot_semtex", EnumBombType.TNT, 8F);
        entry("geontm:det_charge", EnumBombType.TNT, 15F);
        entry("geontm:red_barrel", EnumBombType.TNT, 2.5F);
        entry("geontm:pink_barrel", EnumBombType.TNT, 4F);
        entry("geontm:custom_tnt", EnumBombType.TNT, 10F);

        entry("geontm:ingot_u233", EnumBombType.NUKE, 15F);
        entry("geontm:ingot_u235", EnumBombType.NUKE, 15F);
        entry("geontm:ingot_pu239", EnumBombType.NUKE, 25F);
        entry("geontm:ingot_pu241", EnumBombType.NUKE, 25F);
        entry("geontm:ingot_neptunium", EnumBombType.NUKE, 30F);
        entry("geontm:nugget_u233", EnumBombType.NUKE, 1.5F);
        entry("geontm:nugget_u235", EnumBombType.NUKE, 1.5F);
        entry("geontm:nugget_pu239", EnumBombType.NUKE, 2.5F);
        entry("geontm:nugget_pu241", EnumBombType.NUKE, 2.5F);
        entry("geontm:nugget_neptunium", EnumBombType.NUKE, 3.0F);
        entry("geontm:powder_neptunium", EnumBombType.NUKE, 30F);
        entry("geontm:custom_nuke", EnumBombType.NUKE, 30F);

        cell("geontm:deuterium", EnumBombType.HYDRO, 20F);
        cell("geontm:tritium", EnumBombType.HYDRO, 30F);
        entry("geontm:lithium", EnumBombType.HYDRO, 20F);
        entry("geontm:custom_hydro", EnumBombType.HYDRO, 30F);

        cell("geontm:amat", EnumBombType.AMAT, 5F);
        entry("geontm:custom_amat", EnumBombType.AMAT, 15F);
        entry("geontm:egg_balefire_shard", EnumBombType.AMAT, 15F);
        entry("geontm:egg_balefire", EnumBombType.AMAT, 150F);

        entry("geontm:ingot_tungsten", EnumBombType.DIRTY, 1F);
        entry("geontm:custom_dirty", EnumBombType.DIRTY, 10F);

        entry("geontm:ingot_schrabidium", EnumBombType.SCHRAB, 5F);
        entry("geontm:block_schrabidium", EnumBombType.SCHRAB, 50F);
        entry("geontm:nugget_schrabidium", EnumBombType.SCHRAB, 0.5F);
        entry("geontm:powder_schrabidium", EnumBombType.SCHRAB, 5F);
        cell("geontm:sas3", EnumBombType.SCHRAB, 7.5F);
        cell("geontm:aschrab", EnumBombType.SCHRAB, 15F);
        entry("geontm:custom_schrab", EnumBombType.SCHRAB, 15F);

        entry("geontm:nugget_euphemium", EnumBombType.EUPH, 1F);
        entry("geontm:ingot_euphemium", EnumBombType.EUPH, 1F);

        entry("minecraft:redstone", EnumBombType.TNT, 1.05F, EnumEntryType.MULT);
        entry("minecraft:redstone_block", EnumBombType.TNT, 1.5F, EnumEntryType.MULT);

        entry("geontm:ingot_uranium", EnumBombType.NUKE, 1.05F, EnumEntryType.MULT);
        entry("geontm:ingot_plutonium", EnumBombType.NUKE, 1.15F, EnumEntryType.MULT);
        entry("geontm:ingot_u238", EnumBombType.NUKE, 1.1F, EnumEntryType.MULT);
        entry("geontm:ingot_pu238", EnumBombType.NUKE, 1.15F, EnumEntryType.MULT);
        entry("geontm:nugget_uranium", EnumBombType.NUKE, 1.005F, EnumEntryType.MULT);
        entry("geontm:nugget_plutonium", EnumBombType.NUKE, 1.15F, EnumEntryType.MULT);
        entry("geontm:nugget_u238", EnumBombType.NUKE, 1.01F, EnumEntryType.MULT);
        entry("geontm:nugget_pu238", EnumBombType.NUKE, 1.015F, EnumEntryType.MULT);
        entry("geontm:powder_uranium", EnumBombType.NUKE, 1.05F, EnumEntryType.MULT);
        entry("geontm:powder_plutonium", EnumBombType.NUKE, 1.15F, EnumEntryType.MULT);

        entry("geontm:ingot_pu240", EnumBombType.DIRTY, 1.05F, EnumEntryType.MULT);
        entry("geontm:nuclear_waste", EnumBombType.DIRTY, 1.025F, EnumEntryType.MULT);
        entry("geontm:block_waste", EnumBombType.DIRTY, 1.25F, EnumEntryType.MULT);
        entry("geontm:yellow_barrel", EnumBombType.DIRTY, 1.2F, EnumEntryType.MULT);
    }

    /** The item-keyed file. */
    public static final class Items extends DataMapSource<NukeEntry> {

        public Items() {
            super(MachineDataMaps.CUSTOM_NUKE, false);
        }

        @Override
        protected void values() {
            registerBombItems();
            ITEMS.forEach(this::put);
        }
    }

    /** The fluid-keyed file: the 1.7 cells by the fluid they hold. */
    public static final class Cells extends DataMapSource<NukeEntry> {

        public Cells() {
            super(MachineDataMaps.CUSTOM_NUKE_CELL, false);
        }

        @Override
        protected void values() {
            registerBombItems();
            CELLS.forEach(this::put);
        }
    }
}
