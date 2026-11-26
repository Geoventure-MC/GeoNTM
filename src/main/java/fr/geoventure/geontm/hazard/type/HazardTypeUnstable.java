package fr.geoventure.geontm.hazard.type;

import java.util.List;

import fr.geoventure.geocore.world.Protection;
import fr.geoventure.geontm.explosion.vanillant.ExplosionVNT;
import fr.geoventure.geontm.handler.radiation.ChunkRadiationManager;
import fr.geoventure.geontm.hazard.modifier.IHazardModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Matériaux radioactifs instables à compte à rebours ({@code HazardTypeUnstable}) : chaque pile porte un compteur
 * NBT qui s'incrémente hors mode Créatif et déclenche une détonation protégée + pic de radiation à expiration.
 */
public class HazardTypeUnstable implements IHazardType {

    private static final String TAG_TIMER = "geontm_unstable_timer";

    @Override
    public void onUpdate(LivingEntity target, double level, ItemStack stack) {
        if (target.level().isClientSide() || stack.isEmpty()) return;
        if (target instanceof Player player && (player.isCreative() || player.isSpectator())) return;
        int limit = maxTicks(level);
        int timer = incrementTimer(stack);
        if (timer >= limit) {
            stack.shrink(1);
            if (!stack.isEmpty()) resetTimer(stack);
            detonate(target.level(), target, target.getX(), target.getY() + 0.5D, target.getZ());
        }
    }

    @Override
    public void updateEntity(ItemEntity item, double level) {
        if (item.level().isClientSide() || item.isRemoved()) return;
        ItemStack stack = item.getItem();
        if (stack.isEmpty()) return;
        int limit = maxTicks(level);
        int timer = incrementTimer(stack);
        if (timer >= limit) {
            item.discard();
            detonate(item.level(), item, item.getX(), item.getY() + 0.25D, item.getZ());
        }
    }

    @Override
    public void addHazardInformation(Player player, List<Component> list, double level, ItemStack stack, List<IHazardModifier> modifiers) {
        list.add(Component.translatable("trait.unstable").withStyle(ChatFormatting.DARK_RED));
        int limit = maxTicks(level);
        int timer = readTimer(stack);
        int remainingSeconds = Math.max(0, (limit - timer + 19) / 20);
        list.add(Component.literal("Décomposition : " + remainingSeconds + " s").withStyle(ChatFormatting.RED));
    }

    private static int maxTicks(double level) {
        return (int) Math.max(20L, Math.round(level * 20D));
    }

    private static int readTimer(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null ? tag.getInt(TAG_TIMER) : 0;
    }

    private static int incrementTimer(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        int next = tag.getInt(TAG_TIMER) + 1;
        tag.putInt(TAG_TIMER, next);
        return next;
    }

    private static void resetTimer(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null) tag.putInt(TAG_TIMER, 0);
    }

    private static void detonate(Level level, Entity source, double x, double y, double z) {
        new ExplosionVNT(level, x, y, z, 6.0F)
                .setSource(Protection.Source.of(source))
                .makeStandard()
                .explode();
        ChunkRadiationManager.proxy.incrementRad(level, source.blockPosition(), 250D);
    }
}
