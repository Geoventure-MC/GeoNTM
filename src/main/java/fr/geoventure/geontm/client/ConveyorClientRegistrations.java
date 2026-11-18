package fr.geoventure.geontm.client;

import fr.geoventure.geontm.client.render.GeoNtmBlockEntityRenderers;
import fr.geoventure.geontm.blocks.network.CraneRouter;
import fr.geoventure.geontm.tileentity.ModConveyorBlockEntities;
import fr.geoventure.geontm.blocks.ModConveyorBlocks;
import fr.geoventure.geontm.inventory.ModConveyorMenus;
import fr.geoventure.geocore.client.BlockTint;
import fr.geoventure.geocore.client.Tints;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import fr.geoventure.geontm.client.render.PartitionerRenderer;
import fr.geoventure.geontm.client.render.PistonInserterRenderer;
import fr.geoventure.geontm.client.render.SplitterRenderer;
import fr.geoventure.geontm.inventory.gui.GUICraneBoxer;
import fr.geoventure.geontm.inventory.gui.GUICraneExtractor;
import fr.geoventure.geontm.inventory.gui.GUICraneGrabber;
import fr.geoventure.geontm.inventory.gui.GUICraneInserter;
import fr.geoventure.geontm.inventory.gui.GUICraneRouter;
import fr.geoventure.geontm.inventory.gui.GUICraneUnboxer;

/** Client hooks for the conveyor belt and crane family: crane screens, the OBJ renderers, the sorter's six side tints. */
public final class ConveyorClientRegistrations {

    private ConveyorClientRegistrations() {}

    public static void registerScreens(GeoNtmClientRegistrations.ScreenSink sink) {
        sink.register(ModConveyorMenus.CRANE_INSERTER.get(), GUICraneInserter::new);
        sink.register(ModConveyorMenus.CRANE_EXTRACTOR.get(), GUICraneExtractor::new);
        sink.register(ModConveyorMenus.CRANE_GRABBER.get(), GUICraneGrabber::new);
        sink.register(ModConveyorMenus.CRANE_ROUTER.get(), GUICraneRouter::new);
        sink.register(ModConveyorMenus.CRANE_BOXER.get(), GUICraneBoxer::new);
        sink.register(ModConveyorMenus.CRANE_UNBOXER.get(), GUICraneUnboxer::new);
    }

    public static void registerRenderers(GeoNtmBlockEntityRenderers.Sink sink) {
        GeoNtmBlockEntityRenderers.regGem(sink, ModConveyorBlockEntities.CRANE_SPLITTER.get(), SplitterRenderer::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModConveyorBlockEntities.CRANE_PARTITIONER.get(), PartitionerRenderer::new);
        GeoNtmBlockEntityRenderers.regGem(sink, ModConveyorBlockEntities.PISTON_INSERTER.get(), PistonInserterRenderer::new);
    }

    /** 1.7 {@code CraneRouter.colorMultiplier}: one colour per render pass, i.e. per side, i.e. per tint index. */
    public static void registerBlockColors() {
        List<BlockTint> tints = new ArrayList<>();
        for (int color : CraneRouter.SIDE_COLORS) {
            int argb = 0xFF000000 | color;
            tints.add(new BlockTint() {
                @Override
                public int color(BlockState state) {
                    return argb;
                }

                @Override
                public Set<Property<?>> relevantProperties() {
                    return Set.of();
                }
            });
        }
        Tints.registerBlockTints(tints, ModConveyorBlocks.CRANE_ROUTER);
    }
}
