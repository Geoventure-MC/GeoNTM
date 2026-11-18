package fr.geoventure.geontm.particle.helper;

import fr.geoventure.geontm.packet.ParticlePayload;
import fr.geoventure.geocore.net.GeoCoreNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jspecify.annotations.Nullable;

/**
 * The catalogue of HBM's own particle effects, and the server-side entry point for playing one.
 *
 * <p>An effect is a <i>name plus a bag of parameters</i>, not a particle: {@code ExplosionSmall}
 * spawns fireballs and block debris together, and the tag it carries says how big and how
 * fast. The server never touches a particle class: it names an effect here and
 * {@link #send} puts it on the wire; the client looks the name up in its handler table and builds
 * whatever that effect means (see {@code fr.geoventure.geontm.client.particle.GeoNtmParticleHandlers}).
 *
 * <p><b>This class must stay server-safe.</b> It holds the handler as a plain {@link Object} and the
 * handler interface takes no client types, so nothing here drags {@code Minecraft} onto a dedicated
 * server's class path. (1.12.2 used {@code @SideOnly} to strip the client half out of the server jar;
 * a single source set has no equivalent.)
 *
 * <p>Only the effects this port implements are listed. Upstream declares roughly a hundred; adding
 * one is a constant here plus a handler registration on the client.
 *
 * <p>A registry of records rather than an enum (ADDON-API-INVENTORY E-046): an addon adds its own effects with
 * {@link #register} under a namespaced name and they travel in HBM's particle payload like HBM's. The constants keep
 * their 1.7 names, which are also their wire names.
 */
public final class GeoNtmEffectNT {

    /** Every effect by name, HBM's first (in declaration order), then each addon's in registration order. */
    private static final Map<String, GeoNtmEffectNT> BY_NAME = new LinkedHashMap<>();

