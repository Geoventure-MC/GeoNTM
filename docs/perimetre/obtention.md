# Obtention en survie

Fichier généré par `tools/verifier_obtention.py` à partir du jar compilé : point fixe sur les recettes actives (conditions évaluées avec les réglages par défaut : 528, mode coûteux et LBSM désactivés), en exigeant que la machine de chaque recette soit elle-même obtenable. Les objets, tags et fluides de GeoMods comptent comme fournis.

- Objets et fluides obtenables : **2347** (recettes : 1821, butin : 154, blocs de monde : 110, drops : 21)
- Objets visibles : **1115** ; visibles non obtenables : **324**
- Hypothèse : 121 recettes actives (missiles modulaires, espace, ogives, réacteurs...) ne se débloquent qu'avec un schéma (`blueprint_pools`, mécanisme NTM inchangé) trouvé dans les coffres ; l'outil les compte comme faisables. Les réacteurs, le combustible usé et les déchets sont supposés produits par les machines Java correspondantes. Les règles de broyage construites en Java (`ShredderRecipes` : bûches et planches en sciure, minerais, lingots et blocs en poudre) sont modélisées : la sciure du cordite et le coltan broyé de `coltanpain` en viennent.

| Catégorie | Nombre |
|---|---:|
| volontaire | 8 |
| cosmétique | 92 |
| butin | 18 |
| non touché | 47 |
| produit en jeu | 58 |
| hérité | 30 |
| armes retirées | 79 |

## volontaire

| Objet | Justification |
|---|---|
| `drone_patrol_chunkloading` | arme extrême ou objet écarté, sans recette par décision du propriétaire |
| `drone_patrol_express_chunkloading` | arme extrême ou objet écarté, sans recette par décision du propriétaire |
| `missile_bhole` | arme extrême ou objet écarté, sans recette par décision du propriétaire |
| `missile_doomsday` | arme extrême ou objet écarté, sans recette par décision du propriétaire |
| `missile_nuclear_cluster` | arme extrême ou objet écarté, sans recette par décision du propriétaire |
| `missile_schrabidium` | arme extrême ou objet écarté, sans recette par décision du propriétaire |
| `missile_taint` | arme extrême ou objet écarté, sans recette par décision du propriétaire |
| `missile_volcano` | arme extrême ou objet écarté, sans recette par décision du propriétaire |

## cosmétique

