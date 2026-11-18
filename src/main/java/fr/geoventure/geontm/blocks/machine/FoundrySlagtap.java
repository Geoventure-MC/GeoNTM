package fr.geoventure.geontm.blocks.machine;

import fr.geoventure.geontm.tileentity.ModBlockEntities;
import fr.geoventure.geontm.tileentity.machine.TileEntityFoundryOutlet;
import fr.geoventure.geontm.tileentity.machine.TileEntityFoundrySlagtap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

/** Foundry slagtap (1.12.2 {@code FoundrySlagtap}), the outlet variant that dumps molten waste into
 * the world as dynamic slag piles (raytraces 15 blocks down). */
public class FoundrySlagtap extends FoundryOutlet {

    public FoundrySlagtap(Properties properties) {
        super(properties);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityFoundrySlagtap(pos, state);
    }

}