    /** The flamethrower's cone of burning fuel. */
    public static final GeoNtmEffectNT Flamethrower = geontm("Flamethrower");
    /** A small blast: a few fireballs plus tumbling block debris. */
    public static final GeoNtmEffectNT ExplosionSmall = geontm("ExplosionSmall");
    /** A large blast: a smoke plume and scoops of the ground itself thrown into the air. */
    public static final GeoNtmEffectNT ExplosionLarge = geontm("ExplosionLarge");
    /** Muzzle smoke and sparks from a gunpowder discharge. */
    public static final GeoNtmEffectNT BlackPowder = geontm("BlackPowder");
    /** Drifting embers and ash, for things that are burning down rather than exploding. */
    public static final GeoNtmEffectNT Ashes = geontm("Ashes");
    /** The authored OBJ skeleton strip/fragment effect. */
    public static final GeoNtmEffectNT Skeleton = geontm("Skeleton");
    /** The authored meat/slime/metal giblet billboard effect. */
    public static final GeoNtmEffectNT Giblets = geontm("Giblets");
    /** The full-size RBMK meltdown cloud. */
    public static final GeoNtmEffectNT RBMKMush = geontm("RBMKMush");
    /** The jet of steam a full RBMK boiler vents. */
    public static final GeoNtmEffectNT RBMKSteam = geontm("RBMKSteam");
    /** The 1.7 small-nuke wave, flare and delayed cloud effect. */
    public static final GeoNtmEffectNT Muke = geontm("Muke");
    /** The tiny-tot wave and immediate small-nuke cloud effect. */
    public static final GeoNtmEffectNT TinyTot = geontm("TinyTot");
    /** The smaller Watz/RBMK cloud. */
    public static final GeoNtmEffectNT RBMKMushSmall = geontm("RBMKMushSmall");
    /** One puff of a missile's exhaust trail. Spawned client-side, once per tick per nozzle. */
    public static final GeoNtmEffectNT MissileContrail = geontm("MissileContrail");
    /** Redstone block debris thrown from a living entity hit by a running sawmill blade. */
    public static final GeoNtmEffectNT SawmillDamage = geontm("SawmillDamage");
    /** The drifting cooling-tower smoke puff. */
    public static final GeoNtmEffectNT Tower = geontm("Tower");
    /** One vanilla critical particle used while a refueler fills an item. */
    public static final GeoNtmEffectNT FluidFill = geontm("FluidFill");
    /** Exhaust from a fuelled jetpack. */
    public static final GeoNtmEffectNT Jetpack = geontm("Jetpack");
    /** Purple exhaust from the BJ winged power armour. */
    public static final GeoNtmEffectNT Jetpack_BJ = geontm("Jetpack_BJ");
    /** Cyan exhaust from the DNT nano suit. */
    public static final GeoNtmEffectNT Jetpack_DNS = geontm("Jetpack_DNS");
    /** The old block-destruction particle burst sent when rubble lands. */
    public static final GeoNtmEffectNT BlockBurst = geontm("BlockBurst");
    /** The large explosion's drifting smoke cloud. */
    public static final GeoNtmEffectNT Smoke_Cloud = geontm("Smoke_Cloud");
    /** The large explosion's radial shock smoke. */
    public static final GeoNtmEffectNT Smoke_Shock = geontm("Smoke_Shock");
    /** 1.7 smoke mode {@code radial}: ExSmoke thrown on a gaussian in all three axes (the naval mine). */
    public static final GeoNtmEffectNT Smoke_Radial = geontm("Smoke_Radial");
    /** 1.7 smoke mode {@code foamSplash}: rising foam bubbles (the naval mine under water). */
    public static final GeoNtmEffectNT Smoke_FoamSplash = geontm("Smoke_FoamSplash");
    /** Direct client-side block dust used by the three legacy extinguisher streams. */
    public static final GeoNtmEffectNT VanillaExt_BlockDust = geontm("VanillaExt_BlockDust");
    /**
     * The old {@code gasfire} puff sent by the miner lander's retro burn (ntm-next {@code GasFlamePayload}).
     * Tag: {@code moX/moY/moZ} motion, optional {@code scale} (6.5 when absent).
     */
    public static final GeoNtmEffectNT GasFlame = geontm("GasFlame");
    /**
     * Arc endpoints for a tesla/taint crab (ntm-next {@code TeslaArcPayload}). Tag: {@code entityID} and a
     * {@code targets} list of {@code x/y/z} doubles; the client hands them to the crab it names.
     */
    public static final GeoNtmEffectNT TeslaArc = geontm("TeslaArc");
    /** The ICF's fusion flash (1.7 {@code hadron}): a growing, fading additive quad, every reacting tick. */
    public static final GeoNtmEffectNT Hadron = geontm("Hadron");
    /** Original decontaminator's recoloured town-aura particle, emitted once per client tick. */
    public static final GeoNtmEffectNT DeconAura = geontm("DeconAura");
    /** 1.7 {@code bnuuy}: the diesel suit legs' twin smoke puffs behind the wearer. Tag: {@code player}. */
    public static final GeoNtmEffectNT Bnuuy = geontm("Bnuuy");
    /** 1.7 {@code amat}: {@code ParticleAmatFlash}, a ten-tick white starburst. Tag: {@code scale} (explosion size). */
    public static final GeoNtmEffectNT AmatFlash = geontm("AmatFlash");
    /**
     * 1.7 {@code properJolt}, sent to one player: sets its hurt time and hurt duration directly (hurt direction 0),
     * so the hurt-camera tilt lingers for {@code time} ticks. Tag: {@code time}, {@code maxTime}.
     */
    public static final GeoNtmEffectNT ProperJolt = geontm("ProperJolt");
    /**
     * 1.7 {@code tau} (G-C): {@code count} white line sparks plus one hadron flash, both halved/shortened when
     * {@code small} - the RBMK lever's contact arc. Tag: {@code count} (byte), {@code small}.
     */
    public static final GeoNtmEffectNT Tau = geontm("Tau");
    /**
     * 1.7 {@code marker} (G-C): a {@code RenderOverhead} marker, a see-through outline of the block at the position
     * with an optional label. Tag: {@code color}, {@code label}, {@code expires} (ms, 0 = never), {@code dist}.
     */
    public static final GeoNtmEffectNT Marker = geontm("Marker");
    /** 1.7 {@code debug} (G-C): {@code ParticleText}, floating text. Tag: {@code text}, {@code color}, {@code scale}. */
    public static final GeoNtmEffectNT DebugText = geontm("DebugText");
    /** 1.7 {@code launchSmoke} (G-C): one {@code ParticleSmokePlume}, played client-side by a launcher. Tag: {@code moX/moY/moZ}. */
    public static final GeoNtmEffectNT LaunchSmoke = geontm("LaunchSmoke");
    /**
     * 1.12.2 {@code VanillaBurst_BlockDust}: {@code count} block-dust particles of block id {@code block}, each with
     * motion {@code gaussian * motion} (+0.2 up) and a 50..99 tick life. The crucible's killing hit.
     */
    public static final GeoNtmEffectNT VanillaBurst_BlockDust = geontm("VanillaBurst_BlockDust");
    /** 1.7 {@code deadleaf}: one {@code ParticleDeadLeaf} drifting down from {@code waste_leaves}. Client-side only. */
    public static final GeoNtmEffectNT DeadLeaf = geontm("DeadLeaf");
    /**
     * 1.7 {@code radFog}: one {@code ParticleRadiationFog}, rolled once a second over chunks holding more than
     * {@code fogRad} ({@code RadiationFog}).
     */
    public static final GeoNtmEffectNT RadFog = geontm("RadFog");
    /**
     * 1.7 {@code sweat}: {@code count} 150..199-tick block-dust particles of block {@code block} (runtime block id)
     * scattered over entity {@code entity}'s box. Radiation, digamma, oil and the fat pigeon. Client:
     * {@code LivingEffectParticles}.
     */
    public static final GeoNtmEffectNT Sweat = geontm("Sweat");
    /**
     * 1.7 {@code vomit}: {@code count} particles from entity {@code entity}'s mouth along its look; {@code mode}
     * {@code normal} (green clay dust), {@code blood} (redstone dust) or {@code smoke} (coughed coal).
     */
    public static final GeoNtmEffectNT Vomit = geontm("Vomit");
    /** 1.7 {@code radiation}: {@code count} cyan town-aura flashes around the local player. Client-side only. */
    public static final GeoNtmEffectNT Radiation = geontm("Radiation");
    /** 1.7 {@code exhaust}: {@code count} rocket-flame puffs spread by {@code width}; {@code mode} soyuz or meteor. */
    public static final GeoNtmEffectNT Exhaust = geontm("Exhaust");
    /** 1.7 {@code haze}: one {@code ParticleHaze}, the white-phosphorus haze. */
    public static final GeoNtmEffectNT Haze = geontm("Haze");
    /** 1.7 {@code ufo}: one lone {@code ParticleMukeCloud} at the foot of the UFO's tractor beam. */
    public static final GeoNtmEffectNT Ufo = geontm("Ufo");
    /**
     * Declared after the 44 effects of E-046 so their order stays as it was.
     * 1.7 {@code vanillaExt} mode {@code cloud}: one {@code EntityCloudFX} with motion {@code mX/mY/mZ}; with
     * {@code r/g/b} the ash-glasses gas form (that colour plus 0..0.1, scale 7.5, at rest). Client-side, from
     * {@code BlockGasBase.ashGlassesCloud}.
     */
    public static final GeoNtmEffectNT VanillaExt_Cloud = geontm("VanillaExt_Cloud");
    /** 1.7 {@code network}: a {@code ParticleDebug} port marker. Tag: {@code mode} power or fluid, {@code color}, {@code mX/mY/mZ}. */
    public static final GeoNtmEffectNT Network = geontm("network");
    /** 1.7 {@code vanillaburst} mode {@code reddust}: {@code count} red dust, each moving {@code gaussian * motion}. */
    public static final GeoNtmEffectNT VanillaBurst_RedDust = geontm("VanillaBurst_RedDust");
    /** 1.7 {@code rbmkflame}: an upright additive fire quad. Tag: {@code maxAge}. */
    public static final GeoNtmEffectNT RBMKFlame = geontm("RBMKFlame");
    /**
     * 1.7 {@code vanillaExt} mode {@code largeexplode}: one orange-tinted large explosion of {@code size} and
     * {@code count} grey smoke puffs, each scaled by its index plus one. A turret's muzzle.
     */
    public static final GeoNtmEffectNT VanillaExt_LargeExplode = geontm("VanillaExt_LargeExplode");
    /**
     * 1.7 {@code casing}: one spent casing {@code name} thrown by a {@code CasingEjector} whose offset, motion and
     * angle range ride along ({@code oX/oY/oZ}, {@code mX/mY/mZ}, {@code yawFactor}, {@code pitchFactor}); {@code pitch}
     * and {@code yaw} in radians.
     */
    public static final GeoNtmEffectNT Casing = geontm("Casing");

