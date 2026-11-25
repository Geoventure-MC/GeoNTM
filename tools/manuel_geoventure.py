#!/usr/bin/env python3
"""Pages F1 (QMAW) Geoventure : une page par objet VISIBLE qui n'en a pas dans le manuel de NTM-Continuum.

    python tools/manuel_geoventure.py <jar GeoNTM> --geomods <lang fr_fr.json> --geomods-en <lang en_us.json>
                                      --vanilla <fr_fr.json> --vanilla-en <en_us.json>

Le contenu vient des recettes ACTIVES du jar (recipe_removals appliqués) : comment l'obtenir (machine, ingrédients, schéma exigé)
et à quoi il sert, avec des liens entre pages. Fichiers écrits dans assets/geontm/manual/geoventure/auto/, réécrits à chaque
passage ; les guides écrits à la main sont dans manual/geoventure/guide/. Rien n'est écrit dans les pages d'origine.
"""
import argparse
import collections
import glob
import json
import os
import re
import zipfile

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
MAN = os.path.join(ROOT, 'src/main/resources/assets/geontm/manual')
OUT = os.path.join(MAN, 'geoventure/auto')
PER = os.path.join(ROOT, 'src/main/resources/geontm/perimetre')

ap = argparse.ArgumentParser()
ap.add_argument('jar')
for o in ('geomods', 'geomods-en', 'vanilla', 'vanilla-en'):
    ap.add_argument('--' + o, required=True)
a = ap.parse_args()


def lire(f):
    return {x.split('#')[0].strip() for x in open(os.path.join(PER, f), encoding='utf-8') if x.split('#')[0].strip()}


VISIBLES = (lire('creatif.txt') | lire('ajoute.txt')) - lire('simplifie.txt')
z = zipfile.ZipFile(a.jar)
jl = lambda p: json.load(open(p, encoding='utf-8'))
LANG = {'fr': {}, 'en': {}}
for l, files in (('fr', (a.vanilla, a.geomods)), ('en', (a.vanilla_en, a.geomods_en))):
    for f in files:
        LANG[l].update(jl(f))
    LANG[l].update(json.loads(z.read('assets/geontm/lang/%s.json' % ('fr_fr' if l == 'fr' else 'en_us'))))

