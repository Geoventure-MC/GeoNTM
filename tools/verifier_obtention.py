#!/usr/bin/env python3
"""Ce qu'un joueur peut réellement obtenir en survie dans un jar GeoNTM : point fixe sur toutes les recettes du jar
(sauf retirées), en exigeant que la machine de chaque recette soit elle-même obtenable.

Départ : objets vanilla, tous les tags, objets des autres mods (geomods:, ad_astra:...), eau et lave, blocs de minerai
(worldgen du jar), butin (coffres, mobs) et drops des blocs obtenus.
Les conditions de recette (`requires`, `geocore:requires`) sont évaluées avec les réglages PAR DÉFAUT du mod
(528 désactivé, mode coûteux désactivé, LBSM désactivé) ; un drapeau inconnu rend la recette inactive, comme dans le jeu.

Usage :
    python tools/verifier_obtention.py <jar> [préfixe ...] [--md fichier.md] [--explique id ...]
Variables d'environnement : AUTRES_JARS (jars séparés par « ; », ex. GeoMods : ses tags et objets comptent comme fournis),
FLAGS (ex. « enable_528=1;expensive_mode=1 » pour changer un réglage).
TERRE=1 : mode « ressources planétaires » (une ressource unique par planète, absente de la Terre : desh, ostrum/saturnite, calorite,
schrabidium, starmetal). Les minerais de ces matières ne comptent plus comme du monde, les objets ad_astra:* de desh/ostrum/calorite
ne sont plus fournis, et le rapport liste tout objet de ces matières encore obtenable (= fuite terrestre) avec son origine.
Sans préfixe, tous les objets visibles sont vérifiés. --md écrit le rapport final (docs/perimetre/obtention.md).
--explique id : chaîne de blocage d'un objet (recettes, ingrédients manquants, causes premières).
--retraits fichier.json : écrit les recettes qui exigent un fluide sans producteur obtenable (format recipe_removals) ;
--retraits-caches fichier.json (avec --md) : écrit les recettes qui exigent un objet caché de l'audit pour fabriquer un objet visible.
USAGES=1 : pour chaque objet bloqué, les objets visibles qu'il sert à fabriquer.
Le classement des objets qu'aucune recette ne produisait (A fabricable, B caché, C hors recette) est lu dans
docs/perimetre/obtention-audit.tsv ; tools/simplifier.py en tire la liste des objets cachés (simplifie.txt).
Limites : un tag vide ou inconnu est supposé fourni par le pack ; les fluides d'autres mods aussi ; les recettes
construites en Java sans fichier JSON dans le jar ne sont pas vues ; le butin compte comme obtenable (il faut
l'avoir trouvé) mais il est listé à part dans le rapport.
"""
import collections
import json
import os
import re
import sys
import zipfile

sys.stdout.reconfigure(encoding='utf-8')
args = sys.argv[1:]
jar = zipfile.ZipFile(args[0])
rest = args[1:]
MD = None
LISTE_OBTENUS = False
EXPLIQUE = []
RETRAITS = None
RETRAITS_CACHES = None
PREFIXES = []
i = 0
while i < len(rest):
    if rest[i] == '--md':
        MD = rest[i + 1]
        i += 2
    elif rest[i] == '--obtenus':  # liste les objets visibles obtenus, un par ligne, puis s'arrête (sert au calcul « sans butin »)
        LISTE_OBTENUS = True
        i += 1
    elif rest[i] == '--retraits-caches':
        RETRAITS_CACHES = rest[i + 1]
        i += 2
    elif rest[i] == '--retraits':
        RETRAITS = rest[i + 1]
        i += 2
    elif rest[i] == '--explique':
        i += 1
        while i < len(rest) and not rest[i].startswith('--'):
            EXPLIQUE.append(rest[i])
            i += 1
    else:
        PREFIXES.append(rest[i])
        i += 1

noms = jar.namelist()
P = 'geontm/perimetre/'
lst = lambda f: set(jar.read(P + f).decode().split()) if P + f in noms else set()
MASQUE = lst('simplifie.txt')  # états intermédiaires masqués du créatif et de JEI (plus les armes extrêmes et les objets écartés)
# Un fluide et un objet peuvent porter le même nom (watz, concrete, balefire) : sans distinction, le fluide « watz » des
# recyclages passait pour le bloc du réacteur Watz. Pour ces noms, le fluide s'écrit « fluid:<nom> » dans le point fixe.
COLLISIONS = ({n.split('/')[-1][:-5] for n in noms if n.startswith('data/geontm/fluid_property/') and n.endswith('.json')}
              & ({n[len('assets/geontm/items/'):-5] for n in noms if n.startswith('assets/geontm/items/') and n.endswith('.json')}
                 | {n[len('assets/geontm/blockstates/'):-5] for n in noms if n.startswith('assets/geontm/blockstates/') and n.endswith('.json')}))

# recettes retirées au chargement (recipe_removals) : par fichier, pour pouvoir nommer les volontaires
retirees, volontaires = set(), set()
for n in noms:
    if 'recipe_removals' in n and n.endswith('.json'):
        if (RETRAITS or RETRAITS_CACHES) and n.endswith(('geoventure_sans_producteur.json', 'geoventure_objets_caches.json')):
            continue  # on recalcule ces deux listes : leur état précédent ne doit pas les influencer
        ids = json.loads(jar.read(n))['recipes']
        retirees |= set(ids)
        if n.endswith(('missiles_extremes.json', 'ecartes.json')):
            volontaires |= {r.split(':')[-1].split('/')[-1] for r in ids}

# objets proposés aux joueurs ; les masqués ne sont pas des cibles (sauf les volontairement sans recette, listés à part)
VISIBLE = (lst('creatif.txt') | lst('ajoute.txt')) - (MASQUE - volontaires)

# réglages par défaut du mod (GeneralConfig) ; FLAGS=nom=1;nom=0 pour les changer
FLAGS = collections.defaultdict(bool)
for kv in filter(None, os.environ.get('FLAGS', '').split(';')):
    k, _, v = kv.partition('=')
    FLAGS[k] = v not in ('', '0', 'false')
GEO = re.compile(r'geontm:([a-z0-9_]+)')
ID = re.compile(r'#?[a-z0-9_.-]+:[a-z0-9_/.-]+')
TERRE = bool(os.environ.get('TERRE'))
PLANETAIRE = re.compile(r'(desh|saturnite|ostrum|calorite|starmetal|schrabidium)')
PLANETAIRE_AD_ASTRA = re.compile(r'(desh|ostrum|calorite)')  # fournis par Ad Astra, donc pas sur Terre

# tags d'objets et de fluides : 'ns:chemin' -> membres (ids ou '#tag')
TAGS = collections.defaultdict(set)
AUTRES = [zipfile.ZipFile(p) for p in os.environ.get('AUTRES_JARS', '').split(';') if p]  # ex. le jar GeoMods
for z in [jar] + AUTRES:
    for n in z.namelist():
        m = re.match(r'data/([^/]+)/tags/(items|fluids)/(.+)\.json$', n)
        if m:
            try:
                for v in json.loads(z.read(n)).get('values', []):
                    TAGS[m.group(1) + ':' + m.group(3)].add(v if isinstance(v, str) else v.get('id', ''))
            except Exception:
                pass


def tag_non_vide(t, vus=None):
    vus = vus or set()
    if t in vus:
        return False
    vus.add(t)
    for m in TAGS.get(t, ()):
        if (m.startswith('#') and tag_non_vide(m[1:], vus)) or (m and not m.startswith('#')):
            return True
    return False


