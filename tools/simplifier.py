#!/usr/bin/env python3
"""Allège le créatif et JEI : masque les états intermédiaires des combustibles.

Ces objets restent dans le jeu et les réacteurs continuent de les produire. Un objet cité par une recette reste visible,
pour que le joueur puisse toujours comprendre ce qu'il fabrique ou retraite.

Usage : python tools/simplifier.py
"""
import csv
import re
from pathlib import Path

RACINE = Path(__file__).resolve().parent.parent
CREATIF = RACINE / 'src/main/resources/geontm/perimetre/creatif.txt'
SORTIE = RACINE / 'src/main/resources/geontm/perimetre/simplifie.txt'
DOC = RACINE / 'docs/perimetre/simplification.md'
SOURCES = [(RACINE / 'src/generated/data/pack/data/geontm/recipe', '*.json'),
           (RACINE / 'src/main/java/fr/geoventure/geontm/inventory/recipes', '*.java')]
REGLES = {
    'pastilles RBMK par usure': re.compile(r'^rbmk_pellet_.+(_e[0-9](_xe)?|_xe)$'),
    'combustibles usés': re.compile(r'^(pwr_fuel|rbmk_fuel|watz_pellet)_depleted(_|$)|^pwr_fuel_.*_depleted$'),
    'déchets dérivés': re.compile(r'^nuclear_waste_.+(_depleted|_tiny)(_|$)|^nuclear_waste_.*depleted'),
}


def mots_cites():
    mots = set()
    for dossier, motif in SOURCES:
        for fichier in dossier.rglob(motif):
            mots.update(re.findall(r'[a-z0-9_]+', fichier.read_text(encoding='utf-8', errors='replace')))
    return mots


def main():
    visibles = set(CREATIF.read_text(encoding='utf-8').split())
    with open(RACINE / 'docs/perimetre/contenu.csv', encoding='utf-8-sig', newline='') as f:
        a_simplifier = {l[1] for l in csv.reader(f) if len(l) > 3 and l[0] == 'objet' and l[3] == 'SIMPLIFIER'}
    cites = mots_cites()
    masques, gardes = {}, {}
    for nom, regle in REGLES.items():
        trouves = {i for i in a_simplifier & visibles if regle.search(i)}
        masques[nom] = sorted(trouves - cites)
        gardes[nom] = len(trouves & cites)
    tous = sorted({i for v in masques.values() for i in v})
    SORTIE.write_text('\n'.join(tous) + '\n', encoding='utf-8', newline='\n')
    lignes = ['# Simplification du créatif et de JEI', '', 'Fichier généré par `tools/simplifier.py`. '
              'Les objets masqués restent dans le jeu.', '', '| Règle | Masqués | Gardés car cités par une recette |',
              '|---|---|---|']
    lignes += [f'| {n} | {len(masques[n])} | {gardes[n]} |' for n in REGLES]
    DOC.write_text('\n'.join(lignes) + '\n', encoding='utf-8', newline='\n')
    print(len(tous), 'objets masqués sur', len(visibles), {n: (len(masques[n]), gardes[n]) for n in REGLES})


if __name__ == '__main__':
    main()
