package fr.geoventure.geontm.packet;

import com.google.gson.JsonObject;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.capability.GeoNtmLivingProps;
import fr.geoventure.geontm.client.AbilityToolClientRegistrations;
import fr.geoventure.geontm.client.DroneClientRegistrations;
import fr.geoventure.geontm.client.GunEffectState;
import fr.geoventure.geontm.client.PneumaticClientRegistrations;
import fr.geoventure.geontm.client.PowerGenClientRegistrations;
import fr.geoventure.geontm.client.RenderInputClientRegistrations;
import fr.geoventure.geontm.client.SmogFog;
import fr.geoventure.geontm.client.anim.GunAnimationHandler;
import fr.geoventure.geontm.client.crucible.SlicedMobs;
import fr.geoventure.geontm.client.particle.GluonParticles;
import fr.geoventure.geontm.client.particle.PlasmaBlastClient;
import fr.geoventure.geontm.explosion.vanillant.standard.ExplosionEffectStandard;
import fr.geoventure.geontm.client.render.armor.ModelMan;
import fr.geoventure.geontm.config.ClientConfig;
import fr.geoventure.geontm.config.GeneralConfig;
import fr.geoventure.geontm.config.MachineDynConfig;
import fr.geoventure.geontm.extprop.GeoNtmPlayerProps;
import fr.geoventure.geontm.handler.EnumCraneKey;
import fr.geoventure.geontm.handler.GeoNtmKeybinds;
import fr.geoventure.geontm.handler.GeoNtmKeybindsServer;
import fr.geoventure.geontm.handler.ImpactWorldHandler;
import fr.geoventure.geontm.handler.JetpackHandler;
import fr.geoventure.geontm.interfaces.IControlReceiver;
import fr.geoventure.geontm.inventory.container.ContainerAnvil;
import fr.geoventure.geontm.inventory.recipes.anvil.AnvilRecipes;
import fr.geoventure.geontm.items.weapon.sedna.GunAnimation;
import fr.geoventure.geontm.particle.helper.CasingCreator;
import fr.geoventure.geontm.particle.helper.GeoNtmEffectNT;
import fr.geoventure.geontm.tileentity.IBufPacketReceiver;
import fr.geoventure.geontm.util.AchievementHandler;
import fr.geoventure.geontm.util.InventoryUtil;
import fr.geoventure.geocore.data.Tags;
import fr.geoventure.geocore.net.GeoCoreBuffers;
import fr.geoventure.geocore.net.GeoCoreCodec;
import fr.geoventure.geocore.net.GeoCoreNetwork;
import fr.geoventure.geocore.net.GeoCorePayload;
import fr.geoventure.geocore.net.GeoCoreSync;
import fr.geoventure.geocore.platform.Lifecycle;
import fr.geoventure.geocore.render.Instancing;
import fr.geoventure.geocore.world.Interactions;
import fr.geoventure.geocore.world.Tracking;
import io.netty.buffer.Unpooled;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Loader-agnostic handling for HBM's custom packets. Each loader registers
 * {@link BlockEntitySyncPayload#TYPE}/{@link BlockEntitySyncPayload#CODEC} with its own networking API
 * and routes received payloads here on the client main thread.
 */
public final class GeoNtmPackets {

    private GeoNtmPackets() {}

    /** Declares HBM's play payloads once; loaders flush these declarations at their own hook. */
    public static void declare() {
        GeoCoreSync.install();
        CasingCreator.setSender((level, event) -> {
            if (!(level instanceof ServerLevel server)) return;
            if (event.gun()) {
                sendGunCasing(server, new GunCasingEjectPayload(event.x(), event.y(), event.z(), event.mX(),
                        event.mY(), event.mZ(), event.yaw(), event.pitch(), event.mPitch(), event.mYaw(),
                        event.casing(), event.smoking(), event.smokeLife(), event.smokeLift(), event.nodeLife(),
                        event.legacy()));
            } else {
                sendCasing(server, new CasingEjectPayload(event.x(), event.y(), event.z(), event.mX(), event.mY(),
                        event.mZ(), event.yaw(), event.pitch(), event.mPitch(), event.mYaw(), event.casing(),
                        event.smoking(), event.smokeLife(), event.smokeLift(), event.nodeLife()));
            }
        });
        GeoCoreNetwork.declarePlayToClient(BlockEntitySyncPayload.CODEC);
        GeoCoreNetwork.declarePlayToClient(EntityPropsSyncPayload.CODEC);
        GeoCoreNetwork.declarePlayToClient(ImpactSyncPayload.CODEC);
        GeoCoreNetwork.declarePlayToClient(GemRenderNoticePayload.CODEC);
        GeoCoreNetwork.declarePlayToClient(MachineConfigPayload.CODEC);
        GeoCoreNetwork.declarePlayToClient(RecipeSwitchesPayload.CODEC);
        Lifecycle.onDatapackSync((server, players) -> {
            RecipeSwitchesPayload switches = new RecipeSwitchesPayload(GeneralConfig.recipeSwitches());
            for (ServerPlayer player : players) GeoCoreNetwork.sendToPlayer(player, RecipeSwitchesPayload.CODEC, switches);
        });
        Lifecycle.onPlayerJoin((player, firstTime) -> {
            GeoCoreNetwork.sendToPlayer(player, GemRenderNoticePayload.CODEC, new GemRenderNoticePayload());
            GeoCoreNetwork.sendToPlayer(player, MachineConfigPayload.CODEC, new MachineConfigPayload(MachineDynConfig.values()));
            DuckPayload.onJoin(player);
        });
        GeoCoreNetwork.registerPlayToServer(DuckPayload.CODEC, (payload, player) -> DuckPayload.handle(player));
        GeoCoreNetwork.declarePlayToClient(PollutionSyncPayload.CODEC);
        GeoCoreNetwork.declarePlayToClient(ManlySyncPayload.CODEC);
        GeoCoreNetwork.registerPlayToServer(ControlEventPayload.CODEC,
                (payload, player) -> handleControl(player, payload));
        GeoCoreNetwork.registerPlayToServer(AnvilCraftPayload.CODEC,
                (payload, player) -> handleAnvilCraft(player, payload));
        GeoCoreNetwork.registerPlayToServer(ManualDesignatorPayload.CODEC,
                (payload, player) -> ManualDesignatorPayload.handle(player, payload));
        GeoCoreNetwork.declarePlayToClient(GunAnimationPayload.CODEC);
        GeoCoreNetwork.declarePlayToClient(PlasmaBlastPayload.CODEC);
        GeoCoreNetwork.declarePlayToClient(MuzzleFlashPayload.CODEC);
        GeoCoreNetwork.declarePlayToClient(GunSoundPayload.CODEC);
        GeoCoreNetwork.declarePlayToClient(CasingEjectPayload.CODEC);
        GeoCoreNetwork.declarePlayToClient(GunCasingEjectPayload.CODEC);
        GeoCoreNetwork.declarePlayToClient(ParticlePayload.CODEC);
        GeoCoreNetwork.declarePlayToClient(JetpackSyncPayload.CODEC);
        Tracking.onStart((entity, observer) -> {
            if (entity instanceof ServerPlayer player) JetpackHandler.sendStateTo(player, observer);
        });
        GeoCoreNetwork.declarePlayToClient(JetpackExhaustPayload.CODEC);
        GeoCoreNetwork.declarePlayToClient(ProjectileStatePayload.CODEC);
        GeoCoreNetwork.declarePlayToClient(FallingMultiblockPayload.CODEC);
        GeoCoreNetwork.declarePlayToClient(ExplosionBlocksPayload.CODEC);
        GeoCoreNetwork.registerPlayToServer(JetpackInputPayload.CODEC,
                (payload, player) -> JetpackHandler.handleInput(player, payload));
        GeoCoreNetwork.registerPlayToServer(KeybindPayload.CODEC,
                (payload, player) -> {
                    GeoNtmKeybinds.EnumKeybind keybind =
                            GeoNtmKeybinds.EnumKeybind.byOrdinal(payload.keybind());
                    if (keybind != null) {
                        GeoNtmKeybindsServer.onPressedServer(player, keybind, payload.state());
                    }
                });
        GeoCoreNetwork.registerPlayToServer(CraneKeyPayload.CODEC,
                (payload, player) -> handleCraneKey(player, payload));
        GeoCoreNetwork.registerPlayToServerOffThread(NetworkProbePayload.CODEC,
                NetworkProbePayload::received);
    }

    /** Tells one player's client that their gun played an animation cue. */
    public static void sendGunAnimation(ServerPlayer player, ItemStack stack,
            GunAnimation animation, int configIndex) {
        sendGunAnimation(player, stack, animation, InteractionHand.MAIN_HAND, 0, configIndex);
    }

    /** Sends an animation cue while retaining the source receiver/rail and config index. */
    public static void sendGunAnimation(ServerPlayer player, ItemStack stack,
            GunAnimation animation, int receiver, int configIndex) {
        sendGunAnimation(player, stack, animation, handOf(player, stack), receiver, configIndex);
    }

    /** Sends an animation cue for the exact hand that owns {@code stack}. */
    public static void sendGunAnimation(ServerPlayer player, ItemStack stack,
            GunAnimation animation, InteractionHand hand, int receiver,
            int configIndex) {
        GeoCoreNetwork.sendToPlayer(player, GunAnimationPayload.CODEC,
                new GunAnimationPayload(animation.ordinal(), receiver, configIndex, hand,
                        BuiltInRegistries.ITEM.getKey(stack.getItem()).toString()));
    }

    private static InteractionHand handOf(ServerPlayer player, ItemStack stack) {
        if (player.getItemInHand(InteractionHand.OFF_HAND) == stack) return InteractionHand.OFF_HAND;
        return InteractionHand.MAIN_HAND;
    }

    /** Sends a seeded gun sound to the same tracking population as the source gun packet. */
    public static void sendGunSound(ServerLevel level, boolean legacy, SoundEvent sound, SoundSource source,
            double x, double y, double z, float volume, float pitch, long seed) {
        GunSoundPayload.send(level, legacy, sound, source, x, y, z, volume, pitch, seed);
    }

    /** Sends the observer muzzle edge; the firing player's own animation packet handles the local gun. */
    public static void sendMuzzleFlash(LivingEntity entity) {
        if (entity.level() instanceof ServerLevel level) {
            MuzzleFlashPayload payload = new MuzzleFlashPayload(entity.getId());
            double radiusSquared = 100D * 100D;
            for (ServerPlayer player : level.players()) {
                if (player.distanceToSqr(entity) <= radiusSquared)
                    GeoCoreNetwork.sendToPlayer(player, MuzzleFlashPayload.CODEC, payload);
            }
        }
    }

    /** Sends an ordinary casing snapshot to the tracking population. */
    public static void sendCasing(ServerLevel level, CasingEjectPayload payload) {
        sendToRadius(level, payload.x(), payload.y(), payload.z(), 50D, CasingEjectPayload.CODEC, payload);
    }

    /** Sends a mode-filtered gun casing snapshot to the tracking population. */
    public static void sendGunCasing(ServerLevel level, GunCasingEjectPayload payload) {
        sendToRadius(level, payload.x(), payload.y(), payload.z(), 50D, GunCasingEjectPayload.CODEC, payload);
    }

    private static <P extends GeoCorePayload> void sendToRadius(ServerLevel level,
            double x, double y, double z, double radius, GeoCoreCodec<P> codec, P payload) {
        double radiusSquared = radius * radius;
        for (ServerPlayer player : level.players()) {
            if (player.distanceToSqr(x, y, z) <= radiusSquared) GeoCoreNetwork.sendToPlayer(player, codec, payload);
        }
    }

    /** Attaches client handlers only after the physical client is available. */
    public static void registerClientHandlers() {
        GeoCoreNetwork.registerPlayToClient(BlockEntitySyncPayload.CODEC,
                (payload, context) -> handleSync(context.level(), payload));
        GeoCoreNetwork.registerPlayToClient(EntityPropsSyncPayload.CODEC,
                (payload, context) -> handlePropsSync(context.player(), payload));
        GeoCoreNetwork.registerPlayToClient(GemRenderNoticePayload.CODEC, (payload, context) -> {
            if (!Instancing.isAvailable()) {
                context.player().sendSystemMessage(Component.translatable("geontm.gemrender.missing").withStyle(ChatFormatting.GOLD));
            }
        });
        GeoCoreNetwork.registerPlayToClient(MachineConfigPayload.CODEC, (payload, context) ->
                MachineDynConfig.apply(MachineDynConfig.gson.fromJson(payload.json(), JsonObject.class)));
        GeoCoreNetwork.registerPlayToClient(RecipeSwitchesPayload.CODEC,
                (payload, context) -> GeneralConfig.applyRecipeSwitches(payload.bits()));
        GeoCoreNetwork.registerPlayToClient(ImpactSyncPayload.CODEC, (payload, context) -> {
            // 1.7 PermaSyncHandler.readPacket: the values belong to the level the player is in
            ImpactWorldHandler.lastSyncWorld = context.level();
            ImpactWorldHandler.fire = payload.fire();
            ImpactWorldHandler.dust = payload.dust();
            ImpactWorldHandler.impact = payload.impact();
        });
        GeoCoreNetwork.registerPlayToClient(PollutionSyncPayload.CODEC, (payload, context) -> {
            // 1.7 PermaSyncHandler.readPacket: the pollution of the region the player is in
            SmogFog.pollution[0] = payload.soot();
            SmogFog.pollution[1] = payload.poison();
            SmogFog.pollution[2] = payload.heavyMetal();
            SmogFog.pollution[3] = payload.fallout();
        });
        GeoCoreNetwork.registerPlayToClient(ManlySyncPayload.CODEC, (payload, context) -> ModelMan.setPlayers(payload.ids()));
        GeoCoreNetwork.registerPlayToClient(GunAnimationPayload.CODEC,
                (payload, context) -> GunAnimationHandler.handle(payload));
        GeoCoreNetwork.registerPlayToClient(PlasmaBlastPayload.CODEC,
                (payload, context) -> handlePlasmaBlast(context.level(), payload));
        GeoCoreNetwork.registerPlayToClient(MuzzleFlashPayload.CODEC,
                (payload, context) -> GunEffectState.markMuzzleFlash(payload));
        GeoCoreNetwork.registerPlayToClient(GunSoundPayload.CODEC, GunSoundPayload::handleClient);
        GeoCoreNetwork.registerPlayToClient(CasingEjectPayload.CODEC,
                (payload, context) -> GunEffectState.handleCasing(payload));
        GeoCoreNetwork.registerPlayToClient(GunCasingEjectPayload.CODEC,
                (payload, context) -> {
                    if (payload.legacy() == ClientConfig.GUN_ANIMS_LEGACY)
                        GunEffectState.handleGunCasing(payload);
                });
        GeoCoreNetwork.registerPlayToClient(ParticlePayload.CODEC,
                (payload, context) -> handleParticle(context.level(), payload));
        GeoCoreNetwork.registerPlayToClient(JetpackSyncPayload.CODEC,
                (payload, context) -> JetpackHandler.dispatchClientSync(context.level(), payload));
        GeoCoreNetwork.registerPlayToClient(JetpackExhaustPayload.CODEC,
                (payload, context) -> JetpackHandler.dispatchClientExhaust(context.level(), payload));
        GeoCoreNetwork.registerPlayToClient(ProjectileStatePayload.CODEC,
                (payload, context) -> payload.apply(context.level()));
        // 1.12 PacketSpecialDeath: one packet, the effect id picks the client effect
        GeoCoreNetwork.registerPlayToClient(SpecialDeathPayload.CODEC, (payload, context) -> {
            if (payload.effectId() == SpecialDeathPayload.SLICED) SlicedMobs.handle(payload);
            else GluonParticles.handle(payload);
        });
        GeoCoreNetwork.registerPlayToClient(FallingMultiblockPayload.CODEC,
                (payload, context) -> FallingMultiblockPayload.handleClient(context.level(), payload));
        GeoCoreNetwork.registerPlayToClient(ExplosionBlocksPayload.CODEC, (payload, context) -> ExplosionEffectStandard.performClient(
                context.level(), payload.x(), payload.y(), payload.z(), payload.size(), payload.blocks()));
        PneumaticClientRegistrations.registerPacketHandlers();
        DroneClientRegistrations.registerPacketHandlers();
        PowerGenClientRegistrations.registerPacketHandlers();
        RenderInputClientRegistrations.registerPacketHandlers(); // G-C: tool animation cues
        AbilityToolClientRegistrations.registerPacketHandlers();
    }

    /**
     * Applies a received {@link BlockEntitySyncPayload}: resolves the tile in the given (client) level
     * and replays the byte blob into its {@link IBufPacketReceiver#deserialize}. Must run on the main
     * client thread (each loader's receiver is responsible for that). Failures are swallowed with a
     * warning: a malformed/underflowing buffer must never tear down the netty pipeline.
     */
    public static void handleSync(Level level, BlockEntitySyncPayload payload) {
        handleSync(level, payload.pos(), payload.data());
    }

    public static void handleSync(Level level, BlockPos pos, byte[] data) {
        if (level == null) return;
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof IBufPacketReceiver receiver) {
            var buf = GeoCoreBuffers.wrap(Unpooled.wrappedBuffer(data), level);
            try {
                receiver.deserialize(buf);
            } catch (Exception e) {
                Constants.LOG.warn("Failed to apply BlockEntitySyncPayload at {}: {}", pos, e.toString());
            }
        }
    }

    /**
     * Applies a received {@link ParticlePayload}: resolves the named effect and runs its client
     * handler. Must run on the main client thread (each loader's receiver is responsible for that).
     *
     * <p>An unknown effect name is ignored rather than logged: a server with more effects than this
     * client knows is a version skew, not a bug worth spamming the log for. A handler that throws is
     * caught and warned about: a bad particle must never tear down the netty pipeline, as with
     * {@link #handleSync}.
     */
    public static void handleParticle(Level level, ParticlePayload payload) {
        if (level == null) return;
        GeoNtmEffectNT effect =
                GeoNtmEffectNT.byName(payload.effect());
        if (effect == null) return;
        try {
            effect.summonParticle(level, payload.x(), payload.y(), payload.z(), payload.data());
        } catch (Exception e) {
            Constants.LOG.warn("Failed to play particle effect {}: {}", payload.effect(), e.toString());
        }
    }

    /**
     * Rebuilds the source shockwave using Core's oriented additive particle path. The body lives in a
     * client class: a {@code new ParticlePlasmaBlast(...)} here made the dedicated server's verifier load
     * {@code net.minecraft.client.particle.Particle} while verifying this class and refuse to construct the mod.
     */
    public static void handlePlasmaBlast(Level level, PlasmaBlastPayload payload) {
        PlasmaBlastClient.handle(level, payload);
    }

    /**
     * Applies a received {@link EntityPropsSyncPayload} to the local (client) player's duck props.
     * Must run on the client main thread. {@code player} is the client player handed over by the
     * loader's receiver context.
     */
    public static void handlePropsSync(Player player, EntityPropsSyncPayload payload) {
        handlePropsSync(player, payload.data());
    }

    public static void handlePropsSync(Player player, byte[] data) {
        if (player == null) return;
        try {
            GeoNtmLivingProps.getData(player).deserialize(Unpooled.wrappedBuffer(data));
        } catch (Exception e) {
            Constants.LOG.warn("Failed to apply EntityPropsSyncPayload: {}", e.toString());
        }
    }

    /**
     * Applies a received {@link AnvilCraftPayload} server-side (the anvil's construction crafting).
     * Everything the client claims is re-validated here: the recipe id, that the player actually has
     * an anvil menu open, and the anvil's tier (read from the server's own world via the container).
     * Ingredients are pulled from (and outputs given to) the player's inventory, like the original.
     */
    public static void handleAnvilCraft(ServerPlayer player, AnvilCraftPayload payload) {
        handleAnvilCraft(player, payload.recipeId(), payload.mode());
    }

    /** Crafts per click: one, or with shift enough for one stack of the output. */
    public static int anvilCraftCount(AnvilRecipes.AnvilConstructionRecipe recipe, int mode) {
        if (mode != 1) return 1;
        ItemStack first = recipe.output.get(0).stack;
        return recipe.output.size() > 1 ? 64 : first.getMaxStackSize() / first.getCount();
    }

    public static void handleAnvilCraft(ServerPlayer player, String recipeId, int mode) {
        if (player == null || recipeId == null) return;
        AnvilRecipes.AnvilConstructionRecipe recipe =
                AnvilRecipes.constructionById(recipeId);
        if (recipe == null) return;
        if (!(player.containerMenu instanceof ContainerAnvil anvil)) return;

        if (!recipe.isTierValid(anvil.tier)) return;

        int count = anvilCraftCount(recipe, mode);
        for (int i = 0; i < count; i++) {
            if (InventoryUtil.doesPlayerHaveAStacks(player, recipe.input, true)) {
                InventoryUtil.giveChanceStacksToPlayer(player, recipe.output);
                AchievementHandler.fire(player, recipe.output.get(0).stack); // 1.7 AnvilCraftPacket
            } else {
                break;
            }
        }

        player.inventoryMenu.broadcastChanges();
        player.containerMenu.broadcastChanges();
    }

    /**
     * Applies a received {@link ControlEventPayload} server-side: first enforces the 24-block absolute
     * envelope, then lets the receiver apply its own permission policy and marks accepted changes dirty.
     */
    public static void handleControl(ServerPlayer player, ControlEventPayload payload) {
        handleControl(player, payload.pos(), payload.data());
    }

    public static void handleCraneKey(ServerPlayer player, CraneKeyPayload payload) {
        if (player == null || payload.key() < 0 || payload.key() >= EnumCraneKey.VALUES.length) return;
        GeoNtmPlayerProps.getData(player).setCraneKeyPressed(
                EnumCraneKey.VALUES[payload.key()], payload.pressed());
    }

    public static void handleControl(ServerPlayer player, BlockPos pos, CompoundTag data) {
        if (player == null || pos == null || data == null || data.sizeInBytes() > 16 * 1024
                || containsNestedCompound(data)) return;
        if (player.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) > 24D * 24D) return;
        Level level = player.level();
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof IControlReceiver receiver && receiver.hasPermission(player)) {
            // a protected area that cancels right-clicking the block also refuses its menu's controls
            if (!Interactions.allowsUse(player, pos)) return;
            try {
                receiver.receiveControl(player, data);
                be.setChanged();
            } catch (Exception e) {
                Constants.LOG.warn("Failed to apply ControlEventPayload at {}: {}", pos, e.toString());
            }
        }
    }

    /** Control packets are flat key/value schemas; nested tags have no receiver contract. */
    private static boolean containsNestedCompound(CompoundTag data) {
        for (String key : Tags.keys(data)) {
            if (data.get(key) instanceof CompoundTag
                    || data.get(key) instanceof ListTag) return true;
        }
        return false;
    }
}
