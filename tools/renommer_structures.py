#!/usr/bin/env python3
"""Remplace l'ancien espace de noms hbm dans les structures NBT. Relancer l'outil ne change rien."""
import gzip
import struct
import zlib
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent


def lire_chaine(d, i):
    n = struct.unpack_from('>H', d, i)[0]
    return d[i + 2:i + 2 + n], i + 2 + n


def chaine(b):
    return struct.pack('>H', len(b)) + b


def renommer(b):
    return b.replace(b'hbm:', b'geontm:')


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


modifies = 0
fichiers = sorted(ROOT.glob('src/**/*.nbt'))
for p in fichiers:
    brut = p.read_bytes()
    compresse = brut[:2] == b'\x1f\x8b'
    # Certains fichiers portent des octets après le bloc gzip : seul le premier bloc compte.
    d = zlib.decompressobj(31).decompress(brut) if compresse else brut
    if b'hbm:' not in d:
        continue
    out = bytearray([d[0]])
    nom, i = lire_chaine(d, 1)
    out += chaine(nom)
    fin = charge(d, i, d[0], out)
    assert fin == len(d), p
    assert b'hbm:' not in out, p
    p.write_bytes(gzip.compress(bytes(out), mtime=0) if compresse else bytes(out))
    modifies += 1
print('structures lues', len(fichiers), 'modifiées', modifies)
