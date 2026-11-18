package fr.geoventure.geontm.blocks.turret;

import fr.geoventure.geontm.capability.ICapabilityBlock;
import fr.geoventure.geontm.capability.MachineCaps;
import net.minecraft.core.Direction;
import fr.geoventure.geontm.tileentity.ModBlockEntities;
import fr.geoventure.geocore.registry.TickingBlock;
import fr.geoventure.geontm.tileentity.turret.TileEntityTurretBaseNT;
import fr.geoventure.geontm.tileentity.turret.TileEntityTurretSentry;
import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.world.CompatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

/** 1.12.2's {@code TurretSentry} is a plain block container: the one turret that occupies a single cell. */
public class TurretSentry extends CompatBlock implements TickingBlock, ICapabilityBlock {

    private static final VoxelShape SHAPE = Shapes.block();

    public TurretSentry(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityTurretSentry(pos, state);
    }

    @Override
    protected InteractionResult onUseWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                                 BlockHitResult hit) {
        if (player.isShiftKeyDown()) return InteractionResult.PASS;
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (level.getBlockEntity(pos) instanceof MenuProvider menu && player instanceof ServerPlayer serverPlayer) {
            Menus.openWithPos(serverPlayer, menu, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    /** Live connection rules of the corresponding 1.7 tile, shared by native and foreign contacts. */
    private static final MachineCaps CAPS = MachineCaps.blockKeyed().powerIn().items()
            .faces(BlockEntity.class, (be, side) -> side == Direction.DOWN);

    @Override
    public MachineCaps caps() { return CAPS; }
}
