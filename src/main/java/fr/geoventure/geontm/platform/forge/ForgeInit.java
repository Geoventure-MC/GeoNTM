package fr.geoventure.geontm.platform.forge;

//? if forge {

import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geontm.hazard.HazardSystem;
import fr.geoventure.geontm.util.TooltipColours;
import fr.geoventure.geontm.packet.AnvilCraftPayload;
import fr.geoventure.geontm.packet.ControlEventPayload;
import fr.geoventure.geontm.handler.radiation.RadiationSystemNT;
import fr.geoventure.geocore.registry.RegistryObject;
import java.util.Set;
import java.util.TreeSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

/** Forge 1.20.1 bootstrap; client code is only referenced after the physical-side check. */
@Mod(fr.geoventure.geontm.Constants.MOD_ID)
public final class ForgeInit {

    public ForgeInit() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        fr.geoventure.geocore.platform.ModEntries.start(new fr.geoventure.geontm.GeoNtmEntry());

        modBus.addListener(this::onBuildTabContents);

        // Forge 1.20.1 has no furnace-fuel data map: the burn times answer FurnaceFuelBurnTimeEvent (lazily
        // resolved, so registering before the registries flush is fine).
        fr.geoventure.geocore.registry.Fuels.register(fr.geoventure.geontm.items.ModFuelItems.BURN_TIMES);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ForgeClientInit.init(modBus);
        }

        MinecraftForge.EVENT_BUS.addListener((TickEvent.ServerTickEvent event) -> {
            if (event.phase == TickEvent.Phase.START) {
                fr.geoventure.geontm.tileentity.network.RTTYSystem.updateBroadcastQueue(event.getServer());
            }
            if (event.phase == TickEvent.Phase.END) {
                RadiationSystemNT.tickServer(event.getServer());
            }
        });
        MinecraftForge.EVENT_BUS.addListener((LevelEvent.Unload event) -> {
            if (event.getLevel() instanceof ServerLevel level) {
                RadiationSystemNT.closeWorld(level);
                fr.geoventure.geontm.tileentity.network.RTTYSystem.clearLevel(level);
            }
        });
        MinecraftForge.EVENT_BUS.addListener((ChunkEvent.Load event) -> {
            if (event.getLevel() instanceof ServerLevel level && event.getChunk() instanceof LevelChunk chunk) {
                RadiationSystemNT.onChunkLoaded(level, chunk);
            }
        });
        MinecraftForge.EVENT_BUS.addListener((ChunkEvent.Unload event) -> {
            if (event.getLevel() instanceof ServerLevel level && event.getChunk() instanceof LevelChunk chunk) {
                RadiationSystemNT.onChunkUnloaded(level, chunk);
            }
        });
        MinecraftForge.EVENT_BUS.addListener((ItemTooltipEvent event) -> {
            TooltipColours.apply(event.getItemStack(), event.getToolTip());
            HazardSystem.addHazardInfo(event.getItemStack(), event.getEntity(), event.getToolTip());
        });
        MinecraftForge.EVENT_BUS.addListener((EntityJoinLevelEvent event) -> {
            fr.geoventure.geontm.items.special.ItemNuclearWaste.preserveDroppedEntity(event.getEntity());
            fr.geoventure.geontm.handler.pollution.PollutionHandler.decorateMob(event.getEntity());
        });
        MinecraftForge.EVENT_BUS.addListener((ServerStartedEvent event) -> {
            fr.geoventure.geontm.config.AutocalConfig.load(event.getServer());
            fr.geoventure.geontm.Constants.LOG.info("[GEONTM] Loaded {} total recipes",
                    event.getServer().getRecipeManager().getRecipes().size());
            auditWorldgen(event.getServer());
        });
    }

    private void onBuildTabContents(BuildCreativeModeTabContentsEvent event) {
        ModTabs.accept(event.getTabKey(), event::accept);
    }

    private static void auditWorldgen(MinecraftServer server) {
        Set<ResourceLocation> features = new TreeSet<>();
        server.registryAccess().registryOrThrow(Registries.BIOME).holders().forEach(biome ->
                biome.value().getGenerationSettings().features().forEach(step -> step.forEach(feature ->
                        feature.unwrapKey().map(ResourceKey::location)
                                .filter(id -> id.getNamespace().equals(fr.geoventure.geontm.Constants.MOD_ID))
                                .ifPresent(features::add))));
        // every geontm placed feature named by a loaded forge:add_features modifier must reach a biome
        Set<ResourceLocation> expected = new TreeSet<>();
        server.registryAccess().registryOrThrow(net.minecraftforge.registries.ForgeRegistries.Keys.BIOME_MODIFIERS)
                .forEach(modifier -> {
                    if (modifier instanceof net.minecraftforge.common.world.ForgeBiomeModifiers.AddFeaturesBiomeModifier add) {
                        add.features().forEach(feature -> feature.unwrapKey().map(ResourceKey::location)
                                .filter(id -> id.getNamespace().equals(fr.geoventure.geontm.Constants.MOD_ID))
                                .ifPresent(expected::add));
                    }
                });
        Set<ResourceLocation> missing = new TreeSet<>(expected);
        missing.removeAll(features);
        if (missing.isEmpty()) {
            fr.geoventure.geontm.Constants.LOG.info("[GEONTM] Forge biome modifiers injected all {} modifier features ({} geontm placed features in biomes): {}",
                    expected.size(), features.size(), features);
        } else {
            fr.geoventure.geontm.Constants.LOG.error("[GEONTM] Forge biome modifiers injected {}/{} modifier features; missing {}",
                    expected.size() - missing.size(), expected.size(), missing);
        }
    }
}
//?}
