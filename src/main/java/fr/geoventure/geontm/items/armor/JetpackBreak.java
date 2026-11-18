package fr.geoventure.geontm.items.armor;

import fr.geoventure.geontm.api.environment.Environment;
import fr.geoventure.geontm.handler.JetpackHandler;
import fr.geoventure.geontm.capability.GeoNtmLivingProps;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.sound.ModSounds;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** Regular jetpack with the old automatic airborne hover assist. */
public class JetpackBreak extends JetpackFueledBase {

    public JetpackBreak(FluidType fuel, int maxFuel, Properties properties) {
        super(fuel, maxFuel, properties);
    }

    @Override
    public void addInformation(ItemStack stack, List<Component> tooltip) {
        tooltip.add(Component.literal("Regular jetpack that will automatically hover mid-air."));
        tooltip.add(Component.literal("Sneaking will stop hover mode."));
        tooltip.add(Component.literal("Hover mode will consume less fuel and increase air-mobility."));
        super.addInformation(stack, tooltip);
    }

    /** The same thrust/hover arms that actually support flight and reset the flight timeout. */
    public boolean heldAloft(Player player, ItemStack stack) {
        boolean active = isActive(player);
        return getFuel(stack) > 0 && (active && !player.isShiftKeyDown()
                || (active || !player.isShiftKeyDown()) && !player.onGround()
                && GeoNtmLivingProps.getEnableBackpack(player) && Environment.gravity(player) > 0D);
    }

    @Override
    protected void tickFlight(Level level, Player player, ItemStack stack) {
        if (!heldAloft(player, stack)) return;
        JetpackHandler.resetFlightTime(player);
        // geontm-space: thrust scaled up (never down) with gravity, the jet pushing along the view in free fall, and no
        // hovering without gravity (fr.geoventure.geontm.api.environment.Environment.gravity, 1 on plain GEONTM)
        double gravity = Environment.gravity(player);
        if (isActive(player) && !player.isShiftKeyDown()) {
            player.fallDistance = 0;
            if (gravity == 0D) {
                player.setDeltaMovement(player.getDeltaMovement().add(player.getViewVector(1.0F).scale(0.05D)));
            } else if (player.getDeltaMovement().y < 0.4D) {
                player.setDeltaMovement(player.getDeltaMovement().add(0, 0.1D * Math.max(gravity, 1D), 0));
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.FLAMETHROWER_SHOOT.get(),
                    SoundSource.PLAYERS, 0.25F, 1.5F);
            if (!level.isClientSide()) JetpackHandler.sendExhaust(player, JetpackHandler.EXHAUST_REGULAR);
            useUpFuel(player, stack, 5);
        } else {
            player.fallDistance = 0;
            double thrust = Math.max(gravity, 1D);
            double y = player.getDeltaMovement().y;
            if (y < -1D * thrust) y += 0.2D * thrust;
            else if (y < -0.1D * thrust) y += 0.1D * thrust;
            else if (y < 0D) y = 0D;
            player.setDeltaMovement(player.getDeltaMovement().x * 1.025D, y, player.getDeltaMovement().z * 1.025D);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.FLAMETHROWER_SHOOT.get(),
                    SoundSource.PLAYERS, 0.25F, 1.5F);
            if (!level.isClientSide()) JetpackHandler.sendExhaust(player, JetpackHandler.EXHAUST_REGULAR);
            useUpFuel(player, stack, 10);
        }
    }
}
