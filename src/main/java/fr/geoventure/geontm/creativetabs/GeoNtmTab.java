package fr.geoventure.geontm.creativetabs;

import fr.geoventure.geocore.registry.RegistryObject;
import fr.geoventure.geocore.registry.TabTarget;
import net.minecraft.world.item.Item;


/**
 * The GEONTM creative tabs, mirroring the tabs from the 1.12.2 mod (see {@code MainRegistry}). The
 * {@link #id} is used for the registry name and the {@code itemGroup.geontm.<id>} translation key.
 */
public enum GeoNtmTab implements TabTarget {

    MATERIALS("materials"),     // partsTab: ingots, nuggets, wires, machine parts
    CONTROL("control"),         // controlTab: items that belong in machines, fuels, etc.
    TEMPLATES("templates"),     // templateTab: templates, siren tracks
    RESOURCES("resources"),     // resourceTab: ore and mineral blocks
    BLOCKS("blocks"),           // blockTab: construction blocks
    MACHINES("machines"),       // machineTab: machines, structure parts
    BOMBS("bombs"),             // nukeTab: bombs
    MISSILES("missiles"),       // missileTab: missiles, satellites
    WEAPONS("weapons"),         // weaponTab: turrets, weapons, ammo
    CONSUMABLES("consumables"); // consumableTab: drinks, kits, tools

    public final String id;

    GeoNtmTab(String id) {
        this.id = id;
    }

    /** A registration chain's {@code .tab(GeoNtmTab.X)}; an addon lists its items in HBM's tabs the same way. */
    @Override
    public void list(RegistryObject<? extends Item> item) {
        ModTabs.addTo(this, item);
    }
}
