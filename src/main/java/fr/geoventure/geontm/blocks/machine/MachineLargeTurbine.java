package fr.geoventure.geontm.blocks.machine;

import fr.geoventure.geontm.blocks.ITooltipProvider;
import fr.geoventure.geontm.api.energymk2.IEnergyConnectorBlock;
import fr.geoventure.geontm.api.fluidmk2.IFluidConnectorBlockMK2;
import fr.geoventure.geontm.blocks.multiblock.BlockMultiblockMachine;
import fr.geoventure.geontm.blocks.multiblock.MultiblockSurface;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.inventory.recipes.CoolingRecipes;
import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geontm.tileentity.ModBlockEntities;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineLargeTurbine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

/** The four-long legacy steam turbine multiblock. */
public class MachineLargeTurbine extends BlockMultiblockMachine
        implements IEnergyConnectorBlock, IFluidConnectorBlockMK2, ITooltipProvider {

    public MachineLargeTurbine(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntity createCoreBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityMachineLargeTurbine(pos, state);
    }

    @Override
    public int[] getDimensions() {
        return new int[]{1, 0, 3, 1, 1, 1};
    }

    @Override
    public int getOffset() {
        return 1;
    }

    @Override
    protected InteractionResult onUseWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                                   BlockHitResult hit) {
        if (player.isShiftKeyDown()) return InteractionResult.PASS;
        return super.onUseWithoutItem(state, level, pos, player, hit);
    }

    @Override
    public boolean canConnect(BlockGetter world, BlockPos pos, ForgeDirection dir) {
        return MultiblockSurface.coreEntity(world, pos) instanceof TileEntityMachineLargeTurbine turbine
                && IEnergyConnectorBlock.isPort(turbine.getPowerPos(), pos, dir);
    }

    /** The three steam ports carry power too; 1.7 keeps the rear anchor cell but gives it no port. */
    @Override
    protected void visitCells(BlockPos core, Direction facing, CellVisitor visitor) {
        super.visitCells(core, facing, visitor);

        Direction rot = facing.getClockWise();
        visitor.cell(core.relative(facing), MASK_SOUTH);
        visitor.cell(core.relative(rot), MASK_WEST);
        visitor.cell(core.relative(rot, -1), MASK_EAST);
    }

    @Override
    protected void visitPassiveCells(BlockPos core, Direction facing, PassiveCellVisitor visitor) {
        Direction rot = facing.getClockWise();
        int domains = PASSIVE_POWER_IN | PASSIVE_FLUID_IN;
        visitor.passiveCell(core.relative(facing), MASK_ALL, domains);
        visitor.passiveCell(core.relative(facing, -3), MASK_ALL, domains);
        visitor.passiveCell(core.relative(rot), MASK_ALL, domains);
        visitor.passiveCell(core.relative(rot, -1), MASK_ALL, domains);
    }

    /** A coolant with a cooling recipe, or the spent steam it returns; nothing else reaches the tanks. */
    @Override
    public boolean acceptsFluid(FluidType type) {
        return CoolingRecipes.get(type) != null || type == Fluids.SPENTSTEAM;
    }
}
