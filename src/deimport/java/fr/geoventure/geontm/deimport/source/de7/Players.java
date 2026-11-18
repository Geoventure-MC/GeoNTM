package fr.geoventure.geontm.deimport.source.de7;

import fr.geoventure.geontm.deimport.engine.Ctx;
import fr.geoventure.geontm.delegacy.de7.Entities;
import fr.geoventure.geontm.delegacy.de7.Walker;
import net.minecraft.nbt.CompoundTag;

/** A saved player: every stack it carries (inventory, ender chest, curios, riding entities) and its GEONTM state. */
public final class Players {
    private Players() {}

    public static void convert(CompoundTag player, Walker walker, Ctx ctx, String where) {
        Entities.living(player, ctx, where);
        walker.walk(player, where);
        ctx.playerConverted(player, where);
    }
}