SCHEMAS = {  # famille -> (nom fr, nom en, moyen fr, moyen en) ; voir docs/perimetre/schemas.md et pont/PontProgression
    'discover.ogives': ('Ogives conventionnelles', 'Conventional warheads', 'Balistique I', 'Balistique I'),
    'discover.ogives_lourdes': ('Ogives lourdes', 'Heavy warheads', 'Balistique II', 'Balistique II'),
    'discover.missiles_modulaires': ('Missiles modulaires', 'Modular missiles', 'Dissuasion Stratégique', 'Dissuasion Stratégique'),
    'discover.reacteurs': ('Réacteurs à fission', 'Fission reactors', 'Physique Nucléaire', 'Physique Nucléaire'),
    'discover.fusion': ('Fusion', 'Fusion', 'Matériaux Avancés', 'Matériaux Avancés'),
    'discover.spatial': ('Lancement spatial et satellites', 'Space launch and satellites', 'Aérospatiale', 'Aérospatiale'),
    'discover.espace_lointain': ('Espace lointain', 'Deep space', 'Espace Profond', 'Espace Profond'),
    'secret.ogives_speciales': ('Ogives spéciales', 'Special warheads', None, None),
    'secret.orbital': ('Armes et renseignement orbitaux', 'Orbital weapons and intelligence', None, None),
}
MACHINES = {  # type de recette -> bloc de la machine (son nom vient du fichier de langue)
    'assembly_machine': 'machine_assembly_machine', 'chemical_plant': 'machine_chemical_plant', 'crystallizer': 'machine_crystallizer',
    'centrifuge': 'machine_centrifuge', 'shredder': 'machine_shredder', 'press': 'machine_press', 'soldering': 'machine_soldering_station',
    'arc_welder': 'machine_arc_welder', 'arc_furnace': 'machine_arc_furnace', 'mixer': 'machine_mixer', 'cyclotron': 'machine_cyclotron',
    'purex': 'machine_purex', 'silex': 'machine_silex', 'precision_assembler': 'machine_precass', 'pyrolysis': 'machine_pyrooven',
    'coker': 'machine_coker', 'solidification': 'machine_solidifier', 'liquefaction': 'machine_liquefactor', 'blast_furnace': 'machine_blast_furnace',
    'combination': 'machine_furnace_combination', 'rotary_furnace': 'machine_rotary_furnace', 'electrolyser_metal': 'machine_electrolyser',
    'electrolyser_fluid': 'machine_electrolyser', 'outgasser': 'rbmk_outgasser', 'breeder': 'machine_reactor', 'rock_mill': 'machine_rockmill',
    'gas_centrifuge': 'machine_gascent', 'compressor': 'machine_compressor', 'fusion': 'fusion_torus', 'difurnace': 'machine_difurnace_off',
    'space_assembler': 'satellite_science_assembler', 'storage_drum': 'machine_storage_drum', 'sawmill': 'machine_sawmill',
    'supercomputer': 'machine_supercomputer', 'exposure_chamber': 'machine_exposure_chamber', 'fluid_breeder': 'fusion_breeder',
}
FIXES = {  # types sans bloc : libellé
    'crafting_shaped': ('Établi', 'Crafting table'), 'crafting_shapeless': ('Établi', 'Crafting table'),
    'component_shaped': ('Établi', 'Crafting table'), 'component_shapeless': ('Établi', 'Crafting table'),
    'component_copy_shapeless': ('Établi', 'Crafting table'), 'smelting': ('Four', 'Furnace'), 'component_smelting': ('Four', 'Furnace'),
    'blasting': ('Haut fourneau vanilla', 'Blast furnace'), 'smoking': ('Fumoir', 'Smoker'),
    'anvil_construction': ('Enclume industrielle (construction)', 'Industrial anvil (construction)'),
    'anvil_smithing': ('Enclume industrielle (forge)', 'Industrial anvil (smithing)'), 'crucible': ('Creuset', 'Crucible'),
    'tool_conversion': ('Conversion à l\'outil, bloc posé', 'Tool conversion, placed block'),
}
FORMES = {'pipe': ('tuyau', 'pipe'), 'castplate': ('plaque coulée', 'cast plate'), 'shell': ('coque', 'shell'),
          'densewire': ('fil dense', 'dense wire'), 'wire': ('fil', 'wire'), 'weldedplate': ('plaque soudée', 'welded plate'), 'bolt': ('boulon', 'bolt')}
TAGS = {'plates': 'plate_%s', 'ingots': 'ingot_%s', 'dusts': 'powder_%s', 'billets': 'billet_%s', 'nuggets': 'nugget_%s', 'wires': 'wire_%s',
        'gems': 'gem_%s', 'storage_blocks': 'block_%s', 'plates_cast': None, 'plates_welded': None, 'shells': None, 'pipes': None,
        'bolts': None, 'wires_dense': None}
TAGFORME = {'plates_cast': 'castplate', 'plates_welded': 'weldedplate', 'shells': 'shell', 'pipes': 'pipe', 'bolts': 'bolt', 'wires_dense': 'densewire'}
MOTS = {'dusts': ('poudre', 'dust'), 'plates': ('plaque', 'plate'), 'ingots': ('lingot', 'ingot'), 'nuggets': ('pépite', 'nugget'),
        'billets': ('billette', 'billet'), 'wires': ('fil', 'wire'), 'gems': ('gemme', 'gem'), 'storage_blocks': ('bloc', 'block'),
        'ores': ('minerai', 'ore'), 'raw_materials': ('minerai brut', 'raw ore'), 'dyes': ('colorant', 'dye'), 'fragments': ('fragment', 'fragment')}


# Objets dont le nom en jeu est celui d'une autre clé (ItemAutogen : modèle « Poignée en %s » + matière ; PileDeviceItem : bloc pile_device_N)
NOMS_LIES = {'pile_loader': 'block.geontm.pile_device_0', 'pile_vent': 'block.geontm.pile_device_1', 'pile_control': 'block.geontm.pile_device_2'}
GABARITS = sorted((k[len('item.geontm.'):] for k, v in LANG['en'].items() if k.startswith('item.geontm.') and v.count('%s') == 1), key=len, reverse=True)


