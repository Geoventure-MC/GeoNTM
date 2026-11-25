# Obtention en survie

Fichier généré par `tools/verifier_obtention.py` à partir du jar compilé : point fixe sur les recettes actives (conditions évaluées avec les réglages par défaut : 528, mode coûteux et LBSM désactivés), en exigeant que la machine de chaque recette soit elle-même obtenable. Les objets, tags et fluides de GeoMods comptent comme fournis.

- Objets et fluides obtenables : **2447** (recettes : 1918, butin : 155, blocs de monde : 112, drops : 21)
- Objets visibles : **1305** ; visibles non obtenables : **2**
- Hypothèse : 122 recettes actives (missiles modulaires, espace, ogives, réacteurs...) ne se débloquent qu'avec un schéma (`blueprint_pools`, mécanisme NTM inchangé) trouvé dans les coffres ; l'outil les compte comme faisables. Les réacteurs, le combustible usé et les déchets sont supposés produits par les machines Java correspondantes. Les règles de broyage construites en Java (`ShredderRecipes` : bûches et planches en sciure, minerais, lingots et blocs en poudre) sont modélisées : la sciure du cordite et le coltan broyé de `coltanpain` en viennent. Sont aussi modélisés d'après le code : les constructions autour d'un noyau de structure (lanceurs, Watz, tore de fusion), les conversions à l'outil (`tool_conversion`), les récipients de fluide (vidés dans une machine), les poches de sang et d'expérience, les déchets des barres ZIRNOX usées, les pastilles RTG usées, les schémas ouverts depuis un dossier et la machine d'assemblage de missiles. Un fluide et un objet de même nom (`watz`, `concrete`, `balefire`) sont distingués.

| Catégorie | Nombre |
|---|---:|
| volontaire | 8 |
| hors recette | 2 |

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

## hors recette

| Objet | Justification |
|---|---|
| `soyuz_capsule` | capsule de cargaison déposée par une mission d'atterrisseur (missile_soyuz_lander, SoyuzCapsule) ; se ramasse, jamais fabriquée |
| `syringe_mkunicorn` | recette secrète à disposition mélangée selon la graine du monde (MKUCraftingRecipe), indices dans les livres des donjons de météorite |

## Classement des objets qu'aucune recette ne produisait

