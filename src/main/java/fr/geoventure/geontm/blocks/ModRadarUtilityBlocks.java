package fr.geoventure.geontm.blocks;

import fr.geoventure.geocore.registry.BlockTag;
import fr.geoventure.geocore.registry.Loot;
import fr.geoventure.geocore.registry.Preset;
import fr.geoventure.geontm.registration.CarriedDrops;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.dejson.Variants;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.blocks.machine.BlockHatch;
import fr.geoventure.geontm.blocks.machine.BlockSeal;
import fr.geoventure.geontm.blocks.machine.MachineForceField;
import fr.geoventure.geontm.blocks.machine.MachineOrbus;
import fr.geoventure.geontm.blocks.machine.MachineRadar;
import fr.geoventure.geontm.blocks.machine.MachineRadarLarge;
import fr.geoventure.geontm.blocks.machine.MachineRadarScreen;
import fr.geoventure.geontm.blocks.machine.MachineTeleporter;
import fr.geoventure.geontm.blocks.machine.SoyuzCapsule;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import fr.geoventure.geocore.world.Compat;
import fr.geoventure.geocore.world.CompatBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.UnaryOperator;

/**
 * The radar and utility machines' blocks, as 1.7 {@code ModBlocks} declared them: all {@code Material.iron}.
 * The radars, the screen, the forcefield and the capsule sit on the missile tab; the teleporter, the anchor, the
 * orbus and the seal controller on the machine tab; the hatch on no tab at all (only a controller places it).
 *
 * <p>Models, blockstates, loot and mining tags are stated on each registration (Reg; emitted by Dejson at build
 * time). The renderer-drawn blocks carry the empty placeholder model; their items are 1.7's
 * {@code ItemRenderBase} entries, typed OBJ item data in {@code RadarUtilityDeclarations}.
 */
public final class ModRadarUtilityBlocks {

    private ModRadarUtilityBlocks() {}

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCKS, ITEMS);

    /** Drops itself, pickaxe-mined. */
    private static final Preset SELF_PICKAXE = handle -> handle.loot(Loot.self()).tag(BlockTag.PICKAXE);
    /** Renderer-drawn folded machine: every state on the shared empty placeholder. */
    private static final Preset FOLDED = Presets.rendered(Variants.product(
            Variants.uniform("facing", "north", "east", "south", "west"),
            Variants.uniform("cell", "false", "true")));

    private static UnaryOperator<BlockBehaviour.Properties> iron(float hardness, float resistance) {
        return p -> p.strength(hardness, resistance).sound(SoundType.METAL).requiresCorrectToolForDrops();
    }

    /** Renderer-drawn: the block model is empty, so nothing may cull or suffocate against it. */
    private static UnaryOperator<BlockBehaviour.Properties> rendered(float hardness, float resistance) {
        return p -> iron(hardness, resistance).apply(p).noOcclusion()
                .isSuffocating((s, w, pos) -> false).isViewBlocking((s, w, pos) -> false);
    }

    public static final RegistryObject<MachineRadar> machine_radar =
            REG.block("machine_radar", MachineRadar::new, rendered(5.0F, 6.0F))
                    .transform(Presets.rendered(Variants.simple()).and(SELF_PICKAXE));
    public static final RegistryObject<MachineRadarLarge> machine_radar_large =
            REG.block("machine_radar_large", MachineRadarLarge::new, rendered(5.0F, 6.0F))
                    .transform(FOLDED.and(SELF_PICKAXE));
    public static final RegistryObject<MachineRadarScreen> radar_screen =
            REG.block("radar_screen", MachineRadarScreen::new, rendered(5.0F, 6.0F))
                    .transform(FOLDED.and(SELF_PICKAXE));
    public static final RegistryObject<MachineForceField> machine_forcefield =
            REG.block("machine_forcefield", MachineForceField::new, rendered(5.0F, 60.0F))
                    .transform(Presets.rendered(Variants.simple()).and(SELF_PICKAXE));
    public static final RegistryObject<MachineTeleporter> machine_teleporter =
            REG.block("machine_teleporter", MachineTeleporter::new, iron(5.0F, 6.0F))
                    // 1.7 icons: teleporter_top/_bottom/_side
                    .transform(Presets.cubeBottomTop("geontm:blocks/teleporter_bottom", "geontm:blocks/teleporter_side",
                            "geontm:blocks/teleporter_top").and(SELF_PICKAXE));
    public static final RegistryObject<CompatBlock> teleanchor =
            REG.block("teleanchor", CompatBlock::new, iron(5.0F, 6.0F))
                    // the anchor's bottom is its side
                    .transform(Presets.cubeBottomTop("geontm:blocks/tele_anchor_side", "geontm:blocks/tele_anchor_side",
                            "geontm:blocks/tele_anchor_top").and(SELF_PICKAXE));
    public static final RegistryObject<MachineOrbus> machine_orbus =
            REG.block("machine_orbus", MachineOrbus::new, rendered(5.0F, 6.0F))
                    .transform(FOLDED)
                    .loot(CarriedDrops.ALWAYS)
                    .tag(BlockTag.PICKAXE);
    public static final RegistryObject<BlockSeal> seal_controller =
            REG.block("seal_controller", BlockSeal::new, iron(10.0F, 60.0F))
                    // 1.7 BlockSeal.getIcon: seal_frame everywhere but the one face that looks at the placer
                    .transform(Presets.orientable("geontm:blocks/seal_controller", "geontm:blocks/seal_frame", "geontm:blocks/seal_frame")
                            .states(Variants.product(Variants.facing(), Variants.uniform("powered", "false", "true")))
                            .and(SELF_PICKAXE));
    /** 1.7: hardness and resistance {@code Float.POSITIVE_INFINITY} - unbreakable and blast-proof. */
    public static final RegistryObject<BlockHatch> seal_hatch =
            REG.block("seal_hatch", BlockHatch::new,
                    p -> p.strength(-1.0F, Float.POSITIVE_INFINITY).sound(SoundType.METAL))
                    .transform(Presets.cubeAll("geontm:blocks/seal_hatch_3"))
                    .loot(Loot.nothing());
    public static final RegistryObject<SoyuzCapsule> soyuz_capsule =
            REG.block("soyuz_capsule", SoyuzCapsule::new, rendered(5.0F, 6.0F))
                    .transform(Presets.rendered(Variants.uniform("rusted", "false", "true")).and(SELF_PICKAXE));

    static {
        item("machine_radar", machine_radar, GeoNtmTab.MISSILES);
        item("machine_radar_large", machine_radar_large, GeoNtmTab.MISSILES);
        item("radar_screen", radar_screen, GeoNtmTab.MISSILES);
        item("machine_forcefield", machine_forcefield, GeoNtmTab.MISSILES);
        item("soyuz_capsule", soyuz_capsule, GeoNtmTab.MISSILES);
        item("machine_teleporter", machine_teleporter, GeoNtmTab.MACHINES);
        item("teleanchor", teleanchor, GeoNtmTab.MACHINES);
        item("machine_orbus", machine_orbus, GeoNtmTab.MACHINES);
        item("seal_controller", seal_controller, GeoNtmTab.MACHINES);
        item("seal_hatch", seal_hatch, null);
    }

    private static void item(String name, RegistryObject<? extends Block> block, GeoNtmTab tab) {
        Reg.Handle<BlockItem> item = REG.blockItem(block, Compat::blockDescription);
        if (tab != null) item.tab(tab);
    }

    public static void init() {}
}
