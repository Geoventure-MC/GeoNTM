package fr.geoventure.geontm.client;

import fr.geoventure.geontm.client.render.GeoNtmBlockEntityRenderers;
import fr.geoventure.geontm.tileentity.ModDoorBlockEntities;
import fr.geoventure.geontm.client.render.RenderBlastDoor;
import fr.geoventure.geontm.client.render.RenderDoorGeneric;

/** Client registration bridge for the door family: two block-entity renderers, no screens. */
public final class DoorClientRegistrations {
    private DoorClientRegistrations() {}

    public static void registerRenderers(GeoNtmBlockEntityRenderers.Sink sink) {
        sink.register(ModDoorBlockEntities.DOOR_GENERIC.get(), context -> new RenderDoorGeneric());
        sink.register(ModDoorBlockEntities.BLAST_DOOR.get(), context -> new RenderBlastDoor());
    }
}
