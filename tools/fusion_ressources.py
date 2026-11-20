#!/usr/bin/env python3
"""Réduit les recettes de GeoNTM en partant du contenu gardé et branche les ressources de GeoMods.

Usage : python tools/fusion_ressources.py

À partir des objets GARDER et SIMPLIFIER de docs/perimetre/contenu.csv, l'outil remonte les recettes nécessaires :
chaque objet gardé reçoit sa recette la plus simple, puis les ingrédients de cette recette deviennent nécessaires.
Un objet marqué RETIRER mais indispensable devient une dépendance. Les recettes sans lien avec un objet retiré restent.
L'outil écrit aussi la table Java des ressources GeoMods, le manifeste et docs/perimetre/recettes.md.
"""
import collections
import csv
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
DATA = ROOT / 'src/generated/data'
RECETTES = DATA / 'pack/data/geontm/recipe'
CSV = ROOT / 'docs/perimetre/contenu.csv'
DEPENDANCES = ROOT / 'docs/perimetre/dependances.txt'
JAVA = ROOT / 'src/main/java/fr/geoventure/geontm/inventory/material/GeoventureMatieres.java'
RESSOURCES = ROOT / 'src/main/resources/geontm/perimetre'
SYNONYMES = {'aluminum': 'aluminium'}
FORMES = {'ingot': 'ingots', 'dust': 'dusts', 'plate': 'plates', 'wire': 'wires', 'block': 'storage_blocks', 'ore': 'ores',
          'rod': 'rods'}
PREFIXES = {'ingots': 'ingot', 'nuggets': 'nugget', 'plates': 'plate', 'dusts': 'powder', 'wires': 'wire', 'billets': 'billet',
            'storage_blocks': 'block', 'gems': 'gem', 'crystals': 'crystal', 'bolts': 'bolt', 'plates_cast': 'plate_cast',
            'plates_welded': 'plate_welded', 'shells': 'shell', 'pipes': 'pipe', 'dusts_tiny': 'powder_tiny',
            'wires_dense': 'wire_dense', 'heavy_components': 'heavy_component'}
TABLES_RETIREES = {'ammo_press', 'annihilator', 'lemegeton', 'magic', 'particle_accelerator', 'pedestal', 'plasma_forge',
                   'tool_conversion'}
CLES_SORTIE = re.compile(r'(^|_)(output|outputs|result|results|output_item|output_items|item_output)$')

lignes = list(csv.DictReader(CSV.open(encoding='utf-8')))
decision = {x['id']: x['decision'] for x in lignes}
cibles = {x['id']: x['cible'] for x in lignes if x['decision'] == 'FUSIONNER' and x['theme'] == 'ressources' and x['cible']}
racines = {k for k, v in decision.items() if v in ('GARDER', 'SIMPLIFIER')}


def table_geomods():
    registre = {l.split()[-1] for l in (ROOT / 'docs/perimetre/registre-geomods.txt').read_text(encoding='utf-8').splitlines()
                if l.strip()}
    table = {}
    for nom in sorted(registre):
        m = re.fullmatch(r'([a-z0-9]+)_(ingot|dust|plate|wire|block|ore|rod)', nom)
        if m:
            table[f'{FORMES[m[2]]}/{SYNONYMES.get(m[1], m[1])}'] = 'geomods:' + nom
    table.update({'ingots/desh': 'ad_astra:desh_ingot', 'nuggets/desh': 'ad_astra:desh_nugget',
                  'plates/desh': 'ad_astra:desh_plate', 'storage_blocks/desh': 'ad_astra:desh_block',
                  'ingots/ostrum': 'ad_astra:ostrum_ingot', 'plates/ostrum': 'ad_astra:ostrum_plate',
                  'storage_blocks/ostrum': 'ad_astra:ostrum_block'})
    return table


def ecrire_java(table):
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


def remplacer(v):
    if isinstance(v, dict):
        return {k: remplacer(w) for k, w in v.items()}
    if isinstance(v, list):
        return [remplacer(w) for w in v]
    if isinstance(v, str) and v.startswith('geontm:') and v[7:] in cibles:
        return cibles[v[7:]]
    return v


def analyser(v, sortie, ent, sor):
    """Remplit les entrées et sorties d'une recette."""
    if isinstance(v, dict):
        if 'material' in v and 'shape' in v and not sortie:
            ent.add(('forme', v['shape'], v['material']))
        for k, w in v.items():
            if k != 'type' and not k.endswith('_expensive'):
                analyser(w, sortie or bool(CLES_SORTIE.search(k)), ent, sor)
    elif isinstance(v, list):
        for w in v:
            analyser(w, sortie, ent, sor)
    elif isinstance(v, str):
        if v.startswith('geontm:') and v[7:] in decision:
            (sor if sortie else ent).add(v[7:])
        elif not sortie and v.startswith('#c:') and '/' in v:
            forme, matiere = v[3:].split('/', 1)
            ent.add(('tag', forme, matiere.replace('any_', '')))


def objets_de(entree, geomods):
    if isinstance(entree, str):
        return [entree]
    genre, forme, matiere = entree
    if genre == 'tag':
        if f'{forme}/{matiere}' in geomods:
            return []
        prefixe = PREFIXES.get(forme)
    else:
        prefixe = forme
        pluriel = {v: k for k, v in PREFIXES.items()}.get(forme)
        if pluriel and f'{pluriel}/{matiere}' in geomods:
            return []
    nom = f'{prefixe}_{matiere}' if prefixe else ''
    return [nom] if nom in decision else []


ESPACE = ('desh', 'saturnite', 'starmetal', 'schrabidium')
FORMES_ESPACE = re.compile(r'(ingot|nugget|powder|plate|plate_cast|plate_welded|billet|crystal|block|wire|shell|pipe)_(' + '|'.join(ESPACE) + ')$')


