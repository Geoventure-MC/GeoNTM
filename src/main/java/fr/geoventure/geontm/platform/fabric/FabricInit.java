package fr.geoventure.geontm.platform.fabric;

//? if fabric {

/*import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.registry.Tabs;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geontm.items.ModFuelItems;
import fr.geoventure.geontm.packet.AnvilCraftPayload;
import fr.geoventure.geontm.handler.radiation.RadiationSystemNT;
import fr.geoventure.geocore.platform.ModEntry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import fr.geoventure.geontm.packet.BlockEntitySyncPayload;
import fr.geoventure.geontm.packet.ControlEventPayload;
import fr.geoventure.geontm.packet.EntityPropsSyncPayload;
import net.minecraft.core.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

/^*
 * GEONTM on Fabric: the {@code geocore:entry} entrypoint (not {@code main}). GeoCore runs every GeoCore-based mod's
 * declarations in dependency order, then fills the registries, entity attributes, spawn placements and payloads once
 * for all of them, then runs {@link #registered} (see {@code fr.geoventure.geocore.platform.ModEntries}).
 ^/
public class FabricInit implements ModEntry {

    private final fr.geoventure.geontm.GeoNtmEntry common = new fr.geoventure.geontm.GeoNtmEntry();

    @Override
    public void declare() {
        Constants.LOG.info("Hello Fabric world!");
        common.declare();
    }

    @Override
    public void registered() {
        // Furnace burn times for the ItemFuel family. NeoForge reads these from the `neoforge:furnace_fuels`
        // data map (plain JSON in common resources, ignored by Fabric); Fabric has no data map for fuels, so
        // add them to the FuelValues builder as it is assembled.
        fr.geoventure.geocore.registry.Fuels.register(ModFuelItems.BURN_TIMES);

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            fr.geoventure.geontm.config.AutocalConfig.load(server);
            Constants.LOG.info("[GEONTM] Loaded {} total recipes", server.getRecipeManager().getRecipes().size());
        });
        ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
            fr.geoventure.geontm.items.special.ItemNuclearWaste.preserveDroppedEntity(entity);
            fr.geoventure.geontm.handler.pollution.PollutionHandler.decorateMob(entity);
        });

        // Worldgen: the biome modifiers ModBiomeModifiers declares reach Fabric through GeoCore's
        // BiomeFeatures.install(), called from MainRegistry right after the declaration.

        // Every loader consumes the same listing, including addon positions and component-bearing variants.
        ModTabs.CONTENTS.keySet().forEach(tabKey ->
                Tabs.modifyEntries(tabKey, output -> ModTabs.accept(tabKey, output::accept)));

        // Power/network engine driver: tick the nodespace each server tick, clear it on level unload.
        // The radiation engine ticks on the same hook.
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.START_SERVER_TICK.register(
                fr.geoventure.geontm.tileentity.network.RTTYSystem::updateBroadcastQueue);
        fr.geoventure.geocore.platform.Lifecycle.onServerTick(server -> {
            RadiationSystemNT.tickServer(server);
        });
        fr.geoventure.geocore.platform.Lifecycle.onLevelUnload((server, level) -> {
            RadiationSystemNT.closeWorld(level);
            fr.geoventure.geontm.tileentity.network.RTTYSystem.clearLevel(level);
        });

        // Radiation chunk lifecycle: (re)build pockets on load, persist on unload. Unload is 2-arg on
        // both lines; Load grew a "newly generated" flag in the 26.1 fabric-api, so its SAM differs.
        fr.geoventure.geocore.platform.Lifecycle.onChunkLoad((level, chunk) -> RadiationSystemNT.onChunkLoaded(level, chunk));
        fr.geoventure.geocore.platform.Lifecycle.onChunkUnload((level, chunk) -> RadiationSystemNT.onChunkUnloaded(level, chunk));


        // Item and material hazards, now that items exist.
        common.registered();
    }

}
*///?}
