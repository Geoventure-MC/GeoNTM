package fr.geoventure.geontm.space.structure;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.registry.RegistryObject;
import fr.geoventure.geocore.world.Structures;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

/**
 * The planet structures' types: {@code ntmspace:body_nbt} (hbm-space's NBT structures and the meteor dungeon, per body,
 * {@link BodyNbtStructure}; its pieces are HBM's {@code hbm:nbt_component}) and {@code ntmspace:body_dungeon} (the
 * spaceship and Ike's tomb, {@link BodyDungeonStructure}, piece {@code ntmspace:body_dungeon_piece}). The structures,
 * structure sets and their placement are data ({@code data/ntmspace/worldgen/structure}, {@code structure_set}).
 */
public final class SpaceStructureTypes {

    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_TYPE, Constants.MOD_ID);
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES =
            DeferredRegister.create(Registries.STRUCTURE_PIECE, Constants.MOD_ID);
    private static final Reg REG = Reg.of(STRUCTURE_TYPES, STRUCTURE_PIECES);

    public static final RegistryObject<StructureType<BodyNbtStructure>> BODY_NBT =
            REG.register(Registries.STRUCTURE_TYPE, "body_nbt", () -> Structures.type(BodyNbtStructure.CODEC));

    private SpaceStructureTypes() {}

    /** Registers the types and the 1.7 spawns ({@link SpaceStructures}). */
    public static void init() {
        SpaceStructures.registerSpawns();
    }
}