def conditions_ok(d):
    """Les conditions de la recette tiennent avec les réglages par défaut."""
    for k in ('requires', 'geocore:requires'):
        req = d.get(k)
        if req is None:
            continue
        for lit in [req] if isinstance(req, str) else req:
            neg = lit.startswith('!')
            nom = lit[1:] if neg else lit
            if nom.startswith('#') or ':' in nom:
                val = tag_non_vide(nom.lstrip('#'))
            else:
                val = FLAGS[nom]
            if val == neg:
                return False
    for c in d.get('conditions', []) or []:
        if c.get('type') == 'forge:mod_loaded':  # mods d'intégration absents du pack
            return False
    return True


# forme d'une pièce de matière -> famille de tags (convention du mod)
FORMES = {'castplate': 'plates_cast', 'weldedplate': 'plates_welded', 'pipe': 'pipes', 'shell': 'shells', 'wire': 'wires',
          'densewire': 'wires_dense', 'bolt': 'bolts'}
SYNONYMES = {'aluminium': 'aluminum'}
MATIERES_EN_ENTREE = False  # vrai le temps de lire un creuset : « material » sans forme y est une entrée


def exigences(o, out):
    """Remplit `out` d'exigences : chaque élément est un ensemble d'alternatives (une seule suffit)."""
    if isinstance(o, dict):
        if isinstance(o.get('items'), list):
            out.append(frozenset(v for v in o['items'] if isinstance(v, str)))
            return
        t = o.get('type')
        if isinstance(t, str) and 'amount' in o and t.startswith('geontm:') and t[7:] in COLLISIONS:  # fluide homonyme d'un objet
            out.append(frozenset({'geontm:fluid:' + t[7:]}))
            return
        if t == 'geontm:fluid_container' and isinstance(o.get('fluid'), str):  # n'importe quel récipient plein de ce fluide
            out.append(frozenset({o['fluid']}))
            return
        if t == 'geontm:any_of' and isinstance(o.get('of'), list):  # une des alternatives suffit
            alt = set()
            for sous in o['of']:
                tmp = []
                exigences(sous, tmp)
                for g in tmp:
                    alt |= g
            out.append(frozenset(alt))
            return
        if isinstance(t, str) and ('item' in o or 'ingredient' in o):  # « type » qualifie l'objet (cold, stirling, fluid_item)
            o = {k: v for k, v in o.items() if k != 'type'}
        if 'shape' in o and 'material' in o and o['shape'] in FORMES:
            m = SYNONYMES.get(o['material'], o['material'])
            out.append(frozenset({f'#c:{FORMES[o["shape"]]}/{m}'}))
            return
        if isinstance(o.get('material'), str) and 'shape' not in o and MATIERES_EN_ENTREE:
            out.append(frozenset({'mat:' + o['material']}))
            return
        for k, v in o.items():
            if k in ('item_data', 'icon', 'name_wrapper', 'blueprint_pools'):
                continue
            if isinstance(v, str) and ID.fullmatch(v):
                if k == 'tag' or v.startswith('#'):
                    out.append(frozenset({'#' + v.lstrip('#')}))
                elif k in ('item', 'id', 'fluid', 'type', 'ingredient', 'stamp', 'input', 'solder', 'pcb', 'hot') or v.startswith('geontm:'):
                    out.append(frozenset({v}))
            else:
                exigences(v, out)
    elif isinstance(o, list):
        for v in o:
            exigences(v, out)
    elif isinstance(o, str) and ID.fullmatch(o):
        out.append(frozenset({o}))


# dossiers des recettes que l'outil déduit du code Java (ni fichier ni identifiant de recette réel)
SYNTHETIQUES = {'java', 'reacteur', 'construction', 'fonderie', 'enclume', 'chaudiere', 'extracteur'}
CLES_FLUIDES = {'output_fluids', 'output_fluid', 'fluid_output', 'fluid_produced', 'molten_outputs', 'molten', 'byproduct_type'}


def sorties(o, out, fluide=False):
    """Ids geontm produits (objets et fluides) ; les NBT (`tag`, `item_data`) ne produisent rien."""
    if isinstance(o, dict):
        if isinstance(o.get('material'), str) and 'shape' not in o:
            out.add('mat:' + o['material'])
        for k, v in o.items():
            if k in ('tag', 'item_data', 'nbt', 'components'):
                continue
            sorties(v, out, fluide or k in CLES_FLUIDES)
    elif isinstance(o, list):
        for v in o:
            sorties(v, out, fluide)
    elif isinstance(o, str):
        out.update(('fluid:' + n if fluide and n in COLLISIONS else n) for n in GEO.findall(o))


# machines par dossier de recettes : un dossier absent = recette d'établi (toujours possible) ;
# une liste vide = fusionné avec une machine GeoMods (toujours possible)
MACHINES = {
    'anvil_construction': ['anvil_iron', 'anvil_lead', 'anvil_steel', 'anvil_desh', 'anvil_ferrouranium',
                           'anvil_saturnite', 'anvil_bismuth_bronze', 'anvil_arsenic_bronze', 'anvil_schrabidate',
                           'anvil_dnt', 'anvil_osmiridium', 'anvil_murky'],
    'anvil_smithing': ['anvil_iron', 'anvil_lead', 'anvil_steel', 'anvil_desh'],
    'arc_furnace': ['machine_arc_furnace'], 'arc_welder': ['machine_arc_welder'],
    'assembly_machine': ['machine_assembly_machine', 'machine_assembly_factory'], 'centrifuge': ['machine_centrifuge'],
    'chemical_plant': ['machine_chemical_plant', 'machine_chemical_factory'], 'coker': ['machine_coker'],
    'compressor': ['machine_compressor', 'machine_compressor_compact'], 'cracking': ['machine_catalytic_cracker'],
    'crucible': ['machine_crucible'], 'crystallizer': ['machine_crystallizer'], 'cyclotron': ['machine_cyclotron'],
    'electrolyser_fluid': ['machine_electrolyser'], 'electrolyser_metal': ['machine_electrolyser'],
    'exposure_chamber': ['machine_exposure_chamber'], 'fraction': ['machine_fraction_tower'], 'fusion': ['fusion_torus'],
    'gas_centrifuge': ['machine_gascent'], 'pseudo_fluid': ['machine_gascent'], 'hydrotreating': ['machine_hydrotreater'],
    'liquefaction': ['machine_liquefactor'], 'mixer': ['machine_mixer'], 'outgasser': ['rbmk_outgasser'],
    'precision_assembler': ['machine_precass'], 'press': ['machine_press', 'machine_epress'], 'purex': ['machine_purex'],
    'pyrolysis': ['machine_pyrooven'], 'radiolysis': ['machine_radiolysis'], 'refinery': ['machine_refinery'],
    'reforming': ['machine_catalytic_reformer'], 'rotary_furnace': ['machine_rotary_furnace'],
    'shredder': ['machine_shredder'], 'silex': ['machine_silex'], 'soldering': ['machine_soldering_station'],
    'solidification': ['machine_solidifier'], 'storage_drum': ['machine_storage_drum'],
    'supercomputer': ['machine_supercomputer'], 'vacuum_distill': ['machine_vacuum_distill'], 'blast_furnace': ['machine_blast_furnace'],
    'breeder': ['machine_reactor'], 'combination': ['machine_furnace_combination'], 'difurnace': ['machine_difurnace_off'],
    'fluid_breeder': ['fusion_breeder'], 'rock_mill': ['machine_rockmill'], 'sawmill': ['machine_sawmill'],
    'space_assembler': ['satellite_science_assembler'],
    'smelting': [], 'blasting': [], 'smoking': [], 'campfire_cooking': [], 'material_distribution': [],
    'tool_conversion': [],
}

