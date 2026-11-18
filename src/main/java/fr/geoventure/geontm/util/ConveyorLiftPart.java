package fr.geoventure.geontm.util;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;
import fr.geoventure.geontm.blocks.network.BlockConveyorLift;

/** 1.7 {@code BlockConveyorLift}: the isBottom / isTop pair its bounds, snapping, travel and renderer all read. */
public enum ConveyorLiftPart implements StringRepresentable {
    BOTTOM,
    MIDDLE,
    TOP;

    private final String id = name().toLowerCase(Locale.ROOT);

    @Override
    public String getSerializedName() {
        return id;
    }
}
