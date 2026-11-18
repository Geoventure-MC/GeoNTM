package fr.geoventure.geontm.platform.neoforge;

//? if neoforge {

/*import fr.geoventure.geontm.client.GeoNtmClientRegistrations;
import fr.geoventure.geontm.packet.GeoNtmPackets;
//? if >=26.1 {
/^import fr.geoventure.geontm.client.render.MachineItemModelRenderer;
^///?}
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
//? if >=26.1 {
/^import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
^///?}
import net.minecraft.resources.ResourceLocation;

/^*
 * Client-only NeoForge setup. Isolated in its own class so it is never loaded on a dedicated server
 * (it references client-only screen/renderer classes). Wired from the mod constructor behind a
 * Dist.CLIENT guard.
 ^/
public final class NeoForgeClientInit {

    private NeoForgeClientInit() {}

    public static void init(IEventBus modBus) {
        GeoNtmPackets.registerClientHandlers();
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(
                (net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent.LoggingOut event) ->
                        fr.geoventure.geontm.handler.JetpackHandler.clearClientState());
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(
                (net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent.LoggingOut event) ->
                        fr.geoventure.geontm.client.gun.GunSmokeClient.onDisconnect());
        fr.geoventure.geontm.client.particle.GeoNtmParticleHandlers.register();
        fr.geoventure.geontm.client.particle.SingularityParticleHandlers.register();
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

        // Look-overlay HUD (battery charge / duct type / ...), a GUI layer drawn above the vanilla HUD,
        // so its text is extracted/submitted in the same pass (mirrors Fabric's HudElementRegistry).
        modBus.addListener((RegisterGuiLayersEvent event) -> {
            GeoNtmClientRegistrations.registerHud((id, renderer) -> event.registerAboveAll(
                    fr.geoventure.geocore.Ids.of("geontm", id),
                    (graphics, deltaTracker) -> renderer.accept(graphics)));
        });

        GeoNtmClientRegistrations.installScreensAndRenderers();
        // Block tint sources (sellafite shade tint), GeoCore picks the era's colour API and the moment
        // this loader allows registering one.
        GeoNtmClientRegistrations.registerBlockColors();

        // OBJ machine item models. 26.1 has a special-model system and registers a codec for them; below
        // it the equivalent hook is a BEWLR hung off the item's client extensions, fed by the same item
        // definitions parsed on resource reload.
        //? if >=26.1 {
        /^modBus.addListener((RegisterSpecialModelRendererEvent event) ->
                event.register(MachineItemModelRenderer.ID, MachineItemModelRenderer.Unbaked.MAP_CODEC));
        modBus.addListener((RegisterSpecialModelRendererEvent event) ->
                event.register(fr.geoventure.geontm.client.jetpack.JetpackGliderItemModelRenderer.ID,
                        fr.geoventure.geontm.client.jetpack.JetpackGliderItemModelRenderer.Unbaked.MAP_CODEC));
        modBus.addListener((RegisterSpecialModelRendererEvent event) ->
                event.register(fr.geoventure.geontm.client.render.RBMKColumnItemModelRenderer.ID,
                        fr.geoventure.geontm.client.render.RBMKColumnItemModelRenderer.Unbaked.MAP_CODEC));
        modBus.addListener((RegisterSpecialModelRendererEvent event) ->
                event.register(fr.geoventure.geontm.client.render.RBMKBlockColumnItemModelRenderer.ID,
                        fr.geoventure.geontm.client.render.RBMKBlockColumnItemModelRenderer.Unbaked.MAP_CODEC));
        // Guns are a whole item model rather than a special model: only ItemModel.update is handed the
        // display context, and a first-person weapon body is drawn in camera space, not in an item slot.
        modBus.addListener((net.neoforged.neoforge.client.event.RegisterItemModelsEvent event) ->
                event.register(fr.geoventure.geocore.Ids.of("geontm", "gun"),
                        fr.geoventure.geontm.client.render.weapon.GunItemModel.Unbaked.MAP_CODEC));
        // Ported 1.7 IItemRenderers: an item model too, because they branch on the display context.
        modBus.addListener((net.neoforged.neoforge.client.event.RegisterItemModelsEvent event) ->
                event.register(fr.geoventure.geontm.client.render.MachineItemModelRenderer.CustomItem.ID,
                        fr.geoventure.geontm.client.render.MachineItemModelRenderer.CustomItem.Unbaked.MAP_CODEC));
        modBus.addListener((net.neoforged.neoforge.client.event.RegisterItemModelsEvent event) ->
                event.register(MachineItemModelRenderer.ID, MachineItemModelRenderer.Unbaked.MAP_CODEC));
        modBus.addListener((net.neoforged.neoforge.client.event.RegisterItemModelsEvent event) ->
                event.register(fr.geoventure.geontm.client.jetpack.JetpackGliderItemModelRenderer.ID,
                        fr.geoventure.geontm.client.jetpack.JetpackGliderItemModelRenderer.Unbaked.MAP_CODEC));
        ^///?} else {
        modBus.addListener((net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent event) ->
                event.registerReloadListener(
                        (net.minecraft.server.packs.resources.ResourceManagerReloadListener)
                                fr.geoventure.geontm.client.render.MachineItemModels::reload));

        modBus.addListener((net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent event) -> {
            var extension = new net.neoforged.neoforge.client.extensions.common.IClientItemExtensions() {
                private net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer renderer;

                @Override
                public net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    if (renderer == null) {
                        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
                        renderer = new net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer(
                                mc.getBlockEntityRenderDispatcher(), mc.getEntityModels()) {
                            @Override
                            public void renderByItem(net.minecraft.world.item.ItemStack stack,
                                                     net.minecraft.world.item.ItemDisplayContext context,
                                                     com.mojang.blaze3d.vertex.PoseStack poseStack,
                                                     net.minecraft.client.renderer.MultiBufferSource buffers,
                                                     int light, int overlay) {
                                fr.geoventure.geontm.client.render.MachineItemRenderers.render(stack, context, poseStack, buffers, light, overlay);
                            }
                        };
                    }
                    return renderer;
                }
            };
            // Registering for every item of a bound namespace is harmless: vanilla only consults the custom renderer for
            // items whose baked model reports isCustomRenderer(), i.e. those the build gave a
            // builtin/entity parent, which is exactly the machine_obj set.
            // HBM's namespace and every addon namespace listed in MachineItemNamespaces; the event is the only
            // window, so binding stops with it
            fr.geoventure.geontm.client.render.MachineItemNamespaces.bind(namespace ->
                    net.minecraft.core.registries.BuiltInRegistries.ITEM.entrySet().stream()
                            .filter(e -> e.getKey().location().getNamespace().equals(namespace))
                            .forEach(e -> event.registerItem(extension, e.getValue())));
            fr.geoventure.geontm.client.render.MachineItemNamespaces.bind(null);
        });
        //?}
    }
}
*///?}
