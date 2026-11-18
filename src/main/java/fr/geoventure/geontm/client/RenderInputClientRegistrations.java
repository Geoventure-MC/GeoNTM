package fr.geoventure.geontm.client;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.client.anim.ToolAnimations;
import fr.geoventure.geontm.client.crucible.CrucibleClient;
import fr.geoventure.geontm.client.particle.ParticleHadron;
import fr.geoventure.geontm.client.particle.ParticleSmokePlume;
import fr.geoventure.geontm.client.particle.SparkParticles;
import fr.geoventure.geontm.client.particle.TextParticles;
import fr.geoventure.geontm.client.render.RenderOverhead;
import fr.geoventure.geontm.client.render.entity.EntityItemRenderers;
import fr.geoventure.geontm.extprop.GeoNtmPlayerProps;
import fr.geoventure.geontm.handler.ArmorModEvents;
import fr.geoventure.geontm.handler.GeoNtmKeybinds.EnumKeybind;
import fr.geoventure.geontm.packet.KeybindPayload;
import fr.geoventure.geontm.packet.ToolAnimationPayload;
import fr.geoventure.geontm.particle.helper.GeoNtmEffectNT;
import com.mojang.blaze3d.platform.InputConstants;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.client.ClientResourceReloads;
import fr.geoventure.geocore.client.ClientTicks;
import fr.geoventure.geocore.client.KeyMappings;
import fr.geoventure.geocore.client.VanillaHud;
import fr.geoventure.geocore.data.Tags;
import fr.geoventure.geocore.gui.GuiDraw;
import fr.geoventure.geocore.net.GeoCoreNetwork;
import fr.geoventure.geocore.render.Particles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attributes;

/** G-C (rendering and input gaps): the client halves - packet handlers, key mappings, overlays and world renderers. */
public final class RenderInputClientRegistrations {

    private static final ResourceLocation MISC = Ids.of("geontm", "textures/misc/overlay_misc.png");

    /** 1.7 {@code GeoNtmKeybinds.magnetKey}: "geontm.key.toggleMagnet" on Z. */
    public static KeyMappings.Mapping magnetKey;
    private static boolean lastMagnet;

    private RenderInputClientRegistrations() {}

    /** Called from {@code GeoNtmPackets.registerClientHandlers}. */
    public static void registerPacketHandlers() {
        GeoCoreNetwork.registerPlayToClient(ToolAnimationPayload.CODEC,
                (payload, context) -> ToolAnimations.handle(payload));
        CrucibleClient.registerPacketHandlers(); // 1.12.2 crucible anims
    }

    /** Called from {@code GeoNtmClientRegistrations.installKeybinds}. */
    public static void installKeybinds() {
        magnetKey = KeyMappings.register("geontm", "key.geontm.toggleMagnet", InputConstants.KEY_Z, "geontm");
        ClientTicks.onEndTick(RenderInputClientRegistrations::onEndTick);
    }

