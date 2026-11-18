package fr.geoventure.geontm.platform.fabric;

//? if fabric {

/*import fr.geoventure.geontm.client.GeoNtmClientRegistrations;
import fr.geoventure.geontm.packet.BlockEntitySyncPayload;
import fr.geoventure.geontm.packet.EntityPropsSyncPayload;
import fr.geoventure.geontm.packet.GeoNtmPackets;
import fr.geoventure.geocore.net.GeoCoreNetwork;
import fr.geoventure.geontm.hazard.HazardSystem;
import fr.geoventure.geontm.util.TooltipColours;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

/^* Fabric client entrypoint: registers GEONTM menu screens, BlockEntity renderers and packet receivers. ^/
public class FabricClientInit implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        GeoNtmPackets.registerClientHandlers();
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.DISCONNECT.register((handler, client) ->
                fr.geoventure.geontm.handler.JetpackHandler.clearClientState());
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.DISCONNECT.register((handler, client) ->
                fr.geoventure.geontm.client.gun.GunSmokeClient.onDisconnect());
        fr.geoventure.geontm.client.particle.GeoNtmParticleHandlers.register();
        fr.geoventure.geontm.client.particle.SingularityParticleHandlers.register();
        GeoCoreNetwork.flushClientRegistrations();

        // Item tint colours (fluid icons, scraps, ...). 26.1 declares these in the item definitions and
        // the engine applies them; below it GeoCore wires the loader's colour registry to the same
        // stored colour. One call, every version, both loaders.
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

        // Look-overlay HUD (battery charge / duct type / ...), drawn after the vanilla HUD each frame.
        GeoNtmClientRegistrations.registerHud((id, renderer) ->
                fr.geoventure.geocore.client.Hud.addLast("geontm", id, renderer::accept));

        GeoNtmClientRegistrations.installScreensAndRenderers();
        // 26.1: block tint sources (sellafite shade tint): fabric-api has no colour registry there, so
        // register directly on the vanilla BlockColors once the client instance is fully constructed.
        // Below 26.1: OBJ machine item models. 26.1 drives those through its special-model system
        // (registered by MixinSpecialModelRenderers); here the item definitions are parsed on reload and
        // every item of a MachineItemNamespaces namespace gets a dynamic renderer. Only items whose generated model parents onto
        // builtin/entity actually reach it, so registering broadly is free.
        GeoNtmClientRegistrations.registerBlockColors();
//? if <26.1 {

        /^net.fabricmc.fabric.api.resource.ResourceManagerHelper
                .get(net.minecraft.server.packs.PackType.CLIENT_RESOURCES)
                .registerReloadListener(new net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener() {
                    @Override
                    public net.minecraft.resources.Identifier getFabricId() {
                        return fr.geoventure.geocore.Ids.of(
                                fr.geoventure.geontm.Constants.MOD_ID, "machine_item_models");
                    }

                    @Override
                    public void onResourceManagerReload(net.minecraft.server.packs.resources.ResourceManager resources) {
                        fr.geoventure.geontm.client.render.MachineItemModels.reload(resources);
                    }
                });

        // HBM's namespace and every addon namespace listed in MachineItemNamespaces (bound as it is added)
        fr.geoventure.geontm.client.render.MachineItemNamespaces.bind(namespace ->
                net.minecraft.core.registries.BuiltInRegistries.ITEM.entrySet().stream()
                        .filter(e -> e.getKey().location().getNamespace().equals(namespace))
                        .forEach(e -> net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry.INSTANCE
                                .register(e.getValue(), (stack, mode, poseStack, buffers, light, overlay) ->
                                        fr.geoventure.geontm.client.render.MachineItemRenderers.render(stack, mode, poseStack, buffers, light, overlay))));
        ^///?}

        // Hazard tooltips (radioactive/hot/etc. traits on items).
        fr.geoventure.geocore.client.Tooltips.onItemTooltip((stack, lines) -> {
            TooltipColours.apply(stack, lines);
            HazardSystem.addHazardInfo(stack, net.minecraft.client.Minecraft.getInstance().player, lines);
        });
    }
}
*///?}
