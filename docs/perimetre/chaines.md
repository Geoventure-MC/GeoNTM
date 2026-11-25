# Chaînes de production de GeoNTM

Fichier généré par `tools/verifier_chaines.py`.

## Machines par dossier de recettes

| Dossier | Machines visibles |
|---|---|
| anvil_construction | anvil_iron, anvil_lead, anvil_steel, anvil_desh, anvil_ferrouranium, anvil_saturnite, anvil_bismuth_bronze, anvil_arsenic_bronze, anvil_schrabidate, anvil_dnt, anvil_osmiridium |
| anvil_smithing | anvil_iron, anvil_lead, anvil_steel, anvil_desh |
| arc_furnace | machine_arc_furnace |
| arc_welder | machine_arc_welder |
| assembly_machine | machine_assembly_machine, machine_assembly_factory |
| blast_furnace | machine_blast_furnace |
| breeder | **aucune** |
| centrifuge | machine_centrifuge |
| chemical_plant | machine_chemical_plant, machine_chemical_factory |
| coker | machine_coker |
| combination | machine_furnace_combination |
| compressor | machine_compressor, machine_compressor_compact |
| cracking | machine_catalytic_cracker |
| crucible | machine_crucible |
| crystallizer | machine_crystallizer |
| cyclotron | machine_cyclotron |
| difurnace | **aucune** |
| electrolyser_fluid | machine_electrolyser |
| electrolyser_metal | machine_electrolyser |
| exposure_chamber | machine_exposure_chamber |
| fluid_breeder | fusion_breeder |
| fraction | machine_fraction_tower |
| fusion | fusion_torus, fusion_core |
| gas_centrifuge | machine_gascent |
| hydrotreating | machine_hydrotreater |
| liquefaction | machine_liquefactor |
| mixer | machine_mixer |
| outgasser | rbmk_outgasser |
| precision_assembler | machine_precass |
| press | machine_press, machine_epress |
| purex | machine_purex |
| pyrolysis | machine_pyrooven |
| radiolysis | machine_radiolysis |
| refinery | machine_refinery |
| reforming | machine_catalytic_reformer |
| rock_mill | machine_rockmill |
| rotary_furnace | machine_rotary_furnace |
| sawmill | **aucune** |
| shredder | machine_shredder |
| silex | machine_silex |
| soldering | machine_soldering_station |
| solidification | machine_solidifier |
| space_assembler | satellite_science_assembler |
| storage_drum | machine_storage_drum |
| supercomputer | machine_supercomputer |
| vacuum_distill | machine_vacuum_distill |

Tables laissées sans machine visible, par choix : `breeder` (machine_reactor), `difurnace` (machine_difurnace_off) et `sawmill` (machine_sawmill). Aucun objet visible n'en dépend : `tools/verifier_obtention.py` retrouve chaque objet visible sans elles.

Dossiers sans correspondance connue : material_distribution, pseudo_fluid, tool_conversion

## Fluides demandés sans producteur actif

- aucun

## Pièces de missiles introuvables

- aucune