CLES_SORTIE = {'result', 'results', 'output', 'outputs', 'output_items', 'output_fluids', 'output_item', 'output_fluid',
               'output1', 'output2', 'byproduct', 'byproducts', 'solid_output', 'molten_outputs', 'molten', 'item_output',
               'fluid_output', 'fluid_produced', 'produced', 'steam', 'crafting_result', 'byproduct_type'}  # byproduct_type : fluide du four à coke
CLES_NEUTRES = {'type', 'name', 'group', 'requires', 'geocore:requires', 'conditions', 'category', 'icon', 'overlay', 'hidden',
                'name_wrapper', 'custom_localization', 'blueprint_pools', 'blueprint_pools_528', 'auto_switch_group', 'next',
                'from', 'byproduct_type'}
recettes = []  # (id, dossier, machines|None, sorties, exigences)
SANS_SORTIE = []  # (id, dossier, exigences) des recettes dont aucune sortie n'est un objet GeoNTM
actives = 0
for n in noms:
    if not (n.startswith('data/geontm/recipes/') and n.endswith('.json')):
        continue
    rid = n[len('data/geontm/recipes/'):-5]
    if 'geontm:' + rid in retirees or 'geontm:' + rid.split('/')[-1] in retirees:
        continue
    try:
        d = json.loads(jar.read(n))
    except Exception:
        continue
    if not conditions_ok(d):
        continue
    dossier = rid.split('/')[0] if '/' in rid else ''
    # clés de sortie d'une recette (relevées sur tous les types) ; left/right sortent des fractionnements, pas de l'enclume
    cles_sortie = set(CLES_SORTIE) | ({'left', 'right'} if dossier in ('cracking', 'fraction', 'radiolysis') else set())
    sortie = {k: d[k] for k in d if k in cles_sortie}
    # *_expensive : variante du « mode coûteux » de NTM, inactive par défaut
    entree = {k: d[k] for k in d if k not in sortie and k not in CLES_NEUTRES and 'expensive' not in k}
    outs = set()
    sorties(sortie, outs)
    ing = []
    if dossier == 'crucible':
        MATIERES_EN_ENTREE = True
    exigences(entree, ing)
    MATIERES_EN_ENTREE = False
    if dossier == 'material_distribution':  # un objet fondu dans le creuset donne ces matières
        outs = {'mat:' + m['material'] for m in d.get('materials', []) if isinstance(m, dict) and 'material' in m}
        ing = [frozenset({d['input']})] if isinstance(d.get('input'), str) else []
    if dossier == 'tool_conversion':  # bloc transformé en jeu à l'outil (boulonneur, chalumeau) avec ces matériaux
        outs = set(GEO.findall(d.get('to', '')))
        ing = [frozenset({d['from']})] if isinstance(d.get('from'), str) else []
        exigences(d.get('materials', []), ing)
    if not outs:
        SANS_SORTIE.append((rid, dossier, ing))  # sortie vanilla seulement : rien à obtenir, mais ses entrées comptent (fluides)
        continue
    mach = MACHINES.get(dossier, None) if dossier else []
    if dossier == 'material_distribution':
        mach = ['machine_crucible']
    recettes.append((rid, dossier, mach, outs, ing))

# --- comportements codés en Java, sans fichier de recette ------------------------------------------------------------
# Fonderie : une matière fondue (creuset) coulée dans un bassin ou un moule avec le moule de la forme donne la pièce
# `<préfixe>_<matière>` ; les moules se forgent à l'enclume avec un moule vierge (AnvilSmithingMold).
CONNUS = {n[len('assets/geontm/items/'):-5] for n in noms if n.startswith('assets/geontm/items/') and n.endswith('.json')}
CONNUS |= {n[len('assets/geontm/blockstates/'):-5] for n in noms if n.startswith('assets/geontm/blockstates/') and n.endswith('.json')}
COULEES = {'nugget': 'mold_nugget', 'billet': 'mold_billet', 'ingot': 'mold_ingot', 'plate': 'mold_plate',
           'plate_cast': 'mold_plate_cast', 'wire': 'mold_wire', 'shell': 'mold_shell', 'pipe': 'mold_pipe',
           'block': 'mold_block', 'wire_dense': 'mold_wire_dense'}
FORMES_MATIERE = ['plate_welded', 'plate_cast', 'wire_dense', 'heavy_component', 'nugget', 'billet', 'ingot', 'plate',
                  'wire', 'shell', 'pipe', 'bolt', 'block', 'powder', 'fragment']
MATIERES = set()
MATIERE_DE = {}  # objet -> matière dont il est une forme
for c in CONNUS:
    for f in FORMES_MATIERE:
        if c.startswith(f + '_') and len(c) > len(f) + 1:
            MATIERES.add(c[len(f) + 1:])
            MATIERE_DE[c] = c[len(f) + 1:]
            break
for matiere in sorted(MATIERES):
    for forme, moule in COULEES.items():
        cible = f'{forme}_{matiere}'
        if cible in CONNUS and moule in CONNUS:
            recettes.append((f'fonderie/{cible}', 'fonderie', ['machine_crucible'], {cible},
                             [frozenset({'mat:' + matiere}), frozenset({'geontm:' + moule}),
                              frozenset({'geontm:foundry_mold', 'geontm:foundry_basin'})]))
for c in sorted(CONNUS):
    if c.startswith('mold_') and c != 'mold_base':
        recettes.append((f'enclume/{c}', 'enclume', MACHINES['anvil_smithing'], {c}, [frozenset({'geontm:mold_base'})]))

def matieres_des_tags():
    """Matières fournies par des objets hors GeoNTM (vanilla, GeoMods, ...) via les tags c:<forme>/<matière>."""
    res = set()
    for t, membres in TAGS.items():
        m = re.match(r'(?:c|forge):(ingots|nuggets|plates|dusts|storage_blocks|wires|raw_materials|gems)/([a-z0-9_]+)$', t)
        if m and TERRE and PLANETAIRE.search(m.group(2)):
            continue  # desh, ostrum, calorite : fournis par Ad Astra, donc pas sur Terre
        if m and any(x and not x.startswith('#') and not x.startswith('geontm:') for x in membres):
            res.add(SYNONYMES.get(m.group(2), m.group(2)))
            res.add(m.group(2))
    return res


# Fluides sans recette JSON : eau, lave, vapeur et liquide de refroidissement viennent des chaudières, pompes et réacteurs ;
# l'huile chauffée vient de la chaudière industrielle avec ses réchauffeurs ; l'eau lourde et le deutérium, de l'extracteur.
# `oil` : le pétrole brut de la pompe GeoMods (geomods:crude_oil) est le même fluide dans les machines (ConventionFluidTags).
FLUIDES_DE_BASE = ['oil', 'water', 'lava', 'steam', 'hotsteam', 'superhotsteam', 'ultrahotsteam', 'spentsteam', 'coolant',
                   'coolant_hot', 'air', 'none']
for chaud, froid in (('hotoil', 'oil'), ('hotoil_ds', 'oil_ds'), ('hotcrackoil', 'crackoil'), ('hotcrackoil_ds', 'crackoil_ds')):
    recettes.append((f'chaudiere/{chaud}', 'chaudiere', ['machine_industrial_boiler'], {chaud}, [frozenset({'geontm:' + froid})]))
