#!/usr/bin/env python3
"""Mesure ce que le périmètre retire des tables de recettes, sans rien modifier."""
import collections
import csv
import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
PACK = ROOT / 'src/generated/data/pack/data/geontm/recipe'
sys.stdout.reconfigure(encoding='utf-8')
LIGNES = list(csv.DictReader((ROOT / 'docs/perimetre/contenu.csv').open(encoding='utf-8')))
DECISION = {x['id']: x['decision'] for x in LIGNES}
RETIRE = {k for k, v in DECISION.items() if v == 'RETIRER'}
MATIERES_OK = set()
for x in LIGNES:
    m = re.match(r'(ingot|nugget|plate|powder|wire|block|rod)_(.+)$', x['id'])
    if m and x['decision'] != 'RETIRER':
        MATIERES_OK.add(m[2])
MATIERES_OK |= {'iron', 'gold', 'copper', 'coal', 'redstone', 'diamond', 'emerald', 'lapis', 'quartz'}


def parcours(v, cle=''):
    if isinstance(v, dict):
        for k, w in v.items():
            yield from parcours(w, k)
    elif isinstance(v, list):
        for w in v:
            yield from parcours(w, cle)
    elif isinstance(v, str):
        yield cle, v


stats = collections.Counter()
par_table = collections.defaultdict(collections.Counter)
causes = collections.Counter()
cles = collections.Counter()
besoins = collections.Counter()
for p in PACK.rglob('*.json'):
    table = p.relative_to(PACK).parts[0] if len(p.relative_to(PACK).parts) > 1 else '(racine)'
    j = json.loads(p.read_text(encoding='utf-8'))
    motifs, ids = set(), set()
    for k, s in parcours(j):
        cles[k] += 1
        if s.startswith('geontm:') and k != 'type':
            i = s.split(':', 1)[1]
            ids.add(i)
            if i in RETIRE:
                motifs.add('objet:' + i)
        elif k == 'material' and s not in MATIERES_OK:
            motifs.add('matiere:' + s)
        elif s.startswith('#c:') and '/' in s:
            m = s.rsplit('/', 1)[1].replace('any_', '')
            if m not in MATIERES_OK:
                motifs.add('tag:' + m)
    etat = 'retirée' if motifs else 'gardée'
    stats[etat] += 1
    par_table[table][etat] += 1
    for m in motifs:
        causes[m] += 1
    if not motifs:
        for i in ids:
            if DECISION.get(i) == 'SI_BESOIN':
                besoins[i] += 1
print(dict(stats))
print('tables', {t: dict(c) for t, c in sorted(par_table.items())})
print('causes', causes.most_common(45))
print('clés', cles.most_common(30))
print('SI_BESOIN utilisés', len(besoins), 'sur', sum(1 for v in DECISION.values() if v == 'SI_BESOIN'), besoins.most_common(25))
