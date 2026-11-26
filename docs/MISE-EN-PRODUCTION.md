# Mise en production de GeoNTM

Rien de cette liste n'est lancé sans l'accord du propriétaire. GeoNTM n'est aujourd'hui installé que sur le serveur de test.

## Décisions du propriétaire

- [x] Missiles : tous gardés ; les plus extrêmes n'ont ni recette ni présence dans les listes (`tools/simplifier.py`).
- [ ] Les autres objets `A_DECIDER` de `docs/perimetre/contenu.csv` : drones et machines.
- [x] Cratères radioactifs : aucun cratère naturel, seulement ceux créés par une frappe.
- [x] Ogives N2, Taint et Balefire : gardées sans recette.
- [x] Ballistix : à remplacer par GeoNTM une fois la guerre validée.
- [x] Interfaces : images NTM gardées, couche Geoventure sur une dizaine d'écrans principaux, à valider à l'œil.
- [x] Monde : sortie avec une nouvelle saison.

## Preuves à obtenir sur le serveur de test

- [ ] Protection : explosion, radiation et transformation de blocs refusées en zone protégée.
- [ ] Guerre : tir autorisé, payé, puis impact ; tir refusé sans perte du missile ; interception ; annulation remboursée.
- [ ] Énergie : batterie et machines reliées dans les deux sens aux câbles GeoMods, taux 5 HE pour 1 FE.
- [ ] Radioactivité : dose unique, compteur Geiger, combinaisons GeoMods, zones visibles côté GeoMods.
- [ ] Schémas : recette bloquée sans schéma, dossier soumis à la recherche, copie interdite des schémas secrets, butin reçu après une vraie guerre et jamais en double.
- [ ] Espace : minerais rares sur leurs planètes seulement, satellite lancé et inscrit au bon pays.
- [ ] Fabrication : les 39 objets rendus fabricables par l'audit d'obtention (`docs/perimetre/obtention.md`) : circuit étoile (débris de plastique à l'assembleur, puis cristalliseur), noyaux de structure du tore de fusion et du Watz, conversions au boulonneur et au chalumeau, empennages et capteur de radiation d'établi, construction d'un tore, d'un Watz, d'un lanceur Soyouz et d'une table de lancement.
- [ ] Interfaces : créatif, JEI et écrans principaux regardés par un humain.
- [ ] Essai avec plusieurs vrais joueurs, de la connexion par le launcher à une partie complète.

### Ce qui est déjà prouvé hors serveur (26/11, jar 0.1.21)

Aucune des 9 cases ci-dessus n'est cochée : il reste à les jouer sur le serveur de test. Ce qu'on a pu établir sans lui :

- Fabrication : `tools/verifier_obtention.py` (avec le jar GeoMods) relève 2495 objets obtenables ; les 2 objets visibles non obtenables (`soyuz_capsule`, `syringe_mkunicorn`) sont justifiés. Les 24 recettes sans producteur restent cachées, causes dans `docs/perimetre/sans-producteur.md`. À jouer : les constructions (tore, Watz, Soyouz, table de lancement).
- Espace : GameTest `ponts_espace_minerais_rares` (starmetal et schrabidium générés seulement par des fichiers `espace_*`, sur des biomes d'Ad Astra) et audit `TERRE=1` (aucune fuite sur Terre). À jouer : la présence réelle sur Glacio et Mercure, le satellite inscrit au bon pays.
- Orbites : GameTest `ponts_espace_orbites` (Glacio reconnu comme astre d'orbite, orbite -> surface pour les six astres). À jouer : un lancement depuis une station orbitale de Glacio, avec le plugin (il reçoit désormais `glacio_orbit`, au lieu de `earth_orbit`).
- Faune : GameTest `ponts_espace_peuplement` (modificateurs de biome générés, mobs enregistrés). À jouer : l'apparition réelle sur Mars, Vénus et Mercure (de nuit, glyphes seulement).
- Protection, énergie, radioactivité : les GameTests de bout en bout (`warforge_safe_zone_blast`, `power_geomods_bridge_energy_balance`) exigent WarForge ou GeoMods chargés et ne font rien sur le banc ; leur preuve reste à faire sur le serveur.
- Interfaces : le bandeau couvre tous les écrans `GUI*` de GeoNTM (test `ponts_ecrans_bandeau` pour le classement) ; le rendu doit être regardé par un humain.

## Mesures à relever avec et sans GeoNTM

- [ ] Temps de démarrage du serveur et du client.
- [ ] TPS et MSPT avec des joueurs, des réseaux d'énergie et un réacteur en marche.
- [ ] Mémoire utilisée et taille du téléchargement : le jar GeoNTM pèse environ 74 Mo.

## Lancement de la nouvelle saison

1. Fusionner [geoMods#103](https://github.com/Geoventure-MC/geoMods/pull/103) et [Pluginmc#82](https://github.com/Geoventure-MC/Pluginmc/pull/82) après relecture : âges et recherche de GeoNTM y sont déjà, 4 923 identifiants tous verrouillés.
2. Publier GeoCore et GeoNTM en version stable, puis les mettre dans le pack de production avec Ad Astra.
3. Retirer Ballistix et Voltaic du serveur et du pack. Essai du 19/11 sur le serveur de test : le serveur démarre sans eux ; seule GeoMods les cite, en dépendance facultative. Un monde existant perd ses 346 entrées Ballistix, sans gravité sur un monde neuf. Retirer ensuite les ids `ballistix:*` de `ages.yml` et `research-tree.yml`.
4. Créer le monde de la saison avec `explosion_destroy = false` et `extra_explosion_effect = false` dans `serverconfig/superbwarfare-server.toml` : SuperbWarfare casse les blocs avant d'annoncer son explosion, donc aucune protection ne l'arrête (prouvé le 19/11 ; réglage coupé ce jour-là sur le monde actuel). Sauvegarde complète avant ouverture.
5. Vérifier les journaux (« Protection reliée à GeoMods », « Radioactivité reliée à GeoMods », « Satellites reliés »), puis jouer une vraie guerre à deux joueurs : tir, impact, interception, butin.

## Avant le déploiement

- [ ] Fusionner les branches `geontm-api` de geoMods et Pluginmc. Sur ces dépôts, tout commit sur `main` part en ligne : relire d'abord.
- [ ] Publier des versions stables de GeoCore et GeoNTM, plus de préversion `test`.
- [ ] Monter `GeoNetwork.VERSION` si le format des paquets a changé.
- [ ] Ajouter aux âges et à la recherche les identifiants `geontm:*`, tous réellement enregistrés.
- [ ] Vérifier la licence LGPL : sources, `NOTICE.md` et crédits disponibles.
- [ ] Faire une sauvegarde complète du monde, de la base et des configurations, et noter comment revenir en arrière.
- [ ] Prévoir une fenêtre de maintenance et une annonce validée par le propriétaire.

## Après le déploiement

- [ ] Ne plus supprimer d'identifiant GeoNTM : mondes et inventaires en dépendent.
- [ ] Vérifier les journaux de démarrage du mod et du plugin, puis la connexion d'un joueur par le launcher.
- [ ] Surveiller TPS, plantages, économie et tirs de missiles pendant les premières heures.
