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
AUDIT = RACINE / 'docs/perimetre/obtention-audit.tsv'
SOURCES = [(RACINE / 'src/generated/data/pack/data/geontm/recipe', '*.json'),
           (RACINE / 'src/main/java/fr/geoventure/geontm/inventory/recipes', '*.java')]
# Décision du propriétaire : ces objets restent dans le mod, sans recette ni présence dans les listes.
EXTREMES = '''missile_doomsday missile_doomsday_rusted missile_schrabidium missile_n2 missile_bhole missile_taint
missile_volcano missile_nuclear_cluster warhead_mirv warhead_mirvlet warhead_n2 warhead_thermo_endo warhead_thermo_exo
warhead_volcano mp_warhead_15_n2 mp_warhead_10_taint mp_warhead_15_balefire'''.split()
# Décision du propriétaire (19/11) : objets écartés pour l'instant, sans recette (recipe_removals/ecartes.json).
ECARTES = ['drone_patrol_chunkloading', 'drone_patrol_express_chunkloading', 'machine_forcefield', 'machine_icf_press']


def caches_de_l_audit():
    """Objets de l'audit d'obtention classés B : sans recette en amont et sans place en survie, donc cachés (l'id reste)."""
    if not AUDIT.exists():
        return []
    lignes = [l.split('\t') for l in AUDIT.read_text(encoding='utf-8').splitlines() if l.strip() and not l.startswith('#')]
    return sorted(l[0] for l in lignes if l[1] == 'B')


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
    audit = caches_de_l_audit()
    tous = sorted({i for v in masques.values() for i in v} | set(EXTREMES) | set(ECARTES) | set(audit))
    SORTIE.write_text('\n'.join(tous) + '\n', encoding='utf-8', newline='\n')
    lignes = ['# Simplification du créatif et de JEI', '', 'Fichier généré par `tools/simplifier.py`. '
              'Les objets masqués restent dans le jeu.', '', '| Règle | Masqués | Gardés car cités par une recette |',
              '|---|---|---|']
    lignes += [f'| {n} | {len(masques[n])} | {gardes[n]} |' for n in REGLES]
    lignes.append(f'| armes extrêmes, sans recette | {len(EXTREMES)} | 0 |')
    lignes.append(f'| objets écartés, sans recette | {len(ECARTES)} | 0 |')
    lignes.append(f"| audit d'obtention (classe B, voir obtention.md) | {len(audit)} | 0 |")
    DOC.write_text('\n'.join(lignes) + '\n', encoding='utf-8', newline='\n')
    print(len(tous), 'objets masqués sur', len(visibles), {n: (len(masques[n]), gardes[n]) for n in REGLES})


if __name__ == '__main__':
    main()