def nom(rl, l, repli=True):
    ns, _, p = rl.partition(':') if ':' in rl else ('geontm', ':', rl)
    for k in ('item.%s.%s', 'block.%s.%s', 'fluid_type.%s.%s', 'hbmfluid.%s.%s', 'fluid.%s.%s'):
        v = LANG[l].get(k % (ns, p))
        if v and '%' not in v:
            return v.replace('§', '').strip()
    if ns == 'geontm':
        v = LANG[l].get(NOMS_LIES.get(p, ''))
        if v:
            return v.replace('§', '').strip()
        # objet de matière générée : item.geontm.<forme> (« Poignée en %s ») avec geontmmat.<matière>, comme ItemAutogen.getName
        for b in GABARITS:
            if p.startswith(b + '_'):
                g = LANG[l].get('item.geontm.' + b) or LANG['en'].get('item.geontm.' + b)
                m = LANG[l].get('geontmmat.' + p[len(b) + 1:]) or LANG['en'].get('geontmmat.' + p[len(b) + 1:])
                if g and m and g.count('%s') == 1:
                    return g.replace('%s', m).replace('§', '').strip()
    # comme le jeu : une clé absente du français retombe sur l'anglais (les noms propres identiques n'y sont pas répétés)
    return nom(rl, 'en', False) if l == 'fr' and repli else None


MATIERES = {'any_rubber': ('caoutchouc', 'rubber'), 'any_plastic': ('plastique', 'plastic'), 'any_hardplastic': ('plastique dur', 'hard plastic'),
            'any_resistant_alloy': ('alliage résistant', 'resistant alloy'), 'any_bismoid': ('bismoïde', 'bismoid'),
            'any_bismoid_bronze': ('bronze bismoïde', 'bismoid bronze'), 'any_tar': ('goudron', 'tar'), 'any_coke': ('coke', 'coke'),
            'any_plasticexplosive': ('explosif plastique', 'plastic explosive'), 'any_highexplosive': ('explosif puissant', 'high explosive'),
            'any_propellant': ('propergol', 'propellant'), 'weaponsteel': ('acier à armes', 'weapon steel'), 'gunmetal': ('bronze à canon', 'gunmetal')}


def matiere(m, l):
    if m in MATIERES:
        return MATIERES[m][0 if l == 'fr' else 1]
    n = nom('geontm:ingot_' + m, l) or nom('geomods:%s_ingot' % m, l) or nom('minecraft:%s_ingot' % m, l)
    if n:
        return re.sub(r"^(Lingots? (de |d'|du )|Lingot )", '', n).strip() if l == 'fr' else re.sub(r' Ingot$', '', n)
    return m.replace('any_', '').replace('_', ' ')


def forme(f, m, l):
    w = FORMES.get(f, (f, f))[0 if l == 'fr' else 1]
    return '%s (%s)' % (w, matiere(m, l).lower() if l == 'fr' else matiere(m, l))


def tag(t, l):
    p = t.split(':', 1)[-1]
    if '/' in p:
        cat, m = p.split('/', 1)
        if cat in TAGFORME:
            return forme(TAGFORME[cat], m, l)
        for pat in ((TAGS.get(cat) or '') % m if TAGS.get(cat) else '',):
            for ns in ('geontm:', ):
                if pat and nom(ns + pat, l):
                    return nom(ns + pat, l)
        for c, f in (('ingots', '%s_ingot'), ('plates', '%s_plate'), ('dusts', '%s_dust'), ('nuggets', '%s_nugget')):
            if cat == c:
                for ns in ('geomods:', 'minecraft:'):
                    if nom(ns + f % m, l):
                        return nom(ns + f % m, l)
        if cat in MOTS:
            return '%s (%s)' % (MOTS[cat][0 if l == 'fr' else 1], matiere(m, l).lower())
    if t in ('minecraft:planks', 'forge:planks', 'c:planks'):
        return 'planches' if l == 'fr' else 'planks'
    for ns in ('minecraft:', 'geontm:'):
        if nom(ns + p, l) or nom(ns + p.rstrip('s'), l):
            return nom(ns + p, l) or nom(ns + p.rstrip('s'), l)
    return p.replace('/', ' ').replace('_', ' ')


