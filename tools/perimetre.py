#!/usr/bin/env python3
"""Classe chaque objet et bloc de NTM-Continuum selon le périmètre voulu pour GeoNTM.

Usage :
    python tools/perimetre.py <hbm-forge.jar> <registre-geomods.txt>

Écrit docs/perimetre/contenu.csv et docs/PERIMETRE.md. Le registre liste les identifiants GeoMods (une ligne « item <id> »).
Décisions : GARDER, SIMPLIFIER, FUSIONNER (remplacé par un objet Geoventure), SI_BESOIN (gardé seulement si une
recette du périmètre l'exige), RETIRER, A_DECIDER.
"""
import collections
import csv
import json
import re
import sys
import zipfile
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent

# --- Ressources ---------------------------------------------------------------------------------------------
SYNONYMES = {'aluminium': 'aluminum'}
VANILLE = {'iron', 'gold', 'copper', 'coal', 'redstone', 'diamond', 'emerald', 'lapis', 'quartz', 'netherite'}
TERRE = {'graphite': 'modérateur des réacteurs', 'beryllium': 'réflecteur à neutrons', 'boron': 'barres de contrôle',
         'zirconium': 'gaines de combustible', 'thorium': 'combustible alternatif'}
# Une ressource unique par planète, absente de la Terre (décision du propriétaire, 20/11). Desh, ostrum et calorite sont
# ceux d'Ad Astra ; la saturnite de NTM est fusionnée dans l'ostrum (ses identifiants restent enregistrés, sans génération).
ESPACE = {'desh': 'Lune (Ad Astra)', 'ostrum': 'Mars (Ad Astra, la saturnite y est fusionnée)', 'calorite': 'Vénus (Ad Astra)',
          'schrabidium': 'Mercure', 'starmetal': 'Glacio'}
NUCLEAIRE = {'u233', 'u235', 'u238', 'pu238', 'pu239', 'pu240', 'pu241', 'pu_mix', 'plutonium', 'th232',
             'uranium_fuel', 'plutonium_fuel', 'thorium_fuel', 'mox_fuel'}
FORMES = {'ingot': '{}_ingot', 'nugget': '{}_nugget', 'plate': '{}_plate', 'powder': '{}_dust', 'wire': '{}_wire',
          'block': '{}_block', 'ore': '{}_ore', 'rod': '{}_rod'}

AD_ASTRA = {'desh': {'ingot': 'desh_ingot', 'nugget': 'desh_nugget', 'plate': 'desh_plate', 'block': 'desh_block'},
            'saturnite': {'ingot': 'ostrum_ingot', 'plate': 'ostrum_plate', 'block': 'ostrum_block'}}
BLOCS_ESPACE = ['ore_glacio_starmetal']

# --- Machines ------------------------------------------------------------------------------------------------
MACHINES_GARDER = {
    'satellites': 'satlink satlinker radar radar_large missile_assembly',
    'réacteurs': 'zirnox radgen rtg_grey',
    'combustible': 'gascent silex fel purex exposure_chamber cyclotron waste_drum storage_drum',
    'vapeur et énergie': 'industrial_boiler steam_engine stirling stirling_steel industrial_turbine '
                         'chungus tower_large tower_small condenser condenser_powered turbofan '
                         'turbine_gas combustion_engine solar_boiler',
    'production': 'assembly_machine assembly_factory chemical_plant chemical_factory arc_furnace arc_welder '
                  'electrolyser crystallizer vacuum_distill catalytic_cracker catalytic_reformer hydrotreater '
                  'coker liquefactor solidifier compressor compressor_compact rotary_furnace crucible '
                  'strand_caster soldering_station',
}
MACHINES_FUSION = {
    'refinery': 'refinery', 'pumpjack': 'oil_pump', 'fracking_tower': 'oil_derrick', 'fluidtank': 'fluid_tank',
    'pump_electric': 'water_pump', 'pump_steam': 'water_pump', 'transformer': 'transformer',
    'transformer_20': 'transformer', 'battery': 'battery', 'lithium_battery': 'battery_t2',
    'electric_furnace_off': 'electric_furnace', 'shredder': 'crusher', 'press': 'metal_press', 'epress': 'metal_press',
    'centrifuge': 'centrifuge', 'mixer': 'industrial_mixer', 'autocrafter': 'auto_assembler', 'diesel': 'generator',
    'bigasstank': 'liquid_depot', 'conveyor_press': 'conveyor_press', 'fraction_tower': 'modular_refinery',
    'rockmill': 'flour_mill', 'blast_furnace': 'mega_foundry',
}
MACHINES_RETIRER = re.compile(r'^(ammo_press|armor_table|weapon_table|annihilator|keyforge|microwave|siren|teleporter|'
                              r'battery_potato|battery_redd|dineutronium_battery|schrabidium_battery|bat9000|'
                              r'stirling_creative|transformer_dnt|tape_drive|orbus|hephaestus|lpw2|ore_slopper|ashpit|autosaw|'
                              r'battery_socket|capacitor_|controller|detector|difurnace|drain|flare|funnel|'
                              r'furnace_brick|furnace_combination|industrial_generator|intake|precass|pyrooven|'
                              r'rtg_furnace|sawmill|supercomputer|thresher|well|wood_burner|reactor$|reactor_small$|'
                              r'boiler_off$|large_turbine$|minirtg$|puf6_tank$|uf6_tank$)')

