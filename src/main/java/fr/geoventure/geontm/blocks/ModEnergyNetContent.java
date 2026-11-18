package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.data.GeoCoreComponent;
import net.minecraft.core.BlockPos;
import fr.geoventure.geontm.sound.EnergyNetSounds;
import fr.geoventure.geontm.tileentity.ModEnergyNetBlockEntities;

/**
 * One bootstrap hook for the energy-network family: the pylons and their wires, the cable variants, the
 * legacy storage tiers, the suit battery and the tesla coil. Called from {@code MainRegistry.bootstrap}
 * after the machine blocks and before the block-entity registries.
 */
public final class ModEnergyNetContent {

    private ModEnergyNetContent() {}

    /** The cable drum's first pylon (the old NBT {@code x/y/z} triple); absent means no wire is started. */
    public static final GeoCoreComponent<BlockPos> WIRE_START =
            GeoCoreComponent.of(Constants.MOD_ID, "wire_start", BlockPos.CODEC);

    /** A capacitor item's stored charge; capacitors store nothing, but saved items still carry it. */
    public static final GeoCoreComponent<Long> CAPACITOR_POWER = GeoCoreComponent.longValue(Constants.MOD_ID, "capacitor_power");

    /** A battery's stored charge, carried on its dropped item (1.7's {@code IPersistentNBT} power). */
    public static final GeoCoreComponent<Long> BATTERY_POWER = GeoCoreComponent.longValue(Constants.MOD_ID, "battery_power");

    public static void init() {
        ModEnergyNetBlocks.init();
        ModEnergyNetBlockEntities.init();
        EnergyNetSounds.init();
    }
}
