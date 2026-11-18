package fr.geoventure.geontm.blocks.machine;

import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.registry.TickingBlock;
import fr.geoventure.geocore.world.CompatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineSiren;

/**
 * 1.7 {@code MachineSiren}: a plain {@code BlockContainer} with {@code block_steel} top and bottom,
 * {@code machine_siren} on the sides and no facing. It opens its one-slot menu on a non-sneaking click and
 * spills the cassette when broken.
 */
public class MachineSiren extends CompatBlock implements TickingBlock {

    public MachineSiren(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityMachineSiren(pos, state);
    }

    @Override
    protected InteractionResult onUseWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                                 BlockHitResult hit) {
        if (player.isShiftKeyDown()) return InteractionResult.PASS;
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer
                && level.getBlockEntity(pos) instanceof MenuProvider provider) {
            Menus.openWithPos(serverPlayer, provider, pos);
        }
        return InteractionResult.SUCCESS;
    }

}
