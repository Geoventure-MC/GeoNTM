# GeoNTM

GeoNTM apporte à [Geoventure](https://geoventure.fr) une industrie nucléaire, des satellites, des missiles et une vraie radioactivité par zones. C'est un mod Forge 1.20.1 de la même famille que GeoMods, GeoWarfare, GeoCuisine et GeoDecor.

Il part de [NTM-Continuum](https://github.com/Warfactory-Official/NTM-Continuum) 1.0.6, lui-même issu de HBM's Nuclear Tech Mod, et le recentre sur nos besoins : moins de contenu, nos ressources, notre progression et nos interfaces.

> **État : fondations.** Le code est encore celui de NTM-Continuum 1.0.6. Rien n'est déployé en production.

## Ce que nous visons

- **Satellites** : modules à fabriquer, lancement, satellites visibles en jeu, liés à la course à l'espace.
- **Missiles** : plusieurs types, soumis aux règles de guerre de Geoventure.
- **Radioactivité** : zones réelles, protection et décontamination.
- **Machines et réacteurs** : des chaînes complexes, débloquées par les âges, la R&D et les schémas.
- **Ressources maîtrisées** : les ressources Geoventure sur Terre, quelques ajouts, et des matières rares accessibles seulement par fusée.

## Documents

| Document | Contenu |
|---|---|
| [Plan de travail](docs/PLAN.md) | Étapes, architecture et règles du projet |
| [Périmètre](docs/PERIMETRE.md) | Ce qu'on garde, simplifie, remplace ou retire |
| [Détail du contenu](docs/perimetre/contenu.csv) | La décision pour chaque bloc et chaque objet |
| [Licences et crédits](NOTICE.md) | Origine du code et obligations |
| [README d'origine](docs/NTM-CONTINUUM-README.md) | Documentation de NTM-Continuum |

## Dépôts liés

- [GeoCore](https://github.com/Geoventure-MC/GeoCore) : bibliothèque commune, fork de Golem Core compatible Mohist.
- [geoMods](https://github.com/Geoventure-MC/geoMods) : mod principal de Geoventure.

## Construire

Java 17 ou plus. Les deux dépôts doivent être côte à côte, GeoCore dans un dossier nommé `Golem-Core` :

```bash
git clone https://github.com/Geoventure-MC/GeoCore.git Golem-Core
git clone https://github.com/Geoventure-MC/GeoNTM.git
cd Golem-Core && ./gradlew chiseledBuild
cd ../GeoNTM && ./gradlew :1.20.1-forge:build
```

Le jar se trouve dans `versions/1.20.1-forge/build/libs/`. La compilation est aussi lancée par GitHub Actions.

## Licence

LGPL-3.0, comme le projet d'origine. Voir [NOTICE.md](NOTICE.md).
