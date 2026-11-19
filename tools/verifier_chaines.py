#!/usr/bin/env python3
"""Vérifie que les chaînes de production de GeoNTM tiennent avec le contenu visible.

1. Pour chaque dossier de recettes de machine, quelles machines visibles le font tourner.
2. Les fluides : un fluide utilisé par une recette active doit être produit par une recette active, ou venir d'une
   source naturelle connue (eau, lave, vapeur des chaudières, fluides de base listés plus bas).
3. Les missiles et leurs pièces : chaque objet GeoNTM demandé doit être produit par une recette active, par une recette
   d'établi, ou être visible.

« Active » = la recette appartient à un dossier qu'au moins une machine visible fait tourner. Le rapport ne prouve pas
qu'un objet s'obtient en jeu : il signale ce qu'il faut regarder.

Usage : python tools/verifier_chaines.py
"""
import json
import re
from collections import defaultdict
from pathlib import Path

RACINE = Path(__file__).resolve().parent.parent
RECETTES = RACINE / 'src/generated/data/pack/data/geontm/recipe'
PERIMETRE = RACINE / 'src/main/resources/geontm/perimetre'
JAVA = RACINE / 'src/main/java/fr/geoventure/geontm'
SORTIE = RACINE / 'docs/perimetre/chaines.md'

# Dossier de recettes -> machines qui le font tourner (ids sans « geontm: »).
MACHINES = {
    'anvil_construction': ['anvil_iron', 'anvil_lead', 'anvil_steel', 'anvil_desh', 'anvil_ferrouranium',
                           'anvil_saturnite', 'anvil_bismuth_bronze', 'anvil_arsenic_bronze', 'anvil_schrabidate',
                           'anvil_dnt', 'anvil_osmiridium', 'anvil_murky'],
    'anvil_smithing': ['anvil_iron', 'anvil_lead', 'anvil_steel', 'anvil_desh'],
    'arc_furnace': ['machine_arc_furnace'], 'arc_welder': ['machine_arc_welder'],
    'assembly_machine': ['machine_assembly_machine', 'machine_assembly_factory'],
    'blast_furnace': ['machine_difurnace_off', 'machine_difurnace_rtg_off'], 'breeder': ['machine_reactor_breeding'],
    'centrifuge': ['machine_centrifuge'], 'chemical_plant': ['machine_chemical_plant', 'machine_chemical_factory'],
    'coker': ['machine_coker'], 'combination': ['furnace_combination'], 'compressor': ['machine_compressor',
                                                                                   'machine_compressor_compact'],
    'cracking': ['machine_catalytic_cracker'], 'crucible': ['machine_crucible'], 'crystallizer': ['machine_crystallizer'],
    'cyclotron': ['machine_cyclotron'], 'difurnace': ['machine_difurnace_off'],
    'electrolyser_fluid': ['machine_electrolyser'], 'electrolyser_metal': ['machine_electrolyser'],
    'exposure_chamber': ['machine_exposure_chamber'], 'fluid_breeder': ['machine_reactor_breeding'],
    'fraction': ['machine_fraction_tower'], 'fusion': ['fusion_torus', 'fusion_core'],
    'gas_centrifuge': ['machine_gascent'], 'hydrotreating': ['machine_hydrotreater'],
    'liquefaction': ['machine_liquefactor'], 'mixer': ['machine_mixer'], 'outgasser': ['rbmk_outgasser'],
    'precision_assembler': ['machine_precass'], 'press': ['machine_press', 'machine_epress'],
    'purex': ['machine_purex'], 'pyrolysis': ['machine_pyrooven'], 'radiolysis': ['machine_radiolysis'],
    'refinery': ['machine_refinery'], 'reforming': ['machine_catalytic_reformer'], 'rock_mill': ['machine_rock_mill'],
    'rotary_furnace': ['machine_rotary_furnace'], 'sawmill': ['machine_sawmill'], 'shredder': ['machine_shredder'],
    'silex': ['machine_silex'], 'soldering': ['machine_soldering_station'], 'solidification': ['machine_solidifier'],
    'space_assembler': ['machine_space_assembler'], 'storage_drum': ['machine_storage_drum'],
    'supercomputer': ['machine_supercomputer'], 'vacuum_distill': ['machine_vacuum_distill'],
}
# Fluides présents sans recette : sources naturelles ou machines sans recette JSON (chaudières, pompes, condenseurs).
SOURCES = {'minecraft:water', 'minecraft:lava', 'geontm:water', 'geontm:lava', 'geontm:steam', 'geontm:hotsteam',
           'geontm:superhotsteam', 'geontm:ultrahotsteam', 'geontm:spentsteam', 'geontm:coolant',
           'geontm:coolant_hot', 'geontm:air', 'geontm:none',
           # Chauffés dans la chaudière industrielle par les réchauffeurs, sans recette JSON.
           'geontm:hotoil', 'geontm:hotoil_ds', 'geontm:hotcrackoil', 'geontm:hotcrackoil_ds',
           # Extracteur et tour de deutérium.
           'geontm:heavywater', 'geontm:deuterium'}


def lire(nom):
    return set((PERIMETRE / nom).read_text(encoding='utf-8').split())


def fluides(liste):
    for f in liste or []:
        if isinstance(f, dict) and f.get('type'):
            yield f['type']


