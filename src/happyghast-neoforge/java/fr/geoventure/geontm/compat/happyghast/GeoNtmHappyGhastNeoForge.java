package fr.geoventure.geontm.compat.happyghast;

import fr.geoventure.geontm.Constants;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * NeoForge's entry to the happy-ghast Fat Man ({@link GeoNtmHappyGhast}), found by NeoForge's annotation scan. The
 * registers are created while mods construct, before any {@code RegisterEvent}, so HBM's own flush picks them up.
 */
@EventBusSubscriber(modid = Constants.MOD_ID)
public final class GeoNtmHappyGhastNeoForge {

    private GeoNtmHappyGhastNeoForge() {}

    static {
        GeoNtmHappyGhast.init();
    }

    @SubscribeEvent
    public static void construct(FMLConstructModEvent event) {
        GeoNtmHappyGhast.init();
    }

    @SubscribeEvent
    public static void useBlock(PlayerInteractEvent.RightClickBlock event) {
        InteractionResult result = GeoNtmHappyGhast.tryRig(event.getEntity(), event.getLevel(), event.getHand(), event.getPos());
        if (result != null) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }
}
