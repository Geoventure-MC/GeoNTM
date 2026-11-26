package fr.geoventure.geontm.gametest;

import fr.geoventure.geocore.gametest.GameTestHelpers;
import fr.geoventure.geocore.registry.BlockEntities;
import fr.geoventure.geontm.blocks.machine.ModMachineBlocks;
import fr.geoventure.geontm.blocks.multiblock.BlockMultiblockCore;
import fr.geoventure.geontm.entity.missile.EntityRocketLambda;
import fr.geoventure.geontm.entity.missile.EntityRocketSoyuz;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.items.ItemEnums.SoyuzSkinType;
import fr.geoventure.geontm.items.ModMissileItems;
import fr.geoventure.geontm.items.ModSatelliteItems;
import fr.geoventure.geontm.items.machine.ItemSatellite.EnumSatType;
import fr.geoventure.geontm.pont.PontEspace;
import fr.geoventure.geontm.tileentity.machine.TileEntityLaunchpadLambda;
import fr.geoventure.geontm.tileentity.machine.TileEntityLaunchpadSoyuz;
import fr.geoventure.geontm.tileentity.machine.TileEntitySoyuzLauncher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * GameTests de l'identification du lanceur d'un satellite : dernier opérateur (interface ouverte ou ordre de lancement,
 * dans les 10 minutes de jeu) -> poseur du pas de tir -> joueur le plus proche -> nul, pour les trois pas de tir.
 */
public final class SatelliteLanceurGameTests {

    private SatelliteLanceurGameTests() {}

    private static final BlockPos PAD = new BlockPos(3, 1, 3);
    private static final String CLE = "geoventure_lanceur";

    /** Ordre de choix, sans monde : l'opérateur récent prime, puis le poseur, puis le plus proche (évalué en dernier). */
    public static void choix(GameTestHelper helper) {
        UUID operateur = UUID.randomUUID();
        UUID placer = UUID.randomUUID();
        UUID proche = UUID.randomUUID();
        long now = 1_000_000L;
        AtomicInteger appels = new AtomicInteger();

        helper.assertTrue(operateur.equals(PontEspace.choisirLanceur(operateur, now - 5, now, placer, () -> proche)),
                "l'opérateur récent est préféré au poseur");
        helper.assertTrue(operateur.equals(PontEspace.choisirLanceur(operateur, now - PontEspace.FENETRE_OPERATEUR_TICKS, now, placer, () -> proche)),
                "10 minutes pile comptent encore");
        helper.assertTrue(placer.equals(PontEspace.choisirLanceur(operateur, now - PontEspace.FENETRE_OPERATEUR_TICKS - 1, now, placer, () -> proche)),
                "un opérateur de plus de 10 minutes cède la place au poseur");
        helper.assertTrue(placer.equals(PontEspace.choisirLanceur(null, 0, now, placer, () -> proche)),
                "sans opérateur, le poseur");
        helper.assertTrue(placer.equals(PontEspace.choisirLanceur(operateur, now + 50, now, placer, () -> proche)),
                "un geste daté du futur (horloge remise à zéro) n'est pas cru");
        helper.assertTrue(proche.equals(PontEspace.choisirLanceur(null, 0, now, null, () -> { appels.incrementAndGet(); return proche; })),
                "sans opérateur ni poseur, le joueur le plus proche");
        helper.assertTrue(appels.get() == 1, "la recherche du plus proche n'a lieu qu'une fois");
        PontEspace.choisirLanceur(operateur, now, now, placer, () -> { appels.incrementAndGet(); return proche; });
        PontEspace.choisirLanceur(null, 0, now, placer, () -> { appels.incrementAndGet(); return proche; });
        helper.assertTrue(appels.get() == 1, "le plus proche n'est pas cherché quand un opérateur ou un poseur suffit");
        helper.assertTrue(PontEspace.choisirLanceur(null, 0, now, null, () -> null) == null, "personne : nul");
        helper.succeed();
    }

