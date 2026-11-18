package fr.geoventure.geontm.platform.neoforge;

//? if neoforge {

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geocore.registry.RegistryObject;

import fr.geoventure.geontm.hazard.HazardSystem;
import fr.geoventure.geontm.util.TooltipColours;
import fr.geoventure.geontm.handler.radiation.RadiationSystemNT;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@Mod(Constants.MOD_ID)
public class NeoForgeInit {

    public NeoForgeInit(IEventBus eventBus) {

        Constants.LOG.info("Hello NeoForge world!");

        // HBM's common start-up (GeoCore fills every mod's registers from its own RegisterEvent listener).
        fr.geoventure.geocore.platform.ModEntries.start(new fr.geoventure.geontm.GeoNtmEntry());

        // Populate our creative tabs from the common contents map.
        eventBus.addListener(this::onBuildTabContents);


        // Client-only setup (screen registration). Isolated so the server never loads client classes.
        if (fr.geoventure.geocore.platform.Platform.isClient()) {
            NeoForgeClientInit.init(eventBus);
        }

        // Power/network engine driver: tick the nodespace each server tick, clear it on level unload.
        // The radiation engine ticks on the same hook (both are server-thread, end-of-tick).
        NeoForge.EVENT_BUS.addListener((ServerTickEvent.Pre event) ->
                fr.geoventure.geontm.tileentity.network.RTTYSystem.updateBroadcastQueue(event.getServer()));
        NeoForge.EVENT_BUS.addListener((ServerTickEvent.Post event) -> {
            RadiationSystemNT.tickServer(event.getServer());
        });
        NeoForge.EVENT_BUS.addListener((LevelEvent.Unload event) -> {
            if (event.getLevel() instanceof ServerLevel level) {
                RadiationSystemNT.closeWorld(level);
                fr.geoventure.geontm.tileentity.network.RTTYSystem.clearLevel(level);
            }
        });

        // Radiation chunk lifecycle: (re)build pockets on load, persist on unload.
        NeoForge.EVENT_BUS.addListener((ChunkEvent.Load event) -> {
            if (event.getLevel() instanceof ServerLevel level && event.getChunk() instanceof LevelChunk chunk)
                RadiationSystemNT.onChunkLoaded(level, chunk);
        });
        NeoForge.EVENT_BUS.addListener((ChunkEvent.Unload event) -> {
            if (event.getLevel() instanceof ServerLevel level && event.getChunk() instanceof LevelChunk chunk)
                RadiationSystemNT.onChunkUnloaded(level, chunk);
        });

        // Hazard tooltips (radioactive/hot/etc. traits on items).
        NeoForge.EVENT_BUS.addListener((ItemTooltipEvent event) -> {
            TooltipColours.apply(event.getItemStack(), event.getToolTip());
            HazardSystem.addHazardInfo(event.getItemStack(), event.getEntity(), event.getToolTip());
        });
        NeoForge.EVENT_BUS.addListener((EntityJoinLevelEvent event) -> {
            fr.geoventure.geontm.items.special.ItemNuclearWaste.preserveDroppedEntity(event.getEntity());
            fr.geoventure.geontm.handler.pollution.PollutionHandler.decorateMob(event.getEntity());
        });

        NeoForge.EVENT_BUS.addListener((ServerStartedEvent event) -> {
            fr.geoventure.geontm.config.AutocalConfig.load(event.getServer());
            Constants.LOG.info("[GEONTM] Loaded {} total recipes", event.getServer().getRecipeManager().getRecipes().size());
        });
    }

    private void onBuildTabContents(BuildCreativeModeTabContentsEvent event) {
        ModTabs.accept(event.getTabKey(), event::accept);
    }
}
//?}
