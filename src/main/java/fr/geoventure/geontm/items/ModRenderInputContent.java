package fr.geoventure.geontm.items;

import fr.geoventure.geontm.handler.EquipReceiverHandler;
import fr.geoventure.geontm.packet.ToolAnimationPayload;
import fr.geoventure.geocore.net.GeoCoreNetwork;

/**
 * G-C (rendering and input gaps): the common halves - the tool animation cue ({@code GeoNtmAnimationPacket} for
 * {@link IAnimatedItem}s) and the held-item equip edge ({@link IEquipReceiver}).
 */
public final class ModRenderInputContent {

    private ModRenderInputContent() {}

    public static void init() {
        GeoCoreNetwork.declarePlayToClient(ToolAnimationPayload.CODEC);
        EquipReceiverHandler.init();
    }
}
