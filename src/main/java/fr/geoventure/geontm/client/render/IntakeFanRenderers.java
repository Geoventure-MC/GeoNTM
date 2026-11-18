package fr.geoventure.geontm.client.render;

import fr.geoventure.geontm.blocks.machine.MachineFan;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineIntake;
import net.minecraft.world.level.block.entity.BlockEntityType;

/** Feature-owned client registration hook; the central renderer list calls this when integration lands. */
public final class IntakeFanRenderers {

    private IntakeFanRenderers() {}

    public static void register(GeoNtmBlockEntityRenderers.Sink sink,
                                BlockEntityType<TileEntityMachineIntake> intake,
                                BlockEntityType<MachineFan.TileEntityFan> fan) {
        GeoNtmBlockEntityRenderers.regGem(sink, intake, IntakeRenderer::new);
        GeoNtmBlockEntityRenderers.regGem(sink, fan, FanRenderer::new);
    }
}
