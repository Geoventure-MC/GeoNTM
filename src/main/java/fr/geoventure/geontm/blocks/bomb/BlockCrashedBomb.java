package fr.geoventure.geontm.blocks.bomb;

import fr.geoventure.geontm.util.Owners;
import fr.geoventure.geontm.api.block.IToolable;
import fr.geoventure.geontm.config.BombConfig;
import fr.geoventure.geontm.entity.ModNtmEntities;
import fr.geoventure.geontm.entity.logic.EntityBalefire;
import fr.geoventure.geontm.entity.logic.EntityNukeExplosionMK5;
import fr.geoventure.geontm.explosion.vanillant.ExplosionVNT;
import fr.geoventure.geontm.explosion.vanillant.standard.BlockAllocatorStandard;
import fr.geoventure.geontm.explosion.vanillant.standard.BlockProcessorStandard;
import fr.geoventure.geontm.explosion.vanillant.standard.EntityProcessorCross;
import fr.geoventure.geontm.explosion.vanillant.standard.PlayerProcessorStandard;
import fr.geoventure.geontm.interfaces.IBomb;
import fr.geoventure.geontm.items.ModItems;
import fr.geoventure.geontm.main.MainRegistry;
import fr.geoventure.geontm.particle.helper.EntityEffects;
import fr.geoventure.geontm.particle.helper.ExplosionCreator;
import fr.geoventure.geontm.sound.ModSounds;
import fr.geoventure.geocore.registry.TickingBlock;
import fr.geoventure.geocore.world.CompatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import fr.geoventure.geontm.tileentity.bomb.TileEntityCrashedBomb;

/**
 * 1.7 {@code BlockCrashedBomb} ("Dud"): an unbreakable crashed bomb in one of four kinds, drawn by a
 * renderer at a position-seeded tumble. The 1.7 metadata enum is flattened to one block per kind
 * ({@code crashed_bomb_balefire}, {@code _conventional}, {@code _nuke}, {@code _salted}). Defusing it
 * yields its payload; detonating it (redstone does not, only a detonator does) sets that payload off.
 */
public class BlockCrashedBomb extends CompatBlock implements TickingBlock, IBomb, IToolable {

    public enum EnumDudType { BALEFIRE, CONVENTIONAL, NUKE, SALTED }

    public final EnumDudType type;

    public BlockCrashedBomb(Properties properties, EnumDudType type) {
        super(properties);
        this.type = type;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityCrashedBomb(pos, state);
    }

    /** 1.7 onBlockActivated with the defuser: drop the payload and remove the dud. */
    @Override
    public boolean onScrew(Level level, Player player, BlockPos pos, Direction side, Vec3 hit,
                           InteractionHand hand, ToolType tool) {
        if (tool != ToolType.DEFUSER) return false;
        if (level.isClientSide()) return true;

        switch (type) {
            case BALEFIRE -> dropItems(level, pos, new ItemStack(ModItems.egg_balefire_shard.get()));
            case CONVENTIONAL -> dropItems(level, pos, new ItemStack(ModItems.ball_tnt.get(), 16));
            case NUKE -> dropItems(level, pos, new ItemStack(ModItems.ball_tnt.get(), 8),
                    new ItemStack(ModItems.billet_plutonium.get(), 4));
            case SALTED -> dropItems(level, pos, new ItemStack(ModItems.ball_tnt.get(), 8),
                    new ItemStack(ModItems.billet_plutonium.get(), 2), new ItemStack(ModItems.ingot_cobalt.get(), 12));
        }
        level.destroyBlock(pos, false);
        return true;
    }

    public static void dropItems(Level level, BlockPos pos, ItemStack... drops) {
        for (ItemStack drop : drops) {
            level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop));
        }
    }

    @Override
    public BombReturnCode explode(Level level, BlockPos pos, @Nullable Entity detonator) {
        if (level.isClientSide()) return BombReturnCode.DETONATED;
        int x = pos.getX(), y = pos.getY(), z = pos.getZ();
        level.removeBlock(pos, false);

        switch (type) {
            case BALEFIRE -> {
                EntityBalefire bf = new EntityBalefire(ModNtmEntities.BALEFIRE.get(), level);
                bf.setPos(x, y, z);
                bf.destructionRange = (int) (BombConfig.fatmanRadius * 1.25);
                level.addFreshEntity(Owners.give(bf, Owners.detonator(detonator)));
                spawnMush(level, x, y, z, true);
            }
            case CONVENTIONAL -> {
                ExplosionVNT xnt = new ExplosionVNT(level, x + 0.5, y + 0.5, z + 0.5, 35F);
                xnt.setSource(Owners.sourceOf(detonator));
                xnt.setBlockAllocator(new BlockAllocatorStandard(24));
                xnt.setBlockProcessor(new BlockProcessorStandard().setNoDrop());
                xnt.setEntityProcessor(new EntityProcessorCross(5D).withRangeMod(1.5F));
                xnt.setPlayerProcessor(new PlayerProcessorStandard());
                xnt.explode();
                ExplosionCreator.composeEffectLarge(level, x + 0.5, y + 0.5, z + 0.5);
            }
            case NUKE -> {
                EntityNukeExplosionMK5 mk5 = EntityNukeExplosionMK5.statFac(level, 35, x + 0.5, y + 0.5, z + 0.5);
                mk5.detonator = Owners.detonator(detonator);
                if (level.addFreshEntity(mk5)) mk5.startChunkLoading();
                spawnMush(level, x, y, z, MainRegistry.polaroidID == 11 || level.getRandom().nextInt(100) == 0);
            }
            case SALTED -> {
                EntityNukeExplosionMK5 mk5 = EntityNukeExplosionMK5.statFac(level, 25, x + 0.5, y + 0.5, z + 0.5).moreFallout(25);
                mk5.detonator = Owners.detonator(detonator);
                if (level.addFreshEntity(mk5)) mk5.startChunkLoading();
                spawnMush(level, x, y, z, MainRegistry.polaroidID == 11 || level.getRandom().nextInt(100) == 0);
            }
        }
        return BombReturnCode.DETONATED;
    }

    public static void spawnMush(Level level, int x, int y, int z, boolean balefire) {
        level.playSound(null, x + 0.5, y + 0.5, z + 0.5, ModSounds.MUKE_EXPLOSION.get(), SoundSource.BLOCKS, 15.0F, 1.0F);
        EntityEffects.muke(level, x + 0.5, y + 0.5, z + 0.5, false, balefire, 250);
    }
}