# Traits thermiques (data/geontm/fluid_property) : un fluide chauffé (chaudière, échangeur, cœur PWR) ou refroidi (échangeur) en
# donne un autre, sans recette. Ex. thorium_salt -> thorium_salt_hot (PWR) -> thorium_salt_depleted (échangeur), puis PUREX.
# ICF (écarté), PA et TURBINE : non modélisés ici.
CHAUFFE = {'BOILER': 'machine_industrial_boiler', 'HEATEXCHANGER': 'heater_heatex', 'PWR': 'pwr_controller'}
for n in sorted(noms):
    if n.startswith('data/geontm/fluid_property/') and n.endswith('.json'):
        froid, traits_ = n.split('/')[-1][:-5], json.loads(jar.read(n)).get('traits', {})
        paires = [(etape_['produces'], traits_['heatable']) for etape_ in traits_.get('heatable', {}).get('steps', [])]
        if traits_.get('coolable'):
            paires.append((traits_['coolable']['cools_to'], traits_['coolable']))
        for sortie_, trait_ in paires:
            for mode_ in trait_.get('efficiency', {}):
                if mode_ in CHAUFFE:
                    recettes.append((f'chaudiere/{sortie_[7:]}', 'chaudiere', [CHAUFFE[mode_]], {sortie_[7:]},
                                     [frozenset({'geontm:' + froid})]))
for lourd in ('heavywater', 'deuterium'):
    recettes.append((f'extracteur/{lourd}', 'extracteur', ['machine_deuterium_extractor'], {lourd}, [frozenset({'geontm:water'})]))

# Lames coulées (MoldMulti) : le moule de lames et la matière donnent la lame.
for c in sorted(CONNUS):
    for prefixe, moule in (('blade_', 'mold_blade'), ('blades_', 'mold_blades')):
        if c.startswith(prefixe) and moule in CONNUS:
            recettes.append((f'fonderie/{c}', 'fonderie', ['machine_crucible'], {c},
                             [frozenset({'mat:' + c[len(prefixe):]}), frozenset({'geontm:' + moule}),
                              frozenset({'geontm:foundry_mold', 'geontm:foundry_basin'})]))
# Autres comportements Java : un satellite scientifique en orbite (SatelliteScience) écrit les données de vol sur un disque vide.
recettes.append(('java/flightdata', 'java', [], {'drive_disk_flightdata'},
                 [frozenset({'geontm:satellite_science'}), frozenset({'geontm:drive_disk_empty'})]))
# Récipients de fluide (value_maps/item/fluid_container.json) : vidé dans une machine, un récipient plein donne son fluide
# (xpjuice vient d'iv_xp ou d'une fiole d'expérience, blood d'iv_blood, amat de particle_amat).
if 'data/geontm/value_maps/item/fluid_container.json' in noms:
    for objet_, info_ in json.loads(jar.read('data/geontm/value_maps/item/fluid_container.json')).get('values', {}).items():
        fluide_ = (info_.get('fluid') if isinstance(info_, dict) else info_) or ''
        if fluide_.startswith('geontm:'):
            nom_ = fluide_[7:]
            recettes.append((f'java/vider/{objet_}', 'java', [], {'fluid:' + nom_ if nom_ in COLLISIONS else nom_},
                             [frozenset({objet_})]))
# Utilisation d'un objet (ItemSimpleConsumable) : la poche vide utilisée sur soi donne la poche pleine (sang : 5 coeurs ;
# expérience : 100 points). Le dossier de schémas ouvert (ItemBlueprintFolder) donne un schéma au hasard parmi ceux que le pays
# a débloqués (famille discover : blueprint_folder_discover, alt : blueprint_folder).
for plein_, vide_ in (('iv_blood', 'iv_empty'), ('iv_xp', 'iv_xp_empty'), ('blueprints', 'blueprint_folder'),
                      ('blueprints', 'blueprint_folder_discover')):
    recettes.append((f'java/utiliser/{plein_}', 'java', [], {plein_}, [frozenset({'geontm:' + vide_})]))
# Pastilles de générateur RTG : l'usure donne la pastille usée de value_maps/item/rtg_pellet.json (decays_to).
if 'data/geontm/value_maps/item/rtg_pellet.json' in noms:
    for pastille_, info_ in json.loads(jar.read('data/geontm/value_maps/item/rtg_pellet.json')).get('values', {}).items():
        if isinstance(info_, dict) and str(info_.get('decays_to', '')).startswith('geontm:'):
            recettes.append((f'reacteur/{info_["decays_to"][7:]}', 'reacteur', ['machine_rtg_grey'], {info_['decays_to'][7:]},
                             [frozenset({pastille_})]))
# Déchets ZIRNOX : une barre usée se transforme en déchet par une recette d'établi Java (ZirnoxWasteRecipe, données de
# value_maps/item/zirnox_fuel.json : depletes_into -> waste), puis le PUREX les recycle.
if 'data/geontm/value_maps/item/zirnox_fuel.json' in noms:
    for _, info_ in json.loads(jar.read('data/geontm/value_maps/item/zirnox_fuel.json')).get('values', {}).items():
        if isinstance(info_, dict) and info_.get('waste') and info_.get('depletes_into'):
            recettes.append((f'java/zirnox_dechet/{info_["waste"][7:]}', 'java', [], {info_['waste'][7:]},
                             [frozenset({info_['depletes_into']})]))
# Bouteille de gaz pleine : gas_empty (fabricable) remplie à la case de remplissage d'une machine (FluidContainerRegistry).
recettes.append(('java/remplir/gas_full', 'java', [], {'gas_full'}, [frozenset({'geontm:gas_empty'})]))
# Constructions (ConstructionHandler, listées par ConstructionRecipeHandler) : le noyau de structure posé et entouré de ses
# blocs donne la machine formée. Les blocs de structure se fabriquent ; la fusion et le Watz exigent les blocs transformés
# à l'outil (tool_conversion), le Watz aussi ses éléments et refroidisseurs.
for machine_, noyau_, blocs_ in (
        ('launch_table', 'struct_launcher_core_large', ['struct_launcher', 'struct_scaffold']),
        ('soyuz_launcher', 'struct_soyuz_core', ['struct_launcher', 'struct_scaffold', 'concrete_smooth']),
        ('fusion_torus', 'struct_torus_core', ['fusion_component_bscco_welded', 'fusion_component_blanket', 'fusion_component_motor']),
        ('watz', 'struct_watz_core', ['watz_casing_tooled', 'watz_element', 'watz_cooler'])):
    recettes.append((f'construction/{machine_}', 'construction', [], {machine_},
                     [frozenset({'geontm:' + noyau_})] + [frozenset({'geontm:' + b_}) for b_ in blocs_]))
# Machine d'assemblage de missiles : elle assemble ogive, fuselage et propulseur en un missile_custom (items à composants).
recettes.append(('java/missile_custom', 'java', ['machine_missile_assembly'], {'missile_custom'},
                 [frozenset({'geontm:mp_warhead_10_he'}), frozenset({'geontm:mp_fuselage_10_kerosene'}),
                  frozenset({'geontm:mp_thruster_10_kerosene'})]))

