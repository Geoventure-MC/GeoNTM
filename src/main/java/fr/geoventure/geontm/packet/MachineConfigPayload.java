package fr.geoventure.geontm.packet;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.net.GeoCoreCodec;
import fr.geoventure.geocore.net.GeoCorePayload;
import net.minecraft.resources.Identifier;

/** S2C, on join: the server's {@code geontmMachines.json} values ({@code MachineDynConfig.values()}), for GUIs and renderers. */
public record MachineConfigPayload(String json) implements GeoCorePayload {

    private static final int MAX_LENGTH = 1 << 18;

    public static final GeoCoreCodec<MachineConfigPayload> CODEC = GeoCoreCodec.of(MachineConfigPayload.class,
            Ids.of(Constants.MOD_ID, "machine_config"),
            (payload, buffer) -> buffer.writeUtf(payload.json, MAX_LENGTH),
            buffer -> new MachineConfigPayload(buffer.readUtf(MAX_LENGTH)));

    @Override
    public Identifier id() {
        return CODEC.id();
    }
}
