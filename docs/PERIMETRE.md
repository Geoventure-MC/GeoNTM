# Périmètre de GeoNTM

> Généré par `tools/perimetre.py`. **Proposition à valider** : aucun contenu n'est encore retiré du mod.

NTM-Continuum 1.0.6 contient **1266 blocs** et **3463 objets**. Le détail ligne par ligne est dans [contenu.csv](perimetre/contenu.csv).

## Bilan

| Décision | Nombre | Sens |
|---|---:|---|
| GARDER | 345 | repris dans GeoNTM |
| SIMPLIFIER | 1035 | repris, mais regroupé ou réduit |
| FUSIONNER | 76 | remplacé par un objet Geoventure |
| SI_BESOIN | 1362 | gardé seulement si une recette retenue en dépend |
| RETIRER | 1869 | supprimé |
| A_DECIDER | 42 | arbitrage du propriétaire |

## Ce qui reste, par thème

| Thème | Décision | Nombre |
|---|---|---:|
| drones | A_DECIDER | 12 |
| dépendances | SI_BESOIN | 812 |
| machines | A_DECIDER | 7 |
| machines | FUSIONNER | 23 |
| machines : combustible | GARDER | 8 |
| machines : production | GARDER | 21 |
| machines : réacteurs | GARDER | 3 |
| machines : satellites | GARDER | 5 |
| machines : vapeur et énergie | GARDER | 14 |
| missiles | A_DECIDER | 23 |
| missiles | GARDER | 59 |
| missiles | SIMPLIFIER | 122 |
| radioactivité | GARDER | 93 |
| ressources | FUSIONNER | 53 |
| ressources : ajouts | GARDER | 20 |
| ressources : espace | GARDER | 14 |
| ressources : intermédiaires | SI_BESOIN | 550 |
| ressources : nucléaire | GARDER | 47 |
| réacteurs | SIMPLIFIER | 717 |
| satellites | GARDER | 53 |
| schémas | GARDER | 8 |
| électronique | SIMPLIFIER | 196 |

## Ressources proposées

Sur Terre, seules les ressources Geoventure existent, plus ces ajouts :

- **graphite** : modérateur des réacteurs
- **beryllium** : réflecteur à neutrons
- **boron** : barres de contrôle
- **zirconium** : gaines de combustible
- **thorium** : combustible alternatif

Ressources rares, sans minerai sur Terre : il faut une fusée pour les récupérer.

- **desh** : Lune
- **saturnite** : Mars
- **starmetal** : Glacio
- **schrabidium** : Mercure

## Machines gardées

- **satellites** : `satlink`, `satlinker`, `radar`, `radar_large`, `missile_assembly`
- **réacteurs** : `zirnox`, `radgen`, `rtg_grey`
- **combustible** : `gascent`, `silex`, `fel`, `purex`, `exposure_chamber`, `cyclotron`, `waste_drum`, `storage_drum`
- **vapeur et énergie** : `industrial_boiler`, `steam_engine`, `stirling`, `stirling_steel`, `industrial_turbine`, `chungus`, `tower_large`, `tower_small`, `condenser`, `condenser_powered`, `turbofan`, `turbine_gas`, `combustion_engine`, `solar_boiler`
- **production** : `assembly_machine`, `assembly_factory`, `chemical_plant`, `chemical_factory`, `arc_furnace`, `arc_welder`, `electrolyser`, `crystallizer`, `vacuum_distill`, `catalytic_cracker`, `catalytic_reformer`, `hydrotreater`, `coker`, `liquefactor`, `solidifier`, `compressor`, `compressor_compact`, `rotary_furnace`, `crucible`, `strand_caster`, `soldering_station`

## Machines remplacées par les nôtres

| NTM | Geoventure |
|---|---|
| `refinery` | `refinery` |
| `pumpjack` | `oil_pump` |
| `fracking_tower` | `oil_derrick` |
| `fluidtank` | `fluid_tank` |
| `pump_electric` | `water_pump` |
| `pump_steam` | `water_pump` |
| `transformer` | `transformer` |
| `transformer_20` | `transformer` |
| `battery` | `battery` |
| `lithium_battery` | `battery_t2` |
| `electric_furnace_off` | `electric_furnace` |
| `shredder` | `crusher` |
| `press` | `metal_press` |
| `epress` | `metal_press` |
| `centrifuge` | `centrifuge` |
| `mixer` | `industrial_mixer` |
| `autocrafter` | `auto_assembler` |
| `diesel` | `generator` |
| `bigasstank` | `liquid_depot` |
| `conveyor_press` | `conveyor_press` |
| `fraction_tower` | `modular_refinery` |
| `rockmill` | `flour_mill` |
| `blast_furnace` | `mega_foundry` |
