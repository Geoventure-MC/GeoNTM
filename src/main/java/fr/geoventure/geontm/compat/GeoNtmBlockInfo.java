package fr.geoventure.geontm.compat;

import fr.geoventure.geontm.api.energymk2.IEnergyHandlerMK2;
import fr.geoventure.geontm.api.fluidmk2.IFluidUserMK2;
import fr.geoventure.geontm.blocks.multiblock.MultiblockSurface;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.inventory.fluid.tank.FluidTankNTM;
import fr.geoventure.geontm.inventory.material.Mats;
import fr.geoventure.geontm.inventory.recipes.FusionRecipe;
import fr.geoventure.geontm.inventory.recipes.PlasmaForgeRecipe;
import fr.geoventure.geontm.items.machine.ItemFluidIcon;
import fr.geoventure.geontm.tileentity.machine.TileEntityCrucible;
import fr.geoventure.geontm.tileentity.machine.TileEntityFireboxBase;
import fr.geoventure.geontm.tileentity.machine.TileEntityFoundryBase;
import fr.geoventure.geontm.tileentity.machine.TileEntityFurnaceCombination;
import fr.geoventure.geontm.tileentity.machine.TileEntityFurnaceSteel;
import fr.geoventure.geontm.tileentity.machine.TileEntityHeatBoiler;
import fr.geoventure.geontm.tileentity.machine.TileEntityHeatBoilerIndustrial;
import fr.geoventure.geontm.tileentity.machine.TileEntityHeaterElectric;
import fr.geoventure.geontm.tileentity.machine.TileEntityHeaterHeatex;
import fr.geoventure.geontm.tileentity.machine.TileEntityHeaterOilburner;
import fr.geoventure.geontm.tileentity.machine.TileEntityICF;
import fr.geoventure.geontm.tileentity.machine.TileEntitySILEX;
import fr.geoventure.geontm.tileentity.machine.dfc.TileEntityCore;
import fr.geoventure.geontm.tileentity.machine.dfc.TileEntityCoreEmitter;
import fr.geoventure.geontm.tileentity.machine.dfc.TileEntityCoreReceiver;
import fr.geoventure.geontm.tileentity.machine.fusion.TileEntityFusionBoiler;
import fr.geoventure.geontm.tileentity.machine.fusion.TileEntityFusionKlystron;
import fr.geoventure.geontm.tileentity.machine.fusion.TileEntityFusionMHDT;
import fr.geoventure.geontm.tileentity.machine.fusion.TileEntityFusionPlasmaForge;
import fr.geoventure.geontm.tileentity.machine.fusion.TileEntityFusionTorus;
import fr.geoventure.geocore.info.BlockInfo;
import fr.geoventure.geocore.info.Gauge;
import fr.geoventure.geocore.info.Tank;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.world.item.ItemStack;

/**
 * HBM's buffers in a block-info overlay (Jade): HE, tanks, heat and plasma (TU), klystron energy (KyU), DFC beams
 * (Spk), SILEX solution and molten materials, as 1.7's GUIs and look overlays print them. Folded multiblock cells
 * show their core. HE ramps blue to green so it never reads as FE. Values zeroed every tick are read from their
 * last-tick copies.
 */
public final class GeoNtmBlockInfo {

    private static final int HE_FROM = 0xFF1E50FF;
    private static final int HE_TO = 0xFF30E060;
    private static final int TU_FROM = 0xFF901C00;
    private static final int TU_TO = 0xFFFFA030;
    private static final int KYU_FROM = 0xFF5028D0;
    private static final int KYU_TO = 0xFFE850FF;
    private static final int SPK_FROM = 0xFF7F1F1F;
    private static final int SPK_TO = 0xFFFF7F7F;
    private static final int FIELD_FROM = 0xFF103080;
    private static final int FIELD_TO = 0xFF60C0FF;
    private static final int ICF_FROM = 0xFF50005A;
    private static final int ICF_TO = 0xFFFF00AF;
    private static final int SOLUTION_FROM = 0xFF404040;
    private static final int SOLUTION_TO = 0xFFD0D0D0;

    private GeoNtmBlockInfo() {}

