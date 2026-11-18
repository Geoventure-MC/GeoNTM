package fr.geoventure.geontm.client.anim;

import fr.geoventure.geontm.items.ToolAnimation;
import fr.geoventure.geontm.items.tool.ItemBoltgun;
import fr.geoventure.geontm.items.tool.ItemChainsaw;
import fr.geoventure.geontm.items.weapon.grenade.ItemGrenadeUniversal;
import fr.geoventure.geontm.packet.ToolAnimationPayload;
import fr.geoventure.geontm.util.GameTime;
import fr.geoventure.geocore.render.anim.BusAnimation;
import fr.geoventure.geocore.render.anim.BusAnimationKeyframe.IType;
import fr.geoventure.geocore.render.anim.BusAnimationSequence;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * The client half of 1.7 {@code GeoNtmAnimationPacket.handleItem} and the {@code getAnimation} tables of the
 * {@code IAnimatedItem} tools: chainsaw ({@code ItemChainsaw}) and boltgun ({@code ItemBoltgun}). Each table is
 * transcribed from its item; the renderers read the buses ({@code SWING_ROT}/{@code SWING_TRANS}, {@code RECOIL}).
 * The crucible is 1.12.2's now and animates through {@code fr.geoventure.geontm.client.crucible.CrucibleClient}.
 */
public final class ToolAnimations {

    private ToolAnimations() {}