    /** Soyuz Launcher (multibloc) : l'interface ouverte désigne le lanceur, le poseur est le repli. */
    public static void soyuzLauncher(GameTestHelper helper) {
        UUID placer = UUID.randomUUID();
        Player opener = player(helper);

        TileEntitySoyuzLauncher launcher = placeLauncher(helper);
        launcher.setPlacer(placer);
        launcher.createMenu(1, opener.getInventory(), opener);
        helper.assertTrue(opener.getUUID().equals(launcher.operateur()), "ouvrir l'interface retient le joueur");
        helper.assertTrue(opener.getUUID().equals(launchMarked(helper, launcher)),
                "le dernier à avoir ouvert l'interface est le lanceur, pas le poseur");

        launcher = placeLauncher(helper);
        launcher.setPlacer(placer);
        launcher.noterOperateur(opener.getUUID(), helper.getLevel().getGameTime() - PontEspace.FENETRE_OPERATEUR_TICKS - 1);
        helper.assertTrue(placer.equals(launchMarked(helper, launcher)), "opérateur trop ancien : repli sur le poseur");

        launcher = placeLauncher(helper);
        launcher.setPlacer(placer);
        helper.assertTrue(placer.equals(launchMarked(helper, launcher)), "jamais ouvert : le poseur");
        helper.succeed();
    }

    /** Pas de tir Soyuz : l'ordre de lancement reçu d'un joueur le retient ; sans lui, le poseur. */
    public static void soyuzPad(GameTestHelper helper) {
        UUID placer = UUID.randomUUID();
        Player sender = player(helper);

        TileEntityLaunchpadSoyuz pad = placeSoyuzPad(helper);
        pad.setPlacer(placer);
        CompoundTag other = new CompoundTag();
        other.putBoolean("cargo", false);
        pad.receiveControl(sender, other);
        helper.assertTrue(pad.operateur() == null, "un ordre autre que le lancement ne désigne personne");
        CompoundTag launch = new CompoundTag();
        launch.putBoolean("launch", true);
        pad.receiveControl(sender, launch);
        helper.assertTrue(sender.getUUID().equals(pad.operateur()), "l'ordre de lancement retient son expéditeur");
        helper.assertTrue(sender.getUUID().equals(liftSoyuzPad(helper, pad)), "l'expéditeur est le lanceur");

        pad = placeSoyuzPad(helper);
        pad.setPlacer(placer);
        pad.noterOperateur(null, 0L); // même bloc, même état : le bloc n'est pas recréé, on oublie l'opérateur à la main
        helper.assertTrue(placer.equals(liftSoyuzPad(helper, pad)), "lancement sans joueur (redstone, radio) : le poseur");
        helper.succeed();
    }

    /** Pas de tir Lambda : interface ouverte, repli sur le poseur, et mémoire qui survit à la sauvegarde. */
    public static void lambdaPad(GameTestHelper helper) {
        UUID placer = UUID.randomUUID();
        Player opener = player(helper);

        TileEntityLaunchpadLambda pad = placeLambdaPad(helper);
        pad.setPlacer(placer);
        pad.createMenu(1, opener.getInventory(), opener);
        helper.assertTrue(opener.getUUID().equals(liftLambdaPad(helper, pad)), "le dernier opérateur est le lanceur");

        pad = placeLambdaPad(helper);
        pad.setPlacer(placer);
        pad.noterOperateur(null, 0L); // même bloc, même état : le bloc n'est pas recréé, on oublie l'opérateur à la main
        helper.assertTrue(placer.equals(liftLambdaPad(helper, pad)), "jamais ouvert : le poseur");

        // sauvegarde : l'opérateur et son heure reviennent ; une ancienne sauvegarde sans la clé reste lisible
        pad = placeLambdaPad(helper);
        pad.noterOperateur(opener.getUUID(), 4321L);
        var registries = helper.getLevel().registryAccess();
        CompoundTag saved = BlockEntities.save(pad, registries);
        var loaded = BlockEntities.load(pad.getBlockPos(), pad.getBlockState(), saved, registries);
        helper.assertTrue(loaded instanceof TileEntityLaunchpadLambda l && opener.getUUID().equals(l.operateur()) && l.operateurTick() == 4321L,
                "l'opérateur et son heure persistent");
        TileEntityLaunchpadLambda fresh = placeLambdaPad(helper);
        fresh.noterOperateur(null, 0L);
        CompoundTag old = BlockEntities.save(fresh, registries);
        var oldLoaded = BlockEntities.load(fresh.getBlockPos(), fresh.getBlockState(), old, registries);
        helper.assertTrue(oldLoaded instanceof TileEntityLaunchpadLambda l2 && l2.operateur() == null,
                "une sauvegarde sans opérateur se relit sans lanceur");
        helper.succeed();
    }

    // ---- aides ----

    private static Player player(GameTestHelper helper) {
        return GameTestHelpers.mockPlayer(helper, GameType.SURVIVAL);
    }

