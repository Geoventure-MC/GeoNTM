package fr.geoventure.geontm.gametest;

import fr.geoventure.geontm.space.structure.BodyStructures;
import fr.geoventure.geontm.space.structure.SpaceStructures;
import fr.geoventure.geontm.world.gen.NTMWorldGenerator;
import fr.geoventure.geontm.world.gen.nbt.NBTStructure;
import fr.geoventure.geontm.world.gen.nbt.SpawnCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;

import java.util.List;

/**
 * NTM: Space sur les astres d'Ad Astra : les 37 fichiers importés se chargent, chaque structure d'astre est enregistrée,
 * les pièces uniques se résolvent dans les quatre rotations, et chaque astre a un tirage de région qui finit par élire
 * une structure.
 */
public final class SpaceStructureGameTests {

    private SpaceStructureGameTests() {}

    static final List<String> SPAWNS = List.of(SpaceStructures.MUN_BASE, SpaceStructures.MINMUS_BASE, SpaceStructures.MOHO_BASE,
            SpaceStructures.DRES_RBMK, SpaceStructures.DUNA_COMMS, SpaceStructures.IKE_ARTIFACT,
            SpaceStructures.LAYTHE_VERTIBIRD, SpaceStructures.LAYTHE_CRASHED_VERTIBIRD, SpaceStructures.METEOR_DUNGEON);

    public static void spawnsRegistered(GameTestHelper helper) {
        NTMWorldGenerator.registerSpawns();
        SpaceStructures.registerSpawns();
        for (NBTStructure file : SpaceStructures.Files.ALL) helper.assertTrue(file.isLoaded(), "fichier non chargé : " + file);
        for (String name : SPAWNS) helper.assertTrue(NBTStructure.getStructure(name) != null, "non enregistrée : " + name);
        helper.succeed();
    }

    public static void singlePiecesResolve(GameTestHelper helper) {
        SpaceStructures.registerSpawns();
        BlockPos origin = helper.absolutePos(new BlockPos(0, 1, 0));
        for (String name : List.of(SpaceStructures.DUNA_COMMS, SpaceStructures.IKE_ARTIFACT)) {
            SpawnCondition spawn = NBTStructure.getStructure(name);
            for (int rot = 0; rot < 4; rot++) {
                NBTStructure.Component c = new NBTStructure.Component(spawn, spawn.structure, origin.getX(), origin.getY(), origin.getZ(), rot);
                int[] score = c.verify(helper.getLevel());
                helper.assertTrue(score[1] > 0, name + " : palette vide (rotation " + rot + ")");
            }
        }
        helper.succeed();
    }

    public static void everyBodyPicksSomething(GameTestHelper helper) {
        SpaceStructures.registerSpawns();
        for (BodyStructures.Body body : BodyStructures.Body.values()) {
            helper.assertTrue(!body.members().isEmpty(), body.id() + " sans structure");
            BodyStructures.Biome biome = body.biomes().iterator().next();
            boolean found = false;
            for (int cx = 0; cx < 400 && !found; cx++) {
                for (int cz = 0; cz < 40 && !found; cz++) {
                    var pick = BodyStructures.pick(body, 12345L, cx, cz, biome);
                    if (pick != null && pick.member().spawn() != null) found = true;
                }
            }
            helper.assertTrue(found, body.id() + " : aucune structure élue sur 16 000 chunks");
        }
        helper.succeed();
    }
}
