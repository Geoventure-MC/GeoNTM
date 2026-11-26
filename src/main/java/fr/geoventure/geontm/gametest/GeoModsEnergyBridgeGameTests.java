package fr.geoventure.geontm.gametest;

import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.gametest.GameTests;
import fr.geoventure.geocore.world.EnergyCompat;
import fr.geoventure.geocore.world.EnergyView;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.api.energymk2.EnergyConversion;
import fr.geoventure.geontm.api.energymk2.ForeignEnergyBridge;
import fr.geoventure.geontm.api.energymk2.IEnergyHandlerMK2;
import fr.geoventure.geontm.api.energymk2.PowerNetwork;
import fr.geoventure.geontm.blocks.machine.ModMachineBlocks;
import fr.geoventure.geontm.tileentity.machine.TileEntityMachineBattery;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Geoventure: bilan d'énergie entre le réseau de GeoMods (câbles et batteries en Forge Energy) et celui de GeoNTM
 * (HE). Une chaîne de blocs des deux mods est montée côte à côte, puis l'énergie TOTALE (somme des stocks des deux côtés,
 * en HE, plus la réserve du pont des câbles NTM) est relevée dans le temps : au repos elle doit rester constante, et
 * un aller-retour entre les deux réseaux ne doit rien consommer.
 *
 * <p>Ces tests ont besoin du mod GeoMods chargé (le banc ne l'a pas : ils réussissent alors sans rien faire). Pour les
 * jouer pour de bon, déposer dans {@code versions/1.20.1-forge/runs/gametest/mods} GeckoLib et GeoMods compilés avec les
 * noms officiels (le banc n'est pas en SRG), le {@code mods.toml} de GeoMods ramené à la version de Forge du banc, puis
 * lancer {@code bash tools/run_gametests.sh -Pgametest.filter=geontm:power_geomods_bridge_energy_balance}. Sans filtre,
 * retirer ce dossier : avec GeoMods, le reste de la suite ne se comporte plus comme sans lui.
 *
 * <p>Avant la correction de {@code EnergyPush.toForeign} (une batterie NTM en tampon repoussant dans le câble GeoMods qui
 * la rééquilibre), la première chaîne perdait 3 750 HE entre les ticks 250 et 400 sans que rien ne la consomme.
 */
public final class GeoModsEnergyBridgeGameTests {

    public static final GameTests.Suite TESTS = GameTests.suite(Constants.MOD_ID);
    private static final int Y0 = 12;

    /** Une chaîne par ligne : la jonction GeoMods / GeoNTM dans chaque sens, au repos puis en charge. */
    public static final GameTests.Function BALANCE =
            TESTS.function("power_geomods_bridge_energy_balance", 500, () -> GeoModsEnergyBridgeGameTests::balance);

    /** Relevé du bilan au début du régime établi, puis à la fin : au repos le total ne doit plus bouger. */
    private static final int SETTLED = 250, END = 400;
    /** Marge d'arrondi : quelques GE de reste de division dans les câbles GeoMods, jamais plus. */
    private static final long SLACK_HE = 100;

    private GeoModsEnergyBridgeGameTests() {}

    public static void init() {}

    private static boolean absent(GameTestHelper helper) {
        if (EnergyCompat.supported() && BuiltInRegistries.BLOCK.containsKey(Ids.of("geomods", "cable"))) return false;
        helper.succeed();
        return true;
    }

    private static Block gm(String path) {
        return BuiltInRegistries.BLOCK.getOptional(Ids.of("geomods", path)).orElseThrow();
    }

    /** One element of a line along +x, the line starting at x = 6. */
    private record Line(List<BlockPos> geomods, List<BlockPos> ntm, List<BlockPos> ntmCables, List<BlockPos> all) {}

    private static BlockPos put(GameTestHelper helper, Line line, int x, int y, Block block, int kind) {
        BlockPos pos = helper.absolutePos(new BlockPos(x, y, 6));
        helper.getLevel().setBlock(pos, block.defaultBlockState(), 3);
        line.all.add(pos);
        (kind == 0 ? line.geomods : kind == 1 ? line.ntm : line.ntmCables).add(pos);
        return pos;
    }

    private static void fillGeoMods(ServerLevel level, BlockPos pos, int fe) {
        EnergyView view = EnergyCompat.find(level, pos, null);
        view.insert(fe);
    }

    private static long totalHe(ServerLevel level, Line line) {
        long total = 0;
        for (BlockPos p : line.geomods) {
            EnergyView view = EnergyCompat.find(level, p, null);
            if (view != null) total += EnergyConversion.heFromFe(view.amount());
        }
        for (BlockPos p : line.ntm) {
            BlockEntity be = level.getBlockEntity(p);
            if (be instanceof IEnergyHandlerMK2 h) total += h.getPower();
        }
        java.util.Set<ForeignEnergyBridge> seen = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>());
        for (BlockPos p : line.ntmCables) {
            ForeignEnergyBridge bridge = PowerNetwork.bridgeAt(level, p);
            if (bridge != null && seen.add(bridge)) total += bridge.injected();
        }
        return total;
    }

    private static String parts(ServerLevel level, Line line) {
        StringBuilder sb = new StringBuilder();
        for (BlockPos p : line.all) {
            BlockEntity be = level.getBlockEntity(p);
            long v;
            if (be instanceof IEnergyHandlerMK2 h) v = h.getPower();
            else {
                EnergyView view = EnergyCompat.find(level, p, null);
                v = view == null ? -1 : -view.amount() * 5; // négatif = FE de GeoMods (en HE)
            }
            sb.append(v).append(' ');
        }
        return sb.toString();
    }

    /**
     * Monte la chaîne décrite par {@code spec} le long de +x : {@code gb<FE>} batterie GeoMods remplie, {@code gc} câble
     * GeoMods, {@code gf} four électrique GeoMods, {@code nb<I|B|O><HE>} batterie NTM (entrée, tampon, sortie) remplie,
     * {@code nc} câble rouge NTM, {@code nf} four électrique NTM ; {@code *n} répète le jeton.
     */
    private static Line build(GameTestHelper helper, int y, String spec) {
        Line line = new Line(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        int x = 6;
        for (String token : spec.split(" ")) {
            int times = 1;
            if (token.contains("*")) {
                times = Integer.parseInt(token.substring(token.indexOf('*') + 1));
                token = token.substring(0, token.indexOf('*'));
            }
            for (int i = 0; i < times; i++, x++) {
                if (token.startsWith("gb")) {
                    BlockPos b = put(helper, line, x, y, gm("battery"), 0);
                    int fe = Integer.parseInt(token.substring(2));
                    if (fe > 0) fillGeoMods(helper.getLevel(), b, fe);
                } else if (token.equals("gc")) put(helper, line, x, y, gm("cable"), 0);
                else if (token.equals("gf")) put(helper, line, x, y, gm("electric_furnace"), 0);
                else if (token.startsWith("nb")) {
                    TileEntityMachineBattery nb = PortedBatteryAt(helper, line, x, y);
                    int mode = switch (token.charAt(2)) {
                        case 'I' -> TileEntityMachineBattery.MODE_INPUT;
                        case 'O' -> TileEntityMachineBattery.MODE_OUTPUT;
                        default -> TileEntityMachineBattery.MODE_BUFFER;
                    };
                    nb.redLow = (short) mode;
                    nb.redHigh = (short) mode;
                    nb.power = Long.parseLong(token.substring(3));
                } else if (token.equals("nc")) put(helper, line, x, y, ModMachineBlocks.red_cable.get(), 2);
                else if (token.equals("ng")) put(helper, line, x, y, ModMachineBlocks.creative_generator.get(), 1);
                else if (token.equals("nf")) put(helper, line, x, y, ModMachineBlocks.machine_electric_furnace_off.get(), 1);
                else throw new IllegalArgumentException(token);
            }
        }
        return line;
    }

    private record Chain(String spec, long initialHe, long initialNtmHe, Line line) {}

    private static long ntmHe(ServerLevel level, Line line) {
        long total = 0;
        for (BlockPos p : line.ntm) if (level.getBlockEntity(p) instanceof IEnergyHandlerMK2 h) total += h.getPower();
        return total;
    }

    /** Tous les relevés de chaque chaîne, l'énergie totale en HE, et ce que la chaîne doit respecter. */
    private static void balance(GameTestHelper helper) {
        if (absent(helper)) return;
        ServerLevel level = helper.getLevel();
        List<Chain> chains = new ArrayList<>();
        int index = 0;
        for (String spec : new String[]{
                // batterie GeoMods -> câbles GeoMods -> batterie NTM en tampon (le mode par défaut) : la boucle des fuites
                "gb500000 gc*6 nbB0",
                // batterie NTM en tampon pleine à moitié -> câbles GeoMods -> batterie GeoMods
                "nbB500000 gc*6 gb0",
                // machine NTM à l'arrêt au bout des câbles GeoMods, puis au bout d'un câble NTM
                "gb500000 gc*6 nf",
                "gb500000 gc*6 nc*2 nf",
                // batterie NTM en entrée : se remplit, puis plus rien ne bouge
                "gb500000 gc*6 nbI0",
                // l'autre sens : stockage NTM en sortie -> câbles NTM -> câbles GeoMods -> batterie GeoMods
                "nbO1000000 nc*2 gc*2 gb0",
                // contact direct d'une batterie NTM avec une machine GeoMods
                "nbO1000000 gf"}) {
            Line line = build(helper, Y0 + 4 * index++, spec);
            chains.add(new Chain(spec, totalHe(level, line), ntmHe(level, line), line));
        }
        long[] settled = new long[chains.size()], settledNtm = new long[chains.size()];
        helper.runAfterDelay(SETTLED, () -> {
            for (int i = 0; i < settled.length; i++) {
                settled[i] = totalHe(level, chains.get(i).line);
                settledNtm[i] = ntmHe(level, chains.get(i).line);
            }
        });
        helper.runAfterDelay(END, () -> {
            for (int i = 0; i < settled.length; i++) {
                Chain chain = chains.get(i);
                long end = totalHe(level, chain.line);
                Constants.LOG.info("[bridge] {} : {} HE au départ, {} au tick {}, {} au tick {} ; parts {}", chain.spec,
                        chain.initialHe, settled[i], SETTLED, end, END, parts(level, chain.line));
                helper.assertTrue(end <= chain.initialHe + SLACK_HE, chain.spec + " : de l'énergie a été créée ("
                        + chain.initialHe + " HE au départ, " + end + " au tick " + END + ")");
                // ce qui passe d'un réseau à l'autre coûte le transport des câbles GeoMods (quelques %) ; ce qui ne passe plus
                // ne doit rien coûter
                long ntm = ntmHe(level, chain.line);
                long carried = Math.abs(ntm - settledNtm[i]);
                helper.assertTrue(end >= settled[i] - SLACK_HE - carried * 3 / 100, chain.spec
                        + " : une fuite continue entre les deux réseaux, " + (settled[i] - end) + " HE perdus entre les ticks "
                        + SETTLED + " et " + END + " pour " + carried + " HE transportés (" + settled[i] + " puis " + end + ")");
                // l'énergie a bien passé d'un réseau à l'autre : un stockage NTM en sortie se vide, une machine ou une
                // batterie NTM en entrée se remplit
                if (chain.spec.startsWith("nbO")) helper.assertTrue(ntm < chain.initialNtmHe,
                        chain.spec + " : rien n'est sorti du stockage NTM (" + ntm + " HE)");
                else if (chain.spec.endsWith("nf") || chain.spec.endsWith("nbI0")) helper.assertTrue(ntm > chain.initialNtmHe,
                        chain.spec + " : rien n'est entré dans la machine NTM (" + ntm + " HE)");
            }
            helper.succeed();
        });
    }

    private static TileEntityMachineBattery PortedBatteryAt(GameTestHelper helper, Line line, int x, int y) {
        BlockPos pos = helper.absolutePos(new BlockPos(x, y, 6));
        TileEntityMachineBattery battery = PortedBattery.place(helper.getLevel(), pos);
        line.all.add(pos);
        line.ntm.add(pos);
        return battery;
    }
}