# pages existantes : objet -> page (pour les liens)
PAGE = {}
for p in glob.glob(MAN + '/**/*.json', recursive=True):
    q = p.replace(os.sep, '/')
    if '/geoventure/auto/' in q:
        continue
    d = jl(p)
    for t in d.get('trigger', []):
        if isinstance(t, list) and len(t) <= 2 and str(t[0]).startswith('geontm:'):
            PAGE.setdefault(t[0][7:], d['name'])
SANS = sorted(x for x in VISIBLES - set(PAGE) if nom('geontm:' + x, 'fr') and nom('geontm:' + x, 'en'))
for x in SANS:
    PAGE[x] = 'gv:' + x


def lien(rl, l):
    n = nom(rl, l)
    if n is None:
        return None
    n = n.replace('[', '(').replace(']', ')').replace('|', '/')
    x = rl[7:] if rl.startswith('geontm:') else None
    return '[[%s|%s]]' % (PAGE[x], n) if x in PAGE else n


RM = set()
for n in z.namelist():
    if '/recipe_removals/' in n and n.endswith('.json'):
        RM |= set(json.loads(z.read(n))['recipes'])
SORTIES = ('output_items', 'result', 'output', 'outputs', 'solid_output', 'item_output', 'output_item', 'crafting_result', 'byproducts', 'to',
           'output_fluids', 'output_fluid', 'fluid_output', 'molten_outputs', 'molten', 'left', 'right', 'output1', 'output2', 'byproduct_type', 'acid_out')
IGNORE = {'type', 'category', 'name', 'icon', 'pattern', 'conditions', 'show_notification', 'group', 'order', 'duration', 'power', 'frequency',
          'experience', 'cookingtime', 'stamp', 'tier', 'laser_strength', 'pools', 'blueprint_pools', 'wrap', 'tool', 'decay', 'divisor', 'named',
          'consumption', 'copy_from', 'custom_localization', 'particle_effect'}
IDRE = re.compile(r'^[a-z0-9_.-]+:[a-z0-9_./-]+$')


def objets(o, acc, n=1):
    """Objets (rl ou '#tag' ou ('forme', f, m)) avec quantité."""
    if isinstance(o, str):
        if o.startswith('#') and ':' in o:
            acc.append((o, n))
        elif IDRE.match(o):
            acc.append((o, n))
    elif isinstance(o, list):
        for v in o:
            objets(v, acc, n)
    elif isinstance(o, dict):
        c = o.get('count', 1) if isinstance(o.get('count', 1), int) else 1
        if 'shape' in o and 'material' in o:
            acc.append((('forme', o['shape'], o['material']), c))
            return
        if 'material' in o and 'amount' in o:
            return
        if 'type' in o and 'amount' in o:
            acc.append((('fluide', o['type'], o['amount']), 1))
            return
        if isinstance(o.get('of'), list) and o['of']:
            objets(o['of'][0], acc, c)
            return
        for k in ('ingredient', 'item', 'id', 'data'):
            if isinstance(o.get(k), str):
                objets(o[k], acc, c)
                return
            if isinstance(o.get(k), (dict, list)):
                objets(o[k], acc, c)
                return
        if isinstance(o.get('items'), list) and o['items']:
            objets(o['items'][0], acc, c)
        elif isinstance(o.get('tag'), str):
            acc.append(('#' + o['tag'], c))


