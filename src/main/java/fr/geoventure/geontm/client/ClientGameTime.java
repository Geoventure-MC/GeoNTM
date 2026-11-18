package fr.geoventure.geontm.client;

import fr.geoventure.geontm.util.GameTime;
import fr.geoventure.geocore.client.Frames;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

public final class ClientGameTime {

    private ClientGameTime() {
    }

    public static void install() {
        GameTime.CLIENT_MILLIS = ClientGameTime::millis;
        GameTime.CLIENT_NOW = ClientGameTime::now;
    }

    private static long millis() {
        ClientLevel level = Minecraft.getInstance().level;
        return level == null ? 0L : GameTime.millis(level);
    }

    private static long now() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return 0L;
        return GameTime.millis(level) + (long) (Frames.partialTick() * 50F);
    }
}