# --- Règles par nom : la première qui correspond décide ----------------------------------------------------------
RETIRER = [
    (r'^(ammo|gun|weapon|grenade|casing|primer|shell|bolt_action|turret|sandbags|barbed|mine_)(_|$)', 'armes : SBW et GeoWarfare couvrent ce rôle'),
    (r'^(hev|t51|ncrpa|fau|ajr|ajro|bj|dns|dnt|envsuit|paa|rpa|taurun|trenchmaster|steamsuit|dieselsuit|robes|liquidator|pa|cmb|schrabidium_(helmet|plate|legs|boots)|euphemium_(helmet|plate|legs|boots))(_|$)', 'armure ou référence à un autre univers'),
    (r'^(bobblehead|plushie|crayon|canned|snowglobe|record|coin|holotape|achievement|particle|entity|rune|mese|glyphid|chopper|peas|apple|heart|bottle2?|can|loot|journal|book|page|spawn|egg|ball|fatduck|pink|taint|volcano|digamma|balefire|fleija|solinium|boy|man|mike|tsar|gadget|nuke|thermo|prototype|n2|c4|semtex|dynamite|charge|det|detonator|crashed|bomb)(_|$)', 'objet décoratif, parodique ou bombe extrême'),
    (r'^[a-z]+_(sword|pickaxe|axe|shovel|hoe|helmet|chestplate|plate_armor|legs|leggings|boots|hammer|multitool|drill|chainsaw)(_|$)', 'outil ou armure de matière'),
    (r'^(siren|item|crate|custom|lightstone|radio|teisr|shimmer|singularity|dungeon|structure|test|vending|vinyl|creative|bathwater|filing|geysir|volcanic|rebar|pedestal|floodlight|lamp|chimney|jetpack|armor|attachment|flame|mine)(_|$)', 'hors périmètre'),
    (r'^(bedrock|cm|hadron|dfc|ams|crane|pneumatic|conveyor|rail|boxcar|cart|boat|struct|stalactite|stalagmite|platemetal|ducrete|brick|concrete|deco|tile|reinforced|red|ladder|spotlight|lantern|anvil|padlock|security|wand|plant|mush|sand|stone|gravel|dirt|wood|glass|door|sliding|fence|vent|seal|cluster|gneiss|basalt|depth|meteor|meteorite|frozen|fire|foam|asphalt)(_|$)', 'doublon de GeoMods, GeoDecor ou trop de variantes'),
]
THEMES = [
    ('satellites', 'GARDER', r'^(sat|satellite|soyuz|launchpad|launch_pad|deco_sat|pole_satellite|rocket_fuel|missile_soyuz)(_|$)'),
    ('missiles', 'A_DECIDER', r'^(missile|warhead)_.*(nuclear|thermo|mirv|n2|volcano|bhole|schrabidium|taint|doomsday|burst|micro|strong)'),
    ('missiles', 'GARDER', r'^(missile|warhead|launch|designator|radar|fins|thruster)(_|$)'),
    ('missiles', 'SIMPLIFIER', r'^mp(_|$)'),
    ('radioactivité', 'GARDER', r'^(geiger|hazmat|rad|radaway|radx|radsensor|rad_absorber|cap_rad|decon|sellafield|waste|fallout|falloutitem|gas_radon|corium|gem_rad|iv|syringe|med)(_|$)'),
    ('réacteurs', 'SIMPLIFIER', r'^(rbmk|pwr|fusion|watz|icf|pile|nuclear|billet|pellet|cladding)(_|$)'),
    ('drones', 'A_DECIDER', r'^drone(_|$)'),
    ('schémas', 'GARDER', r'^(blueprint|blueprints)(_|$)'),
    ('électronique', 'SIMPLIFIER', r'^(circuit|upgrade|capacitor|coil|motor|mechanism|part|parts)(_|$)'),
]


