package fr.geoventure.geontm.platform.forge;

//? if forge {

import fr.geoventure.geontm.client.GeoNtmClientRegistrations;
import fr.geoventure.geontm.packet.BlockEntitySyncPayload;
import fr.geoventure.geontm.packet.EntityPropsSyncPayload;
import fr.geoventure.geontm.packet.GeoNtmPackets;
import fr.geoventure.geocore.net.GeoCoreNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/** Client bootstrap, isolated from {@link ForgeInit} so dedicated servers never load client classes. */
final class ForgeClientInit {

    private ForgeClientInit() {}

    static void init(IEventBus modBus) {
        GeoNtmPackets.registerClientHandlers();
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(
                (net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggingOut event) ->
                        fr.geoventure.geontm.handler.JetpackHandler.clearClientState());
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(
                (net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggingOut event) ->
                        fr.geoventure.geontm.client.gun.GunSmokeClient.onDisconnect());
        fr.geoventure.geontm.client.particle.GeoNtmParticleHandlers.register();
        fr.geoventure.geontm.client.particle.SingularityParticleHandlers.register();
        GeoCoreNetwork.setClientContextSupplier(() -> new GeoCoreNetwork.ClientContext(
                Minecraft.getInstance().level, Minecraft.getInstance().player));
        GeoCoreNetwork.flushClientRegistrations();

        fr.geoventure.geontm.client.ClientGameTime.install();
        // The material-shape sprites are computed onto the atlas, not shipped; this has to be declared
        // before the first resource load. The fluid droplets are the same, one per registered fluid.
        fr.geoventure.geontm.client.AutogenSprites.install();
        fr.geoventure.geontm.client.FluidSprites.install();
        GeoNtmClientRegistrations.installDebugLines();
        GeoNtmClientRegistrations.installTints();
        GeoNtmClientRegistrations.installItemModelProperties();
        GeoNtmClientRegistrations.installKeybinds();
        GeoNtmClientRegistrations.installGunAnimations();
        GeoNtmClientRegistrations.installEntityEffects();
        GeoNtmClientRegistrations.installArmorLayers();
        fr.geoventure.geocore.render.ForgeItemRenderers.install(fr.geoventure.geontm.client.render.MachineItemRenderers::render);
        modBus.addListener((net.minecraftforge.client.event.RegisterClientReloadListenersEvent event) ->
                event.registerReloadListener(
                        (net.minecraft.server.packs.resources.ResourceManagerReloadListener) resources -> {
                            fr.geoventure.geontm.client.render.MachineItemModels.reload(resources);
                        }));
        GeoNtmClientRegistrations.registerBlockColors();
        GeoNtmClientRegistrations.installScreensAndRenderers();
        // Core's Hud.draw owns the per-layer composition rule: on 1.20.1 each layer must start on a clear
        // depth buffer, or the chat's z-50 lines hide every GEONTM overlay drawn where they are (the
        // power-armour charge bars vanished that way while 26.1 drew them).
        modBus.addListener((RegisterGuiOverlaysEvent event) -> {
            GeoNtmClientRegistrations.registerHud((id, renderer) ->
                    event.registerAboveAll(id, (gui, graphics, partialTick, width, height) ->
                            fr.geoventure.geocore.client.Hud.draw(graphics, renderer::accept)));
        });
    }
}
//?}