| Objet | Justification |
|---|---|
| `circuit_star` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_component_card` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_component_chipset` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_component_cpu` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_component_ram` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_piece_board_blank` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_piece_board_converter` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_piece_board_transistor` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_piece_bridge_bios` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_piece_bridge_bus` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_piece_bridge_chipset` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_piece_bridge_cmos` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_piece_bridge_io` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_piece_bridge_north` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_piece_bridge_south` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_piece_card_board` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_piece_card_processor` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_piece_cpu_cache` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_piece_cpu_clock` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_piece_cpu_ext` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_piece_cpu_logic` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_piece_cpu_register` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_piece_cpu_socket` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_piece_mem_16k_a` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_piece_mem_16k_b` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_piece_mem_16k_c` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_piece_mem_16k_d` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `circuit_star_piece_mem_socket` | objet de collection sans usage (« Currently unused, for it lacks an operating system ») : aucune recette ne l'exige ; ses pièces viennent des scrap_plastic_* des bobbleheads, décor sans recette |
| `missile_obj_context_base` | objet technique ou de test |
| `missile_obj_context_legacy` | objet technique ou de test |
| `missile_skin_camo` | peinture de missile, sans recette dans NTM |
| `missile_skin_desert` | peinture de missile, sans recette dans NTM |
| `missile_skin_flames` | peinture de missile, sans recette dans NTM |
| `missile_skin_manly_pink` | peinture de missile, sans recette dans NTM |
| `missile_skin_metal` | peinture de missile, sans recette dans NTM |
| `missile_skin_orange_insulation` | peinture de missile, sans recette dans NTM |
| `missile_skin_sleek` | peinture de missile, sans recette dans NTM |
| `missile_skin_soviet_glory` | peinture de missile, sans recette dans NTM |
| `missile_skin_soviet_stank` | peinture de missile, sans recette dans NTM |
| `missile_soyuz_1` | identifiants hérités (LegacyMetaIds) : seul missile_soyuz_0 se fabrique |
| `missile_soyuz_2` | identifiants hérités (LegacyMetaIds) : seul missile_soyuz_0 se fabrique |
| `missile_test` | objet technique ou de test |
| `mp_fuselage_10_kerosene_camo` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_kerosene_desert` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_kerosene_flames` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_kerosene_sky` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_kerosene_sleek` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_kerosene_taint` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_long_kerosene_camo` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_long_kerosene_dash` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_long_kerosene_desert` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_long_kerosene_flames` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_long_kerosene_sky` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_long_kerosene_sleek` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_long_kerosene_taint` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_long_kerosene_vap` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_long_solid_bullet` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_long_solid_flames` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_long_solid_silvermoonlight` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_long_solid_sleek` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_long_solid_soviet_glory` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_solid_battery` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_solid_cathedral` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_solid_duracell` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_solid_flames` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_solid_moonlit` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_solid_sleek` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_10_solid_soviet_glory` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_15_20_kerosene_magnusson` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_15_hydrogen_cathedral` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_15_kerosene_blackjack` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_15_kerosene_camo` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_15_kerosene_decorated` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_15_kerosene_desert` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_15_kerosene_lambda` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_15_kerosene_minuteman` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_15_kerosene_pip` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_15_kerosene_polite` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_15_kerosene_sky` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_15_kerosene_steampunk` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_15_kerosene_taint` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_15_kerosene_yuck` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_15_solid_faust` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_15_solid_mimi` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_15_solid_panorama` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_15_solid_roses` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_15_solid_silvermoonlight` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_15_solid_snowy` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_15_solid_soviet_glory` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_fuselage_15_solid_soviet_stank` | variante de peinture de fuselage, sans recette dans NTM |
| `mp_warhead_15_nuclear_mimi` | variante de peinture d'ogive, sans recette dans NTM |
| `mp_warhead_15_nuclear_shark` | variante de peinture d'ogive, sans recette dans NTM |

## butin

| Objet | Justification |
|---|---|
| `blueprint_plate` | schémas trouvés dans les coffres (feature blueprint_chest) |
| `blueprints` | schémas trouvés dans les coffres (feature blueprint_chest) |
| `blueprints_528` | schémas trouvés dans les coffres (feature blueprint_chest) |
| `blueprints_discover` | schémas trouvés dans les coffres (feature blueprint_chest) |
| `blueprints_secret` | schémas trouvés dans les coffres (feature blueprint_chest) |
| `cap_rad` | objet de butin ou de curiosité sans recette dans NTM |
| `gem_rad` | objet de butin ou de curiosité sans recette dans NTM |
| `hazmat_grey_kit` | objet de butin ou de curiosité sans recette dans NTM |
| `hazmat_red_kit` | objet de butin ou de curiosité sans recette dans NTM |
| `iv_xp` | objet de butin ou de curiosité sans recette dans NTM |
| `launch_pad_rusted` | ancienne base de lancement posée par la génération du monde |
| `med_schizophrenia` | objet de butin ou de curiosité sans recette dans NTM |
| `mp_s_20` | pièce de missile sur mesure sans recette dans NTM (créatif ou butin) |
| `mp_stability_15_soyuz` | pièce de missile sur mesure sans recette dans NTM (créatif ou butin) |
| `mp_warhead_15_boxcar` | pièce de missile sur mesure sans recette dans NTM (créatif ou butin) |
| `mp_warhead_15_turbine` | pièce de missile sur mesure sans recette dans NTM (créatif ou butin) |
| `syringe_mkunicorn` | objet de butin ou de curiosité sans recette dans NTM |
| `syringe_taint` | objet de butin ou de curiosité sans recette dans NTM |

## non touché

| Objet | Justification |
|---|---|
| `anvil_murky` | enclume secrète (œuf de Pâques) : exige `undefined`, issu des pastilles DRX (balefire + digamma), combustibles extrêmes laissés tels quels |
| `billet_balefire_gold` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `billet_flashlead` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `billet_unobtainium` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `billet_yharonite` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `fusion_component_bscco_welded` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `fusion_core_block` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `fusion_core_infinite` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `fusion_hatch` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `fusion_heater` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `fusion_klystron_creative` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `fusion_torus` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `icf` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `icf_block` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `icf_component` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `icf_component_structure` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `icf_component_structure_bolted` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `icf_component_vessel` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `icf_component_vessel_welded` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `icf_controller` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `icf_laser_component_capacitor` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `icf_laser_component_casing` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `icf_laser_component_cell` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `icf_laser_component_emitter` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `icf_laser_component_port` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `icf_laser_component_turbo` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `icf_pellet` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `icf_pellet_depleted` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `mp_fuselage_10_xenon_bhole` | fuselage d'arme extrême (trou noir, balefire) |
| `mp_fuselage_15_balefire` | fuselage d'arme extrême (trou noir, balefire) |
| `pellet_rtg_balefire` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `rbmk_control_reasim` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `rbmk_control_reasim_auto` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `rbmk_fuel_balefire` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `rbmk_fuel_balefire_gold` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `rbmk_fuel_drx` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `rbmk_fuel_flashlead` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `rbmk_fuel_test` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `rbmk_pellet_balefire` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `rbmk_pellet_balefire_gold` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `rbmk_pellet_drx` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `rbmk_pellet_flashlead` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `watz_casing_tooled` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `watz_pellet_depleted_nqd` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `watz_pellet_depleted_nqr` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `watz_pellet_nqd` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |
| `watz_pellet_nqr` | fusion, ICF, Watz et combustibles extrêmes laissés tels quels (consigne du propriétaire) |

## produit en jeu

| Objet | Justification |
|---|---|
| `capacitor_bus` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `corium_block` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `fluid_identifier` | identifiant typé réglé à partir de fluid_identifier_multi (craftable) : à vérifier en jeu |
| `gas_full` | réservoir de gaz rempli à la machine (fluid_item) |
| `gas_radon` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `gas_radon_dense` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `gas_radon_tomb` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `launch_table` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `missile_custom` | sortie de la machine d'assemblage de missiles (comportement Java) |
| `missile_kit` | sortie de la machine d'assemblage de missiles (comportement Java) |
| `pellet_rtg_depleted_bismuth` | pastille RTG usée, produite par le générateur |
| `pellet_rtg_depleted_mercury` | pastille RTG usée, produite par le générateur |
| `pellet_rtg_depleted_neptunium` | pastille RTG usée, produite par le générateur |
| `pellet_rtg_depleted_nickel` | pastille RTG usée, produite par le générateur |
| `pellet_rtg_depleted_zirconium` | pastille RTG usée, produite par le générateur |
| `pile_block` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `pile_device` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `pwr_block` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `pwr_printer` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `rad_lava_block` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `radsensor` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `rbmk_debris` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `rbmk_debris_burning` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `rbmk_debris_digamma` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `rbmk_debris_radiating` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `sellafield_0` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `sellafield_1` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `sellafield_2` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `sellafield_3` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `sellafield_4` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `sellafield_5` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `sellafield_bedrock` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `sellafield_slaked` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `soyuz_capsule` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `soyuz_launcher` | bloc formé par une structure, un réacteur ou le monde (pas de recette d'objet) |
| `waste_earth` | déchet produit par les réacteurs ou les retombées |
| `waste_leaves` | déchet produit par les réacteurs ou les retombées |
| `waste_log` | déchet produit par les réacteurs ou les retombées |
| `waste_mox` | déchet produit par les réacteurs ou les retombées |
| `waste_mycelium` | déchet produit par les réacteurs ou les retombées |
| `waste_natural_uranium` | déchet produit par les réacteurs ou les retombées |
| `waste_planks` | déchet produit par les réacteurs ou les retombées |
| `waste_plate_mox` | déchet produit par les réacteurs ou les retombées |
| `waste_plate_pu238be` | déchet produit par les réacteurs ou les retombées |
| `waste_plate_pu239` | déchet produit par les réacteurs ou les retombées |
| `waste_plate_ra226be` | déchet produit par les réacteurs ou les retombées |
| `waste_plate_sa326` | déchet produit par les réacteurs ou les retombées |
| `waste_plate_u233` | déchet produit par les réacteurs ou les retombées |
| `waste_plate_u235` | déchet produit par les réacteurs ou les retombées |
| `waste_plutonium` | déchet produit par les réacteurs ou les retombées |
| `waste_schrabidium` | déchet produit par les réacteurs ou les retombées |
| `waste_thorium` | déchet produit par les réacteurs ou les retombées |
| `waste_trinitite` | déchet produit par les réacteurs ou les retombées |
| `waste_trinitite_red` | déchet produit par les réacteurs ou les retombées |
| `waste_u233` | déchet produit par les réacteurs ou les retombées |
| `waste_u235` | déchet produit par les réacteurs ou les retombées |
| `waste_uranium` | déchet produit par les réacteurs ou les retombées |
| `waste_zfb_mox` | déchet produit par les réacteurs ou les retombées |

## hérité

| Objet | Justification |
|---|---|
| `capacitor_schrabidate` | bloc LEGACY remplacé par l'objet capacitor_* |
| `capacitor_tantalium` | bloc LEGACY remplacé par l'objet capacitor_* |
| `deco_sat_foeq` | ancien satellite ou décor 1.7, remplacé par les satellite_* (sans recette dans NTM) |
| `deco_sat_laser` | ancien satellite ou décor 1.7, remplacé par les satellite_* (sans recette dans NTM) |
| `deco_sat_mapper` | ancien satellite ou décor 1.7, remplacé par les satellite_* (sans recette dans NTM) |
| `deco_sat_radar` | ancien satellite ou décor 1.7, remplacé par les satellite_* (sans recette dans NTM) |
| `deco_sat_resonator` | ancien satellite ou décor 1.7, remplacé par les satellite_* (sans recette dans NTM) |
| `deco_sat_scanner` | ancien satellite ou décor 1.7, remplacé par les satellite_* (sans recette dans NTM) |
| `pile_rod_boron` | barreau de la pile de Chicago marqué LEGACY (classe dépréciée, sans onglet), remplacé par les pile_rod_mk2_* |
| `pile_rod_detector` | barreau de la pile de Chicago marqué LEGACY (classe dépréciée, sans onglet), remplacé par les pile_rod_mk2_* |
| `pile_rod_lithium` | barreau de la pile de Chicago marqué LEGACY (classe dépréciée, sans onglet), remplacé par les pile_rod_mk2_* |
| `pile_rod_plutonium` | barreau de la pile de Chicago marqué LEGACY (classe dépréciée, sans onglet), remplacé par les pile_rod_mk2_* |
| `pile_rod_pu239` | barreau de la pile de Chicago marqué LEGACY (classe dépréciée, sans onglet), remplacé par les pile_rod_mk2_* |
| `pile_rod_source` | barreau de la pile de Chicago marqué LEGACY (classe dépréciée, sans onglet), remplacé par les pile_rod_mk2_* |
| `pile_rod_uranium` | barreau de la pile de Chicago marqué LEGACY (classe dépréciée, sans onglet), remplacé par les pile_rod_mk2_* |
| `sat_base` | ancien satellite ou décor 1.7, remplacé par les satellite_* (sans recette dans NTM) |
| `sat_foeq` | ancien satellite ou décor 1.7, remplacé par les satellite_* (sans recette dans NTM) |
| `sat_gerald` | ancien satellite ou décor 1.7, remplacé par les satellite_* (sans recette dans NTM) |
| `sat_head_laser` | ancien satellite ou décor 1.7, remplacé par les satellite_* (sans recette dans NTM) |
| `sat_head_mapper` | ancien satellite ou décor 1.7, remplacé par les satellite_* (sans recette dans NTM) |
| `sat_head_radar` | ancien satellite ou décor 1.7, remplacé par les satellite_* (sans recette dans NTM) |
| `sat_head_resonator` | ancien satellite ou décor 1.7, remplacé par les satellite_* (sans recette dans NTM) |
| `sat_head_scanner` | ancien satellite ou décor 1.7, remplacé par les satellite_* (sans recette dans NTM) |
| `sat_laser` | ancien satellite ou décor 1.7, remplacé par les satellite_* (sans recette dans NTM) |
| `sat_lunar_miner` | ancien satellite ou décor 1.7, remplacé par les satellite_* (sans recette dans NTM) |
| `sat_mapper` | ancien satellite ou décor 1.7, remplacé par les satellite_* (sans recette dans NTM) |
| `sat_miner` | ancien satellite ou décor 1.7, remplacé par les satellite_* (sans recette dans NTM) |
| `sat_radar` | ancien satellite ou décor 1.7, remplacé par les satellite_* (sans recette dans NTM) |
| `sat_resonator` | ancien satellite ou décor 1.7, remplacé par les satellite_* (sans recette dans NTM) |
| `sat_scanner` | ancien satellite ou décor 1.7, remplacé par les satellite_* (sans recette dans NTM) |

## armes retirées

| Objet | Justification |
|---|---|
| `mechanism_launcher_1` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `mechanism_launcher_2` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `mechanism_revolver_1` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `mechanism_revolver_2` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `mechanism_rifle_1` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `mechanism_rifle_2` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `mechanism_special` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_barrel_heavy` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_barrel_heavy_cdalloy` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_barrel_heavy_desh` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_barrel_heavy_dura_steel` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_barrel_heavy_ferrouranium` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_barrel_heavy_gunmetal` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_barrel_heavy_saturnite` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_barrel_heavy_steel` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_barrel_heavy_tcalloy` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_barrel_heavy_weaponsteel` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_barrel_light` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_barrel_light_arsenic_bronze` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_barrel_light_bismuth_bronze` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_barrel_light_cdalloy` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_barrel_light_desh` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_barrel_light_dura_steel` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_barrel_light_gunmetal` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_barrel_light_saturnite` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_barrel_light_steel` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_barrel_light_tcalloy` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_barrel_light_weaponsteel` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_emitter` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_generic_hde` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_grip` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_grip_desh` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_grip_dura_steel` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_grip_gunmetal` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_grip_pvc` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_grip_saturnite` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_grip_steel` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_grip_weaponsteel` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_mechanism` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_mechanism_gunmetal` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_mechanism_saturnite` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_mechanism_weaponsteel` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_receiver_heavy` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_receiver_heavy_arsenic_bronze` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_receiver_heavy_bismuth_bronze` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_receiver_heavy_cdalloy` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_receiver_heavy_dura_steel` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_receiver_heavy_ferrouranium` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_receiver_heavy_gunmetal` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_receiver_heavy_saturnite` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_receiver_heavy_tcalloy` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_receiver_heavy_weaponsteel` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_receiver_light` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_receiver_light_arsenic_bronze` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_receiver_light_bismuth_bronze` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_receiver_light_cdalloy` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_receiver_light_desh` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_receiver_light_dura_steel` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_receiver_light_gunmetal` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_receiver_light_saturnite` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_receiver_light_steel` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_receiver_light_tcalloy` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_receiver_light_weaponsteel` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_stock` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_stock_desh` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_stock_gunmetal` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_stock_pvc` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_stock_saturnite` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `part_stock_weaponsteel` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `parts_legendary_tier1` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `parts_legendary_tier2` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `parts_legendary_tier3` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `pellet_canister` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `pellet_chlorophyte` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `pellet_claws` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `pellet_coal` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `pellet_coolant` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `pellet_flechette` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |
| `pellet_meteorite` | pièce d'arme ou de munition : les armes sont fournies par SBW et GeoWarfare |

