package fr.geoventure.geontm.gametest;

import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.gametest.GameTests;
import fr.geoventure.geocore.world.EnergyCompat;
import fr.geoventure.geocore.world.EnergyView;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.api.energymk2.EnergyConversion;
import fr.geoventure.geontm.api.energymk2.IEnergyConnectorBlock;
import fr.geoventure.geontm.api.energymk2.IEnergyConnectorMK2;
import fr.geoventure.geontm.api.energymk2.IEnergyHandlerMK2;
import fr.geoventure.geontm.blocks.machine.ModMachineBlocks;
import fr.geoventure.geontm.blocks.multiblock.BlockMultiblockCore;
import fr.geoventure.geontm.blocks.multiblock.MultiblockHandlerXR;
import fr.geoventure.geontm.blocks.network.BlockCable;
import fr.geoventure.geontm.capability.MachineEnergyExposure;
import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geontm.tileentity.machine.TileEntityHeaterElectric;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * Geoventure: another mod's cable (GeoMods' Forge Energy cable) laid against a machine. A consumer must take FE on every
 * face of every block it owns, not only at HBM's power ports, and a neighbour's push must land in the core's HE buffer.
 * The push is the neighbour's own call on the exposed storage ({@link EnergyView#insert}, which is {@code receiveEnergy});
 * the faces NTM's declaration closes on purpose (a launch pad's top) are asserted closed.
 */
public final class EnergyIntakeGameTests {

    public static final GameTests.Suite TESTS = GameTests.suite(Constants.MOD_ID);
    private static final int PADDING = 24;
    /** 200 FE is 1,000 HE at 5:1, below the smallest buffer tested (the soldering station's 2,000 HE). */
    private static final long PUSH_FE = 200;

    public static final GameTests.Function SMALL =
            TESTS.function("power_intake_small_machines", 200, PADDING, () -> h -> run(h, List.of(
                    new Case("machine_radar", all()), new Case("machine_deuterium_extractor", all()),
                    new Case("machine_soldering_station", all()), new Case("machine_centrifuge", all()))));
    public static final GameTests.Function MULTIBLOCK =
            TESTS.function("power_intake_every_block_of_multiblocks", 400, PADDING, () -> h -> run(h, List.of(
                    new Case("machine_chemical_plant", all()), new Case("machine_assembly_factory", all()),
                    new Case("machine_electrolyser", all()), new Case("fusion_klystron", all()))));
    public static final GameTests.Function BATTERY =
            TESTS.function("power_intake_batteries", 200, PADDING, () -> h -> run(h, List.of(
                    new Case("machine_battery", all()), new Case("machine_lithium_battery", all()),
                    // the REDD reports at most half of its BigInteger store, so only what it announced taking is checked
                    new Case("machine_battery_redd", all(), false))));
    public static final GameTests.Function DECLARED_FACES =
            TESTS.function("power_intake_keeps_declared_closed_faces", 200, PADDING, () -> h -> run(h, List.of(
                    // NTM states these faces closed (pad top, tower bottom)
                    new Case("launch_pad", side -> side.getAxis().isHorizontal()),
                    new Case("machine_vacuum_distill", side -> side != Direction.DOWN))));
    public static final GameTests.Function HEATER_STATE =
            TESTS.function("power_intake_follows_machine_state", 100, PADDING, () -> EnergyIntakeGameTests::heater);
    public static final GameTests.Function FORM_AND_BREAK =
            TESTS.function("power_intake_multiblock_forms_and_breaks", 100, PADDING, () -> EnergyIntakeGameTests::formAndBreak);
    public static final GameTests.Function CABLE_ARMS =
            TESTS.function("power_intake_hbm_cable_keeps_ports", 100, PADDING, () -> EnergyIntakeGameTests::hbmCable);

    private record Case(String id, Predicate<Direction> open, boolean readBack) {
        Case(String id, Predicate<Direction> open) { this(id, open, true); }
    }

    private EnergyIntakeGameTests() {}

    public static void init() {}

    private static Predicate<Direction> all() { return side -> true; }

    private static Block block(String id) {
        return BuiltInRegistries.BLOCK.getOptional(Ids.of(Constants.MOD_ID, id)).orElseThrow();
    }