def entrees(d):
    acc = []
    t = d.get('type', '').split(':')[-1]
    if 'pattern' in d and isinstance(d.get('key'), dict):
        compte = collections.Counter(''.join(d['pattern']))
        for k, v in d['key'].items():
            if compte.get(k):
                tmp = []
                objets(v if not isinstance(v, list) else v[0], tmp)
                acc += [(x, n * compte[k]) for x, n in tmp[:1]]
    else:
        for k, v in d.items():
            if k in SORTIES or k in IGNORE:
                continue
            if isinstance(v, (int, float, bool)):
                continue
            if k in ('ingredients', 'input_items', 'inputs', 'toppings', 'pcb', 'solder', 'materials') and isinstance(v, list):
                for e in v:
                    objets(e, acc)
            else:
                objets(v, acc)
    if t == 'tool_conversion':
        acc = []
        objets(d.get('from'), acc)
        for e in d.get('materials', []):
            objets(e, acc)
    tot, ordre = collections.Counter(), []
    for x, n in acc:
        if x not in tot:
            ordre.append(x)
        tot[x] += n
    return [(x, tot[x]) for x in ordre]


def produits(d):
    acc = []
    for k in SORTIES:
        if k in d:
            v = d[k]
            objets(v, acc)
    out = []
    for x, n in acc:
        if isinstance(x, str) and x.startswith('geontm:') and x not in [y for y, _ in out]:
            out.append((x, n))
    return out


OBT, USE = collections.defaultdict(list), collections.defaultdict(list)
for n in sorted(z.namelist()):
    if not (n.startswith('data/geontm/recipes/') and n.endswith('.json')):
        continue
    rid = 'geontm:' + n[len('data/geontm/recipes/'):-5]
    if rid in RM or '.recycle' in rid or 'expensive' in rid or 'lbsm' in rid or 'no528' in rid:
        continue
    d = json.loads(z.read(n))
    t = d.get('type', '').split(':')[-1]
    if t not in MACHINES and t not in FIXES:
        continue
    ent, pro = entrees(d), produits(d)
    if not ent or not pro:
        continue
    for x, c in pro:
        OBT[x[7:]].append((rid, t, d, ent, c))
        for e, _ in ent:
            if isinstance(e, str) and e.startswith('geontm:') and x[7:] in VISIBLES and e != x:
                if x not in USE[e[7:]]:
                    USE[e[7:]].append(x)


def libelle(e, c, l):
    if isinstance(e, tuple) and e[0] == 'forme':
        n = forme(e[1], e[2], l)
    elif isinstance(e, tuple):
        cle = 'geontmfluid.' + e[1].split(':')[-1]  # noms des fluides : clé sans espace de noms
        n = nom(e[1], l) or LANG[l].get(cle) or LANG['en'].get(cle) or e[1].split(':')[-1].replace('_', ' ')
        return '%s mB %s' % (e[2], n)
    elif e.startswith('#'):
        n = tag(e[1:], l)
    else:
        n = lien(e, l) or e.split(':')[-1].replace('_', ' ')
    return '%d x %s' % (c, n) if c > 1 else n


def machine(t, l):
    if t in FIXES:
        return FIXES[t][0 if l == 'fr' else 1]
    return lien('geontm:' + MACHINES[t], l) or MACHINES[t]


def mku(l):
    """Recette de MKUCraftingRecipe : six ingrédients en grille 3x3, disposition tirée de la graine du monde ; indices : LootGenerator.lootBookMeteor."""
    ing = ', '.join(lien('geontm:' + i, l) or i for i in ('powder_iodine', 'powder_fire', 'dust', 'nugget_mercury', 'morning_glory', 'syringe_metal_empty'))
    if l == 'fr':
        return ("Recette secrète à l'établi (grille 3x3) : %s. La disposition est tirée de la graine du monde, donc différente d'un monde à l'autre. "
                "Les piles de butin des donjons de météorite donnent un de ces ingrédients et le livre qui indique son emplacement dans la grille (de 1 à 9)." % ing)
    return ('Secret crafting table recipe (3x3 grid): %s. The layout is drawn from the world seed, so it differs from world to world. '
            'Meteorite dungeon loot piles give one of these ingredients and the book naming its slot in the grid (1 to 9).' % ing)