    private final String name;

    private GeoNtmEffectNT(String name) {
        this.name = name;
    }

    private static GeoNtmEffectNT geontm(String name) {
        return add(name);
    }

    /**
     * Registers an addon's effect under {@code name}, which must be namespaced ({@code "ntmspace:spinlaunch"}); HBM's
     * own effects keep their bare 1.7 names. The server plays it with {@link #send}, the client installs its
     * behaviour with {@link #setHandler}, as for HBM's. Call at mod construction, on both sides.
     *
     * @throws IllegalArgumentException if the name has no namespace or is taken
     */
    public static GeoNtmEffectNT register(String name) {
        if (name.indexOf(':') <= 0) throw new IllegalArgumentException("Addon effect names are namespaced: " + name);
        return add(name);
    }

    private static synchronized GeoNtmEffectNT add(String name) {
        if (BY_NAME.containsKey(name)) throw new IllegalArgumentException("Effect " + name + " is registered twice");
        GeoNtmEffectNT effect = new GeoNtmEffectNT(name);
        BY_NAME.put(name, effect);
        return effect;
    }

    /** The effect's name, as it travels in {@link ParticlePayload} (1.7's enum name for HBM's own). */
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    /** Every effect, HBM's in declaration order first, then the addons'. */
    public static synchronized GeoNtmEffectNT[] values() {
        return BY_NAME.values().toArray(new GeoNtmEffectNT[0]);
    }