# Broyeur : en plus des lignes de ShredderTable (fichiers JSON), ShredderRecipes.registerVanillaSweeps / registerTagSweep
# construisent des règles en Java, sans fichier : bûches -> 4 sciure, planches -> 1 sciure, et pour chaque matière c:<forme>/<matière>
# lingots, plaques, gemmes -> 1 poudre ; minerais, matières brutes -> 2 ; blocs -> 4 ou 9 (la quantité n'importe pas ici).
# La poudre est c:dusts/<matière> quand ce tag n'a qu'un seul membre (sinon la sortie dépend de l'ordre des mods préférés : ignorée) ;
# `coltan` vise powder_coltan_ore (DUST_OVERRIDES), la poudre brute que la plante chimique épure ensuite en powder_coltan.
BROYAGE_BOIS = (('minecraft:logs', 'powder_sawdust'), ('minecraft:planks', 'powder_sawdust'))
BROYAGE_SURCHARGE = {'coltan': 'powder_coltan_ore'}
for tag_, sortie_ in BROYAGE_BOIS:
    recettes.append((f'java/broyeur/{tag_}', 'java', ['machine_shredder'], {sortie_}, [frozenset({'#' + tag_})]))
for tag_ in sorted(TAGS):
    m_ = re.match(r'c:(ingots|plates|gems|ores|raw_materials|storage_blocks)/([a-z0-9_]+)$', tag_)
    if not m_:
        continue
    poudres_ = TAGS.get('c:dusts/' + m_.group(2), set())
    poudre_ = BROYAGE_SURCHARGE.get(m_.group(2)) or (next(iter(poudres_))[7:] if len(poudres_) == 1 and next(iter(poudres_)).startswith('geontm:') else None)
    if poudre_:
        recettes.append((f'java/broyeur/{tag_}', 'java', ['machine_shredder'], {poudre_}, [frozenset({'#' + tag_})]))
# Déchets nucléaires : les réacteurs les produisent ; la table ne contient que les conversions entre formes.
for c in sorted(CONNUS):
    if c.startswith('nuclear_waste_') and '_tiny' not in c and c.rsplit('_', 1)[0] in ('nuclear_waste_long', 'nuclear_waste_short',
                                                                                        'nuclear_waste_long_depleted',
                                                                                        'nuclear_waste_short_depleted'):
        recettes.append((f'reacteur/{c}', 'reacteur', ['machine_zirnox'], {c}, []))

# Autres états de réacteur lus sur les noms : combustible PWR chaud, pastilles RTG usées, pastilles RBMK (démontage d'une barre).
for nouveau_, ancien_ in (('pwr_fuel_hot_', 'pwr_fuel_'), ('pellet_rtg_depleted_', 'pellet_rtg_'), ('rbmk_pellet_', 'rbmk_fuel_')):
    for c in sorted(CONNUS):
        if c.startswith(nouveau_) and (ancien_ + c[len(nouveau_):]) in CONNUS and not c.endswith(('_xe',))                 and not re.search(r'_e[0-9]$', c):
            recettes.append((f'reacteur/{c}', 'reacteur', [], {c}, [frozenset({'geontm:' + ancien_ + c[len(nouveau_):]})]))

# Combustible usé : un réacteur transforme un combustible neuf en son état « depleted » (comportement Java, sans recette).
# On suppose qu'un réacteur gardé existe ; le neuf doit être obtenable. La règle est lue sur les noms.
for c in sorted(CONNUS):
    if '_depleted' in c:
        neuf = c.replace('_depleted', '')
        if neuf in CONNUS and neuf != c:
            recettes.append((f'reacteur/{c}', 'reacteur', [], {c}, [frozenset({'geontm:' + neuf})]))

# Pile de Chicago : une barre brûlée devient la suivante (ItemPileRodMK2.EnumPileRod.turnsInto, comportement Java sans recette) :
# nu -> pu239 -> rgp -> waste, thorium -> thorium_fuel -> waste. Le combustible neuf (nu, thorium) se fabrique à l'assembleur.
for ancien_, nouveau_ in (('nu', 'pu239'), ('pu239', 'rgp'), ('rgp', 'waste'), ('thorium', 'thorium_fuel'), ('thorium_fuel', 'waste')):
    if f'pile_rod_mk2_{ancien_}' in CONNUS and f'pile_rod_mk2_{nouveau_}' in CONNUS:
        recettes.append((f'reacteur/pile_rod_mk2_{nouveau_}', 'reacteur', [], {f'pile_rod_mk2_{nouveau_}'},
                         [frozenset({f'geontm:pile_rod_mk2_{ancien_}'})]))

# garde-fou : une machine citée mais inexistante rendrait tout son dossier de recettes introuvable à tort
for dossier_, machines_ in MACHINES.items():
    for m_ in machines_:
        if m_ not in CONNUS:
            print(f'ATTENTION : machine inconnue du jar pour le dossier {dossier_} : {m_}', file=sys.stderr)

# --- sources hors recette -----------------------------------------------------------------------------------------
SOURCE = {}  # id -> 'butin' | 'minerai' | 'drop'
natifs = set()  # blocs posés par la génération du monde
for n in noms:
    if re.match(r'data/geontm/worldgen/(configured_feature|structure)/.*\.json$', n):
        natifs |= set(GEO.findall(jar.read(n).decode('utf-8', 'replace')))
for x in VISIBLE:
    if x.startswith('ore_') or x.endswith('_ore') or x.startswith('ore'):
        natifs.add(x)
if TERRE:  # les minerais de ces matières ne se génèrent que sur leur planète
    natifs = {x for x in natifs if not PLANETAIRE.search(x)}
butin = set()
for n in noms:
    m = re.match(r'data/geontm/loot_tables/(chests|entities|gameplay)/.*\.json$', n) or re.match(r'data/geontm/item_pool/.*\.json$', n)
    if m:
        butin |= set(GEO.findall(jar.read(n).decode('utf-8', 'replace')))
drops = collections.defaultdict(set)  # bloc -> objets lâchés
for n in noms:
    m = re.match(r'data/geontm/loot_tables/blocks/(.+)\.json$', n)
    if m:
        drops[m.group(1)] |= set(GEO.findall(jar.read(n).decode('utf-8', 'replace'))) - {m.group(1)}

obtenu = set()
origine = {}


ordre = {}


def ajoute(x, comment):
    if x not in obtenu:
        obtenu.add(x)
        origine[x] = comment
        ordre[x] = len(ordre)
        return True
    return False


for x in natifs:
    ajoute(x, 'minerai ou bloc de monde')
for x in FLUIDES_DE_BASE + [v for v in os.environ.get('FLUIDES_EN_PLUS', '').split(',') if v]:  # FLUIDES_EN_PLUS=gas : simulation
    ajoute(x, 'fluide de base')
for x in matieres_des_tags():
    ajoute('mat:' + x, 'matière fournie par un autre mod')
inconnus_machine = collections.Counter()


def ok(ident, vus=None):
    if TERRE and not ident.startswith('geontm:') and PLANETAIRE_AD_ASTRA.search(ident) and not tag_non_vide(ident.lstrip('#')):
        return False  # ressource d'une autre planète (Ad Astra) : pas sur Terre
    if ident.startswith('#'):
        t = ident[1:]
        if not tag_non_vide(t):
            return True  # tag vide ou d'un autre mod : supposé fourni par le pack
        vus = vus or set()
        if t in vus:
            return False
        vus.add(t)
        return any(ok(m, vus) for m in TAGS[t] if m)
    if ident.startswith('mat:'):
        return ident in obtenu
    if not ident.startswith('geontm:'):
        return not (TERRE and PLANETAIRE_AD_ASTRA.search(ident))
    return ident[7:] in obtenu


def groupe_ok(g):
    return any(ok(a) for a in g)


