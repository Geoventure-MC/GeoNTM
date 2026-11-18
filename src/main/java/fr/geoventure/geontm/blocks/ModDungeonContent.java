package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.blocks.generic.BlockPedestal;
import fr.geoventure.geontm.inventory.ModDungeonMenus;
import fr.geoventure.geontm.items.ModDungeonItems;
import fr.geoventure.geontm.tileentity.ModDungeonBlockEntities;
import fr.geoventure.geocore.platform.Lifecycle;
import net.minecraft.server.level.ServerLevel;

/**
 * One bootstrap hook for the dungeon/structure family ({@link ModDungeonBlocks}): the forgotten and jungle-temple
 * bricks, the keyholes, the pedestal and skeleton holder, the loot pile, the old lantern, the dungeon spawner, the
 * action blocks, the cart rails, rebar and the rebar placer. Also expires the pedestals' charm registrations each
 * tick (1.7 {@code ModEventHandler} called {@code BlockPedestal.checkPedestalEntries} per world tick).
 *
 * <p>The jungle dungeon, forgotten dungeon and Red Room worldgen that places these blocks is not ported: the port
 * has no structure system those generators could run on.
 */
public final class ModDungeonContent {

    private ModDungeonContent() {}

    public static void init() {
        ModDungeonBlocks.init();
        ModDungeonItems.init();
        ModDungeonBlockEntities.init();
        ModDungeonMenus.init();
        DungeonDeclarations.declare();

        Lifecycle.onServerTick(server -> {
            for (ServerLevel level : server.getAllLevels()) {
                BlockPedestal.checkPedestalEntries(level.dimension(), level.getGameTime());
            }
        });
        Lifecycle.onServerStopping(server -> BlockPedestal.pedestalEntries.clear());
    }
}