    /** The magnet key's state, sent on every change as 1.7's {@code KeybindPacket} was; the server toggles on press. */
    private static void onEndTick() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || mc.level == null) {
            lastMagnet = false;
            return;
        }
        boolean magnet = mc.screen == null && magnetKey.isHeld();
        if (magnet != lastMagnet) {
            GeoCoreNetwork.sendToServer(KeybindPayload.CODEC, new KeybindPayload(EnumKeybind.TOGGLE_MAGNET.ordinal(), magnet));
            lastMagnet = magnet;
        }
    }

    /** Called from {@code GeoNtmClientRegistrations.registerHud}. */
    public static void registerHud(GeoNtmClientRegistrations.HudSink sink) {
        sink.register("shield_bar", RenderInputClientRegistrations::renderShieldBar);
    }

    /**
     * 1.7 {@code RenderScreenOverlay.renderShieldBar}, drawn while the effective maximum shield (base plus a Life
     * Extender insert) is above zero: the empty bar ({@code overlay_misc} 146,0 81x9), its fill (147,9, ceil(shield *
     * 79 / max) wide) and the shield to one decimal in 0xFFFF80 over a four-way black outline.
     *
     * <p>1.7 drew it in the ARMOR overlay's slot (Forge's {@code left_height}) and raised the left stack by ten, so the
     * armour row moved up above it. No loader here lets an overlay join that stacking on every node, so the bar goes
     * to the row above vanilla's armour row when armour shows (at the armour row otherwise): same bar, rows swapped.
     */
    public static void renderShieldBar(GuiGraphics graphics) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || mc.options.hideGui || mc.gameMode == null || !mc.gameMode.canHurtPlayer()) return;
        float max = ArmorModEvents.effectiveMaxShield(player);
        if (max <= 0) return;
        float shield = GeoNtmPlayerProps.getData(player).shield;

        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        int left = width / 2 - 91;

        // vanilla's armour row: the hearts' rows, spaced as Gui.renderPlayerHealth spaces them
        float maxHealth = Math.max((float) player.getAttributeValue(Attributes.MAX_HEALTH), player.getHealth());
        int absorb = Mth.ceil(player.getAbsorptionAmount());
        int rows = Mth.ceil((maxHealth + absorb) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (rows - 2), 3);
        int armorRow = height - 39 - (rows - 1) * rowHeight - 10;
        boolean armorShows = player.getArmorValue() > 0 && !VanillaHud.hideArmor();
        int top = armorShows ? armorRow - 10 : armorRow;

        GuiDraw.blit(graphics, MISC, left, top, 146, 0, 81, 9, 256, 256);
        int i = (int) Math.ceil(shield * 79 / max);
        if (i > 0) GuiDraw.blit(graphics, MISC, left + 1, top, 147, 9, i, 9, 256, 256);

        Font font = mc.font;
        String label = "" + ((int) (shield * 10F)) / 10D;
        int half = font.width(label) / 2;
        GuiDraw.text(graphics, font, label, left + 41 - half, top + 1, 0xFF000000, false);
        GuiDraw.text(graphics, font, label, left + 39 - half, top + 1, 0xFF000000, false);
        GuiDraw.text(graphics, font, label, left + 40 - half, top, 0xFF000000, false);
        GuiDraw.text(graphics, font, label, left + 40 - half, top + 2, 0xFF000000, false);
        GuiDraw.text(graphics, font, label, left + 40 - half, top + 1, 0xFFFFFF80, false);
    }

    /** Called from {@code GeoNtmClientRegistrations.installEntityEffects}. */
    public static void install() {
        // 1.7 Forge IItemRenderer items on pedestals, skeleton holders and belts (FramedItem's CUSTOM arm)
        ClientResourceReloads.register(Constants.MOD_ID, "framed_item_routing",
                EntityItemRenderers::reload);
        // 1.7 ClientProxy "tau": count line sparks (random gaussian * 0.05 sideways, 0.05 up) and one hadron flash
        SparkParticles.register();
        GeoNtmEffectNT.Tau.setHandler((level, x, y, z, data) -> {
            if (!(level instanceof ClientLevel client)) return;
            boolean small = Tags.getBooleanOr(data, "small", false);
            int count = Tags.getByteOr(data, "count", (byte) 0);
            RandomSource rand = client.getRandom();
            for (int i = 0; i < count; i++) {
                SparkParticles.spawn(client, x, y, z, rand.nextGaussian() * 0.05, 0.05,
                        rand.nextGaussian() * 0.05, small);
            }
            Particles.add(new ParticleHadron(client, x, y, z).makeSmall(small));
        });
        GeoNtmEffectNT.VanillaBurst_RedDust.setHandler((level, x, y, z, data) -> {
            double motion = Tags.getDoubleOr(data, "motion", 0D);
            RandomSource rand = level.getRandom();
            for (int i = 0; i < Tags.getIntOr(data, "count", 0); i++) {
                double mX = rand.nextGaussian() * motion;
                double mY = rand.nextGaussian() * motion;
                double mZ = rand.nextGaussian() * motion;
                // 1.7 set the motion after construction, past EntityReddustFX's own damping
                Particle fx = Minecraft.getInstance().particleEngine.createParticle(DustParticleOptions.REDSTONE, x, y, z, 0D, 0D, 0D);
                if (fx != null) fx.setParticleSpeed(mX, mY, mZ);
            }
        });
        // 1.7 IBlockCT connected textures: hadron coils, coated wire, ICF/PWR shells, deco metals, CT glass, platemetal, pile
        ConnectedTextureRegistrations.register();
        // 1.7 IBlockMultiPass: paintable cable and tube wear their paint in the chunk mesh
        ChunkModelRegistrations.register();
        // 1.7 RenderOverhead markers ("marker" aux effect) and ParticleText ("debug")
        RenderOverhead.register();
        TextParticles.register();
        GeoNtmEffectNT.Marker.setHandler((level, x, y, z, data) -> {
            int expires = Tags.getIntOr(data, "expires", 0);
            String label = Tags.getStringOr(data, "label", "");
            RenderOverhead.queue(BlockPos.containing(x, y, z),
                    new RenderOverhead.Marker(Tags.getIntOr(data, "color", 0))
                            .setDist(Tags.getDoubleOr(data, "dist", 0D))
                            .setExpire(expires > 0 ? System.currentTimeMillis() + expires : 0)
                            .withLabel(label.isEmpty() ? null : label));
        });
        GeoNtmEffectNT.LaunchSmoke.setHandler((level, x, y, z, data) -> {
            if (!(level instanceof ClientLevel client)) return;
            Particles.add(new ParticleSmokePlume(client, x, y, z,
                    Tags.getDoubleOr(data, "moX", 0D), Tags.getDoubleOr(data, "moY", 0D),
                    Tags.getDoubleOr(data, "moZ", 0D)));
        });
        GeoNtmEffectNT.DebugText.setHandler((level, x, y, z, data) -> {
            if (!(level instanceof ClientLevel client)) return;
            TextParticles.spawn(client, x, y, z, Tags.getIntOr(data, "color", 0),
                    Tags.getStringOr(data, "text", ""), Tags.getFloatOr(data, "scale", 1F));
        });
    }
}
