package fr.geoventure.geontm.client.render;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geontm.tileentity.MachineCore;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineCompressorCompact;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.GemBER;
import fr.geoventure.geocore.render.gem.Surface;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public final class CompressorCompactRenderer
        extends GemBER<TileEntityMachineCompressorCompact, CompressorCompactRenderState> {

    private final WavefrontObj model = WavefrontObj.load(
            Ids.of(Constants.MOD_ID, "models/machines/compressor_compact.obj"));
    private final Surface surface = Surface.cutout(
            Constants.MOD_ID + ":textures/models/machines/compressor_compact.png");

    @Override
    protected void setup(Gem.Model<CompressorCompactRenderState> declaration) {
        declaration.offScreen().viewDistance(256)
                .joint()
                .translate(0.5F, 0F, 0.5F)
                .angle(Gem.Axes.Y, state -> state.yaw)
                .child(model.all(), surface);
    }

    @Override public CompressorCompactRenderState createRenderState() {
        return new CompressorCompactRenderState();
    }

    @Override
    public void extract(TileEntityMachineCompressorCompact be, CompressorCompactRenderState state,
                        float partialTicks, Vec3 cameraPosition) {
        BlockState blockState = be.getBlockState();
        int meta = MachineCore.facingOrdinal(blockState, 2);
        ForgeDirection facing = ForgeDirection.getOrientation(meta);
        state.yaw = 90F + facing.getRotationDegrees();
    }
}
