# Recettes sans producteur

Les 24 recettes de `src/main/resources/data/geontm/recipe_removals/geoventure_sans_producteur.json` exigent un fluide qu'aucune recette, aucun récipient ni aucune machine obtenable ne produit. Elles sont cachées (retirées par `recipe_removals`, sans toucher aux fichiers de recette d'origine) plutôt que laissées injouables. Audit du 26/11 sur le jar 0.1.21 : `python tools/verifier_obtention.py <jar> --retraits fichier.json` (avec le jar GeoMods en `AUTRES_JARS`) réécrit exactement la même liste ; `verifier_chaines.py` ne signale aucun fluide sans producteur.

Aucune n'est rendue fabricable : chaque racine manquante est un contenu déjà écarté du périmètre (`docs/perimetre/contenu.csv`), et leur redonner un producteur recréerait une filière que GeoMods couvre déjà ou, c'est un risque d'équilibrage non testé, ouvrirait une source de métaux (fer, mercure, titane, néodyme, bore, zirconium) sans minerai.

| Racine manquante | Pourquoi | Recettes cachées |
|---|---|---|
| `bauxite_solution`, `sodium_aluminate`, `alumina`, `redmud` | la bauxite et ses fragments de minerai de socle (`bedrock_ore_fragment_bauxite`, `stone_resource_bauxite`) sont hors périmètre : l'aluminium vient de GeoMods | 11 : `fraction/bauxite_solution`, `mixer/alumina.0`, `mixer/alumina.1`, `electrolyser_fluid/alumina`, `electrolyser_fluid/redmud`, `rotary_furnace/aluminium`, `rotary_furnace/aluminium_2`, `coker/redmud`, `solidification/redmud`, `silex/fluid_redmud`, `crystallizer/powder_calcium_redmud` |
| `vitriol` | seule la torréfaction des minerais de socle (`combination/bedrock_ore_*`) en produit ; cette filière est hors périmètre | 3 : `coker/vitriol`, `electrolyser_fluid/vitriol`, `silex/fluid_vitriol` |
| `chlorocalcite_solution`, `chlorocalcite_mix`, `chlorocalcite_cleaned`, `calcium_chloride`, `potassium_chloride`, `calcium_solution` | la poudre de chlorocalcite ne vient que des fragments de minerai de socle (`bedrock_ore_fragment_chlorocalcite`, sans recette) | 6 : `mixer/chlorocalcite_mix.0`, `fraction/chlorocalcite_mix`, `chemical_plant/cccentrifuge`, `electrolyser_fluid/calcium_chloride`, `electrolyser_fluid/potassium_chloride`, `coker/calcium_solution` |
| `amat` | capsule d'antimatière (`particle_amat`) retirée : objet extrême | 2 : `purex/flashgold`, `purex/flashlead` |
| `pheromone` | aucune source obtenable d'après l'audit | 1 : `mixer/pheromone_m.0` |
| `stellar_flux` | aucune source obtenable d'après l'audit | 1 : `fusion/stellar` |

Pour rouvrir une famille, il faut d'abord redonner un producteur à sa racine (recette Geoventure `geoventure_*` ou retour de l'ancien contenu au périmètre), puis relancer `--retraits` : la liste se réduit seule.