    private static @Nullable UUID tagged(ItemStack stack) {
        return stack.getTag() != null && stack.getTag().hasUUID(CLE) ? stack.getTag().getUUID(CLE) : null;
    }

    private static ItemStack satellite() {
        return ModSatelliteItems.satellite.stack(EnumSatType.RAY_SCAN);
    }

    private static TileEntitySoyuzLauncher placeLauncher(GameTestHelper helper) {
        FoldedTestSupport.assemble(helper, ModMachineBlocks.soyuz_launcher.get(), PAD, Direction.EAST);
        return GameTestHelpers.blockEntity(helper, PAD, TileEntitySoyuzLauncher.class);
    }

    private static @Nullable UUID launchMarked(GameTestHelper helper, TileEntitySoyuzLauncher launcher) {
        launcher.inventory.setStackInSlot(TileEntitySoyuzLauncher.SLOT_ROCKET, ModMissileItems.missile_soyuz.stack(SoyuzSkinType.NORMAL));
        launcher.inventory.setStackInSlot(TileEntitySoyuzLauncher.SLOT_SATELLITE, satellite());
        launcher.tanks[0].setTankType(Fluids.KEROSENE);
        launcher.tanks[1].setTankType(Fluids.OXYGEN);
        launcher.tanks[0].setFill(TileEntitySoyuzLauncher.FUEL_REQUIRED);
        launcher.tanks[1].setFill(TileEntitySoyuzLauncher.FUEL_REQUIRED);
        launcher.power = launcher.getMaxPower();
        launcher.liftOff(helper.getLevel());
        var rockets = helper.getLevel().getEntitiesOfClass(EntityRocketSoyuz.class, new AABB(helper.absolutePos(PAD)).inflate(16));
        helper.assertTrue(rockets.size() == 1, "une fusée a quitté le pas de tir");
        UUID lanceur = tagged(rockets.get(0).getSat());
        rockets.forEach(Entity::discard);
        return lanceur;
    }

    private static TileEntityLaunchpadSoyuz placeSoyuzPad(GameTestHelper helper) {
        helper.setBlock(PAD, ModMachineBlocks.launchpad_soyuz.get().defaultBlockState().setValue(BlockMultiblockCore.FACING, Direction.NORTH));
        return GameTestHelpers.blockEntity(helper, PAD, TileEntityLaunchpadSoyuz.class);
    }

    private static @Nullable UUID liftSoyuzPad(GameTestHelper helper, TileEntityLaunchpadSoyuz pad) {
        pad.inventory.setStackInSlot(0, ModMissileItems.missile_soyuz.stack(SoyuzSkinType.NORMAL));
        pad.inventory.setStackInSlot(2, satellite());
        pad.tanks[0].setFill(100_000);
        pad.tanks[1].setFill(100_000);
        pad.liftOff(helper.getLevel());
        var rockets = helper.getLevel().getEntitiesOfClass(EntityRocketSoyuz.class, new AABB(helper.absolutePos(PAD)).inflate(16));
        helper.assertTrue(rockets.size() == 1, "une fusée Soyuz a quitté le pas de tir");
        UUID lanceur = tagged(rockets.get(0).getSat());
        rockets.forEach(Entity::discard);
        return lanceur;
    }

    private static TileEntityLaunchpadLambda placeLambdaPad(GameTestHelper helper) {
        helper.setBlock(PAD, ModMachineBlocks.launchpad_lambda.get().defaultBlockState().setValue(BlockMultiblockCore.FACING, Direction.NORTH));
        return GameTestHelpers.blockEntity(helper, PAD, TileEntityLaunchpadLambda.class);
    }

    private static @Nullable UUID liftLambdaPad(GameTestHelper helper, TileEntityLaunchpadLambda pad) {
        pad.inventory.setStackInSlot(0, new ItemStack(ModMissileItems.missile_lambda.get()));
        pad.inventory.setStackInSlot(1, satellite());
        pad.tanks[0].setFill(24_000);
        pad.tanks[1].setFill(24_000);
        pad.liftOff(helper.getLevel());
        var rockets = helper.getLevel().getEntitiesOfClass(EntityRocketLambda.class, new AABB(helper.absolutePos(PAD)).inflate(8));
        helper.assertTrue(rockets.size() == 1, "une fusée Lambda a quitté le pas de tir");
        UUID lanceur = tagged(rockets.get(0).getSat());
        rockets.forEach(Entity::discard);
        return lanceur;
    }
}
