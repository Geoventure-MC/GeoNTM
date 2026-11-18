#!/usr/bin/env python3
"""Garde uniquement la cible Forge 1.20.1 (à lancer après « Set active project to 1.20.1-forge »)."""
import re
import shutil
from pathlib import Path

NODE = '1.20.1-forge'
root = Path.cwd()
settings = root / 'settings.gradle.kts'
s = settings.read_text(encoding='utf-8').replace('\r\n', '\n')
s, n = re.subn(r'( *)match\("26\.1", "fabric", "neoforge"\)\n *match\("1\.21\.1", "fabric", "neoforge"\)\n *match\("1\.20\.1", "fabric", "forge"\)',
               r'\1match("1.20.1", "forge")', s)
assert n == 1
s, n = re.subn(r'vcsVersion = "[^"]+"', f'vcsVersion = "{NODE}"', s)
assert n == 1
s = re.sub(r'listOf\(\s*"26\.1-fabric", "26\.1-neoforge",\s*"1\.21\.1-fabric", "1\.21\.1-neoforge",\s*"1\.20\.1-fabric", "1\.20\.1-forge",\s*\)',
           f'listOf("{NODE}")', s)
settings.write_text(s, encoding='utf-8', newline='\n')
active = (root / 'stonecutter.gradle.kts').read_text(encoding='utf-8')
assert f'stonecutter active "{NODE}"' in active, 'le projet actif n a pas changé'
for d in (root / 'versions').iterdir():
    if d.is_dir() and d.name != NODE:
        shutil.rmtree(d)
for name in ('build.fabric.gradle.kts', 'build.neoforge.gradle.kts', 'Jenkinsfile'):
    (root / name).unlink(missing_ok=True)

generated = root / 'build/generated/data'
if generated.is_dir():
    target = root / 'src/generated/data'
    shutil.rmtree(target, ignore_errors=True)
    shutil.copytree(generated, target)
    props = root / 'gradle.properties'
    text = props.read_text(encoding='utf-8').replace('\r\n', '\n')
    text += ('\n# Tables de recettes figées : générées une fois depuis src/recipes, puis conservées ici.\n'
             'geocore.build.generatedData.generator=:1.20.1-forge:compileJava\n'
             'geocore.build.generatedData.root=src/generated/data\n')
    props.write_text(text, encoding='utf-8', newline='\n')
print('cible unique :', NODE)