## Fluides demandés par une recette active sans producteur obtenable

Un fluide sans producteur rend inutilisables les recettes qui le demandent (les autres voies restent possibles). Le gaz `gas` vient de la gazéification du charbon ou du coke au four de pyrolyse ; les pompes GeoMods ne donnent que le brut.

| Fluide | Recettes qui le demandent |
|---|---|
| `alumina` | 1 : electrolyser_fluid/alumina |
| `amat` | 2 : purex/flashgold, purex/flashlead |
| `bauxite_solution` | 1 : fraction/bauxite_solution |
| `blood` | 3 : compressor/blood_3, mixer/pheromone_m.0, mixer/salient.0 |
| `calcium_chloride` | 1 : electrolyser_fluid/calcium_chloride |
| `calcium_solution` | 1 : coker/calcium_solution |
| `chlorocalcite_cleaned` | 1 : chemical_plant/cccentrifuge |
| `chlorocalcite_mix` | 1 : fraction/chlorocalcite_mix |
| `chlorocalcite_solution` | 1 : mixer/chlorocalcite_mix.0 |
| `naphtha_coker` | 3 : fraction/naphtha_coker, mixer/solvent.3, reforming/naphtha_coker |
| `pheromone` | 1 : mixer/pheromone_m.0 |
| `potassium_chloride` | 1 : electrolyser_fluid/potassium_chloride |
| `redmud` | 4 : coker/redmud, crystallizer/powder_calcium_redmud, electrolyser_fluid/redmud, silex/fluid_redmud |
| `salient` | 1 : solidification/salient |
| `sodium_aluminate` | 4 : mixer/alumina.0, mixer/alumina.1, rotary_furnace/aluminium, rotary_furnace/aluminium_2 |
| `stellar_flux` | 1 : fusion/stellar |
| `thorium_salt_depleted` | 1 : purex/thoriumsalt |
| `vitriol` | 3 : coker/vitriol, electrolyser_fluid/vitriol, silex/fluid_vitriol |
| `xpjuice` | 2 : chemical_plant/epearl, mixer/enderjuice.0 |

## Obtenus seulement par le butin

Ces objets visibles n'ont pas de recette praticable : on ne les a qu'en ouvrant des coffres ou en tuant des mobs.

`blueprint_folder_secret`, `fallout`, `falloutitem`, `fusion_core`, `hazmat_kit`, `iv_blood`, `launch_code`, `launch_code_piece`, `launch_key`, `radaway`, `radaway_flush`, `radaway_strong`, `syringe_metal_super`, `upgrade_5g`

## Ressources planétaires sur Terre

Une ressource unique par planète, absente de la Terre : desh (Lune), ostrum et saturnite (Mars), calorite (Vénus), schrabidium (Mercure), starmetal (Glacio). Calcul `TERRE=1 python tools/verifier_obtention.py <jar>` : les minerais de ces matières ne comptent plus comme du monde et les objets `ad_astra:` de desh, ostrum et calorite ne sont plus fournis ; ce qui reste obtenable est une fuite.

aucune fuite

