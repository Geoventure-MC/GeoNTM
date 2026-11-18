package fr.geoventure.geontm.tileentity.machine;

import fr.geoventure.geontm.api.block.IToolable.ToolType;
import fr.geoventure.geontm.blocks.multiblock.BlockMultiblockCore;
import fr.geoventure.geontm.explosion.vanillant.ExplosionVNT;
import fr.geoventure.geontm.explosion.vanillant.standard.EntityProcessorCrossSmooth;
import fr.geoventure.geontm.explosion.vanillant.standard.ExplosionEffectWeapon;
import fr.geoventure.geontm.explosion.vanillant.standard.PlayerProcessorStandard;
import fr.geoventure.geontm.sound.ModSounds;
import fr.geoventure.geontm.tileentity.deco.TileEntityPressablePlushie;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;

/** The authored press/plushie stroke and hand-drill operations, shared by both presses. */
public final class PlushiePress {

    private PlushiePress() {
    }

    public static SoundEvent sound(Level level, BlockPos pos) {
        if (!(level.getBlockEntity(pos.above()) instanceof TileEntityPressablePlushie plushie)
                || !plushie.isPressable()) return ModSounds.PRESS_OPERATE.get();
        if (plushie.isHundun()) {
            level.destroyBlock(pos, false);
            new ExplosionVNT(level, pos, 5F)
                    .setEntityProcessor(new EntityProcessorCrossSmooth(1D, 50F))
                    .setPlayerProcessor(new PlayerProcessorStandard())
                    .setSFX(new ExplosionEffectWeapon(10, 2.5F, 1F))
                    .explode();
        }
        return plushie.pressSound();
    }

    public static boolean drill(Level level, BlockPos pos, boolean cell, ToolType tool) {
        if (tool != ToolType.HAND_DRILL || !cell) return false;
        BlockMultiblockCore.withoutTeardown(() -> level.removeBlock(pos, false));
        return true;
    }
}
