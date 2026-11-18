# Mise en production de GeoNTM

Rien de cette liste n'est lancé sans l'accord du propriétaire. GeoNTM n'est aujourd'hui installé que sur le serveur de test.

## Décisions du propriétaire

- [ ] Les 42 objets `A_DECIDER` de `docs/perimetre/contenu.csv`, dont `missile_micro` et `missile_nuclear`.
- [ ] Les cratères radioactifs : les garder dans le monde ou non.
- [ ] Les ogives N2, Taint et Balefire, aujourd'hui classées en butin de guerre.
- [ ] Le remplacement de Ballistix : GeoNTM ne doit pas arriver à côté de lui sans choix clair.
- [ ] Le style des interfaces, après avoir vu les écrans en jeu.
- [ ] Le monde visé : nouvelle saison ou monde existant.

## Preuves à obtenir sur le serveur de test

- [ ] Protection : explosion, radiation et transformation de blocs refusées en zone protégée.
- [ ] Guerre : tir autorisé, payé, puis impact ; tir refusé sans perte du missile ; interception ; annulation remboursée.
- [ ] Énergie : batterie et machines reliées dans les deux sens aux câbles GeoMods, taux 5 HE pour 1 FE.
- [ ] Radioactivité : dose unique, compteur Geiger, combinaisons GeoMods, zones visibles côté GeoMods.
- [ ] Schémas : recette bloquée sans schéma, dossier soumis à la recherche, copie interdite des schémas secrets, butin reçu après une vraie guerre et jamais en double.
- [ ] Espace : minerais rares sur leurs planètes seulement, satellite lancé et inscrit au bon pays.
- [ ] Interfaces : créatif, JEI et écrans principaux regardés par un humain.
- [ ] Essai avec plusieurs vrais joueurs, de la connexion par le launcher à une partie complète.

## Mesures à relever avec et sans GeoNTM

- [ ] Temps de démarrage du serveur et du client.
- [ ] TPS et MSPT avec des joueurs, des réseaux d'énergie et un réacteur en marche.
- [ ] Mémoire utilisée et taille du téléchargement : le jar GeoNTM pèse environ 74 Mo.

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
