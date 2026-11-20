# Périmètre de GeoNTM

> Généré par `tools/perimetre.py`. **Proposition à valider** : aucun contenu n'est encore retiré du mod.

NTM-Continuum 1.0.6 contient **1266 blocs** et **3463 objets**. Le détail ligne par ligne est dans [contenu.csv](perimetre/contenu.csv).

## Bilan

| Décision | Nombre | Sens |
|---|---:|---|
| GARDER | 375 | repris dans GeoNTM |
| SIMPLIFIER | 1035 | repris, mais regroupé ou réduit |
| FUSIONNER | 79 | remplacé par un objet Geoventure |
| SI_BESOIN | 1362 | gardé seulement si une recette retenue en dépend |
| RETIRER | 1878 | supprimé |
| A_DECIDER | 0 | arbitrage du propriétaire |

## Ce qui reste, par thème

| Thème | Décision | Nombre |
|---|---|---:|
| drones | GARDER | 10 |
| dépendances | SI_BESOIN | 812 |
| machines | FUSIONNER | 23 |
| machines | GARDER | 5 |
| machines : combustible | GARDER | 8 |
| machines : production | GARDER | 25 |
| machines : réacteurs | GARDER | 3 |
| machines : satellites | GARDER | 5 |
| machines : vapeur et énergie | GARDER | 14 |
| missiles | GARDER | 72 |
| missiles | SIMPLIFIER | 122 |
| radioactivité | GARDER | 93 |
| ressources | FUSIONNER | 56 |
| ressources : ajouts | GARDER | 20 |
| ressources : espace | GARDER | 11 |
| ressources : intermédiaires | SI_BESOIN | 550 |
| ressources : nucléaire | GARDER | 47 |
| réacteurs | SIMPLIFIER | 717 |
| satellites | GARDER | 54 |
| schémas | GARDER | 8 |
| électronique | SIMPLIFIER | 196 |

## Ressources proposées

Sur Terre, seules les ressources Geoventure existent, plus ces ajouts :

- **graphite** : modérateur des réacteurs
- **beryllium** : réflecteur à neutrons
- **boron** : barres de contrôle
- **zirconium** : gaines de combustible
- **thorium** : combustible alternatif

Ressources rares : **une ressource unique par planète**, absente de la Terre (aucun minerai généré, aucune recette de fabrication, aucun butin ni
recyclage). Chacune est exigée par des fabrications de fin de progression ; le détail et les recettes sont dans [obtention.md](perimetre/obtention.md).

| Planète | Ressource | Origine | Exigée par |
|---|---|---|---|
| Lune | desh | Ad Astra (`ad_astra:desh_ingot`) | fusée T2, silex, purex, champ de force, fracker, grand radar |
| Mars | ostrum | Ad Astra (`ad_astra:ostrum_ingot`) ; la saturnite de NTM y est fusionnée | fusée T3, satellite scanneur à rayons, propulseur de missile 15 balefire, barre Watz, dissipateur PWR |
| Vénus | calorite | Ad Astra (`ad_astra:calorite_ingot`) | fusée T4, jet suit, couverture de fusion, forge à plasma de fusion, propulseur nucléaire |
| Mercure | schrabidium | GeoNTM (`geontm:ore_schrabidium`, généré sur Mercure) | tungstène magnétisé, batterie au schrabidium, bombe flottante |
| Glacio | starmetal | GeoNTM (`geontm:ore_glacio_starmetal`, généré sur Glacio) | plaque d'armure lunaire, radgen, jeu de pistons en starmetal |

La saturnite (`ingot_saturnite`, `plate_saturnite`, `block_saturnite`, `ore_mars_saturnite`) garde ses identifiants enregistrés mais n'est plus générée ni
fabriquée : les recettes demandent l'ostrum par les tags `c:ingots/ostrum`, `c:plates/ostrum` et `c:storage_blocks/ostrum`, qui acceptent aussi les stocks
de saturnite, et des recettes sans disposition (ingot, plaque, bloc) convertissent ces stocks en objets Ad Astra. Les fusées T2, T3 et T4 et le jet suit sont des recettes d'Ad Astra
(desh, ostrum, calorite d'origine) : GeoNTM ne les modifie pas.

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
