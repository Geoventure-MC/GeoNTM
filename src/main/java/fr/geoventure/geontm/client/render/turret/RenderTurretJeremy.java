package fr.geoventure.geontm.client.render.turret;

import fr.geoventure.geontm.tileentity.turret.TileEntityTurretJeremy;

public class RenderTurretJeremy extends RenderTurretBase<TileEntityTurretJeremy> {

    public static final TurretState.Emitter<TileEntityTurretJeremy> EMITTER = (turret, state, frame) -> {
        connectors(frame, state.connectorMask);
        mount(frame, state, TurretResources.BASE,
                TurretResources.CHEKHOV, "Carriage", TurretResources.CARRIAGE, 1.5F,
                TurretResources.JEREMY, "Gun", TurretResources.JEREMY_GUN);
    };

    @Override
    protected TurretState.Emitter<TileEntityTurretJeremy> emitter() {
        return EMITTER;
    }
}
