package fr.geoventure.geontm.blocks.machine.pile;

import fr.geoventure.geontm.blocks.ModNuclearMiscBlocks;
import fr.geoventure.geontm.items.ModNuclearMiscItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import fr.geoventure.geontm.tileentity.machine.pile.TileEntityPileSource;

/** Pile Neutron Source / Pile Fuel (Bred) (LEGACY): the same class serves both source blocks. */
@Deprecated
public class BlockGraphiteSource extends BlockGraphiteDrilledTE {

    public BlockGraphiteSource(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityPileSource(pos, state);
    }

    @Override
    protected ItemStack getInsertedItem() {
        return new ItemStack(this == ModNuclearMiscBlocks.BLOCK_GRAPHITE_PLUTONIUM.get()
                ? ModNuclearMiscItems.PILE_ROD_PLUTONIUM.get() : ModNuclearMiscItems.PILE_ROD_SOURCE.get());
    }
}
