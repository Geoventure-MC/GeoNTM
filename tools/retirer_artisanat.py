#!/usr/bin/env python3
"""Retire les recettes d'établi et de four du contenu hors périmètre.

Usage : python tools/retirer_artisanat.py <geontm-forge.jar>

Le jar sert à lire les recettes générées à la compilation. Les ingrédients indispensables au contenu gardé passent dans
docs/perimetre/dependances.txt ; les autres recettes d'objets retirés vont dans data/geontm/recipe_removals/perimetre.json.
"""
import collections
import csv
import json
import re
import sys
import zipfile
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
sys.stdout.reconfigure(encoding='utf-8')
PREFIXE = 'data/geontm/recipes/'
SORTIE = ROOT / 'src/main/resources/data/geontm/recipe_removals/perimetre.json'
DEPENDANCES = ROOT / 'docs/perimetre/dependances.txt'

lignes = list(csv.DictReader((ROOT / 'docs/perimetre/contenu.csv').open(encoding='utf-8')))
decision = {x['id']: x['decision'] for x in lignes}
dependances = set(DEPENDANCES.read_text(encoding='utf-8').split()) if DEPENDANCES.exists() else set()


def identifiant(v):
    if isinstance(v, str):
        return v
    if isinstance(v, dict):
        return v.get('item') or v.get('id') or ''
    return ''


def ingredients(v):
    if isinstance(v, dict):
        if 'item' in v and isinstance(v['item'], str):
            yield v['item']
        for w in v.values():
            yield from ingredients(w)
    elif isinstance(v, list):
        for w in v:
            yield from ingredients(w)


z = zipfile.ZipFile(sys.argv[1])
recettes, par_sortie = [], collections.defaultdict(list)
for n in z.namelist():
    if not (n.startswith(PREFIXE) and n.endswith('.json')) or '/' in n[len(PREFIXE):]:
        continue
    j = json.loads(z.read(n))
    resultat = identifiant(j.get('result') or j.get('output'))
    if not resultat.startswith('geontm:'):
        continue
    objet = resultat[7:]
    entree = j.get('key') or j.get('ingredients') or j.get('ingredient') or {}
    bruts = ' '.join(sorted(set(ingredients(entree))))
    besoins = sorted({i[7:] for i in ingredients(entree) if i.startswith('geontm:') and i[7:] in decision})
    r = {'id': 'geontm:' + n[len(PREFIXE):-5], 'objet': objet, 'besoins': besoins, 'bruts': bruts}
    recettes.append(r)
    par_sortie[objet].append(r)

file = collections.deque(sorted(k for k, v in decision.items() if v in ('GARDER', 'SIMPLIFIER')) + sorted(dependances))
vus = set(file)
while file:
    o = file.popleft()
    choix = min(par_sortie.get(o, []), key=lambda r: (sum(decision.get(b) == 'RETIRER' for b in r['besoins']), len(r['besoins'])),
                default=None)
    if not choix:
        continue
    for b in choix['besoins']:
        if decision.get(b) == 'RETIRER':
            dependances.add(b)
        if b not in vus:
            vus.add(b)
            file.append(b)

ESPACE = re.compile(r'(ingot|nugget|powder|plate|billet|crystal|block|wire)_(desh|saturnite|starmetal|schrabidium)$')


def fabrique_espace(r):
    m = ESPACE.fullmatch(r['objet'])
    return bool(m) and m[2] not in r['bruts']


retires = sorted(r['id'] for r in recettes
                 if fabrique_espace(r) or (decision.get(r['objet']) == 'RETIRER' and r['objet'] not in dependances))
SORTIE.parent.mkdir(parents=True, exist_ok=True)
SORTIE.write_text(json.dumps({'recipes': retires}, indent=1) + '\n', encoding='utf-8', newline='\n')
DEPENDANCES.write_text('\n'.join(sorted(dependances)) + '\n', encoding='utf-8', newline='\n')
print('recettes lues', len(recettes), 'retirées', len(retires), 'dépendances', len(dependances))
