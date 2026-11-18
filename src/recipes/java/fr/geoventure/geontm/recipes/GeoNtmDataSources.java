package fr.geoventure.geontm.recipes;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.recipes.tables.*;
import fr.geoventure.geontm.recipes.data.D1Sources;
import fr.geoventure.geontm.recipes.data.D2Sources;
import fr.geoventure.geontm.recipes.data.D3Sources;
import fr.geoventure.geontm.recipes.data.HazardRegistrySource;
import fr.geoventure.geontm.util.CategoryTagSource;
import fr.geoventure.geocore.world.DataSource;
import fr.geoventure.geocore.world.RecipeSource;
import fr.geoventure.geocore.world.DataSources;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HBM's machine recipe tables: one {@link RecipeSource} per table, each a transliteration of the 1.7 table's
 * {@code registerDefaults()}, generated into {@code data/geontm/recipe/<table>/} at build time ({@code :26.1-neoforge:runGenerateData},
 * see {@code build.neoforge.gradle.kts}). A new table adds its line.
 */
public final class GeoNtmDataSources implements DataSources {

    @Override
    public String namespace() {
        return "geontm";
    }

    @Override
    public List<DataSource<?>> sources() {
        List<DataSource<?>> sources = new ArrayList<>(List.of(
                // GenericRecipe tables
                new FusionTable(),
                new ExposureChamberTable(),
                new FluidBreederTable(),
                new OutgasserTable(),
                new BlastFurnaceTable(),
                new DiFurnaceTable(),
                new MixerTable(),
                new PseudoFluidTable(),
                new CrucibleTable(),
                new PlasmaForgeTable(),
                new PrecAssTable(),
                new SpaceAssemblerTable(),
                new PurexTable(),
                new ChemicalPlantTable(),
                new AssemblyMachineTable(),
                new RockMillTable(),
                new SuperComputerTable(),
                // item-processing tables
                new ToolConversionTable(),
                new RotaryFurnaceTable(),
                new CyclotronTable(),
                new ElectrolyserMetalTable(),
                new ArcFurnaceTable(),
                new MaterialDistributionTable(),
                new CentrifugeTable(),
                new CrystallizerTable(),
                // shredder, SILEX and the fluid tables
                new CompressorTable(),
                new GasCentrifugeTable(),
                new CrackingTable(),
                new ReformingTable(),
                new HydrotreatingTable(),
                new FractionTable(),
                new RadiolysisTable(),
                new RefineryTable(),
                new VacuumDistillTable(),
                new ElectrolyserFluidTable(),
                new CokerTable(),
                new LiquefactionTable(),
                new PyrolysisTable(),
                new SolidificationTable(),
                new StorageDrumTable(),
                new SILEXTable(),
                new ShredderTable(),
                // item tables
                new SawmillTable(),
                new MagicTable(),
                new ParticleAcceleratorTable(),
                new PedestalTable(),
                new AnnihilatorTable(),
                new SolderingTable(),
                new BreederTable(),
                new LemegetonTable(),
                new ArcWelderTable(),
                new PressTable(),
                new CombinationTable(),
                new AmmoPressTable(),
                new AnvilSmithingTable(),
                new AnvilConstructionTable()));
        // data folders (fluid properties, data maps, item pools, removal lists, ...)
        sources.addAll(D3Sources.sources());
        sources.addAll(D1Sources.sources());
        sources.addAll(D2Sources.sources());
        // category and convention tags (GitHub 132), computed from the running game
        sources.add(new CategoryTagSource(Constants.MOD_ID, Map.of(), new HazardRegistrySource.Items()));
        return sources;
    }
}