def geomods_item(mat, forme, registre):
    if forme in AD_ASTRA.get(mat, {}):
        return 'ad_astra:' + AD_ASTRA[mat][forme]
    mat = SYNONYMES.get(mat, mat)
    nom = FORMES[forme].format(mat)
    if nom in registre:
        return 'geomods:' + nom
    if mat in VANILLE and forme in ('ingot', 'nugget', 'block', 'ore'):
        return 'minecraft:' + nom
    return ''


def materiau(ident):
    """(forme, matière) d'un identifiant comme ingot_desh ou ore_uranium_deepslate."""
    forme, _, reste = ident.partition('_')
    if forme not in FORMES or not reste:
        return None
    reste = re.sub(r'^(nether|gneiss|depth|basalt|meteor)_', '', re.sub(r'_(deepslate|scorched)$', '', reste))
    return forme, reste


# Blocs de structure qui forment les rampes de missiles et le lanceur Soyouz : la règle « struct » les retirait à tort,
# ce qui rendait ces lanceurs impossibles à construire en survie (décision du propriétaire, 20/11).
STRUCTURES_DE_LANCEMENT = {'struct_launcher': 'missiles', 'struct_scaffold': 'satellites', 'struct_launcher_core': 'missiles',
                           'struct_launcher_core_large': 'missiles', 'struct_soyuz_core': 'satellites'}


# Machines que la règle « MACHINES_RETIRER » écartait à tort : sans elles, des chaînes gardées ne sont plus faisables en
# survie (assembleur de précision : circuits de contrôle des missiles et satellites ; four de pyrolyse : carbure de tungstène
# et chlore ; superordinateur : calcul de vol ; four combiné : chlore, peroxyde, créosote, donc plastique dur).
# Contrôle : tools/verifier_obtention.py. Leurs recettes sont dans src/generated/data/pack/data/geontm/recipe.
MACHINES_DE_CHAINE = {'machine_furnace_combination', 'machine_precass', 'machine_pyrooven', 'machine_supercomputer'}


def classer(ident, genre, registre):
    """Renvoie (décision, thème, cible, raison)."""
    if ident in MACHINES_DE_CHAINE:
        return 'GARDER', 'machines : production', '', 'nécessaire à une chaîne de production gardée'
    if ident in STRUCTURES_DE_LANCEMENT:
        return 'GARDER', STRUCTURES_DE_LANCEMENT[ident], '', 'forme un lanceur gardé'
    if ident.startswith('machine_obj_'):
        return 'RETIRER', 'hors périmètre', '', 'modèle technique'
    if ident.startswith('machine_'):
        m = ident[8:]
        for groupe, liste in MACHINES_GARDER.items():
            if m in liste.split():
                return 'GARDER', 'machines : ' + groupe, '', ''
        if m in MACHINES_FUSION:
            return 'FUSIONNER', 'machines', 'geomods:' + MACHINES_FUSION[m], 'machine Geoventure équivalente'
        if MACHINES_RETIRER.match(m):
            return 'RETIRER', 'machines', '', 'hors sujet ou trop puissant'
        return 'A_DECIDER', 'machines', '', ''
    mat = materiau(ident)
    if mat:
        forme, m = mat
        cible = geomods_item(m, forme, registre)
        if cible:
            return 'FUSIONNER', 'ressources', cible, 'ressource déjà présente'
        if m in ESPACE:
            return 'GARDER', 'ressources : espace', '', 'uniquement sur ' + ESPACE[m]
        if m in TERRE:
            return 'GARDER', 'ressources : ajouts', '', TERRE[m]
        if m in NUCLEAIRE:
            return 'GARDER', 'ressources : nucléaire', '', 'produit en machine, pas de minerai'
        return 'SI_BESOIN', 'ressources : intermédiaires', '', 'gardé seulement si une recette retenue en dépend'
    for theme, decision, motif in THEMES:
        if re.match(motif, ident):
            return decision, theme, '', ''
    for motif, raison in RETIRER:
        if re.match(motif, ident):
            return 'RETIRER', 'hors périmètre', '', raison
    return 'SI_BESOIN', 'dépendances', '', 'gardé seulement si une recette retenue en dépend'