Audit du 25/11 sur le jar 0.1.13 : 326 objets visibles non obtenables (323 relevés, trois autres révélés par la correction de l’outil). Source : `docs/perimetre/obtention-audit.tsv` (id, classe, famille, raison). **A** : rendu fabricable ; **B** : caché dans `simplifie.txt` (l'id reste enregistré) ; **C** : obtenu hors recette, justifié. La colonne « état » est recalculée sur le jar vérifié.

| Classe | Nombre |
|---|---:|
| A : fabricable | 39 |
| B : caché | 268 |
| C : hors recette | 19 |

### A : rendus fabricables

| Objet | Famille | État | Raison |
|---|---|---|---|
| `circuit_star` | circuit étoile | obtenu (recette circuit_star) | recettes d'origine (composants à la main) ; pièces déjà au cristalliseur |
| `circuit_star_component_card` | circuit étoile | obtenu (recette circuit_star_component_card) | recettes d'origine (composants à la main) ; pièces déjà au cristalliseur |
| `circuit_star_component_chipset` | circuit étoile | obtenu (recette circuit_star_component_chipset) | recettes d'origine (composants à la main) ; pièces déjà au cristalliseur |
| `circuit_star_component_cpu` | circuit étoile | obtenu (recette circuit_star_component_cpu) | recettes d'origine (composants à la main) ; pièces déjà au cristalliseur |
| `circuit_star_component_ram` | circuit étoile | obtenu (recette circuit_star_component_ram) | recettes d'origine (composants à la main) ; pièces déjà au cristalliseur |
| `circuit_star_piece_board_blank` | circuit étoile | obtenu (recette crystallizer/scrap_plastic_board_blank_peroxide) | cristalliseur d'origine (débris de plastique + peroxyde) ; les débris se fabriquent maintenant à l'assembleur (geoventure_scrap_plastic_*) |
| `circuit_star_piece_board_converter` | circuit étoile | obtenu (recette crystallizer/scrap_plastic_board_converter_peroxide) | cristalliseur d'origine (débris de plastique + peroxyde) ; les débris se fabriquent maintenant à l'assembleur (geoventure_scrap_plastic_*) |
| `circuit_star_piece_board_transistor` | circuit étoile | obtenu (recette crystallizer/scrap_plastic_board_transistor_peroxide) | cristalliseur d'origine (débris de plastique + peroxyde) ; les débris se fabriquent maintenant à l'assembleur (geoventure_scrap_plastic_*) |
| `circuit_star_piece_bridge_bios` | circuit étoile | obtenu (recette crystallizer/scrap_plastic_bridge_bios_peroxide) | cristalliseur d'origine (débris de plastique + peroxyde) ; les débris se fabriquent maintenant à l'assembleur (geoventure_scrap_plastic_*) |
| `circuit_star_piece_bridge_bus` | circuit étoile | obtenu (recette crystallizer/scrap_plastic_bridge_bus_peroxide) | cristalliseur d'origine (débris de plastique + peroxyde) ; les débris se fabriquent maintenant à l'assembleur (geoventure_scrap_plastic_*) |
| `circuit_star_piece_bridge_chipset` | circuit étoile | obtenu (recette crystallizer/scrap_plastic_bridge_chipset_peroxide) | cristalliseur d'origine (débris de plastique + peroxyde) ; les débris se fabriquent maintenant à l'assembleur (geoventure_scrap_plastic_*) |
| `circuit_star_piece_bridge_cmos` | circuit étoile | obtenu (recette crystallizer/scrap_plastic_bridge_cmos_peroxide) | cristalliseur d'origine (débris de plastique + peroxyde) ; les débris se fabriquent maintenant à l'assembleur (geoventure_scrap_plastic_*) |
| `circuit_star_piece_bridge_io` | circuit étoile | obtenu (recette crystallizer/scrap_plastic_bridge_io_peroxide) | cristalliseur d'origine (débris de plastique + peroxyde) ; les débris se fabriquent maintenant à l'assembleur (geoventure_scrap_plastic_*) |
| `circuit_star_piece_bridge_north` | circuit étoile | obtenu (recette crystallizer/scrap_plastic_bridge_north_peroxide) | cristalliseur d'origine (débris de plastique + peroxyde) ; les débris se fabriquent maintenant à l'assembleur (geoventure_scrap_plastic_*) |
| `circuit_star_piece_bridge_south` | circuit étoile | obtenu (recette crystallizer/scrap_plastic_bridge_south_peroxide) | cristalliseur d'origine (débris de plastique + peroxyde) ; les débris se fabriquent maintenant à l'assembleur (geoventure_scrap_plastic_*) |
| `circuit_star_piece_card_board` | circuit étoile | obtenu (recette crystallizer/scrap_plastic_card_board_peroxide) | cristalliseur d'origine (débris de plastique + peroxyde) ; les débris se fabriquent maintenant à l'assembleur (geoventure_scrap_plastic_*) |
| `circuit_star_piece_card_processor` | circuit étoile | obtenu (recette crystallizer/scrap_plastic_card_processor_peroxide) | cristalliseur d'origine (débris de plastique + peroxyde) ; les débris se fabriquent maintenant à l'assembleur (geoventure_scrap_plastic_*) |
| `circuit_star_piece_cpu_cache` | circuit étoile | obtenu (recette crystallizer/scrap_plastic_cpu_cache_peroxide) | cristalliseur d'origine (débris de plastique + peroxyde) ; les débris se fabriquent maintenant à l'assembleur (geoventure_scrap_plastic_*) |
| `circuit_star_piece_cpu_clock` | circuit étoile | obtenu (recette crystallizer/scrap_plastic_cpu_clock_peroxide) | cristalliseur d'origine (débris de plastique + peroxyde) ; les débris se fabriquent maintenant à l'assembleur (geoventure_scrap_plastic_*) |
| `circuit_star_piece_cpu_ext` | circuit étoile | obtenu (recette crystallizer/scrap_plastic_cpu_ext_peroxide) | cristalliseur d'origine (débris de plastique + peroxyde) ; les débris se fabriquent maintenant à l'assembleur (geoventure_scrap_plastic_*) |
| `circuit_star_piece_cpu_logic` | circuit étoile | obtenu (recette crystallizer/scrap_plastic_cpu_logic_peroxide) | cristalliseur d'origine (débris de plastique + peroxyde) ; les débris se fabriquent maintenant à l'assembleur (geoventure_scrap_plastic_*) |
| `circuit_star_piece_cpu_register` | circuit étoile | obtenu (recette crystallizer/scrap_plastic_cpu_register_peroxide) | cristalliseur d'origine (débris de plastique + peroxyde) ; les débris se fabriquent maintenant à l'assembleur (geoventure_scrap_plastic_*) |
| `circuit_star_piece_cpu_socket` | circuit étoile | obtenu (recette crystallizer/scrap_plastic_cpu_socket_peroxide) | cristalliseur d'origine (débris de plastique + peroxyde) ; les débris se fabriquent maintenant à l'assembleur (geoventure_scrap_plastic_*) |
| `circuit_star_piece_mem_16k_a` | circuit étoile | obtenu (recette crystallizer/scrap_plastic_mem_16k_a_peroxide) | cristalliseur d'origine (débris de plastique + peroxyde) ; les débris se fabriquent maintenant à l'assembleur (geoventure_scrap_plastic_*) |
| `circuit_star_piece_mem_16k_b` | circuit étoile | obtenu (recette crystallizer/scrap_plastic_mem_16k_b_peroxide) | cristalliseur d'origine (débris de plastique + peroxyde) ; les débris se fabriquent maintenant à l'assembleur (geoventure_scrap_plastic_*) |
| `circuit_star_piece_mem_16k_c` | circuit étoile | obtenu (recette crystallizer/scrap_plastic_mem_16k_c_peroxide) | cristalliseur d'origine (débris de plastique + peroxyde) ; les débris se fabriquent maintenant à l'assembleur (geoventure_scrap_plastic_*) |
| `circuit_star_piece_mem_16k_d` | circuit étoile | obtenu (recette crystallizer/scrap_plastic_mem_16k_d_peroxide) | cristalliseur d'origine (débris de plastique + peroxyde) ; les débris se fabriquent maintenant à l'assembleur (geoventure_scrap_plastic_*) |
| `circuit_star_piece_mem_socket` | circuit étoile | obtenu (recette crystallizer/scrap_plastic_mem_socket_peroxide) | cristalliseur d'origine (débris de plastique + peroxyde) ; les débris se fabriquent maintenant à l'assembleur (geoventure_scrap_plastic_*) |
| `launch_table` | lanceurs | obtenu (recette construction/launch_table) | déjà constructibles (noyau de structure + blocs, ConstructionHandler) ; l'outil de vérification ne lisait pas les constructions |
| `soyuz_launcher` | lanceurs | obtenu (recette construction/soyuz_launcher) | déjà constructibles (noyau de structure + blocs, ConstructionHandler) ; l'outil de vérification ne lisait pas les constructions |
| `missile_custom` | missiles personnalisés | obtenu (recette java/missile_custom) | sortie de la machine d'assemblage de missiles à partir des pièces mp_* ; l'outil de vérification ne lisait pas cette machine |
| `mp_s_20` | pièces et capteurs | obtenu (recette mp_s_20) | recette d'établi ajoutée (geoventure) : aucune recette d'origine |
| `mp_stability_15_soyuz` | pièces et capteurs | obtenu (recette mp_stability_15_soyuz) | recette d'établi ajoutée (geoventure) : aucune recette d'origine |
| `radsensor` | pièces et capteurs | obtenu (recette radsensor) | recette d'établi ajoutée (geoventure) : aucune recette d'origine |
| `capacitor_spark` | réacteurs Watz et fusion | obtenu (recette assembly_machine/capacitorspark) | le réacteur Watz se construit autour de struct_watz_core, dont la recette d'établi d'origine avait été retirée à tort ; sa boue alimente l'acier Combine (condensateur à étincelle) |
| `fusion_component_bscco_welded` | réacteurs Watz et fusion | obtenu (recette tool_conversion/geoventure_fusion_component) | conversion en jeu au chalumeau ou au boulonneur (tool_conversion), table d'origine retirée à tort, rétablie (geoventure_*) |
| `fusion_torus` | réacteurs Watz et fusion | obtenu (recette construction/fusion_torus) | construction autour de struct_torus_core ; la recette d'origine du noyau (ass.fusioncore) manquait, rétablie (geoventure_fusioncore) |
| `watz` | réacteurs Watz et fusion | obtenu (recette construction/watz) | le réacteur Watz se construit autour de struct_watz_core, dont la recette d'établi d'origine avait été retirée à tort ; sa boue alimente l'acier Combine (condensateur à étincelle) |
| `watz_casing_tooled` | réacteurs Watz et fusion | obtenu (recette tool_conversion/geoventure_watz_casing) | conversion en jeu au chalumeau ou au boulonneur (tool_conversion), table d'origine retirée à tort, rétablie (geoventure_*) |

### B : cachés

| Objet | Famille | État | Raison |
|---|---|---|---|
| `icf` | ICF | caché | ICF écarté par le propriétaire (le HDE est forgé avec le flux stellaire de l'ICF) |
| `icf_block` | ICF | caché | ICF écarté par le propriétaire (le HDE est forgé avec le flux stellaire de l'ICF) |
| `icf_component` | ICF | caché | ICF écarté par le propriétaire (le HDE est forgé avec le flux stellaire de l'ICF) |
| `icf_component_structure` | ICF | caché | ICF écarté par le propriétaire (le HDE est forgé avec le flux stellaire de l'ICF) |
| `icf_component_structure_bolted` | ICF | caché | ICF écarté par le propriétaire (le HDE est forgé avec le flux stellaire de l'ICF) |
| `icf_component_vessel` | ICF | caché | ICF écarté par le propriétaire (le HDE est forgé avec le flux stellaire de l'ICF) |
| `icf_component_vessel_welded` | ICF | caché | ICF écarté par le propriétaire (le HDE est forgé avec le flux stellaire de l'ICF) |
| `icf_controller` | ICF | caché | ICF écarté par le propriétaire (le HDE est forgé avec le flux stellaire de l'ICF) |
| `icf_laser_component_capacitor` | ICF | caché | ICF écarté par le propriétaire (le HDE est forgé avec le flux stellaire de l'ICF) |
| `icf_laser_component_casing` | ICF | caché | ICF écarté par le propriétaire (le HDE est forgé avec le flux stellaire de l'ICF) |
| `icf_laser_component_cell` | ICF | caché | ICF écarté par le propriétaire (le HDE est forgé avec le flux stellaire de l'ICF) |
| `icf_laser_component_emitter` | ICF | caché | ICF écarté par le propriétaire (le HDE est forgé avec le flux stellaire de l'ICF) |
| `icf_laser_component_port` | ICF | caché | ICF écarté par le propriétaire (le HDE est forgé avec le flux stellaire de l'ICF) |
| `icf_laser_component_turbo` | ICF | caché | ICF écarté par le propriétaire (le HDE est forgé avec le flux stellaire de l'ICF) |
| `icf_pellet` | ICF | caché | ICF écarté par le propriétaire (le HDE est forgé avec le flux stellaire de l'ICF) |
| `icf_pellet_depleted` | ICF | caché | ICF écarté par le propriétaire (le HDE est forgé avec le flux stellaire de l'ICF) |
| `part_generic_hde` | ICF | caché | ICF écarté par le propriétaire (le HDE est forgé avec le flux stellaire de l'ICF) |
| `mechanism_launcher_1` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `mechanism_launcher_2` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `mechanism_revolver_1` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `mechanism_revolver_2` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `mechanism_rifle_1` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `mechanism_rifle_2` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `mechanism_special` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_barrel_heavy` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_barrel_heavy_cdalloy` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_barrel_heavy_desh` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_barrel_heavy_dura_steel` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_barrel_heavy_ferrouranium` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_barrel_heavy_gunmetal` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_barrel_heavy_saturnite` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_barrel_heavy_steel` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_barrel_heavy_tcalloy` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_barrel_heavy_weaponsteel` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_barrel_light` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_barrel_light_arsenic_bronze` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_barrel_light_bismuth_bronze` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_barrel_light_cdalloy` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_barrel_light_desh` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_barrel_light_dura_steel` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_barrel_light_gunmetal` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_barrel_light_saturnite` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_barrel_light_steel` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_barrel_light_tcalloy` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_barrel_light_weaponsteel` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_emitter` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_grip` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_grip_desh` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_grip_dura_steel` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_grip_gunmetal` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_grip_pvc` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_grip_saturnite` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_grip_steel` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_grip_weaponsteel` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_mechanism` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_mechanism_gunmetal` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_mechanism_saturnite` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_mechanism_weaponsteel` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_receiver_heavy` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_receiver_heavy_arsenic_bronze` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_receiver_heavy_bismuth_bronze` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_receiver_heavy_cdalloy` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_receiver_heavy_dura_steel` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_receiver_heavy_ferrouranium` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_receiver_heavy_gunmetal` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_receiver_heavy_saturnite` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_receiver_heavy_tcalloy` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_receiver_heavy_weaponsteel` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_receiver_light` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_receiver_light_arsenic_bronze` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_receiver_light_bismuth_bronze` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_receiver_light_cdalloy` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_receiver_light_desh` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_receiver_light_dura_steel` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_receiver_light_gunmetal` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_receiver_light_saturnite` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_receiver_light_steel` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_receiver_light_tcalloy` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_receiver_light_weaponsteel` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_stock` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_stock_desh` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_stock_gunmetal` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_stock_pvc` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_stock_saturnite` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `part_stock_weaponsteel` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `parts_legendary_tier1` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `parts_legendary_tier2` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `parts_legendary_tier3` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `pellet_canister` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `pellet_chlorophyte` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `pellet_claws` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `pellet_coal` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `pellet_coolant` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `pellet_flechette` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `pellet_meteorite` | armes | caché | pièce d'arme ou de munition : aucune arme NTM n'est visible (SBW et GeoWarfare) |
| `cap_rad` | coffret, curiosité ou doublon | caché | coffret de départ, curiosité ou doublon d'un objet fabricable (fluid_identifier_multi) |
| `fluid_identifier` | coffret, curiosité ou doublon | caché | coffret de départ, curiosité ou doublon d'un objet fabricable (fluid_identifier_multi) |
| `hazmat_grey_kit` | coffret, curiosité ou doublon | caché | coffret de départ, curiosité ou doublon d'un objet fabricable (fluid_identifier_multi) |
| `hazmat_red_kit` | coffret, curiosité ou doublon | caché | coffret de départ, curiosité ou doublon d'un objet fabricable (fluid_identifier_multi) |
| `med_schizophrenia` | coffret, curiosité ou doublon | caché | coffret de départ, curiosité ou doublon d'un objet fabricable (fluid_identifier_multi) |
| `missile_kit` | coffret, curiosité ou doublon | caché | coffret de départ, curiosité ou doublon d'un objet fabricable (fluid_identifier_multi) |
| `missile_skin_camo` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `missile_skin_desert` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `missile_skin_flames` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `missile_skin_manly_pink` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `missile_skin_metal` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `missile_skin_orange_insulation` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `missile_skin_sleek` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `missile_skin_soviet_glory` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `missile_skin_soviet_stank` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_kerosene_camo` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_kerosene_desert` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_kerosene_flames` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_kerosene_sky` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_kerosene_sleek` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_kerosene_taint` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_long_kerosene_camo` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_long_kerosene_dash` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_long_kerosene_desert` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_long_kerosene_flames` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_long_kerosene_sky` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_long_kerosene_sleek` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_long_kerosene_taint` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_long_kerosene_vap` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_long_solid_bullet` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_long_solid_flames` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_long_solid_silvermoonlight` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_long_solid_sleek` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_long_solid_soviet_glory` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_solid_battery` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_solid_cathedral` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_solid_duracell` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_solid_flames` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_solid_moonlit` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_solid_sleek` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_10_solid_soviet_glory` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_15_20_kerosene_magnusson` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_15_hydrogen_cathedral` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_15_kerosene_blackjack` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_15_kerosene_camo` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_15_kerosene_decorated` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_15_kerosene_desert` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_15_kerosene_lambda` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_15_kerosene_minuteman` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_15_kerosene_pip` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_15_kerosene_polite` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_15_kerosene_sky` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_15_kerosene_steampunk` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_15_kerosene_taint` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_15_kerosene_yuck` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_15_solid_faust` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_15_solid_mimi` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_15_solid_panorama` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_15_solid_roses` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_15_solid_silvermoonlight` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_15_solid_snowy` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_15_solid_soviet_glory` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_fuselage_15_solid_soviet_stank` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_warhead_15_nuclear_mimi` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `mp_warhead_15_nuclear_shark` | cosmétique | caché | peinture ou variante de peinture de missile : décor sans recette en amont |
| `capacitor_bus` | hérité ou sans objet | caché | bloc ou classe dépréciée, bloc formé sans objet, ou outil créatif |
| `capacitor_schrabidate` | hérité ou sans objet | caché | bloc ou classe dépréciée, bloc formé sans objet, ou outil créatif |
| `capacitor_tantalium` | hérité ou sans objet | caché | bloc ou classe dépréciée, bloc formé sans objet, ou outil créatif |
| `fusion_core_block` | hérité ou sans objet | caché | bloc ou classe dépréciée, bloc formé sans objet, ou outil créatif |
| `fusion_core_infinite` | hérité ou sans objet | caché | bloc ou classe dépréciée, bloc formé sans objet, ou outil créatif |
| `fusion_hatch` | hérité ou sans objet | caché | bloc ou classe dépréciée, bloc formé sans objet, ou outil créatif |
| `fusion_heater` | hérité ou sans objet | caché | bloc ou classe dépréciée, bloc formé sans objet, ou outil créatif |
| `fusion_klystron_creative` | hérité ou sans objet | caché | bloc ou classe dépréciée, bloc formé sans objet, ou outil créatif |
| `pile_block` | hérité ou sans objet | caché | bloc ou classe dépréciée, bloc formé sans objet, ou outil créatif |
| `pile_device` | hérité ou sans objet | caché | bloc ou classe dépréciée, bloc formé sans objet, ou outil créatif |
| `pile_rod_boron` | hérité ou sans objet | caché | bloc ou classe dépréciée, bloc formé sans objet, ou outil créatif |
| `pile_rod_detector` | hérité ou sans objet | caché | bloc ou classe dépréciée, bloc formé sans objet, ou outil créatif |
| `pile_rod_lithium` | hérité ou sans objet | caché | bloc ou classe dépréciée, bloc formé sans objet, ou outil créatif |
| `pile_rod_plutonium` | hérité ou sans objet | caché | bloc ou classe dépréciée, bloc formé sans objet, ou outil créatif |
| `pile_rod_pu239` | hérité ou sans objet | caché | bloc ou classe dépréciée, bloc formé sans objet, ou outil créatif |
| `pile_rod_source` | hérité ou sans objet | caché | bloc ou classe dépréciée, bloc formé sans objet, ou outil créatif |
| `pile_rod_uranium` | hérité ou sans objet | caché | bloc ou classe dépréciée, bloc formé sans objet, ou outil créatif |
| `pwr_block` | hérité ou sans objet | caché | bloc ou classe dépréciée, bloc formé sans objet, ou outil créatif |
| `pwr_printer` | hérité ou sans objet | caché | bloc ou classe dépréciée, bloc formé sans objet, ou outil créatif |
| `anvil_murky` | matière ou arme extrême | caché | balefire, flashlead, unobtainium, yharonite, DRX, trou noir : sans recette par décision du propriétaire |
| `billet_balefire_gold` | matière ou arme extrême | caché | balefire, flashlead, unobtainium, yharonite, DRX, trou noir : sans recette par décision du propriétaire |
| `billet_flashlead` | matière ou arme extrême | caché | balefire, flashlead, unobtainium, yharonite, DRX, trou noir : sans recette par décision du propriétaire |
| `billet_unobtainium` | matière ou arme extrême | caché | balefire, flashlead, unobtainium, yharonite, DRX, trou noir : sans recette par décision du propriétaire |
| `billet_yharonite` | matière ou arme extrême | caché | balefire, flashlead, unobtainium, yharonite, DRX, trou noir : sans recette par décision du propriétaire |
| `mp_fuselage_10_xenon_bhole` | matière ou arme extrême | caché | balefire, flashlead, unobtainium, yharonite, DRX, trou noir : sans recette par décision du propriétaire |
| `mp_fuselage_15_balefire` | matière ou arme extrême | caché | balefire, flashlead, unobtainium, yharonite, DRX, trou noir : sans recette par décision du propriétaire |
| `mp_warhead_15_boxcar` | matière ou arme extrême | caché | ogive ou objet extrême (boxcar nucléaire, turbine, taint) : écarté comme les ogives N2, Taint et Balefire |
| `mp_warhead_15_turbine` | matière ou arme extrême | caché | ogive ou objet extrême (boxcar nucléaire, turbine, taint) : écarté comme les ogives N2, Taint et Balefire |
| `pellet_rtg_balefire` | matière ou arme extrême | caché | balefire, flashlead, unobtainium, yharonite, DRX, trou noir : sans recette par décision du propriétaire |
| `rbmk_fuel_balefire` | matière ou arme extrême | caché | balefire, flashlead, unobtainium, yharonite, DRX, trou noir : sans recette par décision du propriétaire |
| `rbmk_fuel_balefire_gold` | matière ou arme extrême | caché | balefire, flashlead, unobtainium, yharonite, DRX, trou noir : sans recette par décision du propriétaire |
| `rbmk_fuel_drx` | matière ou arme extrême | caché | balefire, flashlead, unobtainium, yharonite, DRX, trou noir : sans recette par décision du propriétaire |
| `rbmk_fuel_flashlead` | matière ou arme extrême | caché | balefire, flashlead, unobtainium, yharonite, DRX, trou noir : sans recette par décision du propriétaire |
| `rbmk_fuel_test` | matière ou arme extrême | caché | balefire, flashlead, unobtainium, yharonite, DRX, trou noir : sans recette par décision du propriétaire |
| `rbmk_pellet_balefire` | matière ou arme extrême | caché | balefire, flashlead, unobtainium, yharonite, DRX, trou noir : sans recette par décision du propriétaire |
| `rbmk_pellet_balefire_gold` | matière ou arme extrême | caché | balefire, flashlead, unobtainium, yharonite, DRX, trou noir : sans recette par décision du propriétaire |
| `rbmk_pellet_drx` | matière ou arme extrême | caché | balefire, flashlead, unobtainium, yharonite, DRX, trou noir : sans recette par décision du propriétaire |
| `rbmk_pellet_flashlead` | matière ou arme extrême | caché | balefire, flashlead, unobtainium, yharonite, DRX, trou noir : sans recette par décision du propriétaire |
| `syringe_taint` | matière ou arme extrême | caché | ogive ou objet extrême (boxcar nucléaire, turbine, taint) : écarté comme les ogives N2, Taint et Balefire |
| `blueprint_plate` | modèle ou identifiant hérité | caché | modèle d'objet ou identifiant de métadonnée hérité, pas un objet à fabriquer |
| `blueprints_528` | modèle ou identifiant hérité | caché | modèle d'objet ou identifiant de métadonnée hérité, pas un objet à fabriquer |
| `blueprints_discover` | modèle ou identifiant hérité | caché | modèle d'objet ou identifiant de métadonnée hérité, pas un objet à fabriquer |
| `blueprints_secret` | modèle ou identifiant hérité | caché | modèle d'objet ou identifiant de métadonnée hérité, pas un objet à fabriquer |
| `missile_obj_context_base` | modèle ou identifiant hérité | caché | modèle d'objet ou identifiant de métadonnée hérité, pas un objet à fabriquer |
| `missile_obj_context_legacy` | modèle ou identifiant hérité | caché | modèle d'objet ou identifiant de métadonnée hérité, pas un objet à fabriquer |
| `missile_soyuz_1` | modèle ou identifiant hérité | caché | modèle d'objet ou identifiant de métadonnée hérité, pas un objet à fabriquer |
| `missile_soyuz_2` | modèle ou identifiant hérité | caché | modèle d'objet ou identifiant de métadonnée hérité, pas un objet à fabriquer |
| `missile_test` | modèle ou identifiant hérité | caché | modèle d'objet ou identifiant de métadonnée hérité, pas un objet à fabriquer |
| `corium_block` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `gas_radon` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `gas_radon_dense` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `gas_radon_tomb` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `gem_rad` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `launch_pad_rusted` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `rad_lava_block` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `rbmk_debris` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `rbmk_debris_burning` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `rbmk_debris_digamma` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `rbmk_debris_radiating` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `sellafield_0` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `sellafield_1` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `sellafield_2` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `sellafield_3` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `sellafield_4` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `sellafield_5` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `sellafield_bedrock` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `sellafield_slaked` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `waste_earth` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `waste_leaves` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `waste_log` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `waste_mycelium` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `waste_planks` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `waste_trinitite` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `waste_trinitite_red` | monde ou catastrophe | caché | bloc ou minerai de monde, de retombées ou de catastrophe : jamais fabriqué |
| `waste_plate_mox` | réacteur de recherche | caché | plaque usée rendue par le réacteur de recherche (machine_reactor_small), retiré du périmètre ; les recyclages PUREX qui la demandent sont désactivés |
| `waste_plate_pu238be` | réacteur de recherche | caché | plaque usée rendue par le réacteur de recherche (machine_reactor_small), retiré du périmètre ; les recyclages PUREX qui la demandent sont désactivés |
| `waste_plate_pu239` | réacteur de recherche | caché | plaque usée rendue par le réacteur de recherche (machine_reactor_small), retiré du périmètre ; les recyclages PUREX qui la demandent sont désactivés |
| `waste_plate_ra226be` | réacteur de recherche | caché | plaque usée rendue par le réacteur de recherche (machine_reactor_small), retiré du périmètre ; les recyclages PUREX qui la demandent sont désactivés |
| `waste_plate_sa326` | réacteur de recherche | caché | plaque usée rendue par le réacteur de recherche (machine_reactor_small), retiré du périmètre ; les recyclages PUREX qui la demandent sont désactivés |
| `waste_plate_u233` | réacteur de recherche | caché | plaque usée rendue par le réacteur de recherche (machine_reactor_small), retiré du périmètre ; les recyclages PUREX qui la demandent sont désactivés |
| `waste_plate_u235` | réacteur de recherche | caché | plaque usée rendue par le réacteur de recherche (machine_reactor_small), retiré du périmètre ; les recyclages PUREX qui la demandent sont désactivés |
| `deco_sat_foeq` | satellites hérités | caché | ancien satellite 1.7 : ne lance rien (XSatelliteRegistry), sans recette en amont ; les satellite_* se fabriquent |
| `deco_sat_laser` | satellites hérités | caché | ancien satellite 1.7 : ne lance rien (XSatelliteRegistry), sans recette en amont ; les satellite_* se fabriquent |
| `deco_sat_mapper` | satellites hérités | caché | ancien satellite 1.7 : ne lance rien (XSatelliteRegistry), sans recette en amont ; les satellite_* se fabriquent |
| `deco_sat_radar` | satellites hérités | caché | ancien satellite 1.7 : ne lance rien (XSatelliteRegistry), sans recette en amont ; les satellite_* se fabriquent |
| `deco_sat_resonator` | satellites hérités | caché | ancien satellite 1.7 : ne lance rien (XSatelliteRegistry), sans recette en amont ; les satellite_* se fabriquent |
| `deco_sat_scanner` | satellites hérités | caché | ancien satellite 1.7 : ne lance rien (XSatelliteRegistry), sans recette en amont ; les satellite_* se fabriquent |
| `sat_base` | satellites hérités | caché | ancien satellite 1.7 : ne lance rien (XSatelliteRegistry), sans recette en amont ; les satellite_* se fabriquent |
| `sat_foeq` | satellites hérités | caché | ancien satellite 1.7 : ne lance rien (XSatelliteRegistry), sans recette en amont ; les satellite_* se fabriquent |
| `sat_gerald` | satellites hérités | caché | ancien satellite 1.7 : ne lance rien (XSatelliteRegistry), sans recette en amont ; les satellite_* se fabriquent |
| `sat_head_laser` | satellites hérités | caché | ancien satellite 1.7 : ne lance rien (XSatelliteRegistry), sans recette en amont ; les satellite_* se fabriquent |
| `sat_head_mapper` | satellites hérités | caché | ancien satellite 1.7 : ne lance rien (XSatelliteRegistry), sans recette en amont ; les satellite_* se fabriquent |
| `sat_head_radar` | satellites hérités | caché | ancien satellite 1.7 : ne lance rien (XSatelliteRegistry), sans recette en amont ; les satellite_* se fabriquent |
| `sat_head_resonator` | satellites hérités | caché | ancien satellite 1.7 : ne lance rien (XSatelliteRegistry), sans recette en amont ; les satellite_* se fabriquent |
| `sat_head_scanner` | satellites hérités | caché | ancien satellite 1.7 : ne lance rien (XSatelliteRegistry), sans recette en amont ; les satellite_* se fabriquent |
| `sat_laser` | satellites hérités | caché | ancien satellite 1.7 : ne lance rien (XSatelliteRegistry), sans recette en amont ; les satellite_* se fabriquent |
| `sat_lunar_miner` | satellites hérités | caché | ancien satellite 1.7 : ne lance rien (XSatelliteRegistry), sans recette en amont ; les satellite_* se fabriquent |
| `sat_mapper` | satellites hérités | caché | ancien satellite 1.7 : ne lance rien (XSatelliteRegistry), sans recette en amont ; les satellite_* se fabriquent |
| `sat_miner` | satellites hérités | caché | ancien satellite 1.7 : ne lance rien (XSatelliteRegistry), sans recette en amont ; les satellite_* se fabriquent |
| `sat_radar` | satellites hérités | caché | ancien satellite 1.7 : ne lance rien (XSatelliteRegistry), sans recette en amont ; les satellite_* se fabriquent |
| `sat_resonator` | satellites hérités | caché | ancien satellite 1.7 : ne lance rien (XSatelliteRegistry), sans recette en amont ; les satellite_* se fabriquent |
| `sat_scanner` | satellites hérités | caché | ancien satellite 1.7 : ne lance rien (XSatelliteRegistry), sans recette en amont ; les satellite_* se fabriquent |
| `rbmk_control_reasim` | variante désactivée | caché | mode 528 désactivé par défaut, ou naquadah d'un autre univers (matière absente) |
| `rbmk_control_reasim_auto` | variante désactivée | caché | mode 528 désactivé par défaut, ou naquadah d'un autre univers (matière absente) |
| `watz_pellet_depleted_nqd` | variante désactivée | caché | mode 528 désactivé par défaut, ou naquadah d'un autre univers (matière absente) |
| `watz_pellet_depleted_nqr` | variante désactivée | caché | mode 528 désactivé par défaut, ou naquadah d'un autre univers (matière absente) |
| `watz_pellet_nqd` | variante désactivée | caché | mode 528 désactivé par défaut, ou naquadah d'un autre univers (matière absente) |
| `watz_pellet_nqr` | variante désactivée | caché | mode 528 désactivé par défaut, ou naquadah d'un autre univers (matière absente) |

### C : obtenus hors recette

| Objet | Famille | État | Raison |
|---|---|---|---|
| `soyuz_capsule` | hors recette | sans recette, justifié | capsule de cargaison déposée par une mission d'atterrisseur (missile_soyuz_lander, SoyuzCapsule) ; se ramasse, jamais fabriquée |
| `gas_full` | produit en jeu | obtenu (recette java/remplir/gas_full) | bouteille de gaz remplie : gas_empty (fabricable) remplie dans une machine (FluidContainerRegistry) |
| `iv_xp` | produit en jeu | obtenu (recette java/utiliser/iv_xp) | iv_xp_empty utilisé sur soi (100 points d'expérience) donne iv_xp ; vidé, il donne le fluide xpjuice |
| `pellet_rtg_depleted_bismuth` | produit en jeu | obtenu (recette reacteur/pellet_rtg_depleted_bismuth) | pastille usée rendue par le générateur RTG (value_maps/item/rtg_pellet.json, decays_to) |
| `pellet_rtg_depleted_mercury` | produit en jeu | obtenu (recette reacteur/pellet_rtg_depleted_mercury) | pastille usée rendue par le générateur RTG (value_maps/item/rtg_pellet.json, decays_to) |
| `pellet_rtg_depleted_neptunium` | produit en jeu | obtenu (recette reacteur/pellet_rtg_depleted_neptunium) | pastille usée rendue par le générateur RTG (value_maps/item/rtg_pellet.json, decays_to) |
| `pellet_rtg_depleted_nickel` | produit en jeu | obtenu (recette reacteur/pellet_rtg_depleted_nickel) | pastille usée rendue par le générateur RTG (value_maps/item/rtg_pellet.json, decays_to) |
| `pellet_rtg_depleted_zirconium` | produit en jeu | obtenu (recette reacteur/pellet_rtg_depleted_zirconium) | pastille usée rendue par le générateur RTG (value_maps/item/rtg_pellet.json, decays_to) |
| `waste_mox` | produit en jeu | obtenu (recette java/zirnox_dechet/waste_mox) | bâton ZIRNOX usé transformé en déchet (ZirnoxWasteRecipe, data map zirnox_fuel) ; recyclé au PUREX |
| `waste_natural_uranium` | produit en jeu | obtenu (recette java/zirnox_dechet/waste_natural_uranium) | bâton ZIRNOX usé transformé en déchet (ZirnoxWasteRecipe, data map zirnox_fuel) ; recyclé au PUREX |
| `waste_plutonium` | produit en jeu | obtenu (recette java/zirnox_dechet/waste_plutonium) | bâton ZIRNOX usé transformé en déchet (ZirnoxWasteRecipe, data map zirnox_fuel) ; recyclé au PUREX |
| `waste_schrabidium` | produit en jeu | obtenu (recette java/zirnox_dechet/waste_schrabidium) | bâton ZIRNOX usé transformé en déchet (ZirnoxWasteRecipe, data map zirnox_fuel) ; recyclé au PUREX |
| `waste_thorium` | produit en jeu | obtenu (recette java/zirnox_dechet/waste_thorium) | bâton ZIRNOX usé transformé en déchet (ZirnoxWasteRecipe, data map zirnox_fuel) ; recyclé au PUREX |
| `waste_u233` | produit en jeu | obtenu (recette java/zirnox_dechet/waste_u233) | bâton ZIRNOX usé transformé en déchet (ZirnoxWasteRecipe, data map zirnox_fuel) ; recyclé au PUREX |
| `waste_u235` | produit en jeu | obtenu (recette java/zirnox_dechet/waste_u235) | bâton ZIRNOX usé transformé en déchet (ZirnoxWasteRecipe, data map zirnox_fuel) ; recyclé au PUREX |
| `waste_uranium` | produit en jeu | obtenu (recette java/zirnox_dechet/waste_uranium) | bâton ZIRNOX usé transformé en déchet (ZirnoxWasteRecipe, data map zirnox_fuel) ; recyclé au PUREX |
| `waste_zfb_mox` | produit en jeu | obtenu (recette java/zirnox_dechet/waste_zfb_mox) | bâton ZIRNOX usé transformé en déchet (ZirnoxWasteRecipe, data map zirnox_fuel) ; recyclé au PUREX |
| `syringe_mkunicorn` | recette secrète | sans recette, justifié | recette secrète à disposition mélangée selon la graine du monde (MKUCraftingRecipe), indices dans les livres des donjons de météorite |
| `blueprints` | schémas | obtenu (recette java/utiliser/blueprints) | clic droit sur un dossier de schémas (blueprint_folder, fabricable à la main) ; la recherche du pays filtre les familles |

### Recettes actives qui exigent un objet caché pour fabriquer un objet visible

`shredder/sellafield_0` -> `scrap_nuclear`, `shredder/sellafield_1` -> `scrap_nuclear`, `shredder/sellafield_2` -> `scrap_nuclear`, `shredder/sellafield_3` -> `scrap_nuclear`, `shredder/sellafield_4` -> `scrap_nuclear`, `shredder/sellafield_5` -> `scrap_nuclear`, `silex/rbmk_pellet_balefire_gold_0` -> `nugget_au198`, `silex/rbmk_pellet_balefire_gold_1` -> `nugget_au198`, `silex/rbmk_pellet_balefire_gold_2` -> `nugget_au198`, `silex/rbmk_pellet_balefire_gold_3` -> `nugget_au198`, `silex/rbmk_pellet_balefire_gold_4` -> `nugget_au198`, `silex/rbmk_pellet_flashlead_0` -> `nugget_au198`, `silex/rbmk_pellet_flashlead_0` -> `nugget_bismuth`, `silex/rbmk_pellet_flashlead_0` -> `nugget_gh336`, `silex/rbmk_pellet_flashlead_0` -> `nugget_mercury_tiny`, `silex/rbmk_pellet_flashlead_0` -> `nugget_pb209`, `silex/rbmk_pellet_flashlead_1` -> `nugget_au198`, `silex/rbmk_pellet_flashlead_1` -> `nugget_bismuth`, `silex/rbmk_pellet_flashlead_1` -> `nugget_gh336`, `silex/rbmk_pellet_flashlead_1` -> `nugget_mercury_tiny`, `silex/rbmk_pellet_flashlead_1` -> `nugget_pb209`, `silex/rbmk_pellet_flashlead_2` -> `nugget_au198`, `silex/rbmk_pellet_flashlead_2` -> `nugget_bismuth`, `silex/rbmk_pellet_flashlead_2` -> `nugget_gh336`, `silex/rbmk_pellet_flashlead_2` -> `nugget_mercury_tiny`, `silex/rbmk_pellet_flashlead_2` -> `nugget_pb209`, `silex/rbmk_pellet_flashlead_3` -> `nugget_au198`, `silex/rbmk_pellet_flashlead_3` -> `nugget_bismuth`, `silex/rbmk_pellet_flashlead_3` -> `nugget_gh336`, `silex/rbmk_pellet_flashlead_3` -> `nugget_mercury_tiny`, `silex/rbmk_pellet_flashlead_3` -> `nugget_pb209`, `silex/rbmk_pellet_flashlead_4` -> `nugget_au198`, `silex/rbmk_pellet_flashlead_4` -> `nugget_bismuth`, `silex/rbmk_pellet_flashlead_4` -> `nugget_gh336`, `silex/rbmk_pellet_flashlead_4` -> `nugget_mercury_tiny`, `silex/rbmk_pellet_flashlead_4` -> `nugget_pb209`

## Fluides demandés par une recette active sans producteur obtenable

Un fluide sans producteur rend inutilisables les recettes qui le demandent (les autres voies restent possibles). Le gaz `gas` vient de la gazéification du charbon ou du coke au four de pyrolyse ; les pompes GeoMods ne donnent que le brut.

| Fluide | Recettes qui le demandent |
|---|---|
| aucun | les recettes qui exigeaient un fluide sans producteur sont désactivées par `recipe_removals/geoventure_sans_producteur.json` (écrit par `--retraits`) ; xpjuice et blood viennent des récipients iv_xp, fiole d’expérience et iv_blood |

## Obtenus seulement par le butin

Ces objets visibles n'ont pas de recette praticable : on ne les a qu'en ouvrant des coffres ou en tuant des mobs.

`block_fallout`, `blueprint_folder_secret`, `cable_detector`, `cable_switch`, `chlorine_pinwheel`, `chunk_ore_moonstone`, `fallout`, `falloutitem`, `fusion_core`, `hazmat_kit`, `launch_code`, `launch_code_piece`, `launch_key`, `missile_micro`, `quartz_plutonium`, `scrap`, `syringe_metal_super`, `upgrade_5g`

## Ressources planétaires sur Terre

Une ressource unique par planète, absente de la Terre : desh (Lune), ostrum et saturnite (Mars), calorite (Vénus), schrabidium (Mercure), starmetal (Glacio). Calcul `TERRE=1 python tools/verifier_obtention.py <jar>` : les minerais de ces matières ne comptent plus comme du monde et les objets `ad_astra:` de desh, ostrum et calorite ne sont plus fournis ; ce qui reste obtenable est une fuite.

aucune fuite

