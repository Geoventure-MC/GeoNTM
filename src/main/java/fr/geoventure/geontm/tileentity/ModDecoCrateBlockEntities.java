package fr.geoventure.geontm.tileentity;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.blocks.ModDecoCrateBlocks;
import fr.geoventure.geontm.tileentity.deco.TileEntityBobble;
import fr.geoventure.geontm.tileentity.deco.TileEntityEmitter;
import fr.geoventure.geontm.tileentity.deco.TileEntityGlyphidSpawner;
import fr.geoventure.geontm.tileentity.deco.TileEntityPartEmitter;
import fr.geoventure.geontm.tileentity.deco.TileEntityPlushie;
import fr.geoventure.geontm.tileentity.deco.TileEntitySnowglobe;
import fr.geoventure.geontm.tileentity.deco.TileEntityVendingMachine;
import fr.geoventure.geontm.tileentity.machine.TileEntityBroadcaster;
import fr.geoventure.geontm.tileentity.machine.TileEntityRadioRec;
import fr.geoventure.geontm.tileentity.machine.TileEntityRadiobox;
import fr.geoventure.geontm.tileentity.machine.storage.TileEntityCrateTungsten;
import fr.geoventure.geontm.tileentity.machine.storage.TileEntityFileCabinet;
import fr.geoventure.geontm.tileentity.machine.storage.TileEntitySafe;
import fr.geoventure.geocore.registry.BlockEntities;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

/** Block-entity types of the storage crates, decorative props and simple blocks ({@link ModDecoCrateBlocks}). */
public final class ModDecoCrateBlockEntities {

    private ModDecoCrateBlockEntities() {}

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    private static final Reg REG = Reg.of(BLOCK_ENTITIES);

    public static final RegistryObject<BlockEntityType<TileEntityCrateTungsten>> CRATE_TUNGSTEN =
            REG.blockEntity("crate_tungsten", () -> BlockEntities.ticking(TileEntityCrateTungsten::new,
                    TileEntityCrateTungsten::clientTick, TileEntityCrateTungsten::serverTick,
                    ModDecoCrateBlocks.crate_tungsten.get()));
    public static final RegistryObject<BlockEntityType<TileEntitySafe>> SAFE =
            REG.blockEntity("safe", () -> BlockEntities.type(TileEntitySafe::new, ModDecoCrateBlocks.safe.get()));
    public static final RegistryObject<BlockEntityType<TileEntityFileCabinet>> FILE_CABINET =
            REG.blockEntity("filing_cabinet", () -> BlockEntities.ticking(TileEntityFileCabinet::new,
                    TileEntityFileCabinet::tick, TileEntityFileCabinet::tick, ModDecoCrateBlocks.filing_cabinet.get()));
    public static final RegistryObject<BlockEntityType<TileEntityBobble>> BOBBLE =
            REG.blockEntity("bobblehead", () -> BlockEntities.type(TileEntityBobble::new,
                    ModDecoCrateBlocks.bobblehead.get()));
    public static final RegistryObject<BlockEntityType<TileEntityPlushie>> PLUSHIE =
            REG.blockEntity("plushie", () -> BlockEntities.clientTicking(TileEntityPlushie::new,
                    TileEntityPlushie::clientTick, ModDecoCrateBlocks.plushie.get()));
    public static final RegistryObject<BlockEntityType<TileEntitySnowglobe>> SNOWGLOBE =
            REG.blockEntity("snowglobe", () -> BlockEntities.type(TileEntitySnowglobe::new,
                    ModDecoCrateBlocks.snowglobe.get()));
    public static final RegistryObject<BlockEntityType<TileEntityVendingMachine>> VENDING_MACHINE =
            REG.blockEntity("vending_machine", () -> BlockEntities.type(TileEntityVendingMachine::new,
                    ModDecoCrateBlocks.vending_machine.get()));
    public static final RegistryObject<BlockEntityType<TileEntityBroadcaster>> BROADCASTER =
            REG.blockEntity("broadcaster_pc", () -> BlockEntities.ticking(TileEntityBroadcaster::new,
                    TileEntityBroadcaster::clientTick, TileEntityBroadcaster::serverTick,
                    ModDecoCrateBlocks.broadcaster_pc.get()));
    public static final RegistryObject<BlockEntityType<TileEntityRadiobox>> RADIOBOX =
            REG.blockEntity("radiobox", () -> BlockEntities.serverTicking(TileEntityRadiobox::new,
                    TileEntityRadiobox::serverTick, ModDecoCrateBlocks.radiobox.get()));
    public static final RegistryObject<BlockEntityType<TileEntityRadioRec>> RADIOREC =
            REG.blockEntity("radiorec", () -> BlockEntities.serverTicking(TileEntityRadioRec::new,
                    TileEntityRadioRec::serverTick, ModDecoCrateBlocks.radiorec.get()));
    public static final RegistryObject<BlockEntityType<TileEntityEmitter>> EMITTER =
            REG.blockEntity("deco_emitter", () -> BlockEntities.serverTicking(TileEntityEmitter::new,
                    TileEntityEmitter::serverTick, ModDecoCrateBlocks.deco_emitter.get()));
    public static final RegistryObject<BlockEntityType<TileEntityPartEmitter>> PART_EMITTER =
            REG.blockEntity("part_emitter", () -> BlockEntities.serverTicking(TileEntityPartEmitter::new,
                    TileEntityPartEmitter::serverTick, ModDecoCrateBlocks.part_emitter.get()));
    public static final RegistryObject<BlockEntityType<TileEntityGlyphidSpawner>> GLYPHID_SPAWNER =
            REG.blockEntity("glyphid_spawner", () -> BlockEntities.serverTicking(TileEntityGlyphidSpawner::new,
                    TileEntityGlyphidSpawner::serverTick, ModDecoCrateBlocks.glyphid_spawner.get()));

    public static void init() {}
}
