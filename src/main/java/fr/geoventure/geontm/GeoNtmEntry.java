package fr.geoventure.geontm;

import fr.geoventure.geontm.api.data.GeoNtmDataMaps;
import fr.geoventure.geontm.api.script.GeoNtmScripts;
import fr.geoventure.geontm.config.GeneralConfig;
import fr.geoventure.geontm.config.MachineDynConfig;
import fr.geoventure.geontm.handler.ExplosivesEntityHooks;
import fr.geoventure.geontm.hazard.Hazards;
import fr.geoventure.geontm.inventory.fluid.FluidProperties;
import fr.geoventure.geontm.inventory.recipes.GeoNtmRecipeTables;
import fr.geoventure.geontm.itempool.ItemPool;
import fr.geoventure.geontm.items.ModFluidItems;
import fr.geoventure.geontm.items.machine.ItemFluidIcon;
import fr.geoventure.geontm.itempool.PoolFill;
import fr.geoventure.geontm.packet.GeoNtmPackets;
import fr.geoventure.geontm.tileentity.bomb.TileEntityLaunchPadBase;
import fr.geoventure.geocore.platform.Lifecycle;
import fr.geoventure.geocore.platform.ModEntry;
import fr.geoventure.geocore.viewer.FluidIcons;
import fr.geoventure.geocore.world.EntityLoot;
import fr.geoventure.geocore.world.EntityTicks;

/**
 * HBM's common start-up, in GeoCore's three phases (see {@link fr.geoventure.geocore.platform.ModEntries}): the same on
 * every loader, and the same path an addon's own entry takes. NeoForge and Forge start it from their {@code @Mod}
 * constructor; Fabric's {@code FabricInit} is the {@code geocore:entry} and delegates here.
 *
 * <p><b>Order an addon can rely on</b> (ADDON-API-INVENTORY S-01, S-02): an addon's own {@code ModEntry} runs each
 * phase after HBM's, because GeoCore's {@code ModEntries} runs entries in dependency order (Fabric: the addon lists
 * {@code geontm} under {@code depends}; NeoForge/Forge: its {@code mods.toml} dependency on {@code geontm} has
 * {@code ordering = "AFTER"}, so its constructor, which starts the entry, follows HBM's). So in the addon's
 * {@code declare()} HBM's fluids ({@code Fluids.register(ns, ...)}), materials ({@code Mats.register}), bullet configs
 * ({@code BulletConfigs.register(ResourceLocation, ...)}) and registers already exist, and in its {@code registered()} HBM's
 * hazards, armor protection and launch table are filled: an addon entry added there extends or overrides them.
 */
public class GeoNtmEntry implements ModEntry {

    @Override
    public void declare() {
        CommonClass.init();
        GeoNtmPackets.declare();
        GeoNtmRecipeTables.declare();
        GeoNtmDataMaps.declare();
        ItemPool.declare();
        PoolFill.register();
        EntityLoot.register(); // geocore:looting, read by the entity loot tables
        FluidProperties.declare(); // fluid_property + fluid_property_patch
        GeoNtmScripts.declare(); // the GEONTM script global and GEONTMEvents (inert without KubeJS)
    }

    @Override
    public void registered() {
        fr.geoventure.geontm.pont.PontGeoventure.installer();
        EntityTicks.AFTER.register(ExplosivesEntityHooks::tick);
        // after every mod's declare(), so an addon's MachineDynConfig.register is in the file too
        MachineDynConfig.initialize();
        Lifecycle.onServerStarting(server -> {
            MachineDynConfig.restoreLocal();
            GeneralConfig.restoreLocalRecipeSwitches();
        });
        // Item and material hazards name registered items.
        Hazards.init();
        // the missile item -> entity table, its own start-up step (separate from Hazards.init)
        TileEntityLaunchPadBase.registerLaunchables();
        FluidIcons.declare(ModFluidItems.fluid_icon.get(), ItemFluidIcon::viewerFluid);
        // materials requested by startup scripts (GEONTMEvents.registry), once every register is filled
        GeoNtmScripts.registered();
    }
}