def point_fixe():
    change = True
    while change:
        change = False
        for o in list(obtenu):
            if o in MATIERE_DE:
                change |= ajoute('mat:' + MATIERE_DE[o], 'matière de ' + o)
        for bloc, objets in drops.items():
            if bloc in obtenu:
                for o in objets:
                    change |= ajoute(o, 'drop de ' + bloc)
        for rid, dossier, mach, outs, ing in recettes:
            if outs <= obtenu:
                continue
            if mach is None:
                inconnus_machine[dossier] += 1
                continue
            if mach and not any(m in obtenu for m in mach):
                continue
            if all(groupe_ok(g) for g in ing):
                for o in outs:
                    change |= ajoute(o, 'recette ' + rid)


point_fixe()
if not os.environ.get('SANS_BUTIN'):  # SANS_BUTIN=1 : le butin ne compte pas (calcul de ce qui ne vient que des coffres)
    for x in butin:
        ajoute(x, 'butin')
    point_fixe()

# Simulation : RECETTES_SUPPLEMENTAIRES=<dossier> ajoute des recettes JSON (même disposition que data/geontm/recipes) pour voir
# ce qu'elles débloquent avant de les intégrer ; --restaure liste celles qui servent à un objet visible nouvellement obtenu.
SUPP = set()
base_obtenu = set(obtenu)
dossier_supp = os.environ.get('RECETTES_SUPPLEMENTAIRES')
if dossier_supp:
    for racine_, _, fichiers_ in os.walk(dossier_supp):
        for f_ in fichiers_:
            if not f_.endswith('.json'):
                continue
            chemin_ = os.path.join(racine_, f_)
            rid = os.path.relpath(chemin_, dossier_supp).replace(os.sep, '/')[:-5]
            d = json.load(open(chemin_, encoding='utf-8'))
            if not conditions_ok(d):
                continue
            dossier = rid.split('/')[0] if '/' in rid else ''
            sortie = {k: d[k] for k in d if k in CLES_SORTIE | ({'left', 'right'} if dossier in ('cracking', 'fraction', 'radiolysis') else set())}
            entree = {k: d[k] for k in d if k not in sortie and k not in CLES_NEUTRES and 'expensive' not in k}
            outs = set()
            sorties(sortie, outs)
            ing = []
            MATIERES_EN_ENTREE = dossier == 'crucible'
            exigences(entree, ing)
            MATIERES_EN_ENTREE = False
            if outs:
                recettes.append((rid, dossier, MACHINES.get(dossier, None) if dossier else [], outs, ing))
                SUPP.add(rid)
    point_fixe()


def derive(x, vus):
    """Recettes supplémentaires qui servent à obtenir x (alternative la plus ancienne pour chaque exigence)."""
    if x in vus or x not in obtenu or x in base_obtenu:
        return set()
    vus.add(x)
    o = origine.get(x, '')
    if not o.startswith('recette '):
        return set()
    rid = o[8:]
    r = next((r for r in recettes if r[0] == rid), None)
    res = {rid} if rid in SUPP else set()
    if r is None:
        return res
    if r[2] and not any(m in base_obtenu for m in r[2]):
        m = min((m for m in r[2] if m in obtenu), key=lambda m: ordre[m])
        res |= derive(m, vus)
    for g in r[4]:
        cands = []
        for a in g:
            if a.startswith('#'):
                cands += [m[7:] for m in TAGS.get(a[1:], ()) if m.startswith('geontm:') and m[7:] in obtenu]
            elif a.startswith('geontm:'):
                cands.append(a[7:])
            elif a.startswith('mat:'):
                cands.append(a)
        cands = [c for c in cands if c in obtenu]
        if cands:
            res |= derive(min(cands, key=lambda c: ordre[c]), vus)
    return res


par_sortie = collections.defaultdict(list)
for r in recettes:
    for o in r[3]:
        par_sortie[o].append(r)


def manque(r):
    rid, dossier, mach, outs, ing = r
    mm = []
    if mach is None:
        mm.append('MACHINE INCONNUE ' + dossier)
    elif mach and not any(m in obtenu for m in mach):
        mm.append('MACHINE ' + '/'.join(mach))
    for g in ing:
        if not groupe_ok(g):
            mm.append(' | '.join(sorted(a[7:] if a.startswith('geontm:') else a for a in g)))
    return mm


def meilleure(x):
    rs = par_sortie.get(x, [])
    if not rs:
        return None
    return min(rs, key=lambda r: ('recycle' in r[0] or 'recycl' in r[0], len(manque(r)), r[0]))


def causes(x, vus=None):
    """Causes premières (objets sans aucune recette, machines manquantes) derrière un objet non obtenu."""
    vus = set() if vus is None else vus
    if x in vus or x in obtenu:
        return set()
    vus.add(x)
    r = meilleure(x)
    if r is None:
        return {x}
    res = set()
    for m in manque(r):
        if m.startswith('MACHINE'):
            res.add(m)
            for mach in (r[2] or []):
                res |= causes(mach, vus)
            continue
        for a in m.split(' | '):
            if a.startswith('#'):
                for mb in TAGS.get(a[1:], ()):
                    if mb and not mb.startswith('#') and mb.startswith('geontm:'):
                        res |= causes(mb[7:], vus)
                    elif mb.startswith('#'):
                        res |= {c for t in [mb[1:]] for c in causes_tag(t, vus)}
            else:
                res |= causes(a, vus)
    return res


def causes_tag(t, vus):
    res = set()
    for mb in TAGS.get(t, ()):
        if mb.startswith('#'):
            res |= causes_tag(mb[1:], vus)
        elif mb.startswith('geontm:'):
            res |= causes(mb[7:], vus)
    return res


def explique(x, prof=0, vus=None):
    vus = set() if vus is None else vus
    pad = '  ' * prof
    if x in obtenu:
        print(f'{pad}{x} : OK ({origine.get(x)})')
        return
    if x in vus:
        print(f'{pad}{x} : (déjà vu)')
        return
    vus.add(x)
    rs = sorted(par_sortie.get(x, []), key=lambda r: len(manque(r)))
    if not rs:
        print(f'{pad}{x} : AUCUNE RECETTE')
        return
    print(f'{pad}{x} : non obtenu')
    for r in rs[:3]:
        print(f'{pad}  recette {r[0]} manque {manque(r)}')
    if prof < 6:
        for m in [m for r in rs[:3] for m in manque(r)]:
            if m.startswith('MACHINE'):
                for mach in rs[0][2] or []:
                    explique(mach, prof + 2, vus)
                continue
            for a in m.split(' | '):
                if a.startswith('#'):
                    for mb in sorted(TAGS.get(a[1:], ())):
                        if mb.startswith('geontm:'):
                            explique(mb[7:], prof + 2, vus)
                        elif mb.startswith('#'):
                            print(f'{pad}    (tag {mb})')
                            for mm in sorted(TAGS.get(mb[1:], ())):
                                if mm.startswith('geontm:'):
                                    explique(mm[7:], prof + 3, vus)
                else:
                    explique(a, prof + 2, vus)


if TERRE:
    # tag dont les seuls membres sont ceux d'Ad Astra : n'apporte rien sur Terre (ok() le sait déjà) ; on liste les objets geontm
    fuites = sorted(x for x in obtenu if PLANETAIRE.search(x) and not re.search(r'(fuel|waste|desh_(mix|ready)|^(missile|apple|desh_(axe|hoe|pickaxe|shovel|sword)))', x))  # desh_mix/ready : mélange de terres rares, pas du desh
    print(f'== Ressources planétaires obtenables sur Terre ({len(fuites)})')
    for x in fuites:
        print(f'  {x:42} {origine.get(x)}')
    sys.exit(0)

