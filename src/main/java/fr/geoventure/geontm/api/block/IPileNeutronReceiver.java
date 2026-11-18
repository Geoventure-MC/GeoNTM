package fr.geoventure.geontm.api.block;

/** 1.7 {@code api.geontm.block.IPileNeutronReceiver}: a legacy graphite-pile tile that absorbs cast neutrons. */
@Deprecated
public interface IPileNeutronReceiver {

    void receiveNeutrons(int n);
}
