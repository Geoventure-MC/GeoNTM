package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.inventory.ModNuclearMiscMenus;
import fr.geoventure.geontm.items.ModNuclearMiscItems;
import fr.geoventure.geontm.items.machine.ItemReactorSensor;
import fr.geoventure.geontm.items.special.ItemDemonCore;
import fr.geoventure.geontm.items.special.ItemUnstable;
import fr.geoventure.geontm.tileentity.ModNuclearMiscBlockEntities;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineRadiolysis;
import java.util.Objects;


/**
 * One common bootstrap hook for the miscellaneous nuclear machines and fuel items: the RTG radiolysis
 * chamber, the legacy breeding and research reactors with their remote control, the legacy graphite
 * pile, breeding rods, pile rods, spent plate fuel and the odd nuclear specials.
 */
public final class ModNuclearMiscContent {
    private ModNuclearMiscContent() {}

    public static void init() {
        ModNuclearMiscBlocks.init();
        ModNuclearMiscItems.init();
        ModNuclearMiscBlockEntities.init();
        ModNuclearMiscMenus.init();
        // Components declared as static fields of item/tile classes only exist once the class is
        // initialised; a constructor reference does not do that, so touch them here, before the
        // loaders flush the data-component register (same reason MainRegistry calls ItemHot.init()).
        Objects.requireNonNull(ItemReactorSensor.TARGET);
        Objects.requireNonNull(ItemUnstable.TIMER);
        Objects.requireNonNull(TileEntityMachineRadiolysis.CONTAGION);
        // 1.7 ItemDemonCore.onEntityItemUpdate is driven by the loaders' per-item tick hooks
        // (fr.geoventure.geontm.platform.*Init) next to ItemSingularity/ItemCell/ItemAntimatterPellet.
    }
}