# --- classement des objets sans recette (audit du 25/11) --------------------------------------------------------------
# docs/perimetre/obtention-audit.tsv : pour chacun des objets qu'aucune recette ne produisait (id, classe, famille, raison).
#   A = rendu fabricable (recette ajoutée, ou chaîne réparée) ; B = caché (simplifie.txt) car sans place en survie ;
#   C = obtenu hors recette et justifié (butin, schémas, recette secrète, produit en jeu).
# Le rapport vérifie le classement : un A doit être obtenu, un B masqué, un C obtenu ou justifié.
AUDIT = {}
CHEMIN_AUDIT = os.path.join(os.path.dirname(os.path.abspath(__file__)), '..', 'docs', 'perimetre', 'obtention-audit.tsv')
if os.path.exists(CHEMIN_AUDIT):
    for ligne_ in open(CHEMIN_AUDIT, encoding='utf-8').read().splitlines():
        if ligne_.strip() and not ligne_.startswith('#'):
            id_, classe_, famille_, raison_ = ligne_.split('\t')
            AUDIT[id_] = (classe_, famille_, raison_)
MANUEL = {}


def justification(x):
    if x in volontaires:
        return 'volontaire', 'arme extrême ou objet écarté, sans recette par décision du propriétaire'
    if x in MANUEL:
        return MANUEL[x]
    if x in AUDIT and AUDIT[x][0] == 'C':
        return 'hors recette', AUDIT[x][2]
    return 'à décider', ''


# --- rapport -------------------------------------------------------------------------------------------------------
# Fluides sans producteur obtenable : les recettes qui les exigent ne peuvent pas tourner. `--retraits fichier.json` écrit leur liste
# au format des recipe_removals (désactivation au chargement, rien n'est supprimé).
FLUIDES = {n.split('/')[-1][:-5] for n in noms if n.startswith('data/geontm/fluid_property/') and n.endswith('.json')}
sans_producteur = {}
for f in sorted(FLUIDES):
    cle_ = 'fluid:' + f if f in COLLISIONS else f
    if cle_ in obtenu:
        continue
    users = [r[0] for r in recettes + [(i_, d_, None, None, e_) for i_, d_, e_ in SANS_SORTIE]
             if any('geontm:' + cle_ in g and len(g) == 1 for g in r[4]) and '/' in r[0] and r[1] not in SYNTHETIQUES]
    if users:
        sans_producteur[f] = users
if RETRAITS:
    ids_ = sorted({'geontm:' + u for users_ in sans_producteur.values() for u in users_})
    with open(RETRAITS, 'w', encoding='utf-8', newline='\n') as fichier_:
        fichier_.write(json.dumps({'recipes': ids_}, indent=1) + '\n')
    print(f'{len(ids_)} recettes sans producteur de fluide écrites dans {RETRAITS}')
if LISTE_OBTENUS:
    print(*sorted(x for x in VISIBLE if x in obtenu), sep='\n')
    sys.exit(0)
cibles = PREFIXES or ['']
manquants = sorted(x for x in VISIBLE if x not in obtenu and any(x.startswith(c) for c in cibles))
print(f'obtenables : {len(obtenu)} ; visibles : {len(VISIBLE)} ; visibles non obtenables dans les familles suivies : {len(manquants)}')

bloquants = collections.Counter()
detail = {}
for x in manquants:
    r = meilleure(x)
    if r is None:
        detail[x] = 'aucune recette'
        bloquants['(aucune recette)'] += 1
        continue
    mm = manque(r)
    detail[x] = f'{r[0]} : manque {mm}'
    for b in mm:
        bloquants[b] += 1

if os.environ.get('USAGES'):  # USAGES=1 : pour chaque objet bloqué, les objets visibles que fabriquent les recettes qui l'exigent
    print('\n== Utilisé par (sorties visibles des recettes qui exigent l\'objet bloqué)')
    for x in manquants:
        users = sorted({o for r in recettes for g in r[4] if len(g) == 1 and ('geontm:' + x) in g for o in r[3] if o in VISIBLE and o != x})
        if users:
            print(f'  {x:40} {", ".join(users[:12])}')
print('\n== Maillons qui bloquent le plus')
for b, n in bloquants.most_common(25):
    print(f'{n:4}  {b}')
cc = collections.Counter()
for x in manquants:
    if detail[x] != 'aucune recette':
        for c in causes(x):
            cc[c] += 1
print('\n== Causes premières (sans aucune recette, ou machine absente) derrière les objets visibles bloqués')
for b, n in cc.most_common(70):
    print(f'{n:4}  {b}')
print('\n== Détail')
for x in manquants:
    print(f'  {x:40} {detail[x]}')
if inconnus_machine:
    print('\nTypes de recette sans machine connue :', dict(inconnus_machine))
if SUPP:
    nouveaux = sorted(x for x in VISIBLE if x in obtenu and x not in base_obtenu)
    utiles = set()
    for x in nouveaux:
        utiles |= derive(x, set())
    print(f'\n== Recettes supplémentaires : {len(SUPP)} testées, {len(nouveaux)} objets visibles débloqués, {len(utiles)} utiles')
    for rid in sorted(utiles):
        print('  ', rid)
for x in EXPLIQUE:
    print('\n== Explication', x)
    explique(x)