def fabrique_matiere_espace(r):
    """La recette crée une matière rare sans partir de cette matière : elle doit venir de l'espace."""
    entrees = json.dumps(sorted(map(str, r['ent'])))
    for s in r['sor']:
        m = FORMES_ESPACE.fullmatch(s)
        if m and m[2] not in entrees:
            return True
    return False


def main():
    geomods = table_geomods()
    ecrire_java(geomods)
    recettes = []
    for p in sorted(RECETTES.rglob('*.json')):
        rel = p.relative_to(RECETTES)
        j = json.loads(p.read_text(encoding='utf-8'))
        ent, sor = set(), set()
        analyser(j, False, ent, sor)
        table = rel.parts[0] if len(rel.parts) > 1 else ''
        variante = '.enable_' in p.name
        recettes.append({'p': p, 'j': j, 'table': table, 'ent': ent, 'sor': sor, 'variante': variante})

    producteurs = collections.defaultdict(list)
    for r in recettes:
        if r['table'] not in TABLES_RETIREES and not r['variante']:
            for s in r['sor']:
                producteurs[s].append(r)

    def cout(r):
        items = [o for e in r['ent'] for o in objets_de(e, geomods)]
        return (sum(decision.get(o) == 'RETIRER' for o in items), len(items), r['p'].name)

    necessaires, file, choisies = set(racines), collections.deque(sorted(racines)), set()
    while file:
        o = file.popleft()
        if not producteurs.get(o):
            continue
        r = min(producteurs[o], key=cout)
        choisies.add(id(r))
        for e in r['ent']:
            for x in objets_de(e, geomods):
                if x not in necessaires:
                    necessaires.add(x)
                    file.append(x)

    connues = set(DEPENDANCES.read_text(encoding='utf-8').split()) if DEPENDANCES.exists() else set()
    dependances = sorted(connues | {o for o in necessaires if decision.get(o) == 'RETIRER'})
    if any(r['table'].startswith('anvil_') and id(r) in choisies for r in recettes):
        dependances = sorted(set(dependances) | {o for o, d in decision.items() if o.startswith('anvil_') and d == 'RETIRER'})
    for o in dependances:
        decision[o] = 'SI_BESOIN'
    retire = {k for k, v in decision.items() if v == 'RETIRER'}

    gardees, retirees, adaptees = collections.Counter(), collections.Counter(), 0
    for r in recettes:
        items = {o for e in r['ent'] for o in objets_de(e, geomods)} | r['sor']
        garder = id(r) in choisies or (r['table'] not in TABLES_RETIREES and not r['variante'] and not (items & retire))
        garder = garder and not fabrique_matiere_espace(r)
        if not garder:
            r['p'].unlink()
            retirees[r['table']] += 1
            continue
        nouveau = remplacer(r['j'])
        if nouveau != r['j']:
            adaptees += 1
            r['p'].write_text(json.dumps(nouveau, indent=2, ensure_ascii=False) + '\n', encoding='utf-8', newline='\n')
        gardees[r['table']] += 1
    for d in sorted(RECETTES.rglob('*'), reverse=True):
        if d.is_dir() and not any(d.iterdir()):
            d.rmdir()

    manifeste = [l for l in (DATA / 'manifest.txt').read_text(encoding='utf-8').splitlines() if not l.startswith('geontm/recipe')]
    manifeste += [f'geontm/recipe/{t} {n}' if t else f'geontm/recipe {n}' for t, n in gardees.items()]
    (DATA / 'manifest.txt').write_text('\n'.join(sorted(manifeste)) + '\n', encoding='utf-8', newline='\n')

    DEPENDANCES.write_text('\n'.join(dependances) + '\n', encoding='utf-8', newline='\n')
    for x in lignes:
        if x['id'] in dependances:
            x.update(decision='SI_BESOIN', theme='dépendances', raison='nécessaire à une recette gardée')
    with CSV.open('w', encoding='utf-8', newline='') as f:
        w = csv.DictWriter(f, fieldnames=list(lignes[0]), lineterminator='\n')
        w.writeheader()
        w.writerows(lignes)
    (RESSOURCES / 'retire.txt').write_text('\n'.join(sorted(retire)) + '\n', encoding='utf-8', newline='\n')

    produits = {s for r in recettes if r['p'].exists() for s in r['sor']}
    sans = sorted(o for o in racines if o not in produits)
    machines = [o for o in sans if o.startswith('machine_')]
    t = ['# Recettes conservées', '', f'- Recettes gardées : **{sum(gardees.values())}**',
         f'- Recettes retirées : **{sum(retirees.values())}**', f'- Recettes adaptées à nos objets : **{adaptees}**',
         f'- Objets ajoutés car indispensables : **{len(dependances)}**',
         f'- Tags de matières reliés à GeoMods : **{len(geomods)}**',
         f'- Machines gardées sans recette dans les tables : **{len(machines)}**', '', '| Table | Recettes gardées |', '|---|---:|']
    t += [f'| `{k or "(racine)"}` | {v} |' for k, v in sorted(gardees.items())]
    (ROOT / 'docs/perimetre/recettes.md').write_text('\n'.join(t) + '\n', encoding='utf-8', newline='\n')
    print('gardées', sum(gardees.values()), 'retirées', sum(retirees.values()), 'adaptées', adaptees, 'dépendances', len(dependances))
    print('machines sans recette', len(machines), machines[:50])
    print('autres gardés sans recette', len(sans) - len(machines))
    print('tables', dict(sorted(gardees.items())))


if __name__ == '__main__':
    main()
