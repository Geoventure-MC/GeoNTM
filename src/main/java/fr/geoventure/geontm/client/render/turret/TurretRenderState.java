package fr.geoventure.geontm.client.render.turret;

import fr.geoventure.geontm.tileentity.turret.TileEntityTurretBaseNT;
import fr.geoventure.geocore.render.CompatRenderState;

public final class TurretRenderState<T extends TileEntityTurretBaseNT> extends CompatRenderState {

    public final TurretState<T> turret;

    public TurretRenderState(TurretState.Emitter<T> emitter) {
        this.turret = new TurretState<>(emitter);
    }
}