if MD:
    par_cat = collections.defaultdict(list)
    for x in manquants:
        cat, why = justification(x)
        if detail[x] != 'aucune recette' and cat == 'à décider' and not why:
            why = 'chaîne coupée : ' + detail[x]
        par_cat[cat].append((x, why, detail[x]))
    for x in sorted(volontaires):
        if x not in obtenu and x not in manquants and (x in CONNUS):
            par_cat['volontaire'].append((x, justification(x)[1], ''))
    sources = collections.Counter(v.split(' ')[0] for v in origine.values())
    n_schemas = 0
    for n in noms:
        if n.startswith('data/geontm/recipes/') and n.endswith('.json') and 'blueprint_pools' in jar.read(n).decode('utf-8', 'replace'):
            try:
                if re.search(r'^(discover|secret)', ' '.join(json.loads(jar.read(n)).get('blueprint_pools') or []), re.M):
                    n_schemas += 1
            except Exception:
                pass
    L = ['# Obtention en survie', '',
         'Fichier généré par `tools/verifier_obtention.py` à partir du jar compilé : point fixe sur les recettes actives '
         '(conditions évaluées avec les réglages par défaut : 528, mode coûteux et LBSM désactivés), en exigeant que '
         'la machine de chaque recette soit elle-même obtenable. Les objets, tags et fluides de GeoMods comptent comme fournis.', '',
         f'- Objets et fluides obtenables : **{len(obtenu)}** (recettes : {sources["recette"]}, butin : {sources["butin"]}, '
         f'blocs de monde : {sources["minerai"]}, drops : {sources["drop"]})',
         f'- Objets visibles : **{len(VISIBLE)}** ; visibles non obtenables : **{len(manquants)}**',
         f"- Hypothèse : {n_schemas} recettes actives (missiles modulaires, espace, ogives, réacteurs...) ne se débloquent qu'avec un "
         "schéma (`blueprint_pools`, mécanisme NTM inchangé) trouvé dans les coffres ; l'outil les compte comme faisables. "
         'Les réacteurs, le combustible usé et les déchets sont supposés produits par les machines Java correspondantes. '
         "Les règles de broyage construites en Java (`ShredderRecipes` : bûches et planches en sciure, minerais, lingots et blocs "
         "en poudre) sont modélisées : la sciure du cordite et le coltan broyé de `coltanpain` en viennent. Sont aussi modélisés "
         "d'après le code : les constructions autour d'un noyau de structure (lanceurs, Watz, tore de fusion), les conversions à l'outil "
         "(`tool_conversion`), les récipients de fluide (vidés dans une machine), les poches de sang et d'expérience, les déchets des "
         "barres ZIRNOX usées, les pastilles RTG usées, les schémas ouverts depuis un dossier et la machine d'assemblage de missiles. "
         "Un fluide et un objet de même nom (`watz`, `concrete`, `balefire`) sont distingués.", '']
    ordre = ['à décider', 'volontaire', 'hors recette', 'cosmétique', 'butin', 'bloc formé', 'monde', 'autre mod', 'dérivé']
    L += ['| Catégorie | Nombre |', '|---|---:|']
    for c in sorted(par_cat, key=lambda c: ordre.index(c) if c in ordre else 99):
        L.append(f'| {c} | {len(par_cat[c])} |')
    L.append('')
    for c in sorted(par_cat, key=lambda c: ordre.index(c) if c in ordre else 99):
        L += [f'## {c}', '', '| Objet | Justification |', '|---|---|']
        for x, why, det in par_cat[c]:
            L.append(f'| `{x}` | {why or det} |')
        L.append('')
    if AUDIT:
        erreurs = []
        L += ["## Classement des objets qu'aucune recette ne produisait", '',
              f'Audit du 25/11 sur le jar 0.1.13 : {len(AUDIT)} objets visibles non obtenables (323 relevés, trois autres révélés par la correction de l’outil). Source : `docs/perimetre/obtention-audit.tsv` '
              '(id, classe, famille, raison). **A** : rendu fabricable ; **B** : caché dans `simplifie.txt` (l\'id reste enregistré) ; '
              '**C** : obtenu hors recette, justifié. La colonne « état » est recalculée sur le jar vérifié.', '',
              '| Classe | Nombre |', '|---|---:|']
        for classe_, titre_ in (('A', 'A : fabricable'), ('B', 'B : caché'), ('C', 'C : hors recette')):
            L.append(f'| {titre_} | {sum(1 for v_ in AUDIT.values() if v_[0] == classe_)} |')
        L.append('')
        for classe_, titre_ in (('A', 'A : rendus fabricables'), ('B', 'B : cachés'), ('C', 'C : obtenus hors recette')):
            L += [f'### {titre_}', '', '| Objet | Famille | État | Raison |', '|---|---|---|---|']
            for x_, (c_, famille_, raison_) in sorted(AUDIT.items(), key=lambda kv: (kv[1][1], kv[0])):
                if c_ != classe_:
                    continue
                if classe_ == 'B':
                    etat_ = 'caché' if (x_ in MASQUE or x_ not in VISIBLE) else 'VISIBLE'
                    if etat_ == 'VISIBLE':
                        erreurs.append(x_)
                elif x_ in obtenu:
                    etat_ = 'obtenu (' + origine.get(x_, '?') + ')'
                elif x_ not in VISIBLE:
                    etat_ = 'caché'
                else:
                    etat_ = 'BLOQUÉ' if classe_ == 'A' else 'sans recette, justifié'
                    if classe_ == 'A':
                        erreurs.append(x_)
                L.append(f'| `{x_}` | {famille_} | {etat_} | {raison_} |')
            L.append('')
        print('classement : ' + (f'{len(erreurs)} ERREUR(S) : ' + ', '.join(erreurs) if erreurs else 'tout est conforme'))
        caches_ = {x_ for x_, v_ in AUDIT.items() if v_[0] == 'B'}
        exigeants_ = sorted((r_[0], o_) for r_ in recettes if r_[1] not in SYNTHETIQUES for o_ in r_[3] if o_ in VISIBLE
                            and any(g_ and all(a_.startswith('geontm:') and a_[7:] in caches_ for a_ in g_) for g_ in r_[4]))
        L += ['### Recettes actives qui exigent un objet caché pour fabriquer un objet visible', '',
              ', '.join(f'`{r_}` -> `{o_}`' for r_, o_ in exigeants_) or 'aucune', '']
        print(f'recettes visibles exigeant un objet caché : {len(exigeants_)}', exigeants_[:10])
        if RETRAITS_CACHES:
            ids_ = sorted({'geontm:' + r_ for r_, _ in exigeants_})
            with open(RETRAITS_CACHES, 'w', encoding='utf-8', newline='\n') as fichier_:
                fichier_.write(json.dumps({'recipes': ids_}, indent=1) + '\n')
            print(f'{len(ids_)} recettes exigeant un objet caché écrites dans {RETRAITS_CACHES}')
    L += ['## Fluides demandés par une recette active sans producteur obtenable', '',
          'Un fluide sans producteur rend inutilisables les recettes qui le demandent (les autres voies restent possibles). '
          'Le gaz `gas` vient de la gazéification du charbon ou du coke au four de pyrolyse ; les pompes GeoMods ne donnent que le brut.', '',
          '| Fluide | Recettes qui le demandent |', '|---|---|']
    for f, users in sans_producteur.items():
        L.append(f'| `{f}` | {len(users)} : ' + ', '.join(users[:4]) + (' …' if len(users) > 4 else '') + ' |')
    if not sans_producteur:
        L.append('| aucun | les recettes qui exigeaient un fluide sans producteur sont désactivées par `recipe_removals/geoventure_sans_producteur.json` '
                 '(écrit par `--retraits`) ; xpjuice et blood viennent des récipients iv_xp, fiole d’expérience et iv_blood |')
    L.append('')
    import subprocess
    env = dict(os.environ, SANS_BUTIN='1')
    sortie_sb = subprocess.run([sys.executable, '-I', os.path.abspath(__file__), args[0], '--obtenus'], env=env,
                               capture_output=True, text=True, encoding='utf-8').stdout.split()
    seul_butin = sorted(x for x in VISIBLE if x in obtenu and x not in set(sortie_sb))
    L += ['## Obtenus seulement par le butin', '',
          "Ces objets visibles n'ont pas de recette praticable : on ne les a qu'en ouvrant des coffres ou en tuant des mobs.", '',
          ', '.join(f'`{x}`' for x in seul_butin) or 'aucun', '']
    env_terre = dict(os.environ, TERRE='1')
    terre = subprocess.run([sys.executable, '-I', os.path.abspath(__file__), args[0]], env=env_terre,
                           capture_output=True, text=True, encoding='utf-8').stdout.splitlines()
    fuites_terre = [l.split()[0] for l in terre if l.startswith('  ')]
    L += ['## Ressources planétaires sur Terre', '',
          'Une ressource unique par planète, absente de la Terre : desh (Lune), ostrum et saturnite (Mars), calorite (Vénus), '
          'schrabidium (Mercure), starmetal (Glacio). Calcul `TERRE=1 python tools/verifier_obtention.py <jar>` : les minerais de ces '
          "matières ne comptent plus comme du monde et les objets `ad_astra:` de desh, ostrum et calorite ne sont plus fournis ; ce qui reste "
          'obtenable est une fuite.', '',
          ', '.join(f'`{x}`' for x in fuites_terre) or 'aucune fuite', '']
    with open(MD, 'w', encoding='utf-8', newline='\n') as f:
        f.write('\n'.join(L) + '\n')
    print('rapport écrit :', MD)
