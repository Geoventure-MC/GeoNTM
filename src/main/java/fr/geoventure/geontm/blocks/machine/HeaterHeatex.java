package fr.geoventure.geontm.blocks.machine;

import fr.geoventure.geontm.capability.ICapabilityBlock;
import fr.geoventure.geontm.capability.MachineCaps;
import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geontm.blocks.ITooltipProvider;
import fr.geoventure.geontm.api.fluidmk2.IFluidConnectorBlockMK2;
import fr.geoventure.geontm.blocks.multiblock.BlockMultiblockMachine;
import fr.geoventure.geontm.blocks.multiblock.MultiblockSurface;
import fr.geoventure.geontm.interfaces.ILookOverlay;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.items.machine.IItemFluidId;
import fr.geoventure.geontm.tileentity.ModBlockEntities;
import fr.geoventure.geontm.tileentity.machine.TileEntityHeaterHeatex;
import fr.geoventure.geocore.gui.Menus;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/** The 3x3 legacy heat-exchanger multiblock. */
public class HeaterHeatex extends BlockMultiblockMachine implements IFluidConnectorBlockMK2, ILookOverlay, ITooltipProvider, ICapabilityBlock {

    public HeaterHeatex(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntity createCoreBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityHeaterHeatex(pos, state);
    }

    @Override
    public int[] getDimensions() {
        return new int[]{0, 0, 1, 1, 1, 1};
    }

    @Override
    public int getOffset() {
        return 1;
    }

    /** Sneak + fluid identifier changes the input type; ordinary use opens the legacy menu. */
    @Override
    protected InteractionResult onUseItem(BlockState state, Level level, BlockPos pos, Player player,
                                          InteractionHand hand, ItemStack stack, BlockHitResult hit) {
        if (player.isShiftKeyDown()) {
            if (!(stack.getItem() instanceof IItemFluidId identifier)) {
                return InteractionResult.PASS;
            }
            if (level.isClientSide()) return InteractionResult.SUCCESS;

            BlockEntity core = MultiblockSurface.coreEntity(level, pos);
            if (!(core instanceof TileEntityHeaterHeatex heater)) return InteractionResult.FAIL;
            FluidType type = identifier.getType(level, pos.getX(), pos.getY(), pos.getZ(), stack);
            heater.tanks[0].setTankType(type);
            heater.setChanged();
            player.sendSystemMessage(Component.literal("Changed type to ").withStyle(ChatFormatting.YELLOW)
                    .append(type.getDisplayName()).append(Component.literal("!")));
            return InteractionResult.SUCCESS;
        }

        return openMenu(level, pos, player);
    }

    @Override
    protected InteractionResult onUseWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                                 BlockHitResult hit) {
        if (player.isShiftKeyDown()) return InteractionResult.PASS;
        return openMenu(level, pos, player);
    }

    private InteractionResult openMenu(Level level, BlockPos pos, Player player) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.PASS;
        }
        BlockPos core = MultiblockSurface.coreOfAny(level, pos);
        if (core == null || !(level.getBlockEntity(core) instanceof TileEntityHeaterHeatex heater)) {
            return InteractionResult.PASS;
        }
        Menus.openWithPos(serverPlayer, heater, core);
        return InteractionResult.SUCCESS;
    }

    @Override
    public ILookOverlay.Data getLookOverlay(Level level, BlockPos pos, Player player) {
        if (!(MultiblockSurface.coreEntity(level, pos) instanceof TileEntityHeaterHeatex heater)) return null;
        List<String> text = new ArrayList<>();
        text.add(String.format("%,d", heater.heatEnergy) + " TU");
        return new ILookOverlay.Data(getName().getString(), 0xffff00, 0x404000, text);
    }

    /** The four corner cells are the exchanger's only fluid ports: two on the face, two on the back. */
    @Override
    protected void visitCells(BlockPos core, Direction facing, CellVisitor visitor) {
        super.visitCells(core, facing, visitor);
        Direction rot = facing.getClockWise();
        visitor.cell(core.relative(facing).relative(rot), MASK_SOUTH);
        visitor.cell(core.relative(facing).relative(rot, -1), MASK_SOUTH);
        visitor.cell(core.relative(facing, -1).relative(rot), MASK_NORTH);
        visitor.cell(core.relative(facing, -1).relative(rot, -1), MASK_NORTH);
    }

    @Override
    protected void visitPassiveCells(BlockPos core, Direction facing, PassiveCellVisitor visitor) {
        Direction rot = facing.getClockWise();
        visitor.passiveCell(core.relative(facing).relative(rot), MASK_ALL, PASSIVE_FLUID_IN);
        visitor.passiveCell(core.relative(facing).relative(rot, -1), MASK_ALL, PASSIVE_FLUID_IN);
        visitor.passiveCell(core.relative(facing, -1).relative(rot), MASK_ALL, PASSIVE_FLUID_IN);
        visitor.passiveCell(core.relative(facing, -1).relative(rot, -1), MASK_ALL, PASSIVE_FLUID_IN);
    }

    @Override
    public int[][] getAllPorts(ForgeDirection dir) {
        return rotatePorts(new int[][] {
                {1, 0, 1, ForgeDirection.SOUTH.ordinal()},
                {-1, 0, 1, ForgeDirection.SOUTH.ordinal()},
                {1, 0, -1, ForgeDirection.NORTH.ordinal()},
                {-1, 0, -1, ForgeDirection.NORTH.ordinal()},
        }, dir);
    }

    @Override
    public double[][] getAABBExtras() {
        return new double[][] {
                {1, 1, 0.5, -0.3, 0.4, -0.4, 1},
        };
    }

    /** Live connection rules of the corresponding 1.7 tile, shared by native and foreign contacts. */
    private static final MachineCaps CAPS = MachineCaps.blockKeyed().fluidIn().fluidOut()
            .fluidFaces(BlockEntity.class, (be, side) -> side == null || side.getAxis() == be.getBlockState().getValue(FACING).getAxis());

    @Override
    public MachineCaps caps() { return CAPS; }
}
