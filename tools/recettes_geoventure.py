#!/usr/bin/env python3
"""Écrit les recettes de datapack ajoutées par Geoventure (préfixe de fichier `geoventure_`) et met à jour le manifeste.

Ces recettes comblent des manques de la réduction du périmètre : contenu gardé qu'aucune recette ne produisait
(voir docs/perimetre/obtention.md). Elles sont dans des fichiers à part pour rester compatibles avec les mises à jour de
NTM-Continuum : aucun fichier d'origine n'est modifié.

Usage : python tools/recettes_geoventure.py
Relancer ensuite tools/schemas.py (le noyau de fusion appartient à la famille `discover.fusion`).
"""
import json
from pathlib import Path

RACINE = Path(__file__).resolve().parent.parent
DONNEES = RACINE / 'src/generated/data'
RECETTES = DONNEES / 'pack/data/geontm/recipe'

# Débris de plastique : 1.7 les obtenait en broyant des figurines (ShredderRecipes, bobbleheads), objets de décor qui
# ne sont pas dans le périmètre. L'assembleur les fabrique à la place, à partir de plastique polymère ; le cristalliseur
# (recettes d'origine « scrap_plastic_*_peroxide ») en fait les pièces du circuit étoile.
DEBRIS = ['board_blank', 'board_transistor', 'board_converter', 'bridge_north', 'bridge_south', 'bridge_io', 'bridge_bus',
          'bridge_chipset', 'bridge_cmos', 'bridge_bios', 'cpu_register', 'cpu_clock', 'cpu_logic', 'cpu_cache', 'cpu_ext',
          'cpu_socket', 'mem_socket', 'mem_16k_a', 'mem_16k_b', 'mem_16k_c', 'mem_16k_d', 'card_board', 'card_processor']


def recettes():
    for i, debris in enumerate(DEBRIS):
        yield 'assembly_machine', f'geoventure_scrap_plastic_{debris}', {
            'type': 'geontm:assembly_machine',
            'name': f'ass.geoventure_scrap_plastic_{debris}',
            'input_items': [{'ingredient': '#c:ingots/polymer', 'count': 2}],
            'output_items': [f'geontm:scrap_plastic_{debris}'],
            'duration': 60,
            'power': 100,
            'order': 90000 + i,
        }
    # 1.7 AssemblyMachineRecipes « ass.fusioncore » : le noyau de structure du réacteur à fusion (retiré avec le décor).
    yield 'assembly_machine', 'geoventure_fusioncore', {
        'type': 'geontm:assembly_machine',
        'name': 'ass.fusioncore',
        'input_items': [
            {'ingredient': '#c:plates_welded/any_resistant_alloy', 'count': 8},
            {'ingredient': '#c:ingots/any_hardplastic', 'count': 32},
            {'ingredient': 'geontm:circuit_bismoid', 'count': 8},
            {'ingredient': 'geontm:drive_flash_particlesim', 'count': 4},
        ],
        'output_items': ['geontm:struct_torus_core'],
        'duration': 600,
        'power': 100,
        'blueprint_pools': ['discover.fusion'],
        'order': 47104,
    }
    # Conversions en jeu (NTMToolHandler) : les structures du Watz et du tore n'acceptent que les blocs transformés.
    yield 'tool_conversion', 'geoventure_watz_casing', {
        'type': 'geontm:tool_conversion', 'tool': 'bolt',
        'from': 'geontm:watz_casing', 'to': 'geontm:watz_casing_tooled',
        'materials': [{'material': 'dura_steel', 'shape': 'bolt', 'count': 4}],
    }
    yield 'tool_conversion', 'geoventure_fusion_component', {
        'type': 'geontm:tool_conversion', 'tool': 'torch',
        'from': 'geontm:fusion_component', 'to': 'geontm:fusion_component_bscco_welded',
        'materials': [{'material': 'steel', 'shape': 'castplate'}],
    }
    # Les deux conversions de l'ICF (écarté, objets cachés) : la table d'origine reste entière, comme le vérifie le test
    # addon_content_tool_conversions (quatre conversions de NTMToolHandler).
    yield 'tool_conversion', 'geoventure_icf_component_structure', {
        'type': 'geontm:tool_conversion', 'tool': 'bolt',
        'from': 'geontm:icf_component_structure', 'to': 'geontm:icf_component_structure_bolted',
        'materials': [{'material': 'steel', 'shape': 'castplate'}, {'material': 'dura_steel', 'shape': 'bolt', 'count': 4}],
    }
    yield 'tool_conversion', 'geoventure_icf_component_vessel', {
        'type': 'geontm:tool_conversion', 'tool': 'torch',
        'from': 'geontm:icf_component_vessel', 'to': 'geontm:icf_component_vessel_welded',
        'materials': [{'ingredient': '#c:plates_cast/any_bismoid_bronze'}],
    }


def main():
    ecrites = []
    for table, nom, contenu in recettes():
        chemin = RECETTES / table / f'{nom}.json'
        chemin.parent.mkdir(parents=True, exist_ok=True)
        chemin.write_text(json.dumps(contenu, ensure_ascii=False, indent=2) + '\n', encoding='utf-8', newline='\n')
        ecrites.append(table)
    manifeste = DONNEES / 'manifest.txt'
    lignes = {l.split(' ')[0]: l for l in manifeste.read_text(encoding='utf-8').splitlines() if l.strip()}
    for table in sorted(set(ecrites)):
        n = sum(1 for _ in (RECETTES / table).rglob('*.json'))
        lignes[f'geontm/recipe/{table}'] = f'geontm/recipe/{table} {n}'
    manifeste.write_text('\n'.join(lignes[k] for k in sorted(lignes)) + '\n', encoding='utf-8', newline='\n')
    print(len(ecrites), 'recettes écrites dans', len(set(ecrites)), 'tables')


if __name__ == '__main__':
    main()
