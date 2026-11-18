package fr.geoventure.geontm.compat.computer.rbmk;

import fr.geoventure.geontm.compat.computer.OpenComputers;
import fr.geoventure.geontm.tileentity.machine.rbmk.BlockEntityRBMKControlManual;
import fr.geoventure.geontm.tileentity.machine.rbmk.RBMKColor;
import fr.geoventure.geocore.computer.ComputerMethod;
import net.minecraft.util.Mth;

/** 1.7 {@code TileEntityRBMKControlManual}: the inherited {@code rbmk_control_rod} component plus the rod's colour. */
public final class RBMKControlManualPeripheral extends RBMKControlPeripheral<BlockEntityRBMKControlManual> {

    private static final int COLOR = CONTROL_SLOTS;

    public RBMKControlManualPeripheral(BlockEntityRBMKControlManual machine) {
        super(machine, CONTROL_SLOTS + 1);
    }

    @Override
    protected void capture(BlockEntityRBMKControlManual machine) {
        super.capture(machine);
        put(COLOR, machine.color == null ? -1 : machine.color.ordinal());
    }

    @ComputerMethod
    public final Object[] getColor() {
        // 1.7 :162 dereferences an undyed rod's null colour
        return read(s -> s.intAt(COLOR) < 0 ? OpenComputers.unknownError() : new Object[]{s.intAt(COLOR)});
    }

    @ComputerMethod(mainThread = true)
    public final Object[] setColor(double color) {
        BlockEntityRBMKControlManual machine = machine();
        machine.color = RBMKColor.VALUES[Mth.clamp((int) color, 0, 4)];
        machine.setChanged();
        return new Object[]{true};
    }
}
