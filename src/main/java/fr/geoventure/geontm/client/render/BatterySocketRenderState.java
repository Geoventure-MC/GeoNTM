package fr.geoventure.geontm.client.render;

import fr.geoventure.geocore.render.CompatItemRenderState;
import fr.geoventure.geocore.render.CompatRenderState;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.Nullable;

/** Render state for {@link BatterySocketRenderer}: facing yaw + the inserted battery's part/texture. */
public class BatterySocketRenderState extends CompatRenderState {
    public float yaw;
    public boolean frame;
    public boolean creative;
    public boolean generic;
    public int bodyPart = -1;
    public SocketCentrepieces.@Nullable Centrepiece centrepiece;
    public final CompatItemRenderState other = new CompatItemRenderState();
    public long gameTime;
    public float interp;
    /** The OBJ part to draw for the inserted item ("Battery" / "Capacitor"), or null if empty. */
    public @Nullable String batteryPart;
    /** Texture for that part, or null if no battery is inserted. */
    public @Nullable ResourceLocation batteryTexture;
}