    private static BlockPos assemble(GameTestHelper helper, Block block) {
        ServerLevel level = helper.getLevel();
        BlockPos core = helper.absolutePos(GameTests.paddedCentre(PADDING, 2));
        if (block instanceof BlockMultiblockCore multiblock) MultiblockHandlerXR.placeAssembled(level, core, multiblock, Direction.NORTH);
        else level.setBlock(core, block.defaultBlockState(), 3);
        return core;
    }

    private static void clear(ServerLevel level, List<BlockPos> footprint) {
        for (BlockPos cell : footprint) level.setBlock(cell, Blocks.AIR.defaultBlockState(), 2 | 16);
    }

    private static void run(GameTestHelper helper, List<Case> cases) {
        if (!EnergyCompat.supported()) {
            helper.succeed();
            return;
        }
        next(helper, cases, 0, new ArrayList<>());
    }

    private static void next(GameTestHelper helper, List<Case> cases, int index, List<String> problems) {
        if (index == cases.size()) {
            helper.assertTrue(problems.isEmpty(), problems.size() + " faces do not take a foreign push as declared: "
                    + problems.subList(0, Math.min(12, problems.size())));
            helper.succeed();
            return;
        }
        ServerLevel level = helper.getLevel();
        Case current = cases.get(index);
        BlockPos core = assemble(helper, block(current.id()));
        helper.runAfterDelay(5, () -> {
            List<BlockPos> footprint = ForeignEnergySweep.footprint(level, core);
            if (!(level.getBlockEntity(core) instanceof IEnergyHandlerMK2 machine)) {
                problems.add(current.id() + " has no energy handler at its core");
            } else {
                int pushed = 0;
                for (BlockPos cell : footprint) {
                    for (Direction side : Direction.values()) {
                        if (!level.getBlockState(cell.relative(side)).isAir()) continue;
                        EnergyView view = EnergyCompat.find(level, cell, side);
                        String where = current.id() + " " + helper.relativePos(cell) + " " + side;
                        if (!current.open().test(side)) {
                            if (view != null) problems.add(where + " is open though NTM closes it");
                            continue;
                        }
                        if (view == null) {
                            problems.add(where + " exposes no energy");
                            continue;
                        }
                        machine.setPower(0);
                        long taken = view.insert(PUSH_FE);
                        if (taken != PUSH_FE || current.readBack() && machine.getPower() != EnergyConversion.heFromFe(PUSH_FE)) {
                            problems.add(where + " took " + taken + " FE, core holds " + machine.getPower() + " HE");
                        }
                        pushed++;
                    }
                }
                if (pushed == 0) problems.add(current.id() + " has no face that takes a push");
                machine.setPower(0);
            }
            clear(level, footprint);
            next(helper, cases, index + 1, problems);
        });
    }

    /** A machine that is off (no buffer) refuses and says so; the same machine switched on takes the push. */
    private static void heater(GameTestHelper helper) {
        if (!EnergyCompat.supported()) {
            helper.succeed();
            return;
        }
        ServerLevel level = helper.getLevel();
        BlockPos core = assemble(helper, ModMachineBlocks.heater_electric.get());
        helper.runAfterDelay(5, () -> {
            TileEntityHeaterElectric heater = (TileEntityHeaterElectric) level.getBlockEntity(core);
            List<BlockPos> footprint = ForeignEnergySweep.footprint(level, core);
            BlockPos cell = null;
            Direction face = null;
            for (BlockPos candidate : footprint) {
                for (Direction side : Direction.values()) {
                    if (cell == null && level.getBlockState(candidate.relative(side)).isAir()
                            && EnergyCompat.find(level, candidate, side) != null) {
                        cell = candidate;
                        face = side;
                    }
                }
            }
            helper.assertTrue(cell != null, "the electric heater exposes no energy on any face");
            helper.assertFalse(EnergyCompat.find(level, cell, face).canInsert(), "an off heater (setting 0) has no buffer yet takes FE");
            heater.setting = 3;
            helper.assertTrue(EnergyCompat.find(level, cell, face).canInsert(), "the heater at setting 3 refuses FE");
            long taken = EnergyCompat.find(level, cell, face).insert(PUSH_FE);
            helper.assertTrue(taken == PUSH_FE && heater.power == EnergyConversion.heFromFe(PUSH_FE),
                    "the heater took " + taken + " FE and holds " + heater.power + " HE");
            heater.power = heater.getMaxPower();
            helper.assertTrue(EnergyCompat.find(level, cell, face) != null, "a full heater drops its energy face");
            helper.assertFalse(EnergyCompat.find(level, cell, face).canInsert(), "a full heater still announces room");
            clear(level, footprint);
            helper.succeed();
        });
    }

