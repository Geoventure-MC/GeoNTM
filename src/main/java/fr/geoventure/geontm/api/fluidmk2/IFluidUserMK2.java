package fr.geoventure.geontm.api.fluidmk2;

import fr.geoventure.geontm.api.tile.ILoadedTile;
import fr.geoventure.geontm.inventory.fluid.tank.FluidTankNTM;

/** Base for any tile that participates in a fluid network (provider, receiver, or both). */
public interface IFluidUserMK2 extends IFluidConnectorMK2, ILoadedTile {

    int HIGHEST_VALID_PRESSURE = 5;
    int[] DEFAULT_PRESSURE_RANGE = new int[]{0, 0};

    FluidTankNTM[] getAllTanks();
}
