# Plan de travail

## Principes

1. **Un mod séparé, relié à nos systèmes.** GeoNTM reste un mod à part sous LGPL. Il parle à GeoMods et GeoFactions par des interfaces claires.
2. **Un fork assumé.** Nous partons de la version 1.0.6. Les évolutions de NTM-Continuum seront reprises à la main, si elles valent le coût.
3. **Une liste de ce qu'on garde.** Tout ce qui n'est pas retenu disparaît, voir le [périmètre](PERIMETRE.md).
4. **Le serveur de test d'abord.** Rien ne passe en production sans validation du propriétaire.

## Architecture

```text
GeoCore   : registres, réseau, rendu, interfaces, compatibilité Mohist
   |
GeoNTM    : machines, réacteurs, satellites, missiles, radioactivité
   |
Ponts     : énergie GE, pays et protection, guerre, âges, R&D, économie
   |
GeoMods / GeoFactions / GeoWarfare / GeoCuisine / GeoDecor
```

GeoCore n'est pas réécrit : 3 818 classes compilées de NTM en dépendent. Il devient notre bibliothèque, et les ponts s'y ajoutent.

## Étapes

| Étape | But | Terminé quand |
|---|---|---|
| 0. Fondations ✅ | Dépôts, compilation, périmètre proposé | Le jar se construit sur GitHub |
| 1. Identité (code fait, test en jeu à faire) | Passer au nom GeoNTM, 1.20.1 Forge uniquement | Le mod démarre sur le serveur de test |
| 2. Réduction (en cours) | Masquer puis retirer le contenu hors périmètre | Aucune recette ni référence cassée |
| 3. Ressources | Remplacer les doublons, placer les matières rares dans l'espace | Chaque recette gardée est réalisable |
| 4. Sécurité | Protection des pays, règles de guerre, bombes coupées | Testé sur un territoire protégé |
| 5. Progression | Âges, R&D, schémas | Aucun contournement par recette |
| 6. Systèmes | Radioactivité unique, missiles, énergie | Plus de doublon avec GeoMods ni Ballistix |
| 7. Satellites | Modules, lancement, affichage en jeu | Boucle complète jouable |
| 8. Interfaces | Style Geoventure, écrans simplifiés | Écrans principaux validés en jeu |
| 9. Préparation | Performances, équilibrage, migration | Test avec de vrais joueurs validé |

## Bugs connus

- `geontm:machine_lithium_battery` ne se décharge pas vers les câbles lors du test d'énergie.

## Licences des autres mods

Vu dans les fichiers `mods.toml` des jars le 18/11/2026. À revérifier sur la source avant toute reprise de code.

| Mod | Licence déclarée | Conséquence |
|---|---|---|
| Ballistix, Voltaic, Rechiseled, Fusion, Mega's Arms, Simple Voice Chat, bibliothèques SuperMartijn642 | Tous droits réservés | Pas d'intégration sans accord écrit |
| Ad Astra | Terrarium License | Reste un mod séparé |
| ECF, Refined Storage, Farmer's Delight | MIT | Intégrable avec crédit |
| SBW Drone Warfare | GPL | Intégrable, la partie reprise reste en GPL |
| Superb Warfare | GPL dans le jar | Code et visuels à vérifier séparément |
