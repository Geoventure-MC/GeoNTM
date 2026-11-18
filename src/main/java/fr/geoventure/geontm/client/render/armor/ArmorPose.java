package fr.geoventure.geontm.client.render.armor;

import fr.geoventure.geocore.render.HumanoidPose;
import fr.geoventure.geocore.world.ArmorSlot;

/** What a worn suit's declaration reads: one model for all four slots, replayed once per slot. */
public final class ArmorPose {

    public ArmorSlot slot;
    public HumanoidPose wearer;
}
