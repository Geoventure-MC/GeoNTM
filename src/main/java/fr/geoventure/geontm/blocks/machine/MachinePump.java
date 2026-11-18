package fr.geoventure.geontm.blocks.machine;

import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geontm.blocks.ITooltipProvider;
import fr.geoventure.geontm.api.energymk2.IEnergyConnectorBlock;
import fr.geoventure.geontm.api.fluidmk2.IFluidConnectorBlockMK2;
import fr.geoventure.geontm.blocks.multiblock.BlockMultiblockMachine;
import fr.geoventure.geontm.blocks.multiblock.MultiblockSurface;
import fr.geoventure.geontm.interfaces.ILookOverlay;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.tileentity.ModBlockEntities;
import fr.geoventure.geontm.tileentity.machine.GroundwaterPumps;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachinePumpElectric;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachinePumpSteam;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** The shared 3x3x5 groundwater-pump multiblock. */
public class MachinePump extends BlockMultiblockMachine implements IEnergyConnectorBlock, IFluidConnectorBlockMK2, ILookOverlay, ITooltipProvider {

    private final boolean steam;

    public MachinePump(Properties properties) {
        this(properties, false);
    }

    public MachinePump(Properties properties, boolean steam) {
        super(properties);
        this.steam = steam;
    }

    @Override
    protected BlockEntity createCoreBlockEntity(BlockPos pos, BlockState state) {
        return steam ? new TileEntityMachinePumpSteam(pos, state) : new TileEntityMachinePumpElectric(pos, state);
    }

    @Override
    public int[] getDimensions() {
        return new int[]{3, 0, 1, 1, 1, 1};
    }

    @Override
    public int getOffset() {
        return 1;
    }

    @Override
    public ILookOverlay.Data getLookOverlay(Level level, BlockPos pos, Player player) {
        List<String> text = new ArrayList<>();
        if (steam) {
            if (!(MultiblockSurface.coreEntity(level, pos) instanceof TileEntityMachinePumpSteam pump)) return null;
            text.add("§a-> §r" + pump.steam.getTankType().getDisplayNameString() + ": "
                    + String.format(Locale.US, "%,d", pump.steam.getFill()) + " / "
                    + String.format(Locale.US, "%,d", pump.steam.getMaxFill()) + "mB");
            text.add("§c<- §r" + pump.lps.getTankType().getDisplayNameString() + ": "
                    + String.format(Locale.US, "%,d", pump.lps.getFill()) + " / "
                    + String.format(Locale.US, "%,d", pump.lps.getMaxFill()) + "mB");
            text.add("§c<- §r" + pump.water.getTankType().getDisplayNameString() + ": "
                    + String.format(Locale.US, "%,d", pump.water.getFill()) + " / "
                    + String.format(Locale.US, "%,d", pump.water.getMaxFill()) + "mB");
            if (pump.getBlockPos().getY() > GroundwaterPumps.groundHeight) {
                text.add("&[" + warningColor() + "&]! ! ! ALTITUDE ! ! !");
            }
            if (!pump.onGround) text.add("&[" + warningColor() + "&]! ! ! NO VALID GROUND ! ! !");
        } else {
            if (!(MultiblockSurface.coreEntity(level, pos) instanceof TileEntityMachinePumpElectric pump)) return null;
            text.add("§a-> §r" + String.format(Locale.US, "%,d", pump.power) + " / "
                    + String.format(Locale.US, "%,d", pump.getMaxPower()) + "HE");
            text.add("§c<- §r" + pump.water.getTankType().getDisplayNameString() + ": "
                    + String.format(Locale.US, "%,d", pump.water.getFill()) + " / "
                    + String.format(Locale.US, "%,d", pump.water.getMaxFill()) + "mB");
            if (pump.getBlockPos().getY() > GroundwaterPumps.groundHeight) {
                text.add("&[" + warningColor() + "&]! ! ! ALTITUDE ! ! !");
            }
            if (!pump.onGround) text.add("&[" + warningColor() + "&]! ! ! NO VALID GROUND ! ! !");
        }
        return new ILookOverlay.Data(getName().getString(), 0xffff00, 0x404000, text);
    }

    private static int warningColor() {
        return System.currentTimeMillis() % 1000 < 500 ? 0xff0000 : 0xffff00;
    }

    /** The four horizontal connection ports; the steam pump offers no power at any of them. */
    @Override
    protected void visitCells(BlockPos core, Direction facing, CellVisitor visitor) {
        super.visitCells(core, facing, visitor);
        int roles = steam ? ROLE_FLUID : ROLE_ALL;
        visitor.cell(core.east(), MASK_EAST, roles);
        visitor.cell(core.west(), MASK_WEST, roles);
        visitor.cell(core.south(), MASK_SOUTH, roles);
        visitor.cell(core.north(), MASK_NORTH, roles);
    }

    /** Only the electric pump takes power; the steam one is driven through its fluid inlet. */
    @Override
    protected void visitPassiveCells(BlockPos core, Direction facing, PassiveCellVisitor visitor) {
        int domains = steam ? PASSIVE_FLUID_IN : PASSIVE_POWER_IN | PASSIVE_FLUID_IN;
        visitor.passiveCell(core.east(), MASK_ALL, domains);
        visitor.passiveCell(core.west(), MASK_ALL, domains);
        visitor.passiveCell(core.south(), MASK_ALL, domains);
        visitor.passiveCell(core.north(), MASK_ALL, domains);
    }

    /** The steam pump runs its own working fluid through the ports the electric one uses for water alone. */
    @Override
    public boolean acceptsFluid(FluidType type) {
        if (steam) return type == Fluids.STEAM || type == Fluids.SPENTSTEAM || type == Fluids.WATER;
        return type == Fluids.WATER;
    }

    @Override
    public int[][] getAllPorts(ForgeDirection dir) {
        return rotatePorts(new int[][] {
                {1, 0, 0, ForgeDirection.EAST.ordinal()},
                {-1, 0, 0, ForgeDirection.WEST.ordinal()},
                {0, 0, 1, ForgeDirection.SOUTH.ordinal()},
                {0, 0, -1, ForgeDirection.NORTH.ordinal()},
        }, dir);
    }
}
