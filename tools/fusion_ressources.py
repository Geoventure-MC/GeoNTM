#!/usr/bin/env python3
"""Applique le périmètre aux recettes de GeoNTM et branche les ressources de GeoMods.

Usage : python tools/fusion_ressources.py

- écrit la table Java des ressources GeoMods acceptées dans les tags de matières ;
- remplace dans les recettes les objets NTM qui ont un équivalent Geoventure ;
- retire les recettes des machines supprimées ou qui dépendent d'un objet retiré ;
- met à jour src/generated/data/manifest.txt et docs/perimetre/recettes.md.

Relancer l'outil ne change rien : les recettes déjà traitées ne contiennent plus rien à remplacer.
"""
import collections
import csv
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
DATA = ROOT / 'src/generated/data'
RECETTES = DATA / 'pack/data/geontm/recipe'
JAVA = ROOT / 'src/main/java/fr/geoventure/geontm/inventory/material/GeoventureMatieres.java'
SYNONYMES = {'aluminum': 'aluminium'}
FORMES = {'ingot': 'ingots', 'dust': 'dusts', 'plate': 'plates', 'wire': 'wires', 'block': 'storage_blocks', 'ore': 'ores',
          'rod': 'rods'}
TABLES_RETIREES = {'ammo_press', 'annihilator', 'lemegeton', 'magic', 'particle_accelerator', 'pedestal', 'plasma_forge',
                   'anvil_smithing', 'anvil_construction', 'tool_conversion'}
TAGS_RETIRES = {'smokeless', 'highexplosive', 'weaponsteel', 'gunmetal', 'hardplastic_ammo'}

lignes = list(csv.DictReader((ROOT / 'docs/perimetre/contenu.csv').open(encoding='utf-8')))
decision = {x['id']: x['decision'] for x in lignes}
retire = {k for k, v in decision.items() if v == 'RETIRER'}
cibles = {x['id']: x['cible'] for x in lignes if x['decision'] == 'FUSIONNER' and x['theme'] == 'ressources' and x['cible']}


def ecrire_java():
    registre = {l.split()[-1] for l in (ROOT / 'docs/perimetre/registre-geomods.txt').read_text(encoding='utf-8').splitlines()
                if l.strip()}
    table = {}
    for nom in sorted(registre):
        m = re.fullmatch(r'([a-z0-9_]+)_(ingot|dust|plate|wire|block|ore|rod)', nom)
        if m and '_' not in m[1]:
            matiere = SYNONYMES.get(m[1], m[1])
            table[f'{FORMES[m[2]]}/{matiere}'] = 'geomods:' + nom
    entrees = ',\n'.join(f'            Map.entry("{k}", "{v}")' for k, v in sorted(table.items()))
    JAVA.write_text(f'''package fr.geoventure.geontm.inventory.material;

import fr.geoventure.geocore.dejson.Tags;

import java.util.Map;

/** Ressources GeoMods acceptées par les recettes GeoNTM. Généré par {{@code tools/fusion_ressources.py}}. */
final class GeoventureMatieres {{

    private static final Map<String, String> OBJETS = Map.ofEntries(
{entrees});

    private GeoventureMatieres() {{}}

    /** Déclare un tag {{@code c:}} de matière, avec l'objet Geoventure équivalent s'il existe. */
    static void declarer(String chemin, String... valeurs) {{
        Tags.Tag tag = Tags.of("item", "c", chemin).values(valeurs);
        String objet = OBJETS.get(chemin);
        if (objet != null) tag.optional(objet);
        tag.register();
    }}
}}
''', encoding='utf-8', newline='\n')
    return len(table)


def remplacer(v):
    if isinstance(v, dict):
        return {k: remplacer(w) for k, w in v.items()}
    if isinstance(v, list):
        return [remplacer(w) for w in v]
    if isinstance(v, str) and v.startswith('geontm:') and v[7:] in cibles:
        return cibles[v[7:]]
    return v


def chaines(v, cle=''):
    if isinstance(v, dict):
        for k, w in v.items():
            yield from chaines(w, k)
    elif isinstance(v, list):
        for w in v:
            yield from chaines(w, cle)
    elif isinstance(v, str):
        yield cle, v


def motif_retrait(table, j):
    if table in TABLES_RETIREES:
        return 'machine retirée'
    for cle, s in chaines(j):
        if cle == 'type':
            continue
        if s.startswith('geontm:') and s[7:] in retire:
            return s[7:]
        if s.startswith('#c:') and '/' in s and s.rsplit('/', 1)[1].replace('any_', '') in TAGS_RETIRES:
            return s
    return None


def main():
    nb_tags = ecrire_java()
    gardees, retirees, remplacees = collections.Counter(), collections.Counter(), 0
    causes = collections.Counter()
    for p in sorted(RECETTES.rglob('*.json')):
        rel = p.relative_to(RECETTES)
        table = rel.parts[0] if len(rel.parts) > 1 else ''
        j = json.loads(p.read_text(encoding='utf-8'))
        cause = motif_retrait(table, j)
        if cause:
            p.unlink()
            retirees[table] += 1
            causes[cause] += 1
            continue
        nouveau = remplacer(j)
        if nouveau != j:
            remplacees += 1
            p.write_text(json.dumps(nouveau, indent=2, ensure_ascii=False) + '\n', encoding='utf-8', newline='\n')
        gardees[table] += 1
    for d in sorted(RECETTES.rglob('*'), reverse=True):
        if d.is_dir() and not any(d.iterdir()):
            d.rmdir()

    manifeste = []
    for l in (DATA / 'manifest.txt').read_text(encoding='utf-8').splitlines():
        if not l.startswith('geontm/recipe'):
            manifeste.append(l)
    for table, n in sorted(gardees.items()):
        manifeste.append(f'geontm/recipe/{table} {n}' if table else f'geontm/recipe {n}')
    (DATA / 'manifest.txt').write_text('\n'.join(sorted(manifeste)) + '\n', encoding='utf-8', newline='\n')

    t = ['# Recettes conservées', '', f'- Recettes gardées : **{sum(gardees.values())}**',
         f'- Recettes retirées lors de ce passage : **{sum(retirees.values())}**',
         f'- Recettes adaptées aux objets Geoventure lors de ce passage : **{remplacees}**',
         f'- Tags de matières reliés à GeoMods : **{nb_tags}**', '', '| Table | Recettes gardées |', '|---|---:|']
    t += [f'| `{k or "(racine)"}` | {v} |' for k, v in sorted(gardees.items())]
    if retirees:
        (ROOT / 'docs/perimetre/recettes.md').write_text('\n'.join(t) + '\n', encoding='utf-8', newline='\n')
    print('gardées', sum(gardees.values()), 'retirées', sum(retirees.values()), 'adaptées', remplacees, 'tags', nb_tags)
    print('causes', causes.most_common(30))


if __name__ == '__main__':
    main()