    /** The capability follows the structure: gone with a broken multiblock, back on the new one, on the same blocks. */
    private static void formAndBreak(GameTestHelper helper) {
        if (!EnergyCompat.supported()) {
            helper.succeed();
            return;
        }
        ServerLevel level = helper.getLevel();
        Block plant = block("machine_chemical_plant");
        BlockPos core = assemble(helper, plant);
        helper.runAfterDelay(5, () -> {
            List<BlockPos> footprint = ForeignEnergySweep.footprint(level, core);
            BlockPos outer = null;
            Direction face = null;
            for (BlockPos cell : footprint) {
                if (cell.equals(core)) continue;
                for (Direction side : Direction.values()) {
                    if (outer == null && level.getBlockState(cell.relative(side)).isAir()) {
                        outer = cell;
                        face = side;
                    }
                }
            }
            helper.assertTrue(outer != null && EnergyCompat.find(level, outer, face) != null,
                    "an outer block of the formed chemical plant exposes no energy");
            BlockPos broken = outer;
            Direction brokenFace = face;
            // a player breaking one block takes the structure down
            level.destroyBlock(broken, false);
            helper.runAfterDelay(2, () -> {
                for (BlockPos cell : footprint) {
                    for (Direction side : Direction.values()) {
                        helper.assertTrue(EnergyCompat.find(level, cell, side) == null,
                                "a broken chemical plant still exposes energy at " + helper.relativePos(cell) + " " + side);
                    }
                }
                BlockPos again = assemble(helper, plant);
                helper.runAfterDelay(5, () -> {
                    helper.assertTrue(EnergyCompat.find(level, broken, brokenFace) != null,
                            "the rebuilt chemical plant exposes no energy at the block that was broken");
                    clear(level, ForeignEnergySweep.footprint(level, again));
                    helper.succeed();
                });
            });
        });
    }

    /**
     * HBM's own cable keeps drawing arms to the ports only: the FE intake on the rest of a machine's body is for other
     * mods' cables and must not make a red cable look connected where it is not.
     */
    private static void hbmCable(GameTestHelper helper) {
        if (!EnergyCompat.supported()) {
            helper.succeed();
            return;
        }
        ServerLevel level = helper.getLevel();
        BlockPos core = assemble(helper, block("machine_chemical_plant"));
        helper.runAfterDelay(5, () -> {
            List<BlockPos> footprint = ForeignEnergySweep.footprint(level, core);
            int ports = 0, body = 0;
            for (BlockPos cell : footprint) {
                for (Direction side : Direction.values()) {
                    BlockPos outside = cell.relative(side);
                    if (!level.getBlockState(outside).isAir() || EnergyCompat.find(level, cell, side) == null) continue;
                    // what HBM's cable already drew an arm for: the block's connector rule or a power port
                    boolean port = MachineEnergyExposure.exposesAtPort(level, cell, side)
                            || level.getBlockState(cell).getBlock() instanceof IEnergyConnectorBlock connector
                            && connector.canConnect(level, cell, ForgeDirection.getOrientation(side))
                            || level.getBlockEntity(cell) instanceof IEnergyConnectorMK2 tile
                            && tile.canConnect(ForgeDirection.getOrientation(side));
                    level.setBlock(outside, ModMachineBlocks.red_cable.get().defaultBlockState(), 3);
                    // the shape update a neighbouring block change sends to the cable, which sets its arm
                    boolean arm = level.getBlockState(outside).updateShape(side.getOpposite(), level.getBlockState(cell), level,
                            outside, cell).getValue(BlockCable.PROPERTY_BY_DIRECTION.get(side.getOpposite()));
                    level.setBlock(outside, Blocks.AIR.defaultBlockState(), 2 | 16);
                    helper.assertTrue(arm == port, "a red cable at " + helper.relativePos(outside) + " draws its arm=" + arm
                            + " towards the plant, power port=" + port);
                    if (port) ports++;
                    else body++;
                }
            }
            helper.assertTrue(ports > 0 && body > ports, "the plant has " + ports + " port faces and " + body + " other faces");
            clear(level, footprint);
            helper.succeed();
        });
    }
}