def main():
    jar, registre_txt = sys.argv[1:3]
    registre = {l.split()[1] for l in Path(registre_txt).read_text(encoding='utf-8').splitlines()
                if l.startswith(('item ', 'block '))}
    z = zipfile.ZipFile(jar)
    noms = z.namelist()
    fr = json.loads(z.read('assets/hbm/lang/fr_fr.json'))
    blocs = sorted(n[23:-5] for n in noms if n.startswith('assets/hbm/blockstates/') and n.endswith('.json'))
    objets = sorted(n[17:-5] for n in noms if n.startswith('assets/hbm/items/') and n.endswith('.json') and n.count('/') == 3)
    lignes = [('bloc', b) for b in blocs] + [('objet', o) for o in objets if o not in set(blocs)]
    fichier_dependances = ROOT / 'docs/perimetre/dependances.txt'
    dependances = set(fichier_dependances.read_text(encoding='utf-8').split()) if fichier_dependances.exists() else set()
    out = []
    for genre, ident in lignes:
        d, t, c, r = classer(ident, genre, registre)
        if ident in dependances:
            d, t, c, r = 'SI_BESOIN', 'dépendances', '', 'nécessaire à une recette gardée'
        nom = fr.get(('block.hbm.' if genre == 'bloc' else 'item.hbm.') + ident, '')
        out.append({'type': genre, 'id': ident, 'nom': nom, 'decision': d, 'theme': t, 'cible': c, 'raison': r})

    ressources = ROOT / 'src/main/resources/geontm/perimetre'
    ressources.mkdir(parents=True, exist_ok=True)
    visibles = sorted({x['id'] for x in out if x['decision'] in ('GARDER', 'SIMPLIFIER')} | set(BLOCS_ESPACE))
    retires = sorted(x['id'] for x in out if x['decision'] == 'RETIRER')
    (ressources / 'creatif.txt').write_text('\n'.join(visibles) + '\n', encoding='utf-8', newline='\n')
    (ressources / 'retire.txt').write_text('\n'.join(retires) + '\n', encoding='utf-8', newline='\n')

    dossier = ROOT / 'docs/perimetre'
    dossier.mkdir(parents=True, exist_ok=True)
    with (dossier / 'contenu.csv').open('w', encoding='utf-8', newline='') as f:
        w = csv.DictWriter(f, fieldnames=list(out[0]), lineterminator='\n')
        w.writeheader()
        w.writerows(out)

    par_dec = collections.Counter(x['decision'] for x in out)
    par_theme = collections.Counter((x['theme'], x['decision']) for x in out if x['decision'] != 'RETIRER')
    t = ['# Périmètre de GeoNTM', '',
         '> Généré par `tools/perimetre.py`. **Proposition à valider** : aucun contenu n\'est encore retiré du mod.', '',
         f'NTM-Continuum 1.0.6 contient **{len(blocs)} blocs** et **{len(lignes) - len(blocs)} objets**. '
         'Le détail ligne par ligne est dans [contenu.csv](perimetre/contenu.csv).', '',
         '## Bilan', '', '| Décision | Nombre | Sens |', '|---|---:|---|']
    sens = {'GARDER': 'repris dans GeoNTM', 'SIMPLIFIER': 'repris, mais regroupé ou réduit',
            'FUSIONNER': 'remplacé par un objet Geoventure',
            'SI_BESOIN': 'gardé seulement si une recette retenue en dépend', 'RETIRER': 'supprimé',
            'A_DECIDER': 'arbitrage du propriétaire'}
    t += [f'| {k} | {par_dec[k]} | {sens[k]} |' for k in sens]
    t += ['', '## Ce qui reste, par thème', '', '| Thème | Décision | Nombre |', '|---|---|---:|']
    t += [f'| {a} | {b} | {n} |' for (a, b), n in sorted(par_theme.items())]
    t += ['', '## Ressources proposées', '',
          'Sur Terre, seules les ressources Geoventure existent, plus ces ajouts :', '']
    t += [f'- **{k}** : {v}' for k, v in TERRE.items()]
    t += ['', 'Ressources rares, sans minerai sur Terre : il faut une fusée pour les récupérer.', '']
    t += [f'- **{k}** : {v}' for k, v in ESPACE.items()]
    t += ['', '## Machines gardées', '']
    t += [f'- **{g}** : ' + ', '.join(f'`{m}`' for m in l.split()) for g, l in MACHINES_GARDER.items()]
    t += ['', '## Machines remplacées par les nôtres', '', '| NTM | Geoventure |', '|---|---|']
    t += [f'| `{a}` | `{b}` |' for a, b in MACHINES_FUSION.items()]
    (ROOT / 'docs/PERIMETRE.md').write_text('\n'.join(t) + '\n', encoding='utf-8', newline='\n')
    print(dict(par_dec))
    print({f'{a}/{b}': n for (a, b), n in sorted(par_theme.items())})
    print('machines à décider', [x['id'][8:] for x in out if x['theme'] == 'machines' and x['decision'] == 'A_DECIDER'])


if __name__ == '__main__':
    main()