# Geoventure : les faces d'alimentation que NTM referme exprès (un câble Forge Energy ne s'y branche pas ; partout ailleurs la machine
# prend l'énergie sur chacun de ses blocs). Même règle que le « faces » du bloc : lancement = côtés seulement.
SE_BRANCHE = {x: ('Alimentation : se branche par les côtés (le dessus est fermé, comme dans NTM).',
                  'Power: plug in from the sides (the top is closed, as in NTM).')
              for x in ('launch_pad', 'launch_pad_large', 'launch_table')}
SECRETES = {'syringe_mkunicorn': mku}  # objets sans recette JSON mais fabricables par une recette codée (docs/perimetre/obtention.md)


def page(x, l):
    fr = l == 'fr'
    L = []
    # le lavage d'une variante peinte (ingrédient geontm nommé <x>_<variante>, qui se fabrique avec x) passe après la vraie fabrication
    lavage = lambda r: any(isinstance(e, str) and e.startswith('geontm:' + x + '_') for e, _ in r[3])
    rec = sorted(OBT.get(x, []), key=lambda r: (lavage(r), r[1] in ('storage_drum', 'silex', 'shredder', 'centrifuge'), len(r[3])))[:3]
    if rec:
        L.append('%s :' % ('Obtention' if fr else 'How to get it'))
        for rid, t, d, ent, c in rec:
            s = '- %s%s : %s' % (machine(t, l), ' (x%d)' % c if c > 1 else '', ', '.join(libelle(e, n, l) for e, n in ent[:9]))
            pools = d.get('pools') or d.get('blueprint_pools') or []
            for p in pools:
                if p in SCHEMAS:
                    f = SCHEMAS[p]
                    s += (' - [[gv:schemas|schéma exigé]] : %s' if fr else ' - [[gv:schemas|blueprint required]]: %s') % f[0 if fr else 1]
            L.append(s + '.')
        plus = len(OBT[x]) - len(rec)
        if plus > 0:
            L.append(('(%d autres recettes : voir JEI.)' if fr else '(%d more recipes: see JEI.)') % plus)
    elif x in SECRETES:
        L.append('%s :' % ('Obtention' if fr else 'How to get it'))
        L.append(SECRETES[x](l))
    else:
        L.append('%s :' % ('Obtention' if fr else 'How to get it'))
        L.append('Pas de recette directe : minerai, butin, structure assemblée ou produit d\'une autre machine. JEI (touche R) donne le détail.'
                 if fr else 'No direct recipe: ore, loot, assembled structure or by-product. See JEI (R key).')
    use, vus = [], set()
    for u in USE.get(x, []):
        if lien(u, l) and nom(u, l) not in vus and nom(u, l) != nom('geontm:' + x, l):
            vus.add(nom(u, l))
            use.append(u)
    if use:
        L.append('')
        L.append('%s :' % ('Sert à fabriquer' if fr else 'Used to make'))
        L.append(', '.join(lien(u, l) for u in use[:10]) + ((' et %d autres.' if fr else ' and %d more.') % (len(use) - 10) if len(use) > 10 else '.'))
    if x in SE_BRANCHE:
        L.append('')
        L.append(SE_BRANCHE[x][0 if fr else 1])
    return '<br>'.join(L)


os.makedirs(OUT, exist_ok=True)
for p in glob.glob(OUT + '/*.json'):
    os.remove(p)
for x in SANS:
    o = {'name': 'gv:' + x, 'icon': ['geontm:' + x], 'trigger': [['geontm:' + x]],
         'title': {'en_us': nom('geontm:' + x, 'en'), 'fr_fr': nom('geontm:' + x, 'fr')},
         'content': {'en_us': page(x, 'en'), 'fr_fr': page(x, 'fr')}}
    with open(os.path.join(OUT, x + '.json'), 'w', encoding='utf-8', newline='\n') as f:
        f.write(json.dumps(o, ensure_ascii=False, indent=1) + '\n')
MANQUE = sorted(VISIBLES - set(PAGE))
print(len(SANS), 'pages ;', sum(1 for x in SANS if OBT.get(x)), 'avec recette ;', 'objets visibles sans page :', MANQUE)
if MANQUE:  # un objet visible sans nom exploitable ne doit plus passer en silence
    raise SystemExit(1)
