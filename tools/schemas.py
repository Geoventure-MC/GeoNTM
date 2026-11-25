#!/usr/bin/env python3
"""Répartit les recettes de l'assembleur entre les familles de schémas Geoventure.

« discover » : schémas courants, obtenus par la recherche du pays et copiables.
« secret » : schémas stratégiques, obtenus seulement en butin de guerre et non copiables.
Les recherches de chaque famille sont dans fr.geoventure.geontm.pont.PontProgression.

Usage : python tools/schemas.py
"""
import json
from pathlib import Path

RACINE = Path(__file__).resolve().parent.parent
RECETTES = RACINE / 'src/generated/data/pack/data/geontm/recipe/assembly_machine'
DOC = RACINE / 'docs/perimetre/schemas.md'


def serie(prefixe, *suites):
    return [prefixe + s for s in suites]


FAMILLES = {
    'discover.ogives': ('Ogives conventionnelles', 'mil_rocketry',
                        serie('warheadhe', '1', '2', '3') + serie('warheadinc', '1', '2', '3')
                        + serie('warheadbb', '1', '2', '3')),
    'discover.ogives_lourdes': ('Ogives à sous-munitions et grande rampe', 'mil_ballistics',
                                serie('warheadcl', '1', '2', '3') + ['launchpad', 'stealthmissile']),
    'discover.missiles_modulaires': ('Missiles modulaires', 'mil_strategic', ['missileassembly']
                                     + serie('mpf', '1015bf', '1015hydro', '1015kero', '1015solid', '10kero',
                                             '10kerolong', '10solid', '10solidlong', '10xenon', '1520kero',
                                             '1520solid', '15hydro', '15kero', '15solid')
                                     + serie('mpt', '10kero', '10solid', '10xenon', '15bf', '15bflarge', '15bfshort',
                                             '15hydro', '15hydrodual', '15kero', '15kerodual', '15kerotriple',
                                             '15solid', '15solid16', '20kero', '20kerodual', '20kerotriple',
                                             '20solid', '20solidmulti', '20solidmultier')
                                     + serie('mpw', '10bus', '10cloud', '10he', '10inc', '15he', '15inc')),
    'discover.reacteurs': ('Réacteurs à fission', 'nuclear_tech',
                           ['cirnox', 'rbmkautoloader', 'pileblock']
                           + serie('pile', 'nu', 'pabe', 'pobe', 'thorium', 'zr')
                           + serie('pwr', 'casing', 'channel', 'control', 'controller', 'fuel', 'heatex', 'heatsink',
                                   'neutronsource', 'port', 'reflector', 'reflectoralt')
                           + serie('watz', 'casing', 'cooler', 'rod')),
    'discover.fusion': ('Fusion', 'sci_materials',
                        serie('fusion', 'blanket', 'boiler', 'breeder', 'bscco', 'collector', 'coupler', 'klystron',
                              'mhdt', 'pipes', 'plasmaforge') + ['geoventure_fusioncore']),
    'discover.spatial': ('Lancement spatial et satellites', 'sci_aerospace',
                         ['launchpadsoyuz', 'soyuz', 'lander', 'shuttlemissile', 'satdock', 'satlink', 'radarsat',
                          'scansat', 'relaysat', 'detectorsat', 'spacelab', 'sensorrelay', '0gassembler',
                          'lunarminer']),
    'discover.espace_lointain': ('Espace lointain', 'sci_deepspace',
                                 ['launchpadlambda', 'lambdarocket', 'astrominer', 'xenrelay', 'thrusternerva']),
    'secret.ogives_speciales': ('Ogives spéciales', None,
                                ['warheadnuke', 'warheadthermonuke', 'mpw10nukesmall', 'mpw10nukelarge', 'mpw15nuke',
                                 'mpw15n2', 'mpw15bf', 'mpw10taint']),
    'secret.orbital': ('Armes et renseignement orbitaux', None,
                       ['deathray', 'orbitallaser', 'spysat', 'rayscansat']),
}


def main():
    vues = {}
    for famille, (_, _, recettes) in FAMILLES.items():
        for nom in recettes:
            if nom in vues:
                raise SystemExit(f'{nom} est dans {vues[nom]} et {famille}')
            vues[nom] = famille
            chemin = RECETTES / f'{nom}.json'
            brut = chemin.read_text(encoding='utf-8')
            recette = json.loads(brut)
            if recette.get('blueprint_pools') not in (None, [famille]):
                raise SystemExit(f'{nom} a déjà un schéma : {recette["blueprint_pools"]}')
            sortie = {}
            for cle, valeur in recette.items():
                if cle == 'blueprint_pools':
                    continue
                if cle == 'order':
                    sortie['blueprint_pools'] = [famille]
                sortie[cle] = valeur
            sortie.setdefault('blueprint_pools', [famille])
            texte = json.dumps(sortie, ensure_ascii=False, indent=2)
            chemin.write_text(texte + ('\n' if brut.endswith('\n') else ''), encoding='utf-8', newline='\n')

    lignes = ['# Schémas Geoventure', '',
              'Fichier généré par `tools/schemas.py`.', '',
              '| Famille | Contenu | Obtention | Recettes |', '|---|---|---|---|']
    for famille, (titre, recherche, recettes) in FAMILLES.items():
        origine = f'dossier de schémas, recherche `{recherche}`' if recherche else 'butin de guerre'
        lignes.append(f'| `{famille}` | {titre} | {origine} | {len(recettes)} |')
    DOC.write_text('\n'.join(lignes) + '\n', encoding='utf-8', newline='\n')
    print(len(vues), 'recettes réparties dans', len(FAMILLES), 'familles')


if __name__ == '__main__':
    main()
