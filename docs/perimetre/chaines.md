# Chaînes de production de GeoNTM

Fichier généré par `tools/verifier_chaines.py`.

## Machines par dossier de recettes

| Dossier | Machines visibles |
|---|---|
| anvil_construction | anvil_iron, anvil_lead, anvil_steel, anvil_desh, anvil_ferrouranium, anvil_saturnite, anvil_bismuth_bronze, anvil_arsenic_bronze, anvil_schrabidate, anvil_dnt, anvil_osmiridium, anvil_murky |
| anvil_smithing | anvil_iron, anvil_lead, anvil_steel, anvil_desh |
| arc_furnace | machine_arc_furnace |
| arc_welder | machine_arc_welder |
| assembly_machine | machine_assembly_machine, machine_assembly_factory |
| blast_furnace | **aucune** |
| breeder | **aucune** |
| centrifuge | machine_centrifuge |
| chemical_plant | machine_chemical_plant, machine_chemical_factory |
| coker | machine_coker |
| combination | **aucune** |
| compressor | machine_compressor, machine_compressor_compact |
| cracking | machine_catalytic_cracker |
| crucible | machine_crucible |
| crystallizer | machine_crystallizer |
| cyclotron | machine_cyclotron |
| difurnace | **aucune** |
| electrolyser_fluid | machine_electrolyser |
| electrolyser_metal | machine_electrolyser |
| exposure_chamber | machine_exposure_chamber |
| fluid_breeder | **aucune** |
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
| rock_mill | **aucune** |
| rotary_furnace | machine_rotary_furnace |
| sawmill | **aucune** |
| shredder | machine_shredder |
| silex | machine_silex |
| soldering | machine_soldering_station |
| solidification | machine_solidifier |
| space_assembler | **aucune** |
| storage_drum | machine_storage_drum |
| supercomputer | machine_supercomputer |
| vacuum_distill | machine_vacuum_distill |

Dossiers sans correspondance connue : material_distribution, pseudo_fluid

## Fluides demandés sans producteur actif

- `geontm:amat` : demandé par purex/flashgold, purex/flashlead
- `geontm:bauxite_solution` : demandé par fraction/bauxite_solution
- `geontm:blood` : demandé par mixer/pheromone_m.0, mixer/salient.0
- `geontm:coalcreosote` : demandé par chemical_plant/coallube, coker/coalcreosote, fraction/coalcreosote, solidification/coalcreosote
- `geontm:gas` : demandé par chemical_plant/co2, chemical_plant/coltanpain, chemical_plant/cordite, chemical_plant/deicer
- `geontm:pheromone` : demandé par mixer/pheromone_m.0
- `geontm:stellar_flux` : demandé par fusion/stellar
- `geontm:thorium_salt_depleted` : demandé par purex/thoriumsalt
- `geontm:vitriol` : demandé par coker/vitriol, electrolyser_fluid/vitriol
- `geontm:xpjuice` : demandé par chemical_plant/epearl, mixer/enderjuice.0

## Pièces de missiles introuvables

- `geontm:missile_doomsday_rusted` : pour anvil_construction/missile_doomsday
- `geontm:warhead_volcano` : pour arc_welder/missile_volcano
