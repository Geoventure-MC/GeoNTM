package fr.geoventure.geontm.items.weapon;

import fr.geoventure.geontm.packet.CrucibleAnimPayload;
import fr.geoventure.geontm.packet.CrucibleClickPayload;
import fr.geoventure.geontm.packet.MobSlicerPayload;
import fr.geoventure.geontm.packet.SpecialDeathPayload;
import fr.geoventure.geocore.net.GeoCoreNetwork;

/**
 * The sword cutters' four channels (1.12.2 {@code PacketMobSlicer}, {@code PacketSpecialDeath}, the {@code Anim}
 * effect of {@code AuxParticlePacketNT} and {@code AuxButtonPacket} id 1000). The client handlers are attached by
 * {@code fr.geoventure.geontm.client.crucible.CrucibleClient}.
 */
public final class CrucibleNetwork {

    private CrucibleNetwork() {}

    public static void init() {
        GeoCoreNetwork.declarePlayToClient(CrucibleAnimPayload.CODEC);
        GeoCoreNetwork.declarePlayToClient(SpecialDeathPayload.CODEC);
        GeoCoreNetwork.registerPlayToServer(MobSlicerPayload.CODEC, MobSlicerPayload::handleServer);
        GeoCoreNetwork.registerPlayToServer(CrucibleClickPayload.CODEC, CrucibleClickPayload::handleServer);
    }
}
