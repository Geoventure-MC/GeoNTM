package fr.geoventure.geontm.recipes.data;

import fr.geoventure.geontm.api.data.MachinePartDataMaps;
import fr.geoventure.geontm.api.data.MachinePartDataMaps.DrillBit;
import fr.geoventure.geocore.data.DataMapSource;

/** 1.7 {@code ItemDrillbit.EnumDrillType} (speed, tier, fortune, vein miner, silk touch). */
public final class DrillBitSource extends DataMapSource<DrillBit> {

    public DrillBitSource() {
        super(MachinePartDataMaps.DRILL_BIT, false);
    }

    @Override
    protected void values() {
        put("geontm:drillbit_steel", new DrillBit(1.0D, 1, 0, false, false));
        put("geontm:drillbit_steel_diamond", new DrillBit(1.0D, 1, 2, false, true));
        put("geontm:drillbit_hss", new DrillBit(1.2D, 2, 0, true, false));
        put("geontm:drillbit_hss_diamond", new DrillBit(1.2D, 2, 3, true, true));
        put("geontm:drillbit_desh", new DrillBit(1.5D, 3, 1, true, true));
        put("geontm:drillbit_desh_diamond", new DrillBit(1.5D, 3, 4, true, true));
        put("geontm:drillbit_tcalloy", new DrillBit(2.0D, 4, 1, true, true));
        put("geontm:drillbit_tcalloy_diamond", new DrillBit(2.0D, 4, 4, true, true));
        put("geontm:drillbit_ferro", new DrillBit(2.5D, 5, 1, true, true));
        put("geontm:drillbit_ferro_diamond", new DrillBit(2.5D, 5, 4, true, true));
    }
}