# Dossiers dont la clé « input » est un fluide et non un objet.
ENTREE_FLUIDE = {'refinery', 'fraction', 'cracking', 'reforming', 'hydrotreating', 'vacuum_distill', 'coker',
                 'electrolyser_fluid', 'solidification', 'pseudo_fluid'}


def fluides_sortis(r):
    """Tout {type, amount} hors des entrées, plus les sous-produits nommés par chaîne."""
    for cle, valeur in r.items():
        if cle.startswith('input') or cle in ('hydrogen', 'fluid'):
            continue
        if cle == 'byproduct_type' and isinstance(valeur, str):
            yield valeur
        pile = [valeur]
        while pile:
            v = pile.pop()
            if isinstance(v, dict):
                if isinstance(v.get('type'), str) and 'amount' in v:
                    yield v['type']
                pile.extend(v.values())
            elif isinstance(v, list):
                pile.extend(v)


def fluides_entres(dossier, r):
    yield from fluides(r.get('input_fluids'))
    for cle in ('hydrogen', 'fluid'):
        if isinstance(r.get(cle), dict) and r[cle].get('type'):
            yield r[cle]['type']
    if dossier in ENTREE_FLUIDE and isinstance(r.get('input'), str):
        yield r['input']


def objets(valeur):
    texte = json.dumps(valeur)
    return set(re.findall(r'"(?:item|ingredient|id)"\s*:\s*"geontm:([a-z0-9_]+)"', texte)) | \
        set(re.findall(r'"geontm:([a-z0-9_]+)"', texte))


def main():
    visibles = (lire('creatif.txt') | lire('ajoute.txt')) - lire('simplifie.txt')
    actifs = {d: [m for m in ms if m in visibles] for d, ms in MACHINES.items()}
    recettes = []
    for p in RECETTES.rglob('*.json'):
        dossier = p.relative_to(RECETTES).parts[0]
        recettes.append((dossier, p.stem, json.loads(p.read_text(encoding='utf-8'))))
    inconnus = sorted({d for d, _, _ in recettes if d not in MACHINES})

    produits_fluides, demandes_fluides = defaultdict(set), defaultdict(set)
    produits_objets = set()
    for dossier, nom, r in recettes:
        actif = bool(actifs.get(dossier))
        sorties = {k: v for k, v in r.items() if k.startswith('output') or k == 'result'}
        entrees = {k: v for k, v in r.items() if k.startswith('input') and not k.endswith('expensive')}
        if actif:
            for f in fluides_sortis(r):
                produits_fluides[f].add(f'{dossier}/{nom}')
            produits_objets |= objets(sorties)
            for f in fluides_entres(dossier, r):
                demandes_fluides[f].add(f'{dossier}/{nom}')

    # Recettes d'établi déclarées en Java (dejson) : on retient les sorties nommées.
    for java in (JAVA / 'inventory/recipes').rglob('*.java'):
        texte = java.read_text(encoding='utf-8', errors='replace')
        produits_objets |= set(re.findall(r'(?:shaped|shapeless)\w*\(NS, "([a-z0-9_]+)"', texte))

    manquants = {f: sorted(d)[:4] for f, d in demandes_fluides.items()
                 if f not in produits_fluides and f not in SOURCES}

    # Missiles du catalogue et pièces : recettes qui produisent un missile, et leurs entrées GeoNTM.
    besoins = defaultdict(set)
    for dossier, nom, r in recettes:
        sorties = objets({k: v for k, v in r.items() if k.startswith('output') or k == 'result'})
        if any(s.startswith('missile_') for s in sorties) and actifs.get(dossier):
            for e in objets({k: v for k, v in r.items() if k.startswith('input') or k == 'ingredients'}):
                besoins[e].add(f'{dossier}/{nom}')
    pieces_manquantes = {o: sorted(d)[:3] for o, d in besoins.items()
                         if o not in produits_objets and o not in visibles}

    lignes = ['# Chaînes de production de GeoNTM', '', 'Fichier généré par `tools/verifier_chaines.py`.', '',
              '## Machines par dossier de recettes', '', '| Dossier | Machines visibles |', '|---|---|']
    lignes += [f'| {d} | {", ".join(m) if m else "**aucune**"} |' for d, m in sorted(actifs.items())]
    if inconnus:
        lignes += ['', 'Dossiers sans correspondance connue : ' + ', '.join(inconnus)]
    lignes += ['', '## Fluides demandés sans producteur actif', '']
    lignes += [f'- `{f}` : demandé par {", ".join(d)}' for f, d in sorted(manquants.items())] or ['- aucun']
    lignes += ['', '## Pièces de missiles introuvables', '']
    lignes += [f'- `geontm:{o}` : pour {", ".join(d)}' for o, d in sorted(pieces_manquantes.items())] or ['- aucune']
    SORTIE.write_text('\n'.join(lignes) + '\n', encoding='utf-8', newline='\n')
    print('dossiers sans machine visible :', sorted(d for d, m in actifs.items() if not m))
    print('fluides sans producteur :', len(manquants), sorted(manquants)[:40])
    print('pièces de missiles introuvables :', sorted(pieces_manquantes))


if __name__ == '__main__':
    main()