    public static void handle(ToolAnimationPayload payload) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;
        // 1.7 player.getHeldItem(): the main hand only
        ItemStack stack = minecraft.player.getMainHandItem();
        if (stack.isEmpty() || !BuiltInRegistries.ITEM.getKey(stack.getItem()).toString().equals(payload.itemId())) return;
        if (payload.animation() < 0 || payload.animation() >= ToolAnimation.VALUES.length) return;
        BusAnimation animation = get(stack, ToolAnimation.VALUES[payload.animation()]);
        if (animation != null) GeoNtmAnimations.start(stack.getItem(), animation, InteractionHand.MAIN_HAND, 0);
    }

    static @Nullable BusAnimation get(ItemStack stack, ToolAnimation type) {
        if (stack.getItem() instanceof ItemChainsaw) return chainsaw();
        if (stack.getItem() instanceof ItemBoltgun) return boltgun();
        if (stack.getItem() instanceof ItemGrenadeUniversal && type == ToolAnimation.EQUIP) return grenade(stack);
        return null;
    }

    /** 1.7 {@code ItemGrenadeUniversal.getAnimation(EQUIP)}, per shell; read by {@code ItemRenderGrenade}. */
    private static BusAnimation grenade(ItemStack stack) {
        return switch (ItemGrenadeUniversal.getData(stack).shell()) {
            case FRAG, TECH -> new BusAnimation()
                    .addBus("BODYMOVE", new BusAnimationSequence().setPos(0, -5, 0).addPos(0, -3, 0, 350).addPos(0, 0, 0, 350, IType.SIN_DOWN))
                    .addBus("BODYTURN", new BusAnimationSequence().addPos(0, 0, 45, 350).addPos(0, 0, -15, 350, IType.SIN_DOWN).hold(200).addPos(0, 0, -20, 100, IType.SIN_DOWN).addPos(0, 0, 0, 500, IType.SIN_FULL))
                    .addBus("RINGMOVE", new BusAnimationSequence().hold(900).addPos(0, 0, 1, 150).addPos(0, -3, 3, 300))
                    .addBus("RINGTURN", new BusAnimationSequence().hold(900).addPos(0, 0, 45, 300))
                    .addBus("RENDERRING", new BusAnimationSequence().setPos(1, 1, 1).hold(1350).setPos(0, 0, 0));
            case STICK -> new BusAnimation()
                    .addBus("BODYMOVE", new BusAnimationSequence().setPos(0, -7, 0).addPos(0, 3, 0, 750, IType.SIN_DOWN).holdUntil(1900).addPos(0, 0, 0, 250, IType.SIN_FULL))
                    .addBus("BODYTURN", new BusAnimationSequence().setPos(0, 0, 90).addPos(0, 0, -45, 750, IType.SIN_DOWN).holdUntil(1900).addPos(0, 0, 0, 250, IType.SIN_FULL))
                    .addBus("RINGMOVE", new BusAnimationSequence().hold(800).addPos(0, -0.25, 0, 200, IType.SIN_FULL).hold(250).addPos(0, -0.5, 0, 200, IType.SIN_FULL).addPos(2, -5, 0, 350, IType.SIN_UP))
                    .addBus("RINGTURN", new BusAnimationSequence().hold(800).addPos(0, 360, 0, 200, IType.SIN_FULL).hold(250).addPos(0, 360 * 2, 0, 200, IType.SIN_FULL))
                    .addBus("RENDERRING", new BusAnimationSequence().setPos(1, 1, 1).hold(2100).setPos(0, 0, 0));
            case NUKE -> new BusAnimation()
                    .addBus("BODYMOVE", new BusAnimationSequence().setPos(0, -5, 0).hold(250).addPos(0, 0, 0, 850, IType.SIN_DOWN))
                    .addBus("BODYTURN", new BusAnimationSequence().setPos(0, 0, 90).hold(250).addPos(0, 0, -25, 850, IType.SIN_DOWN).hold(200).addPos(0, 0, -30, 100, IType.SIN_DOWN).addPos(0, 0, 0, 750, IType.SIN_FULL))
                    .addBus("RINGMOVE", new BusAnimationSequence().hold(1300).addPos(0, 0, 1, 150).addPos(0, -3, 3, 300))
                    .addBus("RINGTURN", new BusAnimationSequence().hold(1300).addPos(0, 0, 720, 500)) // SPEEN
                    .addBus("RENDERRING", new BusAnimationSequence().setPos(1, 1, 1).hold(1750).setPos(0, 0, 0));
        };
    }

    /** 1.7 {@code ItemChainsaw.getAnimation}: a fresh swing, or one continued from wherever the last swing is. */
    private static @Nullable BusAnimation chainsaw() {
        int forward = 150;
        int sideways = 100;
        int retire = 200;

        GeoNtmAnimations.Animation current = GeoNtmAnimations.getRelevantAnim(0);
        if (current == null) {
            return new BusAnimation()
                    .addBus("SWING_ROT", new BusAnimationSequence()
                            .addPos(0, 0, 90, forward)
                            .addPos(45, 0, 90, sideways)
                            .addPos(0, 0, 0, retire))
                    .addBus("SWING_TRANS", new BusAnimationSequence()
                            .addPos(0, 0, 3, forward)
                            .addPos(2, 0, 2, sideways)
                            .addPos(0, 0, 0, retire));
        }

        double[] rot = GeoNtmAnimations.getRelevantTransformation("SWING_ROT");
        double[] trans = GeoNtmAnimations.getRelevantTransformation("SWING_TRANS");

        if (GameTime.now() - current.startMillis < 50) return null;

        return new BusAnimation()
                .addBus("SWING_ROT", new BusAnimationSequence()
                        .addPos(rot[0], rot[1], rot[2], 0)
                        .addPos(0, 0, 90, forward)
                        .addPos(45, 0, 90, sideways)
                        .addPos(0, 0, 0, retire))
                .addBus("SWING_TRANS", new BusAnimationSequence()
                        .addPos(trans[0], trans[1], trans[2], 0)
                        .addPos(0, 0, 3, forward)
                        .addPos(2, 0, 2, sideways)
                        .addPos(0, 0, 0, retire));
    }

    /** 1.7 {@code ItemBoltgun.getAnimation}: the barrel's recoil, whatever the cue. */
    private static BusAnimation boltgun() {
        return new BusAnimation()
                .addBus("RECOIL", new BusAnimationSequence()
                        .addPos(1, 0, 1, 50)
                        .addPos(0, 0, 1, 100));
    }
}