    /** 1.7 {@code createSendToThreadedPacket}: this effect for one player only. */
    public void sendTo(ServerPlayer player, double x, double y, double z, @Nullable CompoundTag data) {
        GeoCoreNetwork.sendToPlayer(player, ParticlePayload.CODEC,
                new ParticlePayload(this.name(), x, y, z, data == null ? new CompoundTag() : data));
    }

    /**
     * The client handler for this effect, or null on a server (and on a client before
     * registration). Typed as {@link Object} so this class never references the client-only
     * {@code EffectHandler} functional type in a field signature.
     */
    private Object handler;

    /** Registers the client-side behaviour for this effect. Client only. */
    public void setHandler(EffectHandler handler) {
        this.handler = handler;
    }

    /** Whether a client handler has been registered. */
    public boolean hasHandler() {
        return this.handler != null;
    }

    /**
     * Runs this effect's client handler. Client only, and a no-op for an effect whose handler was
     * never registered: an unimplemented effect is invisible, not a crash.
     */
    public void summonParticle(Level level, double x, double y, double z, @Nullable CompoundTag data) {
        if (this.handler == null) return;
        ((EffectHandler) this.handler).summonParticle(level, x, y, z, data == null ? new CompoundTag() : data);
    }

    /** Plays this effect for every player tracking the chunk containing the position. */
    public void send(Level level, double x, double y, double z, @Nullable CompoundTag data) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        GeoCoreNetwork.sendToTracking(serverLevel, BlockPos.containing(x, y, z), ParticlePayload.CODEC,
                new ParticlePayload(this.name(), x, y, z, data == null ? new CompoundTag() : data));
    }

    /** Sends an AuxParticle-equivalent effect to the source's explicit radius, not just its chunk. */
    public void send(Level level, double x, double y, double z, @Nullable CompoundTag data, double range) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        ParticlePayload payload = new ParticlePayload(this.name(), x, y, z,
                data == null ? new CompoundTag() : data);
        double radiusSquared = range * range;
        for (ServerPlayer player : serverLevel.players()) {
            // level.players() includes a GameTest's bare mock player without the channel; sendToPlayer skips it
            // (NeoForge would otherwise throw for that player).
            if (player.distanceToSqr(x, y, z) <= radiusSquared)
                GeoCoreNetwork.sendToPlayer(player, ParticlePayload.CODEC, payload);
        }
    }

    /**
     * 1.7 {@code createAllAroundThreadedPacket} whose {@code TargetPoint} is not the effect's own position (e.g. the
     * haze puffs scattered around a shell but sent to everyone within 150 of the shell).
     */
    public void sendAround(Level level, double x, double y, double z, @Nullable CompoundTag data,
            double centreX, double centreY, double centreZ, double range) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        double radiusSquared = range * range;
        for (ServerPlayer player : serverLevel.players()) {
            if (player.distanceToSqr(centreX, centreY, centreZ) <= radiusSquared) this.sendTo(player, x, y, z, data);
        }
    }

    /** Plays this effect at the centre of a block position. */
    public void send(Level level, BlockPos pos, @Nullable CompoundTag data) {
        this.send(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, data);
    }

    /** Resolves an effect by name, or null if this port does not implement it. */
    @Nullable
    public static synchronized GeoNtmEffectNT byName(String name) {
        return BY_NAME.get(name);
    }

    /** What one effect does on the client. */
    @FunctionalInterface
    public interface EffectHandler {
        void summonParticle(Level level, double x, double y, double z, CompoundTag data);
    }
}
