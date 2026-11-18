package fr.geoventure.geontm.client;

import fr.geoventure.geontm.blocks.generic.BlockWandJigsaw;
import fr.geoventure.geontm.blocks.generic.BlockWandStructure;
import fr.geoventure.geontm.blocks.generic.BlockWandTandem;
import fr.geoventure.geontm.blocks.generic.ModStructureWandBlocks;
import fr.geoventure.geontm.client.render.GeoNtmBlockEntityRenderers;
import fr.geoventure.geontm.client.render.RenderWandStructure;
import fr.geoventure.geontm.inventory.gui.GUIStructureLoad;
import fr.geoventure.geontm.inventory.gui.GUIStructureSave;
import fr.geoventure.geontm.inventory.gui.GUIWandJigsaw;
import fr.geoventure.geontm.inventory.gui.GUIWandTandem;
import net.minecraft.client.Minecraft;

/**
 * Client side of the structure wand blocks: the screens their right-click opens (1.7 opened them client-side with
 * no container) and the saving block's box renderer.
 */
public final class StructureWandClientRegistrations {

    private StructureWandClientRegistrations() {}

    public static void registerRenderers(GeoNtmBlockEntityRenderers.Sink sink) {
        BlockWandJigsaw.OPEN_SCREEN = tile -> Minecraft.getInstance().setScreen(new GUIWandJigsaw(tile));
        BlockWandTandem.OPEN_SCREEN = tile -> Minecraft.getInstance().setScreen(new GUIWandTandem(tile));
        BlockWandStructure.OPEN_SAVE = tile -> Minecraft.getInstance().setScreen(new GUIStructureSave(tile));
        BlockWandStructure.OPEN_LOAD = tile -> Minecraft.getInstance().setScreen(new GUIStructureLoad(tile));

        sink.register(ModStructureWandBlocks.WAND_STRUCTURE_ENTITY.get(), ctx -> new RenderWandStructure());
    }
}
