#!/usr/bin/env python3
"""Importe les structures de planètes de NTM: Space (GPL-3.0, JamesH2, MellowArpeggiation et l'équipe NTM: Space) dans
GeoNTM : assets/ntmspace/structures/** -> src/main/resources/assets/geontm/structures/space/**.

- « hbm: » devient « geontm: » (comme renommer_structures.py) : la palette legacy de GeoNTM lit ces noms.
- Les six blocs propres à NTM: Space, absents de GeoNTM, deviennent un décor proche (voir ESPACE ci-dessous).

Usage : python tools/importer_structures_espace.py <dossier assets/ntmspace/structures du dépôt NTM: Space>
Le dossier source est une copie de travail (données non fiables) : ce script ne fait que lire ses .nbt.
"""
import gzip
import sys
import zlib
from pathlib import Path

import struct

ROOT = Path(__file__).resolve().parent.parent
DEST = ROOT / 'src/main/resources/assets/geontm/structures/space'

# Blocs de NTM: Space sans équivalent dans GeoNTM -> décor GeoNTM le plus proche (noms legacy, lus par la palette).
ESPACE = {
    b'ntmspace:deco_stainless': b'geontm:tile.deco_steel',
    b'ntmspace:machine_radiator': b'geontm:tile.steel_grate',
    b'ntmspace:machine_solar': b'minecraft:daylight_detector',
    b'ntmspace:machine_stardar': b'geontm:tile.deco_steel',
    b'ntmspace:air_vent': b'geontm:tile.steel_grate',
    b'ntmspace:plushie': b'geontm:plushie',
    # objets dans les coffres : équipement d'oxygène d'Ad Astra (la respiration dans l'espace est celle d'Ad Astra)
    b'ntmspace:oxy_plss': b'ad_astra:oxygen_gear',
    b'ntmspace:oxy_pinwheel': b'ad_astra:gas_tank',
}


# Lecture/écriture NBT reprises de renommer_structures.py (qui s'exécute à l'import : on ne l'importe pas).
def lire_chaine(d, i):
    n = struct.unpack_from('>H', d, i)[0]
    return d[i + 2:i + 2 + n], i + 2 + n


def chaine(b):
    return struct.pack('>H', len(b)) + b


def charge(d, i, t, out):
    fixes = {1: 1, 2: 2, 3: 4, 4: 8, 5: 4, 6: 8}
    if t in fixes:
        out += d[i:i + fixes[t]]
        return i + fixes[t]
    if t in (7, 11, 12):
        n = struct.unpack_from('>i', d, i)[0]
        taille = n * {7: 1, 11: 4, 12: 8}[t]
        out += d[i:i + 4 + taille]
        return i + 4 + taille
    if t == 8:
        s, i = lire_chaine(d, i)
        out += chaine(renommer(s))
        return i
    if t == 9:
        sous = d[i]
        n = struct.unpack_from('>i', d, i + 1)[0]
        out += d[i:i + 5]
        i += 5
        for _ in range(n):
            i = charge(d, i, sous, out)
        return i
    if t == 10:
        while True:
            sous = d[i]
            out.append(sous)
            i += 1
            if sous == 0:
                return i
            nom, i = lire_chaine(d, i)
            out += chaine(nom)
            i = charge(d, i, sous, out)
    raise ValueError(f'type NBT inconnu {t}')


def renommer(b):
    if b in ESPACE:
        return ESPACE[b]
    return b.replace(b'hbm:', b'geontm:')


def convertir(src: Path, dst: Path):
    brut = src.read_bytes()
    compresse = brut[:2] == b'\x1f\x8b'
    d = zlib.decompressobj(31).decompress(brut) if compresse else brut
    out = bytearray([d[0]])
    nom, i = lire_chaine(d, 1)
    out += chaine(nom)
    fin = charge(d, i, d[0], out)
    assert fin == len(d), src
    assert b'hbm:' not in out and b'ntmspace:' not in out, src
    dst.parent.mkdir(parents=True, exist_ok=True)
    dst.write_bytes(gzip.compress(bytes(out), mtime=0))


if __name__ == '__main__':
    source = Path(sys.argv[1])
    n = 0
    for p in sorted(source.rglob('*.nbt')):
        convertir(p, DEST / p.relative_to(source))
        n += 1
    print('structures importées :', n, '->', DEST)
