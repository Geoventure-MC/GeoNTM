#!/usr/bin/env python3
"""Liste le contenu gardé qu'aucune recette du dépôt ou du jar d'origine ne produit encore.

Usage : python tools/verifier_fabrication.py [jar-d-origine]
"""
import csv
import json
import re
import sys
import zipfile
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
sys.stdout.reconfigure(encoding='utf-8')
CLES = re.compile(r'(^|_)(output|outputs|result|results|item_output|output_item|output_items)$')


def sorties(v, dedans=False):
    if isinstance(v, dict):
        for k, w in v.items():
            yield from sorties(w, dedans or bool(CLES.search(k)))
    elif isinstance(v, list):
        for w in v:
            yield from sorties(w, dedans)
    elif dedans and isinstance(v, str) and ':' in v:
        yield v.split(':', 1)[1]


lignes = list(csv.DictReader((ROOT / 'docs/perimetre/contenu.csv').open(encoding='utf-8')))
gardes = {x['id']: x['theme'] for x in lignes if x['decision'] in ('GARDER', 'SIMPLIFIER')}
produits = set()
for p in (ROOT / 'src/generated/data/pack/data/geontm/recipe').rglob('*.json'):
    produits.update(sorties(json.loads(p.read_text(encoding='utf-8'))))
tables_machines = set()
if len(sys.argv) > 1:
    z = zipfile.ZipFile(sys.argv[1])
    for n in z.namelist():
        m = re.match(r'data/hbm/recipes?/([^/]+)(/.*)?\.json$', n)
        if not m:
            continue
        j = json.loads(z.read(n))
        table = m[1] if m[2] else ''
        if table and table != 'crafting':
            continue
        produits.update(s.replace('hbm:', '') for s in sorties(j))
machines = sorted(i for i in gardes if i.startswith('machine_') and i not in produits)
autres = sorted(i for i in gardes if not i.startswith('machine_') and i not in produits)
print('gardés', len(gardes), 'produits', len(set(gardes) & produits))
print('machines sans recette', len(machines), machines[:80])
print('autres sans recette', len(autres), autres[:60])