    public static void init() {
        BlockInfo.redirect((level, pos) ->
                MultiblockSurface.isFoldedCell(level.getBlockState(pos)) ? MultiblockSurface.coreOfCell(level, pos) : null);

        BlockInfo.gauges(IEnergyHandlerMK2.class, (machine, out) -> {
            if (machine.getMaxPower() > 0) {
                out.accept(Gauge.energy(machine.getPower(), machine.getMaxPower(), "HE", HE_FROM, HE_TO));
            }
        });
        BlockInfo.tanks(IFluidUserMK2.class, (machine, out) -> {
            for (FluidTankNTM tank : machine.getAllTanks()) {
                if (tank == null || tank.getMaxFill() <= 0) continue;
                FluidType type = tank.getTankType();
                out.accept(new Tank(type == Fluids.NONE ? null : type.getFluid(), tank.getFill(), tank.getMaxFill()));
            }
        });

        BlockInfo.gauges(TileEntityFireboxBase.class, (te, out) -> heat(out, te.heatEnergy, te.getMaxHeat()));
        BlockInfo.gauges(TileEntityHeaterOilburner.class,
                (te, out) -> heat(out, te.heatEnergy, te.maxHeatEnergy));
        BlockInfo.gauges(TileEntityHeaterElectric.class, (te, out) -> heat(out, te.heatEnergy, 0));
        BlockInfo.gauges(TileEntityHeaterHeatex.class, (te, out) -> heat(out, te.heatEnergy, 0));
        BlockInfo.gauges(TileEntityHeatBoiler.class, (te, out) -> heat(out, te.heat, TileEntityHeatBoiler.maxHeat));
        BlockInfo.gauges(TileEntityHeatBoilerIndustrial.class,
                (te, out) -> heat(out, te.heat, TileEntityHeatBoilerIndustrial.maxHeat));
        BlockInfo.gauges(TileEntityFurnaceSteel.class, (te, out) -> heat(out, te.heat, TileEntityFurnaceSteel.maxHeat));
        BlockInfo.gauges(TileEntityFurnaceCombination.class,
                (te, out) -> heat(out, te.heat, TileEntityFurnaceCombination.maxHeat));

        BlockInfo.gauges(TileEntityFusionKlystron.class,
                (te, out) -> out.accept(Gauge.of("", te.output, te.outputTarget, "KyU", KYU_FROM, KYU_TO)));
        BlockInfo.gauges(TileEntityFusionTorus.class, (te, out) -> {
            long ignition = te.fusionModule.getRecipe() instanceof FusionRecipe recipe ? recipe.ignitionTemp : 0;
            long output = te.fusionModule.getRecipe() instanceof FusionRecipe recipe ? recipe.outputTemp : 0;
            out.accept(Gauge.of("", te.klystronEnergySync(), ignition, "KyU", KYU_FROM, KYU_TO));
            out.accept(Gauge.of("", te.plasmaEnergy, output, "TU", TU_FROM, TU_TO));
        });
        BlockInfo.gauges(TileEntityFusionPlasmaForge.class, (te, out) -> {
            long ignition = te.plasmaModule.getRecipe() instanceof PlasmaForgeRecipe recipe ? recipe.ignitionTemp : 0;
            out.accept(Gauge.of("", te.plasmaEnergySync, ignition, "TU", TU_FROM, TU_TO));
        });
        BlockInfo.gauges(TileEntityFusionBoiler.class,
                (te, out) -> out.accept(Gauge.of("", te.plasmaEnergySync(), 0, "TU", TU_FROM, TU_TO)));
        BlockInfo.gauges(TileEntityFusionMHDT.class, (te, out) -> out.accept(
                Gauge.of("", te.plasmaEnergySync(), TileEntityFusionMHDT.minimumPlasma, "TU/t", TU_FROM, TU_TO)));

        // 1.7's ICF GUI shows the laser as OFFLINE while no laser is linked; the overlay leaves it out
        BlockInfo.gauges(TileEntityICF.class, (te, out) -> {
            if (te.maxLaserSync() > 0) {
                out.accept(Gauge.of("", te.laserSync(), te.maxLaserSync(), "TU/t", TU_FROM, TU_TO));
            }
            out.accept(Gauge.of("", te.heat, TileEntityICF.maxHeat, "TU", ICF_FROM, ICF_TO));
        });

        // the DFC GUIs' labels, which 1.7 wrote in English
        BlockInfo.gauges(TileEntityCoreEmitter.class,
                (te, out) -> out.accept(Gauge.of("Output", te.prev, 0, "Spk", SPK_FROM, SPK_TO)));
        BlockInfo.gauges(TileEntityCoreReceiver.class,
                (te, out) -> out.accept(Gauge.of("Input", te.prevJoules, 0, "Spk", SPK_FROM, SPK_TO)));
        BlockInfo.gauges(TileEntityCore.class, (te, out) -> {
            out.accept(Gauge.of("Restriction Field", te.field, 100, "%", FIELD_FROM, FIELD_TO));
            out.accept(Gauge.of("Heat Saturation", te.prevHeat, 100, "%", TU_FROM, TU_TO));
        });

        // the solution pool: a fluid drawn from the tank shows as a tank, a dissolved item by its name
        BlockInfo.tanks(TileEntitySILEX.class, (te, out) -> {
            ItemStack pool = te.current;
            if (pool != null && pool.getItem() instanceof ItemFluidIcon) {
                out.accept(new Tank(ItemFluidIcon.getFluid(pool).getFluid(), te.currentFill, TileEntitySILEX.maxFill));
            }
        });
        BlockInfo.gauges(TileEntitySILEX.class, (te, out) -> {
            ItemStack pool = te.current;
            if (pool == null || pool.getItem() instanceof ItemFluidIcon) return;
            out.accept(Gauge.of(pool.getItem().getDescriptionId(), te.currentFill, TileEntitySILEX.maxFill, "mB",
                    SOLUTION_FROM, SOLUTION_TO));
        });

        BlockInfo.gauges(TileEntityCrucible.class, (te, out) -> {
            heat(out, te.heat, TileEntityCrucible.maxHeat);
            materials(out, te.recipeStack, TileEntityCrucible.recipeZCapacity);
            materials(out, te.wasteStack, TileEntityCrucible.wasteZCapacity);
        });
        BlockInfo.gauges(TileEntityFoundryBase.class, (te, out) -> {
            if (te.type != null && te.amount > 0) out.accept(material(te.type.getDisplayName(), te.amount,
                    te.getCapacity(), te.type.moltenColor));
        });
    }

    private static void heat(Consumer<Gauge> out, int heat, int max) {
        out.accept(Gauge.of("", heat, max, "TU", TU_FROM, TU_TO));
    }

    private static void materials(Consumer<Gauge> out, List<Mats.MaterialStack> stacks, int capacity) {
        for (Mats.MaterialStack stack : stacks) {
            out.accept(material(stack.material.getDisplayName(), stack.amount, capacity, stack.material.moltenColor));
        }
    }

    /** Amounts in mB, quanta × 2 as 1.7's {@code Mats.formatAmount} prints them. */
    private static Gauge material(String name, int quanta, int capacity, int color) {
        int opaque = 0xFF000000 | color;
        return Gauge.of(name, quanta * 2L, capacity * 2L, "mB", opaque & 0xFF7F7F7F, opaque);
    }
}
